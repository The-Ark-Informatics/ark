
About:

This guide outlines the steps required to compile and install the libmadeline native library
for pedigree visualisation within The Ark. The example commands assume an Ubuntu 12.04LTS
operating system.


Step 1:

Install compilers and other essential build tools:

	sudo apt-get install build-essential


Step 2:

Install the runtime and development packages for libxml2, libcurl and libbz2. Under Ubuntu 12.04LTS,
this can be achieved with the following commands:

	sudo apt-get install libxml2 libxml2-dev
	sudo apt-get install libcurl3 libcurl4-gnutls-dev
	sudo apt-get install zlib1g zlib1g-dev
	sudo apt-get install libbz2-1.0 libbz2-dev
	sudo apt-get install libssl libssl-dev


Step 3:

Edit the Makefile as necessary; this usually means changing the paths to your JDK and headers. See
comments in the Makefile for a guide. 

The default Makefile is configured to run on an Ubuntu 12.04LTS (64-bit) system using OpenJDK 6 (amd64 build).


Step 4:

Remove any previous compilation output and build a new libmadeline:
	make distclean
	make


Step 5:

Copy libmadeline.so to the Tomcat6 shared library directory; on Ubuntu 12.04 this is /usr/share/tomcat6/lib.

	sudo service tomcat6 stop
	
	sudo cp libmadeline.so /usr/share/tomcat6/lib
	sudo chown root:root /usr/share/tomcat6/lib/libmadeline.so
	sudo chmod 644 /usr/share/tomcat6/lib/libmadeline.so


Step 6:

Edit /etc/default/tomcat6 to set the library path as part of JAVA_OPTS:
	JAVA_OPTS="-Djava.awt.headless=true -Djava.library.path=/usr/share/tomcat6/lib -Xmx1024m -XX:+UseConcMarkSweepGC"


Step 7:

Restart the Tomcat service. For Ubuntu platforms, use the command:

	sudo service tomcat6 restart
	
