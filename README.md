
# Blooming-blooms MICROSERVICE.API-GATEWAY

## Description
This is the Spring Cloud API Gateway server, which handles routing to appropriate services and checks user authentication and authorization. The service consists of an authentication filter and an optional authentication filter. The optional authentication filter is necessary when users look up products without logging in. Additionally, there are authorization filters for each role, providing flexible authentication and authorization.


## Getting Started

### Prerequisites

#### Install(Mac brew)
```
brew install redis
```
#### Install(Window)
```
sudo apt-get install redis
```

### Usage 

#### Redis
```
redis-cli -h host -p port -a password
```

## Configuration

### Routing
```
spring:
  cloud:
    config:
      name: apigateway-service
    gateway:
      routes:

        # product
        - id: product-service
          uri: lb://PRODUCT-SERVICE
          predicates:
            - Path=/api/products/**
            - Method=GET,POST,OPTIONS,PUT,DELETE
          filters:
            - RewritePath=/api/products/(?<segment>.*), /$\{segment}
            - name: JwtOptionalGatewayFilter

        # delivery store manager
        - id: delivery-service
          uri: lb://DELIVERY-SERVICE
          predicates:
            - Path=/api/delivery/{variable:.*}/**
            - Method=GET,POST,OPTIONS,PUT,DELETE,PATCH
          filters:
            - RewritePath=/api/delivery/(?<segment>.*), /$\{segment}
            - name: JwtValidation
            - name: StoreAuthorization

        # delivery customer
        - id: delivery-service
          uri: lb://DELIVERY-SERVICE
          predicates:
            - Path=/api/delivery/**
            - Method=GET,POST,OPTIONS,PUT,DELETE,PATCH
          filters:
            - RewritePath=/api/delivery/(?<segment>.*), /$\{segment}
            - name: JwtValidation
            - name: SocialAuthorization


```
## API Documentation

https://www.notion.so/0acd63e526144ac3aeac0bea0413704a?pvs=4

## ERD

https://www.erdcloud.com/d/PSD5Cgi6GrFQbdxgK

## System Architecture
![image](https://github.com/JIUNG9/BB-APIGATEWAY-SERVICE/assets/60885635/ba580899-5ef3-4dda-b242-8d4d84666640)


<!-- Backend Languages and Tools -->
## Backend Languages and Tools
<p align="left">
  <!-- Database Icons -->
  <a href="https://www.mysql.com/" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/mysql/mysql-original-wordmark.svg" alt="mysql" width="40" height="40"/> </a>&nbsp;&nbsp;&nbsp;&nbsp
   <a href="https://redis.io" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/redis/redis-original-wordmark.svg" alt="redis" width="40" height="40"/> </a>&nbsp;&nbsp;&nbsp; &nbsp;
  <a href="https://kafka.apache.org/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/apache_kafka/apache_kafka-icon.svg" alt="kafka" width="40" height="40"/> </a>&nbsp;&nbsp;&nbsp; &nbsp;
  <!-- Framework Icons -->
  <a href="https://spring.io/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/springio/springio-icon.svg" alt="spring" width="40" height="40"/> </a>
</p>



## Contacts

📫 How to reach me **rnwldnd7248@gmai.com**

 📄 Know about my experiences [https://www.notion.so/704f524047084978836216b3621dc12e?pvs=4](https://www.notion.so/704f524047084978836216b3621dc12e?pvs=4)
