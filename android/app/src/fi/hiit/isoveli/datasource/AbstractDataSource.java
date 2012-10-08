package fi.hiit.isoveli.datasource;

import fi.hiit.isoveli.DataController;

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
