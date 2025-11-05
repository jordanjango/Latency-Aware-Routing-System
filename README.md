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
