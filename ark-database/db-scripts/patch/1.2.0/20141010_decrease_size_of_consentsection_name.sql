/**** in order to help enable future UTF-8 usage, decrease the size of this overly large name which also has unique constraint on it
or else mysql will have issues ****/

ALTER TABLE `study`.`consent_section` MODIFY `NAME` VARCHAR(255);

