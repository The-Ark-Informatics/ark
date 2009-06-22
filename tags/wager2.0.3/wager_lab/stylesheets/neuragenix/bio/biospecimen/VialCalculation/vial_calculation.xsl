<?xml version="1.0" encoding="utf-8"?>
<!-- vial_calculation.xsl, part of the Biospecimen channel -->
<!-- author: Huy Hoang -->
<!-- date: 22/07/2004 -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <!--xsl:include href="./biospecimen_menu.xsl"/-->
    
    <!--<xsl:include href="../../common/common_btn_name.xsl"/>-->
    <xsl:include href="./../javascript_code.xsl"/>
   
    <xsl:output method="html" indent="yes" />
    
    <xsl:param name="inventoryChannelURL">inventoryChannelURL_false</xsl:param>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:param name="strErrorMessage">strErrorMessage</xsl:param>  
    <xsl:template match="biospecimen">
        
        <xsl:param name="VIAL_CALCULATION_dtDateOfFirstDiagnosis"><xsl:value-of select="VIAL_CALCULATION_dtDateOfFirstDiagnosis" /></xsl:param>
        
        <xsl:param name="VIAL_CALCULATION_dtDateOfCollection"><xsl:value-of select="VIAL_CALCULATION_dtDateOfCollection" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_dtDateOfProcessing"><xsl:value-of select="VIAL_CALCULATION_dtDateOfProcessing" /></xsl:param>
        
        
        <xsl:param name="VIAL_CALCULATION_dtDateOfFirstDiagnosis_Year"><xsl:value-of select="VIAL_CALCULATION_dtDateOfFirstDiagnosis_Year" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_dtDateOfCollection_Year"><xsl:value-of select="VIAL_CALCULATION_dtDateOfCollection_Year" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_dtDateOfProcessing_Year"><xsl:value-of select="VIAL_CALCULATION_dtDateOfProcessing_Year" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intClinicalStatCalc"><xsl:value-of select="VIAL_CALCULATION_intClinicalStatCalc" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flClinicalStatCalcMonths"><xsl:value-of select="VIAL_CALCULATION_flClinicalStatCalcMonths" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_strSampleType"><xsl:value-of select="VIAL_CALCULATION_strSampleType" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intManualCellCount"><xsl:value-of select="VIAL_CALCULATION_intManualCellCount" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intDilutionFactor"><xsl:value-of select="VIAL_CALCULATION_intDilutionFactor" /></xsl:param>
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
        <xsl:param name="VIAL_CALCULATION_intMRDRNA"><xsl:value-of select="VIAL_CALCULATION_intMRDRNA" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intMRDWash"><xsl:value-of select="VIAL_CALCULATION_intMRDWash" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intPlasma"><xsl:value-of select="VIAL_CALCULATION_intPlasma" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_intGDNA"><xsl:value-of select="VIAL_CALCULATION_intGDNA" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flSurvivalPeriod"><xsl:value-of select="VIAL_CALCULATION_flSurvivalPeriod" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_strClinicalStatus"><xsl:value-of select="VIAL_CALCULATION_strClinicalStatus" /></xsl:param>
        
        <xsl:param name="VIAL_CALCULATION_flSampleWeightingPerVial"><xsl:value-of select="VIAL_CALCULATION_flSampleWeightingPerVial" /></xsl:param>
        <xsl:param name="VIAL_CALCULATION_flSampleWeighting"><xsl:value-of select="VIAL_CALCULATION_flSampleWeighting" /></xsl:param>
        
        <xsl:param name="intInternalPatientID"><xsl:value-of select="intInternalPatientID" /></xsl:param>
        <xsl:param name="intBiospecimenID"><xsl:value-of select="BIOSPECIMEN_intBiospecimenID" /></xsl:param>
        <xsl:param name="strBiospecimenID"><xsl:value-of select="BIOSPECIMEN_strBiospecimenID" /></xsl:param>
        <xsl:param name="strInitialBiospecSampleType"><xsl:value-of select="BIOSPECIMEN_strInitialBiospecSampleType" /></xsl:param>
        
