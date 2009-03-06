<?xml version="1.0" encoding="utf-8"?>
<!--
	Version: $Id: view_patient.xsl,v 1.14 2005/04/05 09:48:04 kleong Exp $
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./patient_menu.xsl"/>
	<xsl:include href="./infopanel.xsl"/>
	<xsl:include href="../../common/common_btn_name.xsl"/>
	<xsl:output method="html" indent="no"/>
	<xsl:param name="biospecimenChannelURL">biospecimenChannelURL_false</xsl:param>
	<xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
	<xsl:template match="patient">
		<!-- Call Infopanel Template which will Call "inforpanel_content" Template -->
		<xsl:call-template name="infopanel">
			<xsl:with-param name="activeSubtab">details</xsl:with-param>
			<xsl:with-param name="PATIENT_intInternalPatientID">
				<xsl:value-of select="PATIENT_intInternalPatientID"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="infopanel_content">
		<!-- Get the parameters from the channel class -->
		<xsl:variable name="PATIENT_intInternalPatientID"><xsl:value-of select="PATIENT_intInternalPatientID"/></xsl:variable>
		<xsl:variable name="PATIENT_strPatientID"><xsl:value-of select="PATIENT_strPatientID"/></xsl:variable>
		<xsl:variable name="PATIENT_strHospitalUR"><xsl:value-of select="PATIENT_strHospitalUR"/></xsl:variable>
		<xsl:variable name="PATIENT_strOtherID"><xsl:value-of select="PATIENT_strOtherID"/></xsl:variable>
		<xsl:variable name="PATIENT_strTitle"><xsl:value-of select="PATIENT_strTitle"/></xsl:variable>
		<xsl:variable name="PATIENT_strSurname"><xsl:value-of select="PATIENT_strSurname"/></xsl:variable>
		<xsl:variable name="PATIENT_strFirstName"><xsl:value-of select="PATIENT_strFirstName"/></xsl:variable>
		<xsl:variable name="PATIENT_strOtherNames"><xsl:value-of select="PATIENT_strOtherNames"/></xsl:variable>
		<xsl:variable name="PATIENT_dtDob"><xsl:value-of select="PATIENT_dtDob"/></xsl:variable>
		<xsl:variable name="PATIENT_strAddressLine1"><xsl:value-of select="PATIENT_strAddressLine1"/></xsl:variable>
		<xsl:variable name="PATIENT_strAddressSuburb"><xsl:value-of select="PATIENT_strAddressSuburb"/></xsl:variable>
		<xsl:variable name="PATIENT_strAddressState"><xsl:value-of select="PATIENT_strAddressState"/></xsl:variable>
		<xsl:variable name="PATIENT_strAddressOtherState"><xsl:value-of select="PATIENT_strAddressOtherState"/></xsl:variable>
		<xsl:variable name="PATIENT_strAddressCountry"><xsl:value-of select="PATIENT_strAddressCountry"/></xsl:variable>
		<xsl:variable name="PATIENT_strAddressOtherCountry"><xsl:value-of select="PATIENT_strAddressOtherCountry"/></xsl:variable>
		<xsl:variable name="PATIENT_intAddressPostCode"><xsl:value-of select="PATIENT_intAddressPostCode"/></xsl:variable>
		<xsl:variable name="PATIENT_intPhoneWork"><xsl:value-of select="PATIENT_intPhoneWork"/></xsl:variable>
		<xsl:variable name="PATIENT_intPhoneHome"><xsl:value-of select="PATIENT_intPhoneHome"/></xsl:variable>
		<xsl:variable name="PATIENT_intPhoneMobile"><xsl:value-of select="PATIENT_intPhoneMobile"/></xsl:variable>
		<xsl:variable name="PATIENT_intFlag"><xsl:value-of select="PATIENT_intFlag"/></xsl:variable>
		<xsl:variable name="PATIENT_strStatus"><xsl:value-of select="PATIENT_strStatus"/></xsl:variable>
		<xsl:variable name="PATIENT_strHospital"><xsl:value-of select="PATIENT_strHospital"/></xsl:variable>
		<xsl:variable name="PATIENT_strMedicareNo"><xsl:value-of select="PATIENT_strMedicareNo"/></xsl:variable>
		<xsl:variable name="PATIENT_Timestamp"><xsl:value-of select="PATIENT_Timestamp"/></xsl:variable>
		<xsl:variable name="PATIENT_strSex"><xsl:value-of select="PATIENT_strSex"/></xsl:variable>
		<xsl:variable name="PATIENT_dtDateOfDeath_Year"><xsl:value-of select="PATIENT_dtDateOfDeath_Year"/></xsl:variable>
		<xsl:variable name="PATIENT_strCauseOfDeath"><xsl:value-of select="PATIENT_strCauseOfDeath"/></xsl:variable>
		<xsl:variable name="error"><xsl:value-of select="error"/></xsl:variable>
		<xsl:variable name="flagged"><xsl:value-of select="flagged"/></xsl:variable>

                <!-- New For CCIA -->
                <xsl:variable name="PATIENT_strDataVerified"><xsl:value-of select="PATIENT_strDataVerified" /></xsl:variable>
                <xsl:variable name="PATIENT_strAlert"><xsl:value-of select="PATIENT_strAlert" /></xsl:variable>
                <xsl:variable name="PATIENT_strInitials"><xsl:value-of select="PATIENT_strInitials" /></xsl:variable>
                <xsl:variable name="PATIENT_strStudyNo"><xsl:value-of select="PATIENT_strStudyNo" /></xsl:variable>
                <xsl:variable name="PATIENT_strCentreNo"><xsl:value-of select="PATIENT_strCentreNo" /></xsl:variable>
                <xsl:variable name="PATIENT_strProtocol"><xsl:value-of select="PATIENT_strProtocol" /></xsl:variable>
                <xsl:variable name="PATIENT_strLFU"><xsl:value-of select="PATIENT_strLFU" /></xsl:variable>
                <xsl:variable name="PATIENT_strClinicalStudies"><xsl:value-of select="PATIENT_strClinicalStudies" /></xsl:variable>
                <xsl:variable name="PATIENT_flEFS"><xsl:value-of select="PATIENT_flEFS" /></xsl:variable>

                <script language="javascript">

                    function disableDeadFields(s) 
                    {
                        if (s.options[s.selectedIndex].value == 'Dead')
                        {
                            document.patient_view.PATIENT_dtDateOfDeath_Day.disabled=false;
                            document.patient_view.PATIENT_dtDateOfDeath_Month.disabled=false;
                            document.patient_view.PATIENT_dtDateOfDeath_Year.disabled=false;
                            document.patient_view.PATIENT_strCauseOfDeath.disabled=false;
                        }
                        else
                        {
                            document.patient_view.PATIENT_dtDateOfDeath_Day.disabled=true;
                            document.patient_view.PATIENT_dtDateOfDeath_Month.disabled=true;
                            document.patient_view.PATIENT_dtDateOfDeath_Year.disabled=true;
                            document.patient_view.PATIENT_strCauseOfDeath.disabled=true;
                            document.patient_view.PATIENT_dtDateOfDeath_Day.value='';
                            document.patient_view.PATIENT_dtDateOfDeath_Month.value='';
                            document.patient_view.PATIENT_dtDateOfDeath_Year.value='';
                        }            
                    }    

                </script>  
                                                    
		<form name="patient_view" action="{$baseActionURL}?action=view_patient" method="post" >
			<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
				<!-- Content -->
				<tr valign="top" height="100%">
					<td align="left">
						<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
							<tr valign="top">
								<td align="left">
									<!-- Search Navigation Links -->
									<table width="100%">
										<tr>
											<td width="80%">
												<!-- Search Navigation Links -->
												&lt;&lt;
												<a href="{$baseActionURL}?uP_root=root&amp;action=previousPatient&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;previousPatient=true">Previous Match</a>
												|
												<a href="{$baseActionURL}?uP_root=root&amp;action=search_patient&amp;submit=true">Search Results</a>
												|
												<a href="{$baseActionURL}?uP_root=root&amp;action=nextPatient&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;nextPatient=true"
