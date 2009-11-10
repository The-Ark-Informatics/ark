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
			Search Samples<br/><hr/>
			</td> 
		</tr> 
	</table>
	<form name="biospecimen_search" action="{$baseActionURL}?{$formParams}" method="post">
	<table width="100%">
                <!-- strBiospecimenID -->
		<xsl:if test="count(BIOSPECIMEN_strBiospecimenIDDisplay) > 0">
                    <tr>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strBiospecimenIDDisplay" />: 
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td class="uportal-label" id="neuragenix-form-row-input">
                                    <input type="text" name="BIOSPECIMEN_strBiospecimenID"  tabindex="1" size="20" class="uportal-input-text" />
                            </td>
                            <td width="5%"></td>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td class="uportal-label" id="neuragenix-form-row-input">
                            </td>
                    </tr>
                </xsl:if>
                
                <!-- strPatientID -->
                <xsl:if test="count(PATIENT_strPatientIDDisplay) > 0">
                    <tr>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                                    <xsl:value-of select="PATIENT_strPatientIDDisplay" />: 
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td id="neuragenix-form-row" class="uportal-label">
                                    <input type="text" name="PATIENT_strPatientID" tabindex="10" size="20" class="uportal-input-text" />
                            </td>
                            <td width="5%"></td>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td id="neuragenix-form-row-input" class="uportal-label">
                            </td>

                    </tr>
                </xsl:if>
                
                <!-- strProtocol -->
                <xsl:if test="count(PATIENT_strProtocolDisplay) > 0">
                    <tr>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                                    <xsl:value-of select="PATIENT_strProtocolDisplay" />: 
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td id="neuragenix-form-row" class="uportal-label">
                                    <select name="PATIENT_strProtocol" tabindex="20" class="uportal-input-text">
                                            <option value="" selected="true" />
                                            <xsl:for-each select="PATIENT_strProtocol">
                                                    <option>
                                                            <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                            <xsl:value-of select="."/>
                                                    </option>
                                            </xsl:for-each>
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

                <!-- Study -->
                <xsl:if test="count(BIOSPECIMEN_intStudyKeyDisplay) > 0">
                        <tr>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                                   <xsl:value-of select="BIOSPECIMEN_intStudyKeyDisplay" />
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td id="neuragenix-form-row" class="uportal-label">
                                    <select name="BIOSPECIMEN_intStudyKey" tabindex="30" class="uportal-input-text">
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
                
                <!-- strOtherID -->
                <xsl:if test="count(BIOSPECIMEN_strOtherID) > 0">
                    <tr>
                            <td id="neuragenix-form-row-label" class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strOtherIDDisplay" />: 
                            </td>
                            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                            <td id="neuragenix-form-row" class="uportal-label">
                                    <input type="text" name="BIOSPECIMEN_strOtherID" size="20" class="uportal-input-text" />
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
                                    <input type="text" name="BIOSPECIMEN_dtSampleDate_Day" size="2" tabindex="40" class="uportal-input-text">
                                            <!-- xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtSampleDate_Day"/></xsl:attribute -->
                                    </input>
                                            &#160;
                                    <input type="text" name="BIOSPECIMEN_dtSampleDate_Month" size="2" tabindex="42" class="uportal-input-text">
                                            <!-- xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtSampleDate_Month"/></xsl:attribute -->
                                    </input>
                            </xsl:when>
                            <!-- when it is drop down -->
                            <xsl:otherwise>
                                    <select name="BIOSPECIMEN_dtSampleDate_Day" tabindex="40" class="uportal-input-text">
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
                                    <select name="BIOSPECIMEN_dtSampleDate_Month" tabindex="42" class="uportal-input-text">
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
                        <input type="text" name="BIOSPECIMEN_dtSampleDate_Year" size="4" tabindex="44" class="uportal-input-text">
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
                                                <input type="text" name="BIOSPECIMEN_dtExtractedDate_Day" size="2" tabindex="50" class="uportal-input-text">
                                                        <!-- xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtExtractedDate_Day"/></xsl:attribute -->
                                                </input>
                                                        &#160;
                                                <input type="text" name="BIOSPECIMEN_dtExtractedDate_Month" size="2" tabindex="52" class="uportal-input-text">
                                                        <!-- xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtExtractedDate_Month"/></xsl:attribute -->
                                                </input>
                                        </xsl:when>
                                        <!-- when it is drop down -->
                                        <xsl:otherwise>
                                                <select name="BIOSPECIMEN_dtExtractedDate_Day" tabindex="50" class="uportal-input-text">
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
                                                        <select name="BIOSPECIMEN_dtExtractedDate_Month" tabindex="52" class="uportal-input-text">
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
                                    <input type="text" name="BIOSPECIMEN_dtExtractedDate_Year" size="4" tabindex="54" class="uportal-input-text">
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
                
                
                
                
	</table>
        <!-- table width="100%">
            <tr>
                <td width="25%"></td>
                <td width="3%"><input type="checkbox" name="excess" value="value"></input></td>
                <td width="10%" id="neuragenix-form-row-label" class="uportal-label">Excess only</td>
                <td width="62%"></td>
            </tr>
	</table -->

        <table>
            <!--=tr><td><br/></td></tr-->
            <tr><td><br/></td></tr>
        </table>        
        
        
	<table width="100%">
			<tr>
				<td width="22%"></td>
				<td width="5%" class="uportal-label">
					<input type="button" name="clear" value="Clear" class="uportal-button"  tabindex="70" onclick="javascript:confirmClear('{$baseActionURL}?{$formParams}')" />
				</td>
                                <td width="5%">&#160;</td>
				<td width="5%" class="uportal-label">
                                	<input type="hidden" name="module" value="BIOSPECIMEN_SEARCH" />
                                        <input type="submit" name="submit" value="Search" class="uportal-button"  tabindex="60"/>
				</td>
				<td width="63%"></td>
			</tr>

	</table>
      	</form>
        
        <!-- make the cursor start at the first data entry input-->
        <script language="javascript">
            document.biospecimen_search.BIOSPECIMEN_strBiospecimenID.focus();
        </script>
        
        
  </xsl:template>


       
 
</xsl:stylesheet>
