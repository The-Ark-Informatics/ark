<?xml version="1.0" encoding="UTF-8"?>
<!--
    Document   : patient_admissions.xsl
    Created on : May 13, 2004, 3:12 PM
    Author     : renny
    Description:Adding admissions of a patient.
    
	Version: $Id: admissions.xsl,v 1.9 2005/04/08 07:00:40 kleong Exp $
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./patient_menu.xsl"/>
	<xsl:include href="./infopanel.xsl"/>
	<xsl:include href="../../common/common_btn_name.xsl"/>
	<xsl:output method="html" indent="no"/>
	<!-- Start Secure Download Implementation -->
	<!--   <xsl:param name="downloadURL">downloadURL_false</xsl:param-->
	<xsl:param name="formParams">
		current=patient_admissions&amp;ADMISSIONS_intPatientID=<xsl:value-of select="PATIENT_intInternalPatientID"/>
	</xsl:param>
            
	<!-- Defined Image File Paths -->
	<xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
	<!-- Get Patient ID -->
	<!--xsl:param name="nodeId">nodeId_false</xsl:param-->
	<xsl:template match="patient">
		<!-- Call Infopanel Template which will Call "infopanel_content" Template -->
		<xsl:call-template name="infopanel">
			<xsl:with-param name="activeSubtab">collection</xsl:with-param>
			<xsl:with-param name="PATIENT_intInternalPatientID">
				<xsl:value-of select="PATIENT_intInternalPatientID"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="infopanel_content">
        
        <script language="javascript">
        function editAdmissions(aURL)
        {
            window.location=aURL;
        }
        </script>        
        
		<xsl:variable name="PATIENT_intInternalPatientID">
			<xsl:value-of select="PATIENT_intInternalPatientID"/>
		</xsl:variable>
		<table width="100%">
			<tr>
				<td class="neuragenix-form-required-text" width="100%">
					<xsl:value-of select="error"/>
				</td>
			</tr>
			<tr>
				<td class="uportal-channel-subtitle">
			Add a History<br/>
				</td>
			</tr>
		</table>
                
		
                <xsl:variable name='ADMISSIONS_intAdmissionkey'><xsl:value-of select="ADMISSIONS_intAdmissionkey"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strOffTreat'><xsl:value-of select="ADMISSIONS_strOffTreat"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strRelapseSite'><xsl:value-of select="ADMISSIONS_strRelapseSite"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strAdmissionID'><xsl:value-of select="ADMISSIONS_strAdmissionID"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_intPatientID'><xsl:value-of select="ADMISSIONS_intPatientID"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_dtAdmission'><xsl:value-of select="ADMISSIONS_dtAdmission"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_dtRelapse'><xsl:value-of select="ADMISSIONS_dtRelapse"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strHospital'><xsl:value-of select="ADMISSIONS_strHospital"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strRefDoctor'><xsl:value-of select="ADMISSIONS_strRefDoctor"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_dtDiagnosis'><xsl:value-of select="ADMISSIONS_dtDiagnosis"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strDiagCategory'><xsl:value-of select="ADMISSIONS_strDiagCategory"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strPrimarySite'><xsl:value-of select="ADMISSIONS_strPrimarySite"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strStage'><xsl:value-of select="ADMISSIONS_strStage"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strRisk'><xsl:value-of select="ADMISSIONS_strRisk"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strClincStatus'><xsl:value-of select="ADMISSIONS_strClincStatus"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strUnderlyingCond'><xsl:value-of select="ADMISSIONS_strUnderlyingCond"/></xsl:variable>        
                
                <xsl:variable name='ADMISSIONS_dtEvent'><xsl:value-of select="ADMISSIONS_dtEvent"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strEvent'><xsl:value-of select="ADMISSIONS_strEvent"/></xsl:variable>
                <xsl:variable name="editAdmissionSelected"><xsl:value-of select="editAdmissionSelected"/></xsl:variable>
                
                <form name="collection_view" action="{$baseActionURL}?uP_root=root&amp;action=admissions" method="post">
                

			<table width="100%">
                                <!-- row 1:   strAdmissionID,   ADMISSIONS_strStage -->
                                <tr>
                                    <td width="1%" class="neuragenix-form-required-text">*</td>
                                    <td width="19%" class="uportal-label" align='left'>
                                        <xsl:value-of select="ADMISSIONS_strAdmissionIDDisplay" />:
                                    </td>
                                    <td width="25%">
                                        <xsl:choose>
                                        <xsl:when test="string-length($editAdmissionSelected) &gt; 0">
                                            <span class="uportal-text"><xsl:value-of select="ADMISSIONS_strAdmissionID"/></span>                                            
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <input type="hidden" name="ADMISSIONS_strAdmissionID" value="0" />
                                            <span class="uportal-text">System Generated</span>
                                        </xsl:otherwise>
                                        </xsl:choose>
                                    </td>
                                    <td width="10%"></td>
                                    <td width="1%" class="neuragenix-form-required-text"/>
                                    <td width="19%" class="uportal-label" valign="top">
                                        <xsl:value-of select="ADMISSIONS_strStageDisplay" />:
                                    </td> 
                                    <td width="25%">
                                        <select name="ADMISSIONS_strStage" tabindex="48" class="uportal-input-text">
                                            <xsl:for-each select="ADMISSIONS_strStage">

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
                                
                                <!-- row 2:   dtAdmission,  ADMISSIONS_strOffTreat  -->
                                <tr>
                                    <!--Variables declaration-->
                                    <xsl:variable name="dtAdmission_Year"><xsl:value-of select="ADMISSIONS_dtAdmission_Year"/></xsl:variable>
                                    <xsl:variable name="dtAdmission_Day"><xsl:value-of select="ADMISSIONS_dtAdmission_Day"/></xsl:variable>
                                    <xsl:variable name="dtAdmission_Month"><xsl:value-of select="ADMISSIONS_dtAdmission_Month"/></xsl:variable>
                                    <td width="1%" class="neuragenix-form-required-text"/>              
                                    <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_dtAdmissionDisplay" />:
                                    </td>
                                    <td width="25%">
                                        <!--configurable date input -->
                                        <xsl:choose>
                                                <xsl:when test="ADMISSIONS_dtAdmission/@display_type='dropdown'">
                                                        <select name="ADMISSIONS_dtAdmission_Day" tabindex="22" class="uportal-input-text">
                                                                <xsl:for-each select="ADMISSIONS_dtAdmission_Day">
                                                                        <option>
                                                                                <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                                <xsl:if test="@selected=1">
                                                                                        <xsl:attribute name="selected">true</xsl:attribute>
                                                                                </xsl:if>
                                                                                <xsl:value-of select="."/>
                                                                        </option>
                                                                </xsl:for-each>
                                                        </select>
                                                        <select name="ADMISSIONS_dtAdmission_Month" tabindex="24" class="uportal-input-text">
                                                                <xsl:for-each select="ADMISSIONS_dtAdmission_Month">
                                                                        <option>
                                                                                <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                                <xsl:if test="@selected=1">
                                                                                        <xsl:attribute name="selected">true</xsl:attribute>
                                                                                </xsl:if>
                                                                                <xsl:value-of select="."/>
                                                                        </option>
                                                                </xsl:for-each>
                                                        </select>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                        <input type="text" name="ADMISSIONS_dtAdmission_Day" value="{$dtAdmission_Day}" size="2" tabindex="22" class="uportal-input-text"/>
                                                        <input type="text" name="ADMISSIONS_dtAdmission_Month" value="{$dtAdmission_Month}" size="2" tabindex="24" class="uportal-input-text"/>
                                                </xsl:otherwise>
                                        </xsl:choose>
                                        <!--end of configurable date input box -->
                                        <input type="text" name="ADMISSIONS_dtAdmission_Year" value="{$dtAdmission_Year}" size="4" tabindex="26" class="uportal-input-text"/>
                                    </td>
                                     <td width="10%"></td>
                                     <td width="1%" class="neuragenix-form-required-text"></td>
                                    <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_strOffTreatDisplay" />:
                                    </td>
                                    <td width="25%">
                                        <input type="text" name="ADMISSIONS_strOffTreat"  value="{$ADMISSIONS_strOffTreat}" size="30" tabindex="50" class="uportal-input-text" />
                                    </td>
                                </tr>
                             
                                <!-- row 3:   ADMISSIONS_strEvent,  ADMISSIONS_dtRelapse  -->
                                <tr>
                                    <td width="1%" class="neuragenix-form-required-text"/>              
                                    <td width="19%" class="uportal-label">
                                            <xsl:value-of select="ADMISSIONS_strEventDisplay"/>:</td>
                                    <td width="25%">
                                            <select name="ADMISSIONS_strEvent" tabindex="28" class="uportal-input-text">
                                                    <xsl:for-each select="ADMISSIONS_strEvent">
                                                            <option>
                                                                    <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                    <xsl:if test="@selected=1">
                                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                                    </xsl:if>
                                                                    <xsl:value-of select="."/>
                                                            </option>
                                                    </xsl:for-each>
                                            </select>
                                    </td>
                                    <td width="10%"></td>
                                    <td width="1%" class="neuragenix-form-required-text"></td>
                                    <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_dtRelapseDisplay" />:
                                    </td>
                                    <td width="25%">
                                        <!--Variables declaration-->
                                        <xsl:variable name="dtRelapse_Year"><xsl:value-of select="ADMISSIONS_dtRelapse_Year"/></xsl:variable>
                                        <xsl:variable name="dtRelapse_Day"><xsl:value-of select="ADMISSIONS_dtRelapse_Day"/></xsl:variable>
                                        <xsl:variable name="dtRelapse_Month"><xsl:value-of select="ADMISSIONS_dtRelapse_Month"/></xsl:variable>
                                       <!--configurable date input -->
                                        <xsl:choose>
                                                <xsl:when test="ADMISSIONS_dtRelapse/@display_type='dropdown'">
                                                        <select name="ADMISSIONS_dtRelapse_Day" tabindex="52" class="uportal-input-text">
                                                                <xsl:for-each select="ADMISSIONS_dtRelapse_Day">
                                                                        <option>
                                                                                <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                                <xsl:if test="@selected=1">
                                                                                        <xsl:attribute name="selected">true</xsl:attribute>
                                                                                </xsl:if>
                                                                                <xsl:value-of select="."/>
                                                                        </option>
                                                                </xsl:for-each>
                                                        </select>
                                                        <select name="ADMISSIONS_dtRelapse_Month" tabindex="54" class="uportal-input-text">
                                                                <xsl:for-each select="ADMISSIONS_dtRelapse_Month">
                                                                        <option>
                                                                                <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                                <xsl:if test="@selected=1">
                                                                                        <xsl:attribute name="selected">true</xsl:attribute>
                                                                                </xsl:if>
                                                                                <xsl:value-of select="."/>
                                                                        </option>
                                                                </xsl:for-each>
                                                        </select>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                        <input type="text" name="ADMISSIONS_dtRelapse_Day" value="{$dtRelapse_Day}" size="2" tabindex="52" class="uportal-input-text"/>
                                                        <input type="text" name="ADMISSIONS_dtRelapse_Month" value="{$dtRelapse_Month}" size="2" tabindex="54" class="uportal-input-text"/>
                                                </xsl:otherwise>
                                        </xsl:choose>
                                        <!--end of configurable date input box -->
                                        <input type="text" name="ADMISSIONS_dtRelapse_Year" value="{$dtRelapse_Year}" size="4" tabindex="56" class="uportal-input-text"/>
                                    </td>
                                 </tr>

                                                                         
                                <!-- row 4:   ADMISSIONS_dtEvent   ADMISSIONS_strRelapseSite  -->
                                <tr>
                                    <td width="1%" class="neuragenix-form-required-text"/>
                                    <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_dtEventDisplay" />:
                                    </td>
                                    <td width="25%">                                    
                                        <!--Variables declaration-->
                                        <xsl:variable name="dtEvent_Year"><xsl:value-of select="ADMISSIONS_dtEvent_Year"/></xsl:variable>
                                        <xsl:variable name="dtEvent_Day"><xsl:value-of select="ADMISSIONS_dtEvent_Day"/></xsl:variable>
                                        <xsl:variable name="dtEvent_Month"><xsl:value-of select="ADMISSIONS_dtEvent_Month"/></xsl:variable>
                                       <!--configurable date input -->
                                        <xsl:choose>
                                                <xsl:when test="ADMISSIONS_dtEvent/@display_type='dropdown'">
                                                        <select name="ADMISSIONS_dtEvent_Day" tabindex="30" class="uportal-input-text">
                                                                <xsl:for-each select="ADMISSIONS_dtEvent_Day">
                                                                        <option>
                                                                                <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                                <xsl:if test="@selected=1">
                                                                                        <xsl:attribute name="selected">true</xsl:attribute>
                                                                                </xsl:if>
                                                                                <xsl:value-of select="."/>
                                                                        </option>
                                                                </xsl:for-each>
                                                        </select>
                                                        <select name="ADMISSIONS_dtEvent_Month" tabindex="32" class="uportal-input-text">
                                                                <xsl:for-each select="ADMISSIONS_dtEvent_Month">
                                                                        <option>
                                                                                <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                                <xsl:if test="@selected=1">
                                                                                        <xsl:attribute name="selected">true</xsl:attribute>
                                                                                </xsl:if>
                                                                                <xsl:value-of select="."/>
                                                                        </option>
                                                                </xsl:for-each>
                                                        </select>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                        <input type="text" name="ADMISSIONS_dtEvent_Day" value="{$dtEvent_Day}" size="2" tabindex="30" class="uportal-input-text"/>
                                                        <input type="text" name="ADMISSIONS_dtEvent_Month" value="{$dtEvent_Month}" size="2" tabindex="32" class="uportal-input-text"/>
                                                </xsl:otherwise>
                                        </xsl:choose>
                                        <!--end of configurable date input box -->
                                        <input type="text" name="ADMISSIONS_dtEvent_Year" value="{$dtEvent_Year}" size="4" tabindex="34" class="uportal-input-text"/>
                                    </td>
                                    <td width="10%"></td>
                                    <td width="1%" class="neuragenix-form-required-text"></td>
                                    <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_strRelapseSiteDisplay" />:
                                    </td>
                                    <td width="25%">
                                        <input type="text" name="ADMISSIONS_strRelapseSite"  value="{$ADMISSIONS_strRelapseSite}" size="30" tabindex="58" class="uportal-input-text" />
                                    </td>
                                </tr>
                                
                                <!-- row 5:  ADMISSIONS_strHospital   ADMISSIONS_strUnderlyingCond -->
                                <tr>
                                    <td width="1%" class="neuragenix-form-required-text"/>              
                                    <td width="19%" class="uportal-label">
                                            <xsl:value-of select="ADMISSIONS_strHospitalDisplay"/>:</td>
                                    <td width="25%">
                                            <select name="ADMISSIONS_strHospital" tabindex="36" class="uportal-input-text">
                                                    <xsl:for-each select="ADMISSIONS_strHospital">
                                                            <option>
                                                                    <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                    <xsl:if test="@selected=1">
                                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                                    </xsl:if>
                                                                    <xsl:value-of select="."/>
                                                            </option>
                                                    </xsl:for-each>
                                            </select>
                                    </td>
                                    <td width="10%"></td>
                                    <td width="1%" class="neuragenix-form-required-text"></td>
                                    <td width="19%" class="uportal-label" valign="top">
                                        <xsl:value-of select="ADMISSIONS_strUnderlyingCondDisplay" />:
                                    </td> 
                                    <td width="25%">
                                        <input type="text" name="ADMISSIONS_strUnderlyingCond"  value="{$ADMISSIONS_strUnderlyingCond}" size="30" tabindex="60" class="uportal-input-text" />
                                    </td>
                                </tr>
                                                                       
                                                                         
                                <!-- row 6:  ADMISSIONS_strRefDoctor   ADMISSIONS_strRisk -->
                                <tr>
                                
                                    <td width="1%" class="neuragenix-form-required-text"/>                                                               
                                    <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_strRefDoctorDisplay" />:
                                    </td>
                                    <td width="25%">
                                            <select name="ADMISSIONS_strRefDoctor" tabindex="36" class="uportal-input-text">
                                                    <xsl:for-each select="ADMISSIONS_strRefDoctor">
                                                            <option>
                                                                    <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                    <xsl:if test="@selected=1">
                                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                                    </xsl:if>
                                                                    <xsl:value-of select="."/>
                                                            </option>
                                                    </xsl:for-each>
                                            </select>
                                    </td>
                                    <td width="10%"></td>
                                    <td width="1%" class="neuragenix-form-required-text"></td>
                                    <td width="19%" class="uportal-label" valign="top">
                                            <xsl:value-of select="ADMISSIONS_strRiskDisplay" />:
                                    </td>
                                    <td width="25%" valign="top">
                                        <input type="text" name="ADMISSIONS_strRisk" value="{$ADMISSIONS_strRisk}"  size="30" tabindex="62" class="uportal-input-text" />
                                    </td>
                                </tr>
                                
                                <!-- row 7:  ADMISSIONS_dtDiagnosis  ADMISSIONS_strPrimarySite  -->
                                <tr>
                                
                                    <!--Variables declaration-->
                                    <xsl:variable name="dtDiagnosis_Year"><xsl:value-of select="ADMISSIONS_dtDiagnosis_Year"/></xsl:variable>
                                    <xsl:variable name="dtDiagnosis_Day"><xsl:value-of select="ADMISSIONS_dtDiagnosis_Day"/></xsl:variable>
                                    <xsl:variable name="dtDiagnosis_Month"><xsl:value-of select="ADMISSIONS_dtDiagnosis_Month"/></xsl:variable>
                                    <td width="1%" class="neuragenix-form-required-text"/>              
                                    <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_dtDiagnosisDisplay" />:
                                    </td>
                                    <td width="25%">
                                        <!--configurable date input -->
                                        <xsl:choose>
                                                <xsl:when test="ADMISSIONS_dtDiagnosis/@display_type='dropdown'">
                                                        <select name="ADMISSIONS_dtDiagnosis_Day" tabindex="40" class="uportal-input-text">
                                                                <xsl:for-each select="ADMISSIONS_dtDiagnosis_Day">
                                                                        <option>
                                                                                <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                                <xsl:if test="@selected=1">
                                                                                        <xsl:attribute name="selected">true</xsl:attribute>
                                                                                </xsl:if>
                                                                                <xsl:value-of select="."/>
                                                                        </option>
                                                                </xsl:for-each>
                                                        </select>
                                                        <select name="ADMISSIONS_dtDiagnosis_Month" tabindex="42" class="uportal-input-text">
                                                                <xsl:for-each select="ADMISSIONS_dtDiagnosis_Month">
                                                                        <option>
                                                                                <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                                <xsl:if test="@selected=1">
                                                                                        <xsl:attribute name="selected">true</xsl:attribute>
                                                                                </xsl:if>
                                                                                <xsl:value-of select="."/>
                                                                        </option>
                                                                </xsl:for-each>
                                                        </select>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                        <input type="text" name="ADMISSIONS_dtDiagnosis_Day" value="{$dtDiagnosis_Day}" size="2" tabindex="40" class="uportal-input-text"/>
                                                        <input type="text" name="ADMISSIONS_dtDiagnosis_Month" value="{$dtDiagnosis_Month}" size="2" tabindex="42" class="uportal-input-text"/>
                                                </xsl:otherwise>
                                        </xsl:choose>
                                        <!--end of configurable date input box -->
                                        <input type="text" name="ADMISSIONS_dtDiagnosis_Year" value="{$dtDiagnosis_Year}" size="4" tabindex="44" class="uportal-input-text"/>
                                    </td>
                                    <td width="10%"></td>
                                    <td width="1%" class="neuragenix-form-required-text"></td>
                                    <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_strPrimarySiteDisplay" />:
                                    </td>
                                    <td width="25%">
                                        <input type="text" name="ADMISSIONS_strPrimarySite" value="{$ADMISSIONS_strPrimarySite}" size="30" tabindex="64" class="uportal-input-text" />
                                    </td>
                                </tr>

                                <!-- row 8 strDiagnosisCategory, strComments -->
                                <tr>
                                    <td width="1%" class="neuragenix-form-required-text"/>              
                                    <td width="19%" class="uportal-label" valign="top">
                                        <xsl:value-of select="ADMISSIONS_strDiagCategoryDisplay" />:
                                    </td> 
                                    <td width="25%">
                                        <select class="uportal-button" multiple="true" size="3" width="22" name="allDiagnosis">
                                            <xsl:for-each select="diagnosis">
                                                <xsl:variable name="DIAGNOSIS_intDiagnosisKey"><xsl:value-of select="DIAGNOSIS_intDiagnosisKey"/></xsl:variable>
                                                <xsl:variable name="DIAGNOSIS_strDiagnosisName"><xsl:value-of select="DIAGNOSIS_strDiagnosisName"/></xsl:variable>
                                                <xsl:variable name="DIAGNOSIS_strEPCCCode"><xsl:value-of select="DIAGNOSIS_strEPCCCode"/></xsl:variable>
                                                <xsl:variable name="DIAGNOSIS_intParentKey"><xsl:value-of select="DIAGNOSIS_intParentKey"/></xsl:variable>
                                                <xsl:variable name="DIAGNOSIS_ADMISSIONS_intDiagnosisAdmissionsKey"><xsl:value-of select="DIAGNOSIS_ADMISSIONS_intDiagnosisAdmissionsKey"/></xsl:variable>

                                                <option value="{$DIAGNOSIS_strDiagnosisName}">
                                                    <!-- xsl:value-of select="DIAGNOSIS_strEPCCCode"/>&#160;&#160;-->
                                                    <xsl:value-of select="DIAGNOSIS_strDiagnosisName"/>
                                                </option>
                                            </xsl:for-each>
                                        </select><br/>

                                       <a tabindex="46" href="javascript:submitToDiagnosis('{$baseActionURL}?PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;ADMISSIONS_intAdmissionkey={$ADMISSIONS_intAdmissionkey}&amp;ADMISSIONS_strAdmissionID={$ADMISSIONS_strAdmissionID}&amp;editAdmissionSelected={$editAdmissionSelected}')">
                                           Select diagnosis 
                                       </a> 
                                    </td>
                                    <td width="10%"></td>
                                    <td width="1%" class="neuragenix-form-required-text"></td>
                                    <td width="19%" class="uportal-label" valign="top">
                                                <xsl:value-of select="ADMISSIONS_strCommentsDisplay"/>:
                                    </td>
                                    <td width="25%" valign="top">
                                                <textarea class="uportal-input-text" cols="22" rows="3" tabindex="66" name="ADMISSIONS_strComments">
                                                    <xsl:value-of select="ADMISSIONS_strComments"/>
                                                </textarea>
                                    </td>
                                </tr>

                                <!-- row 8 empty -->
				<tr>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="19%" class="uportal-label" valign="top"></td>
					<td width="25%" valign="top"></td>
					<td width="10%"/>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="19%" class="uportal-label" valign="top"></td>
					<td width="25%"></td>
				</tr>
                                
                                <!-- row 9 btnClear, btnAdd -->
				<tr>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="19%" class="uportal-label">
                                        <xsl:choose>
                                        <xsl:when test="string-length($editAdmissionSelected) &gt; 0">
                                            <input type="button"  tabindex="72" name="cancel" value="Cancel" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?uP_root=root&amp;action=admissions&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}'"/>                        
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <input type="button"  tabindex="72" name="clear" value="Clear" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?uP_root=root&amp;action=admissions&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}'"/>                        
                                        </xsl:otherwise>
                                        </xsl:choose>
                                            
					</td>
					<td width="25%"></td>
					<td width="10%"/>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="19%" class="uportal-label"></td>
					<td width="25%" align="left">
						<input type="hidden" name="PATIENT_intInternalPatientID" value="{$PATIENT_intInternalPatientID}"/>
						<input type="hidden" name="ADMISSIONS_intPatientID" value="{$PATIENT_intInternalPatientID}"/>
                                                <input type="hidden" name="ADMISSIONS_intAdmissionkey" value="{$ADMISSIONS_intAdmissionkey}"/>
						<xsl:choose>
                                                <xsl:when test="string-length($editAdmissionSelected) &gt; 0">
                                                    <input type="submit" name="updateAdmission" tabindex="70" value="Update" class="uportal-button" onclick="javascript:selectAllDiagnosis()" />                                                
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <input type="submit" name="addAdmission" tabindex="70" value="Add" class="uportal-button" onclick="javascript:selectAllDiagnosis()" />
                                                </xsl:otherwise>
                                                </xsl:choose>
