package fi.hiit.isoveli.activity;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.app.SherlockPreferenceActivity;

import fi.hiit.isoveli.R;
import fi.hiit.isoveli.IsoVeliApplication;

public class PrefsActivity extends SherlockPreferenceActivity
{
    private IsoVeliApplication app;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.app = (IsoVeliApplication) getApplication();

        addPreferencesFromResource(R.xml.prefs);
        setContentView(R.layout.prefs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d(IsoVeliApplication.TAG, "PrefsActivity.onCreate");
    }

    /* Lifecycle methods [TODO: remove if uneeded?] */
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(IsoVeliApplication.TAG, "PrefsActivity.onPause");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(IsoVeliApplication.TAG, "PrefsActivity.onResume");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(IsoVeliApplication.TAG, "PrefsActivity.onStart");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.d(IsoVeliApplication.TAG, "PrefsActivity.onRestart");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(IsoVeliApplication.TAG, "PrefsActivity.onStop");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(IsoVeliApplication.TAG, "PrefsActivity.onDestroy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.d(IsoVeliApplication.TAG, "Main.buttonPrefs clicked: " + item.getItemId());
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

