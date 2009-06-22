<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
                                <xsl:when test="mode='NANODROP'">

                                    <h1>Biospecimens for update</h1>
                                    <table>
                                        <tr>
                                            <td/>
                                            <td colspan="2"  class="stripped_column_heading">
                                                <b>Old</b>
                                            </td>
                                            <td colspan="2" class="stripped_column_heading">
                                                <b>Updated</b>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="stripped_column_heading">
                                                <b>Biospecimen ID</b>
                                            </td>
                                            <td class="stripped_column_heading">
                                                <b>Concentration</b>
                                            </td>
                                            <td class="stripped_column_heading">
                                                <b>Purity</b>
                                            </td>
                                            <td class="stripped_column_heading">
                                                <b>Concentration</b>
                                            </td>
                                            <td class="stripped_column_heading">
                                                <b>Purity</b>
                                            </td>
                                        </tr>
                                        <xsl:for-each select="biospecimen">
                                            <tr>
                                                <td><xsl:choose>
                                                    <xsl:when test="position() mod 2 != 0">
                                                        <xsl:attribute name="class">stripped_light</xsl:attribute>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                  <xsl:value-of select="@id"/>
                                                </td>
                                                <td><xsl:choose>
                                                    <xsl:when test="position() mod 2 != 0">
                                                        <xsl:attribute name="class">stripped_light</xsl:attribute>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                  <xsl:value-of select="oldconc"/>
                                                </td>
                                                <td><xsl:choose>
                                                    <xsl:when test="position() mod 2 != 0">
                                                        <xsl:attribute name="class">stripped_light</xsl:attribute>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                  <xsl:value-of select="oldpurity"/>
                                                </td>
                                                <td><xsl:choose>
                                                    <xsl:when test="position() mod 2 != 0">
                                                        <xsl:attribute name="class">stripped_light</xsl:attribute>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                  <xsl:value-of select="newconc"/>
                                                </td>
                                                <td><xsl:choose>
                                                    <xsl:when test="position() mod 2 != 0">
                                                        <xsl:attribute name="class">stripped_light</xsl:attribute>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                  <xsl:value-of select="newpurity"/>
                                                </td>
                                            </tr>
                                        </xsl:for-each>
                                    </table>
                                </xsl:when>
                                <xsl:otherwise>
                                    <h1>Aliquots to be created</h1>
                                    <table>
                                        <tr>
                                            
                                            <td class="stripped_column_heading" colspan="2">
                                                <b>Stock</b>
                                            </td>
                                            <td class="stripped_column_heading"  colspan="3">
                                                <b>Aliquot</b>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="stripped_column_heading">
                                                <b>Biospecimen ID</b>
                                            </td>
                                            <td class="stripped_column_heading">
                                                <b>Vol. Removed</b>
                                            </td>
                                            <td class="stripped_column_heading">
                                                <b>Aliquot ID</b>
                                            </td>
                                            <td class="stripped_column_heading">
                                                <b>Concentration</b>
                                            </td>
                                            <td class="stripped_column_heading">
                                                <b>Volume</b>
                                            </td>
                                        </tr>
                                        <xsl:for-each select="biospecimen">
                                            <tr>
                                                <xsl:choose>
                                                    <xsl:when test="position() mod 2 != 0">
                                                        <xsl:attribute name="class">stripped_light</xsl:attribute>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                <td><xsl:choose>
                                                    <xsl:when test="position() mod 2 != 0">
                                                        <xsl:attribute name="class">stripped_light</xsl:attribute>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                    <xsl:value-of select="@id"/>
                                                </td>
                                                <td><xsl:choose>
                                                    <xsl:when test="position() mod 2 != 0">
                                                        <xsl:attribute name="class">stripped_light</xsl:attribute>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                    <xsl:value-of select="stockvol"/>
                                                </td>
                                                <td><xsl:choose>
                                                    <xsl:when test="position() mod 2 != 0">
                                                        <xsl:attribute name="class">stripped_light</xsl:attribute>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                    <xsl:value-of select="aliquotid"/>
                                                </td>
                                                <td><xsl:choose>
                                                    <xsl:when test="position() mod 2 != 0">
                                                        <xsl:attribute name="class">stripped_light</xsl:attribute>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                    <xsl:value-of select="aliquotconc"/>
                                                </td>
                                                <td><xsl:choose>
                                                    <xsl:when test="position() mod 2 != 0">
                                                        <xsl:attribute name="class">stripped_light</xsl:attribute>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute name="class">stripped_dark</xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                    <xsl:value-of select="aliquotvol"/>
                                                </td>
                                            </tr>
                                        </xsl:for-each>
                                    </table>
                                    
                                    
                                    </xsl:otherwise>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>
                </td>
            </tr>
            <xsl:variable name="action">
                <xsl:choose>
                    <xsl:when test="mode = 'NANODROP'">confirm</xsl:when>
                    <xsl:otherwise>confirm_batch</xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            
            <form name="verify_form" action="{$baseActionURL}?uP_root=root&amp;action={$action}"
            method="post">
            <tr>
                <td></td><td><input type="submit" value="Submit"/></td>
            </tr>
                </form>
        </table>
        <xsl:variable name="action">
            <xsl:choose>
                <xsl:when test="mode = 'NANODROP'">confirm</xsl:when>
                <xsl:otherwise>confirm_batch</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
       
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
