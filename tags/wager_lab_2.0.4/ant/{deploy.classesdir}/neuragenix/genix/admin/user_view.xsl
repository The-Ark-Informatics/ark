<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./tree_view.xsl"/>
    <xsl:output method="html" indent="no" />
    
<xsl:template match="proccess">

    <table width="100%">
    <tr>
        <td class="uportal-channel-subtitle">User details<hr/>
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
	<xsl:variable name="firstName"><xsl:value-of select="details/USERDETAILS_strFirstName" /></xsl:variable>
        <xsl:variable name="lastName"><xsl:value-of select="details/USERDETAILS_strLastName" /></xsl:variable>
        <xsl:variable name="userName"><xsl:value-of select="details/USERDETAILS_strUserName" /></xsl:variable>
        <xsl:variable name="email"><xsl:value-of select="details/USERDETAILS_strEmail" /></xsl:variable>
        <form action="{$baseActionURL}?action=proccess_user" method="post">
        <table width="100%">
	<tr>
                        <td  class="uportal-label" width="20%" >
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
                    <td id="neuragenix-form-row-label" class="uportal-label" width="20%">
                    <xsl:value-of select="USERDETAILS_strUserNameDisplay"/>: </td>
                    <td id="neuragenix-form-row-label" class="uportal-label"><xsl:value-of select="details/USERDETAILS_strUserName"/></td>
                    <input type="hidden" name="USERDETAILS_strUserName" value="{$userName}"  />
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
                <input type="submit" name="save_user_details" value="Save" class="uportal-button" />
                <!--
                <input type="submit" name="delete_user" value="Delete" 
                onclick="javascript:confirmDelete('{$baseActionURL}?action=proccess_user&amp;delete_user=true&amp;USERDETAILS_strUserName={$userName}')" class="uportal-button"  />
                -->
                <input type="submit" name="change_password" value="Change password" class="uportal-button"/>
                
                <input type="button" name="search" value="Search"  class="uportal-button"
                onclick="javascript:jumpTo('{$baseActionURL}?action=proccess_user&amp;search=true')" />
                        
                </td>
        </tr>
        
        </table>
        </form>
       
</xsl:template>	
</xsl:stylesheet>
