use study;

set @arkFunctionType = 1;

select @arkFunctionType := id from ark_function_type where name = 'NON-REPORT';

INSERT INTO ark_function (`NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ('PEDIGREE', 'Pedigree visualization', @arkFunctionType, 'tab.module.subject.pedigree');
