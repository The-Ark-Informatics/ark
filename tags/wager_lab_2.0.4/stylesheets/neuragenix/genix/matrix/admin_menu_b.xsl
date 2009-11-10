<?xml version="1.0" encoding="utf-8"?>
<!-- admin_menu.xsl. Menu used for all administration.-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="html" indent="no"/>
  <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>

  <xsl:template match="/">
    <table width="100%">
	<tr valign="top">
		
                <td width="100%" valign="top"><xsl:apply-templates/></td>
                
                </tr>
     </table>
  </xsl:template>
</xsl:stylesheet>

