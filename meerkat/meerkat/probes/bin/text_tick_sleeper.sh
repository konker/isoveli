#!/bin/sh

LEN=$1
if [ -z "$1" ]; then
    LEN=20
fi
TICK=$(($LEN/4))
CNT=0

echo "foo: BAR, data: will start sleeping - $CNT"
while true; do
    sleep $TICK
    CNT=$(($CNT + 1))
    echo "foo: BAZ, data: slept for $TICK seconds - $CNT"
done


