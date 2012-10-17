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
    private PendingIntent mPermissionIntent;
    private UsbManager mUsbManager;
    private UsbAccessory mUsbAccessory;
    private ParcelFileDescriptor mFileDescriptor;
    private FileOutputStream mOutputStream;
    private boolean mPermissionGranted;
    private IDataSink mSink;
    //protected HashMap<String, IDataSource> sources;

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(accessory != null){
                            // set up accessory communication
                            mPermissionGranted = true;
                            //openAccessory();
                            initSink();
                            initSources();
                        }
                    }
                    else {
                        Log.d(MeerkatApplication.TAG, "permission denied for accessory " + accessory);
                    }
                }
            }
            else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
                Log.d(MeerkatApplication.TAG, "Accessory detached detected");
                UsbAccessory accessory = (UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                if (accessory != null) {
                    // clean up and close communication with the accessory
                    //closeAccessory();
                }
            }
        }
    };

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.mApplication = (MeerkatApplication)getApplication();

        mUsbManager = (UsbManager)getSystemService(Context.USB_SERVICE);
        
        // register the BroadcastReceiver
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);

        UsbAccessory[] accessoryList = mUsbManager.getAccessoryList();
        if (accessoryList != null && accessoryList.length > 0) {
            mUsbAccessory = accessoryList[0];

            // request permisssion to use the accessory
            mPermissionGranted = false;
            mUsbManager.requestPermission(mUsbAccessory, mPermissionIntent);
        }

        Log.d(MeerkatApplication.TAG, "Service.onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

        Notification notification = new Notification(R.drawable.ic_launcher, getText(R.string.app_name),
                                                    System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, getText(R.string.app_name),
                                        getText(R.string.app_name), pendingIntent);
        
        startForeground(R.id.serviceNotificationId, notification);

        Log.d(MeerkatApplication.TAG, "Service.onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopForeground(true);
        closeAccessory();
        Log.d(MeerkatApplication.TAG, "Service.onDestroy");
    }

    private void initSink()
    {
        mSink = new UsbDataSink(mApplication);
    }

    private void initSources()
    {
        DummyDataSource oSource1 = new DummyDataSource(mSink, (byte)0x10);
        oSource1.start();

        DummyDataSource oSource2 = new DummyDataSource(mSink, (byte)0x20);
        oSource2.start();
    }

    private void closeAccessory()
    {
        try {
            if (mOutputStream != null) {
                mOutputStream.close();
            }
            if (mFileDescriptor != null) {
                mFileDescriptor.close();
            }
        }
        catch(IOException ex) {
            Log.d(MeerkatApplication.TAG, "Service: Error closing streams: " + ex);
        }
        Log.d(MeerkatApplication.TAG, "Service.closeAccessory");
    }

    private void openAccessory()
    {
        mFileDescriptor = mUsbManager.openAccessory(mUsbAccessory);
        if (mFileDescriptor != null) {
            FileDescriptor fd = mFileDescriptor.getFileDescriptor();
            mOutputStream = new FileOutputStream(fd);

            //Thread thread = new Thread(null, this, "AccessoryThread");
            //thread.start();
        }
        else {
            Log.d(MeerkatApplication.TAG, "Service.openAccessory: Failed to open accessory");
        }
        Log.d(MeerkatApplication.TAG, "Service.openAccessory");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
