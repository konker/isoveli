package fi.hiit.meerkat.activity;

import android.util.Log;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;

import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbAccessory;
import android.os.ParcelFileDescriptor;

import java.lang.Runnable;
import java.lang.Thread;
import java.lang.InterruptedException;

import fi.hiit.meerkat.R;
import fi.hiit.meerkat.MeerkatApplication;
//import fi.hiit.meerkat.service.MeerkatService;
import fi.hiit.meerkat.datasink.*;
import fi.hiit.meerkat.view.CameraPreview;


public class MainActivity extends Activity
{
    private static int counter = 0;

    private MeerkatApplication mApplication;
    private CameraPreview mPreview;
    private PictureCallback mPictureCallback;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(MeerkatApplication.TAG, "Main.onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mApplication = (MeerkatApplication)getApplication();

        setupUi();

    }

    private void setupUi()
    {
        Button buttonMasterOnOffToggle =
            (Button)findViewById(R.id.buttonMasterOnOffToggle);
        if (mApplication.isActive()) {
            buttonMasterOnOffToggle.setText(getString(R.string.stop));
        }
        else {
            buttonMasterOnOffToggle.setText(getString(R.string.start));
        }
        buttonMasterOnOffToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(MeerkatApplication.TAG, "Main.buttonMasterOnOffToggle clicked");
                if (mApplication.isActive()) {
                    mApplication.stop();
                    ((Button)view).setText(getString(R.string.start));
                }
                else {
                    mApplication.start();
                    ((Button)view).setText(getString(R.string.stop));
                }
            }
        });

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
        Log.d(MeerkatApplication.TAG, "Main.onPause");
        super.onPause();

        if (mApplication.mCamera != null) {
            mApplication.mCamera.release();
        }
    }

    @Override
    protected void onResume()
    {
        Log.d(MeerkatApplication.TAG, "Main.onResume");
        super.onResume();

        try {
            mApplication.mCamera = Camera.open();

            // set the camera into portrait orientation
            //mApplication.mCamera.setDisplayOrientation(90);

            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mApplication.mCamera);
            FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
            preview.addView(mPreview);
        }
        catch (Exception e){
            // [FIXME: how should this be handled?]
            Log.i(MeerkatApplication.TAG,  "Could not open camera... stopping.");
        }
    }

    @Override
    protected void onStart()
    {
        Log.d(MeerkatApplication.TAG, "Main.onStart");
        super.onStart();
    }

    @Override
    protected void onRestart()
    {
        Log.d(MeerkatApplication.TAG, "Main.onRestart");
        super.onRestart();
    }

    @Override
    protected void onStop()
    {
        Log.d(MeerkatApplication.TAG, "Main.onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        Log.d(MeerkatApplication.TAG, "Main.onDestroy");
        super.onDestroy();
        //mApplication.stop();
    }
}

