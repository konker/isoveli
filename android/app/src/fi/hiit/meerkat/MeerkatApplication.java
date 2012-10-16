package fi.hiit.meerkat;

import android.util.Log;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class MeerkatApplication extends Application implements OnSharedPreferenceChangeListener
{
    public static final String TAG = "MEERKAT";
    public static final String ERROR_TAG = "MEERKAT:ERROR";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.editor = prefs.edit();
        this.prefs.registerOnSharedPreferenceChangeListener(this);

        Log.d(MeerkatApplication.TAG, "App.onCreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Log.i(MeerkatApplication.TAG, "App.onTerminate");
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }
    public SharedPreferences.Editor getPrefsEditor() {
        return editor;
    }

    /* methods required by OnSharedPreferenceChangeListener */
    public synchronized void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        //[TODO]
        Log.i(MeerkatApplication.TAG, "App.onSharedPreferenceChanged");
        return;
    }
}


