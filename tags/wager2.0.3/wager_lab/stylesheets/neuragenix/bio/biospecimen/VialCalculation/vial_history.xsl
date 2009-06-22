<?xml version="1.0" encoding="utf-8"?>
<!-- vial_history.xsl, part of the Biospecimen channel -->
<!-- author: Huy Hoang -->
<!-- date: 29/07/2004 -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <!-- <xsl:include href="./javascript_code.xsl"/> -->
    <!-- <xsl:include href="../../common/common_btn_name.xsl"/> -->
    
    <xsl:output method="html" indent="yes" />
    
    <xsl:param name="inventoryChannelURL">inventoryChannelURL_false</xsl:param>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
        
    <xsl:template match="biospecimen">
        <xsl:param name="VIAL_CALCULATION_intClinicalStatCalc"><xsl:value-of select="VIAL_CALCULATION_intClinicalStatCalc" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flClinicalStatCalcMonths"><xsl:value-of select="VIAL_CALCULATION_flClinicalStatCalcMonths" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_strSampleType"><xsl:value-of select="VIAL_CALCULATION_strSampleType" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intManualCellCount"><xsl:value-of select="VIAL_CALCULATION_intManualCellCount" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flDilutionFactor"><xsl:value-of select="VIAL_CALCULATION_flDilutionFactor" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flmLsSuspended"><xsl:value-of select="VIAL_CALCULATION_flmLsSuspended" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flCellmL"><xsl:value-of select="VIAL_CALCULATION_flCellmL" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flTotalCellCount"><xsl:value-of select="VIAL_CALCULATION_flTotalCellCount" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flmLsResuspended"><xsl:value-of select="VIAL_CALCULATION_flmLsResuspended" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intVialStored"><xsl:value-of select="VIAL_CALCULATION_intVialStored" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flmLsCollectionTube"><xsl:value-of select="VIAL_CALCULATION_flmLsCollectionTube" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flmLsAnticosgulant"><xsl:value-of select="VIAL_CALCULATION_flmLsAnticosgulant" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intConcentration"><xsl:value-of select="VIAL_CALCULATION_intConcentration" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flTotalmLsBM"><xsl:value-of select="VIAL_CALCULATION_flTotalmLsBM" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intSecondLook"><xsl:value-of select="VIAL_CALCULATION_intSecondLook" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flSurvivalPeriod"><xsl:value-of select="VIAL_CALCULATION_flSurvivalPeriod" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_strClinicalStatus"><xsl:value-of select="VIAL_CALCULATION_strClinicalStatus" /></xsl:param>
        
        <xsl:param name="VIAL_CALCULATION_flSampleWeightingPerVial"><xsl:value-of select="VIAL_CALCULATION_flSampleWeightingPerVial" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flSampleWeighting"><xsl:value-of select="VIAL_CALCULATION_flSampleWeighting" /></xsl:param>
        
        <xsl:param name="intInternalPatientID"><xsl:value-of select="intInternalPatientID" /></xsl:param>
        <xsl:param name="intBiospecimenID"><xsl:value-of select="intBiospecimenID" /></xsl:param>
        <xsl:param name="strBiospecimenID"><xsl:value-of select="strBiospecimenID" /></xsl:param>
        <xsl:param name="strInitialBiospecSampleType"><xsl:value-of select="strInitialBiospecSampleType" /></xsl:param>
        

        <link rel="stylesheet" type="text/css" href="stylesheets/neuragenix/bio/xmlsideTree.css"/>
        <table width="100%">
            <tr valign='top'>
                <td id="neuragenix-border-right" width="15%" class="uportal-channel-subtitle">
			Menu<hr></hr>
			<br></br>
			<a href="{$baseActionURL}?current=biospecimen_search">Search biospecimen</a>
			<br></br>
                        <xsl:if test="count(branch) &gt; 0 or count(leaf) &gt; 0">
                            <xsl:apply-templates/>
                        </xsl:if>
                         <br/><br/>
                    
                        <!-- patient details -->
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
                                <!--
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/biospecimen/patient_details/PATIENT_strHospitalURDisplay" />: </b>
                                    </td>
                                    <td width="70%" align="right">
                                            <xsl:value-of select="/biospecimen/patient_details/PATIENT_strHospitalUR" />
                                    </td>
                                </tr>
                                -->
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
                                <!--
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
                                -->
                            </table>
                         </xsl:if>
                        <!-- end of patient details-->
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
                    Vial History<br/>
		</td>
                    <td align="right">
                    
                                       
                        <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
                            alt="Previous" onclick="javascript:document.history_form.submit();"/>
                        <img border="0" src="media/neuragenix/buttons/next_disabled.gif"
                            alt="Next"/>
                </td>
            </tr>
	    <tr><td colspan="2"><hr/></td>
	        </tr>
	</table>
        
        <form name="history_form" action="{$baseActionURL}?current=vial_history" method="post">
            <table width="100%">	 
                <tr>  		
                    <td width="20%" class="uportal-label">
                        User:
                    </td>
                    <td width="80%" class="uportal-text">
                        <xsl:value-of select="VIAL_CALCULATION_strUserName" />
                    </td>
                </tr>
                <tr>  		
                    <td width="20%" class="uportal-label">
                        Date:
                    </td>
                    <td width="80%" class="uportal-text">
                        <xsl:value-of select="VIAL_CALCULATION_dtDateOfCalculation" />
                    </td>
                </tr>
            </table>
            
            <table width="100%">
                <tr>
                    <td width="48%" valign="top">
        
                        <table width="100%">
                            <tr>
                                <td width="100%" class="uportal-table-header">
                                    Settings
                                </td>
                            </tr>
                        </table>
                        
                        <table width="100%">
                            <tr>
                                
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_intClinicalStatCalcDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:choose>
                                    <xsl:when test="string-length($VIAL_CALCULATION_intClinicalStatCalc) = 0 or $VIAL_CALCULATION_intClinicalStatCalc = 0">
                                        NA
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="VIAL_CALCULATION_intClinicalStatCalc" />
                                    </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>

                            <tr>
                               
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_flClinicalStatCalcMonthsDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:choose>
                                    <xsl:when test="string-length($VIAL_CALCULATION_flClinicalStatCalcMonths) = 0 or $VIAL_CALCULATION_flClinicalStatCalcMonths = 0.0">
                                        NA
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="VIAL_CALCULATION_flClinicalStatCalcMonths" />
                                    </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>
                            
                            <tr><td colspan="2" height="20px"></td></tr>
                            
                            <tr>
                                
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_intConcentrationDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:value-of select="VIAL_CALCULATION_intConcentration" />
                                </td>
                            </tr>

                            <tr>
                                
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_intManualCellCountDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:value-of select="VIAL_CALCULATION_intManualCellCount" />
                                </td>
                            </tr>

                            <tr>
                                
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_intDilutionFactorDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:value-of select="VIAL_CALCULATION_intDilutionFactor" />
                                </td>
                            </tr>

                            <tr>
                                
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_flmLsSuspendedDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:value-of select="VIAL_CALCULATION_flmLsSuspended" />
                                </td>
                            </tr>

                            <tr>
                                
                                <td width="69%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_flCellmLDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:choose>
                                    <xsl:when test="string-length($VIAL_CALCULATION_flCellmL) = 0">
                                        NA
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="VIAL_CALCULATION_flCellmL" />
                                    </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>

                            <tr>
                                
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_flTotalCellCountDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:choose>
                                    <xsl:when test="string-length($VIAL_CALCULATION_flTotalCellCount) = 0">
                                        NA
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="VIAL_CALCULATION_flTotalCellCount" />
                                    </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>

                            <tr>
                                
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_flmLsResuspendedDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:choose>
                                    <xsl:when test="string-length($VIAL_CALCULATION_flmLsResuspended) = 0">
                                        NA
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="VIAL_CALCULATION_flmLsResuspended" />
                                    </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>
                            
                            <tr>
                     <tr>           
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_intVialStoredDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:choose>
                                    <xsl:when test="string-length($VIAL_CALCULATION_intVialStored) = 0">
                                        NA
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="VIAL_CALCULATION_intVialStored" />
                                    </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>

                            <td width="60%" class="uportal-label">
                                <xsl:value-of select="VIAL_CALCULATION_flSampleWeightingPerVialDisplay" />:
                            </td>
                            <td width="40%" style="text-align: right" class="uportal-input-text">
                                <xsl:value-of select="VIAL_CALCULATION_flSampleWeightingPerVial" />
                            </td>
                </tr>
                
                <tr>
                    
                    <td width="60%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_flSampleWeightingDisplay" />:
                    </td>
                    <td width="40%" style="text-align: right" class="uportal-input-text">
                        <xsl:value-of select="VIAL_CALCULATION_flSampleWeighting" />
                    </td>
                </tr>
                            
                            
                            
                            
                            
                            
                            
                            <tr>
                                
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_intMRDWashDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:value-of select="VIAL_CALCULATION_intMRDWash" />
                                </td>
                            </tr>

                            <tr>
                                
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_intMRDRNADisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:value-of select="VIAL_CALCULATION_intMRDRNA" />
                                </td>
                            </tr>
                            
                            
                            
                            
                        <tr>
                            <td width="60%" class="uportal-label">
                                <xsl:value-of select="VIAL_CALCULATION_intPlasmaDisplay" />:
                            </td>
                            <td width="40%" style="text-align: right" class="uportal-input-text">
                                <xsl:value-of select="VIAL_CALCULATION_intPlasma" />
                            </td>
                        </tr>
                
                <tr>
                    
                    
                    
                    
                    <td width="60%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_intGDNADisplay" />:
                    </td>
                    <td width="40%" style="text-align: right" class="uportal-input-text">
                        <xsl:value-of select="VIAL_CALCULATION_intGDNA" />
                    </td>
                </tr>
                            
                            
                            
                            
                            
                            <tr><td colspan="2" height="20px"></td></tr>
                            
                            <tr>
                                
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_strCollectionTubeDisplay" />:
                                </td>
                                <td width="40%" class="uportal-input-text">
                                    <!-- xsl:value-of select="VIAL_CALCULATION_strCollectionTube" / -->
                                    <xsl:for-each select="VIAL_CALCULATION_strCollectionTube">
                      
                                        <xsl:if test="@selected=1">
                                            <xsl:value-of select="." />        
                                        </xsl:if>
                            
                                    </xsl:for-each>  
                                                            
                                </td>
                            </tr>

                            <tr>
                                
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_flmLsCollectionTubeDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:value-of select="VIAL_CALCULATION_flmLsCollectionTube" />
                                </td>
                            </tr>

                            <tr>
                                
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_flmLsAnticosgulantDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:value-of select="VIAL_CALCULATION_flmLsAnticosgulant" />
                                </td>
                            </tr>

                            <tr>
                                
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_flTotalmLsBMDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:choose>
                                    <xsl:when test="string-length($VIAL_CALCULATION_flTotalmLsBM) = 0">
                                        NA
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="VIAL_CALCULATION_flTotalmLsBM" />
                                    </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>

                            <tr>
                               
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_intSecondLookDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:value-of select="VIAL_CALCULATION_intSecondLook" />
                                </td>
                            </tr>

                            
                            <tr>
                               
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_flSurvivalPeriodDisplay" />:
                                </td>
                                <td width="40%" style="text-align: right" class="uportal-input-text">
                                    <xsl:value-of select="VIAL_CALCULATION_flSurvivalPeriod" />
                                </td>
                            </tr>
                            <tr>
                               
                                <td width="60%" class="uportal-label">
                                    <xsl:value-of select="VIAL_CALCULATION_strClinicalStatusDisplay" />:
                                </td>
                                <td width="40%" style="text-align: left" class="uportal-input-text">
                                    <xsl:value-of select="VIAL_CALCULATION_strClinicalStatus" />
                                </td>
                            </tr>
                            
                        </table>
                    </td>
                    
                    <td width="4%"></td>
                    
                    <td width="48%" valign="top">
                        <table width="100%">
                            <tr>
                                <td width="100%" class="uportal-table-header">
                                    Inventory Details
                                </td>
                            </tr>
                        </table>
                        
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
                                <td height="20px" colspan="2"></td>
                            </tr>
                           
             
                        </table>
                        
                        <table width="100%">
                            <tr>
                                <td width="100%" class="uportal-table-header">
                                    Generated Sub Biospecimen IDs
                                </td>
                            </tr>
                            <xsl:for-each select="subIDs">
                            <tr>
                                <td class="uportal-label">
                                    <xsl:value-of select="."/>
                                </td>
                            </tr>
                            </xsl:for-each>
                        </table>           
                        
                        
                    </td>
                    </tr>
                        
                        

                        
                    
            </table>
            <table width="100%">
                <tr><td><hr /></td></tr> 
            </table>
            
            
            
            <xsl:for-each select="Allocation">
                <table width="100%">
                    <tr>
                        <td width="50%">
                            <table width="100%">
                                
                                <tr>
                                    <td height="20px" colspan="2"></td>
                                </tr>
                                
                                <tr>
                                    <td colspan="2" class="uportal-channel-table-header"><xsl:value-of select="VIAL_CALCULATION_intVialStored" /> Sub-specimens created and allocated at:</td>
                                </tr>
                                
                                
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
                                
                                
                                   <xsl:apply-templates select="biospecimens">
                                   		<xsl:sort select="strLocation"/>
                                   </xsl:apply-templates>
                                    
                                
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
                
                <tr><td><input type="submit" class="uportal-button" value="Back To Biospecimen"/> </td></tr>
            </table>
           
            <!-- hidden fields to send data to server -->
            <input type="hidden" name="intInternalPatientID" value="{$intInternalPatientID}" />
            <input type="hidden" name="BIOSPECIMEN_intBiospecimenID" value="{$intBiospecimenID}" />
            <input type="hidden" name="BIOSPECIMEN_strBiospecimenID" value="{$strBiospecimenID}" />
             <input type="hidden" name="module" value="core"/>
             <input type="hidden" name="action" value="view_biospecimen"></input>
            
        </form>
        </td>
        </tr>
        </table>
    </xsl:template>
    
    <xsl:template match="biospecimens">
    	<tr>
    		<td width="40%" class="uportal-label">
    			<xsl:value-of select="strBiospecimenID" />
    		</td>
    		<td width="60%" class="uportal-text">
    			<xsl:value-of select="strLocation" />

    		</td>
    	</tr>
    </xsl:template>
</xsl:stylesheet>
