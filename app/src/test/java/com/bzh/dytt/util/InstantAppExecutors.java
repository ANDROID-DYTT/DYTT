package com.bzh.dytt.util;

import android.support.annotation.NonNull;

import com.bzh.dytt.AppExecutors;

import java.util.concurrent.Executor;

public class InstantAppExecutors extends AppExecutors {

    private static Executor instant = new Executor() {
        @Override
        public void execute(@NonNull Runnable command) {
            command.run();
        }
    };

    public InstantAppExecutors() {
        super(instant, instant, instant);
    }
}
