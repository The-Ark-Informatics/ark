<?xml version="1.0" encoding="utf-8"?>

<!-- isearch_menu.xsl. Menu used for intelligent advanced search.-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:variable name="funcpanelImagePath">media/neuragenix/funcpanel</xsl:variable>
    <xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
    <xsl:variable name="spacerImagePath">media/neuragenix/infopanel/spacer.gif</xsl:variable>
<xsl:output method="html" indent="no"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>

    <xsl:template match="root"> 
  
        <table width="100%">
            <tr valign="top">
                <td id="neuragenix-border-right" width="30%" class="uportal-channel-subtitle">
                   <!-- Menu<hr></hr>
                    <br></br>
                    <a href="{$baseActionURL}?uP_root=root&amp;current=build_isearch&amp;addNew=true">New search</a><br /><br /><br />
                    
                    Favourite queries:<br />
                    <xsl:for-each select="isearch/query_list">                    
                        <xsl:variable name="intQueryID"><xsl:value-of select="FAVOURITEQUERY_intQueryID" /></xsl:variable>
                        <a href="{$baseActionURL}?uP_root=root&amp;current=build_isearch&amp;edit=true&amp;strQueryID={$intQueryID}"><xsl:value-of select="FAVOURITEQUERY_strQueryName"/></a><br />
                    </xsl:for-each>-->
                    <table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="250">
                        <tr valign="bottom">
                            <td><img src="{$funcpanelImagePath}/funcpanel_header_active_left.gif"/></td>
                            <td class="funcpanel_header_active" align="left" colspan="1" width="100%">MENU</td>
                            <td><img src="{$funcpanelImagePath}/funcpanel_header_active_right.gif"/></td>
                        </tr>
                        <tr class="funcpanel_content">
                            <td class="funcpanel_content_spacer" colspan="3"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                        </tr>
                        <tr class="funcpanel_content">
                            <td class="funcpanel_left_border">&#160;</td>
                            <td class="form_label"><a href="{$baseActionURL}?uP_root=root&amp;current=build_isearch&amp;addNew=true">New search</a><br /><br /><br />
                                
                                Favourite queries:<br />
                                <xsl:for-each select="isearch/query_list">                    
                                    <xsl:variable name="intQueryID"><xsl:value-of select="FAVOURITEQUERY_intQueryID" /></xsl:variable>
                                    <a href="{$baseActionURL}?uP_root=root&amp;current=build_isearch&amp;edit=true&amp;strQueryID={$intQueryID}"><xsl:value-of select="FAVOURITEQUERY_strQueryName"/></a><br />
                                </xsl:for-each></td>
                            <td class="funcpanel_right_border">&#160;</td>
                        </tr>
                        
                        <tr class="funcpanel_content">
                            <td class="funcpanel_bottom_border" colspan="3"><img width="1" height="4" src="{$spacerImagePath}"/></td>
                        </tr>
                    </table>
                </td>
                
                <td width="70%">
                    <xsl:apply-templates select="isearch"/>
                </td>
            </tr>
        </table> 
	
    </xsl:template>
</xsl:stylesheet>

