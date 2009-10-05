<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./tree_view.xsl"/>
    <xsl:output method="html" indent="no" />
    
<xsl:template match="proccess">

    <table width="100%">
    <tr>
        <td class="uportal-channel-subtitle">Add new sub group<hr/>
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

<xsl:variable name="parentId"><xsl:value-of select="USERGROUP_intParentKey" /></xsl:variable>	
<xsl:variable name="strName"><xsl:value-of select="USERGROUP_strUsergroupName" /></xsl:variable>
<xsl:variable name="strDescription"><xsl:value-of select="USERGROUP_strDescription" /></xsl:variable>
	
        <form action="{$baseActionURL}?action=proccess_group&amp;add_sub_group=true" method="post">
        <table width="100%">
	         
        <tr>
                    <td id="neuragenix-form-row-label" class="uportal-label" width="20%">
                    <xsl:value-of select="USERGROUP_strUsergroupNameDisplay"/>: </td>
                    <td><input class="uportal-input-text" type="text" name="USERGROUP_strUsergroupName" size="20"  
                    value="{$strName}"/>
                    </td>
                    <input type="hidden" name="USERGROUP_intParentKey" value="{$parentId}"  />
                    
        </tr>   
        <tr>
                    <td id="neuragenix-form-row-label" class="uportal-label" width="20%">
                    <xsl:value-of select="USERGROUP_strDescriptionDisplay"/>: </td>
                    <td><input class="uportal-input-text" type="text" name="USERGROUP_strDescription" size="40"  
                    value="{$strDescription}"/>
                    </td>
                   
        </tr>
        
        <tr><td colspan="2"><p></p></td>
        </tr>
        <tr>
            <td colspan="2" align="left">
                <input type="submit" name="add_new_sub_group" tabindex="16" value="Add" class="uportal-button" />
                
                <input type="button" name="cancel" value="Cancel" tabindex="17" class="uportal-button"
                onclick="javascript:history.back()"/>
                
             
               
                </td>
        </tr>
        
        </table>
        </form>
     
</xsl:template>	
</xsl:stylesheet>

