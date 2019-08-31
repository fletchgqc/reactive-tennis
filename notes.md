Reactive Database Client
Native drivers
- Most NoSQL
- Postgresql: https://github.com/eclipse-vertx/vertx-sql-client/tree/3.8/vertx-pg-client

Non-blocking using a connection pool
- RxJava2-JDBC https://github.com/davidmoten/rxjava2-jdbc

ADBA
- Asynchronous JDBC (completablefuture / JDBC-basec)

Truly reactive with Spring Data
- Most NoSQL
- Spring r2dbc https://r2dbc.io



Setup:
Increase font size in three places, including
Settings -> Editor -> Color Scheme -> Console Font

disable logging filter bean and logging config in properties


Script:
- Intro, github, Flux tennis ball demo 9:00
- Webflux.fn vs Controller
- Tests
  - BallHandler
- Write Webflux.fn 27:00 (= 18)
- Test
  - User 30:48 (= 4)
- Aufrufe von Remote-APIs 38:41 (= 8)
- Logging with logOnNext 48:27 (= 10)
- Logging with MdcLogger 1:00:46 (= 18)
- relationaler Datenbankzugriff 1:06:41 (= 6)
- Fehlerbehandlung 1:12:39 (= 6)
- Kafka-Integration? (actulally more fehler stuff 1:15:23 = 3)

https://github.com/fletchgqc/reactive-tennis
