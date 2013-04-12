use study;

set @moduleId = 0;

set @functionId = 0;

set @sequenceNo = 0;

select @moduleId := id from ark_module where name="Subject";

select @functionId := id from ark_function where name="Pedigree";

select @sequenceNo := max(function_sequence)+1 from ark_module_function where ark_module_id = @moduleId;

INSERT INTO ark_module_function (`ARK_MODULE_ID`,`ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) 
VALUES (@moduleId, @functionId, @sequenceNo);
