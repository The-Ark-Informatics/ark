#!/bin/bash -l

APP_CONTEXT=ark-container/src/main/resources/applicationContext.xml
cat ${APP_CONTEXT}.example |\
	sed "s/ark-database/mysql/g" |\
	sed "s/arkadmin/${MYSQL_USER}/g" |\
	sed "s/##MYSQL_PASSWORD##/${MYSQL_PASSWORD}/g" |\
	sed "s/ark-ldap\:/slapd\:/g" |\
	sed "s/##SLAPD_PASSWORD##/${SLAPD_PASSWORD}/g" >\
	$APP_CONTEXT

APP_PROP=ark-container/src/main/resources/application.properties
cat ${APP_PROP}.example |\
	sed "s/\~\/arkFileData/\/opt\/ark-datastore/g" >\
	${APP_PROP}

APP_CONTEXT=ark-container/src/test/resources/applicationContext.xml
cat ${APP_CONTEXT}.example |\
	sed "s/ark-database/mysql/g" |\
	sed "s/arkadmin/${MYSQL_USER}/g" |\
	sed "s/##MYSQL_PASSWORD##/${MYSQL_PASSWORD}/g" |\
	sed "s/ark-ldap\:/slapd\:/g" |\
	sed "s/##SLAPD_PASSWORD##/${SLAPD_PASSWORD}/g" >\
	$APP_CONTEXT

APP_PROP=ark-container/src/test/resources/application.properties
cat ${APP_PROP}.example |\
	sed "s/\~\/arkFileData/\/opt\/ark-datastore/g" >\
	${APP_PROP}
