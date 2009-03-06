<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./header.xsl"/>
    <xsl:include href="./common_javascript.xsl"/>
        
    <xsl:template match="body">     
        <xsl:variable name="backLocation"><xsl:value-of select="backLocation"/></xsl:variable>
        <xsl:call-template name="common_javascript"/>       
        <script language="javascript" >

            function validateSpecimens(aURL)
            {
                var biospecimen = document.getElementById('BIOSPECIMEN_strBiospecimenID').value;
                
                // If biospecimens have been selected can perform smartform generation
                if (biospecimen != "")
                {
                    window.location=aURL;
                }
                else
                {
                    alert ('Please enter a biospecimen ID.');
                }
            }
            
            function selectSmarform(aURL)
            {
                var SmartformID = document.getElementById("Smartform").value;
                window.location=aURL + '&amp;selectSmartform=true&amp;Smartform=' + SmartformID;
            }
            
            
        </script>    
        <xsl:call-template name="header">
                <xsl:with-param name="activeSubtab">specimens</xsl:with-param>
                <xsl:with-param name="previousButtonUrl"><xsl:value-of select="$baseActionURL"/>?module=BATCH_SFRESULTS_GENERATION&amp;action=generation_mode</xsl:with-param>
                <xsl:with-param name="previousButtonFlag">true</xsl:with-param>
                
        </xsl:call-template> 
        <xsl:variable name="varSelectedSF">
                    <xsl:value-of select="selectedSmartformID"/>
        </xsl:variable>                    
        <xsl:variable name="varSelectedSFName">
                    <xsl:value-of select="selectedSmartformName"/>
        </xsl:variable>          
        <xsl:variable name="singlePartGeneration">          
            <xsl:value-of select="singlePartGeneration"/>
        </xsl:variable>
        <xsl:variable name="DisableAddParticipant">          
            <xsl:value-of select="DisableAddParticipant"/>
        </xsl:variable>
        
        <table width="100%">       
        <tr>
            <td class="neuragenix-form-required-text">
                <xsl:value-of select="ErrorMsg"/>
            </td>
        </tr>            
        </table>

        <form action="{$baseActionURL}?module=BATCH_SFRESULTS_GENERATION&amp;action=specimen_selection" method="post">
        <table width="100%">
            <tr>
                <td class="uportal-label">
                    1. Please select your Specimens
                </td>
            </tr>            
        </table>
        <br/>
        <table width="100%">
            <tr>
                <td width="5%"></td>
                <td width="1%" class="neuragenix-form-required-text">*</td>
                <td width="12%" class="uportal-label">Smartforms</td>
                <td width="20%">
                    <select id="Smartform" name="Smartform" tabindex="1" class="uportal-input-text" onchange="javascript:selectSmarform('{$baseActionURL}?module=BATCH_SFRESULTS_GENERATION&amp;action=specimen_selection')">
                        <xsl:for-each select="smartform_list">
                        <xsl:variable name="varSFID">
                                <xsl:value-of select="SMARTFORM_intSmartformID"/>
                        </xsl:variable>
                        <option>
                            <xsl:attribute name="value">
                              <xsl:value-of select="SMARTFORM_intSmartformID"/>
                            </xsl:attribute>
                            <xsl:if test="$varSelectedSF=$varSFID">
                              <xsl:attribute name="selected"
                              >true</xsl:attribute>
                            </xsl:if>
                            <xsl:value-of select="SMARTFORM_strSmartformName"/>
                        </option>                        
                        </xsl:for-each>
                    </select>
                
                </td>
                <td width="40%">                                                                   
                </td>
                <td width="5%"></td>
                <td width="15%">
                </td>                
            </tr>
            <tr>
                <td width="5%"></td>
                <td width="1%" class="neuragenix-form-required-text">*</td>
                <td width="12%" class="uportal-label">Biospecimens</td>
                <td width="20%">
                </td>
                <td  width="40%">                                                                   
                </td>
                <td width="5%"></td>
                <td width="15%">
                </td>                
            </tr>            
         </table> 
         
             <table width="100%" border="0" cellspacing="0" cellpadding="2">
                 <!-- Column Heading Row -->
                 <tr>					
                        <td width="19%"></td>
                        <td width="15%" class="stripped_column_heading">
                            Biospecimen ID
                        </td>
                        <td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
                        <td width="15%" class="stripped_column_heading">
                            Type
                        </td>
                        <td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
                        <td width="15%" class="stripped_column_heading">
                            Sub-Type
                        </td>
                        <td>
                        </td>
                 </tr>  

                
                <xsl:variable name="selectedBio">
                   <xsl:value-of select="biospecimen_list/BIOSPECIMEN_strBiospecimenID"/>
                </xsl:variable>  
                <input type="hidden" id="BIOSPECIMEN_strBiospecimenID" name="SelectedBiospecimen" value="{$selectedBio}" class="uportal-button"/> 
                 
                 <!-- Selected Biospecimens --> 
                 <xsl:for-each select="biospecimen_list">
                    <xsl:variable name="BIOSPECIMEN_intBiospecimenID">
                       <xsl:value-of select="BIOSPECIMEN_intBiospecimenID"/>
                    </xsl:variable>  
                    
                    <tr>
                            <td width="19%"></td>
                            <td width="15%">
                                    <xsl:choose>
                                            <xsl:when test="position() mod 2 != 0">
                                                    <xsl:attribute name="class">stripped_light</xsl:attribute>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                    <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                            </xsl:otherwise>
                                    </xsl:choose>                                    
                                    <xsl:value-of select="BIOSPECIMEN_strBiospecimenID"/>                                    
                            </td>
                            <td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
                            <td width="15%">
                                    <xsl:choose>
                                            <xsl:when test="position() mod 2 != 0">
                                                    <xsl:attribute name="class">stripped_light</xsl:attribute>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                    <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                            </xsl:otherwise>
                                    </xsl:choose>
                                    <xsl:value-of select="BIOSPECIMEN_strSampleType"/> 
                            </td>
                            <td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
                            <td width="15%">                                    
                                    <xsl:choose>
                                            <xsl:when test="position() mod 2 != 0">
                                                    <xsl:attribute name="class">stripped_light</xsl:attribute>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                    <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                            </xsl:otherwise>
                                    </xsl:choose>
                                    <xsl:value-of select="BIOSPECIMEN_strSampleSubType"/>
                            </td>
                            <td>
                                <a href="{$baseActionURL}?module=BATCH_SFRESULTS_GENERATION&amp;action=specimen_selection&amp;Remove=true&amp;BIOSPECIMEN_intBiospecimenID={$BIOSPECIMEN_intBiospecimenID}">remove</a>                                
                            </td>                            
                    </tr>
             </xsl:for-each>                 
         </table>                                
         
         <br/>
         
         <xsl:if test="(string-length( $DisableAddParticipant ) = 0)">
         <table width="100%">
            <tr>
                <td width="5%"></td>
                <td width="1%" class="neuragenix-form-required-text"></td>
                <td width="9%" class="uportal-label">Enter Biospecimen ID:</td>
                <td align="left" width="10%">
                    <input type="text" align="left" name="BIOSPECIMEN_strBiospecimenID" tabindex="4" class="uportal-input-text"/>
                </td>
                <td  width="37%" align="left">
                    <input type="submit" align="left" name="Save" value="Save" tabindex="7" class="uportal-button"/>                                                                   
                </td>
                <td width="5%"></td>
                <td width="15%">
                </td>                
            </tr>            
         
         </table>
         </xsl:if>
        
        <br/>
        <hr/>
        
        <table width="100%">            
            <tr>
                <td align="left">
                        <input type="button" name="Cancel" value="Cancel" tabindex="15" class="uportal-button" onclick="javascript:jumpTo('{$baseActionURL}{backLocation}')"/>        
                </td>                                
                <xsl:choose>
                <xsl:when test="(string-length( $singlePartGeneration ) > 0)">
                <td align="right">
                        <input type="button" name="Next" value="Next" tabindex="9" class="uportal-button" onclick="javascript:validateSpecimens('{$baseActionURL}?module=BATCH_SFRESULTS_GENERATION&amp;action=SelectSFFields')" />        
                </td>
                </xsl:when>
                <xsl:otherwise>
                <td align="right">
                    <xsl:variable name="fromBiospecimenKey"><xsl:value-of select="fromBiospecimenKey"/></xsl:variable>    
                    <input type="button" name="Next" value="Next" tabindex="9" class="uportal-button" onclick="javascript:validateSpecimens('{$smartformChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformChannelTabOrder}&amp;current=smartform_result_view&amp;fromSFBatchGeneration=true&amp;SFBatchGenerationFirstPage=true&amp;domain=Bioanalysis&amp;SMARTFORM_smartformname={$varSelectedSFName}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSelectedSF}&amp;SMARTFORMPARTICIPANTS_intCurrentPage=1&amp;fromBiospecimenKey={$fromBiospecimenKey}')" />        
                </td>
                </xsl:otherwise>
                </xsl:choose>
            </tr>
        </table>
   
     </form>  
               
    </xsl:template>
</xsl:stylesheet>
