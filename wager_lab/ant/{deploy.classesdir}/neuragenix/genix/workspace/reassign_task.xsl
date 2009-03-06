<?xml version="1.0" encoding="utf-8"?>

<!-- 
    reassign_task.xsl, part of the Workspace channel
    Author: hhoang@neuragenix.com
    Date: 25/02/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./workspace_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=reassign_task</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="mediaPath">media/org/jasig/portal/layout/tab-column/nested-tables</xsl:variable>
  
    <xsl:template match="workspace">
    
    <xsl:param name="WORKFLOW_TASK_REASSIGN_intToUserKey"><xsl:value-of select="WORKFLOW_TASK_REASSIGN_intToUserKey" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_REASSIGN_strToUserType"><xsl:value-of select="WORKFLOW_TASK_REASSIGN_strToUserType" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_REASSIGN_strToUserName"><xsl:value-of select="WORKFLOW_TASK_REASSIGN_strToUserName" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_strName"><xsl:value-of select="WORKFLOW_TASK_strName" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_REASSIGN_intTaskKey"><xsl:value-of select="WORKFLOW_TASK_REASSIGN_intTaskKey" /></xsl:param>
    <xsl:param name="isHasIncomingAlert"><xsl:value-of select="isHasIncomingAlert" /></xsl:param>
    
    <form action="{$baseActionURL}?{$formParams}" method="post">
 
    <table width="100%">
        <tr>
            <td width="25%"></td>
            <td width="65%" class="uportal-channel-subtitle">

                     <img name="reassign_task" src="{$mediaPath}/casegenix/controls/reasign_task_on.gif" border="0"></img> 
                
                
                <a href="{$baseActionURL}?current=view_active_task" onMouseOver="document.images['view_active_task'].src= '{$mediaPath}/casegenix/controls/view_active_task_on.gif'" onMouseOut="document.images['view_active_task'].src='{$mediaPath}/casegenix/controls/view_active_task_off.gif'"><img name="view_active_task" src="{$mediaPath}/casegenix/controls/view_active_task_off.gif" border="0"></img></a> 
                     <a href="{$baseActionURL}?current=search_task" onMouseOver="document.images['search_task'].src= '{$mediaPath}/casegenix/controls/search_task_on.gif'" onMouseOut="document.images['search_task'].src='{$mediaPath}/casegenix/controls/search_task_off.gif'"><img name="search_task" src="{$mediaPath}/casegenix/controls/search_task_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=reassign_tasks" onMouseOver="document.images['reasign_all_tasks'].src= '{$mediaPath}/casegenix/controls/reasign_all_tasks_on.gif'" onMouseOut="document.images['reasign_all_tasks'].src='{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif'"><img name="reasign_all_tasks" src="{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=create_alert" onMouseOver="document.images['create_alert'].src= '{$mediaPath}/casegenix/controls/create_alert_on.gif'" onMouseOut="document.images['create_alert'].src='{$mediaPath}/casegenix/controls/create_alert_off.gif'"><img name="create_alert" src="{$mediaPath}/casegenix/controls/create_alert_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=alert_list" onMouseOver="document.images['alert_list'].src= '{$mediaPath}/casegenix/controls/alert_list_on.gif'" onMouseOut="document.images['alert_list'].src='{$mediaPath}/casegenix/controls/alert_list_off.gif'"><img name="alert_list" src="{$mediaPath}/casegenix/controls/alert_list_off.gif" border="0"></img></a><xsl:if test="$isHasIncomingAlert = 'true'"><img src="media/neuragenix/icons/alert.gif" border="0" /></xsl:if>

            </td>
            <td width="10%">
                <input type="submit" name="back" tabindex="8" value="{$backBtnLabel}" class="uportal-button" />
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
            <td width="5%" class="neuragenix-form-required-text" align="right"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strNameDisplay" />:
            </td>
            <td width="20%" class="uportal-text">
                <xsl:value-of select="WORKFLOW_TASK_strName" />
            </td>
            <td width="10%" class="neuragenix-form-required-text" align="right">*</td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_REASSIGN_intToUserKeyDisplay" />:
            </td>
            <td width="20%">
                <input type="text" name="WORKFLOW_TASK_REASSIGN_strToUserName" size="25" tabindex="1" value="{$WORKFLOW_TASK_REASSIGN_strToUserName}" readonly="true" class="uportal-input-text" />
                <input type="submit" name="org_chart" tabindex="2" value="Org chart" class="uportal-button" />
            </td>
            <td width="5%"></td>
        </tr>
        
        <tr>
            <td width="5%"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_REASSIGN_intReassignByDisplay" />:
            </td>
            <td width="20%" class="uportal-text">
                <select name="WORKFLOW_TASK_REASSIGN_intReassignBy" tabindex="1" class="uportal-input-text">
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
            <td width="10%" class="neuragenix-form-required-text" align="right">*</td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_REASSIGN_strReasonDisplay" />:
            </td>
            <td width="20%">
                <textarea name="WORKFLOW_TASK_REASSIGN_strReason" rows="4" cols="60" tabindex="11" class="uportal-input-text">
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
                <input type="button" name="clear" tabindex="10" value="{$clearBtnLabel}" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=reassign_task&amp;WORKFLOW_TASK_strName={$WORKFLOW_TASK_strName}&amp;WORKFLOW_TASK_REASSIGN_intTaskKey={$WORKFLOW_TASK_REASSIGN_intTaskKey}')" />
            </td>
        </tr>
    </table>
    
    <input type="hidden" name="WORKFLOW_TASK_strName" value="{$WORKFLOW_TASK_strName}" />
    <input type="hidden" name="WORKFLOW_TASK_REASSIGN_intTaskKey" value="{$WORKFLOW_TASK_REASSIGN_intTaskKey}" />
    <input type="hidden" name="WORKFLOW_TASK_REASSIGN_intToUserKey" value="{$WORKFLOW_TASK_REASSIGN_intToUserKey}" />
    <input type="hidden" name="WORKFLOW_TASK_REASSIGN_strToUserType" value="{$WORKFLOW_TASK_REASSIGN_strToUserType}" />
    </form>
    </xsl:template>

</xsl:stylesheet>
