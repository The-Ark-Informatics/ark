#!/bin/bash

while [ $(curl --write-out %{http_code} --silent --output /dev/null tomcat:8080/ark) -eq "000" ]; do
	echo "Can't connect to The Ark. Waiting until tomcat has started.."
	sleep 1;
done

#Wait for mysql to be populated
until $(mysql -u ${MYSQL_USER} -hmysql -p$MYSQL_PASSWORD -e '\q' > /dev/null 2>&1)
do
	echo "MySQL database not available. Waiting until it is started..."
	sleep 5;
done

# Starting virtual X screen
Xvfb :1 -screen 0 1920x1080x24 &

DISPLAY=:1.0 mvn verify

# Perserve the exit code from mvn to return at the end of this script when the container ends
EXIT_CODE=$?

echo "Maven exited with: ${EXIT_CODE}"

exit ${EXIT_CODE}
