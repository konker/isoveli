package fi.hiit.meerkat.datasource;

import java.util.List;    

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
        mContext.unregisterReceiver(mReceiver);
        super.stop();
    }

    public void init(Context context)
    {
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
                List<ScanResult> results = mWifiManager.getScanResults();
                // [TODO: serialze results to JSON]
                String json = mGson.toJson(results);  
                Log.d(MeerkatApplication.TAG, json);
                // [TODO: write to sink]
            }
        };
        mContext.registerReceiver(mReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    @Override
    public void tick()
    {
        mWifiManager.startScan();
        /*
        try {
            mSink.write(mChannelId, payload);
        }
        catch (DataSinkPacketTooBigException ex) {
            Log.e(MeerkatApplication.TAG, "Packet too big. Dropping.");
        }
        */
    }
}


