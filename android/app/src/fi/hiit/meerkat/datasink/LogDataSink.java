package fi.hiit.meerkat.datasink;

import android.util.Log;
import fi.hiit.meerkat.DataController;

/**
 */
public class LogDataSink extends AbstractDataSink
{
    public LogDataSink(DataController dataController)
    {
        super(dataController);
    }

    public void write()
    {
        Log.i("meerkat", "LogDataSink.write");
    }
}

