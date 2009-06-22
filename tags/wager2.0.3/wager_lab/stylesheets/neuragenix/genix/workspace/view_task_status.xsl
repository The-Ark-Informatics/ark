<?xml version="1.0" encoding="utf-8"?>

<!-- 
    view_workflow_instance_status.xsl, part of the Workspace channel
    Author: hhoang@neuragenix.com
    Date: 16/03/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./workspace_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=view_task_status</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="mediaPath">media/org/jasig/portal/layout/tab-column/nested-tables</xsl:variable>
  
    <xsl:template match="workspace">
    <xsl:param name="WORKFLOW_TASK_intTaskKey"><xsl:value-of select="WORKFLOW_TASK_intTaskKey" /></xsl:param>
    <xsl:param name="isHasIncomingAlert"><xsl:value-of select="isHasIncomingAlert" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_intWorkflowInstanceKey"><xsl:value-of select="WORKFLOW_TASK_intWorkflowInstanceKey" /></xsl:param> 
    <form name="status" action="{$baseActionURL}?{$formParams}" method="post">
    <table width="100%">
        <tr>
            <td width="25%"></td>
            <td width="65%" class="uportal-channel-subtitle">
            
                  <img name="view_task_status" src="{$mediaPath}/casegenix/controls/view_task_status_on.gif" border="0"></img> 
                       
                     <a href="{$baseActionURL}?current=view_active_task" onMouseOver="document.images['view_active_task'].src= '{$mediaPath}/casegenix/controls/view_active_task_on.gif'" onMouseOut="document.images['view_active_task'].src='{$mediaPath}/casegenix/controls/view_active_task_off.gif'"><img name="view_active_task" src="{$mediaPath}/casegenix/controls/view_active_task_off.gif" border="0"></img></a> 
                     <a href="{$baseActionURL}?current=search_task" onMouseOver="document.images['search_task'].src= '{$mediaPath}/casegenix/controls/search_task_on.gif'" onMouseOut="document.images['search_task'].src='{$mediaPath}/casegenix/controls/search_task_off.gif'"><img name="search_task" src="{$mediaPath}/casegenix/controls/search_task_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=reassign_tasks" onMouseOver="document.images['reasign_all_tasks'].src= '{$mediaPath}/casegenix/controls/reasign_all_tasks_on.gif'" onMouseOut="document.images['reasign_all_tasks'].src='{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif'"><img name="reasign_all_tasks" src="{$mediaPath}/casegenix/controls/reasign_all_tasks_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=create_alert" onMouseOver="document.images['create_alert'].src= '{$mediaPath}/casegenix/controls/create_alert_on.gif'" onMouseOut="document.images['create_alert'].src='{$mediaPath}/casegenix/controls/create_alert_off.gif'"><img name="create_alert" src="{$mediaPath}/casegenix/controls/create_alert_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=alert_list" onMouseOver="document.images['alert_list'].src= '{$mediaPath}/casegenix/controls/alert_list_on.gif'" onMouseOut="document.images['alert_list'].src='{$mediaPath}/casegenix/controls/alert_list_off.gif'"><img name="alert_list" src="{$mediaPath}/casegenix/controls/alert_list_off.gif" border="0"></img></a> <xsl:if test="$isHasIncomingAlert = 'true'"><img src="media/neuragenix/icons/alert.gif" border="0" /></xsl:if> 

            </td>
            <td width="10%">
                <input type="submit" name="back" tabindex="1" value="{$backBtnLabel}" class="uportal-button" />
            </td>
        </tr>
        
    </table>
    <hr/>
    <table width="100%">
        <tr>
            <td width="5%"></td>
            <td width="90%" class="neuragenix-form-required-text">
            <span id="WORKFLOW_TASK_intWorkflowInstanceKey" value="{$WORKFLOW_TASK_intWorkflowInstanceKey}"/>
                <script language="JavaScript">
                    var screenW = 640, screenH = 400;
                    if (parseInt(navigator.appVersion)>3) {
                        if (navigator.appName=="Netscape") {
                            screenW = (screen.width - 36) * 0.9;
                        }
                        else if (navigator.appName.indexOf("Microsoft")!=-1) {
                            screenW = (screen.width - 42) * 0.9;
                        }
                    }

                    var place;
                    var places;
                    var route;
                    place = location.href; 
                    places = place.split("?"); 
                    if(places[1])
                    { 
                      route = places[1].split("WORKFLOW_TASK_intWorkflowInstanceKey=")[1]; 
                      if(route == undefined)
                      {
                        route = document.getElementById( 'WORKFLOW_TASK_intWorkflowInstanceKey' ).getAttribute( 'value')
                      }
                      else
                      {
                        route = route.split("&amp;")[0]; 
                      }
                    }
                    document.write("&lt;applet code='media.neuragenix.genix.workflow.WFViewer.class' archive='media/jgraph/jgraph.jar' WIDTH='" + screenW + "' HEIGHT='" + screenH + "' &gt;&lt;param name='WorkflowInstanceKey' value='" + route + "'&gt;&lt;/applet&gt;" );
                    //document.write("&lt;applet code='media.neuragenix.genix.workflow.WFViewer.class' archive='media/jgraph/jgraph.jar' WIDTH='" + screenW + "' HEIGHT='" + screenH + "' &gt;&lt;param name='WorkflowInstanceKey' value='" );
                </script>
                
                
            </td>
            <td width="5%"></td>
        </tr>
        
    </table>
    <br />

    <table width="100%">
        <tr>
            
            <td width="20%" class="uportal-channel-table-header">
                Task
            </td>
            <td width="10%" class="uportal-channel-table-header">
                Date Received
            </td>
            <td width="10%" class="uportal-channel-table-header">
                Time Received
            </td>
            <td width="10%" class="uportal-channel-table-header">
                Date Completed
            </td>
            <td width="10%" class="uportal-channel-table-header">
                Time Completed
            </td>
            <td width="10%" class="uportal-channel-table-header">
                Performer
            </td>
            <td width="10%" class="uportal-channel-table-header">
                Action
            </td>
            <td width="20%" class="uportal-channel-table-header">
                Comment
            </td>
            
        </tr>
        
        <tr>
            <td colspan="8"><hr /></td>
        </tr>
        <xsl:for-each select="search_task">
            
        <xsl:variable name="WORKFLOW_TASK_intTaskKey"><xsl:value-of select="WORKFLOW_TASK_intTaskKey" /></xsl:variable>
        
        <xsl:if test="string-length(WORKFLOW_TASK_dtDateCompleted) = 0">    
        <tr>
            
            <td width="20%" class="uportal-label"> 
                <xsl:value-of select="WORKFLOW_TASK_strName" /><xsl:value-of select="WORKFLOW_TASK_HISTORY_strName" />
            </td>

            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_dtDateReceived" /><xsl:value-of select="WORKFLOW_TASK_HISTORY_dtDateReceived" />
            </td>

            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strTimeReceived" /><xsl:value-of select="WORKFLOW_TASK_HISTORY_strTimeReceived" />
            </td>
            
            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_dtDateCompleted" />
            </td>
            
            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strTimeCompleted" />
            </td>
            
            <td width="10%" class="uportal-label">
                <xsl:value-of select="ORGUSER_strFirstName" />&#160;<xsl:value-of select="ORGUSER_strLastName" /><xsl:value-of select="ORGGROUP_strOrgGroupName" />
            </td>
            
            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strAction" /><xsl:value-of select="WORKFLOW_TASK_HISTORY_strAction" />
            </td>

            <td width="20%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strComments" /><xsl:value-of select="WORKFLOW_TASK_HISTORY_strComments" />
            </td>
            
            
        </tr>
        </xsl:if>
        </xsl:for-each>
        
		<xsl:for-each select="search_task">

		<!--This will concat the yyyymmddhhmmss which is the year+month+day+hour+minute+second  -->
		<xsl:sort select="concat(substring(WORKFLOW_TASK_dtDateCompleted,7,4),substring(WORKFLOW_TASK_dtDateCompleted,4,2),substring(WORKFLOW_TASK_dtDateCompleted,1,2),substring(WORKFLOW_TASK_strTimeCompleted,1,2),substring(WORKFLOW_TASK_strTimeCompleted,4,2),substring(WORKFLOW_TASK_strTimeCompleted,7,2))" data-type="number" order="descending"/>

        <xsl:variable name="WORKFLOW_TASK_intTaskKey"><xsl:value-of select="WORKFLOW_TASK_intTaskKey" /></xsl:variable>
        
        <xsl:if test="string-length(WORKFLOW_TASK_dtDateCompleted) > 0">    
        <tr>
            
            <td width="20%" class="uportal-label"> 
                <xsl:value-of select="WORKFLOW_TASK_strName" /><xsl:value-of select="WORKFLOW_TASK_HISTORY_strName" />
            </td>

            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_dtDateReceived" /><xsl:value-of select="WORKFLOW_TASK_HISTORY_dtDateReceived" />
            </td>

            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strTimeReceived" /><xsl:value-of select="WORKFLOW_TASK_HISTORY_strTimeReceived" />
            </td>
            
            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_dtDateCompleted" />
            </td>
            
            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strTimeCompleted" />
            </td>
            
            <td width="10%" class="uportal-label">
                <xsl:value-of select="ORGUSER_strFirstName" />&#160;<xsl:value-of select="ORGUSER_strLastName" /><xsl:value-of select="ORGGROUP_strOrgGroupName" />
            </td>
            
            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strAction" /><xsl:value-of select="WORKFLOW_TASK_HISTORY_strAction" />
            </td>

            <td width="20%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_strComments" /><xsl:value-of select="WORKFLOW_TASK_HISTORY_strComments" />
            </td>
            
            
        </tr>
        </xsl:if>
        </xsl:for-each>
        
        <!--xsl:for-each select="search_task_history">
        
        <tr>
            
            <td width="20%" class="uportal-label"> 
                <xsl:value-of select="WORKFLOW_TASK_HISTORY_strName" />
            </td>

            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_HISTORY_dtDateReceived" />
            </td>

            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_HISTORY_strTimeReceived" />
            </td>
            
            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_HISTORY_dtDateCompleted" />
            </td>
            
            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_HISTORY_strTimeCompleted" />
            </td>
            
            <td width="10%" class="uportal-label">
                <xsl:value-of select="ORGUSER_strFirstName" />&#160;<xsl:value-of select="ORGUSER_strLastName" />
            </td>

            <td width="10%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_HISTORY_strAction" />
            </td>
            
            <td width="20%" class="uportal-label">
                <xsl:value-of select="WORKFLOW_TASK_HISTORY_strComments" />
            </td>
            
            
        </tr>
        </xsl:for-each-->
    </table>
    
    
    <!-- hidden fields to send data to server -->
    <input type="hidden" name="WORKFLOW_TASK_intTaskKey" value="{$WORKFLOW_TASK_intTaskKey}" />
    </form>
    </xsl:template>

</xsl:stylesheet>
