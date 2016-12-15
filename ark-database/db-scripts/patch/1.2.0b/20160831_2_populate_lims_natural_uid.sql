update `lims`.`biocollection` set natural_uid=`study`.udf_NaturalSortFormat(biocollection_uid, 20, ' ');
update `lims`.`biospecimen` set natural_uid=`study`.udf_NaturalSortFormat(biospecimen_uid, 20, ' ');
