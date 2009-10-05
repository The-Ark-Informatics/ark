<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : search_smartform.xsl
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

    <xsl:param name="formParams">current=search_smartform</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    
  
    <xsl:template match="smartform">    
    <xsl:param name="strErrorMessage"><xsl:value-of select="strErrorMessage" /></xsl:param>
    
    <form action="{$baseActionURL}?{$formParams}" method="post">
 
    <table width="100%">
        <tr>           
            
            <td width="25%" class="uportal-label">
                <input type="submit" name="addSmartform" tabindex="4" value="{$addBtnLabel}" class="uportal-button" />
                <input type="submit" name="searchSmartform" tabindex="5" value="{$searchBtnLabel}" class="uportal-button" />
            </td>
            
            <td width="75%" class="uportal-label"> 
                <span class="uportal-channel-current-subtitle">Search Smartform </span> 
                <!--<a href="{$baseActionURL}?current=dataelement">DataElement |</a>
                <a href="{$baseActionURL}?current=option"> Option</a>-->
                <!-- <span class="uportal-channel-current-subtitle"> Search task </span> 
                <a href="{$baseActionURL}?current=reassign_tasks">| Reassign all tasks |</a> -->               
                
            </td>
        </tr>        
    </table>
    <hr/>
    
    
    
    <table width="100%">
        <tr valign="top">
            <td width="25%">

                <table width="100%">                   
                    <tr>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="SMARTFORM_strSmartformNameDisplay" />:
                        </td>
                        <td width="80%">
                            <input type="text" name="SMARTFORM_strSmartformName" size="20" tabindex="1" class="uportal-input-text" />
                        </td>
                    </tr>             
                </table>
                
                <table width="90%">
                    <tr><td><hr /></td></tr>
                </table>
                
                <table width="100%">
                    <tr>
                        <td>
                            <input type="submit" name="submitSearchSmartform" tabindex="2" value="{$submitBtnLabel}" class="uportal-button" />
                            <input type="button" name="clear" value="{$clearBtnLabel}" tabindex="3" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=search_smartform')" />
                        </td>
                    </tr>
                </table>
            </td>
            
            <td width="75%">
                
                <table width="100%">
                     <tr>
                        <td colspan="2" class="neuragenix-form-required-text">
                            <xsl:value-of select="strErrorMessage" />
                        </td>
                    </tr>
                    <tr>                                        
                        
                        <td width="20%" class="uportal-channel-table-header">
                            <xsl:value-of select="SMARTFORM_strSmartformNameDisplay" />
                        </td>
                        
                        <td width="80%" class="uportal-channel-table-header">
                            <xsl:value-of select="SMARTFORM_strSmartformDescriptionDisplay" />
                        </td>                      
                        
                     </tr>
                </table>
                <hr/>
                    
                <table width="100%">
                    <xsl:for-each select="search_smartform">             
                    <xsl:variable name="SMARTFORM_intSmartformID"><xsl:value-of select="SMARTFORM_intSmartformID" /></xsl:variable>
                    
                    <tr>                    
                        
                        <td width="20%" class="uportal-label"> 
                            <!-- <a href="{$baseActionURL}?current=view_smartform&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;SMARTFORM_strSmartformName={SMARTFORM_strSmartformName}&amp;SMARTFORM_strSmartformDescription={SMARTFORM_strSmartformDescription}&amp;SMARTFORM_strDomain={SMARTFORM_strDomain}"> -->
                            <a href="{$baseActionURL}?current=view_smartform&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;displaySmartform=true">
                                <xsl:value-of select="SMARTFORM_strSmartformName" />
                            </a>
                        </td>
                        
                        <td width="80%" class="uportal-label">
                            <a href="{$baseActionURL}?current=view_smartform&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;displaySmartform=true">
                                <xsl:value-of select="SMARTFORM_strSmartformDescription" />
                            </a>
                        </td>                        
                        
                    </tr>
                    </xsl:for-each>
                </table>
            </td>
        </tr>
    </table>
    
    <!-- Hidden Fields -->    
    <input type="hidden" name="strErrorMessage" value="{$strErrorMessage}" />
        
    </form>
    </xsl:template>

</xsl:stylesheet>
