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


IN = 0x85
OUT = 0x07
VID = 0x18D1
PID = 0x4E22
ACCESSORY_PID = 0x2D01
ACCESSORY_PID_ALT = 0x2D00
#define LEN 2

MANUFACTURER = 'Morningwood Software'
MODEL = 'SoftUSB'
DESCRIPTION = 'SoftUSB DESCRIPTION'
VERSION_NAME = 'SoftUSB VersionName'
URL = 'http://morningwoodsoftware.com/'
SERIAL_NUMBER = '0466667901'


def main():
    """
    Act as a virtual USB accessory, such that when plugged into a USB 
    host, can be accessed via USB.
    """
    print "USB accessory"
    init()
    setup_accessory(MANUFACTURER, MODEL, DESCRIPTION, VERSION_NAME, URL, SERIAL_NUMBER)
    main_loop()


def main_loop():
    print "MAIN_LOOP"

def init():
    print "INIT"

def setup_accessory(manufacturer, model, description, version_name, url, serial_number):
    print "SETUP_ACCESSORY"



if __name__ == '__main__':
    main()

