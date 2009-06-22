<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./inventory_menu.xsl"/>
    <xsl:param name="formParams">current=view_tank</xsl:param>
    <xsl:output method="html" indent="no"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:template match="inventory">
        <xsl:param name="TANK_intTankID">
            <xsl:value-of select="TANK_intTankID"/>
        </xsl:param>
        <xsl:param name="TANK_dtCommissionDate_Year">
            <xsl:value-of select="TANK_dtCommissionDate_Year"/>
        </xsl:param>
        <xsl:param name="TANK_dtDecommissionDate_Year">
            <xsl:value-of select="TANK_dtDecommissionDate_Year"/>
        </xsl:param>
        <xsl:param name="TANK_dtLastServiceDate_Year">
            <xsl:value-of select="TANK_dtLastServiceDate_Year"/>
        </xsl:param>
        <xsl:param name="TANK_intSiteID">
            <xsl:value-of select="TANK_intSiteID"/>
        </xsl:param>
        <xsl:param name="TANK_strTankName">
            <xsl:value-of select="TANK_strTankName"/>
        </xsl:param>
        <xsl:param name="TANK_strTankLocation">
            <xsl:value-of select="TANK_strTankLocation"/>
        </xsl:param>
        <xsl:param name="TANK_strTankDescription">
            <xsl:value-of select="TANK_strTankDescription"/>
        </xsl:param>
        <xsl:param name="TANK_intTankCapacity">
            <xsl:value-of select="TANK_intTankCapacity"/>
        </xsl:param>
        <xsl:param name="TANK_intTankAvailable">
            <xsl:value-of select="TANK_intTankAvailable"/>
        </xsl:param>
        <xsl:param name="TANK_strStatus">
            <xsl:value-of select="TANK_strStatus"/>
        </xsl:param>
        <xsl:param name="TANK_strLastServiceNote">
            <xsl:value-of select="TANK_strLastServiceNote"/>
        </xsl:param>
        <xsl:param name="blLockError">
            <xsl:value-of select="blLockError"/>
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
                    <form name="saveform" action="{$baseActionURL}?{$formParams}" method="post">
                        <table width="100%">
                            <tr>
                                <td colspan="6" class="uportal-channel-subtitle">
                                    <xsl:value-of select="TANK_strTitleDisplay"/> details<br/>
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
                                    <xsl:value-of select="TANK_intTankCapacityDisplay"/>: </td>
                                <td width="15%" style="text-align: right" class="uportal-input-text">
                                    <xsl:if test="$TANK_intTankCapacity &gt; 1">
                                        <xsl:value-of select="TANK_intTankCapacity"/> cells </xsl:if>
                                    <xsl:if test="$TANK_intTankCapacity &lt; 2">
                                        <xsl:value-of select="TANK_intTankCapacity"/> cell </xsl:if>
                                </td>
                                <td width="20%"/>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TANK_intTankAvailableDisplay"/>: </td>
                                <td width="15%" style="text-align: right" class="uportal-input-text">
                                    <xsl:if test="$TANK_intTankAvailable &gt; 1">
                                        <xsl:value-of select="TANK_intTankAvailable"/> cells </xsl:if>
                                    <xsl:if test="$TANK_intTankAvailable &lt; 2">
                                        <xsl:value-of select="TANK_intTankAvailable"/> cell
                                    </xsl:if>
                                </td>
                                <td width="10%"/>
                            </tr>
                        </table>
                        <table width="100%">
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="SITE_strTitleDisplay"/> name: </td>
                                <td width="25%">
                                    <select name="TANK_intSiteID" tabindex="1"
                                        class="uportal-input-text">
                                        <xsl:for-each select="search_site">
                                            <xsl:variable name="varSiteID">
                                                <xsl:value-of select="SITE_intSiteID"/>
                                            </xsl:variable>
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="SITE_intSiteID"/>
                                                </xsl:attribute>
                                                <xsl:if test="$TANK_intSiteID=$varSiteID">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="SITE_strSiteName"/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                                <td width="10%"/>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TANK_strStatusDisplay"/>: </td>
                                <td width="25%">
                                    <select name="TANK_strStatus" tabindex="5"
                                        class="uportal-input-text">
                                        <xsl:for-each select="TANK_strStatus">
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TANK_strTankNameDisplay"/>: </td>
                                <td width="25%">
                                    <input type="text" name="TANK_strTankName"
                                        value="{$TANK_strTankName}" size="22" tabindex="2"
                                        class="uportal-input-text"/>
                                </td>
                                <td width="10%"/>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TANK_dtCommissionDateDisplay"/>: </td>
                                <td width="25%">
                                    <select name="TANK_dtCommissionDate_Day" tabindex="6"
                                        class="uportal-input-text">
                                        <xsl:for-each select="TANK_dtCommissionDate_Day">
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                    <select name="TANK_dtCommissionDate_Month" tabindex="7"
                                        class="uportal-input-text">
                                        <xsl:for-each select="TANK_dtCommissionDate_Month">
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                    <input type="text" name="TANK_dtCommissionDate_Year"
                                        value="{$TANK_dtCommissionDate_Year}" size="5" tabindex="8"
                                        class="uportal-input-text"/>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TANK_strTankLocationDisplay"/>: </td>
                                <td width="25%">
                                    <textarea name="TANK_strTankLocation" rows="4" cols="20"
                                        tabindex="3" class="uportal-input-text">
                                        <xsl:value-of select="TANK_strTankLocation"/>
                                    </textarea>
                                </td>
                                <td width="10%"/>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TANK_dtLastServiceDateDisplay"/>: </td>
                                <td width="25%">
                                    <select name="TANK_dtLastServiceDate_Day" tabindex="9"
                                        class="uportal-input-text">
                                        <xsl:for-each select="TANK_dtLastServiceDate_Day">
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                    <select name="TANK_dtLastServiceDate_Month" tabindex="10"
                                        class="uportal-input-text">
                                        <xsl:for-each select="TANK_dtLastServiceDate_Month">
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                    <input type="text" name="TANK_dtLastServiceDate_Year"
                                        value="{$TANK_dtLastServiceDate_Year}" size="5"
                                        tabindex="11" class="uportal-input-text"/>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TANK_strTankDescriptionDisplay"/>: </td>
                                <td width="25%">
                                    <textarea name="TANK_strTankDescription" rows="4" cols="20"
                                        tabindex="4" class="uportal-input-text">
                                        <xsl:value-of select="TANK_strTankDescription"/>
                                    </textarea>
                                </td>
                                <td width="10%"/>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TANK_strLastServiceNoteDisplay"/>: </td>
                                <td width="25%">
                                    <textarea name="TANK_strLastServiceNote" rows="4" cols="20"
                                        tabindex="12" class="uportal-input-text">
                                        <xsl:value-of select="TANK_strLastServiceNote"/>
                                    </textarea>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%" class="uportal-label"/>
                                <td width="25%"/>
                                <td width="10%"/>
                                <td width="20%" class="uportal-label">
                                     </td>
                                <td width="25%">
                                </td>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <hr/>
                                </td>
                            </tr>
                        </table>
                        <xsl:if test="hasEditRights">
                        <table width="100%">
                            
                           
                            <tr>
                                <td width="50%" align="left">
                                    <a class="button" href="#" onclick="this.blur(); confirmDelete('{$baseActionURL}?current=view_tank&amp;TANK_intTankID={$TANK_intTankID}')" > <span><img src="/wagerlab/media/neuragenix/icons/delete.png" height="14" align="top" border="0"/> Delete</span></a>
                                </td>
                                
                                <td width="50%" align="right">
                                    <a class="button" name="save" href="#" onclick="this.blur(); document.saveform.submit()"><span><img src="/wagerlab/media/neuragenix/icons/disk.png" height="14" align="top" border="0"/> Save</span></a>  
                                </td>
                            </tr>
                            
                        </table>
                            
                            
                           
                                         </xsl:if>
                        <input type="hidden" name="TANK_intTankID" value="{$TANK_intTankID}"/>
                    </form>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
