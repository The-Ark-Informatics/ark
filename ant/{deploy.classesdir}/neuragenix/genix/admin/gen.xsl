<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="html" indent="no" />

	<xsl:template match="/">
		<table>
			<tr><td class="uportal-channel-subtitle">Please select a list to manage<hr/></td></tr>
				<tr><td class="uportal-channel-subtitle"><xsl:value-of select="abc" /></td>
				</tr>
		</table>
	</xsl:template>

</xsl:stylesheet>
