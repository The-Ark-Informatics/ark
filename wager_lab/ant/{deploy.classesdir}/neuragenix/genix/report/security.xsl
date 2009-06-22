<?xml version="1.0" encoding="utf-8"?>
<!-- security.xsl, Report channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    
    <xsl:template match="securityerror">

    <form action="{$baseActionURL}?current=security" method="post">        
        <!-- Back button -->
        <table width="100%">
        <tr>
            <td width="100%"></td>
            <td class="uportal-label">
                <input type="submit" name="Back" value="Back" class="uportal-button" />
            </td>
        </tr>    
        </table>    

    
        <!-- Title of Channel -->
        <table width="100%">
          <tr><td class="uportal-channel-subtitle">Security Message<br/><hr/></td></tr>
        </table>



            <!-- Form Fill -->        
            <table width="100%">
                <tr>
                    <td class="neuragenix-form-required-text" width="50%">
                            <xsl:value-of select="errorstring" />
                            <br />
                            <xsl:value-of select="errortext" />
                            <br />
                            <xsl:value-of select="errordata" />
                            <br />
                            <xsl:value-of select="strErr" />
                    </td>

                 </tr>
            </table> 
       </form>  
    </xsl:template>
</xsl:stylesheet>
