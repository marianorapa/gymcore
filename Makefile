# Variables
DOCKER_COMPOSE_FILE := docker-compose.yml

# Default target: start all services
.PHONY: up
up:
	docker compose -f $(DOCKER_COMPOSE_FILE) up -d

# Target to start only the database service
.PHONY: db
db:
	docker compose -f $(DOCKER_COMPOSE_FILE) up -d db

# Target to stop all services
.PHONY: down
down:
	docker compose -f $(DOCKER_COMPOSE_FILE) down

# Target to stop the database service only
.PHONY: stop-db
stop-db:
	docker compose -f $(DOCKER_COMPOSE_FILE) stop db

# Target to build all services
.PHONY: build
build:
	docker compose -f $(DOCKER_COMPOSE_FILE) build

# Target to rebuild the application service
.PHONY: rebuild-app
rebuild-app:
	docker compose -f $(DOCKER_COMPOSE_FILE) up -d --build app

.PHONY: build-publish
build-publish:
	docker build --platform linux/amd64 -t marianrap/gymcore:latest .
	docker login
	docker push marianrap/gymcore:latest

.PHONY: build-publish-beta
build-publish-beta:
	docker build --platform linux/amd64 -t marianrap/gymcore:beta .
	docker login
	docker push marianrap/gymcore:beta
