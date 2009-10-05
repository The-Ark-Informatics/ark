<?xml version="1.0" encoding="utf-8"?>

<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./inventory_menu.xsl"/>
    <xsl:param name="formParams">current=view_tray</xsl:param>
    <xsl:output method="html" indent="no"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:param name="downloadURL">downloadURL_false</xsl:param>
    <xsl:param name="nodeId">nodeId_false</xsl:param>
    <xsl:param name="biospecimenChannelURL">biospecimenChannelURL_false</xsl:param>
    <xsl:param name="biospecimenTabOrder">biospecimenTabOrder</xsl:param>
   
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
                    <xsl:variable name="varAdminSection">
                        <xsl:value-of select="intAdminSection"/>
                    </xsl:variable>
                    <xsl:if test="$varAdminSection=1">
                        <xsl:call-template name="inventory_admin"/>
                    </xsl:if>
                </td>
                <td width="70%" style="text-align: center">
                        <table width="100%">
                            <tr>
                                <td colspan="5" class="uportal-channel-subtitle">
                                </td>
                                <td align="right">
                                </td>
                            </tr>
                            <tr>
                                <td><br/></td>
                            </tr>
                            <tr>
                                <td colspan="6">
                                    <hr/>
                                </td>
                            </tr>
                        </table>
                        <table width="100%">
                            <tr>
                                <td class="neuragenix-form-required-text">
                                    <xsl:value-of select="strErrorMessage"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                </table>
    </xsl:template>
    <xsl:template match="sort_type"/>
    <xsl:template match="current_view"/>
    <xsl:template match="current_ID"/>
    <xsl:template match="current_field_string"/>
    <xsl:template match="strSource"/>
</xsl:stylesheet>
