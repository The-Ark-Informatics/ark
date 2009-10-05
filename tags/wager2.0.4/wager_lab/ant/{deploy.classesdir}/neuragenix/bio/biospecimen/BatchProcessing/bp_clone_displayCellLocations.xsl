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
  
  <xsl:template match="BiospecimenGeneration">
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
			Batch Processing - Available Locations<br/><hr/>
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
        
        <br />
        <br />
        <xsl:value-of select="strError" />
        <table width="100%">
        <tr class="uportal-input-text">
           <td></td>
           <td><xsl:value-of select="TRAY_strTrayNameDisplay" /></td>
           <td>Available Cell Set</td>
        </tr>
        
        <form name="PICKEDTRAY" action="{$baseActionURL}?current=bp_clone&amp;stage=TRAYPICK" method="POST">
        <xsl:for-each select="CellSet">
        
        <tr>
                   <xsl:attribute name="class">
                      <xsl:choose>
                      <xsl:when test="position() mod 2 != 0">
                         uportal-input-text
                      </xsl:when>
                      <xsl:otherwise>
                         uportal-text
                      </xsl:otherwise>
                      </xsl:choose>
                   </xsl:attribute>
        
            <td><input type="radio" name="CELLSETTOALLOCATE" value="{SetID}" /></td>
            <td>
            
            <xsl:for-each select="AvailableCell">
               Internal ID : <xsl:value-of select="CELL_intCellID" /> <br />
               Row : <xsl:value-of select="CELL_intRowNo" /> <br />
               Column : <xsl:value-of select="CELL_intColNo" /> <br />
            </xsl:for-each>
            
            
            </td>
        </tr>    
        </xsl:for-each>
        <tr>
           <td colspan="3">
             <input type="submit" value="Do it!" />
           </td>
        </tr>
        </form>
        </table>
        
        
        
           </td>
        </tr>
        
                
	</table>
	
  </xsl:template>

      
       
 
</xsl:stylesheet>
