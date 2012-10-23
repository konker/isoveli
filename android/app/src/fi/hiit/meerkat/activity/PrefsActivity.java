package fi.hiit.meerkat.activity;

/*
import android.util.Log;
import android.os.Bundle;
import android.content.Intent;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.app.SherlockPreferenceActivity;

import fi.hiit.meerkat.R;
import fi.hiit.meerkat.MeerkatApplication;

public class PrefsActivity extends SherlockPreferenceActivity
{
    private MeerkatApplication app;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.app = (MeerkatApplication) getApplication();

        addPreferencesFromResource(R.xml.prefs);
        setContentView(R.layout.prefs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d(MeerkatApplication.TAG, "PrefsActivity.onCreate");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(MeerkatApplication.TAG, "PrefsActivity.onPause");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(MeerkatApplication.TAG, "PrefsActivity.onResume");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(MeerkatApplication.TAG, "PrefsActivity.onStart");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.d(MeerkatApplication.TAG, "PrefsActivity.onRestart");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(MeerkatApplication.TAG, "PrefsActivity.onStop");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(MeerkatApplication.TAG, "PrefsActivity.onDestroy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.d(MeerkatApplication.TAG, "Main.buttonPrefs clicked: " + item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
*/
