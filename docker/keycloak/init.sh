#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

docker run --rm  \
   --name keycloak \
   --net keycloak-network \
   -p 8180:8180 \
   -e KEYCLOAK_USER=admin \
   -e KEYCLOAK_PASSWORD=admin \
   -e DB_VENDOR=mysql \
   -e DB_ADDR=mysql \
   -e DB_PORT=3306 \
   -e DB_DATABASE=keycloak \
   -e DB_USER=keycloak \
   -e DB_PASSWD=password \
   -e KEYCLOAK_LOGLEVEL=DEBUG \
   -v $DIR:/tmp \
   -it quay.io/keycloak/keycloak \
   -Djboss.http.port=8180
#in the future this should be re-added
#--net keycloak-network \
