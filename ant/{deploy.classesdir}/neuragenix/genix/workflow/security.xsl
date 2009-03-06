<?xml version="1.0" encoding="utf-8"?>
<!-- security.xsl, Microarray channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./workflow_menu.xsl"/>
	<xsl:output method="html" indent="no" />
	<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>

    <xsl:template match="securityerror">


        <!-- Title of Channel -->
        <table width="100%">
          <tr><td class="uportal-channel-subtitle">Security Message<br/><hr/></td></tr>
        </table>

        <!-- Form Fill -->
        <form action="{$baseActionURL}?current=security" method="post">
        <!-- Error messages -->
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
