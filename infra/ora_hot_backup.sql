--
SELECT * FROM v$datafile;
SELECT * FROM v$logfile;

--
ALTER TABLESPACE SYSTEM BEGIN BACKUP;
host cp /oradata/SYSTEM/system01.dbf /oraback/system01_`date +%Y%m%d`.dbf
ALTER TABLESPACE SYSTEM END BACKUP;

ALTER TABLESPACE SYSAUX BEGIN BACKUP;
host cp /oradata/SYSTEM/sysaux01.dbf /oraback/sysaux01_`date +%Y%m%d`.dbf
ALTER TABLESPACE SYSAUX END BACKUP;

ALTER TABLESPACE UNDOTBS1 BEGIN BACKUP;
host cp /oradata/SYSTEM/undotbs01.dbf /oraback/undotbs01_`date +%Y%m%d`.dbf
ALTER TABLESPACE UNDOTBS1 END BACKUP;

ALTER TABLESPACE USERS BEGIN BACKUP;
host cp /oradata/SYSTEM/users01.dbf /oraback/users01_`date +%Y%m%d`.dbf
ALTER TABLESPACE USERS END BACKUP;

ALTER TABLESPACE TS_HAPPYTALKOWN_DAT BEGIN BACKUP;
host cp /oradata/DATA/TS_HAPPYTALKOWN_DAT01.dbf /oraback/TS_HAPPYTALKOWN_DAT01_`date +%Y%m%d`.dbf
ALTER TABLESPACE TS_HAPPYTALKOWN_DAT END BACKUP;

ALTER SYSTEM SWITCH LOGFILE;
ALTER SYSTEM ARCHIVE LOG CURRENT;

host rm /oraback/system01_`date -d "2 days ago" +%Y%m%d`.dbf
host rm /oraback/sysaux01_`date -d "2 days ago" +%Y%m%d`.dbf
host rm /oraback/undotbs01_`date -d "2 days ago" +%Y%m%d`.dbf
host rm /oraback/users01_`date -d "2 days ago" +%Y%m%d`.dbf
host rm /oraback/TS_HAPPYTALKOWN_DAT01_`date -d "2 days ago" +%Y%m%d`.dbf

EXIT

--
sqlplus 'sys/1q2w3e4r%T' as sysdba

BEGIN
dbms_scheduler.create_credential (
    credential_name => 'ORACLE',
    username => 'oracle',
    password => 'oracle'
);
END;
/

CREATE OR REPLACE EDITIONABLE PROCEDURE HOT_DAILY_BACKUP ()
IS
BEGIN
	SELECT 1 from dual;
	host touch /oraback/test01_`date +%Y%m%d-%H%M%S`.dbf
END;
/

--
BEGIN
dbms_scheduler.create_job (
	job_name => 'HOT_BACKUP',
	job_type => 'SQL_SCRIPT',
	job_action => '/oracle/orachat/backup.sql',
    credential_name => 'ORACLE',
	start_date => sysdate,
	repeat_interval => 'FREQ=SECONDLY;INTERVAL=10',
	enabled => TRUE
);
END;
/

BEGIN
dbms_scheduler.create_job (
	job_name => 'HOT_BACKUP',
	job_type => 'STORED_PROCEDURE',
	job_action => 'HOT_DAILY_BACKUP',
	start_date => sysdate,
	repeat_interval => 'FREQ=SECONDLY;INTERVAL=10',
	enabled => TRUE
);
END;
/

BEGIN
dbms_scheduler.enable ('HOT_BACKUP');
END;
/

BEGIN
dbms_scheduler.run_job ('HOT_BACKUP');
END;
/

BEGIN
dbms_scheduler.drop_job ('HOT_BACKUP');
END;
/


select * from user_scheduler_jobs;

select o.*, s.* from dba_object o, dba_scheduler_jobs s
where o.object_type = 'JOB'
order by created desc;

select *
from user_scheduler_job_log
order by log_date desc;
