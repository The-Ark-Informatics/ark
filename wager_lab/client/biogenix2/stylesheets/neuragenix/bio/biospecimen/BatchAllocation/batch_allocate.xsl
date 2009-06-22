<?xml version="1.0" encoding="utf-8"?>
<!-- 

    Biospecimen Batch Processing - Batch Cloning Module
    Copyright (C) Neuragenix Pty Ltd, 2005
    Author : Daniel Murley
    Email : dmurley@neuragenix.com
    
    Purpose : Provides data entry for user to  enter details about thhe whatever.
   
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <!-- <xsl:include href="../biospecimen_menu.xsl"/>-->
  <xsl:output method="html" indent="yes" />
    <xsl:param name="strErrorMessage">strErrorMessage</xsl:param>
  <xsl:template match="BATCH_ALLOCATE">
  <!-- Get the parameters from the channel class -->
  <xsl:param name="baseActionURL"><xsl:value-of select="baseActionURL" /></xsl:param>
  
  
	<table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Batch Processing - Enter Specimen IDs                
			</td>
		    <td align="right">
		        <form name="back_form" action="{$baseActionURL}?module=batch_clone&amp;current=bp_clone&amp;stage=OPTIONSET&amp;mode=VALIDATION" method="post"/>
		        <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
		            alt="Previous" onclick="javascript:document.back_form.submit();"/>
		        <img border="0" src="media/neuragenix/buttons/next_disabled.gif"
		            alt="Next"/>
		        </td>
		    
		</tr>
	</table>
	<table width="100%">
		 
		<tr>
			<td class="neuragenix-form-required-text" width="50%">
					<xsl:value-of select="strErrorDuplicateKey" /><xsl:value-of select="strErrorRequiredFields" /><xsl:value-of select="strErrorInvalidDataFields" /><xsl:value-of select="strErrorInvalidData" />
			</td>
			<td class="neuragenix-form-required-text" width="50%" id="neuragenix-required-header" align="right">
			* = Required fields
			</td>

		</tr>
	</table>
               
  </xsl:template>
 
  <xsl:template name="GetStudyDropdown">
      <xsl:param name="id" select="id" />
         <select id="study_{$id}" name="study_{$id}" class="uportal-input-text">
            <xsl:for-each select="study_list">
               <option name="study_selected_{$id}" value="{STUDY_intStudyID}"> <xsl:value-of select="STUDY_strStudyName" /></option>
               
            </xsl:for-each>
         </select>
  </xsl:template>
             
      
  
  
  
  
  
</xsl:stylesheet>
