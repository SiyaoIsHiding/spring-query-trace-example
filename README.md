# Spring Boot Data Example of Using Query Trace in Astra DB
This is a web app to order tacos, demonstrating how to use query trace in Cassandra under Spring Boot framework by injecting customized `CqlSession`, based on the [example code](https://github.com/habuma/spring-in-action-6-samples/tree/main/ch04/tacos-sd-cassandra) in the book Spring in Action.

The version for Cassandra is at the branch `cassandra`, [here](https://github.com/SiyaoIsHiding/spring-query-trace-example/tree/cassandra).

## Relevant Code
The following code in `src/main/java/tacos/config/QueryTraceCqlSession.java` enabled the query tracing, retrieves the query trace information from the `ResultSet` object, and add the `TraceEvent`s into a cache for later use.

```java
    @Override
    public ResultSet execute(Statement<?> statement) {
        Statement<?> injected = statement.setTracing(true);
        ResultSet rs = delegate.execute(injected);
        if (injected.isTracing()) {
            ExecutionInfo info = rs.getExecutionInfo();
            QueryTrace queryTrace = info.getQueryTrace();
            queryTrace.getEvents().forEach(event -> {
                traceCache.cache.add(event);
            });
        }
        return rs;
    }
```

## Demo
### Prerequisite
This demo needs an Astra DB instance. Click [here](https://astra.datastax.com/) to register one for free, and create a keyspace called `taco_cloud` in it.

**Credentials**

You need to fill in the credentials at `src/main/resources/application.yml`:

```yml
spring:
  data:
    cassandra:
      keyspace-name: taco_cloud
      username: <client-id>
      Password: <client-secret>
      schema-action: create-if-not-exists
datastax.astra:
    secure-connect-bundle: <path-to-scb-from-src/main/resources/>
```

1. In your database dashboard page, go to the "connect" tab, and then "Generate Token". From there you grab your `client-id` and `client-secret`.
2. Click "Get Bundle" to download the secure connect bundle, and put it under `src/main/resources/`. Fill in the `<path-to-scb-from-src/main/resources/>`, which is a relevant path to `src/main/resources/`.

### Run the app
`mvn spring-boot:run` to run the app. Go to `localhost:8080`.
![images/home.png](images/home.png)

Build the taco

![images/design.png](images/design.png)

Place the order

![images/order.png](images/order.png)

See the trace

![images/trace.png](images/trace.png)