<?xml version="1.0" encoding="UTF-8"?>
<!--
    Document   : patient_admissions.xsl
    Created on : May 13, 2004, 3:12 PM
    Author     : renny
    Description:Adding admissions of a patient.
    
	Version: $Id: admissions.xsl,v 1.9 2005/04/08 07:00:40 kleong Exp $
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
xmlns:url="http://www.jclark.com/xt/java/java.net.URLEncoder" exclude-result-prefixes="url">
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
                        <td class="neuragenix-form-required-text" width="100%"><xsl:value-of select="error"/></td>
                    </tr>
                    <tr>
                        <td class="uportal-channel-subtitle">Add a Collection</td>
                    </tr>
		</table>
                <xsl:variable name='ADMISSIONS_intAdmissionkey'><xsl:value-of select="ADMISSIONS_intAdmissionkey"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strAdmissionID'><xsl:value-of select="ADMISSIONS_strAdmissionID"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_intPatientID'><xsl:value-of select="ADMISSIONS_intPatientID"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strHospital'><xsl:value-of select="ADMISSIONS_strHospital"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strRefDoctor'><xsl:value-of select="ADMISSIONS_strRefDoctor"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_dtAdmission'><xsl:value-of select="ADMISSIONS_dtAdmission"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_dtSurgery'><xsl:value-of select="ADMISSIONS_dtSurgery"/></xsl:variable>
                <xsl:variable name="editAdmissionSelected"><xsl:value-of select="editAdmissionSelected"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strHospitalUR'><xsl:value-of select="ADMISSIONS_strHospitalUR"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strComments'><xsl:value-of select="ADMISSIONS_strComments"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strCollectGrp'><xsl:value-of select="ADMISSIONS_strCollectGrp"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strEpisodeNo'><xsl:value-of select="ADMISSIONS_strEpisodeNo"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strEpisodeDesc'><xsl:value-of select="ADMISSIONS_strEpisodeDesc"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strTissueType'><xsl:value-of select="ADMISSIONS_strTissueType"/></xsl:variable>
                <xsl:variable name='ADMISSIONS_strTissueClass'><xsl:value-of select="ADMISSIONS_strTissueClass"/></xsl:variable>
                
		<form name="collection_view" action="{$baseActionURL}?uP_root=root&amp;action=admissions" method="post">
                
                        <!-- View Current Collections -->
			<table width="100%">
                        
                            <!-- Row 1: strAdmissionId, strTissueType -->
                            <tr>
                                <td width="1%" class="neuragenix-form-required-text">*</td>
                                <td width="19%" class="uportal-label" align="left">
                                        <xsl:value-of select="ADMISSIONS_strAdmissionIDDisplay"/>:</td>
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
                                <td width="10%"/>
                                <td width="1%" class="neuragenix-form-required-text"/>
                                <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_strTissueTypeDisplay"/>:</td>
                                <td width="25%">
                                    <select name="ADMISSIONS_strTissueType" tabindex="23" class="uportal-input-text">
                                        <xsl:for-each select="ADMISSIONS_strTissueType">
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
                            </tr>
                                
                            <!-- Row 2: dtAdmission, strTissueClass -->
                            <tr>
                                <td width="1%" class="neuragenix-form-required-text"></td>
                                <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_dtAdmissionDisplay"/>:
                                </td>
                                <td width="25%">
                                    <xsl:variable name="dtAdmission_Day"><xsl:value-of select="ADMISSIONS_dtAdmission_Day"/></xsl:variable>
                                    <xsl:variable name="dtAdmission_Month"><xsl:value-of select="ADMISSIONS_dtAdmission_Month"/></xsl:variable>
                                    <xsl:variable name="dtAdmission_Year"><xsl:value-of select="ADMISSIONS_dtAdmission_Year"/></xsl:variable>
                                    <xsl:choose>
                                        <xsl:when test="ADMISSIONS_dtAdmission/@display_type='dropdown'">
                                            <select name="ADMISSIONS_dtAdmission_Day" tabindex="15" class="uportal-input-text">
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
                                            <select name="ADMISSIONS_dtAdmission_Month" tabindex="16" class="uportal-input-text">
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
                                            <input type="text" name="ADMISSIONS_dtAdmission_Day" value="{$dtAdmission_Day}" size="2" tabindex="15" class="uportal-input-text"/>
                                            <input type="text" name="ADMISSIONS_dtAdmission_Month" value="{$dtAdmission_Month}" size="2" tabindex="16" class="uportal-input-text"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <input type="text" name="ADMISSIONS_dtAdmission_Year" value="{$dtAdmission_Year}" size="5" tabindex="17" class="uportal-input-text"/>
                                </td>
                                <td width="10%"/>
                                <td width="1%" class="neuragenix-form-required-text"/>
                                <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_strTissueClassDisplay"/>:</td>
                                <td width="25%">
                                    <select name="ADMISSIONS_strTissueClass" tabindex="28" class="uportal-input-text">
                                        <xsl:for-each select="ADMISSIONS_strTissueClass">
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
                            </tr>

                            <!-- Row 3: strCollectGrp, dtSurgery -->
                            <tr>
                                <td width="1%" class="neuragenix-form-required-text"/>
                                <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_strCollectGrpDisplay"/>:
                                </td>
                                <td width="25%">
                                    <select name="ADMISSIONS_strCollectGrp" tabindex="18" class="uportal-input-text">
                                        <xsl:for-each select="ADMISSIONS_strCollectGrp">
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
                                <td width="10%"/>
                                <xsl:variable name="dtSurgery_Day">
                                        <xsl:value-of select="ADMISSIONS_dtSurgery_Day"/>
                                </xsl:variable>
                                <xsl:variable name="dtSurgery_Month">
                                        <xsl:value-of select="ADMISSIONS_dtSurgery_Month"/>
                                </xsl:variable>
                                <xsl:variable name="dtSurgery_Year">
                                        <xsl:value-of select="ADMISSIONS_dtSurgery_Year"/>
                                </xsl:variable>
                                <td width="1%" class="neuragenix-form-required-text"/>
                                <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_dtSurgeryDisplay"/>:
                                </td>
                                <td width="25%">
                                        <xsl:choose>
                                                <xsl:when test="ADMISSIONS_dtSurgery/@display_type='dropdown'">
                                                        <select name="ADMISSIONS_dtSurgery_Day" tabindex="30" class="uportal-input-text">
                                                                <xsl:for-each select="ADMISSIONS_dtSurgery_Day">
                                                                        <option>
                                                                                <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                                <xsl:if test="@selected=1">
                                                                                        <xsl:attribute name="selected">true</xsl:attribute>
                                                                                </xsl:if>
                                                                                <xsl:value-of select="."/>
                                                                        </option>
                                                                </xsl:for-each>
                                                        </select>
                                                        <select name="ADMISSIONS_dtSurgery_Month" tabindex="31" class="uportal-input-text">
                                                                <xsl:for-each select="ADMISSIONS_dtSurgery_Month">
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
                                                        <input type="text" name="ADMISSIONS_dtSurgery_Day" value="{$dtSurgery_Day}" size="2" tabindex="30" class="uportal-input-text"/>
                                                        <input type="text" name="ADMISSIONS_dtSurgery_Month" value="{$dtSurgery_Month}" size="2" tabindex="31" class="uportal-input-text"/>
                                                </xsl:otherwise>
                                        </xsl:choose>
                                        <input type="text" name="ADMISSIONS_dtSurgery_Year" value="{$dtSurgery_Year}" size="4" tabindex="32" class="uportal-input-text"/>
                                </td>
                            </tr>

                            <!-- Row 4: strEpisodeNo, strRefDoctor (strSurgeon) -->
                            <tr>
                                <td width="1%" class="neuragenix-form-required-text"/>
                                <td width="19%" class="uportal-label">
                                    <xsl:value-of select="ADMISSIONS_strEpisodeNoDisplay"/>:</td>
                                <td width="25%">
                                    <select name="ADMISSIONS_strEpisodeNo" tabindex="20" class="uportal-input-text">
                                        <xsl:for-each select="ADMISSIONS_strEpisodeNo">
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
                                <td width="10%"/>
                                <td width="1%" class="neuragenix-form-required-text"/>
                                <td width="19%" class="uportal-label">
                                        <xsl:value-of select="ADMISSIONS_strRefDoctorDisplay"/>:</td>
                                <td width="25%"> 
                                    <select name="ADMISSIONS_strRefDoctor" tabindex="34" class="uportal-input-text">
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
                            </tr>
                                
                            <!-- Row 5: strEpisodeDesc, strHospital -->
                            <tr>
                                <td width="1%" class="neuragenix-form-required-text"/>
                                <td width="19%" class="uportal-label" valign="top">
                                    <xsl:value-of select="ADMISSIONS_strEpisodeDescDisplay"/>:</td>
                                <td width="25%" valign="left">
                                    <select name="ADMISSIONS_strEpisodeDesc" tabindex="21" class="uportal-input-text">
                                        <xsl:for-each select="ADMISSIONS_strEpisodeDesc">
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
                                <td width="10%"/>
                                <td width="1%" class="neuragenix-form-required-text"/>
                                <td width="19%" class="uportal-label" valign="top">
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
                            </tr>
                                
                            <!-- Row 6: -, strHospitalUR -->
                            <tr>
                                <td width="1%" class="neuragenix-form-required-text"/>
                                <td width="19%" class="uportal-label" valign="top"></td>
                                <td width="25%" valign="left"></td>
                                <td width="10%"/>
                                <td width="1%" class="neuragenix-form-required-text"/>
                                <td width="19%" class="uportal-label" valign="left">
                                        <xsl:value-of select="ADMISSIONS_strHospitalURDisplay"/>:
                                </td>
                                <td width="25%">
                                        <input type="text" align="left" name="ADMISSIONS_strHospitalUR" size="20" tabindex="37" value="{$ADMISSIONS_strHospitalUR}" class="uportal-input-text" />
                                </td>
                            </tr>
                        </table>
                            
			<table width="100%">
                                
                            <!-- Row 7: strComments -->
                            <tr>
                                <td width="1%" class="neuragenix-form-required-text"/>
                                <td width="19%" class="uportal-label" valign="lfet">
                                        <xsl:value-of select="ADMISSIONS_strCommentsDisplay"/>:</td>
                                <td width="74%" valign="left">
                                        <textarea class="uportal-input-text" cols="95" rows="2" tabindex="39" name="ADMISSIONS_strComments">
                                            <xsl:value-of select="ADMISSIONS_strComments"/>
                                        </textarea>
                                </td>
                                <td width="5%" id="neuragenix-end-spacer"/>                                
                            </tr>
                                
                            <!-- Row 8: Empty -->
                            <tr>
                                <td width="1%" class="neuragenix-form-required-text"/>
                                <td width="19%" class="uportal-label" valign="top"></td>
                                <td width="74%" valign="top"></td>
                                <td width="5%" id="neuragenix-end-spacer"/>                                
                            </tr>
                                
                            <!-- Row 9: btnClear, btnAdd -->
                            <tr>
                                <td width="1%" class="neuragenix-form-required-text"/>
                                <td width="19%" class="uportal-label" valign="top">
                                    <xsl:choose>
                                        <xsl:when test="string-length($editAdmissionSelected) &gt; 0">
                                            <input valign="left" type="button"  tabindex="46" name="cancel" value="Cancel" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?uP_root=root&amp;action=admissions&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}'"/>                        
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <input valign="left" type="button"  tabindex="46" name="clear" value="Clear" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?uP_root=root&amp;action=admissions&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}'"/>                        
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                                <td width="74%" valign="top" align="right">
                                    <input type="hidden" name="PATIENT_intInternalPatientID" value="{$PATIENT_intInternalPatientID}"/>
                                    <input type="hidden" name="ADMISSIONS_intPatientID" value="{$PATIENT_intInternalPatientID}"/>
                                    <input type="hidden" name="ADMISSIONS_intAdmissionkey" value="{$ADMISSIONS_intAdmissionkey}"/>                                                
                                    <xsl:choose>
                                        <xsl:when test="string-length($editAdmissionSelected) &gt; 0">
                                            <input type="submit" name="updateAdmission" tabindex="42" value="Update" class="uportal-button" onclick="javascript:selectAllDiagnosis()" />                                                
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <input type="submit" name="addAdmission" tabindex="42" value="Add" class="uportal-button" onclick="javascript:selectAllDiagnosis()" />
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                                <td width="5%" id="neuragenix-end-spacer"/>                                
                            </tr>
                        </table>
		</form>

                <!-- Display Collection History Details -->                
		<xsl:if test="count(admission) &gt; 0">
			<table width="100%">
                            <tr>
                                <td class="uportal-channel-subtitle"><hr/></td>
                            </tr>
                            <tr>
                                <td class="uportal-channel-subtitle">Collection Details<br/></td>
                            </tr>
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

                                        <!-- Row 1: strAdmissionId, strTissueType -->
                                        <tr>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                        <xsl:value-of select="../ADMISSIONS_strAdmissionIDDisplay"/>:
                                                </td>
                                                <td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input" align="left">
                                                        <xsl:value-of select="ADMISSIONS_strAdmissionID"/>
                                                </td>
                                                <td width="5%"/>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                        <xsl:value-of select="../ADMISSIONS_strTissueTypeDisplay"/>:
                                                </td>
                                                <td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input" align="left">
                                                        <xsl:value-of select="ADMISSIONS_strTissueType"/>
                                                </td>
                                                <td width="5%" id="neuragenix-end-spacer"/>
                                        </tr>

                                        <!-- Row 2: dtAdmission, strTissueClass -->
                                        <tr>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                        <xsl:value-of select="../ADMISSIONS_dtAdmissionDisplay"/>:
                                                </td>
                                                <td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input" align="left">
                                                        <xsl:value-of select="ADMISSIONS_dtAdmission"/>
                                                </td>
                                                <td width="5%"/>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                        <xsl:value-of select="../ADMISSIONS_strTissueClassDisplay"/>:
                                                </td>
                                                <td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input" align="left">
                                                        <xsl:value-of select="ADMISSIONS_strTissueClass"/>
                                                </td>
                                                <td width="5%" id="neuragenix-end-spacer"/>
                                        </tr>

                                        <!-- Row 3: strCollectGrp, dtSurgery -->
                                        <tr>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                        <xsl:value-of select="../ADMISSIONS_strCollectGrpDisplay"/>:
                                                </td>
                                                <td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
                                                        <xsl:value-of select="ADMISSIONS_strCollectGrp"/>
                                                </td>
                                                <td width="5%"/>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                        <xsl:value-of select="../ADMISSIONS_dtSurgeryDisplay"/>:
                                                </td>
                                                <td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input" align="left">
                                                        <xsl:value-of select="ADMISSIONS_dtSurgery"/>
                                                </td>
                                                <td width="5%" id="neuragenix-end-spacer"/>
                                        </tr>

                                        <!-- Row 4: strEpisodeNo, strRefDoctor (strSurgeon) -->
                                        <tr>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                        <xsl:value-of select="../ADMISSIONS_strEpisodeNoDisplay"/>:
                                                </td>
                                                <td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
                                                        <xsl:value-of select="ADMISSIONS_strEpisodeNo"/>
                                                </td>
                                                <td width="5%"/>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                        <xsl:value-of select="../ADMISSIONS_strRefDoctorDisplay"/>:
                                                </td>
                                                <td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
                                                        <xsl:value-of select="ADMISSIONS_strRefDoctor"/>
                                                </td>
                                                <td width="5%" id="neuragenix-end-spacer"/>
                                        </tr>

                                        <!-- Row 5: strEpisodeDesc, strHospital -->                                                
                                        <tr>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                        <xsl:value-of select="../ADMISSIONS_strEpisodeDescDisplay"/>:
                                                </td>
                                                <td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
                                                        <xsl:value-of select="ADMISSIONS_strEpisodeDesc"/>
                                                </td>
                                                <td width="5%"/>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                        <xsl:value-of select="../ADMISSIONS_strHospitalDisplay"/>:
                                                </td>
                                                <td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
                                                        <xsl:value-of select="ADMISSIONS_strHospital"/>
                                                </td>
                                                <td width="5%" id="neuragenix-end-spacer"/>
                                        </tr>

                                        <!-- Row 6: intPatientAge strHospitalUR -->
                                        <tr>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                        <xsl:value-of select="../ADMISSIONS_intPatientAgeDisplay"/>:
                                                </td>
                                                <td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
                                                        <xsl:value-of select="ADMISSIONS_intPatientAge"/>
                                                </td>
                                                <td width="5%"/>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                        <xsl:value-of select="../ADMISSIONS_strHospitalURDisplay"/>:
                                                </td>
                                                <td width="26%" class="uportal-input-text" id="neuragenix-form-row-input-input">
                                                        <xsl:value-of select="ADMISSIONS_strHospitalUR"/>
                                                </td>
                                                <td width="5%" id="neuragenix-end-spacer"/>
                                        </tr>
                                    </table>
                                    <table width="100%">
                                        <!-- Row 7: strComments -->
                                        <tr>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                        <xsl:value-of select="../ADMISSIONS_strCommentsDisplay"/>:
                                                </td>
                                                <td width="74%" class="uportal-input-text" id="neuragenix-form-row-input-input">
                                                        <xsl:value-of select="ADMISSIONS_strComments"/>
                                                </td>
                                                <td width="5%" id="neuragenix-end-spacer"/>
                                        </tr>

                                        <!-- Row 8: Smartforms -->
                                        <xsl:variable name="viewAdmissionSmartforms"><xsl:value-of select="/body/patient/smartforms/admission-smartforms/displayAdmissionsSmartforms"/></xsl:variable>
                                        <xsl:if test="$viewAdmissionSmartforms=1">
                                        <tr>
                                                <td width="1%" id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"/>
                                                <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                                                    Smartforms:
                                                </td>
                                                <td width="74%" class="uportal-label" id="neuragenix-form-row-input-input">
                                                    <xsl:variable name="currentAdmissionsNumber"><xsl:value-of select="./ADMISSIONS_strCollectGrp"/></xsl:variable>
                                                    <xsl:variable name="currentEpisodeNumber"><xsl:value-of select="./ADMISSIONS_strEpisodeNo"/></xsl:variable>
                                                    <xsl:for-each select="/body/patient/smartforms/admission-smartforms/admissions">
                                                        <xsl:variable name="admissionsNumber"><xsl:value-of select="./ADMISSIONS_strCollectGrp"/></xsl:variable>
                                                        <xsl:variable name="episodeNumber"><xsl:value-of select="./ADMISSIONS_strEpisodeNo"/></xsl:variable>
                                                        <xsl:if test="($currentAdmissionsNumber = $admissionsNumber) and ($currentEpisodeNumber = $episodeNumber)">
                                                            <xsl:if test="function-available('url:encode')">
                                                                <xsl:variable name="admissionsSmartformLink"><xsl:value-of select="./admissionsSmartformLink"/></xsl:variable>
                                                                <xsl:variable name="strPatientFullName"><xsl:value-of select="./ADMISSIONS_strPatientFullName"/></xsl:variable>
                                                                <a href="{$smartformURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformTab}&amp;{$admissionsSmartformLink}&amp;var2={url:encode($strPatientFullName)}">
                                                                View Collection Group Smartforms</a>
                                                            </xsl:if>
                                                        </xsl:if>
                                                    </xsl:for-each>
                                                </td>
                                                <td width="5%" id="neuragenix-end-spacer"/>
                                        </tr>
                                        </xsl:if>
                                        <tr>
                                                <td width="1%"/>
                                                <td width="18%">
                                                        <input valign="left" type="hidden" name="PATIENT_intInternalPatientID" value="{$PATIENT_intInternalPatientID}"/>
                                                        <input valign="left" type="hidden" name="ADMISSIONS_strAdmissionID" value="{$strAdmissionID}"/>
                                                        <input valign="left" type="button" name="deleteAdmission" value="Delete" class="uportal-button" onclick="javascript:confirmDelete('{$baseActionURL}?action=admissions&amp;deleteAdmission=true&amp;ADMISSIONS_intAdmissionkey={$ADMISSIONS_intAdmissionkey}&amp;ADMISSIONS_strAdmissionID={$strAdmissionID}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}')"/>
                                                </td>
                                                <td width="74%" align="right" >
                                                        <input type="button" name="editAdmission" value="Edit" class="uportal-button" onclick="javascript:editAdmissions('{$baseActionURL}?action=admissions&amp;editAdmission=true&amp;ADMISSIONS_intAdmissionkey={$ADMISSIONS_intAdmissionkey}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}')"/>
                                                </td>
                                                <td width="5%" id="neuragenix-end-spacer"/>
                                        </tr>
                                        <tr>
                                            <td />
                                        </tr>
                                    </table>
				</form>
			</xsl:for-each>
		</xsl:if>
		<!--- Use to move the focus to the Diagnosis_Day-->
		<script language="javascript">
            document.collection_view.ADMISSIONS_dtAdmission_Day.focus();   
        </script>
	</xsl:template>
</xsl:stylesheet>
