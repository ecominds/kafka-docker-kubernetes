#!/bin/bash

#echo "Waiting for Kafka to be ready..."
#sleep 10  # Allow time for Kafka to fully start

cat <<EOF > /tmp/client.properties
security.protocol=SASL_PLAINTEXT
sasl.mechanism=PLAIN
sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username="kafkaclient" password="kafkasecret";
EOF


kafka-topics.sh --bootstrap-server localhost:9092 --command-config /tmp/client.properties --list


kafka-topics.sh --bootstrap-server localhost:9092 --command-config /tmp/client.properties --list

kafka-topics.sh --bootstrap-server localhost:9092 --command-config /tmp/client.properties --create --if-not-exists --topic ecominds-topic --partitions 3 --replication-factor 1

kafka-topics.sh --bootstrap-server localhost:9092 --command-config /tmp/client.properties --create --if-not-exists --topic test-topic --partitions 1 --replication-factor 1

echo "2 Topics are created"