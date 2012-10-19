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
        mPeriodMs = periodMs;
        mChannelId = channelId;
        mSink = sink;
        mRun = true;
        mTickLock = false;
    }

    // this is where the main work happens
    public abstract void tick();

    @Override
    public boolean init(MeerkatApplication application) {
        mApplication = application;
        return true;
    }
    
    @Override
    public void start()
    {
        mRun = true;
        mTickLock = false;
        new WaitTask().execute(mPeriodMs);
        Log.i(MeerkatApplication.TAG, "AbstractPeriodicUIDataSource.start");
    }

    @Override
    public void run() { /* [FIXME: nothing] */ }

    @Override
    public void stop()
    {
        mRun = false;
        Log.i(MeerkatApplication.TAG, "AbstractPeriodicUIDataSource.stop");
    }

    protected class WaitTask extends AsyncTask<Integer, Void, Void> {
        protected Void doInBackground(Integer... periodMs)
        {
            Log.i(MeerkatApplication.TAG, "AbstractPeriodicUIDataSource.WaitTask.doInBackground: " + periodMs[0].intValue());
            try {
                Log.i(MeerkatApplication.TAG, "AbstractPeriodicUIDataSource: task: BEFORE SLEEP"); 
                Thread.sleep(100);
                Log.i(MeerkatApplication.TAG, "AbstractPeriodicUIDataSource: task: AFTER SLEEP"); 
            }
            catch(InterruptedException ex) {
                Log.i(MeerkatApplication.TAG, "AbstractPeriodicUIDataSource: task: interrupted"); 
            }
            return null;
        }

        protected void onPostExecute(Void result)
        {
            Log.i(MeerkatApplication.TAG, "AbstractPeriodicUIDataSource.WaitTask.onPostExecute");
            AbstractPeriodicUIDataSource.this.tick();
            if (AbstractPeriodicUIDataSource.this.mRun) {
                new WaitTask().execute(AbstractPeriodicUIDataSource.this.mPeriodMs);
            }
        }
    }

}


