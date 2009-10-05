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
                <td width="70%">
                    <form action="{$baseActionURL}?{$formParams}" method="post">
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
                                <xsl:choose>
                        <xsl:when test="TANK_dtCommissionDate/@display_type='text'">
                            <input type="text" name="TANK_dtCommissionDate_Day" size="2" tabindex="24">
                                <xsl:attribute name="value"><xsl:value-of select="TANK_dtCommissionDate_Day"/></xsl:attribute>
                            </input>
                            <input type="text" name="TANK_dtCommissionDate_Month" size="2" tabindex="25">
                                    <xsl:attribute name="value"><xsl:value-of select="TANK_dtCommissionDate_Month"/></xsl:attribute>
                            </input>
                            </xsl:when>
                            <xsl:otherwise>
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
                                </xsl:otherwise>
                                </xsl:choose>
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
                                    <xsl:choose>
                                    <xsl:when test="TANK_dtLastServiceDate/@display_type='text'">
                                        <input type="text" name="TANK_dtLastServiceDate_Day" size="2" tabindex="24">
                                            <xsl:attribute name="value"><xsl:value-of select="TANK_dtLastServiceDate_Day"/></xsl:attribute>
                                        </input>
                                        <input type="text" name="TANK_dtLastServiceDate_Month" size="2" tabindex="25">
                                                <xsl:attribute name="value"><xsl:value-of select="TANK_dtLastServiceDate_Month"/></xsl:attribute>
                                        </input>
                                    </xsl:when>
                                    <xsl:otherwise>
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
                                    </xsl:otherwise>
                                    </xsl:choose>
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
                                    <xsl:value-of select="TANK_dtDecommissionDateDisplay"/>: </td>
                                <td width="25%">
                                <xsl:choose>
                        <xsl:when test="TANK_dtCommissionDate/@display_type='text'">
                            <input type="text" name="TANK_dtDecommissionDate_Day" size="2" tabindex="24">
                                <xsl:attribute name="value"><xsl:value-of select="TANK_dtDecommissionDate_Day"/></xsl:attribute>
                            </input>
                            <input type="text" name="TANK_dtDecommissionDate_Month" size="2" tabindex="25">
                                    <xsl:attribute name="value"><xsl:value-of select="TANK_dtDecommissionDate_Month"/></xsl:attribute>
                            </input>
                            </xsl:when>
                            <xsl:otherwise>
                                    <select name="TANK_dtDecommissionDate_Day" tabindex="13"
                                        class="uportal-input-text">
                                        <xsl:for-each select="TANK_dtDecommissionDate_Day">
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
                                    <select name="TANK_dtDecommissionDate_Month" tabindex="14"
                                        class="uportal-input-text">
                                        <xsl:for-each select="TANK_dtDecommissionDate_Month">
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
                                </xsl:otherwise>
                                </xsl:choose>
                                    <input type="text" name="TANK_dtDecommissionDate_Year"
                                        value="{$TANK_dtDecommissionDate_Year}" size="5"
                                        tabindex="15" class="uportal-input-text"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <hr/>
                                </td>
                            </tr>
                        </table>
                        <table width="100%">
                            <tr>
                                <td width="20%">
                                    <input type="submit" name="save" tabindex="16" value="Save"
                                        class="uportal-button"/>
                                    <input type="button" name="delete" value="Delete" tabindex="17"
                                        class="uportal-button"
                                        onclick="javascript:confirmDelete('{$baseActionURL}?current=view_tank&amp;TANK_intTankID={$TANK_intTankID}')"
                                    />
                                </td>
                                <td width="80%"/>
                            </tr>
                        </table>
                        <input type="hidden" name="TANK_intTankID" value="{$TANK_intTankID}"/>
                    </form>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
