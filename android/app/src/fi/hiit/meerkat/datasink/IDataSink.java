package fi.hiit.meerkat.datasink;

import android.content.Context;

/**
 */
public interface IDataSink
{
    public static final int PACKET_MAX_BYTES = 8388608; // 8mb

    public abstract boolean isActive();
    public abstract void open(Context context);
    public abstract void close();
    public abstract void write(byte channelId, byte[] data)
        throws DataSinkPacketTooBigException;
}

