-- JIRA ID WAG-467,WAG-468
-- Insert new WAFSS Report entries.
INSERT
INTO REPORTS
  (
    REPORTKEY,
    REPORTNAME,
    REPORTDESC,
    FILENAME,
    SORTORDER,
    LOGOFILE,
    REPORTTYPE,
    STUDYKEY,
    PRIVILEGE
  )
  VALUES
  (
    4,
    'DNA Report',
    NULL,
    'wafss_dna.jasper',
    4,
    'WADB_logo.jpg',
    'PDF',
    17,
    NULL
  );
INSERT
INTO REPORTS
  (
    REPORTKEY,
    REPORTNAME,
    REPORTDESC,
    FILENAME,
    SORTORDER,
    LOGOFILE,
    REPORTTYPE,
    STUDYKEY,
    PRIVILEGE
  )
  VALUES
  (
    3,
    'ASRB Report',
    NULL,
    'wafss_asrb.jasper',
    3,
    'WADB_logo.jpg',
    'PDF',
    17,
    NULL
  );