<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./inventory_menu.xsl"/>
    <xsl:param name="formParams">current=inventory_view</xsl:param>
    <xsl:output method="html" indent="no"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:template match="inventory">
        <table width="100%">
            <tr valign="top">
                <td width="30%">
                    <table width="250" cellpadding="0" cellspacing="0">
                        <xsl:call-template name="sort_selection"/>
                    </table>
                  
                    <table width="250" cellpadding="0" cellspacing="0">
                        <xsl:apply-templates select="site">
                            <xsl:sort select="availbility" data-type="number" order="descending"/>
                        </xsl:apply-templates>
                        <tr
                            class="funcpanel_content"><td
                                colspan="11"
                                class="funcpanel_bottom_border"><img
                                    src="media/neuragenix/infopanel/spacer.gif"
                                    height="4"
                                    width="1"/></td></tr>
                    </table>
                    <!-- end of Site -->
                    <br/>
                    <br/>
                    <br/>
                    <!-- admin section -->
                    <xsl:variable name="varAdminSection">
                        <xsl:value-of select="intAdminSection"/>
                    </xsl:variable>
                    <xsl:variable name="accesstoexpandedsite">
                        <xsl:value-of select="site[site_expanded='true']/siteAccess"/>
                    </xsl:variable>
                    <xsl:if test="$varAdminSection=1 and $accesstoexpandedsite=1">
                        <xsl:call-template name="inventory_admin"/>
                    </xsl:if>
                </td>
                <td width="70%">
                    <table width="100%">
                        <tr>
                            <td colspan="6" class="uportal-channel-subtitle"> Inventory management<br/>
                                <hr/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
