package com.andela.bookmarkit.services.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HelloStateReceiver extends BroadcastReceiver {
    public static final String TAG = HelloStateReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(Constants.EXTENDED_DATA_STATUS, 0);
        String url = intent.getStringExtra("download_url");
        Log.d(TAG, "onReceive: BroadcastReceiver Received~");
        Log.d(TAG, String.format("onReceive: Received from service: status %d url: %s", status, url));
    }
}
