<?xml version="1.0" encoding="utf-8"?>

<!-- 
    alert_list.xsl, part of the Workspace channel
    Author: hhoang@neuragenix.com
    Date: 02/03/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./workspace_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="mediaPath">media/org/jasig/portal/layout/tab-column/nested-tables</xsl:variable>
  
    <xsl:template match="workspace">
    <xsl:param name="isHasIncomingAlert"><xsl:value-of select="isHasIncomingAlert" /></xsl:param>
    
    <form name="navigationForm" action="{$baseActionURL}?current=alert_list" method="post">
 
    <table width="100%">
        <tr>
            <td width="25%"></td>
            <td width="75%" class="uportal-channel-subtitle">
                   
                    <a href="{$baseActionURL}?current=view_active_task" onMouseOver="document.images['view_active_task'].src= '{$mediaPath}/casegenix/controls/view_active_task_on.gif'" onMouseOut="document.images['view_active_task'].src='{$mediaPath}/casegenix/controls/view_active_task_off.gif'"><img name="view_active_task" src="{$mediaPath}/casegenix/controls/view_active_task_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=search_task" onMouseOver="document.images['search_task'].src= '{$mediaPath}/casegenix/controls/search_task_on.gif'" onMouseOut="document.images['search_task'].src='{$mediaPath}/casegenix/controls/search_task_off.gif'"><img name="search_task" src="{$mediaPath}/casegenix/controls/search_task_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=reassign_tasks" onMouseOver="document.images['reasign_all_tasks'].src= '{$mediaPath}/casegenix/controls/reasign_all_tasks_on.gif'" onMouseOut="document.images['reasign_all_tasks'].src='{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif'"><img name="reasign_all_tasks" src="{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=create_alert" onMouseOver="document.images['create_alert'].src= '{$mediaPath}/casegenix/controls/create_alert_on.gif'" onMouseOut="document.images['create_alert'].src='{$mediaPath}/casegenix/controls/create_alert_off.gif'"><img name="create_alert" src="{$mediaPath}/casegenix/controls/create_alert_off.gif" border="0"></img></a> 
                    
                    <img name="alert_list" src="{$mediaPath}/casegenix/controls/alert_list_on.gif" border="0"></img><xsl:if test="$isHasIncomingAlert = 'true'"><img src="media/neuragenix/icons/alert.gif" border="0" /></xsl:if>
                   
            
            </td>
            <td width="10%">
                <input type="submit" name="refresh" tabindex="1" value="Refresh" class="uportal-button" />
            </td>
        </tr>
        
    </table>
    <hr/>
    <table width="100%">
        <tr>
            <td width="5%"></td>
            <td width="75%" class="neuragenix-form-required-text">
                <xsl:value-of select="strErrorMessage" />
            </td>
            <td width="20%" class="neuragenix-form-required-text" align="right"></td>
        </tr>
        
    </table>
    
    <table width="100%">
        
        <tr>
            <td colspan="7" class="uportal-channel-title">Incoming alerts</td>
        </tr>
        <tr>
            <td colspan="7" height="20px"></td>
        </tr>
        <tr>
            <td width="5%"></td>
            <td width="20%" class="uportal-channel-table-header">
                <xsl:value-of select="WORKSPACE_ALERT_strNameDisplay" />
            </td>
            <td width="10%" class="uportal-channel-table-header">
                <xsl:value-of select="WORKSPACE_ALERT_intSendToUserKeyDisplay" />
            </td>
            <td width="10%" class="uportal-channel-table-header">
                <xsl:value-of select="WORKSPACE_ALERT_dtDateDisplay" />
            </td>
            <td width="10%" class="uportal-channel-table-header">
                <xsl:value-of select="WORKSPACE_ALERT_tmTimeDisplay" />
            </td>
            <td width="40%" class="uportal-channel-table-header">
                <xsl:value-of select="WORKSPACE_ALERT_strDescriptionDisplay" />
            </td>
            
            <td width="5%"></td>
        </tr>
        
        <tr>
            <td colspan="7" height="10px"></td>
        </tr>
        
        <xsl:for-each select="search_incoming_alert">
        <xsl:variable name="WORKSPACE_ALERT_intAlertKey"><xsl:value-of select="WORKSPACE_ALERT_intAlertKey" /></xsl:variable>
        <xsl:variable name="WORKSPACE_ALERT_intFromUserKey"><xsl:value-of select="WORKSPACE_ALERT_intFromUserKey" /></xsl:variable>
        
        <tr>
            <td width="5%"></td>
            <td width="20%" class="uportal-label"> 
                <xsl:value-of select="WORKSPACE_ALERT_strName" />
            </td>

            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKSPACE_ALERT_strSendToUserName" />
            </td>

            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKSPACE_ALERT_dtDate" />
            </td>

            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKSPACE_ALERT_tmTime" />
            </td>

            <td width="40%" class="uportal-label">
                <xsl:value-of select="WORKSPACE_ALERT_strDescription" />
            </td>
            <td width="5%" class="uportal-label">
                <a href="{$baseActionURL}?current=alert_list&amp;remove=true&amp;WORKSPACE_ALERT_intAlertKey={$WORKSPACE_ALERT_intAlertKey}">
                    Remove
                </a>
            </td>
        </tr>
        </xsl:for-each>
    </table>
    
                <xsl:variable name="intCurrentPage"><xsl:value-of select="currentPageI" /></xsl:variable>
                <xsl:variable name="intNoOfPages"><xsl:value-of select="noOfPagesI" /></xsl:variable>
                <xsl:variable name="intRecordPerPage"><xsl:value-of select="noOfRecordsI" /></xsl:variable>
                <script language="javascript">

                    function submitNewPageI( offset ){

                            document.navigationForm.currentPageI.value = parseInt( document.navigationForm.currentPageI.value ) + parseInt(offset);

                            document.navigationForm.submit();

                    }
                </script>
    
                <xsl:if test="count( search_incoming_alert ) > 0">
                <table width="100%">
                    <tr>
                        <td class="uportal-channel-subtitle">
                            <hr/>
                        </td>
                    </tr>
                </table>
                <center>
                <!--form name="navigationForm" action="{$baseActionURL}?current=view_active_task" method="post"-->

                    <table >

                        <tr>          

                            <td class="uportal-label">
                                Page:
                            </td>

                            <td class="uportal-label">
                                <input type="submit" name="previous" value="{$ltBtnLabel}" tabindex="1" class="uportal-button"  onclick="javascript:submitNewPageI('-1')"/>
                            </td>

                            <td class="uportal-label">
                                <input type="text" name="currentPageI" size="4" tabindex="2" value="{$intCurrentPage}" align="right" class="uportal-input-text" />
                            </td>

                            <td class="uportal-label">
                                of <xsl:value-of select="noOfPagesI" />
                            </td>

                            <td class="uportal-label">
                                <input type="button" name="next" value="{$gtBtnLabel}" tabindex="3" class="uportal-button" onclick="javascript:submitNewPageI('1')"/>
                            </td>

                            <td class="uportal-label">
                                <input type="submit" name="go" value="{$goBtnLabel}" tabindex="4" class="uportal-button" />
                            </td>

                            <td>&#160;&#160;&#160;&#160;&#160;</td>

                            <td class="uportal-label">
                                Display
                            </td>

                            <td class="uportal-label">
                                <xsl:variable name="noOfRecordsI"><xsl:value-of select="noOfRecordsI" /></xsl:variable>
                                <!--input type="text" name="noOfRecords" size="4" tabindex="5" value="{$intRecordPerPage}" align="right" class="uportal-input-text" readonly="readonly" /-->
                                <select name="noOfRecordsI" class="uportal-input-text">
                                    <xsl:for-each select="CASEGENIX_intRecordPerPage">
                                        <xsl:variable name="strValue"><xsl:value-of select="."/></xsl:variable>
                                        <option value="{$strValue}">

                                            <xsl:if test="$noOfRecordsI = $strValue">
                                                <xsl:attribute name="selected">1</xsl:attribute>
                                            </xsl:if>
                                            <xsl:value-of select="."/>
                                        </option>
                                    </xsl:for-each>
                                </select>
                            </td>

                            <td class="uportal-label">
                                records at a time
                            </td>

                            <td class="uportal-label">
                                <!--input type="submit" name="set" value="{$setBtnLabel}" tabindex="6" class="uportal-button" /-->
                            </td>

                            <td></td>

                            <td class="uportal-label">
                             </td>
                            <td ></td>


                        </tr>
                    </table>

                <!--/form-->
                </center>
                </xsl:if>
    
    <table width="100%">
        <tr>
            <td colspan="7" height="30px"></td>
        </tr>
        <tr>
            <td colspan="7" class="uportal-channel-title">Outgoing alerts</td>
        </tr>
        <tr>
            <td colspan="7" height="20px"></td>
        </tr>
        <tr>
            <td width="5%"></td>
            <td width="20%" class="uportal-channel-table-header">
                <xsl:value-of select="WORKSPACE_ALERT_strNameDisplay" />
            </td>
            <td width="10%" class="uportal-channel-table-header">
                <xsl:value-of select="WORKSPACE_ALERT_intSendToUserKeyDisplay" />
            </td>
            <td width="10%" class="uportal-channel-table-header">
                <xsl:value-of select="WORKSPACE_ALERT_dtDateDisplay" />
            </td>
            <td width="10%" class="uportal-channel-table-header">
                <xsl:value-of select="WORKSPACE_ALERT_tmTimeDisplay" />
            </td>
            <td width="40%" class="uportal-channel-table-header">
                <xsl:value-of select="WORKSPACE_ALERT_strDescriptionDisplay" />
            </td>
            
            <td width="5%"></td>
        </tr>
        
        <tr>
            <td colspan="7" height="10px"></td>
        </tr>
        
        <xsl:for-each select="search_outgoing_alert">
        <xsl:variable name="WORKSPACE_ALERT_intAlertKey"><xsl:value-of select="WORKSPACE_ALERT_intAlertKey" /></xsl:variable>
        <xsl:variable name="WORKSPACE_ALERT_intFromUserKey"><xsl:value-of select="WORKSPACE_ALERT_intFromUserKey" /></xsl:variable>
        
        <tr>
            <td width="5%"></td>
            <td width="20%" class="uportal-label"> 
                <a href="{$baseActionURL}?current=view_alert&amp;WORKSPACE_ALERT_intAlertKey={$WORKSPACE_ALERT_intAlertKey}&amp;WORKSPACE_ALERT_intFromUserKey={$WORKSPACE_ALERT_intFromUserKey}">
                    <xsl:value-of select="WORKSPACE_ALERT_strName" />
                </a>
            </td>

            <td width="10%" class="uportal-label">
                <a href="{$baseActionURL}?current=view_alert&amp;WORKSPACE_ALERT_intAlertKey={$WORKSPACE_ALERT_intAlertKey}&amp;WORKSPACE_ALERT_intFromUserKey={$WORKSPACE_ALERT_intFromUserKey}">
                    <xsl:value-of select="WORKSPACE_ALERT_strSendToUserName" />
                </a>
            </td>

            <td width="10%" class="uportal-label">
                <a href="{$baseActionURL}?current=view_alert&amp;WORKSPACE_ALERT_intAlertKey={$WORKSPACE_ALERT_intAlertKey}&amp;WORKSPACE_ALERT_intFromUserKey={$WORKSPACE_ALERT_intFromUserKey}">
                    <xsl:value-of select="WORKSPACE_ALERT_dtDate" />
                </a>
            </td>

            <td width="10%" class="uportal-label">
                <a href="{$baseActionURL}?current=view_alert&amp;WORKSPACE_ALERT_intAlertKey={$WORKSPACE_ALERT_intAlertKey}&amp;WORKSPACE_ALERT_intFromUserKey={$WORKSPACE_ALERT_intFromUserKey}">
                    <xsl:value-of select="WORKSPACE_ALERT_tmTime" />
                </a>
            </td>

            <td width="40%" class="uportal-label">
                <a href="{$baseActionURL}?current=view_alert&amp;WORKSPACE_ALERT_intAlertKey={$WORKSPACE_ALERT_intAlertKey}&amp;WORKSPACE_ALERT_intFromUserKey={$WORKSPACE_ALERT_intFromUserKey}">
                    <xsl:value-of select="WORKSPACE_ALERT_strDescription" />
                </a>
            </td>
            <td width="5%" class="uportal-label">
                <a href="javascript:confirmDelete('{$baseActionURL}?current=alert_list&amp;delete=true&amp;WORKSPACE_ALERT_intAlertKey={$WORKSPACE_ALERT_intAlertKey}')">
                    Delete
                </a>
            </td>
        </tr>
        </xsl:for-each>
    </table>
    
    
                <xsl:variable name="intCurrentPage"><xsl:value-of select="currentPage" /></xsl:variable>
                <xsl:variable name="intNoOfPages"><xsl:value-of select="noOfPages" /></xsl:variable>
                <xsl:variable name="intRecordPerPage"><xsl:value-of select="noOfRecords" /></xsl:variable>
                <script language="javascript">

                    function submitNewPage( offset ){

                            document.navigationForm.currentPage.value = parseInt( document.navigationForm.currentPage.value ) + parseInt(offset);

                            document.navigationForm.submit();

                    }
                </script>
                <xsl:if test="count( search_outgoing_alert ) > 0">
                <table width="100%">
                    <tr>
                        <td class="uportal-channel-subtitle">
                            <hr/>
                        </td>
                    </tr>
                </table>
                <center>
                <!--form name="navigationForm" action="{$baseActionURL}?current=view_active_task" method="post"-->

                    <table>

                        <tr>          

                            <td class="uportal-label">
                                Page:
                            </td>

                            <td class="uportal-label">
                                <input type="submit" name="previous" value="{$ltBtnLabel}" tabindex="1" class="uportal-button"  onclick="javascript:submitNewPage('-1')"/>
                            </td>

                            <td class="uportal-label">
                                <input type="text" name="currentPage" size="4" tabindex="2" value="{$intCurrentPage}" align="right" class="uportal-input-text" />
                            </td>

                            <td class="uportal-label">
                                of <xsl:value-of select="noOfPages" />
                            </td>

                            <td class="uportal-label">
                                <input type="button" name="next" value="{$gtBtnLabel}" tabindex="3" class="uportal-button" onclick="javascript:submitNewPage('1')"/>
                            </td>

                            <td class="uportal-label">
                                <input type="submit" name="go" value="{$goBtnLabel}" tabindex="4" class="uportal-button" />
                            </td>

                            <td >&#160;&#160;&#160;&#160;&#160;</td>

                            <td class="uportal-label">
                                Display
                            </td>

                            <td class="uportal-label">
                                <xsl:variable name="noOfRecords"><xsl:value-of select="noOfRecords" /></xsl:variable>
                                <!--input type="text" name="noOfRecords" size="4" tabindex="5" value="{$intRecordPerPage}" align="right" class="uportal-input-text" readonly="readonly" /-->
                                <select name="noOfRecords" class="uportal-input-text">
                                    <xsl:for-each select="CASEGENIX_intRecordPerPage">
                                        <xsl:variable name="strValue"><xsl:value-of select="."/></xsl:variable>
                                        <option value="{$strValue}">

                                            <xsl:if test="$noOfRecords = $strValue">
                                                <xsl:attribute name="selected">1</xsl:attribute>
                                            </xsl:if>
                                            <xsl:value-of select="."/>
                                        </option>
                                    </xsl:for-each>
                                </select>
                            </td>

                            <td class="uportal-label">
                                records at a time
                            </td>

                            <td class="uportal-label">
                                <!--input type="submit" name="set" value="{$setBtnLabel}" tabindex="6" class="uportal-button" /-->
                            </td>

                            <td ></td>

                            <td class="uportal-label">
                             </td>

                            <td ></td>


                        </tr>
                    </table>

                <!--/form-->
                </center>
                </xsl:if>

    </form>
    </xsl:template>

</xsl:stylesheet>
