package fi.hiit.meerkat.datasource;

import java.lang.Thread;
import android.util.Log;
import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.datasink.IDataSink;

/**
 */
public abstract class AbstractPeriodicDataSource implements IDataSource
{
    protected Thread mThread;
    protected byte mChannelId;
    protected IDataSink mSink;
    protected int mPeriodMs;

    public AbstractPeriodicDataSource(IDataSink sink, byte channelId, int periodMs)
    {
        mPeriodMs = periodMs;
        mChannelId = channelId;
        mSink = sink;
    }

    public abstract void tick();

    @Override
    public void start()
    {
        Thread mThread = new Thread(this);
        mThread.start();
        Log.i(MeerkatApplication.TAG, "AbstractPeriodicDataSource.start");
    }

    @Override
    public void run()
    {
        try {
            while (true) {
                tick();
                Thread.sleep(mPeriodMs);
            }
        }
        catch(InterruptedException ex) {
            Log.i(MeerkatApplication.TAG, "AbstractPeriodicDataSource: run: interrupted"); 
        }
        Log.i(MeerkatApplication.TAG, "AbstractPeriodicDataSource.run");
    }

    @Override
    public void stop()
    {
        mThread.stop();
        Log.i(MeerkatApplication.TAG, "AbstractPeriodicDataSource.stop");
    }
}


