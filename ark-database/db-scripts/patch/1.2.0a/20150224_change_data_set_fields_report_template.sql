-- ARK-1395 Improve contents of reports table
use reporting;

-- Rename report descriptions

Update report_template
set DESCRIPTION = 'A listing of phenotype fields comprising a dataset definition.'
where NAME = 'Datasets Field Details Report (Data Dictionary)';

-- Rename reports

Update report_template
set NAME = 'Dataset Field Details'
where NAME = 'Datasets Field Details Report (Data Dictionary)';
