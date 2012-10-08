#
# Copyright 2009-2011 Oleg Mazurov, Circuits At Home, http://www.circuitsathome.com
# MAX3421E USB host controller support
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions
# are met:
# 1. Redistributions of source code must retain the above copyright
#    notice, this list of conditions and the following disclaimer.
# 2. Redistributions in binary form must reproduce the above copyright
#    notice, this list of conditions and the following disclaimer in the
#    documentation and/or other materials provided with the distribution.
# 3. Neither the name of the authors nor the names of its contributors
#    may be used to endorse or promote products derived from this software
#    without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
# ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
# ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
# FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
# DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
# OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
# HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
# LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
# OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
# SUCH DAMAGE.


# USB chapter 9 structures

# Misc.USB constants
DEV_DESCR_LEN    = 18      # device descriptor length
CONF_DESCR_LEN   = 9       # configuration descriptor length
INTR_DESCR_LEN   = 9       # interface descriptor length
EP_DESCR_LEN     = 7       # endpoint descriptor length

# Standard Device Requests
USB_REQUEST_GET_STATUS                   = 0       # Standard Device Request - GET STATUS
USB_REQUEST_CLEAR_FEATURE                = 1       # Standard Device Request - CLEAR FEATURE
USB_REQUEST_SET_FEATURE                  = 3       # Standard Device Request - SET FEATURE
USB_REQUEST_SET_ADDRESS                  = 5       # Standard Device Request - SET ADDRESS
USB_REQUEST_GET_DESCRIPTOR               = 6       # Standard Device Request - GET DESCRIPTOR
USB_REQUEST_SET_DESCRIPTOR               = 7       # Standard Device Request - SET DESCRIPTOR
USB_REQUEST_GET_CONFIGURATION            = 8       # Standard Device Request - GET CONFIGURATION
USB_REQUEST_SET_CONFIGURATION            = 9       # Standard Device Request - SET CONFIGURATION
USB_REQUEST_GET_INTERFACE                = 10      # Standard Device Request - GET INTERFACE
USB_REQUEST_SET_INTERFACE                = 11      # Standard Device Request - SET INTERFACE
USB_REQUEST_SYNCH_FRAME                  = 12      # Standard Device Request - SYNCH FRAME

USB_FEATURE_ENDPOINT_HALT                = 0       # CLEAR/SET FEATURE - Endpoint Halt
USB_FEATURE_DEVICE_REMOTE_WAKEUP         = 1       # CLEAR/SET FEATURE - Device remote wake-up
USB_FEATURE_TEST_MODE                    = 2       # CLEAR/SET FEATURE - Test mode

# Setup Data Constants
USB_SETUP_HOST_TO_DEVICE                 = 0x00    # Device Request bmRequestType transfer direction - host to device transfer
USB_SETUP_DEVICE_TO_HOST                 = 0x80    # Device Request bmRequestType transfer direction - device to host transfer
USB_SETUP_TYPE_STANDARD                  = 0x00    # Device Request bmRequestType type - standard
USB_SETUP_TYPE_CLASS                     = 0x20    # Device Request bmRequestType type - class
USB_SETUP_TYPE_VENDOR                    = 0x40    # Device Request bmRequestType type - vendor
USB_SETUP_RECIPIENT_DEVICE               = 0x00    # Device Request bmRequestType recipient - device
USB_SETUP_RECIPIENT_INTERFACE            = 0x01    # Device Request bmRequestType recipient - interface
USB_SETUP_RECIPIENT_ENDPOINT             = 0x02    # Device Request bmRequestType recipient - endpoint
USB_SETUP_RECIPIENT_OTHER                = 0x03    # Device Request bmRequestType recipient - other

# USB descriptors
USB_DESCRIPTOR_DEVICE            = 0x01    # bDescriptorType for a Device Descriptor.
USB_DESCRIPTOR_CONFIGURATION     = 0x02    # bDescriptorType for a Configuration Descriptor.
USB_DESCRIPTOR_STRING            = 0x03    # bDescriptorType for a String Descriptor.
USB_DESCRIPTOR_INTERFACE         = 0x04    # bDescriptorType for an Interface Descriptor.
USB_DESCRIPTOR_ENDPOINT          = 0x05    # bDescriptorType for an Endpoint Descriptor.
USB_DESCRIPTOR_DEVICE_QUALIFIER  = 0x06    # bDescriptorType for a Device Qualifier.
USB_DESCRIPTOR_OTHER_SPEED       = 0x07    # bDescriptorType for a Other Speed Configuration.
USB_DESCRIPTOR_INTERFACE_POWER   = 0x08    # bDescriptorType for Interface Power.
USB_DESCRIPTOR_OTG               = 0x09    # bDescriptorType for an OTG Descriptor.

# OTG SET FEATURE Constants
OTG_FEATURE_B_HNP_ENABLE                 = 3       # SET FEATURE OTG - Enable B device to perform HNP
OTG_FEATURE_A_HNP_SUPPORT                = 4       # SET FEATURE OTG - A device supports HNP
OTG_FEATURE_A_ALT_HNP_SUPPORT            = 5       # SET FEATURE OTG - Another port on the A device supports HNP

# USB Endpoint Transfer Types
USB_TRANSFER_TYPE_CONTROL                = 0x00    # Endpoint is a control endpoint.
USB_TRANSFER_TYPE_ISOCHRONOUS            = 0x01    # Endpoint is an isochronous endpoint.
USB_TRANSFER_TYPE_BULK                   = 0x02    # Endpoint is a bulk endpoint.
USB_TRANSFER_TYPE_INTERRUPT              = 0x03    # Endpoint is an interrupt endpoint.
bmUSB_TRANSFER_TYPE                      = 0x03    # bit mask to separate transfer type from ISO attributes


# Standard Feature Selectors for CLEAR_FEATURE Requests
USB_FEATURE_ENDPOINT_STALL               = 0       # Endpoint recipient
USB_FEATURE_DEVICE_REMOTE_WAKEUP         = 1       # Device recipient
USB_FEATURE_TEST_MODE                    = 2       # Device recipient

# HID constants. Not part of chapter 9
# Class-Specific Requests
HID_REQUEST_GET_REPORT       = 0x01
HID_REQUEST_GET_IDLE         = 0x02
HID_REQUEST_GET_PROTOCOL     = 0x03
HID_REQUEST_SET_REPORT       = 0x09
HID_REQUEST_SET_IDLE         = 0x0A
HID_REQUEST_SET_PROTOCOL     = 0x0B

# Class Descriptor Types
HID_DESCRIPTOR_HID       = 0x21
HID_DESCRIPTOR_REPORT    = 0x22
HID_DESRIPTOR_PHY        = 0x23

# Protocol Selection
BOOT_PROTOCOL    = 0x00
RPT_PROTOCOL     = 0x01
# HID Interface Class Code
HID_INTF                     = 0x03
# HID Interface Class SubClass Codes
BOOT_INTF_SUBCLASS           = 0x01
# HID Interface Class Protocol Codes
HID_PROTOCOL_NONE            = 0x00
HID_PROTOCOL_KEYBOARD        = 0x01
HID_PROTOCOL_MOUSE           = 0x02


