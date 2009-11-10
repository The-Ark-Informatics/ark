<?xml version="1.0" encoding="utf-8"?>
<!-- $Id: nested-tables.xsl,v 1.8 2005/04/08 06:05:13 kleong Exp $ -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" indent="no"/>
	<xsl:param name="baseActionURL">render.userLayoutRootNode.uP</xsl:param>
	<xsl:param name="skin" select="'imm'"/>
	<xsl:param name="authenticated">false</xsl:param>
	<xsl:variable name="mediaPath">media/org/jasig/portal/layout/tab-column/nested-tables</xsl:variable>
	<!-- This template is supposed to render a fragment of the layout. For example, during
       a detach mode, only the <channel> element that's detached is passed along to the structure
       transformation.
       In general, it should render a fragment that contains not just a single channel, but 
       an entire column or a tab, perhaps :) But I am lazy, so I'll just flatten out all of 
       the channels into one big column.
       -peter.
  -->
	<!-- Is this being used? -->
	<xsl:template match="layout_fragment">
		<html>
			<head>
				<title>
					<xsl:value-of select="content/channel/@name"/>
				</title>
				<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT"/>
				<META HTTP-EQUIV="pragma" CONTENT="no-cache"/>
				<link type="text/css" rel="stylesheet" href="{$mediaPath}/{$skin}/skin/{$skin}.css"/>
				<script language="JavaScript">function openBrWindow(theURL,winName,features) {window.open(theURL,winName,features);}</script>
			</head>
			<body class="uportal-background-content">
				<xsl:for-each select="content//channel">
					<xsl:apply-templates select="."/>
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
	<!-- Page Layout and Footer -->
	<xsl:template match="layout">
		<html>
			<head>
				<title>WAGER Lab</title>
				<link type="text/css" rel="stylesheet" href="{$mediaPath}/{$skin}/skin/{$skin}.css"/>
				<script language="JavaScript">
          function openBrWindow(theURL,winName,features)
          {
            window.open(theURL,winName,features);
          }
        </script>
	<script language="JavaScript">
 function GetMySize()
        {
        if (window.parent != window) {
        window.parent.SetIFrameSize("app",100,window.document.body.scrollWidth + 40);
        }
                 w = window.document.body.scrollWidth + 40;
                h = window.document.body.scrollHeight + 15;
if (window.parent != window) {
window.parent.SetIFrameSize("app",h,w);
}
        }
