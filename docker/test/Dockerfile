FROM the-ark/base:latest

MAINTAINER George Gooden <gecgooden@gmail.com>

RUN apt-get update && apt-get install -y firefox-esr xvfb ffmpeg tmux mysql-client

ADD . /test/

RUN chmod +x /test/test.sh
CMD /test/test.sh
