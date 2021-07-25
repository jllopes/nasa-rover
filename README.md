# Nasa Rover
This project provides a REST service that mimics a Nasa Rover by allowing various movement instructions to be introduced.

## How to run

### Docker Compose
To run this service you can run `docker-compose up web` and you will have a service listening to requests on http://localhost:8080.

After a change in the code you can run `docker-compose up --build web` and update the executed container.

### SBT
#### Prerequisites
* Java JDK 8+
* SBT

To run the application using SBT you can do so with `sbt run`

## Usage
For now, only one endpoint is available for the application at http://localhost:8080/navigate

To call the `/navigate` endpoint you need to pass JSON object with a command string.

Example:

```shell
curl -X POST 'localhost:8080/navigate' -H 'Content-Type: application/json' -d '{"command": "FRF}'
```