<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./inventory_menu.xsl"/>
    <xsl:param name="formParams">current=view_box</xsl:param>
    <xsl:output method="html" indent="no"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="SortType"/>
    <xsl:template match="inventory">
        <xsl:param name="BOX_intBoxID">
            <xsl:value-of select="BOX_intBoxID"/>
        </xsl:param>
        <xsl:param name="BOX_intTankID">
            <xsl:value-of select="BOX_intTankID"/>
        </xsl:param>
        <xsl:param name="BOX_intBoxCapacity">
            <xsl:value-of select="BOX_intBoxCapacity"/>
        </xsl:param>
        <xsl:param name="BOX_intBoxAvailable">
            <xsl:value-of select="BOX_intBoxAvailable"/>
        </xsl:param>
        <xsl:param name="BOX_strBoxName">
            <xsl:value-of select="BOX_strBoxName"/>
        </xsl:param>
        <xsl:param name="BOX_strBoxDescription">
            <xsl:value-of select="BOX_strBoxDescription"/>
        </xsl:param>
        <xsl:param name="blLockError">
            <xsl:value-of select="blLockError"/>
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
                    <form name="saveform" action="{$baseActionURL}?{$formParams}" method="post">
                        <table width="100%">
                            <tr>
                                <td colspan="6" class="uportal-channel-subtitle">
                                    <xsl:value-of select="BOX_strTitleDisplay"/> details<br/>
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
                                    <xsl:value-of select="BOX_intBoxCapacityDisplay"/>: </td>
                                <td width="15%" style="text-align: right" class="uportal-input-text">
                                    <xsl:if test="$BOX_intBoxCapacity &gt; 1">
                                        <xsl:value-of select="BOX_intBoxCapacity"/> cells </xsl:if>
                                    <xsl:if test="$BOX_intBoxCapacity &lt; 2">
                                        <xsl:value-of select="BOX_intBoxCapacity"/> cell </xsl:if>
                                </td>
                                <td width="20%"/>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="BOX_intBoxAvailableDisplay"/>: </td>
                                <td width="15%" style="text-align: right" class="uportal-input-text">
                                    <xsl:if test="$BOX_intBoxAvailable &gt; 1">
                                        <xsl:value-of select="BOX_intBoxAvailable"/> cells </xsl:if>
                                    <xsl:if test="$BOX_intBoxAvailable &lt; 2">
                                        <xsl:value-of select="BOX_intBoxAvailable"/> cell </xsl:if>
                                </td>
                                <td width="10%"/>
                            </tr>
                        </table>
                        <table width="100%">
                            <tr>
                                <td width="20%" class="uportal-label" valign="top">
                                    <xsl:value-of select="TANK_strTitleDisplay"/> name: </td>
                                <td width="25%" valign="top">
                                    <select name="BOX_intTankID" tabindex="1"
                                        class="uportal-input-text">
                                        <xsl:for-each select="search_tank">
                                            <xsl:variable name="varTankID">
                                                <xsl:value-of select="TANK_intTankID"/>
                                            </xsl:variable>
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="TANK_intTankID"/>
                                                </xsl:attribute>
                                                <xsl:if test="$BOX_intTankID=$varTankID">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="TANK_strTankName"/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                                <td width="10%"/>
                                <td width="20%" class="uportal-label" valign="top">
                                    <xsl:value-of select="BOX_strBoxDescriptionDisplay"/>: </td>
                                <td width="25%">
                                    <textarea name="BOX_strBoxDescription" rows="4" cols="20"
                                        tabindex="3" class="uportal-input-text">
                                        <xsl:value-of select="BOX_strBoxDescription"/>
                                    </textarea>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="BOX_strBoxNameDisplay"/>: </td>
                                <td width="25%">
                                    <input type="text" name="BOX_strBoxName"
                                        value="{$BOX_strBoxName}" size="22" tabindex="2"
                                        class="uportal-input-text"/>
                                </td>
                                <td width="10%"/>
                                <td width="20%" class="uportal-label"/>
                                <td width="25%"/>
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
                                    <a class="button" href="#" onclick="this.blur(); confirmDelete('{$baseActionURL}?current=view_box&amp;BOX_intBoxID={$BOX_intBoxID}')" > <span><img src="/wagerlab/media/neuragenix/icons/delete.png" height="14" align="top" border="0"/> Delete</span></a>
                                </td>
                            	<input type="hidden" name="save"/> 
                                <td width="50%" align="right">
                                    <a class="button" name="savebtn" href="#" onclick="this.blur(); document.saveform.save.value = '1'; document.saveform.submit()"><span><img src="/wagerlab/media/neuragenix/icons/disk.png" height="14" align="top" border="0"/> Save</span></a>  
                                </td>
                            </tr>
                            
                            
                            
                           
                        </table>
                        </xsl:if>
                        <input type="hidden" name="BOX_intBoxID" value="{$BOX_intBoxID}"/>
                    </form>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
