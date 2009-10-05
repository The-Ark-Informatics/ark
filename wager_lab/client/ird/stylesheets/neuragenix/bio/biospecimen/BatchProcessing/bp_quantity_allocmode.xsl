<?xml version="1.0" encoding="utf-8"?>
<!-- 

    Biospecimen Batch Processing - Batch Cloning Module
    Copyright (C) Neuragenix Pty Ltd, 2005
    Author : Daniel Murley
    Email : dmurley@neuragenix.com
    
    Purpose : Provides first stage of data entry for a user to batch create a set of biospecimens
   
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="../biospecimen_menu.xsl"/>
  <xsl:output method="html" indent="no" />
  
  <xsl:template match="QuantityAllocation">
  <!-- Get the parameters from the channel class -->
  <xsl:param name="strBiospecimenID"><xsl:value-of select="strBiospecimenID" /></xsl:param>
  <xsl:param name="strBiospecOtherID"><xsl:value-of select="strBiospecOtherID" /></xsl:param>
  <xsl:param name="intBiospecStudyID"><xsl:value-of select="intBiospecStudyID" /></xsl:param>
  <xsl:param name="intInternalPatientID"><xsl:value-of select="intInternalPatientID" /></xsl:param>
  <xsl:param name="intBiospecParentID"><xsl:value-of select="intBiospecParentID" /></xsl:param>
  <xsl:param name="strBiospecParentID"><xsl:value-of select="strBiospecParentID" /></xsl:param>
  
  <!-- 
        <input type="hidden" name="intInternalPatientID" value="{$intInternalPatientID}" />
        <input type="hidden" name="intInvCellID" value="-1" />
	<input type="hidden" name="page1completed" value="true" /> -->
        
	<table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Batch Processing - Quantity Allocation Mode<br/><hr/>
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
        
        
        
	
        <table width="100%">
            <tr>
               <td>
               
               <span class="uportal-label">Please select the mode for this allocation :</span>
               <br />
               <br />
               <form name="OptionSelect" action="{$baseActionURL}?module=BATCH_ALLOCATE&amp;current=bp_allocate&amp;stage=GETMODE" method="POST" class="uportal-text">
                  <input type="radio" name="strAllocMode" value="allocate" checked="checked">Find quantities to allocate to a study</input><br />
                  <input type="radio" name="strAllocMode" value="deliver">Find quantities to mark as delivered</input><br />
                  <br />
                  <table width="100%">
                     <tr>
                        <td align="right">
                            <input type="submit" value="Next &gt;" class="uportal-input-text" />
                        </td>
                     </tr>
                  </table>
               
               </form>
               
               
               </td>
            </tr>
        </table>
	
  </xsl:template>

      
       
 
</xsl:stylesheet>
