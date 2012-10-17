package fi.hiit.meerkat.datasink;

/**
 */
public interface IDataSink
{
    public abstract boolean isActive();
    public abstract void write(byte channelId, byte[] data);
}

