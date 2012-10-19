package fi.hiit.meerkat.datasource;

import java.util.List;    
import java.util.ArrayList;

import android.util.Log;
import android.content.Context;
import android.content.IntentFilter;    
import android.content.Intent;     
import android.content.BroadcastReceiver;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.google.gson.Gson;

import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.datasink.IDataSink;
import fi.hiit.meerkat.datasink.DataSinkPacketTooBigException;
import fi.hiit.meerkat.dto.BluetoothScanResult;

/**
 */
public class BluetoothScanDataSource extends AbstractPeriodicDataSource
{
    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mReceiver;
    private List<BluetoothScanResult> mResults;
    private Gson mGson;

    public BluetoothScanDataSource(IDataSink sink, byte channelId, int periodMs)
    {
        super(sink, channelId, periodMs);
        mGson = new Gson();
    }

    @Override
    public String getLabel()
    {
        return "Bluetooth ScanData Source";
    }

    @Override
    public String getDescription()
    {
        return "Bluetooth Scan data source. Periodically scan locally visible bluetooth devices.";
    }

    @Override
    public void stop()
    {
        Log.i(MeerkatApplication.TAG,  "BluetoothScanDataSource.stop");

        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        if (mReceiver != null) {
            mApplication.unregisterReceiver(mReceiver);
        }
        super.stop();
    }

    @Override
    public boolean init(MeerkatApplication application)
    {
        Log.i(MeerkatApplication.TAG,  "BluetoothScanDataSource.init");
        super.init(application);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            // [FIXME: how should this be handled?]
            Log.i(MeerkatApplication.TAG,  "Bluetooth is disabled... stopping.");
            stop();
            return false;
        }
        else {
            Log.d(MeerkatApplication.TAG,  "BluetoothScanDataSource.init: adapter: " + mBluetoothAdapter);
        }

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    if (mResults == null) {
                        mResults = new ArrayList<BluetoothScanResult>();
                    }

                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mResults.add(new BluetoothScanResult(device));
                }
            }
        };

        // Register the BroadcastReceiver
        mApplication.registerReceiver(mReceiver,
                new IntentFilter(BluetoothDevice.ACTION_FOUND));

        return true;
    }

    @Override
    public void tick()
    {
        Log.i(MeerkatApplication.TAG,  "BluetoothScanDataSource.tick");
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mResults != null) {
            mBluetoothAdapter.cancelDiscovery();

            // serialize results to JSON
            String json = mGson.toJson(mResults);  

            // write JSON to data sink
            try {
                mSink.write(mChannelId, json);
            }
            catch (DataSinkPacketTooBigException ex) {
                Log.e(MeerkatApplication.TAG, "Packet too big. Dropping.");
            }

            // reset results
            mResults.clear();
        }
        else {
            Log.i(MeerkatApplication.TAG, "BluetoothScanDataSource.tick: NULL results.");
        }

        mBluetoothAdapter.startDiscovery();
    }
}


