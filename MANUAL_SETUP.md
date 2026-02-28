# Fleet Admin - Manual Setup Guide

This guide walks through installing the required infrastructure services manually (without Docker).

---

## 1. Install PostgreSQL with PostGIS

### Step 1.1: Download PostgreSQL
1. Go to: https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
2. Download PostgreSQL 15.x for Windows
3. Run the installer

### Step 1.2: Install PostgreSQL
- Set password: `art3ZH9gMzxjDR1L` (matches application.properties)
- Port: 5432 (default)
- Check "Install Stack Builder" for later

### Step 1.3: Install PostGIS
1. Open **Stack Builder** (installed with PostgreSQL)
2. Select your PostgreSQL instance
3. Navigate to **Spatial Extensions** → Check **PostGIS**
4. Complete the installation

### Step 1.4: Create Database
1. Open **pgAdmin 4** (installed with PostgreSQL)
2. Right-click "Databases" → Create Database
3. Name: `fleet_logistics`
4. Done! (PostGIS will be enabled automatically when Spring Boot starts)

---

## 2. Install Kafka

### Option A: ZIP Download (Recommended)

1. Download Kafka: https://archive.apache.org/dist/kafka/3.6.0/kafka_2.13-3.6.0.tgz
2. Extract to `C:\kafka`

### Option B: Using Windows Package Manager
```
powershell
winget install Apache.Kafka
```

### Step 2.1: Start ZooKeeper
```
cmd
cd C:\kafka
bin\windows\zookeeper-server-start.bat config\zookeeper.properties
```

### Step 2.2: Start Kafka (in new terminal)
```
cmd
cd C:\kafka
bin\windows\kafka-server-start.bat config\server.properties
```

---

## 3. Install Redis

### Option A: ZIP Download

1. Download: https://github.com/tporadowski/redis/releases
2. Extract to `C:\redis`

### Option B: Using Chocolatey
```
powershell
choco install redis
```

### Option C: Using Windows Package Manager
```
powershell
winget install Redis.Redis
```

### Step 3.1: Start Redis
```
cmd
cd C:\redis
redis-server.exe
```

---

## 4. Verify All Services Running

After starting all services, verify:

| Service | Port | Test Command |
|---------|------|--------------|
| PostgreSQL | 5432 | `psql -h localhost -U postgres -d fleet_logistics` |
| Kafka | 9092 | Check no errors in terminal |
| Redis | 6379 | `redis-cli ping` → should return PONG |

---

## 5. Start the Application

### Start Backend
```
cmd
cd Fleet_admin\fleet-backend
mvnw spring-boot:run
```

### Start Frontend (in new terminal)
```
cmd
cd Fleet_admin\fleet-frontend
npm install
npm run dev
```

---

## 6. Test the Application

1. Open browser: http://localhost:5173
2. You should see the map
3. Start FleetSimulator to generate data

---

## Quick Reference: Service Ports

| Service | Port | Config Match |
|---------|------|--------------|
| PostgreSQL | 5432 | ✅ matches application.properties |
| Kafka | 9092 | ✅ matches application.properties |
| Redis | 6379 | ✅ matches application.properties |
| Backend | 8080 | (default) |
| Frontend | 5173 | (Vite default) |
