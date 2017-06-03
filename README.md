#########################
# CONTENTS OF THIS FILE #
#########################

 * Introduction
 * Features
 * Implementation
 * Installation and Configuration
 * Running
 * Testing
 * Future Developments
 * FAQ
 * Credits / Contact
 * Versioning
 * License
 * Useful Resources


------------
INTRODUCTION
------------

BabbleSMS is a server application that allows for the sending of SMS.

It is designed to have the look-and-feel of an email account, such that there
is a shallow learning curve. It can be configured to work with an SMS Gateway
or Mobile Network Operator of choice.

Messages sent include those to individuals and to groups. Using shorcodes, the
system can also receive messages.

A sample of it can be seen on the following URL:
http://babblesms.tawi.mobi
Username and password: demo

The application is built on JEE, tested on Redhat WildFly, Tomcat, MySQL and
Linux.


--------
FEATURES
--------

The application allows for the following:
- Addition and editing of Contacts
- Addition and editing of Groups
- Send SMS to Contacts and Groups
- Receive SMS from mobile subscribers
- View credit balance


--------------
IMPLEMENTATION
--------------
## Built With
* [Tomcat](https://tomcat.apache.org) - The application server
* [Maven](https://maven.apache.org) - Dependency management
* [MySQL](https://www.mysql.com) - Relational Database Management System


## Libraries
* [JUnit] (http://www.junit.org) - unit and system testing
* [Google Gson] (https://github.com/google/gson) - json generation and parsing
* [Apache HttpComponents] (https://hc.apache.org) - HTTP utility library
* [Apache Log4j 2] (https://logging.apache.org/log4j) - file and console logger
* [Apache Commons] (https://commons.apache.org) - various utility libraries
* [Hibernate] (http://hibernate.org) - Java ORM framework
* [HikariCP] (https://brettwooldridge.github.io/HikariCP) - database connection pooling
* [Ehcache] (www.ehcache.org) - caching library
* [Joda-Time] (http://www.joda.org) - Time and date manipulations

The application complies with JSE 1.8 and JEE 7.


------------------------------
INSTALLATION AND CONFIGURATION
------------------------------

0 - Prerequisites:
JDK:
    1.8 or above
Memory:
    For best performance, computer should have at least 1GB RAM
Disk:
    Approximately 200MB free space
RDBMS:
    MySQL 5.5 and above or MariaDB 10 and above
Application Server:
    Tomcat 8 and above
Operating System:
    *nix. Has been built and tested on Ubuntu 16.
    Likely it will also will run on Mac, Solaris and Windows.
Other:
    Python 2.x to run some database scripts


1 - Applications:
Install the following applications in your runtime environment:
* MySQL 5.5 and above (or MariaDB 10 and above)
* Maven 3.3.9 and above
* Tomcat 8.5 and above
* For source code editing, you can install the IDE of choice for example
  Eclipse, Spring Tool Suite, Intellj IDEA or Netbeans


2 - Environment Variables:
Ensure that the following environment variables are properly set up:
JAVA_HOME, CATALINA_HOME

For example, on Ubuntu, add the following lines to the .bashrc file in the user
home folder:

    export JAVA_HOME=/opt/jdks/jdk1.8.0
    export PATH=${JAVA_HOME}/bin:$PATH

    export CATALINA_HOME=/opt/Programs/Tomcat/8.5.6
    export PATH=${CATALINA_HOME}/bin:$PATH

The above assumes that JDK 1.8 is unpacked in the folder '/opt/jdks' and Tomcat
is unpacked in the folder '/opt/Programs/Tomcat/'


3 - Configure Tomcat:
Configure Tomcat such that it will allow Maven to automatically deploy to it.
This is done by adding a user with roles manager-gui and manager-script.

Edit the following file
    ${CATALINA_HOME}/conf/tomcat-users.xml

Have the content of the file as follows:

    <?xml version='1.0' encoding='utf-8'?>
    <tomcat-users>

            <role rolename="manager-gui"/>
            <role rolename="manager-script"/>
            <user username="admin" password="password" roles="manager-gui,manager-script" />

    </tomcat-users>

The above authentication credentials match what is in the project's 'pom.xml'.


4 - Configure MySQL:
Ensure that the client and server versions of MySQL or MariaDB are installed on
your local machine

Create the database user account as follows:
        % mysql -h localhost -u root -p
        Enter password: ******
        mysql> CREATE USER 'bank'@'localhost' IDENTIFIED BY 'pass';
        mysql> GRANT ALL ON bankdb.* TO 'bank'@'localhost';
        Query OK, 0 rows affected (0.09 sec)
        mysql> quit
        Bye

    In the commands shown, the '%' represents the prompt displayed by your shell
    or command interpreter, and 'mysql>' is the prompt displayed by mysql.


Create the database as follows:
        % mysql -h localhost -u bank -p
        Enter password: ******
        mysql> CREATE DATABASE bankdb;
        Query OK, 0 rows affected (0.09 sec)
        mysql> quit
        Bye

Locate the SQL and data import script in the folder 'src/main/scripts/sql/data'
Execute the script as follows:
        % mysql -h localhost -u bank -p bankdb < tables.mysql.sql

    The password for the above is 'pass'.

You can verify either from command line or from MySQL Workbench that the tables
have been created and the sample data has been imported.


5 - Logging Configuration (optional):
You can configure the log file outputs by modifying the following config file:
    bankaccount/src/main/resources/log4j2.xml



-------
RUNNING
-------
Run your Tomcat instance with the following shell commands:
    % cd ${CATALINA_HOME}/bin
    % ./catalina.sh run


You can then utilize the following maven commands to manipulate WAR file on
Tomcat:
    mvn tomcat7:deploy -Dmaven.test.skip=true
    mvn tomcat7:undeploy
    mvn tomcat7:redeploy -Dmaven.test.skip=true


-------
TESTING
-------

## API
See the 'Bank Account API.doc' under the 'docs' folder for a detailed explanation
of the web service API.


## Code Coverage
[JaCoCo] (http://www.eclemma.org/jacoco/) is the Java code coverage library used
in the project.

One can install the Eclipse plugin called 'EclEmma' from the Eclipse Marketplace
to observe coverage. Further details are available here: http://www.eclemma.org

JaCoCo plugins are also available for other Java IDEs. For these options, see:
http://www.jacoco.org/jacoco/trunk/doc/integrations.html

In order to generate a report for the code coverage, refer to the instructions
in the pom.xml file under the 'build' section.


## Javadoc
To generate javadocs for the project, use the following Maven command:
    mvn javadoc:javadoc


-------------------
FUTURE DEVELOPMENTS
-------------------
* Dependency Injection, Aspect Oriented Programming and MVC with Spring
  framework

* Swagger framework for automatic documentation of the web service API


----
FAQ
----

Q: Can I use an application container other than Tomcat?

A: Sure you can. Just create the war package and deploy appropriately in your
   preferred application server. You create the package with the following
   command:
    $ mvn package -Dmaven.test.skip=true


-----------------
CREDITS / CONTACT
-----------------

## Current Maintainers:
* Michael Wakahe


## Contributors:
* Eugene Wang'ombe
* Brian Kidiya Asava
* Godfrey Wambua
* Roy Mwika
* Dennis Mutegi
* Migwi Ndung'u
* Peter Mwenda
* Eugene Chimita


## Acknowledgments
* Kenyan mobile money operators

## Contributing
All contributions are welcome. You can pull requests to us via Github.


----------
VERSIONING
----------
The project uses git via Github. The project repository can be viewed here:
https://github.com/TawiKenya/BabbleSMS


--------
LICENSE
--------
This project is licensed under the Open Software License v. 3.0 (OSL-3.0) - see
the [LICENSE.md](LICENSE.md) file for details.


-----------------
USEFUL RESOURCES
-----------------
-