<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./biospecimen_menu.xsl"/>
	<xsl:output method="html" indent="no"/>
	<xsl:param name="inventoryChannelURL">inventoryChannelURL_false</xsl:param>
   	<xsl:param name="patientChannelTabOrder">patientChannelTabOrder</xsl:param>
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
		<xsl:param name="strEncounter">
			<xsl:value-of select="BIOSPECIMEN_strEncounter"/>
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
                
   	<script language="javascript"> function dropDownSubTypeUpdate() {
   		document.biospecimen_form.action.value =
   		'add_biospecimen'; 
   		document.biospecimen_form.keepSubType.value = 'true';
   		document.biospecimen_form.submit();
   		
   		} 
                
                function updateColor (selectTag) 
                {
                   selectTag.style.color = selectTag[selectTag.selectedIndex].style.color;
                }                   
                </script>             
                
      <table width="100%">
         <tr>
            <td class="uportal-channel-subtitle"> Add biospecimen<br/>
               <hr/>
            </td>
         </tr>
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
                     <input type="hidden" name="activeTab" value="3"/> 
                  	<input type="hidden" name="uP_root" value="root"/>
                  	<input type="hidden" name="module" value="biospecimen_search"/>
                  	<input type="hidden" name="intInternalPatientID" value="{$intInternalPatientID}"></input>
                  	<!-- <input type="hidden" name="submit" value="intInternalPatientID"></input> -->
                  	<input type="hidden" name="action" value="patient_entry"/>
                  	
                  </xsl:otherwise>
                  </xsl:choose>

                    	
               	<img border="0" src="media/neuragenix/buttons/previous_enabled.gif" alt="Previous" onclick="javascript:document.back_form.submit();"/>
               	<img border="0" src="media/neuragenix/buttons/next_disabled.gif" alt="Next"/>
               	
               </form>
            </td>
         </tr>

         <tr><td><hr/></td></tr>
      </table>
      <form name="biospecimen_form" action="{$baseActionURL}?current=biospecimen_add"
         method="post">
         <input type="hidden" name="keepSubType" value=""/>
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
         <table width="100%">
            <!-- Row 1 -->
            <tr>
               <td id="neuragenix-form-row-input-label-required"
                  class="neuragenix-form-required-text">*</td>
               <td id="neuragenix-form-row-input-label" class="uportal-label">
                  <xsl:value-of select="BIOSPECIMEN_strBiospecimenIDDisplay"/>: </td>
               <td class="uportal-label" id="neuragenix-form-row-input-input">
               
               <xsl:choose>
                  <xsl:when test="BiospecimenIDAutoGeneration='true'">
                    <span class="uportal-label">System Generated</span>
                    <xsl:variable name="tempValue"><xsl:value-of select="BiospecimenIDAutoGeneration/@tempValue" /></xsl:variable>
                    <input type="hidden" name="BIOSPECIMEN_strBiospecimenID" value="{$tempValue}" />
                  </xsl:when>
                  <xsl:otherwise>
                    <input type="text" name="BIOSPECIMEN_strBiospecimenID" size="20" tabindex="21" value="{$strBiospecimenID}" class="uportal-input-text"/>
                  </xsl:otherwise>      
               </xsl:choose>
               </td>
               <td width="5%"/>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_strParentIDDisplay"/>: </td>
					<td class="uportal-label" id="neuragenix-form-row-input-input">
						<xsl:value-of select="BIOSPECIMEN_strParentID"/>
						<input type="hidden" name="BIOSPECIMEN_strParentID"
							value="{$strBiospecParentID}"/>
						<input type="hidden" name="BIOSPECIMEN_intParentID"
							value="{$intBiospecParentID}"/>
					</td>
					<td id="neuragenix-end-spacer"/>
				</tr>
				<!-- Row 2 -->
				<tr>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
                                                    <xsl:value-of select="BIOSPECIMEN_strSampleTypeDisplay"/>: </td>
					<script language="javascript"> function dropDownUpdate() {
						document.biospecimen_form.action.value = 'add_biospecimen';
						document.biospecimen_form.submit(); } </script>
					<td id="neuragenix-form-row-input-input" class="uportal-label">
						<select name="BIOSPECIMEN_strSampleType" tabindex="22"
							class="uportal-input-text" onchange="javascript:dropDownUpdate()">
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
						</select>
					</td>
					<td width="5%"/>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_strOtherIDDisplay"/>: </td>
					<td id="neuragenix-form-row-input-input" class="uportal-label">
						<input type="text" name="BIOSPECIMEN_strOtherID" size="20" tabindex="31"
							value="{$strBiospecOtherID}" class="uportal-input-text"/>
					</td>
					<td id="neuragenix-end-spacer"/>
				</tr>
				<!-- Row 3 -->
				<tr>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_strSpeciesDisplay"/>: </td>
					<td id="neuragenix-form-row-input" class="uportal-label">
						<select name="BIOSPECIMEN_strSpecies" tabindex="23"
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
						</select>
					</td>
					<td width="5%"/>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_dtSampleDateDisplay"/>
					</td>
					<td width="25%" class="uportal-label">
                                        <xsl:choose>
                                        <xsl:when test="BIOSPECIMEN_dtSampleDate/@display_type='dropdown'">
                                            <select name="BIOSPECIMEN_dtSampleDate_Day" tabindex="32" class="uportal-input-text">
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
                                          </xsl:when>
                                          <xsl:otherwise>
                                            <input type="text" name="BIOSPECIMEN_dtSampleDate_Day" size="2" tabindex="32" class="uportal-input-text">
                                                        <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtSampleDate_Day"/></xsl:attribute>
                                            </input>
                                             <input type="text" name="BIOSPECIMEN_dtSampleDate_Month" size="2" tabindex="33" class="uportal-input-text">
                                                        <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtSampleDate_Month"/></xsl:attribute>
                                            </input>

                                          </xsl:otherwise>
                                          </xsl:choose>


                                            <input type="text" name="BIOSPECIMEN_dtSampleDate_Year" size="5" tabindex="34" class="uportal-input-text">
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
				<!-- Row 4 -->
				<tr>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_intStudyKeyDisplay"/>: </td>
					<td id="neuragenix-form-row-input" class="uportal-label">
						<select class="uportal-input-text" name="BIOSPECIMEN_intStudyKey"
							tabindex="25" onClick="javascript:updateColor(this)" onChange="javascript:updateColor(this)">
							<xsl:variable name="intBiospecStudyID">
								<xsl:value-of select="BIOSPECIMEN_intStudyKey"/>
							</xsl:variable>
							<xsl:for-each select="study_list">
								<xsl:variable name="varIntStudyID">
									<xsl:value-of select="STUDY_intStudyID"/>
								</xsl:variable>
                                                                <xsl:variable name="expired">
                                                                <xsl:value-of
                                                                    select="STUDY_strStudyName/@expired"
                                                                />
                                                                </xsl:variable>
								<xsl:choose>
    								<xsl:when test="$expired = '1'">
                                                                <option style="color: red; font-weight: bold">
									<xsl:attribute name="value">
										<xsl:value-of select="STUDY_intStudyID"/>
									</xsl:attribute>
									<xsl:if test="$intBiospecStudyID = $varIntStudyID">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="STUDY_strStudyName"/>
								</option>
                                                                </xsl:when>
                                                                <xsl:otherwise>
                                                                <option class="uportal-input-text">
									<xsl:attribute name="value">
										<xsl:value-of select="STUDY_intStudyID"/>
									</xsl:attribute>
									<xsl:if test="$intBiospecStudyID = $varIntStudyID">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="STUDY_strStudyName"/>
								</option>
                                                                </xsl:otherwise>
                                                                </xsl:choose>                                                                
							</xsl:for-each>
						</select>
                                                <script language="javascript">
                                                   updateColor(document.biospecimen_form.BIOSPECIMEN_intStudyKey);
                                                </script>
					</td>
					<td width="5%"/>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label">
						<xsl:value-of select="BIOSPECIMEN_dtExtractedDateDisplay"/>
					</td>
					<td width="25%" class="uportal-label">
                                        <xsl:choose>
                                        <xsl:when test="BIOSPECIMEN_dtExtractedDate/@display_type='dropdown'">
                                            <select name="BIOSPECIMEN_dtExtractedDate_Day" tabindex="35"
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

                                            <select name="BIOSPECIMEN_dtExtractedDate_Month" tabindex="36"
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
                                          </xsl:when>
                                          <xsl:otherwise>
                                            <input type="text" name="BIOSPECIMEN_dtExtractedDate_Day" size="2" tabindex="35" class="uportal-input-text">
                                                        <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtExtractedDate_Day"/></xsl:attribute>
                                            </input>
                                             <input type="text" name="BIOSPECIMEN_dtExtractedDate_Month" size="2" tabindex="36" class="uportal-input-text">
                                                        <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtExtractedDate_Month"/></xsl:attribute>
                                            </input>

                                          </xsl:otherwise>
                                          </xsl:choose>
                                    
                                            <input type="text" name="BIOSPECIMEN_dtExtractedDate_Year" size="5" tabindex="37" class="uportal-input-text">
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
						class="neuragenix-form-required-text" valign="top">*</td>
					<td id="neuragenix-form-row-input-label" class="uportal-label" valign="top">
						<xsl:value-of select="BIOSPECIMEN_strEncounterDisplay"/>: </td>
					<td id="neuragenix-form-row-input" class="uportal-label" valign="top">
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
					<td width="5%" valign="top"/>
					<td id="neuragenix-form-row-input-label-required"
						class="neuragenix-form-required-text" valign="top"/>
					<td id="neuragenix-form-row-input-label" class="uportal-label" valign="top">
						Flagged: </td>
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
								<input type="checkbox" name="isFlagged" tabindex="42" value="true"
									checked="checked" class="uportal-text"/>
							</xsl:when>
							<xsl:otherwise>
								<input type="checkbox" name="isFlagged" value="true" tabindex="42"
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
						<textarea name="BIOSPECIMEN_strComments" rows="4" cols="40" tabindex="43"
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
					
					<!-- if subtype lr appears -->
					<xsl:if test="count(BIOSPECIMEN_strSubTypeLR) > 0">
						
						<tr>
							<td width="1%" id="neuragenix-form-row-input-label-required"
								class="neuragenix-form-required-text" valign="top">
								<xsl:if
									test="./BIOSPECIMEN_strSubTypeLRDisplay[@required='true']"
									> * </xsl:if>
							</td>
							<td width="18%" id="neuragenix-form-row-input-label"
								class="uportal-label" valign="top">
								<xsl:value-of select="BIOSPECIMEN_strSubTypeLRDisplay"
									/>: </td>
							
							
							<td width="26%" id="neuragenix-form-row-input"
								class="uportal-label" valign="top">
								<!-- select box for subtype -->
								<select name="BIOSPECIMEN_strSubTypeLR" tabindex="25"
									class="uportal-input-text">
									<xsl:for-each select="BIOSPECIMEN_strSubTypeLR">
										<option>
											<xsl:attribute name="value">
												<xsl:value-of select="."/>
											</xsl:attribute>
											<xsl:if test="@selected='1'">
												<xsl:attribute name="selected"
													>true</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</option>
									</xsl:for-each>
								</select>
							</td>
							
						</tr>
					</xsl:if>    
					
				</xsl:if>
				<!-- subtype table end here -->
			</table>
			<table width="100%">
				<tr> 
					<td width="1%"/>
					<td width="21%" class="uportal-label"/>
					<td width="78%" class="uportal-label">
						<input type="submit" name="save" value="Save" class="uportal-button"
							tabindex="46"/>
						<!-- XXX: agus - unlock sub biospecimen type -->
						<input type="button" name="clear" value="Clear" tabindex="47"
							class="uportal-button"
							onclick="javascript:confirmClear('{$baseActionURL}?current=biospecimen_add&amp;intInternalPatientID={$intInternalPatientID}&amp;BIOSPECIMEN_intParentID={$intBiospecParentID}&amp;BIOSPECIMEN_strParentID={$strBiospecParentID}&amp;module=core&amp;action=add_biospecimen')"
							onblur="javascript:document.biospecimen_form.BIOSPECIMEN_strBiospecimenID.focus()"/>
					</td>
				</tr>
			</table>
		</form>
		<script language="javascript"> document.biospecimen_form.BIOSPECIMEN_strBiospecimenID.focus(); </script>
	</xsl:template>
</xsl:stylesheet>
