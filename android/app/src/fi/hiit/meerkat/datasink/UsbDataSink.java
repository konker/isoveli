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
    private boolean mActive;
    
    public UsbDataSink()
    {
        mActive = false;
    }

    @Override
    public boolean isActive()
    {
        return mActive;
    }

    @Override
    public void open(Context context)
    {
        openAccessory(context);
    }

    @Override
    public void close()
    {
        closeAccessory();
    }

    @Override
    public synchronized void write(byte channelId, String data)
            throws DataSinkPacketTooBigException
    {
        write(channelId, data.getBytes());
    }

    @Override
    public synchronized void write(byte channelId, byte[] data)
            throws DataSinkPacketTooBigException
    {
        if (mOutputStream != null) {
            try {
                // check that data + channelId is not greater than max packet size
                if (data.length >= IDataSink.PACKET_MAX_BYTES) {
                    throw new DataSinkPacketTooBigException();
                }
                mOutputStream.write(makePacket(channelId, data));
            }
            catch (IOException ex) {
                // [TODO: should this be thrown up?]
                Log.d(MeerkatApplication.TAG, "IOException: " + ex);
            }
        }
    }

    private byte[] makePacket(byte channelId, byte[] data)
    {
        byte[] packet = new byte[data.length + 1];
        packet[0] = channelId;
        System.arraycopy(data, 0, packet, 1, data.length);
        return packet;
    }

    // Helper methods
    private void openAccessory(Context context)
    {
        UsbManager usbManager = (UsbManager)context.getSystemService(Context.USB_SERVICE);
        UsbAccessory[] accessoryList = usbManager.getAccessoryList();
        if (accessoryList != null && accessoryList.length > 0) {
            mFileDescriptor = usbManager.openAccessory(accessoryList[0]);
            if (mFileDescriptor != null) {
                FileDescriptor fd = mFileDescriptor.getFileDescriptor();
                mOutputStream = new FileOutputStream(fd);
            }
            else {
                mActive = false;
                Log.d(MeerkatApplication.TAG, "Service.openAccessory: Failed to open accessory: 1");
            }
        }
        else {
            mActive = false;
            Log.d(MeerkatApplication.TAG, "Service.openAccessory: Failed to open accessory: 2");
        }
        mActive = true;
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
        mActive = false;
        Log.d(MeerkatApplication.TAG, "Service.closeAccessory");
    }
}

