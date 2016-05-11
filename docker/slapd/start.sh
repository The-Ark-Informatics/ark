#!/bin/bash
set -e

if [ ! ${SLAPD_PASSWORD} ]; then
	PASSWORD=password
else
	PASSWORD=${SLAPD_PASSWORD}
fi

if [ ! ${ARK_SUPERUSER_PASSWORD} ]; then
	ARK_SUPERUSER_PASSWORD=Password_1
fi

DATADIR=/var/lib/ldap
service slapd stop

if [ ! -f $DATADIR/.complete ]; then

	if [ -d /etc/ldap/slapd.d ]; then
		rm -rf /etc/ldap/slapd.d
		rm -f /etc/ldap.conf
	fi

	HASH=$(slappasswd -s ${PASSWORD})

	echo ${HASH}

	cat << EOF > /etc/ldap/slapd.conf
#
# See slapd.conf(5) for details on configuration options.
# This file should NOT be world readable.
#
include        /etc/ldap/schema/core.schema
include         /etc/ldap/schema/cosine.schema
include         /etc/ldap/schema/inetorgperson.schema
include         /etc/ldap/schema/nis.schema

# Define global ACLs to disable default read access.

# Do not enable referrals until AFTER you have a working directory
# service AND an understanding of referrals.
#referral    ldap://root.openldap.org

pidfile        /var/run/slapd/slapd.pid
argsfile    /var/run/slapd/slapd.args

# Allow bind
allow bind_v2


# Load dynamic backend modules:
modulepath    /usr/lib/ldap
moduleload    back_bdb.la
moduleload    syncprov.la
# moduleload    back_hdb.la
# moduleload    back_ldap.la

# Sample security restrictions
#    Require integrity protection (prevent hijacking)
#    Require 112-bit (3DES or better) encryption for updates
#    Require 63-bit encryption for simple bind
# security ssf=1 update_ssf=112 simple_bind=64

# Sample access control policy:
#    Root DSE: allow anyone to read it
#    Subschema (sub)entry DSE: allow anyone to read it
#    Other DSEs:
#        Allow self write access
#        Allow authenticated users read access
#        Allow anonymous users to authenticate
#    Directives needed to implement policy:
# access to dn.base="" by * read
# access to dn.base="cn=Subschema" by * read
# access to *
#    by self write
#    by users read
#    by anonymous auth
#
# if no access controls are present, the default policy
# allows anyone and everyone to read anything but restricts
# updates to rootdn.  (e.g., "access to * by * read")
#
# rootdn can always read and write EVERYTHING!

#######################################################################
# BDB database definitions
#######################################################################

database    bdb
suffix        "dc=the-ark,dc=org,dc=au"
rootdn        "cn=admin,dc=the-ark,dc=org,dc=au"
# Cleartext passwords, especially for the rootdn, should
# be avoid.  See slappasswd(8) and slapd.conf(5) for details.
# Use of strong authentication encouraged.
rootpw       ${HASH} 

# The database directory MUST exist prior to running slapd AND 
# should only be accessible by the slapd and slap tools.
# Mode 700 recommended.
directory   ${DATADIR} 
# Indices to maintain
index    objectClass    eq,pres
index    ou,cn,mail,surname,givenname    eq,pres,sub
index member,labeledURI            pres,eq

# Replicas

# Entrey cache-size
cachesize    10000

checkpoint 128 15

# Server is a sync provider
overlay syncprov
syncprov-checkpoint 100 10
syncprov-sessionlog 100
EOF
	echo "conf file written"
	service slapd start
	
	if [ -f /tmp/base.ldif ]; then
		if [ -z "$(ldapsearch -w ${PASSWORD} -D 'cn=admin,dc=the-ark,dc=org,dc=au' -b 'dc=the-ark,dc=org,dc=au' | grep 'dn: ou=arkUsers,dc=the-ark,dc=org,dc=au')" ]; then
			/usr/bin/ldapadd -x -D "cn=admin,dc=the-ark,dc=org,dc=au" -f /tmp/base.ldif -w ${PASSWORD}
		else
			echo "Base already exists, skipping..."
		fi
	fi

	if [ -f /tmp/group.ldif ]; then
		if [ -z "$(ldapsearch -w ${PASSWORD} -D 'cn=admin,dc=the-ark,dc=org,dc=au' -b 'ou=arkUsers,dc=the-ark,dc=org,dc=au' | grep 'dn: ou=arkUsers,dc=the-ark,dc=org,dc=au')" ]; then
			/usr/bin/ldapadd -x -D "cn=admin,dc=the-ark,dc=org,dc=au" -f /tmp/group.ldif -w ${PASSWORD}
		else
			echo "Group already exists, skipping..."
		fi
	fi

	while ! [ -f "/usr/src/app/ark-user-account/.completed" ]; do
		echo "Sleeping"
		sleep 1	
	done	


	java -jar /usr/src/app/ark-user-account/target/ark-user-account-1.0.0-jar-with-dependencies.jar arksuperuser@ark.org.au ${ARK_SUPERUSER_PASSWORD} Super User
	rm /usr/src/app/ark-user-account/.completed
	service slapd stop #for some reason this doesn't work
	killall slapd
	touch "$DATADIR/.complete"
fi


if [ -f /var/lib/ldap/alock ]; then
	rm /var/lib/ldap/alock
fi

echo "Starting slapd"
exec /usr/sbin/slapd -h "ldap:///" -u openldap -g openldap -d 5


