FROM tomcat:7-jre8 

MAINTAINER George Gooden <gecgooden@gmail.com>

RUN apt-get update && apt-get install -y libcurl4-gnutls-dev libxml2-dev

EXPOSE 8000

ENV JAVA_OPTS -Djava.library.path=/usr/local/tomcat/lib

ADD start.sh /start.sh
RUN chmod +x /start.sh
CMD /start.sh 
