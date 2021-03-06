# ms-genetics-rest
#### VISION GENERAL
 
API REST que analiza y evalua si un ADN pertenece a un individuo "mutante" o "humano". 
El mismo esta pensado para tolerar flucuationes agresivas de trafico, ya que fue diseñado 
con un patron de mensajeria pub/sub, desacoplando el analisis y respuesta del guardado 
en la base de datos.

El microservicio cuenta con 2 endpoints expuestos:
* /mutant   (__POST__)      "Evalua el ADN enviado"
* /stats    (__GET__)       "Devuelve las estadisticas de mutantes, humanos y ratio"

Respuesta de la API
* Si el ADN enviado para ser evaluado es de un `mutante`, el servicio responde un codigo **HTTP 200-OK**
* Si el ADN enviado para ser evaluado es de un `humano`, el servicio responde un codigo **HTTP 203-FORBIDDEN**
* Si el ADN enviado no es de dimension `NxN`, el servicio responde un codigo **HTTP 400-BAD_REQUEST**
* Si por alguna razon: conexion fallida con Redis, con Postgres o un error no contemplado, el servicio responde un codigo **HTTP 500-INTERNAL_SERVER_ERROR**

#### EJEMPLO DE USO

*Evaluar ADN*

- URL: http://localhost:8080/api/v1.0/genetics/mutant
- METHOD: POST
- BODY: 
```
{
    "dna": ["AGAA", "AACC", "ACGC", "TTTT"]
}
```

*Obtener Estadisticas*

- URL: http://localhost:8080/api/v1.0/genetics/stats
- METHOD: GET


#### AMBIENTE LOCAL (PRUEBAS Y DESARROLLO)

Levantar los contenedores de Postgres y Redis

1) docker run --name mutant-redis -d redis
2) docker run --name murant-db -d -it -p 5432:5432 -e POSTGRES_PASSWORD=password -e POSTGRES_DB=genetics postgres:latest -c max_connections=200

- En Linux usar `psql -h localhost -p 5432 --username=postgres --dbname=genetics` y ejecutar los scripts que se encuentran en ms-genetics-rest/.../resources/sql/__model_genetics_db.sql__ con el fin de crear el schema y la tabla

**Desde Git Hub**

1) git clone https://github.com/rodrisellanes/ms-genetics-rest.git 
2) cd ms-genetics-rest
3) ./gradlew bootRun

Crear una imagen de Docker del ms

4) cd ms-genetics-rest
5) docker build -f Dockerfile -t ms-genetics-rest:[tag] .

**Desde Docker Hub**

1) docker pull rodrisella/ms-genetics-rest:[tag] (latest)
2) docker run --network host -p 8080:8080 ms-genetics-rest:[tag]
 
**Link Postman-collection**

URL: https://www.getpostman.com/collections/8185d838ed2657365a93

En el proyecto `.../root/postman-collection/` se encuentra el JSON de postman para importar manual 

NOTA: En caso de parecer mas practico usar docker-compose (postgres, redis, ms-genetics-rest)
