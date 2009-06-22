<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" indent="no" />
<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
<xsl:template match="/">
<!--
<script language="javascript" >
        
        var openImg = new Image();
        openImg.src = "media/neuragenix/icons/minus.gif";
        var closedImg = new Image();
        closedImg.src = "media/neuragenix/icons/plus.gif";

        function toggleBranch(branch){
                var objBranch = document.getElementById(branch).style;
                if(objBranch.display=="block")
                        objBranch.display="none";
                else
                        objBranch.display="block";
                swapFolder('I' + branch);
        }

        function swapFolder(img){
                objImg = document.getElementById(img);
                if(objImg.src.indexOf(closedImg.src)>-1)
                        objImg.src = openImg.src;
                else
                        objImg.src = closedImg.src;
        }

</script>
-->
<xsl:apply-templates/>
</xsl:template>

<xsl:template match="tree">
	<table width="30%">
<!--		<xsl:apply-templates select="searchResult[parentId='-1']"/>
-->
    </table>	
	<table width="70%">
	</table>
<xsl:apply-templates/>
</xsl:template>

<xsl:template match="searchResult">
<xsl:variable name="treeId"><xsl:value-of select="treeId"/></xsl:variable>
<xsl:variable name="internalId"><xsl:value-of select="internalId"/></xsl:variable>
<xsl:variable name="nodeName"><xsl:value-of select="nodeName"/></xsl:variable>
<xsl:variable name="currentDomain"><xsl:value-of select="currentDomain"/></xsl:variable>
<xsl:variable name="expanded"><xsl:value-of select="expanded"/></xsl:variable>
<xsl:variable name="url"><xsl:value-of select="url"/></xsl:variable>
	<p>
		<!--
		<a href="javascript:toggleBranch({$treeId})">
			<img id="I{$treeId}" src="media/neuragenix/icons/minus.gif" border="0"/>
		</a>
		<a href="{$url}">
			<xsl:value-of select="nodeName"/>
		</a>
		-->
		<xsl:choose>
			<xsl:when test="$expanded='true'">
				<a href="{$baseActionURL}?expanded=false&amp;hashExpandedKey={$currentDomain}_{$internalId}">
					<img id="I{$treeId}" src="media/neuragenix/icons/minus.gif" border="0"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="{$baseActionURL}?expanded=true&amp;hashExpandedKey={$currentDomain}_{$internalId}">
					<img id="I{$treeId}" src="media/neuragenix/icons/plus.gif" border="0"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
		<a href="{$baseActionURL}?{$url}">
			<xsl:value-of select="nodeName"/>
		</a>
	</p>
	<xsl:choose>
		<xsl:when test="$expanded='true'">
    		<div id="{$treeId}" style="margin-left: 3%; display: block;"> 
    			<xsl:apply-templates select="../searchResult[parentDomain=$currentDomain and parentId=$internalId]"/>
    		</div>
		</xsl:when>
		<xsl:otherwise>
    		<div id="{$treeId}" style="margin-left: 3%; display: none;"> 
    			<xsl:apply-templates select="../searchResult[parentDomain=$currentDomain and parentId=$internalId]"/>
    		</div>
		</xsl:otherwise>
	</xsl:choose>
	
</xsl:template>

</xsl:stylesheet>
