FROM maven:3.3.3-jdk-8

ADD docker/mvn/start.sh /start.sh

RUN ["chmod", "+x", "/start.sh"]

WORKDIR /usr/src/app

VOLUME ["/usr/src/app", "/root/.m2"]

CMD ["/start.sh"]
