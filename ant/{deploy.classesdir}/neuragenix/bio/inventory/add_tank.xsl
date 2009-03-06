<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./inventory_menu.xsl"/>

    <xsl:param name="formParams">current=add_tank</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    
  
    <xsl:template match="inventory">
    <xsl:param name="TANK_dtCommissionDate_Year"><xsl:value-of select="TANK_dtCommissionDate_Year" /></xsl:param>
    <xsl:param name="TANK_dtLastServiceDate_Year"><xsl:value-of select="TANK_dtLastServiceDate_Year" /></xsl:param>
    
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
                            Add New <xsl:value-of select="TANK_strTitleDisplay" /><br/><hr/>
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
                            <xsl:value-of select="SITE_strTitleDisplay" /> name:
                        </td>
                        <td width="25%">
                            <select name="TANK_intSiteID" tabindex="1" class="uportal-input-text">
				<xsl:for-each select="search_site">
                                    <xsl:variable name="TANK_intSiteID"><xsl:value-of select="SITE_intSiteID" /></xsl:variable>
                                    <option>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="$TANK_intSiteID" />
                                        </xsl:attribute>
                                        <xsl:if test="count(/inventory/Selected) &gt; 0">
                                            <xsl:variable name="varSiteID"><xsl:value-of select="/inventory/Selected/SITE/SITE_intSiteID"/></xsl:variable>
                                            <xsl:if test="$varSiteID = $TANK_intSiteID">
                                            <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                              </xsl:if>
                                        </xsl:if>
                                        <xsl:value-of select="SITE_strSiteName" />
                                    </option>
				</xsl:for-each>
                            </select>
                        </td>
                        <td width="10%"></td>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="TANK_strStatusDisplay" />:
                        </td>
                        <td width="25%">
                            <select name="TANK_strStatus" tabindex="5" class="uportal-input-text">
				<xsl:for-each select="TANK_strStatus">
                                    <xsl:variable name="TANK_strStatus"><xsl:value-of select="." /></xsl:variable>
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="$TANK_strStatus" />
                                        </xsl:attribute> 
                                        <xsl:choose>
                                        <xsl:when test="count(/inventory/Selected) &gt; 0">
                                            <xsl:variable name="varstrStatus"><xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_strStatus" /></xsl:variable>
                                            <xsl:if test='$TANK_strStatus = $varstrStatus'>
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                            </xsl:if>
                                        </xsl:when>
                                        <xsl:otherwise>
                                        <xsl:if test="@selected=1">
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        </xsl:otherwise>
                                        </xsl:choose>
                                        <xsl:value-of select="." />		
                                    </option>
				</xsl:for-each>
                            </select>
                        </td>
                    </tr>
                    
                    <tr><td width="1%" class="neuragenix-form-required-text">*</td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="TANK_strTankNameDisplay" />:
                        </td>
                        <td width="25%">
                            <input type="text" name="TANK_strTankName" size="22" tabindex="2" class="uportal-input-text">
                            <xsl:if test="count(/inventory/Selected) &gt; 0">
                                <xsl:attribute name="value"><xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_strTankName"/></xsl:attribute>
                            </xsl:if>
                            </input>
                        </td>
                        <td width="10%"></td>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="TANK_dtCommissionDateDisplay" />:
                        </td>
                        <td width="25%">
                        <xsl:choose>
                        <xsl:when test="TANK_dtCommissionDate/@display_type='text'">
                            <input type="text" name="TANK_dtCommissionDate_Day" size="2" tabindex="24">
                                <xsl:choose>
                                <xsl:when test="count(/inventory/Selected) &gt; 0">
                                    <xsl:attribute name="value"><xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_dtCommissionDate_Day"/></xsl:attribute>
                                </xsl:when>
                                <xsl:otherwise>
                                <xsl:attribute name="value"><xsl:value-of select="TANK_dtCommissionDate_Day"/></xsl:attribute>
                                </xsl:otherwise>
                                </xsl:choose>
                            </input>
                            <input type="text" name="TANK_dtCommissionDate_Month" size="2" tabindex="25">
                                <xsl:choose>
                                <xsl:when test="count(/inventory/Selected) &gt; 0">
                                    <xsl:attribute name="value"><xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_dtCommissionDate_Month"/></xsl:attribute>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:attribute name="value"><xsl:value-of select="TANK_dtCommissionDate_Month"/></xsl:attribute>
                                </xsl:otherwise>
                                </xsl:choose>
                            </input>
                        </xsl:when>
                        <xsl:otherwise>
                            <select name="TANK_dtCommissionDate_Day" tabindex="6" class="uportal-input-text">
				<xsl:for-each select="TANK_dtCommissionDate_Day">
                                    <xsl:variable name="TANK_dtCommissionDate_Day"><xsl:value-of select="."/></xsl:variable>
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="." />
                                        </xsl:attribute> 
                                        <xsl:choose>
                                <xsl:when test="count(/inventory/Selected) &gt; 0">
                                    <xsl:variable name="varCommissionDay"><xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_dtCommissionDate_Day"/></xsl:variable>
                                    <xsl:if test="$TANK_dtCommissionDate_Day = $varCommissionDay">
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>
                                </xsl:when>
                                <xsl:otherwise>
                                        <xsl:if test="@selected=1">
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                            </xsl:if>
                                </xsl:otherwise>
                                </xsl:choose>
                                        <xsl:value-of select="." />		
                                    </option>
				</xsl:for-each>
                            </select>
                            
                            <select name="TANK_dtCommissionDate_Month" tabindex="7" class="uportal-input-text">
				<xsl:for-each select="TANK_dtCommissionDate_Month">
                                    <xsl:variable name="TANK_dtCommissionDate_Month"><xsl:value-of select="." /></xsl:variable>
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="$TANK_dtCommissionDate_Month" />
                                        </xsl:attribute> 
                                        <xsl:choose>
                                        <xsl:when test="count(/inventory/Selected) &gt; 0">
                                            <xsl:variable name="varCommissionMonth"><xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_dtCommissionDate_Month"/></xsl:variable>
                                            <xsl:if test="$TANK_dtCommissionDate_Month = $varCommissionMonth">
                                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                            </xsl:if>
                                        </xsl:when>
                                        <xsl:otherwise>
                                        <xsl:if test="@selected=1">
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        </xsl:otherwise>
                                        </xsl:choose>
                                        <xsl:value-of select="$TANK_dtCommissionDate_Month" />		
                                    </option>
				</xsl:for-each>
                            </select>
                            </xsl:otherwise>
                        </xsl:choose>
                            <input type="text" name="TANK_dtCommissionDate_Year" size="5" tabindex="8" class="uportal-input-text">
                            <xsl:choose>
                            <xsl:when test="count(/inventory/Selected) &gt; 0">
                                <xsl:attribute name="value"><xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_dtCommissionDate_Year"/></xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                            <xsl:attribute name="value"><xsl:value-of select='$TANK_dtCommissionDate_Year'/></xsl:attribute>
                            </xsl:otherwise>
                            </xsl:choose>
                            </input>
                        
                        </td>
                    </tr>
                    
                    <tr><td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="TANK_strTankLocationDisplay" />:
                        </td>
                        <td width="25%">
                            <textarea name="TANK_strTankLocation" rows="4" cols="20" tabindex="3" class="uportal-input-text">
                            <xsl:if test="count(/inventory/Selected) &gt; 0">
                                <xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_strTankLocation"/>
                            </xsl:if>
                            </textarea>
                        </td>
                        <td width="10%"></td>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="TANK_dtLastServiceDateDisplay" />:
                        </td>
                        <td width="25%">
                        <xsl:choose>
                        <xsl:when test="TANK_dtLastServiceDate/@display_type='text'">
                            <input type="text" name="TANK_dtLastServiceDate_Day" size="2" tabindex="24">
                                <xsl:choose>
                                <xsl:when test="count(/inventory/Selected) &gt; 0">
                                    <xsl:attribute name="value"><xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_dtLastServiceDate_Day"/></xsl:attribute>
                                </xsl:when>
                                <xsl:otherwise>
                                 <xsl:attribute name="value"><xsl:value-of select="TANK_dtLastServiceDate_Day"/></xsl:attribute>
                             </xsl:otherwise>
                             </xsl:choose>
                            </input>
                            <input type="text" name="TANK_dtLastServiceDate_Month" size="2" tabindex="25">
                                <xsl:choose>
                                <xsl:when test="count(/inventory/Selected) &gt; 0">
                                    <xsl:attribute name="value"><xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_dtLastServiceDate_Month"/></xsl:attribute>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:attribute name="value"><xsl:value-of select="TANK_dtLastServiceDate_Month"/></xsl:attribute>
                                </xsl:otherwise>
                                </xsl:choose>
                            </input>
                        </xsl:when>
                        <xsl:otherwise>
                            <select name="TANK_dtLastServiceDate_Day" tabindex="9" class="uportal-input-text">
				<xsl:for-each select="TANK_dtLastServiceDate_Day">
                                    <xsl:variable name="TANK_dtLastServiceDate_Day"><xsl:value-of select="."/></xsl:variable>
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="$TANK_dtLastServiceDate_Day" />
                                        </xsl:attribute> 
                                        <xsl:choose>
                                        <xsl:when test="count(/inventory/Selected) &gt; 0">
                                            <xsl:variable name="vardtLastServiceDay"><xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_dtLastServiceDate_Day"/></xsl:variable>
                                            <xsl:if test="$TANK_dtLastServiceDate_Day = $vardtLastServiceDay">
                                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                            </xsl:if>
                                        </xsl:when>
                                        <xsl:otherwise>
                                        <xsl:if test="@selected=1">
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        </xsl:otherwise>
                                        </xsl:choose>
                                        <xsl:value-of select="$TANK_dtLastServiceDate_Day" />		
                                    </option>
				</xsl:for-each>
                            </select>
                            
                            <select name="TANK_dtLastServiceDate_Month" tabindex="10" class="uportal-input-text">
				<xsl:for-each select="TANK_dtLastServiceDate_Month">
                                    <xsl:variable name="TANK_dtLastServiceDate_Month"><xsl:value-of select="." /></xsl:variable>
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="$TANK_dtLastServiceDate_Month" />
                                        </xsl:attribute> 
                                        <xsl:choose>
                                        <xsl:when test="count(/inventory/Selected) &gt; 0">
                                            <xsl:variable name="vardtLastServiceMonth"><xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_dtLastServiceDate_Month"/></xsl:variable>
                                            <xsl:if test="$TANK_dtLastServiceDate_Month = $vardtLastServiceMonth">
                                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                            </xsl:if>
                                        </xsl:when>
                                        <xsl:otherwise>
                                        <xsl:if test="@selected=1">
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        </xsl:otherwise>
                                        </xsl:choose>
                                        <xsl:value-of select="TANK_dtLastServiceDate_Month" />		
                                    </option>
				</xsl:for-each>
                            </select>
                            </xsl:otherwise>
                            </xsl:choose>
                            <input type="text" name="TANK_dtLastServiceDate_Year" size="5" tabindex="11" class="uportal-input-text">
                            <xsl:choose>
                            <xsl:when test="count(/inventory/Selected) &gt; 0">
                                <xsl:attribute name="value"><xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_dtLastServiceDate_Year"/></xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:attribute name="value"><xsl:value-of select='$TANK_dtLastServiceDate_Year'/></xsl:attribute>
                            </xsl:otherwise>
                            </xsl:choose>
                            </input>
                        </td>
                    </tr>
                    
                    <tr><td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="TANK_strTankDescriptionDisplay" />:
                        </td>
                        <td width="25%">
                            <textarea name="TANK_strTankDescription" rows="4" cols="20" tabindex="4" class="uportal-input-text">                                
                            <xsl:if test="count(/inventory/Selected) &gt; 0">
                                <xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_strTankDescription"/>
                            </xsl:if>
                            </textarea>
                        </td>
                        <td width="10%"></td>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="TANK_strLastServiceNoteDisplay" />:
                        </td>
                        <td width="25%">
                            <textarea name="TANK_strLastServiceNote" rows="4" cols="20" tabindex="12" class="uportal-input-text">
                               <xsl:if test="count(/inventory/Selected) &gt; 0">
                                <xsl:value-of select="/inventory/Selected/SITE/TANK/TANK_strLastServiceNote"/>
                            </xsl:if> 
                            </textarea>
                        </td>
                    </tr>
                    
                    <tr>
                        <td colspan="7"><hr /></td>
                    </tr>
                </table>
                
                <table width="100%">
                    <tr>	
                        <td width="20%">
                            <input type="submit" name="save" tabindex="13" value="Save" class="uportal-button" />
                            <input type="button" name="clear" value="Clear" tabindex="14" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=add_tank')" />
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
