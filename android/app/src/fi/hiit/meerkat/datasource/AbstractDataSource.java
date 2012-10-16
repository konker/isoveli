package fi.hiit.meerkat.datasource;

import java.lang.Runnable;
import fi.hiit.meerkat.MeerkatApplication;


/**
 */
public abstract class AbstractDataSource implements Runnable
{
    protected MeerkatApplication app;

    public AbstractDataSource(MeerkatApplication app)
    {
        this.app = app;
    }
    public abstract void stop();

    public abstract String getLabel();
    public abstract String getDescription();
}
