package fi.hiit.meerkat.datasource;

import java.lang.Thread;
import android.util.Log;
import android.content.Context;

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
    protected boolean mTickLock;
    protected Context mContext;

    public AbstractPeriodicDataSource(IDataSink sink, byte channelId, int periodMs)
    {
        mPeriodMs = periodMs;
        mChannelId = channelId;
        mSink = sink;
        mRun = true;
        mTickLock = false;
    }

    // this is where the main work happens
    public abstract void tick();

    @Override
    public boolean init(Context context) {
        mContext = context;
        return true;
    }

    @Override
    public void start()
    {
        mRun = true;
        mTickLock = false;
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
                if (!mTickLock) {
                    mTickLock = true;

                    // [FIXME: is there a better way to protect against mRun
                    // being unset during sleep?]
                    if (mRun) {
                        // execute the main task of the thread
                        tick();
                    }

                    mTickLock = false;
                }

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

