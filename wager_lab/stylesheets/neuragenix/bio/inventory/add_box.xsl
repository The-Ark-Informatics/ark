<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./inventory_menu.xsl"/>

    <xsl:param name="formParams">current=add_box</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    
  
    <xsl:template match="inventory">
    
    
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
                            Add New <xsl:value-of select="BOX_strTitleDisplay" /><br/><hr/>
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
                            <xsl:value-of select="TANK_strTitleDisplay" /> name:
                        </td>
                        <td width="25%">
                            <select name="BOX_intTankID" tabindex="1" class="uportal-input-text">
				<xsl:for-each select="search_tank">
                                    
                                    <option>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="TANK_intTankID" />
                                        </xsl:attribute>
                                        
                                       <xsl:value-of select="SITE_strSiteName" /> &gt; <xsl:value-of select="TANK_strTankName" />
                                    </option>
				</xsl:for-each>
                            </select>
                        </td>
                       
                    </tr>
                    
                    <tr><td width="1%" class="neuragenix-form-required-text">*</td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="BOX_strBoxNameDisplay" />:
                        </td>
                        <td width="40%">
                            <input type="text" name="BOX_strBoxName" size="22" tabindex="2" class="uportal-input-text" />
                        </td>
                        <td width="5%"></td>
                        <td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="19%" class="uportal-label">
                            
                        </td>
                        <td width="30%">
                        </td>
                    </tr>
                    <tr>
                        <td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="19%" valign="top" class="uportal-label">
                            <xsl:value-of select="BOX_strBoxDescriptionDisplay" />:
                        </td>
                        <td width="40%" valign="top">
                            <textarea name="BOX_strBoxDescription" rows="4" cols="20" tabindex="3" class="uportal-input-text">                                
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
                            <input type="submit" name="save" tabindex="4" value="Save" class="uportal-button" />
                            <input type="button" name="clear" value="Clear" tabindex="5" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=add_box')" />
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
