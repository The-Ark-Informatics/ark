#The Ark on Docker

The [Docker](https://www.docker.com) configuration supplied in this repository allows quick and easy building of The Ark.

This configuration has five containers defined which are responsible for compiling, running and testing The Ark. These containers are tied together using [Docker Compose](https://www.docker.com/products/docker-compose). 

##Usage

1. `cd docker/`
2. `docker-compose build`
3. `docker-compose up`

##Prerequisites
- [Docker](https://www.docker.com)
- [Docker Compose](https://www.docker.com/products/docker-compose)

##Extra Commands

These additional commands should be used from within the docker directory.
Command				| Usage
--------------------------------|------------------------------
`docker-compose up -d` 				| Starts the containers in daemon mode meaning that no logs are printed to STDOUT
`docker-compose down -v` 			| Tears down all containers, and deletes all of the associated storage volumes.
`docker exec -it <containername> /bin/bash` 	| Starts an interactive bash session within the container named \<containername\>
`docker-compose -f docker-compose.yml -f docker-compose.test.yml run test` | Runs the full Integration Test suite inside the 'test' container

##Passwords

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

##Containers

###Base
The base container is responsible for compiling all the components of The Ark. It uses maven to compile ark-user-account.jar, used to setup the Super User in the LDAP database, and ark.war, the deployable war file containing The Ark. It also uses Make/gcc to compile libmadeline.so, the JNI library used to create subject pedigree trees.

The compiled products are placed in a Volume at /usr/target so that they are accessible to the other containers.

###DB
The MySQL container. This contains a SQL script that will be imported on an initial run. It also configures the Super User account name to match what is stored in the env_file.

###SLAPD
The LDAP container. This container executes the ark-user-account.jar to import the Super User account into the LDAP database. 

###Tomcat
The Tomcat container gathers the ark.war and libmadeline.so, and hosts The Ark on port 8080. It also has port 8000 exposed for JDPA. 

###Test
The test container runs the Integration tests via Maven. The test container is defined in the docker-compose.test.yml file, so that it can doesn't impact standard usage of docker-compose. 
It has uses xvfb as a virtual framebuffer so that a browser can run. The xvfb is recorded with ffmpeg and the output saved to the docker/test/output directory.
