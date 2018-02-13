use study;

/*1*/  update ark_role set description='The top-level administrator of The Ark, capable of performing all functions.' where name='Super Administrator';
/*2*/  update ark_role set description='Administrator access to study management functionality.' where name='Study Administrator	';
/*3*/  update ark_role set description='Read-only access to study management functionality.' where name='Study Read-Only User';
/*4*/  update ark_role set description='Administrator access to subject management functionality.' where name='Subject Administrator';
/*5*/  update ark_role set description='Data manager access to subject management functionality.' where name='Subject Data Manager';
/*6*/  update ark_role set description='Read-only access to subject management functionality.' where name='Subject Read-Only User';
/*7*/  update ark_role set description='Read-only access to clinical dataset management functionality.' where name='Pheno Read-Only User';
/*8*/  update ark_role set description='Data manager access to clinical dataset management functionality.' where name='Pheno Data Manager';
/*9*/  update ark_role set description='Read-only access to Laboratory Information Management System (LIMS) functionality.' where name='LIMS Read-Only User';
/*10*/ update ark_role set description='Data manager access to Laboratory Information Management System (LIMS) functionality.' where name='LIMS Data Manager';
/*12*/ update ark_role set description='Administrator access to Laboratory Information Management System (LIMS) functionality.' where name='LIMS Administrator';
/*13*/ update ark_role set description='Administrator access to clinical dataset management functionality.' where name='Pheno Administrator';
/*15*/ update ark_role set description='Read-only access to work tracking functionality.' where name='Work Tracking Read-Only User';
/*16*/ update ark_role set description='Administrator access to work tracking functionality.' where name='Work Tracking Administrator';
/*17*/ update ark_role set description='Data manager access to work tracking functionality.' where name='Work Tracking Data Manager';
/*18*/ update ark_role set description='Administrator access to Laboratory Information Management System (LIMS) functionality.' where name='Laboratory Administrator';
/*20*/ update ark_role set description='Administrator access to reporting functionality.' where name='Reporting Administrator';
/*21*/ update ark_role set description='Data manager access to reporting functionality.' where name='Reporting Data Manager';
/*22*/ update ark_role set description='Read-only access to reporting functionality.' where name='Reporting Read-Only User';
/*23*/ update ark_role set description='Administrator access to disease data management functionality.' where name='Disease Administrator';
/*24*/ update ark_role set description='Data manager access to disease data management functionality.' where name='Disease Data Manager';
/*25*/ update ark_role set description='Read-only access to disease data management functionality.' where name='Disease Read-Only User';
/*26*/ update ark_role set description='Administrator access to genomics (SPARK) functionality.' where name='Genomics Administrator';
/*27*/ update ark_role set description='Data manager access to genomics (SPARK) functionality.' where name='Genomics Data Manager';
/*28*/ update ark_role set description='Read-only access to genomics (SPARK) functionality.' where name='Genomics Read-Only User';
/*29*/ update ark_role set description='Administrator access to calendar functionality.' where name='Calendar Administrator';
/*30*/ update ark_role set description='Data manager access to calendar functionality.' where name='Calendar Data Manager';
/*31*/ update ark_role set description='Read-only access to calendar functionality.' where name='Calendar Read-Only User';

SET @ID1=(SELECT ID FROM `study`.`ark_role` WHERE name='Geno Read-Only User'); 
SET @ID2=(SELECT ID FROM `study`.`ark_role` WHERE name='New Role'); 

DELETE FROM `study`.`ark_role_policy_template` WHERE `ARK_ROLE_ID`=@ID1;
DELETE FROM `study`.`ark_role` WHERE `ID`=@ID1;
DELETE FROM `study`.`ark_role_policy_template` WHERE `ARK_ROLE_ID`=@ID2;
DELETE FROM `study`.`ark_role` WHERE `ID`=@ID2;

