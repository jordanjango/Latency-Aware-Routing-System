# Kubernetes

### 1. Cluster

A **cluster** is a set of machines running together under the same environment to provide scalability and high availability.

**Components of a Cluster:**

1. **API Server** – Serves API requests for managing the nodes and cluster resources.
2. **etcd** – A key-value store that maintains the state of the cluster.
3. **Scheduler** – Assigns pods to nodes based on resource availability and constraints.
4. **Kubelet** – An agent that runs on each node, monitors and ensures pods are running as expected.
5. **Controller Manager** – Ensures that the current state matches the desired state by creating replicas, restarting pods, etc.
6. **kubectl** – CLI tool to interact with the cluster.
7. **Kube Proxy** – Enables communication between pods and services within the cluster.

**Controllers in Kubernetes:**
A controller is a control loop that continuously monitors the cluster state and reconciles it with the **desired state** specified in YAML files. Kubernetes is **declarative**, so whatever you define in YAML, the controller ensures it is reflected in the cluster.

---

### 2. Node

A **node** is a physical or virtual machine within the cluster that runs pods.

* Each node can host multiple pods, the smallest deployable unit in Kubernetes.
* For local testing, a single node is enough, but in production, multiple nodes improve **scalability** and **availability**.

---

### 3. Pods

**Pods** are the smallest unit in Kubernetes and are used to run one or more containers.

* Each pod has its own IP and storage space.
* Pods provide isolation and allow containers within them to communicate internally.

**Containers inside pods:**

* A **container** is a standalone executable package containing your code, libraries, and dependencies.
* Containers share the **host OS kernel** to run and can be thought of as lightweight virtual machines.

---

# Lifecycle of Pods

Before diving into pods, it’s important to understand **how Docker works**, since pods run containers.

---

# What is Docker and How Does It Work?

**Docker** is an **OS-level virtualization platform** that allows applications to run smoothly in isolated environments called containers.

---

### Docker Architecture

Docker follows a **client-server architecture**:

1. **Docker Client** – The terminal or CLI used to interact with Docker. It triggers API calls.
2. **Docker Daemon** – The background service that listens to Docker API requests and manages containers.
3. **Docker Host** – The machine (physical or virtual) that runs Docker.
4. **Docker Registry** – Stores Docker images. Can be:

   * **Public registry** (e.g., Docker Hub)
   * **Private registry** (used by organizations for proprietary images)

---

### Docker Objects

1. **Image** – Blueprint defining what a container should contain.
2. **Container** – A running instance of an image, isolated from the host system.
3. **Storage** – Persists data even if containers crash. Types:

   * **Volume** – Shared section on the host system for persistent storage
   * **Bind Mount** – Directly maps files or directories from the host to the container
   * **tmpfs Mount** – Temporary, in-memory storage, not persisted to disk
4. **Network** – Enables communication between containers and the host.

---

### How Containers Run

A traditional **VM** runs on a **hypervisor**:

* Hypervisor manages hardware resources for VMs.
* VMs are heavy because:

  1. Each VM has a full OS
  2. Each VM consumes GBs of disk space and RAM

```
Hardware
    |
Hypervisor
------------
VM-1 = OS + Applications
VM-2 = OS + Applications
```

---

**Containers are lightweight** because they run on the **host OS kernel**, sharing resources directly:

* **Kernel** = Interface between hardware and software
* Manages CPU, memory, disk, and network
* Provides isolation via **namespaces** and **cgroups**

```
+------------------------------------------------------+
|                  Physical Hardware                  |
|   CPU, Memory, Disk, Network                         |
+------------------------------------------------------+
                ^
                | (resource access)
                |
+------------------------------------------------------+
|                  Host OS Kernel                     |
|  - Process scheduling                                |
|  - Memory management                                 |
|  - I/O handling (disk, network)                      |
|  - Namespaces & cgroups (container isolation)       |
+------------------------------------------------------+
                ^
                | (isolated execution environment)
                |
+----------------------------+   +----------------------------+
|        Container 1         |   |        Container 2         |
|  - App + libraries         |   |  - App + libraries        |
|  - Filesystem from image   |   |  - Filesystem from image  |
|  - Uses host kernel syscalls|   |  - Uses host kernel syscalls|
+----------------------------+   +----------------------------+
```

