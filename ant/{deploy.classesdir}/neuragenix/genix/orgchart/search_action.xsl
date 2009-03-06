<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./tree_view.xsl"/>
    <xsl:output method="html" indent="no" />
    
<xsl:template match="info">

    <table width="100%">
    <tr>
        <td class="uportal-channel-subtitle">Search<hr/>
        </td>
    </tr>
  
    <tr>
        <td class="neuragenix-form-required-text"><xsl:value-of select="error"/>
        </td>
    </tr>
    <tr>
           <td>
           <xsl:apply-templates select="searchDetails"/>
        </td>
    </tr>
    </table>
 
    
    
</xsl:template>
    
<xsl:template match="searchDetails">
        <xsl:variable name="searchString"><xsl:value-of select="searchString"/></xsl:variable>
        <xsl:variable name="jobTitle"><xsl:value-of select="jobTitle"/></xsl:variable>
        <table width="100%">
	<form action="{$baseActionURL}?action=user" method="post">
	<tr><td class="uportal-label" >Name:
 		</td>
		<td class="uportal-label">
				<input class="uportal-input-text" type="text" name="search_string" value="{$searchString}" size="30"  />
		</td>
	</tr>

	<tr><td class="uportal-label" >Job title:
 		</td>
		<td class="uportal-label">
				<input class="uportal-input-text" type="text" name="job_title" value="{$jobTitle}" size="30"  />
		</td>
	</tr>

	<tr><td class="uportal-label">Employment type:
		</td>
		<td class="uportal-label">		
				<select class="uportal-input-text" name="employment_type">

                       	<option value=""></option>
                        <xsl:for-each select="ORGUSER_strEmploymentType">
                            <xsl:variable name="title"><xsl:value-of select="."/></xsl:variable>

                        	<option value="{$title}"><xsl:value-of select="." /></option>

                        </xsl:for-each>
                    </select>
		</td>

 	</tr>




        <tr>
            <td class="uportal-label" colspan="2" align="left">
                <input type="submit" name="search_button" value="Search" class="uportal-input-text" />
                 
                <input type="reset" name="clear" value="Reset" class="uportal-input-text"/>
                        
                </td>
        </tr>
        </form>
        <tr><td colspan="2"><p/></td></tr>
        <tr>
                    <td id="neuragenix-form-row-label-required" class="uportal-label" valign="top" width="50%">
                      <table width="100%" >
                            <tr>
                            <td id="neuragenix-form-row-label-required" class="uportal-label" >
                            Users found: 
                            </td>
                            
                            </tr>
                            <xsl:for-each select="user">
				<xsl:variable name="userKey"><xsl:value-of select="ORGUSER_intOrgUserKey"/></xsl:variable>
                                <xsl:variable name="userName"><xsl:value-of select="ORGUSER_strFirstName"/>&#160;<xsl:value-of select="ORGUSER_strLastName"/></xsl:variable>
                                <tr>
                                    <td >
                                    <a href="{$baseActionURL}?action=user&amp;ORGUSER_intOrgUserKey={$userKey}"><xsl:value-of select="ORGUSER_strFirstName"/>&#160;<xsl:value-of select="ORGUSER_strLastName"/>
                                    </a>
                                    </td>
                                </tr>
                            
                            </xsl:for-each>
                        
                        
                        
                      
                      </table>  
                    </td>
                    <td id="neuragenix-form-row-label-required" class="uportal-label" valign="top" width="50%">
                        
                        <table width="100%">
                            <tr>
                            <td id="neuragenix-form-row-label-required" class="uportal-label">
                            Groups found: 
                            </td>
                            
                            </tr>
                            <xsl:for-each select="group">
                                <xsl:variable name="groupName"><xsl:value-of select="ORGGROUP_strOrgGroupName"/></xsl:variable>
                                <xsl:variable name="groupId"><xsl:value-of select="ORGGROUP_intOrgGroupKey"/></xsl:variable>
                                <tr>
                                    <td >
                                    <a href="{$baseActionURL}?action=group&amp;ORGGROUP_intOrgGroupKey={$groupId}">
                                    <xsl:value-of select="ORGGROUP_strOrgGroupName"/>
                                    </a>
                                    </td>
                                </tr>
                            
                            </xsl:for-each>
                        
                        
                        
                      
                      </table>  
                    
                     
                   </td>
        </tr>
        
        </table>
        
       
</xsl:template>	
</xsl:stylesheet>
