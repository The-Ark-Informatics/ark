/*SELECT * FROM `study`.`audit_history`;
*/

/*
update study.unit_type
set measurement_type_id = 0; /* other /

this is a catchall until someone steps up to fill this inother
some may warrent a new type of density, maybe molarity, iu,   or something else

to find out those without category yet ;
	select distinct name from study.unit_type where measurement_type_id is null;


 */


update study.unit_type
set measurement_type_id = 1 /*distanece*/
where name in ('mm', 'cm', 'm', 'km', 'feet');


update study.unit_type
set measurement_type_id = 2 /*volume*/
where name in ('ml', 'L');


update study.unit_type
set measurement_type_id = 3 /*time*/
where name in ('s', 'min', 'hrs', 'Days', 'Months', 'Years', 'Age'); /*yes; age is dubious*/


update study.unit_type
set measurement_type_id = 4 /*weight-mass*/
where name in ('g', 'kg');


update study.unit_type
set measurement_type_id = 5 /*weight per volume*/
where name in ('ug/L', 'g/L', 'g/L', 'g/L', 'mg/dl', 'mg/L', 'kg/m2');


update study.unit_type
set measurement_type_id = 6 /* distance per time */
where name in ('mm/hr');


update study.unit_type
set measurement_type_id = 7 /* percentage or fraction */
where name in ('%');