</script>
				<link type="text/css" rel="stylesheet" href="htmlarea/dijit/themes/soria/soria.css"/>
			</head>
			<body onLoad="GetMySize()" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0" marginheight="0" marginwidth="0" class="nihilo">
				<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
					<!--<tr valign="top">
						<td>
							<xsl:apply-templates select="header"/>
						</td>
					</tr>-->
					<xsl:if test="not(//focused)">
						<tr>
							<td>
								<xsl:apply-templates select="navigation"/>
							</td>
						</tr>
					</xsl:if>
					<tr height="100%" valign="top">
						<td>
							<xsl:apply-templates select="content"/>
						</td>
					</tr>
					
					
				</table>
			</body>
		</html>
	</xsl:template>
	<!-- Page Header -->
	<xsl:template match="header">
		<table width="100%" height="20" border="0" cellspacing="0" cellpadding="0">
			<tr valign="top">
				<td>
					<img src="{$mediaPath}/{$skin}/institutional/header_left.jpg" border="0"/>
				</td>
				<!-- Empty Message Bar -->
				<td class="header_message_bar_content_empty" width="100%" align="right" valign="middle">&#160;</td>
				<!-- Message Bar with Content
				<td>
					<img src="{$mediaPath}/{$skin}/institutional/header_message_bar_left.gif" border="0"/>
				</td>
				<td class="header_message_bar_content" width="100%" align="right" valign="middle">
					Message Bar Content Here!
				</td>
				<td>
					<img src="{$mediaPath}/{$skin}/institutional/header_message_bar_right.gif" border="0"/>
				</td>
				-->
				<xsl:copy-of select="channel[@name='Header']"/>
			</tr>
		</table>
		<!-- Log-In -->
		<xsl:copy-of select="channel[@name='Login']"/>
	</xsl:template>
	<!-- Navigation -->
	<xsl:template match="navigation">
		<table summary="add summary" height="21" border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr>
				<debug><xsl:copy-of select="."/></debug>
				<xsl:for-each select="tab">
					<!-- Ignore Smartform Channel -->
					<xsl:if test="@name!='Smartform'">
						<!-- Decide if This is the First Tab -->
						<xsl:variable name="firstTabFlag">
							<xsl:value-of select="position()=1 or (position()=2 and preceding-sibling::tab[1]/@name='Smartform')"/>
						</xsl:variable>
						<!-- Decide if Next Tab is Active -->
						<xsl:variable name="nextTabActiveFlag">
							<xsl:value-of select="(following-sibling::tab[1]/@name='Smartform' and following-sibling::tab[2]/@activeTab='true') or (following-sibling::tab[1]/@name!='Smartform' and following-sibling::tab[1]/@activeTab='true')"/>
						</xsl:variable>
						<!-- Decide if This is the Last Tab -->
						<xsl:variable name="lastTabFlag">
							<xsl:value-of select="position()=last() or (position()=(last()-1) and following-sibling::tab[1]/@name='Smartform')"/>
						</xsl:variable>
						<!-- Add Image Before First Tab -->
						<xsl:if test="$firstTabFlag=1">
							<xsl:choose>
								<xsl:when test="@activeTab='true'">
									<td width="21">
										<img alt="interface image" src="{$mediaPath}/{$skin}/navigation/navtab_active_first_left.gif"/>
									</td>
								</xsl:when>
								<xsl:otherwise>
									<td width="21">
										<img alt="interface image" src="{$mediaPath}/{$skin}/navigation/navtab_inactive_first_left.gif"/>
									</td>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						<!-- Add Tab Label -->
						<xsl:choose>
							<!-- This Tab is Active -->
							<xsl:when test="@activeTab='true'">
								<td nowrap="nowrap" class="navigation_tab_active">
									<a href="{$baseActionURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={position()}" class="navigation_tab_active">
										<xsl:value-of select="@name"/>
									</a>
								</td>
							</xsl:when>
							<!-- This Tab is Inactive -->
							<xsl:otherwise>
								<td nowrap="nowrap" class="navigation_tab_inactive">
									<a href="{$baseActionURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={position()}" class="navigation_tab_inactive">
										<xsl:value-of select="@name"/>
									</a>
								</td>
							</xsl:otherwise>
						</xsl:choose>
						<!-- Add Image After Label -->
						<xsl:choose>
							<!-- This Tab is Active -->
							<xsl:when test="@activeTab='true'">
								<xsl:choose>
									<!-- Last Tab is Active -->
									<xsl:when test="$lastTabFlag='true'">
										<td width="18">
											<img alt="interface image" src="{$mediaPath}/{$skin}/navigation/navtab_active_last_right.gif"/>
										</td>
									</xsl:when>
									<xsl:otherwise>
										<!-- Not Last Tab -->
										<td width="21">
											<img alt="interface image" src="{$mediaPath}/{$skin}/navigation/navtab_active_middle_right.gif"/>
										</td>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<!-- This Tab is Inactive -->
							<xsl:otherwise>
								<xsl:choose>
									<!-- Last Tab is Inactive -->
									<xsl:when test="$lastTabFlag='true'">
										<td width="18">
											<img alt="interface image" src="{$mediaPath}/{$skin}/navigation/navtab_inactive_last_right.gif"/>
										</td>
									</xsl:when>
									<xsl:otherwise>
										<!-- Not Last Tab - Check if Next Tab Active -->
										<xsl:choose>
											<xsl:when test="$nextTabActiveFlag='true'">
												<td width="21">
													<img alt="interface image" src="{$mediaPath}/{$skin}/navigation/navtab_active_middle_left.gif"/>
												</td>
											</xsl:when>
											<xsl:otherwise>
												<td width="18">
													<img alt="interface image" src="{$mediaPath}/{$skin}/navigation/navtab_inactive_middle_divider.gif"/>
												</td>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:for-each>
				<td width="100%" class="navigation_tab_right_space" nowrap="nowrap">
              &#160;
            </td><xsl:if test="count(tab) &lt;= 3"><td><img src="{$mediaPath}/{$skin}/navigation/08_Filler.jpg"/></td></xsl:if>
			</tr>
		</table>
	</xsl:template>
	<!-- Content i.e. section under header and navigation tabs -->
	<xsl:template match="content">
		<xsl:choose>
			<xsl:when test="not(//focused)">
				<table border="0" cellspacing="0" cellpadding="0" class="uportal-background-content" width="100%" height="100%">
					<!-- Add Row to Define Columns for Channels -->
					<tr height="1">
						<td class="channel_border_hstrip">
							<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="10" height="1"/>
						</td>
						<td class="channel_border_hstrip" width="100%">
							<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="1" height="1"/>
						</td>
						<td class="channel_border_hstrip">
							<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="10" height="1"/>
						</td>
					</tr>
					<xsl:choose>
						<xsl:when test="count(column/channel)=1">
							<!-- Render Single Channel -->
							<xsl:apply-templates select="column/channel">
								<xsl:with-param name="oneChannelOnly">true</xsl:with-param>
							</xsl:apply-templates>
						</xsl:when>
						<xsl:otherwise>
							<!-- Render Each Channel -->
							<xsl:for-each select="column">
								<xsl:apply-templates select="channel">
									<xsl:with-param name="oneChannelOnly">false</xsl:with-param>
								</xsl:apply-templates>
							</xsl:for-each>
							<!-- Space Below Channels if Not the Only Channel -->
							<tr height="100%">
								<td class="channel_border_left">
									<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="10" height="1"/>
								</td>
								<td><img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="1" height="1"/></td>
								<td class="channel_border_right">
									<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="10" height="1"/>
								</td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
					<!-- Add Row to Define Columns for Channels -->
					<tr height="1">
						<td class="channel_border_hstrip">
							<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="10" height="1"/>
						</td>
						<td class="channel_border_hstrip" width="100%">
							<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="1" height="1"/>
						</td>
						<td class="channel_border_hstrip">
							<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="10" height="1"/>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="focused"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="channel">
		<!-- Parameter Set to "true" if Only Channel on Screen -->
		<xsl:param name="oneChannelOnly">false</xsl:param>
		<!-- Channel Title Row -->
		<tr valign="top" height="26">
			<td class="channel_title_bar"><img src="{$mediaPath}/{$skin}/skin/transparent.gif" width="10" height="1"/></td>
			<td class="channel_title_bar">
				<table width="100%" height="26" border="0" cellspacing="0" cellpadding="0">
					<tr height="26" align="left" valign="middle">
						<td>
							<img src="{$mediaPath}/{$skin}/skin/transparent.gif" width="8" height="1"/>
						</td>
						<!-- Channel Title -->
						<td class="channel_title_bar" width="100%">
							<xsl:value-of select="@title"/>
						</td>
						<!-- Control Buttons -->
						<td align="right" nowrap="nowrap">
							<!-- <xsl:call-template name="controls"/> -->
						</td>
					</tr>
				</table>
			</td>
			<td class="channel_title_bar" height="1"><img src="{$mediaPath}/{$skin}/skin/transparent.gif" width="10" height="1"/></td>
		</tr>
		<!-- Separator -->
		<tr height="1">
			<td class="channel_title_bar_separator_bevel" colspan="3">
				<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="1" height="1"/>
			</td>
		</tr>
		<tr height="1">
			<td class="channel_title_bar_separator" height="1" colspan="3">
				<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="1" height="1"/>
			</td>
		</tr>
		<xsl:choose>
			<xsl:when test="@minimized != 'true'">
				<!-- Add Channel Content if Not Minimised -->
				<tr valign="top">
					<xsl:if test="$oneChannelOnly='true'">
						<xsl:attribute name="height">100%</xsl:attribute>
					</xsl:if>
					<td class="channel_border_left"><img src="{$mediaPath}/{$skin}/skin/transparent.gif" width="10" height="1"/></td>
					<td>
						<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
							<tr valign="top">
								<td><xsl:copy-of select="."/></td>
							</tr>
						</table>
					</td>
					<td class="channel_border_right"><img src="{$mediaPath}/{$skin}/skin/transparent.gif" width="10" height="1"/></td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<!-- Minimised: Render Spacing Row -->
				<tr>
					<td>&#160;</td>
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="footer">
		<xsl:for-each select="channel">
			<td align="center">
				<xsl:copy-of select="."/>
			</td>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="controls">
		<!-- Help button -->
		<xsl:if test="not(@hasHelp='false')">
			<a href="{$baseActionURL}?uP_help_target={@ID}">
				<img alt="help" src="{$mediaPath}/{$skin}/controls/help.gif" border="0"/>
			</a>
		</xsl:if>
		<!-- About button -->
		<xsl:if test="not(@hasAbout='false')">
			<a href="{$baseActionURL}?uP_about_target={@ID}">
				<img alt="about" src="{$mediaPath}/{$skin}/controls/about.gif" border="0"/>
			</a>
		</xsl:if>
		<!-- Edit button -->
		<xsl:if test="not(@editable='false')">
			<a href="{$baseActionURL}?uP_edit_target={@ID}">
				<img alt="edit" src="{$mediaPath}/{$skin}/controls/edit.gif" border="0"/>
			</a>
		</xsl:if>
		<!-- Print button -->
		<xsl:if test="@printable='true'">
			<a href="{$baseActionURL}?uP_print_target={@ID}">
				<img alt="print" src="{$mediaPath}/{$skin}/controls/print.gif" border="0"/>
			</a>
		</xsl:if>
		<!-- Focus button -->
		<xsl:if test="not(//focused)">
			<a href="{$baseActionURL}?uP_root={@ID}">
				<img alt="focus" src="{$mediaPath}/{$skin}/controls/focus_active.gif" border="0"/>
			</a>
		</xsl:if>
		<!-- Minimize/maximize button -->
		<xsl:if test="not(//focused)">
			<xsl:choose>
				<xsl:when test="@minimized='true'">
					<a href="{$baseActionURL}?uP_tcattr=minimized&amp;minimized_channelId={@ID}&amp;minimized_{@ID}_value=false">
						<img alt="maximize" src="{$mediaPath}/{$skin}/controls/maximise.gif" border="0"/>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<a href="{$baseActionURL}?uP_tcattr=minimized&amp;minimized_channelId={@ID}&amp;minimized_{@ID}_value=true">
						<img alt="minimize" src="{$mediaPath}/{$skin}/controls/minimise.gif" border="0"/>
					</a>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		<!-- Detach button -->
		<a href="#" onClick="openBrWindow('{$baseActionURL}?uP_detach_target={@ID}','detachedChannel','toolbar=yes,location=yes,status=yes,menubar=yes,scrollbars=yes,resizable=yes,width=640,height=480')">
			<img alt="detach" src="{$mediaPath}/{$skin}/controls/detach_inactive.gif" border="0"/>
		</a>
	</xsl:template>
	<xsl:template match="focused">
		<xsl:apply-templates select="channel" mode="focused"/>
	</xsl:template>
	<xsl:template match="channel" mode="focused">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="uportal-background-content">
			<tr>
				<td width="80">
					<img alt="interface image" src="{$mediaPath}/{$skin}/focused/focused_01.gif" width="80" height="21" border="0" usemap="#focused_01_Map"/>
				</td>
				<td width="100%" height="21" colspan="2" style="background-image:url({$mediaPath}/{$skin}/focused/focused_03.gif); background-repeat:repeat-x;">
					<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="1" height="21"/>
				</td>
				<td width="45">
					<img alt="interface image" src="{$mediaPath}/{$skin}/focused/focused_04.gif" width="45" height="21"/>
				</td>
			</tr>
			<tr>
				<td width="80">
					<img alt="interface image" src="{$mediaPath}/{$skin}/focused/focused_05.gif" width="80" height="33" border="0" usemap="#focused_05_Map"/>
				</td>
				<td width="100%" align="left" valign="bottom" class="uportal-channel-title" nowrap="nowrap">
					<xsl:value-of select="@title"/>
				</td>
				<td align="right" valign="bottom" nowrap="nowrap">
					<!-- <xsl:call-template name="controls"/> -->
				</td>
				<td width="45">
					<img alt="interface image" src="{$mediaPath}/{$skin}/focused/focused_08.gif" width="45" height="33"/>
				</td>
			</tr>
			<tr>
				<td width="80">
					<img alt="interface image" src="{$mediaPath}/{$skin}/focused/focused_09.gif" width="80" height="1"/>
				</td>
				<td colspan="2" class="uportal-background-dark">
					<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="1" height="1"/>
				</td>
				<td width="45">
					<img alt="interface image" src="{$mediaPath}/{$skin}/focused/focused_12.gif" width="45" height="1"/>
				</td>
			</tr>
			<tr>
				<td width="80">
					<img alt="interface image" src="{$mediaPath}/{$skin}/focused/focused_13.gif" width="80" height="15"/>
				</td>
				<td colspan="2">
					<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="1" height="15"/>
				</td>
				<td width="45">
					<img alt="interface image" src="{$mediaPath}/{$skin}/focused/focused_16.gif" width="45" height="15"/>
				</td>
			</tr>
			<tr>
				<td width="80" style="background-image:url({$mediaPath}/{$skin}/focused/focused_17.gif); background-repeat:repeat-y;">
					<img alt="interface image" src="{$mediaPath}/{$skin}/skin/transparent.gif" width="80" height="1"/>
				</td>
				<td align="left" valign="top" colspan="2" class="uportal-channel-text">
					<xsl:if test="@minimized != 'true'">
						<xsl:copy-of select="."/>
					</xsl:if>
				</td>
				<td width="45" style="background-image:url({$mediaPath}/{$skin}/focused/focused_20.gif); background-repeat:repeat-y;">
					<img alt="interface image" src="{$mediaPath}/{$skin}/focused/focused_20.gif" width="45" height="1"/>
				</td>
			</tr>
			<tr>
				<td width="80">
					<img alt="interface image" src="{$mediaPath}/{$skin}/focused/focused_24.gif" width="80" height="50"/>
				</td>
				<td height="50" colspan="2" style="background-image:url({$mediaPath}/{$skin}/focused/focused_26.gif); background-repeat:repeat-x;">
					<img alt="interface image" src="{$mediaPath}/{$skin}/focused/focused_26.gif" width="1" height="50"/>
				</td>
				<td width="45">
					<img alt="interface image" src="{$mediaPath}/{$skin}/focused/focused_27.gif" width="45" height="50"/>
				</td>
			</tr>
		</table>
		<map id="focused_01_Map" name="focused_01_Map">
			<area shape="circle" alt="Return to portal" coords="36,34,19" href="{$baseActionURL}?uP_root=root"/>
		</map>
		<map id="focused_05_Map" name="focused_05_Map">
			<area shape="circle" alt="Return to portal" coords="36,13,19" href="{$baseActionURL}?uP_root=root"/>
		</map>
	</xsl:template>
</xsl:stylesheet>
