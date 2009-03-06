<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>
    <xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
    <xsl:variable name="subtabImagePath">media/neuragenix/infopanel</xsl:variable>
    <xsl:template name="infopaneltop">
        <xsl:param name="titleString"/>
        <tr valign="bottom">
            <td>
                <img border="0" src="{$infopanelImagePath}/infopanel_top_left1.gif" width="5" height="27"/>
            </td>
            <td>
                <img border="0" src="{$infopanelImagePath}/infopanel_top_left2.gif" width="5" height="27"/>
            </td>
            <td width="100%" height="27" style="background-image: url('{$infopanelImagePath}/infopanel_top_border.gif'); background-repeat: repeat-x;">
                <table width="100%" height="100%" cellpadding="0" cellspacing="0">
                    <tr valign="bottom">
                        <!-- Patient ID -->
                        <td class="infopanel_title" align="left">
                            
                                    <xsl:value-of select="$titleString"/>
                            
                        </td>
                        <!-- Subtabs -->
                        <td align="right">
                           
                        </td>
                    </tr>
                </table>
            </td>
            <td>
                <img border="0" src="{$infopanelImagePath}/infopanel_top_right1.gif" width="5" height="27"/>
            </td>
            <td>
                <img border="0" src="{$infopanelImagePath}/infopanel_top_right2.gif" width="5" height="27"/>
            </td>
        </tr>
        
      
</xsl:template>
    <xsl:template name="infopanelbottom">
        <tr valign="top">
            <td>
                <img border="0" src="{$infopanelImagePath}/infopanel_bottom_left1.gif" width="5" height="8"/>
            </td>
            <td>
                <img border="0" src="{$infopanelImagePath}/infopanel_bottom_left2.gif" width="5" height="8"/>
            </td>
            <td width="100%" height="8" style="background-image: url('{$infopanelImagePath}/infopanel_bottom_border.gif'); background-repeat: repeat-x;">&#160;</td>
            <td>
                <img border="0" src="{$infopanelImagePath}/infopanel_bottom_right1.gif" width="5" height="8"/>
            </td>
            <td>
                <img border="0" src="{$infopanelImagePath}/infopanel_bottom_right2.gif" width="5" height="8"/>
            </td>
        </tr>
        </xsl:template>
</xsl:stylesheet>