<!--- Java script used in this stylesheet -->        
        <script language="javascript" >

	function confirmDelete(tfBox, aURL) {
            var confirmAnswer = confirm('Are you sure you want to delete this record?');

            if(confirmAnswer == true){
		getTextValue(tfBox, aURL);
            }
	}
        
        function jumpTo(aURL){
            window.location=aURL;
        }
        
        function setValueForAnticosgulant(collection_tube){
            
            if (collection_tube == "6 mls ACD") {
                document.myForm.VIAL_CALCULATION_flmLsAnticosgulant.value = "0.8";
             }
            else if (collection_tube == "9 mls ACD") {
            
                document.myForm.VIAL_CALCULATION_flmLsAnticosgulant.value = "1.0";
            }
            else if (collection_tube == "EDTA") {
                document.myForm.VIAL_CALCULATION_flmLsAnticosgulant.value = "0.0";
            }
            else {
                document.myForm.VIAL_CALCULATION_flmLsAnticosgulant.value = "0.0";
            }
        }
        
        function setMyFormActionSubmit(action)
        {
            document.myForm.action.value=action;
            document.myForm.submit()
        }
        </script>
        
        <xsl:call-template name="javascript_code"/>
        <link rel="stylesheet" type="text/css" href="stylesheets/neuragenix/bio/xmlsideTree.css"/>
        <table width="100%">
            <tr valign='top'>
                <td id="neuragenix-border-right" width="15%" class="uportal-channel-subtitle">
			Menu<hr></hr>
			<br></br>
			<a href="{$baseActionURL}?current=biospecimen_search">Search samples</a>
			<br></br>
                        <xsl:if test="count(branch) &gt; 0 or count(leaf) &gt; 0">
                            <xsl:apply-templates/>
                        </xsl:if>
                         <br/><br/>
                        <span class="uportal-text">
                            <xsl:if test="count(biospecimenSideTree) &gt; 0">
                                <xsl:for-each select="biospecimenSideTree">
                                    <xsl:for-each select="node">
                                        <xsl:variable name="level">
                                            <xsl:value-of select="./@depth"/>
                                        </xsl:variable>
                                        <xsl:call-template name="spacer">
                                            <xsl:with-param name="spacerAmount" select="number($level)"
                                            />
                                        </xsl:call-template>
                                        <xsl:choose>
                                            <xsl:when test="./@hasChildren='true'">
                                                <img src="media/neuragenix/buttons/blue_arrow_open.gif"
                                                />
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <img src="media/neuragenix/icons/bullet.gif"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                        <a
                                            href="{$baseActionURL}?module=core&amp;action=view_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={./@internalID}">
                                            <xsl:value-of select="."/>
                                        </a>
                                        <xsl:if test="./@currentNode='true'">
                                            <img src="media/neuragenix/icons/current_node.gif"/>
                                        </xsl:if>
                                        <br/>
                                    </xsl:for-each>
                                </xsl:for-each>
                            </xsl:if>
                        </span>
                        <br/>
                        <br/>                         
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
                         <br/><br/>
                    <!-- TODO this should be in menu stylesheet and all file should call templates from it-->
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
                        <!--end of left hand side thing -->
                    
                </td>
                <td width="5%" />
                <td width="80%">
	<table width="100%">
            <tr>
		<td class="uportal-channel-subtitle">
                    Step 1 - Vial Calculations<br/>
		</td>
                <td align="right">
                    <form name="back_form" action="{$baseActionURL}?uP_root=root" method="POST">
                        <input type="hidden" name="action" value="view_biospecimen"/>
                        <input type="hidden" name="module" value="core"/>
                        <input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
                            value="{$intBiospecimenID}"/>
                        </form>
                   
                <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
                    alt="Previous" onclick="javascript:document.back_form.submit();"/>
                <img border="0" src="media/neuragenix/buttons/next_disabled.gif"
                    alt="Next"/>
                    </td>
            </tr>
	    <tr><td colspan="2"><hr/></td></tr>
	</table>
        
	<table width="100%">	 
            <tr>  	
                    <!-- Error msg -->
		<td class="neuragenix-form-required-text" width="80%">
                    <xsl:value-of select="strErrorDuplicateKey" /><xsl:value-of select="strErrorRequiredFields" /><xsl:value-of select="strErrorInvalidDataFields" /><xsl:value-of select="strErrorInvalidData" /><xsl:value-of select="strErrorChildStillExist" /><xsl:value-of select="strErrorBiospecTypeChange" />
                    <xsl:value-of select="$strErrorMessage" /><xsl:value-of select="strLockError" />
		</td>
                
		<td class="neuragenix-form-required-text" width="20%" id="neuragenix-required-header" align="right">
                    * = Required fields
		</td>
            </tr>
            <!--tr>  		
		<td class="uportal-text" width="80%">
                    First diagnosis date entry will be used. If the date is blank, no date has been specified. Please specify a date.
		</td>
		<td class="neuragenix-form-required-text" width="20%" id="neuragenix-required-header" align="right">
                    
		</td>
            </tr-->
	</table>
        
        <form name="myForm" action="{$baseActionURL}?current=vial_calculation" method="post">
            <table width="100%">
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_dtDateOfFirstDiagnosisDisplay" />:
                    </td>
                    <td width="25%">
                        <select name="VIAL_CALCULATION_dtDateOfFirstDiagnosis_Day" tabindex="1" class="uportal-input-text">
                            <option></option>
                                
                            <xsl:for-each select="VIAL_CALCULATION_dtDateOfFirstDiagnosis_Day">
                                
                                <option>
                                    <xsl:attribute name="value">
                                    <xsl:value-of select="." />
                                    </xsl:attribute> 
                                    <xsl:if test="@selected=1 and string-length($VIAL_CALCULATION_dtDateOfFirstDiagnosis) > 0">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>

                                    <xsl:value-of select="." />		
                                </option>
                            </xsl:for-each>
                        </select>

                        <select name="VIAL_CALCULATION_dtDateOfFirstDiagnosis_Month" tabindex="2" class="uportal-input-text">
                            <option></option>
                            <xsl:for-each select="VIAL_CALCULATION_dtDateOfFirstDiagnosis_Month">

                                <option>
                                    <xsl:attribute name="value">
                                    <xsl:value-of select="." />
                                    </xsl:attribute> 
                                    <xsl:if test="@selected=1 and string-length($VIAL_CALCULATION_dtDateOfFirstDiagnosis) > 0">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>

                                    <xsl:value-of select="." />		
                                </option>
                            </xsl:for-each>
                        </select>

                        <input type="text" name="VIAL_CALCULATION_dtDateOfFirstDiagnosis_Year" value="" size="4" tabindex="3" class="uportal-input-text">
                            <xsl:if test="string-length($VIAL_CALCULATION_dtDateOfFirstDiagnosis) > 0">
                                <xsl:attribute name="value"><xsl:value-of select="VIAL_CALCULATION_dtDateOfFirstDiagnosis_Year" /></xsl:attribute>
                            </xsl:if>
                        </input>
                        
                        
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%"></td>
                    <td width="25%"></td>
                </tr>
                
                <!-- row 2-->
                
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_intClinicalStatCalcDisplay" />:
                    </td>
                    <td width="25%" style="text-align: right" class="uportal-input-text">
                        <xsl:choose>
                        <xsl:when test="string-length($VIAL_CALCULATION_intClinicalStatCalc) = 0 or $VIAL_CALCULATION_intClinicalStatCalc = 0">
                            NA
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="VIAL_CALCULATION_intClinicalStatCalc" />
                        </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%"></td>
                    <td width="25%"></td>
                </tr>
                
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_flClinicalStatCalcMonthsDisplay" />:
                    </td>
                    <td width="25%" style="text-align: right" class="uportal-input-text">
                        <xsl:choose>
                        <xsl:when test="string-length($VIAL_CALCULATION_flClinicalStatCalcMonths) = 0 or $VIAL_CALCULATION_flClinicalStatCalcMonths = 0.0">
                            NA
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="VIAL_CALCULATION_flClinicalStatCalcMonths" />
                        </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%"></td>
                    <td width="25%"></td>
                </tr>
                
                <!--row 3-->
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_strSampleTypeDisplay" />:
                    </td>
                    <td width="25%">
                        <select name="VIAL_CALCULATION_strSampleType" tabindex="4" class="uportal-input-text">
                            <xsl:for-each select="VIAL_CALCULATION_strSampleType">

                                <option>
                                    <xsl:attribute name="value">
                                    <xsl:value-of select="." />
                                    </xsl:attribute> 
                                    <xsl:if test="@selected=1">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>

                                    <xsl:value-of select="." />		
                                </option>
                            </xsl:for-each>
                        </select>
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%"></td>
                    <td width="25%"></td>
                </tr>
                
                
                <!--row 4-->
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_dtDateOfCollectionDisplay" />:
                    </td>
                    <td width="25%" style="text-align: right" class="uportal-input-text">
                        <xsl:choose>
                        <xsl:when test="string-length($VIAL_CALCULATION_dtDateOfCollection) = 0">
                            NA
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="VIAL_CALCULATION_dtDateOfCollection" />
                        </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%"></td>
                    <td width="25%"></td>
                </tr>
                <!-- row 5-->
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_dtDateOfProcessingDisplay" />:
                    </td>
                    <td width="25%" style="text-align: right" class="uportal-input-text">
                        <xsl:choose>
                        <xsl:when test="string-length($VIAL_CALCULATION_dtDateOfProcessing) = 0">
                            NA
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="VIAL_CALCULATION_dtDateOfProcessing" />
                        </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%"></td>
                    <td width="25%"></td>
                </tr>
                <!-- row 6-->
                <tr>
                    <td colspan="7" height="20px"></td>
                </tr>
               
                <!-- contrentration row 7-->
                <tr>
                    <td class="neuragenix-form-required-text" width="1%">*</td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_intConcentrationDisplay" />:
                    </td>
                    <td width="25%">
                        <select name="VIAL_CALCULATION_intConcentration" tabindex="5" class="uportal-input-text">
                            <option value="5000000">
                                <xsl:if test="VIAL_CALCULATION_intConcentration = 5000000">
                                    <xsl:attribute name="selected">true</xsl:attribute>
                                </xsl:if>
                                5*10^6
                            </option>
                            <option value="10000000">
                                <xsl:if test="VIAL_CALCULATION_intConcentration = 10000000">
                                    <xsl:attribute name="selected">true</xsl:attribute>
                                </xsl:if>
                                1*10^7
                            </option>
                            <option value="20000000">
                                <xsl:if test="VIAL_CALCULATION_intConcentration = 20000000">
                                    <xsl:attribute name="selected">true</xsl:attribute>
                                </xsl:if>
                                2*10^7
                            </option>
                            <option value="40000000">
                                <xsl:if test="VIAL_CALCULATION_intConcentration = 40000000">
                                    <xsl:attribute name="selected">true</xsl:attribute>
                                </xsl:if>
                                4*10^7
                            </option>
                        </select>
                        <!--input type="text" style="text-align: right" name="VIAL_CALCULATION_intConcentration" value="{$VIAL_CALCULATION_intConcentration}" size="12" tabindex="5" class="uportal-input-text" /-->
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%"></td>
                    <td width="25%"></td>
                </tr>
                
                
                <!-- row 8 -->
                <tr>
                    <td class="neuragenix-form-required-text" width="1%">*</td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_intManualCellCountDisplay" />:
                    </td>
                    <td width="25%">
                        <xsl:choose > 
                            <xsl:when test="string($VIAL_CALCULATION_intManualCellCount)">   
                            <input type="text" style="text-align: right" name="VIAL_CALCULATION_intManualCellCount" value="{$VIAL_CALCULATION_intManualCellCount}" size="12" tabindex="6" class="uportal-input-text" />
                         </xsl:when>
                            <xsl:otherwise>   
                                <input type="text" style="text-align: right" name="VIAL_CALCULATION_intManualCellCount" value="0" size="12" tabindex="6" class="uportal-input-text" />
                            </xsl:otherwise>
                        </xsl:choose>
                        
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%"></td>
                    <td width="25%"></td>
                </tr>
                
                <!-- row 9-->
                <tr>
                    <td class="neuragenix-form-required-text" width="1%">*</td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_intDilutionFactorDisplay" />:
                    </td>
                    <td width="25%">
                        <xsl:choose>
                            <xsl:when test="string($VIAL_CALCULATION_intDilutionFactor)">
                                <input type="text" style="text-align: right" name="VIAL_CALCULATION_intDilutionFactor" value="{$VIAL_CALCULATION_intDilutionFactor}" size="12" tabindex="7" class="uportal-input-text" />
                            </xsl:when>
                            <xsl:otherwise>
                                <input type="text" style="text-align: right" name="VIAL_CALCULATION_intDilutionFactor" value="0" size="12" tabindex="7" class="uportal-input-text" />
                            </xsl:otherwise>
                            </xsl:choose>
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%"></td>
                    <td width="25%"></td>
                </tr>
                
                <!-- row 10-->
                <tr>
                    <td class="neuragenix-form-required-text" width="1%">*</td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_flmLsSuspendedDisplay" />:
                    </td>
                    <td width="25%">
                        <xsl:choose>
                            <xsl:when test="string($VIAL_CALCULATION_flmLsSuspended)">
                                    <input type="text" style="text-align: right" name="VIAL_CALCULATION_flmLsSuspended" value="{$VIAL_CALCULATION_flmLsSuspended}" size="12" tabindex="8" class="uportal-input-text" />
                            </xsl:when>
                            <xsl:otherwise>
                                <input type="text" style="text-align: right" name="VIAL_CALCULATION_flmLsSuspended" value="0" size="12" tabindex="8" class="uportal-input-text" />    
                            </xsl:otherwise>
                            </xsl:choose>
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%"></td>
                    <td width="25%"></td>
                </tr>
                
                <!-- row 11 -->
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_flCellmLDisplay" />:
                    </td>
                    <td width="25%" style="text-align: right" class="uportal-input-text">
                        <xsl:choose>
                        <xsl:when test="string-length($VIAL_CALCULATION_flCellmL) = 0">
                            NA
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="VIAL_CALCULATION_flCellmL" />
                        </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%"></td>
                    <td width="25%"></td>
                </tr>
                
                
                
                <!-- 12 -->
                             
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_flTotalCellCountDisplay" />:
                    </td>
                    <td width="20%" style="text-align: right" class="uportal-input-text">
                        <xsl:choose>
                        <xsl:when test="string-length($VIAL_CALCULATION_flTotalCellCount) = 0">
                            NA
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="VIAL_CALCULATION_flTotalCellCount" />
                        </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td width="8%"></td>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_strCollectionTubeDisplay" />:
                    </td>
                    <td width="25%">
                        <select name="VIAL_CALCULATION_strCollectionTube" tabindex="16" class="uportal-input-text" onchange="javascript:setValueForAnticosgulant(document.myForm.VIAL_CALCULATION_strCollectionTube.options[document.myForm.VIAL_CALCULATION_strCollectionTube.selectedIndex].text)">
                            <xsl:for-each select="VIAL_CALCULATION_strCollectionTube">

                                <option>
                                    <xsl:attribute name="value">
                                    <xsl:value-of select="." />
                                    </xsl:attribute> 
                                    <xsl:if test="@selected=1">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>

                                    <xsl:value-of select="." />		
                                </option>
                            </xsl:for-each>
                        </select>
                    </td>
                </tr>
                
                
                
                <!-- row 13-->
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_flmLsResuspendedDisplay" />:
                    </td>
                    <td width="25%" style="text-align: right" class="uportal-input-text">
                        <xsl:choose>
                        <xsl:when test="string-length($VIAL_CALCULATION_flmLsResuspended) = 0">
                            NA
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="VIAL_CALCULATION_flmLsResuspended" />
                        </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td width="8%"></td>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_flmLsCollectionTubeDisplay" />:
                    </td>
                    <td width="25%">
                        <xsl:choose>
                            <xsl:when test="string($VIAL_CALCULATION_flmLsCollectionTube)">
                        <input type="text" style="text-align: right" name="VIAL_CALCULATION_flmLsCollectionTube" value="{$VIAL_CALCULATION_flmLsCollectionTube}" size="17" tabindex="17" class="uportal-input-text" />
                            </xsl:when>
                            <xsl:otherwise>
                                <input type="text" style="text-align: right" name="VIAL_CALCULATION_flmLsCollectionTube" value="0" size="17" tabindex="17" class="uportal-input-text" />
                            </xsl:otherwise>
                            </xsl:choose>
                            
                            
                    </td>
                </tr>
                
                <!-- row 14 -->
                <tr>
                    <td class="neuragenix-form-required-text" width="1%">*</td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_intVialStoredDisplay" />:
                    </td>
                    <td width="25%">
                        <xsl:choose>
                         <xsl:when test="string($VIAL_CALCULATION_intVialStored)">
                            <input type="text" style="text-align: right" name="VIAL_CALCULATION_intVialStored" value="{$VIAL_CALCULATION_intVialStored}" size="12" tabindex="9" class="uportal-input-text" />
                        </xsl:when>
                            <xsl:otherwise>
                                <input type="text" style="text-align: right" name="VIAL_CALCULATION_intVialStored" value="0" size="12" tabindex="9" class="uportal-input-text" />
                                </xsl:otherwise>
                            </xsl:choose>
                    </td>
                    <td width="8%"></td>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_flmLsAnticosgulantDisplay" />:
                    </td>
                    <td width="25%">

                        <xsl:if test="string-length($VIAL_CALCULATION_flmLsAnticosgulant) = 0">
                        <input type="text"  style="text-align: right" name="VIAL_CALCULATION_flmLsAnticosgulant" value="0.8" size="12" tabindex="18" class="uportal-input-text" />
                        </xsl:if>
                        <xsl:if test="string-length($VIAL_CALCULATION_flmLsAnticosgulant) > 0"> 
                            <input type="text" style="text-align: right" name="VIAL_CALCULATION_flmLsAnticosgulant" value="{$VIAL_CALCULATION_flmLsAnticosgulant}" size="12" tabindex="18" class="uportal-input-text" />
                        </xsl:if> 
                    </td>
                </tr>
                
                
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <!--td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_intMRDWashDisplay" />:
                    </td>
                    <td width="25%">
                        <select name="VIAL_CALCULATION_intMRDWash" tabindex="10" class="uportal-input-text">
                            <xsl:for-each select="VIAL_CALCULATION_intMRDWash">

                                <option>
                                    <xsl:attribute name="value">
                                    <xsl:value-of select="." />
                                    </xsl:attribute> 
                                    <xsl:if test="@selected=1">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>

                                    <xsl:value-of select="." />		
                                </option>
                            </xsl:for-each>
                        </select>
                    </td-->
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_intMRDWashDisplay" />:
                    </td>
                    <td width="25%">
                        <input type="text" style="text-align: right" name="VIAL_CALCULATION_intMRDWash" size="12" tabindex="10" class="uportal-input-text">
                            <xsl:choose>
                            <xsl:when test="string-length($VIAL_CALCULATION_intMRDWash) = 0">
                                <xsl:attribute name="value">0</xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:attribute name="value">
                                    <xsl:value-of select="VIAL_CALCULATION_intMRDWash" />
                                </xsl:attribute>
                            </xsl:otherwise>
                            </xsl:choose>
                        </input>
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_flTotalmLsBMDisplay" />:
                    </td>
                    <td width="25%" style="text-align: right" class="uportal-input-text">
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
                
                
                <!--row 16 -->
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <!--td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_intMRDRNADisplay" />:
                    </td>
                    <td width="25%">
                        <select name="VIAL_CALCULATION_intMRDRNA" tabindex="9" class="uportal-input-text">
                            <xsl:for-each select="VIAL_CALCULATION_intMRDRNA">

                                <option>
                                    <xsl:attribute name="value">
                                    <xsl:value-of select="." />
                                    </xsl:attribute> 
                                    <xsl:if test="@selected=1">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>

                                    <xsl:value-of select="." />		
                                </option>
                            </xsl:for-each>
                        </select>
                    </td-->
                    
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_intMRDRNADisplay" />:
                    </td>
                    <td width="25%">
                        <input type="text" style="text-align: right" name="VIAL_CALCULATION_intMRDRNA" size="12" tabindex="11" class="uportal-input-text">
                            <xsl:choose>
                            <xsl:when test="string-length($VIAL_CALCULATION_intMRDRNA) = 0">
                                <xsl:attribute name="value">0</xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:attribute name="value">
                                    <xsl:value-of select="VIAL_CALCULATION_intMRDRNA" />
                                </xsl:attribute>
                            </xsl:otherwise>
                            </xsl:choose>
                        </input>
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_flSampleWeightingDisplay" />:
                    </td>
                    <td width="25%">
                        <input type="text" style="text-align: right" name="VIAL_CALCULATION_flSampleWeighting" value="{$VIAL_CALCULATION_flSampleWeighting}" size="12" tabindex="9" class="uportal-input-text" />
                    </td>
                </tr>
                
                  <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_intPlasmaDisplay" />:
                    </td>
                    <td width="25%">
                        <input type="text" style="text-align: right" name="VIAL_CALCULATION_intPlasma" size="12" tabindex="12" class="uportal-input-text">
                            <xsl:choose>
                            <xsl:when test="string-length($VIAL_CALCULATION_intPlasma) = 0">
                                <xsl:attribute name="value">0</xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:attribute name="value">
                                    <xsl:value-of select="VIAL_CALCULATION_intPlasma" />
                                </xsl:attribute>
                            </xsl:otherwise>
                            </xsl:choose>
                        </input>
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_flSampleWeightingPerVialDisplay" />:
                    </td>
                    <td width="25%" style="text-align: right" class="uportal-input-text">
                        <xsl:choose>
                            <xsl:when test="string-length($VIAL_CALCULATION_flSampleWeightingPerVial) = 0">
                            NA
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="VIAL_CALCULATION_flSampleWeightingPerVial" />
                        </xsl:otherwise>
                        </xsl:choose>
                    </td>                    
                </tr>
                
              
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_intGDNADisplay" />:
                    </td>
                    <td width="25%">
                        <input type="text" style="text-align: right" name="VIAL_CALCULATION_intGDNA" size="12" tabindex="13" class="uportal-input-text">
                            <xsl:choose>
                            <xsl:when test="string-length($VIAL_CALCULATION_intGDNA) = 0">
                                <xsl:attribute name="value">0</xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:attribute name="value">
                                    <xsl:value-of select="VIAL_CALCULATION_intGDNA" />
                                </xsl:attribute>
                            </xsl:otherwise>
                            </xsl:choose>
                        </input>
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_intSecondLookDisplay" />:
                    </td>
                    <td width="25%">
                        <input type="text" style="text-align: right" name="VIAL_CALCULATION_intSecondLook" value="{$VIAL_CALCULATION_intSecondLook}" size="12" tabindex="19" class="uportal-input-text" />
                    </td>
                </tr>
              
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_strClinicalStatusDisplay" />:
                    </td>
                    <td width="25%">
                        <input type="text" style="text-align: right" name="VIAL_CALCULATION_strClinicalStatus" size="12" tabindex="14" class="uportal-input-text">
                            <xsl:choose>
                            <xsl:when test="string-length($VIAL_CALCULATION_strClinicalStatus) = 0">
                                <xsl:attribute name="value"> </xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:attribute name="value">
                                    <xsl:value-of select="VIAL_CALCULATION_strClinicalStatus" />
                                </xsl:attribute>
                            </xsl:otherwise>
                            </xsl:choose>
                        </input>
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%"></td>
                    <td width="25%"></td>
                </tr>
                
                <tr>
                    <td class="neuragenix-form-required-text" width="1%"></td>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="VIAL_CALCULATION_flSurvivalPeriodDisplay" />:
                    </td>
                    <td width="25%">
                        <input type="text" style="text-align: right" name="VIAL_CALCULATION_flSurvivalPeriod" size="12" tabindex="15" class="uportal-input-text">
                            <xsl:choose>
                            <xsl:when test="string-length($VIAL_CALCULATION_flSurvivalPeriod) = 0">
                                <xsl:attribute name="value">0</xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:attribute name="value">
                                    <xsl:value-of select="VIAL_CALCULATION_flSurvivalPeriod" />
                                </xsl:attribute>
                            </xsl:otherwise>
                            </xsl:choose>
                        </input>
                    </td>
                    <td width="8%"></td>
                    <td width="1%"></td>
                    <td width="20%"></td>
                    <td width="25%"></td>
                </tr>        
              
            </table>
            
            <table width="100%">
                <tr><td><hr /></td></tr>
            </table>
    
            
            
            <!-- hidden fields to send data to server -->
            <input type="hidden" name="intInternalPatientID" value="{$intInternalPatientID}" />
            <input type="hidden" name="BIOSPECIMEN_intBiospecimenID" value="{$intBiospecimenID}" />
            <input type="hidden" name="BIOSPECIMEN_strBiospecimenID" value="{$strBiospecimenID}" />
            <input type="hidden" name="BIOSPECIMEN_dtBiospecSampleDate" value="{$VIAL_CALCULATION_dtDateOfCollection}" />
            <input type="hidden" name="BIOSPECIMEN_dtBiospecDateExtracted" value="{$VIAL_CALCULATION_dtDateOfProcessing}" />  
            <input type="hidden" name="BIOSPECIMEN_strInitialBiospecSampleType" value="{$strInitialBiospecSampleType}" />
            
            <input type="hidden" name="VIAL_CALCULATION_dtDateOfFirstDiagnosis" value="{$VIAL_CALCULATION_dtDateOfFirstDiagnosis}" />
            <input type="hidden" name="VIAL_CALCULATION_dtDateOfCollection" value="{$VIAL_CALCULATION_dtDateOfCollection}" />
            <input type="hidden" name="VIAL_CALCULATION_dtDateOfProcessing" value="{$VIAL_CALCULATION_dtDateOfProcessing}" />
            
            <input type="hidden" name="VIAL_CALCULATION_intClinicalStatCalc" value="{$VIAL_CALCULATION_intClinicalStatCalc}" />
            <input type="hidden" name="VIAL_CALCULATION_flClinicalStatCalcMonths" value="{$VIAL_CALCULATION_flClinicalStatCalcMonths}" />
            <input type="hidden" name="VIAL_CALCULATION_flCellmL" value="{$VIAL_CALCULATION_flCellmL}" />
            <input type="hidden" name="VIAL_CALCULATION_flTotalCellCount" value="{$VIAL_CALCULATION_flTotalCellCount}" />
            <input type="hidden" name="VIAL_CALCULATION_flmLsResuspended" value="{$VIAL_CALCULATION_flmLsResuspended}" />
            <input type="hidden" name="VIAL_CALCULATION_flTotalmLsBM" value="{$VIAL_CALCULATION_flTotalmLsBM}" />
            <input type="hidden" name="VIAL_CALCULATION_intMRDWash" value="{$VIAL_CALCULATION_intMRDWash}" />
            <input type="hidden" name="VIAL_CALCULATION_intMRDRNA" value="{$VIAL_CALCULATION_intMRDRNA}" />
            <input type="hidden" name="VIAL_CALCULATION_intPlasma" value="{$VIAL_CALCULATION_intPlasma}" />
            <input type="hidden" name="VIAL_CALCULATION_intGDNA" value="{$VIAL_CALCULATION_intGDNA}" />             
            
            <input type="hidden" name="VIAL_CALCULATION_intClinicalStatCalc" value="{$VIAL_CALCULATION_intClinicalStatCalc}" />
            <input type="hidden" name="VIAL_CALCULATION_flClinicalStatCalcMonths" value="{$VIAL_CALCULATION_flClinicalStatCalcMonths}" />
            <input type="hidden" name="VIAL_CALCULATION_intManualCellCount" value="{$VIAL_CALCULATION_intManualCellCount}" />
            <input type="hidden" name="VIAL_CALCULATION_intDilutionFactor" value="{$VIAL_CALCULATION_intDilutionFactor}" />
            <input type="hidden" name="VIAL_CALCULATION_flmLsSuspended" value="{$VIAL_CALCULATION_flmLsSuspended}" />
            <input type="hidden" name="VIAL_CALCULATION_flCellmL" value="{$VIAL_CALCULATION_flCellmL}" />
            <input type="hidden" name="VIAL_CALCULATION_flTotalCellCount" value="{$VIAL_CALCULATION_flTotalCellCount}" />
            <input type="hidden" name="VIAL_CALCULATION_flSampleWeightingPerVial" value="{$VIAL_CALCULATION_flSampleWeightingPerVial}" />
            <input type="hidden" name="VIAL_CALCULATION_flmLsCollectionTube" value="{$VIAL_CALCULATION_flmLsCollectionTube}" />
            
            <input type="hidden" name="VIAL_CALCULATION_intConcentration" value="{$VIAL_CALCULATION_intConcentration}" />
            <input type="hidden" name="VIAL_CALCULATION_flTotalmLsBM" value="{$VIAL_CALCULATION_flTotalmLsBM}" />
            <input type="hidden" name="VIAL_CALCULATION_intSecondLook" value="{$VIAL_CALCULATION_intSecondLook}" />
            <input type="hidden" name="module" value="VIAL_CALCULATION"/>
            <input type="hidden" name="action" value="generate_sub_biospecimen"/>
            
            <table width="100%">
                <tr>
                    <td>
                        <input type="button" name="perform_calc" tabindex="26" value="Perform calculation" class="uportal-button" 
                                onclick="javascript:setMyFormActionSubmit('perform_calculation');"/>
                        
                        <xsl:if test="($strErrorMessage='Calculation performed') or ($strErrorMessage='Calculations saved')">
                            
                            <input type="button" name="confirm_vial_calculation" tabindex="27" value="Confirm Vial Calculation" class="uportal-button"
                                onclick="javascript:setMyFormActionSubmit('confirm_vial_calculation');"/>
                        </xsl:if>
                        <xsl:if test="($strErrorMessage='Calculation performed') or ($strErrorMessage='Calculations saved')">
                            
                        <input type="button" name="save_vial_calculation" tabindex="28" value="Save Vial Calculation" class="uportal-button"
                                  onclick="javascript:setMyFormActionSubmit('save_vial_calculation');"/>
                        </xsl:if>
                    </td>
                </tr>
            </table>
            
        </form>
        </td>
        </tr>
        </table>
    </xsl:template>
    <xsl:template name="spacer">
        <xsl:param name="spacerAmount">0</xsl:param>
        <xsl:if test="$spacerAmount&gt;0">
            <img src="media/neuragenix/icons/dtree/empty.gif" width="16" height="16"/>
            <xsl:call-template name="spacer">
                <xsl:with-param name="spacerAmount" select="number($spacerAmount)-1"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
 </xsl:stylesheet>
