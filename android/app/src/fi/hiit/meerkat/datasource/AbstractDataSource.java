package fi.hiit.meerkat.datasource;

import fi.hiit.meerkat.DataController;

/**
 */
public abstract class AbstractDataSource
{
    protected DataController dataController;

    public AbstractDataSource(DataController dataController)
    {
        this.dataController = dataController;
    }
    public abstract void start();
    public abstract void stop();
}
