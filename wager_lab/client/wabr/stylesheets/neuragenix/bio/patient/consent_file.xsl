<?xml version="1.0" encoding="utf-8"?>
<!--
    Document   : patient_consent.xml
    Copyright ï¿½ 2003 Neuragenix, Inc .  All rights reserved.
    Created on :    
    Author     :
    
    Modified on: 11/02/2004
    Author     : Anita Balraj
    Description: Edit Consent Study and Secure Download Implementation
             
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./patient_menu.xsl"/>
	<xsl:include href="../../common/common_btn_name.xsl"/>
	<xsl:include href="./infopanel.xsl"/>
	<xsl:output method="html" indent="no"/>
	<!-- Start Secure Download Implementation -->
	<xsl:variable name="downloadURL">downloadURL_false</xsl:variable>
	<xsl:variable name="nodeId">nodeId_false</xsl:variable>
	<!-- End Secure -->
	<xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
	<xsl:template match="patient">
		<!-- Call Infopanel Template which will Call "inforpanel_content" Template -->
		<xsl:call-template name="infopanel">
			<xsl:with-param name="activeSubtab">consent</xsl:with-param>
			<xsl:with-param name="PATIENT_intInternalPatientID">
				<xsl:value-of select="PATIENT_intInternalPatientID"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="infopanel_content">
		<!-- Get the parameters from the channel class -->
		<xsl:variable name="PATIENT_intInternalPatientID">
			<xsl:value-of select="PATIENT_intInternalPatientID"/>
		</xsl:variable>
		<xsl:variable name="CONSENT_intConsentKey">
			<xsl:value-of select="CONSENT_intConsentKey"/>
		</xsl:variable>
		<xsl:variable name="CONSENTSTUDY_intConsentKey">
			<xsl:value-of select="CONSENTSTUDY_intConsentKey"/>
		</xsl:variable>
		<xsl:variable name="CONSENTSTUDY_intConsentStudyKey">
			<xsl:value-of select="CONSENTSTUDY_intConsentStudyKey"/>
		</xsl:variable>
		<xsl:variable name="CONSENTSTUDY_intStudyID">
			<xsl:value-of select="CONSENTSTUDY_intStudyID"/>
		</xsl:variable>
		<xsl:variable name="CONSENTSTUDY_strApproved">
			<xsl:value-of select="CONSENTSTUDY_strApproved"/>
		</xsl:variable>
		<xsl:variable name="CONSENTSTUDY_dtApprovedDate">
			<xsl:value-of select="CONSENTSTUDY_dtApprovedDate"/>
		</xsl:variable>
		<xsl:variable name="CONSENTSTUDY_strResearcher">
			<xsl:value-of select="CONSENTSTUDY_strResearcher"/>
		</xsl:variable>
		<xsl:variable name="CONSENTSTUDY_strRefDoctor">
			<xsl:value-of select="CONSENTSTUDY_strRefDoctor"/>
		</xsl:variable>
		<xsl:variable name="CONSENTSTUDY_strFileName">
			<xsl:value-of select="CONSENTSTUDY_strFileName"/>
		</xsl:variable>
		<xsl:variable name="CONSENTSTUDY_strComments">
			<xsl:value-of select="CONSENTSTUDY_strComments"/>
		</xsl:variable>
		<!-- Start Edit Consent Study-->
		<xsl:variable name="strEdit">
			<xsl:value-of select="strEdit"/>
		</xsl:variable>
		<!-- End Edit Consent Study-->
		<table width="100%">
			<tr>
				<td class="neuragenix-form-required-text" width="100%">
					<xsl:value-of select="error"/>
				</td>
			</tr>
		</table>
		<form action="{$baseActionURL}?uP_root=root&amp;action=consent&amp;addFile=true&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;CONSENTSTUDY_intConsentStudyKey={$CONSENTSTUDY_intConsentStudyKey}" method="POST" enctype="multipart/form-data">
			<table>
				<tr>
					<td class="uportal-label">Please select a signed consent:</td>
				</tr>
				<tr>
					<td class="uportal-label">
						<input type="file" name="CONSENTSTUDY_strFileName" class="uportal-input-text"/>
					</td>
				</tr>
				<tr>
					<td id="neuragenix-form-row-label-required" class="uportal-label">
						<input type="submit" name="saveFile" value="Add" class="uportal-button"/>
					</td>
				</tr>
			</table>
		</form>
	</xsl:template>
</xsl:stylesheet>
