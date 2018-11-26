package com.example.avdey.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    final String LOG_TAG = "myLOG";
    ExecutorService executorService;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Service create");
        executorService = Executors.newFixedThreadPool(1);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Service onStartCommand");

        PendingIntent pi = intent.getParcelableExtra(MainActivity.PARAM_PANDING);
        MyTask task = new MyTask(startId, pi);
        executorService.execute(task);
        return super.onStartCommand(intent, flags, startId);

    }


    class MyTask implements Runnable {
        int startId;
        PendingIntent pi;

        public MyTask(int startId, PendingIntent pi) {
            this.startId = startId;
            this.pi = pi;
        }

        @Override
        public void run() {
            Log.d(LOG_TAG, "MyTask " + startId);

            try {
                pi.send(MainActivity.STATUS_START);
                TimeUnit.SECONDS.sleep(5);

                Intent intent = new Intent().putExtra(MainActivity.PARAM_RESULT, "finish");
                pi.send(MyService.this, MainActivity.STATUS_FINISH, intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

            stop();
        }

        void stop() {
            Log.d(LOG_TAG, "stop: ");
            stopSelf(startId);
        }
    }




    public IBinder onBind(Intent intent) {
        return null;
    }




}

