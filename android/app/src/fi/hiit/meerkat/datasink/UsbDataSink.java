package fi.hiit.meerkat.datasink;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileDescriptor;

import android.util.Log;
import android.os.ParcelFileDescriptor;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbAccessory;
import android.content.Context;

import fi.hiit.meerkat.MeerkatApplication;

/**
 */
public class UsbDataSink implements IDataSink
{
    private ParcelFileDescriptor mFileDescriptor;
    private FileOutputStream mOutputStream;
    
    public UsbDataSink(MeerkatApplication application)
    {
        openAccessory(application);
    }


    public void onDestroy()
    {
        closeAccessory();
    }

    @Override
    public boolean isActive()
    {
        return false;
    }

    /*
    @Override
    public void run()
    {
        synchronized(this) {
            // DO SOMETHING?
        }
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException ex) {
            Log.d(MeerkatApplication.TAG, "sleep: interrupted: " + ex);
        }
    }
    */

    @Override
    public synchronized void write(byte channelId, byte[] data)
    {
        byte[] packet = new byte[data.length + 1];
        packet[0] = channelId;
        System.arraycopy(data, 0, packet, 1, data.length);
        if (mOutputStream != null) {
            try {
                mOutputStream.write(packet);
            }
            catch (IOException ex) {
                // [TODO: should this be thrown up?]
                Log.d(MeerkatApplication.TAG, "IOException: " + ex);
            }
        }
    }

    private void openAccessory(MeerkatApplication application)
    {
        UsbManager usbManager = (UsbManager)application.getSystemService(Context.USB_SERVICE);
        UsbAccessory[] accessoryList = usbManager.getAccessoryList();
        if (accessoryList != null && accessoryList.length > 0) {
            mFileDescriptor = usbManager.openAccessory(accessoryList[0]);
            if (mFileDescriptor != null) {
                FileDescriptor fd = mFileDescriptor.getFileDescriptor();
                mOutputStream = new FileOutputStream(fd);
            }
            else {
                Log.d(MeerkatApplication.TAG, "Service.openAccessory: Failed to open accessory: 1");
            }
        }
        else {
            Log.d(MeerkatApplication.TAG, "Service.openAccessory: Failed to open accessory: 2");
        }
        Log.d(MeerkatApplication.TAG, "Service.openAccessory");
    }

    private void closeAccessory()
    {
        try {
            if (mOutputStream != null) {
                mOutputStream.close();
            }
            if (mFileDescriptor != null) {
                mFileDescriptor.close();
            }
        }
        catch(IOException ex) {
            Log.d(MeerkatApplication.TAG, "Service: Error closing streams: " + ex);
        }
        Log.d(MeerkatApplication.TAG, "Service.closeAccessory");
    }
}


