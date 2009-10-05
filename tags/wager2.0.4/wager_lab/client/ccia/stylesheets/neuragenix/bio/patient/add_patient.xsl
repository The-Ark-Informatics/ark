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
  <xsl:param name="PATIENT_strMedicareNo"><xsl:value-of select="PATIENT_strMedicareNo" /></xsl:param>
  <xsl:param name="PATIENT_strSex"><xsl:value-of select="PATIENT_strSex" /></xsl:param>
  <xsl:param name="PATIENT_dtDateOfDeath_Year"><xsl:value-of select="PATIENT_dtDateOfDeath_Year" /></xsl:param>
  <xsl:param name="PATIENT_strCauseOfDeath"><xsl:value-of select="PATIENT_strCauseOfDeath" /></xsl:param>
  <!-- New For CCIA -->
  <xsl:param name="PATIENT_strDataVerified"><xsl:value-of select="PATIENT_strDataVerified" /></xsl:param>
  <xsl:param name="PATIENT_strAlert"><xsl:value-of select="PATIENT_strAlert" /></xsl:param>
  <xsl:param name="PATIENT_strInitials"><xsl:value-of select="PATIENT_strInitials" /></xsl:param>
  <xsl:param name="PATIENT_strStudyNo"><xsl:value-of select="PATIENT_strStudyNo" /></xsl:param>
  <xsl:param name="PATIENT_strCentreNo"><xsl:value-of select="PATIENT_strCentreNo" /></xsl:param>
  <xsl:param name="PATIENT_strProtocol"><xsl:value-of select="PATIENT_strProtocol" /></xsl:param>
  <xsl:param name="PATIENT_strLFU"><xsl:value-of select="PATIENT_strLFU" /></xsl:param>
  <xsl:param name="PATIENT_strClinicalStudies"><xsl:value-of select="PATIENT_strClinicalStudies" /></xsl:param>
  <xsl:param name="PATIENT_flEFS"><xsl:value-of select="PATIENT_flEFS" /></xsl:param>

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
  
    <xsl:variable name="error"><xsl:value-of select="error"/></xsl:variable>
  <xsl:variable name="titleError"><xsl:value-of select="titleError"/></xsl:variable>

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
        
