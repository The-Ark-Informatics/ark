<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    
<xsl:output method="html" indent="no" />
  
<xsl:template match="/">
    <xsl:apply-templates/>
</xsl:template>    
<xsl:template match="info">

<xsl:param name="downloadURL"><xsl:value-of select="downloadURL" /></xsl:param>
    <table width="100%">
    <tr>
        <td class="uportal-label" valign="center">
           Please wait while we connect to the file server...<br/>
           Close this window to go back.
	<META HTTP-EQUIV="Refresh" CONTENT="1;URL={$downloadURL}"/>   
        </td>
    </tr><!--xsl:value-of select="detail"/-->
    <!--tr>
        <td  class="uportal-label"  valign="center">
           <a href="#" onclick="history.back()">Click here</a> to go back...
	   
        </td>
    </tr-->
    
<tr>
<td>
	
</td>

</tr>
    </table>
 
    
    
</xsl:template>
    

</xsl:stylesheet>
