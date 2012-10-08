package fi.hiit.isoveli.datasink;

import fi.hiit.isoveli.DataController;

/**
 */
public abstract class AbstractDataSink
{
    protected DataController dataController;

    public AbstractDataSink(DataController dataController)
    {
        this.dataController = dataController;
    }

    abstract void write();
}

