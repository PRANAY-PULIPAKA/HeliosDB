<div align="center">

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=700&size=30&pause=1000&color=FF6B35&center=true&vCenter=true&width=700&lines=HELIOSDB+%E2%80%94+In-Memory+KV+Store;Built+from+Scratch+in+Java;Redis-Inspired+%7C+LLD+Showcase;TCP+%7C+TTL+%7C+Persistence+%7C+Concurrent" alt="Typing SVG" />

<br/>

```
██╗  ██╗███████╗██╗     ██╗ ██████╗ ███████╗██████╗ ██████╗ 
██║  ██║██╔════╝██║     ██║██╔═══██╗██╔════╝██╔══██╗██╔══██╗
███████║█████╗  ██║     ██║██║   ██║███████╗██║  ██║██████╔╝
██╔══██║██╔══╝  ██║     ██║██║   ██║╚════██║██║  ██║██╔══██╗
██║  ██║███████╗███████╗██║╚██████╔╝███████║██████╔╝██████╔╝
╚═╝  ╚═╝╚══════╝╚══════╝╚═╝ ╚═════╝ ╚══════╝╚═════╝ ╚═════╝
              In-Memory Key-Value Store
```

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Protocol-TCP%20%2F%20Socket-FF6B35?style=for-the-badge&logo=cloudflare&logoColor=white"/>
  <img src="https://img.shields.io/badge/Concurrency-Multi--Threaded-4ECDC4?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Persistence-AOF%20Log-A855F7?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Status-Active-22C55E?style=for-the-badge"/>
</p>

<p align="center">
  <b>A production-inspired, thread-safe, in-memory key-value store built from scratch in Java.</b><br/>
  HeliosDB simulates the internals of Redis — TCP server, TTL eviction, persistence, and concurrent access — using only core Java. No frameworks. No shortcuts.
</p>

</div>

---

## 🧭 Table of Contents

