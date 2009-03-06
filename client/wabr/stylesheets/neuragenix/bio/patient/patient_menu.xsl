<?xml version="1.0" encoding="utf-8"?>
<!--
	patient_menu.xsl. Menu used for all patients.

	Version: $Id: patient_menu.xsl,v 1.14 2005/04/27 08:12:06 renny Exp $
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
xmlns:url="http://www.jclark.com/xt/java/java.net.URLEncoder" exclude-result-prefixes="url">

	<xsl:output method="html" indent="no"/>
	<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
	<xsl:param name="downloadURL">downloadURL</xsl:param>
	<xsl:param name="biospecimenURL">biospecimenURL</xsl:param>
	<xsl:param name="studyURL">studyURL</xsl:param>
	<xsl:param name="smartformURL">smartformURL</xsl:param>
	<xsl:param name="downloadTab">downloadTab</xsl:param>
	<xsl:param name="smartformTab">smartformTab</xsl:param>
	<xsl:param name="studyTab">studyTab</xsl:param>
	<xsl:param name="biospecimenTab">biospecimenTab</xsl:param>
	<xsl:param name="downloadNodeId">downloadNodeId</xsl:param>
	<xsl:param name="action">
		<xsl:value-of select="/body/action"/>
	</xsl:param>
	<!-- Define Image File Paths -->
	<xsl:variable name="funcpanelImagePath">media/neuragenix/funcpanel</xsl:variable>
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
	<xsl:variable name="spacerImagePath">media/neuragenix/infopanel/spacer.gif</xsl:variable>
	<xsl:template match="/">
		<script language="javascript">
			function confirmDelete(aURL)
			{
				var confirmAnswer = confirm('Are you sure you want to delete this record?');
				if(confirmAnswer == true)
				{
					window.location=aURL + '&amp;delete=true';
				}
			}
			function confirmClear(aURL)
			{
				var confirmAnswer = confirm('Are you sure you want to clear the fields?');
				if(confirmAnswer == true)
				{
					window.location=aURL + '&amp;clear=true';
				}
			}
                        function submitThis(formname)
                        {
                            eval("document."+formname+".submit()");
                        }
	</script>
        
        <xsl:variable name="disable-PATIENT_strPatientID"><xsl:value-of select="/body/disable/disable-PATIENT_strPatientID"/></xsl:variable>
        <!-- Tom enable PATIENT_strHospitalUR -->
		<xsl:variable name="disable-PATIENT_strHospitalUR"><xsl:value-of select="/body/disable/disable-PATIENT_strHospitalUR"/></xsl:variable>
        <!-- End Tom enable PATIENT_strHospitalUR -->
		<xsl:variable name="disable-PATIENT_strSurname"><xsl:value-of select="/body/disable/disable-PATIENT_strSurname"/></xsl:variable>
        <xsl:variable name="disable-PATIENT_strFirstName"><xsl:value-of select="/body/disable/disable-PATIENT_strFirstName"/></xsl:variable>
        <xsl:variable name="disable-PATIENT_dtDob"><xsl:value-of select="/body/disable/disable-PATIENT_dtDob"/></xsl:variable>
        <!--xsl:variable name="disable-PATIENT_strProtocol"><xsl:value-of select="/body/disable/disable-PATIENT_strProtocol"/></xsl:variable-->
        <xsl:variable name="disable-PATIENT_strSpecies"><xsl:value-of select="/body/disable/disable-PATIENT_strSpecies"/></xsl:variable>
