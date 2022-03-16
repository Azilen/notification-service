# Azilen Notification Service

Azilen Notification Service is a component designed to send notifications such as Email, SMS and Push Notifications.

## The Architecture

Azilen Notification Service is built on Java with JHipster Framework. The service has an Amazon SQS Listener that
listens to messages receiving on SQS Queue. It does following,

1. Listens to SQS Messages
2. Process SQS Message and decides which notification to send (Email, SMS, Push) based on event type provided in the
   message payload.
3. Sends the notification to recipient accordingly.
4. Saves the notification history to database.

Following diagram demonstrates this process.

![Azilen Notification Service Architecture Diagram](https://i.ibb.co/d23KQt2/Untitled-Workspace-9.png)

## Supported Integrations

#### Email

- Amazon Pinpoint

#### SMS

- Twilio

#### Push Notifications

- To be implemented

## Pre-requisites

### Tools

* Java 8+
* Maven
* Docker (with Docker Compose)

### Services

* AWS Account
* Twilio Account

### Configurations

* Keep AWS Secret Key, Access Key and Region name handy
* Create AWS Pinpoint Project
* Verify Sender email address
* Verify recipient email addresses if running on Sandbox (Not required for Production)
* Keep Pinpoint Project Name, Project ID and Sender email address handy
* Create SQS Queue on AWS
* Keep SQS Queue name and URL handy
* Create Twilio Account (if not done already)
* Verify From Phone Number
* Keep Twilio Account SID, Auth Token and From Phone Number handy
* Setup PostgreSQL Database (Instructions available in **Installation Steps** section)

## Installation Steps

After installing tools on your Server and after acquiring services, use following steps Install Azilen Notification
Service,

1. Checkout source code from [Git]
2. Go to source code directory and run following command to build the source code,

```
mvn clean install -DskipTests
```

3. Go to Service directory

```
cd Service
```

4. Build Docker image for application from Service directory with following command,

```
./mvnw package -Pprod verify -DskipTests jib:dockerBuild
```

5. Go to Schema directory for building docker image for schema creation

```
cd ../Schema/src/main/docker
```

6. Build Docker image for Database Schema from Schema directory with following command,

```
docker build -t azilen-notification-app-schema:1.0.0 .
```

## Configure Service

Service configuration has 2 steps. One is database configuration and other is application/service configuration.

### Configure Database

*If you already have PostgreSQL Database pre-installed on your server, you can skip this step and configure the
application only instead.*

1. Go to docker directory to configure the database (Go to Service directory again),

```
cd ../../../../Service/src/main/docker/
```

2. Run PostgreSQL database docker image with following command,

```
docker-compose -f master.yml up -d azilen-notification-postgresql
```

3. Check docker logs with following command if database system is ready to accept connections,

```
docker logs -f --tail 100 azilen-notification-postgresql
```

4. Run PgAdmin User Interface for PostgreSQL,

```
docker-compose -f master.yml up -d pgadmin
```

5. Check docker logs with following command if PgAdmin is up,

```
docker logs -f --tail 100 pgadmin
```

6. If you find continuous error of `[Errno 13] Permission denied: '/var/lib/pgadmin/sessions'` in PgAdmin logs, execute
   following 3 commands,

```
docker stop pgadmin && docker rm pgadmin
sudo chmod -R 777 ~/docker-volumes/pgadmin_volume
docker-compose -f master.yml up -d pgadmin
docker logs -f --tail 100 pgadmin
```

7. Once PgAdmin runs successfully, it should print this logs in docker logs `Listening at: http://[::]:80 (1)`.

8. Open PgAdmin with your `Host IP` and port number `8086`. Eg, [http://localhost:8086].
9. Enter PgAdmin Default Username and Password configured in `Service/src/main/docker/pgadmin.yml` file.
10. Configure new server in PgAdmin with host as `azilen-notification-postgresql` and Username and Password as
    configured in `Service/src/main/docker/postgresql.yml` file.
11. Check whether `AzilenNotificationApp` Database is created successfully.
12. Next create `notifications` schema with following command,

```
docker-compose -f master.yml up -d azilen-notification-app-schema
```

13. Check whether `notifications` schema is created inside `AzilenNotificationApp` Database. *(Refresh schema if does
    not appear).*

### Configure Application

Now that database configuration is done successfully, let's configure application first,

1. Open `Service/src/main/docker/app.yml` file in editor.
2. Replace all the values mentioned with Square brackets `[CONFIG_VALUE]` with your AWS Pinpoint, SQS Configurations and
   Twilio Configurations. *(Change Database configuration as well if you are already using pre-installed database).*
3. Note that, all the values to be replaced with including Square brackets `[` `]`. Do not keep Square brackets in value
   part.

## Run Service

1. Assuming you are in the following directory to run the service from Service directory. If not, go to this directory,

```
cd Service/src/main/docker
```

2. Execute following command to Run the service,

```
docker-compose -f app.yml up -d azilen-notification-api-service
```

3. Check docker logs with following command if Service is running successfully,

```
docker logs -f --tail 100 azilen-notification-api-service
```

4. On successful run, docker logs should print this line `Application 'AzilenNotificationService' is running!`.
5. Service would be running on port number `38086`.

Feel free to connect on [hushen.savani@azilen.com]

[Git]: https://www.github.com

[hushen.savani@azilen.com]: mailto:hushen.savani@azilen.com

[http://localhost:8086]: http://localhost:8086
