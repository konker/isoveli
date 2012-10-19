package fi.hiit.meerkat.service;

import java.util.HashMap;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileDescriptor;

import android.util.Log;
import android.app.PendingIntent;
import android.app.Notification;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbAccessory;
import android.os.ParcelFileDescriptor;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import fi.hiit.meerkat.R;
import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.activity.MainActivity;
import fi.hiit.meerkat.datasink.*;
import fi.hiit.meerkat.datasource.*;

/**
  */
public class MeerkatService extends Service
{
    private static final String ACTION_USB_PERMISSION =
            "fi.hiit.meerkat.USB_PERMISSION";

    private MeerkatApplication mApplication;
    private IDataSink mSink;
    //protected HashMap<String, IDataSource> sources;
    private WifiScanDataSource mSource2;

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.mApplication = (MeerkatApplication)getApplication();

        initSink();

        initSources();

        Log.d(MeerkatApplication.TAG, "Service.onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

        // Notification with app icon
        Notification notification =
            new Notification(R.drawable.ic_launcher, getText(R.string.app_name),
                             System.currentTimeMillis());
        // clicking notification opens MainActivity
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, getText(R.string.app_name),
                                        getText(R.string.app_name), pendingIntent);
        
        // Start the service in the foreground
        startForeground(R.id.serviceNotificationId, notification);

        Log.d(MeerkatApplication.TAG, "Service.onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        closeSources();
        closeSink();
        stopForeground(true);
        Log.d(MeerkatApplication.TAG, "Service.onDestroy");
    }

    private void initSink()
    {
        mSink = new UsbDataSink();
        mSink.open(this);
    }
    private void closeSink()
    {
        mSink.close();
    }

    private void initSources()
    {
        mSource1 = new WifiScanDataSource(mSink, (byte)0x20, 10000);
        mSource1.init(this);
        mSource1.start();
    }
    private void closeSources()
    {
        mSource1.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
