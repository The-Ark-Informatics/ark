<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./tree_view.xsl"/>
    <xsl:output method="html" indent="no" />

        
<xsl:template match="proccess">

    <table width="100%">
    <tr>
        <td class="uportal-channel-subtitle">Group permissions<hr/>
        </td>
    </tr>
  
    <tr>
        <td class="neuragenix-form-required-text"><xsl:value-of select="error"/>
        </td>
    </tr>
    <tr>
           <td>
           <xsl:apply-templates select="permissionDetails"/>
        </td>
    </tr>
    </table>
 
    
    
</xsl:template>
    
  
<xsl:template match="permissionDetails">
	<xsl:variable name="groupName"><xsl:value-of select="USERGROUP_strUsergroupName" /></xsl:variable>
        <xsl:variable name="usergroupId"><xsl:value-of select="USERGROUP_intUsergroupKey" /></xsl:variable>
        <form name="permissionMenu" action="{$baseActionURL}?action=proccess_group&amp;set_permission=true" method="post">
        <table>
	<tr>
                        <td id="neuragenix-form-row-label" class="uportal-label" colspan="2">
                            <xsl:value-of select="USERGROUP_strUsergroupNameDisplay"/>: 
                        </td>
                        <td id="neuragenix-form-row-label" class="neuragenix-form-required-text" >
                            <xsl:value-of select="USERGROUP_strUsergroupName"/>
                        </td>
        
        </tr>
        <tr>
        <td class="uportal-label" colspan="2">
                    <xsl:value-of select="GROUP_strGroupNameDisplay"/>: <br/>
        </td>
        <td class="uportal-label" colspan="1">
                        <select class="uportal-button" size="1" width="20" name="GROUP_intGroupKey">
                        <option value="" >--- Please choose a item ---</option>
                        <xsl:variable name="currentGroup"><xsl:value-of select="currentGroup"/></xsl:variable>
                        
                        <xsl:for-each select="groups">
                            <xsl:variable name="groupId"><xsl:value-of select="GROUP_intGroupKey"/></xsl:variable>
                        
                            <xsl:choose>    
                            <xsl:when test="$groupId = $currentGroup">
                            
                                <option value="{$groupId}" selected="true"><xsl:value-of select="GROUP_strGroupName" /></option>  
                                
                            </xsl:when>
                            <xsl:otherwise>
                                <option value="{$groupId}"><xsl:value-of select="GROUP_strGroupName" /></option>
                            </xsl:otherwise>
                            </xsl:choose>
                                                 
                        </xsl:for-each>
                    
                    </select>
               
                </td>
        </tr>
         <tr>
                    <td colspan="2">
                    </td>
                    <td class="uportal-label">
                    <input type="submit" name="change_group" value="View" class="uportal-button"/>
                    </td>
                </tr>
        <tr>
                <td class="uportal-label">
                    Permitted Activities:<br/><br/>
                    <select multiple="true" class="uportal-button" size="10"  name="groupActivities">
                        
                        <xsl:for-each select="groupActivities">
                            <xsl:variable name="activity"><xsl:value-of select="ACTIVITY_intActivityKey"/></xsl:variable>
                            
                            <!--rennypv...to indtroduce the description...option value="{$activity}"><xsl:value-of select="ACTIVITY_strActivityName" /></option-->                            
                            <option value="{$activity}"><xsl:value-of select="ACTIVITY_strActivityDesc" /></option>                            
                        </xsl:for-each>
                    
                    </select>
               
                </td>
               
                <td class="uportal-label" valign="middle">
                    
                    <input type="button" name="add_act_in" value="  &lt;  " 
                    onclick="javascript:move(document.permissionMenu.allActivities,document.permissionMenu.groupActivities)" 
                    class="uportal-button"/>
                    
                    <br/>
                    
                    <input type="button" name="take_act_out" value="  &gt;  " 
                    onclick="javascript:move(document.permissionMenu.groupActivities,document.permissionMenu.allActivities)" 
                    class="uportal-button"/>
                    
                    <br/>
                    
                    <input type="button" name="add_all_act_in" value=" &lt;&lt; " 
                    onclick="javascript:moveAll(document.permissionMenu.allActivities,document.permissionMenu.groupActivities)" 
                    class="uportal-button"/>
                    
                    <br/>
                    
                    <input type="button" name="take_all_act_out" value=" &gt;&gt; " 
                    onclick="javascript:moveAll(document.permissionMenu.groupActivities, document.permissionMenu.allActivities)" 
                    class="uportal-button"/>
                    
              
                    
                </td>
                
                <td class="uportal-label">
                    Denied Activities:<br/><br/>
                    <select class="uportal-button" multiple="true" size="10" width="20" name="allActivities">
                        
                        <xsl:for-each select="allActivities">
                            <xsl:variable name="activity"><xsl:value-of select="ACTIVITY_intActivityKey"/></xsl:variable>
                            <option value="{$activity}"><xsl:value-of select="ACTIVITY_strActivityDesc" /></option>                            
                            <!-- rennypv...removed to display the description....option value="{$activity}"><xsl:value-of select="ACTIVITY_strActivityName" /></option-->                            
                        </xsl:for-each>
                    
                    </select>
               
                </td>
               
        
        </tr>
        
        <!--+++++++++++++++++++++++++++ FIELD SECURITY ++++++++++++++++++++++++++++++-->
        <tr>
                <td class="uportal-label">
                    Fields Available for Restriction:<br/><br/>
                    <select class="uportal-button" multiple="true" size="10" width="20" name="allDenyActivities">
                        
                        <xsl:for-each select="allDenyActivities">
                            <xsl:variable name="activity"><xsl:value-of select="ACTIVITY_intActivityKey"/></xsl:variable>
                            <!--rennypv description display...option value="{$activity}"><xsl:value-of select="ACTIVITY_strActivityName" /></option-->                            
                            <option value="{$activity}"><xsl:value-of select="ACTIVITY_strActivityDesc" /></option>                            
                        </xsl:for-each>
                    
                    </select>
               
                </td>
                <td class="uportal-label" valign="middle">
                    
                    <input type="button" name="add_act_in" value="  &lt;  " 
                    onclick="javascript:move(document.permissionMenu.groupDenyActivities,document.permissionMenu.allDenyActivities)" 
                    class="uportal-button"/>
                    
                    <br/>
                    
                    <input type="button" name="take_act_out" value="  &gt;  " 
                    onclick="javascript:move(document.permissionMenu.allDenyActivities,document.permissionMenu.groupDenyActivities)" 
                    class="uportal-button"/>
                    
                    <br/>
                    
                    <input type="button" name="add_all_act_in" value=" &lt;&lt; " 
                    onclick="javascript:moveAll(document.permissionMenu.groupDenyActivities,document.permissionMenu.allDenyActivities)" 
                    class="uportal-button"/>
                    
                    <br/>
                    
                    <input type="button" name="take_all_act_out" value=" &gt;&gt; " 
                    onclick="javascript:moveAll( document.permissionMenu.allDenyActivities,document.permissionMenu.groupDenyActivities)" 
                    class="uportal-button"/>
                    
              
                    
                </td>
                
               <td class="uportal-label">
                    Fields restricted:<br/><br/>
                    <select multiple="true" class="uportal-button" size="10"  name="groupDenyActivities">
                        
                        <xsl:for-each select="groupDenyActivities">
                            <xsl:variable name="activity"><xsl:value-of select="ACTIVITY_intActivityKey"/></xsl:variable>
                            
                            <!--rennypv....to display the description..option value="{$activity}"><xsl:value-of select="ACTIVITY_strActivityName" /></option-->                            
                            <option value="{$activity}"><xsl:value-of select="ACTIVITY_strActivityDesc" /></option>                            
                        </xsl:for-each>
                    
                    </select>
               
                </td>
               
        
        </tr>
        
        
        
        
        
        <tr>
        <td colspan="3">
                    
                    <input type="submit" name="save_permission" value="Save" class="uportal-button"
                    onclick="javascript:doSubmit(document.permissionMenu, document.permissionMenu.groupActivities,document.permissionMenu.groupDenyActivities)"/>
                    <input type="hidden" name="USERGROUP_intUsergroupKey" value="{$usergroupId}"/>
                    <input type="button" class="uportal-button"
                    onclick="javascript:history.back()" value="Cancel"/>
                         
                    </td>
        
        </tr>
        
        </table>
        </form>
                <!--      <tr>
                        <td  class="uportal-label">
                        Email: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input">
				<input type="text" name="USERDETAILS_strEmail" value="{$email}" size="40" class="uportal-button" />
			</td>
        
        </tr>
        <tr><td colspan="2"><p></p></td>
        </tr>
        <tr>
            <td class="uportal-label" colspan="2" align="left">
                <input type="submit" name="save" tabindex="16" value="Save" class="uportal-button" />
                 
                <input type="button" name="delete" value="Delete" tabindex="17" class="uportal-button" 
                onclick="javascript:confirmDelete('{$baseActionURL}?action=delete_user&amp;USERDETAILS_strUserName={$userName}')" />
                 &#160;
                 
                 <a href="{$baseActionURL}?action=set_password&amp;USERDETAILS_strUserName={$userName}"> Change password</a>
                        
                </td>
        </tr>
        -->


       
</xsl:template>	
</xsl:stylesheet>
