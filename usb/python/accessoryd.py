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
    - separate config file?
        - json doesn't allow hex literals?
        - yaml?
            - requires another dependency
    - data format
        - data channels?
    - data persistence/streaming
"""
import sys

if sys.version < '2.5':                                                               
    sys.exit('Python 2.5 or 2.6 is required.')  

import pathhack

import logging
from threading import Thread
import bottle
from accessory.accessory import Accessory
from daemon import runner

# NOTE: edit config.rb as appropriate
from config.config import config


accessory = None

#### BOTTLE
@bottle.route('/')
def index():
    return "<h1>Meerkat: %s</h1>" % accessory.stdout_path

if __name__ == '__main__':

    # configure logging
    logging.basicConfig(level=logging.DEBUG,
                        #filename=config['logfile'],
                        stream=sys.stdout,
                        format='%(asctime)s [%(threadName)s] %(message)s',
                        datefmt='%Y-%m-%d %H:%M:%S')

    if len(sys.argv) == 1 or sys.argv[1] == 'run':
        # Non daemon version
        try:
            """
            accessory = Accessory(config)
            accessory.setup()

            accessory_thread = Thread(target=accessory.run,
                                      name='accessory-thread')
            accessory_thread.setDaemon(True)
            accessory_thread.start()
            """

            http_thread = Thread(target=bottle.run,
                                kwargs=dict(host='', port='8080'),
                                name='http-thread')
            http_thread.setDaemon(True)
            http_thread.start()

            accessory = Accessory(config)
            accessory.setup()
            accessory.run()
        except KeyboardInterrupt:
            # [FIXME: won't exit here until receives next input?]
            logging.info("Keyboard interrupt. Exiting.")
            accessory.close()
            sys.exit(-1)
        except Exception as ex1:
            logging.error("Error running accessory: %s. Exiting." % ex1)
            accessory.close()
            sys.exit(-2)
    else:
        try:
            accessory = Accessory()
            daemon_runner = runner.DaemonRunner(accessory)
            daemon_runner.do_action()
        except runner.DaemonRunnerStopFailureError as ex1:
            logging.error("Could not stop: %s." % ex1)
            sys.exit(-1)
        except Exception as ex2:
            logging.error("Error running accessory: %s. Exiting." % ex2)
            accessory.close()
            sys.exit(-2)


