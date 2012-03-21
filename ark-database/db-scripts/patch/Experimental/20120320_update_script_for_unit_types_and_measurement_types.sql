/*#SELECT * FROM `study`.`unit_type`;
#select * from study.ark_function_type; can be rport or non-report
#select * from study.measurement_type;


#--dev only run drop if exists
#--drop table study.measurement_type;
*/
CREATE TABLE study.measurement_type (
	ID INT primary key,
	VALUE VARCHAR(64),
	DESCRIPTION VARCHAR(512)
);


ALTER TABLE study.unit_type
	ADD COLUMN MEASUREMENT_TYPE_ID int;

ALTER TABLE study.unit_type
	ADD COLUMN DISPLAY_ORDER int;

ALTER TABLE study.unit_type
	ADD CONSTRAINT
	FOREIGN KEY fk_unit_type_measurement(MEASUREMENT_TYPE_ID) 
	REFERENCES study.measurement_type(ID) ; 


INSERT INTO study.measurement_type(ID, VALUE)
VALUES (999, 'Other');
INSERT INTO study.measurement_type(ID, VALUE)
VALUES (1, 'Distance');
INSERT INTO study.measurement_type(ID, VALUE)
VALUES (2, 'Volume');
INSERT INTO study.measurement_type(ID, VALUE)
VALUES (3, 'Time');
INSERT INTO study.measurement_type(ID, VALUE)
VALUES (4, 'Weight');
INSERT INTO study.measurement_type(ID, VALUE)
VALUES (5, 'Weight per Volume');
INSERT INTO study.measurement_type(ID, VALUE)
VALUES (6, 'Distance per Time');
INSERT INTO study.measurement_type(ID, VALUE)
VALUES (7, 'Percentage or Fraction');



