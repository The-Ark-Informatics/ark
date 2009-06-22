<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./patient_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>


  <xsl:output method="html" indent="no" />
  <xsl:template match="/body/patient">
  
  
   <xsl:variable name="PATIENT_strPatientID"><xsl:value-of select="PATIENT_strPatientID"/></xsl:variable>
   <xsl:variable name="PATIENT_strSurname"><xsl:value-of select="PATIENT_strSurname"/></xsl:variable>
   <xsl:variable name="PATIENT_strHospitalUR"><xsl:value-of select="PATIENT_strHospitalUR"/></xsl:variable>
   <xsl:variable name="PATIENT_strFirstName"><xsl:value-of select="PATIENT_strFirstName"/></xsl:variable>
   <xsl:variable name="PATIENT_dtDob_Day"><xsl:value-of select="PATIENT_dtDob_Day"/></xsl:variable>
   <xsl:variable name="PATIENT_dtDob_Month"><xsl:value-of select="PATIENT_dtDob_Month"/></xsl:variable>
   <xsl:variable name="PATIENT_dtDob_Year"><xsl:value-of select="PATIENT_dtDob_Year"/></xsl:variable>
   <xsl:variable name="back"><xsl:value-of select="back"/></xsl:variable>
   
   
   
	<!--table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Search patient<br/><hr/>
			</td> 
		</tr> 
	</table-->
        <table width="100%">
		 
		<tr>
			<td class="neuragenix-form-required-text" width="50%">
					<xsl:value-of select="error" />
			</td>
			<td class="neuragenix-form-required-text" width="50%" id="neuragenix-required-header" align="right">
			
			</td>

		</tr>
	</table>
	<!--form action="{$baseActionURL}?action=search_patient&amp;newSearch=true" method="post">
	<table>
		<tr>
			<td id="neuragenix-form-row-label" class="uportal-label">
				<xsl:value-of select="PATIENT_strPatientIDDisplay" />: 
			</td>
			<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
			<td class="uportal-label" id="neuragenix-form-row-input">
				<input type="text" value="{$PATIENT_strPatientID}" name="PATIENT_strPatientID" size="20" class="uportal-input-text" />
			</td>
			<td width="5%"></td>
			<td id="neuragenix-form-row-label" class="uportal-label">
				<xsl:value-of select="PATIENT_strHospitalURDisplay" />: 
			</td>
			<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
			<td class="uportal-label" id="neuragenix-form-row-input">
				<input type="text" value="{$PATIENT_strHospitalUR}" name="PATIENT_strHospitalUR" size="20" class="uportal-input-text" />
			</td>
		</tr>
     
		<tr>
		
			
			<td id="neuragenix-form-row-label" class="uportal-label">
				<xsl:value-of select="PATIENT_strSurnameDisplay" />: 
			</td>
			<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
			<td id="neuragenix-form-row" class="uportal-label">
				<input type="text" value="{$PATIENT_strSurname}" name="PATIENT_strSurname" size="20" class="uportal-input-text" />
			</td>
			<td width="5%"></td>
			<td id="neuragenix-form-row-label" class="uportal-label">
				<xsl:value-of select="PATIENT_strFirstNameDisplay" />: 
			</td>
			<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
			<td id="neuragenix-form-row-input" class="uportal-label">
				<input type="text" value="{$PATIENT_strFirstName}" name="PATIENT_strFirstName" size="20" class="uportal-input-text" />
			</td>

		</tr>
		<tr>
			<td id="neuragenix-form-row-label" class="uportal-label">
				<xsl:value-of select="PATIENT_dtDobDisplay" />: 
			</td>
			<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
			<td id="neuragenix-form-row-input" class="uportal-label">

                                <select name="PATIENT_dtDob_Day" tabindex="22" class="uportal-input-text">
                                <option value=""/>
                                <xsl:for-each select="PATIENT_dtDob_Day">
                                
                                        <option>
                                                <xsl:attribute name="value">
                                                <xsl:value-of select="." />
                                                </xsl:attribute>
                                                <xsl:if test="string-length( $back ) > '0'">
                                                    <xsl:if test="@selected = '1'">
                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                    </xsl:if>
                                                </xsl:if>
                                                <xsl:value-of select="." />
                                        </option>
                                </xsl:for-each>
                                </select>

                                <select name="PATIENT_dtDob_Month" tabindex="23" class="uportal-input-text">
                                <option value=""/>
                                <xsl:for-each select="PATIENT_dtDob_Month">
                                        <option> 
                                                <xsl:attribute name="value">
                                                <xsl:value-of select="." />
                                                </xsl:attribute>
                                                <xsl:if test="string-length( $back ) > '0'">
                                                    <xsl:if test="@selected = '1'">
                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                    </xsl:if>
                                                    
                                                </xsl:if>
                                                <xsl:value-of select="." />
                                        </option>
                                </xsl:for-each>
                                </select>
                                
				<input type="text" name="PATIENT_dtDob_Year" size="6" tabindex="24" class="uportal-input-text">
                                    <xsl:if test="string-length( $back ) > '0'">
                                            <xsl:attribute name="value"><xsl:value-of select="PATIENT_dtDob_Year"/></xsl:attribute>
                                    </xsl:if>
                                </input>
                                        
                                
                        </td>
                        <td width="5%"></td>
			
			<td id="neuragenix-form-row-label" class="uportal-label">
				<xsl:value-of select="PATIENT_strSpeciesDisplay"/>
			</td>
			<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
			<td id="neuragenix-form-row" class="uportal-label">
                            <select name="PATIENT_strSpecies" tabindex="23" class="uportal-input-text">
                                
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
                                <option value=""/>
                            </select>
                        </td>

		</tr>
                <tr>
                        <td id="neuragenix-form-row-label" class="uportal-label">
				<input type="reset" name="clear" value="{$clearBtnLabel}" class="uportal-button" />       
			</td>
			<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
			<td id="neuragenix-form-row-input" class="uportal-label" align="center">
                             
                        </td>
                        <td width="5%"></td>
			
			<td id="neuragenix-form-row-label" class="uportal-label">
				
			</td>
			<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
			<td id="neuragenix-form-row" class="uportal-label" align="center">
                            <input type="submit" name="submit" value="{$searchBtnLabel}" class="uportal-button" />
                        </td>
                </tr>
			

	</table>
	</form-->
  </xsl:template>

      
       
 
</xsl:stylesheet>
