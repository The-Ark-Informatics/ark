<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="html" indent="no" />
	<xsl:include href="./admin_menu.xsl"/>
	<xsl:param name="strUsername" />

	<xsl:template match="userprofile">
		<table>
				<tr><td class="uportal-channel-subtitle" colspan="3">Managing user's profiles for <xsl:value-of select="$strUsername"/><hr/></td></tr>
				<tr><td class="uportal-label"><xsl:value-of select="strProfileNameDisplay" /></td><td class="uportal-label"><xsl:value-of select="strProfileDescDisplay" /></td><td></td></tr>


			<xsl:for-each select="searchResult">
			<tr>
				<td id="neuragenix-form-row-input" class="uportal-label"><xsl:value-of select="strProfileName"/></td>	
				<td id="neuragenix-form-row-input" class="uportal-label"><xsl:value-of select="strProfileDesc"/></td>	
				<td id="neuragenix-form-row-input" class="uportal-label">
					<a onclick="confirm('Are you sure you want to delete this profile from this user?')">
						<xsl:attribute name="href">
							<xsl:value-of select="$baseActionURL"/>?current=deleteuserprofile&amp;intUserProfileID=<xsl:value-of select="intUserProfileID"/>&amp;strUsername=<xsl:value-of select="$strUsername"/>&amp;USERPROFILE_Timestamp=<xsl:value-of select="USERPROFILE_Timestamp"/>
						</xsl:attribute>(delete)</a>
				</td>
			</tr>
			</xsl:for-each>
			<tr>
				<form  action="{$baseActionURL}?current=insertuserprofile" method="post">
				<td colspan="2"><select name="intProfileID">
			<xsl:for-each select="LOV_PROFILE">
			<option>
				<xsl:attribute name="value">
					 <xsl:value-of select="LOV_intProfileID"/>
				</xsl:attribute>
				<xsl:value-of select="LOV_strProfileName"/> - <xsl:value-of select="LOV_strProfileDesc"/>
			</option>
			</xsl:for-each>
				</select></td>
				<td>
					<input type="hidden" value="{$strUsername}" name="strUsername"/>
					<input type="submit" value="Add" name="adduserprofile"/>
				</td>	
				</form>
			</tr>
		</table>
	</xsl:template>

</xsl:stylesheet>
