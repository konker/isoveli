package fi.hiit.meerkat.datasink;

import android.util.Log;
import fi.hiit.meerkat.MeerkatApplication;

/**
 */
public class LogDataSink implements IDataSink
{
    @Override
    public boolean isActive()
    {
        return false;
    }

    @Override
    public void write(byte channelId, byte[] data)
    {
        Log.i(MeerkatApplication.TAG, "LogDataSink.write");
    }
}

