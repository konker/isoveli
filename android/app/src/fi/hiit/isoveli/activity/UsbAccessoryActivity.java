package fi.hiit.isoveli.activity;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.app.SherlockActivity;

import fi.hiit.isoveli.R;
import fi.hiit.isoveli.IsoVeliApplication;
import fi.hiit.isoveli.DataController;

public class UsbAccessoryActivity extends SherlockActivity
{
    private IsoVeliApplication app;
    private DataController dataController;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.app = (IsoVeliApplication) getApplication();
        this.dataController = new DataController();

        Log.d(IsoVeliApplication.TAG, "UsbAccessory.onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        /*
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main_activity, menu);
        */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        /*
        switch (item.getItemId()) {
            case R.id.menuPreferences:
                Intent intent = new Intent(this, PrefsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        */
        return super.onOptionsItemSelected(item);
    }

    /* Lifecycle methods [TODO: remove if uneeded?] */
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(IsoVeliApplication.TAG, "UsbAccessory.onPause");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(IsoVeliApplication.TAG, "UsbAccessory.onResume");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(IsoVeliApplication.TAG, "UsbAccessory.onStart");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.d(IsoVeliApplication.TAG, "UsbAccessory.onRestart");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(IsoVeliApplication.TAG, "UsbAccessory.onStop");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(IsoVeliApplication.TAG, "UsbAccessory.onDestroy");
    }
}

