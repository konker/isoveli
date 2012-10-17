package fi.hiit.meerkat.datasink;

import fi.hiit.meerkat.DataController;

/**
 */
public abstract class AbstractDataSink
{
    protected DataController dataController;

    public AbstractDataSink(DataController dataController)
    {
        this.dataController = dataController;
    }

    public abstract boolean isActive();

    abstract void write(byte channelId, byte[] data);
}

