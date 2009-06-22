<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : addedit_options.xsl
    Copyright ? 2004 Neuragenix, Inc .  All rights reserved.
    Created on : 14 April 2004    
    Author     : Anita Balraj
    
    Modified on: 
    Author     : 
    Description: 
             
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./smartform_menu.xsl"/>
<xsl:include href="../../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=addedit_options</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
      
    <xsl:template match="smartform">    
    <xsl:param name="strErrorMessage"><xsl:value-of select="strErrorMessage" /></xsl:param>
    <xsl:param name="SMARTFORM_intSmartformID"><xsl:value-of select="SMARTFORM_intSmartformID" /></xsl:param>    
    <xsl:param name="DEPoolSelected"><xsl:value-of select="DEPoolSelected" /></xsl:param>    
    <xsl:param name="DATAELEMENTS_intDataElementIDselected"><xsl:value-of select="DATAELEMENTS_intDataElementIDselected" /></xsl:param>        
          
    <!--  **************************-->
    <xsl:param name="OPTIONPOOL_intOptionPoolID"><xsl:value-of select="OPTIONPOOL_intOptionPoolID" /></xsl:param>
    <xsl:param name="OptionPoolSelected"><xsl:value-of select="OptionPoolSelected" /></xsl:param>    
    <xsl:param name="OPTIONS_intOptionID"><xsl:value-of select="OPTIONS_intOptionID" /></xsl:param>
    <xsl:param name="OPTIONS_intOptionIDSelected"><xsl:value-of select="OPTIONS_intOptionIDSelected" /></xsl:param>
    <xsl:param name="OPTIONS_strOptionLabel"><xsl:value-of select="OPTIONS_strOptionLabel" /></xsl:param>
    <xsl:param name="OPTIONS_strOptionValue"><xsl:value-of select="OPTIONS_strOptionValue" /></xsl:param>
    <xsl:param name="addClicked"><xsl:value-of select="addClicked" /></xsl:param> 
    <xsl:param name="addOption"><xsl:value-of select="addOption" /></xsl:param>   
    
          
    <form name="frmaddedit_options" action="{$baseActionURL}?{$formParams}" method="post">
   
    <table width="100%">
        <tr>           
            
            <td width="15%" class="uportal-label">
                <input type="submit" name="addSmartform" tabindex="8" value="{$addBtnLabel}" class="uportal-button" />
                <input type="submit" name="searchSmartform" tabindex="9" value="{$searchBtnLabel}" class="uportal-button" />
            </td>
            
            <td width="80%" class="uportal-label">   
                        <span class="uportal-channel-current-subtitle"> Add/Edit Options</span>
                <!--<a href="{$baseActionURL}?current=dataelement">DataElement |</a>
                <a href="{$baseActionURL}?current=option"> Option</a>   -->
            </td>
            
            <td width="5" class="uportal-label" align="right">
                <input type="submit" name="backtoAddEditDataElements" tabindex= "10" value="{$backBtnLabel}" class="uportal-button" />
            </td>
        </tr>        
    </table>
    <hr/>   
    
    
    <table width="100%">
        <tr valign="top">
            <td width="15%">
                            
            </td>
                
            <td width="70%">
                
                <table width="100%">                    
                     <tr>
                        <td colspan="2" class="neuragenix-form-required-text">
                            <xsl:value-of select="strErrorMessage" />
                        </td>
                    </tr>                 
                 </table>
                 
                 <table width="100%">
                    <tr>
                        <td width="30%" class="uportal-label">
                            Select the Option Pool:
                        </td>
                        <td width="50%">                        
                             <select name="OPTIONPOOL_intOptionPoolID" tabindex="1" class="uportal-input-text" onChange="document.frmaddedit_options.submit();">
                             
                             <option value="null">--- Select ---</option>  
                                                                  
                            <xsl:for-each select="addEditOptions_loadOptionPool">
                                <xsl:variable name="OPTIONPOOL_intOptionPoolID"><xsl:value-of select="OPTIONPOOL_intOptionPoolID" /></xsl:variable>
                                <option>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="OPTIONPOOL_intOptionPoolID" />
                                        </xsl:attribute>                  
                                                    
                                        <xsl:if test="$OPTIONPOOL_intOptionPoolID=$OptionPoolSelected">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                                            
                                        <xsl:value-of select="OPTIONPOOL_strOptionPoolName" /> 
                                 </option>                    
                                    
                            </xsl:for-each>
                            </select>
                        </td>
                        <td width="20%" class="uportal-text">                        
                            <a href="{$baseActionURL}?current=view_optionpool&amp;OPTIONPOOL_intOptionPoolID={$OptionPoolSelected}&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}&amp;OptionPoolSelected={$OptionPoolSelected}&amp;OPTIONS_intOptionIDSelected={$OPTIONS_intOptionIDSelected}" >Edit Option Pool</a><br></br>
                            <a href="{$baseActionURL}?current=add_optionpool&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}&amp;OptionPoolSelected={$OptionPoolSelected}&amp;OPTIONS_intOptionIDSelected={$OPTIONS_intOptionIDSelected}" >Add Option Pool</a>
                        </td>
                    </tr> 
                       
                    <tr></tr>
                    
                </table>
                  <hr/>
                 <table width="100%">
		     <tr >			
			<td width="30%" class="uportal-label">
				Options:
			</td>
			
			<td width="50%"  align="left" class="uportal-label" id="neuragenix-form-row-input-input">
				<SELECT readonly = "true" Name="OPTIONS_intOptionID" Size="5" tabindex="2" class="uportal-input-text" onChange="document.frmaddedit_options.submit();">
                                    <xsl:for-each select="addEditOptions_loadOptions">
					<xsl:variable name="OPTIONS_intOptionID"><xsl:value-of select="OPTIONS_intOptionID" /></xsl:variable>
					<xsl:variable name="DATAELEMENTS_intDataElementType"><xsl:value-of select="DATAELEMENTS_intDataElementType" /></xsl:variable>
                                        <xsl:variable name="DATAELEMENTS_strDataElementName"><xsl:value-of select="DATAELEMENTS_strDataElementName" /></xsl:variable>

						<option>
							<xsl:if test="$OPTIONS_intOptionID = $OPTIONS_intOptionIDSelected">
								<xsl:attribute name="selected">true</xsl:attribute> 
							</xsl:if>
							<xsl:attribute name="value"><xsl:value-of select="OPTIONS_intOptionID" /></xsl:attribute>
                                                                                                                
                                                        Option: <xsl:value-of select="OPTIONS_strOptionLabel" />	
						</option>
                                                
                                    </xsl:for-each>
				</SELECT>
			</td>
			<td width="20%"  align="left">
                        
                          <xsl:if test="$OPTIONS_intOptionIDSelected != 'null'"> 
				<table border="0">                                
                                    <tr>
                                        <td align="left" class="uportal-text">
                                        
                                            <a href="{$baseActionURL}?current=addedit_options&amp;moveUp=true&amp;OPTIONS_intOptionID={$OPTIONS_intOptionID}&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}&amp;OptionPoolSelected={$OptionPoolSelected}">     
                                                 move up                                                     
                                           </a> 
                                            
					</td>
                                        <td align="left">                                        
                                        </td>
                                    </tr>                                    
                                    <tr>
                                        <td align="left" class="uportal-text">
                                            <!-- <a href="{$baseActionURL}?current=build_smartform&amp;deleteSFDE=true&amp;SMARTFORMTODATAELEMENTS_DEIdSelected={$SMARTFORMTODATAELEMENTS_DEIdSelected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}"> -->
                                            <a href="#" onclick="javascript:confirmDeleteOption('{$baseActionURL}?current=addedit_options&amp;deleteOption=true&amp;OPTIONS_intOptionID={$OPTIONS_intOptionID}&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}&amp;OptionPoolSelected={$OptionPoolSelected}')">                                        
                                                    delete
                                            </a> 
                                        </td>
                                        <td align="left">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="left" class="uportal-text">
                                            <a href="{$baseActionURL}?current=addedit_options&amp;moveDown=true&amp;OPTIONS_intOptionID={$OPTIONS_intOptionID}&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}&amp;OptionPoolSelected={$OptionPoolSelected}">                                           
                                                    move down
                                            </a>
                                        </td>
                                    </tr>
				</table>
                          </xsl:if>
			</td>
                    </tr>
                    
                    <tr></tr>	
                </table>              
                
                <xsl:if test="$OptionPoolSelected != 'null'">
                    <table width="100%">
                        <tr>
                            <td>
                                <input type="submit" name="addOption" tabindex="3" value="Add Option" class="uportal-button" />                           
                            </td>
                        </tr>
                    </table>
                </xsl:if>
                
                
                
              <!-- #############################   ADD OPTIONS ########################################################## -->             
             <!--<xsl:if test="$DATAELEMENTS_intDataElementType != 'PAGEBREAK' or $strMode != 'addPageBreak'"> 
                  <xsl:if test="$strMode = 'newTitleClicked' or $DATAELEMENTS_intDataElementType = 'TITLE' or $DATAELEMENTS_intDataElementType = 'COMMENT'">-->
             <xsl:if test="($OptionPoolSelected != 'null') or ($addClicked = 'yes')">
                  <hr/>
                  <table width="100%">
                        <tr>
                            <td width="100%" class="uportal-channel-table-header">
                                Add/Edit Options
                            </td>
                        </tr>
                        <tr></tr>                    
                  </table> 
                  <table width="100%">
                        <tr>
                            <td width="20%" class="uportal-label">
                                <xsl:value-of select="OPTIONS_intOptionIDDisplay" />:
                            </td>                  
                            <td width ="80%" class="uportal-text">
                                <xsl:choose>
                                <xsl:when test="$OPTIONS_intOptionIDSelected = 'null'"> 
                                             System generated    
                                </xsl:when>
                                <xsl:otherwise>
                                            <xsl:value-of select="OPTIONS_intOptionIDSelected" />
                                </xsl:otherwise>   
                                </xsl:choose>                                  
                            </td>                                       
                        </tr>                     
                        <tr>
                            <td width="20%" class="uportal-label">
                                <xsl:value-of select="OPTIONS_strOptionLabelDisplay" />:
                            </td>
                            <td width="80%">                    
                                <input type="text" name="OPTIONS_strOptionLabel" value="{$OPTIONS_strOptionLabel}" size="25" tabindex="4" class="uportal-input-text"/>                                                     
                            </td>
                        </tr>
                        <tr>
                            <td width="20%" class="uportal-label">
                               <xsl:value-of select="OPTIONS_strOptionValueDisplay" />:
                            </td>
                            <td width="80%">
                                 <input type="text" name="OPTIONS_strOptionValue" value="{$OPTIONS_strOptionValue}" size="25" tabindex="5" class="uportal-input-text"/>                                                   
                            </td>
                        </tr>                       
                        <tr>
                            <td width="20%">
                            </td>
                            <td width="80%">
                              <xsl:choose>
                              <xsl:when test="$OPTIONS_intOptionIDSelected = 'null'">
                                <input type="submit" name="saveOption" tabindex="6" value="{$saveBtnLabel}" class="uportal-button" /> 
                              </xsl:when>
                              <!--<xsl:if test="$OPTIONS_intOptionIDSelected = 'null'">-->
                              <xsl:otherwise>
                                <input type="submit" name="updateOption" tabindex="6" value="{$updateBtnLabel}" class="uportal-button" />                                                                                       
                              </xsl:otherwise>
                              </xsl:choose>
                              
                            </td>   
                        </tr>
                    </table>
                    
                 <!-- </xsl:if>
              </xsl:if> -->
              
              <xsl:if test="$DATAELEMENTS_intDataElementIDselected != 'null'">
                  <hr/>  
                  <table width="100%">
                        <tr>
                            <td width="100%">
                                 <input type="submit" name="LinkToDataElement" tabindex="7" value="Link To DataElement" class="uportal-button" />                                          
                            </td>
                        </tr>
                  </table>            
              </xsl:if>
             
             
                <!--    ************************************List DataElements *****************************> -->
                 <!--<xsl:if test="$DATAELEMENTS_intDataElementType != 'PAGEBREAK' or $strMode != 'addPageBreak'">
                    <xsl:if test="$DATAELEMENTS_intDataElementIDselected != 'null'">  -->
                    
                        <hr/>      
                        <table width="100%">
                            <tr>
                                <td width="100%" class="uportal-channel-table-header">
                                    List of DataElements using this Option Pool
                                </td>
                            </tr>
                        </table>
                       
                        <table width="100%">
                            <tr>                 
                                <td width="20%" class="uportal-channel-table-header">
                                   DataElement ID
                                </td>                        
                                <td width="80%" class="uportal-channel-table-header">
                                    DataElement Name
                                </td>                  
                             </tr>                 
                        </table>
                        <hr/>

                        <xsl:for-each select="list_of_dataelements">
                        <table width="100%">
                                                    
                             <xsl:variable name="DATAELEMENTS_intDataElementID"><xsl:value-of select="DATAELEMENTS_intDataElementID" /></xsl:variable> 
                             <xsl:variable name="DATAELEMENTS_intDataElementPoolID"><xsl:value-of select="DATAELEMENTS_intDataElementPoolID" /></xsl:variable> 
                             
                            <tr>                 
                                <td width="20%" class="uportal-label">                              
                                    <a href="{$baseActionURL}?current=addedit_dataelements&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementID}&amp;DEPoolSelected={$DATAELEMENTS_intDataElementPoolID}">
                                        <xsl:value-of select="DATAELEMENTS_intDataElementID" />
                                    </a>
                                </td>

                                <td width="80%" class="uportal-label">
                                    <a href="{$baseActionURL}?current=addedit_dataelements&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementID}&amp;DEPoolSelected={$DATAELEMENTS_intDataElementPoolID}">
                                        <xsl:value-of select="DATAELEMENTS_strDataElementName" />
                                    </a>
                                </td>                  
                            </tr>
                           
                        </table> 
                        </xsl:for-each>
                      </xsl:if>  
                    <!-- </xsl:if>    
                 </xsl:if> -->
                
      
            </td>
            <td width="15%"></td>
         </tr>
      </table>
                
           
    
  <!-- Hidden fields -->
    <input type="hidden" name="strErrorMessage" value="{$strErrorMessage}" />
    <input type="hidden" name="SMARTFORM_intSmartformID" value="{$SMARTFORM_intSmartformID}" />
    <input type="hidden" name="DEPoolSelected" value="{$DEPoolSelected}" />  
    <input type="hidden" name="DATAELEMENTS_intDataElementIDselected" value="{$DATAELEMENTS_intDataElementIDselected}" />    
    <br></br>
    <!-- ************************-->
    <input type="hidden" name="OptionPoolSelected" value="{$OptionPoolSelected}" />
    <input type="hidden" name="OPTIONS_intOptionIDSelected" value="{$OPTIONS_intOptionIDSelected}" />
    <br></br>
    <input type="hidden" name="addClicked" value="{$addClicked}" />
    </form>  
    </xsl:template>

</xsl:stylesheet>
