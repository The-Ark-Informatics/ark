<?xml version="1.0" encoding="utf-8"?>

<!-- 
    view_task.xsl, part of the Workspace channel
    Author: hhoang@neuragenix.com
    Date: 25/02/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./workspace_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=view_task</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:param name="caseChannelURL">caseChannelURL_false</xsl:param>
    <xsl:param name="caseChannelTabOrder">caseChannelTabOrder</xsl:param>
    <xsl:param name="smartformChannelURL">smartformChannelURL_false</xsl:param>
    <xsl:param name="smartformChannelTabOrder">smartformChannelTabOrder</xsl:param>
    <xsl:variable name="mediaPath">media/org/jasig/portal/layout/tab-column/nested-tables</xsl:variable>
  
    <xsl:template match="workspace">
    <xsl:param name="WORKFLOW_TASK_intTaskKey"><xsl:value-of select="WORKFLOW_TASK_intTaskKey" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_intWorkflowInstanceKey"><xsl:value-of select="WORKFLOW_TASK_intWorkflowInstanceKey" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_strName"><xsl:value-of select="WORKFLOW_TASK_strName" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_strSignoff"><xsl:value-of select="WORKFLOW_TASK_strSignoff" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_strAction"><xsl:value-of select="WORKFLOW_TASK_strAction" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_strStatus"><xsl:value-of select="WORKFLOW_TASK_strStatus_Selected" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_dtDateReceived_Year"><xsl:value-of select="WORKFLOW_TASK_dtDateReceived_Year" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_dtDateCompleted_Year"><xsl:value-of select="WORKFLOW_TASK_dtDateCompleted_Year" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_strMultiAction"><xsl:value-of select="WORKFLOW_TASK_strMultiAction" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_strInstruction"><xsl:value-of select="WORKFLOW_TASK_strInstruction" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_strReassignable"><xsl:value-of select="WORKFLOW_TASK_strReassignable" /></xsl:param>
    <xsl:param name="source"><xsl:value-of select="source" /></xsl:param>
    <xsl:param name="hasAction"><xsl:value-of select="hasAction" /></xsl:param>
    <xsl:param name="multi_user"><xsl:value-of select="multi_user" /></xsl:param>
    <xsl:param name="isHasIncomingAlert"><xsl:value-of select="isHasIncomingAlert" /></xsl:param>
    
    <form action="{$baseActionURL}?{$formParams}" method="post">
 
    <table width="100%">
        <tr>
            <td width="25%"></td>
            <td width="65%" class="uportal-channel-subtitle">
                
                    <img name="view_task" src="{$mediaPath}/casegenix/controls/view_task_on.gif" border="0"></img> 
                      
                     <a href="{$baseActionURL}?current=view_active_task" onMouseOver="document.images['view_active_task'].src= '{$mediaPath}/casegenix/controls/view_active_task_on.gif'" onMouseOut="document.images['view_active_task'].src='{$mediaPath}/casegenix/controls/view_active_task_off.gif'"><img name="view_active_task" src="{$mediaPath}/casegenix/controls/view_active_task_off.gif" border="0"></img></a> 
                     <a href="{$baseActionURL}?current=search_task" onMouseOver="document.images['search_task'].src= '{$mediaPath}/casegenix/controls/search_task_on.gif'" onMouseOut="document.images['search_task'].src='{$mediaPath}/casegenix/controls/search_task_off.gif'"><img name="search_task" src="{$mediaPath}/casegenix/controls/search_task_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=reassign_tasks" onMouseOver="document.images['reasign_all_tasks'].src= '{$mediaPath}/casegenix/controls/reasign_all_tasks_on.gif'" onMouseOut="document.images['reasign_all_tasks'].src='{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif'"><img name="reasign_all_tasks" src="{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=create_alert" onMouseOver="document.images['create_alert'].src= '{$mediaPath}/casegenix/controls/create_alert_on.gif'" onMouseOut="document.images['create_alert'].src='{$mediaPath}/casegenix/controls/create_alert_off.gif'"><img name="create_alert" src="{$mediaPath}/casegenix/controls/create_alert_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=alert_list" onMouseOver="document.images['alert_list'].src= '{$mediaPath}/casegenix/controls/alert_list_on.gif'" onMouseOut="document.images['alert_list'].src='{$mediaPath}/casegenix/controls/alert_list_off.gif'"><img name="alert_list" src="{$mediaPath}/casegenix/controls/alert_list_off.gif" border="0"></img></a> <xsl:if test="$isHasIncomingAlert = 'true'"><img src="media/neuragenix/icons/alert.gif" border="0" /></xsl:if>

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
            <td width="20%" class="neuragenix-form-required-text" align="right"></td>
        </tr>
        
    </table>
    
    <table width="100%">
        <tr>
            <td width="5%"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strNameDisplay" />:
            </td>
            <td width="20%" class="uportal-text">
                <xsl:value-of select="WORKFLOW_TASK_strName" />
            </td>
            <td width="10%"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_dtDateReceivedDisplay" />:
            </td>
            <td width="20%" class="uportal-text">
                <xsl:value-of select="WORKFLOW_TASK_dtDateReceived" />
            </td>
            <td width="5%"></td>
        </tr>
        
        <tr>
            <td width="5%"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strPriorityDisplay" />:
            </td>
            <td width="20%" class="uportal-text">
                <xsl:value-of select="WORKFLOW_TASK_strPriority" />
            </td>
            <td width="10%"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_dtDateCompletedDisplay" />:
            </td>
            <td width="20%" class="uportal-text">
                <xsl:value-of select="WORKFLOW_TASK_dtDateCompleted" />
            </td>
            <td width="5%"></td>
        </tr>
        
        <tr>
            <td width="5%"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strStatusDisplay" />:
            </td>
            <td width="20%" class="uportal-text">
                <xsl:value-of select="WORKFLOW_TASK_strStatus_Selected" />
            </td>
            <td width="10%"></td>
            <td width="15%" class="uportal-label">
                
            </td>
            <td width="20%">
                
            </td>
            <td width="5%"></td>
        </tr>
        
        <tr>
            <td width="5%"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strInstructionDisplay" />:
            </td>
            <td colspan="4" class="uportal-text">
            
                <xsl:for-each select="data">
                    <xsl:variable name="type"><xsl:value-of select="type" /></xsl:variable>
                    
                    <xsl:choose>
                    <xsl:when test="$type = 'text'">
                        <xsl:value-of select="content" />
                    </xsl:when>
                    
                    <xsl:otherwise>
                        <xsl:variable name="domain"><xsl:value-of select="domain" /></xsl:variable>
                        <xsl:variable name="param"><xsl:value-of select="param" /></xsl:variable>
                        
                        <xsl:choose>
                        <xsl:when test="$domain = 'CASE'">
                            <a href="{$caseChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$caseChannelTabOrder}{$param}" class="uportal-label">
                                <xsl:value-of select="content" />
                            </a>
                        </xsl:when>
                        
                        <xsl:when test="$domain = 'SMARTFORM'">
                            <a href="{$smartformChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformChannelTabOrder}{$param}" class="uportal-label">
                                <xsl:value-of select="content" />
                            </a>
                        </xsl:when>
                         
                        </xsl:choose>
                    </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
                
            </td>
            <td width="5%"></td>
        </tr>
        
        <tr>
            <td width="5%"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strCommentsDisplay" />:
            </td>
            <td colspan="4">
                <textarea name="WORKFLOW_TASK_strComments" rows="4" cols="120" tabindex="3" class="uportal-input-text">
                    <xsl:value-of select="WORKFLOW_TASK_strComments" />
                </textarea>
            </td>
            <td width="5%"></td>
        </tr>
        
        <xsl:if test="$hasAction='true'">
        <tr>
            <td width="5%"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strActionDisplay" />:
            </td>
            <td colspan="4">
                <xsl:choose>
                <xsl:when test="$WORKFLOW_TASK_strMultiAction = 'Yes'">
                <select name="WORKFLOW_TASK_strAction" multiple="true" size="4" tabindex="1" class="uportal-input-text">
                    <xsl:for-each select="WORKFLOW_TASK_strAction">
                        <option>
                            <xsl:attribute name="value">
                            <xsl:value-of select="." />
                            </xsl:attribute> 
                            <xsl:if test="@selected=1">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                            </xsl:if>

                            <xsl:value-of select="." />
                        </option>
                    </xsl:for-each>
                </select>
                </xsl:when>
                
                <xsl:otherwise>
                <select name="WORKFLOW_TASK_strAction" tabindex="1" class="uportal-input-text">
                    <xsl:for-each select="WORKFLOW_TASK_strAction">
                        <option>
                            <xsl:attribute name="value">
                            <xsl:value-of select="." />
                            </xsl:attribute> 
                            <xsl:if test="@selected=1">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                            </xsl:if>

                            <xsl:value-of select="." />
                        </option>
                    </xsl:for-each>
                </select>    
                </xsl:otherwise>
                </xsl:choose>
            </td>
            <td width="5%"></td>
        </tr>
        </xsl:if>
        
        <xsl:if test="$WORKFLOW_TASK_strSignoff='Yes'">
        <tr>
            <td width="5%"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strSignoffDisplay" />:
            </td>
            <td colspan="4">
                <input type="password" name="signoff_pw" size="25" tabindex="1" class="uportal-input-text" />
            </td>
            <td width="5%"></td>
        </tr>
        </xsl:if>
        
        <xsl:if test="$multi_user='true'">
        <tr>
            <td width="5%"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_intPerformerDisplay" />:
            </td>
            <td colspan="4">
                
                <select name="WORKFLOW_TASK_intPerformer" tabindex="1" class="uportal-input-text">
                    <xsl:for-each select="OrgUser">
                        <option>
                            <xsl:attribute name="value">
                            <xsl:value-of select="WORKFLOW_TASK_intPerformer" />
                            </xsl:attribute> 
                            
                            <xsl:value-of select="WORKFLOW_TASK_strPerformer" />
                        </option>
                    </xsl:for-each>
                </select>
            </td>
            <td width="5%"></td>
        </tr>
        </xsl:if>
        
    </table>
    
    <table width="100%">
        <tr><td><hr /></td></tr>
    </table>

    <table width="100%">
        <tr>
            <td width="25%"></td>
            <td width="75%">
                <input type="submit" name="save" tabindex="4" value="{$saveBtnLabel}" class="uportal-button" />
                <input type="submit" name="done" tabindex="5" value="Done" class="uportal-button" />
                <input type="button" name="view_status" tabindex="6" value="View status" class="uportal-button" onclick="javascript:jumpTo('{$baseActionURL}?current=view_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}&amp;WORKFLOW_TASK_intWorkflowInstanceKey={$WORKFLOW_TASK_intWorkflowInstanceKey}&amp;view_status=true')" />
                <xsl:if test="$WORKFLOW_TASK_strReassignable = 'Yes'">
                <input type="submit" name="reassign" tabindex="7" value="Reassign task" class="uportal-button" />
                </xsl:if>
            </td>
        </tr>
    </table>
    
     <!-- hidden fields to send data to server -->
    <input type="hidden" name="WORKFLOW_TASK_intTaskKey" value="{$WORKFLOW_TASK_intTaskKey}" />
    <input type="hidden" name="WORKFLOW_TASK_strName" value="{$WORKFLOW_TASK_strName}" />
    <input type="hidden" name="WORKFLOW_TASK_strStatus" value="{$WORKFLOW_TASK_strStatus}" />
    <input type="hidden" name="WORKFLOW_TASK_strSignoff" value="{$WORKFLOW_TASK_strSignoff}" />
    <input type="hidden" name="WORKFLOW_TASK_strPerformerType" value="User" />
    <input type="hidden" name="source" value="{$source}" />
    <input type="hidden" name="hasAction" value="{$hasAction}" />
    
    <xsl:if test="$multi_user='false'">
        <xsl:variable name="WORKFLOW_TASK_intPerformer"><xsl:value-of select="OrgUser/WORKFLOW_TASK_intPerformer" /></xsl:variable>
        <input type="hidden" name="WORKFLOW_TASK_intPerformer" value="{$WORKFLOW_TASK_intPerformer}" />
        
    </xsl:if>
    </form>
    </xsl:template>

</xsl:stylesheet>
