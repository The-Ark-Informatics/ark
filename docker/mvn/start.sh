#!/bin/bash
set -eo pipefail

#check if /usr/src/app is not empty then do the following

#Replace SLAPD password in ark-user-account
cat ark-user-account/src/main/resources/applicationContext.xml.example |\
	sed "s/type-in-password/${SLAPD_PASSWORD}/g" >\
	ark-user-account/src/main/resources/applicationContext.xml


#Build ark-user-account with assembly:assembly for slapd container
mvn -f ark-user-account/pom.xml clean assembly:assembly

#Replace passwords and user names in ark-container/.../applicationContext.xml
APP_CONTEXT=ark-container/src/main/resources/applicationContext.xml
cat ${APP_CONTEXT}.example |\
	sed "s/ark-database/mysql/g" |\
	sed "s/arkadmin/root/g" |\
	sed "s/##MYSQL_PASSWORD##/${MYSQL_ROOT_PASSWORD}/g" |\
	sed "s/ark-ldap\:/slapd\:/g" |\
	sed "s/##SLAPD_PASSWORD##/${SLAPD_PASSWORD}/g" >\
	$APP_CONTEXT

APP_PROP=ark-container/src/main/resources/application.properties
cat ${APP_PROP}.example |\
	sed "s/\~\/arkFileData/\/opt\/ark-datastore/g" >\
	${APP_PROP}

mvn clean package


