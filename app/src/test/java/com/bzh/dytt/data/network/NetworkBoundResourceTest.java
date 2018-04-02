package com.bzh.dytt.data.network;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bzh.dytt.AppExecutors;
import com.bzh.dytt.util.ApiUtil;
import com.bzh.dytt.util.CountingAppExecutors;
import com.bzh.dytt.util.InstantAppExecutors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * 有关NetworkBoundResource的设计意图可以参考： https://developer.android.com/topic/libraries/architecture/guide.html#addendum
 * Test Case :
 * 1. db success without network
 * 2. db success with network success
 * 3. db success with network failure
 * 4. pure network success
 * 5. pure network failure
 */
@RunWith(Parameterized.class)
public class NetworkBoundResourceTest {

    private final boolean useRealExecutors;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private Function<Foo, Void> saveCallResult;

    private Function<Foo, Boolean> shouldFetch;

    private AtomicBoolean fetchedOnce = new AtomicBoolean(false);

    private MutableLiveData<Foo> dbData = new MutableLiveData<>();

    private Function<Void, LiveData<ApiResponse<Foo>>> createCall;

    private CountingAppExecutors countingAppExecutors;

    private NetworkBoundResource<Foo, Foo> networkBoundResource;

    public NetworkBoundResourceTest(boolean useRealExecutors) {
        this.useRealExecutors = useRealExecutors;
        if (useRealExecutors) {
            countingAppExecutors = new CountingAppExecutors();
        }
    }

    @Parameterized.Parameters
    public static List<Boolean> param() {
        return Arrays.asList(true, false);
    }

    @Before
    public void init() throws Exception {
        AppExecutors appExecutors = useRealExecutors ? countingAppExecutors.getAppExecutors() : new InstantAppExecutors();
        networkBoundResource = new NetworkBoundResource<Foo, Foo>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Foo item) {
                saveCallResult.apply(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Foo data) {
                return shouldFetch.apply(data) && fetchedOnce.compareAndSet(false, true);
            }

            @NonNull
            @Override
            protected LiveData<Foo> loadFromDb() {
                return dbData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Foo>> createCall() {
                return createCall.apply(null);
            }
        };
    }

    private void drain() {
        if (!useRealExecutors) {
            return;
        }
        try {
            countingAppExecutors.drainTasks(1, TimeUnit.SECONDS);
        } catch (Throwable t) {
            throw new AssertionError(t);
        }
    }

    @Test
    public void pureNetworkSuccess() {
        shouldFetch = new Function<Foo, Boolean>() {
            @Override
            public Boolean apply(Foo foo) {
                return Objects.isNull(foo);
            }
        };

        final Foo networkValue = new Foo(1);
        createCall = new Function<Void, LiveData<ApiResponse<Foo>>>() {
            @Override
            public LiveData<ApiResponse<Foo>> apply(Void aVoid) {
                return ApiUtil.createCall(Response.<Foo>success(networkValue));
            }
        };

        final AtomicReference<Foo> saved = new AtomicReference<>();
        final Foo fetchedDbValue = new Foo(1);
        saveCallResult = new Function<Foo, Void>() {
            @Override
            public Void apply(Foo foo) {
                saved.set(foo);
                dbData.setValue(fetchedDbValue);
                return null;
            }
        };

        Observer<Resource<Foo>> observer = Mockito.mock(Observer.class);
        networkBoundResource.getAsLiveData().observeForever(observer);
        drain();
        verify(observer).onChanged(Resource.<Foo>loading(null));

        dbData.setValue(null);
        drain();
        assertThat(saved.get(), is(networkValue));
        verify(observer).onChanged(Resource.success(fetchedDbValue));
    }

    @Test
    public void pureNetworkFailure() {

        shouldFetch = new Function<Foo, Boolean>() {
            @Override
            public Boolean apply(Foo foo) {
                return Objects.isNull(foo);
            }
        };

        createCall = new Function<Void, LiveData<ApiResponse<Foo>>>() {
            @Override
            public LiveData<ApiResponse<Foo>> apply(Void aVoid) {
                return ApiUtil.createCall(Response.<Foo>error(500, ResponseBody.create(MediaType.parse("text/html"), "error")));
            }
        };

        final AtomicBoolean saved = new AtomicBoolean();
        saveCallResult = new Function<Foo, Void>() {
            @Override
            public Void apply(Foo foo) {
                saved.set(true);
                return null;
            }
        };

        // 初始化
        Observer<Resource<Foo>> observer = Mockito.mock(Observer.class);
        networkBoundResource.getAsLiveData().observeForever(observer);
        drain();
        verify(observer).onChanged(Resource.<Foo>loading(null));

        dbData.setValue(null);
        drain();
        assertThat(saved.get(), is(false));
        verify(observer).onChanged(Resource.<Foo>error("error", null));
    }

