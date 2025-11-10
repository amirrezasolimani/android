package com.example.p3;

import android.os.Bundle;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private CheckBox checkBox;
    private Button btnCheckStatus;
    private TextView tvStatus;
    private InternetReceiver internetReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBox = findViewById(R.id.checkBox);
        btnCheckStatus = findViewById(R.id.btnCheckStatus);
        tvStatus = findViewById(R.id.tvStatus);

        internetReceiver = new InternetReceiver(new InternetReceiver.InternetStatusListener() {
            @Override
            public void onStatusChanged(String status) {
                if (!checkBox.isChecked()) {
                    tvStatus.setText(status);
                }
            }
        });

        btnCheckStatus.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                String status = getInternetStatus(this);
                showDialog(status);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkBox.isChecked()) {
            registerReceiver(internetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(internetReceiver);
        } catch (IllegalArgumentException ignored) {}
    }

    private void showDialog(String status) {
        new AlertDialog.Builder(this)
                .setTitle("Internet Status")
                .setMessage(status)
                .setPositiveButton("OK", null)
                .show();
    }

    public static String getInternetStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return "No connectivity manager available";

        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        if (nc == null) return "No Internet Connection";

        boolean wifi = nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        boolean mobile = nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);

        if (wifi && mobile) return "Connected to both Wi-Fi and Mobile Data";
        else if (wifi) return "Connected via Wi-Fi";
        else if (mobile) return "Connected via Mobile Data";
        else return "No Internet Connection";
    }
}