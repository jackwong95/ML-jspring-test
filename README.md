## Rebuild && Install
```aidl
mvn clean install
```

## Run spring boot project
```aidl
mvn spring-boot:run
```

## Running unit tests
```
mvn test
```

## Specifications/Tools/Stacks
```$xslt
Intellj, Maven, H2 db in runtime memory, java 8, spring boot, postman
```


## End points (tested with postman)
```aidl
POST
localhost:8080/feature
{
    "featureName": "f1",
    "email": "e1",
    "enable" : true
}
RESPONSE 200 OK

POST
{
    "featureName": "f2",
    "email": "e2",
    "enable" : true
}
RESPONSE 200 OK

POST
{
    "email": "e2",
    "enable" : true
}
RESPONSE 400 Bad Request
{
    "timestamp": "2020-06-11T09:56:18.473+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "",
    "path": "/feature"
}

POST
{
    "featureName": "f2",
    "email": "e2",
    "enable" : null
}
RESPONSE 304 Not Modified

GET
localhost:8080/feature?email=e1&featureName=f1
RESPONSE 200
{
    "canAccess": true
}

localhost:8080/feature?email=e2&featureName=f2
RESPONSE 200
{
    "canAccess": true
}

localhost:8080/feature?email=e1&featureName=f2
RESPONSE 200
{
    "canAccess": false
}

localhost:8080/feature?email=e1
RESPONSE 400
{
    "timestamp": "2020-06-11T09:48:31.735+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "",
    "path": "/feature"
}

localhost:8080/feature?email=e1&featureName=null
RESPONSE 400
Feature cannot be found

localhost:8080/feature?email=e1ff&featureName=f1
RESPONSE 400
Email cannot be found
```