package fi.hiit.meerkat.service;

import android.util.Log;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.DataController;

/**
  */

public class MeerkatService extends Service
{
    private MeerkatApplication app;

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.app = (MeerkatApplication)getApplication();

        Log.d(MeerkatApplication.TAG, "Service.onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        Log.d(MeerkatApplication.TAG, "Service.onStartCommand");

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(MeerkatApplication.TAG, "Service.onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
