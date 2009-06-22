-- changes to ix_patient
ALTER TABLE ix_patient DROP COLUMN "ALERT";
ALTER TABLE ix_patient ADD COLUMN "ALERT" varchar(50);
ALTER TABLE ix_patient DROP COLUMN "INITIALS";
ALTER TABLE ix_patient ADD COLUMN "INITIALS" varchar(50);
ALTER TABLE ix_patient DROP COLUMN "STUDYNO";
ALTER TABLE ix_patient ADD COLUMN "STUDYNO" varchar(50);
ALTER TABLE ix_patient DROP COLUMN "CENTRENO";
ALTER TABLE ix_patient ADD COLUMN "CENTRENO" varchar(50);
ALTER TABLE ix_patient DROP COLUMN "PROTOCOL";
ALTER TABLE ix_patient ADD COLUMN "PROTOCOL" varchar(50);
ALTER TABLE ix_patient DROP COLUMN "CLINICALSTATUS";
ALTER TABLE ix_patient ADD COLUMN "CLINICALSTATUS" varchar(50);
ALTER TABLE ix_patient DROP COLUMN "LFU";
ALTER TABLE ix_patient ADD COLUMN "LFU" varchar(50);
ALTER TABLE ix_patient DROP COLUMN "EFS";
ALTER TABLE ix_patient ADD COLUMN "EFS" float8;

-- changes to ix_admissions
ALTER TABLE ix_admissions DROP COLUMN "OFF_TREAT";
ALTER TABLE ix_admissions ADD COLUMN "OFF_TREAT" varchar(50);
ALTER TABLE ix_admissions DROP COLUMN "RELAPSE_DATE";
ALTER TABLE ix_admissions ADD COLUMN "RELAPSE_DATE" date;
ALTER TABLE ix_admissions DROP COLUMN "RELAPSE_SITE";
ALTER TABLE ix_admissions ADD COLUMN "RELAPSE_SITE" varchar(50);
ALTER TABLE ix_admissions DROP COLUMN "PRIM_SITE";
ALTER TABLE ix_admissions ADD COLUMN "PRIM_SITE" varchar(50);
ALTER TABLE ix_admissions DROP COLUMN "STAGE";
ALTER TABLE ix_admissions ADD COLUMN "STAGE" varchar(50);
ALTER TABLE ix_admissions DROP COLUMN "UNDERLYINGCOND";
ALTER TABLE ix_admissions ADD COLUMN "UNDERLYINGCOND" varchar(50);
ALTER TABLE ix_admissions DROP COLUMN "RISK";
ALTER TABLE ix_admissions ADD COLUMN "RISK" varchar(50);
ALTER TABLE ix_admissions DROP COLUMN "DIAG_CATEGORY";
ALTER TABLE ix_admissions ADD COLUMN "DIAG_CATEGORY" varchar(500);

ALTER TABLE ix_admissions DROP COLUMN "EVENT_TYPE";
ALTER TABLE ix_admissions ADD COLUMN "EVENT_TYPE" varchar(50);
ALTER TABLE ix_admissions DROP COLUMN "EVENT_DATE";
ALTER TABLE ix_admissions ADD COLUMN "EVENT_DATE" date;


-- changes to ix_biospecimen
ALTER TABLE ix_biospecimen DROP COLUMN "QTY_REMAIN";
ALTER TABLE ix_biospecimen ADD COLUMN "QTY_REMAIN" float8;
ALTER TABLE ix_biospecimen DROP COLUMN "CLINIC_AGE";
ALTER TABLE ix_biospecimen ADD COLUMN "CLINIC_AGE" int8;

-- changes to the ix_vial_calculation
ALTER TABLE ix_vial_calculation DROP COLUMN "SAMPLEWEIGHTING";
ALTER TABLE ix_vial_calculation ADD COLUMN "SAMPLEWEIGHTING" int4;
ALTER TABLE ix_vial_calculation DROP COLUMN "SPLEWEIGHTPERVIAL";
ALTER TABLE ix_vial_calculation ADD COLUMN "SPLEWEIGHTPERVIAL" int4;
ALTER TABLE ix_vial_details DROP COLUMN "SAMPLEWEIGHTING";
ALTER TABLE ix_vial_details ADD COLUMN "SAMPLEWEIGHTING" int4;
ALTER TABLE ix_vial_details DROP COLUMN "SPLEWEIGHTPERVIAL";
ALTER TABLE ix_vial_details ADD COLUMN "SPLEWEIGHTPERVIAL" int4;

-- add activity for vial calculation
DELETE FROM ix_activity where "NAME" = 'do_vial_calculation';
INSERT INTO ix_activity ("NAME", "DESCRIPTION") values ('do_vial_calculation', 'Vial Calculation');

