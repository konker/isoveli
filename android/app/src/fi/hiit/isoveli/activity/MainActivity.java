package fi.hiit.isoveli.activity;

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

import fi.hiit.isoveli.R;
import fi.hiit.isoveli.IsoVeliApplication;
import fi.hiit.isoveli.DataController;


public class MainActivity extends SherlockActivity implements Runnable
{
    private static final String ACTION_USB_PERMISSION =
            "fi.hiit.isoveli.USB_PERMISSION";
    private static int counter = 0;

    private IsoVeliApplication app;
    //private DataController dataController;

    private PendingIntent mPermissionIntent;
    private UsbManager mUsbManager;
    private UsbAccessory mUsbAccessory;
    private ParcelFileDescriptor mFileDescriptor;
    private FileOutputStream mOutputStream;
    private FileInputStream mInputStream;

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MainActivity.ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(accessory != null){
                            //call method to set up accessory communication
                            openAccessory();
                        }
                    }
                    else {
                        Log.d(IsoVeliApplication.TAG, "permission denied for accessory " + accessory);
                    }
                }
            }
            else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
                Log.d(IsoVeliApplication.TAG, "Accessory detached detected");
                UsbAccessory accessory = (UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                if (accessory != null) {
                    // call your method that cleans up and closes communication with the accessory
                    closeAccessory();
                }
            }
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.app = (IsoVeliApplication)getApplication();
        //this.dataController = new DataController();
        //this.setupUi();
        
        mUsbManager = (UsbManager)getSystemService(Context.USB_SERVICE);
        
        // register the BroadcastReceiver
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);


        Intent intent = getIntent();
        mUsbAccessory = (UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);

        if (mUsbAccessory == null) {
            UsbAccessory[] accessoryList = mUsbManager.getAccessoryList();
            if (accessoryList != null && accessoryList.length > 0) {
                mUsbAccessory = accessoryList[0];

                // request permisssion to use the accessory
                mUsbManager.requestPermission(mUsbAccessory, mPermissionIntent);

            }
        }
        else {
            openAccessory();
        }




        /*
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
            ParcelFileDescriptor mFileDescriptor = mUsbManager.openAccessory(mUsbAccessory);
            if (mFileDescriptor != null) {
                FileDescriptor fd = mFileDescriptor.getFileDescriptor();
                mInputStream = new FileInputStream(fd);
                mOutputStream = new FileOutputStream(fd);

                try {
                    mOutputStream.write(("-KONKER " + MainActivity.counter++ + "").getBytes());
                }
                catch (IOException ex) {
                    Log.d(IsoVeliApplication.TAG, "IOException: " + ex);
                }
                //Thread thread = new Thread(null, this, "AccessoryThread");
                //thread.start();
            }
        }
        */

        Log.d(IsoVeliApplication.TAG, "Main.onCreate");
    }

    private void closeAccessory()
    {
        try {
            if (mOutputStream != null) {
                mOutputStream.close();
            }
            if (mInputStream != null) {
                mInputStream.close();
            }
            if (mFileDescriptor != null) {
                mFileDescriptor.close();
            }
        }
        catch(IOException ex) {
            Log.d(IsoVeliApplication.TAG, "Error closing streams: " + ex);
        }
        Log.d(IsoVeliApplication.TAG, "Main.closeAccessory");
    }

    private void openAccessory()
    {
        mFileDescriptor = mUsbManager.openAccessory(mUsbAccessory);
        if (mFileDescriptor != null) {
            FileDescriptor fd = mFileDescriptor.getFileDescriptor();
            mInputStream = new FileInputStream(fd);
            mOutputStream = new FileOutputStream(fd);

            //Thread thread = new Thread(null, this, "AccessoryThread");
            //thread.start();
        }
        else {
            Log.d(IsoVeliApplication.TAG, "Main.openAccessory: Failed to open accessory");
        }
        Log.d(IsoVeliApplication.TAG, "Main.openAccessory");
    }

    @Override
    public void run()
    {
        while (true) {
            synchronized(this) {
                if (mOutputStream != null) {
                    try {
                        mOutputStream.write(("-KONKER " + MainActivity.counter++ + "").getBytes());
                    }
                    catch (IOException ex) {
                        Log.d(IsoVeliApplication.TAG, "write: failed: " + ex);
                    }
                    Log.d(IsoVeliApplication.TAG, "write: " + MainActivity.counter);
                }
            }
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException ex) {
                Log.d(IsoVeliApplication.TAG, "sleep: interrupted: " + ex);
            }
        }
    }

    private void setupUi()
    {
        Button buttonPing = (Button)findViewById(R.id.buttonPing);
        buttonPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(IsoVeliApplication.TAG, "Main.buttonPing clicked");
                if (mOutputStream != null) {
                    try {
                        mOutputStream.write(("KONKER " + MainActivity.counter++ + "").getBytes());
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
                if (mInputStream != null) {
                    try {
                        byte[] buf = new byte[128];
                        int c = mInputStream.read(buf);
                        Log.d(IsoVeliApplication.TAG, "read: (" + c + ")|" + new String(buf) + "|");
                    }
                    catch (IOException ex) {
                        Log.d(IsoVeliApplication.TAG, "IOException: " + ex);
                    }
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
        closeAccessory();

        Log.d(IsoVeliApplication.TAG, "Main.onDestroy");
    }
}

