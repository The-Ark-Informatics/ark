SELECT * FROM reporting.custom_field_display_search;

/*option one*/
select from thistaquery_filterble
where cfd.cf.function = constants.fun ction_lims
and search = 1

select from thistable
where cfd.cf.function = constatnts.pheno
and search = 1

x6ish queries

/*option 2 - cfd_search has a type/function field - means storing an extra thing that could derived...yet getting it less complicated?*/
select from thistable
where type = lims
and search = 1

select from thistable
where type = pheno
and search = 1

x6ish queries