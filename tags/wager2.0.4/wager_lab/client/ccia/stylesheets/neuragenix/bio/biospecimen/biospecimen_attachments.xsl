<?xml version="1.0" encoding="utf-8"?>
<!--
    Document   : patient_consent.xml
    Copyright ï¿½ 2003 Neuragenix, Inc .  All rights reserved.
    Created on :    
    Author     :
    
    Modified on: 11/02/2004u
    Author     : rennypv
    Description: Edit Consent Study and Secure Download Implementation
             
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./biospecimen_menu.xsl"/>
	<xsl:include href="../../common/common_btn_name.xsl"/>
	<xsl:include href="./javascript_code.xsl"/>
	<xsl:output method="html" indent="no"/>
	<!-- Start Secure Download Implementation -->
	<xsl:param name="downloadURL">downloadURL_false</xsl:param>
	<xsl:param name="formParams">
		current=biospecimen_download_attachment&amp;BIOSPECIMEN_intBiospecimenID=<xsl:value-of
			select="/biospecimenAttachments/BIOSPECIMEN_intBiospecimenID"/>
	</xsl:param>
	<xsl:param name="nodeId">nodeId_false</xsl:param>
	<xsl:param name="strErrorMessage">strErrorMessage</xsl:param>
	<xsl:template match="biospecimenAttachments">
		<xsl:variable name="intBiospecimenID"><xsl:value-of select="/biospecimenAttachments/BIOSPECIMEN_intBiospecimenID"/></xsl:variable>
		<xsl:variable name="intInternalPatientID">
			<xsl:value-of select="BIOSPECIMEN_intInternalPatientID"/>
		</xsl:variable>
		<table width="100%">
			<tr>
				<td class="uportal-channel-subtitle"> Sample Attachments </td>
				<td width="70%" style="text-align: right">
					<!--back button -->
					<form name="back_form" action="{$baseActionURL}?uP_root=root" method="POST">
						<input type="hidden" name="action" value="view_biospecimen"/>
						<input type="hidden" name="module" value="core"/>
						<input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
							value="{$intBiospecimenID}"/>
						<img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
							alt="Previous" onclick="javascript:document.back_form.submit();"/>
						<img border="0" src="media/neuragenix/buttons/next_disabled.gif"
							alt="Next"/>
					</form>
					<!-- end of back button -->
				</td>
			</tr>
		</table>
		<table width="100%">
			<hr/>
			<tr>
				<!-- errors message -->
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
				</tr>
				<td class="neuragenix-form-required-text" width="20%"
					id="neuragenix-required-header" align="right"/>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td width="10%"/>
				<td width="15%" class="uportal-label">
					<xsl:value-of select="BIOSPECIMEN_strBiospecimenIDDisplay"/>: </td>
				<td width="15%" class="uportal-text">
					<xsl:value-of select="BIOSPECIMEN_strBiospecimenID"/>
				</td>
				<td width="5%"/>
				<td width="15%" class="uportal-label">
					<xsl:value-of select="BIOSPECIMEN_intParentIDDisplay"/>: </td>
				<td width="15%" class="uportal-text">
					<xsl:value-of select="BIOSPECIMEN_intParentID"/>
				</td>
				<td width="25%"/>
			</tr>
			<tr>
				<td width="10%"/>
				<td width="15%" class="uportal-label">
					<xsl:value-of select="BIOSPECIMEN_strSampleTypeDisplay"/>: </td>
				<td width="15%" class="uportal-text">
					<xsl:value-of select="BIOSPECIMEN_strSampleType"/>
				</td>
				<td width="5%"/>
				<td width="15%" class="uportal-label">
					<xsl:value-of select="BIOSPECIMEN_strGradeDisplay"/>: </td>
				<td width="15%" class="uportal-text">
					<xsl:value-of select="BIOSPECIMEN_strGrade"/>
				</td>
				<td width="25%"/>
			</tr>
			<tr>
				<td width="10%"/>
				<td width="15%" class="uportal-label">
					<xsl:value-of select="BIOSPECIMEN_strSpeciesDisplay"/>: </td>
				<td width="15%" class="uportal-text">
					<xsl:value-of select="BIOSPECIMEN_strSpecies"/>
				</td>
				<td width="5%"/>
				<td width="15%" class="uportal-label"/>
				<td width="15%" class="uportal-label"/>
				<td width="25%"/>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td>
					<hr/>
				</td>
			</tr>
		</table>
		<xsl:if test="count(attachments/ATTACHMENTS_domainName) &gt; 0">
			<table width="100%">
				<tr>
					<td height="10px"/>
				</tr>
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
				<xsl:for-each select="attachments">
					<xsl:variable name="strID">
						<xsl:value-of select="ATTACHMENTS_strID"/>
					</xsl:variable>
					<xsl:variable name="strAttachmentkey">
						<xsl:value-of select="ATTACHMENTS_attachmentkey"/>
					</xsl:variable>
					<xsl:variable name="strFilename">
						<xsl:value-of select="ATTACHMENTS_strAttachmentsFileName"/>
					</xsl:variable>
					<tr>
						<td width="1%" class="neuragenix-form-required-text"/>
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
							<a
								href="{$downloadURL}?uP_root={$nodeId}&amp;domain=ATTACHMENTS&amp;primary_field=ATTACHMENTS_attachmentkey&amp;primary_value={$strAttachmentkey}&amp;file_name_field=ATTACHMENTS_strAttachmentsFileName&amp;property_name=neuragenix.bio.patient.SaveAttachmentsLocation&amp;activity_required=biospecimen_attachments"
								target="_blank">view </a>
						</td>
						<!-- End Secure Download Implementation -->
						<td width="5%" class="uportal-input-text">
							<a
								href="javascript:confirmDelete('{$baseActionURL}?uP_root=root&amp;{$formParams}&amp;action=delete_attachment&amp;module=attachments&amp;ATTACHMENTS_intAttachmentKey={$strAttachmentkey}&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecimenID}')"
								> del</a>
						</td>
					</tr>
				</xsl:for-each>
				<tr>
					<td colspan="6">
						<hr/>
					</td>
				</tr>
			</table>
		</xsl:if>
		<form action="{$baseActionURL}?uP_root=root&amp;{$formParams}" method="post"
			enctype="multipart/form-data">
			<input type="hidden" name="module" value="Attachments"/>
			<input type="hidden" name="action" value="add_attachment"/>
			<input type="hidden" name="BIOSPECIMEN_intBiospecimenID" value="{$intBiospecimenID}"/>
			<table width="100%">
				<tr>
					<td class="uportal-channel-subtitle"> Add a file<br/>
					</td>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td class="neuragenix-form-required-text" colspan="2">
						<xsl:value-of select="strErrorFileToLarge"/>
					</td>
				</tr>
				<tr>
					<td class="uportal-label" height="5px"/>
				</tr>
				<tr>
					<td class="uportal-label" height="5px"> Comments: </td>
					<td>
						<textarea name="ATTACHMENTS_strAttachmentComments" rows="2" cols="15"
							class="uportal-input-text"/>
					</td>
				</tr>
				<tr>
					<td class="uportal-label" height="5px"> File: </td>
					<td>
						<input type="file" name="ATTACHMENTS_strAttachmentsFileName"
							class="uportal-input-text"/>
					</td>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td width="5%" class="uportal-label">
						<input type="submit" name="saveAttachments" value="{$saveBtnLabel}"
							class="uportal-button"/>
					</td>
					<td width="5%" class="uportal-label">
						<!--
						
						<input type="button" name="cancel" value="Clear" class="uportal-button"
							onclick="javascript:jumpTo('{$baseActionURL}?uP_root=root&amp;module=attachments&amp;{$formParams}')"
						/> -->
					</td>
					<td width="90%"/>
				</tr>
			</table>
		</form>
		<table width="100%">
			<tr>
				<td>
					<hr/>
				</td>
			</tr>
		</table>
		<table>
			<td>
				<!--back button -->
				<form action="{$baseActionURL}?uP_root=root" method="POST">
					<input type="hidden" name="action" value="view_biospecimen"/>
					<input type="hidden" name="module" value="core"/>
					<input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
						value="{$intBiospecimenID}"/>
					<input type="submit" name="backbutton" value="&lt; Back" tabindex="40"
						class="uportal-button"/>
				</form>
				<!-- end of back button -->
			</td>
		</table>
	</xsl:template>
</xsl:stylesheet>
