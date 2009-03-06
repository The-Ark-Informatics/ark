<?xml version="1.0" encoding="utf-8"?>
<!--
    Document   : report_result.xml
    Copyright ï¿½ 2003 Neuragenix, Inc .  All rights reserved.
    Created on :    
    Author     :
    
    Modified on: 18/02/2004
    Author     : Anita Balraj
    Description: Secure Download Implementation
             
-->
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./report_menu.xsl"/>
    <xsl:include href="./report_list.xsl"/>
    
    <xsl:output method="html" indent="no" />
    <!-- Start Secure Download Implementation -->
    <xsl:param name="downloadURL">downloadURL_false</xsl:param>   
    <xsl:param name="nodeId">nodeId_false</xsl:param>
    <!-- End Secure --> 
    
    <xsl:template match="report">
        <xsl:param name="query"><xsl:value-of select="query"/></xsl:param>
        <xsl:param name="strFileName"><xsl:value-of select="strFileName"/></xsl:param>
        <xsl:param name="strSessionID"><xsl:value-of select="strSessionID"/></xsl:param>
        <xsl:param name="REPORT_intReportKey"><xsl:value-of select="REPORT_intReportKey"/></xsl:param>
    
        <table width="100%">
            <tr>
                <td class="uportal-channel-subtitle">
                    Report detail<br/><hr/>
                </td>
            </tr>
        </table>

        <table width="100%">
            <tr>
                <td class="uportal-label">
                    <a href="{$baseActionURL}?uP_root=root&amp;current=report_category_add">Add new category</a>
                    |
                    <a href="{$baseActionURL}?uP_root=root&amp;current=report_add">Add new report</a>
                    |
                    <a href="{$baseActionURL}?uP_root=root&amp;current=report_view&amp;REPORT_intReportKey={$REPORT_intReportKey}">Edit report</a>
                    
                    <!-- Start Secure Download Implementation -->
                    <!-- <a href="./files/report_files/{$strFileName}" target="_blank">PDF export</a> -->                    
                    <!-- End Secure --> 
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

        <xsl:if test="$query != ''">
            <iframe src="testpage.jsp?query={$query}&amp;strSessionID={$strSessionID}" height="600" frameborder="no" width="100%"></iframe>
        </xsl:if>
       
    </xsl:template>

</xsl:stylesheet>
