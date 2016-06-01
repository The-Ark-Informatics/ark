#!/bin/bash
set -eo pipefail

#cat << EOF | debconf-set-selections
#slapd slapd/password2 password root123
#slapd slapd/password1 password root123
#EOF
export DEBIAN_FRONTEND=noninteractive
echo -e " \
	slapd    slapd/internal/generated_adminpw    password   openstack
slapd    slapd/password2    password    openstack
slapd    slapd/internal/adminpw    password openstack
slapd    slapd/password1    password    openstack
" |  debconf-set-selections