<xsl:variable name="disablePatientFlaggedNames"><xsl:value-of select="/body/disable/disablePatientFlaggedNames"/></xsl:variable>
		<!-- Table Containing Entire Channel Workspace (Both Left and Right Panes) -->
		<table cellpadding="0" cellspacing="0" border="0" height="100%" width="100%">
			<tr height="100%" valign="top">
				<!-- Left Pane -->
				<td  id="neuragenix-border-right" class="uportal-channel-subtitle">
					<!-- Start of New Left Pane -->
					<table cellpadding="0" cellspacing="0" width="250" border="0">
						<tr>
							<td><img width="1" height="10" src="{$spacerImagePath}"/></td>
						</tr>
						<!-- Search Form -->
						<tr valign="top">
							<td align="left">
								<!-- Declare Variables for Search Form Field Values -->
								<xsl:variable name="PATIENT_strPatientID">
									<xsl:value-of select="/body/search/PATIENT_strPatientID"/>
								</xsl:variable>
								<xsl:variable name="PATIENT_strSurname">
									<xsl:value-of select="/body/search/PATIENT_strSurname"/>
								</xsl:variable>
								<!-- Tom enable PATIENT_strHospitalUR -->
								<xsl:variable name="ADMISSIONS_strHospitalUR"><xsl:value-of select="/body/search/ADMISSIONS_strHospitalUR"/></xsl:variable>
								<!-- End Tom enable PATIENT_strHospitalUR -->
								<xsl:variable name="PATIENT_strFirstName">
									<xsl:value-of select="/body/search/PATIENT_strFirstName"/>
								</xsl:variable>
								<xsl:variable name="PATIENT_dtDob_Day">
									<xsl:value-of select="/body/search/PATIENT_dtDob_Day"/>
								</xsl:variable>
								<xsl:variable name="PATIENT_dtDob_Month">
									<xsl:value-of select="/body/search/PATIENT_dtDob_Month"/>
								</xsl:variable>
								<xsl:variable name="PATIENT_dtDob_Year">
									<xsl:value-of select="/body/search/PATIENT_dtDob_Year"/>
								</xsl:variable>
								<table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="100%">
									<form name="patient_search" action="{$baseActionURL}?action=search_patient&amp;newSearch=true" method="post">
										<tr valign="bottom">
											<td><img src="{$funcpanelImagePath}/funcpanel_header_active_left.gif"/></td>
											<td class="funcpanel_header_active" align="left" colspan="3" width="100%">NEW SEARCH</td>
											<td><img src="{$funcpanelImagePath}/funcpanel_header_active_right.gif"/></td>
										</tr>
										<tr class="funcpanel_content">
											<td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
										</tr>
										<xsl:if test="$disable-PATIENT_strPatientID != 'true'">
                                                                                <tr class="funcpanel_content">
											<td class="funcpanel_left_border">&#160;</td>
											<td class="form_label"><xsl:value-of select="/body/search/PATIENT_strPatientIDDisplay"/>&#160;:</td>
											<td>&#160;</td>
											<td><input class="funcpanel_form" type="text" value="{$PATIENT_strPatientID}" tabindex="1" name="PATIENT_strPatientID" size="20"/></td>
											<td class="funcpanel_right_border">&#160;</td>
										</tr>                                                                                
										<tr class="funcpanel_content">
											<td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
										</tr>
                                                                                </xsl:if>
										<!-- Tom enable PATIENT_strHospitalUR -->
										
										<xsl:if test="$disable-PATIENT_strHospitalUR != 'true'">
                                                                                <tr class="funcpanel_content">
											<td class="funcpanel_left_border">&#160;</td>
											<td class="form_label"><!--xsl:value-of select="/body/patient/ADMISSIONS_strHospitalURDisplay"/-->Hospital UR&#160;:</td>
											<td>&#160;</td>
											<td><input class="funcpanel_form" type="text" value="{$ADMISSIONS_strHospitalUR}" name="ADMISSIONS_strHospitalUR" size="20"/></td>
											<td class="funcpanel_right_border">&#160;</td>
										</tr>
										<tr class="funcpanel_content">
											<td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
										</tr>
                                                                                </xsl:if>
										
										<!-- End Tom enable PATIENT_strHospitalUR -->
										
                                                                                <tr class="funcpanel_content">
											<td class="funcpanel_left_border">&#160;</td>
											<td class="form_label"><xsl:value-of select="/body/search/PATIENT_strSurnameDisplay"/>&#160;:</td>
											<td>&#160;</td>
											<td><input class="funcpanel_form" type="text" value="{$PATIENT_strSurname}" tabindex="2" name="PATIENT_strSurname" size="20"/></td>
											<td class="funcpanel_right_border">&#160;</td>
										</tr>
										<tr class="funcpanel_content">
											<td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
										</tr>
                                                                            
                                                                                <xsl:if test="$disable-PATIENT_strFirstName != 'true'">
										<tr class="funcpanel_content">
											<td class="funcpanel_left_border">&#160;</td>
											<td class="form_label"><xsl:value-of select="/body/search/PATIENT_strFirstNameDisplay"/>&#160;:</td>
											<td>&#160;</td>
											<td><input class="funcpanel_form" type="text" value="{$PATIENT_strFirstName}" tabindex="3" name="PATIENT_strFirstName" size="20"/></td>
											<td class="funcpanel_right_border">&#160;</td>
										</tr>
										<tr class="funcpanel_content">
											<td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
										</tr>
                                                                                </xsl:if>
                                                                                <xsl:if test="$disable-PATIENT_dtDob != 'true'">
										<tr class="funcpanel_content">
											<td class="funcpanel_left_border">&#160;</td>
											<td class="form_label"><xsl:value-of select="/body/search/PATIENT_dtDobDisplay"/>&#160;:</td>
											<td>&#160;</td>
											<td>
												<xsl:choose>
													<xsl:when test="PATIENT_dtDob/@display_type='dropdown'">
														<select name="PATIENT_dtDob_Day" tabindex="4">
															<option value=""/>
															<xsl:for-each select="PATIENT_dtDob_Day">
																<option>
																	<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
																	<xsl:if test="@selected = '1'">
																		<xsl:attribute name="selected">true</xsl:attribute>
																	</xsl:if>
																	<xsl:value-of select="."/>
																</option>
															</xsl:for-each>
														</select>
														<select name="PATIENT_dtDob_Month" tabindex="5">
															<option value=""/>
															<xsl:for-each select="PATIENT_dtDob_Month">
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
													<!-- when it is a text-->
													<xsl:otherwise>
														<input class="funcpanel_form" type="text" name="PATIENT_dtDob_Day" size="2" tabindex="4">
															<xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDob_Day"/></xsl:attribute>
														</input>
														<input class="funcpanel_form" type="text" name="PATIENT_dtDob_Month" size="2" tabindex="5">
															<xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDob_Month"/></xsl:attribute>
														</input>
													</xsl:otherwise>
												</xsl:choose>
												<input class="funcpanel_form" type="text" name="PATIENT_dtDob_Year" size="6" tabindex="6">
													<xsl:attribute name="value"><xsl:value-of select="/body/search/PATIENT_dtDob_Year"/></xsl:attribute>
												</input>
											</td>
											<td class="funcpanel_right_border">&#160;</td>
										</tr>
										<tr class="funcpanel_content">
											<td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
										</tr>
                                                                                </xsl:if>
										<!--
                                                                                <xsl:if test="$disable-PATIENT_strSpecies != 'true'">
										<tr class="funcpanel_content">
											<td class="funcpanel_left_border">&#160;</td>
											<td class="form_label"><xsl:value-of select="/body/search/PATIENT_strSpeciesDisplay"/>&#160;:</td>
											<td>&#160;</td>
											<td>
												<select class="funcpanel_form" name="PATIENT_strSpecies" tabindex="7">
													<xsl:for-each select="/body/search/PATIENT_strSpecies">
														<option>
															<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
															<xsl:if test="@selected = '1'">
																<xsl:attribute name="selected">true</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="."/>
														</option>
													</xsl:for-each>
													<option value=""/>
												</select>
											</td>
											<td class="funcpanel_right_border">&#160;</td>
										</tr>
										<tr class="funcpanel_content">
											<td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
										</tr>
                                                                                </xsl:if>-->
										<tr valign="top">
											<td><img src="{$funcpanelImagePath}/funcpanel_footer_left.gif"/></td>
											<td class="funcpanel_footer" align="right" colspan="3" width="100%">
												<input class="submit_image_button" type="image" src="{$buttonImagePath}/search_enabled.gif" tabindex="8" name="submit" value="Search"
													onblur="javascript:document.patient_search.PATIENT_strPatientID.focus()"/>
											</td>
											<td><img src="{$funcpanelImagePath}/funcpanel_footer_right.gif"/></td>
										</tr>
									</form>
								</table>
							</td>
						</tr>
						<tr>
							<td><img width="1" height="10" src="{$spacerImagePath}"/></td>
						</tr>
						<!-- Patient Details -->
						<xsl:choose>
							<!-- Active Patient Exists - Show Patient Details -->
							<xsl:when test="string-length( /body/patient/PATIENT_intInternalPatientID ) > 0">
								<tr valign="top">
									<td align="left">
										<table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="100%">
											<tr valign="bottom">
												<td><img src="{$funcpanelImagePath}/funcpanel_header_active_left.gif"/></td>
												<td class="funcpanel_header_active" align="left" colspan="3" width="100%">PATIENT DETAILS</td>
												<td><img src="{$funcpanelImagePath}/funcpanel_header_active_right.gif"/></td>
											</tr>
											<tr class="funcpanel_content">
												<td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
											</tr>
											<tr class="funcpanel_content">
												<td class="funcpanel_left_border">&#160;</td>
												<td class="form_label"><xsl:value-of select="/body/patient/PATIENT_strPatientIDDisplay"/>&#160;:</td>
												<td>&#160;</td>
												<td><xsl:value-of select="/body/patient/PATIENT_strPatientID"/></td>
												<td class="funcpanel_right_border">&#160;</td>
											</tr>
											<!-- Tom enable PATIENT_strHospitalUR -->
											<!-- 
											<tr class="funcpanel_content">
												<td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
											</tr>
											<tr class="funcpanel_content">
												<td class="funcpanel_left_border">&#160;</td>
												<td class="form_label"><xsl:value-of select="/body/patient/ADMISSIONS_strHospitalURDisplay"/>&#160;:</td>
												<td>&#160;</td>
												<td><xsl:value-of select="/body/patient/ADMISSIONS_strHospitalUR"/></td>
												<td class="funcpanel_right_border">&#160;</td>
											</tr>
											-->
											<!-- End Tom enable PATIENT_strHospitalUR -->
											<tr class="funcpanel_content">
												<td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
											</tr>
											<tr class="funcpanel_content">
												<td class="funcpanel_left_border">&#160;</td>
												<td class="form_label"><xsl:value-of select="/body/patient/PATIENT_strSurnameDisplay"/>&#160;:</td>
												<td>&#160;</td>
												<td><xsl:value-of select="/body/patient/PATIENT_strSurname"/></td>
												<td class="funcpanel_right_border">&#160;</td>
											</tr>
											<tr class="funcpanel_content">
												<td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
											</tr>
											<tr class="funcpanel_content">
												<td class="funcpanel_left_border">&#160;</td>
												<td class="form_label"><xsl:value-of select="/body/patient/PATIENT_strFirstNameDisplay"/>&#160;:</td>
												<td>&#160;</td>
												<td><xsl:value-of select="/body/patient/PATIENT_strFirstName"/></td>
												<td class="funcpanel_right_border">&#160;</td>
											</tr>
											<tr class="funcpanel_content">
												<td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
											</tr>
											<tr class="funcpanel_content">
												<td class="funcpanel_left_border">&#160;</td>
												<td class="form_label"><xsl:value-of select="/body/patient/PATIENT_dtDobDisplay"/>&#160;:</td>
												<td>&#160;</td>
												<td><xsl:value-of select="/body/patient/PATIENT_dtDob"/></td>
												<td class="funcpanel_right_border">&#160;</td>
											</tr>
											<tr class="funcpanel_content">
												<td class="funcpanel_bottom_border" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
											</tr>
										</table>
									</td>
								</tr>
							</xsl:when>
							<!-- No Active Patient - Show Inactive Patient Details -->
							<xsl:otherwise>
								<tr valign="top">
									<td align="left">
										<table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="100%">
											<tr valign="bottom">
												<td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_left.gif"/></td>
												<td class="funcpanel_header_inactive" align="left" colspan="3" width="100%">PATIENT DETAILS</td>
												<td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_right.gif"/></td>
											</tr>
										</table>
									</td>
								</tr>
							</xsl:otherwise>
						</xsl:choose>
						<tr>
							<td><img width="1" height="10" src="{$spacerImagePath}"/></td>
						</tr>
						
						<!-- Smartforms -->
						<!--  <xsl:choose> -->
							<!-- Smartforms Exist - Render List -->
							
								<xsl:variable name="PATIENT_intInternalPatientID">
									<xsl:value-of select="/body/patient/PATIENT_intInternalPatientID"/>
								</xsl:variable>
								<xsl:variable name="PATIENT_strSurname">
									<xsl:value-of select="/body/patient/PATIENT_strSurname"/>
								</xsl:variable>
								<xsl:variable name="PATIENT_strFirstName">
									<xsl:value-of select="/body/patient/PATIENT_strFirstName"/>
								</xsl:variable>
								<xsl:variable name="PATIENT_strPatientID">
									<xsl:value-of select="/body/patient/PATIENT_strPatientID"/>
								</xsl:variable>
								<tr valign="top">
									<td align="left">
										<table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="100%">
											<xsl:choose>
											<xsl:when test="string-length( /body/patient/PATIENT_intInternalPatientID ) > 0">
												<tr valign="bottom">
													<td><img src="{$funcpanelImagePath}/funcpanel_header_active_left.gif"/></td>
													<td class="funcpanel_header_active" align="left" width="100%">SMARTFORMS</td>
													<td><img src="{$funcpanelImagePath}/funcpanel_header_active_right.gif"/></td>
												</tr>
											</xsl:when>
											<xsl:otherwise>
												<tr valign="bottom">
													<td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_left.gif"/></td>
													<td class="funcpanel_header_inactive" align="left" width="100%">SMARTFORMS</td>
													<td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_right.gif"/></td>
												</tr>
											</xsl:otherwise>
											</xsl:choose>
											<xsl:if test="string-length( /body/patient/PATIENT_intInternalPatientID ) > 0 and count ( /body/patient/smartforms/study ) > 0">
											<tr class="funcpanel_content">
												<td class="funcpanel_content_spacer" colspan="3"><img width="1" height="4" src="{$spacerImagePath}"/></td>
											</tr>
											<xsl:for-each select="/body/patient/smartforms/study">
												<xsl:variable name="STUDY_intStudyID">
													<xsl:value-of select="STUDY_intStudyID"/>
												</xsl:variable>
												<tr class="funcpanel_content">
													<td class="funcpanel_left_border">&#160;</td>
													<td>
                                                                                                        <xsl:variable name="formname">smartformstudy_<xsl:value-of select="STUDY_intStudyID"/></xsl:variable>
                                                                                                                <form name="{$formname}" action="{$smartformURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformTab}&amp;domain=Study&amp;participant={$PATIENT_intInternalPatientID}&amp;intStudyID={$STUDY_intStudyID}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;strOrigin=patient_result" method="post">>
                                                                                                                           <input type="hidden" value="{$PATIENT_strFirstName}" name="var1"/>
                                                                                                                           <xsl:variable name="url">domain=Study&amp;participant={$PATIENT_intInternalPatientID}&amp;intStudyID={$STUDY_intStudyID}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;strOrigin=patient_result</xsl:variable>
                                                                                                                            <input type="hidden" value="{$PATIENT_strSurname}" name="var2"/>
                                                                                                                            <input type="hidden" value="{$PATIENT_strPatientID}" name="title"/>
                                                                                                                            <!--input type="hidden" value="{$formname}" name="form_name"/-->
                                                                                                                            <a href="javascript:submitThis('{$formname}');">
														
															<xsl:value-of select="STUDY_strStudyName"/>&#160;:
														</a>
                                                                                                                </form>
													</td>
													<td class="funcpanel_right_border">&#160;</td>
												</tr>
												<tr class="funcpanel_content">
													<td class="funcpanel_content_separator" colspan="3"><img width="1" height="4" src="{$spacerImagePath}"/></td>
												</tr>
												<xsl:for-each select="smartform">
													<xsl:variable name="varSmartformParticipantID">
														<xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformParticipantID"/>
													</xsl:variable>
													<xsl:variable name="varSmartformID">
														<xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformID"/>
													</xsl:variable>
													<xsl:variable name="varCurrentPage">
														<xsl:value-of select="SMARTFORMPARTICIPANTS_intCurrentPage"/>
													</xsl:variable>
													<xsl:variable name="varUserNote">
														<xsl:value-of select="SMARTFORMPARTICIPANTS_strUserNote"/>
													</xsl:variable>
													<xsl:variable name="varSmartformStatus">
														<xsl:value-of select="SMARTFORMPARTICIPANTS_strSmartformStatus"/>
													</xsl:variable>
													<xsl:variable name="SMARTFORM_strSmartformName">
														<xsl:value-of select="SMARTFORM_strSmartformName"/>
													</xsl:variable>

													<tr class="funcpanel_expanded_content">
														<td class="funcpanel_left_border">&#160;</td>
														<td>
                                                                                                                        <xsl:variable name="formname">smartforms_<xsl:value-of select="$STUDY_intStudyID"/>_<xsl:value-of select="$varSmartformID"/></xsl:variable>
															<img width="10" height="1" src="{$spacerImagePath}"/>
                                                                                                                        <form name="{$formname}" action="{$smartformURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformTab}&amp;current=smartform_result_view&amp;domain=Study&amp;participant={$PATIENT_intInternalPatientID}&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}&amp;SMARTFORMPARTICIPANTS_intCurrentPage={$varCurrentPage}&amp;SMARTFORMPARTICIPANTS_strSmartformStatus={$varSmartformStatus}&amp;intStudyID={$STUDY_intStudyID}&amp;SMARTFORM_smartformname={$SMARTFORM_strSmartformName}&amp;strTitle={$PATIENT_strPatientID}&amp;fromPatient=true" method="post">
                                                                                                                           <input type="hidden" value="{$PATIENT_strFirstName}" name="var1"/>
                                                                                                                           <xsl:variable name="url">current=smartform_result_view&amp;domain=Study&amp;participant={$PATIENT_intInternalPatientID}&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}&amp;SMARTFORMPARTICIPANTS_intCurrentPage={$varCurrentPage}&amp;SMARTFORMPARTICIPANTS_strSmartformStatus={$varSmartformStatus}&amp;intStudyID={$STUDY_intStudyID}&amp;SMARTFORM_smartformname={$SMARTFORM_strSmartformName}&amp;strTitle={$PATIENT_strPatientID}&amp;fromPatient=true</xsl:variable>
                                                                                                                            <input type="hidden" value="{$PATIENT_strSurname}" name="var2"/>
                                                                                                                            <input type="hidden" value="{$PATIENT_strPatientID}" name="title"/>
                                                                                                                            <!--input type="hidden" value="{$formname}" name="form_name"/-->
                                                                                                                            <a href="javascript:submitThis('{$formname}');">
                                                                                                                            <xsl:value-of select="SMARTFORM_strSmartformName"/>
                                                                                                                            </a>
                                                                                                                        </form>
															<!--rennypva href="{$smartformURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformTab}&amp;current=smartform_result_view&amp;domain=Study&amp;participant={$PATIENT_intInternalPatientID}&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}&amp;SMARTFORMPARTICIPANTS_intCurrentPage={$varCurrentPage}&amp;SMARTFORMPARTICIPANTS_strSmartformStatus={$varSmartformStatus}&amp;intStudyID={$STUDY_intStudyID}&amp;SMARTFORM_smartformname={$SMARTFORM_strSmartformName}&amp;var1={$PATIENT_strFirstName}&amp;var2={$PATIENT_strSurname}&amp;title={$PATIENT_strPatientID}&amp;strTitle={$PATIENT_strPatientID}&amp;fromPatient=true">
																<xsl:value-of select="SMARTFORM_strSmartformName"/>
															</a-->
														</td>
														<td class="funcpanel_right_border">&#160;</td>
													</tr>
													<tr class="funcpanel_expanded_content">
														<td class="funcpanel_content_separator" colspan="3"><img width="1" height="4" src="{$spacerImagePath}"/></td>
													</tr>
												</xsl:for-each>
											</xsl:for-each>
							</xsl:if>
							<xsl:if test="string-length( /body/patient/PATIENT_intInternalPatientID ) > 0">
							   <tr class="funcpanel_content" >
                                       <td class="funcpanel_left_border" bgcolor="FFFFCC">&#160;</td>
                                       <td bgcolor="FFFFCC">
                                               <form name="analysisForm" action="{$smartformURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformTab}&amp;strcurrent=patient_view&amp;domain=Bioanalysis&amp;PATIENT_intPatientID={$PATIENT_intInternalPatientID}&amp;PatientAnalysis=true" method="post">>
                                               <a href="javascript:submitThis('analysisForm');">														
                                                       View all analysis results
                                               </a>
                                               </form>
                                       </td>
                                       <td class="funcpanel_right_border" bgcolor="FFFFCC">&#160;</td>
                               </tr>
                               <tr class="funcpanel_content" >
                                       <td class="funcpanel_content_separator" bgcolor="FFFFCC" colspan="3"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                               </tr> 
							</xsl:if>


							
										</table>
									</td>
								</tr>

							<!-- No Smartforms Exist - Show Inactive Panel -->
