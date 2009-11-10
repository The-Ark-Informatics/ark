DROP SEQUENCE "ix_report_category_seq";
CREATE SEQUENCE "ix_report_category_seq" INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

DROP TABLE "ix_report_category";
CREATE TABLE "ix_report_category" (
	"REPORTCATKEY" int8 DEFAULT nextval('"ix_report_category_seq"'::text) NOT NULL,
        "PARENTKEY" int8,
	"NAME" text,
	"DESCRIPTION" text,
	"DELETED" int default '0',
	"TIMESTAMP" varchar(55) default timeofday(),
	CONSTRAINT "ix_report_category_categorykey" PRIMARY KEY ("REPORTCATKEY") );

DROP SEQUENCE "ix_report_seq";
CREATE SEQUENCE "ix_report_seq" INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

DROP TABLE "ix_report";
CREATE TABLE "ix_report" (
	"REPORTKEY" int8 DEFAULT nextval('"ix_report_seq"'::text) NOT NULL,
        "REPORTCATKEY" int8,
	"NAME" text,
        "QUERY" text,
	"DESCRIPTION" text,
        "MDXQUERYFILE" text,
	"DELETED" int default '0',
	"TIMESTAMP" varchar(55) default timeofday(),
	CONSTRAINT "ix_report_reportkey" PRIMARY KEY ("REPORTKEY") );


INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('report_view', 'View report');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('report_add', 'Add report');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('report_update', 'Update report');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('report_delete', 'Delete report');