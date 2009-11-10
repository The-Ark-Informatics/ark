<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="html" indent="no" />
	<xsl:include href="./admin_menu.xsl"/>

	<xsl:template match="userselect">
		<table>
				<tr><td class="uportal-channel-subtitle" colspan="5">Manage Profiles<hr/></td></tr>
				<tr><td class="uportal-label">Please select a user to assign profiles to:</td></tr>
				<tr><td class="uportal-label"><xsl:value-of select="strUsernameDisplay" /></td><td class="uportal-label"><xsl:value-of select="strPersonFirstNameDisplay" /></td><td class="uportal-label"><xsl:value-of select="strPersonLastNameDisplay" /></td><td class="uportal-label"><xsl:value-of select="strPersonEmailDisplay" /></td></tr>
			<xsl:for-each select="searchResult" >
				<tr>
						<td class="uportal-text">
							<a>
								<xsl:attribute name="href">
									<xsl:value-of select="$baseActionURL"/>?current=profileassign&amp;strUsername=<xsl:value-of select="strUsername" />
								</xsl:attribute>
								<xsl:value-of select="strUsername" />
							</a>
						</td>
						<td class="uportal-text">
							<a>
								<xsl:attribute name="href">
									<xsl:value-of select="$baseActionURL"/>?current=profileassign&amp;strUsername=<xsl:value-of select="strUsername" />
								</xsl:attribute>
								<xsl:value-of select="strPersonFirstName" />
							</a>
						</td>
						<td class="uportal-text">
							<a>
								<xsl:attribute name="href">
									<xsl:value-of select="$baseActionURL"/>?current=profileassign&amp;strUsername=<xsl:value-of select="strUsername" />
								</xsl:attribute>
								<xsl:value-of select="strPersonLastName" />
							</a>
						</td>
						<td class="uportal-text">
							<a>
								<xsl:attribute name="href">
									<xsl:value-of select="$baseActionURL"/>?current=profileassign&amp;strUsername=<xsl:value-of select="strUsername" />
								</xsl:attribute>
								<xsl:value-of select="strPersonEmail" />
							</a>
						</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

</xsl:stylesheet>
