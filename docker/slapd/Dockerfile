FROM ubuntu:14.04 

MAINTAINER George Gooden <gecgooden@gmail.com>

RUN apt-get update && apt-get install -y openjdk-7-jre

ADD prepare.sh /tmp/prepare.sh
RUN ["chmod", "+x", "/tmp/prepare.sh"]
RUN /tmp/prepare.sh 

RUN apt-get install -y slapd ldap-utils

ADD start.sh /tmp/start.sh
ADD base.ldif /tmp/base.ldif
ADD group.ldif /tmp/group.ldif
RUN ["chmod", "+x", "/tmp/start.sh"]

CMD /tmp/start.sh
