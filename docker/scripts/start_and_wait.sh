#!/bin/bash
YAML=`pwd`/docker/docker-compose.yml
echo "START AND WAIT"

docker-compose -f $YAML down -v
docker-compose -f $YAML build > /dev/null
docker-compose -f $YAML up -d

IP="127.0.0.1"
if [ -n "$DOCKER_HOST" ]; then
	IP=$(echo $DOCKER_HOST | cut -f2 -d: | cut -f3 -d/)
fi

while [ $(curl --write-out %{http_code} --silent --output /dev/null ${IP}:8080/ark) -eq "000" ]; do
	echo "Not ready, sleeping"
	sleep 5;
done
