<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="html" indent="no" />
	<xsl:include href="./admin_menu.xsl"/>

	<xsl:template match="useredit">
		<table>
				<tr><td class="uportal-channel-subtitle" colspan="5">Manage Users<hr/></td></tr>
				<tr><td class="uportal-label"><xsl:value-of select="strUsernameDisplay" /></td><td class="uportal-label"><xsl:value-of select="strPersonFirstNameDisplay" /></td><td class="uportal-label"><xsl:value-of select="strPersonLastNameDisplay" /></td><td class="uportal-label"><xsl:value-of select="strPersonEmailDisplay" /></td><td></td></tr>
			<xsl:for-each select="searchResult" >
				<tr>
					<form method="post" action="{$baseActionURL}?current=edituser">
						<td class="uportal-text">
							<input type="hidden" name="PERSON_DIR_Timestamp">
								<xsl:attribute name="value"><xsl:value-of select="PERSON_DIR_Timestamp" /></xsl:attribute>
							</input>
							<input type="hidden" name="strUsername">
								<xsl:attribute name="value"><xsl:value-of select="strUsername" /></xsl:attribute>
							</input>
							<xsl:value-of select="strUsername" />
						</td>
						<td class="uportal-text">
							<input type="text" name="strPersonFirstName">
								<xsl:attribute name="value"><xsl:value-of select="strPersonFirstName" /></xsl:attribute>
							</input>
						</td>
						<td class="uportal-text">
							<input type="text" name="strPersonLastName">
								<xsl:attribute name="value"><xsl:value-of select="strPersonLastName" /></xsl:attribute>
							</input>
						</td>
						<td class="uportal-text">
							<input type="text" name="strPersonEmail">
								<xsl:attribute name="value"><xsl:value-of select="strPersonEmail" /></xsl:attribute>
							</input>
						</td>
						<td class="uportal-text"><input type="submit" value="Update"/></td>
					</form>
				</tr>
			</xsl:for-each>
				<tr><td colspan="5"><hr/></td></tr>
				<tr>
					<form method="post" action="{$baseActionURL}?current=newuser">
						<td class="uportal-text">
							<input type="text" name="strUsername">
							</input>
						</td>
						<td class="uportal-text">
							<input type="text" name="strPersonFirstName">
							</input>
						</td>
						<td class="uportal-text">
							<input type="text" name="strPersonLastName">
							</input>
						</td>
						<td class="uportal-text">
							<input type="text" name="strPersonEmail">
							</input>
						</td>
						<td class="uportal-text"><input type="submit" class="uportal-button" value="Insert"/></td>
					</form>
				</tr>
		</table>
	</xsl:template>

</xsl:stylesheet>
