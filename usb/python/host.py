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
ACCESSORY_PID = 0x2D01
ACCESSORY_PID_ALT = 0x2D00
#define LEN 2
"""
USB_STATE_MASK                                    = 0xf0

USB_STATE_DETACHED                                = 0x10
USB_DETACHED_SUBSTATE_INITIALIZE                  = 0x11        
USB_DETACHED_SUBSTATE_WAIT_FOR_DEVICE             = 0x12
USB_DETACHED_SUBSTATE_ILLEGAL                     = 0x13
USB_ATTACHED_SUBSTATE_SETTLE                      = 0x20
USB_ATTACHED_SUBSTATE_RESET_DEVICE                = 0x30    
USB_ATTACHED_SUBSTATE_WAIT_RESET_COMPLETE         = 0x40
USB_ATTACHED_SUBSTATE_WAIT_SOF                    = 0x50
USB_ATTACHED_SUBSTATE_GET_DEVICE_DESCRIPTOR_SIZE  = 0x60
USB_STATE_ADDRESSING                              = 0x70
USB_STATE_CONFIGURING                             = 0x80
USB_STATE_RUNNING                                 = 0x90
USB_STATE_ERROR                                   = 0xa0


USB_ACCESSORY_VENDOR_ID       = 0x18D1
USB_ACCESSORY_PRODUCT_ID      = 0x2D00
USB_ACCESSORY_ADB_PRODUCT_ID  = 0x2D01

ACCESSORY_STRING_MANUFACTURER = 0
ACCESSORY_STRING_MODEL        = 1
ACCESSORY_STRING_DESCRIPTION  = 2
ACCESSORY_STRING_VERSION      = 3
ACCESSORY_STRING_URI          = 4
ACCESSORY_STRING_SERIAL       = 5

ACCESSORY_GET_PROTOCOL        = 51
ACCESSORY_SEND_STRING         = 52
ACCESSORY_START               = 53

def main():
    """
    Act as a virtual USB accessory, such that when plugged into a USB 
    host, can be accessed via USB.
    """
    usb_handler = UsbHandler(0x04e8, 0x681c)
    accessory = AndroidAccessory(usb_handler, 'HIIT', 'DataSink', 'DataSink DESCRIPTION', '1.0', 'http://www.hiit.fi/', '0466667901')


class UsbHandler(object):
    def __init__(self, vendor_id, product_id):
        self.vendor_id = vendor_id
        self.product_id = product_id

        """
        self.device = self.device = usb.core.find(idVendor=vendor_id, idProduct=product_id)
        if (self.device == None):
            print "ERROR: Device not found"
            exit()

        if self.device.is_kernel_driver_active(0) is True:
            self.device.detach_kernel_driver(0)
        if self.device.is_kernel_driver_active(1) is True:
            self.device.detach_kernel_driver(1)
        if self.device.is_kernel_driver_active(2) is True:
            self.device.detach_kernel_driver(2)
        if self.device.is_kernel_driver_active(3) is True:
            self.device.detach_kernel_driver(3)

        #self.device.set_configuration()
        """

        print "UsbHandler.__init__"


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

        self.foo()
        print "AndroidAccessory.__init__"

    def get_protocol(self):
        #self.usb_handler.device.ctrl_transfer(0x21, 0x09, 0x81, 0, INIT_PACKET1, 100)
        protocol = self.usb_handler.device.ctrl_transfer(USB_SETUP_DEVICE_TO_HOST |
                                                         USB_SETUP_TYPE_VENDOR |
                                                         USB_SETUP_RECIPIENT_DEVICE,
                                                         ACCESSORY_GET_PROTOCOL, 0, 0)
        print USB_SETUP_DEVICE_TO_HOST | USB_SETUP_TYPE_VENDOR | USB_SETUP_RECIPIENT_DEVICE
        return protocol


    def send_string(self, index, s):
        self.usb_handler.device.ctrl_transfer(USB_SETUP_HOST_TO_DEVICE |
                                              USB_SETUP_TYPE_VENDOR |
                                              USB_SETUP_RECIPIENT_DEVICE,
                                              ACCESSORY_SEND_STRING, 0, 0, index, s)

    def switch_device(self):
        protocol = self.get_protocol()
        print "PROTOCOL: ", protocol

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

        usb.util.claim_interface(dev, intf)
        print "KONK"
        print usb.util.build_request_type(usb.util.CTRL_IN, usb.util.CTRL_TYPE_STANDARD, usb.util.CTRL_RECIPIENT_INTERFACE)
        print usb.control.get_interface(dev, 1)
        print usb.control.get_status(dev, intf)
        print usb.control.get_configuration(dev)

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
        #print "Version Code Device:", devVersion
        #time.sleep(1)   #sometimes hangs on the next transfer :(

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
            data = ep_in.read(16)
            print data
        except usb.USBError:
            print "USB error"
            del dev
            del cfg
            del intf
            del ep_in
            del ep_out
            raise

        del dev
        del cfg
        del intf
        del ep_in
        del ep_out

if __name__ == '__main__':
    main()

