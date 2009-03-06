<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : details.xsl
    Created on : 30 September 2004, 16:11
    Author     : renny
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="caseURL"><xsl:value-of select="/details/caseURL"/></xsl:variable>
    <xsl:variable name="caseTab"><xsl:value-of select="/details/caseTab"/></xsl:variable>

  
    <xsl:template match="details">
    <script language="javascript" >
        function gotoCase(aURL)
        {
            window.location=aURL;        
        }
    </script>
        <xsl:if test="count(/details/CORECASE_intCoreCaseKey) &gt; 0">
        <xsl:variable name="CORECASE_intCoreCaseKey"><xsl:value-of select="/details/CORECASE_intCoreCaseKey"/></xsl:variable>
        <table width="100%">
            <tr>
                <td class="uportal-label" align='right'>
                <xsl:value-of select="CORECASE_intCoreCaseKeyDisplay"/>:&#160;<xsl:value-of select="CORECASE_intCoreCaseKey"/>&#160;&#160;&#160;<xsl:value-of select="CORECASE_intCaseIDDisplay"/>:&#160;<xsl:value-of select="CORECASE_intCaseID"/>&#160;&#160;&#160;<input type="button" value="&lt; Back" class="uportal-button" name="backtoCase" size="8" onclick="javascript:gotoCase('{$caseURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$caseTab}&amp;action=view_case&amp;CORECASE_intCoreCaseKey={$CORECASE_intCoreCaseKey}&amp;blFirst=true');"/></td>
            </tr>
        </table>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>