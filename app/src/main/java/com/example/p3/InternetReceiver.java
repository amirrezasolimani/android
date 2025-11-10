package com.example.p3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InternetReceiver extends BroadcastReceiver {

    public interface InternetStatusListener {
        void onStatusChanged(String status);
    }

    private final InternetStatusListener listener;

    public InternetReceiver(InternetStatusListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = MainActivity.getInternetStatus(context);
        listener.onStatusChanged(status);
    }
}

