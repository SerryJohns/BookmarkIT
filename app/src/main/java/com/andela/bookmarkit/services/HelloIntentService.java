package com.andela.bookmarkit.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.andela.bookmarkit.services.receivers.Constants;

public class HelloIntentService extends IntentService {
    public static final String TAG = HelloIntentService.class.getSimpleName();
    private int startId;

    public HelloIntentService() {
        super("HelloIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            // Do some work here
            Log.d(TAG, "Intent Service running");
            String url = intent.getStringExtra("download_url");
            Log.d(TAG, "onHandleIntent: Data Received: " + url);
            Thread.sleep(2000);

            int status = Constants.COMPLETE;
            Intent localIntent = new Intent(Constants.BROADCAST_ACTION)
                    .putExtra(Constants.EXTENDED_DATA_STATUS, status)
                    .putExtra("download_url", "https://updated_download_url");

            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

            // Service must stop itself
            stopSelf(startId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Starting", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStartCommand: Service starting");
        this.startId = startId;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // if the service allows binding, implement this. calling super is optional
        return super.onBind(intent);
    }
}
