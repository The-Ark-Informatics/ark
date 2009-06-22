<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./inventory_menu.xsl"/>

    <xsl:param name="formParams">current=add_tank</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    
  
    <xsl:template match="inventory">
    <xsl:param name="TANK_dtCommissionDate_Year"><xsl:value-of select="TANK_dtCommissionDate_Year" /></xsl:param>
    <xsl:param name="TANK_dtLastServiceDate_Year"><xsl:value-of select="TANK_dtLastServiceDate_Year" /></xsl:param>
    
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
                    <tr class="funcpanel_content">
                        <td colspan="11" class="funcpanel_bottom_border">
                            <img src="media/neuragenix/infopanel/spacer.gif" height="4"
                                width="1"/>
                        </td>
                    </tr>
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
                <table width="100%">
                    <tr>
                        <td colspan="6" class="uportal-channel-subtitle">
                            Add New <xsl:value-of select="TANK_strTitleDisplay" /><br/><hr/>
                        </td>
                    </tr>
                    
                </table>
                
                <table width="100%">
                    <tr>
                        <td class="neuragenix-form-required-text">
                            <xsl:value-of select="strErrorMessage" />
                        </td>
                    </tr>
                </table>
                
                <table width="100%">
                    <tr><td width="1%" class="neuragenix-form-required-text">*</td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="SITE_strTitleDisplay" /> name:
                        </td>
                        <td width="25%">
                            <select name="TANK_intSiteID" tabindex="1" class="uportal-input-text">
				<xsl:for-each select="search_site">
                                    
                                    <option>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="SITE_intSiteID" />
                                        </xsl:attribute>
                                        
                                        <xsl:value-of select="SITE_strSiteName" />
                                    </option>
				</xsl:for-each>
                            </select>
                        </td>
                        <td width="10%"></td>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="TANK_strStatusDisplay" />:
                        </td>
                        <td width="25%">
                            <select name="TANK_strStatus" tabindex="5" class="uportal-input-text">
				<xsl:for-each select="TANK_strStatus">
		
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="." />
                                        </xsl:attribute> 
                                        <xsl:if test="@selected=1">
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>

                                        <xsl:value-of select="." />		
                                    </option>
				</xsl:for-each>
                            </select>
                        </td>
                    </tr>
                    
                    <tr><td width="1%" class="neuragenix-form-required-text">*</td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="TANK_strTankNameDisplay" />:
                        </td>
                        <td width="25%">
                            <input type="text" name="TANK_strTankName" size="22" tabindex="2" class="uportal-input-text" />
                        </td>
                        <td width="10%"></td>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="TANK_dtCommissionDateDisplay" />:
                        </td>
                        <td width="25%">
                            <select name="TANK_dtCommissionDate_Day" tabindex="6" class="uportal-input-text">
				<xsl:for-each select="TANK_dtCommissionDate_Day">
		
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="." />
                                        </xsl:attribute> 
                                        <xsl:if test="@selected=1">
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>

                                        <xsl:value-of select="." />		
                                    </option>
				</xsl:for-each>
                            </select>
                            
                            <select name="TANK_dtCommissionDate_Month" tabindex="7" class="uportal-input-text">
				<xsl:for-each select="TANK_dtCommissionDate_Month">
		
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="." />
                                        </xsl:attribute> 
                                        <xsl:if test="@selected=1">
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>

                                        <xsl:value-of select="." />		
                                    </option>
				</xsl:for-each>
                            </select>
                            
                            <input type="text" name="TANK_dtCommissionDate_Year" value="{$TANK_dtCommissionDate_Year}" size="5" tabindex="8" class="uportal-input-text" />
                        </td>
                    </tr>
                    
                    <tr><td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="TANK_strTankLocationDisplay" />:
                        </td>
                        <td width="25%">
                            <textarea name="TANK_strTankLocation" rows="4" cols="20" tabindex="3" class="uportal-input-text">
                                
                            </textarea>
                        </td>
                        <td width="10%"></td>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="TANK_dtLastServiceDateDisplay" />:
                        </td>
                        <td width="25%">
                            <select name="TANK_dtLastServiceDate_Day" tabindex="9" class="uportal-input-text">
				<xsl:for-each select="TANK_dtLastServiceDate_Day">
		
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="." />
                                        </xsl:attribute> 
                                        <xsl:if test="@selected=1">
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>

                                        <xsl:value-of select="." />		
                                    </option>
				</xsl:for-each>
                            </select>
                            
                            <select name="TANK_dtLastServiceDate_Month" tabindex="10" class="uportal-input-text">
				<xsl:for-each select="TANK_dtLastServiceDate_Month">
		
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="." />
                                        </xsl:attribute> 
                                        <xsl:if test="@selected=1">
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>

                                        <xsl:value-of select="." />		
                                    </option>
				</xsl:for-each>
                            </select>
                            
                            <input type="text" name="TANK_dtLastServiceDate_Year" value="{$TANK_dtLastServiceDate_Year}" size="5" tabindex="11" class="uportal-input-text" />
                        </td>
                    </tr>
                    
                    <tr><td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="TANK_strTankDescriptionDisplay" />:
                        </td>
                        <td width="25%">
                            <textarea name="TANK_strTankDescription" rows="4" cols="20" tabindex="4" class="uportal-input-text">                                
                            </textarea>
                        </td>
                        <td width="10%"></td>
                        <td width="20%" class="uportal-label">
                            <xsl:value-of select="TANK_strLastServiceNoteDisplay" />:
                        </td>
                        <td width="25%">
                            <textarea name="TANK_strLastServiceNote" rows="4" cols="20" tabindex="12" class="uportal-input-text">
                                
                            </textarea>
                        </td>
                    </tr>
                    
                    <tr>
                        <td colspan="7"><hr /></td>
                    </tr>
                </table>
                
                <table width="100%">
                    <tr>	
                        <td width="20%">
                            <input type="submit" name="save" tabindex="13" value="Save" class="uportal-button" />
                            <input type="button" name="clear" value="Clear" tabindex="14" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=add_tank')" />
                        </td>
                        <td width="80%"></td>
                    </tr>
                </table>
    </form>
                
            </td>
        </tr>
    </table>
    </xsl:template>

</xsl:stylesheet>
