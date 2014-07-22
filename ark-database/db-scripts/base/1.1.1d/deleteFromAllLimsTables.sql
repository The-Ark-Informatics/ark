select * from   lims.barcode_printer ; 

delete from lims.access_request ;   -- perhaps we can make this useable...we have a drop down on biospecimens for it
delete from lims.appointments ;    --  Can we just delete this
delete from lims.attachment ;	 --  Can we just delete this	
delete from lims.barcode_command ;
delete from lims.barcode_label where study_id is not null;
delete from lims.barcode_printer ;   -- not used - it actually could be a perfect table for ip printing...maybe let's keep and use?
delete from lims.barcodeformat ;    -- can we possibly delete this
delete from lims.barcodeid_engine ;  -- can we possibly delete this
delete from lims.bio_transaction ;
delete from lims.biocollection ;
delete from lims.biocollection_custom_field_data ;
delete from lims.biocollectionuid_sequence ;
delete from lims.biocollectionuid_template ;
delete from lims.biospecimenuid_template ;
delete from lims.biospecimen_before_fieldchange ;    -- definitely get rid of this!  but may have constraint issues
delete from lims.biospecimen_custom_field_data ;
delete from lims.biospecimenuid_sequence ;
delete from lims.biospecimen ;
delete from lims.flag ;  			-- this can be deleted
delete from lims.group ;        -- this can be deleted
delete from lims.inv_cell ;
delete from lims.inv_box ;
delete from lims.inv_rack ;
delete from lims.inv_freezer ;
delete from lims.inv_site ;
delete from lims.inv_type ;      -- this can be deleted
delete from lims.listofvalues ;     -- this can be deleted
delete from lims.listofvalues_description ; -- this can be deleted
delete from lims.note ; -- this can be deleted
delete from lims.samplecode ; -- this can be deleted
delete from lims.study_inv_site ;
/*dropped these
delete from lims.tmp_bio_transaction ;
delete from lims.tmp_biospecimen ;
delete from lims.tmp_biospecimen_custom_field_data ;

— these tables may not be everywhere
delete from lims.tmp_biospecimen;
delete from lims.tmp_bio_transaction;
delete from lims.tmp_biospecimen_custom_field_data;


*/
/* drop tables...this can happen in another script
delete from lims.biodata ;
delete from lims.biodata_criteria ;
delete from lims.biodata_field ;
delete from lims.biodata_field_group ;
delete from lims.biodata_field_lov ;
delete from lims.biodata_group ;
delete from lims.biodata_group_criteria ;
delete from lims.biodata_lov_list ;
delete from lims.biodata_type ;
delete from lims.biodata_unit ;
*/

/*******************
 LOOK UP TABLES DO NOT DELETE
delete from lims.cell_status ;
delete from lims.bio_sampletype ;
delete from lims.biospecimen_status ;
delete from lims.biospecimen_grade ;
delete from lims.biospecimen_protocol ;
delete from lims.biospecimen_quality ;
delete from lims.biospecimen_species ;
delete from lims.biospecimen_storage ;
delete from lims.biospecimenuid_padchar ;
delete from lims.biospecimenuid_token ;
delete from lims.treatment_type ;
delete from lims.unit ;
delete from lims.inv_col_row_type ;
delete from lims.biospecimen_anticoagulant ;
delete from lims.barcode_label_data ;
delete from lims.bio_transaction_status ;


delete from lims.biocollectionuid_padchar ;
delete from lims.biocollectionuid_token ;

*************************/