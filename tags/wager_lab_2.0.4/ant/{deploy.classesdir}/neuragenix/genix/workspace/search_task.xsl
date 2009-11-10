<?xml version="1.0" encoding="utf-8"?>
<!-- 
    search_task.xsl, part of the Workspace channel
    Author: hhoang@neuragenix.com
    Date: 25/02/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./workspace_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=search_task</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="mediaPath">media/org/jasig/portal/layout/tab-column/nested-tables</xsl:variable>
  
    <xsl:template match="workspace">
    <xsl:param name="WORKFLOW_TASK_dtDateReceived_Year"><xsl:value-of select="WORKFLOW_TASK_dtDateReceived_Year" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_dtDateCompleted_Year"><xsl:value-of select="WORKFLOW_TASK_dtDateCompleted_Year" /></xsl:param>
    <xsl:param name="operator1"><xsl:value-of select="operator1" /></xsl:param>
    <xsl:param name="operator2"><xsl:value-of select="operator2" /></xsl:param>
    <xsl:param name="isHasIncomingAlert"><xsl:value-of select="isHasIncomingAlert" /></xsl:param>
    
    <form name="navigationForm" action="{$baseActionURL}?{$formParams}" method="post">
 
    <table width="100%">
        <tr>
            <td width="25%"></td>
            <td width="75%" class="uportal-label">
                   
                     <a href="{$baseActionURL}?current=view_active_task" onMouseOver="document.images['view_active_task'].src= '{$mediaPath}/casegenix/controls/view_active_task_on.gif'" onMouseOut="document.images['view_active_task'].src='{$mediaPath}/casegenix/controls/view_active_task_off.gif'"><img name="view_active_task" src="{$mediaPath}/casegenix/controls/view_active_task_off.gif" border="0"></img></a> 
                     <img name="search_task" src="{$mediaPath}/casegenix/controls/search_task_on.gif" border="0"></img> 
                   
                
                    <a href="{$baseActionURL}?current=reassign_tasks" onMouseOver="document.images['reasign_all_tasks'].src= '{$mediaPath}/casegenix/controls/reasign_all_tasks_on.gif'" onMouseOut="document.images['reasign_all_tasks'].src='{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif'"><img name="reasign_all_tasks" src="{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=create_alert" onMouseOver="document.images['create_alert'].src= '{$mediaPath}/casegenix/controls/create_alert_on.gif'" onMouseOut="document.images['create_alert'].src='{$mediaPath}/casegenix/controls/create_alert_off.gif'"><img name="create_alert" src="{$mediaPath}/casegenix/controls/create_alert_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=alert_list" onMouseOver="document.images['alert_list'].src= '{$mediaPath}/casegenix/controls/alert_list_on.gif'" onMouseOut="document.images['alert_list'].src='{$mediaPath}/casegenix/controls/alert_list_off.gif'"><img name="alert_list" src="{$mediaPath}/casegenix/controls/alert_list_off.gif" border="0"></img></a> <xsl:if test="$isHasIncomingAlert = 'true'"><img src="media/neuragenix/icons/alert.gif" border="0" /></xsl:if>

                
            </td>
        </tr>
        
    </table>
    <hr/>
    
    <table width="100%">
        <tr valign="top">
            <td width="25%">

                <table width="100%">
                    <tr>
                        <td colspan="2" class="neuragenix-form-required-text">
                            <xsl:value-of select="strErrorMessage" />
                        </td>
                    </tr>
                    
                    <tr>
                        <td width="40%" class="uportal-label">
                            <xsl:value-of select="WORKFLOW_TASK_strNameDisplay" />:
                        </td>
                        <td width="60%">
                            <input type="text" name="WORKFLOW_TASK_strName" size="25" tabindex="1" class="uportal-input-text" />
                        </td>
                    </tr>
                    
                    <tr>
                        <td width="40%" class="uportal-label">
                            <xsl:value-of select="WORKFLOW_TASK_strPriorityDisplay" />:
                        </td>
                        <td width="60%">
                            <select name="WORKFLOW_TASK_strPriority" tabindex="2" class="uportal-input-text">
                                <option value="" />
				<xsl:for-each select="WORKFLOW_TASK_strPriority">
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
                    </tr>
                    
                    <tr>
                        <td width="40%" class="uportal-label">
                            <xsl:value-of select="WORKFLOW_TASK_strStatusDisplay" />:
                        </td>
                        <td width="60%">
                            <select name="WORKFLOW_TASK_strStatus" tabindex="3" class="uportal-input-text">
                                <option value="" />
				<xsl:for-each select="WORKFLOW_TASK_strStatus">
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
                    </tr>
                    
                    <tr>
                        <td width="40%" class="uportal-label">
                            <xsl:value-of select="WORKFLOW_TASK_dtDateReceivedDisplay" />:
                        </td>
                        <td width="60%">
                            <select name="WORKFLOW_TASK_dtDateReceived_Operator" tabindex="4" class="uportal-input-text">
                                <option></option>
				<option>=</option>
                                <option>&lt;&gt;</option>
                                <option>&lt;</option>
                                <option>&lt;=</option>
                                <option>&gt;</option>
                                <option>&gt;=</option>
                            </select>
                            <select name="WORKFLOW_TASK_dtDateReceived_Day" tabindex="5" class="uportal-input-text">
                                <option></option>
				<xsl:for-each select="WORKFLOW_TASK_dtDateReceived_Day">
                                    
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
                            
                            <select name="WORKFLOW_TASK_dtDateReceived_Month" tabindex="6" class="uportal-input-text">
                                <option></option>
				<xsl:for-each select="WORKFLOW_TASK_dtDateReceived_Month">
                                    
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
                            
                            <input type="text" name="WORKFLOW_TASK_dtDateReceived_Year" value="{$WORKFLOW_TASK_dtDateReceived_Year}" size="5" tabindex="7" class="uportal-input-text" />
                        </td>
                    </tr>
                    
                    <tr>
                        <td width="40%" class="uportal-label">
                            <xsl:value-of select="WORKFLOW_TASK_dtDateCompletedDisplay" />:
                        </td>
                        <td width="60%">
                        
                            <select name="WORKFLOW_TASK_dtDateCompleted_Operator" tabindex="8" class="uportal-input-text">
                                <option></option>
				<option>=</option>
                                <option>&lt;&gt;</option>
                                <option>&lt;</option>
                                <option>&lt;=</option>
                                <option>&gt;</option>
                                <option>&gt;=</option>
                            </select>
                            
                            <select name="WORKFLOW_TASK_dtDateCompleted_Day" tabindex="9" class="uportal-input-text">
                                <option></option>
				<xsl:for-each select="WORKFLOW_TASK_dtDateCompleted_Day">
                                    
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
                            
                            <select name="WORKFLOW_TASK_dtDateCompleted_Month" tabindex="10" class="uportal-input-text">
                                <option></option>
				<xsl:for-each select="WORKFLOW_TASK_dtDateCompleted_Month">
                                    
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
                            
                            <input type="text" name="WORKFLOW_TASK_dtDateCompleted_Year" value="{$WORKFLOW_TASK_dtDateCompleted_Year}" size="5" tabindex="11" class="uportal-input-text" />
                        </td>
                    </tr>
                </table>
                
                <table width="90%">
                    <tr><td><hr /></td></tr>
                </table>
                
                <table width="100%">
                    <tr>
                        <td>
                            <input type="submit" name="search" tabindex="12" value="{$submitBtnLabel}" class="uportal-button" />
                            <input type="hidden" name="naviSearch" value="search" />
                            <input type="button" name="clear" value="{$clearBtnLabel}" tabindex="13" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=search_task')" />
                        </td>
                    </tr>
                </table>
            </td>
            
            <td width="75%">
               <xsl:variable name="task_order"><xsl:value-of select="task_order" /></xsl:variable>
                <xsl:variable name="task_sort"><xsl:value-of select="task_sort" /></xsl:variable>  
                <table width="100%">
                    <tr>
                    
                        <td width="20%" class="uportal-channel-table-header">
                        <!--a href="{$baseActionURL}?{$formParams}&amp;sort=true&amp;type=Task&amp;order={$task_order}"-->
                        <a href="{$baseActionURL}?{$formParams}&amp;naviSearch=true&amp;orderBy=WORKFLOW_TASK_strName">
                            <xsl:value-of select="WORKFLOW_TASK_strNameDisplay" />
                        </a>
                        </td>
                        
                        <td width="10%" class="uportal-channel-table-header">
                        <xsl:variable name="priority_order"><xsl:value-of select="priority_order" /></xsl:variable>
                        <a href="{$baseActionURL}?{$formParams}&amp;naviSearch=true&amp;orderBy=WORKFLOW_TASK_strPriority">
                            <xsl:value-of select="WORKFLOW_TASK_strPriorityDisplay" />
                        </a>
                        </td>
                        
                        <td width="15%" class="uportal-channel-table-header">
                        <xsl:variable name="DateTime_order"><xsl:value-of select="DateTime_order" /></xsl:variable>
                        <a href="{$baseActionURL}?{$formParams}&amp;naviSearch=true&amp;orderBy=WORKFLOW_TASK_dtDateReceived">
                            <xsl:value-of select="WORKFLOW_TASK_dtDateReceivedDisplay" />
                        </a>
                        </td>
                        
                        <td width="15%" class="uportal-channel-table-header">
                        <xsl:variable name="DateCompleted_order"><xsl:value-of select="DateCompleted_order" /></xsl:variable>
                        <a href="{$baseActionURL}?{$formParams}&amp;naviSearch=true&amp;orderBy=WORKFLOW_TASK_dtDateCompleted">
                            <xsl:value-of select="WORKFLOW_TASK_dtDateCompletedDisplay" />
                        </a>
                        </td>
                        
                        <td width="10%" class="uportal-channel-table-header">
                        <xsl:variable name="status_order"><xsl:value-of select="status_order" /></xsl:variable>
                        <a href="{$baseActionURL}?{$formParams}&amp;naviSearch=true&amp;orderBy=WORKFLOW_TASK_strStatus">
                            <xsl:value-of select="WORKFLOW_TASK_strStatusDisplay" />
                        </a>
                        </td>
                    </tr>
                </table>
                
                    
                <table width="100%">
                    <xsl:for-each select="search_task">
                    
                    <xsl:variable name="WORKFLOW_TASK_intTaskKey"><xsl:value-of select="WORKFLOW_TASK_intTaskKey" /></xsl:variable>
                    <xsl:variable name="WORKFLOW_TASK_strPriority"><xsl:value-of select="WORKFLOW_TASK_strPriority" /></xsl:variable>
                    <tr>
                        <xsl:choose>
                        <!--xsl:when test="$lineType = 'odd'"-->
                        <xsl:when test="position() mod 2 != 0">
                        <td width="20%" class="uportal-background-light"> 
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                <xsl:value-of select="WORKFLOW_TASK_strName" />
                            </a>
                        </td>
                        
                        <td width="10%" class="uportal-background-light">
                        <xsl:choose>
                        <xsl:when test="$WORKFLOW_TASK_strPriority = 1">
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                Low
                            </a>
                        </xsl:when>
                        
                        <xsl:when test="$WORKFLOW_TASK_strPriority = 2">
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                Medium
                            </a>
                        </xsl:when>
                        
                        <xsl:when test="$WORKFLOW_TASK_strPriority = 3">
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                High
                            </a>
                        </xsl:when>
                        </xsl:choose>    
                        </td>
                        
                        <td width="15%" class="uportal-background-light">
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                <xsl:value-of select="WORKFLOW_TASK_dtDateReceived" />
                            </a>
                        </td>
                        
                        <td width="15%" class="uportal-background-light">
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                <xsl:value-of select="WORKFLOW_TASK_dtDateCompleted" />
                            </a>
                        </td>
                        
                        <td width="10%" class="uportal-background-light">
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                <xsl:value-of select="WORKFLOW_TASK_strStatus" />
                            </a>
                        </td>
                        </xsl:when>
                        
                        <xsl:otherwise>
                        <td width="20%" class="neuragenix-form-row-input"> 
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                <xsl:value-of select="WORKFLOW_TASK_strName" />
                            </a>
                        </td>
                        
                        <td width="10%" class="neuragenix-form-row-input">
                        <xsl:choose>
                        <xsl:when test="$WORKFLOW_TASK_strPriority = 1">
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                Low
                            </a>
                        </xsl:when>
                        
                        <xsl:when test="$WORKFLOW_TASK_strPriority = 2">
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                Medium
                            </a>
                        </xsl:when>
                        
                        <xsl:when test="$WORKFLOW_TASK_strPriority = 3">
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                High
                            </a>
                        </xsl:when>
                        </xsl:choose>    
                        </td>
                        
                        <td width="15%" class="neuragenix-form-row-input">
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                <xsl:value-of select="WORKFLOW_TASK_dtDateReceived" />
                            </a>
                        </td>
                        
                        <td width="15%" class="neuragenix-form-row-input">
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                <xsl:value-of select="WORKFLOW_TASK_dtDateCompleted" />
                            </a>
                        </td>
                        
                        <td width="10%" class="neuragenix-form-row-input">
                            <a href="{$baseActionURL}?current=view_task&amp;source=search_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                                <xsl:value-of select="WORKFLOW_TASK_strStatus" />
                            </a>
                        </td>
                        </xsl:otherwise>
                        </xsl:choose>
                    </tr>
                    </xsl:for-each>
                
                </table>
                
                <!-- PAGING -->
                
                <xsl:variable name="intCurrentPage"><xsl:value-of select="currentPage" /></xsl:variable>
                <xsl:variable name="intNoOfPages"><xsl:value-of select="noOfPages" /></xsl:variable>
                <xsl:variable name="intRecordPerPage"><xsl:value-of select="noOfRecords" /></xsl:variable>
                <script language="javascript">

                function submitNewPage( offset ){

                        document.navigationForm.currentPage.value = parseInt( document.navigationForm.currentPage.value ) + parseInt(offset);

                        document.navigationForm.submit();

                }
                </script>
                <xsl:if test="count( search_task ) > 0">
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

                            <td width="5%" class="uportal-label">
                                Page:
                            </td>

                            <td width="3%" class="uportal-label">
                                <input type="submit" name="previous" value="{$ltBtnLabel}" tabindex="1" class="uportal-button"  onclick="javascript:submitNewPage('-1')"/>
                            </td>

                            <td width="5%" class="uportal-label">
                                <input type="text" name="currentPage" size="4" tabindex="2" value="{$intCurrentPage}" align="right" class="uportal-input-text" />
                            </td>

                            <td width="8%" class="uportal-label">
                                of <xsl:value-of select="noOfPages" />
                            </td>

                            <td width="3%" class="uportal-label">
                                <input type="button" name="next" value="{$gtBtnLabel}" tabindex="3" class="uportal-button" onclick="javascript:submitNewPage('1')"/>
                            </td>

                            <td width="4%" class="uportal-label">
                                <input type="submit" name="go" value="{$goBtnLabel}" tabindex="4" class="uportal-button" />
                            </td>

                            <td width="5%"></td>

                            <td width="7%" class="uportal-label">
                                Display
                            </td>

                            <td width="5%" class="uportal-label">
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

                            <td width="25%" class="uportal-label">
                                records at a time
                            </td>

                            <td width="3%" class="uportal-label">
                                <!--input type="submit" name="set" value="{$setBtnLabel}" tabindex="6" class="uportal-button" /-->
                            </td>

                            <td width="10%"></td>

                            <td width="10%" class="uportal-label">
                             </td>

                            <td width="13%" ></td>


                        </tr>
                    </table>

                <!--/form-->
                </center>
                </xsl:if>
                
                
            </td>
        </tr>
    </table>
    </form>
    
    
    
    
    </xsl:template>

</xsl:stylesheet>
