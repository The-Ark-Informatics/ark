<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="html" indent="no" />
	<xsl:include href="./admin_menu.xsl"/>
	<xsl:param name="strTypeDef">Values</xsl:param>
	<xsl:param name="strTypeNew">Values</xsl:param>

	<xsl:template match="lovedit">
		<table>
				<tr><td class="uportal-channel-subtitle" colspan="4">Managing List of "<xsl:value-of select="$strTypeDef"/>"<hr/></td></tr>
				<tr><td class="uportal-label"><xsl:value-of select="strValueDisplay" /></td><td class="uportal-label"><xsl:value-of select="strDescriptionDisplay" /></td><td class="uportal-label"><xsl:value-of select="intSortOrderDisplay" /></td><td></td></tr>
			<xsl:for-each select="searchResult" >
				<tr>
					<form method="post" action="{$baseActionURL}?current=editval">
						<td class="uportal-text">
							<input type="hidden" name="intLovKey">
								<xsl:attribute name="value"><xsl:value-of select="intLovKey" /></xsl:attribute>
							</input>
							<input type="hidden" name="strType">
								<xsl:attribute name="value"><xsl:value-of select="strType" /></xsl:attribute>
							</input>
							<input type="text" name="strValue">
								<xsl:attribute name="value"><xsl:value-of select="strValue" /></xsl:attribute>
							</input>
						</td>
						<td class="uportal-text">
							<input type="text" name="strDescription">
								<xsl:attribute name="value"><xsl:value-of select="strDescription" /></xsl:attribute>
							</input>
						</td>
						<td class="uportal-text">
							<input type="text" name="intSortOrder">
								<xsl:attribute name="value"><xsl:value-of select="intSortOrder" /></xsl:attribute>
							</input>
						</td>
						<td class="uportal-text"><input type="submit" value="Update"/></td>
					</form>
				</tr>
			</xsl:for-each>
				<tr><td><hr/></td></tr>
				<tr>
					<form method="post" action="{$baseActionURL}?current=insertval">
						<td class="uportal-text">
							<input type="hidden" name="strType">
								<xsl:attribute name="value"><xsl:value-of select="$strTypeNew" /></xsl:attribute>
							</input>
							<input type="text" name="strValue">
							</input>
						</td>
						<td class="uportal-text">
							<input type="text" name="strDescription">
							</input>
						</td>
						<td class="uportal-text">
							<input type="text" name="intSortOrder">
							</input>
						</td>
						<td class="uportal-text"><input type="submit" class="uportal-button" value="Insert"/></td>
					</form>
				</tr>
		</table>
	</xsl:template>

</xsl:stylesheet>
