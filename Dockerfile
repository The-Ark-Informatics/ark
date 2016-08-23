FROM maven:3.3.3-jdk-8

MAINTAINER George Gooden <gecgooden@gmail.com>

ADD docker/mvn/start.sh /start.sh
ADD docker/env_file /env_file
RUN ["chmod", "+x", "/start.sh"]

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
VOLUME ["/usr/src/app"]

CMD /start.sh
