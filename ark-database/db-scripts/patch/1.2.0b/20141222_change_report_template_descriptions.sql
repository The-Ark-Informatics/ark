-- ARK-1395 Improve contents of reports table
use reporting;

Update report_template
set DESCRIPTION = 'An overview of subject information for a given study.'
where NAME = 'Study Summary Report';

Update report_template
set DESCRIPTION = 'Detailed subject information for study-level consent.'
where NAME = 'Study-level Consent Details Report';

Update report_template
set DESCRIPTION = 'Detailed subject information with respect to study component consent.'
where NAME = 'Study Component Consent Details Report';

Update report_template
set DESCRIPTION = 'A listing of phenotype fields comprising a dataset definition.'
where NAME = 'Phenotypic Field Details Report (Data Dictionary)';

Update report_template
set DESCRIPTION = 'A listing of user roles and permissions for a given study.'
where NAME = 'Study User Role Permissions Report';

Update report_template
set DESCRIPTION = 'A listing of invoiced billable item costs for a researcher.'
where NAME = 'Work Researcher Cost Report';

Update report_template
set DESCRIPTION = 'A listing of invoiced billable item costs, grouped by type, for a researcher.'
where NAME = 'Work Researcher Detail Cost Report';

Update report_template
set DESCRIPTION = 'A listing of invoiced billable item costs, grouped by type, for a study.'
where NAME = 'Work Study Detail Cost Report';

Update report_template
set DESCRIPTION = 'An overview of biospecimen information for a given study.'
where NAME = 'Biospecimen Summary Report';

Update report_template
set DESCRIPTION = 'Detailed biospecimen information, including inventory, for a given study.'
where NAME = 'Biospecimen Detail Report';
