<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./javascript_code.xsl"/>
    
    <xsl:output method="html" indent="yes"/>
    <!-- <xsl:include href="./biospecimen_common.xsl" /> -->
    <xsl:param name="inventoryChannelURL">inventoryChannelURL_false</xsl:param>
    <xsl:param name="smartformChannelURL">smartformChannelURL_false</xsl:param>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:param name="smartformChannelTabOrder">smartformChannelTabOrder</xsl:param>
    <xsl:param name="patientChannelURL">patientChannelURL_false</xsl:param>
    <xsl:param name="patientChannelTabOrder">patientChannelTabOrder</xsl:param>
    <xsl:param name="strErrorMessage">strErrorMessage</xsl:param>
    <xsl:template match="/ErrorOnly">
        <xsl:value-of select="$strErrorMessage" />
    </xsl:template>
    
    <xsl:template match="/biospecimen">
		 <xsl:param name="BIOSPECIMEN_age">
            <xsl:value-of select="BIOSPECIMEN_age"/>
        </xsl:param>
        <xsl:param name="viewnotes">
            <xsl:value-of select="viewnotes"/>
        </xsl:param>
		
        <xsl:param name="showInventory">
            <xsl:value-of select="showInventory"/>
        </xsl:param>
        <xsl:param name="showTransactions">
            <xsl:value-of select="showTransactions"/>
        </xsl:param>
        <!-- Get the parameters from the channel class -->
        <xsl:param name="BIOSPECIMEN_Timestamp">
            <xsl:value-of select="BIOSPECIMEN_Timestamp"/>
        </xsl:param>
        <xsl:param name="intBiospecimenID">
            <xsl:value-of select="BIOSPECIMEN_intBiospecimenID"/>
        </xsl:param>
        <xsl:param name="strBiospecimenID">
            <xsl:value-of select="BIOSPECIMEN_strBiospecimenID"/>
        </xsl:param>
        <xsl:param name="dtBiospecSampleDate">
            <xsl:value-of select="BIOSPECIMEN_dtBiospecSampleDate"/>
        </xsl:param>
        <xsl:param name="strBiospecOtherID">
            <xsl:value-of select="BIOSPECIMEN_strOtherID"/>
        </xsl:param>
        <xsl:param name="intInternalPatientID">
            <xsl:value-of select="BIOSPECIMEN_intPatientID"/>
        </xsl:param>
        <xsl:param name="intBiospecParentID">
            <xsl:value-of select="BIOSPECIMEN_intParentID"/>
        </xsl:param>
        <xsl:param name="strBiospecParentID">
            <xsl:value-of select="BIOSPECIMEN_strParentID"/>
        </xsl:param>
        <xsl:param name="strBiospecCode">
            <xsl:value-of select="BIOSPECIMEN_strBiospecCode"/>
        </xsl:param>
        <xsl:param name="strBiospecDescription">
            <xsl:value-of select="BIOSPECIMEN_strBiospecDescription"/>
        </xsl:param>
        <xsl:param name="intDepth">
            <xsl:value-of select="BIOSPECIMEN_intDepth"/>
        </xsl:param>
        <xsl:param name="strBiospecGestAt">
            <xsl:value-of select="BIOSPECIMEN_strBiospecGestAt"/>
        </xsl:param>
        <xsl:param name="intBiospecNumCollected">
            <xsl:value-of select="BIOSPECIMEN_intBiospecNumCollected"/>
        </xsl:param>
        <xsl:param name="intBiospecNumRemoved">
            <xsl:value-of select="BIOSPECIMEN_intBiospecNumRemoved"/>
        </xsl:param>
        <xsl:param name="intBiospecVolRemoved">
            <xsl:value-of select="BIOSPECIMEN_intBiospecVolRemoved"/>
        </xsl:param>
        <xsl:param name="intBiospecVolume">
            <xsl:value-of select="BIOSPECIMEN_intBiospecVolume"/>
        </xsl:param>
        <xsl:param name="strBiospecSampleTypeSelected">
            <xsl:value-of select="BIOSPECIMEN_strBiospecSampleTypeSelected"/>
        </xsl:param>
        <xsl:param name="flag">
            <xsl:value-of select="flag"/>
        </xsl:param>
        <!--<xsl:param name="strEncounter">
            <xsl:value-of select="BIOSPECIMEN_strEncounter"/>
        </xsl:param>-->
        <xsl:param name="intInvCellID">
            <xsl:value-of select="/biospecimen/inventory_info/CELL_intCellID"/>
        </xsl:param>
        <xsl:param name="strSource">biospecimen</xsl:param>
        <!-- XXX: agus - unlock sub biospecimen type -->
        <xsl:param name="strInitialBiospecSampleType">
            <xsl:value-of select="//BIOSPECIMEN_strSampleType[@selected='1']"/>
        </xsl:param>
        <!-- XXX: agus end -->
        <xsl:param name="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Year">
            <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Year"/>
        </xsl:param>
        <xsl:param name="strBiospecGrade">
            <xsl:value-of select="strBiospecGrade"/>
        </xsl:param>
        <xsl:param name="dtBiospecDateExtracted">
            <xsl:value-of select="BIOSPECIMEN_dtExtractedDate"/>
        </xsl:param>
        <!--
  <xsl:param name="dtBiospecDateDistributed"><xsl:value-of select="dtBiospecDateDistributed" /></xsl:param>
  -->
        <xsl:param name="strBiospecSpecies">
            <xsl:value-of select="strBiospecSpecies"/>
        </xsl:param>
        <xsl:param name="intBiospecStudyID">
            <xsl:value-of select="BIOSPECIMEN_intStudyKey"/>
        </xsl:param>
        
        <!-- Vial calculation -->
        <xsl:param name="VIAL_CALCULATION_intVialCalculationKey">
            <xsl:value-of select="VIAL_CALCULATION_intVialCalculationKey"/>
        </xsl:param>
        <xsl:param name="VIAL_DETAILS_intClinicalStatCalc">
            <xsl:value-of select="VIAL_DETAILS_intClinicalStatCalc"/>
        </xsl:param>
        <xsl:param name="VIAL_DETAILS_flClinicalStatCalcMonths">
            <xsl:value-of select="VIAL_DETAILS_flClinicalStatCalcMonths"/>
        </xsl:param>
        <xsl:param name="VIAL_DETAILS_intManualCellCount">
            <xsl:value-of select="VIAL_DETAILS_intManualCellCount"/>
        </xsl:param>
        <xsl:param name="VIAL_DETAILS_intDilutionFactor">
            <xsl:value-of select="VIAL_DETAILS_intDilutionFactor"/>
        </xsl:param>
        <xsl:param name="VIAL_DETAILS_intmLsSuspended">
            <xsl:value-of select="VIAL_DETAILS_intmLsSuspended"/>
        </xsl:param>
        <xsl:param name="VIAL_DETAILS_intCellmL">
            <xsl:value-of select="VIAL_DETAILS_intCellmL"/>
        </xsl:param>
        <xsl:param name="VIAL_DETAILS_intTotalCellCount">
            <xsl:value-of select="VIAL_DETAILS_intTotalCellCount"/>
        </xsl:param>
        <xsl:param name="VIAL_DETAILS_intmLsResuspended">
            <xsl:value-of select="VIAL_DETAILS_intmLsResuspended"/>
        </xsl:param>
        <xsl:param name="VIAL_DETAILS_flmLsCollectionTube">
            <xsl:value-of select="VIAL_DETAILS_flmLsCollectionTube"/>
        </xsl:param>
        <xsl:param name="VIAL_DETAILS_flmLsAnticosgulant">
            <xsl:value-of select="VIAL_DETAILS_flmLsAnticosgulant"/>
        </xsl:param>
        <xsl:param name="VIAL_DETAILS_intConcentration">
            <xsl:value-of select="VIAL_DETAILS_intConcentration"/>
        </xsl:param>
        <xsl:param name="VIAL_DETAILS_flTotalmLsBM">
            <xsl:value-of select="VIAL_DETAILS_flTotalmLsBM"/>
        </xsl:param>
        <xsl:param name="VIAL_DETAILS_intSecondLook">
            <xsl:value-of select="VIAL_DETAILS_intSecondLook"/>
        </xsl:param>
        <xsl:param name="strStoredIn">
            <xsl:value-of select="BIOSPECIMEN_strStoredIn"/>
        </xsl:param>
        <xsl:param name="BIOSPECIMEN_tmBiospecSampleTime">
            <xsl:value-of select="BIOSPECIMEN_tmSampleTime"/>
        </xsl:param>
        <xsl:param name="tmBiospecSampleTime">
            <xsl:value-of select="BIOSPECIMEN_tmSampleTime"/>
        </xsl:param>
        <xsl:param name="tmBiospecExtractedTime">
            <xsl:value-of select="BIOSPECIMEN_tmExtractedTime"/>
        </xsl:param>
        <xsl:variable name="BIOSPECIMEN_dtSampleDate" select="BIOSPECIMEN_dtSampleDate"/>
        <xsl:variable name="BIOSPECIMEN_dtExtractedDate" select="BIOSPECIMEN_dtExtractedDate"/>
        <xsl:call-template name="javascript_code"/>
        <script language="javascript"> function dropDownSubTypeUpdate() {
            document.biospecimen_form.action.value =
            'refresh_view_biospecimen'; 
            document.biospecimen_form.keepSubType.value = 'true';
            document.biospecimen_form.submit();
            
            } 
            function updateColor (selectTag) 
            {
                selectTag.style.color = selectTag[selectTag.selectedIndex].style.color;
            }            
            </script>   
        <link rel="stylesheet" type="text/css" href="stylesheets/neuragenix/bio/xmlsideTree.css"/>
        <table width="100%">
            <tr valign="top">
                <td id="neuragenix-border-right" width="15%" class="uportal-channel-subtitle"> Menu<hr/>
                    <br/>
                    <a href="{$baseActionURL}?module=NEW_SEARCH">Search biospecimens</a>
                    <br/>
                    <xsl:if test="biospecimen_modules/quantityAllocations='true'">
                        <a
                            href="{$baseActionURL}?uP_root=root&amp;current=bp_allocate&amp;stage=BEGIN&amp;module=BATCH_ALLOCATE"
                            >Quantity allocation</a>
                        <br/>    
                    </xsl:if>
                    <a href="{$baseActionURL}?module=BATCH_SFRESULTS_GENERATION&amp;action=creation_type&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}">Batch Analysis Results</a>
                    <br/>
                    <br/>
                    <br/>
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
                                <td width="30%">
                                    <b>
                                        <xsl:value-of
                                            select="/biospecimen/patient_details/PATIENT_strPatientIDDisplay"
                                        />:</b>
                                </td>
                                <td width="70%" align="right">
                                    <xsl:value-of
                                        select="/biospecimen/patient_details/PATIENT_strPatientID"/>
                                </td>
                            </tr>
                            <tr class="uportal-input-text">
                                <td width="30%">
                                    <b>
                                        <xsl:value-of
                                            select="/biospecimen/patient_details/PATIENT_strSurnameDisplay"
                                        />: </b>
                                </td>
                                <td width="70%" align="right">
                                    <xsl:value-of
                                        select="/biospecimen/patient_details/PATIENT_strSurname"/>
                                </td>
                            </tr>
                            <tr class="uportal-input-text">
                                <td width="30%">
                                    <b>
                                        <xsl:value-of
                                            select="/biospecimen/patient_details/PATIENT_strFirstNameDisplay"
                                        />: </b>
                                </td>
                                <td width="70%" align="right">
                                    <xsl:value-of
                                        select="/biospecimen/patient_details/PATIENT_strFirstName"/>
                                </td>
                            </tr>
                            <tr class="uportal-input-text">
                                <td width="30%">
                                    <b>
                                        <xsl:value-of
                                            select="/biospecimen/patient_details/PATIENT_dtDobDisplay"
                                        />: </b>
                                </td>
                                <td width="70%" align="right">
                                    <xsl:value-of
                                        select="/biospecimen/patient_details/PATIENT_dtDob"/>
                                </td>
                            </tr>
                            <tr class="uportal-input-text">
                                <td width="100%" colspan="2" align="right">
                                    <xsl:variable name="patientID">
                                        <xsl:value-of
                                            select="/biospecimen/patient_details/PATIENT_intInternalPatientID"
                                        />
                                    </xsl:variable>
                                    <a
                                        href="{$patientChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$patientChannelTabOrder}&amp;action=view_patient&amp;PATIENT_intInternalPatientID={$patientID}"
                                        >View Patient Details</a>
                                </td>
                            </tr>
                        </table>
                    </xsl:if>
                    <br/>
                    <br/>
                    <xsl:if test="count(/biospecimen/flagged) &gt; 0"> Flagged Records : <table
                            border="0" width="100%">
                            <xsl:for-each select="/biospecimen/flagged">
                                <tr>
                                    <td class="uportal-input-text">
                                        <xsl:variable name="BIOSPECIMEN_strBiospecimenID">
                                            <xsl:value-of select="BIOSPECIMEN_strBiospecimenID"/>
                                        </xsl:variable>
                                        <xsl:variable name="intFlagID">
                                            <xsl:value-of select="FLAG_intID"/>
                                        </xsl:variable>
                                        <a
                                            href="{$baseActionURL}?uP_root=root&amp;module=core&amp;action=view_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={$intFlagID}">
                                            <xsl:value-of select="BIOSPECIMEN_strBiospecimenID"/>
                                        </a>
                                    </td>
                                </tr>
                            </xsl:for-each>
                        </table>
                    </xsl:if>
                    <!--                       <xsl:call-template name="branch"/> -->
                </td>
                <td width="5%"/>
                <!--                </tr>
            </table>-->
                <td width="80%">
                    <table width="100%">
                        <tr>
                            <td class="uportal-channel-subtitle">View biospecimen</td>
                            <td align="right">
                                <form name="back_form" action="{$baseActionURL}" method="POST">
                                    <!-- back to the parent biospecimen -->
                                    
                                            <input type="hidden" name="uP_sparam" value="activeTab"/>
                                            <input type="hidden" name="activeTab" value="3"/>
                                            <input type="hidden" name="uP_root" value="root"/>
                                            <input type="hidden" name="module"
                                                value="biospecimen_search"/>
                                            <input type="hidden" name="action"
                                                value="redo_last_search"/>
                                       
                                </form>
                                <xsl:variable name="fromPatientAnalysis"><xsl:value-of select="fromPatientAnalysis"/></xsl:variable>
                                
                                <xsl:choose>
                                <xsl:when test="($fromPatientAnalysis = 'true')">
                                    <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
                                        alt="Previous" onclick="javascript:jumpTo('{$smartformChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformChannelTabOrder}&amp;domain=Bioanalysis&amp;strcurrent=patient_view&amp;PATIENT_intPatientID={$intInternalPatientID}')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
                                        alt="Previous" onclick="javascript:document.back_form.submit();"/>                                
                                </xsl:otherwise>
                                </xsl:choose>        
                            <img border="0" src="media/neuragenix/buttons/next_disabled.gif"
                                alt="Next"/>
                            </td>
                        </tr>
                    </table>
                    <table width="100%">
                        <tr><td><hr/></td></tr>
                    </table>
                    <!-- button bar -->
                    <table width="100%">
                        <tr>
                            <td>
                                <script>
                                    function changeButtonMessage1(message){
                                    document.getElementById('userButtonLabel').value=message;
                                    }
                                    function hideButtonMessage1(){
                                    buttonLabelMessage.style.visibility='hidden';
                                    }
                                    function showButtonMessage1(message)
                                    {
                                    document.getElementById('userButtonLabel').value=message;
                                    buttonLabelMessage.style.visibility='visible';
                                    }
                                    
                                    function hideButtonMessage2(){
                                        buttonLabelMessage2.style.visibility='hidden';
                                    }
                                    function showButtonMessage2(message)
                                    {
                                        document.getElementById('userButtonLabel2').value=message;
                                        buttonLabelMessage2.style.visibility='visible';
                                    }
                                    
                                    
                                </script>
                                <!-- button start -->
                                <img alt="Add sub biospecimen" name="add_sub1" tabindex="80" src="media/neuragenix/buttons/add_sub_bio_norm.jpg"
                                   onclick="javascript:document.add_sub_form.submit();" 
                                    onMouseover="javascript:document.add_sub1.src='media/neuragenix/buttons/add_sub_bio_sel.jpg'; showButtonMessage1('Add a new sub biospecimen');"
                                    onMouseOut="javascript:document.add_sub1.src='media/neuragenix/buttons/add_sub_bio_norm.jpg'; hideButtonMessage1();"
                                    ></img>&#160;

                                
                                <img alt="Clone" name="clone1" tabindex="80" src="media/neuragenix/buttons/clone_norm.jpg"
                                   onclick="javascript:document.clone_form.submit();" 
                                    onMouseover="javascript:document.clone1.src='media/neuragenix/buttons/clone_sel.jpg';showButtonMessage1('Clone');"
                                    onMouseOut="javascript:document.clone1.src='media/neuragenix/buttons/clone_norm.jpg';hideButtonMessage1();"
                                 ></img>&#160;

                                
                               
                                
                                <xsl:if test="biospecimen_modules/batchCreating='true'">
                                    <xsl:if test="$intBiospecParentID='-1'">
                                       <img alt="Batch Create" tabindex="82" onclick="javascript:document.batch_create_form.submit();" name="batch_create1"
                                          src="media/neuragenix/buttons/batch_create_norm.jpg"
                                           onMouseOver="javascript:document.batch_create1.src='media/neuragenix/buttons/batch_create_sel.jpg';showButtonMessage1('Batch Create');"
                                           onMouseOut="javascript:document.batch_create1.src='media/neuragenix/buttons/batch_create_norm.jpg';hideButtonMessage1();"
                                          >
                                       </img>&#160;

                                    </xsl:if>
                                </xsl:if>    
                                
                                <!--
                                <xsl:if test="biospecimen_modules/batchCloning='true'">
                                    <img alt="Batch Clone" tabindex="82" onclick="javascript:document.batch_clone_form.submit();" name="batch_clone1"
                                          src="media/neuragenix/buttons/batch_clone_norm.jpg'"
                                          onMouseOver="javascript:document.batch_create1.src='media/neuragenix/buttons/batch_create_sel.jpg"
                                          onMouseOut="javascript:document.batch_create1.src='media/neuragenix/buttons/batch_create_norm.jpg'"
                                          /> -->

                                   
                                 
                               
                                <xsl:if test="biospecimen_modules/bioanalysis='true'"> 
                                   <img alt="Analysis Result" tabindex="82" 
                                          name="results1"
                                          src="media/neuragenix/buttons/analysis_results_norm.jpg"
                                       onMouseOver="javascript:document.results1.src='media/neuragenix/buttons/analysis_results_sel.jpg';showButtonMessage1('Analysis Results');"
                                       onMouseOut="javascript:document.results1.src='media/neuragenix/buttons/analysis_results_norm.jpg';hideButtonMessage1();"
                                         onclick="javascript:jumpTo('{$smartformChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformChannelTabOrder}&amp;domain=Bioanalysis&amp;participant={$intBiospecimenID}&amp;intStudyID={$intBiospecStudyID}&amp;strcurrent=biospecimen_view&amp;intInternalPatientID={$intInternalPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;strOrigin=biospecimen_view&amp;title={$strBiospecimenID}&amp;var1={$strBiospecSampleTypeSelected}&amp;var2={$dtBiospecSampleDate}')"
                                         />&#160;


                                                   
                                </xsl:if>
                                <!--
                                <xsl:variable name="intVialKey" select="VIAL_CALCULATION_intVialKey"/>
                                <xsl:if test="biospecimen_modules/vialCalculation='true'">
                                    <xsl:choose>buttonLabelAddSpecimen.style.visibility='visible';
                                        <xsl:when test="$intVialKey  ='-1'">
                                            <button type="button" class="uportal-button"
                                                tabindex="82" onclick="javascript:document.vial_cal_form.submit();">Vial Calculation</button>
                                            &#160; 
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <button type="button" class="uportal-button"
                                                tabindex="82" onclick="javascript:document.vial_cal_history_form.submit();">Vial Calculation History</button>
                                            &#160;
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:if>
                                -->
                                
                                <xsl:if test="biospecimen_modules/studySmartforms='true'">
                                   <img alt="Study Result" tabindex="82" 
                                      onclick="javascript:jumpTo('{$smartformChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformChannelTabOrder}&amp;domain=Biospecimen&amp;participant={$intBiospecimenID}&amp;intStudyID={$intBiospecStudyID}&amp;strcurrent=biospecimen_view&amp;intInternalPatientID={$intInternalPatientID}&amp;intBiospecimenID={$intBiospecimenID}&amp;strOrigin=biospecimen_view&amp;title={$strBiospecimenID}&amp;var1={$strBiospecSampleTypeSelected}&amp;var2={$dtBiospecSampleDate}')"
                                      name="study_results1"
                                      src="media/neuragenix/buttons/study_results_norm.jpg"
                                       onMouseOver="javascript:document.study_results1.src='media/neuragenix/buttons/study_results_sel.jpg';showButtonMessage1('Study Results');"
                                       onMouseOut="javascript:document.study_results1.src='media/neuragenix/buttons/study_results_norm.jpg';hideButtonMessage1();"
                                     />&#160;

                               </xsl:if>
                                
                                
                               <img alt="Attachments" tabindex="84"
                                  name="attachments1"
                                   onclick="javascript:document.attachment_form.submit();"
                                   onMouseover="javascript:document.attachments1.src='media/neuragenix/buttons/attachments_sel.jpg';showButtonMessage1('Attachments');"
                                   src="media/neuragenix/buttons/attachments_norm.jpg"
                                   onMouseOut="javascript:document.attachments1.src='media/neuragenix/buttons/attachments_norm.jpg';hideButtonMessage1();"                                   
                                   /> &#160;



                                
                                <xsl:if test="count(/biospecimen/AutoDocs) &gt; 0">
                                 <img alt="Generate Report" tabindex="84"
                                    name="report1"
                                    onclick="javascript:document.report_form.submit();"      
                                     onMouseover="javascript:document.report1.src='media/neuragenix/buttons/gen_reports_sel.jpg';showButtonMessage1('Generate Report'); "
                                   src="media/neuragenix/buttons/gen_reports_norm.jpg"
                                     onMouseOut="javascript:document.report1.src='media/neuragenix/buttons/gen_reports_norm.jpg';hideButtonMessage1();"                                   
                                 />   &#160;

                           </xsl:if>
                               
                                <!-- end of buttons -->
                            </td>  
                        </tr>
                       <tr>
                            <td name="buttonMessage1">
                                
                                   <div id="buttonLabelMessage" style="position:relative; visibility:hidden; z-index:10">
                                       <input size="30" type="text" id="userButtonLabel" style="border-style:none; background-color=#FFFFF" bgcolor="#FFFFF" value="" />
                                   </div>
                                <hr/>
                            </td>
                         </tr>

                    </table>
                    
                    
                    
                    
                    
                    <form name="biospecimen_form"
                        action="{$baseActionURL}?current=biospecimen_view&amp;module=core"
                        method="post">
                        <input type="hidden" name="keepSubType" value=""/>
                        <input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
                            value="{$intBiospecimenID}"/>
                        <input type="hidden" name="BIOSPECIMEN_intPatientID"
                            value="{$intInternalPatientID}"/>
                        <input type="hidden" name="PATIENT_intInternalPatientID"
                            value="{$intInternalPatientID}"/>
                        <input type="hidden" name="BIOSPECIMEN_Timestamp"
                            value="{$BIOSPECIMEN_Timestamp}"/>
                        <input type="hidden" name="action" value="save_biospecimen"/>
                        <input type="hidden" name="current" value="view_biospecimen"/>
                        <table width="100%">
                            <tr>
                                <td class="neuragenix-form-required-text" width="50%">
                                    <xsl:value-of select="strErrorDuplicateKey"/>
                                    <xsl:value-of select="strErrorRequiredFields"/>
                                    <xsl:value-of select="strErrorInvalidDataFields"/>
                                    <xsl:value-of select="strErrorInvalidData"/>
                                    <xsl:value-of select="strErrorChildStillExist"/>
                                    <xsl:value-of select="strErrorBiospecTypeChange"/>
                                    <xsl:value-of select="$strErrorMessage"/>
                                    <xsl:value-of select="strLockError"/>
                                </td>
                                <td class="neuragenix-form-required-text" width="50%"
                                    id="neuragenix-required-header" align="right"> * = Required
                                    fields </td>
                            </tr>
                        </table>
                        <table width="100%">
                            <tr>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                    <xsl:if
                                        test="./BIOSPECIMEN_strBiospecimenIDDisplay[@required='true']"
                                        > * </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strBiospecimenIDDisplay"/>: </td>
                                <td class="uportal-label" id="neuragenix-form-row-input-input">
                                    <!-- <xsl:value-of select="BIOSPECIMEN_strBiospecimenID"/> -->
                                    <input type="text" name="BIOSPECIMEN_strBiospecimenID" value="{$strBiospecimenID}" size="15" class="uportal-input-text"/>
                                </td>
                                <td width="5%"/>
                                <!--
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                    <xsl:if test="./BIOSPECIMEN_strParentDisplay[@required='true']">
                                        * </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strParentIDDisplay"/>: </td>
                                <td width="26%" class="uportal-label"
                                    id="neuragenix-form-row-input-input">
                                    <xsl:value-of select="BIOSPECIMEN_strParentID"/>-->
                                    <input type="hidden" name="BIOSPECIMEN_strParentID"
                                        value="{$strBiospecParentID}"/>
                                    <input type="hidden" name="BIOSPECIMEN_intParentID"
                                        value="{$intBiospecParentID}"/>
                                <!--</td> -->
                                <td width="5%" id="neuragenix-end-spacer"/> 
                            </tr>
                            <tr>
                                <input type="hidden" name="newTypeSelected" value="false"/> 
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                    <xsl:if
                                        test="./BIOSPECIMEN_strSampleTypeDisplay[@required='true']">
                                        * </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strSampleTypeDisplay"/>: </td>
                                <script language="javascript"> function dropDownUpdate() {
                                    document.biospecimen_form.action.value = 'refresh_view_biospecimen'; 
                                    document.biospecimen_form.newTypeSelected.value = 'true';
                                    document.biospecimen_form.submit();                                    
                                    } </script>
                                <!-- 
                                document.biospecimen_form.action.value = 'refresh_allocation_search';
                                document.biospecimen_form.submit();
                                -->
                                <td width="26%" id="neuragenix-form-row-input-input"
                                    class="uportal-label">
                                    <select name="BIOSPECIMEN_strSampleType" tabindex="21"
                                        class="uportal-input-text"
                                        onchange="javascript:dropDownUpdate();">
                                        <xsl:for-each select="BIOSPECIMEN_strSampleType">
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                                <td width="5%"/>
                                <!--
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                    <xsl:if test="./BIOSPECIMEN_strOtherDisplay[@required='true']">
                                        * </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strOtherIDDisplay"/>: </td>
                                <td width="25%" id="neuragenix-form-row-input-input"
                                    class="uportal-label">
                                    <input type="text" name="BIOSPECIMEN_strOtherID" size="20"
                                        tabindex="27" value="{$strBiospecOtherID}"
                                        class="uportal-input-text"/>
                                </td> -->
                                <td width="5%" id="neuragenix-end-spacer"/>
                            </tr>
                            <tr>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                    <xsl:if test="./BIOSPECIMEN_strSpeciesDisplay[@required='true']"
                                        > * </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strSpeciesDisplay"/>: </td>
                                <td width="26%" id="neuragenix-form-row-input" class="uportal-label">
                                    <select name="BIOSPECIMEN_strSpecies" tabindex="22"
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_strSpecies">
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                                <td width="5%"/>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                    <xsl:if
                                        test="./BIOSPECIMEN_strSampleDateDisplay[@required='true']">
                                        * </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_dtSampleDateDisplay"/>
                                </td>
                                <td width="25%" class="uportal-label">
                                        <xsl:choose>
                                        <xsl:when test="BIOSPECIMEN_dtSampleDate/@display_type='dropdown'">
                                            <select name="BIOSPECIMEN_dtSampleDate_Day" tabindex="32" class="uportal-input-text">
                                                    <xsl:for-each select="BIOSPECIMEN_dtSampleDate_Day">
                                                            <option>
                                                                    <xsl:attribute name="value">
                                                                            <xsl:value-of select="."/>
                                                                    </xsl:attribute>
                                                                    <xsl:if test="@selected=1">
                                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                                    </xsl:if>
                                                                    <xsl:value-of select="."/>
                                                            </option>
                                                    </xsl:for-each>
                                            </select>

                                            <select name="BIOSPECIMEN_dtSampleDate_Month" tabindex="33"
                                                    class="uportal-input-text">
                                                    <xsl:for-each select="BIOSPECIMEN_dtSampleDate_Month">
                                                            <option>
                                                                    <xsl:attribute name="value">
                                                                            <xsl:value-of select="."/>
                                                                    </xsl:attribute>
                                                                    <xsl:if test="@selected=1">
                                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                                    </xsl:if>
                                                                    <xsl:value-of select="."/>
                                                            </option>
                                                    </xsl:for-each>
                                            </select>
                                          </xsl:when>
                                          <xsl:otherwise>
                                            <input type="text" name="BIOSPECIMEN_dtSampleDate_Day" size="2" tabindex="32" class="uportal-input-text">
                                                        <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtSampleDate_Day"/></xsl:attribute>
                                            </input>
                                             <input type="text" name="BIOSPECIMEN_dtSampleDate_Month" size="2" tabindex="33" class="uportal-input-text">
                                                        <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtSampleDate_Month"/></xsl:attribute>
                                            </input>

                                          </xsl:otherwise>
                                          </xsl:choose>


                                            <input type="text" name="BIOSPECIMEN_dtSampleDate_Year" size="5" tabindex="34" class="uportal-input-text">
                                                    <xsl:attribute name="value">
                                                            <xsl:value-of select="BIOSPECIMEN_dtSampleDate_Year"/>
                                                    </xsl:attribute>
                                            </input>
                                </td>
                                <td width="5%" id="neuragenix-end-spacer"/>
                            </tr>
                            <tr>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                    <xsl:if
                                        test="./BIOSPECIMEN_intStudyKeyDisplay[@required='true']"> *
                                    </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_intStudyKeyDisplay"/>: </td>
                                <td width="26%" id="neuragenix-form-row-input" class="uportal-label">
                                    <select class="uportal-input-text"
                                        name="BIOSPECIMEN_intStudyKey" tabindex="23" onClick="javascript:updateColor(this)" onChange="javascript:updateColor(this)">                                        
                                        <xsl:if test="string-length(PermissionChangeStudy) = 0">
                                            <xsl:attribute name="disabled">true</xsl:attribute>
                                        </xsl:if>                                                                                
                                        <xsl:variable name="BIOSPECIMEN_intStudyID">
                                            <xsl:value-of select="intBiospecStudyID"/>
                                        </xsl:variable>
                                        <xsl:for-each select="study_list_biospecimen">
                                            <xsl:variable name="varIntStudyID">
                                                <xsl:value-of select="STUDY_intStudyID"/>
                                            </xsl:variable>
                                            <xsl:variable name="expired">
                                                <xsl:value-of
                                                    select="STUDY_strStudyName/@expired"
                                                />
                                            </xsl:variable> 
                                            <xsl:choose>
                                            <xsl:when test="$expired = '1'">
                                            <option style="color: red; font-weight: bold">
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="STUDY_intStudyID"/>
                                                </xsl:attribute>
                                                <xsl:if test="$intBiospecStudyID = $varIntStudyID">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="STUDY_strStudyName"/>
                                            </option>
                                            </xsl:when>
                                            <xsl:otherwise>
                                            <option class="uportal-input-text">
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="STUDY_intStudyID"/>
                                                </xsl:attribute>
                                                <xsl:if test="$intBiospecStudyID = $varIntStudyID">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="STUDY_strStudyName"/>
                                            </option>
                                            </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:for-each>
                                    </select>
                                    <script language="javascript">
                                       updateColor(document.biospecimen_form.BIOSPECIMEN_intStudyKey);
                                    </script>
                                </td>
                                <td width="5%"/>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                    <xsl:if
                                        test="./BIOSPECIMEN_dtExtractedDateDisplay[@required='true']"
                                        > * </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_dtExtractedDateDisplay"/>: </td>
                                <td width="25%" class="uportal-label">
                                        <xsl:choose>
                                        <xsl:when test="BIOSPECIMEN_dtExtractedDate/@display_type='dropdown'">
                                            <select name="BIOSPECIMEN_dtExtractedDate_Day" tabindex="35"
                                                    class="uportal-input-text">
                                                    <xsl:for-each select="BIOSPECIMEN_dtExtractedDate_Day">
                                                            <option>
                                                                    <xsl:attribute name="value">
                                                                            <xsl:value-of select="."/>
                                                                    </xsl:attribute>
                                                                    <xsl:if test="@selected=1">
                                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                                    </xsl:if>
                                                                    <xsl:value-of select="."/>
                                                            </option>
                                                    </xsl:for-each>
                                            </select>

                                            <select name="BIOSPECIMEN_dtExtractedDate_Month" tabindex="36"
                                                    class="uportal-input-text">
                                                    <xsl:for-each select="BIOSPECIMEN_dtExtractedDate_Month">
                                                            <option>
                                                                    <xsl:attribute name="value">
                                                                            <xsl:value-of select="."/>
                                                                    </xsl:attribute>
                                                                    <xsl:if test="@selected=1">
                                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                                    </xsl:if>
                                                                    <xsl:value-of select="."/>
                                                            </option>
                                                    </xsl:for-each>
                                            </select>
                                          </xsl:when>
                                          <xsl:otherwise>
                                            <input type="text" name="BIOSPECIMEN_dtExtractedDate_Day" size="2" tabindex="35" class="uportal-input-text">
                                                        <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtExtractedDate_Day"/></xsl:attribute>
                                            </input>
                                             <input type="text" name="BIOSPECIMEN_dtExtractedDate_Month" size="2" tabindex="36" class="uportal-input-text">
                                                        <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtExtractedDate_Month"/></xsl:attribute>
                                            </input>

                                          </xsl:otherwise>
                                          </xsl:choose>
                                    
                                            <input type="text" name="BIOSPECIMEN_dtExtractedDate_Year" size="5" tabindex="37" class="uportal-input-text">
                                                    <xsl:attribute name="value">
                                                            <xsl:value-of select="BIOSPECIMEN_dtExtractedDate_Year"/>
                                                    </xsl:attribute>
                                            </input>
                                </td>
                                <td width="5%" id="neuragenix-end-spacer"/>
                            </tr>

                            <tr>
                                <!--
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text" valign="top">
                                
                                    <xsl:if
                                        test="./BIOSPECIMEN_strEncounterDisplay[@required='true']">
                                        * </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label" valign="top">
                                    <xsl:value-of select="BIOSPECIMEN_strEncounterDisplay"/>: </td>
                                <td width="26%" id="neuragenix-form-row-input" class="uportal-label"
                                    valign="top">
                                    <select name="BIOSPECIMEN_strEncounter" tabindex="24"
                                        class="uportal-input-text">
                                        <xsl:for-each select="search_encounter_list">
                                            <xsl:variable name="varEncounter">
                                                <xsl:value-of select="ADMISSIONS_strAdmissionID"/>
                                            </xsl:variable>
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="ADMISSIONS_strAdmissionID"
                                                  />
                                                </xsl:attribute>
                                                <xsl:if test="$strEncounter=$varEncounter">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="ADMISSIONS_strAdmissionID"/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                                
                                <td width="5%" valign="top"/>-->
								 <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text" valign="top"/>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label" valign="top"> Age: </td>
                                <td width="25%" valign="top">
                                    <xsl:value-of select="BIOSPECIMEN_age"/>
                                </td>
								
                                <!--   <td width="5%" id="neuragenix-end-spacer" valign="top"/>-->
                                <!-- Comments -->
                                <!--<td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text" valign="top"/>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label" valign="top"/>
                                <td width="26%" id="neuragenix-form-row-input" class="uportal-label"
                                    valign="top"/>-->
                                <td width="5%" valign="top" rowspan="2"/>
                                <td rowspan="2" width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text" valign="top"/>
                                <td rowspan="2" width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label" valign="top">
                                    <xsl:value-of select="BIOSPECIMEN_strCommentsDisplay"/>: </td>
                                <td rowspan="2" width="25%" valign="top">
                                    <textarea name="BIOSPECIMEN_strComments" rows="4" cols="40"
                                        tabindex="41" class="uportal-input-text">
                                        <xsl:value-of select="BIOSPECIMEN_strComments"/>
                                    </textarea>
                                </td>
                                <td rowspan="2" width="5%" id="neuragenix-end-spacer" valign="top"/>
                            </tr>
                            <!--Age-->
							<tr>
                               
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text" valign="top"/>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label" valign="top"> Flagged: </td>
                                <td width="25%" valign="top">
                                    <input type="hidden" name="wasFlagged" value="{$flag}"/>
                                    <xsl:choose>
                                        <xsl:when test="contains($flag,'true')">
                                            <input type="checkbox" name="isFlagged" tabindex="40"
                                                checked="checked" value="true" class="uportal-text"
                                            />
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <input type="checkbox" tabindex="40" name="isFlagged"
                                                value="true" class="uportal-text"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                             
                                <td width="5%" valign="top"/>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text" valign="top"/>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label" valign="top"></td>
                                <td width="25%" valign="top">
                                  
                                </td>
                                <td width="5%" id="neuragenix-end-spacer" valign="top"/>
                            </tr>
							<!--Age-->
                            <!-- subtype table go here -->
                            <xsl:if test="count(BIOSPECIMEN_strSampleSubType) &gt; 0">
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text" valign="top">
                                        <xsl:if
                                            test="./BIOSPECIMEN_strSampleSubTypeDisplay[@required='true']"
                                            > * </xsl:if>
                                    </td>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label" valign="top">
                                        <xsl:value-of select="BIOSPECIMEN_strSampleSubTypeDisplay"
                                        />: </td>
                                    <td width="26%" id="neuragenix-form-row-input"
                                        class="uportal-label" valign="top">
                                        <!-- select box for subtype -->
                                        <select name="BIOSPECIMEN_strSampleSubType" tabindex="25"
                                            class="uportal-input-text">
                                            <xsl:for-each select="BIOSPECIMEN_strSampleSubType">
                                                <option>
                                                  <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                  </xsl:attribute>
                                                  <xsl:if test="@selected='1'">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                  <xsl:value-of select="."/>
                                                </option>
                                            </xsl:for-each>
                                        </select>
                                    </td>
                                    <td width="5%" valign="top"/>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text" valign="top"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label" valign="top">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_strSubTypeDescriptionDisplay"/>: </td>
                                    <td width="25%" valign="top">
                                        <textarea name="BIOSPECIMEN_strSubTypeDescription" rows="4"
                                            cols="40" tabindex="42" class="uportal-input-text">
                                            <xsl:value-of select="BIOSPECIMEN_strSubTypeDescription"
                                            />
                                        </textarea>
                                    </td>
                                    <td width="5%" id="neuragenix-end-spacer" valign="top"/>
                                </tr>                                                                   
                            </xsl:if>
                            <!-- subtype table end here -->
                            <!-- if subtype lr appears -->
                            <xsl:if test="count(BIOSPECIMEN_strSubTypeLR) &gt; 0">

                            <tr>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text" valign="top">
                                    <xsl:if
                                        test="./BIOSPECIMEN_strSubTypeLRDisplay[@required='true']"
                                        > * </xsl:if>

                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label" valign="top">
                                    <xsl:value-of select="BIOSPECIMEN_strSubTypeLRDisplay"
                                        />: </td>


                                <td width="26%" id="neuragenix-form-row-input"
                                    class="uportal-label" valign="top">
                                    <!-- select box for subtype -->
                                    <select name="BIOSPECIMEN_strSubTypeLR" tabindex="25"
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_strSubTypeLR">
                                            <option>
                                                <xsl:attribute name="value">
                                                    <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected='1'">
                                                    <xsl:attribute name="selected"
                                                        >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>

                            </tr>
                            </xsl:if>                            
                            <tr>
                                <td align="left" colspan="4">
                                    <input type="button" name="delete" value="Delete" tabindex="44"
                                        class="uportal-button"
                                        onblur="javascript:document.biospecimen_form.BIOSPECIMEN_strSampleType.focus()"
                                        onclick="javascript:confirmDelete('{$baseActionURL}?current=biospecimen_view&amp;module=core&amp;action=delete_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;target=biospecimen')"
                                    />
                                </td>
                                <td align="right" colspan="4">
                                    <input type="hidden" name="BIOSPECIMEN_intCellID"
                                        value="{$intInvCellID}"/>
                                    <input type="submit" name="save" value="Save Biospecimen"
                                        tabindex="43" class="uportal-button"
                                        onblur="javascript:document.biospecimen_form.BIOSPECIMEN_strSampleType.focus()"
                                        onclick="javascript:submitBiospecimenForm()"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="8">
                                    <hr/>
                                </td>
                            </tr>
                        </table>
                        
                        <xsl:if test="string-length(PermissionChangeStudy) = 0">
                            <xsl:choose>
                            <xsl:when test="string-length(BIOSPECIMEN_intStudyKey) = 0">
                            <xsl:for-each select="study_list_biospecimen">
                                <xsl:if test="STUDY_strStudyName=../default_system_study">
                                    <xsl:variable name="STUDY_intStudyID">
                                        <xsl:value-of select="STUDY_intStudyID"/>
                                    </xsl:variable>                                    
                                    <input type="hidden" name="BIOSPECIMEN_intStudyKey" value='{$STUDY_intStudyID}'/>
                                </xsl:if>
                            </xsl:for-each>
                            </xsl:when>
                            <xsl:otherwise>
                                <input type="hidden" name="BIOSPECIMEN_intStudyKey" value='{$intBiospecStudyID}'/>
                            </xsl:otherwise>
                            </xsl:choose>
                        </xsl:if>                        
                    </form>
                    <!-- end of biospecimen form -->
                    <xsl:if test="contains($showInventory,'true')">
                        <table width="100%">
                            <tr>
                                <td id="neuragenix-form-row-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td class="uportal-channel-table-header" colspan="6">Location:</td>
                                <td/>
                            </tr>
                            <tr>
                                <td height="10px" colspan="8"/>
                            </tr>
			                           
                            <tr>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                      Site: </td>
                                <td class="uportal-text">
                                    <xsl:value-of select="inventory_info/SITE_strSiteName"/>
                                </td>
                                <td width="5%"/>
                                
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                      Location ID: </td>
                                <td class="uportal-text">
                                    <xsl:value-of select="inventory_info/intLocationID"/>
                                </td>
                                <td width="5%"/>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    </td>
                                <td class="uportal-text">
                                    
                                </td>
                                <td id="neuragenix-end-spacer"/>
                                
                            </tr>
                            <tr>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    Storage Unit: </td>
                                <td class="uportal-text">
                                    <xsl:value-of select="inventory_info/TANK_strTankName"/>
                                </td>
                                <td width="5%"/>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    Column: </td>
                                <td class="uportal-text">
                                	<script language="JavaScript" type="text/javascript">	
                                			var row = "<xsl:value-of select="inventory_info/TRAY_intColumnNumber"/>"									
											document.write(row.toUpperCase());										
									</script>                                     
                                </td>
                                <td id="neuragenix-end-spacer"/>
                            </tr>
                            <tr>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label"> Rack: </td>
                                <td class="uportal-text">
                                    <xsl:value-of select="inventory_info/BOX_strBoxName"/>
                                </td>
                                <td width="5%"/>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label"> Row: </td>
                                <td class="uportal-text">
                                		<script language="JavaScript" type="text/javascript">	
                                			var row = "<xsl:value-of select="inventory_info/TRAY_intRowNumber"/>"									
											document.write(row.toUpperCase());										
										</script>                                    
                                </td>
                                <td id="neuragenix-end-spacer"/>
                            </tr>
                            <tr>                                
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    Box: </td>
                                <td class="uportal-text">
                                    <xsl:value-of select="inventory_info/TRAY_strTrayName"/>
                                </td>
                                <td id="neuragenix-end-spacer"/>
                            </tr>
                            <tr>
                                <td colspan="1"/>
                                <td class="uportal-text" align="right" colspan="8">
                                    <xsl:choose>
                                        <xsl:when test="inventory_info/SITE_strSiteName = 'N/A'">
                                            <form
                                                action="{$inventoryChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=4&amp;source=allocate&amp;intInternalPatientID={$intInternalPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;strOrigin=view_biospecimen"
                                                method="post">
                                                <input type="submit" class="uportal-button"
                                                  value="Allocate"/>
                                            </form>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <table align="right">
                                                <tr>
                                                  <td>
                                                  <form method="post"
                                                  action="{$inventoryChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=4&amp;current=view_tray&amp;source=view&amp;intInternalPatientID={$intInternalPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;CELL_intCellID={$intInvCellID}&amp;strOrigin=view_biospecimen">
                                                  <input type="submit" value="View"
                                                  class="uportal-button"/>
                                                  </form>
                                                  </td>
                                                  <td>
                                                  <form method="post"
                                                  action="{$inventoryChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=4&amp;current=view_tray&amp;source=change&amp;PATIENT_intInternalPatientID={$intInternalPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;CELL_intCellID={$intInvCellID}&amp;strOrigin=view_biospecimen">
                                                  <input value="Change" type="submit"
                                                  class="uportal-button"/>
                                                  </form>
                                                  </td>
                                                  <td>
                                                  <form method="post"
                                                  action="{$baseActionURL}?current=view_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;intInvCellID={$intInvCellID}&amp;module=ALLOCATE_CELL&amp;action=unallocate">
                                                  <input value="Unallocate" type="submit"
                                                  class="uportal-button"/>
                                                  </form>
                                                  </td>
                                                </tr>
                                            </table>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="9">
                                    <hr/>
                                </td>
                            </tr>
                        </table>
                    </xsl:if>
                   	<!-- Notes -->
					<xsl:if test="$viewnotes ='yes'">
					<xsl:if test="count(biospecimen_notes)=1">
						<table width="100%">
							<xsl:if test="count(./biospecimen_notes/notes) &gt; 0">
							<tr>
								<td/>
								<td class="uportal-channel-table-header"
									colspan="4"> Notes:</td>
								<td/>
								
							</tr>
							<tr>
								<td/>
								<td width="50%" class="uportal-label">
									Description
								</td>
								<td width="20%" class="uportal-label">									
										Date Added
								</td>
                                                                <td width="10%" class="uportal-label">
                                                                                Added By
                                                                </td>                
								<td width="18%"/>
							</tr>
							</xsl:if>							
								<xsl:for-each select="./biospecimen_notes/notes">
									<xsl:variable name="intNoteKey">
										<xsl:value-of
											select="NOTES_intNotesKey"/>
									</xsl:variable>
									
									<tr>
										<td/>
										<td class="uportal-input-text">
											<xsl:value-of
												select="NOTES_strDescription"/>
										</td>
										<td class="uportal-input-text">
											<xsl:value-of select="NOTES_dtDate"/>
										</td>
                                                                                <td class="uportal-input-text">
											<xsl:value-of select="NOTES_strAddedBy"/>
										</td>
										<td class="uportal-input-text">
											
											<a
												href="{$baseActionURL}?module=NOTES&amp;action=delete_notes&amp;NOTES_intNotesKey={$intNoteKey}
											&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}">
												Delete</a>
										
										</td>
									</tr>
									
								</xsl:for-each>
							
						</table>
						<br/>
						
							<table width="100%">
								
								<form name="notes_form" action="{$baseActionURL}"
									method="post">
									<input type="hidden" name="action"
										value="add_notes"/>
									<input type="hidden" name="module"
										value="NOTES"/>
									<input type="hidden"
										name="BIOSPECIMEN_intBiospecimenID"
										value="{$intBiospecimenID}"/>
									
									<tr>
										<td/>
										<td valign="top" class="uportal-label">
											Notes:</td>
										<td valign="top">
											<textarea name="NOTES_strDescription"
												rows="4" cols="40" tabindex="60"
												class="uportal-input-text"/>
										</td>
									</tr>
									<tr>
										<td colspan="4" align="right">
											<input type="submit"
												class="uportal-button"
												tabindex="61" value="Add Note"/>
											
										</td>
									</tr>
									
								</form>
									<tr>
										<td colspan="4"><hr/></td>
									</tr>
							</table>
						
					</xsl:if>
					</xsl:if><!--End of Notes-->
                    
                    <xsl:if test="contains($showTransactions, 'true')">
                        <!-- transaction stuff -->
                        <table width="100%">						
							
                            <tr>
                                <td id="neuragenix-form-row-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td class="uportal-channel-table-header" colspan="6">Quantity
                                    Details:</td>
                                <td/>
                            </tr>
                            <tr>
                                <td height="10px" colspan="8"/>
                            </tr>
                            <xsl:for-each select="search_trans">
                                <xsl:variable name="BIOSPECIMEN_TRANSACTIONS_intBioTransactionID">
                                    <xsl:value-of
                                        select="BIOSPECIMEN_TRANSACTIONS_intBioTransactionID"/>
                                </xsl:variable>
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"> Date: </td>
                                    <td width="26%" class="uportal-input-text"
                                        id="neuragenix-form-row-input-input">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate"/>
                                    </td>
                                    <td width="5%"/>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"> Treatment: </td>
                                    <td width="26%" class="uportal-input-text"
                                        id="neuragenix-form-row-input-input">
                                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strTreatment"
                                        />
                                    </td>
                                    <td width="5%" id="neuragenix-end-spacer"/>
                                </tr>
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"> Quantity: </td>
                                    <td width="26%" class="uportal-input-text"
                                        id="neuragenix-form-row-input-input"
                                        style="text-align: right">
                                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_flQuantity"/>
                                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strUnit"/>
                                    </td>
                                    <td width="5%"/>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"> Reason: </td>
                                    <td width="26%" class="uportal-input-text"
                                        id="neuragenix-form-row-input-input">
                                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strReason"/>
                                    </td>
                                    <td width="5%" id="neuragenix-end-spacer"/>
                                </tr>
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"> Owner: </td>
                                    <td width="26%" class="uportal-input-text"
                                        id="neuragenix-form-row-input-input">
                                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strOwner"/>
                                    </td>
                                    <td width="5%"/>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"> Recorded By: </td>
                                    <td width="26%" class="uportal-input-text"
                                        id="neuragenix-form-row-input-input">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_strRecordedBy"/>
                                    </td>
                                    <td width="5%" id="neuragenix-end-spacer"/>
                                </tr>
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"> Collaborator: </td>
                                    <td width="26%" class="uportal-input-text"
                                        id="neuragenix-form-row-input-input">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_strCollaborator"/>
                                    </td>
                                    <td width="5%"/>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label">
                                        <!-- assigned study label --> Assigned Study: </td>
                                    <td width="26%" class="uportal-input-text"
                                        id="neuragenix-form-row-input-input">
                                        <!-- assigned study -->
                                        <xsl:variable name="studyKey">
                                            <xsl:value-of
                                                select="BIOSPECIMEN_TRANSACTIONS_intStudyKey"/>
                                        </xsl:variable>
                                        <xsl:for-each select="../study_list">
                                            <xsl:variable name="intStudyID">
                                                <xsl:value-of select="STUDY_intStudyID"/>
                                            </xsl:variable>
                                            <xsl:variable name="strStudyName">
                                                <xsl:value-of select="STUDY_strStudyName"/>
                                            </xsl:variable>
                                            <xsl:if test="$studyKey = $intStudyID">
                                                <xsl:value-of select="$strStudyName"/>
                                            </xsl:if>
                                        </xsl:for-each>
                                    </td>
                                    <td width="5%" id="neuragenix-end-spacer"/>
                                </tr>
                                <xsl:variable name="quantityStatus">
                                    <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strStatus"/>
                                </xsl:variable>
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"> Status: </td>
                                    <td width="26%" class="uportal-input-text"
                                        id="neuragenix-form-row-input-input">
                                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strStatus"/>
                                    </td>
                                    <xsl:if test="$quantityStatus='Delivered'">
                                        <td width="5%"/>
                                        <td width="1%" id="neuragenix-form-row-input-label-required"
                                            class="neuragenix-form-required-text"/>
                                        <td width="18%" id="neuragenix-form-row-input-label"
                                            class="uportal-label"> Delivery Date: </td>
                                        <td width="26%" class="uportal-input-text"
                                            id="neuragenix-form-row-input-input">
                                            <xsl:value-of
                                                select="BIOSPECIMEN_TRANSACTIONS_dtDeliveryDate"/>
                                        </td>
                                    </xsl:if>
                                    <td width="5%" id="neuragenix-end-spacer"/>
                                </tr>
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"> Fixation Time: </td>
                                    <td width="26%" class="uportal-input-text"
                                        id="neuragenix-form-row-input-input">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_strFixationTime"/>
                                    </td>
                                    <td width="5%" id="neuragenix-end-spacer"/>
                                </tr>
                                <tr>
                                    <td/>
                                    <td>
                                        <form
                                            action="{$baseActionURL}?module=core&amp;action=delete_quantity&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;BIOSPECIMEN_TRANSACTIONS_intBioTransactionID={$BIOSPECIMEN_TRANSACTIONS_intBioTransactionID}"
                                            method="post">
                                            <input type="submit" class="uportal-button"
                                                value="Delete"/>
                                        </form>
                                    </td>
                                    <td colspan="5"/>
                                    <!-- delete a transaction -->
                                    <td class="uportal-label">
                                        <xsl:if test="$quantityStatus='Allocated'">
                                            <form
                                                action="{$baseActionURL}?module=core&amp;action=upgrade_quantity&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;BIOSPECIMEN_TRANSACTIONS_intBioTransactionID={$BIOSPECIMEN_TRANSACTIONS_intBioTransactionID}"
                                                method="post">
                                                <input type="submit" class="uportal-button"
                                                  value="Delivered"/>
                                            </form>
                                        </xsl:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="8">
                                        <hr/>
                                    </td>
                                </tr>
                            </xsl:for-each>
                        </table>
                        <!-- quantity form -->
                        <form name="quantity_form"
                            action="{$baseActionURL}?uP_root=root&amp;module=core&amp;action=save_quantity"
                            method="POST">
                            <input type="hidden" name="current" value="biospecimen_view"/>
                            <input type="hidden" name="intInternalPatientID"
                                value="{$intInternalPatientID}"/>
                            <input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
                                value="{$intBiospecimenID}"/>
                            <input type="hidden" name="BIOSPECIMEN_strParentID"
                                value="{$strBiospecimenID}"/>
                            <input type="hidden" name="BIOSPECIMEN_TRANSACTIONS_intBiospecimenID"
                                value="{$intBiospecimenID}"/>
                            <table width="100%">
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"> Available quantity: </td>
                                    <td width="26%" id="neuragenix-form-row-input"
                                        class="uportal-input-text" style="text-align: right">
                                        <xsl:value-of select="BIOSPECIMEN_flNumberCollected"/>
                                    </td>
                                    <td width="5%"/>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text">
                                        <xsl:if
                                            test="./BIOSPECIMEN_TRANSACTIONS_strCollaboratorDisplay[@required='true']"
                                            > * </xsl:if>
                                    </td>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"> Collaborator: </td>
                                    <td width="26%" id="neuragenix-form-row-input"
                                        class="uportal-label">
                                        <select name="BIOSPECIMEN_TRANSACTIONS_strCollaborator"
                                            tabindex="61" class="uportal-input-text">
                                            <xsl:for-each
                                                select="BIOSPECIMEN_TRANSACTIONS_strCollaborator">
                                                <option>
                                                  <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                  </xsl:attribute>
                                                  <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                  <xsl:value-of select="."/>
                                                </option>
                                            </xsl:for-each>
                                        </select>
                                    </td>
                                    <td width="5%" id="neuragenix-end-spacer"/>
                                </tr>
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text">
                                        <xsl:if
                                            test="./BIOSPECIMEN_TRANSACTIONS_dtTransactionDateDisplay[@required='true']"
                                            > * </xsl:if>
                                    </td>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_dtTransactionDateDisplay"
                                        />: </td>
                                    <td width="26%" id="neuragenix-form-row-input"
                                        class="uportal-label">
                                        <select
                                            name="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Day"
                                            tabindex="51" class="uportal-input-text">
                                            <xsl:for-each
                                                select="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Day">
                                                <option>
                                                  <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                  </xsl:attribute>
                                                  <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                  <xsl:value-of select="."/>
                                                </option>
                                            </xsl:for-each>
                                        </select>
                                        <select
                                            name="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Month"
                                            tabindex="52" class="uportal-input-text">
                                            <xsl:for-each
                                                select="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Month">
                                                <option>
                                                  <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                  </xsl:attribute>
                                                  <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                  <xsl:value-of select="."/>
                                                </option>
                                            </xsl:for-each>
                                        </select>
                                        <input type="text"
                                            name="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Year"
                                            value="{$BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Year}"
                                            size="5" tabindex="53" class="uportal-input-text"/>
                                    </td>
                                    <td width="5%"/>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text">
                                        <xsl:if
                                            test="BIOSPECIMEN_TRANSACTIONS_dtTransactionDateDisplay[@required='true']"
                                            >*</xsl:if>
                                    </td>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_strTreatmentDisplay"/>: </td>
                                    <td width="26%" id="neuragenix-form-row-input"
                                        class="uportal-label">
                                        <select name="BIOSPECIMEN_TRANSACTIONS_strTreatment"
                                            tabindex="62" class="uportal-input-text">
                                            <xsl:for-each
                                                select="BIOSPECIMEN_TRANSACTIONS_strTreatment">
                                                <option>
                                                  <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                  </xsl:attribute>
                                                  <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                  <xsl:value-of select="."/>
                                                </option>
                                            </xsl:for-each>
                                        </select>
                                    </td>
                                    <td width="5%" id="neuragenix-end-spacer"/>
                                </tr>
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text">
                                        <xsl:if
                                            test="BIOSPECIMEN_TRANSACTIONS_flQuantityDisplay[@required='true']"
                                            >*</xsl:if>
                                    </td>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_flQuantityDisplay"/>: </td>
                                    <td width="26%" class="uportal-label"
                                        id="neuragenix-form-row-input-input">
                                        <input type="text"
                                            name="BIOSPECIMEN_TRANSACTIONS_flQuantity" size="10"
                                            tabindex="54" class="uportal-input-text"
                                            style="text-align: right"/>
                                            
                                        <!-- anton: preselect the quantity unit if there was a quantity added with particular unit-->
                                        <xsl:variable name="BIOSPECIMEN_TRANSACTIONS_alwaysDispalyUnits" select="BIOSPECIMEN_TRANSACTIONS_alwaysDispalyUnits"/>
                                        <xsl:choose>
                                            <xsl:when test="$BIOSPECIMEN_TRANSACTIONS_alwaysDispalyUnits='1'">
                                                <select name="BIOSPECIMEN_TRANSACTIONS_strUnit" tabindex="55" class="uportal-input-text">
                                                    <xsl:for-each select="BIOSPECIMEN_TRANSACTIONS_strUnit">
                                                        <option>
                                                            <xsl:attribute name="value">
                                                            <xsl:value-of select="."/>
                                                            </xsl:attribute>
                                                            <xsl:if test="@selected=1">
                                                                <xsl:attribute name="selected">true</xsl:attribute>
                                                            </xsl:if>
                                                            <xsl:value-of select="."/>
                                                        </option>
                                                    </xsl:for-each>                                            
                                                </select>
                                            </xsl:when>

                                            <xsl:when test="count(search_trans/BIOSPECIMEN_TRANSACTIONS_strUnit) &gt; 0">
                                                &#160; <xsl:value-of select="search_trans/BIOSPECIMEN_TRANSACTIONS_strUnit[1]"/>
                                            </xsl:when>

                                            <xsl:otherwise>
                                                <select name="BIOSPECIMEN_TRANSACTIONS_strUnit" tabindex="55" class="uportal-input-text">
                                                    <xsl:for-each select="BIOSPECIMEN_TRANSACTIONS_strUnit">
                                                        <option>
                                                            <xsl:attribute name="value">
                                                            <xsl:value-of select="."/>
                                                            </xsl:attribute>
                                                            <xsl:if test="@selected=1">
                                                                <xsl:attribute name="selected">true</xsl:attribute>
                                                            </xsl:if>
                                                            <xsl:value-of select="."/>
                                                        </option>
                                                    </xsl:for-each>                                            
                                                </select>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                        
                                      
                                 
                                        
                                        
                                    </td>
                                    <td width="5%"/>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text">
                                        <xsl:if
                                            test="BIOSPECIMEN_TRANSACTIONS_strReasonDisplay[@required='true']"
                                            >*</xsl:if>
                                    </td>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_strReasonDisplay"/>: </td>
                                    <td width="26%" class="uportal-label"
                                        id="neuragenix-form-row-input-input">
                                        <textarea name="BIOSPECIMEN_TRANSACTIONS_strReason" rows="4"
                                            cols="25" class="uportal-input-text" tabindex="63">
                                            <xsl:value-of select="strBiospecDescription"/>
                                        </textarea>
                                    </td>
                                    <td width="5%" id="neuragenix-end-spacer"/>
                                </tr>
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text">
                                        <xsl:if
                                            test="BIOSPECIMEN_TRANSACTIONS_strOwnerDisplay[@required='true']"
                                            >*</xsl:if>
                                    </td>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_strOwnerDisplay"/>: </td>
                                    <td width="26%" class="uportal-label"
                                        id="neuragenix-form-row-input-input">
                                        <select name="BIOSPECIMEN_TRANSACTIONS_strOwner"
                                            tabindex="56" class="uportal-input-text">
                                            <xsl:variable name="strCurrentUser">
                                                <xsl:value-of
                                                  select="BIOSPECIMEN_TRANSACTIONS_strRecordedBy"
                                                />
                                            </xsl:variable>
                                            <xsl:for-each select="search_user">
                                                <xsl:variable name="strUser">
                                                  <xsl:value-of select="USERDETAILS_strUserName"/>
                                                </xsl:variable>
                                                <option>
                                                  <xsl:attribute name="value">
                                                  <xsl:value-of
                                                  select="USERDETAILS_strUserName"/>
                                                  </xsl:attribute>
                                                  <xsl:if test="$strCurrentUser = $strUser">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                  <xsl:value-of select="USERDETAILS_strUserName"/>
                                                </option>
                                            </xsl:for-each>
                                        </select>
                                    </td>
                                    <td width="5%"/>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_strRecordedByDisplay"
                                        />: </td>
                                    <td width="26%" class="uportal-text"
                                        id="neuragenix-form-row-input-input">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_strRecordedBy"/>
                                        <input type="hidden"
                                            name="BIOSPECIMEN_TRANSACTIONS_strRecordedBy">
                                            <xsl:attribute name="value">
                                                <xsl:value-of
                                                  select="BIOSPECIMEN_TRANSACTIONS_strRecordedBy"
                                                />
                                            </xsl:attribute>
                                        </input>
                                    </td>
                                    <td width="5%" id="neuragenix-end-spacer"/>
                                </tr>
                                <!-- study dropdown -->
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text">
                                        <xsl:if
                                            test="BIOSPECIMEN_TRANSACTIONS_intStudyKeyDisplay[@required='true']"
                                            >*</xsl:if>
                                    </td>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_intStudyKeyDisplay"/>: </td>
                                    <td width="26%" class="uportal-label"
                                        id="neuragenix-form-row-input-input">
                                        <select name="BIOSPECIMEN_TRANSACTIONS_intStudyKey"
                                            tabindex="57" class="uportal-input-text">
                                            <xsl:variable name="strSelectedBioTxnStudy">
                                                <xsl:value-of
                                                  select="BIOSPECIMEN_TRANSACTIONS_intStudyKey"/>
                                            </xsl:variable>
                                            <xsl:for-each select="study_list">
                                                <xsl:variable name="intStudyID">
                                                  <xsl:value-of select="./STUDY_intStudyID"/>
                                                </xsl:variable>
                                                <xsl:variable name="strStudyName">
                                                  <xsl:value-of select="./STUDY_strStudyName"/>
                                                </xsl:variable>
                                                <option>
                                                  <xsl:attribute name="value">
                                                  <xsl:value-of select="./STUDY_intStudyID"/>
                                                  </xsl:attribute>
                                                  <xsl:if
                                                  test="$strSelectedBioTxnStudy = $intStudyID">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                  <xsl:value-of select="./STUDY_strStudyName"/>
                                                </option>
                                            </xsl:for-each>
                                        </select>
                                    </td>
                                    <td width="5%"/>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text">
                                        <xsl:if
                                            test="BIOSPECIMEN_TRANSACTIONS_strStatusDisplay[@required='true']"
                                            >*</xsl:if>
                                    </td>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_strStatusDisplay"/>: </td>
                                    <td width="26%" class="uportal-text"
                                        id="neuragenix-form-row-input-input">
                                        <select name="BIOSPECIMEN_TRANSACTIONS_strStatus"
                                            tabindex="64" class="uportal-input-text">
                                            <xsl:for-each
                                                select="BIOSPECIMEN_TRANSACTIONS_strStatus">
                                                <option>
                                                  <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                  </xsl:attribute>
                                                  <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                  <xsl:value-of select="."/>
                                                </option>
                                            </xsl:for-each>
                                        </select>
                                    </td>
                                    <td width="5%" id="neuragenix-end-spacer"/>
                                </tr>
                                <!-- Destination Study -->
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text">
                                        <xsl:if
                                            test="BIOSPECIMEN_TRANSACTIONS_strFixationTimeDisplay[@required='true']"
                                            >*</xsl:if>
                                    </td>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_strFixationTimeDisplay"
                                        />: </td>
                                    <td width="26%" class="uportal-label"
                                        id="neuragenix-form-row-input-input">
                                        <input tabindex="58"
                                            name="BIOSPECIMEN_TRANSACTIONS_strFixationTime"
                                            class="uportal-input-text"/>
                                    </td>
                                    <td width="5%"/>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"/>
                                    <td width="26%" class="uportal-text"
                                        id="neuragenix-form-row-input-input"/>
                                    <td width="5%" id="neuragenix-end-spacer"/>
                                </tr>
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"/>
                                    <td width="26%" class="uportal-label"
                                        id="neuragenix-form-row-input-input"/>
                                    <td width="5%"/>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label"/>
                                    <td width="26%" class="uportal-text"
                                        id="neuragenix-form-row-input-input"/>
                                    <td width="5%" id="neuragenix-end-spacer"/>
                                </tr>
                            </table>
                            <table width="100%">
                                <tr>
                                    <td width="90%" class="uportal-label"/>
                                    <td width="10%" class="uportal-label">
                                        <input type="submit" name="save_quantity"
                                            value="Save Quantity" tabindex="65"
                                            class="uportal-button"
                                            onblur="javascript:document.quantity_form.BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Day.focus()"
                                        />
                                    </td>
                                </tr>
                            </table>
                        </form>
                         <hr/> 
                    </xsl:if>
                    
                    
                    
                    <table width="100%">
                        <tr>
                            <td >
                                <div id="buttonLabelMessage2" style="position:relative; visibility:hidden;">
                                    <input type="text" size="30" id="userButtonLabel2" style="border-style:none; background-color=#FFFFF" bgcolor="#FFFFF" value="" />
                                </div>
                            </td>
                        </tr>
                        
                        <tr>
                           <td>
                              
                              
                                <!-- button start -->
                                <img alt="Add sub biospecimen" name="add_sub2" tabindex="80" src="media/neuragenix/buttons/add_sub_bio_norm.jpg"
                                   onclick="javascript:document.add_sub_form.submit();" 
                                    onMouseover="javascript:document.add_sub2.src='media/neuragenix/buttons/add_sub_bio_sel.jpg';showButtonMessage2('Add a new sub biospecimen');"
                                    onMouseOut="javascript:document.add_sub2.src='media/neuragenix/buttons/add_sub_bio_norm.jpg';hideButtonMessage2()"
                                   ></img>
                               &#160; 
                                <img alt="Clone" name="clone2" tabindex="80" src="media/neuragenix/buttons/clone_norm.jpg"
                                   onclick="javascript:document.clone_form.submit();" 
                                    onMouseover="javascript:document.clone2.src='media/neuragenix/buttons/clone_sel.jpg';showButtonMessage2('Clone');"
                                    onMouseOut="javascript:document.clone2.src='media/neuragenix/buttons/clone_norm.jpg';hideButtonMessage2()"
                                 ></img>
                                
                               
                               &#160; 
                                
                                <xsl:if test="biospecimen_modules/batchCreating='true'">
                                    <xsl:if test="$intBiospecParentID='-1'">
                                       <img alt="Batch Create" tabindex="82" onclick="javascript:document.batch_create_form.submit();" name="batch_create2"
                                          src="media/neuragenix/buttons/batch_create_norm.jpg"
                                           onMouseOver="javascript:document.batch_create2.src='media/neuragenix/buttons/batch_create_sel.jpg';showButtonMessage2('Batch Create');"
                                           onMouseOut="javascript:document.batch_create2.src='media/neuragenix/buttons/batch_create_norm.jpg';hideButtonMessage2()"
                                          >
                                       </img>
                               &#160; 
                                    </xsl:if>
                                </xsl:if>    
                                
                               

                                   
                                 
                               
                                <xsl:if test="biospecimen_modules/bioanalysis='true'"> 
                                   <img alt="Analysis Result" tabindex="82" 
                                          name="results2"
                                          src="media/neuragenix/buttons/analysis_results_norm.jpg"
                                       onMouseOver="javascript:document.results2.src='media/neuragenix/buttons/analysis_results_sel.jpg';showButtonMessage2('Analysis Results');"
                                       onMouseOut="javascript:document.results2.src='media/neuragenix/buttons/analysis_results_norm.jpg';hideButtonMessage2()"
                                         onclick="javascript:jumpTo('{$smartformChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformChannelTabOrder}&amp;domain=Bioanalysis&amp;participant={$intBiospecimenID}&amp;intStudyID={$intBiospecStudyID}&amp;strcurrent=biospecimen_view&amp;intInternalPatientID={$intInternalPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;strOrigin=biospecimen_view&amp;title={$strBiospecimenID}&amp;var1={$strBiospecSampleTypeSelected}&amp;var2={$dtBiospecSampleDate}')"
                                         />
                               &#160; 
                                </xsl:if>
                               
                                
                                <xsl:if test="biospecimen_modules/studySmartforms='true'">
                                   <img alt="Study Result" tabindex="82" 
                                      onclick="javascript:jumpTo('{$smartformChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformChannelTabOrder}&amp;domain=Biospecimen&amp;participant={$intBiospecimenID}&amp;intStudyID={$intBiospecStudyID}&amp;strcurrent=biospecimen_view&amp;intInternalPatientID={$intInternalPatientID}&amp;intBiospecimenID={$intBiospecimenID}&amp;strOrigin=biospecimen_view&amp;title={$strBiospecimenID}&amp;var1={$strBiospecSampleTypeSelected}&amp;var2={$dtBiospecSampleDate}')"
                                      name="study_results2"
                                      src="media/neuragenix/buttons/study_results_norm.jpg"
                                       onMouseOver="javascript:document.study_results2.src='media/neuragenix/buttons/study_results_sel.jpg';showButtonMessage2('Study Results');"
                                       onMouseOut="javascript:document.study_results2.src='media/neuragenix/buttons/study_results_norm.jpg';hideButtonMessage2()"
                                     />&#160;
                                                   
                               </xsl:if>
                                
                                
                               <img alt="Attachments" tabindex="84"
                                  name="attachments2"
                                   onclick="javascript:document.attachment_form.submit();"
                                   onMouseover="javascript:document.attachments2.src='media/neuragenix/buttons/attachments_sel.jpg';showButtonMessage2('Attachments');"
                                   src="media/neuragenix/buttons/attachments_norm.jpg"
                                   onMouseOut="javascript:document.attachments2.src='media/neuragenix/buttons/attachments_norm.jpg';hideButtonMessage2()"                                   
                                   /> 
                                &#160;


                                
                                <xsl:if test="count(/biospecimen/AutoDocs) &gt; 0">
                                 <img alt="Generate Report" tabindex="84"
                                    name="report2"
                                    onclick="javascript:document.report_form.submit();"      
                                     onMouseover="javascript:document.report2.src='media/neuragenix/buttons/gen_reports_sel.jpg';showButtonMessage2('Generate Reports');"
                                   src="media/neuragenix/buttons/gen_reports_norm.jpg"
                                     onMouseOut="javascript:document.report2.src='media/neuragenix/buttons/gen_reports_norm.jpg';hideButtonMessage2()"                                   
                                 />   &#160;

                           </xsl:if>
                                
                                
                                
                                
                                <!-- end of buttons -->
                                
                                <!-- add sub biospecimen -->
                                <form action="{$baseActionURL}" name="add_sub_form" method="POST">
                                    <input type="hidden" name="PATIENT_intInternalPatientID"
                                        value="{$intInternalPatientID}"/>
                                    <input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
                                        value="{$intBiospecimenID}"/>
                                    <input type="hidden" name="BIOSPECIMEN_strParentID"
                                        value="{$strBiospecimenID}"/>
                                    <input type="hidden" name="BIOSPECIMEN_intParentID"
                                        value="{$intBiospecimenID}"/>
                                    <input type="hidden" name="BIOSPECIMEN_intParentDepth"
                                        value="{$intDepth}"/>
                                    <input type="hidden"
                                        name="BIOSPECIMEN_strSampleType"
                                        value="{$strInitialBiospecSampleType}"/>
                                    <input type="hidden" name="module" value="core"/>
                                    <input type="hidden" name="action" value="add_biospecimen"/>
                                    
                                </form>
                                
                               
                           
                                <!-- clone biospecimen -->
                                <form action="{$baseActionURL}" name="clone_form" method="POST">
                                    <input type="hidden" name="intInternalPatientID"
                                        value="{$intInternalPatientID}"/>
                                    <input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
                                        value="{$intBiospecimenID}"/>
                                    <input type="hidden" name="BIOSPECIMEN_strParentID"
                                        value="{$strBiospecParentID}"/>
                                    <input type="hidden" name="BIOSPECIMEN_intStudyKey"
                                        value="{$intBiospecStudyID}"/>
                                    <input type="hidden" name="action" value="clone_biospecimen"/>
                                    <input type="hidden" name="module" value="core"/>
                                    
                                </form>
                           
                                <!-- batch creating :: available if properties say so -->
                                <xsl:if test="biospecimen_modules/batchCreating='true'">
                                    <xsl:if test="$intBiospecParentID='-1'">
                                
                                    <form name="batch_create_form"
                                        action="{$baseActionURL}?module=BATCH_CREATION&amp;action=batch_create_start&amp;intParentBiospecimenKey={BIOSPECIMEN_intBiospecimenID}&amp;BIOSPECIMEN_strBiospecimenID={BIOSPECIMEN_strBiospecimenID}"
                                        method="post">
                                        <input type="hidden" name="intPatientKey" value="{$intInternalPatientID}"/>
                                        
                                    </form>
                                    </xsl:if>
                                </xsl:if>
                           
                                <!-- batch cloning :: available if properties say so -->
                                <xsl:if test="biospecimen_modules/batchCloning='true'">
                                    <form name="batch_clone_form"
                                        action="{$baseActionURL}?module=BATCH_CLONE&amp;stage=BEGIN&amp;BIOSPECIMEN_intBiospecimenID={BIOSPECIMEN_intBiospecimenID}&amp;BIOSPECIMEN_strBiospecimenID={BIOSPECIMEN_strBiospecimenID}"
                                        method="post">
                                        
                                    </form>
                                </xsl:if>
                           
                                <!-- bioanalysis/analysis results :: available if properties say so -->
                                <xsl:if test="biospecimen_modules/bioanalysis='true'">
                                    <form name="analysis_result_form" action="{$baseActionURL}" method="POST">
                                        <input type="hidden" name="intInternalPatientID"
                                            value="{$intInternalPatientID}"/>
                                        <input type="hidden" name="intBiospecimenID"
                                            value="{$intBiospecimenID}"/>
                                        <input type="hidden" name="BIOSPECIMEN_strParentID"
                                            value="{$strBiospecimenID}"/>
                                        <input type="hidden" name="BIOSPECIMEN_intParentID"
                                            value="{$intBiospecimenID}"/>
                                        <input type="hidden" name="BIOSPECIMEN_intStudyKey"
                                            value="{$intBiospecStudyID}"/>
                                        <input type="hidden" name="action" value="biospecimen_study"/>
                                        <input type="hidden" name="module" value="core"/>
                                        <!--<input type="button" value="Analysis results" tabindex="83"
                                            class="uportal-button"
                                            onclick="javascript:jumpTo('{$smartformChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformChannelTabOrder}&amp;domain=Bioanalysis&amp;participant={$intBiospecimenID}&amp;intStudyID={$intBiospecStudyID}&amp;strcurrent=biospecimen_view&amp;intInternalPatientID={$intInternalPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;strOrigin=biospecimen_view&amp;title={$strBiospecimenID}&amp;var1={$strBiospecSampleTypeSelected}&amp;var2={$dtBiospecSampleDate}')"
                                        />-->
                                    </form>
                                </xsl:if>
                            
                                <xsl:variable name="intVialKey" select="VIAL_CALCULATION_intVialKey"/>
                                <xsl:if test="biospecimen_modules/vialCalculation='true'">
                                    <xsl:choose>
                                    <xsl:when test="$intVialKey  ='-1'">
                                        <form name="vial_cal_form" action="{$baseActionURL}?current=vial_calculation"
                                            method="POST">
                                            <input type="hidden" name="intInternalPatientID"
                                                value="{$intInternalPatientID}"/>
                                            <input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
                                                value="{$intBiospecimenID}"/>
                                            <input type="hidden" name="BIOSPECIMEN_strBiospecimenID"
                                                value="{$strBiospecimenID}"/>
                                            <input type="hidden"
                                                name="BIOSPECIMEN_strInitialBiospecSampleType"
                                                value="{$strInitialBiospecSampleType}"/>
                                            <input type="hidden" name="module"
                                                value="vial_calculation"/>
                                            <input type="hidden" name="action"
                                                value="add_vial_calculation"/>
                                            <input type="hidden" name="BIOSPECIMEN_dtSampleDate"
                                                value="{$BIOSPECIMEN_dtSampleDate}"/>
                                            <input type="hidden" name="BIOSPECIMEN_dtExtractedDate"
                                                value="{$BIOSPECIMEN_dtExtractedDate}"/>
                                            <!--<input type="submit" name="vial_calculation"
                                                value="Vial Calculation" tabindex="43"
                                                class="uportal-button"/>-->
                                         </form>       
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <form name="vial_cal_history_form"  action="{$baseActionURL}?" method="POST">
                                            <input type="hidden" name="intInternalPatientID"
                                                value="{$intInternalPatientID}"/>
                                            <input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
                                                value="{$intBiospecimenID}"/>
                                            <input type="hidden" name="BIOSPECIMEN_strBiospecimenID"
                                                value="{$strBiospecimenID}"/>
                                            <input type="hidden"
                                                name="BIOSPECIMEN_strInitialBiospecSampleType"
                                                value="{$strInitialBiospecSampleType}"/>
                                            <input type="hidden" name="module"
                                                value="vial_calculation"/>
                                            <input type="hidden"
                                                name="VIAL_CALCULATION_intVialCalculationKey"
                                                value="{$intVialKey}"/>
                                            <input type="hidden" name="action"
                                                value="vial_calculation_history"/>
                                            <!-- <input type="submit" name="vial_calculation_history"
                                                value="Vial Calculation History " tabindex="43"
                                                class="uportal-button"/> -->
                                        </form>
                                    </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:if>
                           
                                <xsl:if test="biospecimen_modules/studySmartforms='true'">
                                    <form name="study_form" action="{$baseActionURL}" method="POST">
                                        <input type="hidden" name="current"
                                            value="biospecimen_study"/>
                                        <input type="hidden" name="intInternalPatientID"
                                            value="{$intInternalPatientID}"/>
                                        <input type="hidden" name="intBiospecimenID"
                                            value="{$intBiospecimenID}"/>
                                        <input type="hidden" name="strBiospecParentID"
                                            value="{$strBiospecimenID}"/>
                                        <input type="hidden" name="intBiospecParentID"
                                            value="{$intBiospecimenID}"/>
                                        <input type="hidden" name="intStudyID"
                                            value="{$intBiospecStudyID}"/>
                                        <!-- <input type="button" value="Study results" tabindex="42"
                                            class="uportal-button"
                                            onclick="javascript:jumpTo('{$smartformChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformChannelTabOrder}&amp;domain=Biospecimen&amp;participant={$intBiospecimenID}&amp;intStudyID={$intBiospecStudyID}&amp;strcurrent=biospecimen_view&amp;intInternalPatientID={$intInternalPatientID}&amp;intBiospecimenID={$intBiospecimenID}&amp;strOrigin=biospecimen_view&amp;title={$strBiospecimenID}&amp;var1={$strBiospecSampleTypeSelected}&amp;var2={$dtBiospecSampleDate}')"
                                        /> -->
                                    </form>
                                </xsl:if>
                            
                                <form name="attachment_form" action="{$baseActionURL}?ATTACHMENTS_domainName=Biospecimen"
                                    method="POST">
                                    <input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
                                        value="{$intBiospecimenID}"/>
                                    <input type="hidden" name="intInternalPatientID"
                                        value="{$intInternalPatientID}"/>
                                    <input type="hidden" name="module" value="Attachments"/>
                                    <!-- <input type="submit" value="Attachments" tabindex="84"
                                        class="uportal-button"/> -->
                                </form>
                             
                            <xsl:if test="count(/biospecimen/AutoDocs) &gt; 0">
                             
                                <form name="report_form" action="{$baseActionURL}?strDomain=Bioanalysis"
                                    method="POST">
                                    <xsl:variable name="autodocsTitle"><xsl:value-of select="/biospecimen/AutoDocs"/></xsl:variable>
                                    <input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
                                        value="{$intBiospecimenID}"/>
                                    <input type="hidden" name="module" value="AutoDocs"/>
                                    <input type="hidden" name="action" value="prepare"/>
                                    <input type="hidden" name="StartOrder" value="1"/>
                                    <input type="hidden" name="EndOrder" value="3"/>
                                    <!-- <input type="submit" value="{$autodocsTitle}" tabindex="88"
                                        class="uportal-button"/> -->
                                </form>
                                 
                            </xsl:if>
                            
                            </td>
                         </tr>
                        </table>
                </td>
            </tr>
        </table>
        <!-- Move the cursor to the first date entry box as the page is loaded-->
        <script language="javascript"> document.biospecimen_form.BIOSPECIMEN_strComments.focus();
        </script>
    </xsl:template>
    <xsl:template match="strBiospecimenID"/>
    <xsl:template name="branch" match="branch">
        <xsl:variable name="varBiospecimenID">
            <xsl:value-of select="intBiospecimenID"/>
        </xsl:variable>
        <xsl:variable name="varBiospecSampleType">
            <xsl:value-of select="strBiospecSampleType"/>
        </xsl:variable>
        <xsl:variable name="highlightID">
            <xsl:value-of select="/biospecimen/strBiospecimenID"/>
        </xsl:variable>
        <xsl:variable name="intInternalPatientID">
            <xsl:value-of select="/biospecimen/patient_details/PATIENT_intInternalPatientID"/>
        </xsl:variable>
        <xsl:variable name="strBiospecimenID">
            <xsl:value-of select="strBiospecimenID"/>
        </xsl:variable>
        <span class="trigger">
            <table width="100%">
                <tr>
                    <td width="2%">
                        <img src="media/neuragenix/icons/open.gif">
                            <!--        <xsl:attribute name="id">I<xsl:value-of select="@id"/></xsl:attribute>
        <xsl:attribute name="onClick">
            showBranch('<xsl:value-of select="@id"/>');
        </xsl:attribute>-->
                        </img>
                    </td>
                    <td width="18%">
                        <a
                            href="{$baseActionURL}?current=biospecimen_view&amp;intBiospecimenID={$varBiospecimenID}&amp;strInitialBiospecSampleType={$varBiospecSampleType}&amp;intInternalPatientID={$intInternalPatientID}">
                            <xsl:value-of select="$strBiospecimenID"/>
                        </a>
                        <!--br/-->
                    </td>
                    <xsl:if test="$strBiospecimenID = $highlightID">
                        <td>
                            <img src="media/neuragenix/icons/current_node.gif"/>
                        </td>
                    </xsl:if>
                </tr>
            </table>
        </span>
        <span class="branch">
            <xsl:attribute name="id">
                <xsl:value-of select="@id"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </span>
    </xsl:template>
    <xsl:template match="leaf">
        <xsl:variable name="varBiospecimenID">
            <xsl:value-of select="intBiospecimenID"/>
        </xsl:variable>
        <xsl:variable name="varBiospecSampleType">
            <xsl:value-of select="strBiospecSampleType"/>
        </xsl:variable>
        <xsl:variable name="highlightID">
            <xsl:value-of select="/biospecimen/strBiospecimenID"/>
        </xsl:variable>
        <xsl:variable name="strBiospecimenID">
            <xsl:value-of select="strBiospecimenID"/>
        </xsl:variable>
        <xsl:variable name="intInternalPatientID">
            <xsl:value-of select="/biospecimen/patient_details/PATIENT_intInternalPatientID"/>
        </xsl:variable>
        <table width="100%">
            <tr>
                <td width="2%">
                    <img src="media/neuragenix/icons/bullet.gif"/>
                </td>
                <td width="18%">
                    <a
                        href="{$baseActionURL}?current=biospecimen_view&amp;intBiospecimenID={$varBiospecimenID}&amp;strInitialBiospecSampleType={$varBiospecSampleType}&amp;intInternalPatientID={$intInternalPatientID}">
                        <xsl:value-of select="strBiospecimenID"/>
                    </a>
                    <!--br/-->
                </td>
                <xsl:if test="$strBiospecimenID = $highlightID">
                    <td>
                        <img src="media/neuragenix/icons/current_node.gif"/>
                    </td>
                </xsl:if>
                <td width="25%"/>
            </tr>
        </table>
    </xsl:template>
    <xsl:template match="intBiospecimenID"/>
    <xsl:template match="intBiospecParentIDDisplay"/>
    <!--xsl:template match="(count(addpatientbutton)=1)"/-->
    <xsl:template match="addpatientbutton"/>
    <xsl:template match="currentPage"/>
    <xsl:template match="intInternalPatientID"/>
    <xsl:template match="intStudyID"/>
    <xsl:template match="strBiospecSampleType"/>
    <xsl:template match="strBiospecSampleSubType"/>
    <xsl:template match="intBiospecParentID"/>
    <xsl:template match="dtBiospecSampleDate"/>
    <xsl:template match="intBiospecNumCollected"/>
    <xsl:template match="intBiospecNumRemoved"/>
    <xsl:template match="PATIENT_Timestamp"/>
    <xsl:template match="strSurname"/>
    <xsl:template match="strPatientID"/>
    <xsl:template match="strFirstName"/>
    <xsl:template match="dtDob"/>
    <xsl:template match="strHospitalUR"/>
    <xsl:template match="intInternalPatientID"/>
    <xsl:template match="strSurnameDisplay"/>
    <xsl:template match="strPatientIDDisplay"/>
    <xsl:template match="strFirstNameDisplay"/>
    <xsl:template match="dtDobDisplay"/>
    <xsl:template match="strHospitalURDisplay"/>
    <xsl:template match="intInternalPatientIDDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_intBiospecimenIDDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_dtTransactionDateDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strTreatmentDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_flQuantityDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strReasonDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strUnitDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strOwnerDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strCollaboratorDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strRecordedByDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Day"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_intStudyKeyDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strStatusDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strFixationTimeDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strStudyDisplay"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_intBiospecimenID"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Year"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Month"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strTreatment_Selected"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strTreatment"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_flQuantity"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strReason"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strUnit_Selected"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strUnit"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strOwner"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strCollaborator_Selected"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strCollaborator"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strRecordedBy"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strStatus"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strStatus_Selected"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strFixationTime"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strStudy"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strStudy_Selected"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_intStudyKey"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_dtDeliveryDate"/>
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_intBioTransactionID"/>
    <xsl:template match="LOV_STUDY"/>
    <xsl:template match="strBiospecSampleTypeDisplay"/>
    <xsl:template match="strBiospecParentIDDisplay"/>
    <xsl:template match="dtBiospecDateDistributedDisplay"/>
    <xsl:template match="strBiospecOtherIDDisplay"/>
    <xsl:template match="strBiospecSpeciesDisplay"/>
    <xsl:template match="intInternalPatientIDDisplay"/>
    <xsl:template match="dtBiospecSampleDateDisplay"/>
    <xsl:template match="strBiospecimenIDDisplay"/>
    <xsl:template match="intBiospecStudyIDDisplay"/>
    <xsl:template match="strBiospecGradeDisplay"/>
    <xsl:template match="intBiospecimenIDDisplay"/>
    <xsl:template match="dtBiospecDateExtractedDisplay"/>
    <xsl:template match="intInvCellIDDisplay"/>
    <xsl:template match="strBiospecSampleTypeSelected"/>
    <xsl:template match="strBiospecSampleType"/>
    <xsl:template match="strBiospecParentID"/>
    <xsl:template match="dtBiospecDateDistributed"/>
    <xsl:template match="strBiospecOtherID"/>
    <xsl:template match="strBiospecSpeciesSelected"/>
    <xsl:template match="strBiospecSpecies"/>
    <xsl:template match="intInternalPatientID"/>
    <xsl:template match="dtBiospecSampleDate"/>
    <xsl:template match="intBiospecStudyID"/>
    <!-- Anita Start -->
    <xsl:template match="intBiospecStudyIDSelected"/>
    <!-- Anita End -->
    <xsl:template match="strBiospecGradeSelected"/>
    <xsl:template match="strBiospecGrade"/>
    <xsl:template match="dtBiospecDateExtracted"/>
    <xsl:template match="intInvCellID"/>
    <xsl:template match="BIOSPECIMEN_Timestamp"/>
    <xsl:template match="strDaySelected"/>
    <xsl:template match="strDay"/>
    <xsl:template match="strMonthSelected"/>
    <xsl:template match="strMonth"/>
    <xsl:template match="strLockError"/>
    <xsl:template match="strErrorMessage"/>
    <xsl:template match="BIOSPECIMEN_flNumberCollected"/>
    <xsl:template match="search_trans"/>
    <xsl:template match="search_user"/>
    <!-- Anita Start -->
    <xsl:template match="load_study"/>
    <xsl:template match="STUDY_intStudyID"/>
    <xsl:template match="STUDY_strStudyName"/>
    <xsl:template match="strStudyName"/>
    <xsl:template match="intStudyID"/>
    <xsl:template match="strStoredInDisplay"/>
    <xsl:template match="strStoredInSelected"/>
    <xsl:template match="strStoredIn"/>
    <xsl:template match="tmBiospecSampleTimeDisplay"/>
    <xsl:template match="tmBiospecSampleTime"/>
    <xsl:template match="tmBiospecSampleTime_Hour"/>
    <xsl:template match="tmBiospecSampleTime_Minute"/>
    <xsl:template match="tmBiospecSampleTime_AMPM"/>
    <xsl:template match="tmBiospecSampleTimeSelected"/>
    <xsl:template match="tmBiospecExtractedTimeDisplay"/>
    <xsl:template match="tmBiospecExtractedTime"/>
    <xsl:template match="tmBiospecExtractedTimeSelected"/>
    <xsl:template match="tmBiospecExtractedTime_Hour"/>
    <xsl:template match="tmBiospecExtractedTime_Minute"/>
    <xsl:template match="tmBiospecExtractedTime_AMPM"/>
    <xsl:template match="BIOSPECIMEN_tmBiospecSampleTime"/>
    <xsl:template match="BIOSPECIMEN_tmBiospecSampleTime_Hour"/>
    <xsl:template match="BIOSPECIMEN_tmBiospecSampleTime_Minute"/>
    <xsl:template match="BIOSPECIMEN_tmBiospecSampleTime_AMPM"/>
    <!-- Anita End -->
    <xsl:template match="strSource"/>
    <xsl:template match="strSiteName"/>
    <xsl:template match="strTankName"/>
    <xsl:template match="strBoxName"/>
    <xsl:template match="strTrayName"/>
    <xsl:template match="strColNo"/>
    <xsl:template match="strRowNo"/>
    <xsl:template match="intNewInvCellID"/>
    <xsl:template match="strInitialBiospecSampleType"/>
    <xsl:template match="strBiospecSampleSubTypeDisplay"/>
    <xsl:template match="strBiospecDescriptionDisplay"/>
    <xsl:template match="strBiospecSampleSubType"/>
    <xsl:template match="strBiospecDescription"/>
    <xsl:template match="strBiospecSampleSubTypeSelected"/>
    <xsl:template match="flag"/>
    <xsl:template match="intCellID"/>
    <xsl:template match="VIAL_CALCULATION_intVialCalculationKey"/>
    <xsl:template match="VIAL_DETAILS_intClinicalStatCalc"/>
    <xsl:template match="VIAL_DETAILS_flClinicalStatCalcMonths"/>
    <xsl:template match="VIAL_DETAILS_intManualCellCount"/>
    <xsl:template match="VIAL_DETAILS_intDilutionFactor"/>
    <xsl:template match="VIAL_DETAILS_intmLsSuspended"/>
    <xsl:template match="VIAL_DETAILS_intCellmL"/>
    <xsl:template match="VIAL_DETAILS_intTotalCellCount"/>
    <xsl:template match="VIAL_DETAILS_intmLsResuspended"/>
    <xsl:template match="VIAL_DETAILS_flmLsCollectionTube"/>
    <xsl:template match="VIAL_DETAILS_flmLsAnticosgulant"/>
    <xsl:template match="VIAL_DETAILS_intConcentration"/>
    <xsl:template match="VIAL_DETAILS_flTotalmLsBM"/>
    <xsl:template match="VIAL_DETAILS_intSecondLook"/>
    <xsl:template match="VIAL_DETAILS_strMRDRNA"/>
    <xsl:template match="VIAL_DETAILS_strMRDWash"/>
    <xsl:template match="VIAL_DETAILS_strMRDRNA_Selected"/>
    <xsl:template match="VIAL_DETAILS_strMRDWash_Selected"/>
    <xsl:template match="VIAL_DETAILS_intBiospecimenID"/>
    <xsl:template match="VIAL_DETAILS_strCollectionTube"/>
    <xsl:template match="VIAL_DETAILS_strCollectionTube_Selected"/>
    <xsl:template match="VIAL_DETAILS_intVialDetailKey"/>
    <xsl:template match="VIAL_DETAILS_intClinicalStatCalcDisplay"/>
    <xsl:template match="VIAL_DETAILS_flClinicalStatCalcMonthsDisplay"/>
    <xsl:template match="VIAL_DETAILS_intManualCellCountDisplay"/>
    <xsl:template match="VIAL_DETAILS_intDilutionFactorDisplay"/>
    <xsl:template match="VIAL_DETAILS_intmLsSuspendedDisplay"/>
    <xsl:template match="VIAL_DETAILS_intCellmLDisplay"/>
    <xsl:template match="VIAL_DETAILS_intTotalCellCountDisplay"/>
    <xsl:template match="VIAL_DETAILS_intmLsResuspendedDisplay"/>
    <xsl:template match="VIAL_DETAILS_flmLsCollectionTubeDisplay"/>
    <xsl:template match="VIAL_DETAILS_flmLsAnticosgulantDisplay"/>
    <xsl:template match="VIAL_DETAILS_intConcentrationDisplay"/>
    <xsl:template match="VIAL_DETAILS_flTotalmLsBMDisplay"/>
    <xsl:template match="VIAL_DETAILS_intSecondLookDisplay"/>
    <xsl:template match="VIAL_DETAILS_strMRDRNADisplay"/>
    <xsl:template match="VIAL_DETAILS_strMRDWashDisplay"/>
    <xsl:template match="VIAL_DETAILS_intBiospecimenIDDisplay"/>
    <xsl:template match="VIAL_DETAILS_strCollectionTubeDisplay"/>
    <xsl:template match="PATIENT_strPatientIDDisplay"/>
    <xsl:template match="PATIENT_strHospitalURDisplay"/>
    <xsl:template match="PATIENT_strSurnameDisplay"/>
    <xsl:template match="PATIENT_strFirstNameDisplay"/>
    <xsl:template match="PATIENT_dtDobDisplay"/>
    <xsl:template match="PATIENT_strPatientID"/>
    <xsl:template match="PATIENT_strHospitalUR"/>
    <xsl:template match="PATIENT_strSurname"/>
    <xsl:template match="PATIENT_strFirstName"/>
    <xsl:template match="PATIENT_dtDob"/>
    <xsl:template match="PATIENT_dtDob_Day"/>
    <xsl:template match="PATIENT_dtDob_Month"/>
    <xsl:template match="PATIENT_dtDob_Year"/>
    <xsl:template match="PATIENT_intInternalPatientID"/>
    <xsl:template match="strComments"/>
    <xsl:template match="strCommentsDisplay"/>
    <!--<xsl:template match="strEncounter"/>-->
    <xsl:template match="strSelectedEncounter"/>
    <!--<xsl:template match="strEncounterDisplay"/>-->
    <xsl:template match="search_encounter_list"/>
    <xsl:template match="strHour"/>
    <xsl:template match="strMinute"/>
    <xsl:template match="strTime"/>
    <xsl:template match="intFlagPatientID"/>
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
