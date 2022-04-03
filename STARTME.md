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

Prerequisites:
Please checkout project from git: https://github.com/natalija94/advertisement-supplier. Please set java 11 to the system path.
Please download Maven. 
Please enable Lombok in your IDE.

1. Set _http://localhost:8081,http://localhost:8082,http://localhost:8083_ as _bidders_ config in application.properties
2. Do maven _clean install_ in order to generate .jar to be started.
3. From the root of the project navigate to target: _cd target_
4. Start the application: java -jar _-advertisement-supplier-1.0.0.jar_


*Regarding points 2,3,4: for development and debug mode I only use the _start.run.xml_ script which is pound in .run folder.
*Automatically recognized as start scripts in Intelli JIDEA.




### REMARK: Docker Alternative to 1.2a
(Not complete)
I tried to configure the complete system using Docker compose including: this project and the instances of the one you provided.
I had the problem with **connection refused** with http://localhost:8082 and http://localhost:8083. I tried different things - with no success.
That's why I introduced the solution in 1.2a.


Prerequisites:
- Please checkout project from git: https://github.com/natalija94/advertisement-supplier.
- PLease download Maven.
- Please enable Lombok in your IDE.

Please find two files `docker-compose.yml` + `Dockerfile`.

*Remark:
docker-compose up  :   requires **jar** file of ad supplier service.
You need maven so you can perform the command: **clean install**.

After **clean install** target folder is generated and .jar which docker compose requires.
First you need image for app service. 
Perform: **docker-compose up** .
Please run the command **docker-compose up** - in order to start the service.