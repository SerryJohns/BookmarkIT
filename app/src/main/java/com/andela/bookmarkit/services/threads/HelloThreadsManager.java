package com.andela.bookmarkit.services.threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.andela.bookmarkit.services.threads.PhotoTask.PhotoDecodeRunnable.START_DOWNLOAD_COMPLETE;
import static com.andela.bookmarkit.services.threads.PhotoTask.PhotoSetRunnable.START_DOWNLOAD;

public class HelloThreadsManager {
    public static final String TAG = HelloThreadsManager.class.getSimpleName();
    public static final int TASK_COMPLETE = 202;
    private static HelloThreadsManager sInstance;

    public static HelloThreadsManager getInstance() {
        // Creates a single instance of PhotoManager
        if (sInstance != null) {
            return sInstance;
        } else {
            return sInstance = new HelloThreadsManager();
        }
    }

    private static Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // handle communication to the UI thread

            PhotoTask photoTask = (PhotoTask) msg.obj;

            switch (msg.what) {
                case TASK_COMPLETE:
                    Log.d(TAG, "handleMessage: Message sent from handler thread");
                    cancelAll();
                    break;
                default:
                    // Pass along other messages from the UI
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private static final int KEEP_ALIVE_TIME = 1;

    public static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    // Instantiates the queue of runnables as a LinkedBlockingQueue
    public static final BlockingQueue<Runnable> decodeWorkQueue = new LinkedBlockingQueue<Runnable>();

    // Thread pool manager
    ThreadPoolExecutor decodeThreadPool;

    private HelloThreadsManager() {

        // Creates a thread pool manager
        decodeThreadPool = new ThreadPoolExecutor(
                NUMBER_OF_CORES, // Initial pool size
                NUMBER_OF_CORES, // Max pool size
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                decodeWorkQueue);
    }

    public void handleState(PhotoTask photoTask, int state) {
        switch (state) {
            case START_DOWNLOAD_COMPLETE:
                Log.d(TAG, "handleState: Starting download complete thread");
                decodeThreadPool.execute(photoTask.getPhotoDecodeRunnable());
                break;
            case START_DOWNLOAD:
                Log.d(TAG, "handleState: Starting photoSetRunnable thread");
                decodeThreadPool.execute(photoTask.getPhotoSetRunnable());
                break;
            case TASK_COMPLETE:
                Log.d(TAG, "handleState: Starting taskComplete task");
                Message completeMessage = handler.obtainMessage(state, photoTask);
                completeMessage.sendToTarget();
                break;
        }
    }

    public static void cancelAll() {
        Runnable[] runnableArray = new Runnable[decodeWorkQueue.size()];

        int len = runnableArray.length;

        synchronized (sInstance) {
            for(int runnableIndex = 0; runnableIndex < len; runnableIndex++) {
                // Get the current thread
                Runnable runnable = runnableArray[runnableIndex];
                Thread thread = null;
                if (runnable instanceof PhotoTask.PhotoDecodeRunnable) {
                    thread = ((PhotoTask.PhotoDecodeRunnable) runnable).getImageDecodeThread();
                }

                if (runnable instanceof PhotoTask.PhotoSetRunnable) {
                    thread = ((PhotoTask.PhotoSetRunnable) runnable).getImageSetThread();
                }

                // If the thread exists, post an interrupt to it
                if (null != thread) {
                    thread.interrupt();
                }
            }
        }

    }

}
