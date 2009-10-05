<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!--xsl:include href="./place_menu.xsl"/-->
<xsl:output method="html" indent="no" />
<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>

<xsl:template match="root">
    <table width="100%">
        <tr>
            <td width="20%" valign="top">
                <table width="100%">
                    <tr>
                        <td class="uportal-channel-subtitle">
                            Report list<br/><hr/>
                        </td>
                    </tr>
                    <tr>
                        <td class="uportal-label">
                            <xsl:apply-templates select="tree"/>
                        </td>
                    </tr>
                    
                </table>
            </td>
            <td width="80%" valign="top">
                <xsl:apply-templates select="report"/>
            </td>
        </tr>
    </table>
</xsl:template>

<xsl:template match="tree">
    <xsl:apply-templates select="searchResult[parentId='-1']"/>
</xsl:template>

<xsl:template match="searchResult">
    <xsl:variable name="treeId"><xsl:value-of select="treeId"/></xsl:variable>
    <xsl:variable name="internalId"><xsl:value-of select="internalId"/></xsl:variable>
    <xsl:variable name="nodeName"><xsl:value-of select="nodeName"/></xsl:variable>
    <xsl:variable name="currentDomain"><xsl:value-of select="currentDomain"/></xsl:variable>
    <xsl:variable name="expanded"><xsl:value-of select="expanded"/></xsl:variable>
    <xsl:variable name="url"><xsl:value-of select="url"/></xsl:variable>
   
    <p>
    <xsl:choose>
        <xsl:when test="$expanded='true'">
            <a href="{$baseActionURL}?uP_root=root&amp;expanded=false&amp;hashExpandedKey={$currentDomain}_{$internalId}">
                    <img id="I{$treeId}" src="media/neuragenix/icons/minus.gif" border="0"/>
            </a>
        </xsl:when>
        <xsl:otherwise>
            <a href="{$baseActionURL}?uP_root=root&amp;expanded=true&amp;hashExpandedKey={$currentDomain}_{$internalId}">
                    <img id="I{$treeId}" src="media/neuragenix/icons/plus.gif" border="0"/>
            </a>
        </xsl:otherwise>
    </xsl:choose>
    &#160;
    <a href="{$baseActionURL}?uP_root=root&amp;{$url}">
        <xsl:value-of select="nodeName"/>
    </a>
    </p>
    
    <xsl:choose>
        <xsl:when test="$expanded='true'">
            <div id="{$treeId}" style="margin-left: 5ex; display: block;"> 
                <xsl:apply-templates select="../searchResult[parentDomain=$currentDomain and parentId=$internalId]"/>
            </div>
        </xsl:when>
        <xsl:otherwise>
            <div id="{$treeId}" style="margin-left: 5ex; display: none;"> 
                <xsl:apply-templates select="../searchResult[parentDomain=$currentDomain and parentId=$internalId]"/>
            </div>
        </xsl:otherwise>
    </xsl:choose>
	
</xsl:template>
</xsl:stylesheet>
