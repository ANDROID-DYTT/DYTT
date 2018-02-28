
package com.bzh.dytt;

import android.app.Application;

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 */
public class TestApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
