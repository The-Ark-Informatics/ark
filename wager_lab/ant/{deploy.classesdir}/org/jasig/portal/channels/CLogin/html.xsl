<?xml version="1.0" encoding="utf-8"?>
<!--
	Log-In Channel Stylesheet for Biogenix v4.5
	Description:
		If no use log-in is current, this style sheet generates a log-in form and
		notification messages resulting from the last log-in attempt. If a log-in
		is current, no output is generated.

	Version: $Id: html.xsl,v 1.8 2005/04/07 07:34:14 kleong Exp $
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" indent="yes"/>
	<xsl:param name="baseActionURL">default</xsl:param>
	<xsl:param name="unauthenticated">false</xsl:param>
	<xsl:variable name="mediaPath" select="'media/org/jasig/portal/channels/CLogin'"/>
	<xsl:variable name="loginImagePath">media/neuragenix</xsl:variable>
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
	<xsl:variable name="loginImagePath">media/neuragenix/login</xsl:variable>
	<xsl:variable name="spacerImage">media/neuragenix/spacer.gif</xsl:variable>
	<xsl:template match="login-status">
		<!-- Only Generate Log-In Form if Not Logged -->
		<xsl:if test="$unauthenticated!='false'">
			<table cellpadding="5" cellspacing="0" width="100%" border="0"
				style="height : 120px; background-image : url('{$loginImagePath}/login_login_strip_bg.jpg'); background-repeat : no-repeat;">
				<form method="post" action="Authentication">
					<input value="login" name="action" type="hidden"/>
					<tr valign="middle">
						<td align="left">
							<table cellpadding="1" cellspacing="0" border="0" width="600">
								<tr>
									<td><img src="{$spacerImage}" border="0" width="20" height="1"/></td>
									<td>
										<span style="color : #306799; font-size : 14px; font-weight : bold;">LOGIN</span>
									</td>
									<td colspan="2">
										<xsl:choose>
											<xsl:when test="count(failure)>0">
												<table cellpadding="2" cellspacing="2" border="0">
													<tr>
														<td style="background-color : #FF0000; color : #FFFFFF;">The user name/password combination entered is not valid. Please try again.</td>
													</tr>
												</table>
											</xsl:when>
											<xsl:when test="count(error)>0">
												<table cellpadding="2" cellspacing="2" border="0" style="background-color : #FF0000; color : #FFFFFF;">
													<tr>
														<td style="background-color : #FF0000; color : #FFFFFF;">An unexpected error has occured. Please try again later.</td>
													</tr>
												</table>
											</xsl:when>
											<xsl:otherwise>&#160;</xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
								<tr>
									<td>&#160;</td>
									<td class="form_label">Name&#160;:&#160;</td>
									<td align="right"><input type="text" name="userName" value="" style="background-color : #FFFFFF;" size="15"/></td>
									<td width="100%">&#160;</td>
								</tr>
								<tr>
									<td>&#160;</td>
									<td class="form_label">Password&#160;:&#160;</td>
									<td align="right"><input type="password" name="password"  style="background-color : #FFFFFF;" size="15"/></td>
									<td width="100%">&#160;</td>
								</tr>
								<tr>
									<td>&#160;</td>
									<td colspan="2" align="right"><input class="submit_image_button" type="image" src="{$buttonImagePath}/login_enabled.gif" name="Login" value="Login" alt="Login"/></td>
									<td>&#160;</td>
								</tr>
							</table>
						</td>
					</tr>
				</form>
			</table>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
