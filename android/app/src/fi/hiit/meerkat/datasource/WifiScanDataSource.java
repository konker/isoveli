package fi.hiit.meerkat.datasource;

import java.util.List;    
import java.util.ArrayList;

import android.util.Log;
import android.content.Context;
import android.content.IntentFilter;    
import android.content.Intent;     
import android.content.BroadcastReceiver;
import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;    

import com.google.gson.Gson;

import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.datasink.IDataSink;
import fi.hiit.meerkat.datasink.DataSinkPacketTooBigException;

/**
 */
public class WifiScanDataSource extends AbstractPeriodicDataSource
{
    private WifiManager mWifiManager;
    private BroadcastReceiver mReceiver;
    private List<ScanResult> mResults;
    private Gson mGson;

    public WifiScanDataSource(IDataSink sink, byte channelId, int periodMs)
    {
        super(sink, channelId, periodMs);
        mGson = new Gson();
    }

    @Override
    public String getLabel()
    {
        return "Wifi ScanData Source";
    }

    @Override
    public String getDescription()
    {
        return "Wifi Scan data source. Periodically scan wifi APN information.";
    }

    @Override
    public void stop()
    {
        Log.i(MeerkatApplication.TAG,  "WifiScanDataSource.stop");

        mContext.unregisterReceiver(mReceiver);
        super.stop();
    }

    @Override
    public boolean init(Context context)
    {
        Log.i(MeerkatApplication.TAG,  "WifiScanDataSource.init");
        super.init(context);

        mWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager.isWifiEnabled() == false) {
            // [FIXME: how should this be handled?]
            Log.i(MeerkatApplication.TAG,  "Wifi is disabled... enabling.");
            mWifiManager.setWifiEnabled(true);
        }

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                String action = intent.getAction();
                // When scan is ready
                if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                    mResults = mWifiManager.getScanResults();
                }
            }
        };
        mContext.registerReceiver(mReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        return true;
    }

    @Override
    public void tick()
    {
        Log.i(MeerkatApplication.TAG,  "WifiScanDataSource.tick");
        if (mResults != null) {

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
            Log.i(MeerkatApplication.TAG, "WifiScanDataSource.tick: NULL results.");
        }

        mWifiManager.startScan();
    }
}