>Next Match</a>
												&gt;&gt;
											</td>
										</tr>
									</table>
									<!-- Error Messages -->
									<table width="100%">
										<tr>
											<td class="message_text" width="80%">
												<xsl:value-of select="error"/>
											</td>
										</tr>
									</table>
									<!-- New Patient Details -->
									<table cellpadding="0" cellspacing="0" width="100%" border="0">
										<tr valign="top">
											<!-- Left Details Column -->
											<td align="left" width="50%">
												<table cellpadding="0" cellspacing="4" width="100%" border="0">
                                                                                                        <!-- row 1 -->
													<tr valign="top">
														<td class="form_label" width="40%"><xsl:value-of select="PATIENT_strPatientIDDisplay"/>:</td>
														<td width="10">&#160;</td>
                                                                                                                <td class="form_label" width="60%"><xsl:value-of select="PATIENT_strPatientID"/></td>
													</tr>
                                                                                                        <!-- row 2 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strHospitalURDisplay"/>:</td>
														<td width="10">&#160;</td>
                                                                                                                <td><input type="text" align="right" name="PATIENT_strHospitalUR" tabindex="21" value="{$PATIENT_strHospitalUR}"/></td>
													</tr>
                                                                                                        <!-- row 3 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strFirstNameDisplay"/>:</td>
														<td width="10">&#160;</td>
                                                                                                                <td><input type="text" name="PATIENT_strFirstName" size="20" tabindex="22" value="{$PATIENT_strFirstName}"/></td>
													</tr>
                                                                                                        <!-- row 4 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strSurnameDisplay"/>:</td>
														<td width="10">&#160;</td>
                                                                                                                <td><input type="text" name="PATIENT_strSurname" size="20" tabindex="23" value="{$PATIENT_strSurname}"/></td>
													</tr>
                                                                                                        <!-- row 5 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strSexDisplay"/>:<span class="required_text">*</span></td>
														<td width="10">&#160;</td>
                                                                                                                <td>
															<select name="PATIENT_strSex" tabindex="24">
																<xsl:for-each select="PATIENT_strSex">
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
                                                                                                        <!-- row 6 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strInitialsDisplay"/>:</td>
                                                                                                                <td width="10">&#160;</td>
														<td><input type="text" name="PATIENT_strInitials" size="20" tabindex="25" value="{$PATIENT_strInitials}"/></td>
													</tr>
													<tr class="form_separator"><td colspan="3"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td></tr>
                                                                                                        <!-- row 7 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strStudyNoDisplay"/>:</td>
														<td width="10">&#160;</td>
                                                                                                                <td><input type="text" name="PATIENT_strStudyNo" size="20" tabindex="26" value="{$PATIENT_strStudyNo}"/></td>
													</tr>
                                                                                                        <!-- row 8 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strCentreNoDisplay"/>:</td>
														<td width="10">&#160;</td>
                                                                                                                <td><input type="text" name="PATIENT_strCentreNo" size="20" tabindex="27" value="{$PATIENT_strCentreNo}"/></td>
													</tr>
                                                                                                        <!-- row 9 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strProtocolDisplay"/>:</td>
														<td width="10">&#160;</td>
                                                                                                                <td>
															<select name="PATIENT_strProtocol" tabindex="28">
																<xsl:for-each select="PATIENT_strProtocol">
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
                                                                                                        <!-- row 10 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_intFlagDisplay"/>:</td>
														<td width="10">&#160;</td>
                                                                                                                <td>
															<xsl:choose>
																<xsl:when test=" string-length( $flagged ) &gt; 0">
																	<input type="checkbox" class="checkbox" name="PATIENT_intFlag" tabindex="29" value="1" checked="true"/>
																</xsl:when>
																<xsl:otherwise>
																	<input type="checkbox" class="checkbox" name="PATIENT_intFlag" tabindex="29" value="1"/>
																</xsl:otherwise>
															</xsl:choose>
														</td>
													</tr>
                                                                                                        <tr class="form_separator"><td colspan="3"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td></tr>
                                                                                                        <!-- row 11 -->
                                                                                                        <tr valign="top">
														<td class="form_label" width="40%"><xsl:value-of select="ADMISSIONS_dtDiagnosisDisplay"/>:</td>
														<td width="10">&#160;</td>
                                                                                                                <td class="form_label" width="60%"><xsl:value-of select="ADMISSIONS_dtDiagnosis"/></td>
													</tr>
                                                                                                        <!-- row 12 -->
                                                                                                        <tr valign="top">
														<td class="form_label" width="40%"><xsl:value-of select="ADMISSIONS_strDiagCategoryDisplay"/>:</td>
														<td width="10">&#160;</td>
                                                                                                                <td class="form_label" width="60%"><xsl:value-of select="ADMISSIONS_strDiagCategory"/></td>
													</tr>
												</table>
											</td>
											<td width="25%"><img width="20" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
											<!-- Right Details Column -->
											<td align="left" width="25%">
												<table cellpadding="0" cellspacing="4" width="100%" height="100%" border="0">
                                                                                                        <!-- row 1 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_dtDobDisplay"/>:<span class="required_text"></span></td>
														
														<td>
															<!-- Date-of-Birth -->
															<xsl:choose>
																<xsl:when test="PATIENT_dtDob/@display_type='text'">
																	<input type="text" name="PATIENT_dtDob_Day" size="2" tabindex="30">
																		<xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDob_Day"/></xsl:attribute>
																	</input>
																	<input type="text" name="PATIENT_dtDob_Month" size="2" tabindex="31">
																		<xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDob_Month"/></xsl:attribute>
																	</input>
																</xsl:when>
																<!-- when it is drop down -->
																<xsl:otherwise>
																	<select name="PATIENT_dtDob_Day" tabindex="30">
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
																	<select name="PATIENT_dtDob_Month" tabindex="31">
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
																</xsl:otherwise>
															</xsl:choose>
															<input type="text" name="PATIENT_dtDob_Year" size="4" tabindex="32">
																<xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDob_Year"/></xsl:attribute>
															</input>
														</td>
													</tr>
                                                                                                        <!-- row 2 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strStatusDisplay"/>:</td>
														
														<td>
															<select name="PATIENT_strStatus" tabindex="33"  onChange="javascript:disableDeadFields(this);">
																<xsl:for-each select="PATIENT_strStatus">
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
                                                                                                        <!-- row 3 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_dtDateOfDeathDisplay"/>:</td>
														<td>
															<xsl:choose>
																<xsl:when test="PATIENT_dtDateOfDeath/@display_type='dropdown'">
																	<select name="PATIENT_dtDateOfDeath_Day" tabindex="34" class="uportal-input-text">
																		<option value=""/>
																		<xsl:for-each select="PATIENT_dtDateOfDeath_Day">
																			<option>
																				<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
																				<xsl:if test="@selected = '1'">
																					<xsl:attribute name="selected">true</xsl:attribute>
																				</xsl:if>
																				<xsl:value-of select="."/>
																			</option>
																		</xsl:for-each>
																	</select>
																	<select name="PATIENT_dtDateOfDeath_Month" tabindex="35" class="uportal-input-text">
																		<option value=""/>
																		<xsl:for-each select="PATIENT_dtDateOfDeath_Month">
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
																	<input type="text" name="PATIENT_dtDateOfDeath_Day" size="2" tabindex="34" class="uportal-input-text">
																		<xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDateOfDeath_Day"/></xsl:attribute>
																	</input>
																	<input type="text" name="PATIENT_dtDateOfDeath_Month" size="2" tabindex="35" class="uportal-input-text">
																		<xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDateOfDeath_Month"/></xsl:attribute>
																	</input>
																</xsl:otherwise>
															</xsl:choose>
															<input type="text" name="PATIENT_dtDateOfDeath_Year" size="4" tabindex="36" class="uportal-input-text">
																<xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDateOfDeath_Year"/></xsl:attribute>
															</input>
														</td>
													</tr>
                                                                                                        <!-- row 4 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strCauseOfDeathDisplay"/>:</td>
														<td>
															<select name="PATIENT_strCauseOfDeath" tabindex="37">
																<xsl:for-each select="PATIENT_strCauseOfDeath">
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
                                                                                                        <!-- row 5 -->
													<tr class="form_separator"><td colspan="3"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td></tr>
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_intAddressPostCodeDisplay"/>:</td>
														<td><input type="text" align="right" name="PATIENT_intAddressPostCode" size="20" tabindex="38" value="{$PATIENT_intAddressPostCode}"/></td>
													</tr>
                                                                                                        <!-- row 6 -->
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strDataVerifiedDisplay"/>:</td>
														<td width="25%">
                                                                                                                        <input type="checkbox" name="PATIENT_strDataVerified" tabindex="39">
                                                                                                                                <xsl:if test="string-length( $PATIENT_strDataVerified ) > '0'">
                                                                                                                                        <xsl:attribute name="checked">true</xsl:attribute>
                                                                                                                                </xsl:if>
                                                                                                                        </input>
                                                                                                                </td>
													</tr>
													<!-- row 7 -->
													<tr class="form_separator"><td colspan="3"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td></tr>
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strClinicalStudiesDisplay"/>:</td>
														<td><input type="text" align="right" name="PATIENT_strClinicalStudies" size="20" tabindex="40" value="{$PATIENT_strClinicalStudies}"/></td>
													</tr>
                                                                                                        <!-- row 8 -->
                                                                                                        <tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strLFUDisplay"/>:</td>
														<td><input type="text" align="right" name="PATIENT_strLFU" size="20" tabindex="41" value="{$PATIENT_strLFU}"/></td>
													</tr>
                                                                                                        <!-- row 9 -->
                                                                                                        <tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strAlertDisplay"/>:</td>
														<td>
															<select name="PATIENT_strAlert" tabindex="42">
																<xsl:for-each select="PATIENT_strAlert">
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
                                                                                                        <!-- row 10 -->
                                                                                                        <tr valign="top">
														<td class="form_label" width="40%"><xsl:value-of select="PATIENT_flEFSDisplay"/>:</td>
														
														<td class="form_label" width="60%"><xsl:value-of select="PATIENT_flEFS"/></td>
													</tr>
                                                                                                        <!-- row 11 -->
                                                                                                        <tr class="form_separator"><td colspan="3"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td></tr>
													<tr valign="top">
														<td class="form_label"><xsl:value-of select="PATIENT_strCommentsDisplay"/>:</td>
														
														<td>
															<textarea name="PATIENT_strComments" rows="4" cols="40" tabindex="43">
																<xsl:value-of select="PATIENT_strComments"/>
															</textarea>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<!-- Separator -->
				<tr class="channel_button_strip_separator">
					<td width="100%" height="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
				</tr>
				<tr>
					<td width="100%" height="1"><img width="1" height="4" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
				</tr>

                                <tr>
                                    <td>
                                        <table cellpadding="0" cellspacing="0" width="100%">
                                            <tr valign="top">
                                                    <td class="required_text" align="left">* = Required field</td>
                                            </tr>        
                                        </table>
                                    </td>    
                                </tr>
                                <!-- Buttons -->
				<tr valign="top">
					<td>
						<table cellpadding="0" cellspacing="0" width="100%">
                                                                <tr valign="middle">        
									<td><img width="20" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
									<td align="left" width="100%">
										<!-- Use Link to Perform Delete so an ENTER Keypress will Activate the "Save" Button -->
										<a href="#" onclick="javascript:confirmDelete('{$baseActionURL}?action=delete_patient&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;PATIENT_Timestamp={$PATIENT_Timestamp}')">
											<img src="{$buttonImagePath}/delete_enabled.gif" border="0" alt="Delete"/>
										</a>
									</td>
									<td align="right">
										<!-- IExplorer Hack: since no submit attributes are passed, set "save" parameter -->
										<input type="hidden" name="save" value="not_null"/>
										<!-- Default Form Submission -->
										<input class="submit_image_button" src="{$buttonImagePath}/save_enabled.gif" type="image" name="save" value="{$saveBtnLabel}" tabindex="47" onblur="javascript:document.patient_view.PATIENT_strHospitalUR.focus()"/>
									</td>
								</tr>
						</table>
					</td>
				</tr>
			</table>
			<!-- add the Patient Internal ID as a hidden field to be submitted to the channel-->
			<input type="hidden" name="PATIENT_strPatientID" value="{$PATIENT_strPatientID}"/>
			<input type="hidden" name="PATIENT_intInternalPatientID" value="{$PATIENT_intInternalPatientID}"/>
			<!--input type="hidden" name="PATIENT_Timestamp" value="{$PATIENT_Timestamp}"/-->
		</form>
		<script language="JavaScript">
			document.patient_view.PATIENT_strHospitalUR.focus();
                        disableDeadFields(document.patient_view.PATIENT_strStatus);
		</script>
	</xsl:template>
</xsl:stylesheet>
