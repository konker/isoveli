#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
# meerkat.probes.bluetooth_scan
#
# Copyright 2012 Konrad Markus
#
# Author: Konrad Markus <konker@gmail.com>
#

import sys
from bluetooth import DeviceDiscoverer
import json

class JsonDeviceDiscoverer(DeviceDiscoverer, object):
    def pre_inquiry(self):
        self.results = []
    
    def device_discovered(self, address, device_class, name):
        self.results.append(dict(address=address, name=name, device_class=device_class))

    def inquiry_complete(self):
        sys.stdout.write(json.dumps(self.results))


def main():
    d = JsonDeviceDiscoverer()
    d.find_devices(lookup_names = True)
    d.process_inquiry()


if __name__ == '__main__':
    main()
