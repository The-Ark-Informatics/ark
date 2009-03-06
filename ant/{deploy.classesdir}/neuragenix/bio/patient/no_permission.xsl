<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    
    <xsl:output method="html" indent="no" />

    <xsl:template match="/body">


        <!-- Title of Channel -->
        <table width="100%">
          <tr><td class="uportal-channel-subtitle">Security Message<br/><hr/></td></tr>
        </table>

        
        <!-- Error messages -->
        <table width="100%">
            <tr>
                <td class="neuragenix-form-required-text" width="50%">
                        You do not have permission to view this channel.
                        
                </td>
            </tr>
        </table> 
               
    </xsl:template>
</xsl:stylesheet>
