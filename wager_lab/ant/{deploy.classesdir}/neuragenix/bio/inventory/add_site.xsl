<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./inventory_menu.xsl"/>

    <xsl:param name="formParams">current=add_site</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    
  
    <xsl:template match="inventory">
    <form action="{$baseActionURL}?{$formParams}" method="post">
    <table width="100%">
        <tr valign="top">
            <td width="30%">
                <table width="100%">
                    <tr>
                        <td class="uportal-channel-subtitle">
                            Inventory hierarchy<br/><hr/>
                        </td>
                    </tr>
                    
                </table>


                <table width="100%">
                    <xsl:for-each select="site">
                        <xsl:variable name="varSiteID"><xsl:value-of select="SITE_intSiteID"/></xsl:variable>
                        <xsl:variable name="site_expanded"><xsl:value-of select="site_expanded" /></xsl:variable>
                        <xsl:variable name="current_node"><xsl:value-of select="current_node" /></xsl:variable>
                        
                        <tr>    
                            <td width="5%">
                                <xsl:choose>
                                    <xsl:when test="$site_expanded = 'true'">
                                        <a href="{$baseActionURL}?current=view_site&amp;id={$varSiteID}&amp;target=SITE&amp;SITE_intSiteID={$varSiteID}">
                                            <img src="media/neuragenix/icons/open.gif" border="0" />
                                        </a>
                                    </xsl:when>
                                    
                                    <xsl:when test="$site_expanded = 'false'">
                                        <a href="{$baseActionURL}?current=view_site&amp;id={$varSiteID}&amp;target=SITE&amp;SITE_intSiteID={$varSiteID}">
                                            <img src="media/neuragenix/icons/closed.gif" border="0" />
                                        </a>
                                    </xsl:when>
                                </xsl:choose>
                            </td>
                            
                            <td colspan="5" width="95%" class="uportal-text">
                                <a href="{$baseActionURL}?current=view_site&amp;SITE_intSiteID={$varSiteID}">
                                    <xsl:value-of select="SITE_strSiteName" />
                                    <!--xsl:value-of select="concat('/inventory/site',$varInvSiteID)" /-->
                                </a>
                                
                                <xsl:if test="$current_node = 'true'">
                                <img src="media/neuragenix/icons/current_node.gif" border="0" />
                                </xsl:if>
                            </td>
                        </tr>
                        
<!--                  TANK                             -->     
                        <xsl:if test="$site_expanded='true'">
                        
                        <xsl:for-each select="tank">
                            <xsl:variable name="varTankID"><xsl:value-of select="TANK_intTankID"/></xsl:variable>
                            <xsl:variable name="usage"><xsl:value-of select="usage"/></xsl:variable>
                            <xsl:variable name="tank_expanded"><xsl:value-of select="tank_expanded" /></xsl:variable>
                            <xsl:variable name="current_node"><xsl:value-of select="current_node" /></xsl:variable>
                            
                            <tr>    
                                <td width="5%"></td>
                                <td width="5%">
                                    <xsl:choose>
                                        <xsl:when test="$tank_expanded = 'true'">
                                            <a href="{$baseActionURL}?current=view_tank&amp;id={$varTankID}&amp;target=TANK&amp;TANK_intTankID={$varTankID}">
                                                <img src="media/neuragenix/icons/open.gif" border="0" />
                                            </a>
                                        </xsl:when>

                                        <xsl:when test="$tank_expanded = 'false'">
                                            <a href="{$baseActionURL}?current=view_tank&amp;id={$varTankID}&amp;target=TANK&amp;TANK_intTankID={$varTankID}">
                                                <img src="media/neuragenix/icons/closed.gif" border="0" />
                                            </a>
                                        </xsl:when>
                                    </xsl:choose>
                                </td>
                                <td width="5%">
                                    <xsl:choose>
                                        <xsl:when test="$usage = 0">
                                            <img src="media/neuragenix/icons/empty_tank.gif" border="0"  />
                                        </xsl:when>
                                        
                                        <xsl:when test="$usage = 1">
                                            <img src="media/neuragenix/icons/green_tank.gif" border="0"  />
                                        </xsl:when>
                                        
                                        <xsl:when test="$usage = 2">
                                            <img src="media/neuragenix/icons/yellow_tank.gif" border="0"  />
                                        </xsl:when>
                                        
                                        <xsl:when test="$usage = 3">
                                            <img src="media/neuragenix/icons/red_tank.gif" border="0"  />
                                        </xsl:when>
                                    </xsl:choose>
                                </td>

                                <td colspan="3" width="85%" class="uportal-text">
                                    <a href="{$baseActionURL}?current=view_tank&amp;TANK_intTankID={$varTankID}">
                                        <xsl:value-of select="TANK_strTankName" />
                                        <!--xsl:value-of select="concat('/inventory/site',$varInvSiteID)" /-->
                                    </a>
                                    <xsl:if test="$current_node = 'true'">
                                    <img src="media/neuragenix/icons/current_node.gif" border="0" />
                                    </xsl:if>
                                </td>
                            </tr>
                            
