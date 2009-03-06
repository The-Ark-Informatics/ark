<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./tree_view.xsl"/>
    <xsl:output method="html" indent="no" />
    
<xsl:template match="proccess">

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
        
	<xsl:variable name="searchString"><xsl:value-of select="searchString" /></xsl:variable>
        
        
        <table width="100%">
	
        <form action="{$baseActionURL}?action=proccess_user&amp;search=true" method="post">
        
        <tr>
                        <td  class="uportal-label">
                        Search string: 
                        </td>
			<td class="uportal-label" >
				<input class="uportal-input-text" type="text" name="search_string" value="{$searchString}" size="30"  />
			</td>
        
        </tr>
        <tr>
            <td class="uportal-label" colspan="2" align="left">
                <input type="submit" name="search" value="Search" class="uportal-button" />
                 
                <input type="button" name="cancel" value="Cancel" class="uportal-button"
                onclick="javascript:history.back()"/>
                        
                </td>
        </tr>
        </form>
        <tr><td colspan="2"><p/></td></tr>
        <tr>
                    <td  class="uportal-label" valign="top" width="50%">
                      <table width="100%" >
                            <tr>
                            <td  class="uportal-label" >
                            Users found: 
                            </td>
                            
                            </tr>
                            <xsl:for-each select="user">
                                <xsl:variable name="userName"><xsl:value-of select="USERDETAILS_strUserName"/></xsl:variable>
                                <tr>
                                    <td >
                                    <a href="{$baseActionURL}?action=proccess_user&amp;USERDETAILS_strUserName={$userName}"><xsl:value-of select="USERDETAILS_strUserName"/>
                                    </a>
                                    </td>
                                </tr>
                            
                            </xsl:for-each>
                        
                        
                        
                      
                      </table>  
                    </td>
                    <td  class="uportal-label" valign="top" width="50%">
                        
                        <table width="100%">
                            <tr>
                            <td  class="uportal-label">
                            Groups found: 
                            </td>
                            
                            </tr>
                            <xsl:for-each select="usergroup">
                                <xsl:variable name="groupName"><xsl:value-of select="USERGROUP_strUsergroupName"/></xsl:variable>
                                <xsl:variable name="groupId"><xsl:value-of select="USERGROUP_intUsergroupKey"/></xsl:variable>
                                <tr>
                                    <td >
                                    <a href="{$baseActionURL}?action=proccess_group&amp;USERGROUP_intUsergroupKey={$groupId}">
                                    <xsl:value-of select="USERGROUP_strUsergroupName"/>
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
