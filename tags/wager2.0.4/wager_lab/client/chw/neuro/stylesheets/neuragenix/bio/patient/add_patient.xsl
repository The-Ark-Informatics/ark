<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./patient_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>
  <xsl:output method="html" indent="no" />

  <xsl:template match="patient">
  <!-- Get the parameters from the channel class -->
  <xsl:param name="PATIENT_strPatientID"><xsl:value-of select="PATIENT_strPatientID" /></xsl:param>
  <xsl:param name="PATIENT_strHospitalUR"><xsl:value-of select="PATIENT_strHospitalUR" /></xsl:param>
  <xsl:param name="PATIENT_strTitle"><xsl:value-of select="PATIENT_strTitle" /></xsl:param>
  <xsl:param name="PATIENT_strSurname"><xsl:value-of select="PATIENT_strSurname" /></xsl:param>
  <xsl:param name="PATIENT_strOtherID"><xsl:value-of select="PATIENT_strOtherID" /></xsl:param>
  <xsl:param name="PATIENT_strFirstName"><xsl:value-of select="PATIENT_strFirstName" /></xsl:param>
  <xsl:param name="PATIENT_strOtherNames"><xsl:value-of select="PATIENT_strOtherNames" /></xsl:param>
  <xsl:param name="PATIENT_dtDob_Year"><xsl:value-of select="PATIENT_dtDob_Year" /></xsl:param>
  <xsl:param name="PATIENT_strAddressLine1"><xsl:value-of select="PATIENT_strAddressLine1" /></xsl:param>
  <xsl:param name="PATIENT_strAddressSuburb"><xsl:value-of select="PATIENT_strAddressSuburb" /></xsl:param>
  <xsl:param name="PATIENT_strAddressState"><xsl:value-of select="PATIENT_strAddressState" /></xsl:param>
  <xsl:param name="PATIENT_strAddressOtherState"><xsl:value-of select="PATIENT_strAddressOtherState" /></xsl:param>
  <xsl:param name="PATIENT_strAddressCountry"><xsl:value-of select="PATIENT_strAddressCountry" /></xsl:param>
  <xsl:param name="PATIENT_strAddressOtherCountry"><xsl:value-of select="PATIENT_strAddressOtherCountry" /></xsl:param>
  <xsl:param name="PATIENT_intAddressPostCode"><xsl:value-of select="PATIENT_intAddressPostCode" /></xsl:param>
  <xsl:param name="PATIENT_intPhoneWork"><xsl:value-of select="PATIENT_intPhoneWork" /></xsl:param>
  <xsl:param name="PATIENT_intPhoneHome"><xsl:value-of select="PATIENT_intPhoneHome" /></xsl:param>
  <xsl:param name="PATIENT_intPhoneMobile"><xsl:value-of select="PATIENT_intPhoneMobile" /></xsl:param>
  <xsl:param name="PATIENT_strStatus"><xsl:value-of select="PATIENT_strStatus" /></xsl:param>
  <xsl:param name="PATIENT_strHospital"><xsl:value-of select="PATIENT_strHospital" /></xsl:param>
  <!--<xsl:param name="PATIENT_strMedicareNo"><xsl:value-of select="PATIENT_strMedicareNo" /></xsl:param> -->
  <xsl:param name="PATIENT_strSex"><xsl:value-of select="PATIENT_strSex" /></xsl:param>
  <xsl:param name="PATIENT_dtDateOfDeath_Year"><xsl:value-of select="PATIENT_dtDateOfDeath_Year" /></xsl:param>
  <xsl:param name="PATIENT_strCauseOfDeath"><xsl:value-of select="PATIENT_strCauseOfDeath" /></xsl:param>
  <xsl:variable name="error"><xsl:value-of select="error"/></xsl:variable>
  <xsl:variable name="titleError"><xsl:value-of select="titleError"/></xsl:variable>

    <script language="javascript">
    
        function disableDeadFields(s) 
        {
            if (s.options[s.selectedIndex].value == 'Dead')
            {
                document.frm_addpatient.PATIENT_dtDateOfDeath_Day.disabled=false;
                document.frm_addpatient.PATIENT_dtDateOfDeath_Month.disabled=false;
                document.frm_addpatient.PATIENT_dtDateOfDeath_Year.disabled=false;
                document.frm_addpatient.PATIENT_strCauseOfDeath.disabled=false;
            }
            else
            {
                document.frm_addpatient.PATIENT_dtDateOfDeath_Day.disabled=true;
                document.frm_addpatient.PATIENT_dtDateOfDeath_Month.disabled=true;
                document.frm_addpatient.PATIENT_dtDateOfDeath_Year.disabled=true;
                document.frm_addpatient.PATIENT_strCauseOfDeath.disabled=true;
                document.frm_addpatient.PATIENT_dtDateOfDeath_Day.value='';
                document.frm_addpatient.PATIENT_dtDateOfDeath_Month.value='';
                document.frm_addpatient.PATIENT_dtDateOfDeath_Year.value='';
            }            
        }   
        
    </script>  

  	<form name="frm_addpatient" action="{$baseActionURL}?action=add_patient" method="post">
	<table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Add patient<br/><hr/>
			</td>
		</tr>
	</table>
	<table width="100%">
		 
		<tr>
			<td class="neuragenix-form-required-text" width="80%">
                                       <xsl:value-of select="error"/> <br/>
                                       <xsl:value-of select="titleError"/>
			</td>
			<td class="neuragenix-form-required-text" width="20%" id="neuragenix-required-header" align="right">
			* = Required fields
			</td>

		</tr>
	</table>
        
	<table width="100%">
                <!-- row 1-->
		<tr>
                        <td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strPatientIDDisplay" />: 
			</td>
			
			<td width="25%">
				<input type="hidden" align="right" name="PATIENT_strPatientID" value="0"/>
                                <span class="uportal-label">System Generated</span>
			</td>
			<td width="5%"></td>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strSexDisplay" />:
			</td>
                        <td width="25%">
                            <select name="PATIENT_strSex" tabindex="20" class="uportal-input-text">
				<xsl:for-each select="PATIENT_strSex">		
                                    <option>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="." />
                                        </xsl:attribute> 
                                        <xsl:if test="@selected=1">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        <xsl:value-of select="." />		
                                    </option>
				</xsl:for-each>
                            </select>
			</td>
			<td width="13%"></td>
			

			<td width="13%"></td>
		</tr>
                <!--row 2-->
		<tr>
                        <td width="1%" class="neuragenix-form-required-text"></td>
                        <!-- 
                        <td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strMedicareNoDisplay" />: 
			</td>
			
			<td width="25%">
				<input type="text" align="right" name="PATIENT_strMedicareNo" tabindex="21" value="{$PATIENT_strMedicareNo}" class="uportal-input-text"/>
			</td>
                        -->                       
                        <td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strHospitalURDisplay" />: 
			</td>
			
			<td width="25%">
				<input type="text" align="right" name="PATIENT_strHospitalUR" tabindex="11" value="{$PATIENT_strHospitalUR}" class="uportal-input-text"/>
			</td>
			<td width="5%"></td>
			<td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strSpeciesDisplay" />:
			</td>
			
			<td width="25%">
				<select name="PATIENT_strSpecies" tabindex="25" class="uportal-input-text">
                                
                                <xsl:for-each select="PATIENT_strSpecies">
                                        <option>
                                                <xsl:attribute name="value">
                                                <xsl:value-of select="." />
                                                </xsl:attribute>
                                                <xsl:if test="@selected = '1'">
                                                        <xsl:attribute name="selected">true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="." />
                                        </option>
                                </xsl:for-each>
                                </select>
			</td>
                        
		</tr>
                <!-- row 3-->
                <tr>
                       <td width="1%" class="neuragenix-form-required-text"/>
                          <td width="19%" class="uportal-label" valign="top">
                             <xsl:value-of select="PATIENT_strHospitalDisplay"/>:</td>
                          <td width="25%">
                          <select name="PATIENT_strHospital" tabindex="12" class="uportal-input-text">
                          <xsl:for-each select="PATIENT_strHospital">
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
                        
                        <!-- Patient Category -->
                        <td width="5%"></td>
                        <td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
                        
				<xsl:value-of select="PATIENT_strCategoryDisplay" />:
			</td>
			<td width="25%">
				<select name="PATIENT_strCategory" tabindex="26" class="uportal-input-text">
				<xsl:for-each select="PATIENT_strCategory">
					<option>
						<xsl:attribute name="value">
						<xsl:value-of select="." />
						</xsl:attribute> 
						<xsl:if test="@selected=1">
							<xsl:attribute name="selected">true</xsl:attribute> 
						</xsl:if>
						<xsl:value-of select="." />		
					</option>
				</xsl:for-each>
				</select>
			</td>
			<td width="13%"></td>
			<!--
                        <td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strTitleDisplay" />: 
			</td>
			<td width="25%">
                          <select name="PATIENT_strTitle" tabindex="22" class="uportal-input-text">
				<xsl:for-each select="PATIENT_strTitle">		
                                    <option>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="." />
                                        </xsl:attribute> 
                                        <xsl:if test="@selected=1">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        <xsl:value-of select="." />		
                                    </option>
				</xsl:for-each>
                            </select>
			</td>
			<td width="5%"></td>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strAddressLine1Display" />: 
			</td>
			<td width="25%">
				<input type="text" name="PATIENT_strAddressLine1" size="20" tabindex="37" value="{$PATIENT_strAddressLine1}" class="uportal-input-text" />
			</td>
                        
                        
			<td width="13%"></td>
                         -->
		</tr>
               
                <!-- row 4 -->
		<tr> 
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strFirstNameDisplay" />: 
			</td>
			<td width="25%">
				<input type="text" name="PATIENT_strFirstName" size="20" tabindex="13" value="{$PATIENT_strFirstName}" class="uportal-input-text" />
			</td>
			<td width="5%"></td>
                        <!-- Diagnosis here -->
                        
                        <td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
                        
				<xsl:value-of select="PATIENT_strDiagnosisDisplay" />:
			</td>
			<td width="25%">
				<select name="PATIENT_strDiagnosis" tabindex="27" class="uportal-input-text">
				<xsl:for-each select="PATIENT_strDiagnosis">
					<option>
						<xsl:attribute name="value">
						<xsl:value-of select="." />
						</xsl:attribute> 
						<xsl:if test="@selected=1">
							<xsl:attribute name="selected">true</xsl:attribute> 
						</xsl:if>
						<xsl:value-of select="." />		
					</option>
				</xsl:for-each>
				</select>
			</td>
                        <!--
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strAddressSuburbDisplay" />: 
			</td>
			<td width="25%">
				<input type="text" name="PATIENT_strAddressSuburb" size="20" tabindex="38" value="{$PATIENT_strAddressSuburb}" class="uportal-input-text" />
			</td>
                        -->
                        <td width="13%"></td>                       
		</tr>
                 
                <!-- row 5 -->
		<tr>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strSurnameDisplay" />: 	
			</td>
			<td width="25%">
				<input type="text" name="PATIENT_strSurname" size="20" tabindex="14" value="{$PATIENT_strSurname}" class="uportal-input-text" />
			</td>
			<td width="5%"></td>
                        <!-- Biopsy here -->
                        <td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
                        
				<xsl:value-of select="PATIENT_strStatusDisplay" />:
			</td>
			<td width="25%">
				<select name="PATIENT_strStatus" tabindex="28" class="uportal-input-text">
				<xsl:for-each select="PATIENT_strStatus">
					<option>
						<xsl:attribute name="value">
						<xsl:value-of select="." />
						</xsl:attribute> 
						<xsl:if test="@selected=1">
							<xsl:attribute name="selected">true</xsl:attribute> 
						</xsl:if>
						<xsl:value-of select="." />		
					</option>
				</xsl:for-each>
				</select>
			</td>
                        <!--
			<td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strAddressStateDisplay" />: 
			</td>
			<td width="25%">
				<select name="PATIENT_strAddressState" tabindex="39" class="uportal-input-text">
				<xsl:for-each select="PATIENT_strAddressState">
		
				<option>
				<xsl:attribute name="value">
				<xsl:value-of select="." />
				</xsl:attribute> 
				<xsl:if test="@selected=1">
					<xsl:attribute name="selected">true</xsl:attribute> 
				</xsl:if>
				
				<xsl:value-of select="." />		
				</option>
				</xsl:for-each>
				</select>

			</td>
			
			<td width="13%"></td>
                        -->

		</tr>
                
                <!-- row 6 -->
                
		<tr> 
			<td width="1%" class="neuragenix-form-required-text">*</td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_dtDobDisplay" />: 
			</td>
			<td width="25%">

			 <xsl:choose>
                                <xsl:when test="PATIENT_dtDob/@display_type='dropdown'">
                    
                                
				<select name="PATIENT_dtDob_Day" tabindex="15" class="uportal-input-text">
                                <option value=""/>
				<xsl:for-each select="PATIENT_dtDob_Day">
					<option>
						<xsl:attribute name="value">
                                                    <xsl:value-of select="." />
						</xsl:attribute> 
                                                 <xsl:if test="string-length( $titleError ) > '0'">
                                                    <xsl:if test="@selected = '1'">
                                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                                    </xsl:if>
                                                </xsl:if>
						<xsl:value-of select="." />
                                               	
					</option>
				</xsl:for-each>
				</select>
                                                               
                                <select name="PATIENT_dtDob_Month" tabindex="16" class="uportal-input-text">
                                <option value=""/>
				<xsl:for-each select="PATIENT_dtDob_Month">
					<option>
						<xsl:attribute name="value">
						<xsl:value-of select="." />
						</xsl:attribute> 
                                                <xsl:if test="string-length( $titleError ) > '0'"> 
                                                    <xsl:if test="@selected = '1'">
                                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                                    </xsl:if>
                                                </xsl:if>
                                                
						<xsl:value-of select="." />		
					</option>
				</xsl:for-each>
				</select> 
                                </xsl:when>
                                <!-- when it is a text-->
                                <xsl:otherwise>
                                <input type="text" name="PATIENT_dtDob_Day" size="2" tabindex="15" class="uportal-input-text">
                                         <xsl:if test="string-length( $titleError ) > '0'"> 
                                            <xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDob_Day"/></xsl:attribute> 
                                         </xsl:if>
                                </input>
                                <input type="text" name="PATIENT_dtDob_Month" size="2" tabindex="16" class="uportal-input-text">
                                        <xsl:if test="string-length( $titleError ) > '0'"> 
                                            <xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDob_Month"/></xsl:attribute>
                                        </xsl:if>
                                </input>
                           
                                
                                </xsl:otherwise>
                                </xsl:choose>

				<input type="text" name="PATIENT_dtDob_Year" size="4" tabindex="17" class="uportal-input-text">
                                    <xsl:if test="string-length( $titleError ) > '0'">
                                        <xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDob_Year"/></xsl:attribute> 
                                    </xsl:if> 
                                </input>

			</td>
			<td width="5%"></td>
                        <!--
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" id="neuragenix-form-row-input-label" class="uportal-label">
				<xsl:value-of select="PATIENT_intAddressPostCodeDisplay" />: 
			</td>
			<td width="25%">
				<input align="right" type="text" name="PATIENT_intAddressPostCode" size="20" tabindex="40" value="{$PATIENT_intAddressPostCode}" class="uportal-input-text" />
			</td>
			
			<td width="13%"></td>
                        -->
		</tr>
                <!--row 7-->
                <!-- Gestat here -->
                <tr>
                        <td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strGestatDisplay" />: 
			</td>
			<td width="25%">
				<input type="text" name="PATIENT_strGestat" size="20" tabindex="18" value="{PATIENT_strGestat}" class="uportal-input-text" />
			</td>
			<td width="5%"></td>
                        
                </tr>
                <!--
                <tr>                
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_dtDateOfDeathDisplay" />: 
			</td>
			<td width="25%">
                            <xsl:choose>
                            <xsl:when test="PATIENT_dtDateOfDeath/@display_type='dropdown'">
				<select name="PATIENT_dtDateOfDeath_Day" tabindex="28" class="uportal-input-text">
                                <option value=""/>
				<xsl:for-each select="PATIENT_dtDateOfDeath_Day">
					<option>
						<xsl:attribute name="value">
                                                    <xsl:value-of select="." />
						</xsl:attribute> 
                                                 <xsl:if test="string-length( $titleError ) > '0'">
                                                    <xsl:if test="@selected = '1'">
                                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                                    </xsl:if>
                                                  </xsl:if>
						<xsl:value-of select="." />		
					</option>
				</xsl:for-each>
				</select>
                            

				<select name="PATIENT_dtDateOfDeath_Month" tabindex="29" class="uportal-input-text">
                                <option value=""/>
				<xsl:for-each select="PATIENT_dtDateOfDeath_Month">
					<option>
						<xsl:attribute name="value">
                                                    <xsl:value-of select="." />
						</xsl:attribute> 
                                                <xsl:if test="string-length( $titleError ) > '0'"> 
                                                    <xsl:if test="@selected = '1'">
                                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                                    </xsl:if>
                                                </xsl:if>
						<xsl:value-of select="." />		
					</option>
				</xsl:for-each>
				</select>
                              </xsl:when>
                              <xsl:otherwise>
                                <input type="text" name="PATIENT_dtDateOfDeath_Day" size="2" tabindex="28" class="uportal-input-text">
                                        <xsl:if test="string-length( $titleError ) > '0'"> 
                                            <xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDateOfDeath_Day"/></xsl:attribute>
                                        </xsl:if>    
                                </input>
                                 <input type="text" name="PATIENT_dtDateOfDeath_Month" size="2" tabindex="29" class="uportal-input-text">
                                        <xsl:if test="string-length( $titleError ) > '0'"> 
                                            <xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDateOfDeath_Month"/></xsl:attribute>
                                        </xsl:if> 
                                </input>
                              
                              </xsl:otherwise>
                              </xsl:choose>

				<input type="text" name="PATIENT_dtDateOfDeath_Year" size="4" tabindex="30" class="uportal-input-text">
                                    <xsl:if test="string-length( $titleError ) > '0'">
                                        <xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDateOfDeath_Year"/></xsl:attribute> 
                                    </xsl:if> 
                                </input>

			</td>
			<td width="5%"></td>
			<td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strAddressCountryDisplay" />: 
			</td>
			<td width="25%">
				<select name="PATIENT_strAddressCountry" tabindex="41" class="uportal-input-text">
				<xsl:for-each select="PATIENT_strAddressCountry">
				<option>
				<xsl:attribute name="value">
				<xsl:value-of select="." />
				</xsl:attribute> 
				<xsl:if test="@selected=1">
					<xsl:attribute name="selected">true</xsl:attribute> 
				</xsl:if>
				
				<xsl:value-of select="." />		
				</option>
				</xsl:for-each>
				</select>
			</td>
                       
			<td width="13%"></td>
		</tr>
                -->
                <!-- row 8 -->
                <!--
		<tr>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strCauseOfDeathDisplay" />: 
			</td>
			<td width="25%">
				<select name="PATIENT_strCauseOfDeath" tabindex="31" class="uportal-input-text">
				<xsl:for-each select="PATIENT_strCauseOfDeath">
		
				<option>
				<xsl:attribute name="value">
				<xsl:value-of select="." />
				</xsl:attribute> 
				<xsl:if test="@selected=1">
					<xsl:attribute name="selected">true</xsl:attribute> 
				</xsl:if>
				
				<xsl:value-of select="." />		
				</option>
				</xsl:for-each>
				</select>
			</td>
			<td width="5%"></td>
			<td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strAddressOtherCountryDisplay" />: 
			</td>
			<td width="25%">
				<input type="text" name="PATIENT_strAddressOtherCountry" size="20" tabindex="42" value="{$PATIENT_strAddressOtherCountry}" class="uportal-input-text" />
			</td>
                        
			<td width="13%"></td>
		</tr>
                
                
                -->
                <!-- row 9 -->
                <!--
		<tr>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_intPhoneHomeDisplay" />: 
			</td>
			<td width="25%">
				<input type="text" align="right" name="PATIENT_intPhoneHome" size="20" tabindex="32" value="{$PATIENT_intPhoneHome}" class="uportal-input-text" />
			</td>
			<td width="5%"></td>	
			<td width="1%" class="neuragenix-form-required-text"></td>
                 	<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strAddressOtherStateDisplay" />: 
			</td>
			<td width="25%">
				<input type="text" name="PATIENT_strAddressOtherState" size="20" tabindex="43" value="{$PATIENT_strAddressOtherState}" class="uportal-input-text" />
			</td>

                        <td width="13%"></td> 
		</tr>
                -->
                <!-- row 10 -->
                <!--
		<tr>
                
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_intPhoneWorkDisplay" />:
			</td>
			<td width="25%">
				<input type="text" align="right" name="PATIENT_intPhoneWork" size="20" tabindex="33" value="{$PATIENT_intPhoneWork}" class="uportal-input-text" />
			</td>
			<td width="5%"></td>
                
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strStatusDisplay" />:
			</td>
			<td width="25%">
				<select name="PATIENT_strStatus" tabindex="44" class="uportal-input-text" >
				<xsl:for-each select="PATIENT_strStatus">
					<option>
						<xsl:attribute name="value">
						<xsl:value-of select="." />
						</xsl:attribute> 
						<xsl:if test="@selected=1">
							<xsl:attribute name="selected">true</xsl:attribute> 
						</xsl:if>
						<xsl:value-of select="." />		
					</option>
				</xsl:for-each>
				</select>
			</td>
			<td width="13%"></td> 
		</tr>
                -->
                <!-- row 11 --> 
		<!--
                <tr>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_intPhoneMobileDisplay" />: 
			</td>
			<td width="25%">
				<input type="text" align="right" name="PATIENT_intPhoneMobile" size="20" tabindex="34" value="{$PATIENT_intPhoneMobile}" class="uportal-input-text" />
			</td>
			<td width="5%"></td> 
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				
			</td>
			<td width="25%">
				
			</td>
			<td width="13%"></td>
		</tr>
                -->
                                               
	</table>
        
	<table width="100%">
                <tr>
			<td height= "5px"/>
		</tr>

		<tr>
			<td width="1%"></td>
			<td width="33%" class="uportal-label"></td>
			<td width="5%" class="uportal-label">
				<input type="reset" name="clear" value="{$clearBtnLabel}" tabindex="46" class="uportal-button" />
			</td>
			<td width="5%" class="uportal-label">
				<input type="submit" name="save" value="{$saveBtnLabel}" tabindex="45" class="uportal-button" 
                                onblur="javascript:document.frm_addpatient.PATIENT_strHospitalUR.focus()"/>
			</td>
			<td width="5%" class="uportal-label">

				<!-- <input type="submit" name="pmci" value="Lookup PAS" tabindex="20" class="uportal-button" /> -->
			</td>
			<td width="51%" class="uportal-label"></td>

		</tr>

	</table>
	</form>
        <!-- move the cursor to the first entry box by default-->
        <script>
            document.frm_addpatient.PATIENT_strHospitalUR.focus();
            //disableDeadFields(document.frm_addpatient.PATIENT_strStatus);
        </script>
  </xsl:template>

      
       
 
</xsl:stylesheet>
