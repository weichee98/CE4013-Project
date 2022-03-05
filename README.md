# CE4013-Project

To run server:

    java -jar ./CE4013-Project-1.0-SNAPSHOT.jar -sc server -reqr 0.1 -resr 0.1 -amo true

To run client:

    java -jar ./CE4013-Project-1.0-SNAPSHOT.jar -sc client -sip 127.0.0.1

Command line arguments:

| Arguments | Description                                     | Default Value | Valid input       |
|-----------|-------------------------------------------------|---------------|-------------------|
| `-sc`     | Run server or client                            | Client        | server/client     |
| `-sip`    | IP address of the server (used only for client) | 127.0.0.1     | Valid ip address  |
| `-reqr`   | Request loss rate                               | 0.1           | Float between 0-1 |
| `-resr`   | Response loss rate                              | 0.1           | Float between 0-1 |
| `-amo`    | At most once (true) or at least once (false)    | true          | true/false        |