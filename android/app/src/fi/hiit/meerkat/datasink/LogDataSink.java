package fi.hiit.meerkat.datasink;

import android.util.Log;
import android.content.Context;
import fi.hiit.meerkat.MeerkatApplication;


/**
 */
public class LogDataSink implements IDataSink
{
    @Override
    public boolean isActive()
    {
        return true;
    }

    @Override
    public void open(Context context) { }

    @Override
    public void close() { }

    @Override
    public void write(byte channelId, byte[] data)
    {
        String payload = channelId + (new String(data));
        Log.i(MeerkatApplication.TAG, payload);
    }
}

