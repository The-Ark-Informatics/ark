<?xml version="1.0" encoding="utf-8"?>

<!-- upload_template.xsl To upload templates-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./admin_menu.xsl"/>
  <xsl:output method="html" indent="no"/>

  <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>


  <xsl:template match="upload_templates">
    <table width='100%'>
        <tr>
                <td colspan='6' class="neuragenix-form-required-text">
                    <xsl:value-of select="strError"/>
                </td>
        </tr>
    </table>
    </xsl:template>

</xsl:stylesheet> 
