#!/usr/bin/env python
#
# accessory.py
#
# Copyright 2012 Konrad Markus
#
# Author: Konrad Markus <konker@gmail.com>
#

# Permission is hereby granted, free of charge, to any person
# obtaining a copy of this software and associated documentation files
# (the "Software"), to deal in the Software without restriction,
# including without limitation the rights to use, copy, modify, merge,
# publish, distribute, sublicense, and/or sell copies of the Software,
# and to permit persons to whom the Software is furnished to do so,
# subject to the following conditions:
#
# The above copyright notice and this permission notice shall be
# included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
# IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
# CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
# TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
# SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

from ch9 import *
import usb.core
import usb.util
import usb.control
import sys
import time

"""
IN = 0x85
OUT = 0x07
VID = 0x18D1
PID = 0x4E22
#define LEN 2
"""

USB_ACCESSORY_VENDOR_ID       = 0x18D1
USB_ACCESSORY_PRODUCT_ID      = 0x2D00
USB_ACCESSORY_ADB_PRODUCT_ID  = 0x2D01
USB_REQUEST_SET_CONFIGURATION = 0x09    # Standard Device Request - SET CONFIGURATION


ACCESSORY_STRING_MANUFACTURER = 0
ACCESSORY_STRING_MODEL        = 1
ACCESSORY_STRING_DESCRIPTION  = 2
ACCESSORY_STRING_VERSION      = 3
ACCESSORY_STRING_URI          = 4
ACCESSORY_STRING_SERIAL       = 5

ACCESSORY_GET_PROTOCOL        = 51
ACCESSORY_SEND_STRING         = 52
ACCESSORY_START               = 53
ACCESSORY_STOP                = 59

def main():
    """
    Act as a virtual USB accessory, such that when plugged into a USB 
    host, can be accessed via USB.
    """
    #usb_handler = UsbHandler(0x04e8, 0x681c)
    vendor_id = USB_ACCESSORY_VENDOR_ID
    product_id = 0x4e22
    try:
        usb_handler = UsbHandler(vendor_id, product_id)
        accessory = AndroidAccessory(usb_handler, 'HIIT', 'DataSink', 'DataSink DESCRIPTION', '1.0', 'http://www.hiit.fi/', '0466667901')
    except ValueError:
        print "Normal device not found."
        try:
            usb_handler = UsbHandler(vendor_id, USB_ACCESSORY_ADB_PRODUCT_ID, True)
        except ValueError:
            print "Accessory device not found. Exiting."
            exit(1)

        """
        while(True):
            try:
                data = usb_handler.ep_out.read(128)
                print data
            except Exception as ex:
                print ex
        """

        while(raw_input("--> ")):
            s = raw_input('r/w: ')
            try:
                if (s == 'r'):
                    data = usb_handler.ep_out.read(128)
                elif (s == 'w'):
                    data = usb_handler.ep_in.write("KONKERSKI")
                else:
                    usb_handler = UsbHandler(vendor_id, USB_ACCESSORY_ADB_PRODUCT_ID)
                print data
            except Exception as ex:
                print ex
            except KeyboardInterrupt:
                print "Exiting."
                accessory.stop_accessory()
                exit()


class UsbHandler(object):
    def __init__(self, vendor_id, product_id, set_conf=False):
        self.vendor_id = vendor_id
        self.product_id = product_id

        self.device = usb.core.find(idVendor=self.vendor_id, idProduct=self.product_id)
        if self.device is None:
            raise ValueError('Device not found')

        try:
            if self.device.is_kernel_driver_active(1) is True:
                self.device.detach_kernel_driver(1)
        except:
            pass

        if set_conf and False:
            self.set_configuration(1)

        self.find_endpoints()
        print "UsbHandler.__init__"


    def set_configuration(self, value):
        bmRequestType = USB_SETUP_HOST_TO_DEVICE | USB_SETUP_TYPE_STANDARD | USB_SETUP_RECIPIENT_DEVICE
        bRequest = USB_REQUEST_SET_CONFIGURATION
        self.device.ctrl_transfer(bmRequestType, bRequest, 0, value)


    def find_endpoints(self):
        cfg = self.device.get_active_configuration()
        intf = cfg[(0,0)]

        self.ep_in = usb.util.find_descriptor(intf,
                                         custom_match = lambda e: \
                                         usb.util.endpoint_direction(e.bEndpointAddress) == \
                                         usb.util.ENDPOINT_IN)

        self.ep_out = usb.util.find_descriptor(intf,
                                          custom_match = lambda e: \
                                          usb.util.endpoint_direction(e.bEndpointAddress) == \
                                          usb.util.ENDPOINT_OUT)

        assert self.ep_out is not None
        assert self.ep_in is not None
        print "OUT ep addr is ", hex(self.ep_out.bEndpointAddress)
        print "IN  ep addr is ", hex(self.ep_in.bEndpointAddress)


