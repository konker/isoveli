package fi.hiit.meerkat;

/**
 */

import java.util.HashMap;
import android.util.Log;

import fi.hiit.meerkat.datasource.*;
import fi.hiit.meerkat.datasink.*;
import fi.hiit.meerkat.MeerkatApplication;

public class DataController
{
    protected HashMap<String, AbstractDataSource> sources;
    protected UsbDataSink mDataSink;

    public DataController()
    {
        sources = new HashMap<String, AbstractDataSource>();
        mDataSink = new UsbDataSink(this);

        initSources();
        startSources();
    }

    public void write(byte channelId, byte[] data)
    {
        mDataSink.write(channelId, data);
    }

    public void initSources()
    {
        // [TODO]
    }

    public void startSources()
    {
        /*[TODO]
        for (String s : sources.keySet()) { 
            Log.i(MeerkatApplication.TAG, "DataController: Starting source: " + s);
            sources.get(s).start();
        }
        */
    }
}
