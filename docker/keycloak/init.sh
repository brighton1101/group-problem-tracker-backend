#!/bin/bash


docker run --rm  \
   --name keycloak \
   --net keycloak-network \
   -p 8080:8080 \
   -e KEYCLOAK_USER=admin \
   -e KEYCLOAK_PASSWORD=admin \
   -e DB_VENDOR=mysql \
   -e DB_ADDR=mysql \
   -e DB_PORT=3306 \
   -e DB_DATABASE=keycloak \
   -e DB_USER=keycloak \
   -e DB_PASSWD=password \
   -it quay.io/keycloak/keycloak 
#in the future this should be re-added
#--net keycloak-network \
