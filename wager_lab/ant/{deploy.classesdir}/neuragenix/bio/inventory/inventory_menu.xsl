<?xml version="1.0" encoding="utf-8"?>

<!-- inventory_menu.xsl. Menu used for all inventory.-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/">
        <script language="javascript"> function confirmDelete(aURL) { var confirmAnswer =
            confirm('Are you sure you want to delete this record?'); if(confirmAnswer == true){
            window.location=aURL + '&amp;delete=true'; } } function confirmClear(aURL) { var
            confirmAnswer = confirm('Are you sure you want to clear the fields?'); if(confirmAnswer
            == true) { window.location=aURL + '&amp;clear=true'; } } function jumpTo(aURL){
            window.location=aURL; } </script>
        <xsl:apply-templates/>
    </xsl:template>
    <xsl:template name="sort_selection">
        <!-- grab the sort_type -->
        <xsl:variable name="sort_type">
            <xsl:value-of select="/inventory/sort_type"/>
        </xsl:variable>
        <xsl:variable name="current_view">
            <xsl:value-of select="/inventory/current_view"/>
        </xsl:variable>
        <xsl:variable name="current_ID">
            <xsl:value-of select="/inventory/current_ID"/>
        </xsl:variable>
        <xsl:variable name="current_field_string">
            <xsl:value-of select="/inventory/current_field_string"/>
        </xsl:variable>
        <!-- decide whether the link has target or not -->
        <xsl:variable name="target">
            <xsl:value-of select="/inventory/target"/>
        </xsl:variable>
        <tr valign="baseline">
            <td class="uportal-channel-subtitle" valign="top"> Inventory hierarchy <br/>
            </td>
            <td valign="top" align="right">
                <xsl:choose>
                    <xsl:when test="string($target)">
                        <form name="sort_selecting" method="post"
                            action="{$baseActionURL}?uP_root=root&amp;current={$current_view}&amp;id={$current_ID}&amp;target={$target}&amp;{$current_field_string}={$current_ID}#frm_InventoryAnchor">
                            <select name="sort_type"
                                onChange="javascript:document.sort_selecting.submit()"
                                class="uportal-input-text">
                                <option>
                                    <xsl:attribute name="value">1</xsl:attribute>
                                    <xsl:if test="$sort_type='1'">
                                        <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if> Sort alphabetically </option>
                                <option>
                                    <xsl:attribute name="value">2</xsl:attribute>
                                    <xsl:if test="$sort_type='2'">
                                        <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if> Sort by availability </option>
                                <option>
                                    <xsl:attribute name="value">3</xsl:attribute>
                                    <xsl:if test="$sort_type='3'">
                                        <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if>Sort by date created</option>
                            </select>
                        </form>
                    </xsl:when>
                    <xsl:otherwise>
                        <form name="sort_selecting" method="post"
                            action="{$baseActionURL}?uP_root=root&amp;current={$current_view}&amp;{$current_field_string}={$current_ID}#frm_InventoryAnchor">
                            <select name="sort_type"
                                onChange="javascript:document.sort_selecting.submit()"
                                class="uportal-input-text">
                                <option>
                                    <xsl:attribute name="value">1</xsl:attribute>
                                    <xsl:if test="$sort_type='1'">
                                        <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if> Sort alphabetically </option>
                                <option>
                                    <xsl:attribute name="value">2</xsl:attribute>
                                    <xsl:if test="$sort_type='2'">
                                        <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if> Sort by availability </option>
                                <option>
                                    <xsl:attribute name="value">3</xsl:attribute>
                                    <xsl:if test="$sort_type='3'">
                                        <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if> Sort by date created</option>
                            </select> 
                        </form>
                    </xsl:otherwise>
                </xsl:choose>
            </td>
            
            
          </tr>
          <tr valign="top">
            <td colspan="2" valign="top">
                    <hr/>
                </td>
          </tr>
        
        
    </xsl:template>
    <xsl:template match="tray">
        <xsl:variable name="sort_type">
            <xsl:value-of select="/inventory/sort_type"/>
        </xsl:variable>
        <xsl:variable name="varTrayID">
            <xsl:value-of select="TRAY_intTrayID"/>
        </xsl:variable>
        <xsl:variable name="usage">
            <xsl:value-of select="usage"/>
        </xsl:variable>
        <xsl:variable name="current_node">
            <xsl:value-of select="current_node"/>
        </xsl:variable>
        <tr>
            <td width="5%"/>
            <td width="5%"/>
            <td width="5%"/>
            <td width="5%"/>
            <td width="5%">
                <xsl:choose>
                    <xsl:when test="$usage = 0">
                        <img src="media/neuragenix/icons/empty_tank.gif" border="0"/>
                    </xsl:when>
                    <xsl:when test="$usage = 1">
                        <img src="media/neuragenix/icons/green_tank.gif" border="0"/>
                    </xsl:when>
                    <xsl:when test="$usage = 2">
                        <img src="media/neuragenix/icons/yellow_tank.gif" border="0"/>
                    </xsl:when>
                    <xsl:when test="$usage = 3">
                        <img src="media/neuragenix/icons/red_tank.gif" border="0"/>
                    </xsl:when>
                </xsl:choose>
            </td>
            <td width="75%" class="uportal-text">
                <a href="{$baseActionURL}?uP_root=root&amp;current=view_tray&amp;TRAY_intTrayID={$varTrayID}#frm_InventoryAnchor">
                    <xsl:value-of select="TRAY_strTrayName"/> 
                    <!--xsl:value-of select="concat('/inventory/site',$varInvSiteID)" /-->
                </a>
                <xsl:if test="$current_node = 'true'">
                    <a name="frm_InventoryAnchor"/>
                    <img src="media/neuragenix/icons/current_node.gif" border="0"/>
                </xsl:if>
            </td>
        </tr>
    </xsl:template>
    <xsl:template match="box">
        <xsl:variable name="sort_type">
            <xsl:value-of select="/inventory/sort_type"/>
        </xsl:variable>
        <xsl:variable name="varBoxID">
            <xsl:value-of select="BOX_intBoxID"/>
        </xsl:variable>
        <xsl:variable name="availbility">
            <xsl:value-of select="availbilty"/>
        </xsl:variable>
        <xsl:variable name="usage">
            <xsl:value-of select="usage"/>
        </xsl:variable>
        <xsl:variable name="box_expanded">
            <xsl:value-of select="box_expanded"/>
        </xsl:variable>
        <xsl:variable name="current_node">
            <xsl:value-of select="current_node"/>
        </xsl:variable>
        
        <tr>
            <td width="5%"/>
            <td width="5%"/>
            <td width="5%">
                <xsl:choose>
                    <xsl:when test="$box_expanded = 'true'">
                        <a
                            href="{$baseActionURL}?uP_root=root&amp;current=view_box&amp;id={$varBoxID}&amp;target=BOX&amp;BOX_intBoxID={$varBoxID}#BOX_{$varBoxID}">
                            <img src="media/neuragenix/icons/open.gif" border="0"/>
                        </a>
                        
                    </xsl:when>
                    <xsl:when test="$box_expanded = 'false'">
                        <a
                            href="{$baseActionURL}?uP_root=root&amp;current=view_box&amp;id={$varBoxID}&amp;target=BOX&amp;BOX_intBoxID={$varBoxID}#BOX_{$varBoxID}">
                            <img src="media/neuragenix/icons/closed.gif" border="0"/>
                        </a>
                    </xsl:when>
                </xsl:choose>
                <a name="BOX_{$varBoxID}"/>
            </td>
            <td width="5%">
                <xsl:choose>
                    <xsl:when test="$usage = 0">
                        <img src="media/neuragenix/icons/empty_tank.gif" border="0"/>
                    </xsl:when>
                    <xsl:when test="$usage = 1">
                        <img src="media/neuragenix/icons/green_tank.gif" border="0"/>
                    </xsl:when>
                    <xsl:when test="$usage = 2">
                        <img src="media/neuragenix/icons/yellow_tank.gif" border="0"/>
                    </xsl:when>
                    <xsl:when test="$usage = 3">
                        <img src="media/neuragenix/icons/red_tank.gif" border="0"/>
                    </xsl:when>
                </xsl:choose>
            </td>
            <td width="80%" colspan="2" class="uportal-text">
                <a href="{$baseActionURL}?uP_root=root&amp;current=view_box&amp;BOX_intBoxID={$varBoxID}#frm_InventoryAnchor">
                    <xsl:value-of select="BOX_strBoxName"/>
                    
                </a>
                <xsl:if test="$current_node = 'true'">
                    <a name="frm_InventoryAnchor"/>
                    <img src="media/neuragenix/icons/current_node.gif" border="0"/>
                </xsl:if>
            </td>
        </tr>
        <!-- Call the tray  templates  -->
        <xsl:if test="$box_expanded='true'">
            <xsl:choose>
                <xsl:when test="$sort_type='1'">
                    <xsl:apply-templates select="tray">
                        <xsl:sort select="TRAY_strTrayName" data-type="text"/>
                    </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="$sort_type='2'">
                    <xsl:apply-templates select="tray">
                        <!-- sort based on the availble cells -->
                        <xsl:sort select="usage"  data-type="number" order="ascending"/>
                    </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="$sort_type=3">
                    <xsl:apply-templates select="tray">
                        <xsl:sort select="TRAY_intTrayID" data-type="number"
                            order="descending"/>
                        
                        
                    </xsl:apply-templates>
                </xsl:when>
            </xsl:choose>
        </xsl:if>
        <!-- end of box -->
    </xsl:template>
    <xsl:template match="tank">
        <xsl:variable name="sort_type">
            <xsl:value-of select="/inventory/sort_type"/>
        </xsl:variable>
        <xsl:variable name="varTankID">
            <xsl:value-of select="TANK_intTankID"/>
        </xsl:variable>
        <xsl:variable name="usage">
            <xsl:value-of select="usage"/>
        </xsl:variable>
        <xsl:variable name="tank_expanded">
            <xsl:value-of select="tank_expanded"/>
        </xsl:variable>
        <xsl:variable name="current_node">
            <xsl:value-of select="current_node"/>
        </xsl:variable>
        <tr>
            <td width="5%"/>
            <td width="5%">
                <xsl:choose>
                    <xsl:when test="$tank_expanded = 'true'">
                        <a
                           href="{$baseActionURL}?uP_root=root&amp;current=view_tank&amp;id={$varTankID}&amp;target=TANK&amp;TANK_intTankID={$varTankID}#TANK_{$varTankID}">
                            <img src="media/neuragenix/icons/open.gif" border="0"/>
                        </a>
                    </xsl:when>
                    <xsl:when test="$tank_expanded = 'false'">
                        <a
                           href="{$baseActionURL}?uP_root=root&amp;current=view_tank&amp;id={$varTankID}&amp;target=TANK&amp;TANK_intTankID={$varTankID}#TANK_{$varTankID}">
                            <img src="media/neuragenix/icons/closed.gif" border="0"/>
                        </a>
                    </xsl:when>
                 </xsl:choose>
                 <a name="TANK_{$varTankID}"/>
            </td>
            <td width="5%">
                <xsl:choose>
                    <xsl:when test="$usage = 0">
                        <img src="media/neuragenix/icons/empty_tank.gif" border="0"/>
                    </xsl:when>
                    <xsl:when test="$usage = 1">
                        <img src="media/neuragenix/icons/green_tank.gif" border="0"/>
                    </xsl:when>
                    <xsl:when test="$usage = 2">
                        <img src="media/neuragenix/icons/yellow_tank.gif" border="0"/>
                    </xsl:when>
                    <xsl:when test="$usage = 3">
                        <img src="media/neuragenix/icons/red_tank.gif" border="0"/>
                    </xsl:when>
                </xsl:choose>
            </td>
            <td colspan="3" width="85%" class="uportal-text">
                <a href="{$baseActionURL}?uP_root=root&amp;current=view_tank&amp;TANK_intTankID={$varTankID}#frm_InventoryAnchor">
                <xsl:value-of select="TANK_strTankName"/>
                
                   </a>
                <!--xsl:value-of select="concat('/inventory/site',$varInvSiteID)" /-->
                <xsl:if test="$current_node = 'true'">
                    <a name="frm_InventoryAnchor"/>
                    <img src="media/neuragenix/icons/current_node.gif" border="0"/>
                </xsl:if>
            </td>
        </tr>
        
        
        
        <!--   call box templates   -->
        <xsl:if test="$tank_expanded='true'">
            <xsl:choose>
                <xsl:when test="$sort_type=1">
                    <xsl:apply-templates select="box">
                        <xsl:sort select="BOX_strBoxName" data-type="text"/>
                    </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="$sort_type=2">
                    <xsl:apply-templates select="box">
                        <xsl:sort select="usage"  data-type="number" order="ascending"/>
                    </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="$sort_type=3">
                    <xsl:apply-templates select="box">
                        
                        <xsl:sort select="BOX_intBoxID" data-type="number"
                            order="descending"/>
                        
                        
                    </xsl:apply-templates>
                </xsl:when>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
    <xsl:template match="site">
        <xsl:variable name="sort_type">
            <xsl:value-of select="/inventory/sort_type"/>
        </xsl:variable>
        <!-- for each site -->
        <xsl:variable name="varSiteID">
            <xsl:value-of select="SITE_intSiteID"/>
        </xsl:variable>
        <xsl:variable name="site_expanded">
            <xsl:value-of select="site_expanded"/>
        </xsl:variable>
        <xsl:variable name="current_node">
            <xsl:value-of select="current_node"/>
        </xsl:variable>
        <tr>
            <td width="5%">
                <xsl:choose>
                    <xsl:when test="$site_expanded = 'true'">
                        <a
                           href="{$baseActionURL}?uP_root=root&amp;current=view_site&amp;id={$varSiteID}&amp;target=SITE&amp;SITE_intSiteID={$varSiteID}#SITE_{$varSiteID}">
                            <img src="media/neuragenix/icons/open.gif" border="0"/>
                        </a>
                    </xsl:when>
                    <xsl:when test="$site_expanded = 'false'">
                        <a
                           href="{$baseActionURL}?uP_root=root&amp;current=view_site&amp;id={$varSiteID}&amp;target=SITE&amp;SITE_intSiteID={$varSiteID}#SITE_{$varSiteID}">
                            <img src="media/neuragenix/icons/closed.gif" border="0"/>
                        </a>
                     </xsl:when>
                     
                  </xsl:choose>

                  <a name="SITE_{$varSiteID}"/>
            </td>
            <td colspan="5" width="95%" class="uportal-text">
                <a href="{$baseActionURL}?uP_root=root&amp;current=view_site&amp;SITE_intSiteID={$varSiteID}#frm_InventoryAnchor">
                    <xsl:value-of select="SITE_strSiteName"/>
                    <!--xsl:value-of select="concat('/inventory/site',$varInvSiteID)" /-->
                </a>
                <xsl:if test="$current_node = 'true'">
                    <a name="frm_InventoryAnchor"/>
                    <img src="media/neuragenix/icons/current_node.gif" border="0"/>
                </xsl:if>
            </td>
        </tr>
        <!--     call the tank template --> 
        <!-- expand it -->
        <xsl:if test="$site_expanded='true'">
            <xsl:choose>
                <xsl:when test="$sort_type='1'">
                    <xsl:apply-templates select="tank">
                        <xsl:sort select="TANK_strTankName" data-type="text"/>
                    </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="$sort_type=2">
                    <xsl:apply-templates select="tank">
                        <xsl:sort select="usage"  data-type="number" order="ascending"/>
                    </xsl:apply-templates>
                </xsl:when>
                <xsl:when test="$sort_type=3">
                    <xsl:apply-templates select="tank">
                        <xsl:sort select="TANK_intTankID" data-type="number"
                            order="descending"/>
                       
                        
                    </xsl:apply-templates>
                </xsl:when>
            </xsl:choose>
        </xsl:if>
        <!-- end of Tank -->
    </xsl:template>
    <xsl:template name="inventory_admin">
        <span class="uportal-channel-subtitle"> Inventory administration<br/>
            <hr/>
        </span>
        <table width="100%" border="0" cellspacing="0" cellpadding="2">
            <xsl:variable name="varAdminAddSite">
                <xsl:value-of select="intInventoryAddSite"/>
            </xsl:variable>
            <xsl:variable name="varAdminAddTank">
                <xsl:value-of select="intInventoryAddTank"/>
            </xsl:variable>
            <xsl:variable name="varAdminAddBox">
                <xsl:value-of select="intInventoryAddBox"/>
            </xsl:variable>
            <xsl:variable name="varAdminAddTray">
                <xsl:value-of select="intInventoryAddTray"/>
            </xsl:variable>
            <xsl:variable name="varAdminViewHistory">
                <xsl:value-of select="intInventoryHistory"/>
            </xsl:variable>
            <tr class="uportal-input-text">
                <td class="uportal-channel-subtitle" width="95%">
                    <xsl:if test="intInventoryAddSite=1">
                        <a href="{$baseActionURL}?uP_root=root&amp;current=add_site">Add <xsl:value-of
                                select="SITE_strTitleDisplay"/>
                        </a>
                    </xsl:if>
                </td>
            </tr>
            <tr class="uportal-input-text">
                <td class="uportal-channel-subtitle" width="95%">
                    <xsl:if test="intInventoryAddTank=1">
                        <a href="{$baseActionURL}?uP_root=root&amp;current=add_tank">Add <xsl:value-of
                                select="TANK_strTitleDisplay"/>
                        </a>
                    </xsl:if>
                </td>
            </tr>
            <tr class="uportal-input-text">
                <td class="uportal-channel-subtitle" width="95%">
                    <xsl:if test="intInventoryAddBox=1">
                        <a href="{$baseActionURL}?uP_root=root&amp;current=add_box">Add <xsl:value-of
                                select="BOX_strTitleDisplay"/>
                        </a>
                    </xsl:if>
                </td>
            </tr>
            <tr class="uportal-input-text">
                <td class="uportal-channel-subtitle" width="95%">
                    <xsl:if test="intInventoryAddTray=1">
                        <a href="{$baseActionURL}?uP_root=root&amp;current=add_tray">Add <xsl:value-of
                                select="TRAY_strTitleDisplay"/>
                        </a>
                    </xsl:if>
                </td>
            </tr>
            <tr class="uportal-input-text">
                <td class="uportal-channel-subtitle" width="95%">
                    <xsl:if test="$varAdminViewHistory=1">
                        <a href="{$baseActionURL}?uP_root=root&amp;current=view_history">Inventory History</a>
                    </xsl:if>
                </td>
            </tr>
        </table>
    </xsl:template>
    
</xsl:stylesheet>
