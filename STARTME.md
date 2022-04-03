Bidding system
==============

Yieldlab is a technology service provider, connecting suppliers (those who have space to show ads, e.g. on their
websites) to bidders (those who actually want to show ads). The core process is to listen for requests, gather metadata
and bids, and afterwards to determine who is winning. This challenge is about setting up a simplified version of this
core process as its own application.

## 1 Start auction

### 1.1 Start the Advertisement Bidder applications

To start the test environment, either use the script `scripts/test-setup.sh` or run the following commands one after the other from your shell:


### 1.2a Start the Advertisement Supplier application
I had in mind configuring the complete start up using Docker compose including: this project and the instances of the one you provided.
I had the problem with **connection refused** inside the different containers.
That's why I introduced the solution with _custom network and custom addresses_.

Prerequisites:
- Please checkout project from git: https://github.com/natalija94/advertisement-supplier.
- Please download Maven.
- Please enable Lombok in your IDE.

Please find two files `docker-compose.yml` + `Dockerfile`.

*Remark:
docker-compose up  :   requires **jar** file of ad supplier service.
You need maven so you can perform the command: **clean install**.

After **clean install** target folder is generated and .jar (which docker compose requires).
You will need image for app service. After image building please consider following procedure:

In order to manage the communication between different services network is introduced. Please do following steps:
1. docker network create auction --gateway 172.18.0.1 --subnet 172.18.0.1/16
2. start the services-> docker-compose up (All the services will be started: the Bidders and Supplier.)
3. run the test script


### REMARK: Alternative to 1.2a
While i had some difficulties with services communication in the development, I had to consider alternatives.
That's why this solution is introduced.

Prerequisites:
Please checkout project from git: https://github.com/natalija94/advertisement-supplier. Please set   11 to the system path.
Please download Maven.
Please enable Lombok in your IDE.

1. Set _http://localhost:8081,http://localhost:8082,http://localhost:8083_ as _bidders_ config in application.properties
2. Do maven _clean install_ in order to generate .jar to be started.
3. From the root of the project navigate to target: _cd target_
4. Start the application: java -jar _-advertisement-supplier-1.0.0.jar_


*Regarding points 2,3,4: for development and debug mode I only use the _start.run.xml_ script which is pound in .run folder.
*Automatically recognized as start scripts in Intelli JIDEA.