    @Test
    public void dbSuccessWithoutNetwork() {
        final AtomicBoolean saved = new AtomicBoolean(false);

        shouldFetch = new Function<Foo, Boolean>() {
            @Override
            public Boolean apply(Foo foo) {
                return false;
            }
        };

        saveCallResult = new Function<Foo, Void>() {
            @Override
            public Void apply(Foo foo) {
                saved.set(true);
                return null;
            }
        };

        Observer<Resource<Foo>> observer = Mockito.mock(Observer.class);

        networkBoundResource.getAsLiveData().observeForever(observer);
        drain();
        verify(observer).onChanged(Resource.<Foo>loading(null));

        Foo dbFoo = new Foo(1);
        dbData.setValue(dbFoo);
        drain();
        verify(observer).onChanged(Resource.success(dbFoo));
        assertThat(saved.get(), is(false));

        Foo dbFoo2 = new Foo(2);
        dbData.setValue(dbFoo2);
        drain();
        verify(observer).onChanged(Resource.success(dbFoo2));

        verifyNoMoreInteractions(observer);
    }

    @Test
    public void dbSuccessWithNetworkSuccess() {
        final Foo dbValue = new Foo(1);
        final Foo dbValue2 = new Foo(2);
        final AtomicReference<Foo> saved = new AtomicReference<>();

        // 是否抓取数据
        shouldFetch = new Function<Foo, Boolean>() {
            @Override
            public Boolean apply(Foo foo) {
                return foo == dbValue;
            }
        };

        // 网络数据
        final MutableLiveData<ApiResponse<Foo>> apiResponseLiveData = new MutableLiveData();
        createCall = new Function<Void, LiveData<ApiResponse<Foo>>>() {
            @Override
            public LiveData<ApiResponse<Foo>> apply(Void aVoid) {
                return apiResponseLiveData;
            }
        };

        // 存储网络数据
        saveCallResult = new Function<Foo, Void>() {
            @Override
            public Void apply(Foo foo) {
                saved.set(foo);
                dbData.setValue(dbValue2);
                return null;
            }
        };

        // 初始化
        Observer<Resource<Foo>> observer = Mockito.mock(Observer.class);
        networkBoundResource.getAsLiveData().observeForever(observer);
        drain();
        verify(observer).onChanged(Resource.<Foo>loading(null));

        // 验证数据库观察结果
        dbData.setValue(dbValue);
        drain();
        verify(observer).onChanged(Resource.<Foo>loading(dbValue));

        // 验证网络请求成功
        final Foo networkResult = new Foo(1);
        apiResponseLiveData.setValue(new ApiResponse<Foo>(Response.success(networkResult)));
        drain();
        assertThat(saved.get(), is(networkResult));
        verify(observer).onChanged(Resource.<Foo>success(dbValue2));

        // 验证是否还有未验证的行为
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void dbSuccessWithNetworkFailure() {
        final Foo dbValue = new Foo(1);
        final AtomicBoolean saved = new AtomicBoolean();

        shouldFetch = new Function<Foo, Boolean>() {
            @Override
            public Boolean apply(Foo foo) {
                return dbValue == foo;
            }
        };

        final MutableLiveData<ApiResponse<Foo>> apiResponseMutableLiveData = new MutableLiveData<>();
        createCall = new Function<Void, LiveData<ApiResponse<Foo>>>() {
            @Override
            public LiveData<ApiResponse<Foo>> apply(Void aVoid) {
                return apiResponseMutableLiveData;
            }
        };

        saveCallResult = new Function<Foo, Void>() {
            @Override
            public Void apply(Foo foo) {
                saved.set(true);
                return null;
            }
        };

        // 初始化
        Observer<Resource<Foo>> observer = Mockito.mock(Observer.class);
        networkBoundResource.getAsLiveData().observeForever(observer);
        drain();
        verify(observer).onChanged(Resource.<Foo>loading(null));

        // 验证数据库观察结果
        dbData.setValue(dbValue);
        drain();
        verify(observer).onChanged(Resource.loading(dbValue));

        // 验证网络请求失败
        apiResponseMutableLiveData.setValue(new ApiResponse<Foo>(Response.<Foo>error(400, ResponseBody.create(MediaType.parse("text/html"), "error"))));
        drain();
        assertThat(saved.get(), is(false));
        verify(observer).onChanged(Resource.error("error", dbValue));

        // 再次验证
        final Foo dbValue2 = new Foo(2);
        dbData.setValue(dbValue2);
        drain();
        verify(observer).onChanged(Resource.error("error", dbValue2));

        //
        verifyNoMoreInteractions(observer);
    }

    static class Foo {

        int value;

        Foo(int value) {
            this.value = value;
        }
    }
}