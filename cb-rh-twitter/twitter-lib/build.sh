#!/usr/bin/env bash

docker build -t ezeev/twitter-streamer:latest .
docker push ezeev/twitter-streamer:latest