<!-- 							<xsl:otherwise>
								<tr valign="top">
									<td align="left">
										<table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="100%">
											<tr valign="bottom">
												<td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_left.gif"/></td>
												<td class="funcpanel_header_inactive" align="left" width="100%">SMARTFORMS</td>
												<td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_right.gif"/></td>
											</tr>
										</table>
									</td>
								</tr>
							</xsl:otherwise>
						</xsl:choose> -->
                                                <tr>
							<td><img width="1" height="10" src="{$spacerImagePath}"/></td>
						</tr>
                                                
						<!-- Admission Smartforms -->
						<xsl:if test="/body/patient/smartforms/admission-smartforms/displayAdmissionsSmartforms = 1">
							<!-- Smartforms Exist - Render List -->
							<xsl:if test="string-length( /body/patient/PATIENT_intInternalPatientID ) > 0 and  count (/body/patient/smartforms/admission-smartforms/admissions) > 0">
                                                            <tr valign="top">
                                                                <td align="left">
                                                                    <table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="100%">
                                                                        <tr valign="bottom">
                                                                            <td><img src="{$funcpanelImagePath}/funcpanel_header_active_left.gif"/></td>
                                                                            <td class="funcpanel_header_active" align="left" width="100%">COLLECTION SMARTFORMS</td>
                                                                            <td><img src="{$funcpanelImagePath}/funcpanel_header_active_right.gif"/></td>
                                                                        </tr>
                                                                        <tr class="funcpanel_content">
                                                                            <td class="funcpanel_content_spacer" colspan="3"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                                                                        </tr>
                                                                        <xsl:for-each select="/body/patient/smartforms/admission-smartforms/admissions">
                                                                            <tr class="funcpanel_content">
                                                                                <td class="funcpanel_left_border">&#160;</td>
                                                                                <td>
                                                                                    <xsl:variable name="admissionsSmartformLink"><xsl:value-of select="admissionsSmartformLink"/></xsl:variable>
                                                                                    <xsl:variable name="admissionsNumber"><xsl:value-of select="./ADMISSIONS_strCollectGrp"/></xsl:variable>
                                                                                    <xsl:variable name="strPatientFullName"><xsl:value-of select="./ADMISSIONS_strPatientFullName"/></xsl:variable>
                                                                                    <xsl:if test="function-available('url:encode')">
                                                                                        <a href="{$smartformURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformTab}&amp;{$admissionsSmartformLink}&amp;var2={url:encode($strPatientFullName)}">
                                                                                        Episode <xsl:value-of select="./ADMISSIONS_strEpisodeNo"/>: Collection Group <xsl:value-of select="$admissionsNumber"/></a>
                                                                                    </xsl:if>
                                                                                </td>
                                                                                <td class="funcpanel_right_border">&#160;</td>
                                                                            </tr>
                                                                        </xsl:for-each>
                                                                    </table>
                                                                </td>
                                                            </tr>
							</xsl:if>
						</xsl:if>
						<tr>
							<td><img width="1" height="10" src="{$spacerImagePath}"/></td>
						</tr>

                                                <!-- Flagged Records -->
						<xsl:choose>
							<!-- Flagged Records Exists - Show Records -->
							<xsl:when test="count( body/flags/patient ) > 0">
								<tr valign="top">
									<td align="left">
										<table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="100%">
											<tr valign="bottom">
												<td><img src="{$funcpanelImagePath}/funcpanel_header_active_left.gif"/></td>
												<td class="funcpanel_header_active" align="left" width="100%">FLAGGED RECORDS</td>
												<td><img src="{$funcpanelImagePath}/funcpanel_header_active_right.gif"/></td>
											</tr>
											<tr class="funcpanel_content">
												<td class="funcpanel_content_spacer" colspan="3"><img width="1" height="4" src="{$spacerImagePath}"/></td>
											</tr>
											<xsl:variable name="hideFlaggedPatientNames">
												<xsl:value-of select="/body/flags/hideFlaggedPatientNames"/>
												</xsl:variable>
											<xsl:for-each select="body/flags/patient">
												<xsl:variable name="PATIENT_intInternalPatientID">
													<xsl:value-of select="PATIENT_intInternalPatientID"/>
												</xsl:variable>
													<tr class="funcpanel_content">
														<td class="funcpanel_left_border">&#160;</td>
														<td>
															<a href="{$baseActionURL}?uP_root=root&amp;action=view_patient&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}">
																<xsl:value-of select="PATIENT_strPatientID"/><xsl:if test="$hideFlaggedPatientNames='false'">
																<xsl:if test="PATIENT_strFirstName!= '' or PATIENT_strSurname !=''"> - </xsl:if>
																<xsl:if test="PATIENT_strSurname!=''">
																	<xsl:value-of select="PATIENT_strSurname"/>
																	</xsl:if>
																<xsl:if test="PATIENT_strSurname!= '' and PATIENT_strFirstName!=''">, </xsl:if><xsl:if test="PATIENT_strFirstName!=''"><xsl:value-of select="PATIENT_strFirstName"/></xsl:if></xsl:if>
															</a>
														</td>
														<td class="funcpanel_right_border">&#160;</td>
													</tr>
													<tr class="funcpanel_content">
														<td class="funcpanel_content_separator" colspan="3"><img width="1" height="4" src="{$spacerImagePath}"/></td>
													</tr>
											</xsl:for-each>
										</table>
									</td>
								</tr>
							</xsl:when>
							<!-- No Flagged Records Exist - Show Inactive Panel -->
							<xsl:otherwise>
								<tr valign="top">
									<td align="left">
										<table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="100%">
											<tr valign="bottom">
												<td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_left.gif"/></td>
												<td class="funcpanel_header_inactive" align="left" width="100%">FLAGGED RECORDS</td>
												<td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_right.gif"/></td>
											</tr>
										</table>
									</td>
								</tr>
							</xsl:otherwise>
						</xsl:choose>
						<tr>
							<td><img width="1" height="10" src="{$spacerImagePath}"/></td>
						</tr>

					</table> <!-- End of New Left Pane -->
