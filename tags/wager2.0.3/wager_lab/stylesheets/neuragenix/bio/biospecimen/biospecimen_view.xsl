<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./javascript_code.xsl"/>
    <xsl:include href="./infopanel.xsl"/>
    <xsl:output method="html" indent="yes"/>
    <!-- <xsl:include href="./biospecimen_common.xsl" /> -->
    <xsl:param name="inventoryChannelURL">inventoryChannelURL_false</xsl:param>
    <xsl:param name="smartformChannelURL">smartformChannelURL_false</xsl:param>
    <xsl:variable name="funcpanelImagePath">media/neuragenix/funcpanel</xsl:variable>
   
    <xsl:variable name="spacerImagePath">media/neuragenix/infopanel/spacer.gif</xsl:variable>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:param name="smartformChannelTabOrder">smartformChannelTabOrder</xsl:param>
    <xsl:param name="inventoryChannelTabOrder"></xsl:param>
    <xsl:param name="patientChannelURL">patientChannelURL_false</xsl:param>
    <xsl:param name="patientChannelTabOrder">patientChannelTabOrder</xsl:param>
    <xsl:param name="biospecimenChannelTabOrder">biospecimenChannelTabOrder</xsl:param>
    <xsl:param name="strErrorMessage">strErrorMessage</xsl:param>
    <xsl:template match="/ErrorOnly">
        <xsl:value-of select="$strErrorMessage" />
    </xsl:template>
    
    <xsl:template name="biotree">
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
    </xsl:template>
    
    
    <xsl:template name="quantity_details">
        <xsl:param name="intBiospecimenID"/>
        <table width="100%" border="0" style="empty-cells:show;">
            <xsl:if test="count(search_trans) > 0">
            <tr>
                <td class="stripped_column_heading" >Date</td>
                <td class="stripped_column_heading" >Type</td>
                <td class="stripped_column_heading">Treatment</td>
                <td class="stripped_column_heading">Quantity</td>
                <td class="stripped_column_heading">Reason</td>
                <td class="stripped_column_heading">Recorded By</td>
                <td class="stripped_column_heading">Delivery Date</td> 
                <td class="stripped_column_heading"></td>
            </tr>
            </xsl:if>
            <xsl:if test="count(search_trans) = 0">
                There are no transactions for this biospecimen
                </xsl:if>
            <xsl:for-each select="search_trans">
                
                <xsl:sort select="substring(BIOSPECIMEN_TRANSACTIONS_dtTransactionDate,7,4)" order="descending"/> <!-- year  -->
                <xsl:sort select="substring(BIOSPECIMEN_TRANSACTIONS_dtTransactionDate,3,2)" order="descending"/> <!-- month -->
                <xsl:sort select="substring(BIOSPECIMEN_TRANSACTIONS_dtTransactionDate,1,2)" order="descending"/> <!-- day   -->
                <xsl:sort select="BIOSPECIMEN_TRANSACTIONS_intBioTransactionID" order="descending"/>
                <xsl:variable name="BIOSPECIMEN_TRANSACTIONS_intBioTransactionID">
                    <xsl:value-of
                        select="BIOSPECIMEN_TRANSACTIONS_intBioTransactionID"/>
                </xsl:variable>
                <xsl:variable name="quantityStatus">
                    <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strStatus"/>
                </xsl:variable>
                <tr>
                    <xsl:choose>
                                            <xsl:when test="position() mod 2 != 0">
                            <xsl:attribute name="class">stripped_light</xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="class">stripped_dark</xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>
                    
                    
                    <td><xsl:choose>
                        <xsl:when test="position() mod 2 != 0">
                            <xsl:attribute name="class">stripped_light</xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="class">stripped_dark</xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>
                        <xsl:value-of
                        select="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate"/></td>
                    <td><xsl:choose>
                        <xsl:when test="position() mod 2 != 0">
                            <xsl:attribute name="class">stripped_light</xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="class">stripped_dark</xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose> <xsl:value-of
                        select="BIOSPECIMEN_TRANSACTIONS_strType"/></td>
                    <td><xsl:choose>
                        <xsl:when test="position() mod 2 != 0">
                            <xsl:attribute name="class">stripped_light</xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="class">stripped_dark</xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>  <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strTreatment"/></td>
                    <td><xsl:choose>
                        <xsl:when test="position() mod 2 != 0">
                            <xsl:attribute name="class">stripped_light</xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="class">stripped_dark</xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>
                        <xsl:variable name="flQuantity"><xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_flQuantity"/></xsl:variable>
                        <xsl:choose>
                            <xsl:when test="$flQuantity &gt; 0">
                                <img src="media/neuragenix/icons/up.gif"/>&#160;
                                <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_flQuantity"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <img src="media/neuragenix/icons/down.gif"/>&#160;
                                <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_flQuantity * -1"/>
                            </xsl:otherwise>
                        </xsl:choose>
                                             <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strUnit"/></td>
                    <td><xsl:choose>
                        <xsl:when test="position() mod 2 != 0">
                            <xsl:attribute name="class">stripped_light</xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="class">stripped_dark</xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose><xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strReason"/></td>
                    
                    <td> <xsl:choose>
                        <xsl:when test="position() mod 2 != 0">
                            <xsl:attribute name="class">stripped_light</xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="class">stripped_dark</xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose> <xsl:value-of
                        select="BIOSPECIMEN_TRANSACTIONS_strRecordedBy"/></td>
                   
                    <td><xsl:choose>
                        <xsl:when test="position() mod 2 != 0">
                            <xsl:attribute name="class">stripped_light</xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="class">stripped_dark</xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose></td>
                    <td><xsl:choose>
                        <xsl:when test="position() mod 2 != 0">
                            <xsl:attribute name="class">stripped_light</xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="class">stripped_dark</xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>
                        <a href=
                            "{$baseActionURL}?module=core&amp;action=delete_quantity&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;BIOSPECIMEN_TRANSACTIONS_intBioTransactionID={$BIOSPECIMEN_TRANSACTIONS_intBioTransactionID}"
                        method="post">Delete</a>
                    
                        <xsl:if test="$quantityStatus='Allocated'">
                           <a href="{$baseActionURL}?module=core&amp;action=upgrade_quantity&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;BIOSPECIMEN_TRANSACTIONS_intBioTransactionID={$BIOSPECIMEN_TRANSACTIONS_intBioTransactionID}"
                                >Delivered</a>
                            
                        </xsl:if>
                        
                    </td>
                    
                </tr>
                
            </xsl:for-each>
        </table>
    </xsl:template>
    
    
    
    <xsl:template match="/biospecimen">
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
        <xsl:param name="strEncounter">
            <xsl:value-of select="BIOSPECIMEN_strEncounter"/>
        </xsl:param>
        <xsl:param name="intInvCellID">
            <xsl:value-of select="/biospecimen/inventory_info/CELL_intCellID"/>
        </xsl:param>
        <xsl:param name="strSource">biospecimen</xsl:param>
        <!-- XXX: agus - unlock sub biospecimen type -->
        <xsl:param name="strInitialBiospecSampleType">
            <xsl:value-of select="strInitialBiospecSampleType"/>
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
	<xsl:param name="BIOSPECIMEN_TRANSACTIONS_strConcentration">
	    <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strConcentration"/>
	</xsl:param>
        <xsl:param name="BIOSPECIMEN_TRANSACTIONS_strSampleSubType">
            <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strSampleSubType"/>
        </xsl:param>
	<xsl:param name="strPatientDOB">
	 <xsl:value-of  select="/biospecimen/patient_details/PATIENT_dtDob"/>
	</xsl:param>

        <xsl:param name="flDNAConc">
            <xsl:value-of select="BIOSPECIMEN_flDNAConc"/>
        </xsl:param>

        <xsl:param name="flPurity">
            <xsl:value-of select="BIOSPECIMEN_flPurity"/>
        </xsl:param>
	
        <xsl:variable name="BIOSPECIMEN_dtSampleDate" select="BIOSPECIMEN_dtSampleDate"/>
        <xsl:variable name="BIOSPECIMEN_dtExtractedDate" select="BIOSPECIMEN_dtExtractedDate"/>
        <xsl:call-template name="javascript_code"/>
        <link rel="stylesheet" type="text/css" href="stylesheets/neuragenix/bio/xmlsideTree.css"/>
        <script language="javascript">
	<!--window.onload = function()
{
                var toggle = document.biospecimen_form.BIOSPECIMEN_strSampleType;


var selectBox = document.biospecimen_form.BIOSPECIMEN_strSampleType;
                        user_input = selectBox.options[selectBox.selectedIndex].value
                                var tmp = document.getElementsByTagName('*');
                                for (var i=0;i &lt; tmp.length ; i++)
                                {
                                        if(tmp[i].className == 'uportal-label2' || tmp[i].className =='uportal-input-text2')
                                                {

                                                        if (user_input == "Serum")
                                                        {
                                                                tmp[i].style.visibility = 'hidden';
                                                        }
                                                        if (user_input == "DNA" || user_input == "RNA")
                                                        {
                                                                tmp[i].style.visibility = 'visible';
                                                        }


                                                }
                                }
				 if (user_input == "Serum")
                                                        {
                                                        	selectBox.options[0] = null;
							}
                                                        if (user_input == "DNA")
                                                        {
                                                                selectBox.options[1] = null;
                                                        }

}
-->
function archiveData() {
purity = document.biospecimen_form.BIOSPECIMEN_flPurity;
conc = document.biospecimen_form.BIOSPECIMEN_flDNAConc;
comments = document.biospecimen_form.BIOSPECIMEN_strComments;
commentsval = comments.value;
today = new Date();
commentsval = commentsval + '\n' + 'Data archived on ' + today.getDate() + '/' + (today.getMonth()+1) + '/' + today.getFullYear();
commentsval = commentsval + '\nPurity: ' + purity.value;
commentsval = commentsval + '\nConcentration :' + conc.value;
comments.value = commentsval;
}
	</script>
        


	<table width="100%">
            <tr valign="top">
                <td id="neuragenix-border-right"  class="uportal-channel-subtitle"> Menu<hr/>
                    <br/>
                    <a href="{$baseActionURL}?current=biospecimen_search">Search biospecimens</a>
                    <br/>
                    <xsl:if test="biospecimen_modules/quantityAllocations='true'">
                        <a
                            href="{$baseActionURL}?uP_root=root&amp;current=bp_allocate&amp;stage=BEGIN&amp;module=BATCH_ALLOCATE"
                            >Quantity allocation</a>
                    </xsl:if>
                    <br/>
                    <br/>
                    <table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="250">
                        <tr valign="bottom">
                            <td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_left.gif"/></td>
                            <td class="funcpanel_header_inactive" align="left" colspan="3" width="100%">HIERARCHY</td>
                            <td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_right.gif"/></td>
                        </tr>
                        <tr class="funcpanel_content">
                            <td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                        </tr>
                        <tr class="funcpanel_content">
                            <td class="funcpanel_left_border">&#160;</td>
                           <td colspan="3">
                            <xsl:call-template name="biotree"/>
                        </td>
                            <td class="funcpanel_right_border">&#160;</td>
                        </tr>
                        <tr class="funcpanel_content">
                            <td class="funcpanel_bottom_border" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                        </tr>
                    </table>
                         
                    <br/>
                    <br/>
                    <xsl:if test="count(/biospecimen/patient_details) &gt; 0">
                        <table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="250">
                            <tr valign="bottom">
                                <td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_left.gif"/></td>
                                <td class="funcpanel_header_inactive" align="left" colspan="3" width="100%">PATIENT DETAILS</td>
                                <td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_right.gif"/></td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_left_border">&#160;</td>
                                <td class="form_label"><xsl:value-of select="/biospecimen/patient_details/PATIENT_strPatientIDDisplay"/>&#160;:</td>
                                <td>&#160;</td>
                                <td><xsl:value-of select="/biospecimen/patient_details/PATIENT_strPatientID"/></td>
                                <td class="funcpanel_right_border">&#160;</td>
                            </tr>
                            <!-- Seena
                                <tr class="funcpanel_content">
                                <td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                                </tr>
                                <tr class="funcpanel_content">
                                <td class="funcpanel_left_border">&#160;</td>
                                <td class="form_label"><xsl:value-of select="/biospecimen/patient_details/PATIENT_strHospitalURDisplay"/>&#160;:</td>
                                <td>&#160;</td>
                                <td><xsl:value-of select="/biospecimen/patient_details/PATIENT_strHospitalUR"/></td>
                                <td class="funcpanel_right_border">&#160;</td>
                                </tr>
                            -->
                            
                            
                            <tr class="funcpanel_content">
                                <td class="funcpanel_left_border">&#160;</td>
                                <td class="form_label"><xsl:value-of select="/biospecimen/patient_details/PATIENT_strSurnameDisplay"/>&#160;:</td>
                                <td>&#160;</td>
                                <td><xsl:value-of select="/biospecimen/patient_details/PATIENT_strSurname"/></td>
                                <td class="funcpanel_right_border">&#160;</td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_left_border">&#160;</td>
                                <td class="form_label"><xsl:value-of select="/biospecimen/patient_details/PATIENT_strFirstNameDisplay"/>&#160;:</td>
                                <td>&#160;</td>
                                <td><xsl:value-of select="/biospecimen/patient_details/PATIENT_strFirstName"/></td>
                                <td class="funcpanel_right_border">&#160;</td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_left_border">&#160;</td>
                                <td class="form_label"><xsl:value-of select="/biospecimen/patient_details/PATIENT_dtDobDisplay"/>&#160;:</td>
                                <td>&#160;</td>
                                <td><xsl:value-of select="/biospecimen/patient_details/PATIENT_dtDob"/></td>
                                <td class="funcpanel_right_border">&#160;</td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_left_border">&#160;</td>
                                <td class="form_label">Consent Status&#160;:</td>
                                <td>&#160;</td>
                                <td><xsl:if test="/biospecimen/patient_details/PATIENT_strStatus[@selected=1]!='Consented'">
                                    <xsl:attribute name="class">withdrawn</xsl:attribute>
                                </xsl:if>
                                    <xsl:value-of select="/biospecimen/patient_details/PATIENT_strStatus[@selected=1]"/></td>
                                <td class="funcpanel_right_border">&#160;</td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_left_border">&#160;</td>
                                <td class="form_label" colspan="3"><xsl:variable name="patientID">
                                    <xsl:value-of
                                        select="/biospecimen/patient_details/PATIENT_intInternalPatientID"
                                    />
                                    </xsl:variable>
                                    <a
                                        href="{$patientChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$patientChannelTabOrder}&amp;action=view_patient&amp;PATIENT_intInternalPatientID={$patientID}"
                                        >View Patient Details</a></td>
                                <td class="funcpanel_right_border">&#160;</td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_bottom_border" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                            </tr>
                        </table>
                                           
                           
                    </xsl:if>
                    <!--
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
                    </xsl:if>-->
                    <!--                       <xsl:call-template name="branch"/> -->
                </td>
                <td width="5%"/>
                <!--                </tr>
            </table>-->
                <td width="100%">
                    <table width="100%">
                        <tr>
                            <td class="uportal-channel-subtitle"></td>
                            <td align="right">
                                <form name="back_form" action="{$baseActionURL}" method="POST">
                                    <!-- back to the parent biospecimen -->
                                    
                                    <input type="hidden" name="uP_sparam" value="activeTab"/>
                                    <input type="hidden" name="activeTab" value="{$biospecimenChannelTabOrder}"/>
                                    <input type="hidden" name="uP_root" value="root"/>
                                    <input type="hidden" name="module"
                                        value="biospecimen_search"/>
                                    <input type="hidden" name="action"
                                        value="redo_last_search"/>
                                    
                                </form>
                                
                                <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
                                    alt="Previous" onclick="javascript:document.back_form.submit();"/>
                                <img border="0" src="media/neuragenix/buttons/next_disabled.gif"
                                    alt="Next"/>
                            </td>
                        </tr>
                    </table>
                    <table  width="100%" cellpadding="0" cellspacing="0" border="0"   >
                        <tr><td colspan="3">
                            <xsl:call-template name="infopaneltop">
                                <xsl:with-param name="titleString"><xsl:value-of select="BIOSPECIMEN_strBiospecimenID"/></xsl:with-param>
                                
                            </xsl:call-template>
                        </td>
                        </tr> 
                        
                        
                        
                        <tr class="funcpanel_content">
                            <td class="funcpanel_left_border" width="1px">&#160;</td>
                            <td>
                    
                    <xsl:if test="not(bioOffSite)" >
                    <table width="100%">
                        <tr><td><hr/></td></tr>
                    </table>
                    <table width="100%">
                        <tr>
			
                            <td>
                                <form action="{$baseActionURL}" method="POST" name="topprocessform">
                                    <input type="hidden" name="PATIENT_intInternalPatientID"
                                        value="{$intInternalPatientID}"/>
                                    <input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
                                        value="{$intBiospecimenID}"/>
                                    <input type="hidden" name="BIOSPECIMEN_strParentID"
                                        value="{$strBiospecimenID}"/>
                                    <input type="hidden" name="BIOSPECIMEN_intParentID"
                                        value="{$intBiospecimenID}"/>
                                    <input type="hidden" name="module" value="core"/>
                                    <input type="hidden" name="action" value="add_biospecimen"/>
                                    <a class="button" href="#" onclick="this.blur(); document.forms.topprocessform.submit();"><span>Process</span></a>
                                    <!--<input type="submit" value="Process / Aliquot" tabindex="80"
                                    class="uportal-button"/>-->
                                    <a class="button" href="#" onclick="this.blur(); javascript:archiveData();"><span>Archive data</span></a>
				  <!-- <input type="button" onClick="javascript:archiveData()" value="Archive data to Comments" />-->
                                </form>
                            </td>
				
                            <td>
                                <!-- clone biospecimen -->
                                <form action="{$baseActionURL}" method="POST" name="topcloneform">
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
                                   <!-- <input type="submit" value="Clone" tabindex="81"
                                    class="uportal-button"/>-->
                                    <a class="button" href="#" onclick="this.blur(); document.forms.topcloneform.submit();"><span>Clone</span></a>
                                </form>
                            </td>
			

                        </tr>
                        <tr>
                            <td colspan="9">
                                <hr/>
                            </td>
                        </tr>
                    </table>
                    </xsl:if>
                    <form name="biospecimen_form"
                        action="{$baseActionURL}?current=biospecimen_view&amp;module=core"
                        method="post">
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
				<!-- Moved to bottom left corner 5/08/2005 CE -->
				<!--
                                <td class="neuragenix-form-required-text" width="50%"
                                    id="neuragenix-required-header" align="right"> * = Required
                                    fields </td>
				-->
				<td></td>
                            </tr>
                        </table>
                        <table width="100%">
                            <tr>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                    <xsl:if
                                        test="./BIOSPECIMEN_strBiospecimenIDDisplay[@required='true']"
                                        >  </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strBiospecimenIDDisplay"/>: </td>
                                <td class="uportal-label" id="neuragenix-form-row-input-input">
                                    <xsl:value-of select="BIOSPECIMEN_strBiospecimenID"/>
                                    
                                        <input type="hidden" name="BIOSPECIMEN_strBiospecimenID"
                                            value="{$strBiospecimenID}"/>
                                </td>
                                <td width="5%"/>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                   
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    Volume: </td>
                                <xsl:variable name="qty_collected">
                                    <xsl:choose>
                                        <xsl:when test="number(BIOSPECIMEN_flNumberCollected)">
                                            <xsl:value-of select="number(BIOSPECIMEN_flNumberCollected)" />
                                        </xsl:when>
                                        <xsl:otherwise>0</xsl:otherwise>
                                    </xsl:choose>
                                </xsl:variable>
                                <xsl:variable name="qty_removed">
                                    <xsl:choose>
                                        <xsl:when test="number(BIOSPECIMEN_flNumberRemoved)">
                                            <xsl:value-of select="number(BIOSPECIMEN_flNumberRemoved)" />
                                        </xsl:when>
                                        <xsl:otherwise>0</xsl:otherwise>
                                    </xsl:choose>
                                </xsl:variable>
                                
                                <td width="26%" class="uportal-label"
                                    id="neuragenix-form-row-input-input">
                                    <xsl:value-of select="format-number($qty_collected+$qty_removed,'#.0000')"/>
                                    
                                </td>
                                <td width="5%" id="neuragenix-end-spacer"/>
                            </tr>

				<!-- Commented out second row CE 11/10/2005
                            <tr>
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
                                    document.biospecimen_form.action.value =
                                    'refresh_view_biospecimen'; document.biospecimen_form.submit();
                                    } </script>
                                document.biospecimen_form.action.value = 'refresh_allocation_search';
                                document.biospecimen_form.submit();
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
                                </td>
                                <td width="5%" id="neuragenix-end-spacer"/>
                            </tr>
				-->
                            <tr>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
				
                                    <xsl:if test="./BIOSPECIMEN_strSampleTypeDisplay[@required='true']"
                                        >  </xsl:if>
				
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
				<!--
                                    <xsl:value-of select="BIOSPECIMEN_strSpeciesDisplay"/>:
					<xsl:value-of select="BIOSPECIMEN_strSampleSubTypeDisplay"/>:
				-->
				<xsl:value-of select="BIOSPECIMEN_strSampleTypeDisplay"/>
				<input type="hidden" value="{./BIOSPECIMEN_strSampleType[@selected=1]}" name="BIOSPECIMEN_strSampleType" />
				</td>
                                <td width="26%" id="neuragenix-form-row-input" class="uportal-label">
				<!--
                                    <select name="BIOSPECIMEN_strSampleSubType" tabindex="22"
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_strSampleSubType">
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
				-->
				<xsl:value-of select="BIOSPECIMEN_strSampleType[@selected=1]"/>
                                                <!--<xsl:if test="@selected=1">
                                                 <xsl:value-of select="."/>
						</xsl:if>
                                        </xsl:for-each>
				<xsl:value-of select="BIOSPECIMEN_strSampleType"/>-->
                                </td>
                                <td width="5%"/>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                   
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:if
                                        test="number(BIOSPECIMEN_flDNAConc)"> 
                                        Quantity (ug): </xsl:if> </td>
                                <td width="26%" id="neuragenix-form-row-input" class="uportal-label">
                                    <xsl:variable name="qty_collected">
                                        <xsl:choose>
                                            <xsl:when test="number(BIOSPECIMEN_flNumberCollected)">
                                                <xsl:value-of select="number(BIOSPECIMEN_flNumberCollected)" />
                                            </xsl:when>
                                            <xsl:otherwise>0</xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:variable>
                                    <xsl:variable name="qty_removed">
                                        <xsl:choose>
                                            <xsl:when test="number(BIOSPECIMEN_flNumberRemoved)">
                                                <xsl:value-of select="number(BIOSPECIMEN_flNumberRemoved)" />
                                            </xsl:when>
                                            <xsl:otherwise>0</xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:variable>
                                   <xsl:variable name="quantitytotal">
                                    <xsl:value-of select="format-number($qty_collected+$qty_removed,'#.0000')"/>
                                       </xsl:variable>
                                    <xsl:if test="number(BIOSPECIMEN_flDNAConc) and number($quantitytotal)"> 
                                        <xsl:value-of select="format-number($quantitytotal*BIOSPECIMEN_flDNAConc,'#.00')"/>
                                        
                                        </xsl:if>
                                    
                                </td>
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
                                  <!--  <xsl:choose>
                                        <xsl:when test="bioOffSite">-->
                                            <xsl:value-of select="study_list_biospecimen[@selected=1]/STUDY_strStudyName"/>
                                <!--        </xsl:when>
                                        <xsl:otherwise>
                                    <select class="uportal-input-text"
                                        name="BIOSPECIMEN_intStudyKey" tabindex="23">
                                        <xsl:variable name="BIOSPECIMEN_intStudyID">
                                            <xsl:value-of select="intBiospecStudyID"/>
                                        </xsl:variable>
                                        <xsl:for-each select="study_list_biospecimen">
                                            <xsl:variable name="varIntStudyID">
                                                <xsl:value-of select="STUDY_intStudyID"/>
                                            </xsl:variable>
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="STUDY_intStudyID"/>
                                                </xsl:attribute>
                                                <xsl:if test="$intBiospecStudyID = $varIntStudyID">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="STUDY_strStudyName"/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                    </xsl:otherwise>
                                    </xsl:choose>-->
                                    
                                </td>
                               
                            </tr>
                           
                            <!-- -->
                            <tr>
                                <td colspan="8">
                                    <hr/>
                                </td>
                            </tr>
                            <tr>
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
                                        
                                        <xsl:when test="bioOffSite">
                                            <xsl:value-of select="BIOSPECIMEN_dtSampleDate"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            
                                            
                                            <select name="BIOSPECIMEN_dtSampleDate_Day" tabindex="28"
                                                class="uportal-input-text">
                                                <xsl:for-each select="BIOSPECIMEN_dtSampleDate_Day">
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
                                            <select name="BIOSPECIMEN_dtSampleDate_Month" tabindex="29"
                                                class="uportal-input-text">
                                                <xsl:for-each select="BIOSPECIMEN_dtSampleDate_Month">
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
                                            <input type="text" name="BIOSPECIMEN_dtSampleDate_Year" size="5"
                                                tabindex="30" class="uportal-input-text">
                                                <xsl:attribute name="value">
                                                    <xsl:value-of select="BIOSPECIMEN_dtSampleDate_Year"/>
                                                </xsl:attribute>
                                            </input>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                               
                                <td width="5%"/>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                    <xsl:if
                                        test="./BIOSPECIMEN_strSampleTimeDisplay[@required='true']">
                                        * </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_tmSampleTimeDisplay"/>: </td>
                                <td width="25%" class="uportal-label">
                                    <xsl:choose>
                                        <xsl:when test="bioOffSite">
                                            <xsl:value-of select="BIOSPECIMEN_tmSampleTime"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <select name="BIOSPECIMEN_tmSampleTime_Hour" tabindex="31"
                                                class="uportal-input-text">
                                                <xsl:for-each select="BIOSPECIMEN_tmSampleTime_Hour">
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
                                            <select name="BIOSPECIMEN_tmSampleTime_Minute" tabindex="32"
                                                class="uportal-input-text">
                                                <xsl:for-each select="BIOSPECIMEN_tmSampleTime_Minute">
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
                                            <select name="BIOSPECIMEN_tmSampleTime_AMPM" tabindex="33"
                                                class="uportal-input-text">
                                                <xsl:for-each select="BIOSPECIMEN_tmSampleTime_AMPM">
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
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                                <td width="5%" id="neuragenix-end-spacer"/>
                                
                            </tr>
                            
                            
                            
                            <tr>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                    <xsl:if test="./BIOSPECIMEN_strGradeDisplay[@required='true']">
                                        * </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strGradeDisplay"/>: </td>
                                <td width="26%" id="neuragenix-form-row-input" class="uportal-label">
                                    <select name="BIOSPECIMEN_strGrade" tabindex="24"
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_strGrade">
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
                                        test="./BIOSPECIMEN_dtExtractedDateDisplay[@required='true']"
                                        > * </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_dtExtractedDateDisplay"/>: </td>
                                <td width="25%" class="uportal-label">
                                    <select name="BIOSPECIMEN_dtExtractedDate_Day" tabindex="34"
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_dtExtractedDate_Day">
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
                                    <select name="BIOSPECIMEN_dtExtractedDate_Month" tabindex="35"
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_dtExtractedDate_Month">
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
                                    <input type="text" name="BIOSPECIMEN_dtExtractedDate_Year"
                                        size="5" tabindex="36" class="uportal-input-text">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="BIOSPECIMEN_dtExtractedDate_Year"
                                            />
                                        </xsl:attribute>
                                    </input>
                                </td>
                                <td width="5%" id="neuragenix-end-spacer"/>
                            </tr>
                            <tr>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">
                                    <xsl:if
                                        test="./BIOSPECIMEN_strStoredInDisplay[@required='true']"> *
                                    </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strStoredInDisplay"/>: </td>
                                <td width="26%" id="neuragenix-form-row-input" class="uportal-label">
                                    <select name="BIOSPECIMEN_strStoredIn" tabindex="25"
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_strStoredIn">
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
                                        test="./BIOSPECIMEN_tmExtractedDisplay[@required='true']"> *
                                    </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_tmExtractedTimeDisplay"/>:
                                </td>
                                <td width="25%" class="uportal-label">
                                    <select name="BIOSPECIMEN_tmExtractedTime_Hour" tabindex="37"
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_tmExtractedTime_Hour">
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
                                    <select name="BIOSPECIMEN_tmExtractedTime_Minute" tabindex="38"
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_tmExtractedTime_Minute">
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
                                    <select name="BIOSPECIMEN_tmExtractedTime_AMPM" tabindex="39"
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_tmExtractedTime_AMPM">
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
                                <td width="5%" id="neuragenix-end-spacer"/>
                            </tr>
                            <tr>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text" valign="top">
                                    <xsl:if
                                        test="./BIOSPECIMEN_strEncounterDisplay[@required='true']">
                                        * </xsl:if>
                                </td>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label" valign="center">
                                    <xsl:value-of select="BIOSPECIMEN_strEncounterDisplay"/>: </td>
                                <td width="26%" id="neuragenix-form-row-input" class="uportal-label"
                                    valign="top">
                                    <select name="BIOSPECIMEN_strEncounter" tabindex="26"
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
                                <td width="5%" valign="top"/>
                                <td width="1%" id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text" valign="center"/>
                                <td width="18%" id="neuragenix-form-row-input-label"
                                    class="uportal-label" valign="center"> Flagged: </td>
                                <td width="25%" valign="center">
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
                                <td width="5%" id="neuragenix-end-spacer" valign="top"/>
                            </tr>
                            <tr>
                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text" valign="top"/>
                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label" valign="top">
				<xsl:if test="./BIOSPECIMEN_strSampleType[@selected=1]='DNA'">
                                        DNA Concentration <xsl:text>(ng/&#956;L):</xsl:text>
                                </xsl:if>
				<xsl:if test="./BIOSPECIMEN_strSampleType[@selected=1]='RNA'">
                                        RNA Concentration <xsl:text>(ng/&#956;L):</xsl:text>
                                </xsl:if>
				</td>
                                <td width="26%" id="neuragenix-form-row-input" class="uportal-label" valign="top">
				   <xsl:if test="./BIOSPECIMEN_strSampleType[@selected=1]='DNA'">
                                        <input type="text" name="BIOSPECIMEN_flDNAConc" value="{$flDNAConc}"/>
				   </xsl:if>
				   <xsl:if test="./BIOSPECIMEN_strSampleType[@selected=1]='RNA'">
                                        <input type="text" name="BIOSPECIMEN_flDNAConc" value="{$flDNAConc}"/>
				   </xsl:if>
				</td>

				<td width="5%" valign="top"/>	
                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text" valign="top"/>
                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label" valign="top">
				<xsl:if test="./BIOSPECIMEN_strSampleType[@selected=1]='DNA'">
                                    <xsl:value-of select="BIOSPECIMEN_flPurityDisplay"/>: 
				</xsl:if>
				<xsl:if test="./BIOSPECIMEN_strSampleType[@selected=1]='RNA'">
                                    <xsl:value-of select="BIOSPECIMEN_flPurityDisplay"/>: 
				</xsl:if>
				</td>
                                <td width="25%" valign="top">
				 <xsl:if test="./BIOSPECIMEN_strSampleType[@selected=1]='DNA'">
					 <input type="text" name="BIOSPECIMEN_flPurity" value="{$flPurity}"/>
				</xsl:if>
				 <xsl:if test="./BIOSPECIMEN_strSampleType[@selected=1]='RNA'">
					 <input type="text" name="BIOSPECIMEN_flPurity" value="{$flPurity}"/>
				</xsl:if>
                                </td>
                                <td width="5%" id="neuragenix-end-spacer" valign="top"/>
				</tr>
				<!-- strComments section -->
				<tr>
				<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text" valign="top"/>
                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label" valign="top">
                                    <xsl:value-of select="BIOSPECIMEN_strQualityDisplay"/>: </td>
                                <td width="25%" valign="top">
                                    <select name="BIOSPECIMEN_strQuality" 
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_strQuality">
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
                                <td width="5%" id="neuragenix-end-spacer" valign="top"/>
				<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text" valign="top"/>
                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label" valign="top">
				
				<xsl:if test="./BIOSPECIMEN_strSampleType[@selected=1]='DNA'">
                                    <xsl:value-of select="BIOSPECIMEN_strProtocolDisplay"/>:
				</xsl:if> </td>
                                <td width="25%" valign="top">
					
				<xsl:if test="./BIOSPECIMEN_strSampleType[@selected=1]='DNA'">
                                    <select name="BIOSPECIMEN_strProtocol" 
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_strProtocol">
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
				</xsl:if>
                                </td>

				</tr>	
                                <tr>
				<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text" valign="top"/>
                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label" valign="top">
                                    <xsl:value-of select="BIOSPECIMEN_strAnticoagDisplay"/>: </td>
                                <td width="25%" valign="top">
                                    <select name="BIOSPECIMEN_strAnticoag" 
                                        class="uportal-input-text">
					<xsl:for-each select="BIOSPECIMEN_strAnticoag">
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
                                <td width="5%" id="neuragenix-end-spacer" valign="top"/>

				
                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text" valign="top"/>
                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label" valign="top">
                                    <xsl:value-of select="BIOSPECIMEN_strCommentsDisplay"/>: </td>
                                <td width="25%" valign="top">
                                    <textarea name="BIOSPECIMEN_strComments" rows="4" cols="40"
                                        tabindex="41" class="uportal-input-text">
                                        <xsl:value-of select="BIOSPECIMEN_strComments"/>
                                    </textarea>
                                </td>
                                <td width="5%" id="neuragenix-end-spacer" valign="top"/>
                            </tr>
                            <!-- subtype table go here - - >
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
                                        < ! - - select box for subtype - - >
                                        <select name="BIOSPECIMEN_strSampleSubType" tabindex="27"
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
                            < ! - - subtype table end here -->
			    <tr>
				<td class="neuragenix-form-required-text" width="100%"
                                    id="neuragenix-required-header" align="left" colspan="8"> * = Required fields
				</td>
			    </tr>
                            <xsl:if test="not(bioOffSite)">
                            <tr>
                                <td align="left" colspan="4">
                                    <a class="button" href="#" onclick="this.blur(); javascript:confirmDelete('{$baseActionURL}?current=biospecimen_view&amp;module=core&amp;action=delete_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;target=biospecimen');"><span><img src="/wagerlab/media/neuragenix/icons/delete.png" height="14" align="top" border="0"/> Delete</span></a>
                                   <!-- <input type="button" name="delete" value="Delete" tabindex="44"
                                        class="uportal-button"
                                        onclick="javascript:confirmDelete('{$baseActionURL}?current=biospecimen_view&amp;module=core&amp;action=delete_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;target=biospecimen')"
                                    />-->
                                </td>
                                <td align="right" colspan="4">
                                    <table align="right">
				<xsl:variable name="strSampleType">
<xsl:value-of select="BIOSPECIMEN_strSampleType[@selected=1]"/>
				</xsl:variable>
                                    <tr>
                                    <td>
                                    
                                        <xsl:choose>
                                            <xsl:when test="$intBiospecStudyID=17">
                                                <xsl:choose>
                                                    <xsl:when test="$strSampleType='Frozen lymphocytes (F) '">
                                                        
                                                        <a class="button" href="#" onclick="this.blur(); document.location.href='/wagerlab/WAFSSLNBarcode.prn?barcode={$strBiospecimenID}&amp;dateOfSample={$BIOSPECIMEN_dtSampleDate}&amp;patientKey={$intInternalPatientID}'"><span><img src="/wagerlab/media/neuragenix/icons/printer.png" height="14" align="top" border="0"/> Print Barcode</span></a>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        
                                                        <a class="button" href="#" onclick="this.blur(); document.location.href='/wagerlab/WAFSSBioBarcode.prn?barcode={$strBiospecimenID}&amp;dob={$strPatientDOB}'"><span><img src="/wagerlab/media/neuragenix/icons/printer.png" height="14" align="top" border="0"/> Print Barcode</span></a>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </xsl:when>
                                            <xsl:when test="$intBiospecStudyID=65">
                                                <a class="button" href="#" onclick="this.blur(); document.location.href='/wagerlab/BDSBarcode.prn?barcode={$strBiospecimenID}&amp;patientkey={$intInternalPatientID}'"><span><img src="/wagerlab/media/neuragenix/icons/printer.png" height="14" align="top" border="0"/> Print Barcode</span></a>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <a class="button" href="#" onclick="this.blur(); document.location.href='/wagerlab/DefaultBarcode.prn?barcode={$strBiospecimenID}&amp;dob={$strPatientDOB}'"><span><img src="/wagerlab/media/neuragenix/icons/printer.png" height="14" align="top" border="0"/> Print Barcode</span></a>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                        
                                        </td>
                                  
                              <td>
                                    <a class="button" href="#" onclick="this.blur(); javascript:submitBiospecimenForm();"><span><img src="/wagerlab/media/neuragenix/icons/disk.png" height="14" align="top" border="0"/> Save</span></a>
                                   </td></tr>
                                    </table>
                                </td>
                            </tr>
                            </xsl:if>
                            <tr>
                                <td colspan="8">
                                    <hr/>
                                </td>
                            </tr>
                        </table>
                    </form>
                    
                    <!-- end of biospecimen form -->
                    <xsl:if test="contains($showInventory,'true')">
                        <table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="100%">
                            <tr valign="bottom">
                                <td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_left.gif"/></td>
                                <td class="funcpanel_header_inactive" align="left" colspan="3" width="100%">LOCATION </td>
                                <td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_right.gif"/></td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_left_border">&#160;</td>
                                <td colspan="3">
                        <table width="100%">
                            
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
                                    Box: </td>
                                <td class="uportal-text">
                                    <xsl:value-of select="inventory_info/TRAY_strTrayName"/>
                                </td>
                                <td id="neuragenix-end-spacer"/>
                            </tr>
                            <tr>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    Freezer: </td>
                                <td class="uportal-text">
                                    <xsl:value-of select="inventory_info/TANK_strTankName"/>
                                </td>
                                <td width="5%"/>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    Column: </td>
                                <td class="uportal-text">
                                    <xsl:value-of select="inventory_info/TRAY_intColumnNumber"/>
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
                                    <xsl:value-of select="inventory_info/TRAY_intRowNumber"/>
                                </td>
                                <td id="neuragenix-end-spacer"/>
                            </tr>
                            <tr>
                                <td colspan="6"/>
                                <td colspan="2" class="uportal-text">
                                    <xsl:choose>
                                        <xsl:when test="inventory_info/SITE_strSiteName = 'N/A'">
                                            <form
                                                action="{$inventoryChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$inventoryChannelTabOrder}&amp;source=allocate&amp;intInternalPatientID={$intInternalPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;strOrigin=view_biospecimen"
                                                method="post" name="allocateform">
                                                <a class="button" href="#" onclick="this.blur(); document.forms.allocateform.submit();"><span>Allocate</span></a>
                                               <!-- <input type="submit" class="uportal-button"
                                                  value="Allocate"/>-->
                                            </form>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <table align="right">
                                                <tr>
                                                  <td>
                                                      <a class="button" href="#" onclick="this.blur(); document.location.href='{$inventoryChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$inventoryChannelTabOrder}&amp;current=view_tray&amp;source=view&amp;intInternalPatientID={$intInternalPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;CELL_intCellID={$intInvCellID}&amp;strOrigin=view_biospecimen'"><span>View</span></a>
                                                  </td>
                                                    <xsl:if test="inventory_info/canChangeUnallocInv = '1'">
                                                    <td>
                                                        <a class="button" href="#" onclick="this.blur(); document.location.href='{$inventoryChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$inventoryChannelTabOrder}&amp;current=view_tray&amp;source=change&amp;PATIENT_intInternalPatientID={$intInternalPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;CELL_intCellID={$intInvCellID}&amp;strOrigin=view_biospecimen'"><span>Change</span></a>
                                                    </td>
                                                    <td>
                                                        <a class="button" href="#" onclick="this.blur(); document.location.href='{$baseActionURL}?current=view_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;intInvCellID={$intInvCellID}&amp;module=ALLOCATE_CELL&amp;action=unallocate'"><span>Unallocate</span></a>
                                                    </td>
                                                    </xsl:if>
                                                <!--  <form method="post"
                                                  action="{$inventoryChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$inventoryChannelTabOrder}&amp;current=view_tray&amp;source=view&amp;intInternalPatientID={$intInternalPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;CELL_intCellID={$intInvCellID}&amp;strOrigin=view_biospecimen">
                                                  <input type="submit" value="View"
                                                  class="uportal-button"/>
                                                  </form>
                                                  </td>
                                                   
                                                  <td>
                                                  <form method="post"
                                                  action="{$inventoryChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$inventoryChannelTabOrder}&amp;current=view_tray&amp;source=change&amp;PATIENT_intInternalPatientID={$intInternalPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}&amp;CELL_intCellID={$intInvCellID}&amp;strOrigin=view_biospecimen">
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
                                                        </xsl:if>-->
                                                </tr>
                                            </table>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>
                            
                        </table>
                                </td>
                                <td class="funcpanel_right_border">&#160;</td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_bottom_border" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                            </tr>
                        </table>    
                                    
                    </xsl:if>
                    <xsl:if test="contains($showTransactions, 'true')">
                        <!-- transaction stuff -->
                        <br/>
                        <table class="funcpanel" cellpadding="0" cellspacing="0" border="0">
                            <tr valign="bottom">
                                <td><img src="{$funcpanelImagePath}/funcpanel_header_active_left.gif"/></td>
                                <td class="funcpanel_header_active" align="left" colspan="3" width="100%">TRANSACTION HISTORY</td>
                                <td><img src="{$funcpanelImagePath}/funcpanel_header_active_right.gif"/></td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_left_border">&#160;</td>
                                <td colspan="3">
                                    <xsl:call-template name="quantity_details">
                                        <xsl:with-param name="intBiospecimenID"><xsl:value-of select="$intBiospecimenID"/></xsl:with-param>
                                    </xsl:call-template>
                              

                                        <br/>
                                </td>
                                <td class="funcpanel_right_border">&#160;</td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_bottom_border" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                            </tr>
                        </table>
                                                            <xsl:if test="not(bioOffSite)">
                                        <br/><br/>
                        <table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="100%">
                            <tr valign="bottom">
                                <td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_left.gif"/></td>
                                <td class="funcpanel_header_inactive" align="left" colspan="3" width="100%">ADD TRANSACTION</td>
                                <td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_right.gif"/></td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_left_border">&#160;</td>
                                <td colspan="3">
                        
                        
                     
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
                                        <xsl:value-of select="format-number(BIOSPECIMEN_flNumberCollected,'#.0000')"/>
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
					<select name="BIOSPECIMEN_TRANSACTIONS_strUnit"
                                            tabindex="55" class="uportal-input-text">
                                            <xsl:for-each select="BIOSPECIMEN_TRANSACTIONS_strUnit">
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
                                            test="BIOSPECIMEN_TRANSACTIONS_strReasonDisplay[@required='true']"
                                            >*</xsl:if>
                                    </td>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_strReasonDisplay"/>: </td>
                                    <td width="26%" class="uportal-label"
                                        id="neuragenix-form-row-input-input">
					<!-- Changed to text box, not area 5/08/2005 CE
                                        <textarea name="BIOSPECIMEN_TRANSACTIONS_strReason" rows="4"
                                            cols="25" class="uportal-input-text" tabindex="63">
                                            <xsl:value-of select="strBiospecDescription"/>
                                        </textarea>
					-->
					<input tabindex="63" name="BIOSPECIMEN_TRANSACTIONS_strReason" class="uportal-input-text"/>	
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
                                                  select="BIOSPECIMEN_intStudyKey"/>
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
					<!--
                                        <xsl:if
                                            test="BIOSPECIMEN_TRANSACTIONS_strFixationTimeDisplay[@required='true']"
                                            >*</xsl:if>
					-->
                                    </td>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label">
					<!--
                                        <xsl:value-of
                                            select="BIOSPECIMEN_TRANSACTIONS_strFixationTimeDisplay"
                                        />:
					--> 
				    </td>
                                    <td width="26%" class="uportal-label"
                                        id="neuragenix-form-row-input-input">
					<!--
                                        <input tabindex="58"
                                            name="BIOSPECIMEN_TRANSACTIONS_strFixationTime"
                                            class="uportal-input-text"/>
					-->
                                    </td>
                                    <td width="5%"/>
				    	<td width="1%" id="neuragenix-form-row-input-label-required"
                                        	class="neuragenix-form-required-text"/>
				   	<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label2">
                                        <!--	<xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strConcentrationDisplay"/>: -->
				   	</td>
                                    	<td width="26%" class="uportal-text2" id="neuragenix-form-row-input-input">
			<!--				        	<input tabindex="65"
                                            		name="BIOSPECIMEN_TRANSACTIONS_strConcentration"
                                            		class="uportal-input-text2"/>-->

				    	</td>
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
                                    <td width="80%" class="uportal-label"/>
                                    <td width="20%" class="uportal-label">
                                        <a class="button" href="#" onclick="this.blur(); document.forms.quantity_form.submit();"><span>Save Quantity</span></a>
                                      <!--  <input type="submit" name="save_quantity"
                                            value="Save Quantity" tabindex="65"
                                            class="uportal-button"
                                            onblur="javascript:document.quantity_form.BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Day.focus()"
                                        />-->
                                    </td>
                                </tr>
                            </table>
                            <input type="hidden" name="BIOSPECIMEN_TRANSACTIONS_strStype" value="Manual"/>
                        </form></td>
                                    <td class="funcpanel_right_border">&#160;</td>
                            </tr>
                            <tr class="funcpanel_content">
                                <td class="funcpanel_bottom_border" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                            </tr>
                        </table>
                                    </xsl:if>
        </xsl:if>
             
                    
		    <!-- Not required 5/08/2005 CE  -->
		    <!-- Start of bottom button bar -->
                        <xsl:if test="not(bioOffSite)">
                            <hr/>
                    <table width="100%">
                        <tr>
				<!-- Not required 5/08/2005 CE  -->
			
                            <td>
                                <form action="{$baseActionURL}" method="POST" name="processform">
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
                                    <input type="hidden" name="module" value="core"/>
                                    <input type="hidden" name="action" value="add_biospecimen"/>
                                    <a class="button" href="#" onclick="this.blur(); document.forms.processform.submit();"><span>Process</span></a>
                                   <!-- <input type="submit" value="Process / Aliquot" tabindex="80"
                                        class="uportal-button"/>-->
                                </form>
                            </td>
				
                            <td>
                                <!-- clone biospecimen -->
                                <form action="{$baseActionURL}" method="POST" name ="cloneform">
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
                                    <a class="button" href="#" onclick="this.blur(); document.forms.cloneform.submit();"><span>Clone</span></a>
                                  <!--  <input type="submit" value="Clone" tabindex="81"
                                        class="uportal-button"/>-->
                                </form>
                            </td>
			    
			
                        </tr>
                    </table>
                        </xsl:if>
                </td>	<td class="funcpanel_right_border" width="1px">&#160;</td>
            </tr>
	    
	    <tr><td colspan="3">
	        
	        <xsl:call-template name="infopanelbottom">
	            
	            
	        </xsl:call-template>
	    </td>
	    </tr>
	</table>
		    <!-- End of bottom button bar -->
                </td>
            </tr>
        </table>
        <!-- Move the cursor to the first date entry box as the page is loaded-->
                    
             
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
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strConcentrationDisplay"/>
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
    <xsl:template match="BIOSPECIMEN_TRANSACTIONS_strConcentration"/>
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
    <xsl:template match="strEncounter"/>
    <xsl:template match="strSelectedEncounter"/>
    <xsl:template match="strEncounterDisplay"/>
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
