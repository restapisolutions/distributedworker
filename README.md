#Interview Assignment

###Assumptions

Based on the assignment description, I assume that the database is a SQL database and the table creation and migration are done with other external tools, so we can run the worker concurrently.
Workers should not be involved in database creation, they should only use it. 

In this application, the worker behavior that was stated in the assignment is implemented in java but the concurrency is done on the database level.

###DB setup

Im using a postgreSQL instance running on localhost. You can connect to any external postgres instance by configuring the application properties file at `spring.datasource.url`.

You can run a postgreSQL instance like me, using docker .

`docker run --name workerdb -e POSTGRES_PASSWORD=password -d -p 5432:5432 postgres`

To get a shell :  
`docker exec -it workerdb bash`

Here you need to switch to a different user
`su postgres`
and run 
`psql`

Here we create the database

``` sql
CREATE DATABASE workerdb; 
CREATE USER admin WITH CREATEDB;
ALTER USER admin WITH password 'password';
GRANT ALL ON DATABASE workerdb TO admin;
```

enter the database as admin
`
 \c workerdb admin
`

create the required table:

``` sql
CREATE TABLE workertable (
    id SERIAL PRIMARY KEY NOT NULL,
    url TEXT NOT NULL,
    status TEXT NOT NULL,
    http_code TEXT
)
```

Database seed...
``` sql
INSERT INTO workertable(id,url,status,http_code) VALUES (0, 'https://proxify.io', 'NEW', null);

INSERT INTO workertable(id,url,status,http_code) VALUES (1, 'https://reddit.com', 'NEW', null);

INSERT INTO workertable(id,url,status,http_code) VALUES (2, 'https://google.com', 'NEW', null);

INSERT INTO workertable(id,url,status,http_code) VALUES (3, 'https://thisurldoesntexist', 'NEW', null);

```

Oh, and also the magic sauce, our stored procedure for the concurrency, I use it to set the status to processing so 2 workers cannot do the same job.

This is postgreSQL specific but can be easily modified for any other database;
```sql
CREATE OR REPLACE FUNCTION get_processing() RETURNS integer AS $$

DECLARE rowid integer;
BEGIN

SELECT id FROM workertable WHERE status = 'NEW' LIMIT 1 FOR UPDATE INTO rowid;

IF NOT FOUND THEN 
    RETURN -1;
END IF;

UPDATE workertable SET status = 'PROCESSING' WHERE id = rowid;

RETURN rowid;

END;
$$ LANGUAGE plpgsql;
```

After this you are ready to run a worker!

What is happening? Stored functions always run inside the transactions, and I can lock the row in the database by using FOR UPDATE.
After this, I set the status and return the ID of the row. This is atomic and helps with the concurrency as other workers will not be able to select the same row. 

Locks and concurrency are notoriously hard and I think this is the simplest solution.

##Application structure
The spring boot application entry point is the DistributedworkerApplication.java file,
this will run the application, but the **WorkerRunner** class inside Runner package contains the business logic;

The **workerRepository.get_processing()** will call the stored function from the database which we call through the Service to catch any exceptions.
We are using the ID returned by getProcessing if the function was able to set a status to processing for us, otherwise when returns -1 we know there are no more jobs to schedule.

