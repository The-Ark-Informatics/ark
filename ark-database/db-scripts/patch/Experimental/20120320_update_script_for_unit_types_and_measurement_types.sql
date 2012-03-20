#SELECT * FROM `study`.`unit_type`;

#select * from study.ark_function_type; can be rport or non-report
/*
CREATE TABLE study.measurement_type (
	ID INT primary key,
	VALUE VARCHAR(64),
	DESCRIPTION VARCHAR(512)
);

select * from study.measurement_type;

ALTER TABLE study.unit_type
	ADD COLUMN MEASUREMENT_TYPE_ID int;

ALTER TABLE study.unit_type
	ADD COLUMN DISPLAY_ORDER int;

ALTER TABLE study.unit_type
	ADD CONSTRAINT
	FOREIGN KEY fk_unit_type_measurement(MEASUREMENT_TYPE_ID) 
	REFERENCES study.measurement_type(ID) ; 
*/

insert into study.measurement_type(id, value)
values (0, 'Other');
insert into study.measurement_type(id, value)
values (1, 'Distance');
insert into study.measurement_type(id, value)
values (2, 'Volume');
insert into study.measurement_type(id, value)
values (3, 'Time');
insert into study.measurement_type(id, value)
values (4, 'Weight');
insert into study.measurement_type(id, value)
values (5, 'Weight per Volume');
insert into study.measurement_type(id, value)
values (6, 'Distance per Time');
insert into study.measurement_type(id, value)
values (7, 'Percentage or Fraction');

