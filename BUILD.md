#Build Instructions

###NOTE: Server setup instructions available [here](https://the-ark.atlassian.net/wiki/pages/viewpage.action?pageId=360491)

To build this project, the following steps are required:

1. Clone the repository to your computer.
2. Copy the `applicationContext.xml.example` and `application.properties.example` files in `ark-container/src/main/resources` to `ark-container/src/main/resources/applicationContext.xml` and `ark-container/src/main/resources/application.properties` respectively.
3. Edit the two files you copied in the previous step such that they contain your connection and password settings for MySQL, LDAP and Mail Server.
4. Run the `ark-build-pwd.sh` shell script in the root of the repository. 

All you need to do now is copy the output war file from `ark-container/target/ark.war` to your server.