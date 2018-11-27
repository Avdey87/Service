package com.example.avdey.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    private TextView textView;
    private Button btStart;
    private ProgressBar progressBar;
    BroadcastReceiver br;

    public static int STATUS_START = 1;
    public static int STATUS_FINISH = 0;

    public static final String PARAM_TASK = "task";
    public final static String PARAM_STATUS = "status";

    public static final String BROADCAST_ACTION = "com.avdey.service";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        btStart = findViewById(R.id.bt_start);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(PARAM_STATUS, 0);
                Log.d(LOG_TAG, "onReceive status" + status);

                if (status == STATUS_START) {
                    textView.setText("Loading..");
                    btStart.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (status == STATUS_FINISH) {
                    textView.setText("Ready!!");
                    btStart.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }

            }


        };

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(br, intentFilter);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                startService(intent);
            }
        });
    }


}