-- changes to the ix_listofvalues
DELETE FROM ix_listofvalues where "TYPE" = 'BIOTXN_STATUS';
INSERT into ix_listofvalues ("TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE", "DELETED") values ('BIOTXN_STATUS', 'Available', 'Available', '1', 'ENG', '0');
INSERT into ix_listofvalues ("TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE", "DELETED") values ('BIOTXN_STATUS', 'Allocated', 'Allocated', '1', 'ENG', '0');
INSERT into ix_listofvalues ("TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE", "DELETED") values ('BIOTXN_STATUS', 'Delivered', 'Delivered', '1', 'ENG', '0');

-- changes to units
DELETE FROM ix_listofvalues where "TYPE" = 'BIO_QUANTITY_UNITS';
INSERT into ix_listofvalues ("TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE", "DELETED") values ('BIO_QUANTITY_UNITS', 'vial', 'vial', '1', 'ENG', '0');

-- Alerts
DELETE FROM ix_listofvalues where "TYPE" = 'ALERT_OPTION';
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'ALERT_OPTION', '', '', 0, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'ALERT_OPTION', 'No', '', 1, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'ALERT_OPTION', 'Yes', '', 2, 'ENG');

-- List of roman numerals for stage of disease in patient history
DELETE FROM ix_listofvalues where "TYPE" = 'DIAGNOSISSTAGE';
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'DIAGNOSISSTAGE', '', '', 0, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'DIAGNOSISSTAGE', 'I', '', 1, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'DIAGNOSISSTAGE', 'II', '', 2, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'DIAGNOSISSTAGE', 'III', '', 3, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'DIAGNOSISSTAGE', 'IV', '', 4, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'DIAGNOSISSTAGE', 'V', '', 5, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'DIAGNOSISSTAGE', 'VI', '', 6, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'DIAGNOSISSTAGE', 'VII', '', 7, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'DIAGNOSISSTAGE', 'VIII', '', 8, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'DIAGNOSISSTAGE', 'IX', '', 9, 'ENG');

-- List of roman numerals for stage of disease in patient history
DELETE FROM ix_listofvalues where "TYPE" = 'CONSENTVERSION';
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'CONSENTVERSION', '', '', 0, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'CONSENTVERSION', 'v1', 'v1', 1, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'CONSENTVERSION', 'v2', 'v2', 2, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'CONSENTVERSION', 'v3', 'v3', 3, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'CONSENTVERSION', 'v4', 'v4', 4, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'CONSENTVERSION', 'v5', 'v5', 5, 'ENG');


-- List of events used in patient history
DELETE FROM ix_listofvalues where "TYPE" = 'EVENTTYPE';
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'EVENTTYPE', '', '', 0, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'EVENTTYPE', 'VIP', 'VIP', 1, 'ENG');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") VALUES (-1, 'EVENTTYPE', 'Unknown', 'Unknown', 2, 'ENG');

--new activity for changing the biospecimen locations in inventory
delete from ix_activity where "NAME" = 'inventory_change_location';
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('inventory_change_location', 'Inventory - Change biospecimen location');
delete from ix_activity where "NAME" = 'inventory_view_history';
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('inventory_view_history', 'Inventory - View History');

-- Tables and sequences for inventory moving
DROP SEQUENCE ix_inv_history_seq;
CREATE SEQUENCE ix_inv_history_seq
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 2147483647
    NO MINVALUE
    CACHE 1;

DROP SEQUENCE ix_inv_cell_history_seq;
CREATE SEQUENCE ix_inv_cell_history_seq
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 2147483647
    NO MINVALUE
    CACHE 1;

DROP TABLE ix_inv_history;
CREATE TABLE ix_inv_history
(
"HISTORYKEY" bigint DEFAULT nextval('"ix_inv_history_seq"'::text) NOT NULL,
"HISTORYDATE" date NOT NULL,
"USER" character varying(100),
"ROLLBACK" int4 DEFAULT 0,
"DELETED" int4 DEFAULT 0,
"TIMESTAMP" character varying(55) DEFAULT timeofday()
);

DROP TABLE ix_inv_cell_history;
CREATE TABLE ix_inv_cell_history
(
"CELLHISTKEY" bigint DEFAULT nextval('"ix_inv_cell_history_seq"'::text) NOT NULL,
"HISTORYKEY" bigint NOT NULL,
"BIOSPECIMENKEY" bigint NOT NULL,
"OLD_CELLKEY" bigint NOT NULL,
"NEW_CELLKEY" bigint NOT NULL,
"ROLLBACK" int4 DEFAULT 0,
"DELETED" int4 DEFAULT 0,
"TIMESTAMP" character varying(55) DEFAULT timeofday()
);

