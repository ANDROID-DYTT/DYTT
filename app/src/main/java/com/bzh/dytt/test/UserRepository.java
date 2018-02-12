package com.bzh.dytt.test;

import android.arch.lifecycle.LiveData;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;

@Singleton  // informs Dagger that this class should be constructed once
public class UserRepository {
    private final Webservice webservice;
    private final UserDao userDao;
    private final Executor executor;

    @Inject
    public UserRepository(Webservice webservice, UserDao userDao, Executor executor) {
        this.webservice = webservice;
        this.userDao = userDao;
        this.executor = executor;
    }

    public LiveData<User> getUser(String userId) {
        refreshUser(userId);
        // return a LiveData directly from the database.
        return userDao.load(userId);
    }

    private void refreshUser(final String userId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // running in a background thread
                // check if user was fetched recently
//            boolean userExists = userDao.hasUser(FRESH_TIMEOUT);
                boolean userExists = false;
                if (!userExists) {
                    // refresh the data
                    Response response = null;
                    try {
                        response = webservice.getUser(userId).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // TODO check for error etc.
                    // Update the database.The LiveData will automatically refresh so
                    // we don't need to do anything else here besides updating the database
                    userDao.save((User) response.body());
                }
            }
        });
    }
}