class AndroidAccessory(object):
    """
    """
    def __init__(self, usb_handler, manufacturer, model, description, version_name, uri, serial):
        self.usb_handler = usb_handler
        self.manufacturer = manufacturer
        self.model = model
        self.description = description
        self.version = version_name
        self.uri = uri
        self.serial = serial

        self.switch_device()
        print "AndroidAccessory.__init__"


    def connect_accessory(self):
        self.usb_handler = UsbHandler(self.usb_handler.vendor_id, ACCESSORY_PID)
        print "Accessory connected"


    def switch_device(self):
        protocol = self.get_protocol()

        if protocol < 1:
            raise "Protocol not supported: ", protocol
        
        print "Device supports protocol: ", protocol

        self.send_string(ACCESSORY_STRING_MANUFACTURER, self.manufacturer)
        self.send_string(ACCESSORY_STRING_MODEL, self.model)
        self.send_string(ACCESSORY_STRING_DESCRIPTION, self.description)
        self.send_string(ACCESSORY_STRING_VERSION, self.version)
        self.send_string(ACCESSORY_STRING_URI, self.uri)
        self.send_string(ACCESSORY_STRING_SERIAL, self.serial)

        self.start_accessory()
        print "Accessory started"


    def get_protocol(self):
        bmRequestType = USB_SETUP_DEVICE_TO_HOST | USB_SETUP_TYPE_VENDOR | USB_SETUP_RECIPIENT_DEVICE
        bRequest = ACCESSORY_GET_PROTOCOL
        wLength = 2
        msg = self.usb_handler.device.ctrl_transfer(bmRequestType, bRequest, 0, 0, wLength)  #Read 2 bytes

        return  msg[1] << 8 | msg[0];


    def start_accessory(self):
        bmRequestType = USB_SETUP_HOST_TO_DEVICE | USB_SETUP_TYPE_VENDOR | USB_SETUP_RECIPIENT_DEVICE
        bRequest = ACCESSORY_START
        self.usb_handler.device.ctrl_transfer(bmRequestType, bRequest, 0, 0)


    def send_string(self, index, s):
        bmRequestType = USB_SETUP_HOST_TO_DEVICE | USB_SETUP_TYPE_VENDOR | USB_SETUP_RECIPIENT_DEVICE
        bRequest = ACCESSORY_SEND_STRING
        assert self.usb_handler.device.ctrl_transfer(bmRequestType, bRequest, 0, index, s) == len(s)


"""
    def foo(self):
        dev = usb.core.find(idVendor=self.usb_handler.vendor_id, idProduct=self.usb_handler.product_id)
        if dev is None:
            raise ValueError('Device not found')

        if dev.is_kernel_driver_active(1) is True:
            dev.detach_kernel_driver(1)

        #This crashes?
        #dev.set_configuration()
        #cfg = usb.util.find_descriptor(dev, 1)[0]
        cfg = dev.get_active_configuration()
        print dir(cfg)

        intf = cfg[(1,0)]
        for i in intf:
            print i

        #usb.util.claim_interface(dev, intf)
        #print "KONK"
        #print usb.util.build_request_type(usb.util.CTRL_IN, usb.util.CTRL_TYPE_STANDARD, usb.util.CTRL_RECIPIENT_INTERFACE)
        #print usb.control.get_interface(dev, 1)
        #print usb.control.get_status(dev, intf)
        #print usb.control.get_configuration(dev)

        ep_in = usb.util.find_descriptor(intf,
                                         custom_match = lambda e: \
                                         usb.util.endpoint_direction(e.bEndpointAddress) == \
                                         usb.util.ENDPOINT_IN)

        ep_out = usb.util.find_descriptor(intf,
                                          custom_match = lambda e: \
                                          usb.util.endpoint_direction(e.bEndpointAddress) == \
                                          usb.util.ENDPOINT_OUT)

        assert ep_out is not None
        assert ep_in is not None

        #verify endpoint addresses
        print "OUT ep addr is ", hex(ep_out.bEndpointAddress)
        print "IN  ep addr is ", hex(ep_in.bEndpointAddress)

        #do ctrl transfers to setup accessory mode
        bmRequestType = USB_SETUP_DEVICE_TO_HOST | USB_SETUP_TYPE_VENDOR | USB_SETUP_RECIPIENT_DEVICE
        bRequest = ACCESSORY_GET_PROTOCOL
        wLength = 2
        msg = dev.ctrl_transfer(bmRequestType, bRequest, 0, 0, wLength)  #Read 2 bytes
        #msg = dev.ctrl_transfer(0x80, 0)

        devVersion = msg[1] << 8 | msg[0];
        print "Version Code Device:", devVersion
        time.sleep(1)   #sometimes hangs on the next transfer :(

        exit(0)
        #print ep_out.read(16)
        # these are based onlibusb_control_transfer(handle,0x40,52,0,0,(char*)manufacturer,strlen(manufacturer),0);
        assert dev.ctrl_transfer(0x40, 52, 0, 0, self.manufacturer) == len(manufacturer)
        assert dev.ctrl_transfer(0x40, 52, 0, 1, self.model) == len(model)
        assert dev.ctrl_transfer(0x40, 52, 0, 2, self.description) == len(description)
        assert dev.ctrl_transfer(0x40, 52, 0, 3, self.version) == len(version)
        assert dev.ctrl_transfer(0x40, 52, 0, 4, self.uri) == len(uri)
        assert dev.ctrl_transfer(0x40, 52, 0, 5, self.serial) == len(serial)

        print "Accessory Identification sent", devVersion

        assert dev.ctrl_transfer(0x40, 53, 0, 0, "") == 0

        print "Put device into accessory mode", devVersion
        try:
            data = ep_out.read(16)
            print data
        except usb.USBError:
            print "USB error"
"""

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print "Exiting."
        exit()

