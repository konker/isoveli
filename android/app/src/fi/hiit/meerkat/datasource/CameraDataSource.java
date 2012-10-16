package fi.hiit.meerkat.datasource;

import android.util.Log;
import android.hardware.Camera;
import fi.hiit.meerkat.MeerkatApplication;

/**
 */
public class CameraDataSource extends AbstractDataSource
{
    public CameraDataSource(MeerkatApplication app)
    {
        super(app);
    }

    @Override
    public String getLabel()
    {
        return "CameraDataSource";
    }

    @Override
    public String getDescription()
    {
        return "Read camera stream";
    }

    @Override
    public void run()
    {
        Log.i(MeerkatApplication.TAG, "CameraDataSource.run");
        Camera camera = getCameraInstance();
        if (camera == null) {
            Log.i(MeerkatApplication.TAG, "Camera could not be opened");
            /*[TODO: handle camera error]*/
        }
        Log.i(MeerkatApplication.TAG, "Camera opened: " + camera);


    }

    @Override
    public void stop()
    {
        Log.i(MeerkatApplication.TAG, "CameraDataSource.stop");
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance()
    {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

}
