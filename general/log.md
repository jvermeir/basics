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
curl http://localhost:8080 -d "{name:First, email:someemail@someemailprovider.com}"

# Java version, TODO: use json in request body
curl http://localhost:8080/api/customer -d name=First2 -d email=someemail2@someemailprovider.com
curl http://localhost:8080/api/customer/all
```

actuator

```
curl http://localhost:8080/api/actuator/mappings
# endpoints, add /api to get the full url
curl http://localhost:8080/api/actuator/mappings |jq | grep predicate
```

## demo

see [scripts/test.sh](scripts/test.sh)