-- 패스워드 만료 기간 해제
alter profile default limit password_life_time unlimited;

-- 사용자 생성
create user HAPPYTALKOWN identified by local1234;
grant connect to HAPPYTALKOWN;
grant connect, resource, dba to HAPPYTALKOWN;
grant create session to HAPPYTALKOWN;

-- Fulltext search index 권한
@/oracle/orachat/19.0.0/ctx/admin/catctx.sql CTXSYS SYSAUX TEMP NOLOCK
@/oracle/orachat/19.0.0/ctx/admin/defaults/drdefuk.sql
@/oracle/orachat/19.0.0/ctx/admin/defaults/drdefko.sql
grant ctxapp to HAPPYTALKOWN;
grant execute on ctxsys.ctx_cls to HAPPYTALKOWN;
grant execute on ctxsys.ctx_ddl to HAPPYTALKOWN;
grant execute on ctxsys.ctx_doc to HAPPYTALKOWN;
grant execute on ctxsys.ctx_output to HAPPYTALKOWN;
grant execute on ctxsys.ctx_query to HAPPYTALKOWN;
grant execute on ctxsys.ctx_report to HAPPYTALKOWN;
grant execute on ctxsys.ctx_thes to HAPPYTALKOWN;
grant execute on ctxsys.ctx_ulexer to HAPPYTALKOWN;

-- 패스워드 만료시
alter user HAPPYTALKOWN account unlock;
alter user HAPPYTALKOWN identified by local1234;

