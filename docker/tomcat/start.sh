#!/bin/bash

if [ ! -d /usr/target ]; then 
	echo "Volumes from base container not mounted. Mount volumes and try again"
	exit 1
fi

while [ ! -f /usr/target/libmadeline.so ]; do
	sleep 1
done

mv /usr/target/libmadeline.so /usr/local/tomcat/lib
chown root:root /usr/local/tomcat/lib/libmadeline.so
chmod 644 /usr/local/tomcat/lib/libmadeline.so


while [ ! -f /usr/target/ark.war ]; do
	sleep 1
done

mv /usr/target/ark.war webapps/ark.war

catalina.sh jpda run 
