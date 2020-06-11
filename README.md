## Rebuild && Install
```aidl
mvn clean install
```

## Run spring boot project
```aidl
mvn spring-boot:run
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

GET
localhost:8080/feature?email=e1&featureName=f1
RESPONSE
{
    "canAccess": true
}
```

## Running unit tests
```$xslt
mvn test
```

## Specifications/Tools/Stacks
```$xslt
Intellj, Maven, H2 db in runtime memory, java 8, spring boot, postman
```

