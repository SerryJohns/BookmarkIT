package com.andela.bookmarkit.services.threads;

import android.os.Process;
import android.util.Log;

public class HelloRunnable implements Runnable {
    public static final String TAG = HelloRunnable.class.getSimpleName();

    @Override
    public void run() {
        // Moves the current Thread into the background
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        // Code to run on the thread
        Log.d(TAG, "run: Hello Runnable running");
    }
}
