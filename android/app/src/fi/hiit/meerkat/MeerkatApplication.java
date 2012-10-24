package fi.hiit.meerkat;

import java.util.HashMap;

import android.util.Log;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.hardware.Camera;

import fi.hiit.meerkat.datasource.*;
import fi.hiit.meerkat.usb.UsbController;

/**
  */
public class MeerkatApplication extends Application implements OnSharedPreferenceChangeListener
{
    public static final String TAG = "MEERKAT";
    public static final String ERROR_TAG = "MEERKAT:ERROR";
    private static MeerkatApplication instance;

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private UsbController mUsbController;
    private IDataSource mHeartbeat;
    private HashMap<String, IDataSource> mSources;

    private boolean mActive;
    public Camera mCamera; // [FIXME: public]

    public static MeerkatApplication getInstance()
    {
        return instance;
    }

    @Override
    public void onCreate()
    {
        Log.d(MeerkatApplication.TAG, "Application.onCreate");
        super.onCreate();

        instance = this;

        mActive = false;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();
        mPrefs.registerOnSharedPreferenceChangeListener(this);

        mUsbController = new UsbController();
        mUsbController.open(this);
        initHeartbeat();

        initSources();
    }

    @Override
    public void onTerminate()
    {
        Log.i(MeerkatApplication.TAG, "App.onTerminate");
        super.onTerminate();
        stopSources();
        stopHeartbeat();

        mUsbController.close();

        if (mCamera != null) {
            mCamera.release();
        }
    }

    public boolean isActive()
    {
        return mActive;
    }
    public void setActive(boolean active)
    {
        mActive = active;
    }

    public void initHeartbeat()
    {
        mHeartbeat = new HeartbeatDataSource(mUsbController, (byte)0x0, 5000);
        mHeartbeat.start();
    }
    
    public void stopHeartbeat()
    {
        mHeartbeat.stop();
    }

    public void initSources()
    {
        Log.i(MeerkatApplication.TAG, "Application.initSources");
        mSources = new HashMap<String, IDataSource>();

        mSources.put("WifiScanDataSource",
                new WifiScanDataSource(mUsbController, (byte)0x20, 10000));

        mSources.put("BluetoothScanDataSource",
                new BluetoothScanDataSource(mUsbController, (byte)0x30, 10000));

        mSources.put("CameraPhotoDataSource",
                new CameraPhotoDataSource(mUsbController, (byte)0x40, 5000));

        /*
        mSources.put("HeartbeatDataSource",
                new HeartbeatDataSource(mUsbController, (byte)0x0, 5000));
                */
    }

    public void start()
    {
        Log.i(MeerkatApplication.TAG, "Application.start");
        startSources();
        mActive = true;
    }
    public void stop()
    {
        Log.i(MeerkatApplication.TAG, "Application.stop");
        stopSources();
        mActive = false;
    }

    public void startSources()
    {
        for (IDataSource source : mSources.values()) {
            source.start();
        }
    }

    public void stopSources()
    {
        for (IDataSource source : mSources.values()) {
            source.stop();
        }
    }

    public SharedPreferences getPrefs()
    {
        return mPrefs;
    }
    public SharedPreferences.Editor getPrefsEditor()
    {
        return mEditor;
    }

    /* methods required by OnSharedPreferenceChangeListener */
    @Override
    public synchronized void onSharedPreferenceChanged(SharedPreferences prefs, String key)
    {
        //[TODO]
        Log.i(MeerkatApplication.TAG, "App.onSharedPreferenceChanged");
        return;
    }
}


