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

import usb
import time

# NOTE: edit config.rb as appropriate
from config import config

# USB constants
USB_SETUP_HOST_TO_DEVICE      = 0x00
USB_SETUP_DEVICE_TO_HOST      = 0x80
USB_SETUP_TYPE_VENDOR         = 0x40
USB_SETUP_RECIPIENT_DEVICE    = 0x00

# Android accessory mode USB constants
ACCESSORY_STRING_MANUFACTURER = 0
ACCESSORY_STRING_MODEL        = 1
ACCESSORY_STRING_DESCRIPTION  = 2
ACCESSORY_STRING_VERSION      = 3
ACCESSORY_STRING_URI          = 4
ACCESSORY_STRING_SERIAL       = 5
ACCESSORY_GET_PROTOCOL        = 51
ACCESSORY_SEND_STRING         = 52
ACCESSORY_START               = 53

# Android accessory id constants
USB_ACCESSORY_VENDOR_ID       = 0x18D1
USB_ACCESSORY_PRODUCT_ID      = 0x2D00
USB_ACCESSORY_ADB_PRODUCT_ID  = 0x2D01


def main():
    device = connect_device(config['device']['vid'], config['device']['pid'])

    if device is not None:
        switch_device(device, config['accessory'])
        time.sleep(1)

    device = connect_accessory()
    if device is None:
        print "Could not connect to accessory device. Exiting."
        exit(5)

    ep_in, ep_out = find_endpoints(device)
    try:
        data = device.read(ep_in.bEndpointAddress, 512, timeout=10000)
        print data.tostring()
    except Exception as ex:
        print "Error reading: %s. Exiting." % ex
        exit(6)


def find_endpoints(device):
    cfg = device.get_active_configuration()
    interface_number = cfg[(0,0)].bInterfaceNumber
    alternate_setting = usb.control.get_interface(device, interface_number)
    intf = usb.util.find_descriptor(
        cfg,
        bInterfaceNumber = interface_number,
        bAlternateSetting = alternate_setting
    )

    ep_in = usb.util.find_descriptor(
                         intf,
                         custom_match = \
                         lambda e: \
                            usb.util.endpoint_direction(e.bEndpointAddress) == \
                            usb.util.ENDPOINT_IN)

    ep_out = usb.util.find_descriptor(
                         intf,
                         custom_match = \
                         lambda e: \
                            usb.util.endpoint_direction(e.bEndpointAddress) == \
                            usb.util.ENDPOINT_OUT)

    assert ep_out is not None
    assert ep_in is not None
    print "OUT ep: %s" % hex(ep_out.bEndpointAddress)
    print "IN  ep: %s" % hex(ep_in.bEndpointAddress)

    return ep_in, ep_out


def connect_device(vid, pid):
    try:
        device = usb.core.find(idVendor=vid, idProduct=pid)
    except ValueError:
        print "Device %s:%s device not found." % (vid, pid)

    print "Device %s:%s connected." % (vid, pid)
    return device


def connect_accessory():
    try:
        device = usb.core.find(idVendor=USB_ACCESSORY_VENDOR_ID, idProduct=USB_ACCESSORY_ADB_PRODUCT_ID)
        print "ADB accessory connected."
        return device
    except ValueError:
        print "ADB accessory not found."

    try:
        device = usb.core.find(idVendor=USB_ACCESSORY_VENDOR_ID, idProduct=USB_ACCESSORY_PRODUCT_ID)
        print "Non-ADB accessory connected."
        return device
    except ValueError:
        print "Non-ADB accessory not found. Exiting."
        exit(4)


def switch_device(device, accessory_config):
    try:
        protocol = get_protocol(device)

        if protocol < 1:
            raise "Protocol not supported: ", protocol
        print "Device supports protocol: ", protocol

        send_string(device, ACCESSORY_STRING_MANUFACTURER,
                            accessory_config['manufacturer'])

        send_string(device, ACCESSORY_STRING_MODEL,
                            accessory_config['model'])

        send_string(device, ACCESSORY_STRING_DESCRIPTION,
                            accessory_config['description'])

        send_string(device, ACCESSORY_STRING_VERSION,
                            accessory_config['version'])

        send_string(device, ACCESSORY_STRING_URI,
                            accessory_config['uri'])

        send_string(device, ACCESSORY_STRING_SERIAL,
                            accessory_config['serial'])

        start_accessory(device)
        print "Accessory switched."
    except Exception as ex:
        print "Could switch device: %s. Exiting." % ex
        exit(3)


def get_protocol(device):
    bmRequestType = USB_SETUP_DEVICE_TO_HOST | USB_SETUP_TYPE_VENDOR | USB_SETUP_RECIPIENT_DEVICE
    bRequest = ACCESSORY_GET_PROTOCOL
    wLength = 2
    msg = device.ctrl_transfer(bmRequestType, bRequest, 0, 0, wLength)  #Read 2 bytes

    return  msg[1] << 8 | msg[0];


def send_string(device, index, s):
    bmRequestType = USB_SETUP_HOST_TO_DEVICE | USB_SETUP_TYPE_VENDOR | USB_SETUP_RECIPIENT_DEVICE
    bRequest = ACCESSORY_SEND_STRING
    assert device.ctrl_transfer(bmRequestType, bRequest, 0, index, s) == len(s)


def start_accessory(device):
    bmRequestType = USB_SETUP_HOST_TO_DEVICE | USB_SETUP_TYPE_VENDOR | USB_SETUP_RECIPIENT_DEVICE
    bRequest = ACCESSORY_START
    device.ctrl_transfer(bmRequestType, bRequest, 0, 0)
    

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print "Exiting."
        exit()

