#!/usr/bin/env bash
docker run --name mysqlCC -p 3306:3306 \
-e MYSQL_ROOT_PASSWORD=1234 \
-e MYSQL_USER=demo_java \
-e MYSQL_PASSWORD=1234 \
-e MYSQL_DATABASE=pro \
-d mysql/mysql-server:8.0.19
