<?xml version="1.0" encoding="utf-8"?>
<!-- security.xsl, for Smartform Admin -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" indent="no" />

    <xsl:template match="securityerror">


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
          
    </xsl:template>
</xsl:stylesheet>
