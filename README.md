#For build project use next
1) - mvn clean install
    OR
2) - mvn clean install -DskipTests=true

Both of them will create Uber Jar

#For running server use either of
1) - java -jar target/restfullserver-1.0-SNAPSHOT.jar

2) - mvn exec:java

#While server starting initial data will printed in console
#To test there needed to send json request like:
curl -X POST -H "content-type: application/json" -d '{ "country" : "UK", "os": "ios", "limit" : 1 }' http://localhost:8081/ads



Logic tested by rest-assured framework
