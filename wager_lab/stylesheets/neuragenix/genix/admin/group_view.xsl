<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./tree_view.xsl"/>
    <xsl:output method="html" indent="no" />

        
<xsl:template match="proccess">

    <table width="100%">
    <tr>
        <td class="uportal-channel-subtitle">Group details<hr/>
        </td>
    </tr>
  
    <tr>
        <td class="neuragenix-form-required-text"><xsl:value-of select="error"/>
        </td>
    </tr>
    <tr>
           <td>
           <xsl:apply-templates select="groupDetails"/>
        </td>
    </tr>
    </table>
 
    
    
</xsl:template>
    
    
 
<xsl:template match="allUsers/user/name"/>	
<xsl:template match="groupUsers/user/name"/>
<xsl:template match="groupDetails">
	<xsl:variable name="groupName"><xsl:value-of select="details/USERGROUP_strUsergroupName" /></xsl:variable>
        <xsl:variable name="description"><xsl:value-of select="details/USERGROUP_strDescription" /></xsl:variable>
        <xsl:variable name="groupId"><xsl:value-of select="details/USERGROUP_intUsergroupKey" /></xsl:variable>
        
        <form name="mainForm" action="{$baseActionURL}?action=proccess_group" method="post">
        <table width="100%">
	<tr>
                        <td  class="uportal-label" width="20%">
                        <xsl:value-of select="USERGROUP_strUsergroupNameDisplay"/>: 
                        </td>
			<td class="uportal-label" >
				<input type="text" 
                                name="USERGROUP_strUsergroupName" value="{$groupName}" size="20" class="uportal-input-text" />
			</td>
        
        </tr>
        <tr>
                        <td id="neuragenix-form-row-label" class="uportal-label" width="20%">
                        <xsl:value-of select="USERGROUP_strDescriptionDisplay"/>: 
                        </td>
			<td class="uportal-label" >
				<input class="uportal-button"  
                                name="USERGROUP_strDescription" value="{$description}" size="50" />
			</td>
        
        </tr>
        
     
        <tr>
                    <td colspan="2" id="neuragenix-form-row-label" class="uportal-label">
                    
                    <input type="submit" name="save_group_details" value="Save" class="uportal-button"/>
                    <input type="hidden" name="USERGROUP_intUsergroupKey" value="{$groupId}"/>
                    
                    
                    <input type="submit" name="delete_group" value="Delete" 
                    onclick="javascript:confirmDelete('{$baseActionURL}?action=proccess_group&amp;delete_group=true&amp;USERGROUP_intUsergroupKey={$groupId}')"
                    class="uportal-button"/>
                    
                    <input type="submit" name="add_sub_group" value="Add sub-group" 
                    class="uportal-button"/>
                    
                   
                    
                    <input type="submit" name="set_permission" value="Set permission" 
                    class="uportal-button"/>
                    
                                        
                    </td>
        
                    
        </tr>
         
        </table>
        </form>
        
        <form name="userMenu" action="{$baseActionURL}?action=proccess_group" method="post">
        <table>
        <tr>
                <td class="uportal-label">
                    Users in group:<br/>
                    <select multiple="true" class="uportal-button" size="10"  name="groupUsers">
                        
                        <xsl:for-each select="../groupUsers/user">
                            <xsl:variable name="userName"><xsl:value-of select="name"/></xsl:variable>
                            <option value="{$userName}"><xsl:value-of select="name" /></option>                            
                        </xsl:for-each>
                    
                    </select>
               
                </td>
                <td class="uportal-label" valign="middle">
                    
                    <input type="button" name="add_user_in" value="  &lt;  " 
                    onclick="javascript:move(document.userMenu.allUsers,document.userMenu.groupUsers)" 
                    class="uportal-button"/>
                    
                    <br/>
                    
                    <input type="button" name="take_user_out" value="  &gt;  " 
                    onclick="javascript:move(document.userMenu.groupUsers,document.userMenu.allUsers)" 
                    class="uportal-button"/>
                    
                    <br/>
                    
                    <input type="button" name="add_all_user_in" value=" &lt;&lt; " 
                    onclick="javascript:moveAll(document.userMenu.allUsers,document.userMenu.groupUsers)" 
                    class="uportal-button"/>
                    
                    <br/>
                    
                    <input type="button" name="take_all_user_out" value=" &gt;&gt; " 
                    onclick="javascript:moveAll(document.userMenu.groupUsers,document.userMenu.allUsers)" 
                    class="uportal-button"/>
                    
              
                    
                </td>
                
                <td class="uportal-label">
                    Users NOT in group:<br/>
                    <select class="uportal-button" multiple="true" size="10" width="20" name="allUsers">
                        
                        <xsl:for-each select="../allUsers/user">
                            <xsl:variable name="userName"><xsl:value-of select="name"/></xsl:variable>
                            <option value="{$userName}"><xsl:value-of select="name" /></option>                            
                        </xsl:for-each>
                    
                    </select>
               
                </td>
        
        </tr>
        <tr>
        <td colspan="3">
                    
                    <input type="submit" name="save_user_group_details" value="Save" class="uportal-button"
                    onclick="javascript:doSubmit(document.userMenu, document.userMenu.groupUsers,document.userMenu.allUsers)"/>
                    <input type="hidden" name="USERGROUP_intUsergroupKey" value="{$groupId}"/>
                    
                                        
                    <input type="submit" name="add_user" value="Add user" 
                    class="uportal-button"/>
                    
                    <input type="button" name="search" value="Search"  class="uportal-button"
                    onclick="javascript:jumpTo('{$baseActionURL}?action=proccess_user&amp;search=true')" />
                    
                    
                                        
                    </td>
        
        </tr>
        
        </table>
        </form>
                <!--      <tr>
                        <td  class="uportal-label">
                        Email: 
                        </td>
			<td class="uportal-label" >
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
