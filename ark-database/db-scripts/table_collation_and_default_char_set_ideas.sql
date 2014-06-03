select * from study;


SELECT * FROM information_schema.SCHEMATA ;


SELECT T.table_name, CCSA.character_set_name FROM information_schema.`TABLES` T,
       information_schema.`COLLATION_CHARACTER_SET_APPLICABILITY` CCSA
WHERE CCSA.collation_name = T.table_collation;

alter table study.study convert to character set utf8 collate utf8_unicode_ci;

/**** stack overflow reference 
For Schemas:

SELECT default_character_set_name FROM information_schema.SCHEMATA S
WHERE schema_name = "schemaname";

For Tables:

SELECT CCSA.character_set_name FROM information_schema.`TABLES` T,
       information_schema.`COLLATION_CHARACTER_SET_APPLICABILITY` CCSA
WHERE CCSA.collation_name = T.table_collation
  AND T.table_schema = "schemaname"
  AND T.table_name = "tablename";

For Columns:

SELECT character_set_name FROM information_schema.`COLUMNS` C
WHERE table_schema = "schemaname"
  AND table_name = "tablename"
  AND column_name = "columnname";
****************/