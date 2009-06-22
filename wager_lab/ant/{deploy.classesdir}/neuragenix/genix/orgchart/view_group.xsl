<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./tree_view.xsl"/>
    <xsl:output method="html" indent="no" />

        
<xsl:template match="info">

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
    
    
 
<xsl:template match="groupDetails">
	<xsl:variable name="groupName"><xsl:value-of select="details/ORGGROUP_strOrgGroupName" /></xsl:variable>
        <xsl:variable name="groupDescription"><xsl:value-of select="details/ORGGROUP_strDescription" /></xsl:variable>
        <xsl:variable name="groupKey"><xsl:value-of select="details/ORGGROUP_intOrgGroupKey" /></xsl:variable>
		<xsl:variable name="groupParent"><xsl:value-of select="details/ORGGROUP_intParentGroupKey" /></xsl:variable>
        <xsl:variable name="groupLocation"><xsl:value-of select="details/ORGGROUP_strLocation" /></xsl:variable>
        <xsl:variable name="groupSupervisor"><xsl:value-of select="details/ORGGROUP_intSupervisor" /></xsl:variable>
        <xsl:variable name="groupDeleted"><xsl:value-of select="details/ORGGROUP_intDeleted" /></xsl:variable>
        <xsl:variable name="groupGroup"><xsl:value-of select="details/ORGGROUP_intGroupKey" /></xsl:variable>
        
        <form name="mainForm" action="{$baseActionURL}?action=group" method="post">
        <table width="100%">
	<tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label" width="20%">
                        <xsl:value-of select="ORGGROUP_strOrgGroupNameDisplay"/>: 
                        </td>
						<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
							<input type="text" 
                                name="ORGGROUP_strOrgGroupName" value="{$groupName}" size="20" class="uportal-input-text" />
						</td>
        
        </tr>
        <tr>
                        <td id="neuragenix-form-row-label" class="uportal-label" width="20%">
                        <xsl:value-of select="ORGGROUP_strDescriptionDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
				<input class="uportal-input-text" type="text" 
                                name="ORGGROUP_strDescription" value="{$groupDescription}" size="50" />
			</td>
        
        </tr>
       	<tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label" width="20%">
                        <xsl:value-of select="ORGGROUP_strLocationDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
				<input type="text" 
                                name="ORGGROUP_strLocation" value="{$groupLocation}" size="20" class="uportal-input-text" />
			</td>
        
        </tr>
        <tr>
                        <td id="neuragenix-form-row-label" class="uportal-label" width="20%">
                        <xsl:value-of select="ORGGROUP_intSupervisorDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
	             <select class="uportal-input-text" name="ORGGROUP_intSupervisor">
                        
                        <xsl:for-each select="supervisor/user">
                            <xsl:variable name="userKey"><xsl:value-of select="ORGUSER_intOrgUserKey"/></xsl:variable>
							<xsl:choose>
								<xsl:when test="ORGUSER_intOrgUserKey=$groupSupervisor">

		                            <option value="{$userKey}" selected="true"><xsl:value-of select="ORGUSER_strFirstName" />&#160; <xsl:value-of select="ORGUSER_strLastName"/></option>                            
								</xsl:when>
								<xsl:otherwise>
				
		                            <option value="{$userKey}"><xsl:value-of select="ORGUSER_strFirstName" />&#160; <xsl:value-of select="ORGUSER_strLastName"/></option>                            
								</xsl:otherwise>
							</xsl:choose>
                        </xsl:for-each>
                    
                    </select>
			</td>
        
        </tr>
        
        
                    
         
        <tr>
                <td class="uportal-label">
                    Users in group:<br/>
                    <select multiple="true" class="uportal-input-text" size="10"  name="groupUsers">
                        
                        <xsl:for-each select="groupUsers/user">
                            <xsl:variable name="userKey"><xsl:value-of select="ORGUSER_intOrgUserKey"/></xsl:variable>
                            <option value="{$userKey}"><xsl:value-of select="ORGUSER_strFirstName" />&#160; <xsl:value-of select="ORGUSER_strLastName"/></option>                            
                        </xsl:for-each>
                    
                    </select>
               
                </td>
                <td class="uportal-label" valign="middle">
                    <br/>
                    
                    <input type="button" name="take_user_out" value="  &gt;  " 
                    onclick="javascript:move(document.mainForm.groupUsers,document.mainForm.allUsers)" 
                    class="uportal-button"/>
                    <br/>

					<input type="button" name="add_user_in" value="  &lt;  " 
                    onclick="javascript:move(document.mainForm.allUsers,document.mainForm.groupUsers)" 
                    class="uportal-button"/>
                    
                    <br/>
                    
                    <input type="button" name="add_all_user_in" value=" &lt;&lt; " 
                    onclick="javascript:moveAll(document.mainForm.allUsers,document.mainForm.groupUsers)" 
                    class="uportal-button"/>
                    
                    <br/>
                    
                    <input type="button" name="take_all_user_out" value=" &gt;&gt; " 
                    onclick="javascript:moveAll(document.mainForm.groupUsers,document.mainForm.allUsers)" 
                    class="uportal-button"/>
                    
              
                    
                </td>
                
                <td class="uportal-label">
                    Users NOT in group:<br/>
                    <select class="uportal-input-text" multiple="true" size="10" width="20" name="allUsers">
                        
                        <xsl:for-each select="otherUsers/user">
                            <xsl:variable name="userKey"><xsl:value-of select="ORGUSER_intOrgUserKey"/></xsl:variable>
                            <option value="{$userKey}"><xsl:value-of select="ORGUSER_strFirstName" />&#160; <xsl:value-of select="ORGUSER_strLastName"/></option>                            
                        </xsl:for-each>
                    
                    </select>
               
                </td>
        
        </tr>
        <tr>
        <td colspan="3" class="uportal-label">
                    
                    <input type="submit" name="save_button" value="Save" class="uportal-input-text"
                    onclick="javascript:doSubmit(document.mainForm, document.mainForm.groupUsers,document.mainForm.allUsers)"/>
                    <input type="submit" name="add_group" value="Add Group" class="uportal-input-text"/>
                   
                    <input type="submit" name="search_button" value="Search" class="uportal-input-text"/>
                     <input type="button" name="add_user" value="Add User" class="uportal-input-text"
				onclick="javascript:jumpTo('{$baseActionURL}?action=user&amp;add_user=add')"/>
                    <input type="button" name="delete_group" value="Delete" class="uportal-input-text"
				onclick="javascript:confirmDelete('{$baseActionURL}?action=group&amp;ORGGROUP_intOrgGroupKey={$groupKey}&amp;delete_group=true')"/>
	
     
                    <input type="hidden" name="ORGGROUP_intParentGroupKey" value="{$groupParent}"/>
                    <input type="hidden" name="ORGGROUP_intOrgGroupKey" value="{$groupKey}"/>
                    <input type="hidden" name="ORGGROUP_intDeleted" value="{$groupDeleted}"/>
                    <input type="hidden" name="ORGGROUP_intGroupKey" value="{$groupGroup}"/>
	
  		</td>

        </tr>
        
        </table>
        </form>

       
</xsl:template>	
</xsl:stylesheet>
