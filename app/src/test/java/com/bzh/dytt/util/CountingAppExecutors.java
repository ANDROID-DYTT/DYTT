/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bzh.dytt.util;

import android.support.annotation.NonNull;

import com.bzh.dytt.AppExecutors;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CountingAppExecutors {

    private final Object LOCK = new Object();

    private int taskCount = 0;

    private AppExecutors appExecutors;

    public CountingAppExecutors() {
        Runnable increment = new Runnable() {
            @Override
            public void run() {
                synchronized (LOCK) {
                    taskCount--;
                    if (taskCount == 0) {
                        LOCK.notifyAll();
                    }
                }
            }
        };
        Runnable decrement = new Runnable() {
            @Override
            public void run() {
                synchronized (LOCK) {
                    taskCount++;
                }
            }
        };
        appExecutors = new AppExecutors(
                new CountingExecutor(increment, decrement),
                new CountingExecutor(increment, decrement),
                new CountingExecutor(increment, decrement));
    }

    public AppExecutors getAppExecutors() {
        return appExecutors;
    }

    public void drainTasks(int time, TimeUnit timeUnit) throws InterruptedException, TimeoutException {
        long end = System.currentTimeMillis() + timeUnit.toMillis(time);
        while (true) {
            synchronized (LOCK) {
                if (taskCount == 0) {
                    return;
                }
                long now = System.currentTimeMillis();
                long remaining = end - now;
                if (remaining > 0) {
                    LOCK.wait(remaining);
                } else {
                    throw new TimeoutException("could not drain tasks");
                }
            }
        }
    }

    private static class CountingExecutor implements Executor {

        private final Executor delegate = Executors.newSingleThreadExecutor();

        private final Runnable increment;

        private final Runnable decrement;

        public CountingExecutor(Runnable increment, Runnable decrement) {
            this.increment = increment;
            this.decrement = decrement;
        }

        @Override
        public void execute(@NonNull final Runnable command) {
            increment.run();
            delegate.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        command.run();
                    } finally {
                        decrement.run();
                    }
                }
            });
        }
    }
}
