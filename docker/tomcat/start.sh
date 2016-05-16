#!/bin/bash

while [ ! -f "/usr/src/app/.complete" ]; do
	echo "ARK war not available yet, sleeping 5s then trying again..."
	sleep 5
done

rm /usr/src/app/.complete
mv /usr/src/app/ark-container/target/ark.war webapps/ark.war

catalina.sh jpda run 

