<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : add_optionpool.xsl
    Copyright ï¿½ 2004 Neuragenix, Inc .  All rights reserved.
    Created on : 16 April 2004    
    Author     : Anita Balraj
    
    Modified on: 
    Author     : 
    Description: 
             
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./smartform_menu.xsl"/>
<xsl:include href="../../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=add_optionpool</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>    
  
    <xsl:template match="smartform">
    <xsl:param name="OPTIONPOOL_intOptionPoolID"><xsl:value-of select="OPTIONPOOL_intOptionPoolID" /></xsl:param>  
    <xsl:param name="OPTIONPOOL_strOptionPoolName"><xsl:value-of select="OPTIONPOOL_strOptionPoolName" /></xsl:param>
    <xsl:param name="OptionPoolSelected"><xsl:value-of select="OptionPoolSelected" /></xsl:param>
    <xsl:param name="OPTIONS_intOptionIDSelected"><xsl:value-of select="OPTIONS_intOptionIDSelected" /></xsl:param>            
    <xsl:param name="strErrorMessage"><xsl:value-of select="strErrorMessage" /></xsl:param>
    <xsl:param name="SMARTFORM_intSmartformID"><xsl:value-of select="SMARTFORM_intSmartformID" /></xsl:param>
    <xsl:param name="DEPoolSelected"><xsl:value-of select="DEPoolSelected" /></xsl:param>   
    <xsl:param name="DATAELEMENTS_intDataElementIDselected"><xsl:value-of select="DATAELEMENTS_intDataElementIDselected" /></xsl:param>        
    
    
    <form action="{$baseActionURL}?{$formParams}" method="post">
 
    <table width="100%">
        <tr>           
            
            <td width="29%" class="uportal-label">
                <input type="submit" name="addSmartform" tabindex="5" value="{$addBtnLabel}" class="uportal-button" />
                <input type="submit" name="searchSmartform" tabindex="6" value="{$searchBtnLabel}" class="uportal-button" />
            </td>
            
            <td width="66%" class="uportal-label">   
                <span class="uportal-channel-current-subtitle">Add OptionPool</span>
                <!--<a href="{$baseActionURL}?current=dataelement">DataElement |</a>
                <a href="{$baseActionURL}?current=option"> Option</a>         -->
            </td>
            
            <td width="5" class="uportal-label" align="right">
                <input type="submit" name="backtoAddEditOptions" tabindex= "7" value="{$backBtnLabel}" class="uportal-button" />
            </td>
        </tr>        
    </table>
    <hr/>   
    
    
    <table width="115%">
        <tr valign="top">
        
            <td width="25%"></td>
            <td width="77%">
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
                            <xsl:value-of select="OPTIONPOOL_intOptionPoolIDDisplay" />:
                        </td>                  
                        <td width ="80%" class="uportal-text">				
			    System generated                
                        </td>                                       
                    </tr>                     
                    <tr>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="OPTIONPOOL_strOptionPoolNameDisplay" />:
                        </td>
                        <td width="80%">
                            <input type="text" name="OPTIONPOOL_strOptionPoolName" size="41" tabindex="1" class="uportal-input-text" value="{$OPTIONPOOL_strOptionPoolName}" />
                            
                        </td>
                    </tr>
                    <tr>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="OPTIONPOOL_strOptionPoolDescDisplay" />:
                        </td>
                        <td width="80%">
                            <textarea name="OPTIONPOOL_strOptionPoolDesc" rows="4" cols="40" tabindex="2" class="uportal-input-text">
                                <xsl:value-of select="OPTIONPOOL_strOptionPoolDesc" />
                            </textarea>                            
                        </td>
                    </tr>
                   
               </table>
                    
               <table width="100%">
                    <tr>	
                        <td width="20%">                        
                        <input type="reset" name="reset" value="Reset" tabindex="3" class="uportal-button" />                           
                        <input type="submit" name="saveNewOptionPool" tabindex="4" value="{$saveBtnLabel}" class="uportal-button" />                        
                        </td>                 
                        <td width="80%"></td>  
                     </tr>                                                                  
                </table>
             </td>
            <td width="8%"></td>           
        </tr>
    </table> 
    
    <!-- Hidden Fields -->    
    <input type="hidden" name="OPTIONPOOL_intOptionPoolID" value="{$OPTIONPOOL_intOptionPoolID}" />    
   
    
    <input type="hidden" name="strErrorMessage" value="{$strErrorMessage}" />
    <input type="hidden" name="SMARTFORM_intSmartformID" value="{$SMARTFORM_intSmartformID}" />
    <input type="hidden" name="DEPoolSelected" value="{$DEPoolSelected}" />  
    <br></br>
    <input type="hidden" name="DATAELEMENTS_intDataElementIDselected" value="{$DATAELEMENTS_intDataElementIDselected}" />    
    <input type="hidden" name="OptionPoolSelected" value="{$OptionPoolSelected}" />
    <br></br>
    <input type="hidden" name="OPTIONS_intOptionIDSelected" value="{$OPTIONS_intOptionIDSelected}" />
    </form>
    </xsl:template>

</xsl:stylesheet>
