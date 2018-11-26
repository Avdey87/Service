package com.example.avdey.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button btStart;
    private ProgressBar progressBar;

    final int TASK_CODE = 1;

    public static int STATUS_START = 1;
    public static int STATUS_FINISH =0;

    public static final String PARAM_PANDING = "pendingIntent";
    public static final String PARAM_RESULT = "Loading..";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        btStart = findViewById(R.id.bt_start);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);


        btStart.setOnClickListener(new View.OnClickListener() {
            PendingIntent pi;

            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                pi = createPendingResult(TASK_CODE, intent, 0);
                intent = new Intent(MainActivity.this, MyService.class).putExtra(PARAM_PANDING, pi);
                startService(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == STATUS_START) {
            textView.setText(PARAM_RESULT);
            btStart.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }
        if (resultCode == STATUS_FINISH) {
            textView.setText(PARAM_RESULT);
            btStart.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }

    }
}
