<?xml version="1.0" encoding="utf-8"?>

<!-- 
    reassign_tasks.xsl, part of the Workspace channel
    Author: hhoang@neuragenix.com
    Date: 25/02/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./workspace_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=reassign_tasks</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="mediaPath">media/org/jasig/portal/layout/tab-column/nested-tables</xsl:variable>
  
    <xsl:template match="workspace">
    <xsl:param name="WORKFLOW_TASK_REASSIGN_intFromUserKey"><xsl:value-of select="WORKFLOW_TASK_REASSIGN_intFromUserKey" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_REASSIGN_strFromUserType"><xsl:value-of select="WORKFLOW_TASK_REASSIGN_strFromUserType" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_REASSIGN_intToUserKey"><xsl:value-of select="WORKFLOW_TASK_REASSIGN_intToUserKey" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_REASSIGN_strToUserType"><xsl:value-of select="WORKFLOW_TASK_REASSIGN_strToUserType" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_REASSIGN_strFromUserName"><xsl:value-of select="WORKFLOW_TASK_REASSIGN_strFromUserName" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_REASSIGN_strToUserName"><xsl:value-of select="WORKFLOW_TASK_REASSIGN_strToUserName" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_REASSIGN_dtFromDate_Year"><xsl:value-of select="WORKFLOW_TASK_REASSIGN_dtFromDate_Year" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_REASSIGN_dtToDate_Year"><xsl:value-of select="WORKFLOW_TASK_REASSIGN_dtToDate_Year" /></xsl:param>
    <xsl:param name="isHasIncomingAlert"><xsl:value-of select="isHasIncomingAlert" /></xsl:param>
    
    <form action="{$baseActionURL}?{$formParams}" method="post">
 
    <table width="100%">
        <tr>
            <td width="25%"></td>
            <td width="75%" class="uportal-channel-subtitle">
                 
                     <a href="{$baseActionURL}?current=view_active_task" onMouseOver="document.images['view_active_task'].src= '{$mediaPath}/casegenix/controls/view_active_task_on.gif'" onMouseOut="document.images['view_active_task'].src='{$mediaPath}/casegenix/controls/view_active_task_off.gif'"><img name="view_active_task" src="{$mediaPath}/casegenix/controls/view_active_task_off.gif" border="0"></img></a> 
                     
                     <a href="{$baseActionURL}?current=search_task" onMouseOver="document.images['search_task'].src= '{$mediaPath}/casegenix/controls/search_task_on.gif'" onMouseOut="document.images['search_task'].src='{$mediaPath}/casegenix/controls/search_task_off.gif'"><img name="search_task" src="{$mediaPath}/casegenix/controls/search_task_off.gif" border="0"></img></a> 
                    <img name="reassign_tasks" src="{$mediaPath}/casegenix/controls/reasign_all_tasks_on.gif" border="0"></img> 
                 
                     
                    <a href="{$baseActionURL}?current=create_alert" onMouseOver="document.images['create_alert'].src= '{$mediaPath}/casegenix/controls/create_alert_on.gif'" onMouseOut="document.images['create_alert'].src='{$mediaPath}/casegenix/controls/create_alert_off.gif'"><img name="create_alert" src="{$mediaPath}/casegenix/controls/create_alert_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=alert_list" onMouseOver="document.images['alert_list'].src= '{$mediaPath}/casegenix/controls/alert_list_on.gif'" onMouseOut="document.images['alert_list'].src='{$mediaPath}/casegenix/controls/alert_list_off.gif'"><img name="alert_list" src="{$mediaPath}/casegenix/controls/alert_list_off.gif" border="0"></img></a> <xsl:if test="$isHasIncomingAlert = 'true'"><img src="media/neuragenix/icons/alert.gif" border="0" /></xsl:if>

            
            
               
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
            <td width="20%" class="neuragenix-form-required-text" align="right">* = Required fields</td>
        </tr>
        
    </table>
    
    <table width="100%">
        <tr>
            <td width="5%" class="neuragenix-form-required-text" align="right">*</td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_REASSIGN_intFromUserKeyDisplay" />:
            </td>
            <td width="20%" class="uportal-text">
                <input type="text" name="WORKFLOW_TASK_REASSIGN_strFromUserName" readonly="true" size="25" tabindex="1" value="{$WORKFLOW_TASK_REASSIGN_strFromUserName}" class="uportal-input-text" />
                <input type="submit" name="org_chart_from" tabindex="2" value="Org chart" class="uportal-button" />
            </td>
            <td width="10%" class="neuragenix-form-required-text" align="right">*</td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_REASSIGN_intReassignByDisplay" />:
            </td>
            <td width="20%">
                <select name="WORKFLOW_TASK_REASSIGN_intReassignBy" tabindex="3" class="uportal-input-text">
                    <xsl:for-each select="OrgUser">
                        <option>
                            <xsl:if test="@selected=1">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                            </xsl:if>
                            <xsl:attribute name="value">
                            <xsl:value-of select="ORGUSER_intOrgUserKey" />
                            </xsl:attribute> 
                            
                            <xsl:value-of select="ORGUSER_strOrgUserName" />
                        </option>
                    </xsl:for-each>
                </select>
            </td>
            <td width="5%"></td>
        </tr>
        
        <tr>
            <td width="5%" class="neuragenix-form-required-text" align="right">*</td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_REASSIGN_intToUserKeyDisplay" />:
            </td>
            <td width="20%" class="uportal-text">
                <input type="text" name="WORKFLOW_TASK_REASSIGN_strToUserName" readonly="true" size="25" tabindex="2" value="{$WORKFLOW_TASK_REASSIGN_strToUserName}" class="uportal-input-text" />
                <input type="submit" name="org_chart_to" tabindex="4" value="Org chart" class="uportal-button" />
            </td>
            <td width="10%" class="neuragenix-form-required-text" align="right">*</td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_REASSIGN_strReasonDisplay" />:
            </td>
            <td width="20%">
                <textarea name="WORKFLOW_TASK_REASSIGN_strReason" rows="4" cols="60" tabindex="4" class="uportal-input-text">
                    <xsl:value-of select="WORKFLOW_TASK_REASSIGN_strReason" />
                </textarea>
            </td>
            <td width="5%"></td>
        </tr>
        
    </table>
    
    <table width="100%">
        <tr><td><hr /></td></tr>
    </table>

    <table width="100%">
        <tr>
            <td width="25%"></td>
            <td width="75%">
                <input type="submit" name="save" tabindex="10" value="{$saveBtnLabel}" class="uportal-button" />
                <input type="button" name="clear" tabindex="10" value="{$clearBtnLabel}" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=reassign_tasks')" />
            </td>
        </tr>
    </table>
    
    <table width="100%">
        <tr>
            <td height="20px" colspan="5"></td>
        </tr>
        <tr>
            <td colspan="5">Reassigns</td>
        </tr>
        <tr>
            <td colspan="5"><hr /></td>
        </tr>
        <tr>
            <td height="10px" colspan="5"></td>
        </tr>
        <tr>
            <td width="5%"></td>
            <td width="20%" class="uportal-channel-table-header">
                From performer
            </td>
            <td width="20%" class="uportal-channel-table-header">
                To performer
            </td>
            <td width="40%" class="uportal-channel-table-header">
                Reason
            </td>
            <td width="15%" class="uportal-channel-table-header">
               
            </td>
        </tr>
        
        <tr>
            <td height="10px" colspan="5"></td>
        </tr>
        
        <xsl:for-each select="search_reassign_tasks">
        <xsl:variable name="WORKFLOW_TASK_REASSIGN_intTaskReassignKey"><xsl:value-of select="WORKFLOW_TASK_REASSIGN_intTaskReassignKey" /></xsl:variable>
        <tr>
            <td width="5%"></td>
            <td width="20%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_REASSIGN_strFromUserName" />
            </td>
            <td width="20%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_REASSIGN_strToUserName" />
            </td>
            <td width="40%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_REASSIGN_strReason" />
            </td>
            <td width="15%" class="uportal-label">
                <a href="javascript:confirmDelete('{$baseActionURL}?current=reassign_tasks&amp;delete=true&amp;WORKFLOW_TASK_REASSIGN_intTaskReassignKey={$WORKFLOW_TASK_REASSIGN_intTaskReassignKey}')">
                    Delete
                </a>
            </td>
        </tr>
        </xsl:for-each>
    </table>
    
    <input type="hidden" name="WORKFLOW_TASK_REASSIGN_intFromUserKey" value="{$WORKFLOW_TASK_REASSIGN_intFromUserKey}" />
    <input type="hidden" name="WORKFLOW_TASK_REASSIGN_strFromUserType" value="{$WORKFLOW_TASK_REASSIGN_strFromUserType}" />
    <input type="hidden" name="WORKFLOW_TASK_REASSIGN_intToUserKey" value="{$WORKFLOW_TASK_REASSIGN_intToUserKey}" />
    <input type="hidden" name="WORKFLOW_TASK_REASSIGN_strToUserType" value="{$WORKFLOW_TASK_REASSIGN_strToUserType}" />
    
    </form>
    </xsl:template>

</xsl:stylesheet>
