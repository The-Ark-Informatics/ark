<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes"/>



    <xsl:variable name="funcpanelImagePath">media/neuragenix/funcpanel</xsl:variable>

    <xsl:variable name="spacerImagePath">media/neuragenix/infopanel/spacer.gif</xsl:variable>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>

    <xsl:template name="content">
        <xsl:variable name="action">
            <xsl:choose>
                <xsl:when test="mode = 'NANODROP'">verify</xsl:when>
                <xsl:otherwise>verify_batch</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <table width="100%">
            <tr>
                <td valign="top">
                    <xsl:choose>
                        <xsl:when test="count(error) &gt; 0">
                            <img src="img/error.gif"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <img src="img/tick.gif"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </td>
                <td valign="top">
                    <xsl:choose>
                        <xsl:when test="count(error) &gt; 0"> ERROR: There are the following
                            errors with this upload file:<br/>
                            <ul>
                                <xsl:for-each select="error">
                                    <li>
                                        <xsl:value-of select="."/>
                                    </li>
                                </xsl:for-each>
                            </ul>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:choose>
                                <xsl:when test="mode ='NANODROP'">
                                    <h1>Biospecimens to be updated: </h1>
                                    <form name="verify_form"
                                        action="{$baseActionURL}?uP_root=root&amp;action={$action}"
                                        method="post">
                                        <table>
                                            <tr>
                                                <td>
                                                  <b>Biospecimen ID</b>
                                                </td>
                                                <td>
                                                  <b>Concentration</b>
                                                </td>
                                                <td>
                                                  <b>Purity</b>
                                                </td>
                                            </tr>
                                            <xsl:for-each select="biospecimen">
                                                <tr>
                                                  <td>
                                                  <xsl:value-of select="@id"/> </td>
                                                  <td>
                                                  <xsl:if test="conc=1">X</xsl:if>
                                                  </td>
                                                  <td>
                                                  <xsl:if test="purity=1">X</xsl:if>
                                                  </td>
                                                </tr>
                                            </xsl:for-each>
                                            <tr>
                                                <td>
                                                  <input type="submit" value="Submit"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </form> Total number of biospecimens found: <xsl:value-of
                                        select="biocount"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <h1>Biospecimens to be aliquotted </h1>
                                    <form name="verify_form"
                                        action="{$baseActionURL}?uP_root=root&amp;action={$action}"
                                        method="post">
                                        <table>
                                            <tr>
                                                <td>
                                                  <b>Biospecimen ID</b>
                                                </td>
                                            </tr>
                                            <xsl:for-each select="biospecimen">
                                                <tr>
                                                  <td>
                                                  <xsl:value-of select="@id"/></td>
                                                </tr>
                                            </xsl:for-each>
                                            <tr>
                                                <td>
                                                  <input type="submit" value="Submit"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </form> Total number of biospecimens found: <xsl:value-of
                                        select="biocount"/>
                                </xsl:otherwise>
                            </xsl:choose>
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