DROP INDEX ix_indx_inv_history;
DROP INDEX ix_indx_inv_cell_history;
DROP INDEX ix_indx_inv_cell_history_biospecimen;
CREATE INDEX ix_indx_inv_history ON ix_inv_history USING btree ("HISTORYKEY");
CREATE INDEX ix_indx_inv_cell_history ON ix_inv_cell_history USING btree ("HISTORYKEY");
CREATE INDEX ix_indx_inv_cell_history_biospecimen ON ix_inv_cell_history USING btree ("BIOSPECIMENKEY");

-- change referring doctor in admissions to lov_type
DELETE FROM ix_listofvalues where "TYPE" = 'REF_DOCTOR';
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE", "ISEDITABLE") VALUES (-1, 'REF_DOCTOR', '', '', 0, 'ENG', '0');
INSERT INTO ix_listofvalues ("GROUPKEY", "TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE", "ISEDITABLE") VALUES (-1, 'REF_DOCTOR', 'Dr Smith', 'Dr Smith', 0, 'ENG', '0');
INSERT INTO ix_listofvalues_description ("TYPE", "DESCRIPTION") VALUES ('REF_DOCTOR', 'Referring Doctor');

-- update grade
delete from ix_listofvalues where "TYPE" like '%GRADE%';
insert into ix_listofvalues ("TYPE", "VALUE", "SORTORDER") values ('BIOSPECGRADE', '', '0');
insert into ix_listofvalues ("TYPE", "VALUE", "SORTORDER") values ('BIOSPECGRADE', '< 4 hrs', '1');
insert into ix_listofvalues ("TYPE", "VALUE", "SORTORDER") values ('BIOSPECGRADE', '> 4-12 hrs', '2');
insert into ix_listofvalues ("TYPE", "VALUE", "SORTORDER") values ('BIOSPECGRADE', '> 12-24 hrs', '3');
insert into ix_listofvalues ("TYPE", "VALUE", "SORTORDER") values ('BIOSPECGRADE', '> 24-48 hrs', '4');
insert into ix_listofvalues ("TYPE", "VALUE", "SORTORDER") values ('BIOSPECGRADE', '> 48 hrs', '5');


-- new permissions for patient module
delete from ix_activity where "NAME" = 'patient_notes';
delete from ix_activity where "NAME" = 'patient_notes_delete';
delete from ix_activity where "NAME" = 'patient_notes_add';
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_notes', 'Patient Notes - View Notes');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_notes_delete', 'Patient Notes - Delete Notes');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_notes_add', 'Patient Notes - Add Notes');

delete from ix_activity where "NAME" = 'patient_appointments';
delete from ix_activity where "NAME" = 'patient_appointments_delete';
delete from ix_activity where "NAME" = 'patient_appointments_add';
delete from ix_activity where "NAME" = 'patient_appointments_edit';
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_appointments', 'Patient Appointments - View Appointments');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_appointments_delete', 'Patient Appointments - Delete Appointments');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_appointments_add', 'Patient Appointments - Add Appointments');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_appointments_edit', 'Patient Appointments - Edit an Appointments');

delete from ix_activity where "NAME" = 'patient_attachments';
delete from ix_activity where "NAME" = 'patient_attachments_add';
delete from ix_activity where "NAME" = 'patient_attachments_delete';
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_attachments', 'Patient Attachment - View Attachments');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_attachments_add', 'Patient Attachment - Add Attachments');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_attachments_delete', 'Patient Attachment - Delete Attachments');

delete from ix_activity where "NAME" = 'patient_consent';
delete from ix_activity where "NAME" = 'patient_consent_update';
delete from ix_activity where "NAME" = 'patient_consent_study_add';
delete from ix_activity where "NAME" = 'patient_consent_study_delete';
delete from ix_activity where "NAME" = 'patient_consent_study_edit';
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_consent', 'Patient Consent - View Consent');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_consent_update', 'Patient Consent - Update Consent');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_consent_study_add', 'Patient Study Consent - Add Study');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_consent_study_delete', 'Patient Study Consent - Delete Study');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_consent_study_edit', 'Patient Study Consent - Edit Study');

delete from ix_activity where "NAME" = 'patient_edit_admission';
delete from ix_activity where "NAME" = 'patient_admissions';
delete from ix_activity where "NAME" = 'patient_admissions_add';
delete from ix_activity where "NAME" = 'patient_admissions_edit';
delete from ix_activity where "NAME" = 'patient_admissions_delete';
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_admissions', 'Admissions - View Admissions');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_admissions_add', 'Admissions - Add Admissions');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_admissions_edit', 'Admissions - Edit Admissions');
INSERT INTO "ix_activity" ("NAME", "DESCRIPTION") VALUES ('patient_admissions_delete', 'Admissions - Delete Admissions');

