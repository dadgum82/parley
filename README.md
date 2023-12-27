# parley

## General

Parley is a simple chat server written in Java. It is using `openapi codegen`, `mapstruct`, `lombok`, `spring boot`,
and `websockets` aka Server Side Events (sse) with the goal of being utilizing code generation over manual
implementation.
Since we are using code generation for the API, the project meets and is validated against the OpenAPI 3.0
Specification (OAS).

## Implemented Features

* User avatar
* User chat
* Chat history
* Chat message
* Chat room
* Emoji
* Chat Room creation
* Chat Room deletion
* Chat Room join
* Chat Room icon

## Potential Features

* User status
* User friends
* User friend requests
* User login
* User logout
* User registration
* User profile
* chat room leave

## Swagger

Swagger is used to document the API. The swagger-ui is available at
`http://<localhost>/<context-path>/<servlet-path>/swagger-ui/index.html`
For example, if you are running on localhost:8080, the swagger-ui is available at
`http://localhost/parley/api/swagger-ui/index.html`

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

Resource Link to use the JNDI configuration in our application, the best way to add it in the server context.xml file.
Notice that ResourceLink name should be matching with the JNDI context name we are using in our application. Also make
sure MySQL jar is present in the tomcat lib directory, otherwise tomcat will not be able to create the MySQL database
connection pool

```
<ResourceLink name="jdbc/parleyDB"
    global="jdbc/parleyDB"
    auth="Container"
    type="javax.sql.DataSource" />
```