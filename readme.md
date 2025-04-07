# This is current brainstorm of the API business logic

### Create new gift card:
 Issue new card requires nothing, the system should allow zero balance at start and provide **secure generated code**
 via Apache Commons Validator luhn method.
### Update gift card balance:
 The card number must be valid, and the status should allow the update.
### Update gift card expire date:
 The card number must be valid, and status should allow the update.
### Cancel/block gift card:
The card number must be valid, cannot delete if in use, and balance should be zero.
### Bulk Operations:
Each card must have valid code, balance, and status.
### Exchange balance between cards:
 both card numbers must be valid,
 the card need credit in balance to transfer,
 the status should allow the Exchange for both cards, and the cards must not be expired.
### PCI DSS
 I found on the internet that this is industry pattern to guarantee security over cards applications

# This is current architectural brainstorm

### Layered Architecture
 I will split the app using a common architecture with controllers, services, and repositories. As I have two
 APIs to build (one Spring Boot and another .Net Core) I will start using this conventional one, and then work
 on a DDD or clean in the other.

### Batch/Bulk Operations
 First I'm gonna work with messaging tech, the RabbitMQ can be my choice here. I heard about Spring Batch, I can give it
 a try. I'm thinking that the volume of data here can be huge on Spring Sales or Black Fridays, it could have to process
 a thousand transactions per batch.

### Documentation
 The Springdoc is the best option today for Spring Boot API docs.

### Elastic
 In order to have a transactions history I will add elastic and kibana.
 

# Current project stack:
 - JAVA 23
 - Spring Boot (Web, JPA, and Validation) 3.4.3
 - H2 Database 2.3.232
 - Lombok 1.18.36
 - Maven 3.9.9
 - Springdoc 2.8.6
 - Apache Commons Validator 1.7

