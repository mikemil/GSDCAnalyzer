# GSDCAnalyzer
Analyze so source code projects looking for components/modules that need to be migrated as part of our migration processing for 
GSDC, JBoss and Infrastructure v2.

This includes looking for things like:
* web services
  * RPC needs to be migrated to JAX-WS
  * Security type of LTPA needs to be changed to Basic Auth
* web service clients
* Hibernate
  * migrate to standard JPA
* OpenJPA
  * migrate to standard JPA
* Service Locator files
  * need to be modified for Infv2 
  * need to be moved to INF_SHARED_CONFIG RTC component
* remote EJB
* TaskManager and classes extending AtomicWorkTask
* Reference to J2C connections
* DataSource files need to be migrated to the Active/Active format

All of these represent work to be done as part of the migration for the project
