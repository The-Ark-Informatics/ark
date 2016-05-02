#!/bin/bash
YAML=`pwd`/docker/docker-compose.yml
echo "START AND WAIT"

docker-compose -f $YAML down -v
docker-compose -f $YAML build
docker-compose -f $YAML up -d

while [ $(curl --write-out %{http_code} --silent --output /dev/null 192.168.99.100:8080/ark) -eq "000" ]; do
	echo "Not ready, sleeping"
	sleep 5;
done
