package fi.hiit.meerkat.datasource;

import java.lang.Runnable;
import fi.hiit.meerkat.MeerkatApplication;


/**
 */
public interface IDataSource extends Runnable
{
    public abstract void start();
    public abstract void stop();
    public abstract String getLabel();
    public abstract String getDescription();
}
