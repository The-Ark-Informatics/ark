<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./inventory_menu.xsl"/>
    <xsl:param name="formParams">current=view_site</xsl:param>
    <xsl:output method="html" indent="no"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:template match="inventory">
        <xsl:param name="SITE_intSiteID">
            <xsl:value-of select="SITE_intSiteID"/>
        </xsl:param>
        <xsl:param name="SITE_strSiteName">
            <xsl:value-of select="SITE_strSiteName"/>
        </xsl:param>
        <xsl:param name="SITE_strSiteAddress">
            <xsl:value-of select="SITE_strSiteAddress"/>
        </xsl:param>
        <xsl:param name="SITE_strSiteContact">
            <xsl:value-of select="SITE_strSiteContact"/>
        </xsl:param>
        <xsl:param name="SITE_strSitePhone">
            <xsl:value-of select="SITE_strSitePhone"/>
        </xsl:param>
        <xsl:param name="blLockError">
            <xsl:value-of select="blLockError"/>
        </xsl:param>
        <xsl:param name="hasEditRights">
            <xsl:value-of select="hasEditRights"/>
        </xsl:param>
        <table width="100%">
            <tr valign="top">
                <td width="30%">
                    <table width="250" cellspacing="0" cellpadding="0">
                            <xsl:call-template name="sort_selection"/>
                    </table>
                   
                    
                    <table width="250" cellspacing="0" cellpadding="0">
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
                    <form name="site_view" action="{$baseActionURL}?{$formParams}" method="post">
                        <table width="100%">
                            <tr>
                                <td colspan="6" class="uportal-channel-subtitle">
                                    <xsl:value-of select="SITE_strTitleDisplay"/> Details<br/>
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
                                    <xsl:value-of select="SITE_strSiteNameDisplay"/>: </td>
                                <td width="25%">
                                    <input type="text" name="SITE_strSiteName"
                                        value="{$SITE_strSiteName}" size="22" tabindex="21"
                                        class="uportal-input-text"/>
                                </td>
                                <td width="10%"/>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="SITE_strSiteContactDisplay"/>: </td>
                                <td width="25%">
                                    <input type="text" name="SITE_strSiteContact"
                                        value="{$SITE_strSiteContact}" size="22" tabindex="23"
                                        class="uportal-input-text"/>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="SITE_strSiteAddressDisplay"/>: </td>
                                <td width="25%">
                                    <textarea name="SITE_strSiteAddress" rows="4" cols="20"
                                        tabindex="22" class="uportal-input-text">
                                        <xsl:value-of select="SITE_strSiteAddress"/>
                                    </textarea>
                                </td>
                                <td width="10%"/>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="SITE_strSitePhoneDisplay"/>: </td>
                                <td width="25%">
                                    <input type="text" name="SITE_strSitePhone"
                                        value="{$SITE_strSitePhone}" size="22" tabindex="24"
                                        class="uportal-input-text"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <hr/>
                                </td>
                            </tr>
                        </table>
                        <xsl:if test="$hasEditRights = '1'">
                        <table width="100%">
                            
                            <tr>
                                <td width="50%" align="left">
                                    <a class="button" href="#" onclick="this.blur(); confirmDelete('{$baseActionURL}?current=view_site&amp;SITE_intSiteID={$SITE_intSiteID}')" > <span><img src="/wagerlab/media/neuragenix/icons/delete.png" height="14" align="top" border="0"/> Delete</span></a>
                                </td>
                              
                                <td width="50%" align="right">
                                    <a class="button" name="save" href="#" onclick="this.blur(); document.site_view.submit()"><span><img src="/wagerlab/media/neuragenix/icons/disk.png" height="14" align="top" border="0"/> Save</span></a>  
                                </td>
                            </tr>
                            
                    
                            
                        </table>
                            </xsl:if>
                        <input type="hidden" name="SITE_intSiteID" value="{$SITE_intSiteID}"/>
                    </form>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