<!--                                                onblur="javascript:document.collection_view.ADMISSIONS_dtDiagnosis_Day.focus()"/-->
					</td>
				</tr>
			</table>
		</form>

		<xsl:if test="count(admission) &gt; 0">
			<table width="100%">
				<tr><td class="uportal-channel-subtitle"><hr/></td></tr>
				<tr><td class="uportal-channel-subtitle">History Details<br/></td></tr>
			</table>
			<xsl:for-each select="admission">
				<form action="{$baseActionURL}?uP_root=root&amp;{$formParams}" method="post">
					<xsl:variable name="strAdmissionID">
						<xsl:value-of select="ADMISSIONS_strAdmissionID"/>
					</xsl:variable>
					<xsl:variable name="ADMISSIONS_intAdmissionkey">
						<xsl:value-of select="ADMISSIONS_intAdmissionkey"/>
					</xsl:variable>
					<table width="100%">
                                                <!-- row 1:  ADMISSIONS_strAdmissionID   strOffTreat -->
						<tr>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_strAdmissionIDDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input" align="right">
								<xsl:value-of select="ADMISSIONS_strAdmissionID"/>
							</td>
							<td width="5%"/>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                                <xsl:value-of select="../ADMISSIONS_strStageDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
                                                                <xsl:value-of select="ADMISSIONS_strStage"/>
                                                        </td>
							<td width="5%" id="neuragenix-end-spacer"/>
						</tr>
                                                <!-- row 2:  ADMISSIONS_dtAdmission   ADMISSIONS_strOffTreat -->
						<tr>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_dtAdmissionDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input" align="right">
								<xsl:value-of select="ADMISSIONS_dtAdmission"/>
							</td>
							<td width="5%"/>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                        <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_strOffTreatDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input" align="right">
								<xsl:value-of select="ADMISSIONS_strOffTreat"/>
							</td>
							<td width="5%" id="neuragenix-end-spacer"/>
						</tr>
                                                <!-- row 3:   ADMISSIONS_strEvent   ADMISSIONS_dtRelapse -->
						<tr>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_strEventDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input" align="right">
								<xsl:value-of select="ADMISSIONS_strEvent"/>
							</td>
							<td width="5%"/>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_dtRelapseDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input" align="right">
								<xsl:value-of select="ADMISSIONS_dtRelapse"/>
							</td>
                                                </tr>
                                                <!-- row 4:   ADMISSIONS_dtEvent   ADMISSIONS_strRelapseSite -->
						<tr>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_dtEventDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input" align="right">
								<xsl:value-of select="ADMISSIONS_dtEvent"/>
							</td>
							<td width="5%"/>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_strRelapseSiteDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input" align="right">
								<xsl:value-of select="ADMISSIONS_strRelapseSite"/>
							</td>
							<td width="5%" id="neuragenix-end-spacer"/>
						</tr>
                                                <!-- row 5:   ADMISSIONS_strHospital   ADMISSIONS_strUnderlyingCond -->
						<tr>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_strHospitalDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
								<xsl:value-of select="ADMISSIONS_strHospital"/>
							</td>
							<td width="5%"/>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_strUnderlyingCondDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
								<xsl:value-of select="ADMISSIONS_strUnderlyingCond"/>
							</td>
							<td width="5%" id="neuragenix-end-spacer"/>
						</tr>
                                                <!-- row 6:   ADMISSIONS_strRefDoctor   ADMISSIONS_strRisk -->
						<tr>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_strRefDoctorDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
								<xsl:value-of select="ADMISSIONS_strRefDoctor"/>
							</td>
							<td width="5%"/>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_strRiskDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
								<xsl:value-of select="ADMISSIONS_strRisk"/>
							</td>
							<td width="5%" id="neuragenix-end-spacer"/>
						</tr>
                                                <!-- row 7:   ADMISSIONS_dtDiagnosis   ADMISSIONS_strPrimarySite -->
						<tr>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_dtDiagnosisDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
								<xsl:value-of select="ADMISSIONS_dtDiagnosis"/>
							</td>
							<td width="5%"/>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_strPrimarySiteDisplay"/>:
                                                        </td>
							<td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
								<xsl:value-of select="ADMISSIONS_strPrimarySite"/>
							</td>
							<td width="5%" id="neuragenix-end-spacer"/>
						</tr>
					</table>
                                        <table width="100%">
                                                <!-- row 8:   ADMISSIONS_strDiagCategory -->
						<tr>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_strDiagCategoryDisplay"/>:
                                                        </td>
							<td width="76%" class="uportal-input-text" id="neuragenix-form-row-input-input">
								<xsl:value-of select="ADMISSIONS_strDiagCategory"/>
							</td>
							<td width="5%" id="neuragenix-end-spacer"/>
						</tr>
                                                <!-- row 9:  ADMISSIONS_strComments -->
						<tr>
							<td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
							<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
								<xsl:value-of select="../ADMISSIONS_strCommentsDisplay"/>:
                                                        </td>
							<td width="76%" class="uportal-input-text" id="neuragenix-form-row-input-input">
								<xsl:value-of select="ADMISSIONS_strComments"/>
							</td>
							<td width="5%" id="neuragenix-end-spacer"/>
						</tr>
                                                <!-- row 10:  empty -->
                                        </table>
                                        <table width="100%">
						<tr>
							<td width="1%"/>
							<td width="18%">
								<input type="hidden" name="PATIENT_intInternalPatientID" value="{$PATIENT_intInternalPatientID}"/>
								<input type="hidden" name="ADMISSIONS_strAdmissionID" value="{$ADMISSIONS_strAdmissionID}"/>
								<input type="button" name="deleteAdmission" value="Delete" class="uportal-button" onclick="javascript:confirmDelete('{$baseActionURL}?action=admissions&amp;deleteAdmission=true&amp;ADMISSIONS_intAdmissionkey={$ADMISSIONS_intAdmissionkey}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}')"/>
							</td>
							<td width="26%"/>
							<td width="5%"/>
							<td width="1%"/>
							<td width="18%">
							</td>
							<td width="26%" align="right">
                                                                <input type="button" name="editAdmission" value="Edit" class="uportal-button" onclick="javascript:editAdmissions('{$baseActionURL}?action=admissions&amp;editAdmission=true&amp;ADMISSIONS_intAdmissionkey={$ADMISSIONS_intAdmissionkey}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}')"/>
                    
                                                        </td>
							<td width="5%" id="neuragenix-end-spacer"/>
						</tr>
						<tr>
							<td/>
						</tr>
                                        </table>
				</form>
			</xsl:for-each>
		</xsl:if>
                
		<!--- Use to move the focus to the Diagnosis_Day-->
		<script language="javascript">
                    function selectAllDiagnosis()
                    {
                        var i;

                        for(i = 0; i &lt; document.collection_view.allDiagnosis.options.length; i++){
                            document.collection_view.allDiagnosis.options[i].selected = true;
                        }

                        return true;
                    }

                    function submitToDiagnosis( baseActionURL )
                    {
                        document.collection_view.action= baseActionURL + '&amp;action=diagnosis';
                        document.collection_view.submit();            
                    }
                    
                    document.collection_view.ADMISSIONS_dtDiagnosis_Day.focus();   
                </script>
                
	</xsl:template>
</xsl:stylesheet>