**Example:**

* An app inside a container performing a sum:

  * CPU executes instructions via **host kernel scheduling**
  * Network calls use **host network interfaces** via kernel mediation
  * Disk writes (if using a volume) are handled by the host filesystem

This **sharing of resources** makes Docker containers lightweight and efficient compared to VMs.

---

### Summary

* **Kubernetes Pods** run containers.
* **Containers** share the host kernel to execute processes efficiently.
* **Docker** enables this OS-level virtualization.
* **Kubernetes** orchestrates these containers across multiple nodes, maintaining the desired state and providing scalability.


Kubernetes cluster consists of multiple nodes.
Each node provides an environment with resources such as CPU, RAM, storage, and network.
Pods run on nodes and request the resources they need.
Each pod can contain one or more containers, which may run different services like backend, frontend, database, cache, etc.
Containers are isolated from each other and the host system but share the host kernel to access hardware resources.
Any request or instruction from a container (e.g., CPU computation, memory allocation, disk I/O, or network access) goes through the host kernel, which manages access to the physical hardware.
                                                
                                                
                                                x-----------------------------------------------x

# Message Queue

## 1️⃣ What are message queues?

- Message queues are **architectural components** that enable **asynchronous and decoupled communication** between services or nodes.

---

## 2️⃣ Why do we need message queues?

- To make services **asynchronous**, so they don’t have to wait for each other.
- To **decouple services**, reducing dependency and preventing one service failure from affecting others.

---

## 3️⃣ Which pattern does a message queue implement?

- Message queues implement the **Publisher-Subscriber (Pub-Sub) model**.
- **Publisher** → sends messages to a queue/topic.
- **Subscriber/Consumer** → receives messages independently.

---

## 4️⃣ Advantages of message queues

1. **Asynchronous communication** → non-blocking and efficient.
2. **Decoupling** → services are independent; a faulty node doesn’t crash the system.
3. **Scalability** → producers and consumers can scale independently.

---

## 5️⃣ Problems and solutions

### a) Producer faster than consumer

- **Problem:** Publisher sends events faster than consumers can process → lag or memory overflow.
- **Solutions:**

  1. **Backpressure:** Slow down the publisher if throughput is too high.
  2. **Scale consumers horizontally:** Multiple consumers share the load.

---

### b) Node outages

- **Problem:** When a node goes down, queued data may not be consumed immediately.
- **Solution:** Replicate data in brokers to ensure **high availability** and prevent data loss.

---

### c) Head-of-Line (HOL) blocking

- **Problem:** A failing message at the front of the queue can block subsequent messages.
- **Solution:**

  1. Implement **fixed retry mechanisms**.
  2. Discard or move the problematic message so other messages can continue processing.



-----------------------------------------------------------------------------------------------------------------
# Kafka Overview

Kafka is a **distributed, highly fault-tolerant event streaming system** and message queue for building scalable applications. It is **topic-based** and supports **high-throughput, low-latency message delivery**.

---

## Key Components

* **Producer**: Sends events/messages to Kafka topics.
* **Consumer**: Reads events/messages from Kafka topics.
* **Topic**: Logical channel that organizes messages; messages are sent to a topic.
* **Partition**: Each topic can be divided into multiple partitions for **parallelism, scalability, and availability**.
* **Broker**: Kafka server that stores partitions.
* **ZooKeeper / KRaft**: Manages Kafka cluster metadata, leader election, and cluster state.

---

## Kafka Architecture

