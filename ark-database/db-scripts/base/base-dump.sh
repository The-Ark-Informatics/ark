#!/bin/bash

EXPECTED_ARGS=3
E_BADARGS=65

if [ $# -ne $EXPECTED_ARGS ] 
then
  echo "Usage: `basename $0` {version} {databsae hostname} {databsae password}"
  exit $E_BADARGS
fi

VERSION=$1
HOSTNAME=$2
PASSWORD=$3

echo "Exporting database schemas"
echo "==============================="
echo "/* MySQL script to create the Ark database schema and default reference tables */;" > $VERSION/ark-$VERSION.sql
echo "" >> $VERSION/ark-$VERSION.sql

echo "/* Creating the schemas */" >> $VERSION/ark-$VERSION.sql
echo "CREATE DATABASE /*!32312 IF NOT EXISTS*/ audit /*!40100 DEFAULT CHARACTER SET latin1 */;" >> $VERSION/ark-$VERSION.sql
echo "CREATE DATABASE /*!32312 IF NOT EXISTS*/ study /*!40100 DEFAULT CHARACTER SET latin1 */;" >> $VERSION/ark-$VERSION.sql
echo "CREATE DATABASE /*!32312 IF NOT EXISTS*/ pheno /*!40100 DEFAULT CHARACTER SET latin1 */;" >> $VERSION/ark-$VERSION.sql
echo "CREATE DATABASE /*!32312 IF NOT EXISTS*/ geno /*!40100 DEFAULT CHARACTER SET latin1 */;" >> $VERSION/ark-$VERSION.sql
echo "CREATE DATABASE /*!32312 IF NOT EXISTS*/ lims /*!40100 DEFAULT CHARACTER SET latin1 */;" >> $VERSION/ark-$VERSION.sql
echo "CREATE DATABASE /*!32312 IF NOT EXISTS*/ reporting /*!40100 DEFAULT CHARACTER SET latin1 */;" >> $VERSION/ark-$VERSION.sql
echo "" >> $VERSION/ark-$VERSION.sql

echo ""
echo "Exporting Audit table structures"
echo "" >> $VERSION/ark-$VERSION.sql
echo 'USE audit;' >> $VERSION/ark-$VERSION.sql
echo "" >> $VERSION/ark-$VERSION.sql
mysqldump --no-data -h $HOSTNAME -u arkadmin -p$PASSWORD audit | sed 's/AUTO_INCREMENT=[0-9]*\b//' >> $VERSION/ark-$VERSION.sql

echo ""
echo "Study table structures"
echo "" >> $VERSION/ark-$VERSION.sql
echo 'USE study;' >> $VERSION/ark-$VERSION.sql
mysqldump --no-data -h $HOSTNAME -u arkadmin -p$PASSWORD study | sed 's/AUTO_INCREMENT=[0-9]*\b//' | sed 's/130.95.56/127.0.0/' >> $VERSION/ark-$VERSION.sql

echo ""
echo "Pheno table structures"
echo "" >> $VERSION/ark-$VERSION.sql
echo 'USE pheno;' >> $VERSION/ark-$VERSION.sql
mysqldump --no-data -h $HOSTNAME -u arkadmin -p$PASSWORD pheno | sed 's/AUTO_INCREMENT=[0-9]*\b//' >> $VERSION/ark-$VERSION.sql

echo ""
echo "Geno table structures"
echo "" >> $VERSION/ark-$VERSION.sql
echo 'USE geno;' >> $VERSION/ark-$VERSION.sql
mysqldump --no-data -h $HOSTNAME -u arkadmin -p$PASSWORD geno | sed 's/AUTO_INCREMENT=[0-9]*\b//' >> $VERSION/ark-$VERSION.sql

echo ""
echo "Lims table structures"
echo "" >> $VERSION/ark-$VERSION.sql
echo 'USE lims;' >> $VERSION/ark-$VERSION.sql
mysqldump --no-data -h $HOSTNAME -u arkadmin -p$PASSWORD lims | sed 's/AUTO_INCREMENT=[0-9]*\b//' >> $VERSION/ark-$VERSION.sql

echo ""
echo "Dumping Reporting table structures"
echo "" >> $VERSION/ark-$VERSION.sql
echo 'USE reporting;' >> $VERSION/ark-$VERSION.sql
mysqldump --no-data -h $HOSTNAME -u arkadmin -p$PASSWORD reporting | sed 's/AUTO_INCREMENT=[0-9]*\b//' >> $VERSION/ark-$VERSION.sql

echo "Exporting reference/lookup data"
echo "==============================="
echo "Study reference data"
echo "/* Reference/lookup data */" >> $VERSION/ark-$VERSION.sql
echo "" >> $VERSION/ark-$VERSION.sql
echo 'USE study;' >> $VERSION/ark-$VERSION.sql
mysqldump -h $HOSTNAME -u arkadmin -p$PASSWORD --no-create-info --complete-insert study action_type \
address_status address_type ark_function ark_function_type ark_module ark_module_function \
ark_module_role ark_permission ark_role ark_role_policy_template \
consent_answer consent_status consent_type correspondence_direction_type \
correspondence_mode_type correspondence_outcome_type correspondence_status_type country \
country_state delimiter_type domain_type email_account_type entity_type field_type file_format \
gender_type marital_status person_contact_method phone_status phone_type \
registration_status relationship study_comp_status study_status subject_status subjectuid_padchar \
subjectuid_token title_type unit_type vital_status yes_no consent_option measurement_type \
>> $VERSION/ark-$VERSION.sql

echo "Pheno reference data"
echo "" >> $VERSION/ark-$VERSION.sql
echo 'USE pheno;' >> $VERSION/ark-$VERSION.sql
mysqldump -h $HOSTNAME -u arkadmin -p$PASSWORD --no-create-info --complete-insert pheno delimiter_type \
field_type file_format questionnaire_status status >> $VERSION/ark-$VERSION.sql 

echo "Geno reference data"
echo "" >> $VERSION/ark-$VERSION.sql
echo 'USE geno;' >> $VERSION/ark-$VERSION.sql
mysqldump -h $HOSTNAME -u arkadmin -p$PASSWORD --no-create-info --complete-insert geno delimiter_type \
file_format import_type marker_type meta_data_type status >> $VERSION/ark-$VERSION.sql

echo "LIMS reference data"
echo "" >> $VERSION/ark-$VERSION.sql
echo 'USE lims;' >> $VERSION/ark-$VERSION.sql
mysqldump -h $HOSTNAME -u arkadmin -p$PASSWORD --no-create-info --complete-insert lims bio_sampletype \
bio_transaction_status biospecimen_anticoagulant biospecimen_grade \
biospecimen_quality biospecimen_species biospecimen_status biospecimen_storage \
biocollectionuid_padchar biocollectionuid_token biospecimenuid_padchar biospecimenuid_token cell_status barcode_label barcode_label_data \
inv_col_row_type treatment_type unit >> $VERSION/ark-$VERSION.sql

echo "Reporting reference data"
echo "" >> $VERSION/ark-$VERSION.sql
echo 'USE reporting;' >> $VERSION/ark-$VERSION.sql
mysqldump -h $HOSTNAME -u arkadmin -p$PASSWORD --no-create-info --complete-insert reporting \
report_output_format report_template >> $VERSION/ark-$VERSION.sql

echo "/* Initialise the super user in the database */" >> $VERSION/ark-$VERSION.sql
echo "" >> $VERSION/ark-$VERSION.sql
echo "USE study;" >> $VERSION/ark-$VERSION.sql
echo "-- Insert first Super User as a valid account (replace the value for 'LDAP_USER_NAME' if necessary)" >> $VERSION/ark-$VERSION.sql 
echo "INSERT INTO study.ark_user (ID, LDAP_USER_NAME) VALUES (1, 'arksuperuser@ark.org.au');" >> $VERSION/ark-$VERSION.sql
echo "-- Set up the permissions for the first Super User (ark_role_id = 1)" >> $VERSION/ark-$VERSION.sql
echo "INSERT INTO study.ark_user_role (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (1,1,1,1,NULL);" >> $VERSION/ark-$VERSION.sql
echo "INSERT INTO study.ark_user_role (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (2,1,1,2,NULL);" >> $VERSION/ark-$VERSION.sql
echo "INSERT INTO study.ark_user_role (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (3,1,1,3,NULL);" >> $VERSION/ark-$VERSION.sql
echo "INSERT INTO study.ark_user_role (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (4,1,1,4,NULL);" >> $VERSION/ark-$VERSION.sql
echo "INSERT INTO study.ark_user_role (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (5,1,1,5,NULL);" >> $VERSION/ark-$VERSION.sql
echo "-- NB: ark_module_id = 6 (Reporting) omitted, because reporting relies on permissions defined in other modules." >> $VERSION/ark-$VERSION.sql
echo "INSERT INTO study.ark_user_role (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (7,1,1,7,NULL);" >> $VERSION/ark-$VERSION.sql