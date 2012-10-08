package fi.hiit.isoveli.datasource;

import android.util.Log;
import android.hardware.Camera;
import fi.hiit.isoveli.DataController;

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
        Log.i("IsoVeli", "CameraDataSource.start");
        Camera camera = getCameraInstance();
        if (camera == null) {
            Log.i("IsoVeli", "Camera could not be opened");
            /*[TODO: handle camera error]*/
        }
        Log.i("IsoVeli", "Camera opened: " + camera);


    }

    public void stop()
    {
        Log.i("IsoVeli", "CameraDataSource.stop");
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
