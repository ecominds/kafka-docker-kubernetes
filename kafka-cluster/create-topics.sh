#!/bin/bash

#echo "Waiting for Kafka to be ready..."
#sleep 10  # Allow time for Kafka to fully start

kafka-topics.sh --bootstrap-server localhost:9092 --create --if-not-exists --topic ecominds-topic --partitions 3 --replication-factor 1

kafka-topics.sh --bootstrap-server localhost:9092 --create --if-not-exists --topic test-topic --partitions 1 --replication-factor 1

echo "2 Topics are created"