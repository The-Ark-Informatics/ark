<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:output method="html" indent="no" />
    
<xsl:template match="proccess">

    <table width="100%">
    <tr>
        <td class="uportal-channel-subtitle">Set your password<hr/>
        </td>
    </tr>
  
    <tr>
        <td class="neuragenix-form-required-text"><xsl:value-of select="error"/>
        </td>
    </tr>
    <tr>
           <td>
           <xsl:apply-templates select="userPassword"/>
        </td>
    </tr>
    </table>
 
    
    
</xsl:template>
    
    
    	
<xsl:template match="userPassword">

<xsl:variable name="userName"><xsl:value-of select="USERDETAILS_strUserName" /></xsl:variable>	
        <form action="{$baseActionURL}?action=proccess_user&amp;change_password=true" method="post">
        <table width="100%">
	
          
        <tr>
                    <td id="neuragenix-form-row-label" class="uportal-label" width="20%">
                    <xsl:value-of select="USERDETAILS_strUserNameDisplay"/>: </td>
                    <td><xsl:value-of select="USERDETAILS_strUserName"/></td>
                    <input type="hidden" name="USERDETAILS_strUserName" value="{$userName}"  />
        </tr>
        <tr>
                        <td  class="uportal-label" width="20%">
                        Password: 
                        </td>
			<td class="uportal-label" >
				<input class="uportal-input-text" type="password" name="USERDETAILS_strPassword" size="20"  />
			</td>
        
        </tr>
        <tr>
                        <td  class="uportal-label" width="20%">
                        Re-type: 
                        </td>
			<td class="uportal-label" >
                        
				<input class="uportal-input-text" type="password" name="re-type" size="20" />
			</td>
        
        </tr>
        <tr><td colspan="2"><p></p></td>
        </tr>
        <tr>
            <td colspan="2" align="left">
                <input type="submit" name="save_user_password" tabindex="16" value="Save" class="uportal-button" />
                
                <input type="button" name="cancel" value="Cancel" tabindex="17" class="uportal-button" 
                onclick="javascript:jumpTo('{$baseActionURL}?action=proccess_user&amp;USERDETAILS_strUserName={$userName}')" />
                
             
               
                </td>
        </tr>
        
        </table>
        </form>
     
</xsl:template>	
</xsl:stylesheet>

