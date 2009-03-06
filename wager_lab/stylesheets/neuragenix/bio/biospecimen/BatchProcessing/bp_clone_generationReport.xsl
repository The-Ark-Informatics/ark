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
			Batch Processing - Generation Report<br/><hr/>
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
		<td class="neuragenix-form-required-text" width="80%">
                    <xsl:value-of select="strErrorDuplicateKey" /><xsl:value-of select="strErrorRequiredFields" /><xsl:value-of select="strErrorInvalidDataFields" /><xsl:value-of select="strErrorInvalidData" /><xsl:value-of select="strErrorChildStillExist" /><xsl:value-of select="strErrorBiospecTypeChange" />
                    <xsl:value-of select="strErrorMessage" /><xsl:value-of select="strLockError" />
		</td>
		<td class="neuragenix-form-required-text" width="20%" id="neuragenix-required-header" align="right">
                    
		</td>
            </tr>
	</table>
        
        <form action="{$baseActionURL}?uP_root=root&amp;current=biospecimen_search" method="post">
        <xsl:for-each select="Allocation">
            <table width="100%">
                <tr>
                    <td width="50%">
                        <table width="100%">
                            <tr>
                                <td width="40%" class="uportal-label">
                                    Parent ID:
                                </td>
                                <td width="60%" class="uportal-text">
                                    <xsl:value-of select="strBiospecimenID" />
                                </td>
                            </tr>
                            
                            <tr>
                                <td width="40%" class="uportal-label">
                                    Parent Type:
                                </td>
                                <td width="60%" class="uportal-text">
                                    <xsl:value-of select="strInitialBiospecSampleType" />
                                </td>
                            </tr>
                            
                             <tr>
                                <td width="40%" class="uportal-label">
                                    Allocation Type:
                                </td>
                                    <td width="60%" class="uportal-text">
                                        <xsl:choose>
                                            <xsl:when test="GD_AllocationMode=0">
                                                Continuous
                                            </xsl:when>
                                            <xsl:when test="GD_AllocationMode=1">
                                                Fill Gaps
                                            </xsl:when>
                                            <xsl:when test="GD_AllocationMode=2">
                                                Create New Tray
                                            </xsl:when>
                                            <xsl:when test="GD_AllocationMode=3">
                                                No Allocation
                                            </xsl:when>
                                            <xsl:otherwise>
                                                Undefined
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </td>
                            </tr>
                            
                            
                            
                            <tr>
                                <td height="20px" colspan="2"></td>
                            </tr>
                            
                            <tr>
                                <td colspan="2" class="uportal-channel-table-header"><xsl:value-of select="VIAL_CALCULATION_intVialStored" /> Sub-specimens created and allocated at:</td>
                            </tr>
                            
                            <xsl:for-each select="InventoryNames">
                            <tr>
                                <td width="40%" class="uportal-label">
                                    Site name:
                                </td>
                                <td width="60%" class="uportal-text">
                                    <xsl:value-of select="strSiteName" />
                                </td>
                            </tr>
                            
                            <tr>
                                <td width="40%" class="uportal-label">
                                    Tank name:
                                </td>
                                <td width="60%" class="uportal-text">
                                    <xsl:value-of select="strTankName" />
                                </td>
                            </tr>
                            
                            <tr>
                                <td width="40%" class="uportal-label">
                                    Box name:
                                </td>
                                <td width="60%" class="uportal-text">
                                    <xsl:value-of select="strBoxName" />
                                </td>
                            </tr>
                            
                            <tr>
                                <td width="40%" class="uportal-label">
                                    Tray name:
                                </td>
                                <td width="60%" class="uportal-text">
                                    <xsl:value-of select="strTrayName" />
                                </td>
                            </tr>
                            </xsl:for-each>
                            
                            <tr>
                                <td height="20px" colspan="2"></td>
                            </tr>
                            
                            <tr>
                                <td width="40%" class="uportal-channel-table-header">
                                    Sample ID
                                </td>
                                <td width="60%" class="uportal-channel-table-header">
                                    Cell location
                                </td>
                            </tr>
                            
                            <xsl:for-each select="newGeneratedBiospecimen">
                            <tr>
                                <td width="40%" class="uportal-label">
                                    <xsl:value-of select="strBiospecimenID" />
                                </td>
                                <td width="60%" class="uportal-text">
                                    <xsl:choose>
                                        <xsl:when test="../noAllocation='true'">
                                           N/A
                                        </xsl:when>
                                        <xsl:otherwise>
                                           <xsl:value-of select="strLocation" />
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>
                            </xsl:for-each>
                        </table>
                    </td>
                    <td width="50%">
                        <table border="1">
                        
                            <xsl:for-each select="Row">
                            <tr>
                                <xsl:for-each select="Col">
                                <xsl:variable name="label"><xsl:value-of select="label" /></xsl:variable>
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="$label='0'">
                                        </xsl:when>

                                        <xsl:when test="$label='-1'">
                                            <xsl:variable name="CELL_intCellID"><xsl:value-of select="CELL_intCellID" /></xsl:variable>
                                            <xsl:variable name="CELL_intBiospecimenID"><xsl:value-of select="CELL_intBiospecimenID" /></xsl:variable>
                                            <xsl:variable name="CELL_intPatientID"><xsl:value-of select="CELL_intPatientID" /></xsl:variable>
                                            <xsl:variable name="CELL_info"><xsl:value-of select="CELL_info" /></xsl:variable>

                                            <xsl:choose>
                                                <xsl:when test="$CELL_intBiospecimenID = '-1'">
                                                    <img src="media/neuragenix/icons/unused.gif" border="0" title="{$CELL_info}" />
                                                </xsl:when>

                                                <xsl:when test="$CELL_intBiospecimenID = '-2'">
                                                    <img src="media/neuragenix/icons/tobeused.gif" border="0" title="{$CELL_info}" />
                                                </xsl:when>
                                                
                                                <xsl:otherwise>
                                                    <img src="media/neuragenix/icons/used.gif" border="0" title="{$CELL_info}" />
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:when>

                                        <xsl:otherwise>
                                            <xsl:value-of select="label" />
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                                </xsl:for-each>
                                
                            </tr>    
                            </xsl:for-each>
                        </table>
                        <xsl:if test="not(noAllocation='true')">
                            <table width="100%">
                                <tr>
                                    <!--td width="10%"></td-->
                                    <td width="10%">
                                        <img src="media/neuragenix/icons/tobeused.gif" border="0" />
                                    </td>
                                    <td width="90%" class="uportal-text">
                                        Newly Allocated Cells
                                    </td>
                                </tr>
                                <tr>
                                    <!--td width="10%"></td-->
                                    <td width="10%">
                                        <img src="media/neuragenix/icons/used.gif" border="0" />
                                    </td>
                                    <td width="90%" class="uportal-text">
                                        Existing Cells
                                    </td>
                                </tr>
                            </table>
                        </xsl:if>
                    </td>
                </tr>
            </table>
            
            <table width="100%">
                <tr><td><hr /></td></tr>
            </table>
    
            
            
            
         </xsl:for-each>  
         
            <table width="100%">
                <tr>
                    <td>
                        <!-- <input type="submit" name="back" value="{$backBtnLabel}" class="uportal-button" /> -->
                        <input type="submit" name="finish" value="Finished" class="uportal-button" />
                        <!-- <input type="button" name="print" value="Print" class="uportal-button" onclick="javascript:printPage()" /> -->
                    </td>
                </tr>
            </table>
         
         
            </form>
     
            
	
  </xsl:template>

      
       
 
</xsl:stylesheet>
