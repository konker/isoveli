package fi.hiit.meerkat.datasource;

import java.lang.Thread;
import android.util.Log;
import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.datasink.IDataSink;

/**
 */
public class DummyDataSource extends AbstractPeriodicDataSource
{
    private int i;

    public DummyDataSource(IDataSink sink, byte channelId, int periodMs)
    {
        super(sink, channelId, periodMs);
        i = 0;
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
    public void tick()
    {
        byte[] payload = new byte[512];
        for (int j=1; j<=512; j++) {
            payload[j-1] = (byte)(j % 255);
        }
        payload[511] = (byte)(i++ % 255);

        mSink.write(mChannelId, payload);
        Log.i(MeerkatApplication.TAG, "DummyDataSource: " + mChannelId + ": run: TICK: " + i + ":|" + (new String(payload)) + "|");
    }
}

