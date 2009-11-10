<?xml version="1.0" encoding="utf-8"?>

<!-- 
    create_alert.xsl, part of the Workspace channel
    Author: hhoang@neuragenix.com
    Date: 25/02/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./workspace_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=create_alert</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="mediaPath">media/org/jasig/portal/layout/tab-column/nested-tables</xsl:variable>
  
    <xsl:template match="workspace">
    <xsl:param name="WORKSPACE_ALERT_intAlertKey"><xsl:value-of select="WORKSPACE_ALERT_intAlertKey" /></xsl:param>
    <xsl:param name="WORKSPACE_ALERT_strName"><xsl:value-of select="WORKSPACE_ALERT_strName" /></xsl:param>
    <xsl:param name="WORKSPACE_ALERT_intFromUserKey"><xsl:value-of select="WORKSPACE_ALERT_intFromUserKey" /></xsl:param>
    <xsl:param name="WORKSPACE_ALERT_strFromUserType"><xsl:value-of select="WORKSPACE_ALERT_strFromUserType" /></xsl:param>
    <xsl:param name="WORKSPACE_ALERT_intSendToUserKey"><xsl:value-of select="WORKSPACE_ALERT_intSendToUserKey" /></xsl:param>
    <xsl:param name="WORKSPACE_ALERT_strSendToUserType"><xsl:value-of select="WORKSPACE_ALERT_strSendToUserType" /></xsl:param>
    <xsl:param name="WORKSPACE_ALERT_strSendToUserName"><xsl:value-of select="WORKSPACE_ALERT_strSendToUserName" /></xsl:param>
    <xsl:param name="WORKSPACE_ALERT_dtDate_Year"><xsl:value-of select="WORKSPACE_ALERT_dtDate_Year" /></xsl:param>
    <xsl:param name="isHasIncomingAlert"><xsl:value-of select="isHasIncomingAlert" /></xsl:param>
    
    <form action="{$baseActionURL}?{$formParams}" method="post">
 
    <table width="100%">
        <tr>
            <td width="25%"></td>
            <td width="75%" class="uportal-channel-subtitle">
                <a href="{$baseActionURL}?current=view_active_task" onMouseOver="document.images['view_active_task'].src= '{$mediaPath}/casegenix/controls/view_active_task_on.gif'" onMouseOut="document.images['view_active_task'].src='{$mediaPath}/casegenix/controls/view_active_task_off.gif'"><img name="view_active_task" src="{$mediaPath}/casegenix/controls/view_active_task_off.gif" border="0"></img></a> 
                <a href="{$baseActionURL}?current=search_task" onMouseOver="document.images['search_task'].src= '{$mediaPath}/casegenix/controls/search_task_on.gif'" onMouseOut="document.images['search_task'].src='{$mediaPath}/casegenix/controls/search_task_off.gif'"><img name="search_task" src="{$mediaPath}/casegenix/controls/search_task_off.gif" border="0"></img></a> 
                <a href="{$baseActionURL}?current=reassign_tasks" onMouseOver="document.images['reasign_all_tasks'].src= '{$mediaPath}/casegenix/controls/reasign_all_tasks_on.gif'" onMouseOut="document.images['reasign_all_tasks'].src='{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif'"><img name="reasign_all_tasks" src="{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif" border="0"></img></a> 
                
                <img name="create_alert" src="{$mediaPath}/casegenix/controls/create_alert_on.gif" border="0"></img> 

                <a href="{$baseActionURL}?current=alert_list" onMouseOver="document.images['alert_list'].src= '{$mediaPath}/casegenix/controls/alert_list_on.gif'" onMouseOut="document.images['alert_list'].src='{$mediaPath}/casegenix/controls/alert_list_off.gif'"><img name="alert_list" src="{$mediaPath}/casegenix/controls/alert_list_off.gif" border="0"></img></a><xsl:if test="$isHasIncomingAlert = 'true'"><img src="media/neuragenix/icons/alert.gif" border="0" /></xsl:if> 

              
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
                <xsl:value-of select="WORKSPACE_ALERT_strNameDisplay" />:
            </td>
            <td width="20%" class="uportal-text">
                <input type="text" name="WORKSPACE_ALERT_strName" size="25" tabindex="1" value="{$WORKSPACE_ALERT_strName}" class="uportal-input-text" />
            </td>
            <td width="10%" class="neuragenix-form-required-text" align="right">*</td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKSPACE_ALERT_dtDateDisplay" />:
            </td>
            <td width="20%">
                <select name="WORKSPACE_ALERT_dtDate_Day" tabindex="4" class="uportal-input-text">
                    <xsl:for-each select="WORKSPACE_ALERT_dtDate_Day">

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

                <select name="WORKSPACE_ALERT_dtDate_Month" tabindex="5" class="uportal-input-text">
                    <xsl:for-each select="WORKSPACE_ALERT_dtDate_Month">

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

                <input type="text" name="WORKSPACE_ALERT_dtDate_Year" value="{$WORKSPACE_ALERT_dtDate_Year}" size="5" tabindex="6" class="uportal-input-text" />
            </td>
            <td width="5%"></td>
        </tr>
        
        <tr>
            <td width="5%"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKSPACE_ALERT_intFromUserKeyDisplay" />:
            </td>
            <td width="20%" class="uportal-text">
                <select name="WORKSPACE_ALERT_intFromUserKey" tabindex="1" class="uportal-input-text">
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
                <xsl:value-of select="WORKSPACE_ALERT_tmTimeDisplay" />:
            </td>
            <td width="20%">
                <select name="WORKSPACE_ALERT_tmTime_Hour" tabindex="7" class="uportal-input-text">
                    <xsl:for-each select="WORKSPACE_ALERT_tmTime_Hour">

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
                
                <select name="WORKSPACE_ALERT_tmTime_Minute" tabindex="8" class="uportal-input-text">
                    <xsl:for-each select="WORKSPACE_ALERT_tmTime_Minute">

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
                
                <select name="WORKSPACE_ALERT_tmTime_AMPM" tabindex="9" class="uportal-input-text">
                    <xsl:for-each select="WORKSPACE_ALERT_tmTime_AMPM">

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
            </td>
            <td width="5%"></td>
        </tr>
        
        <tr>
            <td width="5%" class="neuragenix-form-required-text" align="right">*</td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKSPACE_ALERT_strSendToUserNameDisplay" />:
            </td>
            <td width="20%" class="uportal-text">
                <input type="text" name="WORKSPACE_ALERT_strSendToUserName" readonly="true" size="25" tabindex="2" value="{$WORKSPACE_ALERT_strSendToUserName}" class="uportal-input-text" />
                <input type="submit" name="org_chart" tabindex="3" value="Org chart" class="uportal-button" />
            </td>
            <td width="10%" class="neuragenix-form-required-text" align="right"></td>
            <td width="15%" class="uportal-label">
                <xsl:value-of select="WORKSPACE_ALERT_strDescriptionDisplay" />:
            </td>
            <td width="20%">
                <textarea name="WORKSPACE_ALERT_strDescription" rows="5" cols="60" tabindex="3" class="uportal-input-text">
                    <xsl:value-of select="WORKSPACE_ALERT_strDescription" />
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
                <input type="submit" name="save" tabindex="11" value="{$saveBtnLabel}" class="uportal-button" />
                <input type="button" name="clear" tabindex="12" value="{$clearBtnLabel}" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=create_alert')" />
            </td>
        </tr>
    </table>
    
    <input type="hidden" name="WORKSPACE_ALERT_intSendToUserKey" value="{$WORKSPACE_ALERT_intSendToUserKey}" />
    <input type="hidden" name="WORKSPACE_ALERT_strSendToUserType" value="{$WORKSPACE_ALERT_strSendToUserType}" />
    <input type="hidden" name="WORKSPACE_ALERT_strFromUserType" value="User" />
    <input type="hidden" name="WORKSPACE_ALERT_strStatus" value="Not sent" />
    <input type="hidden" name="WORKSPACE_ALERT_strType" value="Manual" />
    </form>
    </xsl:template>

</xsl:stylesheet>
