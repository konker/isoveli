#!/usr/bin/env python
#
# reader
#
# Copyright 2012 Konrad Markus
#
# Author: Konrad Markus <konker@gmail.com>
#

from storage.sqlite import Storage


def main():
    storage = Storage('data/accessoryd.db')
    print list(storage.reader())


if __name__ == '__main__':
    main()
