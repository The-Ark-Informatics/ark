#!/bin/bash -l

cat ark-user-account/src/main/resources/applicationContext.xml.example |\
	sed "s/type-in-password/${SLAPD_PASSWORD}/g" >\
	ark-user-account/src/main/resources/applicationContext.xml
