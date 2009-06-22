<?xml version="1.0" encoding="utf-8"?>
<!-- locking_error.xsl, Integration channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./integration_menu.xsl"/>
    <xsl:output method="html" indent="no" />

    <xsl:template match="lock_error">


        <!-- Title of Channel -->
        <table width="100%">
          <tr><td class="uportal-channel-subtitle">Locking Message<br/><hr/></td></tr>
        </table>

        <!-- Form Fill -->
        <form action="{$baseActionURL}?current=locking_error" method="post">
        <!-- Error messages -->
        <table width="100%">
            <tr>
                <td class="neuragenix-form-required-text" width="50%">
                    The records you looking for are currently not available to view. Please try again later.
                </td>
            </tr>
        </table> 
        </form>        
    </xsl:template>
</xsl:stylesheet>
