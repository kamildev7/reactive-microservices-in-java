# Microservices

This directory contains the different microservices examples developed in Eclipse Vert.x and RxJava technologies.
In order of appearance:

* [Hello Microservice - HTTP](microservices/hello-microservice-http)
* [Hello Consumer Microservice - HTTP](microservices/hello-consumer-microservice-http)
* [Hello Microservice - Event Bus](microservices/hello-microservice-message)
* [Hello Consumer Microservice - Event Bus](microservices/hello-consumer-microservice-message)
* [Hello Microservice with faults](microservices/hello-microservice-faulty)
* [Hello Consumer Microservice with timeout and reply](microservices/hello-consumer-microservice-timeout)

The _-consumer_ projects invoke the corresponding services:

* The [Hello Consumer Microservice - HTTP](microservices/hello-consumer-microservice-http) invokes the [Hello Microservice - HTTP](microservices/hello-microservice-http).
* The [Hello Consumer Microservice - Event Bus](microservices/hello-consumer-microservice-message) invokes the [Hello Microservice - Event Bus](microservices/hello-microservice-message).
* The [Hello Consumer Microservice with timeout and reply](microservices/hello-consumer-microservice-timeout) invokes the [Hello Microservice with faults](microservices/hello-microservice-faulty).
   
Be aware to run both at the same time.  
 
Each project is built with Apache Maven:
 
```
cd $DIRECTORY
mvn clean package
```
 
Each project contains a `README` with running instructions. 
