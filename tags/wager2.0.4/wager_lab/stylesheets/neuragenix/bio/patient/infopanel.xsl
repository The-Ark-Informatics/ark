<?xml version="1.0" encoding="UTF-8"?>
<!--
	Information Panel HTML Generator for Biogenix v4.5 Patient Screens

	Description:
		This file contains the "infopanel" template which generates HTML
		for the information panels (i.e. panels on the right) in the patient screens.
		It is included by other stylesheets which passes the following parameters
		to the template:

		PATIENT_intInternalPatientID -
			Internal patient ID used to generate links on each subtab.

		activeSubtab -
			Symbol indicating which subtab is active. This may be "details,"
			"collection," "consent," "followUp," "biospecimen" or "notes."

		The template generates the frame and calls the "infopanel_template"
		template to generate the frame content.

	Version: $Id: infopanel.xsl,v 1.11 2005/04/05 01:08:54 daniel Exp $
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- Defined Image File Paths -->
	<xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
	<xsl:variable name="subtabImagePath">media/neuragenix/infopanel</xsl:variable> <!-- Replace with infopanelImagePath -->
	<!--
		Description:
			This template generates HTML for the information panels (i.e. panels on the right)
			on the patient screens, including the border, title bar and patient subtabs, if required.
			It is included by other stylesheets which pass the following parameters
			to the template:

		Parameters:
			activeSubtab - (Optional)
				Symbol indicating which subtab is active, or if not passed or set to an empty
				string, it indicates that subtabs should not be generated. Valid values are
				"details," "collection," "consent," "followUp," "biospecimen" and "notes."

			titleString - (Optional)
				Title to display on the title bar. This defaults to the patient ID, if specified.

			PATIENT_intInternalPatientID - (Optional)
				Internal patient ID used to generate links on each subtab.
				Only required if subtabs are generated.

			previousButtonFlag - (Optional)
				Set to "true" if the previous button is to be enabled or "false" if not.
				"true" is the default value.

			previousButtonUrl - (Optional)
				If specified, this determines the link URL for the "Previous" button.

	-->
	<xsl:template name="infopanel">
		<xsl:param name="activeSubtab"/>
		<xsl:param name="titleString"/>
		<xsl:param name="PATIENT_intInternalPatientID"/>
		<xsl:param name="previousButtonFlag">true</xsl:param>
		<xsl:param name="previousButtonUrl"/>
                
		<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
			<!-- Top Button Bar -->
			<tr height="28" valign="middle">
				<td align="left">
					<!--
					<a href="{$baseActionURL}?uP_root=root&amp;action=add_patient">
						<img border="0" src="{$buttonImagePath}/add_new_patient.gif" alt="Add New Patient"/>
					</a>-->
				</td>
				<td align="right">
					<!-- "Previous" Button -->
					<xsl:choose>
						<xsl:when test="$previousButtonFlag='true'">
							<xsl:choose>
								<xsl:when test="$previousButtonUrl=''">
									<a href="{$baseActionURL}?{$action}&amp;back=true&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
										<img border="0" src="{$buttonImagePath}/previous_enabled.gif" alt="Previous"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="{$previousButtonUrl}">
										<img border="0" src="{$buttonImagePath}/previous_enabled.gif" alt="Previous"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<img border="0" src="{$buttonImagePath}/previous_disabled.gif" alt="Next"/>
						</xsl:otherwise>
					</xsl:choose>
					<!-- "Next" Button -->
					<img border="0" src="{$buttonImagePath}/next_disabled.gif" alt="Next"/>
				</td>
			</tr>
			<tr class="channel_button_strip_separator">
				<td width="100%" colspan="2">
					<img border="0" width="1" height="1" src="{$infopanelImagePath}/spacer.gif"/>
				</td>
			</tr>
			<tr>
				<td width="100%" colspan="2">
					<img border="0" width="1" height="10" src="{$infopanelImagePath}/spacer.gif"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<!-- Info Panel Frame -->
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
						<tr valign="bottom">
							<td>
								<img border="0" src="{$infopanelImagePath}/infopanel_top_left1.gif" width="5" height="27"/>
							</td>
							<td>
								<img border="0" src="{$infopanelImagePath}/infopanel_top_left2.gif" width="5" height="27"/>
							</td>
							<td width="100%" height="27" style="background-image: url('{$infopanelImagePath}/infopanel_top_border.gif'); background-repeat: repeat-x;">
								<table width="100%" height="100%" cellpadding="0" cellspacing="0">
									<tr valign="bottom">
										<!-- Patient ID -->
										<td class="infopanel_title" align="left">
											<xsl:choose>
												<xsl:when test="$titleString=''">
													<xsl:value-of select="PATIENT_strPatientID"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$titleString"/>
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<!-- Subtabs -->
										<td align="right">
											<xsl:choose>
												<!-- No Active Subtab Specified - Render Empty Cell -->
												<xsl:when test="$activeSubtab=''">
													&#160;
												</xsl:when>
												<!-- Active Subtab Specified - Call Template to Render Subtabs -->
												<xsl:otherwise>
													<xsl:call-template name="infopanel_subtabs">
														<xsl:with-param name="activeSubtab">
															<xsl:value-of select="$activeSubtab"/>
														</xsl:with-param>
														<xsl:with-param name="PATIENT_intInternalPatientID">
															<xsl:value-of select="$PATIENT_intInternalPatientID"/>
														</xsl:with-param>
													</xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
										</td>
									</tr>
								</table>
							</td>
							<td>
								<img border="0" src="{$infopanelImagePath}/infopanel_top_right1.gif" width="5" height="27"/>
							</td>
							<td>
								<img border="0" src="{$infopanelImagePath}/infopanel_top_right2.gif" width="5" height="27"/>
							</td>
						</tr>
						<tr valign="top">
							<td width="5" style="background-image: url('{$infopanelImagePath}/infopanel_left_border.gif'); background-repeat: repeat-y;">&#160;</td>
							<td width="100%" height="100%" colspan="3">
								<!-- Add Frame Content -->
								<xsl:call-template name="infopanel_content"/>
							</td>
							<td width="5" style="background-image: url('{$infopanelImagePath}/infopanel_right_border.gif'); background-repeat: repeat-y;">&#160;</td>
						</tr>
						<tr valign="top">
							<td>
								<img border="0" src="{$infopanelImagePath}/infopanel_bottom_left1.gif" width="5" height="8"/>
							</td>
							<td>
								<img border="0" src="{$infopanelImagePath}/infopanel_bottom_left2.gif" width="5" height="8"/>
							</td>
							<td width="100%" height="8" style="background-image: url('{$infopanelImagePath}/infopanel_bottom_border.gif'); background-repeat: repeat-x;">&#160;</td>
							<td>
								<img border="0" src="{$infopanelImagePath}/infopanel_bottom_right1.gif" width="5" height="8"/>
							</td>
							<td>
								<img border="0" src="{$infopanelImagePath}/infopanel_bottom_right2.gif" width="5" height="8"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- Bottom Button Bar -->
			<tr height="100%">
				<td width="100%" colspan="2">
					<img border="0" width="1" height="10" src="{$infopanelImagePath}/spacer.gif"/>
				</td>
			</tr>
			<tr class="channel_button_strip_separator">
				<td width="100%" colspan="2">
					<img border="0" width="1" height="1" src="{$infopanelImagePath}/spacer.gif"/>
				</td>
			</tr>
			<tr height="28" valign="middle">
				<td align="left"><img border="0" width="1" height="1" src="{$infopanelImagePath}/spacer.gif"/></td>
				<td align="right">
					<!-- "Previous" Button -->
					<xsl:choose>
						<xsl:when test="$previousButtonFlag='true'">
							<xsl:choose>
								<xsl:when test="$previousButtonUrl=''">
									<a href="{$baseActionURL}?{$action}&amp;back=true&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
										<img border="0" src="{$buttonImagePath}/previous_enabled.gif" alt="Previous"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="{$previousButtonUrl}">
										<img border="0" src="{$buttonImagePath}/previous_enabled.gif" alt="Previous"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<img border="0" src="{$buttonImagePath}/previous_disabled.gif" alt="Next"/>
						</xsl:otherwise>
					</xsl:choose>
					<!-- "Next" Button -->
					<img border="0" src="{$buttonImagePath}/next_disabled.gif" alt="Next"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!--
		Description:
			Generate HTML for the information panel subtabs.

		Parameters:
			PATIENT_intInternalPatientID -
				Internal patient ID used to generate links on each subtab.

			activeSubtab -
				Symbol indicating which subtab is active. This may be "details,"
				"collection," "consent," "followUp," "biospecimen," "notes" or "attachments."
	-->
	<xsl:template name="infopanel_subtabs">
		<xsl:param name="activeSubtab"/>
		<xsl:param name="PATIENT_intInternalPatientID"/>
                <xsl:variable name="highlightConsentCount"><xsl:value-of select="/body/highlightMenu/consentCount"/></xsl:variable>
                <xsl:variable name="highlightAdmissionsCount"><xsl:value-of select="/body/highlightMenu/admissionsCount"/></xsl:variable>
                <xsl:variable name="highlightAppointmentsCount"><xsl:value-of select="/body/highlightMenu/appointmentsCount"/></xsl:variable>
                <xsl:variable name="highlightBiospecimenCount"><xsl:value-of select="/body/highlightMenu/biospecimenCount"/></xsl:variable>
                <xsl:variable name="highlightNotesCount"><xsl:value-of select="/body/highlightMenu/notesCount"/></xsl:variable>
                <xsl:variable name="highlightAttachmentsCount"><xsl:value-of select="/body/highlightMenu/attachmentsCount"/></xsl:variable>
                
		<table cellspacing="0" cellpadding="0" border="0">
			<tr valign="bottom">
				<xsl:choose>
					<xsl:when test="$activeSubtab='details'">
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_first_left.gif"/></td>
						<td class="infopanel_subtab_active">Details</td>
					</xsl:when>
					<xsl:otherwise>
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_inactive_first_left.gif"/></td>
						<td class="infopanel_subtab_inactive">
							<a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=view_patient&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
								Details
							</a>
						</td>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$activeSubtab='collection'">
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_middle_left.gif"/></td>
						<td class="infopanel_subtab_active">Collection</td>
					</xsl:when>
					<xsl:when test="$activeSubtab='details'">
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_middle_right.gif"/></td>
                                                <xsl:choose>
                                                    <xsl:when test="$highlightAdmissionsCount &gt; '0'">
                                                        <td class="infopanel_subtab_inactive" >
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=admissions&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        <font style="text-decoration:underline;color:#8A2BE2;">Collection</font>
                                                                </a>
                                                        </td>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=admissions&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        Collection
                                                                </a>
                                                        </td>
                                                    </xsl:otherwise>
                                                </xsl:choose>
						
					</xsl:when>
					<xsl:otherwise>
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_inactive_middle_divider.gif"/></td>
                                                <xsl:choose>
                                                    <xsl:when test="$highlightAdmissionsCount &gt; '0'">
                                                        <td class="infopanel_subtab_inactive" >
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=admissions&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        <font style="text-decoration:underline;color:#8A2BE2;">Collection</font>
                                                                </a>
                                                        </td>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=admissions&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        Collection
                                                                </a>
                                                        </td>
                                                    </xsl:otherwise>
                                                </xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<!--<xsl:choose>
					<xsl:when test="$activeSubtab='consent'">
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_middle_left.gif"/></td>
						<td class="infopanel_subtab_active">Consent</td>
					</xsl:when>
					<xsl:when test="$activeSubtab='collection'">
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_middle_right.gif"/></td>
                                                <xsl:choose>
                                                    <xsl:when test="$highlightConsentCount &gt; '0'">
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=consent&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        <font style="text-decoration:underline;color:#8A2BE2;">Consent</font>
                                                                </a>
                                                        </td>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=consent&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        Consent
                                                                </a>
                                                        </td>
                                                    </xsl:otherwise>
                                                </xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_inactive_middle_divider.gif"/></td>
                                                <xsl:choose>
                                                    <xsl:when test="$highlightConsentCount &gt; '0'">
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=consent&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        <font style="text-decoration:underline;color:#8A2BE2;">Consent</font>
                                                                </a>
                                                        </td>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=consent&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        Consent
                                                                </a>
                                                        </td>
                                                    </xsl:otherwise>
                                                </xsl:choose>
					</xsl:otherwise>
				</xsl:choose>-->
				<!--<xsl:choose>
					<xsl:when test="$activeSubtab='followUp'">
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_middle_left.gif"/></td>
						<td class="infopanel_subtab_active">Follow&#160;Up</td>
					</xsl:when>
					<xsl:when test="$activeSubtab='consent'">
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_middle_right.gif"/></td>
                                                <xsl:choose>
                                                    <xsl:when test="$highlightAppointmentsCount &gt; '0'">
                                                        <td class="infopanel_subtab_inactive" >
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=appointments&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        <font style="text-decoration:underline;color:#8A2BE2;">Follow&#160;Up</font>
                                                                </a>
                                                        </td>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=appointments&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        Follow&#160;Up
                                                                </a>
                                                        </td>
                                                    </xsl:otherwise>
                                                </xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_inactive_middle_divider.gif"/></td>
                                                <xsl:choose>
                                                    <xsl:when test="$highlightAppointmentsCount &gt; '0'">
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=appointments&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                <font style="text-decoration:underline;color:#8A2BE2;">Follow&#160;Up</font>
                                                                </a>
                                                        </td>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=appointments&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                Follow&#160;Up
                                                                </a>
                                                        </td>
                                                    </xsl:otherwise>
                                                </xsl:choose>
					</xsl:otherwise>
				</xsl:choose>-->
				<xsl:choose>
					<xsl:when test="$activeSubtab='biospecimen'">
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_middle_left.gif"/></td>
						<td class="infopanel_subtab_active">Biospecimen</td>
					</xsl:when>
					<xsl:when test="$activeSubtab='collection'">
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_middle_right.gif"/></td>
                                                <xsl:choose>
                                                    <xsl:when test="$highlightBiospecimenCount &gt; '0'">
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$biospecimenURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTab}&amp;intInternalPatientID={$PATIENT_intInternalPatientID}&amp;current=biospecimen_search&amp;submit=intInternalPatientID&amp;currentPage=patient_results&amp;module=biospecimen_search&amp;action=patient_entry">
                                                                        <font style="text-decoration:underline;color:#8A2BE2;">Biospecimen</font>
                                                                </a>
                                                        </td>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$biospecimenURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTab}&amp;intInternalPatientID={$PATIENT_intInternalPatientID}&amp;current=biospecimen_search&amp;submit=intInternalPatientID&amp;currentPage=patient_results&amp;module=biospecimen_search&amp;action=patient_entry">
                                                                        Biospecimen
                                                                </a>
                                                        </td>
                                                    </xsl:otherwise>
                                                </xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_inactive_middle_divider.gif"/></td>
                                                <xsl:choose>
                                                    <xsl:when test="$highlightBiospecimenCount &gt; '0'">
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$biospecimenURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTab}&amp;intInternalPatientID={$PATIENT_intInternalPatientID}&amp;current=biospecimen_search&amp;submit=intInternalPatientID&amp;currentPage=patient_results&amp;module=biospecimen_search&amp;action=patient_entry">
                                                                        <font style="text-decoration:underline;color:#8A2BE2;">Biospecimen</font>
                                                                </a>
                                                        </td>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$biospecimenURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTab}&amp;intInternalPatientID={$PATIENT_intInternalPatientID}&amp;current=biospecimen_search&amp;submit=intInternalPatientID&amp;currentPage=patient_results&amp;module=biospecimen_search&amp;action=patient_entry">
                                                                        Biospecimen
                                                                </a>
                                                        </td>
                                                    </xsl:otherwise>
                                                </xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$activeSubtab='notes'">
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_middle_left.gif"/></td>
						<td class="infopanel_subtab_active">Notes</td>
					</xsl:when>
					<xsl:when test="$activeSubtab='biospeciman'">
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_middle_right.gif"/></td>
                                                <xsl:choose>
                                                    <xsl:when test="$highlightNotesCount &gt; '0'">
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=notes&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        <font style="text-decoration:underline;color:#8A2BE2;">Notes</font>
                                                                </a>
                                                        </td>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=notes&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        Notes
                                                                </a>
                                                        </td>
                                                    </xsl:otherwise>
                                                </xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_inactive_middle_divider.gif"/></td>
                                                <xsl:choose>
                                                    <xsl:when test="$highlightNotesCount &gt; '0'">
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=notes&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        <font style="text-decoration:underline;color:#8A2BE2;">Notes</font>
                                                                </a>
                                                        </td>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=notes&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        Notes
                                                                </a>
                                                        </td>
                                                    </xsl:otherwise>
                                                </xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$activeSubtab='attachments'">
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_middle_left.gif"/></td>
						<td class="infopanel_subtab_active">Attachments</td>
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_last_right.gif"/></td>
					</xsl:when>
					<xsl:when test="$activeSubtab='notes'">
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_active_middle_right.gif"/></td>
                                                <xsl:choose>
                                                    <xsl:when test="$highlightAttachmentsCount &gt; '0'">
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=attachments&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        <font style="text-decoration:underline;color:#8A2BE2;">Attachments</font>
                                                                </a>						
                                                        </td>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=attachments&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        Attachments
                                                                </a>						
                                                        </td>
                                                    </xsl:otherwise>
                                                </xsl:choose>
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_inactive_last_right.gif"/></td>
					</xsl:when>
					<xsl:otherwise>
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_inactive_middle_divider.gif"/></td>
                                                <xsl:choose>
                                                    <xsl:when test="$highlightAttachmentsCount &gt; '0'">
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=attachments&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        <font style="text-decoration:underline;color:#8A2BE2;">Attachments</font>
                                                                </a>						
                                                        </td>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <td class="infopanel_subtab_inactive">
                                                                <a class="infopanel_subtab" href="{$baseActionURL}?uP_root=root&amp;action=attachments&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
                                                                        Attachments
                                                                </a>						
                                                        </td>
                                                    </xsl:otherwise>
                                                </xsl:choose>
						<td><img border="0" src="{$subtabImagePath}/infopanel_subtab_inactive_last_right.gif"/></td>
					</xsl:otherwise>
				</xsl:choose>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