- [Why HeliosDB?](#-why-heliosdb)
- [Architecture](#-architecture)
- [Features](#-features)
- [Supported Commands](#-supported-commands)
- [Design Deep Dive](#-design-deep-dive)
- [Getting Started](#-getting-started)
- [Example Session](#-example-session)
- [Project Structure](#-project-structure)
- [Design Patterns Used](#-design-patterns-used)
- [Future Roadmap](#-future-roadmap)
- [Key Learnings](#-key-learnings)

---

## 💡 Why HeliosDB?

> *"You don't truly understand a system until you've built it."*

Most developers use Redis daily but rarely understand what happens under the hood. HeliosDB tears open that black box:

- How does Redis handle **thousands of concurrent clients**?
- How does **TTL expiration** work — lazy vs active eviction?
- How does Redis **survive a restart** without losing data?
- How is a **TCP command protocol** parsed and executed end-to-end?

**HeliosDB** answers all of these — built entirely from scratch, without external frameworks.

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      CLIENT LAYER                           │
│              Telnet / Netcat / Custom Client                │
└──────────────────────────┬──────────────────────────────────┘
                           │  TCP Connection (port 6379)
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                      TCP SERVER                             │
│          ServerSocket → accepts connections                 │
│          Spawns a new Thread per client                     │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                   COMMAND PARSER                            │
│       Parses raw text → Command object (cmd + args)         │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                    SERVICE LAYER                            │
│       Routes commands → Executes business logic             │
│       Writes to append-only log for persistence             │
└────────────┬─────────────────────────────┬──────────────────┘
             │                             │
             ▼                             ▼
┌────────────────────┐         ┌───────────────────────┐
│   IN-MEMORY STORE  │         │   PERSISTENCE LAYER   │
│  ConcurrentHashMap │         │   Append-Only Log     │
│  + TTL ExpiryMap   │         │   (helios.aof)        │
└────────────────────┘         └───────────────────────┘
             ▲
             │
┌────────────────────┐
│   TTL SCHEDULER    │
│  Background sweep  │
│   every 1 second   │
└────────────────────┘
```

---

## ✨ Features

| Feature | Description | Implementation |
|--------|-------------|----------------|
| 🔌 **TCP Server** | Accepts concurrent client connections | `ServerSocket` + thread-per-client |
| 🗃️ **Key-Value Store** | Fast in-memory storage | `ConcurrentHashMap<String, String>` |
| ⏱️ **TTL Support** | Keys auto-expire after N seconds | Lazy + active dual eviction |
| 🔒 **Thread Safety** | Multiple clients safely served | `ConcurrentHashMap`, atomic ops |
| 🔄 **Persistence** | Survive server restarts | Append-only log (AOF) |
| 🧹 **Background Cleanup** | Expired keys purged proactively | `ScheduledExecutorService` |
| 📟 **Command Protocol** | Human-readable command interface | Custom parser — extensible design |

---

## 📟 Supported Commands

```bash
# ── WRITE ──────────────────────────────────────────────────────────
SET  <key> <value>           # Store a value
SET  <key> <value> <ttl>     # Store with TTL in seconds

# ── READ ───────────────────────────────────────────────────────────
GET    <key>                 # Retrieve value (nil if missing/expired)
EXISTS <key>                 # 1 if key exists, 0 otherwise
KEYS   *                     # List all active (non-expired) keys

# ── DELETE ─────────────────────────────────────────────────────────
DEL  <key>                   # Remove a key immediately
```

### Quick Reference

| Command | Syntax | Response | Example |
|---------|--------|----------|---------|
| `SET` | `SET key value [ttl]` | `OK` | `SET city delhi 60` |
| `GET` | `GET key` | value or `(nil)` | `GET city` |
| `DEL` | `DEL key` | `1` deleted / `0` not found | `DEL city` |
| `EXISTS` | `EXISTS key` | `1` / `0` | `EXISTS city` |
| `KEYS` | `KEYS *` | list of keys | `KEYS *` |

---

## 🔬 Design Deep Dive

### 1. Concurrency Model — Thread-Per-Client

```
New TCP connection arrives
          │
          ▼
 ServerSocket.accept()  ← blocking call on main thread
          │
          ▼
 new Thread(new ClientHandler(socket)).start()
          │
          ▼
 Each thread independently:
    read input → parse → execute → write response
```

- Each client gets a **dedicated thread** — simple, predictable, debuggable
- Shared state accessed only via `ConcurrentHashMap` — no explicit locking required
- Scales well for moderate concurrency (hundreds of simultaneous clients)

---

### 2. TTL — Dual Eviction Strategy

HeliosDB uses the **exact same strategy as Redis** — combining lazy and active expiration:

```
┌─────────────────────────────────────────────────────┐
│               TTL EVICTION STRATEGY                 │
├──────────────────────┬──────────────────────────────┤
│   Lazy Expiration    │   Active Expiration          │
├──────────────────────┼──────────────────────────────┤
│ Triggered on GET     │ Background thread runs       │
│ If expired → (nil)   │ every 1 second               │
│ + delete entry       │ Sweeps all TTL entries       │
│                      │ Removes stale keys           │
├──────────────────────┼──────────────────────────────┤
│ Zero overhead on     │ Prevents memory leaks        │
│ non-accessed keys    │ for keys never re-accessed   │
└──────────────────────┴──────────────────────────────┘
```

**Internal storage:**
```java
ConcurrentHashMap<String, String> store;      // key → value
ConcurrentHashMap<String, Long>   expiryMap;  // key → expiry (epoch ms)
```

---

### 3. Persistence — Append-Only Log (AOF)

```
Client sends:  SET name pranay 60
                    │
                    ▼
         Service executes command
                    │
          ┌─────────┴──────────┐
          ▼                    ▼
  Updates in-memory       Appends to helios.aof:
      store                "SET name pranay 60\n"

On server restart:
          │
          ▼
  AOF replayed line-by-line
          │
          ▼
  Store fully rebuilt to last committed state
  (Expired TTL keys skipped on replay)
```

---

### 4. Command Parser — Extensible Design

```java
// Raw TCP input:  "SET city mumbai 30"
//                        ↓
// Command { cmd="SET", args=["city", "mumbai", "30"] }
//                        ↓
// CommandRouter.route(command) → SetCommand.execute()
```

Adding a new command = implementing one interface. Zero changes to the parser, server, or router.

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- `telnet` or `nc` (netcat) in your terminal

### Build & Run

```bash
# 1. Clone the repository
git clone https://github.com/yourusername/heliosdb.git
cd heliosdb

# 2. Compile
javac -d out src/*.java

# 3. Start the server
java -cp out TCPServer
# HeliosDB started on port 6379...

# 4. Connect from another terminal
telnet localhost 6379
# OR
nc localhost 6379
```

### Run Tests

```bash
javac -cp .:junit-5.jar -d out test/*.java
java -cp out:junit-5.jar org.junit.runner.JUnitCore StoreTest
```

---

## 🖥️ Example Session

```bash
$ nc localhost 6379

SET name pranay
OK

GET name
pranay

SET session abc123 5
OK

GET session
abc123

# ... wait 5 seconds ...

GET session
(nil)

EXISTS name
1

EXISTS session
0

KEYS *
name

DEL name
1

GET name
(nil)
```

---

## 📁 Project Structure

```
Client (Telnet / Netcat)
│
▼
TCP Server (Multi-threaded)
│
▼
Command Parser
│
▼
Command Handler (Command Pattern)
│
▼
Service Layer (KeyValueService)
│
▼
Storage Engine (InMemoryStore)
│
├── ConcurrentHashMap (Thread-safe storage)
├── TTL Manager
│ ├── ExpiryEntry (priority-based expiry)
│ └── Background Cleanup Thread
│
└── Persistence Layer
│
├── AOF Logger (write operations)
└── AOF Loader (recovery on startup)
```

---

## 🧩 Design Patterns Used

| Pattern | Where Applied | Why |
|---------|--------------|-----|
| **Command Pattern** | Each operation is a `Command` object | Decouples parsing from execution; trivial to add new commands |
| **Strategy Pattern** | Dual TTL eviction (lazy + active) | Two interchangeable expiry strategies, independently configurable |
| **Single Responsibility** | Parser / Router / Store / Persistence are all separate classes | Each class has exactly one reason to change |
| **Template Method** | Base `Command` interface with `execute()` | Consistent contract across all command types |
| **Factory Method** | `CommandParser` creates `Command` objects | Encapsulates instantiation logic, hidden from the router |

---

## 🗺️ Future Roadmap

```
✅ Phase 1 — Core (Complete)
   ├── TCP Server with concurrent client support
   ├── SET / GET / DEL / EXISTS / KEYS
   ├── TTL with lazy + active dual eviction
   └── Append-Only Log persistence

🔲 Phase 2 — Production Hardening
   ├── [ ] LRU cache eviction (bounded memory usage)
   ├── [ ] RDB snapshot persistence (point-in-time)
   ├── [ ] AUTH command (password authentication)
   └── [ ] INCR / EXPIRE / TTL / RENAME commands

🔲 Phase 3 — Performance
   ├── [ ] Non-blocking I/O (Java NIO / Netty)
   ├── [ ] Thread pool model (replace thread-per-client)
   └── [ ] Benchmark: ops/sec vs real Redis

🔲 Phase 4 — Distributed Mode
   ├── [ ] Consistent hashing for horizontal sharding
   ├── [ ] Leader election (simplified Raft)
   └── [ ] Primary → Replica replication
```

---

## 📚 Key Learnings

Building HeliosDB from scratch taught me:

**Systems Design**
- Why Redis uses dual TTL eviction — lazy is free, active prevents memory leaks
- How AOF enables crash recovery without requiring periodic snapshots
- Trade-offs between thread-per-client vs thread pool vs event loop (NIO)

**Java Concurrency**
- `ConcurrentHashMap` is safe for individual reads/writes but not for compound operations
- `ScheduledExecutorService` for precise, low-overhead background scheduling
- Thread lifecycle management and graceful socket/resource cleanup with try-with-resources

**Clean Architecture**
- Parser → Router → Handler separation keeps the codebase open for extension
- Command pattern makes adding new operations a matter of adding one file
- Single Responsibility Principle applied consistently at every layer

**Networking**
- TCP is stream-based — input framing and delimiters matter
- `ServerSocket.accept()` is blocking — the main thread should do only this
- Proper socket lifecycle and resource cleanup prevents connection leaks

---

## 🤝 Contributing

Contributions are welcome! If you're implementing a command or a phase from the roadmap:

1. Fork the repo
2. Create a branch: `git checkout -b feature/expire-command`
3. Implement + add tests
4. Open a PR with a clear description of what was added and why

---

## 📄 License

MIT License — free to use, fork, and learn from.

---

<div align="center">

**Built with ☕ Java and a deep curiosity for how things work under the hood.**

*If HeliosDB helped you understand Redis internals or LLD concepts — drop a ⭐, it means a lot!*

</div>
