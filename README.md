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

#Optimisations
1) If limit is zero or less we will return empty ads
2) - The logic firstly reads all applied id from mongo
   - Shaffle them
   - Take size according to the limit
   - load from database appropriate records
3) - Added Caching mechanism from Guava
   - Loading data by ids made through this cache to prevent additional request to database
   - Cache can be disabled from Mbean server and fluently tune from cfg.properties file.

#Testing
- There was used JMeter to test server
- When size of threads is rising up to 60, HTTP Request failed with errors:

    e.g.
  Thread Name: Thread Group 1-20

  1 - Response code: Non HTTP response code: java.net.SocketException
    - Response message: Non HTTP response message: Socket closed

  2 - Response code: Non HTTP response code: java.io.InterruptedIOException
    - Response message: Non HTTP response message: Connection has been shut down

    (That's why need more time to investigate problem, read all specification regarding tuning Jetty server)

#There is no method to measure throughput due to there is no more free time.



