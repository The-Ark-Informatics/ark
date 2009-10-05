<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./tree_view.xsl"/>
    <xsl:output method="html" indent="no" />
    
  
        
<xsl:template match="proccess">

    <table width="100%">
    <tr>
        <td class="uportal-channel-subtitle">Delete group<hr/>
        </td>
    </tr>
  
    <tr>
        <td class="neuragenix-form-required-text"><xsl:value-of select="error"/>
        </td>
    </tr>

    </table>
 
    
    
</xsl:template>
    
    
 


       

</xsl:stylesheet>
