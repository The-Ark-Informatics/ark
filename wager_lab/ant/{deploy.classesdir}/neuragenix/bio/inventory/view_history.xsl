<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:include href="./inventory_menu.xsl"/>
    <xsl:param name="formParams">current=view_history</xsl:param>
    <xsl:output method="html" indent="no"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="SortType"/>
    <xsl:template match="inventory">
 
        <table width="100%">
            <tr valign="top">
                <td width="30%">
                    <table width="100%">
                            <xsl:call-template name="sort_selection"/>
                    </table>
                    
                    <table width="100%">
                        <xsl:apply-templates select="site">
                            <xsl:sort select="availbility" data-type="number" order="descending"/>
                        </xsl:apply-templates>
                    </table>
                    <br/>
                    <br/>
                    <br/>
                    <xsl:variable name="varAdminSection"><xsl:value-of select="intAdminSection"/></xsl:variable>
                    <xsl:if test="$varAdminSection=1">
                        <xsl:call-template name="inventory_admin"/>
                    </xsl:if>
                </td>
                <td width="70%">
                    <table width="100%">
                        <tr>
                            <td colspan="6" class="uportal-channel-subtitle">Inventory History<br/>
                                <hr/>
                            </td>
                        </tr>
                    </table>
                
                    <table width="100%">
                        <xsl:for-each select="history">
                                <xsl:choose>
                                        <xsl:when test="position() mod 2 != 0">
                                                <tr class="uportal-input-text">
                                                <td>
                                                    <table width="100%">
                                                        <tr>
                                                        <td width="100%" class="uportal-input-text">
                                                            <table width="100%">
                                                                <tr>
                                                                    <td width="20%"><b>History ID:</b></td>
                                                                    <td width="80%"><xsl:value-of select="intHistoryID"/></td>
                                                                </tr>
                                                                <tr>
                                                                    <td width="20%"><b>History Date:</b></td>
                                                                    <td width="80%"><xsl:value-of select="dtHistory"/></td>
                                                                </tr>
                                                                <tr>
                                                                    <td width="20%"><b>User:</b></td>
                                                                    <td width="80%"><xsl:value-of select="strUser"/></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        </tr>
                                                        <tr>
                                                        <td>
                                                            <table width="100%">
                                                                <xsl:for-each select="cell">
                                                                    <tr>
                                                                        <td width="10%"><b>Sample</b></td>
                                                                        <td width="10%"><xsl:value-of select="strSampleID"/></td>
                                                                        <td width="10%"><b>moved from</b></td>
                                                                        <td width="30%"><xsl:value-of select="old/SITE_strSiteName"/>,<xsl:value-of select="old/TANK_strTankName"/>,<xsl:value-of select="old/BOX_strBoxName"/>,<xsl:value-of select="old/TRAY_strTrayName"/>,<xsl:value-of select="old/CELL_strCellName"/></td>
                                                                        <td width="10%"><b>to</b></td>
                                                                        <td width="30%"><xsl:value-of select="new/SITE_strSiteName"/>,<xsl:value-of select="new/TANK_strTankName"/>,<xsl:value-of select="new/BOX_strBoxName"/>,<xsl:value-of select="new/TRAY_strTrayName"/>,<xsl:value-of select="new/CELL_strCellName"/></td>
                                                                    </tr>
                                                                </xsl:for-each>
                                                            </table>
                                                        </td>
                                                        </tr> 
                                                    </table>
                                                </td>
                                                </tr>
                                        </xsl:when>
                                        <xsl:otherwise>
                                                <tr class="uportal-text">
                                                <td width="100%" class="uportal-text">
                                                    <table width="100%">
                                                        <tr>
                                                        <td width="100%" class="uportal-text">
                                                            <table width="100%">
                                                                <tr>
                                                                    <td width="20%"><b>History ID:</b></td>
                                                                    <td width="80%"><xsl:value-of select="intHistoryID"/></td>
                                                                </tr>
                                                                <tr>
                                                                    <td width="20%"><b>History Date:</b></td>
                                                                    <td width="80%"><xsl:value-of select="dtHistory"/></td>
                                                                </tr>
                                                                <tr>
                                                                    <td width="20%"><b>User:</b></td>
                                                                    <td width="80%"><xsl:value-of select="strUser"/></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        </tr>
                                                        <tr>
                                                        <td>
                                                            <table width="100%">
                                                                <xsl:for-each select="cell">
                                                                    <tr>
                                                                        <td width="10%"><b>Sample</b></td>
                                                                        <td width="10%"><xsl:value-of select="strSampleID"/></td>
                                                                        <td width="10%"><b>moved from</b></td>
                                                                        <td width="30%"><xsl:value-of select="old/SITE_strSiteName"/>,<xsl:value-of select="old/TANK_strTankName"/>,<xsl:value-of select="old/BOX_strBoxName"/>,<xsl:value-of select="old/TRAY_strTrayName"/>,<xsl:value-of select="old/CELL_strCellName"/></td>
                                                                        <td width="10%"><b>to</b></td>
                                                                        <td width="30%"><xsl:value-of select="new/SITE_strSiteName"/>,<xsl:value-of select="new/TANK_strTankName"/>,<xsl:value-of select="new/BOX_strBoxName"/>,<xsl:value-of select="new/TRAY_strTrayName"/>,<xsl:value-of select="new/CELL_strCellName"/></td>
                                                                    </tr>
                                                                </xsl:for-each>
                                                            </table>
                                                        </td>
                                                        </tr> 
                                                    </table>
                                                </td>
                                                </tr>
                                        </xsl:otherwise>
                                </xsl:choose>
                        </xsl:for-each>
                    </table>
                    
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
