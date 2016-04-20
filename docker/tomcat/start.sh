#!/bin/bash

while [ ! -d "/usr/src/app/ark-container/target/ark.war" ]; do
	echo "ARK war not available yet, sleeping 10 s then trying again..."
	sleep 10
done

cp /usr/src/app/ark-container/target/ark.war webapps/ark.war

catalina.sh run

