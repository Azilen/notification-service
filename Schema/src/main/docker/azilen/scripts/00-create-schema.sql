DO
$do$
BEGIN
   CREATE SCHEMA IF NOT EXISTS notifications;
   create extension IF NOT EXISTS "uuid-ossp" schema notifications version "1.1";
END
$do$;
