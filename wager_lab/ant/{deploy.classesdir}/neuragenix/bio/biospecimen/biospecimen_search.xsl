<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./biospecimen_menu.xsl"/>

<xsl:param name="formParams">current=biospecimen_search</xsl:param>

  <xsl:output method="html" indent="no" />
  <!-- Get the parameters from the channel class -->
  <xsl:param name="strBiospecimenID"><xsl:value-of select="strBiospecimenID" /></xsl:param>
  <xsl:param name="strPatientID"><xsl:value-of select="strPatientID" /></xsl:param>
  <xsl:param name="strConsentID"><xsl:value-of select="strConsentID" /></xsl:param>
  <xsl:param name="strErrorMessage">strErrorMessage</xsl:param>
  
  <xsl:template match="/ErrorOnly">
        <xsl:value-of select="$strErrorMessage" />
    </xsl:template>
  
  <xsl:template match="biospecimen">
	<table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Search biospecimen<br/><hr/>
			</td> 
		</tr> 
	</table>
	<form name="biospecimen_search" action="{$baseActionURL}?{$formParams}" method="post">
	<table width="100%">
		<xsl:if test="count(BIOSPECIMEN_strBiospecimenIDDisplay) > 0">
                    <tr>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strBiospecimenIDDisplay" />: 
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td class="uportal-label" id="neuragenix-form-row-input">
                                    <input type="text" name="BIOSPECIMEN_strBiospecimenID" size="20" tabindex="1" class="uportal-input-text" />
                            </td>
                            <td width="5%"></td>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td class="uportal-label" id="neuragenix-form-row-input">
                            </td>
                    </tr>
                </xsl:if>
                
                <xsl:if test="count(PATIENT_strPatientIDDisplay) > 0">
                    <tr>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                                    <xsl:value-of select="PATIENT_strPatientIDDisplay" />: 
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td id="neuragenix-form-row" class="uportal-label">
                                    <input type="text" name="PATIENT_strPatientID" size="20" tabindex="4" class="uportal-input-text" />
                            </td>
                            <td width="5%"></td>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td id="neuragenix-form-row-input" class="uportal-label">
                            </td>

                    </tr>
                </xsl:if>
                
                <xsl:if test="count(BIOSPECIMEN_strOtherID) > 0">
                    <tr>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strOtherIDDisplay" />: 
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td id="neuragenix-form-row" class="uportal-label">
                                    <input type="text" name="BIOSPECIMEN_strOtherID" size="20" tabindex="7" class="uportal-input-text" />
                            </td>
                            <td width="5%"></td>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td id="neuragenix-form-row-input" class="uportal-label">
                            </td>

                    </tr>
                </xsl:if>
                
                <!-- Sample Date -->
                
                <xsl:if test="count(BIOSPECIMEN_dtSampleDateDisplay) > 0">
                    <tr>
                        <td id="neuragenix-form-row-label" class="uportal-label">
                                <xsl:value-of select="BIOSPECIMEN_dtSampleDateDisplay" />: 
                        </td>
                        <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                        <td id="neuragenix-form-row" class="uportal-label">
                           
                           <xsl:choose>
                            <xsl:when test="BIOSPECIMEN_dtSampleDate/@display_type='text'">
                                    <input type="text" name="BIOSPECIMEN_dtSampleDate_Day" size="2" tabindex="10" class="uportal-input-text">
                                            
                                    </input>
                                            &#160;
                                    <input type="text" name="BIOSPECIMEN_dtSampleDate_Month" size="2" tabindex="11" class="uportal-input-text">
                                            
                                    </input>
                            </xsl:when>
                            <!-- when it is drop down -->
                            <xsl:otherwise>
                                    <select name="BIOSPECIMEN_dtSampleDate_Day" tabindex="10" class="uportal-input-text">
                                            <option value="" selected="true" />
                                            <xsl:for-each select="BIOSPECIMEN_dtSampleDate_Day">
                                                    <option>
                                                            <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                            
                                                            <!-- uncheck below for default date of today -->
                                                            <!-- 
                                                            <xsl:if test="@selected = '1'">
                                                                    <xsl:attribute name="selected">true</xsl:attribute>
                                                            </xsl:if>
                                                            -->
                                                            <xsl:value-of select="."/>
                                                    </option>
                                            </xsl:for-each>
                                    </select>
                                            &#160;
                                    <select name="BIOSPECIMEN_dtSampleDate_Month" tabindex="11" class="uportal-input-text">
                                            <option value="" selected="true" />
                                            <xsl:for-each select="BIOSPECIMEN_dtSampleDate_Month">
                                                    <option>
                                                            <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                            
                                                            <!--  uncheck below for default date of today -->
                                                            <!--
                                                            <xsl:if test="@selected = '1'">
                                                                    <xsl:attribute name="selected">true</xsl:attribute>
                                                            </xsl:if>
                                                            -->
                                                            <xsl:value-of select="."/>
                                                    </option>
                                            </xsl:for-each>
                                    </select>
                            </xsl:otherwise>
                        </xsl:choose>
                                &#160;
                        <input type="text" name="BIOSPECIMEN_dtSampleDate_Year" size="4" tabindex="12" class="uportal-input-text">
                                <!-- <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtSampleDate_Year"/></xsl:attribute> -->
                        </input>    
                        
                        
                        
                        
                        </td>
                        <td width="5%"></td>
                        <td id="neuragenix-form-row-label" class="uportal-label">
                        </td>
                        <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                        <td id="neuragenix-form-row-input" class="uportal-label">
                        </td>
                     </tr>
                </xsl:if>
                
                <!-- Extracted Date -->
                
                <xsl:if test="count(BIOSPECIMEN_dtExtractedDateDisplay) > 0">
                        <tr>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_dtExtractedDateDisplay" />: 
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td id="neuragenix-form-row" class="uportal-label">
                                <xsl:choose>
                                        <xsl:when test="BIOSPECIMEN_dtExtractedDate/@display_type='text'">
                                                <input type="text" name="BIOSPECIMEN_dtExtractedDate_Day" size="2" tabindex="14" class="uportal-input-text">
                                                        
                                                </input>
                                                        &#160;
                                                <input type="text" name="BIOSPECIMEN_dtExtractedDate_Month" size="2" tabindex="15" class="uportal-input-text">
                                                        
                                                </input>
                                        </xsl:when>
                                        <!-- when it is drop down -->
                                        <xsl:otherwise>
                                                <select name="BIOSPECIMEN_dtExtractedDate_Day" tabindex="14" class="uportal-input-text">
                                                        <option value="" selected="true"/>
                                                        <xsl:for-each select="BIOSPECIMEN_dtExtractedDate_Day">
                                                                <option>
                                                                        <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                        <!-- Uncheck this if you want to use todays date as default -->
                                                                        <!-- 
                                                                        <xsl:if test="@selected = '1'">
                                                                                <xsl:attribute name="selected">true</xsl:attribute>
                                                                        </xsl:if>
                                                                        -->
                                                                        <xsl:value-of select="."/>
                                                                </option>
                                                        </xsl:for-each>
                                                </select>
                                                        &#160;
                                                        <select name="BIOSPECIMEN_dtExtractedDate_Month" tabindex="15" class="uportal-input-text">
                                                        <option value="" selected="true"/>
                                                        <xsl:for-each select="BIOSPECIMEN_dtExtractedDate_Month">
                                                                <option>
                                                                        <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                                        <!-- uncheck below to use todays date as default -->
                                                                        <!-- 
                                                                        <xsl:if test="@selected = '1'">
                                                                                <xsl:attribute name="selected">true</xsl:attribute>
                                                                        </xsl:if>
                                                                        -->
                                                                        <xsl:value-of select="."/>
                                                                </option>
                                                        </xsl:for-each>
                                                </select>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                            &#160;
                                    <input type="text" name="BIOSPECIMEN_dtExtractedDate_Year" size="4" tabindex="16" class="uportal-input-text">
                                            <!-- <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtExtractedDate_Year"/></xsl:attribute> -->
                                    </input>
                                
                            </td>
                            <td width="5%"></td>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td id="neuragenix-form-row-input" class="uportal-label">
                            </td>

                        </tr>
                
                </xsl:if>
                <!-- Study -->
                
                <xsl:if test="count(BIOSPECIMEN_intStudyKeyDisplay) > 0">
                        <tr>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                                   <xsl:value-of select="BIOSPECIMEN_intStudyKeyDisplay" />:
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td id="neuragenix-form-row" class="uportal-label">
                                    <select name="BIOSPECIMEN_intStudyKey" class="uportal-input-text" tabindex="20">
                                        <xsl:for-each select="study_list">
                                            <option>
                                               <xsl:attribute name="value"><xsl:value-of select="STUDY_intStudyID" /></xsl:attribute>
                                               <xsl:value-of select="STUDY_strStudyName" />
                                            </option>
                                        </xsl:for-each>
                                             <option value="" selected="true">
                                             </option>
                                             
                                    </select>
                            </td>
                            <td width="5%"></td>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td id="neuragenix-form-row-input" class="uportal-label">
                            </td>

                    </tr>
                </xsl:if>
                
                
                
	</table>
        <table width="100%">
            <tr>
                <td width="25%"></td>
                <td width="3%"><input type="checkbox" name="excess" value="value"></input></td>
                <td width="10%" id="neuragenix-form-row-label" class="uportal-label">Excess only</td>
                <td width="62%"></td>
            </tr>

	</table>
        
        
        
	<table width="100%">
			<tr>
				<td width="26%"></td>
				<td width="5%" class="uportal-label">
                                	<input type="hidden" name="module" value="BIOSPECIMEN_SEARCH" />
                                        <input type="submit" name="submit" value="Search" tabindex="25" class="uportal-button"/>
				</td>
				<td width="5%" class="uportal-label">
					<input type="button" name="clear" value="Clear" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?{$formParams}')" />
				</td>
				<td width="64%"></td>
			</tr>

	</table>
      	</form>
        
        <!-- make the cursor start at the first data entry input-->
        <script language="javascript">
            document.biospecimen_search.BIOSPECIMEN_strBiospecimenID.focus();
        </script>
        
        
  </xsl:template>


       
 
</xsl:stylesheet>