```
       +---------------------+
       |       Producer      |
       +---------+-----------+
                 |
                 v
              Topic: Orders
         +-------+-------+-------+
         | P0    | P1    | P2    |
         +-------+-------+-------+
           |       |       |
           v       v       v
        +-----+ +-----+ +-----+
        | B1  | | B2  | | B3  |
        +-----+ +-----+ +-----+
           |       |       |
        Replication across brokers
           |
           v
       +-------+
       |Consumer|
       +-------+
```

* **Topic** = logical container
* **Partition** = physical queue-like storage
* **Broker** = server storing partitions
* **Consumer** reads from leader partitions

---

## Partition Assignment Algorithms

1. **Key-based Partitioning**

   * Messages with the same key go to the same partition.
   * Example: `{key: P0, message_1}` → Partition P0

2. **Hashing Primary Key**

   * Use `partition = hash(key) % number_of_partitions`
   * Ensures messages with same key always go to the same partition.

3. **Round-Robin Assignment**

   * Messages are assigned to partitions in a rotating order.

---

## Replication for Fault Tolerance

* Kafka uses **leader-follower replication**:

  * **Leader** handles reads/writes.
  * **Followers** replicate data from the leader.
* **Leader election**: Kafka automatically chooses a leader. If a broker fails, a follower in sync becomes the new leader.

**Example: Replication Factor = 2**

| Partition | Brokers storing data |
| --------- | -------------------- |
| P0        | B1, B2               |
| P1        | B2, B3               |
| P2        | B3, B1               |

Broker storage:

```
B1 -> [P0 (Leader), P2 (Follower)]
B2 -> [P0 (Follower), P1 (Leader)]
B3 -> [P1 (Follower), P2 (Leader)]
```

* **If B1 goes down**:

  * P0 leader → B2
  * P2 leader → B3
  * Updated replication table:

| Partition | Leader | Follower |
| --------- | ------ | -------- |
| P0        | B2     | None     |
| P1        | B2     | B3       |
| P2        | B3     | None     |

---

## Producer Workflow

```
Producer (Order Service)
          |
          v
       Topic: Orders (logical container)
          |
          v
   Partition Assignment (key/round-robin)
          |
          v
  Partition P0 (Leader on B1, Follower on B2)
          |
      Stored on Broker(s)
```

* Producer sends message → assigned partition → leader broker stores it → followers replicate.
* Consumers read from the leader of each partition.




# Kafka Partition Structure

Kafka partitions are the **physical unit of storage** for a topic and behave like **logs (queues)**. Each partition stores messages in order and assigns each message a unique **offset**.

---

## 1. Partition Basics

* **Ordered**: Messages are always in the order they were produced.
* **Immutable**: Messages, once written, cannot be changed.
* **Replicated**: Can have copies on multiple brokers for fault tolerance.
* **Offset**: Unique sequential ID for each message in the partition.

---

## 2. Partition Structure (Conceptual)

```
Partition P0
----------------------------
| Offset | Message        |
----------------------------
| 0      | {"orderId":1} |
| 1      | {"orderId":2} |
| 2      | {"orderId":3} |
| 3      | {"orderId":4} |
| ...    | ...            |
----------------------------
```

* Each row = **one message**
* **Leader broker** stores the partition and handles reads/writes
* **Follower brokers** replicate the partition

---

## 3. Partition Across Brokers

Partitions are stored on brokers according to the **replication factor**.

**Example: Replication Factor = 2**

| Partition | Leader | Follower |
| --------- | ------ | -------- |
| P0        | B1     | B2       |
| P1        | B2     | B3       |
| P2        | B3     | B1       |

* **Leader** handles reads/writes
* **Followers** replicate asynchronously
* If a broker fails, a follower becomes the new leader

---

**Key takeaway**: A Kafka partition is essentially a **log of messages stored in order**, with unique offsets for each message, and replicated across brokers for fault tolerance.
