# Basics

This is a collection of sample projects in several languages I like to work with.
The example shows a basic version of a REST service that allows adding, listing and retrieving a simple Customer record.
The Scala version also offers a gRPC api.

## Setup

Create a Mysql database, e.g. using Docker.

see https://spring.io/guides/gs/accessing-data-mysql

create database for demo:

```
docker-compose up

docker exec -it mysql bash

mysql --password
# password = my-secret-pw

create database db_example;
create user 'springuser'@'%' identified by 'ThePassword';
grant all on db_example.* to 'springuser'@'%';

# create table

use db_example
# copy and past contents of src/main/resources/schema.sql
```

## Test

The implementation in each language offers the following endpoints:

| Endpoint            | Description                |
|---------------------|----------------------------|
| GET /customers      | Json list of all customers |
| GET /customers/{id} | Query customer by uuid     |
| POST /customers     | Save a new customer        |
| PUT /customers      | Update a customer          |

Save a customer
```bash
curl -v -d '{"name":"i1", "email": "a@com"}' \
-H 'Content-Type: application/json' \
-X POST localhost:8080/api/customers
```

Get customer by id
```bash
curl -v localhost:8080/api/customers/4303cdaa-1504-4271-a614-1106daa2e83
```

List all customers
```bash 
curl -v localhost:8080/api/customers
```

Note: the Kotlin version currently requires a trailing / when POSTing a new customer.

## Actuator

The actuator works for Java and Kotlin and shows documentation for each endpoint
```
curl http://localhost:8080/api/actuator/mappings
# endpoints, add /api to get the full url
curl http://localhost:8080/api/actuator/mappings | jq | grep predicate
```

## Front end

`frontend/basics` is a small website implementing basic CRUD functions to manipulate the data in the customer table, 
using one of the backend services. 

Start the front end:

```
npm run build && npm run server
```

Access the user interface

```
http://localhost:8081/
```

## TODO

- show error better message when unique constraint is violated
- flyway?