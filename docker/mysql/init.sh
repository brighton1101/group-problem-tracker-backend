#!/bin/bash
#note that once this is run the first time, you can restart mysql with docker restart mysql
docker run --name mysql -d \
   --net keycloak-network \
   -e MYSQL_DATABASE=keycloak \
   -e MYSQL_USER=keycloak \
   -e MYSQL_PASSWORD=password \
   -e MYSQL_ROOT_PASSWORD=root_password mysql
#in the future this should be readded
#--net keycloak-network \
#docker network create keycloak-network
