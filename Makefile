.PHONY: build
build:
	#mvn clean package
	docker compose up --build

PHONY: run
run:
	#mvn clean package
	docker compose up --build -d

PHONY: stop
stop:
	docker compose down
