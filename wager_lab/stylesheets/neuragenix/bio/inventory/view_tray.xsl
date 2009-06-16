<?xml version="1.0" encoding="utf-8"?>

<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./inventory_menu.xsl"/>
    <xsl:param name="formParams">current=view_tray&amp;save=Save</xsl:param>
    <xsl:output method="html" indent="no"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:param name="downloadURL">downloadURL_false</xsl:param>
    <xsl:param name="nodeId">nodeId_false</xsl:param>
    <xsl:param name="biospecimenChannelURL">biospecimenChannelURL_false</xsl:param>
    <xsl:param name="biospecimenTabOrder"></xsl:param>
    <xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>

    <xsl:template match="inventory">
        <xsl:param name="TRAY_intTrayID">
            <xsl:value-of select="TRAY_intTrayID"/>
        </xsl:param>
        <xsl:param name="TRAY_intBoxID">
            <xsl:value-of select="TRAY_intBoxID"/>
        </xsl:param>
        <xsl:param name="TRAY_strTrayName">
            <xsl:value-of select="TRAY_strTrayName"/>
        </xsl:param>
        <xsl:param name="TRAY_intTrayCapacity">
            <xsl:value-of select="TRAY_intTrayCapacity"/>
        </xsl:param>
        <xsl:param name="TRAY_intTrayAvailable">
            <xsl:value-of select="TRAY_intTrayAvailable"/>
        </xsl:param>
        <xsl:param name="TRAY_intNoOfRow">
            <xsl:value-of select="TRAY_intNoOfRow"/>
        </xsl:param>
        <xsl:param name="TRAY_intNoOfCol">
            <xsl:value-of select="TRAY_intNoOfCol"/>
        </xsl:param>
        <xsl:param name="TRAY_strRowNoType">
            <xsl:value-of select="TRAY_strRowNoType"/>
        </xsl:param>
        <xsl:param name="TRAY_strColNoType">
            <xsl:value-of select="TRAY_strColNoType"/>
        </xsl:param>
        <xsl:param name="strSource">
            <xsl:value-of select="strSource"/>
        </xsl:param>
        <xsl:param name="intCurrentPatientID">
            <xsl:value-of select="intCurrentPatientID"/>
        </xsl:param>
        <xsl:param name="intCurrentBiospecimenID">
            <xsl:value-of select="intCurrentBiospecimenID"/>
        </xsl:param>
        
        <xsl:param name="intCurrentCellID">
            <xsl:value-of select="intCurrentCellID"/>
        </xsl:param>
        <xsl:param name="blLockError">
            <xsl:value-of select="blLockError"/>
        </xsl:param>
        <xsl:param name="strReportName">
            <xsl:value-of select="strReportName"/>
        </xsl:param>
        <xsl:param name="strInitialBiospecSampleType">
            <xsl:value-of select="strInitialBiospecSampleType"/>
        </xsl:param>
        <xsl:param name="strBackButton">
            <xsl:value-of select="strBackButton"/>
        </xsl:param>
        <xsl:param name="blBackToVialCalc">
            
            <xsl:value-of select="blBackToVialCalc"/>
        </xsl:param>
        <SCRIPT LANGUAGE="JavaScript" SRC="htmlarea/popupmouseover.js"> </SCRIPT>
        <script language="javascript">
            Style=["white","black","#6d1b1b","white","","","","","","","","","","",200,"",2,2,10,10,51,1,0,"",""];
            function htmlLineBreaks(str) { var regstr = new String("\n"); var regexpstr = new
            RegExp(regstr, "g"); str2 = str.replace(regexpstr, "&lt;BR&gt;"); return str2; } </script>
        <DIV id="TipLayer" style="visibility:hidden;position:absolute;z-index:1000;top:-100"/>
        <table width="100%">
            <tr valign="top">
                <td width="30%">
                    <table width="250" cellspacing="0" cellpadding="0">
                        <xsl:call-template name="sort_selection"/>
                    </table>


                    <table width="250" cellspacing="0" cellpadding="0">
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
                    <br/>
                    <br/>
                    <br/>
                    <xsl:variable name="varAdminSection">
                        <xsl:value-of select="intAdminSection"/>
                    </xsl:variable>
                    <xsl:variable name="accesstoexpandedsite">
                        <xsl:value-of select="site[site_expanded='true']/siteAccess"/>
                    </xsl:variable>
                    <xsl:if test="$varAdminSection=1 and $accesstoexpandedsite=1">
                        <xsl:call-template name="inventory_admin"/>
                    </xsl:if>
                </td>
                <td width="70%" style="text-align: center">
                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                        <tr valign="bottom">
                            <td>
                                <img border="0" src="{$infopanelImagePath}/infopanel_top_left1.gif"
                                    width="5" height="27"/>
                            </td>
                            <td>
                                <img border="0" src="{$infopanelImagePath}/infopanel_top_left2.gif"
                                    width="5" height="27"/>
                            </td>
                            <td width="100%" height="27"
                                style="background-image: url('{$infopanelImagePath}/infopanel_top_border.gif'); background-repeat: repeat-x;">
                                <table width="100%" height="100%" cellpadding="0" cellspacing="0">
                                    <tr valign="bottom">
                                        <td class="infopanel_title" align="left">
                                            <xsl:value-of select="TRAY_strTitleDisplay"/> details </td>
                                        <!-- Subtabs -->
                                        <td align="right"> &#160; </td>
                                    </tr>

                                </table>
                            </td>
                            <td>
                                <img border="0" src="{$infopanelImagePath}/infopanel_top_right1.gif"
                                    width="5" height="27"/>
                            </td>
                            <td>
                                <img border="0" src="{$infopanelImagePath}/infopanel_top_right2.gif"
                                    width="5" height="27"/>
                            </td>
                        </tr>
                        <tr valign="top">
                            <td width="5"
                                style="background-image: url('{$infopanelImagePath}/infopanel_left_border.gif'); background-repeat: repeat-y;"
                                >&#160;</td>
                            <td/>

                            <td>
                               
                                <form name="saveform" action="{$baseActionURL}?uP_root=root&amp;{$formParams}"
                                    method="post">
                                   
                                    <table width="100%">
                                        <!--<tr>
                                            <td colspan="5" class="uportal-channel-subtitle"> </td>
                                            <td align="right">
                                                
                                                <xsl:if test="$blBackToVialCalc = 'true'">
                                                  <input type="button" name="back_to_vial_calc"
                                                  tabindex="5" value="Back to vial calc"
                                                  class="uportal-button"
                                                  onclick="javascript:jumpTo('{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTabOrder}&amp;current=create_sub_specimen&amp;back=true')"
                                                  />
                                                </xsl:if>
                                            </td>
                                        </tr>-->
                                       
                                        <tr>
                                            <td>
                                                <table>
                                                  <tr>
                                                      
                                                  <!-- Save button -->
                                                      <td>
                                                          <xsl:if test="hasEditRights">
                                                              <a class="button" href="{$baseActionURL}?uP_root=root&amp;current=add_transfer&amp;TRANSFER_intTrayID={$TRAY_intTrayID}" onclick="this.blur();"><span><img src="/wagerlab/media/neuragenix/icons/transfer.png" height="14" align="top" border="0"/>Transfer</span></a>  
                                         
                                                          <a class="button" href="#" onclick="this.blur(); document.location.href='/wagerlab/DefaultBarcode.prn?trayid={$TRAY_intTrayID}';"><span><img src="/wagerlab/media/neuragenix/icons/printer.png" height="14" align="top" border="0"/>Print Box</span></a>  
                                                          
                                                          <a class="button" href="#" onclick="this.blur(); window.open('{$downloadURL}?uP_root={$nodeId}&amp;file_name={$strReportName}&amp;property_name=neuragenix.bio.search.ExportFileLocation&amp;activity_required=inventory_view')"><span><img src="/wagerlab/media/neuragenix/icons/report.png" height="14" align="top" border="0"/>Box Report</span></a>
                                                           
                                                              <a class="button" href="#" onclick="this.blur(); document.location.href='{$baseActionURL}?uP_root=root&amp;current=batch_allocate&amp;TRAY_intTrayID={$TRAY_intTrayID}'"><span><img src="/wagerlab/media/neuragenix/icons/scanner.gif" height="14" align="top" border="0"/>Batch Allocate</span></a>
                                   
                                                              <a class="button" href="#" onclick="this.blur(); document.location.href='{$baseActionURL}?uP_root=root&amp;current=batch_unallocate&amp;TRAY_intTrayID={$TRAY_intTrayID}'"><span>Unallocate</span></a>
                                                              </xsl:if>
                                                          <xsl:if test="string($strBackButton)">
                                                          <a class="button" href="#" onclick="this.blur(); jumpTo('{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTabOrder}&amp;{$strBackButton}')"><span>Back</span></a>
                                                          </xsl:if>
                                                  </td>
                                                  
                                                  <!-- Print Tray -->
                                                  
                                                 <!-- <table cellpadding="0" cellspacing="0">
                                                  <tr>
                                                  <td>
                                                  <img
                                                  src="media/neuragenix/icons/button_left.gif"
                                                  />
                                                  </td>
                                                  <td
                                                  style="background-image: url('media/neuragenix/icons/button_bgnd.gif')">
                                                  <a
                                                  style="font-weight:bold; text-decoration: none; color: black"
                                                  href="/Barcode.prn?trayid={$TRAY_intTrayID}"
                                                  >Print Tray</a>
                                                  </td>
                                                  <td>
                                                  <img src="media/neuragenix/icons/button_right.gif" />
                                                  </td>
                                                  </tr>
                                                  </table>-->
                                                  
                                                     <!-- <td><input
                                                          value="Print Tray"
                                                          onclick="document.location.href='/Barcode.prn?trayid={$TRAY_intTrayID}'"
                                                          class="uportal-button"
                                                          type="button"/></td>-->
                                                  <!-- Generate Report Button -->
                                                 
                                                      <!--<input type="button" name="delete"
                                                          value="Delete" tabindex="9" class="uportal-button"
                                                          onclick="javascript:confirmDelete('{$baseActionURL}?uP_root=root&amp;current=view_tray&amp;TRAY_intTrayID={$TRAY_intTrayID}')"
                                                          align="right"/>-->
                                                      </tr>
                                                      </table>
                                                </td>
                                        </tr>
                                        <tr>
                                            <td colspan="6">
                                                <hr/>
                                            </td>
                                        </tr>

                                    </table>
                                    
                                    <table width="100%">
                                        <tr>
                                            <td class="neuragenix-form-required-text">
                                                <xsl:value-of select="strErrorMessage"/>
                                            </td>
                                        </tr>
                                    </table>
                                    <table width="100%">
                                        <xsl:if test="$blLockError = 'true'">
                                            <tr>
                                                <td colspan="5"
                                                  class="neuragenix-form-required-text"> This
                                                  record is being viewed by other users. You
                                                  cannot update it now. Please try again later.
                                                </td>
                                            </tr>
                                            <tr>
                                                <td height="10px"/>
                                            </tr>
                                        </xsl:if>
                                        <tr>
                                            <td width="20%" class="uportal-label">
                                                <xsl:value-of select="TRAY_intTrayCapacityDisplay"
                                                />: </td>
                                            <td width="15%" style="text-align: right"
                                                class="uportal-input-text">
                                                <xsl:if test="$TRAY_intTrayCapacity &gt; 1">
                                                  <xsl:value-of select="TRAY_intTrayCapacity"/>
                                                  cells </xsl:if>
                                                <xsl:if test="$TRAY_intTrayCapacity &lt; 2">
                                                  <xsl:value-of select="TRAY_intTrayCapacity"/>
                                                  cell </xsl:if>
                                            </td>
                                            <td width="20%"/>
                                            <td width="20%" class="uportal-label">
                                                <xsl:value-of select="TRAY_intTrayAvailableDisplay"
                                                />: </td>
                                            <td width="15%" style="text-align: right"
                                                class="uportal-input-text">
                                                <xsl:if test="$TRAY_intTrayAvailable &gt; 1">
                                                  <xsl:value-of select="TRAY_intTrayAvailable"/>
                                                  cells </xsl:if>
                                                <xsl:if test="$TRAY_intTrayAvailable &lt; 2">
                                                  <xsl:value-of select="TRAY_intTrayAvailable"/>
                                                  cell </xsl:if>
                                            </td>
                                            <td width="10%"/>
                                        </tr>
                                    </table>
                                    <table width="100%">
                                        <tr>
                                            <td width="20%" class="uportal-label">
                                                <xsl:value-of select="BOX_strTitleDisplay"/> name: </td>
                                            <td width="25%">
                                                <select name="TRAY_intBoxID" tabindex="1"
                                                  class="uportal-input-text">
                                                  <xsl:for-each select="search_box">
                                                  <xsl:variable name="varBoxID">
                                                  <xsl:value-of select="BOX_intBoxID"/>
                                                  </xsl:variable>
                                                  <option>
                                                  <xsl:attribute name="value">
                                                  <xsl:value-of select="BOX_intBoxID"
                                                  />
                                                  </xsl:attribute>
                                                  <xsl:if test="$TRAY_intBoxID=$varBoxID">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                      <xsl:value-of select="SITE_strSiteName" /> &gt; <xsl:value-of select="TANK_strTankName" /> &gt;  <xsl:value-of select="BOX_strBoxName" />
                                                  </option>
                                                  </xsl:for-each>
                                                </select>
                                            </td>
                                            <td width="10%"/>
                                            <td width="20%" class="uportal-label">
                                                <xsl:value-of select="TRAY_intNoOfColDisplay"/>: </td>
                                            <td width="25%">
                                                <input type="text" style="text-align: right"
                                                  name="TRAY_intNoOfCol"
                                                  value="{$TRAY_intNoOfCol}" size="4"
                                                  tabindex="5" class="uportal-input-text"/>
                                                <select name="TRAY_strColNoType" tabindex="6"
                                                  class="uportal-input-text">
                                                  <xsl:for-each select="TRAY_strColNoType">
                                                  <option>
                                                  <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                  </xsl:attribute>
                                                  <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                  <xsl:value-of select="."/>
                                                  </option>
                                                  </xsl:for-each>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="20%" class="uportal-label">
                                                <xsl:value-of select="TRAY_strTrayNameDisplay"/>: </td>
                                            <td width="25%">
                                                <input type="text" name="TRAY_strTrayName"
                                                  value="{$TRAY_strTrayName}" size="22"
                                                  tabindex="2" class="uportal-input-text"/>
                                            </td>
                                            <td width="10%"/>
                                            <td width="20%" class="uportal-label">
                                                <xsl:value-of select="TRAY_intNoOfRowDisplay"/>: </td>
                                            <td width="25%">
                                                <input type="text" style="text-align: right"
                                                  name="TRAY_intNoOfRow"
                                                  value="{$TRAY_intNoOfRow}" size="4"
                                                  tabindex="3" class="uportal-input-text"/>
                                                <select name="TRAY_strRowNoType" tabindex="4"
                                                  class="uportal-input-text">
                                                  <xsl:for-each select="TRAY_strRowNoType">
                                                  <option>
                                                  <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                  </xsl:attribute>
                                                  <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                  <xsl:value-of select="."/>
                                                  </option>
                                                  </xsl:for-each>
                                                </select>
                                            </td>
                                            
                                        </tr>
                                        <tr>
                                            <td colspan="5">
                                                <hr/>
                                            </td>
                                        </tr>
                                    </table>
                                    <style type="text/css"> .submitLink { background-color:
                                        transparent; text-decoration: none; font-weight: bold;
                                        border: none; cursor: pointer; cursor: hand; } </style>
                                   <!-- <table width="100%">
                                        <tr>
                                            <td width="20%">

                                                <table cellpadding="0" cellspacing="0">
                                                  <tr>
                                                  <td>
                                                  <img
                                                  src="media/neuragenix/icons/button_left.gif"
                                                  />
                                                  </td>
                                                  <td
                                                  style="background-image: url('media/neuragenix/icons/button_bgnd.gif')">
                                                  <a
                                                  style="font-weight:bold; text-decoration: none; color: black"
                                                  href="javascript:confirmDelete('{$baseActionURL}?uP_root=root&amp;current=view_tray&amp;TRAY_intTrayID={$TRAY_intTrayID}')"
                                                  >Delete</a>
                                                  </td>
                                                  <td>
                                                  <img
                                                  src="media/neuragenix/icons/button_right.gif"
                                                  />
                                                  </td>
                                                  </tr>
                                                </table>
                                            </td>
                                            <td width="5%">
                                                <table cellpadding="0" cellspacing="0">
                                                  <tr>
                                                  <td>
                                                  <img
                                                  src="media/neuragenix/icons/button_left.gif"
                                                  />
                                                  </td>
                                                  <td
                                                  style="background-image: url('media/neuragenix/icons/button_bgnd.gif')">
                                                  <a
                                                  style="font-weight:bold; text-decoration: none; color: black"
                                                  href="/Barcode.prn?trayid={$TRAY_intTrayID}"
                                                  >Print Tray</a>
                                                  </td>
                                                  <td>
                                                  <img
                                                  src="media/neuragenix/icons/button_right.gif"
                                                  />
                                                  </td>
                                                  </tr>
                                                </table>
                                            </td>
                                            <td width="5%" align="right">
                                                <table cellpadding="0" cellspacing="0">
                                                  <tr>
                                                  <td>
                                                  <img
                                                  src="media/neuragenix/icons/button_left.gif"
                                                  />
                                                  </td>
                                                  <td
                                                  style="background-image: url('media/neuragenix/icons/button_bgnd.gif')">
                                                  <input type="submit" name="save"
                                                  value="Save" class="submitLink"/>
                                                  </td>
                                                  <td>
                                                  <img
                                                  src="media/neuragenix/icons/button_right.gif"
                                                  />
                                                  </td>
                                                  </tr>
                                                </table>
                                                <input type="button" name="generateReport"
                                                  value="Generate report" tabindex="9"
                                                  class="uportal-button"
                                                  onclick="javascript:window.open('{$downloadURL}?uP_root={$nodeId}&amp;file_name={$strReportName}&amp;property_name=neuragenix.bio.search.ExportFileLocation&amp;activity_required=inventory_view')"
                                                  align="right"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td height="20px"/>
                                        </tr>
                                        </table>-->
                                    
                                    <table width="100%">
                                        <tr>
                                            <td width="50%" align="left">
                                                <xsl:if test="hasEditRights">  <a class="button" href="#" onclick="this.blur(); confirmDelete('{$baseActionURL}?uP_root=root&amp;current=view_tray&amp;TRAY_intTrayID={$TRAY_intTrayID}')" > <span><img src="/wagerlab/media/neuragenix/icons/delete.png" height="14" align="top" border="0"/> Delete</span></a></xsl:if>
                                            </td>
                                            
                                            <td width="50%" align="right">
                                                <xsl:if test="hasEditRights or hasSaveOnlyRights">  <a class="button" name="save" href="#" onclick="this.blur(); document.saveform.submit()"><span><img src="/wagerlab/media/neuragenix/icons/disk.png" height="14" align="top" border="0"/> Save</span></a>  </xsl:if>
                                            </td>
                                        </tr>
                                        
                                    </table>
                                   
                                    
                                    <table align="center">
                                        <tr>
                                            <td style="text-align: center">LOCATION GRID</td>
                                        </tr>
                                        <tr>
                                            <td height="20px"/>
                                        </tr>
                                        <tr>
                                            <td style="text-align: center" class="uportal-text">To
                                                view cell information, place mouse over it.</td>
                                        </tr>
                                       <!-- <xsl:if test="$strSource = 'inventory'">
                                            <tr>
                                                <td style="text-align: center" class="uportal-text">

                                                  <table cellpadding="0" cellspacing="0"
                                                  align="center">
                                                  <tr>
                                                  <td>
                                                  <img
                                                  src="media/neuragenix/icons/button_left.gif"
                                                  />
                                                  </td>
                                                  <td
                                                  style="background-image: url('media/neuragenix/icons/button_bgnd.gif')">
                                                  <input type="submit"
                                                  name="change_cells"
                                                  class="submitLink"
                                                  value="Change cell locations"
                                                  tabindex="10"/>
                                                  </td>
                                                  <td>
                                                  <img
                                                  src="media/neuragenix/icons/button_right.gif"
                                                  />
                                                  </td>
                                                  </tr>
                                                  </table>
                                                </td>
                                            </tr>
                                            </xsl:if>-->
                                        
                                        <tr>
                                            <td height="10px"/>
                                        </tr>
                                    </table>
                                    <table border="0" cellpadding="0" cellspacing="0"
                                        style="background-color: white" align="center">
                                        <tr>
                                           <!-- <td colspan="3">
                                                <img src="media/neuragenix/icons/celltop.jpg"/>
                                                </td> -->
                                            <td><img src="media/neuragenix/icons/cell_topleft.jpg"/></td>
                                            <td style="background-image: url('media/neuragenix/icons/cell_top.jpg')" height="34"></td>
                                            <td><img src="media/neuragenix/icons/cell_topright.jpg"/></td>
                                        </tr>
                                        <tr>
                                           
                                                <td style="background-image: url('media/neuragenix/icons/cell_left.jpg')" width="39"></td>
                                            
                                            <td align="center" width="243px">
                                                <table style="background-color: white">
                                                  <xsl:if test="$strSource = 'allocate'">
                                                  <xsl:for-each select="Row">
                                                  <tr>
                                                  <xsl:for-each select="Col">
                                                  <xsl:variable name="label">
                                                  <xsl:value-of select="label"
                                                  />
                                                  </xsl:variable>
                                                  <td
                                                  style="border-style: solid; border-color: white; border-width: 1px;">
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$label='0'"/>
                                                  <xsl:when
                                                  test="$label='-1'">
                                                  <xsl:variable
                                                  name="CELL_intCellID">
                                                  <xsl:value-of
                                                  select="CELL_intCellID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_intWithdrawn">
                                                  <xsl:value-of
                                                  select="CELL_intWithdrawn"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_intBiospecimenID">
                                                  <xsl:value-of
                                                  select="CELL_intBiospecimenID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_intPatientID">
                                                  <xsl:value-of
                                                  select="CELL_intPatientID"
                                                  />
                                                  </xsl:variable>
                                                  <!-- get into the cell -->
                                                  <xsl:variable
                                                  name="CELL_info">
                                                  <xsl:value-of
                                                  select="CELL_info"
                                                  />
                                                  </xsl:variable>
                                                  <script
                                                  language="javascript"
                                                  >var cellinfo =
                                                  "<![CDATA[]]><xsl:value-of
                                                  select="CELL_info"
                                                  /><![CDATA[";]]>
                                                  var
                                                  celltitle="Biospecimen";</script>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intBiospecimenID = '-1'">
                                                  <!-- unallocated cell -->
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTabOrder}&amp;current=biospecimen_view&amp;source=inventory&amp;module=ALLOCATE_CELL&amp;action=allocate&amp;CELL_intCellID={$CELL_intCellID}&amp;PATIENT_intInternalPatientID={$intCurrentPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intCurrentBiospecimenID}&amp;BIOSPECIMEN_strInitialBiospecSampleType={$strInitialBiospecSampleType}">
                                                  <img
                                                  src="media/neuragenix/icons/unused.gif"
                                                  border="0"
                                                  onmouseover="tooltip.show(htmlLineBreaks(new String('{$CELL_info}')))"
                                                  onmouseout="tooltip.hide()"
                                                  />
                                                  </a>
                                                  </xsl:when>
                                                  <xsl:when
                                                  test="$CELL_intWithdrawn = '1'">
                                                  <!-- withdrawn cell -->
                                                  <img
                                                  src="media/neuragenix/icons/withdrawn.gif"
                                                  border="0"
                                                  onmouseover="tooltip.show(htmlLineBreaks(new String('{$CELL_info}')))"
                                                  onmouseout="tooltip.hide()"
                                                  />
                                                      
                                                      
                                                  </xsl:when>
                                                  <!-- cell occupied -->
                                                  <xsl:otherwise>
                                                  <img
                                                  src="media/neuragenix/icons/used.gif"
                                                  border="0"
                                                  onmouseover="tooltip.show(htmlLineBreaks(new String('{$CELL_info}')))"
                                                  onmouseout="tooltip.hide()"
                                                  />
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <xsl:value-of
                                                  select="label"/>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </td>
                                                  </xsl:for-each>
                                                  </tr>
                                                  </xsl:for-each>
                                                  </xsl:if>
                                                  <!-- changing the location of the bispecimen -->
                                                  <xsl:if test="$strSource = 'change'">
                                                  <xsl:for-each select="Row">
                                                  <tr>
                                                  <xsl:for-each select="Col">
                                                  <xsl:variable name="label">
                                                  <xsl:value-of select="label"
                                                  />
                                                  </xsl:variable>
                                                  <td
                                                  style="border-style: solid; border-color: white; border-width: 1px;">
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$label='-1'">
                                                  <xsl:variable
                                                  name="CELL_intCellID">
                                                  <xsl:value-of
                                                  select="CELL_intCellID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_intBiospecimenID">
                                                  <xsl:value-of
                                                  select="CELL_intBiospecimenID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_intPatientID">
                                                  <xsl:value-of
                                                  select="CELL_intPatientID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_info">
                                                  <xsl:value-of
                                                  select="CELL_info"
                                                  />
                                                  </xsl:variable>
                                                  <script
                                                  language="javascript"
                                                  >var cellinfo =
                                                  "<![CDATA[]]><xsl:value-of
                                                  select="CELL_info"
                                                  /><![CDATA[";]]>
                                                  var
                                                  celltitle="Biospecimen";</script>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intBiospecimenID = '-1'">
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTabOrder}&amp;current=biospecimen_view&amp;source=inventory&amp;module=ALLOCATE_CELL&amp;CELL_intCellID={$CELL_intCellID}&amp;PATIENT_intInternalPatientID={$intCurrentPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intCurrentBiospecimenID}&amp;BIOSPECIMEN_strInitialBiospecSampleType={$strInitialBiospecSampleType}&amp;action=relocate">
                                                  <img
                                                  src="media/neuragenix/icons/unused.gif"
                                                  border="0"
                                                  onmouseover="tooltip.show(htmlLineBreaks(new String('{$CELL_info}')))"
                                                  onmouseout="tooltip.hide()"
                                                  />
                                                  </a>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intCellID = $intCurrentCellID">
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTabOrder}&amp;current=biospecimen_view&amp;source=inventory&amp;CELL_intCellID={$CELL_intCellID}&amp;intInternalPatientID={$CELL_intPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$CELL_intBiospecimenID}&amp;BIOSPECIMEN_strInitialBiospecSampleType={$strInitialBiospecSampleType}">
                                                  <img
                                                  src="media/neuragenix/icons/current.gif"
                                                  border="0"
                                                  onmouseover="tooltip.show(htmlLineBreaks(new String('{$CELL_info}')))"
                                                  onmouseout="tooltip.hide()"
                                                  />
                                                  </a>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <img
                                                  src="media/neuragenix/icons/used.gif"
                                                  border="0"
                                                  onmouseover="tooltip.show(htmlLineBreaks(new String('{$CELL_info}')))"
                                                  onmouseout="tooltip.hide()"
                                                  />
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <xsl:value-of
                                                  select="label"/>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </td>
                                                  </xsl:for-each>
                                                  </tr>
                                                  </xsl:for-each>
                                                  </xsl:if>
                                                  <!-- view only -->
                                                  <xsl:if test="$strSource = 'view'">
                                                  <xsl:for-each select="Row">
                                                  <tr>
                                                  <xsl:for-each select="Col">
                                                  <xsl:variable name="label">
                                                  <xsl:value-of select="label"
                                                  />
                                                  </xsl:variable>
                                                  <td
                                                  style="border-style: solid; border-color: white; border-width: 1px;">
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$label='-1'">
                                                  <xsl:variable
                                                  name="CELL_intCellID">
                                                  <xsl:value-of
                                                  select="CELL_intCellID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_intBiospecimenID">
                                                  <xsl:value-of
                                                  select="CELL_intBiospecimenID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_intPatientID">
                                                  <xsl:value-of
                                                  select="CELL_intPatientID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_intWithdrawn">
                                                  <xsl:value-of
                                                  select="CELL_intWithdrawn"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_info">
                                                  <xsl:value-of
                                                  select="CELL_info"
                                                  />
                                                  </xsl:variable>
                                                  <script
                                                  language="javascript"
                                                  >var cellinfo =
                                                  "<![CDATA[]]><xsl:value-of
                                                  select="CELL_info"
                                                  /><![CDATA[";]]>
                                                  var
                                                  celltitle="Biospecimen";</script>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intBiospecimenID = '-1'">
                                                  <img
                                                  src="media/neuragenix/icons/unused.gif"
                                                  border="0"
                                                  onmouseover="tooltip.show(htmlLineBreaks(new String('{$CELL_info}')))"
                                                  onmouseout="tooltip.hide()"
                                                  />
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intCellID = $intCurrentCellID">
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTabOrder}&amp;current=biospecimen_view&amp;source=inventory&amp;CELL_intCellID={$CELL_intCellID}&amp;Patient_intInternalPatientID={$CELL_intPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$CELL_intBiospecimenID}&amp;strInitialBiospecSampleType={$strInitialBiospecSampleType}&amp;module=core&amp;action=view_biospecimen">
                                                  <img
                                                  src="media/neuragenix/icons/current.gif"
                                                  border="0"
                                                  onmouseover="tooltip.show(htmlLineBreaks(new String('{$CELL_info}')))"
                                                  onmouseout="tooltip.hide()"
                                                  />
                                                  </a>
                                                  </xsl:when>
                                                  <xsl:when
                                                  test="$CELL_intWithdrawn = '1'">
                                                  <!-- withdrawn cell -->
                                                  <img
                                                  src="media/neuragenix/icons/withdrawn.gif"
                                                  border="0"
                                                  onmouseover="tooltip.show(htmlLineBreaks(new String('{$CELL_info}')))"
                                                  onmouseout="tooltip.hide()"
                                                  />
                                                  </xsl:when>

                                                  <xsl:otherwise>
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTabOrder}&amp;current=biospecimen_view&amp;source=inventory&amp;CELL_intCellID={$CELL_intCellID}&amp;intInternalPatientID={$CELL_intPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$CELL_intBiospecimenID}&amp;strInitialBiospecSampleType={$strInitialBiospecSampleType}&amp;module=core&amp;action=view_biospecimen">
                                                  <img
                                                  src="media/neuragenix/icons/used.gif"
                                                  border="0"
                                                  onmouseover="tooltip.show(htmlLineBreaks(new String('{$CELL_info}')))"
                                                  onmouseout="tooltip.hide()"
                                                  />
                                                  </a>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <xsl:value-of
                                                  select="label"/>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </td>
                                                  </xsl:for-each>
                                                  </tr>
                                                  </xsl:for-each>
                                                  </xsl:if>
                                                  <xsl:if test="$strSource = 'inventory'">
                                                  <xsl:for-each select="Row">
                                                  <tr>
                                                  <xsl:for-each select="Col">
                                                  <xsl:variable name="label">
                                                  <xsl:value-of select="label"
                                                  />
                                                  </xsl:variable>
                                                  <td
                                                  style="border-style: solid; border-color: white; border-width: 1px;">
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$label='0'"/>
                                                  <xsl:when
                                                  test="$label='-1'">
                                                  <xsl:variable
                                                  name="CELL_intCellID">
                                                  <xsl:value-of
                                                  select="CELL_intCellID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_intBiospecimenID">
                                                  <xsl:value-of
                                                  select="CELL_intBiospecimenID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_intPatientID">
                                                  <xsl:value-of
                                                  select="CELL_intPatientID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_intWithdrawn">
                                                  <xsl:value-of
                                                  select="CELL_intWithdrawn"
                                                  />
                                                  </xsl:variable>

                                                  <xsl:variable
                                                  name="CELL_info">
                                                  <xsl:value-of
                                                  select="CELL_info"
                                                  />
                                                  </xsl:variable>
                                                  <script
                                                  language="javascript"
                                                  >var cellinfo =
                                                  "<![CDATA[]]><xsl:value-of
                                                  select="CELL_info"
                                                  /><![CDATA[";]]>
                                                  var
                                                  celltitle="Biospecimen";</script>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intBiospecimenID = '-1'">
                                                  <img
                                                  src="media/neuragenix/icons/unused.gif"
                                                  border="0"
                                                  onmouseover="tooltip.show(htmlLineBreaks(new String('{$CELL_info}')))"
                                                  onmouseout="tooltip.hide()"
                                                  />
                                                  </xsl:when>
                                                  <xsl:when
                                                  test="$CELL_intWithdrawn = '1'">
                                                  <!-- withdrawn cell -->
                                                  <img
                                                  src="media/neuragenix/icons/withdrawn.gif"
                                                  border="0"
                                                  onmouseover="tooltip.show(htmlLineBreaks(new String('{$CELL_info}')))"
                                                  onmouseout="tooltip.hide()"
                                                  />
                                                  </xsl:when>

                                                  <xsl:otherwise>
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTabOrder}&amp;current=biospecimen_view&amp;source=inventory&amp;CELL_intCellID={$CELL_intCellID}&amp;intInternalPatientID={$CELL_intPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$CELL_intBiospecimenID}&amp;strInitialBiospecSampleType={$strInitialBiospecSampleType}&amp;module=core&amp;action=view_biospecimen">
                                                  <img
                                                  src="media/neuragenix/icons/used.gif"
                                                  border="0"
                                                  onmouseover="tooltip.show(htmlLineBreaks(new String('{$CELL_info}')))"
                                                  onmouseout="tooltip.hide()"
                                                  />
                                                  </a>
                                                  </xsl:otherwise>
                                                      
                                                  </xsl:choose>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <xsl:value-of
                                                  select="label"/>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </td>
                                                  </xsl:for-each>
                                                  </tr>
                                                  </xsl:for-each>
                                                  </xsl:if>
                                                </table>
                                            </td>
                                            <td style="background-image: url('media/neuragenix/icons/cell_right.jpg')" width="55"></td>
                                        </tr>
                                        <tr>
                                            <td><img src="media/neuragenix/icons/cell_botleft.jpg"/></td>
                                            <td style="background-image: url('media/neuragenix/icons/cell_bottom.jpg')" height="40"></td>
                                            <td><img src="media/neuragenix/icons/cell_botright.jpg"/></td>
                                        </tr>
                                    </table>
                                    <input type="hidden" name="TRAY_intTrayID"
                                        value="{$TRAY_intTrayID}"/>
                                    <input type="hidden" name="TRAY_intTrayCapacity"
                                        value="{$TRAY_intTrayCapacity}"/>
                                    <input type="hidden" name="TRAY_intTrayAvailable"
                                        value="{$TRAY_intTrayAvailable}"/>
                                    <xsl:if test="$blBackToVialCalc = 'true'">
                                        <input type="hidden" name="vial_calc" value="true"/>
                                    </xsl:if>


                                </form>
                            </td>
                            <td/>
                            <td width="5"
                                style="background-image: url('{$infopanelImagePath}/infopanel_right_border.gif'); background-repeat: repeat-y;"
                                >&#160;</td>
                        </tr>
                        <tr valign="top">
                            <td>
                                <img border="0"
                                    src="{$infopanelImagePath}/infopanel_bottom_left1.gif" width="5"
                                    height="8"/>
                            </td>
                            <td>
                                <img border="0"
                                    src="{$infopanelImagePath}/infopanel_bottom_left2.gif" width="5"
                                    height="8"/>
                            </td>
                            <td height="8"
                                style="background-image: url('{$infopanelImagePath}/infopanel_bottom_border.gif'); background-repeat: repeat-x;"
                                >&#160;</td>
                            <td>
                                <img border="0"
                                    src="{$infopanelImagePath}/infopanel_bottom_right1.gif"
                                    width="5" height="8"/>
                            </td>
                            <td>
                                <img border="0"
                                    src="{$infopanelImagePath}/infopanel_bottom_right2.gif"
                                    width="5" height="8"/>
                            </td>
                        </tr>


                    </table>

                </td>
            </tr>
        </table>

    </xsl:template>
</xsl:stylesheet>
