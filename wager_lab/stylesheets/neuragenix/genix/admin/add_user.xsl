<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./tree_view.xsl"/>
    <xsl:output method="html" indent="no" />
    
<xsl:template match="proccess">

    <table width="100%">
    <tr>
        <td class="uportal-channel-subtitle">Add user<hr/>
        </td>
    </tr>
  
    <tr>
        <td class="neuragenix-form-required-text"><xsl:value-of select="error"/>
        </td>
    </tr>
    <tr>
           <td>
           <xsl:apply-templates select="userDetails"/>
        </td>
    </tr>
    </table>
 
    
    
</xsl:template>
    
<xsl:template match="userDetails">
        
	<xsl:variable name="firstName"><xsl:value-of select="USERDETAILS_strFirstName" /></xsl:variable>
        <xsl:variable name="lastName"><xsl:value-of select="USERDETAILS_strLastName" /></xsl:variable>
        <xsl:variable name="userName"><xsl:value-of select="USERDETAILS_strUserName" /></xsl:variable>
        <xsl:variable name="email"><xsl:value-of select="USERDETAILS_strEmail" /></xsl:variable>
        <form action="{$baseActionURL}?action=proccess_group&amp;add_user=true" method="post">
        <table width="100%">
	
        
        
        <tr>
                        <td  class="uportal-label" width="20%">
                        <xsl:value-of select="USERDETAILS_strFirstNameDisplay"/>: 
                        </td>
			<td class="uportal-label" >
				<input class="uportal-input-text" type="text" name="USERDETAILS_strFirstName" value="{$firstName}" size="20"  />
			</td>
        
        </tr>
        <tr>
                        <td  class="uportal-label" width="20%">
                        <xsl:value-of select="USERDETAILS_strLastNameDisplay"/>: 
                        </td>
			<td class="uportal-label" >
				<input class="uportal-input-text" type="text" name="USERDETAILS_strLastName" value="{$lastName}" size="20"  />
			</td>
        
        </tr>
        <tr>
                        <td  class="uportal-label" width="20%">
                        <xsl:value-of select="USERDETAILS_strUserNameDisplay"/>: 
                        </td>
			<td class="uportal-label" >
				<input class="uportal-input-text" type="text" name="USERDETAILS_strUserName" value="{$userName}" size="20"  />
                                
			</td>
        
        </tr>
        
        
        <tr>
                        <td  class="uportal-label" width="20%">
                        <xsl:value-of select="USERDETAILS_strEmailDisplay"/>: 
                        </td>
			<td class="uportal-label" >
				<input class="uportal-input-text" type="text" name="USERDETAILS_strEmail" value="{$email}" size="40"  />
			</td>
        
        </tr>
        <tr><td colspan="2"><p></p></td>
        </tr>
        <tr>
            <td class="uportal-label" colspan="2" align="left">
            <xsl:if test="clone">
            <xsl:variable name="cloneuser"><xsl:value-of select="clone" /></xsl:variable>                
            <input type="hidden" name="USERID_strUserName" value="{$cloneuser}"/>
            </xsl:if>
            <xsl:if test="userGroupKey">
            <xsl:variable name="groupkey"><xsl:value-of select="userGroupKey" /></xsl:variable> 
            <!--xsl:value-of select="userGroupKey" /-->           
            <input type="hidden" name="UsergroupKey" value="{$groupkey}"/>
            <input type="hidden" name="USERGROUP_intUsergroupKey" value="{$groupkey}"/>
            </xsl:if>
                <input type="submit" name="add_new_user" value="Save" class="uportal-button" />
                <xsl:choose>
                <xsl:when test="userGroupKey">
                    <xsl:variable name="groupkey"><xsl:value-of select="userGroupKey" /></xsl:variable> 
                    <input type="button" name="cancel" value="Cancel" class="uportal-button"
                    onclick="javascript:jumpTo('{$baseActionURL}?cancel=true&amp;UsergroupKey={$groupkey}')"/>
                </xsl:when>
                <xsl:otherwise>
                    <input type="button" name="cancel" value="Cancel" class="uportal-button"
                    onclick="javascript:jumpTo('{$baseActionURL}?cancel=true')"/>
                </xsl:otherwise>
                </xsl:choose>
                
                <input type="button" name="search" value="Search"  class="uportal-button"
                onclick="javascript:jumpTo('{$baseActionURL}?action=proccess_user&amp;search=true')" />
                
                </td>
        </tr>
        
        </table>
        </form>
       
</xsl:template>	
</xsl:stylesheet>
