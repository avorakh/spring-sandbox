# spring-batch

## Billing Job
Billing job.
1. Create a PostgreSQL container
```bash
docker run --name "postgres"  \
  --env "POSTGRES_PASSWORD=postgres" \
  --env "POSTGRES_USER=postgres" \
  -d -p 5432:5432 "postgres:14.1-alpine" \
  "postgres"
```
2. Connect to the database
```bash
docker exec -it postgres psql -U postgres
```
3. Create the metadata tables
```sql
CREATE TABLE BATCH_JOB_INSTANCE  (
    JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
    VERSION BIGINT ,
    JOB_NAME VARCHAR(100) NOT NULL,
    JOB_KEY VARCHAR(32) NOT NULL,
    constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ;
CREATE TABLE BATCH_JOB_EXECUTION  (
    JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
    VERSION BIGINT  ,
    JOB_INSTANCE_ID BIGINT NOT NULL,
    CREATE_TIME TIMESTAMP NOT NULL,
    START_TIME TIMESTAMP DEFAULT NULL ,
    END_TIME TIMESTAMP DEFAULT NULL ,
    STATUS VARCHAR(10) ,
    EXIT_CODE VARCHAR(2500) ,
    EXIT_MESSAGE VARCHAR(2500) ,
    LAST_UPDATED TIMESTAMP,
    constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
    references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ;
CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
    JOB_EXECUTION_ID BIGINT NOT NULL ,
    PARAMETER_NAME VARCHAR(100) NOT NULL ,
    PARAMETER_TYPE VARCHAR(100) NOT NULL ,
    PARAMETER_VALUE VARCHAR(2500) ,
    IDENTIFYING CHAR(1) NOT NULL ,
    constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;
CREATE TABLE BATCH_STEP_EXECUTION  (
    STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
    VERSION BIGINT NOT NULL,
    STEP_NAME VARCHAR(100) NOT NULL,
    JOB_EXECUTION_ID BIGINT NOT NULL,
    CREATE_TIME TIMESTAMP NOT NULL,
    START_TIME TIMESTAMP DEFAULT NULL ,
    END_TIME TIMESTAMP DEFAULT NULL ,
    STATUS VARCHAR(10) ,
    COMMIT_COUNT BIGINT ,
    READ_COUNT BIGINT ,
    FILTER_COUNT BIGINT ,
    WRITE_COUNT BIGINT ,
    READ_SKIP_COUNT BIGINT ,
    WRITE_SKIP_COUNT BIGINT ,
    PROCESS_SKIP_COUNT BIGINT ,
    ROLLBACK_COUNT BIGINT ,
    EXIT_CODE VARCHAR(2500) ,
    EXIT_MESSAGE VARCHAR(2500) ,
    LAST_UPDATED TIMESTAMP,
    constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;
CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
    STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT ,
    constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
    references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ;
CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
    JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
    SHORT_CONTEXT VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT ,
    constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
    references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;
CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE BATCH_JOB_SEQ MAXVALUE 9223372036854775807 NO CYCLE;

CREATE TABLE BILLING_DATA
(
    DATA_YEAR     INTEGER,
    DATA_MONTH    INTEGER,
    ACCOUNT_ID    INTEGER,
    PHONE_NUMBER  VARCHAR(12),
    DATA_USAGE    FLOAT,
    CALL_DURATION INTEGER,
    SMS_COUNT     INTEGER
);
```
4. Check that all tables were correctly created using the \d command:
```
postgres=# \d
                      List of relations
 Schema |             Name             |   Type   |  Owner   
--------+------------------------------+----------+----------
 public | batch_job_execution          | table    | postgres
 public | batch_job_execution_context  | table    | postgres
 public | batch_job_execution_params   | table    | postgres
 public | batch_job_execution_seq      | sequence | postgres
 public | batch_job_instance           | table    | postgres
 public | batch_job_seq                | sequence | postgres
 public | batch_step_execution         | table    | postgres
 public | batch_step_execution_context | table    | postgres
 public | batch_step_execution_seq     | sequence | postgres
(9 rows)

postgres=# 
```
4. How to verify the Batch metadata in the database
```bash
docker exec postgres psql -U postgres -c 'select * from BATCH_JOB_EXECUTION;'
docker exec postgres psql -U postgres -c 'select * from BATCH_JOB_INSTANCE;'
docker exec postgres psql -U postgres -c 'select * from BATCH_JOB_EXECUTION_PARAMS;'
docker exec postgres psql -U postgres -c 'select * from BATCH_STEP_EXECUTION;'
docker exec postgres psql -U postgres -c 'select count(*) from BILLING_DATA;'
```
5. Launch with args (from a root project)
```bash
./gradlew :spring-batch:bootRun --args='input.file=input/billing-2023-01.csv output.file=staging/billing-report-2023-01.csv skip.file=staging/billing-data-skip-2023-01.psv data.year=2023 data.month=1'
./gradlew :spring-batch:bootRun --args='input.file=input/billing-2023-02.csv output.file=staging/billing-report-2023-02.csv skip.file=staging/billing-data-skip-2023-02.psv data.year=2023 data.month=2'
./gradlew :spring-batch:bootRun --args='input.file=input/billing-2023-03.csv output.file=staging/billing-report-2023-03.csv skip.file=staging/billing-data-skip-2023-03.psv data.year=2023 data.month=3'
./gradlew :spring-batch:bootRun --args='input.file=input/billing-2023-04.csv output.file=staging/billing-report-2023-04.csv skip.file=staging/billing-data-skip-2023-04.psv data.year=2023 data.month=4'
```