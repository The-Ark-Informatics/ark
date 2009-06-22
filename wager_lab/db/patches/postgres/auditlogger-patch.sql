---
--- DB Patch for use with the NGX Field Level Audit Logger
--- Copyright (C) Neuragenix/xBio Systems Pty Ltd, 2006
--- Author : Daniel Murley (dmurley@xbiosystems.com)
---

CREATE SEQUENCE ix_audit_log_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE SEQUENCE ix_audit_items_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

DROP TABLE ix_audit_log;
CREATE TABLE ix_audit_log
(
  "AUDITLOGKEY" int4 NOT NULL DEFAULT nextval('ix_audit_log_seq'::text),
  "USER" varchar(55),
  "REASON" varchar(4000),
  "DATE" date,
  "TIMESTAMP" varchar(55) DEFAULT timeofday()
);

DROP TABLE ix_audit_items;
CREATE TABLE ix_audit_items
(
  "AUDITITEMKEY" int4 NOT NULL DEFAULT nextval('ix_audit_items_seq'::text),
  "DOMAIN" varchar(100),
  "FIELD" varchar(100),
  "ORIGINALVALUE" varchar(4000),
  "NEWVALUE" text,
  "ACTIVITY" varchar(100),
  "ROWKEY" int4,
  "AUDITLOGKEY" int4,
  "TIMESTAMP" varchar(55) DEFAULT timeofday()
);

