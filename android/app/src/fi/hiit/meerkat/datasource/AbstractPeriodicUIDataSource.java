package fi.hiit.meerkat.datasource;

import android.util.Log;
import android.content.Context;
import android.os.AsyncTask;

import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.datasink.IDataSink;

/**
 */
public abstract class AbstractPeriodicUIDataSource implements IDataSource
{
    protected byte mChannelId;
    protected IDataSink mSink;
    protected int mPeriodMs;
    protected boolean mRun;
    protected boolean mTickLock;
    protected MeerkatApplication mApplication;

    public AbstractPeriodicUIDataSource(IDataSink sink, byte channelId, int periodMs)
    {
        Log.d(MeerkatApplication.TAG, "\tAbstractPeriodicUIDataSource.ctor");
        mPeriodMs = periodMs;
        mChannelId = channelId;
        mSink = sink;
        mRun = true;
        mTickLock = false;
    }

    // this is where the main work happens
    public abstract void tick();

    @Override
    public void start()
    {
        Log.d(MeerkatApplication.TAG, "\tAbstractPeriodicUIDataSource.start");
        mRun = true;
        mTickLock = false;
        new WaitTask().execute(mPeriodMs);
    }

    @Override
    public void run() { /* [Nothing. Using AsyncTask instead of Thread.] */ }

    @Override
    public boolean isRunning()
    {
        return mRun;
    }

    @Override
    public void stop()
    {
        Log.d(MeerkatApplication.TAG, "\tAbstractPeriodicUIDataSource.stop");
        mRun = false;
    }

    protected class WaitTask extends AsyncTask<Integer, Void, Void> {
        protected Void doInBackground(Integer... periodMs)
        {
            Log.d(MeerkatApplication.TAG, "\tAbstractPeriodicUIDataSource.WaitTask.doInBackground: " + periodMs[0].intValue());
            try {
                Thread.sleep(periodMs[0].intValue());
            }
            catch(InterruptedException ex) {
                Log.i(MeerkatApplication.TAG, "AbstractPeriodicUIDataSource: task: interrupted"); 
            }
            return null;
        }

        protected void onPostExecute(Void result)
        {
            Log.d(MeerkatApplication.TAG, "\tAbstractPeriodicUIDataSource.WaitTask.onPostExecute");
            if (AbstractPeriodicUIDataSource.this.mRun) {
                AbstractPeriodicUIDataSource.this.tick();
                new WaitTask().execute(AbstractPeriodicUIDataSource.this.mPeriodMs);
            }
        }
    }
}


