-- 기타
alter session set nls_language='english';
archive log list

-- 백업
$ cat backup.sql
ALTER TABLESPACE SYSTEM BEGIN BACKUP;
host cp /oradata/SYSTEM/system01.dbf /oraback/system01_`date +%Y%m%d-%H%M%S`.dbf
ALTER TABLESPACE SYSTEM END BACKUP;

ALTER TABLESPACE SYSAUX BEGIN BACKUP;
host cp /oradata/SYSTEM/sysaux01.dbf /oraback/sysaux01_`date +%Y%m%d-%H%M%S`.dbf
ALTER TABLESPACE SYSAUX END BACKUP;

ALTER TABLESPACE UNDOTBS1 BEGIN BACKUP;
host cp /oradata/SYSTEM/undotbs01.dbf /oraback/undotbs01_`date +%Y%m%d-%H%M%S`.dbf
ALTER TABLESPACE UNDOTBS1 END BACKUP;

ALTER TABLESPACE USERS BEGIN BACKUP;
host cp /oradata/SYSTEM/users01.dbf /oraback/users01_`date +%Y%m%d-%H%M%S`.dbf
ALTER TABLESPACE USERS END BACKUP;

ALTER TABLESPACE TS_HAPPYTALKOWN_DAT BEGIN BACKUP;
host cp /oradata/DATA/TS_HAPPYTALKOWN_DAT01.dbf /oraback/TS_HAPPYTALKOWN_DAT01_`date +%Y%m%d-%H%M%S`.dbf
ALTER TABLESPACE TS_HAPPYTALKOWN_DAT END BACKUP;

ALTER SYSTEM SWITCH LOGFILE;
ALTER SYSTEM ARCHIVE LOG CURRENT;

EXIT
