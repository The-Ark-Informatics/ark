<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./inventory_menu.xsl"/>
    <xsl:param name="formParams">current=inventory_view</xsl:param>
    <xsl:output method="html" indent="no"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
	 <xsl:param name="biospecimenChannelURL">biospecimenChannelURL_false</xsl:param>

	<xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>
<xsl:param name="strInitialBiospecSampleType">
            <xsl:value-of select="strInitialBiospecSampleType"/>
        </xsl:param>
    <xsl:template match="inventory">
	
	<xsl:variable name="currentCellID"><xsl:value-of select="intCellID"/></xsl:variable>
	<xsl:variable name="currentTrayID"><xsl:value-of select="TRAY_intTrayID"/></xsl:variable>
        <table width="100%">
            <tr valign="top">
                <td width="30%">
                    <table width="250" cellpadding="0" cellspacing="0">
                        <xsl:call-template name="sort_selection"/>
                    </table>
                  
                    <table width="250" cellpadding="0" cellspacing="0">
                        <xsl:apply-templates select="site">
                            <xsl:sort select="availbility" data-type="number" order="descending"/>
                        </xsl:apply-templates>
                            <tr class="funcpanel_content">
                                    <td colspan="11" class="funcpanel_bottom_border">
                                            <img src="media/neuragenix/infopanel/spacer.gif" height="4"
                                                    width="1"/>
                                    </td>
                            </tr>
                    </table>
                    <!-- end of Site -->
                    <br/>
                    <br/>
                    <br/>
                    <!-- admin section -->
                    <xsl:variable name="varAdminSection">
                        <xsl:value-of select="intAdminSection"/>
                    </xsl:variable>
                    <xsl:if test="$varAdminSection=1">
                        <xsl:call-template name="inventory_admin"/>
                    </xsl:if>
                </td>
		
                <td width="70%">
	 <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                <tr valign="bottom">
                                                        <td>
                                                                <img border="0" src="{$infopanelImagePath}/infopanel_top_left1.gif" width="5" height="27"/>
                                                        </td>
                                                        <td>
                                                                <img border="0" src="{$infopanelImagePath}/infopanel_top_left2.gif" width="5" height="27"/>
                                                        </td>
                                                        <td width="100%" height="27" style="background-image: url('{$infopanelImagePath}/infopanel_top_border.gif'); background-repeat: repeat-x;">
                                                                <table width="100%" height="100%" cellpadding="0" cellspacing="0">
                                                                        <tr valign="bottom">
                                                                                <td class="infopanel_title" align="left">
										BATCH ALLOCATION
                                                                                </td>
                                                                                <!-- Subtabs -->
                                                                                <td align="right">
                                                                                                        &#160;
                                                                                </td>	</tr>
                                                                </table>
                                                        </td>
                                                        <td>
                                                                <img border="0" src="{$infopanelImagePath}/infopanel_top_right1.gif" width="5" height="27"/>
                                                        </td>
                                                        <td>
                                                                <img border="0" src="{$infopanelImagePath}/infopanel_top_right2.gif" width="5" height="27"/>
                                                        </td>
                                                </tr>
                                                <tr valign="top">
                                                        <td width="5" style="background-image: url('{$infopanelImagePath}/infopanel_left_border.gif'); background-repeat: repeat-y;">&#160;</td>
                                                        <td width="100%" height="100%" colspan="3">
                                                                <!-- Add Frame Content -->
							<table width="100%">
							<tr>
							<td>
Scan the barcode of the specimen to go into Row: <xsl:value-of select="CELL_strRow"/>, Col: <xsl:value-of select="CELL_strCol"/> <form action="{$baseActionURL}?current=batch_allocate&amp;CELL_intCellID={$currentCellID}&amp;TRAY_intTrayID={$currentTrayID}" method="post" name="batch_form">
<input type="text" name="BIOSPECIMEN_strBiospecimenID" value="" />
</form>