<!--	
					<xsl:if test="string-length( /body/patient/PATIENT_intInternalPatientID ) > 0">
-->
						<!-- Smartform List -->
<!--						
						<xsl:if test="count ( /body/patient/smartforms/study ) > 0">
							<xsl:variable name="PATIENT_intInternalPatientID">
								<xsl:value-of select="/body/patient/PATIENT_intInternalPatientID"/>
							</xsl:variable>
							<xsl:variable name="PATIENT_strSurname">
								<xsl:value-of select="/body/patient/PATIENT_strSurname"/>
							</xsl:variable>
							<xsl:variable name="PATIENT_strFirstName">
								<xsl:value-of select="/body/patient/PATIENT_strFirstName"/>
							</xsl:variable>
							<xsl:variable name="PATIENT_strPatientID">
								<xsl:value-of select="/body/patient/PATIENT_strPatientID"/>
							</xsl:variable>
							<p/>
							<p/>
							<span class="uportal-channel-subtitle">Smartforms</span>
							<hr/>
							<table width="100%" border="0" cellspacing="0" cellpadding="2">
								<xsl:for-each select="/body/patient/smartforms/study">
									<xsl:variable name="STUDY_intStudyID">
										<xsl:value-of select="STUDY_intStudyID"/>
									</xsl:variable>
									<tr class="uportal-input-text">
										<td width="100%">
											<b>
												<a href="{$smartformURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformTab}&amp;domain=Study&amp;participant={$PATIENT_intInternalPatientID}&amp;intStudyID={$STUDY_intStudyID}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;strOrigin=patient_result&amp;title={$PATIENT_strPatientID}&amp;var1={$PATIENT_strFirstName}&amp;var2={$PATIENT_strSurname}">
													<xsl:value-of select="STUDY_strStudyName"/>:
                                                        </a>
											</b>
										</td>
									</tr>
									<xsl:for-each select="smartform">
