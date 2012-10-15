#!/usr/bin/env ruby
#
# host.rb
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

require "libusb"
require "yaml"

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


def main()
  # read in config file
  config = begin
    YAML::load_file(File.join(File.dirname(File.expand_path(__FILE__)), 'config.yml'))

  rescue
    puts "Could not read config.yml. Exiting."
    exit(1)
  end

  puts config['device']
  handle = connect_device(config['device']['vid'], config['device']['pid'])

  if not handle.nil?
    switch_device(handle, config['accessory'])
    sleep(1)
  end

  handle = connect_accessory()
  if handle.nil?
    puts "Accessory device is None. Exiting."
    exit(5)
  end

  ep_in, ep_out = find_endpoints(handle)
  begin
    data = handle.read(ep_in.bEndpointAddress, 512, timeout=10000)
    puts data
  rescue
    puts "Error reading: ", ex, " Exiting."
    exit(6)
  end
end

def connect_device(vid, pid)
  usb = LIBUSB::Context.new
  usb.devices(:idVendor => vid, :idProduct => pid).first
end

def switch_device(handle, accessory_info)
  begin
    protocol = get_protocol(handle)

    if protocol < 1:
        raise "Protocol not supported: ", protocol
    
    print "Device supports protocol: ", protocol
    sleep(1)

    send_string(handle, ACCESSORY_STRING_MANUFACTURER, accessory_info['manufacturer'])
    send_string(handle, ACCESSORY_STRING_MODEL, accessory_info['model'])
    send_string(handle, ACCESSORY_STRING_DESCRIPTION, accessory_info['description'])
    send_string(handle, ACCESSORY_STRING_VERSION, accessory_info['version'])
    send_string(handle, ACCESSORY_STRING_URI, accessory_info['uri'])
    send_string(handle, ACCESSORY_STRING_SERIAL, accessory_info['serial'])

    start_accessory(handle)
  except Exception as ex:
      print "Could switch device: ", ex, " Exiting."
      exit(3)

  print "Accessory started"
end

def get_protocol(handle)
    bmRequestType = USB_SETUP_DEVICE_TO_HOST | USB_SETUP_TYPE_VENDOR | USB_SETUP_RECIPIENT_DEVICE
    bRequest = ACCESSORY_GET_PROTOCOL
    wLength = 2
    msg = handle.ctrl_transfer(bmRequestType, bRequest, 0, 0, wLength)  #Read 2 bytes

    return  msg[1] << 8 | msg[0];
end

def send_string(handle, index, string)
end

def start_accessory(handle)
end

def connect_accessory()
  nil
end

def find_endpoints(device)
  return nil, nil
end


# run the program
main()






