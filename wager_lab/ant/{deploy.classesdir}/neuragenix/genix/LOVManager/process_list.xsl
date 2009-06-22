<?xml version="1.0" encoding="utf-8"?>
<!-- process_list.xsl, part of the Microarray Module -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./admin_menu.xsl"/>
<xsl:param name="formParams">current=process_list</xsl:param>

  <xsl:output method="html" indent="no" />

  <xsl:template match="process_list">
    <form action="{$baseActionURL}?current=process_list" method="post">
	<table width="100%">
	  <tr><td class="uportal-channel-subtitle">Process List<br/><hr/></td>
	  </tr>
          <tr>
                <td class="neuragenix-form-required-text" width="50%">
                        <xsl:value-of select="strErrorDuplicateKey" />
                        <xsl:value-of select="strErrorRequiredFields" />
                        <xsl:value-of select="strErrorInvalidDataFields" />
                        <br />
                        <xsl:value-of select="strErr" />
                </td>
          </tr>
	</table>	
        <table>
            <tr>
                <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
                <td id="neuragenix-form-row-label" class="uportal-label"></td>
                <td class="uportal-label" id="neuragenix-form-row-input">                    
                    <input type="submit" name="start" value="start new email alert" class="uportal-button" />
                    <input type="submit" name="refresh" value="refresh" class="uportal-button" />
                </td>
            </tr>
        </table>
	
        <table width="100%">
            <tr>
                <td id="neuragenix-form-row-label" class="uportal-label">
                        <xsl:value-of select="intProcessKeyDisplay" /></td>
                <td id="neuragenix-form-row-label" class="uportal-label">
                        <xsl:value-of select="strProcessTypeDisplay" /></td>
                <td id="neuragenix-form-row-label" class="uportal-label">
                        <xsl:value-of select="strProcessStatusDisplay" /></td>
                <td id="neuragenix-form-row-label" class="uportal-label">
                        <xsl:value-of select="strProcessDescriptionDisplay" /></td>
            </tr>
            <xsl:for-each select="searchResult">
            <xsl:sort select="intProcessKey" data-type="number" order="descending"/>

            <xsl:variable name="varintProcessKey"><xsl:value-of select="intProcessKey" /></xsl:variable>
            <xsl:variable name="varstrProcessType"><xsl:value-of select="strProcessType" /></xsl:variable>
            
            <tr>
                <td width="5%" class="uportal-text">
                  <a href="{$baseActionURL}?current=process_list&amp;intProcessKey={$varintProcessKey}" >
                   <xsl:value-of select="intProcessKey" /></a></td>
                <td width="20%" class="uportal-text">
                  <a href="{$baseActionURL}?current=process_list&amp;intProcessKey={$varintProcessKey}" >
                   <xsl:value-of select="strProcessType" /></a></td>
                <td width="10%" class="uportal-text">
                  <a href="{$baseActionURL}?current=process_list&amp;intProcessKey={$varintProcessKey}" >
                    <xsl:value-of select="strProcessStatus" /></a></td>
                <td width="50%" class="uportal-text">
                  <a href="{$baseActionURL}?current=process_list&amp;intProcessKey={$varintProcessKey}" >
                    <xsl:value-of select="strProcessDescription" /></a></td>
                <td width="5%" class="uportal-text">
                     <a href="{$baseActionURL}?current=process_list&amp;target=stop_process&amp;strProcessType={$varstrProcessType}&amp;intProcessKey={$varintProcessKey}">Stop</a>
                </td>
                <td width="5%" class="uportal-text">
                     <a href="{$baseActionURL}?current=process_list&amp;target=del_process&amp;strProcessType={$varstrProcessType}&amp;intProcessKey={$varintProcessKey}">Del</a>
                </td>
            </tr>
            </xsl:for-each>
	</table>
    </form>
  </xsl:template> 
</xsl:stylesheet>
