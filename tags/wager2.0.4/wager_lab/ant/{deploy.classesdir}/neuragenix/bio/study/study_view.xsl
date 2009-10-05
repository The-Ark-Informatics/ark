<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./study_menu.xsl"/>
	<xsl:include href="../../common/common_btn_name.xsl"/>
	<xsl:output method="html" indent="no"/>
	<xsl:param name="biospecimenChannelURL">biospecimenChannelURL_false</xsl:param>
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
	<xsl:template match="study">
		<!-- Get the parameters from the channel class -->
		<xsl:param name="intStudyID">
			<xsl:value-of select="STUDY_intStudyID"/>
		</xsl:param>
		<xsl:param name="strStudyName">
			<xsl:value-of select="STUDY_strStudyName"/>
		</xsl:param>
		<xsl:param name="strStudyCode">
			<xsl:value-of select="STUDY_strStudyCode"/>
		</xsl:param>
		<xsl:param name="strStudyOwner">
			<xsl:value-of select="STUDY_strStudyOwner"/>
		</xsl:param>
		<xsl:param name="strStudyDesc">
			<xsl:value-of select="STUDY_strStudyDescription"/>
		</xsl:param>
		<xsl:param name="dtStudyStart">
			<xsl:value-of select="STUDY_dtStudyStartDate"/>
		</xsl:param>
		<xsl:param name="dtStudyEnd">
			<xsl:value-of select="STUDY_dtStudyEndDate"/>
		</xsl:param>
		<xsl:param name="intTargetPatientNo">
			<xsl:value-of select="STUDY_intTargetPatientNumber"/>
		</xsl:param>
		<xsl:param name="intActualPatientNo">
			<xsl:value-of select="intActualPatientNo"/>
		</xsl:param>
		<xsl:param name="STUDY_Timestamp">
			<xsl:value-of select="STUDY_Timestamp"/>
		</xsl:param>
		<table cellpadding="0" cellspacing="0" width="100%" height="100%" border="0">
			<form name="study_view" action="{$baseActionURL}?current=study_view" method="post">
				<tr>
					<td>
						<table width="100%">
							<tr>
								<td class="uportal-channel-subtitle">View study</td>
								<td align="right">
									<!-- IExplorer Hack: since no submit attributes are passed, set "save" parameter -->
									<input type="hidden" name="back" value="not_null"/>
									<!-- Back and Next Buttons -->
									<input class="submit_image_button" src="{$buttonImagePath}/previous_enabled.gif" type="image" name="back" value="{$backBtnLabel}" alt="Previous"
										tabindex="40" onblur="javascript:document.study_view.strStudyName.focus()"/>
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
								<td width="5%"/>
								<td width="15%">
									<a href="{$baseActionURL}?current=study_view&amp;target=study_surveys&amp;intStudyID={$intStudyID}">Smartforms</a>
								</td>
								<td width="5%"/>
								<td width="15%">
									<a href="{$baseActionURL}?current=study_view&amp;target=study_ethics_view&amp;intStudyID={$intStudyID}">Ethics approvals</a>
								</td>
								<td width="5%"/>
								<td width="15%">
									<a href="{$patientChannelURL}?uP_sparam=activeTab&amp;activeTab={$patientChannelTabOrder}&amp;uP_root=root&amp;submit=submit&amp;action=search_patient&amp;newSearch=true&amp;intStudyID={$intStudyID}">Participants</a>
								</td>
								<td width="5%"/>
								<td width="15%">
									<!-- a href="{$baseActionURL}?current=study_view&amp;target=study_permissions&amp;intStudyID={$intStudyID}">Transaction Permissions</a-->
									<a href="{$baseActionURL}?current=study_view&amp;target=study_allocation&amp;intStudyID={$intStudyID}">Biospecimen Allocations</a>
								</td>
								<td width="5%"/>
								<td width="15%">
									<!-- <a href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=3&amp;intStudyID={$intStudyID}&amp;current=biospecimen_search&amp;submit=intStudyID&amp;currentPage=study_view">Biospecimen</a> -->
								</td>
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
									<xsl:value-of select="strError"/>
								</td>
								<td class="neuragenix-form-required-text" width="20%" id="neuragenix-required-header" align="right">
						* = Required fields
						</td>
							</tr>
						</table>
						<table>
							<tr>
								<td width="1%" class="neuragenix-form-required-text">
								<xsl:if test="./STUDY_strStudyNameDisplay[@required='true']">
									*
								</xsl:if> 
								</td>
								<td width="20%" class="uportal-label">
									<xsl:value-of select="STUDY_strStudyNameDisplay"/>: 
						</td>
								<td width="79%" align="left" class="uportal-label" id="neuragenix-form-row-input-input">
									<input type="text" name="strStudyName" size="30" value="{$strStudyName}" class="uportal-input-text" tabindex="1"/>
								</td>
							</tr>
							<xsl:if test="string(STUDY_strStudyCodeDisplay)"> 
								<tr>
									<td width="1%" class="neuragenix-form-required-text">
										<xsl:if test="./STUDY_strStudyCodeDisplay[@required='true']">
											*
										</xsl:if>
									</td>
									<td width="20%" class="uportal-label">
										<xsl:value-of select="STUDY_strStudyCodeDisplay"/>: 
									</td>
									<td width="79%" align="left" class="uportal-label" id="neuragenix-form-row-input-input">
										<input type="text" name="strStudyCode" size="10" value="{$strStudyCode}" class="uportal-input-text" tabindex="11"/>
									</td>
								</tr>
							</xsl:if>
							
							<tr>
								<td width="1%" class="neuragenix-form-required-text">
									<xsl:if test="./STUDY_strStudyOwnerDisplay[@required='true']">
									*
								</xsl:if></td>
								<td width="20%" class="uportal-label">
									<xsl:value-of select="STUDY_strStudyOwnerDisplay"/>: 
						</td>
								<td width="79%" class="uportal-label" id="neuragenix-form-row-input-input">
									<input type="text" name="strStudyOwner" size="30" value="{$strStudyOwner}" class="uportal-input-text" tabindex="20"/>
								</td>
							</tr>
							<tr>
								<td width="1%" class="neuragenix-form-required-text">
									<xsl:if test="./STUDY_dtStudyStartDateDisplay[@required='true']">
										*
									</xsl:if>
								</td>
								
								<td width="20%" class="uportal-label">
									<xsl:value-of select="STUDY_dtStudyStartDateDisplay"/>: 
						</td>
								<td class="uportal-label" width="79%">
									<input type="text" name="dtStudyStart" size="12" value="{$dtStudyStart}" class="uportal-input-text" tabindex="30"/>
								</td>
							</tr>
							<tr>
								<td width="1%" class="neuragenix-form-required-text">
									<xsl:if test="./STUDY_strStudyEndDateDisplay[@required='true']">
										*
									</xsl:if> 
									
								</td>
								<td width="20%" class="uportal-label">
									<xsl:value-of select="STUDY_dtStudyEndDateDisplay"/>: 
						</td>
								<td class="uportal-label" width="79%">
									<input type="text" name="dtStudyEnd" size="12" value="{$dtStudyEnd}" class="uportal-input-text" tabindex="40"/>
								</td>
							</tr>
							<tr>
								<td width="1%" class="neuragenix-form-required-text">
									<xsl:if test="./STUDY_intTargetPatientNumberDisplay[@required='true']">
										*
									</xsl:if>
								</td>
								<td width="20%" class="uportal-label">
									<xsl:value-of select="STUDY_intTargetPatientNumberDisplay"/>: 
						</td>
								<td class="uportal-label" width="79%">
									<input type="text" name="intTargetPatientNo" size="5" value="{$intTargetPatientNo}" class="uportal-input-text" tabindex="50"/>
								</td>
							</tr>
							<tr>
								<td width="1%" class="neuragenix-form-required-text"/>
								<td width="20%" class="uportal-label">
							Actual patient no.: 
						</td>
								<td class="uportal-text" width="79%">
									<xsl:value-of select="intActualPatientNo"/>
								</td>
							</tr>
							<tr>
								<td width="1%" class="neuragenix-form-required-text">
									<xsl:if test="./STUDY_strStudyDescriptionDisplay[@required='true']">
										*
									</xsl:if>
								</td>
								<td width="20%" class="uportal-label">
									<xsl:value-of select="STUDY_strStudyDescriptionDisplay"/>: 
						</td>
								<td class="uportal-label" width="79%">
									<textarea name="strStudyDesc" rows="5" cols="40" class="uportal-input-text" tabindex="60">
										<xsl:value-of select="STUDY_strStudyDescription"/>
									</textarea>
								</td>
							</tr>
						</table>
						<!-- Display save and delete button when the study is editable-->
						<table width="100%">
							<tr>
								<xsl:variable name="readonly" select="readonly"/>
								<xsl:choose>
									<xsl:when test="$readonly='true'">
										<td class="neuragenix-form-required-text" width="20%" id="neuragenix-required-header" align="left">
											<xsl:value-of select="strReadonlyMessage"/>
										</td>
									</xsl:when>
									<xsl:otherwise>
										<td width="21%" class="uportal-label"/>
										<!-- td width="5%" class="uportal-label">
							<input type="button" name="clear" value="{$clearBtnLabel}" class="uportal-button" tabindex="80" onclick="javascript:confirmClear('{$baseActionURL}?current=study_view&amp;intStudyID={$intStudyID}')" />
						</td -->
										<td align="left" width="13%" class="uportal-label">
											<input type="button" name="delete" value="{$deleteBtnLabel}" class="uportal-button" tabindex="90" onclick="javascript:confirmDelete('{$baseActionURL}?current=study_view&amp;intStudyID={$intStudyID}')"/>
										</td>
										<td align="right" width="14%" class="uportal-label">
											<input type="submit" name="save" value="{$saveBtnLabel}" class="uportal-button" tabindex="70" onblur="javascript:document.study_view.strStudyName.focus()"/>
										</td>
										<td width="52%" class="uportal-label"/>
									</xsl:otherwise>
								</xsl:choose>
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
									<input class="submit_image_button" src="{$buttonImagePath}/previous_enabled.gif" type="image" name="back" value="{$backBtnLabel}" alt="Previous"
										tabindex="40" onblur="javascript:document.study_view.strStudyName.focus()"/>
									<img src="{$buttonImagePath}/next_disabled.gif" border="0" alt="Next"/>
								</td>
							</tr>
						</table>
						<!-- add the study Internal ID as a hidden field to be submitted to the channel-->
						<input type="hidden" name="intStudyID" value="{$intStudyID}"/>
						<!--input type="hidden" name="STUDY_Timestamp" value="{$STUDY_Timestamp}"/-->
					</td>
				</tr>
			</form>
		</table>
	</xsl:template>
</xsl:stylesheet>
