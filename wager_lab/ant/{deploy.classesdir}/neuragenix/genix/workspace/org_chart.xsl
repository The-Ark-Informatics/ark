<?xml version="1.0" encoding="utf-8"?>

<!-- 
    org_chart.xsl, part of the Workspace channel
    Author: hhoang@neuragenix.com
    Date: 04/03/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./workspace_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=org_chart</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="mediaPath">media/org/jasig/portal/layout/tab-column/nested-tables</xsl:variable>
    <xsl:param name="source_page"><xsl:value-of select="workspace/source_page"/></xsl:param>
    <xsl:param name="field_name"><xsl:value-of select="workspace/field_name"/></xsl:param>
  
    <xsl:template match="workspace">
    
        <table width="100%">
            <tr>
                <td width="20%"></td>
                <td width="80%" class="uportal-channel-subtitle">
                    <img name="organisation_chart" src="{$mediaPath}\\casegenix\\controls\\organisation_chart_on.gif" border="0"></img> 
                   
                     <a href="{$baseActionURL}?current=view_active_task" onMouseOver="document.images['view_active_task'].src= '{$mediaPath}\\casegenix\\controls\\view_active_task_on.gif'" onMouseOut="document.images['view_active_task'].src='{$mediaPath}\\casegenix\\controls\\view_active_task_off.gif'"><img name="view_active_task" src="{$mediaPath}\\casegenix\\controls\\view_active_task_off.gif" border="0"></img></a> 
                     <a href="{$baseActionURL}?current=search_task" onMouseOver="document.images['search_task'].src= '{$mediaPath}\\casegenix\\controls\\search_task_on.gif'" onMouseOut="document.images['search_task'].src='{$mediaPath}\\casegenix\\controls\\search_task_off.gif'"><img name="search_task" src="{$mediaPath}\\casegenix\\controls\\search_task_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=reassign_tasks" onMouseOver="document.images['reasign_all_tasks'].src= '{$mediaPath}\\casegenix\\controls\\reasign_all_tasks_on.gif'" onMouseOut="document.images['reasign_all_tasks'].src='{$mediaPath}\\casegenix\\controls\\reasign_all_tasks_off.gif'"><img name="reasign_all_tasks" src="{$mediaPath}\\casegenix\\controls\\reasign_all_tasks_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=create_alert" onMouseOver="document.images['create_alert'].src= '{$mediaPath}\\casegenix\\controls\\create_alert_on.gif'" onMouseOut="document.images['create_alert'].src='{$mediaPath}\\casegenix\\controls\\create_alert_off.gif'"><img name="create_alert" src="{$mediaPath}\\casegenix\\controls\\create_alert_off.gif" border="0"></img></a> 
                    <a href="{$baseActionURL}?current=alert_list" onMouseOver="document.images['alert_list'].src= '{$mediaPath}\\casegenix\\controls\\alert_list_on.gif'" onMouseOut="document.images['alert_list'].src='{$mediaPath}\\casegenix\\controls\\alert_list_off.gif'"><img name="alert_list" src="{$mediaPath}\\casegenix\\controls\\alert_list_off.gif" border="0"></img></a> 

                </td>
            </tr>

        </table>
        <hr/>
        <table width="100%">
            <tr>
                <td width="5%"></td>
                <td width="95%" class="uportal-channel-subtitle">
                    <xsl:apply-templates select="orgTree" />
                </td>
            </tr>
        </table>
        
    </xsl:template>
    
    <xsl:template match="orgTree">
        <xsl:apply-templates select="treeNode[ORGGROUPTREE_intParentGroupKey='-1']"/>
    </xsl:template>
    
    <xsl:template match="treeNode">
	<p>
	    <xsl:variable name="cid"><xsl:value-of select="ORGGROUPTREE_intOrgGroupKey"/></xsl:variable>
	    <xsl:variable name="expanded"><xsl:value-of select="expanded"/></xsl:variable>
	    <xsl:variable name="url_action"><xsl:value-of select="url_action"/></xsl:variable>
            
	    <xsl:if test="$url_action='group'">
		<xsl:choose>
			<xsl:when test="$expanded='true'">

				<a href="{$baseActionURL}?current=org_chart&amp;source_page={$source_page}&amp;field_name={$field_name}&amp;treeAction=collapse&amp;ORGGROUP_intOrgGroupKey={$cid}">
					<img  src="media/neuragenix/icons/minus.gif" border="0"/>
				</a>&#160;
			</xsl:when>
			<xsl:otherwise>
				<a href="{$baseActionURL}?current=org_chart&amp;source_page={$source_page}&amp;field_name={$field_name}&amp;treeAction=expand&amp;ORGGROUP_intOrgGroupKey={$cid}">
					<img  src="media/neuragenix/icons/plus.gif" border="0"/>
				</a>&#160;
			</xsl:otherwise>
		</xsl:choose>
            </xsl:if>
            
            <xsl:choose>
                <xsl:when test="$url_action='group'">
                    <xsl:variable name="id"><xsl:value-of select="ORGGROUPTREE_intOrgGroupKey"/></xsl:variable>
                    <xsl:variable name="name"><xsl:value-of select="ORGGROUPTREE_strOrgGroupName"/></xsl:variable>
                        <a href="{$baseActionURL}?current={$source_page}&amp;source=orgchart&amp;{$field_name}={$name}&amp;type=Group&amp;key={$cid}">
                            <xsl:value-of select="ORGGROUPTREE_strOrgGroupName"/>
                        </a>	
                </xsl:when>
                <xsl:otherwise>

                    <xsl:variable name="id"><xsl:value-of select="ORGUSERTREE_intOrgUserKey"/></xsl:variable>
                    <xsl:variable name="first_name"><xsl:value-of select="ORGUSERTREE_strFirstName"/></xsl:variable>
                    <xsl:variable name="last_name"><xsl:value-of select="ORGUSERTREE_strLastName"/></xsl:variable>
                    <a href="{$baseActionURL}?current={$source_page}&amp;source=orgchart&amp;{$field_name}={$first_name} {$last_name}&amp;type=User&amp;key={$id}">
                        <xsl:value-of select="ORGUSERTREE_strFirstName"/>&#160;<xsl:value-of select="ORGUSERTREE_strLastName"/>
                    </a>
                </xsl:otherwise>
            </xsl:choose>

            <xsl:choose>
		<xsl:when test="$expanded='true'">
                    <div style="margin-left: 20px; display: block;"> 
                            <xsl:apply-templates select="../treeNode[ORGGROUPTREE_intParentGroupKey=$cid]"/>
                    </div>
		</xsl:when>
                
		<xsl:otherwise>
                    <div style="margin-left: 20px; display: none;"> 
                            <xsl:apply-templates select="../treeNode[ORGGROUPTREE_intParentGroupKey=$cid]"/>
                    </div>
		</xsl:otherwise>	
            </xsl:choose>
	</p>
    </xsl:template>

</xsl:stylesheet>
