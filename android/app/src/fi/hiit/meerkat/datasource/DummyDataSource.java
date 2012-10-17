package fi.hiit.meerkat.datasource;

import java.lang.Thread;
import android.util.Log;
import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.service.MeerkatService;
import fi.hiit.meerkat.datasink.IDataSink;

/**
 */
public class DummyDataSource implements IDataSource
{
    private MeerkatService mService;
    private Thread mThread;
    private byte mChannelId;
    private IDataSink mSink;

    public DummyDataSource(IDataSink sink, byte channelId)
    {
        mChannelId = channelId;
        mSink = sink;
    }

    @Override
    public String getLabel()
    {
        return "DummyDataSource";
    }

    @Override
    public String getDescription()
    {
        return "Dummy data source. Just emmits dummy data.";
    }

    @Override
    public void start()
    {
        Thread mThread = new Thread(this);
        mThread.start();
        Log.i(MeerkatApplication.TAG, "DummyDataSource.start");
    }

    @Override
    public void run()
    {
        int i = 1;
        try {
            while (true) {
                Thread.sleep(1000);
                mSink.write(mChannelId, ("DummyDataSource: TICK: " + i++).getBytes());
                Log.i(MeerkatApplication.TAG, "DummyDataSource: " + mChannelId + ": run: TICK: " + i);
            }
        }
        catch(InterruptedException ex) {
            Log.i(MeerkatApplication.TAG, "DummyDataSource: run: interrupted"); 
        }
        Log.i(MeerkatApplication.TAG, "DummyDataSource.run");
    }

    @Override
    public void stop()
    {
        Log.i(MeerkatApplication.TAG, "DummyDataSource.stop");
    }
}

