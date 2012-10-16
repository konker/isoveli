#
# logger.logger
#
# Copyright 2012 Konrad Markus
#
# Author: Konrad Markus <konker@gmail.com>
#

import sys
import logging

RECORD_SEP = "\n"

class Logger():
    def __init__(self, filename, autoflush=True):
        self.autoflush = autoflush
        try:
            self.fd = open(filename, 'a')
        except IOError as ex:
            logging.error("Logger: Could not open file %s: %s" % (filename, ex))
            raise ex


    def write_record(self, array):
        array.tofile(self.fd)
        self.fd.write(RECORD_SEP)
        if self.autoflush:
            self.fd.flush()


    def write_str_record(self, s):
        self.fd.write(s)
        self.fd.write(RECORD_SEP)
        if self.autoflush:
            self.fd.flush()


    def write_str(self, s):
        self.fd.write(s)
        if self.autoflush:
            self.fd.flush()


    def close(self):
        self.fd.close()

