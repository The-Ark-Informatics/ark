#!/bin/bash

mvn initialize

mvn clean package

echo "Ark war file built"

export PWD_DIR=`pwd`

sudo service tomcat7 stop

cd /var/lib/tomcat7/webapps/

sudo rm -rf ark*

sudo cp $PWD_DIR/ark-container/target/ark.war .

sudo service tomcat7 start

sudo tail -1000f /var/log/tomcat7/catalina.out

