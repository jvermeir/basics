# Basics

This is a collection of sample projects in several languages I like to work with. 
The example shows a basic version of a REST service that allows adding, listing and retrieving a simple Customer record.

# Spring experiments

## Mysql

Run database:

```
mkdir data
docker run --name some-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=my-secret-pw -d -v $PWD/data:/var/lib/mysql mysql:8.3.0
```

see https://spring.io/guides/gs/accessing-data-mysql

create database for demo:

```
docker exec -it some-mysql bash

mysql --password
# password = my-secret-pw

create database db_example;
create user 'springuser'@'%' identified by 'ThePassword';
grant all on db_example.* to 'springuser'@'%';
```

test app with

```
# save a customer
curl http://localhost:8080/api/customer -d "{name:First, email:someemail@someemailprovider.com}"
# list all customers
curl http://localhost:8080/api/customer/all
# retrieve a customer by uuid
curl http://localhost:8080/api/customer/c9c845b4-3a9b-4473-b0a3-949d315c7031
```

Note: the Kotlin version currently requires a trailing / when POSTing a new customer.

## Actuator

```
curl http://localhost:8080/api/actuator/mappings
# endpoints, add /api to get the full url
curl http://localhost:8080/api/actuator/mappings |jq | grep predicate
```

## demo

see [scripts/test.sh](scripts/test.sh)