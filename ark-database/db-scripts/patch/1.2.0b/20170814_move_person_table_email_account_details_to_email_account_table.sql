INSERT INTO study.email_account (NAME, PRIMARY_ACCOUNT, PERSON_ID, EMAIL_ACCOUNT_TYPE_ID, EMAIL_STATUS_ID)
select p.PREFERRED_EMAIL as NAME, 1 as PRIMARY_ACCOUNT, p.id as PERSON_ID, 1 as EMAIL_ACCOUNT_TYPE_ID,p.PREFERRED_EMAIL_STATUS as EMAIL_STATUS_ID
from study.person p 
where (p.preferred_email is not null and p.preferred_email <> '');

INSERT INTO study.email_account (NAME, PRIMARY_ACCOUNT, PERSON_ID, EMAIL_ACCOUNT_TYPE_ID, EMAIL_STATUS_ID)
select p.OTHER_EMAIL as Name, 0 as PRIMARY_ACCOUNT, p.id as PERSON_ID, 1 as EMAIL_ACCOUNT_TYPE_ID, p.OTHER_EMAIL_STATUS as EMAIL_STATUS_ID
from study.person p 
where (p.other_email is not null and p.other_email <> '');
