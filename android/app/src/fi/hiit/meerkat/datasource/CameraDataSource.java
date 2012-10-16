package fi.hiit.meerkat.datasource;

import android.util.Log;
import android.hardware.Camera;
import fi.hiit.meerkat.DataController;

/**
 */
public class CameraDataSource extends AbstractDataSource
{
    public CameraDataSource(DataController dataController)
    {
        super(dataController);
    }

    public void start()
    {
        Log.i("meerkat", "CameraDataSource.start");
        Camera camera = getCameraInstance();
        if (camera == null) {
            Log.i("meerkat", "Camera could not be opened");
            /*[TODO: handle camera error]*/
        }
        Log.i("meerkat", "Camera opened: " + camera);


    }

    public void stop()
    {
        Log.i("meerkat", "CameraDataSource.stop");
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
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
