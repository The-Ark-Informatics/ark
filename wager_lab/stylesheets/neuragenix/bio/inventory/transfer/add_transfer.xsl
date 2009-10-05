<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="../inventory_menu.xsl"/>
    <xsl:param name="formParams">current=add_transfer</xsl:param>
    <xsl:output method="html" indent="no"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="SortType"/>
    <xsl:template match="inventory">
        
        <xsl:param name="blLockError">
            <xsl:value-of select="blLockError"/>
        </xsl:param>
        <xsl:param name="strTrayName">
            <xsl:value-of select="strTrayName"/>
        </xsl:param>
        <xsl:param name="currentTrayID">
            <xsl:value-of select="intCurrentTray"/>
        </xsl:param>
        <table width="100%">
            <tr valign="top">
                <td width="30%">
                    <table width="250" cellpadding="0" cellspacing="0">
                            <xsl:call-template name="sort_selection"/>
                    </table>
                    
                    <table width="250"  cellpadding="0" cellspacing="0">
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
                    <br/>
                    <br/>
                    <br/>
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
                    <form action="{$baseActionURL}?{$formParams}" method="post">
                        <input type="hidden" name="TRANSFER_intTrayID" value="{$currentTrayID}"/>
                        <table width="100%">
                            <tr>
                                <td colspan="6" class="uportal-channel-subtitle">
                                   Request to transfer a box off-site<br/>
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
                        <table width="100%">
                            <xsl:if test="$blLockError = 'true'">
                                <tr>
                                    <td colspan="5" class="neuragenix-form-required-text"> This
                                        record is being viewed by other users. You cannot update it
                                        now. Please try again later. </td>
                                </tr>
                                <tr>
                                    <td height="10px"/>
                                </tr>
                            </xsl:if>
                            <tr>
                                <td width="20%" class="uportal-label">
                                    Tray: </td>
                                <td width="15%" style="text-align: right" class="uportal-input-text">
                                   
                                        <xsl:value-of select="strTrayName"/>
                                    
                                </td>
                                <td width="20%"/>
                                <td width="20%" class="uportal-label">
                                    Current Site: </td>
                                <td width="15%" style="text-align: right" class="uportal-input-text">
                                    <xsl:value-of select="intCurrentSite" />
                                                                      </td>
                                <td width="10%"/>
                            </tr>
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TRANSFER_intSiteIDDisplay"/>: </td>
                                <td width="15%" style="text-align: right" class="uportal-input-text">
                                    <select name="TRANSFER_intSiteID">
                                        <xsl:for-each select="search_site">
                                            <option>
                                                <xsl:attribute name="value"><xsl:value-of select="SITE_intSiteID" /></xsl:attribute>
                                                <xsl:value-of select="SITE_strSiteName" />
                                            </option>
                                        </xsl:for-each>
                                    </select> 
                                </td>
                                <td width="10%"/>
                                
                                
                            </tr>
                            
                        </table>
                        
                        <table width="100%">
                            <tr>
                                <td width="10%">
                                    <input type="submit" name="save" tabindex="4" value="Save"
                                        class="uportal-button"/>
                                    
                                </td>
                                <td width="80%"/>
                            </tr>
                        </table>
                       
                    </form>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
