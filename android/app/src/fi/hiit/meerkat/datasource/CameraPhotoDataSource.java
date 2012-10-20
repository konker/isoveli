package fi.hiit.meerkat.datasource;

import android.util.Log;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.view.SurfaceView;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.FrameLayout;

import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.datasink.IDataSink;
import fi.hiit.meerkat.datasink.DataSinkPacketTooBigException;
import fi.hiit.meerkat.R;

/**
 */
public class CameraPhotoDataSource extends AbstractPeriodicUIDataSource
{
    private PictureCallback mPictureCallback;
    private byte[] mResult;

    public CameraPhotoDataSource(IDataSink sink, byte channelId, int periodMs)
    {
        super(sink, channelId, periodMs);
    }

    @Override
    public String getLabel()
    {
        return "Camera Photo Data Source";
    }

    @Override
    public String getDescription()
    {
        return "Camera photo data source. Periodically take a photo with the main camera.";
    }

    @Override
    public void stop()
    {
        Log.i(MeerkatApplication.TAG,  "CameraPhotoDataSource.stop");
        super.stop();
    }

    @Override
    public boolean init(MeerkatApplication application)
    {
        Log.i(MeerkatApplication.TAG,  "CameraPhotoDataSource.init");
        super.init(application);

        if (!mApplication.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // no camera on this device
            // [FIXME: how should this be handled?]
            Log.i(MeerkatApplication.TAG,  "No camera support... stopping.");
            stop();
            return false;
        }

        mPictureCallback = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.i(MeerkatApplication.TAG, "CameraPhotoDataSource.mPictureCallback: " + data.length);
                mResult = data;
                mApplication.mCamera.startPreview();
            }
        };

        return true;
    }

    @Override
    public void tick()
    {
        Log.i(MeerkatApplication.TAG,  "CameraPhotoDataSource.tick");
        if (mResult != null) {
            // write JSON to data sink
            try {
                mSink.write(mChannelId, mResult);
            }
            catch (DataSinkPacketTooBigException ex) {
                Log.e(MeerkatApplication.TAG, "Packet too big. Dropping.");
            }

            // reset results
            mResult = null;
        }
        else {
            Log.i(MeerkatApplication.TAG, "CameraPhotoDataSource.tick: NULL result.");
        }

        try {
            mApplication.mCamera.takePicture(null, null, mPictureCallback);
        }
        catch (Exception ex) {
            Log.i(MeerkatApplication.TAG, "CameraPhotoDataSource.tick: takePicture failed: " + ex);
        }
    }
}

