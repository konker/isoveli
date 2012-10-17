package fi.hiit.meerkat.datasink;

import android.util.Log;

import fi.hiit.meerkat.DataController;
import fi.hiit.meerkat.MeerkatApplication;

/**
 */
public class UsbDataSink extends AbstractDataSink implements Runnable
{
    public UsbDataSink(DataController dataController)
    {
        super(dataController);
    }

    @Override
    public boolean isActive()
    {
        return false;
    }

    @Override
    public void run()
    {
        synchronized(this) {
            // DO SOMETHING?
        }
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException ex) {
            Log.d(MeerkatApplication.TAG, "sleep: interrupted: " + ex);
        }
    }

    @Override
    public void write(byte channelId, byte[] data)
    {
        /*
        if (mOutputStream != null) {
            try {
                mOutputStream.write(channelId);
                mOutputStream.write(data);
            }
            catch (IOException ex) {
                // [TODO: should this be thrown up?]
                Log.d(MeerkatApplication.TAG, "IOException: " + ex);
            }
        }
        */
    }
}


