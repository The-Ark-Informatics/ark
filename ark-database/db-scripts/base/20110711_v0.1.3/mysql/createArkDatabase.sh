#! /bin/bash
HOST_STRING=localhost
USERNAME=arkadmin
PASSWORD=REPLACE
VERSION=20110711

echo "Dropping/Creating schemas..."
mysql -h $HOST_STRING -u $USERNAME -p$PASSWORD<<EOFMYSQL
DROP SCHEMA IF EXISTS pheno;
DROP SCHEMA IF EXISTS geno;
DROP SCHEMA IF EXISTS lims;
DROP SCHEMA IF EXISTS reporting;
DROP SCHEMA IF EXISTS study;

CREATE SCHEMA IF NOT EXISTS study;
CREATE SCHEMA IF NOT EXISTS pheno;
CREATE SCHEMA IF NOT EXISTS geno;
CREATE SCHEMA IF NOT EXISTS lims;
CREATE SCHEMA IF NOT EXISTS reporting;
EOFMYSQL

CURRENT_DIR=`pwd`
echo "Current directory: $CURRENT_DIR"

echo "Importing from dump files..."
mysql -h $HOST_STRING -u $USERNAME -p$PASSWORD study <  "$CURRENT_DIR/$VERSION""_study_schemaOnly.sql"
mysql -h $HOST_STRING -u $USERNAME -p$PASSWORD study < "$CURRENT_DIR/$VERSION""_study_dataOnly.sql"
mysql -h $HOST_STRING -u $USERNAME -p$PASSWORD pheno < "$CURRENT_DIR/$VERSION""_pheno_schemaOnly.sql"
mysql -h $HOST_STRING -u $USERNAME -p$PASSWORD pheno < "$CURRENT_DIR/$VERSION""_pheno_dataOnly.sql"
mysql -h $HOST_STRING -u $USERNAME -p$PASSWORD geno < "$CURRENT_DIR/$VERSION""_geno_schemaOnly.sql"
mysql -h $HOST_STRING -u $USERNAME -p$PASSWORD geno < "$CURRENT_DIR/$VERSION""_geno_dataOnly.sql"
mysql -h $HOST_STRING -u $USERNAME -p$PASSWORD lims < "$CURRENT_DIR/$VERSION""_lims_schemaOnly.sql"
mysql -h $HOST_STRING -u $USERNAME -p$PASSWORD lims < "$CURRENT_DIR/$VERSION""_lims_dataOnly.sql"
mysql -h $HOST_STRING -u $USERNAME -p$PASSWORD reporting < "$CURRENT_DIR/$VERSION""_reporting_schemaOnly.sql"
mysql -h $HOST_STRING -u $USERNAME -p$PASSWORD reporting < "$CURRENT_DIR/$VERSION""_reporting_dataOnly.sql"

echo "Creating default Super Administrator"
mysql -h $HOST_STRING -u $USERNAME -p$PASSWORD reporting < "$CURRENT_DIR/$VERSION""_createSuperAdministrator.sql"