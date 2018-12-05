package com.example.avdey.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LocalService extends Service {
    final String LOG_TAG = "myLOG";
    private final IBinder binder = new LocalBinder();
    ExecutorService executorService;


    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Service create");
        executorService = Executors.newFixedThreadPool(1);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Service onStartCommand");

        int task = intent.getIntExtra(MainActivity.PARAM_TASK, 0);
        MyRun myRun = new MyRun(startId, task);
        executorService.execute(myRun);
        return super.onStartCommand(intent, flags, startId);
    }

    class MyRun implements Runnable {
        int startId;
        int task;

        public MyRun(int startId, int task) {
            this.startId = startId;
            this.task = task;
            Log.d(LOG_TAG, "MyRun create" + startId);
        }

        @Override
        public void run() {
            Log.d(LOG_TAG, "MyTask " + startId);
           Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
            try {
                intent.putExtra(MainActivity.PARAM_TASK, task);
                intent.putExtra(MainActivity.PARAM_STATUS, MainActivity.STATUS_START);
                sendBroadcast(intent);

                TimeUnit.SECONDS.sleep(5);

                intent.putExtra(MainActivity.PARAM_STATUS, MainActivity.STATUS_FINISH);
                sendBroadcast(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            stop();
        }

        void stop() {
            Log.d(LOG_TAG, "stop: ");
            stopSelf(startId);
        }
    }



}
