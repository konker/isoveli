package fi.hiit.meerkat;

import java.util.ArrayList;

import android.util.Log;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

import fi.hiit.meerkat.datasource.AbstractDataSource;
import fi.hiit.meerkat.datasink.AbstractDataSink;

/**
  */
public class MeerkatApplication extends Application implements OnSharedPreferenceChangeListener
{
    public static final String TAG = "MEERKAT";
    public static final String ERROR_TAG = "MEERKAT:ERROR";

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private ArrayList<AbstractDataSource> mSources;
    private AbstractDataSink mDataSink;

    @Override
    public void onCreate()
    {
        super.onCreate();

        this.mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.mEditor = mPrefs.edit();
        this.mPrefs.registerOnSharedPreferenceChangeListener(this);

        Log.d(MeerkatApplication.TAG, "App.onCreate");
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();

        Log.i(MeerkatApplication.TAG, "App.onTerminate");
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


