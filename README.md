# Run Kafka on Kubernetes
Kafka is an open-source distributed stream processing framework. It was developed at Linkedin and open sourced in 2011.

- It runs on a cluster of one or more servers knwon as brokers which can be located in multiple data centres.

- The kafka cluster stores streams of records (messages) in categories called Topics.

- Zookeeper is a coordination service for distributed applications. It maintains which brokers are alive and available and provides failure notifications.

- Zookeeper also performs the leader selection from the available brokers if the existing one goes down.

## Kafka Structure
Below is the structure of a Kafka cluster
![kafka-structure](images/00-kafka-structure.jpg?raw=true "Kafka Structure")

## Advantages of Using Kubernetes with Kafka :

- Easy to deploy and manage a new cluster
- Easy to manage Kafka and to scale up
- Easy to perform configuration changes, upgrades, and restarts


## Create a kubernetes cluster on GCP
Create a kubernetes cluster, i.e 'kafka-cluster'

Ref: https://console.cloud.google.com/kubernetes/add?project=ecominds

<details>
<summary><b>See details in screenshot</b></summary>

![kubernetes-cluster](images/01-kubernetes-cluster.jpg?raw=true "kubernetes Cluster")

</details>

<details>
<summary><b>Command Line of above action to create a Kubernetes cluster</b></summary>

```
$ gcloud beta container --project "ecominds" clusters create "kafka-cluster" --no-enable-basic-auth \ 
--cluster-version "1.24.9-gke.3200" --release-channel "regular" --machine-type "e2-medium" --image-type "COS_CONTAINERD" \ 
--disk-type "pd-balanced" --disk-size "100" --metadata disable-legacy-endpoints=true \ 
--scopes "https://www.googleapis.com/auth/devstorage.read_only","https://www.googleapis.com/auth/logging.write", \ 
"https://www.googleapis.com/auth/monitoring","https://www.googleapis.com/auth/servicecontrol", \ 
"https://www.googleapis.com/auth/service.management.readonly","https://www.googleapis.com/auth/trace.append" \ 
--num-nodes "3" --logging=SYSTEM,WORKLOAD --monitoring=SYSTEM --enable-ip-alias --network "projects/ecominds/global/networks/default" \ 
--subnetwork "projects/ecominds/regions/us-central1/subnetworks/default" --no-enable-intra-node-visibility \ 
--default-max-pods-per-node "110" --no-enable-master-authorized-networks --addons HorizontalPodAutoscaling,HttpLoadBalancing, \
GcePersistentDiskCsiDriver --enable-autoupgrade --enable-autorepair --max-surge-upgrade 1 --max-unavailable-upgrade 0 \ 
--enable-shielded-nodes --node-locations "us-central1-c"
```
![kubernetes-cluster](images/02-kubernetes-cluster.jpg?raw=true "kubernetes Cluster")

</details>

## Configure Kafka
Follow the below steps to configure Kafka

![kafka-deployment](images/03-kafka-deployment.jpg?raw=true "Kafka Deployment")

### Step 1 - Open cloud console
```
# Run the below command to fetch the correct configuration
# Syntax: gcloud container clusters get-credentials <CLUSTER_NAME> --region=<COMPUTE_REGION> --project=<PROJECT_ID>
$ gcloud container clusters get-credentials kafka-cluster --zone us-central1-c --project ecominds
Fetching cluster endpoint and auth data.
kubeconfig entry generated for kafka-cluster.


# Clone the git repository `kafka-docker-kubernetes`
$ git clone https://github.com/ecominds/kafka-docker-kubernetes.git
$ cd kafka-docker-kubernetes
```
### Step 2 - Create a namespace
```
# Execute below command to create a namespace 'kafka'
$ kubectl apply -f 00-namespace.yaml 
```

### Step 3 - Create `zookeeper` service
```
$ kubectl apply -f 01-zookeeper.yaml
```

### Step 4 - Create `kafka` service
```
$ kubectl apply -f 02-kafka.yaml
```

## Verify running services
```
$ kubectl -n kafka get all
```
![kafka-deployment](images/04-kafka-deployment.jpg?raw=true "Kafka Deployment")

### Test Apache Kafka
![kafka-deployment](images/05-kafka-deployment.jpg?raw=true "Kafka Deployment")

**1. Create topic**
```
$ export KAFKA_POD_NAME="$(kubectl -n kafka get pods  --selector=app=kafka-broker -o jsonpath='{.items[0].metadata.name}')"

$ kubectl -n kafka exec -it $KAFKA_POD_NAME -- kafka-topics.sh --create --zookeeper zookeeper-service:2181 --replication-factor 1 --partitions 1 --topic kafkatopic1
```

**2. Start a Kafka message consumer** 
This consumer will connect to the cluster and retrieve and display messages as they are published to the `kafkatopic1` topic

```
$ kubectl -n kafka exec -it $KAFKA_POD_NAME -- kafka-console-consumer.sh --bootstrap-server kafka-service:9092 --topic kafkatopic1  --from-beginning --max-messages 10
```

**3. Publish message** (Start in a new console)
```
# First, set the kafka pod name
$ export KAFKA_POD_NAME="$(kubectl -n kafka get pods  --selector=app=kafka-broker -o jsonpath='{.items[0].metadata.name}')"

$ kubectl -n kafka exec -it $KAFKA_POD_NAME -- kafka-console-producer.sh --broker-list kafka-service:9092 --topic kafkatopic1 --producer.config /opt/kafka/config/producer.properties
```
![kafka-deployment](images/05-kafka-deployment.jpg?raw=true "Kafka Deployment")