<!-- NEW STUFF -->
                <!-- row 1 -->
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
				<xsl:value-of select="PATIENT_dtDobDisplay" />: 
			</td>
			<td width="25%">

			 <xsl:choose>
                                <xsl:when test="PATIENT_dtDob/@display_type='dropdown'">
                    
                                
				<select name="PATIENT_dtDob_Day" tabindex="29" class="uportal-input-text">
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
                                                               
                                <select name="PATIENT_dtDob_Month" tabindex="30" class="uportal-input-text">
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
                                <input type="text" name="PATIENT_dtDob_Day" size="2" tabindex="29" class="uportal-input-text">
                                         <xsl:if test="string-length( $titleError ) > '0'"> 
                                            <xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDob_Day"/></xsl:attribute> 
                                         </xsl:if>
                                </input>
                                <input type="text" name="PATIENT_dtDob_Month" size="2" tabindex="30" class="uportal-input-text">
                                        <xsl:if test="string-length( $titleError ) > '0'"> 
                                            <xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDob_Month"/></xsl:attribute>
                                        </xsl:if>
                                </input>
                           
                                
                                </xsl:otherwise>
                                </xsl:choose>

				<input type="text" name="PATIENT_dtDob_Year" size="4" tabindex="31" class="uportal-input-text">
                                    <xsl:if test="string-length( $titleError ) > '0'">
                                        <xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDob_Year"/></xsl:attribute> 
                                    </xsl:if> 
                                </input>

			</td>
			<td width="13%"></td>
		</tr>
                
                <!-- row 2 -->
		<tr>
                        <td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strHospitalURDisplay" />: 
			</td>
			
			<td width="25%">
				<input type="text" align="right" name="PATIENT_strHospitalUR" tabindex="21" value="{$PATIENT_strHospitalUR}" class="uportal-input-text"/>
			</td>
			<td width="5%"></td>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strStatusDisplay" />:
			</td>
			<td width="25%">
                                <select name="PATIENT_strStatus" tabindex="32" class="uportal-input-text" onChange="javascript:disableDeadFields(this);" >
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
                                
                <!-- row 3 -->
		<tr>
                        <td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strFirstNameDisplay" />: 
			</td>
			<td width="25%">
				<input type="text" name="PATIENT_strFirstName" size="20" tabindex="22" value="{$PATIENT_strFirstName}" class="uportal-input-text" />
			</td>
			<td width="5%"></td>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_dtDateOfDeathDisplay" />: 
			</td>
			<td width="25%">
                            <xsl:choose>
                            <xsl:when test="PATIENT_dtDateOfDeath/@display_type='dropdown'">
				<select name="PATIENT_dtDateOfDeath_Day" tabindex="33" class="uportal-input-text">
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
                            

				<select name="PATIENT_dtDateOfDeath_Month" tabindex="34" class="uportal-input-text">
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
                                <input type="text" name="PATIENT_dtDateOfDeath_Day" size="2" tabindex="33" class="uportal-input-text">
                                        <xsl:if test="string-length( $titleError ) > '0'"> 
                                            <xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDateOfDeath_Day"/></xsl:attribute>
                                        </xsl:if>    
                                </input>
                                 <input type="text" name="PATIENT_dtDateOfDeath_Month" size="2" tabindex="34" class="uportal-input-text">
                                        <xsl:if test="string-length( $titleError ) > '0'"> 
                                            <xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDateOfDeath_Month"/></xsl:attribute>
                                        </xsl:if> 
                                </input>
                              
                              </xsl:otherwise>
                              </xsl:choose>

				<input type="text" name="PATIENT_dtDateOfDeath_Year" size="4" tabindex="35" class="uportal-input-text">
                                    <xsl:if test="string-length( $titleError ) > '0'">
                                        <xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDateOfDeath_Year"/></xsl:attribute> 
                                    </xsl:if> 
                                </input>

			</td>
			<td width="13%"></td>
		</tr>
                
                <!-- row 4 -->
		<tr>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strSurnameDisplay" />: 	
			</td>
			<td width="25%">
				<input type="text" name="PATIENT_strSurname" size="20" tabindex="23" value="{$PATIENT_strSurname}" class="uportal-input-text" />
			</td>
			<td width="5%"></td>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strCauseOfDeathDisplay" />: 
			</td>
			<td width="25%">
				<select name="PATIENT_strCauseOfDeath" tabindex="36" class="uportal-input-text">
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
			<td width="13%"></td>
		</tr>
                
                <!-- row 5 -->
		<tr>
			<td width="1%" class="neuragenix-form-required-text">*</td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strSexDisplay" />:
			</td>
			<td width="25%">
                            <select name="PATIENT_strSex" tabindex="24" class="uportal-input-text">
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
			<td width="5%"></td>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" id="neuragenix-form-row-input-label" class="uportal-label">
				<xsl:value-of select="PATIENT_intAddressPostCodeDisplay" />: 
			</td>
			<td width="25%">
				<input align="right" type="text" name="PATIENT_intAddressPostCode" size="20" tabindex="37" value="{$PATIENT_intAddressPostCode}" class="uportal-input-text" />
			</td>
			<td width="13%"></td>
		</tr>
                
                <!-- row 6 -->
		<tr>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strInitialsDisplay" />:
			</td>
			<td width="25%">
				<input type="text" name="PATIENT_strInitials" size="20" tabindex="25" value="{$PATIENT_strInitials}" class="uportal-input-text" />
			</td>
			<td width="5%"></td>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strDataVerifiedDisplay" />:
			</td>
			<td width="25%">
				<input type="checkbox" name="PATIENT_strDataVerified" tabindex="38">
                                        <xsl:if test="string-length( $PATIENT_strDataVerified ) > '0'">
                                                <xsl:attribute name="checked">true</xsl:attribute>
                                        </xsl:if>
                                </input>
			</td>
			<td width="13%" ></td>
		</tr>
        
                <!-- row 7 -->
                <tr>
			<td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strStudyNoDisplay" />:
			</td>
			<td width="25%">
				<input type="text" name="PATIENT_strStudyNo" size="20" tabindex="26" value="{$PATIENT_strStudyNo}" class="uportal-input-text" />
			</td>
			<td width="5%"></td>
			<td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strClinicalStudiesDisplay" />:
			</td>
			<td width="25%">
                            <input style="text-align: right" type="text" name="PATIENT_strClinicalStudies" size="20" tabindex="39" value="{$PATIENT_strClinicalStudies}" class="uportal-input-text" />                        
			</td>
			<td width="13%" ></td>
		</tr>
        
                <!-- row 8 -->
                <tr>
			<td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strCentreNoDisplay" />:
			</td>
			<td width="25%">
				<input type="text" name="PATIENT_strCentreNo" size="20" tabindex="27" value="{$PATIENT_strCentreNo}" class="uportal-input-text" />
			</td>
			<td width="5%"></td>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="15%" class="uportal-label">
                            <xsl:value-of select="PATIENT_strLFUDisplay" />:				
			</td>
			<td width="25%">
                            <input style="text-align: right" type="text" name="PATIENT_strLFU" size="20" tabindex="40" value="{$PATIENT_strLFU}" class="uportal-input-text" />
			</td>
			<td width="13%"></td> 
		</tr>
                
                <!-- row 9 -->
                <tr>
			<td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strProtocolDisplay" />:
			</td>
			<td width="25%">
				<select name="PATIENT_strProtocol" tabindex="28" class="uportal-input-text">
                                    <xsl:for-each select="PATIENT_strProtocol">		
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
				<xsl:value-of select="PATIENT_strAlertDisplay" />:
			</td>
			<td width="25%">
				<select name="PATIENT_strAlert" tabindex="41" class="uportal-input-text">
                                    <xsl:for-each select="PATIENT_strAlert">		
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

                <!-- row 10 -->
                <tr valign="top">
			<td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="15%" class="uportal-label"></td>
			<td width="25%"></td>
			<td width="5%"></td>
			<td width="1%" class="neuragenix-form-required-text"></td>                        
			<td width="15%" class="uportal-label">
				<xsl:value-of select="PATIENT_strCommentsDisplay"/>:
			</td>
			<td width="25%">
                                <textarea name="PATIENT_strComments" rows="4" cols="40" tabindex="42">
                                        <xsl:value-of select="PATIENT_strComments"/>
                                </textarea>
                        </td>
			<td width="13%"></td> 
                </tr>
                        
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
            disableDeadFields(document.frm_addpatient.PATIENT_strStatus);
        </script>
  </xsl:template>

      
       
 
</xsl:stylesheet>
