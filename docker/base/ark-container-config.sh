#!/bin/bash -l

APP_CONTEXT=ark-container/src/main/resources/applicationContext.xml
cat ${APP_CONTEXT}.example |\
	sed "s/ark-database/mysql/g" |\
	sed "s/arkadmin/root/g" |\
	sed "s/##MYSQL_PASSWORD##/${MYSQL_ROOT_PASSWORD}/g" |\
	sed "s/ark-ldap\:/slapd\:/g" |\
	sed "s/##SLAPD_PASSWORD##/${SLAPD_PASSWORD}/g" >\
	$APP_CONTEXT

APP_CONTEXT=ark-container/src/test/resources/applicationContext.xml
cat ${APP_CONTEXT}.example |\
	sed "s/ark-database/mysql/g" |\
	sed "s/arkadmin/root/g" |\
	sed "s/##MYSQL_PASSWORD##/${MYSQL_ROOT_PASSWORD}/g" |\
	sed "s/ark-ldap\:/slapd\:/g" |\
	sed "s/##SLAPD_PASSWORD##/${SLAPD_PASSWORD}/g" >\
	$APP_CONTEXT

