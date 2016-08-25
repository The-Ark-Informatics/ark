FROM the-ark/base:latest

MAINTAINER George Gooden <gecgooden@gmail.com>

RUN echo "deb http://packages.linuxmint.com debian import" >> /etc/apt/sources.list && \
	gpg --keyserver pgp.mit.edu --recv-keys 3EE67F3D0FF405B2 && \
	gpg --export 3EE67F3D0FF405B2 > 3EE67F3D0FF405B2.gpg && \
	apt-key add ./3EE67F3D0FF405B2.gpg && \
	rm ./3EE67F3D0FF405B2.gpg

RUN apt-get update && apt-get install -y firefox xvfb ffmpeg tmux

ADD . /test/

RUN chmod +x /test/test.sh
CMD /test/test.sh
