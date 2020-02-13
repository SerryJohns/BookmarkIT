package com.andela.bookmarkit.services.threads;

import android.util.Log;

import static com.andela.bookmarkit.services.threads.PhotoTask.PhotoDecodeRunnable.DOWNLOAD_COMPLETE;
import static com.andela.bookmarkit.services.threads.PhotoTask.PhotoDecodeRunnable.START_DOWNLOAD_COMPLETE;

public class PhotoTask {
    public static final String TAG = PhotoTask.class.getSimpleName();

    public PhotoTask() {
    }

    public Runnable getPhotoDecodeRunnable() {
        return new PhotoDecodeRunnable();
    }

    public Runnable getPhotoSetRunnable() {
        return new PhotoSetRunnable();
    }

    // Gets a handle to the object that creates the thread pools
    private HelloThreadsManager threadsManager = HelloThreadsManager.getInstance();

    public void handleDecodeState(int state) {
        int outState = state;
        switch (state) {
            case DOWNLOAD_COMPLETE:
                outState = HelloThreadsManager.TASK_COMPLETE;
                break;
        }
        // Call generalized state method
        handleState(outState);
    }

    void handleState(int state) {
        threadsManager.handleState(this, state);
    }

    public class PhotoDecodeRunnable implements Runnable {
        public static final int START_DOWNLOAD_COMPLETE = 200;
        public static final int DOWNLOAD_COMPLETE = 203;

        private Thread imageDecodeThread;

        @Override
        public void run() {
            setImageDecodeThread(Thread.currentThread());
            if (Thread.interrupted()) {
                return;
            }

            Log.d(TAG, "run: PhotoDecodeRunnable task running: photos");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handleDecodeState(DOWNLOAD_COMPLETE);
        }

        public void setImageDecodeThread(Thread imageDecodeThread) {
            this.imageDecodeThread = imageDecodeThread;
        }

        public Thread getImageDecodeThread() {
            return imageDecodeThread;
        }
    }

    public class PhotoSetRunnable implements Runnable {
        public static final int START_DOWNLOAD = 201;
        private Thread imageSetThread;

        @Override
        public void run() {
            setImageSetThread(Thread.currentThread());
            if (Thread.interrupted()) {
                return;
            }

            Log.d(TAG, "run: PhotoSetRunnable task running: setting");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handleDecodeState(START_DOWNLOAD_COMPLETE);
        }

        public Thread getImageSetThread() {
            return imageSetThread;
        }

        public void setImageSetThread(Thread imageSetThread) {
            this.imageSetThread = imageSetThread;
        }
    }
}
