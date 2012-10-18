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
    protected boolean mRun;

    public AbstractPeriodicDataSource(IDataSink sink, byte channelId, int periodMs)
    {
        mPeriodMs = periodMs;
        mChannelId = channelId;
        mSink = sink;
        mRun = true;
    }

    public abstract void tick();

    @Override
    public void start()
    {
        mRun = true;
        mThread = new Thread(this);
        mThread.start();
        Log.i(MeerkatApplication.TAG, "AbstractPeriodicDataSource.start");
    }

    @Override
    public void run()
    {
        Log.i(MeerkatApplication.TAG, "AbstractPeriodicDataSource.run");
        try {
            while (mRun) {
                // execute the main task of the thread
                tick();

                // sleep for mPeriodMs milliseconds
                Thread.sleep(mPeriodMs);
            }
        }
        catch(InterruptedException ex) {
            Log.i(MeerkatApplication.TAG, "AbstractPeriodicDataSource: run: interrupted"); 
        }
    }

    @Override
    public void stop()
    {
        mRun = false;
        Log.i(MeerkatApplication.TAG, "AbstractPeriodicDataSource.stop");
    }
}


