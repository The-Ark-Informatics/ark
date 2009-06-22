<?xml version="1.0" encoding="utf-8"?>
<!--
    Document   : patient_consent.xml
    Copyright ï¿½ 2003 Neuragenix, Inc .  All rights reserved.
    Created on :    
    Author     :
    
    Modified on: 11/02/2004
    Author     : rennypv
    Description: Add Attachments
              
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./patient_menu.xsl"/>
	<xsl:include href="../../common/common_btn_name.xsl"/>
	<xsl:include href="./infopanel.xsl"/>
	<xsl:output method="html" indent="no"/>
	<!-- Start Secure Download Implementation -->
	<xsl:param name="formParams">current=patient_download_attachment&amp;ATTACHMENTS_strID=<xsl:value-of select="/patient/PATIENT_strPatientID"/>&amp;ATTACHMENTS_domainName=Patient</xsl:param>
	<xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
	<xsl:template match="patient">
		<!-- Call Infopanel Template which will Call "inforpanel_content" Template -->
		<xsl:call-template name="infopanel">
			<xsl:with-param name="activeSubtab">attachments</xsl:with-param>
			<xsl:with-param name="PATIENT_intInternalPatientID">
				<xsl:value-of select="PATIENT_intInternalPatientID"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="infopanel_content">
		<xsl:variable name="intInternalPatientID">
			<xsl:value-of select="PATIENT_intInternalPatientID"/>
		</xsl:variable>
		<xsl:variable name="PATIENT_intInternalPatientID">
			<xsl:value-of select="PATIENT_intInternalPatientID"/>
		</xsl:variable>
		<xsl:if test="count(attachment) &gt; 0">
			<table width="100%">
				<tr>
					<td class="uportal-channel-subtitle">
			Attachments<br/>
					</td>
				</tr>
			</table>
			<table width="100%" cellspacing="0">
				<tr>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="12%" class="uportal-label">
						<xsl:value-of select="ATTACHMENTS_strAttachmentCommentsDisplay"/>
					</td>
					<td width="12%" class="uportal-label">
						<xsl:value-of select="ATTACHMENTS_strAttachedByDisplay"/>
					</td>
					<td width="12%" class="uportal-label">
						<xsl:value-of select="ATTACHMENTS_strAttachmentTimestampDisplay"/>
					</td>
					<td width="5%" class="uportal-label">File</td>
					<td width="5%" class="uportal-label">Delete</td>
				</tr>
				<xsl:for-each select="attachment">
					<xsl:variable name="strPatientID">
						<xsl:value-of select="ATTACHMENTS_strID"/>
					</xsl:variable>
					<xsl:variable name="strAttachmentkey">
						<xsl:value-of select="ATTACHMENTS_attachmentkey"/>
					</xsl:variable>
					<xsl:variable name="strFilename">
						<xsl:value-of select="ATTACHMENTS_strAttachmentsFileName"/>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="position() mod 2 != 0">
							<tr class="uportal-input-text">
								<td width="1%"/>
								<td width="20%" class="uportal-input-text">
									<xsl:value-of select="ATTACHMENTS_strAttachmentComments"/>
								</td>
								<td width="20%" class="uportal-input-text">
									<xsl:value-of select="ATTACHMENTS_strAttachedBy"/>
								</td>
								<td width="12%" class="uportal-input-text">
									<xsl:value-of select="ATTACHMENTS_strAttachmentTimestamp"/>
								</td>
								<td width="5%" class="uportal-input-text">
									<a href="{$downloadURL}?uP_root={$downloadNodeId}&amp;domain=ATTACHMENTS&amp;primary_field=ATTACHMENTS_attachmentkey&amp;primary_value={$strAttachmentkey}&amp;file_name_field=ATTACHMENTS_strAttachmentsFileName&amp;property_name=neuragenix.bio.patient.SaveAttachmentsLocation&amp;activity_required=patient_attachments" target="_blank">view </a>
								</td>
								<!-- End Secure Download Implementation -->
								<td width="5%" class="uportal-input-text">
									<a href="javascript:confirmDelete('{$baseActionURL}?uP_root=root&amp;action=attachments&amp;deleteAttachments=true&amp;PATIENT_intInternalPatientID={$strPatientID}&amp;ATTACHMENTS_attachmentkey={$strAttachmentkey}')"> del</a>
								</td>
							</tr>
						</xsl:when>
						<xsl:otherwise>
							<tr class="uportal-text">
								<td width="1%"/>
								<td width="20%" class="uportal-text">
									<xsl:value-of select="ATTACHMENTS_strAttachmentComments"/>
								</td>
								<td width="20%" class="uportal-text">
									<xsl:value-of select="ATTACHMENTS_strAttachedBy"/>
								</td>
								<td width="12%" class="uportal-text">
									<xsl:value-of select="ATTACHMENTS_strAttachmentTimestamp"/>
								</td>
								<td width="5%" class="uportal-text">
									<a href="{$downloadURL}?uP_root={$downloadNodeId}&amp;domain=ATTACHMENTS&amp;primary_field=ATTACHMENTS_attachmentkey&amp;primary_value={$strAttachmentkey}&amp;file_name_field=ATTACHMENTS_strAttachmentsFileName&amp;property_name=neuragenix.bio.patient.SaveAttachmentsLocation&amp;activity_required=patient_attachments" target="_blank">view </a>
								</td>
								<!-- End Secure Download Implementation -->
								<td width="5%" class="uportal-text">
									<a href="javascript:confirmDelete('{$baseActionURL}?uP_root=root&amp;action=attachments&amp;deleteAttachments=true&amp;PATIENT_intInternalPatientID={$strPatientID}&amp;ATTACHMENTS_attachmentkey={$strAttachmentkey}')"> del</a>
								</td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</table>
		</xsl:if>
		<form name="attachment_form" action="{$baseActionURL}?uP_root=root&amp;action=attachments" method="post" enctype="multipart/form-data">
			<table width="100%">
				<tr>
					<td class="neuragenix-form-required-text">
						<xsl:value-of select="error"/>
					</td>
				</tr>
				<tr>
					<td class="uportal-channel-subtitle">
                    Add a file<br/>
					</td>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td class="uportal-label" colspan="2">
				
			</td>
				</tr>
				<tr>
					<td class="uportal-label" valign="top">
                            Comments:
			</td>
					<td>
						<textarea name="ATTACHMENTS_strAttachmentComments" rows="4" cols="30" tabindex="21" class="uportal-input-text"/>
					</td>
				</tr>
				<tr>
					<td class="uportal-label" height="5px">
                            File:
			</td>
					<td>
						<input type="file" name="ATTACHMENTS_strAttachmentsFileName" tabindex="22" class="uportal-input-text"/>
					</td>
				</tr>
				<tr>
					<td class="uportal-label">
						<input type="reset" name="cancel" value="{$cancelBtnLabel}" class="uportal-button"/>
						<input type="hidden" name="ATTACHMENTS_domainName" value="Patient" class="uportal-input-text"/>
						<input type="hidden" name="PATIENT_intInternalPatientID" value="{$intInternalPatientID}"/>
					</td>
					<td class="uportal-label" align="center">
						<input type="submit" name="saveAttachments" tabindex="23" value="{$saveBtnLabel}" class="uportal-button" onblur="javascript:document.attachment_form.ATTACHMENTS_strAttachmentComments.focus()"/>
					</td>
				</tr>
			</table>
		</form>
		<script language="Javascript">
            document.attachment_form.ATTACHMENTS_strAttachmentComments.focus();
        </script>
	</xsl:template>
</xsl:stylesheet>
