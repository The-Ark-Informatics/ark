<?xml version="1.0" encoding="utf-8"?>

<!-- 
    smartform_list.xsl, part of the Smartform channel
    Author: hhoang@neuragenix.com
    Date: 26/03/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=smartform_no_domain</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    
  
    <xsl:template match="smartform">
        <table width="100%">
            <tr>
                <td class="uportal-label">No domain loaded.</td>
            </tr>
        </table>
    </xsl:template>

</xsl:stylesheet>
