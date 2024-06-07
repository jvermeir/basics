# Scala rest + grpc version

## build

Generate code from customer.proto file:

```bash
sbt compile
```

## Test REST interface

see [../README.md](../README.md)

## Test gRPC 

Add a new customer

```bash
grpcurl -d '{"name": "name1", "email":"email1"}' -plaintext \
-import-path ~/dev/basics/scala/src/main/protobuf \
-proto customer.proto \
localhost:8090 nl.vermeir.scala.grpc.CustomerService.CreateCustomer
```

Get a customer by UUID

```bash
grpcurl -d '{"id": "85ddcd78-67e0-4cf2-b947-228cc7c8e39a"}' -plaintext \
-import-path ~/dev/basics/scala/src/main/protobuf \
-proto customer.proto \
localhost:8090 nl.vermeir.scala.grpc.CustomerService.GetCustomerById
```

List all customers 

```bash
grpcurl -d '{}' -plaintext \
-import-path ~/dev/basics/scala/src/main/protobuf \
-proto customer.proto \
localhost:8090 nl.vermeir.scala.grpc.CustomerService.ListCustomers
```