<!--                  BOX                             -->     
                            <xsl:if test="$tank_expanded='true'">                            
                            <xsl:for-each select="box">
                                <xsl:variable name="varBoxID"><xsl:value-of select="BOX_intBoxID"/></xsl:variable>
                                <xsl:variable name="usage"><xsl:value-of select="usage"/></xsl:variable>
                                <xsl:variable name="box_expanded"><xsl:value-of select="box_expanded" /></xsl:variable>
                                <xsl:variable name="current_node"><xsl:value-of select="current_node" /></xsl:variable>
                                
                                <tr>    
                                    <td width="5%"></td>
                                    <td width="5%"></td>
                                    <td width="5%">
                                        <xsl:choose>
                                            <xsl:when test="$box_expanded = 'true'">
                                                <a href="{$baseActionURL}?current=view_box&amp;id={$varBoxID}&amp;target=BOX&amp;BOX_intBoxID={$varBoxID}">
                                                    <img src="media/neuragenix/icons/open.gif" border="0" />
                                                </a>
                                            </xsl:when>

                                            <xsl:when test="$box_expanded = 'false'">
                                                <a href="{$baseActionURL}?current=view_box&amp;id={$varBoxID}&amp;target=BOX&amp;BOX_intBoxID={$varBoxID}">
                                                    <img src="media/neuragenix/icons/closed.gif" border="0" />
                                                </a>
                                            </xsl:when>
                                        </xsl:choose>
                                    </td>
                                    
                                    <td width="5%">
                                        <xsl:choose>
                                            <xsl:when test="$usage = 0">
                                                <img src="media/neuragenix/icons/empty_tank.gif" border="0"  />
                                            </xsl:when>

                                            <xsl:when test="$usage = 1">
                                                <img src="media/neuragenix/icons/green_tank.gif" border="0"  />
                                            </xsl:when>

                                            <xsl:when test="$usage = 2">
                                                <img src="media/neuragenix/icons/yellow_tank.gif" border="0"  />
                                            </xsl:when>

                                            <xsl:when test="$usage = 3">
                                                <img src="media/neuragenix/icons/red_tank.gif" border="0"  />
                                            </xsl:when>
                                        </xsl:choose>
                                    </td>
                                
                                    <td width="80%" colspan="2" class="uportal-text">
                                        <a href="{$baseActionURL}?current=view_box&amp;BOX_intBoxID={$varBoxID}">
                                            <xsl:value-of select="BOX_strBoxName" />
                                            <!--xsl:value-of select="concat('/inventory/site',$varInvSiteID)" /-->
                                        </a>
                                        <xsl:if test="$current_node = 'true'">
                                        <img src="media/neuragenix/icons/current_node.gif" border="0" />
                                        </xsl:if>
                                    </td>
                                </tr>
                                
