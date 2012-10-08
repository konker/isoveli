package fi.hiit.isoveli.datasink;

import android.util.Log;
import fi.hiit.isoveli.DataController;

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
        Log.i("IsoVeli", "LogDataSink.write");
    }
}

