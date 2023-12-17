# parley

Parley is a simple chat server written in Java. It is using openapi, spring boot, and websockets.

## Tomcat Settings

### catalina.properties

This is the location of the application.properties file. This is used to set the active profile.

```
spring.profiles.active=home
```

### server.xml

GlobalNamingResources section of the server.xml file. This is used to set the database connection.

```
 <Resource name="jdbc/parleyDB" 
               global="jdbc/parleyDB" 
               auth="Container" 
               type="javax.sql.DataSource" 
               driverClassName="org.sqlite.JDBC" 
               url="jdbc:sqlite:C:/Users/jrack/parleyDB/parley.db" 
               
               maxActive="100" 
               maxIdle="20" 
               minIdle="5" 
               maxWait="10000"/>
```

### context.xml

Resource Link to use the JNDI configuration in our application, best way to add it in the server context.xml file.
Notice that ResourceLink name should be matching with the JNDI context name we are using in our application. Also make
sure MySQL jar is present in the tomcat lib directory, otherwise tomcat will not be able to create the MySQL database
connection pool

```
<ResourceLink name="jdbc/parleyDB"
    global="jdbc/parleyDB"
    auth="Container"
    type="javax.sql.DataSource" />
```