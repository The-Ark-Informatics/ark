<?xml version="1.0" encoding="utf-8"?>
<!--
	Version: $Id: search_results.xsl,v 1.6 2005/04/04 04:48:59 daniel Exp $
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./patient_menu.xsl"/>
	<xsl:include href="../../common/common_btn_name.xsl"/>
	<xsl:include href="./infopanel.xsl"/>
	<xsl:output method="html" indent="no"/>
	<!-- Declare Image Paths as Variables -->
	<xsl:param name="biospecimenChannelURL">biospecimenChannelURL_false</xsl:param>
	<xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
	<xsl:template match="/body/results">
		<xsl:variable name="intStudyID">
			<xsl:value-of select="intStudyID"/>
		</xsl:variable>
		<!-- Call Infopanel Template which will Call "inforpanel_content" Template -->
		<xsl:call-template name="infopanel">
			<xsl:with-param name="titleString">SEARCH RESULTS</xsl:with-param>
			<xsl:with-param name="previousButtonFlag">
				<xsl:value-of select="blBackStudyButton"/>
			</xsl:with-param>
			<xsl:with-param name="previousButtonUrl">
				<xsl:value-of select="$studyURL"/>?current=study_results&amp;uP_sparam=activeTab&amp;activeTab=<xsl:value-of select="$studyTab"/>&amp;uP_root=root&amp;intStudyID=<xsl:value-of select="$intStudyID"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="infopanel_content">
		<!-- Set Variables for Results Data -->
		<xsl:variable name="intCurrentPage">
			<xsl:value-of select="currentPage"/>
		</xsl:variable>
		<xsl:variable name="intNoOfPages">
			<xsl:value-of select="noOfPages"/>
		</xsl:variable>
		<xsl:variable name="intRecordPerPage">
			<xsl:value-of select="noOfRecords"/>
		</xsl:variable>
		<script language="javascript">
			function submitNewPage( offset )
			{
				document.searchForm.currentPage.value = parseInt( document.searchForm.currentPage.value ) + parseInt(offset);
				document.searchForm.submit();
			}
		</script>
		<form name="searchForm" action="{$baseActionURL}?action=search_patient&amp;submit=true" method="post">
			<table cellpadding="0" cellspacing="0" border="0" width="100%">
				<!-- Column Heading Row -->
				<tr>
                                        <!-- PATIENT_strPatientIDDisplay -->
					<td class="stripped_column_heading" width="2"><img width="2" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
					<td class="stripped_column_heading">
						<xsl:variable name="idOrder">
							<xsl:value-of select="PATIENT_strPatientID_Order"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$idOrder = 'ASC'">
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=PATIENT_strPatientID&amp;order=DESC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="PATIENT_strPatientIDDisplay"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=PATIENT_strPatientID&amp;order=ASC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="PATIENT_strPatientIDDisplay"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td>
                                        <!-- PATIENT_strSurnameDisplay -->
					<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
					<td width="15%" class="stripped_column_heading">
						<xsl:variable name="surnameOrder">
							<xsl:value-of select="PATIENT_strSurname_Order"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$surnameOrder = 'ASC'">
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=PATIENT_strSurname&amp;order=DESC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="PATIENT_strSurnameDisplay"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=PATIENT_strSurname&amp;order=ASC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="PATIENT_strSurnameDisplay"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td>
                                        <!-- PATIENT_strFirstNameDisplay -->
					<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
					<td class="stripped_column_heading">
						<xsl:variable name="firstnameOrder">
							<xsl:value-of select="PATIENT_strFirstName_Order"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$firstnameOrder = 'ASC'">
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=PATIENT_strFirstName&amp;order=DESC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="PATIENT_strFirstNameDisplay"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=PATIENT_strFirstName&amp;order=ASC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="PATIENT_strFirstNameDisplay"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td>
                                        <!-- PATIENT_dtDobDisplay -->
					<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
					<td class="stripped_column_heading">
						<xsl:variable name="dobOrder">
							<xsl:value-of select="PATIENT_dtDob_Order"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$dobOrder = 'ASC'">
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=PATIENT_dtDob&amp;order=DESC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="PATIENT_dtDobDisplay"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=PATIENT_dtDob&amp;order=ASC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="PATIENT_dtDobDisplay"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td>
                                        <!-- PATIENT_strSpeciesDisplay -->
					<!-- td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
					<td class="stripped_column_heading" width="5%">
						<xsl:variable name="speciesOrder">
							<xsl:value-of select="PATIENT_strSpecies_Order"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$speciesOrder = 'ASC'">
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=PATIENT_strSpecies&amp;order=DESC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="PATIENT_strSpeciesDisplay"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=PATIENT_strSpecies&amp;order=ASC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="PATIENT_strSpeciesDisplay"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td  -->
                                        <!-- Patient Biospecimens -->
					<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
					<td class="stripped_column_heading">
						Sample
					</td>
                                        <!-- Patient Consent -->
					<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
					<td class="stripped_column_heading">
						Consent
					</td>
                                        <!-- ADMISSIONS_strDiagCategoryDisplay -->
					<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
					<td class="stripped_column_heading" width="5%">
						<xsl:variable name="diagnosisOrder"><xsl:value-of select="ADMISSIONS_strDiagCategory_Order"/></xsl:variable>
						<xsl:choose>
							<xsl:when test="$diagnosisOrder = 'ASC'">
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=ADMISSIONS_strDiagCategory&amp;order=DESC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="ADMISSIONS_strDiagCategoryDisplay"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=ADMISSIONS_strDiagCategory&amp;order=ASC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="ADMISSIONS_strDiagCategoryDisplay"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td>
                                        <!-- ADMISSIONS_strStageDisplay -->
					<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
					<td class="stripped_column_heading" width="5%">
						<xsl:variable name="stageOrder"><xsl:value-of select="ADMISSIONS_strStage_Order"/></xsl:variable>
						<xsl:choose>
							<xsl:when test="$stageOrder = 'ASC'">
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=ADMISSIONS_strStage&amp;order=DESC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="ADMISSIONS_strStageDisplay"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=ADMISSIONS_strStage&amp;order=ASC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="ADMISSIONS_strStageDisplay"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td>
                                        <!-- ADMISSIONS_dtDiagnosisDisplay -->
					<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
					<td class="stripped_column_heading" width="5%">
						<xsl:variable name="diagDtOrder"><xsl:value-of select="ADMISSIONS_dtDiagnosis_Order"/></xsl:variable>
						<xsl:choose>
							<xsl:when test="$diagDtOrder = 'ASC'">
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=ADMISSIONS_dtDiagnosis&amp;order=DESC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="ADMISSIONS_dtDiagnosisDisplay"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=ADMISSIONS_dtDiagnosis&amp;order=ASC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="ADMISSIONS_dtDiagnosisDisplay"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td>
                                        <!-- ADMISSIONS_strCommentsDisplay -->
					<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
					<td class="stripped_column_heading" width="5%">
						<xsl:variable name="adm_strCommentsOrder"><xsl:value-of select="ADMISSIONS_strComments_Order"/></xsl:variable>
						<xsl:choose>
							<xsl:when test="$adm_strCommentsOrder = 'ASC'">
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=ADMISSIONS_strComments&amp;order=DESC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="ADMISSIONS_strCommentsDisplay"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a class="stripped_column_heading" href="{$baseActionURL}?action=search_patient&amp;submit=true&amp;orderBy=ADMISSIONS_strComments&amp;order=ASC&amp;noOfRecords={$intRecordPerPage}&amp;currentPage={$intCurrentPage}">
									<xsl:value-of select="ADMISSIONS_strCommentsDisplay"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<td class="stripped_column_heading" width="2"><img width="2" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
				</tr>
				<!-- <div style="width : 100%; height : auto; overflow : auto;"> -->
					<!-- Iterate through Each Patient Match -->
					<xsl:for-each select="patient">
						<xsl:variable name="varPatientInternalID">
							<xsl:value-of select="PATIENT_intInternalPatientID"/>
						</xsl:variable>
						<tr>
							<td width="2">
								<!-- Assign Cell Class Based on Whether Row is Odd or Even -->
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<xsl:attribute name="class">stripped_light</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">stripped_dark</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<img width="2" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/>
							</td>
                                                        <!-- PATIENT_strPatientID -->
							<td>
								<!-- Assign Cell Class Based on Whether Row is Odd or Even -->
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<xsl:attribute name="class">stripped_light</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">stripped_dark</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<a class="stripped_content" href="{$baseActionURL}?action=view_patient&amp;PATIENT_intInternalPatientID={$varPatientInternalID}">
									<xsl:value-of select="PATIENT_strPatientID"/>
								</a>
							</td>
                                                        <!-- PATIENT_strSurname -->
							<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
							<td>
								<!-- Assign Cell Class Based on Whether Row is Odd or Even -->
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<xsl:attribute name="class">stripped_light</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">stripped_dark</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
									<a class="stripped_content" href="{$baseActionURL}?action=view_patient&amp;PATIENT_intInternalPatientID={$varPatientInternalID}">
										<xsl:value-of select="PATIENT_strSurname"/>
									</a>
							</td>
                                                        <!-- PATIENT_strFirstName -->
							<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
							<td>
								<!-- Assign Cell Class Based on Whether Row is Odd or Even -->
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<xsl:attribute name="class">stripped_light</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">stripped_dark</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<a class="stripped_content" href="{$baseActionURL}?action=view_patient&amp;PATIENT_intInternalPatientID={$varPatientInternalID}">
									<xsl:value-of select="PATIENT_strFirstName"/>
								</a>
							</td>
                                                        <!-- PATIENT_dtDob -->
							<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
							<td>
								<!-- Assign Cell Class Based on Whether Row is Odd or Even -->
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<xsl:attribute name="class">stripped_light</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">stripped_dark</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<a class="stripped_content" href="{$baseActionURL}?action=view_patient&amp;PATIENT_intInternalPatientID={$varPatientInternalID}">
									<xsl:value-of select="PATIENT_dtDob"/>
								</a>
							</td>
                                                        <!-- PATIENT_strSpecies -->
							<!-- td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
							<td>
								<!- Assign Cell Class Based on Whether Row is Odd or Even ->
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<xsl:attribute name="class">stripped_light</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">stripped_dark</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<a class="stripped_content" href="{$baseActionURL}?action=view_patient&amp;PATIENT_intInternalPatientID={$varPatientInternalID}">
									<xsl:value-of select="PATIENT_strSpecies"/>
								</a>
							</td -->
                                                        <!-- Patient Biospeciemns -->
							<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
							<td>
								<!-- Assign Cell Class Based on Whether Row is Odd or Even -->
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<xsl:attribute name="class">stripped_light</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">stripped_dark</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<a class="stripped_content" href="{$biospecimenURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTab}&amp;intInternalPatientID={$varPatientInternalID}&amp;current=biospecimen_search&amp;submit=intInternalPatientID&amp;currentPage=patient_results&amp;searchResultPage={$intCurrentPage}&amp;module=biospecimen_search&amp;action=patient_entry">view</a>
							</td>
                                                        <!-- Patient Consent -->
							<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
							<td>
								<!-- Assign Cell Class Based on Whether Row is Odd or Even -->
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<xsl:attribute name="class">stripped_light</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">stripped_dark</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<a class="stripped_content" href="{$baseActionURL}?action=consent&amp;PATIENT_intInternalPatientID={$varPatientInternalID}">view</a>
							</td>
                                                        <!-- ADMISSIONS_strDiagCategory -->
							<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
							<td>
								<!-- Assign Cell Class Based on Whether Row is Odd or Even -->
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<xsl:attribute name="class">stripped_light</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">stripped_dark</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<a class="stripped_content" href="{$baseActionURL}?action=view_patient&amp;PATIENT_intInternalPatientID={$varPatientInternalID}">
									<xsl:value-of select="ADMISSIONS_strDiagCategory"/>
								</a>
							</td>
                                                        <!-- ADMISSIONS_strStage -->
							<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
							<td>
								<!-- Assign Cell Class Based on Whether Row is Odd or Even -->
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<xsl:attribute name="class">stripped_light</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">stripped_dark</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<a class="stripped_content" href="{$baseActionURL}?action=view_patient&amp;PATIENT_intInternalPatientID={$varPatientInternalID}">
									<xsl:value-of select="ADMISSIONS_strStage"/>
								</a>
							</td>
                                                        <!-- ADMISSIONS_dtDiagnosis -->
							<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
							<td>
								<!-- Assign Cell Class Based on Whether Row is Odd or Even -->
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<xsl:attribute name="class">stripped_light</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">stripped_dark</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<a class="stripped_content" href="{$baseActionURL}?action=view_patient&amp;PATIENT_intInternalPatientID={$varPatientInternalID}">
									<xsl:value-of select="ADMISSIONS_dtDiagnosis"/>
								</a>
							</td>
                                                        <!-- ADMISSIONS_strComments -->
							<td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
							<td>
								<!-- Assign Cell Class Based on Whether Row is Odd or Even -->
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<xsl:attribute name="class">stripped_light</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">stripped_dark</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<a class="stripped_content" href="{$baseActionURL}?action=view_patient&amp;PATIENT_intInternalPatientID={$varPatientInternalID}">
									<xsl:value-of select="ADMISSIONS_strComments"/>
								</a>
							</td>
							<td class="stripped_dark" width="2">
								<!-- Assign Cell Class Based on Whether Row is Odd or Even -->
								<xsl:choose>
									<xsl:when test="position() mod 2 != 0">
										<xsl:attribute name="class">stripped_light</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">stripped_dark</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<img width="2" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/>
							</td>
						</tr>
					</xsl:for-each>
				<!-- </div> -->
			</table>
			<!-- Search Navigation Buttons (Not Reskinned) -->
			<table width="100%">
				<tr>
					<td>
						<table width="100%">
							<tr>
								<td width="5%" class="uportal-label">Page: </td>
								<td width="3%" class="uportal-label">
									<input type="submit" name="previous" value="{$ltBtnLabel}" tabindex="41" class="uportal-button" onclick="javascript:submitNewPage('-1')"/>
								</td>
								<td width="5%" class="uportal-label">
									<input type="text" name="currentPage" size="4" tabindex="42" value="{$intCurrentPage}" align="right" class="uportal-input-text"/>
								</td>
								<td width="6%" class="uportal-label">of <xsl:value-of select="noOfPages"/>
								</td>
								<td width="3%" class="uportal-label">
									<input type="button" name="next" value="{$gtBtnLabel}" tabindex="43" class="uportal-button" onclick="javascript:submitNewPage('1')"/>
								</td>
								<td width="4%" class="uportal-label">
									<input type="submit" name="go" value="{$goBtnLabel}" tabindex="44" class="uportal-button"/>
								</td>
								<td width="5%"/>
								<td width="7%" class="uportal-label">Display</td>
								<td width="5%" class="uportal-label">
									<input type="text" name="noOfRecords" size="4" tabindex="45" value="{$intRecordPerPage}" align="right" class="uportal-input-text"/>
								</td>
								<td width="15%" class="uportal-label">records at a time</td>
								<td width="3%" class="uportal-label">
									<input type="submit" name="set" value="{$setBtnLabel}" tabindex="46" class="uportal-button" onblur="javascript:document.searchForm.previous.focus()"/>
								</td>
								<td width="10%"/>
								<td width="13%"/>
							</tr>
						</table>

                                                <xsl:if test="boolean(noOfResults)">
                                                    <table>
                                                            <tr>
                                                                    <td width="30%" class="uportal-label">Total Number of Results: </td>
                                                                    <td width="10%" class="uportal-label" align="left">
                                                                            <xsl:value-of select="noOfResults"/>
                                                                    </td>
                                                                    <td width="60%"/>
                                                            </tr>
                                                    </table>
                                                </xsl:if>
					</td>
				</tr>
			</table>
		</form>
	</xsl:template>
</xsl:stylesheet>
