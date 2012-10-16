package fi.hiit.meerkat;

/**
 */

import java.util.HashMap;
import android.util.Log;

import fi.hiit.meerkat.datasource.*;
import fi.hiit.meerkat.datasink.*;

public class DataController
{
    protected HashMap<String, AbstractDataSource> sources;
    protected HashMap<String, AbstractDataSink> sinks;

    public DataController()
    {
        sources = new HashMap<String, AbstractDataSource>();
        sinks = new HashMap<String, AbstractDataSink>();

        initSources();
        initSinks();

        startSources();
    }

    public boolean write(byte[] data)
    {
        //[TODO]
        return true;
    }

    public void initSources()
    {
        sources.put("camera", new CameraDataSource(this));
    }

    public void initSinks()
    {
        sinks.put("log", new LogDataSink(this));
    }

    public void startSources()
    {
        for (String s : sources.keySet()) { 
            Log.i("meerkat", "Starting source: " + s);
            sources.get(s).start();
        }
    }
}
