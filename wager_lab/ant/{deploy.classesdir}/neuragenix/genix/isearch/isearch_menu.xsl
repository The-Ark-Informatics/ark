<?xml version="1.0" encoding="utf-8"?>

<!-- isearch_menu.xsl. Menu used for intelligent advanced search.-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html" indent="no"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>

    <xsl:template match="root"> 
  
        <table width="100%">
            <tr valign="top">
                <td id="neuragenix-border-right" width="15%" class="uportal-channel-subtitle">
                    Menu<hr></hr>
                    <br></br>
                    <a href="{$baseActionURL}?uP_root=root&amp;current=build_isearch&amp;addNew=true">New search</a><br /><br /><br />
                    
                    Favourite queries:<br />
                    <xsl:for-each select="isearch/query_list">                    
                        <xsl:variable name="intQueryID"><xsl:value-of select="FAVOURITEQUERY_intQueryID" /></xsl:variable>
                        <a href="{$baseActionURL}?uP_root=root&amp;current=build_isearch&amp;edit=true&amp;strQueryID={$intQueryID}"><xsl:value-of select="FAVOURITEQUERY_strQueryName"/></a><br />
                    </xsl:for-each>
                </td>
                <td width="5%"></td>
                <td width="80%">
                    <xsl:apply-templates select="isearch"/>
                </td>
            </tr>
        </table> 
	
    </xsl:template>
</xsl:stylesheet>