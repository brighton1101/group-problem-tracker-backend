#!/bin/bash

mkdir ~/mongodb

docker run \
	-d \
	-p 27017:27017 \
	-v ~/mongodb:/data/db \
	mongo:latest
