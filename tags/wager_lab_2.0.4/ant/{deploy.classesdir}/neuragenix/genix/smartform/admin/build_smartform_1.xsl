<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : build_smartform.xsl
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

    <xsl:param name="formParams">current=build_smartform</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    
    <!-- <xsl:key name="SmartformsDataElements_key" match="/smartform/SmartformsDataElements/SMARTFORMTODATAELEMENTS_intDataElementID" use="substring(.,1,10)"/> -->
    
    <xsl:template match="smartform">    
    <xsl:param name="strErrorMessage"><xsl:value-of select="strErrorMessage" /></xsl:param>
    <xsl:param name="SMARTFORM_intSmartformID"><xsl:value-of select="SMARTFORM_intSmartformID" /></xsl:param>
    <xsl:param name="SMARTFORM_strSmartformName"><xsl:value-of select="SMARTFORM_strSmartformName" /></xsl:param>        
    <xsl:param name="DATAELEMENTPOOL_intDataElementPoolID"><xsl:value-of select="DATAELEMENTPOOL_intDataElementPoolID" /></xsl:param>    
    <xsl:param name="DATAELEMENTPOOL_strDataElementPoolName"><xsl:value-of select="DATAELEMENTPOOL_strDataElementPoolName" /></xsl:param>
    <xsl:param name="DATAELEMENTS_intDataElementID"><xsl:value-of select="DATAELEMENTS_intDataElementID" /></xsl:param>
    <xsl:param name="DATAELEMENTS_strDataElementName"><xsl:value-of select="DATAELEMENTS_strDataElementName" /></xsl:param>
    <xsl:param name="DATAELEMENTS_intDataElementType"><xsl:value-of select="DATAELEMENTS_intDataElementType" /></xsl:param>    
    <xsl:param name="DEPoolSelected"><xsl:value-of select="DEPoolSelected" /></xsl:param>    
    <xsl:param name="SMARTFORMTODATAELEMENTS_DEIdSelected"><xsl:value-of select="SMARTFORMTODATAELEMENTS_DEIdSelected" /></xsl:param>    
    <xsl:param name="SMARTFORMTODATAELEMENTS_intDataElementID"><xsl:value-of select="SMARTFORMTODATAELEMENTS_intDataElementID" /></xsl:param>    
    
    
    
    <form name="frmbuild_smartform" action="{$baseActionURL}?{$formParams}" method="post">
    <script language="javascript" >

	function confirmDelete(aURL) {
            var confirmAnswer = confirm('Are you sure you want to delete this record?');

            if(confirmAnswer == true){
		window.location=aURL + '&amp;deleteSFDE=true';
            }
	}
    </script>
 
    <table width="100%">
        <tr>           
            
            <td width="15%" class="uportal-label">
                <input type="submit" name="addSmartform" tabindex="8" value="{$addBtnLabel}" class="uportal-button" />
                <input type="submit" name="searchSmartform" tabindex="9" value="{$searchBtnLabel}" class="uportal-button" />
            </td>
            
            <td width="80%" class="uportal-label">  
                <span class="uportal-channel-current-subtitle">Build Smartform </span>
                <!--<a href="{$baseActionURL}?current=dataelement">DataElement |</a>
                <a href="{$baseActionURL}?current=option"> Option</a> -->
                
                <!-- <span class="uportal-channel-current-subtitle"> Search task </span> 
                <a href="{$baseActionURL}?current=reassign_tasks">| Reassign all tasks |</a> -->               
                
            </td>
            
            <td width="5" class="uportal-label" align="right">
                <input type="submit" name="backtoViewSmartform" tabindex= "10" value="{$backBtnLabel}" class="uportal-button" />
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
                    <tr>
                        <td width="100%" class="uportal-channel-table-header">
                            Add DataElements to "<xsl:value-of select="SMARTFORM_strSmartformName" />"
                        </td>
                    </tr>
                 </table>
                 <hr/>
                 <table width="100%">
		     <tr >			
			<td width="30%" class="uportal-label">
				DataElements:
			</td>
			
			<td width="50%"  align="left" class="uportal-label" id="neuragenix-form-row-input-input">
				<SELECT readonly = "true" Name="SMARTFORMTODATAELEMENTS_intDataElementID" Size="5" tabindex="1" class="uportal-input-text" onChange="javascript:buildSmartform('{$baseActionURL}?current=build_smartform&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}')">
					<xsl:for-each select="SmartformsDataElements">
					<xsl:variable name="DATAELEMENTS_intDataElementID"><xsl:value-of select="DATAELEMENTS_intDataElementID" /></xsl:variable>
					<xsl:variable name="DATAELEMENTS_intDataElementType"><xsl:value-of select="DATAELEMENTS_intDataElementType" /></xsl:variable>
                                        <xsl:variable name="DATAELEMENTS_strDataElementName"><xsl:value-of select="DATAELEMENTS_strDataElementName" /></xsl:variable>

						<option>
							<xsl:if test="$DATAELEMENTS_intDataElementID = $SMARTFORMTODATAELEMENTS_DEIdSelected">
								<xsl:attribute name="selected">true</xsl:attribute> 
							</xsl:if>
							<xsl:attribute name="value"><xsl:value-of select="DATAELEMENTS_intDataElementID" /></xsl:attribute> 
							<xsl:choose> 
								<xsl:when test="$DATAELEMENTS_intDataElementType='COMMENT'"> 
									C: <xsl:value-of select="DATAELEMENTS_strDataElementName" />
								</xsl:when>
								<xsl:when test="$DATAELEMENTS_intDataElementType='TITLE'"> 
									T: <xsl:value-of select="DATAELEMENTS_strDataElementName" />
								</xsl:when>
								<xsl:when test="$DATAELEMENTS_intDataElementType='PAGEBREAK'"> 
									---- PAGE BREAK ----
								</xsl:when>
								<xsl:when test="$DATAELEMENTS_intDataElementType='SCRIPT'"> 
									S:  <xsl:value-of select="DATAELEMENTS_strDataElementName" />
								</xsl:when>
								<xsl:otherwise> 
									Q:  <xsl:value-of select="DATAELEMENTS_strDataElementName" />
								</xsl:otherwise> 
							</xsl:choose>
						</option>
                                                
					</xsl:for-each>
				</SELECT>
			</td>
			<td width="20%"  align="left">                          
                          
                          <xsl:if test="$SMARTFORMTODATAELEMENTS_DEIdSelected != 'null'">
				<table border="0">                                
                                    <tr>
                                        <td align="left" class="uportal-text">
                                        
                                            <a href="{$baseActionURL}?current=build_smartform&amp;moveUp=true&amp;SMARTFORMTODATAELEMENTS_DEIdSelected={$SMARTFORMTODATAELEMENTS_DEIdSelected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}">     
                                                 move up                                                     
                                           </a> 
                                            
					</td>
                                        <td align="left">                                        
                                        </td>
                                    </tr>                                    
                                    <tr>
                                        <td align="left" class="uportal-text">
                                            <!-- <a href="{$baseActionURL}?current=build_smartform&amp;deleteSFDE=true&amp;SMARTFORMTODATAELEMENTS_DEIdSelected={$SMARTFORMTODATAELEMENTS_DEIdSelected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}"> -->
                                            <a href="#" onclick="javascript:confirmDelete('{$baseActionURL}?current=build_smartform&amp;deleteSFDE=true&amp;SMARTFORMTODATAELEMENTS_DEIdSelected={$SMARTFORMTODATAELEMENTS_DEIdSelected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}')">                                        
                                                    delete
                                            </a> 
                                        </td>
                                        <td align="left">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="left" class="uportal-text">
                                            <a href="{$baseActionURL}?current=build_smartform&amp;moveDown=true&amp;SMARTFORMTODATAELEMENTS_DEIdSelected={$SMARTFORMTODATAELEMENTS_DEIdSelected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}">                                           
                                                    move down
                                            </a>
                                        </td>
                                    </tr>
				</table>
                          </xsl:if>
			</td>
                    </tr>
                    <tr></tr>
                    <tr></tr>	
                    <tr></tr>
                    <tr></tr>	
                </table>
                <hr/>
                <table width="100%">
                    <tr>
                        <td width="30%" class="uportal-label">
                            Select the DataElement Pool:
                        </td>
                        <td width="40%" align="left">                        
                             <select name="DATAELEMENTPOOL_intDataElementPoolID" tabindex="2" class="uportal-input-text" onChange="document.frmbuild_smartform.submit();">
                             
                             <option value="null">--- Select ---</option>                             
                                                                  
				<xsl:for-each select="buildsmartform_DataElementPool">
                                <xsl:variable name="DATAELEMENTPOOL_intDataElementPoolID"><xsl:value-of select="DATAELEMENTPOOL_intDataElementPoolID" /></xsl:variable>
                                <option>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="DATAELEMENTPOOL_intDataElementPoolID" />
                                        </xsl:attribute>                  
                                                    
                                        <xsl:if test="$DATAELEMENTPOOL_intDataElementPoolID=$DEPoolSelected">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                                            
                                        <xsl:value-of select="DATAELEMENTPOOL_strDataElementPoolName" /> 
                                 </option>                    
                                    
				</xsl:for-each>
                            </select>
                        </td>
                        <td width="30%" align="left"> 
                            <!--<xsl:if test="$DEPoolSelected != 'null'"> -->
                                <input type="submit" name="AddEditDataElements" tabindex="3" value="Add/Edit DataElements" class="uportal-button" />
                            <!--</xsl:if> -->
                        </td>
                    </tr>    
                    <tr></tr>
                    
                </table>
                
                <xsl:if test="$DEPoolSelected != 'null'">               
                 
                <table width="100%">
                    <tr>   
                        <td width="5%">
                        </td>
                        <td width="70%" class="uportal-channel-table-header">
                            <xsl:value-of select="DATAELEMENTS_strDataElementNameDisplay" />
                        </td>                        
                        <td width="20%" class="uportal-channel-table-header">
                            <xsl:value-of select="DATAELEMENTS_intDataElementTypeDisplay" />
                        </td>                  
                     </tr>             
                     <hr/>
                      <xsl:for-each select="buildsmartform_DataElements">                  
                      <xsl:variable name="DATAELEMENTS_intDataElementPoolID"><xsl:value-of select="DATAELEMENTS_intDataElementPoolID" /></xsl:variable>                                              
                      <xsl:variable name="DATAELEMENTS_intDataElementID"><xsl:value-of select="DATAELEMENTS_intDataElementID" /></xsl:variable>   
                           <!-- DE DEId:<xsl:value-of select="DATAELEMENTS_intDataElementID"/>  -->
                       
                        <tr>
                            <td width="10%">
                                <input type="checkbox">
                                <xsl:attribute name="name"><xsl:value-of select="DATAELEMENTS_intDataElementID" /></xsl:attribute>            
                                
                                 <!-- <xsl:for-each select="key('SmartformsDataElements_key',$DATAELEMENTS_intDataElementID)">--> 
                                <xsl:for-each select="/smartform/SmartformsDataElements">                  
                                <xsl:variable name="SMARTFORMTODATAELEMENTS_intDataElementID" select="./SMARTFORMTODATAELEMENTS_intDataElementID"/>
                                                          
                                <xsl:if test="$DATAELEMENTS_intDataElementID = $SMARTFORMTODATAELEMENTS_intDataElementID">
                                <xsl:attribute name="checked">true</xsl:attribute>                                                                            
                                </xsl:if>
                                
                               </xsl:for-each>  
                                                     
                                </input>
                            </td>                         
                            <td width="70%" class="uportal-label">                            
                                    <xsl:value-of select="DATAELEMENTS_strDataElementName" />                            
                            </td>

                            <td width="20%" class="uportal-label">                       
                             <xsl:variable name="DATAELEMENTS_intDataElementType"><xsl:value-of select="DATAELEMENTS_intDataElementType" /></xsl:variable>                     
                                    <xsl:choose> 
                                        <xsl:when test="$DATAELEMENTS_intDataElementType='COMMENT'"> 
                                                Comment
                                        </xsl:when>
                                        <xsl:when test="$DATAELEMENTS_intDataElementType='TITLE'"> 
                                                Title
                                        </xsl:when>
                                        <xsl:when test="$DATAELEMENTS_intDataElementType='PAGEBREAK'"> 
                                               Page Break
                                        </xsl:when>
                                        <xsl:when test="$DATAELEMENTS_intDataElementType='TEXT'"> 
                                                Text
                                        </xsl:when>
                                        <xsl:when test="$DATAELEMENTS_intDataElementType='DROPDOWN'"> 
                                                Dropdown
                                        </xsl:when>
                                        <xsl:when test="$DATAELEMENTS_intDataElementType='NUMERIC'"> 
                                                Numeric
                                        </xsl:when>
                                        <xsl:when test="$DATAELEMENTS_intDataElementType='DATE'"> 
                                                Date
                                        </xsl:when>
                                        <xsl:when test="$DATAELEMENTS_intDataElementType='SCRIPT'"> 
                                                Script
                                        </xsl:when>
                                        <xsl:when test="$DATAELEMENTS_intDataElementType='SYSTEM LOOKUP'"> 
                                                System Lookup
                                        </xsl:when>
                                        <xsl:when test="$DATAELEMENTS_intDataElementType='MONETARY'"> 
                                                Monetary
                                        </xsl:when>
                                        <xsl:when test="$DATAELEMENTS_intDataElementType='ATTACHMENT'"> 
                                                Attachment
                                        </xsl:when>
                                        <xsl:otherwise> 

                                        </xsl:otherwise> 
                                 </xsl:choose>

                            </td>                        

                        </tr>
                        
                        
                        </xsl:for-each>
                    
                </table>
                
                
                <table width="100%">
                    <tr>
                        <td>
                            <input type="submit" name="saveBuildSmartForm" tabindex="4" value="{$saveBtnLabel}" class="uportal-button" />
                            <input type="button" name="checkAllDataElements" tabindex="5" value="Check All" class="uportal-button" onClick="javascript:change(frmbuild_smartform,true)"/>
                            <input type="button" name="clearAllDataElements" tabindex="6" value="Clear All" class="uportal-button" onClick="javascript:change(frmbuild_smartform,false)"/>                            
                            <input type="reset" name="resetDataElements" value="Reset" tabindex="7" class="uportal-button" />                           
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <!--<input type="submit" name="AddEditDataElements" tabindex="9" value="Add/Edit DataElements" class="uportal-button" />  -->
                        </td>
                    </tr>
                </table>
                
                </xsl:if>
            </td>
            <td width="15%"/>
        </tr>
    </table>
    
    <!-- Hidden Fields -->    
    <input type="hidden" name="strErrorMessage" value="{$strErrorMessage}" />
    <input type="hidden" name="SMARTFORM_intSmartformID" value="{$SMARTFORM_intSmartformID}" />
    <input type="hidden" name="DEPoolSelected" value="{$DEPoolSelected}" />  
    <input type="hidden" name="SMARTFORMTODATAELEMENTS_DEIdSelected" value="{$SMARTFORMTODATAELEMENTS_DEIdSelected}" />    
     
    </form>
    </xsl:template>

</xsl:stylesheet>
