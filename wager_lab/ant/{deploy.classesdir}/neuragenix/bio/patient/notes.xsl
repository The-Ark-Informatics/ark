<?xml version="1.0" encoding="utf-8"?>
<!--
	Version: $Id: notes.xsl,v 1.7 2005/03/24 07:05:50 kleong Exp $
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./patient_menu.xsl"/>
	<xsl:include href="./infopanel.xsl"/>
	<xsl:include href="../../common/common_btn_name.xsl"/>
	<xsl:output method="html" indent="no"/>
	<xsl:template match="patient">
		<!-- Call Infopanel Template which will Call "patient_content" Template -->
		<xsl:call-template name="infopanel">
			<xsl:with-param name="activeSubtab">notes</xsl:with-param>
			<xsl:with-param name="PATIENT_intInternalPatientID">
				<xsl:value-of select="PATIENT_intInternalPatientID"/>
			</xsl:with-param>
			<xsl:with-param name="contentTemplate">patient_content</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="infopanel_content">
		<xsl:variable name="intInternalPatientID">
			<xsl:value-of select="PATIENT_intInternalPatientID"/>
		</xsl:variable>
		<xsl:variable name="PATIENT_intInternalPatientID">
			<xsl:value-of select="PATIENT_intInternalPatientID"/>
		</xsl:variable>
		<xsl:if test="count(note) &gt; 0">
			<table width="100%">
				<tr>
					<td class="uportal-channel-subtitle">
			Notes<br/>
						<hr/>
					</td>
				</tr>
			</table>
			<table width="100%" cellspacing="0">
				<tr>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="10%" class="uportal-label">
						<xsl:value-of select="NOTES_strNoteNameDisplay"/>
					</td>
					<td width="20%" class="uportal-label">
						<xsl:value-of select="NOTES_strNoteTypeDisplay"/>
					</td>
					<td width="20%" class="uportal-label">
						<xsl:value-of select="NOTES_dtDateDisplay"/>
					</td>
					<td width="39%" class="uportal-label">
						<xsl:value-of select="NOTES_strDescriptionDisplay"/>
					</td>
					<td width="5%" class="uportal-label">File</td>
					<td width="5%" class="uportal-label">Delete</td>
				</tr>
				<tr>
					<td colspan="7">
						<hr/>
					</td>
				</tr>
				<xsl:for-each select="note">
					<xsl:variable name="strPatientID">
						<xsl:value-of select="ATTACHMENTS_strID"/>
					</xsl:variable>
					<xsl:variable name="NOTES_intNotesKey">
						<xsl:value-of select="NOTES_intNotesKey"/>
					</xsl:variable>
					<xsl:variable name="NOTES_strFileName">
						<xsl:value-of select="NOTES_strFileName"/>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="position() mod 2 != 0">
							<tr class="uportal-input-text">
								<td width="1%" class="neuragenix-form-required-text"/>
								<td width="10%" class="uportal-input-text">
									<xsl:value-of select="NOTES_strNoteName"/>
								</td>
								<td width="20%" class="uportal-input-text">
									<xsl:value-of select="NOTES_strNoteType"/>
								</td>
								<td width="20%" class="uportal-input-text">
									<xsl:value-of select="NOTES_dtDate"/>
								</td>
								<td width="39%" class="uportal-input-text">
									<xsl:value-of select="NOTES_strDescription"/>
								</td>
								<td width="5%" class="uportal-input-text">
									<xsl:if test="string-length( NOTES_strFileName ) > 0">
										<a href="{$downloadURL}?uP_root={$downloadNodeId}&amp;domain=NOTES&amp;primary_field=NOTES_intNotesKey&amp;primary_value={$NOTES_intNotesKey}&amp;file_name_field=NOTES_strFileName&amp;property_name=neuragenix.bio.patient.SaveNotesLocation&amp;activity_required=patient_notes" target="_blank">view </a>
									</xsl:if>
								</td>
								<!-- End Secure Download Implementation -->
								<td width="5%" class="uportal-input-text">
									<a href="javascript:confirmDelete('{$baseActionURL}?uP_root=root&amp;action=notes&amp;deleteNotes=true&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;NOTES_intNotesKey={$NOTES_intNotesKey}')"> del</a>
								</td>
							</tr>
						</xsl:when>
						<xsl:otherwise>
							<tr class="uportal-text">
								<td width="1%" class="neuragenix-form-required-text"/>
								<td width="10%" class="uportal-text">
									<xsl:value-of select="NOTES_strNoteName"/>
								</td>
								<td width="20%" class="uportal-text">
									<xsl:value-of select="NOTES_strNoteType"/>
								</td>
								<td width="20%" class="uportal-text">
									<xsl:value-of select="NOTES_dtDate"/>
								</td>
								<td width="39%" class="uportal-text">
									<xsl:value-of select="NOTES_strDescription"/>
								</td>
								<td width="5%" class="uportal-text">
									<xsl:if test="string-length( NOTES_strFileName ) > 0">
										<a href="{$downloadURL}?uP_root={$downloadNodeId}&amp;domain=NOTES&amp;primary_field=NOTES_intNotesKey&amp;primary_value={$NOTES_intNotesKey}&amp;file_name_field=NOTES_strFileName&amp;property_name=neuragenix.bio.patient.SaveNotesLocation&amp;activity_required=patient_notes" target="_blank">view </a>
									</xsl:if>
								</td>
								<!-- End Secure Download Implementation -->
								<td width="5%" class="uportal-text">
									<a href="javascript:confirmDelete('{$baseActionURL}?uP_root=root&amp;action=notes&amp;deleteNotes=true&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;NOTES_intNotesKey={$NOTES_intNotesKey}')"> del</a>
								</td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</table>
			<hr/>
		</xsl:if>
		<form name="note_form" action="{$baseActionURL}?uP_root=root&amp;action=notes" method="post" enctype="multipart/form-data">
			<table width="100%">
				<tr>
					<td class="neuragenix-form-required-text">
						<xsl:value-of select="error"/>
					</td>
				</tr>
				<tr>
					<td class="uportal-channel-subtitle">
                    Add note<br/>
					</td>
				</tr>
			</table>
			<table width="100%">
				<xsl:variable name="NOTES_strNoteName">
					<xsl:value-of select="NOTES_strNoteName"/>
				</xsl:variable>
				<xsl:variable name="NOTES_strNoteType">
					<xsl:value-of select="NOTES_strNoteType"/>
				</xsl:variable>
				<xsl:variable name="NOTES_strDescription">
					<xsl:value-of select="NOTES_strDescription"/>
				</xsl:variable>
				<xsl:variable name="NOTES_dtDate_Day">
					<xsl:value-of select="NOTES_dtDate_Day"/>
				</xsl:variable>
				<xsl:variable name="NOTES_dtDate_Month">
					<xsl:value-of select="NOTES_dtDate_Month"/>
				</xsl:variable>
				<xsl:variable name="NOTES_dtDate_Year">
					<xsl:value-of select="NOTES_dtDate_Year"/>
				</xsl:variable>
				<tr>
					<td class="uportal-label" colspan="3">
				
			</td>
				</tr>
				<tr>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td class="uportal-label" valign="top">
						<xsl:value-of select="NOTES_strNoteNameDisplay"/>:
			</td>
					<td>
						<input type="text" name="NOTES_strNoteName" size="20" tabindex="21" value="{$NOTES_strNoteName}" class="uportal-input-text"/>
					</td>
				</tr>
				<tr>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td class="uportal-label" valign="top">
						<xsl:value-of select="NOTES_strNoteTypeDisplay"/>:
			</td>
					<td>
						<select name="NOTES_strNoteType" tabindex="22" class="uportal-input-text">
							<xsl:for-each select="NOTES_strNoteType">
								<option>
									<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
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
					<td width="1%" class="neuragenix-form-required-text">*</td>
					<td class="uportal-label" valign="top">
						<xsl:value-of select="NOTES_dtDateDisplay"/>:
			</td>
					<td>
						<xsl:choose>
							<xsl:when test="NOTES_dtDate/@display_type='dropdown'">
								<select name="NOTES_dtDate_Day" tabindex="23" class="uportal-input-text">
									<xsl:for-each select="NOTES_dtDate_Day">
										<option>
											<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
											<xsl:if test="@selected = '1'">
												<xsl:attribute name="selected">true</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</option>
									</xsl:for-each>
								</select>
								<select name="NOTES_dtDate_Month" tabindex="24" class="uportal-input-text">
									<option value=""/>
									<xsl:for-each select="NOTES_dtDate_Month">
										<option>
											<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
											<xsl:if test="@selected = '1'">
												<xsl:attribute name="selected">true</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</option>
									</xsl:for-each>
								</select>
							</xsl:when>
							<xsl:otherwise>
								<input type="text" name="NOTES_dtDate_Day" size="2" tabindex="23" class="uportal-input-text">
									<xsl:attribute name="value"><xsl:value-of select="NOTES_dtDate_Day"/></xsl:attribute>
								</input>
								<input type="text" name="NOTES_dtDate_Month" size="2" tabindex="24" class="uportal-input-text">
									<xsl:attribute name="value"><xsl:value-of select="NOTES_dtDate_Month"/></xsl:attribute>
								</input>
							</xsl:otherwise>
						</xsl:choose>
						<input type="text" name="NOTES_dtDate_Year" size="4" tabindex="25" class="uportal-input-text">
							<xsl:attribute name="value"><xsl:value-of select="NOTES_dtDate_Year"/></xsl:attribute>
						</input>
					</td>
				</tr>
				<tr>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td class="uportal-label" valign="top">
						<xsl:value-of select="NOTES_strDescriptionDisplay"/>:
			</td>
					<td>
						<textarea name="NOTES_strDescription" rows="4" cols="30" tabindex="26" class="uportal-input-text">
							<xsl:value-of select="NOTES_strDescription"/>
						</textarea>
					</td>
				</tr>
				<tr>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td class="uportal-label" height="5px">
                            File:
			</td>
					<td>
						<input type="file" name="NOTES_strFileName" class="uportal-input-text" tabindex="27"/>
					</td>
				</tr>
				<tr>
					<td class="uportal-label" colspan="2">
						<input type="reset" name="cancel" value="{$cancelBtnLabel}" class="uportal-button"/>
						<input type="hidden" name="NOTES_strDomain" value="Patient" class="uportal-input-text"/>
						<input type="hidden" name="PATIENT_intInternalPatientID" value="{$PATIENT_intInternalPatientID}"/>
						<input type="hidden" name="NOTES_intID" value="{$PATIENT_intInternalPatientID}"/>
					</td>
					<td class="uportal-label" align="center">
						<input type="submit" name="saveNotes" value="{$saveBtnLabel}" tabindex="28" class="uportal-button" onblur="javascript:document.note_form.NOTES_strNoteName.focus()"/>
					</td>
				</tr>
			</table>
		</form>
		<script language="javascript">
            document.note_form.NOTES_strNoteName.focus();
         </script>
	</xsl:template>
</xsl:stylesheet>
