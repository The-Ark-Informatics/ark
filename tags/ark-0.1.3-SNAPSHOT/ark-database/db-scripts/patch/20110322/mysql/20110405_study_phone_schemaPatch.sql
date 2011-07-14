-- Enforces uniqueness on the area code and phone number for a specific person 
USE study;
CREATE UNIQUE INDEX `AREA_CODE` USING BTREE ON `phone`(`AREA_CODE`, `PHONE_NUMBER`, `PERSON_ID`);