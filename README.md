# ms-genetics-rest
Lookup for mutant DNA


1) Comando para levantar un Postgres

 docker run --rm -d -it --network host -p 5432:5432 -e POSTGRES_PASSWORD=password -e POSTGRES_DB=genetics postgres:latest -c max_connections=200
 
 1) Comando para crear Docker image
 
 .../ms-genetics-rest/docker build -f Dockerfile -t ms-genetics-rest:1.0.0 .
