#!/usr/bin/env python
#
# host_simple.py
#
# Copyright 2012 Konrad Markus
#
# Author: Konrad Markus <konker@gmail.com>
#

# [TODO: change name from host_simple ]

import usb
import time
from daemon import runner

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


class Accessory():
    def __init__(self):
        self.stdin_path = '/dev/null'
        self.stdout_path = '/dev/tty'
        self.stderr_path = '/dev/tty'
        self.pidfile_path =  '/tmp/foo.pid'
        self.pidfile_timeout = 5


    def run(self):
        print "RUN"
        print "---"

        # Main loop
        while(True):
            try:
                data = self.device.read(self.ep_in.bEndpointAddress, self.ep_in.wMaxPacketSize, timeout=0)
                print data.tostring()
            except Exception as ex:
                # [TODO: what should happen here?]
                print "SLEEPING"
                pass


    def setup(self):
        self.connect_device(config['device']['vid'], config['device']['pid'])
        if self.device is not None:
            self.switch_device(config['accessory'])
            time.sleep(1)

        self.connect_accessory()
        if self.device is None:
            print "Could not connect to accessory device. Exiting."
            exit(5)

        self.find_endpoints()
        print "Setup complete."


    def find_endpoints(self):
        cfg = self.device.get_active_configuration()
        interface_number = cfg[(0,0)].bInterfaceNumber
        alternate_setting = usb.control.get_interface(self.device, interface_number)
        intf = usb.util.find_descriptor(
            cfg,
            bInterfaceNumber = interface_number,
            bAlternateSetting = alternate_setting
        )

        self.ep_in = usb.util.find_descriptor(
                             intf,
                             custom_match = \
                             lambda e: \
                                usb.util.endpoint_direction(e.bEndpointAddress) == \
                                usb.util.ENDPOINT_IN)

        self.ep_out = usb.util.find_descriptor(
                             intf,
                             custom_match = \
                             lambda e: \
                                usb.util.endpoint_direction(e.bEndpointAddress) == \
                                usb.util.ENDPOINT_OUT)

        assert self.ep_out is not None
        assert self.ep_in is not None
        print "OUT ep: %s" % hex(self.ep_out.bEndpointAddress)
        print "IN  ep: %s" % hex(self.ep_in.bEndpointAddress)


    def connect_device(self, vid, pid):
        try:
            self.device = usb.core.find(idVendor=vid, idProduct=pid)
        except ValueError:
            self.device = None
            print "Device %s:%s device not found." % (vid, pid)

        print "Device %s:%s connected." % (vid, pid)


    def connect_accessory(self):
        try:
            self.device = usb.core.find(idVendor=USB_ACCESSORY_VENDOR_ID, idProduct=USB_ACCESSORY_ADB_PRODUCT_ID)
            print "ADB accessory connected."
            return
        except ValueError:
            print "ADB accessory not found."

        try:
            self.device = usb.core.find(idVendor=USB_ACCESSORY_VENDOR_ID, idProduct=USB_ACCESSORY_PRODUCT_ID)
            print "Non-ADB accessory connected."
            return
        except ValueError:
            print "Non-ADB accessory not found."
            self.device = None


    def switch_device(self, accessory_config):
        try:
            protocol = self.get_protocol()

            if protocol < 1:
                raise "Protocol not supported: ", protocol
            print "Device supports protocol: ", protocol

            self.send_string(ACCESSORY_STRING_MANUFACTURER,
                        accessory_config['manufacturer'])

            self.send_string(ACCESSORY_STRING_MODEL,
                        accessory_config['model'])

            self.send_string(ACCESSORY_STRING_DESCRIPTION,
                        accessory_config['description'])

            self.send_string(ACCESSORY_STRING_VERSION,
                        accessory_config['version'])

            self.send_string(ACCESSORY_STRING_URI,
                        accessory_config['uri'])

            self.send_string(ACCESSORY_STRING_SERIAL,
                                accessory_config['serial'])

            self.start_accessory()
            print "Accessory switched."
        except Exception as ex:
            print "Could switch device: %s. Exiting." % ex
            exit(3)


    def get_protocol(self):
        bmRequestType = USB_SETUP_DEVICE_TO_HOST | USB_SETUP_TYPE_VENDOR | USB_SETUP_RECIPIENT_DEVICE
        bRequest = ACCESSORY_GET_PROTOCOL
        wLength = 2
        msg = self.device.ctrl_transfer(bmRequestType, bRequest, 0, 0, wLength)  #Read 2 bytes

        return  msg[1] << 8 | msg[0];


    def send_string(self, index, s):
        bmRequestType = USB_SETUP_HOST_TO_DEVICE | USB_SETUP_TYPE_VENDOR | USB_SETUP_RECIPIENT_DEVICE
        bRequest = ACCESSORY_SEND_STRING
        assert self.device.ctrl_transfer(bmRequestType, bRequest, 0, index, s) == len(s)


    def start_accessory(self):
        bmRequestType = USB_SETUP_HOST_TO_DEVICE | USB_SETUP_TYPE_VENDOR | USB_SETUP_RECIPIENT_DEVICE
        bRequest = ACCESSORY_START
        self.device.ctrl_transfer(bmRequestType, bRequest, 0, 0)
    

if __name__ == '__main__':
    try:
        accessory = Accessory()
        accessory.setup()
        daemon_runner = daemon.runner.DaemonRunner(accessory)
        daemon_runner.do_action()
    except KeyboardInterrupt:
        print "Exiting."
        exit()

