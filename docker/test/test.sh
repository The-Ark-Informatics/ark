#!/bin/bash

#xvfb-run --listen-tcp --server-num 44 --auth-file /tmp/xvfb.auth -s "-ac -screen 0 1920x1080x24" firefox http://google.com &

while [ $(curl --write-out %{http_code} --silent --output /dev/null tomcat:8080/ark) -eq "000" ]; do
	echo "Can't connect to The Ark. Waiting until tomcat has started.."
	sleep 1;
done


Xvfb :1 -screen 0 1920x1080x24 &

tmux new-session -d -s Recording1 'ffmpeg -y -f x11grab -video_size 1920x1080 -i :1.0 -codec:v libx264 -r 30 /output/integration-test.mp4'

DISPLAY=:1.0 mvn verify

# Perserve the exit code from mvn to return at the end of this script when the container ends
EXIT_CODE=$?

echo "Maven exited with: ${EXIT_CODE}"

tmux send-keys -t Recording1 q

sleep 10

exit ${EXIT_CODE}
