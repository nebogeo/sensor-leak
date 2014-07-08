SensorLeak for dBsCode
======================

A simple android program that spits it's sensor data over UDP, for use when
prototyping as an easy source of plenty of sensor data.

Note:

Debug UDP packets on linux (over wifi) with:

`sudo tcpdump -i wlan0 udp port 8888 -vv -X`
