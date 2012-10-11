package fi.hiit.isoveli.activity;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbAccessory;
import android.os.ParcelFileDescriptor;

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

import fi.hiit.isoveli.R;
import fi.hiit.isoveli.IsoVeliApplication;
import fi.hiit.isoveli.DataController;


public class MainActivity extends SherlockActivity
{
    private static int counter = 0;

    private IsoVeliApplication app;
    private DataController dataController;

    private TextView textMessage;
    private UsbManager mUsbManager;
    private UsbAccessory mUsbAccessory;
    private ParcelFileDescriptor mPfd;
    private FileDescriptor mFd;
    private FileOutputStream mOs;
    private FileInputStream mIs;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.app = (IsoVeliApplication) getApplication();
        this.dataController = new DataController();

        textMessage = (TextView)findViewById(R.id.textMessage);
        textMessage.setText(R.string.app_name);

        Button buttonPing = (Button)findViewById(R.id.buttonPing);
        buttonPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(IsoVeliApplication.TAG, "Main.buttonPing clicked");
                if (mOs != null) {
                    try {
                        mOs.write(("KONKER " + MainActivity.counter++ + "").getBytes());
                    }
                    catch (IOException ex) {
                        Log.d(IsoVeliApplication.TAG, "IOException: " + ex);
                    }
                }
            }
        });

        Button buttonPong = (Button)findViewById(R.id.buttonPong);
        buttonPong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(IsoVeliApplication.TAG, "Main.buttonPong clicked");
                if (mIs != null) {
                    try {
                        byte[] buf = new byte[128];
                        int c = mIs.read(buf);
                        Log.d(IsoVeliApplication.TAG, "read: (" + c + ")|" + new String(buf) + "|");
                    }
                    catch (IOException ex) {
                        Log.d(IsoVeliApplication.TAG, "IOException: " + ex);
                    }
                }
            }
        });

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        Intent intent = getIntent();
        mUsbAccessory = (UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);

        if (mUsbAccessory == null) {
            UsbAccessory[] accessoryList = mUsbManager.getAccessoryList();
            if (accessoryList != null && accessoryList.length > 0) {
                mUsbAccessory = accessoryList[0];
            }
        }

        if (mUsbAccessory != null) {
            Log.d(IsoVeliApplication.TAG, "openAccessory: " + mUsbAccessory);
            ParcelFileDescriptor mPfd = mUsbManager.openAccessory(mUsbAccessory);
            if (mPfd != null) {
                FileDescriptor mFd = mPfd.getFileDescriptor();
                mIs = new FileInputStream(mFd);
                mOs = new FileOutputStream(mFd);

                try {
                    mOs.write(("-KONKER " + MainActivity.counter++ + "").getBytes());
                }
                catch (IOException ex) {
                    Log.d(IsoVeliApplication.TAG, "IOException: " + ex);
                }
                //Thread thread = new Thread(null, this, "AccessoryThread");
                //thread.start();
            }
        }

        Log.d(IsoVeliApplication.TAG, "Main.onCreate");
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
        Log.d(IsoVeliApplication.TAG, "Main.onPause");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(IsoVeliApplication.TAG, "Main.onResume");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(IsoVeliApplication.TAG, "Main.onStart");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.d(IsoVeliApplication.TAG, "Main.onRestart");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(IsoVeliApplication.TAG, "Main.onStop");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        try {
            if (mOs != null) {
                mOs.close();
            }
            if (mIs != null) {
                mIs.close();
            }
            if (mPfd != null) {
                mPfd.close();
            }
        }
        catch(IOException ex) {
            Log.d(IsoVeliApplication.TAG, "Error closing streams: " + ex);
        }

        Log.d(IsoVeliApplication.TAG, "Main.onDestroy");
    }
}

