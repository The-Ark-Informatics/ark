<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" indent="yes"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="funcpanelImagePath">media/neuragenix/funcpanel</xsl:variable>
    
    <xsl:variable name="spacerImagePath">media/neuragenix/infopanel/spacer.gif</xsl:variable>
<xsl:template name="content">
<table width="100%">
<tr>
<td valign="top">
<xsl:choose>
<xsl:when test="count(error) &gt; 0">
<img src="media/org/wager/error.gif"/>
</xsl:when>
<xsl:otherwise>
<img src="media/org/wager/tick.gif"/>
</xsl:otherwise>
</xsl:choose>
</td>
<td >
    <xsl:choose>
        <xsl:when test="mode='NANODROP'">
            <h1> The Nanodrop data upload was completed successfully.</h1>
        </xsl:when>
        <xsl:otherwise>
           <h1>The aliquots were created successfully.</h1>
            </xsl:otherwise>
    </xsl:choose>

</td>
</tr>
</table>
</xsl:template>

    <xsl:template match="/nanodrop">
        <xsl:variable name="title">
            <xsl:choose>
                <xsl:when test="mode = 'NANODROP'">NANODROP UPLOAD</xsl:when>
                <xsl:otherwise>BATCH ALIQUOT</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <html>
            <body>
                <br/>
                <br/>
                <table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="80%">
                    <tr valign="bottom">
                        <td>
                            <img src="{$funcpanelImagePath}/funcpanel_header_active_left.gif"/>
                        </td>
                        <td class="funcpanel_header_active" align="left" colspan="3" width="100%">
                            <xsl:value-of select="$title"/>
                        </td>
                        <td>
                            <img src="{$funcpanelImagePath}/funcpanel_header_active_right.gif"/>
                        </td>
                    </tr>
                    <tr class="funcpanel_content">
                        <td class="funcpanel_content_spacer" colspan="5">
                            <img width="1" height="4" src="{$spacerImagePath}"/>
                        </td>
                    </tr>
                    <tr class="funcpanel_content">
                        <td class="funcpanel_left_border">&#160;</td>
                        <td colspan="3">
                            <xsl:call-template name="content"/>
                        </td>
                        <td class="funcpanel_right_border">&#160;</td>
                    </tr>
                    <tr class="funcpanel_content">
                        <td class="funcpanel_bottom_border" colspan="5">
                            <img width="1" height="4" src="{$spacerImagePath}"/>
                        </td>
                    </tr>
                </table>
                
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
