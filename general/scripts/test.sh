#
# note, add trailing slash for Kotlin version, remove for Java version
# TODO: fix slash problem
#
curl 'http://localhost:8080/api/customer/' -H 'Content-Type: application/json' -d '{"name":"First", "email":"someemail@someemailprovider.com"}'

curl http://localhost:8080/api/customer/all

# id field of first customer
ID=`curl -s http://localhost:8080/api/customer/all |jq -r '.[0].id'`

# use id variable to retrieve customer data
curl http://localhost:8080/api/customer/$ID |jq
