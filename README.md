# Weather

Simple project to handle weather metrics. 

**Design Considerations**
* Designed as POC, so did not consider Non-functional requirements as Throughput, availability, etc. 
* Identified 3 possible services, but to easy the dev all were placed in same project:
  1. Sensor Administration: Manage sensor subscription and activation. 
  2. Metrics Management: Handle the reception of new metric values. Possible will handle large volume of request so should be under a LB with multiple instances. (Db could be a constraint but will depend on the expected load, innodb will help with this in the earliest stages)
  3. Query Service: Handle queries regarding readings. It may have high demand also.
* No security was added to easy the poc.
* For simplicity, a SQL database was selected as it seems the data is structured.
* The DB so far has no major consideration, besides required indexes for queries.
  * May have a Master-slave replication where the replication can be use fo queries.
  * It seems the retention of readings is up to 30 days, so it may be added an archive process for older data.
* Added just basic validations in rest endpoints.
* Testing using spock and MockMvc due most cases are integration points (rest or db access)
 

**Run Steps**
1. Build the project
   * mvn clean install
2. Start docker compose (in docker folder). (it needs 2 steps due to a sync issue where "sensor-server" starts before mysql is fully up)
   1. docker-compose up mysqldb
   2. docker-compose up sensor-server
   
**NOTE**: Can use now "docker-compose up" also as added a WA in the file to restart if fails, fix is add new scripts at boot time to verify the service start.


Alternative:
1. Start Mysql db
   * e.g.: docker-compose up mysqldb
2. mvn spring-boot:run                                                     