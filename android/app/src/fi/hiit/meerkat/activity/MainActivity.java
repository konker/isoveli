package fi.hiit.meerkat.activity;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.PendingIntent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbAccessory;
import android.os.ParcelFileDescriptor;

import java.lang.Runnable;
import java.lang.Thread;
import java.lang.InterruptedException;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.app.SherlockActivity;

import fi.hiit.meerkat.R;
import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.service.MeerkatService;
import fi.hiit.meerkat.DataController;
import fi.hiit.meerkat.datasink.*;


public class MainActivity extends SherlockActivity
{
    private static int counter = 0;

    private MeerkatApplication app;
    private DataController mDataController;
    private UsbDataSink mDataSink;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.app = (MeerkatApplication)getApplication();
        this.mDataController = new DataController();

        //this.setupUi();

        Intent intent = new Intent(this, MeerkatService.class);
        startService(intent);

        Log.d(MeerkatApplication.TAG, "Main.onCreate");
    }

    private void setupUi()
    {
        Button buttonMasterOnOffToggle = (Button)findViewById(R.id.buttonMasterOnOffToggle);
        buttonMasterOnOffToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(MeerkatApplication.TAG, "Main.buttonMasterOnOffToggle clicked");
                /*
                if (app.isActive()) {
                    app.stopAll();
                    view.setText(R.string.start);
                }
                else {
                    app.startAll();
                    view.setText(R.string.stop);
                }
                */
                /*
                if (mOutputStream != null) {
                    try {
                        mOutputStream.write(("KONKER " + MainActivity.counter++ + "").getBytes());
                    }
                    catch (IOException ex) {
                        Log.d(MeerkatApplication.TAG, "IOException: " + ex);
                    }
                }
                */
            }
        });

        /*
        Button buttonPong = (Button)findViewById(R.id.buttonPong);
        buttonPong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(MeerkatApplication.TAG, "Main.buttonPong clicked");
                if (mInputStream != null) {
                    try {
                        byte[] buf = new byte[128];
                        int c = mInputStream.read(buf);
                        Log.d(MeerkatApplication.TAG, "read: (" + c + ")|" + new String(buf) + "|");
                    }
                    catch (IOException ex) {
                        Log.d(MeerkatApplication.TAG, "IOException: " + ex);
                    }
                }
            }
        });
        */
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
        Log.d(MeerkatApplication.TAG, "Main.onPause");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(MeerkatApplication.TAG, "Main.onResume");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(MeerkatApplication.TAG, "Main.onStart");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.d(MeerkatApplication.TAG, "Main.onRestart");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(MeerkatApplication.TAG, "Main.onStop");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        Log.d(MeerkatApplication.TAG, "Main.onDestroy");
    }
}

