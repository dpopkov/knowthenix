## Register new user
```shell script
curl -X POST -H 'Content-Type: application/json' -d @register-data.json http://localhost:8080/user/register
```
or
```shell script
curl  --request POST 'http://localhost:8080/user/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstName":"Jane",
    "lastName":"Doe",
    "username":"jane",
    "email":"jane@example.org"
}'
```

## Login using username and password
```shell script
curl -v -X POST -H 'Content-Type: application/json' -d '{"username":"<username>","password":"<password>"}' http://localhost:8080/user/login
```
or
```shell script
curl -v --request POST 'http://localhost:8080/user/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username":"jane",
    "password":"<password-value>"
}'
```

## Add new user
```shell script
curl -X POST http://localhost:8080/user \
--header 'Authorization: Bearer <JWT-value-of-current-user-goes-here>' \
--form 'firstName="Billy"' \
--form 'lastName="Bones"' \
--form 'username="bill"' \
--form 'email="bill@example.org"' \
--form 'role="ROLE_SUPER_ADMIN"' \
--form 'notLocked="true"' \
--form 'active="true"'
```

## Update user
```shell script
curl -X PUT http://localhost:8080/user \
--header 'Authorization: Bearer <JWT-value-of-current-user-goes-here>' \
--form 'firstName="Billy"' \
--form 'lastName="BigBones"' \
--form 'username="bill"' \
--form 'email="bill@example.org"' \
--form 'role="ROLE_SUPER_ADMIN"' \
--form 'notLocked="true"' \
--form 'active="true"' \
--form 'currentUsername="bill"'
```

## Delete user
```shell script
curl --request DELETE 'http://localhost:8080/user/<username>' \
--header 'Authorization: Bearer <JWT-value-of-current-user-goes-here>'
```
