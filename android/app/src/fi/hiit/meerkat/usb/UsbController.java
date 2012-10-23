package fi.hiit.meerkat.usb;

import java.lang.Runnable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileDescriptor;

import android.util.Log;
import android.os.ParcelFileDescriptor;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbAccessory;
import android.content.Context;

import fi.hiit.meerkat.MeerkatApplication;
import fi.hiit.meerkat.datasink.*;
import fi.hiit.meerkat.protocol.UsbProtocol;


/**
 */
public class UsbController implements IDataSink, Runnable
{
    //[FIXME: maybe could be larger]
    public static final int MAX_IN_PACKET_SIZE = 1024;

    private ParcelFileDescriptor mFileDescriptor;
    private FileOutputStream mOutputStream;
    private FileInputStream mInputStream;
    private UsbProtocol mProtocol;
    private Thread mListenThread;
    private boolean mActive;
    private boolean mListening;
    
    public UsbController()
    {
        mActive = false;
        mListening = false;
        mProtocol = new UsbProtocol();
    }

    @Override
    public boolean isActive()
    {
        return mActive;
    }

    @Override
    public void open(Context context)
    {
        Log.d(MeerkatApplication.TAG, "UsbController.open");
        openAccessory(context);
        //startListener();
    }

    @Override
    public void close()
    {
        Log.d(MeerkatApplication.TAG, "UsbController.close");
        //stopListener();
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

    public void startListener()
    {
        mListening = true;
        mListenThread = new Thread(this);
        mListenThread.start();
        Log.i(MeerkatApplication.TAG, "UsbController.start");
    }

    public void stopListener()
    {
        mListening = false;
        Log.i(MeerkatApplication.TAG, "UsbController.stop");
    }

    @Override
    public synchronized void run()
    {
        Log.i(MeerkatApplication.TAG, "UsbController.run");
        try {
            while (mListening) {
                // [FIXME: does this need to be re-initialized each time?]
                byte[] packet = new byte[MAX_IN_PACKET_SIZE];
                // [FIXME: should he storeAndExecute be inside try for IOException?]
                try {
                    if (mInputStream.read(packet, 0, MAX_IN_PACKET_SIZE) > 0) {
                        Log.d(MeerkatApplication.TAG, "UsbController.run: got packet: " + new String(packet));

                        // Send to the protocol adapter to deal with
                        mProtocol.storeAndExecute(packet);
                    }
                }
                catch (IOException ioe) {
                    Log.i(MeerkatApplication.TAG, "UsbController: run: IOException: " + ioe); 
                }

                // [FIXME: do we need this?]
                Thread.sleep(1000);
            }
        }
        catch(InterruptedException ex) {
            Log.i(MeerkatApplication.TAG, "UsbController: run: interrupted"); 
        }
    }

    private synchronized byte[] makePacket(byte channelId, byte[] data)
    {
        byte[] packet = new byte[data.length + 1];
        packet[0] = channelId;
        System.arraycopy(data, 0, packet, 1, data.length);
        return packet;
    }

    // Helper methods
    private void openAccessory(Context context)
    {
        Log.d(MeerkatApplication.TAG, "UsbController.openAccessory");
        UsbManager usbManager = (UsbManager)context.getSystemService(Context.USB_SERVICE);
        UsbAccessory[] accessoryList = usbManager.getAccessoryList();
        if (accessoryList != null && accessoryList.length > 0) {
            mFileDescriptor = usbManager.openAccessory(accessoryList[0]);
            if (mFileDescriptor != null) {
                FileDescriptor fd = mFileDescriptor.getFileDescriptor();
                mOutputStream = new FileOutputStream(fd);
                mInputStream = new FileInputStream(fd);
            }
            else {
                mActive = false;
                Log.d(MeerkatApplication.TAG, "UsbController.openAccessory: Failed to open accessory: 1");
            }
        }
        else {
            mActive = false;
            Log.d(MeerkatApplication.TAG, "UsbController.openAccessory: Failed to open accessory: 2");
        }
        mActive = true;
        Log.d(MeerkatApplication.TAG, "UsbController.openAccessory");
    }

    private void closeAccessory()
    {
        Log.d(MeerkatApplication.TAG, "UsbController.closeAccessory");
        try {
            if (mOutputStream != null) {
                mOutputStream.close();
            }
            if (mInputStream != null) {
                mInputStream.close();
            }
            if (mFileDescriptor != null) {
                mFileDescriptor.close();
            }
        }
        catch(IOException ex) {
            Log.d(MeerkatApplication.TAG, "UsbController: Error closing streams: " + ex);
        }
        mActive = false;
        Log.d(MeerkatApplication.TAG, "UsbController.closeAccessory");
    }
}


