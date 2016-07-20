#!/bin/bash

cd /usr/src/app/ark-common/src/main/native/madeline

sed -i "s/java-6-openjdk-amd64/java-8-openjdk-amd64/g" Makefile
make clean
make

cd -

mv /usr/src/app/ark-common/src/main/native/madeline/libmadeline.so /usr/local/tomcat/lib/
chown root:root /usr/local/tomcat/lib/libmadeline.so
chmod 644 /usr/local/tomcat/lib/libmadeline.so
#execstack -c /usr/local/tomcat/lib/libmadeline.so

while [ ! -f "/usr/src/app/.complete" ]; do
	echo "ARK war not available yet, sleeping 5s then trying again..."
	sleep 5
done

rm /usr/src/app/.complete
mv /usr/src/app/ark-container/target/ark.war webapps/ark.war

export JAVA_OPTS="-Djava.library.path=/usr/local/tomcat/lib"
catalina.sh jpda run 

