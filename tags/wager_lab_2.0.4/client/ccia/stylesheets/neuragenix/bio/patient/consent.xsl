<?xml version="1.0" encoding="utf-8"?>
<!--
    Document   : patient_consent.xml
    Copyright : 2003 Neuragenix, Inc .  All rights reserved.
    Created on :    
    Author     :
    
    Modified on: 11/02/2004
    Author     : Anita Balraj
    Description: Edit Consent Study and Secure Download Implementation

	Version: $Id: consent.xsl,v 1.15 2005/04/27 08:11:13 renny Exp $
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./patient_menu.xsl"/>
	<xsl:include href="./infopanel.xsl"/>
	<xsl:include href="../../common/common_btn_name.xsl"/>
	<xsl:output method="html" indent="no"/>
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
	<!-- Start Secure Download Implementation -->
	<!-- End Secure -->
	<xsl:template match="patient">
		<!-- Call Infopanel Template which will Call "infopanel_content" Template -->
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
		<xsl:variable name="PATIENT_strPatientID">
			<xsl:value-of select="/body/patient/PATIENT_strPatientID"/>
		</xsl:variable>
		<xsl:variable name="PATIENT_strPatientFirstName">
			<xsl:value-of select="/body/patient/PATIENT_strFirstName"/>
		</xsl:variable>
		<xsl:variable name="PATIENT_strPatientSurname">
			<xsl:value-of select="/body/patient/PATIENT_strSurname"/>
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
		<xsl:variable name="CONSENTSTUDY_strConsentVersion">
			<xsl:value-of select="CONSENTSTUDY_strConsentVersion"/>
		</xsl:variable>
		<xsl:variable name="CONSENTSTUDY_strCommentsDisplay">
			<xsl:value-of select="CONSENTSTUDY_strCommentsDisplay"/>
		</xsl:variable>
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
		<form name="consent_form" action="{$baseActionURL}?uP_root=root&amp;action=consent&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}" method="POST">
			<table>
				<tr>
					<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"/>
					<td class="uportal-channel-table-header">Consent</td>
					<td/>
				</tr>
				<tr>
					<td height="10px">
						<xsl:variable name="CONSENT_intConsentKey">
							<xsl:value-of select="CONSENT_intConsentKey"/>
						</xsl:variable>
						<input type="hidden" value="{$CONSENT_intConsentKey}" name="CONSENT_intConsentKey"/>
						<input type="hidden" value="{$PATIENT_intInternalPatientID}" name="CONSENT_intInternalPatientID"/>
					</td>
				</tr>
				<tr>
					<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"/>
					<td class="uportal-label">
						<xsl:value-of select="CONSENT_strFutureStudyDisplay"/>:
					</td>
					<td class="uportal-label">
						<select name="CONSENT_strFutureStudy" class="uportal-input-text" tabindex="31">
							<xsl:for-each select="CONSENT_strFutureStudy">
								<xsl:variable name="CONSENT_strFutureStudy">
									<xsl:value-of select="."/>
								</xsl:variable>
								<option value="{$CONSENT_strFutureStudy}">
									<xsl:if test="@selected = '1'">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>
					</td>
				</tr>
				<tr>
					<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"/>
					<td class="uportal-label">
						<xsl:value-of select="CONSENT_strContactOKDisplay"/>:
					</td>
					<td class="uportal-label">
						<select name="CONSENT_strContactOK" class="uportal-input-text" tabindex="32">
							<xsl:for-each select="CONSENT_strContactOK">
								<xsl:variable name="CONSENT_strContactOK">
									<xsl:value-of select="."/>
								</xsl:variable>
								<option value="{$CONSENT_strContactOK}">
									<xsl:if test="@selected = '1'">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>
					</td>
				</tr>
			</table>
			<table>
				<br/>
				<tr>
					<td width="1%" id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"/>
					<td width="17%" class="uportal-label">
						<xsl:value-of select="CONSENT_strCommentsDisplay"/>:
					</td>
					<td width="83%" class="uportal-label">
						<textarea name="CONSENT_strComments" rows="3" cols="33" tabindex="33" class="uportal-input-text">
							<xsl:value-of select="CONSENT_strComments"/>
						</textarea>
					</td>
				</tr>
				<tr>
					<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"/>
					<td class="uportal-label">
						<input type="submit" name="updateContactConsent" tabindex="34" value="Update consent" class="uportal-button" onblur="javascript:document.consent_form.CONSENT_strFutureStudy.focus()"/>
					</td>
					<td class="uportal-label"/>
				</tr>
			</table>
		</form>
		<hr/>
		<table width="100%">
			<tr>
				<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"/>
				<td class="uportal-channel-table-header">Projects consented to</td>
				<td class="neuragenix-form-required-text" width="20%" align="right">
			* = Required fields
			</td>
			</tr>
		</table>
		<table border="0" width="100%" cellspacing="0">
			<tr>
				<td height="10px"/>
			</tr>
			<tr>
				<td width="5%" class="uportal-label">
					<xsl:value-of select="CONSENTSTUDY_strConsentVersionDisplay"/>
				</td>
				<td width="12%" class="uportal-label">
					<xsl:value-of select="CONSENTSTUDY_intStudyIDDisplay"/>
				</td>
				<td width="10%" class="uportal-label">
					<xsl:value-of select="CONSENTSTUDY_strApprovedDisplay"/>
				</td>
				<td width="10%" class="uportal-label">
					<xsl:value-of select="CONSENTSTUDY_dtApprovedDateDisplay"/>
				</td>
				<td width="15%" class="uportal-label">
					<xsl:value-of select="CONSENTSTUDY_strResearcherDisplay"/>
				</td>
				<td width="5%" class="uportal-label">&#160;</td>
				<td width="5%" class="uportal-label">&#160;</td>
				<td width="5%" class="uportal-label">&#160;</td>
			</tr>
			<xsl:for-each select="consent_study">
				<xsl:variable name="CONSENTSTUDY_intConsentStudyKey">
					<xsl:value-of select="CONSENTSTUDY_intConsentStudyKey"/>
				</xsl:variable>
				<xsl:variable name="CONSENTSTUDY_strConsentVersion">
					<xsl:value-of select="CONSENTSTUDY_strConsentVersion"/>
				</xsl:variable>
				<xsl:variable name="CONSENTSTUDY_strApproved">
					<xsl:value-of select="CONSENTSTUDY_strApproved"/>
				</xsl:variable>
				<xsl:variable name="CONSENTSTUDY_intApproved">
					<xsl:value-of select="CONSENTSTUDY_intApproved"/>
				</xsl:variable>
				<xsl:variable name="CONSENTSTUDY_strFileName">
					<xsl:value-of select="CONSENTSTUDY_strFileName"/>
				</xsl:variable>
				<xsl:variable name="CONSENTSTUDY_intStudyID">
					<xsl:value-of select="CONSENTSTUDY_intStudyID"/>
				</xsl:variable>
				<xsl:variable name="STUDY_strStudyName">
					<xsl:value-of select="STUDY_strStudyName"/>
				</xsl:variable>
				<xsl:variable name="CONSENT_STUDY_Timestamp">
					<xsl:value-of select="CONSENT_STUDY_Timestamp"/>
				</xsl:variable>
				<tr class="uportal-input-text" valign="top">
					<xsl:choose>
						<xsl:when test="position() mod 2 != 0">
							<xsl:attribute name="class">stripped_dark</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="class">stripped_light</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>
					<td width="5%">
						<xsl:value-of select="CONSENTSTUDY_strConsentVersion"/>
					</td>
					<td width="12%">
                                            <xsl:variable name="formname">consent_<xsl:value-of select="$CONSENTSTUDY_intStudyID"/></xsl:variable>
                                            <form name="{$formname}" action="{$smartformURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformTab}&amp;domain=Study&amp;participant={$PATIENT_intInternalPatientID}&amp;intStudyID={$CONSENTSTUDY_intStudyID}&amp;action=consent&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;strOrigin=patient_result" method="post">>
                                                <xsl:variable name="url">domain=Study&amp;participant={$PATIENT_intInternalPatientID}&amp;intStudyID={$CONSENTSTUDY_intStudyID}&amp;action=consent&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;strOrigin=patient_result</xsl:variable>
                                               <input type="hidden" value="{$PATIENT_strPatientFirstName}" name="var1"/>
                                                <input type="hidden" value="{$PATIENT_strPatientSurname}" name="var2"/>
                                                <input type="hidden" value="consentsmartforms" name="formname"/>
                                                <input type="hidden" value="{$PATIENT_strPatientID}" name="title"/>
                                                <a href="javascript:submitThis('{$formname}');">
							<xsl:value-of select="STUDY_strStudyName"/>
						</a>
                                            </form>
					</td>
					<td width="10%">
						<xsl:value-of select="CONSENTSTUDY_strApproved"/>
					</td>
					<td width="10%">
						<xsl:value-of select="CONSENTSTUDY_dtApprovedDate"/>
					</td>
					<td width="15%">
						<xsl:value-of select="CONSENTSTUDY_strResearcher"/>
					</td>
					<td>
						<!-- File Attach Button -->
						<xsl:choose>
							<xsl:when test="string-length( $CONSENTSTUDY_intConsentStudyKey ) > '0'">
								<a href="{$baseActionURL}?uP_root=root&amp;action=consent&amp;addFile=true&amp;CONSENTSTUDY_intConsentStudyKey={$CONSENTSTUDY_intConsentStudyKey}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">Attach&#160;file</a>
							</xsl:when>
							<xsl:otherwise>&#160;</xsl:otherwise>
						</xsl:choose>
					</td>
					<td>
						<!-- File View Button -->
						<xsl:choose>
							<xsl:when test="string-length( $CONSENTSTUDY_strFileName ) > '0'">
								<a href="{$downloadURL}?uP_root={$downloadNodeId}&amp;domain=CONSENTSTUDY&amp;primary_field=CONSENTSTUDY_intConsentStudyKey&amp;primary_value={$CONSENTSTUDY_intConsentStudyKey}&amp;file_name_field=CONSENTSTUDY_strFileName&amp;property_name=neuragenix.bio.patient.SaveConsentLocation&amp;activity_required=patient_consent" target="_blank">View&#160;file</a>
							</xsl:when>
							<xsl:otherwise>&#160;</xsl:otherwise>
						</xsl:choose>
					</td>
					<td>&#160;</td>
				</tr>
				<tr class="uportal-input-text" valign="top">
					<xsl:choose>
						<xsl:when test="position() mod 2 != 0">
							<xsl:attribute name="class">stripped_dark</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="class">stripped_light</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>
					<td>&#160;</td>
					<td colspan="4" align="right">
						<table cellpadding="0" cellspacing="0">
							<tr valign="top">
								<td class="uportal-label" width="100" align="left"><xsl:value-of select="$CONSENTSTUDY_strCommentsDisplay"/>&#160;:&#160;</td>
								<td width="100%" align="left"><xsl:value-of select="CONSENTSTUDY_strComments"/></td>
							</tr>
						</table>
					</td>
					<td>&#160;</td>
					<td>&#160;</td>
					<td align="right">
						<xsl:choose>
							<xsl:when test="string-length( $CONSENTSTUDY_intConsentStudyKey ) > '0'">
								<!-- Edit and Delete Buttons -->
								<a href="{$baseActionURL}?uP_root=root&amp;action=consent&amp;editConsent=true&amp;CONSENTSTUDY_intConsentStudyKey={$CONSENTSTUDY_intConsentStudyKey}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}"><img src="{$buttonImagePath}/edit_icon_enabled.gif" alt="Edit" border="0"/></a>&#160;<a href="javascript:confirmDelete('{$baseActionURL}?uP_root=root&amp;action=consent&amp;deleteConsent=true&amp;CONSENTSTUDY_intStudyID={$CONSENTSTUDY_intStudyID}&amp;CONSENTSTUDY_intConsentStudyKey={$CONSENTSTUDY_intConsentStudyKey}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}')"><img src="{$buttonImagePath}/delete_icon_enabled.gif" alt="Delete" border="0"/></a>
							</xsl:when>
							<xsl:otherwise>&#160;</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:for-each>
			<!-- Start Edit Consent Study - Added "and" condition, to load all dropdowns for consent study,in new mode-->
			<xsl:if test="(count( study ) > 0 ) or ( string-length($strEdit) > 0)">
				<tr>
					<td><img src="{$infopanelImagePath}/spacer.gif" border="0" width="1" height="5"/></td>
					<td><img src="{$infopanelImagePath}/spacer.gif" border="0" width="1" height="5"/></td>
					<td><img src="{$infopanelImagePath}/spacer.gif" border="0" width="1" height="5"/></td>
					<td><img src="{$infopanelImagePath}/spacer.gif" border="0" width="140" height="5"/></td>
					<td><img src="{$infopanelImagePath}/spacer.gif" border="0" width="1" height="5"/></td>
					<td><img src="{$infopanelImagePath}/spacer.gif" border="0" width="1" height="5"/></td>
					<td><img src="{$infopanelImagePath}/spacer.gif" border="0" width="1" height="5"/></td>
					<td><img src="{$infopanelImagePath}/spacer.gif" border="0" width="1" height="5"/></td>
				</tr>
				<form action="{$baseActionURL}?uP_root=root&amp;action=consent&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}" method="POST">
					<tr valign="top">
						<td width="5%" class="uportal-label" valign="top">
							<select name="CONSENTSTUDY_strConsentVersion" tabindex="35" class="uportal-input-text">
								<xsl:for-each select="CONSENTSTUDY_strConsentVersion">
									<xsl:variable name="CONSENTSTUDY_strConsentVersion">
										<xsl:value-of select="."/>
									</xsl:variable>
									<option value="{$CONSENTSTUDY_strConsentVersion}">
										<xsl:if test="@selected = '1'">
											<xsl:attribute name="selected">true</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="."/>
									</option>
								</xsl:for-each>
							</select>
						</td>
                                                
                                                <td width="12%" class="uportal-label" valign="top">
                                                        <xsl:variable name="default_study" >
                                                            <xsl:value-of select="//default_study"/>
                                                        </xsl:variable>
							<input type="hidden" name="CONSENTSTUDY_intConsentKey" value="{$CONSENT_intConsentKey}"/>
							<input type="hidden" name="CONSENT_intConsentKey" value="{$CONSENT_intConsentKey}"/>
							<xsl:choose>
								<xsl:when test="string-length( $strEdit ) = '0'">
									<select class="uportal-input-text"  tabindex="36" name="CONSENTSTUDY_intStudyID">
										<xsl:for-each select="study">
											<xsl:sort select="STUDY_strStudyName"/>
											<xsl:variable name="varIntStudyID">
												<xsl:value-of select="STUDY_intStudyID"/>
											</xsl:variable>
                                                                                        <xsl:variable name="studyName">
												<xsl:value-of select="STUDY_strStudyName"/>
											</xsl:variable>
											<option>
												<xsl:attribute name="value"><xsl:value-of select="STUDY_intStudyID"/></xsl:attribute>
												<xsl:if test="$default_study=$studyName">
													<xsl:attribute name="selected">true</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="STUDY_strStudyName"/>
											</option>
										</xsl:for-each>
									</select>
								</xsl:when>
								<xsl:otherwise>
									<xsl:variable name="varIntStudyID">
										<xsl:value-of select="CONSENTSTUDY_intStudyID"/>
									</xsl:variable>
									<input type="hidden" name="CONSENTSTUDY_intStudyID" value="{$varIntStudyID}"/>
									<input type="hidden" name="CONSENTSTUDY_intConsentStudyKey" value="{$CONSENTSTUDY_intConsentStudyKey}"/>
									<xsl:for-each select="study">
										<xsl:variable name="varIntStudyIDSelect">
											<xsl:value-of select="STUDY_intStudyID"/>
										</xsl:variable>
										<xsl:if test="$varIntStudyID = $varIntStudyIDSelect">
											<xsl:value-of select="STUDY_strStudyName"/>
										</xsl:if>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</td> 
                                                <!--
						<td width="12%" class="uportal-label" valign="top">
							<input type="hidden" name="CONSENTSTUDY_intConsentKey" value="{$CONSENT_intConsentKey}"/>
							<input type="hidden" name="CONSENT_intConsentKey" value="{$CONSENT_intConsentKey}"/>
							<xsl:choose>
								<xsl:when test="string-length( $strEdit ) = '0'">
									<select class="uportal-input-text"  tabindex="36" name="CONSENTSTUDY_intStudyID">
										<xsl:for-each select="study">
											<xsl:sort select="STUDY_strStudyName"/>
											<xsl:variable name="varIntStudyID">
												<xsl:value-of select="STUDY_intStudyID"/>
											</xsl:variable>
											<option>
												<xsl:attribute name="value"><xsl:value-of select="STUDY_intStudyID"/></xsl:attribute>
												<xsl:if test="@selected = '1'">
													<xsl:attribute name="selected">true</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="STUDY_strStudyName"/>
											</option>
										</xsl:for-each>
									</select>
								</xsl:when>
								<xsl:otherwise>
									<xsl:variable name="varIntStudyID">
										<xsl:value-of select="CONSENTSTUDY_intStudyID"/>
									</xsl:variable>
									<input type="hidden" name="CONSENTSTUDY_intStudyID" value="{$varIntStudyID}"/>
									<input type="hidden" name="CONSENTSTUDY_intConsentStudyKey" value="{$CONSENTSTUDY_intConsentStudyKey}"/>
									<xsl:for-each select="study">
										<xsl:variable name="varIntStudyIDSelect">
											<xsl:value-of select="STUDY_intStudyID"/>
										</xsl:variable>
										<xsl:if test="$varIntStudyID = $varIntStudyIDSelect">
											<xsl:value-of select="STUDY_strStudyName"/>
										</xsl:if>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</td> -->
						<td width="10%" class="uportal-label" valign="top">
							<select name="CONSENTSTUDY_strApproved" tabindex="39" class="uportal-input-text">
								<xsl:for-each select="CONSENTSTUDY_strApproved">
									<xsl:variable name="CONSENTSTUDY_strApproved">
										<xsl:value-of select="."/>
									</xsl:variable>
									<option value="{$CONSENTSTUDY_strApproved}">
										<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
										<xsl:if test="@selected = '1'">
											<xsl:attribute name="selected">true</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="."/>
									</option>
								</xsl:for-each>
							</select>
						</td>
						<td width="10%" class="uportal-label" valign="top">
							<xsl:choose>
								<xsl:when test="CONSENTSTUDY_dtApprovedDate/@display_type='dropdown'">
									<select name="CONSENTSTUDY_dtApprovedDate_Day" tabindex="41" class="uportal-input-text">
										<xsl:for-each select="CONSENTSTUDY_dtApprovedDate_Day">
											<xsl:variable name="CONSENTSTUDY_dtApprovedDate_Day">
												<xsl:value-of select="."/>
											</xsl:variable>
											<option value="{$CONSENTSTUDY_dtApprovedDate_Day}">
												<xsl:if test="@selected = '1'">
													<xsl:attribute name="selected">true</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="."/>
											</option>
										</xsl:for-each>
									</select>
									<select name="CONSENTSTUDY_dtApprovedDate_Month" tabindex="42" class="uportal-input-text">
										<xsl:for-each select="CONSENTSTUDY_dtApprovedDate_Month">
											<xsl:variable name="CONSENTSTUDY_dtApprovedDate_Month">
												<xsl:value-of select="."/>
											</xsl:variable>
											<option value="{$CONSENTSTUDY_dtApprovedDate_Month}">
												<xsl:if test="@selected = '1'">
													<xsl:attribute name="selected">true</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="."/>
											</option>
										</xsl:for-each>
									</select>
								</xsl:when>
								<xsl:otherwise>
									<input type="text" name="CONSENTSTUDY_dtApprovedDate_Day" size="2" tabindex="41" class="uportal-input-text">
										<xsl:attribute name="value"><xsl:value-of select="CONSENTSTUDY_dtApprovedDate_Day"/></xsl:attribute>
									</input>
									<input type="text" name="CONSENTSTUDY_dtApprovedDate_Month" size="2" tabindex="42" class="uportal-input-text">
										<xsl:attribute name="value"><xsl:value-of select="CONSENTSTUDY_dtApprovedDate_Month"/></xsl:attribute>
									</input>
								</xsl:otherwise>
							</xsl:choose>
							<input type="text" name="CONSENTSTUDY_dtApprovedDate_Year" size="5" tabindex="43" class="uportal-input-text">
								<xsl:attribute name="value"><xsl:value-of select="CONSENTSTUDY_dtApprovedDate_Year"/></xsl:attribute>
							</input>
						</td>
						<td width="15%" class="uportal-label" valign="top">
							<select name="CONSENTSTUDY_strResearcher" tabindex="47" class="uportal-input-text">
								<xsl:for-each select="CONSENTSTUDY_strResearcher">
									<xsl:variable name="CONSENTSTUDY_strResearcher">
										<xsl:value-of select="."/>
									</xsl:variable>
									<option value="{$CONSENTSTUDY_strResearcher}">
										<xsl:if test="@selected = '1'">
											<xsl:attribute name="selected">true</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="."/>
									</option>
								</xsl:for-each>
							</select>
						</td>
						<td>&#160;</td>
						<td>&#160;</td>
						<td>&#160;</td>
					</tr>
					<tr valign="top">
						<td>&#160;</td>
						<td colspan="4" align="right">
							<table cellpadding="0" cellspacing="0">
								<tr valign="top">
									<td class="uportal-label" width="100" align="left"><xsl:value-of select="CONSENTSTUDY_strCommentsDisplay"/>&#160;:&#160;</td>
									<td width="100%" align="left">
										<textarea name="CONSENTSTUDY_strComments" rows="3" cols="70" tabindex="50" class="uportal-input-text">
											<xsl:value-of select="CONSENTSTUDY_strComments"/>
										</textarea>
									</td>
								</tr>
							</table>
						</td>
						<td>&#160;</td>
						<td>&#160;</td>
					</tr>
					<tr>
						<td colspan="14" align="right">
							<table width="100%">
								<tr>
									<!-- Cancel button -->
									<td width="50%" class="uportal-label" align="left">
										<input type="submit" name="cancel" value="Clear" class="uportal-button"/>
									</td>
									<!-- Save Button -->
									<td width="50%" class="uportal-label" align="right">
										<input type="submit" value="Save" name="saveConsent" tabindex="55" class="uportal-button"/>
										<input type="hidden" name="PATIENT_intInternalPatientID" value="{$PATIENT_intInternalPatientID}"/>
										<input type="hidden" name="strEdit" value="{strEdit}"/>
										<input type="hidden" name="CONSENTSTUDY_strFileName" value="{$CONSENTSTUDY_strFileName}"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</form>
			</xsl:if>
			<!-- End Edit Consent Study-->
		</table>
		<script language="javascript">
			document.consent_form.CONSENT_strFutureStudy.focus();
		</script>
	</xsl:template>
</xsl:stylesheet>
