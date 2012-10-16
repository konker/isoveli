package fi.hiit.meerkat.datasink;

import android.util.Log;
import fi.hiit.meerkat.DataController;

/**
 */
public class UsbDataSink extends AbstractDataSink implements Runnable
{
    public static String TAG = UsbDataSink.class.getName();

    public UsbDataSink(DataController dataController)
    {
        super(dataController);
    }

    @Override
    public void run()
    {
        /*
        try {
            Looper.prepare();

            // Handler will bind to Looper that is attached to the current thread
            handler = new Handler();

            Looper.loop();
        }
        catch (Throwable t) {
            Log.e(TAG, "UsbDataSink: halted", t);
        }
        */
    }

    public void write()
    {
    }
}


