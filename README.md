# Staff Picks Java

This is a simple Maven project that builds a standalone JAR which contains a Jetty webserver and a simple bookstore servlet. The application is able
to be built into a container and then available to be deployed as an Azure Web App.

### Running locally:
To build the software run the following command:

```bash
$ mvn package
```

This will generate a jar file at `target/bookstore-v2-1.0.0-SNAPSHOT.jar` directory.

To run the application:

```
java -jar target/bookstore-v2-1.0.0-SNAPSHOT.jar
```

The logs from the jar file should report the url to access the web server on, which is port `8080` by default.