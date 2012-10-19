package fi.hiit.meerkat.dto;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothClass;

/**
 */
public class BluetoothScanResult
{
    //[TODO: should these be private with getters/setters?]
    public String mAddress;
    public String mName;
    public int mMajorDeviceClass;
    public int mDeviceClass;

    public BluetoothScanResult(BluetoothDevice device)
    {
        mAddress = device.getAddress();
        mName = device.getName();

        BluetoothClass bluetoothClass = device.getBluetoothClass();
        mMajorDeviceClass = bluetoothClass.getMajorDeviceClass();
        mDeviceClass = bluetoothClass.getDeviceClass();
    }
}


