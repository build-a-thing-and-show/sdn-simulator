# SDN Simulator

A Spring Boot application for SDN simulation with Docker containerization support.

## Prerequisites

- Docker and Docker Compose installed
- WSL2 (if using Windows)
- Java 17 (for local development)
- Maven (for local development)

## Running the Application

### Development Environment

The development environment includes Spring Boot DevTools for automatic restarts when code changes are detected.

```bash
# Build and run in development mode
docker-compose -f docker-compose.dev.yml up --build

# Run in background
docker-compose -f docker-compose.dev.yml up -d --build

# View logs
docker-compose -f docker-compose.dev.yml logs -f

# Stop development environment
docker-compose -f docker-compose.dev.yml down
```

**Features in Development Mode:**
- Auto-restart on code changes
- LiveReload support
- Volume mounting for source code
- Maven cache persistence

### Production Environment

The production environment uses an optimized build with pre-compiled JAR files.

```bash
# Build and run in production mode
docker-compose up --build

# Run in background
docker-compose up -d --build

# View logs
docker-compose logs -f

# Stop production environment
docker-compose down
```

**Features in Production Mode:**
- Optimized Docker image
- Pre-compiled JAR execution
- Minimal resource usage

## Accessing the Application

Once running, the application will be available at:
- **URL:** http://localhost:8080/
- **Response:** "Hello World"

## Project Structure

```
sdn-simulator/
├── src/
│   └── main/
│       └── java/
│           └── com/batash/simulator/sdn_simulator/
│               ├── SdnSimulatorApplication.java
│               └── HelloController.java
├── docker-compose.yml          # Production configuration
├── docker-compose.dev.yml      # Development configuration
├── Dockerfile                  # Production Dockerfile
├── Dockerfile.dev              # Development Dockerfile
├── .dockerignore
└── pom.xml
```

## Development Workflow

1. Make changes to your Java code
2. Save the files
3. Spring Boot DevTools will automatically detect changes and restart the application
4. Refresh your browser to see the changes

## Troubleshooting

### Port Already in Use
If port 8080 is already in use, stop the conflicting service or change the port mapping in docker-compose files.

### Docker Permission Issues (WSL)
Make sure Docker Desktop has WSL integration enabled in Settings → Resources → WSL Integration.

### Container Won't Start
Check logs with:
```bash
docker-compose logs sdn-simulator
```

## Useful Commands

```bash
# Remove all containers and images
docker-compose down --rmi all

# Remove volumes
docker-compose down -v

# Rebuild without cache
docker-compose build --no-cache

# Check container status
docker ps
```