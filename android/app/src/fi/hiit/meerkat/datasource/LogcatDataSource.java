package fi.hiit.meerkat.datasource;

import android.util.Log;
import fi.hiit.meerkat.MeerkatApplication;

/**
 */
public class LogcatDataSource extends AbstractDataSource
{
    public LogcatDataSource(MeerkatApplication app)
    {
        super(app);
    }

    @Override
    public String getLabel()
    {
        return "LogcatDataSource";
    }

    @Override
    public String getDescription()
    {
        return "Read logcat data";
    }

    @Override
    public void run()
    {
        Log.i(MeerkatApplication.TAG, "LogcatDataSource.run");
    }

    @Override
    public void stop()
    {
        Log.i(MeerkatApplication.TAG, "LogcatDataSource.stop");
    }


}
