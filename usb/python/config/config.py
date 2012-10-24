import os

config = {
    # paths relative from this config file
    "pidfile": os.path.realpath(os.path.join(os.path.dirname(__file__),  '..', 'var', 'accessoryd.pid')),
    "logfile": os.path.realpath(os.path.join(os.path.dirname(__file__),  '..', 'log', 'accessoryd.log')),
    "datafile": os.path.realpath(os.path.join(os.path.dirname(__file__), '..', 'data', 'accessoryd.db')),

    "device": {
        "vid": 0x18D1,
        "pid": 0x4E22
    },
    "accessory": {
        "manufacturer": "HIIT",
        "model": "DataSink",
        "description": "DataSink DESCRIPTION",
        "version": "1.0",
        "uri": "http://www.hiit.fi/",
        "serial": "0466667901"
    }
}
