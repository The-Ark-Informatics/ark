DELETE FROM study.ark_module_function 
where ark_module_id = (SELECT id from study.ark_module where name='subject')
        AND ark_function_id = (select id from study.ark_function where name='SUBJECT_UPLOAD');

SET @SEQ_ID = 1;

SELECT @SEQ_ID := max(amf.function_sequence)+1 
FROM study.ark_module_function amf
     	inner join study.ark_module am on am.id=amf.ark_module_id   
where am.name='study';

INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) 
VALUES ((SELECT id from study.ark_module where name='study'), 
        (select id from study.ark_function where name='SUBJECT_UPLOAD'), 
        @SEQ_ID );
