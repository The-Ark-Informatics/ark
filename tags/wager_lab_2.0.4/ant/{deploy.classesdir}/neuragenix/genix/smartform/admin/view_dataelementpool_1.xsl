<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : view_dataelementpool.xsl
    Copyright ï¿½ 2004 Neuragenix, Inc .  All rights reserved.
    Created on : 14 April 2004    
    Author     : Anita Balraj
    
    Modified on: 
    Author     : 
    Description: 
             
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./smartform_menu.xsl"/>
<xsl:include href="../../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=view_dataelementpool</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>    
  
    <xsl:template match="smartform">
    
    <xsl:param name="DATAELEMENTPOOL_intDataElementPoolID"><xsl:value-of select="DATAELEMENTPOOL_intDataElementPoolID" /></xsl:param>
    <xsl:param name="DATAELEMENTPOOL_strDataElementPoolName"><xsl:value-of select="DATAELEMENTPOOL_strDataElementPoolName" /></xsl:param>
    <xsl:param name="DATAELEMENTPOOL_strDataElementPoolDesc"><xsl:value-of select="DATAELEMENTPOOL_strDataElementPoolDesc" /></xsl:param>
    <xsl:param name="strErrorMessage"><xsl:value-of select="strErrorMessage" /></xsl:param>
    <xsl:param name="SMARTFORM_intSmartformID"><xsl:value-of select="SMARTFORM_intSmartformID" /></xsl:param>
    <xsl:param name="DEPoolSelected"><xsl:value-of select="DEPoolSelected" /></xsl:param>   
    <xsl:param name="DATAELEMENTS_intDataElementIDselected"><xsl:value-of select="DATAELEMENTS_intDataElementIDselected" /></xsl:param>        
    
    
    <form action="{$baseActionURL}?{$formParams}" method="post">
 
    <table width="100%">
        <tr>           
            
            <td width="29%" class="uportal-label">
                <input type="submit" name="addSmartform" tabindex="6" value="{$addBtnLabel}" class="uportal-button" />
                <input type="submit" name="searchSmartform" tabindex="7" value="{$searchBtnLabel}" class="uportal-button" />
            </td>
            
            <td width="66%" class="uportal-label">  
                <span class="uportal-channel-current-subtitle">View DataElementPool</span>
                <!--<a href="{$baseActionURL}?current=dataelement">DataElement |</a>
                <a href="{$baseActionURL}?current=option"> Option</a>         -->
            </td>
            
            <td width="5" class="uportal-label" align="right">
                <input type="submit" name="backtoAddEditDataElements" tabindex= "8" value="{$backBtnLabel}" class="uportal-button" />
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
                            <xsl:value-of select="DATAELEMENTPOOL_intDataElementPoolIDDisplay" />:
                        </td>               
			<td width ="80%" class="uportal-text">				
				<xsl:if test="$DATAELEMENTPOOL_intDataElementPoolID != ''">
					<xsl:value-of select="DATAELEMENTPOOL_intDataElementPoolID" />
				</xsl:if>
			</td>              
                    </tr>                     
                    <tr>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTPOOL_strDataElementPoolNameDisplay" />:
                        </td>
                        <td width="80%">
                            <input type="text" name="DATAELEMENTPOOL_strDataElementPoolName" value="{$DATAELEMENTPOOL_strDataElementPoolName}" size="41" tabindex="1" class="uportal-input-text" />                            
                        </td>
                    </tr>
                    <tr>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="DATAELEMENTPOOL_strDataElementPoolDescDisplay" />:
                        </td>
                        <td width="80%">
                            <textarea name="DATAELEMENTPOOL_strDataElementPoolDesc" rows="4" cols="40" tabindex="2" class="uportal-input-text">
                                <xsl:value-of select="DATAELEMENTPOOL_strDataElementPoolDesc" />
                            </textarea>                            
                        </td>
                    </tr>
                    
               </table>
                    
               <table width="100%">
                    <tr>	
                        <td width="20%">
                        
                        <input type="reset" name="reset" value="Reset" tabindex="3" class="uportal-button" />                           
                        <input type="submit" name="updateDataElementPool" tabindex="4" value="{$updateBtnLabel}" class="uportal-button" />
                        <input type="button" name="deleteDataElementPool" tabindex="5" value="{$deleteBtnLabel}" class="uportal-button" onclick="javascript:confirmDeleteDataElementPool('{$baseActionURL}?current=view_dataelementpool&amp;DATAELEMENTPOOL_intDataElementPoolID={$DATAELEMENTPOOL_intDataElementPoolID}&amp;DATAELEMENTPOOL_strDataElementPoolName={$DATAELEMENTPOOL_strDataElementPoolName}&amp;DATAELEMENTPOOL_strDataElementPoolDesc={$DATAELEMENTPOOL_strDataElementPoolDesc}&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}')"/>
                        
                        </td>                 
                        <td width="80%"></td>  
                     </tr>                                                                  
                </table>
             </td>
            <td width="8%"></td>           
        </tr>
    </table>
    
    <!-- Hidden Fields -->    
    <input type="hidden" name="DATAELEMENTPOOL_intDataElementPoolID" value="{$DATAELEMENTPOOL_intDataElementPoolID}" />
    
    <input type="hidden" name="strErrorMessage" value="{$strErrorMessage}" />
    <input type="hidden" name="SMARTFORM_intSmartformID" value="{$SMARTFORM_intSmartformID}" />
    <input type="hidden" name="DEPoolSelected" value="{$DEPoolSelected}" />  
    <input type="hidden" name="DATAELEMENTS_intDataElementIDselected" value="{$DATAELEMENTS_intDataElementIDselected}" />    
    
    </form>
    </xsl:template>

</xsl:stylesheet>
