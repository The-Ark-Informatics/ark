<?xml version="1.0" encoding="utf-8"?>

<!-- 
    view_active_task.xsl, part of the Workspace channel
    Author: hhoang@neuragenix.com
    Date: 25/02/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./workspace_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=view_active_task</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="mediaPath">media/org/jasig/portal/layout/tab-column/nested-tables</xsl:variable>
  
    <xsl:template match="workspace">
    <xsl:param name="isHasIncomingAlert"><xsl:value-of select="isHasIncomingAlert" /></xsl:param>
    
    <form action="{$baseActionURL}?{$formParams}" method="post">
    <xsl:variable name="task_order"><xsl:value-of select="task_order" /></xsl:variable>
    <xsl:variable name="task_sort"><xsl:value-of select="task_sort" /></xsl:variable>
    <script language="javascript" >
        function jumpTo(aURL)
        {
                window.location=aURL;
        }
    </script>
    <table width="100%">
        <tr>
            <td width="25%"></td>
            <td width="65%" class="uportal-label">
                 <img name="view_active_task" src="{$mediaPath}/casegenix/controls/view_active_task_on.gif" border="0"></img> 
                   
                     <a href="{$baseActionURL}?current=search_task" onMouseOver="document.images['search_task'].src= '{$mediaPath}/casegenix/controls/search_task_on.gif'" onMouseOut="document.images['search_task'].src='{$mediaPath}/casegenix/controls/search_task_off.gif'"><img name="search_task" src="{$mediaPath}/casegenix/controls/search_task_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=reassign_tasks" onMouseOver="document.images['reasign_all_tasks'].src= '{$mediaPath}/casegenix/controls/reasign_all_tasks_on.gif'" onMouseOut="document.images['reasign_all_tasks'].src='{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif'"><img name="reasign_all_tasks" src="{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=create_alert" onMouseOver="document.images['create_alert'].src= '{$mediaPath}/casegenix/controls/create_alert_on.gif'" onMouseOut="document.images['create_alert'].src='{$mediaPath}/casegenix/controls/create_alert_off.gif'"><img name="create_alert" src="{$mediaPath}/casegenix/controls/create_alert_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=alert_list" onMouseOver="document.images['alert_list'].src= '{$mediaPath}/casegenix/controls/alert_list_on.gif'" onMouseOut="document.images['alert_list'].src='{$mediaPath}/casegenix/controls/alert_list_off.gif'"><img name="alert_list" src="{$mediaPath}/casegenix/controls/alert_list_off.gif" border="0"></img></a><xsl:if test="$isHasIncomingAlert = 'true'"><img src="media/neuragenix/icons/alert.gif" border="0" /></xsl:if> 

            
                
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
            <td width="5%"></td>
            <td width="30%" class="uportal-channel-table-header">
            <a href="{$baseActionURL}?{$formParams}&amp;orderBy=WORKFLOW_TASK_strName">
            <xsl:value-of select="WORKFLOW_TASK_strNameDisplay" />
            </a>
            </td>
            <td width="10%" class="uportal-channel-table-header">

            <a href="{$baseActionURL}?{$formParams}&amp;orderBy=WORKFLOW_TASK_strPriority">
                <xsl:value-of select="WORKFLOW_TASK_strPriorityDisplay" />
            </a>
            </td>
            <td width="15%" class="uportal-channel-table-header">
            
            <a href="{$baseActionURL}?{$formParams}&amp;orderBy=WORKFLOW_TASK_dtDateReceived">
                <xsl:value-of select="WORKFLOW_TASK_dtDateReceivedDisplay" />
            </a>
            </td>
            <td width="15%" class="uportal-channel-table-header">
            
            <a href="{$baseActionURL}?{$formParams}&amp;orderBy=WORKFLOW_TASK_dtDateReceived">
                <xsl:value-of select="WORKFLOW_TASK_strTimeReceivedDisplay" />
            </a>
            </td>
            <td width="20%" class="uportal-channel-table-header">
            
            <a href="{$baseActionURL}?{$formParams}&amp;orderBy=WORKFLOW_TASK_strStatus">
                <xsl:value-of select="WORKFLOW_TASK_strStatusDisplay" />
            </a>
            </td>
            
            <td width="5%"></td>
        </tr>
        
        <tr>
            <td height="10px"></td>
        </tr>
        <xsl:variable name="order"><xsl:value-of select="order" /></xsl:variable>
        <xsl:choose>
        <xsl:when test="string-length( sort ) > 0">        
        <xsl:for-each select="search_task" >
        <xsl:sort select="WORKFLOW_TASK_strName" data-type="text" order='{$order}'/>
        <xsl:variable name="WORKFLOW_TASK_intTaskKey"><xsl:value-of select="WORKFLOW_TASK_intTaskKey" /></xsl:variable>
        <xsl:variable name="WORKFLOW_TASK_strPriority"><xsl:value-of select="WORKFLOW_TASK_strPriority" /></xsl:variable>
        <tr>
            <xsl:choose>
            <!--xsl:when test="$lineType = 'odd'"-->
            <xsl:when test="position() mod 2 != 0">
            <td width="5%"></td>
            <td width="30%" class="uportal-background-light"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_strName" />
                </a>
            </td>

            <td width="10%" class="uportal-background-light">
            <xsl:choose>
            <xsl:when test="$WORKFLOW_TASK_strPriority = 1">
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    Low
                </a>
            </xsl:when>

            <xsl:when test="$WORKFLOW_TASK_strPriority = 2">
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    Medium
                </a>
            </xsl:when>

            <xsl:when test="$WORKFLOW_TASK_strPriority = 3">
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    High
                </a>
            </xsl:when>
            </xsl:choose>    
            </td>

            <td width="15%" class="uportal-background-light"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_dtDateReceived" />
                </a>
            </td>

            <td width="15%" class="uportal-background-light"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_strTimeReceived" />
                </a>
            </td>
            
            <td width="20%" class="uportal-background-light"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_strStatus" />
                </a>
            </td>

            <td width="5%"></td>
            </xsl:when>
            
            <xsl:otherwise>
            <td width="5%"></td>
            <td width="30%" class="neuragenix-form-row-input"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_strName" />
                </a>
            </td>

            <td width="10%" class="neuragenix-form-row-input">
            <xsl:choose>
            <xsl:when test="$WORKFLOW_TASK_strPriority = 1">
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    Low
                </a>
            </xsl:when>

            <xsl:when test="$WORKFLOW_TASK_strPriority = 2">
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    Medium
                </a>
            </xsl:when>

            <xsl:when test="$WORKFLOW_TASK_strPriority = 3">
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    High
                </a>
            </xsl:when>
            </xsl:choose>    
            </td>

            <td width="15%" class="neuragenix-form-row-input"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_dtDateReceived" />
                </a>
            </td>

            <td width="15%" class="neuragenix-form-row-input"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_strTimeReceived" />
                </a>
            </td>
            
            <td width="20%" class="neuragenix-form-row-input"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_strStatus" />
                </a>
            </td>

            <td width="5%"></td>
            </xsl:otherwise>
            </xsl:choose>
        </tr>
        </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
        <xsl:for-each select="search_task">
        <xsl:variable name="WORKFLOW_TASK_intTaskKey"><xsl:value-of select="WORKFLOW_TASK_intTaskKey" /></xsl:variable>
        <xsl:variable name="WORKFLOW_TASK_strPriority"><xsl:value-of select="WORKFLOW_TASK_strPriority" /></xsl:variable>
        <tr>
            <xsl:choose>
            <!--xsl:when test="$lineType = 'odd'"-->
            <xsl:when test="position() mod 2 != 0">
            <td width="5%"></td>
            <td width="30%" class="uportal-background-light"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_strName" />
                </a>
            </td>

            <td width="10%" class="uportal-background-light">
            <xsl:choose>
            <xsl:when test="$WORKFLOW_TASK_strPriority = 1">
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    Low
                </a>
            </xsl:when>

            <xsl:when test="$WORKFLOW_TASK_strPriority = 2">
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    Medium
                </a>
            </xsl:when>

            <xsl:when test="$WORKFLOW_TASK_strPriority = 3">
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    High
                </a>
            </xsl:when>
            </xsl:choose>    
            </td>

            <td width="15%" class="uportal-background-light"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_dtDateReceived" />
                </a>
            </td>

            <td width="15%" class="uportal-background-light"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_strTimeReceived" />
                </a>
            </td>
            
            <td width="20%" class="uportal-background-light"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_strStatus" />
                </a>
            </td>

            <td width="5%"></td>
            </xsl:when>
            
            <xsl:otherwise>
            <td width="5%"></td>
            <td width="30%" class="neuragenix-form-row-input"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_strName" />
                </a>
            </td>

            <td width="10%" class="neuragenix-form-row-input">
            <xsl:choose>
            <xsl:when test="$WORKFLOW_TASK_strPriority = 1">
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    Low
                </a>
            </xsl:when>

            <xsl:when test="$WORKFLOW_TASK_strPriority = 2">
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    Medium
                </a>
            </xsl:when>

            <xsl:when test="$WORKFLOW_TASK_strPriority = 3">
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    High
                </a>
            </xsl:when>
            </xsl:choose>    
            </td>

            <td width="15%" class="neuragenix-form-row-input"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_dtDateReceived" />
                </a>
            </td>

            <td width="15%" class="neuragenix-form-row-input"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_strTimeReceived" />
                </a>
            </td>
            
            <td width="20%" class="neuragenix-form-row-input"> 
                <a href="{$baseActionURL}?current=view_task&amp;source=view_active_task&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}">
                    <xsl:value-of select="WORKFLOW_TASK_strStatus" />
                </a>
            </td>

            <td width="5%"></td>
            </xsl:otherwise>
            </xsl:choose>
        </tr>
        </xsl:for-each>
        </xsl:otherwise>
        </xsl:choose>
        
    </table>
        
    </form>
    
    <xsl:variable name="intCurrentPage"><xsl:value-of select="currentPage" /></xsl:variable>
    <xsl:variable name="intNoOfPages"><xsl:value-of select="noOfPages" /></xsl:variable>
    <xsl:variable name="intRecordPerPage"><xsl:value-of select="noOfRecords" /></xsl:variable>
    <script language="javascript">

    function submitNewPage( offset ){

            document.navigationForm.currentPage.value = parseInt( document.navigationForm.currentPage.value ) + parseInt(offset);

            document.navigationForm.submit();

    }
    </script>
    
        <table width="100%">
            <tr>
                <td class="uportal-channel-subtitle">
                    <hr/>
                </td>
            </tr>
        </table>
        <center>
        <form name="navigationForm" action="{$baseActionURL}?current=view_active_task" method="post">

            <table>
                
                <tr>          

                    <td class="uportal-label">
                        Page:
                    </td>

                    <td  class="uportal-label">
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
                    <td ></td>

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

        </form>
        </center>
    </xsl:template>

</xsl:stylesheet>
