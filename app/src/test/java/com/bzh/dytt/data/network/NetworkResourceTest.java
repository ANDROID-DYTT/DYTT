package com.bzh.dytt.data.network;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

import com.bzh.dytt.AppExecutors;
import com.bzh.dytt.util.InstantAppExecutors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@RunWith(JUnit4.class)
public class NetworkResourceTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private NetworkResource<List<Foo>, Foo> networkResource;

    private Function<Foo, List<Foo>> saveCallResult;

    private Function<Void, LiveData<ApiResponse<Foo>>> createCall;
    private AppExecutors appExecutors;

    @Before
    public void init() throws Exception {
        appExecutors = new InstantAppExecutors();

    }

    @Test
    public void networkSuccess() {

        Foo foo = new Foo(1);
        final MutableLiveData<ApiResponse<Foo>> liveData = new MutableLiveData<>();
        ApiResponse<Foo> apiResponse = new ApiResponse<>(Response.success(foo));
        createCall = new Function<Void, LiveData<ApiResponse<Foo>>>() {
            @Override
            public LiveData<ApiResponse<Foo>> apply(Void aVoid) {
                return liveData;
            }
        };

        final ArrayList<Foo> list = new ArrayList<>();
        list.add(foo);
        saveCallResult = new Function<Foo, List<Foo>>() {
            @Override
            public List<Foo> apply(Foo foo) {
                return list;
            }
        };

        networkResource = new NetworkResource<List<Foo>, Foo>(appExecutors) {

            @Override
            protected List<Foo> saveCallResult(@NonNull Foo item) {
                return saveCallResult.apply(item);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Foo>> createCall() {
                return createCall.apply(null);
            }
        };

        Observer<Resource<List<Foo>>> observer = mock(Observer.class);
        networkResource.getAsLiveData().observeForever(observer);
        verify(observer).onChanged(Resource.<List<Foo>>loading(null));

        liveData.setValue(apiResponse);
        verify(observer).onChanged(Resource.<List<Foo>>success(list));
    }

    @Test
    public void networkFailure() {

        final MutableLiveData<ApiResponse<Foo>> liveData = new MutableLiveData<>();
        ApiResponse<Foo> apiResponse = new ApiResponse<>(Response.<Foo>error(400, ResponseBody.create(MediaType.parse("html/txt"), "Error Message")));
        createCall = new Function<Void, LiveData<ApiResponse<Foo>>>() {
            @Override
            public LiveData<ApiResponse<Foo>> apply(Void aVoid) {
                return liveData;
            }
        };

        networkResource = new NetworkResource<List<Foo>, Foo>(appExecutors) {

            @Override
            protected List<Foo> saveCallResult(@NonNull Foo item) {
                return null;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Foo>> createCall() {
                return createCall.apply(null);
            }
        };

        Observer<Resource<List<Foo>>> observer = mock(Observer.class);
        networkResource.getAsLiveData().observeForever(observer);
        verify(observer).onChanged(Resource.<List<Foo>>loading(null));

        liveData.setValue(apiResponse);
        verify(observer).onChanged(Resource.<List<Foo>>error("Error Message", null));
    }

    static class Foo {

        int value;

        Foo(int value) {
            this.value = value;
        }
    }
}