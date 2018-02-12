package com.bzh.dytt.test;

import android.arch.lifecycle.LiveData;

import javax.inject.Singleton;

@Singleton
        // informs Dagger that this class should be constructed once
class UserRepository {
    Webservice webservice;
    UserDao userDao;

    public LiveData<Resource<User>> loadUser(final String userId) {
//        return new NetworkBoundResource<User, User>() {
//            @Override
//            protected void saveCallResult(@NonNull User item) {
//                userDao.insert(item);
//            }
//
//            @Override
//            protected boolean shouldFetch(@Nullable User data) {
//                return rateLimiter.canFetch(userId) && (data == null || !isFresh(data));
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<User> loadFromDb() {
//                return userDao.load(userId);
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<ApiResponse<User>> createCall() {
//                return webservice.getUser(userId);
//            }
//        }.getAsLiveData();
        return null;
    }
}