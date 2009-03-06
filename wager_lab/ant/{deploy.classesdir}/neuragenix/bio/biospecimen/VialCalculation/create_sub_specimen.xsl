<?xml version="1.0" encoding="utf-8"?>
<!-- available_tray_list.xsl, part of the Biospecimen channel -->
<!-- author: Huy Hoang -->
<!-- date: 23/07/2004 -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <!--xsl:include href="./biospecimen_menu.xsl"/-->
   <!-- <xsl:include href="../../common/common_btn_name.xsl"/> -->
    
    <xsl:output method="html" indent="yes" />
    
    <xsl:param name="inventoryChannelURL">inventoryChannelURL_false</xsl:param>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
        
    <xsl:template match="vial_calculation">
    
        <xsl:param name="intInternalPatientID"><xsl:value-of select="BIOSPECIMEN_intInternalPatientID" /></xsl:param>
        <xsl:param name="intBiospecimenID"><xsl:value-of select="BIOSPECIMEN_intBiospecimenID" /></xsl:param>
        <xsl:param name="strBiospecimenID"><xsl:value-of select="BIOSPECIMEN_strBiospecimenID" /></xsl:param>
        <xsl:param name="strInitialBiospecSampleType"><xsl:value-of select="BIOSPECIMEN_strInitialBiospecSampleType" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intVialStored"><xsl:value-of select="VIAL_CALCULATION_intVialStored" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intMRDWash"><xsl:value-of select="VIAL_CALCULATION_intMRDWash" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intMRDRNA"><xsl:value-of select="VIAL_CALCULATION_intMRDRNA" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intPlasma"><xsl:value-of select="VIAL_CALCULATION_intPlasma" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intGDNA"><xsl:value-of select="VIAL_CALCULATION_intGDNA" /></xsl:param>
        <xsl:param name="TRAY_intTrayID"><xsl:value-of select="TRAY_intTrayID" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intLocationNeeded"><xsl:value-of select="VIAL_CALCULATION_intLocationNeeded" /></xsl:param>
        
        <xsl:param name="intAllocationID"><xsl:value-of select="intAllocationID"/></xsl:param>
        <xsl:param name="locationID"><xsl:value-of select="locationID" /></xsl:param>

        <!--- Java script used in this stylesheet -->        
        <script language="javascript" >

            function printPage() {
                window.print();
            }
        </script>
        
        <link rel="stylesheet" type="text/css" href="stylesheets/neuragenix/bio/xmlsideTree.css"/>
        <table width="100%">
            <tr valign='top'>
                <td id="neuragenix-border-right" width="15%" class="uportal-channel-subtitle">
			Menu<hr></hr>
			<br></br>
			<a href="{$baseActionURL}?current=biospecimen_search">Search Biospecimen</a>
			<br></br>
                        <xsl:if test="count(branch) &gt; 0 or count(leaf) &gt; 0">
                            <xsl:apply-templates/>
                        </xsl:if>
                         <br/><br/>
                         <xsl:if test="count(/biospecimen/patient_details) &gt; 0">
                            <table width="100%" border="0" cellspacing="0" cellpadding="2">
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/biospecimen/patient_details/PATIENT_strPatientIDDisplay" />:</b> 
                                    </td>
                                    <td width="70%"  align="right">
                                            <xsl:value-of select="/biospecimen/patient_details/PATIENT_strPatientID" />
                                    </td>
                                </tr>
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/biospecimen/patient_details/PATIENT_strHospitalURDisplay" />: </b>
                                    </td>
                                    <td width="70%" align="right">
                                            <xsl:value-of select="/biospecimen/patient_details/PATIENT_strHospitalUR" />
                                    </td>
                                </tr>
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/biospecimen/patient_details/PATIENT_strSurnameDisplay" />: </b>
                                    </td>
                                    <td width="70%"  align="right"> 
                                            <xsl:value-of select="/biospecimen/patient_details/PATIENT_strSurname" />
                                    </td>
                                </tr>
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/biospecimen/patient_details/PATIENT_strFirstNameDisplay" />: </b>
                                    </td>
                                    <td width="70%" align="right">
                                            <xsl:value-of select="/biospecimen/patient_details/PATIENT_strFirstName" />
                                    </td>
                                </tr>
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/biospecimen/patient_details/PATIENT_dtDobDisplay" />: </b>
                                    </td>
                                    <td width="70%" align="right">
                                            <xsl:value-of select="/biospecimen/patient_details/PATIENT_dtDob" />
                                    </td>
                                </tr>
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b>First referring doctor:</b>
                                    </td>
                                    <td width="70%" align="right">
                                            <xsl:value-of select="/biospecimen/patient_details/ADMISSIONS_strFirstRefDoctor" />
                                    </td>
                                </tr>
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b>Last referring doctor:</b>
                                    </td>
                                    <td width="70%" align="right">
                                            <xsl:value-of select="/biospecimen/patient_details/ADMISSIONS_strLastRefDoctor" />
                                    </td>
                                </tr>
                                
                            </table>
                         </xsl:if>
                         <br/><br/>
                        <xsl:if test='count(/biospecimen/flagged) &gt; 0'>
                        Flagged Records :
                        <table border='0' width='100%'>
                            <xsl:for-each select="/biospecimen/flagged">
                            <tr><td class='uportal-input-text'>
                            <xsl:variable name="intBiospecimenID"><xsl:value-of select="intBiospecimenID"/></xsl:variable>                
                                <a href="{$baseActionURL}?uP_root=root&amp;current=biospecimen_view&amp;intBiospecimenID={$intBiospecimenID}&amp;intInternalPatientID={$intInternalPatientID}"><xsl:value-of select="strBiospecimenID" /></a>
                              </td></tr>
                            </xsl:for-each>
                        </table>
                        </xsl:if>
                </td>
                <td width="5%" />
                <td width="80%">
	<table width="100%">
            <tr>
		<td class="uportal-channel-subtitle">
                    Step 3 - Create Sub Samples
		</td>
		    <td align="right">
		        <form name="back_form" action="{$baseActionURL}?uP_root=root" method="POST">
		            <input type="hidden" name="action" value="back"/>
		            <input type="hidden" name="module" value="vial_calculation"/>
		            <input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
		                value="{$intBiospecimenID}"/>
		        </form>
		        
		        <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
		            alt="Previous" onclick="javascript:document.back_form.submit();"/>
		        <img border="0" src="media/neuragenix/buttons/next_disabled.gif"
		            alt="Next"/>
		    </td>   
            </tr>
	    <tr><td colspan="3"><hr/></td>
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
                  
        <form action="{$baseActionURL}?current=create_sub_specimen" method="post">
            <table width="100%">
                <tr>
                    <td width="50%">
                        <table width="100%">
                            <tr>
                                <td width="40%" class="uportal-label">
                                    Parent ID:
                                </td>
                                <td width="60%" class="uportal-text">
                                    <xsl:value-of select="BIOSPECIMEN_strBiospecimenID" />
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
                                <td height="20px" colspan="2"></td>
                            </tr>
                            
                            <tr>
                                <td colspan="2" class="uportal-channel-table-header"><xsl:value-of select="VIAL_CALCULATION_intVialStored" /> Sub-specimens created and allocated at:</td>
                            </tr>
                            
                            <xsl:for-each select="search_location">
                            <tr>
                                <td width="40%" class="uportal-label">
                                    <xsl:value-of select="../SITE_strSiteNameDisplay"/>:
                                </td>
                                <td width="60%" class="uportal-text">
                                    <xsl:value-of select="SITE_strSiteName" />
                                </td>
                            </tr>
                            
                            <tr>
                                <td width="40%" class="uportal-label">
                                    <xsl:value-of select="../TANK_strTankNameDisplay"/>:
                                </td>
                                <td width="60%" class="uportal-text">
                                    <xsl:value-of select="TANK_strTankName" />
                                </td>
                            </tr>
                            
                            <tr>
                                <td width="40%" class="uportal-label">
                                    <xsl:value-of select="../BOX_strBoxNameDisplay"/>:
                                </td>
                                <td width="60%" class="uportal-text">
                                    <xsl:value-of select="BOX_strBoxName" />
                                </td>
                            </tr>
                            
                            <tr>
                                <td width="40%" class="uportal-label">
                                    <xsl:value-of select="../TRAY_strTrayNameDisplay"/>:
                                </td>
                                <td width="60%" class="uportal-text">
                                    <xsl:value-of select="TRAY_strTrayName" />
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
                                    <xsl:value-of select="strLocation" />
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
                    </td>
                </tr>
            </table>
            
            <table width="100%">
                <tr><td><hr /></td></tr>
            </table>
    
            <table width="100%">
                <tr>
                    <td>
                        <!--<input type="submit" name="back" value="{$backBtnLabel}" class="uportal-button" /> -->
                        <input type="submit" name="finish" value="Confirm this allocation" class="uportal-button" />
                        <input type="button" name="print" value="Print" class="uportal-button" onclick="javascript:printPage()" />
                    </td>
                </tr>
            </table>
            
            <!-- hidden fields to send data to server -->
           <!--
            <input type="hidden" name="intInternalPatientID" value="{$intInternalPatientID}" />
            <input type="hidden" name="BIOSPECIMEN_intBiospecimenID" value="{$intBiospecimenID}" />
            <input type="hidden" name="BIOSPECIMEN_strBiospecimenID" value="{$strBiospecimenID}" />
            <input type="hidden" name="strInitialBiospecSampleType" value="{$strInitialBiospecSampleType}" />
            <input type="hidden" name="VIAL_CALCULATION_intVialStored" value="{$VIAL_CALCULATION_intVialStored}" />
            <input type="hidden" name="VIAL_CALCULATION_intMRDWash" value="{$VIAL_CALCULATION_intMRDWash}" />
            <input type="hidden" name="VIAL_CALCULATION_intMRDRNA" value="{$VIAL_CALCULATION_intMRDRNA}" />
            <input type="hidden" name="VIAL_CALCULATION_intPlasma" value="{$VIAL_CALCULATION_intPlasma}" />
            <input type="hidden" name="VIAL_CALCULATION_intGDNA" value="{$VIAL_CALCULATION_intGDNA}" />
            <input type="hidden" name="TRAY_intTrayID" value="{$TRAY_intTrayID}" />
            <input type="hidden" name="VIAL_CALCULATION_intLocationNeeded" value="{$VIAL_CALCULATION_intLocationNeeded}" />
            -->
            <input type="hidden" name="module" value="VIAL_CALCULATION"/>
            <input type="hidden" name="action" value="confirm_allocation_option"/>
            <input type="hidden" name="subaction" value="allocate_option"/>
            <input type="hidden" name="intAllocationID" value="{$intAllocationID}"/>            
            <input type="hidden" name="locationID" value="{$locationID}"/>
            
            
            
            
        </form>
        </td>
        </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
