<?xml version="1.0" encoding="utf-8"?>
<!--
	Version: $Id: appointments.xsl,v 1.8 2005/03/24 07:05:50 kleong Exp $
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./patient_menu.xsl"/>
	<xsl:include href="./infopanel.xsl"/>
	<xsl:include href="../../common/common_btn_name.xsl"/>
	<xsl:output method="html" indent="no"/>
	<xsl:template match="patient">
		<!-- Call Infopanel Template which will Call "patient_content" Template -->
		<xsl:call-template name="infopanel">
			<xsl:with-param name="activeSubtab">followUp</xsl:with-param>
			<xsl:with-param name="PATIENT_intInternalPatientID">
				<xsl:value-of select="PATIENT_intInternalPatientID"/>
			</xsl:with-param>
			<xsl:with-param name="contentTemplate">patient_content</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="infopanel_content">
		<!-- Get the parameters from the channel class -->
		<xsl:param name="PATIENT_intInternalPatientID">
			<xsl:value-of select="PATIENT_intInternalPatientID"/>
		</xsl:param>
		<xsl:variable name="APPOINTMENTS_intInternalPatientID">
			<xsl:value-of select="APPOINTMENTS_intInternalPatientID"/>
		</xsl:variable>
		<xsl:variable name="APPOINTMENTS_dtAppDate">
			<xsl:value-of select="APPOINTMENTS_dtAppDate"/>
		</xsl:variable>
		<xsl:variable name="APPOINTMENTS_strAppPurpose">
			<xsl:value-of select="APPOINTMENTS_strAppPurpose"/>
		</xsl:variable>
		<xsl:variable name="APPOINTMENTS_strAppNotify">
			<xsl:value-of select="APPOINTMENTS_strAppNotify"/>
		</xsl:variable>
		<xsl:variable name="APPOINTMENTS_dtAppAlertDate">
			<xsl:value-of select="APPOINTMENTS_dtAppAlertDate"/>
		</xsl:variable>
		<xsl:variable name="APPOINTMENTS_tmAppTime">
			<xsl:value-of select="APPOINTMENTS_tmAppTime"/>
		</xsl:variable>
		<xsl:variable name="APPOINTMENTS_intSurveyID">
			<xsl:value-of select="APPOINTMENTS_intSurveyID"/>
		</xsl:variable>
		<xsl:variable name="strEdit">
			<xsl:value-of select="strEdit"/>
		</xsl:variable>
		<xsl:variable name="APPOINTMENTS_intAppointmentID">
			<xsl:value-of select="APPOINTMENTS_intAppointmentID"/>
		</xsl:variable>
		<form name="appointment_view" action="{$baseActionURL}?action=appointments" method="post">
			<table width="100%">
				<tr>
					<td class="neuragenix-form-required-text" width="80%">
						<xsl:value-of select="error"/>
					</td>
					<td class="neuragenix-form-required-text" width="20%" id="neuragenix-required-header" align="right">
			* = Required fields
			</td>
				</tr>
			</table>
			<table width="100%" cellspacing="0">
				<tr>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="14%" class="uportal-channel-table-header">Appointments
			</td>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="14%" class="uportal-label">
			</td>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="22%" class="uportal-label"/>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="12%" class="uportal-label"/>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="16%" class="uportal-label">
			</td>
					<!--td width="10%" class="uportal-label">
			</td-->
					<td width="5%" class="uportal-label">
			</td>
				</tr>
				<tr>
					<td width="1%" class="neuragenix-form-required-text">*</td>
					<td width="14%" class="uportal-label">
						<xsl:value-of select="APPOINTMENTS_dtAppDateDisplay"/>
					</td>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="14%" class="uportal-label">
						<xsl:value-of select="APPOINTMENTS_tmAppTimeDisplay"/>
					</td>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="22%" class="uportal-label">
						<xsl:value-of select="APPOINTMENTS_strAppPurposeDisplay"/>
					</td>
					<td width="1%" class="neuragenix-form-required-text">*</td>
					<td width="12%" class="uportal-label">
						<xsl:value-of select="APPOINTMENTS_strAppNotifyDisplay"/>
					</td>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="16%" class="uportal-label">
						<xsl:value-of select="APPOINTMENTS_dtAppAlertDateDisplay"/>
					</td>
					<!--td width="10%" class="uportal-label">
			<xsl:value-of select="SMARTFORM_strSmartformNameDisplay" />
			</td-->
					<td width="5%" class="uportal-label">
			</td>
				</tr>
				<tr>
					<td height="10px" colspan="12">
						<hr/>
					</td>
				</tr>
				<xsl:for-each select="appointment">
					<xsl:variable name="varDataTest">
						<xsl:value-of select="dtAppDate"/>
					</xsl:variable>
					<xsl:variable name="APPOINTMENTS_intInternalPatientID">
						<xsl:value-of select="APPOINTMENTS_intInternalPatientID"/>
					</xsl:variable>
					<xsl:variable name="APPOINTMENTS_dtAppDate">
						<xsl:value-of select="APPOINTMENTS_dtAppDate"/>
					</xsl:variable>
					<xsl:variable name="APPOINTMENTS_tmAppTime">
						<xsl:value-of select="APPOINTMENTS_tmAppTime"/>
					</xsl:variable>
					<xsl:variable name="APPOINTMENTS_strAppPurpose">
						<xsl:value-of select="APPOINTMENTS_strAppPurpose"/>
					</xsl:variable>
					<xsl:variable name="APPOINTMENTS_strAppNotify">
						<xsl:value-of select="APPOINTMENTS_strAppNotify"/>
					</xsl:variable>
					<xsl:variable name="APPOINTMENTS_dtAppAlertDate">
						<xsl:value-of select="APPOINTMENTS_dtAppAlertDate"/>
					</xsl:variable>
					<xsl:variable name="APPOINTMENTS_tmAppTime">
						<xsl:value-of select="APPOINTMENTS_tmAppTime"/>
					</xsl:variable>
					<xsl:variable name="APPOINTMENTS_intSurveyID">
						<xsl:value-of select="APPOINTMENTS_intSurveyID"/>
					</xsl:variable>
					<xsl:variable name="APPOINTMENTS_intAppointmentKey">
						<xsl:value-of select="APPOINTMENTS_intAppointmentKey"/>
					</xsl:variable>
					<xsl:variable name="APPOINTMENTS_Timestamp">
						<xsl:value-of select="APPOINTMENTS_Timestamp"/>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="(position() mod 2) != 0">
							<tr class="uportal-input-text">
								<td width="1%"/>
								<td width="14%" class="uportal-input-text">
									<xsl:value-of select="APPOINTMENTS_dtAppDate"/>
								</td>
								<td width="1%"/>
								<td width="14%" class="uportal-input-text">
									<xsl:value-of select="APPOINTMENTS_tmAppTime"/>
								</td>
								<td width="1%"/>
								<td width="22%" class="uportal-input-text">
									<xsl:value-of select="APPOINTMENTS_strAppPurpose"/>
								</td>
								<td width="1%"/>
								<td width="12%" class="uportal-input-text">
									<xsl:value-of select="APPOINTMENTS_strAppNotify"/>
								</td>
								<td width="1%"/>
								<td width="16%" class="uportal-input-text">
									<xsl:value-of select="APPOINTMENTS_dtAppAlertDate"/>
								</td>
								<!--td width="10%" class="uportal-text">
                            <xsl:for-each select="../smartforms/study/smartform">   
                                <xsl:variable name="SurveyKey"><xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformID" /></xsl:variable>
                                <xsl:if test="$SurveyKey=$APPOINTMENTS_intSurveyID">
                                            <xsl:value-of select="SMARTFORM_strSmartformName" />
                                </xsl:if>
                            </xsl:for-each>
			</td-->
								<td width="5%" class="uportal-input-text">
									<a href="{$baseActionURL}?action=appointments&amp;editAppointment={$APPOINTMENTS_intAppointmentKey}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;APPOINTMENTS_Timestamp={$APPOINTMENTS_Timestamp}">Edit</a>
								</td>
								<td width="5%" class="uportal-input-text">
									<xsl:if test="$APPOINTMENTS_intAppointmentKey != ''">
										<a href="javascript:confirmDelete('{$baseActionURL}?action=appointments&amp;deleteAppointment=true&amp;APPOINTMENTS_intAppointmentKey={$APPOINTMENTS_intAppointmentKey}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;APPOINTMENTS_Timestamp={$APPOINTMENTS_Timestamp}')">Del</a>
									</xsl:if>
								</td>
							</tr>
						</xsl:when>
						<xsl:otherwise>
							<tr class="uportal-text">
								<td width="1%"/>
								<td width="14%" class="uportal-text">
									<xsl:value-of select="APPOINTMENTS_dtAppDate"/>
								</td>
								<td width="1%"/>
								<td width="14%" class="uportal-text">
									<xsl:value-of select="APPOINTMENTS_tmAppTime"/>
								</td>
								<td width="1%"/>
								<td width="22%" class="uportal-text">
									<xsl:value-of select="APPOINTMENTS_strAppPurpose"/>
								</td>
								<td width="1%"/>
								<td width="12%" class="uportal-text">
									<xsl:value-of select="APPOINTMENTS_strAppNotify"/>
								</td>
								<td width="1%"/>
								<td width="16%" class="uportal-text">
									<xsl:value-of select="APPOINTMENTS_dtAppAlertDate"/>
								</td>
								<!--td width="10%" class="uportal-text">
                            <xsl:for-each select="../smartforms/study/smartform">   
                                <xsl:variable name="SurveyKey"><xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformID" /></xsl:variable>
                                <xsl:if test="$SurveyKey=$APPOINTMENTS_intSurveyID">
                                            <xsl:value-of select="SMARTFORM_strSmartformName" />
                                </xsl:if>
                            </xsl:for-each>
			</td-->
								<td width="5%" class="uportal-text">
									<a href="{$baseActionURL}?action=appointments&amp;editAppointment={$APPOINTMENTS_intAppointmentKey}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;APPOINTMENTS_Timestamp={$APPOINTMENTS_Timestamp}">Edit</a>
								</td>
								<td width="5%" class="uportal-text">
									<xsl:if test="$APPOINTMENTS_intAppointmentKey != ''">
										<a href="javascript:confirmDelete('{$baseActionURL}?action=appointments&amp;deleteAppointment=true&amp;APPOINTMENTS_intAppointmentKey={$APPOINTMENTS_intAppointmentKey}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;APPOINTMENTS_Timestamp={$APPOINTMENTS_Timestamp}')">Del</a>
									</xsl:if>
								</td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				<tr>
					<td height="10px" colspan="12"/>
				</tr>
				<tr>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="14%" class="uportal-label">
						<xsl:choose>
							<xsl:when test="APPOINTMENTS_dtAppDate/@display_type='dropdown'">
								<select name="APPOINTMENTS_dtAppDate_Day" tabindex="21" class="uportal-input-text">
									<xsl:choose>
										<xsl:when test="$strEdit > 0">
											<xsl:for-each select="editFields/APPOINTMENTS_dtAppDate_Day">
												<option>
													<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
													<xsl:if test="@selected = '1'">
														<xsl:attribute name="selected">true</xsl:attribute>
													</xsl:if>
													<xsl:value-of select="."/>
												</option>
											</xsl:for-each>
										</xsl:when>
										<xsl:otherwise>
											<xsl:for-each select="APPOINTMENTS_dtAppDate_Day">
												<option>
													<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
													<xsl:if test="@selected = '1'">
														<xsl:attribute name="selected">true</xsl:attribute>
													</xsl:if>
													<xsl:value-of select="."/>
												</option>
											</xsl:for-each>
										</xsl:otherwise>
									</xsl:choose>
								</select>
								<select name="APPOINTMENTS_dtAppDate_Month" tabindex="22" class="uportal-input-text">
									<xsl:choose>
										<xsl:when test="$strEdit > 0">
											<xsl:for-each select="editFields/APPOINTMENTS_dtAppDate_Month">
												<option>
													<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
													<xsl:if test="@selected = '1'">
														<xsl:attribute name="selected">true</xsl:attribute>
													</xsl:if>
													<xsl:value-of select="."/>
												</option>
											</xsl:for-each>
										</xsl:when>
										<xsl:otherwise>
											<xsl:for-each select="APPOINTMENTS_dtAppDate_Month">
												<option>
													<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
													<xsl:if test="@selected = '1'">
														<xsl:attribute name="selected">true</xsl:attribute>
													</xsl:if>
													<xsl:value-of select="."/>
												</option>
											</xsl:for-each>
										</xsl:otherwise>
									</xsl:choose>
								</select>
							</xsl:when>
							<xsl:otherwise>
								<input type="text" name="APPOINTMENTS_dtAppDate_Day" size="2" tabindex="21" class="uportal-input-text">
									<xsl:choose>
										<xsl:when test="$strEdit > 0">
											<xsl:attribute name="value"><xsl:value-of select="editFields/APPOINTMENTS_dtAppDate_Day"/></xsl:attribute>
										</xsl:when>
										<xsl:otherwise>
											<xsl:attribute name="value"><xsl:value-of select="APPOINTMENTS_dtAppDate_Day"/></xsl:attribute>
										</xsl:otherwise>
									</xsl:choose>
								</input>
								<input type="text" name="APPOINTMENTS_dtAppDate_Month" size="2" tabindex="22" class="uportal-input-text">
									<xsl:choose>
										<xsl:when test="$strEdit > 0">
											<xsl:attribute name="value"><xsl:value-of select="editFields/APPOINTMENTS_dtAppDate_Month"/></xsl:attribute>
										</xsl:when>
										<xsl:otherwise>
											<xsl:attribute name="value"><xsl:value-of select="APPOINTMENTS_dtAppDate_Month"/></xsl:attribute>
										</xsl:otherwise>
									</xsl:choose>
								</input>
							</xsl:otherwise>
						</xsl:choose>
						<!-- end of configurable input box types -->
						<input type="text" name="APPOINTMENTS_dtAppDate_Year" size="4" tabindex="23" class="uportal-input-text">
							<xsl:choose>
								<xsl:when test="$strEdit > 0">
									<xsl:attribute name="value"><xsl:value-of select="editFields/APPOINTMENTS_dtAppDate_Year"/></xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="value"><xsl:value-of select="APPOINTMENTS_dtAppDate_Year"/></xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</input>
					</td>
					<!--td width="12%" class="uportal-label">
			<input type="text" name="dtAppDate" size="12" value="{$dtAppDate}" class="uportal-input-text" />
			</td-->
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="14%" class="uportal-label">
						<select name="APPOINTMENTS_tmAppTime_Hour" tabindex="24" class="uportal-input-text">
							<xsl:choose>
								<xsl:when test="$strEdit > 0">
									<xsl:for-each select="editFields/APPOINTMENTS_tmAppTime_Hour">
										<option>
											<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
											<xsl:if test="@selected=1">
												<xsl:attribute name="selected">true</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</option>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="APPOINTMENTS_tmAppTime_Hour">
										<option>
											<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
											<xsl:if test="@selected=1">
												<xsl:attribute name="selected">true</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</option>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</select>
						<select name="APPOINTMENTS_tmAppTime_Minute" tabindex="25" class="uportal-input-text">
							<xsl:choose>
								<xsl:when test="$strEdit > 0">
									<xsl:for-each select="editFields/APPOINTMENTS_tmAppTime_Minute">
										<option>
											<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
											<xsl:if test="@selected = '1'">
												<xsl:attribute name="selected">true</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</option>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="APPOINTMENTS_tmAppTime_Minute">
										<option>
											<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
											<xsl:if test="@selected = '1'">
												<xsl:attribute name="selected">true</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</option>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</select>
						<select name="APPOINTMENTS_tmAppTime_AMPM" tabindex="26" class="uportal-input-text">
							<xsl:choose>
								<xsl:when test="$strEdit > 0">
									<xsl:for-each select="editFields/APPOINTMENTS_tmAppTime_AMPM">
										<option>
											<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
											<xsl:if test="@selected = '1'">
												<xsl:attribute name="selected">true</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</option>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="APPOINTMENTS_tmAppTime_AMPM">
										<option>
											<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
											<xsl:if test="@selected = '1'">
												<xsl:attribute name="selected">true</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</option>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</select>
					</td>
					<!--td width="12%" class="uportal-label">
			<input type="text" name="tmAppTime" size="12" value="{$tmAppTime}" class="uportal-input-text" />
			</td-->
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="22%" class="uportal-label">
						<xsl:choose>
							<xsl:when test="$strEdit > 0">
								<xsl:variable name="editAppointment_strAppPurpose">
									<xsl:value-of select="editFields/APPOINTMENTS_strAppPurpose"/>
								</xsl:variable>
								<textarea name="APPOINTMENTS_strAppPurpose" rows="3" cols="40" tabindex="27" class="uportal-input-text">
									<xsl:value-of select="editFields/APPOINTMENTS_strAppPurpose"/>
								</textarea>
							</xsl:when>
							<xsl:otherwise>
								<textarea name="APPOINTMENTS_strAppPurpose" rows="3" cols="40" tabindex="27" class="uportal-input-text">
									<xsl:value-of select="APPOINTMENTS_strAppPurpose"/>
								</textarea>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="12%" class="uportal-label">
						<select class="uportal-input-text" tabindex="28" name="APPOINTMENTS_strAppNotify">
							<xsl:variable name="editFields_strNotifyEmail">
								<xsl:value-of select="editFields/APPOINTMENTS_strAppNotify"/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$strEdit > 0">
									<xsl:for-each select="user">
										<xsl:sort select="USERDETAILS_strEmail"/>
										<xsl:variable name="USERDETAILS_strEmail">
											<xsl:value-of select="USERDETAILS_strEmail"/>
										</xsl:variable>
										<xsl:variable name="USERDETAILS_strUserName">
											<xsl:value-of select="USERDETAILS_strUserName"/>
										</xsl:variable>
										<xsl:if test="string-length( $USERDETAILS_strEmail ) > '0'">
											<option>
												<xsl:attribute name="value"><xsl:value-of select="USERDETAILS_strUserName"/></xsl:attribute>
												<xsl:if test="$editFields_strNotifyEmail=$USERDETAILS_strUserName">
													<xsl:attribute name="selected">true</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="USERDETAILS_strUserName"/>
											</option>
										</xsl:if>
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="user">
										<xsl:sort select="USERDETAILS_strEmail"/>
										<xsl:variable name="USERDETAILS_strEmail">
											<xsl:value-of select="USERDETAILS_strEmail"/>
										</xsl:variable>
										<xsl:if test="string-length( $USERDETAILS_strEmail ) > '0'">
											<option>
												<xsl:attribute name="value"><xsl:value-of select="USERDETAILS_strUserName"/></xsl:attribute>
												<xsl:value-of select="USERDETAILS_strUserName"/>
											</option>
										</xsl:if>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</select>
					</td>
					<td width="1%" class="neuragenix-form-required-text"/>
					<td width="16%" class="uportal-label">
						<xsl:choose>
							<xsl:when test="APPOINTMENTS_dtAppAlertDate/@display_type='dropdown'">
								<select name="APPOINTMENTS_dtAppAlertDate_Day" tabindex="29" class="uportal-input-text">
									<xsl:choose>
										<xsl:when test="$strEdit > 0">
											<xsl:for-each select="editFields/APPOINTMENTS_dtAppAlertDate_Day">
												<option>
													<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
													<xsl:if test="@selected = '1'">
														<xsl:attribute name="selected">true</xsl:attribute>
													</xsl:if>
													<xsl:value-of select="."/>
												</option>
											</xsl:for-each>
										</xsl:when>
										<xsl:otherwise>
											<xsl:for-each select="APPOINTMENTS_dtAppAlertDate_Day">
												<option>
													<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
													<xsl:if test="@selected = '1'">
														<xsl:attribute name="selected">true</xsl:attribute>
													</xsl:if>
													<xsl:value-of select="."/>
												</option>
											</xsl:for-each>
										</xsl:otherwise>
									</xsl:choose>
								</select>
								<select name="APPOINTMENTS_dtAppAlertDate_Month" tabindex="30" class="uportal-input-text">
									<xsl:choose>
										<xsl:when test="$strEdit > 0">
											<xsl:for-each select="editFields/APPOINTMENTS_dtAppAlertDate_Month">
												<option>
													<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
													<xsl:if test="@selected = '1'">
														<xsl:attribute name="selected">true</xsl:attribute>
													</xsl:if>
													<xsl:value-of select="."/>
												</option>
											</xsl:for-each>
										</xsl:when>
										<xsl:otherwise>
											<xsl:for-each select="APPOINTMENTS_dtAppAlertDate_Month">
												<option>
													<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
													<xsl:if test="@selected = '1'">
														<xsl:attribute name="selected">true</xsl:attribute>
													</xsl:if>
													<xsl:value-of select="."/>
												</option>
											</xsl:for-each>
										</xsl:otherwise>
									</xsl:choose>
								</select>
							</xsl:when>
							<xsl:otherwise>
								<input type="text" name="APPOINTMENTS_dtAppAlertDate_Day" size="2" tabindex="29" class="uportal-input-text">
									<xsl:choose>
										<xsl:when test="$strEdit > 0">
											<xsl:attribute name="value"><xsl:value-of select="editFields/APPOINTMENTS_dtAppAlertDate_Day"/></xsl:attribute>
										</xsl:when>
										<xsl:otherwise>
											<xsl:attribute name="value"><xsl:value-of select="APPOINTMENTS_dtAppAlertDate_Day"/></xsl:attribute>
										</xsl:otherwise>
									</xsl:choose>
								</input>
								<input type="text" name="APPOINTMENTS_dtAppAlertDate_Month" size="2" tabindex="30" class="uportal-input-text">
									<xsl:choose>
										<xsl:when test="$strEdit > 0">
											<xsl:attribute name="value"><xsl:value-of select="editFields/APPOINTMENTS_dtAppAlertDate_Month"/></xsl:attribute>
										</xsl:when>
										<xsl:otherwise>
											<xsl:attribute name="value"><xsl:value-of select="APPOINTMENTS_dtAppAlertDate_Month"/></xsl:attribute>
										</xsl:otherwise>
									</xsl:choose>
								</input>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:variable name="editFields_strUserName">
							<xsl:value-of select="USERDETAILS_strUserName"/>
						</xsl:variable>
						<input type="text" name="APPOINTMENTS_dtAppAlertDate_Year" size="4" tabindex="31" class="uportal-input-text">
							<xsl:choose>
								<xsl:when test="$strEdit > 0">
									<xsl:attribute name="value"><xsl:value-of select="editFields/APPOINTMENTS_dtAppAlertDate_Year"/></xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="value"><xsl:value-of select="APPOINTMENTS_dtAppAlertDate_Year"/></xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</input>
					</td>
					<!--td width="12%" class="uportal-label">
			<input type="text" name="dtAppAlertDate" size="12" value="{$dtAppAlertDate}" class="uportal-input-text" />
			</td-->
					<!--td width="10%" class="uportal-label">
			<xsl:variable name="editFields_intSurveyID"><xsl:value-of select="editFields/APPOINTMENTS_intSurveyID" /></xsl:variable>

                        <select class="uportal-input-text" name="APPOINTMENTS_intSurveyID" >
                        
                        <xsl:choose>
                            <xsl:when test="$strEdit > 0">
                                <xsl:for-each select="smartforms/study/smartform">
                                    <xsl:sort select="SMARTFORM_strSmartformName" />
                                    <option> 
                                            <xsl:variable name="SurveyKey"><xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformID" /></xsl:variable>
                                            <xsl:attribute name="value">
                                                    <xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformID" />
                                            </xsl:attribute> 
                                            <xsl:if test="$SurveyKey=$editFields_intSurveyID">
                                                        <xsl:attribute name="selected">true</xsl:attribute>
                                            </xsl:if>
                                            <xsl:value-of select="SMARTFORM_strSmartformName" />
                                    </option>
                                </xsl:for-each>
                            </xsl:when>
                            <xsl:otherwise>
                                    <option value=""/>
                                    <xsl:for-each select="smartforms/study/smartform">
                                        <xsl:sort select="SMARTFORM_strSmartformName" />
                                            <option> 
                                                    <xsl:attribute name="value">
                                                            <xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformID" />
                                                    </xsl:attribute> 
                                                    <xsl:value-of select="SMARTFORM_strSmartformName" />
                                            </option>
			            </xsl:for-each>
                            </xsl:otherwise>
                        </xsl:choose>
                        </select>
                  
			</td-->
					<td width="5%" class="uportal-label">
			
			</td>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td width="50%" class="uportal-label" align="left">
						<input type="submit" name="cancel" value="{$cancelBtnLabel}" class="uportal-button"/>
					</td>
					<td width="50%" class="uportal-label" align="right">
						<input type="submit" name="saveAppointment" tabindex="32" value="{$saveBtnLabel}" class="uportal-button" onblur="javascript:document.appointment_view.APPOINTMENTS_dtAppDate_Day.focus()"/>
					</td>
				</tr>
			</table>
			<!-- add the Patient Internal ID as a hidden field to be submitted to the channel-->
			<input type="hidden" name="PATIENT_intInternalPatientID" value="{$PATIENT_intInternalPatientID}"/>
			<xsl:if test="$strEdit > 0">
				<input type="hidden" name="updateAppointment" value="{$strEdit}"/>
			</xsl:if>
			<!--input type="hidden" name="intAppointmentID" value="" /-->
		</form>
		<script language="javascript">
			document.appointment_view.APPOINTMENTS_dtAppDate_Day.focus();
		</script>
	</xsl:template>
</xsl:stylesheet>
