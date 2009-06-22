<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : add_smartform.xsl
    Copyright ï¿½ 2004 Neuragenix, Inc .  All rights reserved.
    Created on : 4 March 2004    
    Author     : Anita Balraj
    
    Modified on: 
    Author     : 
    Description: 
             
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./smartform_menu.xsl"/>
<xsl:include href="../../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=add_smartform</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>    
  
    <xsl:template match="smartform">
    <xsl:param name="SMARTFORM_intSmartformID"><xsl:value-of select="SMARTFORM_intSmartformID" /></xsl:param>  
    <xsl:param name="SMARTFORM_strSmartformName"><xsl:value-of select="SMARTFORM_strSmartformName" /></xsl:param>
    
    <form action="{$baseActionURL}?{$formParams}" method="post">
 
    <table width="100%">
        <tr>           
            
            <td width="32%" class="uportal-label">
                <input type="submit" name="addSmartform" tabindex="7" value="{$addBtnLabel}" class="uportal-button" />
                <input type="submit" name="searchSmartform" tabindex="8" value="{$searchBtnLabel}" class="uportal-button" />
            </td>
            
            <td width="68%" class="uportal-label">                
                <span class="uportal-channel-current-subtitle">Add Smartform</span>
                <!--<a href="{$baseActionURL}?current=dataelement">DataElement |</a>
                <a href="{$baseActionURL}?current=option"> Option</a>         -->
            </td>
        </tr>        
    </table>
    <hr/>   
    
    
    <table width="100%">
        <tr valign="top">
        
            <td width="32%"></td>
            <td width="50%">
                <table width="100%">    
                    <tr>
                        <td class="neuragenix-form-required-text">
                            <xsl:value-of select="strErrorMessage" />
                        </td>
                    </tr>
                </table>
                <table width="100%">
                    <tr>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="SMARTFORM_intSmartformIDDisplay" />:
                        </td>                  
                        <td width ="80%" class="uportal-text">				
			    System generated                
                        </td>                                       
                    </tr>                     
                    <tr>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="SMARTFORM_strSmartformNameDisplay" />:
                        </td>
                        <td width="80%">
                            <input type="text" name="SMARTFORM_strSmartformName" size="41" tabindex="1" class="uportal-input-text" value="{$SMARTFORM_strSmartformName}" />
                            <!-- <xsl:value-of select="SMARTFORM_strSmartformName" /> -->
                        </td>
                    </tr>
                    <tr>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="SMARTFORM_strSmartformDescriptionDisplay" />:
                        </td>
                        <td width="80%">
                            <textarea name="SMARTFORM_strSmartformDescription" rows="4" cols="40" tabindex="2" class="uportal-input-text">
                                <xsl:value-of select="SMARTFORM_strSmartformDescription" />
                            </textarea>                            
                        </td>
                    </tr>
                    <tr>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="SMARTFORM_strDomainDisplay" />:
                        </td>
                        <td width="80%">
                        
                            <select name="SMARTFORM_strDomain" tabindex="3" class="uportal-input-text">
				<xsl:for-each select="SMARTFORM_strDomain">		
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
                    </tr>
                    <!-- Sensitivity -->
                    <tr>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="SMARTFORM_strSensitivityDisplay" />:
                        </td>
                        <td width="80%">
                        
                            <select name="SMARTFORM_strSensitivity" tabindex="4" class="uportal-input-text">
				<xsl:for-each select="SMARTFORM_strSensitivity">		
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
                    </tr>
                    <!-- Sensitivity -->
               </table>
                    
               <table width="100%">
                    <tr>	
                        <td width="20%">
                        <!-- <input type="button" name="clear" value="{$clearBtnLabel}" tabindex="6" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=add_smartform')" />   -->
                        <input type="reset" name="reset" value="Reset" tabindex="5" class="uportal-button" />                           
                        <input type="submit" name="saveNewSmartform" tabindex="6" value="{$saveBtnLabel}" class="uportal-button" />
                        <!-- <input type="button" name="deleteNewSmartform" tabindex="8" value="{$deleteBtnLabel}" class="uportal-button" onclick="javascript:confirmDelete('{$baseActionURL}?current=add_smartform')"/> -->

                        <!-- <input type="submit" name="buildsmartform" tabindex="1" value="{$buildsmartform}" class="uportal-button" /> -->
                        </td>                 
                        <td width="80%"></td>  
                     </tr>                                                                  
                </table>
             </td>
           <td width="18%"></td>          
        </tr>
    </table> 
    
    <!-- Hidden Fields -->    
    <input type="hidden" name="SMARTFORM_intSmartformID" value="{$SMARTFORM_intSmartformID}" />    
   
    </form>
    </xsl:template>

</xsl:stylesheet>
