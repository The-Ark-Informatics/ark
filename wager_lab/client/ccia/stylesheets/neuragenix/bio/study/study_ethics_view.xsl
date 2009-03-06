<?xml version="1.0" encoding="utf-8"?>
<!--
    Document   : study_ethics_view.xml
    Copyright (c) 2005 Neuragenix, Inc .  All rights reserved.
    Created on :    
    Author     :
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./study_menu.xsl"/>
	<xsl:include href="../../common/common_btn_name.xsl"/>
	<xsl:output method="html" indent="no"/>
	<!-- Start Secure Download Implementation -->
	<xsl:param name="downloadURL">downloadURL_false</xsl:param>
	<xsl:param name="nodeId">nodeId_false</xsl:param>
	<!-- End Secure -->
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
	<xsl:template match="study">
		<!-- Get the parameters from the channel class -->
		<xsl:param name="intStudyID">
			<xsl:value-of select="intStudyID"/>
		</xsl:param>
		<xsl:param name="strStudyName">
			<xsl:value-of select="strStudyName"/>
		</xsl:param>
		<xsl:param name="dtStudyStart">
			<xsl:value-of select="dtStudyStart"/>
		</xsl:param>
		<xsl:param name="dtStudyEnd">
			<xsl:value-of select="dtStudyEnd"/>
		</xsl:param>
		<xsl:param name="strSurveyName">
			<xsl:value-of select="strSurveyName"/>
		</xsl:param>
		<xsl:variable name="intEthicsApproved">
			<xsl:value-of select="intEthicsApproved"/>
		</xsl:variable>
		<xsl:variable name="strEthicsFileName">
			<xsl:value-of select="strEthicsFileName"/>
		</xsl:variable>
		<xsl:variable name="strEthicsApprovedBy">
			<xsl:value-of select="strEthicsApprovedBy"/>
		</xsl:variable>
		<xsl:variable name="dtEthicsAppDate">
			<xsl:value-of select="dtEthicsAppDate"/>
		</xsl:variable>
		<xsl:variable name="strEthicsComments">
			<xsl:value-of select="strEthicsComments"/>
		</xsl:variable>
		<table cellpadding="0" cellspacing="0" width="100%" height="100%" border="0">
			<form name="study_ethics_view" action="{$baseActionURL}?uP_root=root&amp;current=study_ethics_view" method="post">
				<tr>
					<td>
						<table width="100%">
							<tr>
								<td class="uportal-channel-subtitle">Ethics approval for project</td>
								<td align="right">
									<!-- IExplorer Hack: since no submit attributes are passed, set "save" parameter -->
									<input type="hidden" name="back" value="not_null"/>
									<!-- Back and Next Buttons -->
									<a class="submit_image_button" href="{$baseActionURL}?uP_root=root&amp;current=study_results&amp;intStudyID={$intStudyID}">
										<img src="{$buttonImagePath}/previous_enabled.gif" border="0" alt="Previous"/>
									</a>
									<img src="{$buttonImagePath}/next_disabled.gif" border="0" alt="Next"/>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<hr/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%">
							<tr>
								<td width="20%" class="uportal-label">
									<xsl:value-of select="strStudyNameDisplay"/>: </td>
								<td width="20%" class="uportal-text">
									<xsl:value-of select="strStudyName"/>
								</td>
								<td width="5%"/>
								<td width="20%"/>
								<td width="20%"/>
								<td width="15%"/>
							</tr>
							<tr>
								<td width="20%" class="uportal-label">
									<xsl:value-of select="dtStudyStartDisplay"/>: </td>
								<td width="20%" class="uportal-text">
									<xsl:value-of select="dtStudyStart"/>
								</td>
								<td width="5%"/>
								<td width="20%" class="uportal-label">
									<xsl:value-of select="dtStudyEndDisplay"/>:</td>
								<td width="20%" class="uportal-text">
									<xsl:value-of select="dtStudyEnd"/>
								</td>
								<td width="15%"/>
							</tr>
						</table>
						<table width="100%">
							<tr>
								<td>
									<hr/>
								</td>
							</tr>
						</table>
						<table width="100%">
							<tr>
								<td class="neuragenix-form-required-text" width="80%">
									<xsl:value-of select="strLockError"/>
									<xsl:value-of select="strErrorFailedUpdate"/>
									<xsl:value-of select="strErrorDuplicateKey"/>
									<xsl:value-of select="strErrorRequiredFields"/>
									<xsl:value-of select="strErrorInvalidDataFields"/>
									<xsl:value-of select="strErrorInvalidData"/>
								</td>
								<td class="neuragenix-form-required-text" width="20%" id="neuragenix-required-header" align="right">
							* = Required fields
							</td>
							</tr>
						</table>
						<table width="100%">
							<tr>
								<td width="1%" class="neuragenix-form-required-text"/>
								<td width="20%" class="uportal-channel-table-header">Ethics approvals</td>
								<td width="1%" class="neuragenix-form-required-text"/>
								<td width="15%" class="uportal-label"/>
								<td width="1%" class="neuragenix-form-required-text"/>
								<td width="20%" class="uportal-label"/>
								<td width="1%" class="neuragenix-form-required-text"/>
								<td width="26%" class="uportal-label"/>
								<td width="5%" class="neuragenix-form-required-text"/>
								<td width="5%" class="uportal-label"/>
								<td width="5%" class="uportal-label"/>
							</tr>
							<tr>
								<td height="10px"/>
							</tr>
						</table>
						<table width="100%">
							<tr>
								<td width="1%" class="neuragenix-form-required-text">*</td>
								<td width="20%" class="uportal-label">
									<xsl:value-of select="strEthicsApprovedByDisplay"/>
								</td>
								<td width="1%" class="neuragenix-form-required-text"/>
								<td width="10%" class="uportal-label">
									<xsl:value-of select="intEthicsApprovedDisplay"/>
								</td>
								<td width="1%" class="neuragenix-form-required-text"/>
								<td width="20%" class="uportal-label">
									<xsl:value-of select="dtEthicsAppDateDisplay"/>
								</td>
								<td width="1%" class="neuragenix-form-required-text"/>
								<td width="30%" class="uportal-label">
									<xsl:value-of select="strEthicsCommentsDisplay"/>
								</td>
								<td width="5%" class="uportal-label">File</td>
								<td width="5%" class="uportal-label">File</td>
								<td width="6%" class="uportal-label">Delete</td>
							</tr>
							<xsl:for-each select="searchResult">
								<xsl:variable name="intEthicsStudyID">
									<xsl:value-of select="intEthicsStudyID"/>
								</xsl:variable>
								<xsl:variable name="intEthicsApproved">
									<xsl:value-of select="intEthicsApproved"/>
								</xsl:variable>
								<xsl:variable name="intStudyID">
									<xsl:value-of select="intStudyID"/>
								</xsl:variable>
								<xsl:variable name="strEthicsFileName">
									<xsl:value-of select="strEthicsFileName"/>
								</xsl:variable>
								<xsl:variable name="strEthicsApprovedBy">
									<xsl:value-of select="strEthicsApprovedBy"/>
								</xsl:variable>
								<xsl:variable name="ETHICS_CONSENT_Timestamp">
									<xsl:value-of select="ETHICS_CONSENT_Timestamp"/>
								</xsl:variable>
								<tr>
									<td width="1%" class="neuragenix-form-required-text"/>
									<td width="20%" class="uportal-text">
										<xsl:value-of select="strEthicsApprovedBy"/>
									</td>
									<td width="1%" class="neuragenix-form-required-text"/>
									<td width="10%" class="uportal-text">
										<xsl:if test="$intEthicsApproved= -1">Yes</xsl:if>
										<xsl:if test="$intEthicsApproved= 0">No</xsl:if>
									</td>
									<td width="1%" class="neuragenix-form-required-text"/>
									<td width="20%" class="uportal-text">
										<xsl:value-of select="dtEthicsAppDate"/>
									</td>
									<td width="1%" class="neuragenix-form-required-text"/>
									<td width="30%" class="uportal-text">
										<xsl:value-of select="strEthicsComments"/>
									</td>
									<td width="5%" class="uportal-text">
										<xsl:if test="$intEthicsStudyID!= ''">
											<a href="{$baseActionURL}?uP_root=root&amp;current=study_ethics_view&amp;target=add_file&amp;intStudyID={$intStudyID}&amp;intEthicsStudyID={$intEthicsStudyID}&amp;ETHICS_CONSENT_Timestamp={$ETHICS_CONSENT_Timestamp}">add </a>
										</xsl:if>
									</td>
									<!-- Start Secure Download Module Implementation - Passing intConsentStudyID, strconsentFileName and nodeid of the channel download-->
									<td width="5%" class="uportal-text">
										<xsl:if test="$strEthicsFileName!= ''">
											<!-- <a href="./files/consent_files/{$strEthicsFileName}" target="_blank">view </a> -->
											<a href="{$downloadURL}?uP_root={$nodeId}&amp;domain=STUDY ETHICS CONSENT&amp;primary_field=STUDY ETHICS CONSENT_intEthicsStudyID&amp;primary_value={$intEthicsStudyID}&amp;file_name_field=STUDY ETHICS CONSENT_strEthicsFileName&amp;property_name=neuragenix.bio.patient.SaveConsentLocation&amp;activity_required=study_ethics_view" target="_blank">view </a>
										</xsl:if>
									</td>
									<!-- End Secure-->
									<td width="6%" class="uportal-text">
										<xsl:if test="$intEthicsStudyID!= ''">
											<a href="javascript:confirmDelete('{$baseActionURL}?uP_root=root&amp;current=study_ethics_view&amp;target=delete&amp;intEthicsStudyID={$intEthicsStudyID}&amp;intStudyID={$intStudyID}')"> del</a>
										</xsl:if>
									</td>
								</tr>
							</xsl:for-each>
							<tr>
								<!-- Approved by text box-->
								<td width="1%" class="neuragenix-form-required-text" valign="top"/>
								<td width="20%" class="uportal-label" valign="top">
									<input type="text" name="strEthicsApprovedBy" tabindex="21" size="25" value="{$strEthicsApprovedBy}" class="uportal-input-text"/>
								</td>
								<td width="1%" class="neuragenix-form-required-text" valign="top"/>
								<td width="10%" class="uportal-label" valign="top">
									<!-- Appprove selection box-->
									<select name="intEthicsApproved" tabindex="22" class="uportal-input-text">
										<option value="-1">
											<xsl:if test="$intEthicsApproved = -1">
												<xsl:attribute name="selected">true</xsl:attribute>
											</xsl:if>
												Yes
										</option>
										<option value="0">
											<xsl:if test="$intEthicsApproved = 0">
												<xsl:attribute name="selected">true</xsl:attribute>
											</xsl:if>
												No
										</option>
									</select>
								</td>
								<!-- Date -->
								<td width="1%" class="neuragenix-form-required-text" valign="top"/>
								<td width="20%" class="uportal-label" valign="top">
									<input type="text" name="dtEthicsAppDate" size="17" tabindex="23" value="{$dtEthicsAppDate}" class="uportal-input-text"/>
								</td>
								<td width="1%" class="neuragenix-form-required-text" valign="top"/>
								<td width="30%" class="uportal-label" valign="top">
									<textarea name="strEthicsComments" tabindex="24" rows="2" cols="20" class="uportal-input-text">
										<xsl:value-of select="strEthicsComments"/>
									</textarea>
								</td>
								<td width="5%" class="uportal-label" valign="top"/>
								<td width="5%" class="uportal-label" valign="top"/>
								<td width="6%" class="uportal-label" valign="top"/>
							</tr>
						</table>
						<table width="100%">
							<tr>
								<td width="1%"/>
								<td>
									<input type="submit" tabindex="25" name="saveApproval" value="{$saveBtnLabel}" class="uportal-button" onblur="javascript:document.study_ethics_view.strEthicsApprovedBy.focus()"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr height="100%">
					<td>
						<table width="100%" height="100%" border="0">
							<tr height="100%">
								<td>&#160;</td>
							</tr>
							<tr>
								<td><hr/></td>
							</tr>
							<tr>
								<td align="right">
									<!-- IExplorer Hack: since no submit attributes are passed, set "save" parameter -->
									<input type="hidden" name="back" value="not_null"/>
									<!-- Back and Next Buttons -->
									<a class="submit_image_button" href="{$baseActionURL}?uP_root=root&amp;current=study_results&amp;intStudyID={$intStudyID}">
										<img src="{$buttonImagePath}/previous_enabled.gif" border="0" alt="Previous"/>
									</a>
									<img src="{$buttonImagePath}/next_disabled.gif" border="0" alt="Next"/>
								</td>
							</tr>
						</table>
						<!-- add the study Internal ID as a hidden field to be submitted to the channel-->
						<input type="hidden" name="intStudyID" value="{$intStudyID}"/>
					</td>
				</tr>
			</form>
		</table>
		<script language="javascript">
			document.study_ethics_view.strEthicsApprovedBy.focus();
		</script>
	</xsl:template>
</xsl:stylesheet>
