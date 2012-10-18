package fi.hiit.meerkat.datasource;

import java.lang.Thread;
import android.util.Log;
import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.datasink.IDataSink;
import fi.hiit.meerkat.datasink.DataSinkPacketTooBigException;

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
        return "Dumm Data Source";
    }

    @Override
    public String getDescription()
    {
        return "Dummy data source. Periodically emmits dummy data.";
    }

    @Override
    public void tick()
    {
        ++i;
        byte[] payload = new byte[16384*2];
        for (int j=0; j<payload.length; j++) {
            payload[j] = (byte)((48 + i) % 255);
        }
        payload[0] = '*';
        payload[payload.length - 1] = '*';

        try {
            mSink.write(mChannelId, payload);
            Log.i(MeerkatApplication.TAG, "DummyDataSource: " + mChannelId + ": run: TICK: " + i + ":|" + (new String(payload)) + "|");
        }
        catch (DataSinkPacketTooBigException ex) {
            Log.e(MeerkatApplication.TAG, "Packet too big. Dropping.");
        }
    }
}

