<?xml version="1.0" encoding="utf-8"?>
<!-- process_list.xsl, part of the Microarray Module -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./integration_menu.xsl"/>
<xsl:param name="formParams">current=process_view</xsl:param>

  <xsl:output method="html" indent="no" />

  <xsl:template match="integration">
    <form action="{$baseActionURL}?current=process_view" method="post">
	<table width="100%">
	  <tr><td class="uportal-channel-subtitle">Process List<br/><hr/></td>
	  </tr>
	</table>	
        <table>
            <tr>
                <td class="uportal-label" id="neuragenix-form-row-input">                    
                    <input type="submit" name="refresh" value="refresh" class="uportal-button" />
                </td>
            </tr>
        </table>
	
        <table width="100%">
            <tr>
                <td id="neuragenix-form-row-label" class="uportal-label">
                        Process Key</td>
                <td id="neuragenix-form-row-label" class="uportal-label">
                        Type</td>
                <td id="neuragenix-form-row-label" class="uportal-label">
                        Status</td>
                <td id="neuragenix-form-row-label" class="uportal-label">
                        Description</td>
            </tr>
            <xsl:for-each select="searchResult">
            <xsl:sort select="intProcessKey" data-type="number" order="descending"/>

            <xsl:variable name="varintProcessKey"><xsl:value-of select="intProcessKey" /></xsl:variable>
            <xsl:variable name="varstrProcessType"><xsl:value-of select="strProcessType" /></xsl:variable>
            <xsl:variable name="varstrProcessReport"><xsl:value-of select="strProcessReport" /></xsl:variable>
            
            <tr>
                <td width="5%" class="uportal-text">
                  <a href="{$baseActionURL}?current=process_view&amp;intProcessKey={$varintProcessKey}" >
                   <xsl:value-of select="intProcessKey" /></a></td>
                <td width="20%" class="uportal-text">
                  <a href="{$baseActionURL}?current=process_view&amp;intProcessKey={$varintProcessKey}" >
                   <xsl:value-of select="strProcessType" /></a></td>
                <td width="10%" class="uportal-text">
                  <a href="{$baseActionURL}?current=process_view&amp;intProcessKey={$varintProcessKey}" >
                    <xsl:value-of select="strProcessStatus" /></a></td>
                <td width="40%" class="uportal-text">
                  <a href="{$baseActionURL}?current=process_view&amp;intProcessKey={$varintProcessKey}" >
                    <xsl:value-of select="strProcessDescription" /></a></td>
                <td width="20%" class="uportal-text">
                  <xsl:if test="$varstrProcessReport != ''">
                  <a target="_blank">
                    <xsl:attribute name="href"><xsl:value-of select="strProcessReport" /></xsl:attribute>
                    Report</a>
                  </xsl:if>
                </td>
            </tr>
            </xsl:for-each>
	</table>
    </form>
  </xsl:template> 
</xsl:stylesheet>