<!--                  TRAY                             -->     
                                <xsl:if test="$box_expanded='true'">   
                                <xsl:for-each select="tray">
                                    <xsl:variable name="varTrayID"><xsl:value-of select="TRAY_intTrayID"/></xsl:variable>
                                    <xsl:variable name="usage"><xsl:value-of select="usage"/></xsl:variable>  
                                    <xsl:variable name="current_node"><xsl:value-of select="current_node" /></xsl:variable>
                                    
                                    <tr>    
                                        <td width="5%"></td>
                                        <td width="5%"></td>
                                        <td width="5%"></td>
                                        <td width="5%"></td>
                                        
                                        <td width="5%">
                                            <xsl:choose>
                                                <xsl:when test="$usage = 0">
                                                    <img src="media/neuragenix/icons/empty_tank.gif" border="0"  />
                                                </xsl:when>

                                                <xsl:when test="$usage = 1">
                                                    <img src="media/neuragenix/icons/green_tank.gif" border="0"  />
                                                </xsl:when>

                                                <xsl:when test="$usage = 2">
                                                    <img src="media/neuragenix/icons/yellow_tank.gif" border="0"  />
                                                </xsl:when>

                                                <xsl:when test="$usage = 3">
                                                    <img src="media/neuragenix/icons/red_tank.gif" border="0"  />
                                                </xsl:when>
                                            </xsl:choose>
                                        </td>

                                        <td width="75%" class="uportal-text">
                                            <a href="{$baseActionURL}?current=view_tray&amp;TRAY_intTrayID={$varTrayID}">
                                                <xsl:value-of select="TRAY_strTrayName" />
                                                <!--xsl:value-of select="concat('/inventory/site',$varInvSiteID)" /-->
                                            </a>
                                            <xsl:if test="$current_node = 'true'">
                                            <img src="media/neuragenix/icons/current_node.gif" border="0" />
                                            </xsl:if>
                                        </td>
                                    </tr>                     
                                </xsl:for-each>
                                </xsl:if>
                                
                            </xsl:for-each>
                            </xsl:if>
                            
                        </xsl:for-each>
                        </xsl:if>
                        
                    </xsl:for-each>
                </table>
                <br/>
                <br/>
                <br/>
                <xsl:variable name="varAdminSection"><xsl:value-of select="intAdminSection"/></xsl:variable>
                <xsl:if test="$varAdminSection=1"> 
                      <span class="uportal-channel-subtitle">
                                    Inventory administration<br/>
                                    <hr/>
                                
                       </span>
                
                
                    <table width="100%" border="0" cellspacing="0" cellpadding="2">
                       <xsl:variable name="varAdminAddSite"><xsl:value-of select="intInventoryAddSite"/></xsl:variable>
                       <xsl:variable name="varAdminAddTank"><xsl:value-of select="intInventoryAddTank"/></xsl:variable>
                       <xsl:variable name="varAdminAddBox"><xsl:value-of select="intInventoryAddBox"/></xsl:variable>
                       <xsl:variable name="varAdminAddTray"><xsl:value-of select="intInventoryAddTray"/></xsl:variable>
                      



                           <tr class="uportal-input-text">
                              <td class="uportal-channel-subtitle" width="95%">
                                 <xsl:if test="intInventoryAddSite=1">
                                    <a href="{$baseActionURL}?current=add_site">Add <xsl:value-of select="SITE_strTitleDisplay" /></a>
                                 </xsl:if>
                              </td>
                           </tr>

                           <tr class="uportal-input-text">
                              <td class="uportal-channel-subtitle" width="95%">
                                 <xsl:if test="intInventoryAddTank=1">
                                    <a href="{$baseActionURL}?current=add_tank">Add <xsl:value-of select="TANK_strTitleDisplay" /></a>
                                 </xsl:if>
                              </td>
                           </tr>

                           <tr class="uportal-input-text">
                              <td class="uportal-channel-subtitle" width="95%">
                                 <xsl:if test="intInventoryAddBox=1">
                                    <a href="{$baseActionURL}?current=add_box">Add <xsl:value-of select="BOX_strTitleDisplay" /></a>
                                 </xsl:if>
                              </td>
                           </tr>

                           <tr class="uportal-input-text">
                              <td class="uportal-channel-subtitle" width="95%">
                                 <xsl:if test="intInventoryAddTray=1">
                                    <a href="{$baseActionURL}?current=add_tray">Add <xsl:value-of select="TRAY_strTitleDisplay" /></a>
                                 </xsl:if>
                              </td>
                           </tr>

                       

                    </table>
                </xsl:if>

                
                
            
                
            </td>
            
            <td width="70%">
                
                <table width="100%">
                    <tr>
                        <td colspan="6" class="uportal-channel-subtitle">
                            Add New <xsl:value-of select="SITE_strTitleDisplay" /><br/><hr/>
                        </td>
                    </tr>
                    
                </table>
                
                <table width="100%">
                    <tr>
                        <td class="neuragenix-form-required-text">
                            <xsl:value-of select="strErrorMessage" />
                        </td>
                    </tr>
                </table>
                
                <table width="100%">
                    <tr><td width="1%" class="neuragenix-form-required-text">*</td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="SITE_strSiteNameDisplay" />:
                        </td>
                        <td width="25%">
                            <input type="text" name="SITE_strSiteName" size="22" tabindex="1" class="uportal-input-text">
                            <xsl:if test="count(/inventory/Selected) &gt; 0">
                                <xsl:attribute name="value">
                                    <xsl:value-of select="/inventory/Selected/SITE/SITE_strSiteName"/>
                                </xsl:attribute>
                            </xsl:if>
                            </input>
                        </td>
                        <td width="10%"></td>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="SITE_strSiteContactDisplay" />:
                        </td>
                        <td width="25%">
                            <input type="text" name="SITE_strSiteContact" size="22" tabindex="3" class="uportal-input-text">
                            <xsl:if test="count(/inventory/Selected) &gt; 0">
                                <xsl:attribute name="value">
                                    <xsl:value-of select="/inventory/Selected/SITE/SITE_strSiteContact"/>
                                </xsl:attribute>
                            </xsl:if>
                            </input>
                        </td>
                    </tr>
                    
                    <tr><td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="SITE_strSiteAddressDisplay" />:
                        </td>
                        <td width="25%">
                            <textarea name="SITE_strSiteAddress" rows="4" cols="20" tabindex="2" class="uportal-input-text">
                             <xsl:if test="count(/inventory/Selected) &gt; 0">
                                    <xsl:value-of select="/inventory/Selected/SITE/SITE_strSiteAddress"/>
                            </xsl:if>   
                            </textarea>
                        </td>
                        <td width="10%"></td>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="SITE_strSitePhoneDisplay" />:
                        </td>
                        <td width="25%">
                            <input type="text" name="SITE_strSitePhone" size="22" tabindex="4" class="uportal-input-text">
                            <xsl:if test="count(/inventory/Selected) &gt; 0">
                                <xsl:attribute name="value">
                                    <xsl:value-of select="/inventory/Selected/SITE/SITE_strSitePhone"/>
                                </xsl:attribute>
                            </xsl:if>
                            </input>
                        </td>
                    </tr>
                    
                    <tr>
                        <td colspan="7"><hr /></td>
                    </tr>
                </table>
                
                <table width="100%">
                    <tr>	
                        <td width="20%">
                            <input type="submit" name="save" tabindex="5" value="Save" class="uportal-button" />
                            <input type="button" name="clear" value="Clear" tabindex="6" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=add_site')" />
                        </td>
                        <td width="80%"></td>
                    </tr>
                </table>
                
            </td>
        </tr>
    </table>
    </form>
    </xsl:template>

</xsl:stylesheet>
