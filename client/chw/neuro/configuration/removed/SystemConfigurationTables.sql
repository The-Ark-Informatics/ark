--from daniel by email (log to Bugzilla Bug 636 apply this script to databases)
CREATE TABLE ix_system_configuration
(
  "SYSCONFIGKEY" int8 NOT NULL DEFAULT nextval('"ix_sys_cfg_seq"'::text),
  "CONFIGNAME" varchar(50),
  "DESCRIPTION" varchar(200),
  "VALUE" varchar(50),
  "DELETED" int4 DEFAULT 0,
  "TIMESTAMP" varchar(55) DEFAULT timeofday(),
  CONSTRAINT syscfg_pkey PRIMARY KEY ("SYSCONFIGKEY"),
  CONSTRAINT syscfg_name UNIQUE ("CONFIGNAME")
);

CREATE SEQUENCE ix_sys_cfg_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 7
  CACHE 1;
