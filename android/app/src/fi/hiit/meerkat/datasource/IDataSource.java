package fi.hiit.meerkat.datasource;

import java.lang.Runnable;
import android.content.Context;


/**
 */
public interface IDataSource extends Runnable
{
    public abstract boolean init(Context context);
    public abstract void start();
    public abstract void stop();
    public abstract String getLabel();
    public abstract String getDescription();
}
