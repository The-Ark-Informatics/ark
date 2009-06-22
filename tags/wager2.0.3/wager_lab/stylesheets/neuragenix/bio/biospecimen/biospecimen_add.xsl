<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./biospecimen_menu.xsl"/>
	<xsl:include href="./infopanel.xsl"/>
	<xsl:output method="html" indent="no"/>
	<xsl:param name="inventoryChannelURL">inventoryChannelURL_false</xsl:param>
	<xsl:param name="patientChannelTabOrder">patientChannelTabOrder</xsl:param>
	<xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>
	<xsl:variable name="funcpanelImagePath">media/neuragenix/funcpanel</xsl:variable>
	<xsl:variable name="spacerImagePath">media/neuragenix/infopanel/spacer.gif</xsl:variable>
	<xsl:param name="biospecimenChannelTabOrder">biospecimenChannelTabOrder</xsl:param>
	<xsl:param name="strErrorMessage">strErrorMessage</xsl:param>
	<xsl:template match="biospecimen">
		<!-- Get the parameters from the channel class -->
		<xsl:param name="strBiospecimenID">
			<xsl:value-of select="BIOSPECIMEN_strBiospecimenID"/>
		</xsl:param>
		<xsl:param name="strBiospecSampleType">
			<xsl:value-of select="BIOSPECIMEN_strSampleType"/>
		</xsl:param>
		<xsl:param name="strBiospecLocation">
			<xsl:value-of select="BIOSPECIMEN_strBiospecLocation"/>
		</xsl:param>
		<xsl:param name="dtBiospecSampleDate">
			<xsl:value-of select="BIOSPECIMEN_dtSampleDate"/>
		</xsl:param>
		<xsl:param name="dtBiospecDateExtracted">
			<xsl:value-of select="BIOSPECIMEN_dtExtractedDate"/>
		</xsl:param>
		<xsl:param name="strBiospecOtherID">
			<xsl:value-of select="BIOSPECIMEN_strOtherID"/>
		</xsl:param>
		<xsl:param name="intBiospecStudyID">
			<xsl:value-of select="BIOSPECIMEN_intStudyKey"/>
		</xsl:param>
		<xsl:param name="intInternalPatientID">
			<xsl:value-of select="PATIENT_intInternalPatientID"/>
		</xsl:param>
		<xsl:param name="intBiospecParentID">
			<xsl:value-of select="BIOSPECIMEN_intParentID"/>
		</xsl:param>
		<xsl:param name="strBiospecParentID">
			<xsl:value-of select="BIOSPECIMEN_strParentID"/>
		</xsl:param>
		<xsl:param name="intDepth">
			<xsl:value-of select="BIOSPECIMEN_intDepth"/>
		</xsl:param>
		<xsl:param name="flag">
			<xsl:value-of select="flag"/>
		</xsl:param>
		<xsl:param name="flQuantity">
			<xsl:value-of select="BIOSPECIMEN_flQuantity"/>
		</xsl:param>
		<xsl:param name="strEncounter">
			<xsl:value-of select="BIOSPECIMEN_strEncounter"/>
		</xsl:param>
		<xsl:param name="flDNAConc">
			<xsl:value-of select="BIOSPECIMEN_flDNAConc"/>
		</xsl:param>

		<xsl:param name="strStoredIn">
			<xsl:value-of select="BIOSPECIMEN_strStoredIn"/>
		</xsl:param>
		<xsl:param name="BIOSPECIMEN_tmBiospecSampleTime">
			<xsl:value-of select="BIOSPECIMEN_tmSampleTime"/>
		</xsl:param>
		<xsl:param name="tmBiospecSampleTime">
			<xsl:value-of select="BIOSPECIMEN_tmSampleTime"/>
		</xsl:param>
		<xsl:param name="tmBiospecExtractedTime">
			<xsl:value-of select="BIOSPECIMEN_tmExtractedTime"/>
		</xsl:param>
		<xsl:param name="parent_strSampleType">
			<xsl:value-of select="parent_strSampleType"/>
		</xsl:param>
		<link href="htmlarea/lert.css" rel="stylesheet" type="text/css"/>

		<script src="htmlarea/lert.js" type="text/javascript"/>
		<script src="htmlarea/add_biospecimen.js" type="text/javascript"/>
		
		<table  width="100%" cellpadding="0" cellspacing="0" border="0"   >
			<tr><td colspan="3">
				<xsl:call-template name="infopaneltop">
					<xsl:with-param name="titleString">ADD BIOSPECIMEN</xsl:with-param>
					
				</xsl:call-template>
			</td>
			</tr> 
			
			
			
			<tr class="funcpanel_content">
				<td class="funcpanel_left_border" width="1px">&#160;</td>
				<td>
		<table width="100%">
			
			<tr align="right">
				<td>
					<form name="back_form" action="{$baseActionURL}" method="POST">

						<!-- Future,  back to patient page 
                  <input type="hidden" name="intInternalPatientID"
                     value="{$intInternalPatientID}"/>
                  -->


						<!-- back to the parent biospecimen -->
						<xsl:choose>
							<xsl:when test="$intBiospecParentID != -1">
								<input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
									value="{$intBiospecParentID}"/>
								<input type="hidden" name="action" value="view_biospecimen"/>
								<input type="hidden" name="module" value="core"/>
							</xsl:when>
							<xsl:otherwise>

								<input type="hidden" name="uP_sparam" value="activeTab"/>
								<input type="hidden" name="activeTab"
									value="{$biospecimenChannelTabOrder}"/>
								<input type="hidden" name="uP_root" value="root"/>
								<input type="hidden" name="module" value="biospecimen_search"/>
								<input type="hidden" name="intInternalPatientID"
									value="{$intInternalPatientID}"/>
								<!-- <input type="hidden" name="submit" value="intInternalPatientID"></input> -->
								<input type="hidden" name="action" value="patient_entry"/>

							</xsl:otherwise>
						</xsl:choose>


						<img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
							alt="Previous" onclick="javascript:document.back_form.submit();"/>
						<img border="0" src="media/neuragenix/buttons/next_disabled.gif" alt="Next"/>

					</form>
				</td>
			</tr>

			
		</table>
		<form name="biospecimen_form" action="{$baseActionURL}?current=biospecimen_add"
			method="post">
			<input type="hidden" name="BIOSPECIMEN_intPatientID" value="{$intInternalPatientID}"/>
			<input type="hidden" name="PATIENT_intInternalPatientID" value="{$intInternalPatientID}"/>
			<input type="hidden" name="intInvCellID" value="-1"/>
			<input type="hidden" name="page1completed" value="true"/>
			<input type="hidden" name="module" value="core"/>
			<input type="hidden" name="action" value="save_biospecimen"/>

			<table width="100%">
				<tr>
					<td class="neuragenix-form-required-text" width="50%">
						<xsl:value-of select="strErrorDuplicateKey"/>
						<xsl:value-of select="strErrorRequiredFields"/>
						<xsl:value-of select="strErrorInvalidDataFields"/>
						<xsl:value-of select="strErrorInvalidData"/>
						<xsl:value-of select="$strErrorMessage"/>
					</td>
					<td class="neuragenix-form-required-text" width="50%"
						id="neuragenix-required-header" align="right"> * = Required fields </td>
				</tr>
			</table>
			<!-- Order of tabs: left column starts from 21 upwards. The right column starts from 31-->
			<table>
				<!-- Row 1 -->

				<input type="hidden" name="BIOSPECIMEN_strParentID" value="{$strBiospecParentID}"/>
				<input type="hidden" name="BIOSPECIMEN_intParentID" value="{$intBiospecParentID}"/>
				<!-- Row 2 -->

				<xsl:if test="$intBiospecParentID != -1">
					<tr>
						<td id="neuragenix-form-row-input-label-required"
							class="neuragenix-form-required-text"/>
						<td id="neuragenix-form-row-input-label" class="uportal-label"> Type of
							Biospecimen: </td>
						<td id="neuragenix-form-row-input-input" class="uportal-label">
							<select name="BIOSPECIMEN_strProcessingType" tabindex="22"
								class="uportal-input-text">
								<xsl:for-each select="BIOSPECIMEN_strProcessingType/type">
									<option>
										<xsl:attribute name="value">
											<xsl:value-of select="."/>
										</xsl:attribute>
										<xsl:value-of select="."/>
									</option>
								</xsl:for-each>
							</select>
						</td>

					</tr>

					<tr>

						<td/>
						<td id="neuragenix-form-row-input-label" class="uportal-label"> Available
							Stock Quantity: </td>
						<td class="uportal-label" id="neuragenix-form-row-input-input">

							<span class="uportal-label">
								<xsl:value-of select="BIOSPECIMEN_flNumberCollected"/>
							</span>
						</td>

						<td width="5%"/>
						<td class="neuragenix-form-required-text"/>
						<xsl:if test="parent_strSampleType='DNA'">
							<td class="uportal-label">Stock Concentration:</td>

							<td class="uportal-label">
								<xsl:value-of select="parent_flDNAConc"/>
							</td>
							<xsl:variable name="parent_flDNAConc">
								<xsl:value-of select="parent_flDNAConc"/>
							</xsl:variable>
							

							<input type="hidden" name="parentDNA" value="{$parent_flDNAConc}"/>
							
						</xsl:if>
						<xsl:variable name="parent_flQuantity">
							<xsl:value-of select="parent_flQuantity"/>
						</xsl:variable>
						<input type="hidden" name="parentQuantity" value="{$parent_flQuantity}"/>
					</tr>
				</xsl:if>
			</table>
			<hr/>
			<table width="100%">
				<tr>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text">*</td>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_strBiospecimenIDDisplay"/>: </td>
					<td class="uportal-label" id="neuragenix-form-row-input-input">
						<xsl:choose>
							<xsl:when test="BiospecimenIDAutoGeneration='true'">
								<span class="uportal-label">System Generated</span>
								<xsl:variable name="tempValue">
									<xsl:value-of select="BiospecimenIDAutoGeneration/@tempValue"/>
								</xsl:variable>
								<input type="hidden" name="BIOSPECIMEN_strBiospecimenID"
									value="{$tempValue}"/>
							</xsl:when>
							<xsl:otherwise>
								<input type="text" name="BIOSPECIMEN_strBiospecimenID" size="20"
									tabindex="21" value="{$strBiospecimenID}"
									class="uportal-input-text"/>
							</xsl:otherwise>
						</xsl:choose>

					</td>

					<td width="5%"/>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<!--<xsl:value-of select="BIOSPECIMEN_strOtherIDDisplay"/>: --></td>
					<td id="neuragenix-form-row-input-input" class="uportal-label">
						<!--<input type="text" name="BIOSPECIMEN_strOtherID" size="20" tabindex="31"
							value="{$strBiospecOtherID}" class="uportal-input-text"/>-->
					</td>
					<td id="neuragenix-end-spacer"/>
				</tr>
				<!-- Row 3 -->
				<tr>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_strSampleTypeDisplay"/>: </td>
					<script language="javascript">
                            function dropDownUpdate() {
                                document.biospecimen_form.action.value = 'add_biospecimen';
                                document.biospecimen_form.submit();
                                }</script>
					
						
					<td id="neuragenix-form-row-input-input" class="uportal-label">
						<select  name="BIOSPECIMEN_strSampleType" tabindex="22"	class="uportal-input-text">							
								<xsl:if test="$intBiospecParentID = -1">		
									<xsl:attribute name="multiple"></xsl:attribute>
									<xsl:attribute name="size">4</xsl:attribute>
								</xsl:if>
							<xsl:for-each select="BIOSPECIMEN_strSampleType">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if test="@selected=1">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select> <xsl:if test="$intBiospecParentID = -1">
							X <input type="text" length="4" size="4"
						name="BIOSPECIMEN_intMultiple" value="1"/>
						</xsl:if>
					</td>

					<!--<xsl:value-of select="BIOSPECIMEN_strSpeciesDisplay"/>: -->
					<input type="hidden" name="BIOSPECIMEN_strSpecies" value="Human"/>
					<!--<select name="BIOSPECIMEN_strSpecies" tabindex="23"
							class="uportal-input-text">
							<xsl:for-each select="BIOSPECIMEN_strSpecies">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if test="@selected=1">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>-->
					<td width="5%"/>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_dtSampleDateDisplay"/>
					</td>
					<td width="25%" class="uportal-label">
						<select name="BIOSPECIMEN_dtSampleDate_Day" tabindex="32"
							class="uportal-input-text">
							<xsl:for-each select="BIOSPECIMEN_dtSampleDate_Day">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if test="@selected=1">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>
						<select name="BIOSPECIMEN_dtSampleDate_Month" tabindex="33"
							class="uportal-input-text">
							<xsl:for-each select="BIOSPECIMEN_dtSampleDate_Month">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if test="@selected=1">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>
						<input type="text" name="BIOSPECIMEN_dtSampleDate_Year" size="5"
							tabindex="34" class="uportal-input-text">
							<xsl:attribute name="value">
								<xsl:value-of select="BIOSPECIMEN_dtSampleDate_Year"/>
							</xsl:attribute>
						</input>
					</td>
					<!--td id="neuragenix-form-row-input-input" class="uportal-label">
				<input type="text" name="dtBiospecSampleDate" size="20" tabindex="3" value="{$dtBiospecSampleDate}" class="uportal-input-text" />
			</td-->
					<td id="neuragenix-end-spacer"/>
				</tr>

				<tr>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_intStudyKeyDisplay"/>: </td>
					<td id="neuragenix-form-row-input" class="uportal-label">
						<select class="uportal-input-text" name="BIOSPECIMEN_intStudyKey"
							tabindex="25">
							<xsl:variable name="intBiospecStudyKey">
								<xsl:value-of select="BIOSPECIMEN_intStudyKey"/>
							</xsl:variable>
							<xsl:for-each select="study_list">
								<xsl:variable name="varIntStudyID">
									<xsl:value-of select="STUDY_intStudyID"/>
								</xsl:variable>
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="STUDY_intStudyID"/>
									</xsl:attribute>
									<xsl:if test="$intBiospecStudyKey = $varIntStudyID">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="STUDY_strStudyName"/>
								</option>
							</xsl:for-each>
						</select>
					</td>

					<td width="5%"/>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>

					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_tmSampleTimeDisplay"/>
					</td>
					<td width="25%" class="uportal-label">
						<select name="BIOSPECIMEN_tmSampleTime_Hour" tabindex="35"
							class="uportal-input-text">
							<xsl:for-each select="BIOSPECIMEN_tmSampleTime_Hour">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if
										test="string-length( BIOSPECIMEN_tmSampleTime_Hour ) > '0'">
										<xsl:if test="@selected=1">
											<xsl:attribute name="selected">true</xsl:attribute>
										</xsl:if>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>
						<select name="BIOSPECIMEN_tmSampleTime_Minute" tabindex="36"
							class="uportal-input-text">
							<xsl:for-each select="BIOSPECIMEN_tmSampleTime_Minute">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if
										test="string-length( BIOSPECIMEN_tmSampleTime_Minute ) > '0'">
										<xsl:if test="@selected=1">
											<xsl:attribute name="selected">true</xsl:attribute>
										</xsl:if>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>
						<select name="BIOSPECIMEN_tmSampleTime_AMPM" tabindex="37"
							class="uportal-input-text">
							<xsl:for-each select="BIOSPECIMEN_tmSampleTime_AMPM">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if
										test="string-length( BIOSPECIMEN_tmSampleTime_AMPM ) > '0'">
										<xsl:if test="@selected=1">
											<xsl:attribute name="selected">true</xsl:attribute>
										</xsl:if>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>


					</td>

				</tr>

				<!-- Row 4 -->
				<tr>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text">*</td>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_strEncounterDisplay"/>: </td>
					<td id="neuragenix-form-row-input" class="uportal-label">
						<select name="BIOSPECIMEN_strEncounter" tabindex="27"
							class="uportal-input-text">
							<xsl:for-each select="search_encounter_list">
								<xsl:variable name="varEncounter">
									<xsl:value-of select="ADMISSIONS_strAdmissionID"/>
								</xsl:variable>
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="ADMISSIONS_strAdmissionID"/>
									</xsl:attribute>
									<xsl:if test="$strEncounter=$varEncounter">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="ADMISSIONS_strAdmissionID"/>
								</option>
							</xsl:for-each>
						</select>
					</td>

					<td width="5%"/>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_dtExtractedDateDisplay"/>
					</td>
					<td width="25%" class="uportal-label">
						<select name="BIOSPECIMEN_dtExtractedDate_Day" tabindex="38"
							class="uportal-input-text">
							<xsl:for-each select="BIOSPECIMEN_dtExtractedDate_Day">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if test="@selected=1">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>
						<select name="BIOSPECIMEN_dtExtractedDate_Month" tabindex="39"
							class="uportal-input-text">
							<xsl:for-each select="BIOSPECIMEN_dtExtractedDate_Month">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if test="@selected=1">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>
						<input type="text" name="BIOSPECIMEN_dtExtractedDate_Year" size="5"
							tabindex="40" class="uportal-input-text">
							<xsl:attribute name="value">
								<xsl:value-of select="BIOSPECIMEN_dtExtractedDate_Year"/>
							</xsl:attribute>
						</input>
					</td>
					<td id="neuragenix-end-spacer"/>
				</tr>

				<!-- last row-->
				<tr>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label2">
						<xsl:value-of select="BIOSPECIMEN_flDNAConcDisplay"/>
					</td>
					<td id="neuragenix-form-row-input" class="uportal-label2">
						<input type="text" name="BIOSPECIMEN_flDNAConc" value="{$flDNAConc}"/>
					</td>


					<td width="5%" valign="top"/>

					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text" valign="top"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_tmExtractedTimeDisplay"/>
					</td>
					<td width="25%" class="uportal-label">
						<select name="BIOSPECIMEN_tmExtractedTime_Hour" tabindex="41"
							class="uportal-input-text">
							<xsl:for-each select="BIOSPECIMEN_tmExtractedTime_Hour">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if
										test="string-length( BIOSPECIMEN_tmExtractedTime_Hour ) > '0'">
										<xsl:if test="@selected=1">
											<xsl:attribute name="selected">true</xsl:attribute>
										</xsl:if>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>
						<select name="BIOSPECIMEN_tmExtractedTime_Minute" tabindex="42"
							class="uportal-input-text">
							<xsl:for-each select="BIOSPECIMEN_tmExtractedTime_Minute">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if
										test="string-length( BIOSPECIMEN_tmExtractedTime_Minute ) > '0'">
										<xsl:if test="@selected=1">
											<xsl:attribute name="selected">true</xsl:attribute>
										</xsl:if>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>
						<select name="BIOSPECIMEN_tmExtractedTime_AMPM" tabindex="43"
							class="uportal-input-text">
							<xsl:for-each select="BIOSPECIMEN_tmExtractedTime_AMPM">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if
										test="string-length( BIOSPECIMEN_tmExtractedTime_AMPM ) > '0'">
										<xsl:if test="@selected=1">
											<xsl:attribute name="selected">true</xsl:attribute>
										</xsl:if>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>


					</td>

				</tr>

				<tr>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label"> Quantity : </td>

					<td id="neuragenix-form-row-input" class="uportal-label">
						<input type="text" name="BIOSPECIMEN_flQuantity" value="{$flQuantity}"
							size="5"/>
						<select name="BIOSPECIMEN_strUnit" class="uportal-input-text">
							<xsl:for-each select="BIOSPECIMEN_strUnit">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if test="string-length( BIOSPECIMEN_strUnit ) > '0'">
										<xsl:if test="@selected=1">
											<xsl:attribute name="selected">true</xsl:attribute>
										</xsl:if>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
						</select>
					</td>
					<td width="5%"/>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text" valign="top"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label"> Flagged: </td>
					<td width="25%" valign="top">
						<xsl:choose>
							<xsl:when test="string($flag)">
								<input type="hidden" name="wasFlagged" value="{$flag}"/>
							</xsl:when>
							<xsl:otherwise>
								<input type="hidden" name="wasFlagged" value="false"/>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test="contains($flag,'true')">
								<input type="checkbox" name="isFlagged" tabindex="44" value="true"
									checked="checked" class="uportal-text"/>
							</xsl:when>
							<xsl:otherwise>
								<input type="checkbox" name="isFlagged" value="true" tabindex="44"
									class="uportal-text"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<td id="neuragenix-end-spacer" valign="top"/>
				</tr>
				<tr>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text" valign="top"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label" valign="top"/>
					<td id="neuragenix-form-row-input" class="uportal-label" valign="top"/>
					<td width="5%" valign="top"/>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text" valign="top"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label" valign="top">
						<xsl:value-of select="BIOSPECIMEN_strCommentsDisplay"/>: </td>
					<td width="25%" valign="top">
						<textarea name="BIOSPECIMEN_strComments" rows="4" cols="40" tabindex="45"
							class="uportal-input-text">
							<xsl:value-of select="BIOSPECIMEN_strComments"/>
						</textarea>
					</td>
					<td id="neuragenix-end-spacer" valign="top"/>
				</tr>
				<!-- subtype table go here -->
				<xsl:if test="count(BIOSPECIMEN_strSampleSubType) &gt; 0">
					<tr>
						<td width="1%" id="neuragenix-form-row-input-label-required"
							class="neuragenix-form-required-text" valign="top"/>
						<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label"
							valign="top">Sub type: </td>
						<td width="26%" id="neuragenix-form-row-input" class="uportal-label"
							valign="top">
							<!-- select box for subtype -->
							<select name="BIOSPECIMEN_strSampleSubType" tabindex="28"
								class="uportal-input-text">
								<xsl:for-each select="BIOSPECIMEN_strSampleSubType">
									<option>
										<xsl:attribute name="value">
											<xsl:value-of select="."/>
										</xsl:attribute>
										<xsl:if test="@selected='1'">
											<xsl:attribute name="selected">true</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="."/>
									</option>
								</xsl:for-each>
							</select>
						</td>
						<td width="5%" valign="top"/>
						<td width="1%" id="neuragenix-form-row-input-label-required"
							class="neuragenix-form-required-text" valign="top"/>
						<td width="18%" id="neuragenix-form-row-input-label" class="uportal-label"
							valign="top">
							<xsl:value-of select="BIOSPECIMEN_strSubTypeDescriptionDisplay"/>: </td>
						<td width="25%" valign="top">
							<textarea name="BIOSPECIMEN_strSubTypeDescription" rows="4" cols="40"
								tabindex="44" class="uportal-input-text">
								<xsl:value-of select="BIOSPECIMEN_strSubTypeDescription"/>
							</textarea>
						</td>
						<td width="5%" id="neuragenix-end-spacer" valign="top"/>
					</tr>
				</xsl:if>
				<!-- subtype table end here -->
			</table>
			<input type="hidden" value="1" name="BIOSPECIMEN_strWADB"/>
			<table width="100%">
				<tr>
					<td width="1%"/>
					<td width="21%" class="uportal-label"/>
					<td width="78%" class="uportal-label">
						<xsl:choose>

							<xsl:when test="$intBiospecParentID != -1">
								<input type="button" class="uportal-button"
									onclick="aliquotdialog();" value="Save"/>
							</xsl:when>
							<xsl:otherwise>
								<input type="submit" name="save" value="Save" class="uportal-button"
									tabindex="50"/>

							</xsl:otherwise>
						</xsl:choose>
						<!-- XXX: agus - unlock sub biospecimen type -->
						<input type="button" name="clear" value="Clear" tabindex="51"
							class="uportal-button"
							onclick="javascript:confirmClear('{$baseActionURL}?current=biospecimen_add&amp;intInternalPatientID={$intInternalPatientID}&amp;BIOSPECIMEN_intParentID={$intBiospecParentID}&amp;BIOSPECIMEN_strParentID={$strBiospecParentID}&amp;module=core&amp;action=add_biospecimen')"
						/>
					</td>
				</tr>
			</table>
		</form>
				</td>	<td class="funcpanel_right_border" width="1px">&#160;</td>
			</tr>
			
			<tr><td colspan="3">
				
				<xsl:call-template name="infopanelbottom">
					
					
				</xsl:call-template>
			</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
