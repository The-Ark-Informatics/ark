FROM ubuntu:14.04

MAINTAINER George Gooden <gecgooden@gmail.com>

RUN apt-get update && apt-get install -y \
	slapd \
	ldap-utils

ADD start.sh /tmp/start.sh
ADD base.ldif /tmp/base.ldif

CMD /tmp/start.sh
