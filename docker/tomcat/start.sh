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

mkdir -p /etc/the-ark

cat <<EOF >/etc/the-ark/the-ark.properties
database.host=mysql
database.username=$MYSQL_USER
database.password=$MYSQL_PASSWORD

ldap.host=slapd
ldap.password=$SLAPD_PASSWORD

wicket.runtimeConfiguration=DEVELOPMENT
EOF

catalina.sh jpda run
