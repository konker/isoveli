package fi.hiit.meerkat;

import java.util.HashMap;

import android.util.Log;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

/**
  */
public class MeerkatApplication extends Application implements OnSharedPreferenceChangeListener
{
    public static final String TAG = "MEERKAT";
    public static final String ERROR_TAG = "MEERKAT:ERROR";

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    private boolean mActive;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mActive = false;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();
        mPrefs.registerOnSharedPreferenceChangeListener(this);

        Log.d(MeerkatApplication.TAG, "App.onCreate");
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();

        Log.i(MeerkatApplication.TAG, "App.onTerminate");
    }

    public boolean isActive()
    {
        return mActive;
    }
    public void setActive(boolean active)
    {
        mActive = active;
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
    public synchronized void onSharedPreferenceChanged(SharedPreferences prefs, String key)
    {
        //[TODO]
        Log.i(MeerkatApplication.TAG, "App.onSharedPreferenceChanged");
        return;
    }
}


