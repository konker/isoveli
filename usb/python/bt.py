#!/usr/bin/env python
#
# bluetooth.py
#
# Copyright 2012 Konrad Markus
#
# Author: Konrad Markus <konker@gmail.com>
#

import bluetooth
import select

# http://people.csail.mit.edu/albert/bluez-intro/x339.html
class Discoverer(bluetooth.DeviceDiscoverer):
    
    def pre_inquiry(self):
        self.done = False
    
    def device_discovered(self, address, device_class, name):
        print "%s: %s, %s" % (address, name, device_class)

    def inquiry_complete(self):
        self.done = True

def main():
    d = Discoverer()
    d.find_devices(lookup_names = True)

    readfiles = [ d, ]
    while True:
        rfds = select.select( readfiles, [], [] )[0]

        if d in rfds:
            d.process_event()

        if d.done: break

"""
class Bluetooth(bluetooth.DeviceDiscoverer):
    def device_discovered(self, address, device_class, name):
        print "%s, %s, %s" % (address, name, device_class)

def main():
    b = Bluetooth()
    b.find_devices()
    #devices = bluetooth.discover_devices(lookup_names=True)
    #print devices
    
    #for bdaddr in devices:
    #    print bluetooth.lookup_name( bdaddr )
"""

if __name__ == '__main__':
    main()
