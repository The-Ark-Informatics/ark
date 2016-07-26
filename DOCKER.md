#The Ark on Docker

The [Docker](https://www.docker.com) configuration supplied in this repository allows quick and easy building of The Ark.

This configuration has four containers defined; MySQL, LDAP (Slapd), Tomcat and Maven. These containers are integrated together using [Docker Compose](https://www.docker.com/products/docker-compose). 

###Usage

1. `cd docker/`
2. `docker-compose build`
3. `docker-compose up`

###Prerequisites
- [Docker](https://www.docker.com)
- [Docker Compose](https://www.docker.com/products/docker-compose)


###Extra Commands

Command				| Usage
--------------------------------|------------------------------
`docker-compose up -d` 				| Starts the containers in daemon mode meaning that no logs are printed to STDOUT
`docker-compose down -v` 			| Tears down all containers, and deletes all of the associated storage volumes.
`docker exec -it <containername> /bin/bash` 	| Starts an interactive bash session within the container named \<containername\>


###Passwords

The default passwords are contained in [`docker/env_file`](docker/env_file) and should be modified before the initial `docker-compose up`, as the services are configured in the first run of each container. Passwords can be changed by tearing down all containers and deleting their volumes, however this will also delete any data stored in the MySQL and LDAP containers.

###Configuration
Inside `docker/env_file`, you can also set the following Environment Variables which will configure the software inside each container:

Environment Variable 			| Usage	                                  | Default Value
----------------------------------------|-----------------------------------------|------------------------------
`MYSQL_ROOT_PASSWORD`			| Changes the MySQL root user password    | `mysql-password`
`SLAPD_PASSWORD`			| The password for the LDAP database      | `slapd-password`
`ARK_USERNAME`				| The Ark Superuser's username            | `arksuperuser@ark.org.au`
`ARK_SUPERUSER_PASSWORD`		| The Ark Superuser's password            | `Password_1`

In addition to these variables, you can also add environment variables listed [here](https://hub.docker.com/_/mysql/) to configure MySQL.

###Explanation
When `docker-compose up` is called for the first time, all 4 containers are built and started. The Maven and MySQL containers begin their work, compiling the web application and importing the base database respectively, while the Tomcat and LDAP containers wait for the output of Maven. The LDAP container waits until the ark-user-account jar has been created, as it is needed to populate the LDAP database with The Ark root account, and the Tomcat waits until ark.war has been created, and deploys it once is.

###Container Specific Quirks
For the maven container, we mount ~/.m2 to /root/.m2 so that all maven repositories are cached and that the users maven settings.xml file applies.
