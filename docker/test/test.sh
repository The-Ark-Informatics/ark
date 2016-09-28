#!/bin/bash

while [ $(curl --write-out %{http_code} --silent --output /dev/null tomcat:8080/ark) -eq "000" ]; do
	echo "Can't connect to The Ark. Waiting until tomcat has started.."
	sleep 1;
done

#Wait for mysql to be populated
until $(mysql -u root -hmysql -p$MYSQL_ROOT_PASSWORD -e '\q' > /dev/null 2>&1)
do
	echo "MySQL database not available. Waiting until it is started..."
	sleep 1;
done


Xvfb :1 -screen 0 1920x1080x24 &

# Starting in a tmux session so that it's easy to send the q required to stop the recording
tmux new-session -d -s Recording1 'ffmpeg -y -f x11grab -video_size 1920x1080 -i :1.0 -codec:v libx264 -r 30 /output/integration-test.mp4'

DISPLAY=:1.0 mvn verify

# Perserve the exit code from mvn to return at the end of this script when the container ends
EXIT_CODE=$?

echo "Maven exited with: ${EXIT_CODE}"

# Send q to the ffmpeg recording running in the tmux session
tmux send-keys -t Recording1 q

sleep 10

exit ${EXIT_CODE}
