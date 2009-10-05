<?xml version="1.0" encoding="utf-8"?>
<!--
	Header Channel Stylesheet for Biogenix v4.5

	Description:
		Renders the Home, Channel Admin, Preferences and Logout buttons on the page header.

	Version: $Id: html.xsl,v 1.1 2005/03/22 05:01:23 kleong Exp $ 
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:param name="baseActionURL">default</xsl:param>
	<xsl:param name="authenticated">false</xsl:param>
	<xsl:param name="viewPreferences">
		<xsl:value-of select="viewPreferences"/>
	</xsl:param>
	<xsl:param name="skin" select="'imm'"/>
	<xsl:variable name="mediaPath">media/org/jasig/portal/layout/tab-column/nested-tables</xsl:variable>
	<xsl:template match="header">
		<xsl:if test="$authenticated != 'false'">
			<!-- Home Button -->
			<td>
				<a class="uportal-navigation-control" href="{$baseActionURL}?uP_root=root">
					<img name="header_home_button" src="{$mediaPath}/{$skin}/institutional/header_home_unselected.jpg" border="0"/>
				</a>
			</td>
			<xsl:if test="chan-mgr-chanid">
				<!-- Channel Admin Button -->
				<td>
					<a class="uportal-navigation-control" href="{$baseActionURL}?uP_root={chan-mgr-chanid}">
						<img name="header_channel_admin_button" src="{$mediaPath}/{$skin}/institutional/header_channel_admin_unselected.jpg" border="0"/>
					</a>
				</td>
			</xsl:if>
			<xsl:if test="viewPreferences='1'">
				<!-- Preferences Button -->
				<td>
					<a class="uportal-navigation-control" href="{$baseActionURL}?uP_root={preferences-chanid}">
						<img name="header_preferences_button" src="{$mediaPath}/{$skin}/institutional/header_preferences_unselected.jpg" border="0"/>
					</a>
				</td>
			</xsl:if>
			<!-- Logout Button -->
			<td>
				<a class="uportal-navigation-control" href="Logout">
					<img name="header_logout_button" src="{$mediaPath}/{$skin}/institutional/header_logout_unselected.gif" border="0"/>
				</a>
			</td>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
