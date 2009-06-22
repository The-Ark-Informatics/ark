<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="html" indent="no" />
	<xsl:include href="./admin_menu.xsl"/>

	<xsl:template match="lovselect">
		<!-- <table>
			<tr><td class="uportal-channel-subtitle">Please select a list to manage<hr/></td></tr>
			<xsl:for-each select="*" >
			<xsl:sort select="." />
				<tr><td class="uportal-channel-subtitle"><a ><xsl:attribute name="href"><xsl:value-of select="$baseActionURL" />?current=editlist&amp;editlist=<xsl:value-of select="substring-before(name(), 'Display')" /></xsl:attribute><xsl:value-of select="." /></a></td></tr>
			</xsl:for-each>
		</table> -->
                <table>
                    <tr>
                        <td align="centre" >
                           <span class="uportal-label"> Please select one of the options from the left to modify </span>
                        </td>
                    </tr>
                </table>   
	</xsl:template>

</xsl:stylesheet>