-->
										<!-- SMARTFROM INTEGRATION -->
<!--										
										<xsl:variable name="varSmartformParticipantID">
											<xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformParticipantID"/>
										</xsl:variable>
										<xsl:variable name="varSmartformID">
											<xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformID"/>
										</xsl:variable>
										<xsl:variable name="varCurrentPage">
											<xsl:value-of select="SMARTFORMPARTICIPANTS_intCurrentPage"/>
										</xsl:variable>
										<xsl:variable name="varUserNote">
											<xsl:value-of select="SMARTFORMPARTICIPANTS_strUserNote"/>
										</xsl:variable>
										<xsl:variable name="varSmartformStatus">
											<xsl:value-of select="SMARTFORMPARTICIPANTS_strSmartformStatus"/>
										</xsl:variable>
										<xsl:variable name="SMARTFORM_strSmartformName">
											<xsl:value-of select="SMARTFORM_strSmartformName"/>
										</xsl:variable>
										<tr class="uportal-input-text">
											<td align="right">
-->
												<!-- TODO: FIX THIS LINK -->
<!--												
												<a href="{$smartformURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformTab}&amp;current=smartform_result_view&amp;domain=Study&amp;participant={$PATIENT_intInternalPatientID}&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}&amp;SMARTFORMPARTICIPANTS_intCurrentPage={$varCurrentPage}&amp;SMARTFORMPARTICIPANTS_strSmartformStatus={$varSmartformStatus}&amp;intStudyID={$STUDY_intStudyID}&amp;SMARTFORM_smartformname={$SMARTFORM_strSmartformName}&amp;var1={$PATIENT_strFirstName}&amp;var2={$PATIENT_strSurname}&amp;title={$PATIENT_strPatientID}&amp;strTitle={$PATIENT_strPatientID}&amp;fromPatient=true">
													<xsl:value-of select="SMARTFORM_strSmartformName"/>
												</a>
											</td>
										</tr>
									</xsl:for-each>
								</xsl:for-each>
							</table>
						</xsl:if>
					</xsl:if>
-->
					<!-- End of Left Pane -->
				</td>
				<!-- Spacer Between Left and Right Panes -->
				 <td><img width="20" height="1" src="{$spacerImagePath}"/></td>
				<!-- Right Pane -->
				<td width="100%">
					<xsl:apply-templates select="body"/>
				</td>
			</tr>
		</table>
	</xsl:template>
        <xsl:template match="/body/disable"/>
	<xsl:template match="/body/flags"/>
	<xsl:template match="/body/search"/>
	<xsl:template match="/body/patient/smartforms"/>
	<xsl:template match="/body/action"/>
        <xsl:template match="/body/highlightMenu"/>        
</xsl:stylesheet>
