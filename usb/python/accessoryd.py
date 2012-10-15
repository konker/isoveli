#!/usr/bin/env python
#
# accessoryd.py
#
# Copyright 2012 Konrad Markus
#
# Author: Konrad Markus <konker@gmail.com>
#


"""
TODO:
    - logging
    - data format
        - data channels?
    - data persistence/streaming
"""
import sys

if sys.version < '2.5':                                                               
    sys.exit('Python 2.5 or 2.6 is required.')  

import os
import time
import logging
import usb
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
        self.pidfile_path =  'accessoryd.pid'
        self.pidfile_timeout = 5


    def run(self):
        self.setup()

        # Main loop
        while(True):
            try:
                data = self.epIn.read(self.epIn.wMaxPacketSize, timeout=0)
                print data.tostring()
            except usb.core.USBError as ex1:
                # [TODO: could we try to re-connect here?]
                logging.error("USB connection error: %s. Exiting." % ex1)
                exit(100)
            except Exception as ex2:
                # [TODO: what should happen here?]
                #logging.debug("SLEEPING %s" % ex)
                #time.sleep(1)
                logging.error("Error with I/O: %s. Exiting." % ex2)
                exit(101)


    def setup(self):
        self.connect_device(config['device']['vid'], config['device']['pid'])
        if self.device is not None:
            logging.info("Device %x:%x connected." % \
                (config['device']['vid'], config['device']['pid']))

            try:
                self.switch_device(config['accessory'])
                logging.info("Device switched to accessory mode.")
                time.sleep(1)
            except Exception as ex:
                logging.error("Could not switch device to accessory mode: %s. Exiting." % ex)
                exit(1)
        else:
            logging.error("Could not connect to device. Trying accessory...")

        self.connect_accessory()
        if self.device is not None:
            logging.info("Accessory connected with pid: %x." % self.accessory_pid)
        else:
            logging.error("Could not connect to accessory. Exiting.")
            exit(2)

        self.find_endpoint()
        if self.epIn is not None:
            logging.info("IN  endpoint: %s" % hex(self.epIn.bEndpointAddress))
        else:
            logging.error("Could not find IN endpoint. Exiting.")
            exit(3)

        logging.info("Setup complete.")


    def connect_device(self, vid, pid):
        try:
            self.device = usb.core.find(idVendor=vid, idProduct=pid)
        except ValueError:
            self.device = None


    def connect_accessory(self):
        try:
            self.device = usb.core.find(idVendor=USB_ACCESSORY_VENDOR_ID, idProduct=USB_ACCESSORY_ADB_PRODUCT_ID)
        except ValueError:
            self.device = None

        if self.device is not None:
            self.accessory_pid = USB_ACCESSORY_ADB_PRODUCT_ID
            return

        try:
            self.device = usb.core.find(idVendor=USB_ACCESSORY_VENDOR_ID, idProduct=USB_ACCESSORY_PRODUCT_ID)
        except ValueError:
            self.device = None

        if self.device is not None:
            self.accessory_pid = USB_ACCESSORY_PRODUCT_ID
            return


    def switch_device(self, accessory_config):
        self.protocol = self.get_protocol()

        if self.protocol < 1:
            raise "Compatable protocol not supported. Protocol: %d. " % protocol

        self.send_string(ACCESSORY_STRING_MANUFACTURER, accessory_config['manufacturer'])
        self.send_string(ACCESSORY_STRING_MODEL, accessory_config['model'])
        self.send_string(ACCESSORY_STRING_DESCRIPTION, accessory_config['description'])
        self.send_string(ACCESSORY_STRING_VERSION, accessory_config['version'])
        self.send_string(ACCESSORY_STRING_URI, accessory_config['uri'])
        self.send_string(ACCESSORY_STRING_SERIAL, accessory_config['serial'])

        self.start_accessory()


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


    def find_endpoint(self):
        configuration = self.device.get_active_configuration()
        interface = configuration[(0,0)]

        self.epIn = usb.util.find_descriptor(
            interface,
            custom_match = \
            lambda e: \
               usb.util.endpoint_direction(e.bEndpointAddress) == \
               usb.util.ENDPOINT_IN)


if __name__ == '__main__':

    # configure logging
    logfile = os.path.realpath(os.path.join(os.path.dirname(__file__), 'accessoryd.log'))
    logging.basicConfig(level=logging.DEBUG,
                        filename=logfile,
                        format='%(asctime)s [%(threadName)s] %(message)s',
                        datefmt='%Y-%m-%d %H:%M:%S')

    # Non daemon version
    try:
        accessory = Accessory()
        accessory.run()
    except KeyboardInterrupt:
        # [FIXME: won't exit here until receives next input?]
        logging.info("Keyboard interrupt. Exiting.")
        exit(-1)
    except Exception as ex1:
        loggin.error("Error running accessory: %s. Exiting." % ex1)
        exit(-2)
    """

    try:
        accessory = Accessory()
        daemon_runner = runner.DaemonRunner(accessory)
        daemon_runner.do_action()
    except runner.DaemonRunnerStopFailureError as ex1:
        logging.error("Could not stop: %s." % ex1)
        exit(-1)
    except Exception as ex2:
        logging.error("Error running accessory: %s. Exiting." % ex2)
        exit(-2)
    """

