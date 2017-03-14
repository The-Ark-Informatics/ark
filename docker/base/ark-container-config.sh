#!/bin/bash -l

APP_CONTEXT=ark-container/src/main/resources/applicationContext.xml
cat ${APP_CONTEXT}.example |\
	sed "s/ark-database/mysql/g" |\
	sed "s/arkadmin/root/g" |\
	sed "s/##MYSQL_PASSWORD##/${MYSQL_ROOT_PASSWORD}/g" |\
	sed "s/ark-ldap\:/slapd\:/g" |\
	sed "s/##SLAPD_PASSWORD##/${SLAPD_PASSWORD}/g" >\
	$APP_CONTEXT

/bin/cat <<EOM >/usr/target/database.properties
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://mysql:3306
jdbc.user=root
jdbc.password=${MYSQL_ROOT_PASSWORD}
EOM

/bin/cat <<EOM >/usr/target/ldap.properties
ldap.url=ldap://slapd:389
ldap.basePeopleDn=ou=arkUsers
ldap.base=dc=the-ark,dc=org,dc=au
ldap.userDn=cn=admin,dc=the-ark,dc=org,dc=au
ldap.password=${SLAPD_PASSWORD}
EOM

APP_PROP=ark-container/src/main/resources/application.properties
cat ${APP_PROP}.example |\
	sed "s/\~\/arkFileData/\/opt\/ark-datastore/g" >\
	${APP_PROP}

APP_CONTEXT=ark-container/src/test/resources/applicationContext.xml
cat ${APP_CONTEXT}.example |\
	sed "s/ark-database/mysql/g" |\
	sed "s/arkadmin/root/g" |\
	sed "s/##MYSQL_PASSWORD##/${MYSQL_ROOT_PASSWORD}/g" |\
	sed "s/ark-ldap\:/slapd\:/g" |\
	sed "s/##SLAPD_PASSWORD##/${SLAPD_PASSWORD}/g" >\
	$APP_CONTEXT

APP_PROP=ark-container/src/test/resources/application.properties
cat ${APP_PROP}.example |\
	sed "s/\~\/arkFileData/\/opt\/ark-datastore/g" >\
	${APP_PROP}
