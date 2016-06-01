FROM tomcat:7-jre8

MAINTAINER George Gooden <gecgooden@gmail.com>

#ENV JAVA_OPTS="-Xss100m"
EXPOSE 8080

ADD start.sh /tmp/start.sh
RUN ["chmod", "+x", "/tmp/start.sh"]

CMD /tmp/start.sh