</td><td align="right"><img src="media/org/wager/hand_scanner.jpg"/></td>
							</tr>
							<tr><td colspan="2">
							<center>
                        <table border="0" cellpadding="0" cellspacing="0" style="background-color: white">
                                <td><img src="media/neuragenix/icons/cell_topleft.jpg"/></td>
                                <td style="background-image: url('media/neuragenix/icons/cell_top.jpg')" height="34"></td>
                                <td><img src="media/neuragenix/icons/cell_topright.jpg"/></td>
                        <tr> 
                                <td style="background-image: url('media/neuragenix/icons/cell_left.jpg')" width="39"></td><td align="center" width="243px">
                        <table style="background-color: white">
                                <xsl:for-each select="Row">
                                    <tr>
                                        <xsl:for-each select="Col">
                                            <xsl:variable name="label">
                                                <xsl:value-of select="label"/>
                                            </xsl:variable>
                                            <td style="border-style: solid; border-color: white; border-width: 1px;">
                                                <xsl:choose>
                                                  <xsl:when test="$label='-1'">
                                                  <xsl:variable name="CELL_intCellID">
                                                  <xsl:value-of select="CELL_intCellID"/>
                                                  </xsl:variable>
                                                  <xsl:variable name="CELL_intBiospecimenID">
                                                  <xsl:value-of
                                                  select="CELL_intBiospecimenID"/>
                                                  </xsl:variable>
                                                  <xsl:variable name="CELL_intPatientID">
                                                  <xsl:value-of select="CELL_intPatientID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable name="CELL_info">
                                                  <xsl:value-of select="CELL_info"/>
                                                  </xsl:variable>
                                                  <xsl:choose>
							<xsl:when
                                                  test="$CELL_intCellID = $currentCellID">
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=3&amp;current=biospecimen_view&amp;source=inventory&amp;CELL_intCellID={$CELL_intCellID}&amp;Patient_intInternalPatientID={$CELL_intPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$CELL_intBiospecimenID}&amp;strInitialBiospecSampleType={$strInitialBiospecSampleType}&amp;module=core&amp;action=view_biospecimen">
                                                  <img
                                                  src="media/neuragenix/icons/current.gif"
                                                  border="0"
                                                  title="{$CELL_info}"
                                                  />
                                                  </a>
                                                  </xsl:when>
							<xsl:otherwise>
						  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intBiospecimenID = '-1'">
                                                  <img
                                                  src="media/neuragenix/icons/unused.gif"
                                                  border="0" title="{$CELL_info}"                                                                                                                                                  
                                                  />
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=3&amp;current=biospecimen_view&amp;source=inventory&amp;CELL_intCellID={$CELL_intCellID}&amp;intInternalPatientID={$CELL_intPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$CELL_intBiospecimenID}&amp;strInitialBiospecSampleType={$strInitialBiospecSampleType}&amp;module=core&amp;action=view_biospecimen">
                                                  <img
                                                  src="media/neuragenix/icons/used.gif"
                                                  border="0"
                                                  title="{$CELL_info}"
                                                  />
                                                  </a>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <xsl:value-of select="label"/>
                                                  </xsl:otherwise>
                                                </xsl:choose>
                                            </td>
                                        </xsl:for-each>
                                    </tr>
                                </xsl:for-each>

							</table>
	</td>
                                <td style="background-image: url('media/neuragenix/icons/cell_right.jpg')" width="55"></td></tr>
                                <tr>
                                        <td><img src="media/neuragenix/icons/cell_botleft.jpg"/></td>
                                        <td style="background-image: url('media/neuragenix/icons/cell_bottom.jpg')" height="40"></td>
                                        <td><img src="media/neuragenix/icons/cell_botright.jpg"/></td>
                                </tr>
                        </table>
                        </center>



</td>
							</tr>
							</table> 
                                                        </td>
                                                        <td width="5" style="background-image: url('{$infopanelImagePath}/infopanel_right_border.gif'); background-repeat: repeat-y;">&#160;</td>
                                                </tr>
                                                <tr valign="top">
                                                        <td>
                                                                <img border="0" src="{$infopanelImagePath}/infopanel_bottom_left1.gif" width="5" height="8"/>
                                                        </td>
							<td>
                                                                <img border="0" src="{$infopanelImagePath}/infopanel_bottom_left2.gif" width="5" height="8"/>
                                                        </td>
                                                        <td width="100%" height="8" style="background-image: url('{$infopanelImagePath}/infopanel_bottom_border.gif'); background-repeat: repeat-x;">&#160;</td>
                                                        <td>
                                                                <img border="0" src="{$infopanelImagePath}/infopanel_bottom_right1.gif" width="5" height="8"/>
                                                        </td>
                                                        <td>
                                                                <img border="0" src="{$infopanelImagePath}/infopanel_bottom_right2.gif" width="5" height="8"/>
                                                        </td>
                                                </tr>
                                        </table>
	
                </td>
            </tr>
        </table>
	
	<script language="javascript"> document.batch_form.BIOSPECIMEN_strBiospecimenID.focus();</script>
    </xsl:template>
</xsl:stylesheet>
