package fi.hiit.meerkat.datasource;

import android.util.Log;
import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.datasink.IDataSink;
import fi.hiit.meerkat.datasink.DataSinkPacketTooBigException;

/**
 */
public class HeartbeatDataSource extends AbstractPeriodicDataSource
{
    private int i;

    public HeartbeatDataSource(IDataSink sink, byte channelId, int periodMs)
    {
        super(sink, channelId, periodMs);
        Log.d(MeerkatApplication.TAG, "HeartbeatDataSource.ctor");
        i = 0;
    }

    @Override
    public String getLabel()
    {
        return "Heartbeat data Source";
    }

    @Override
    public String getDescription()
    {
        return "Heartbeat data source. Periodically emmits heartbeat signal.";
    }

    @Override
    public void tick()
    {
        Log.d(MeerkatApplication.TAG, "HeartbeatDataSource.tick");

        byte[] payload = {'H'};
        try {
            mSink.write(mChannelId, payload);
        }
        catch (DataSinkPacketTooBigException ex) {
            Log.e(MeerkatApplication.TAG, "Packet too big. Dropping.");
        }
    }
}


