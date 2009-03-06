<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" indent="no" />
<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>

    <xsl:output method="html" indent="no" />

    <xsl:template match="/">


        <!-- Title of Channel -->
        <table width="100%">
          <tr><td class="uportal-channel-subtitle">Security Error.<br/><hr/></td></tr>
        </table>

        <!-- Error messages -->
        <table width="100%">
            <tr>
                <td class="neuragenix-form-required-text" width="50%">
					Error! You do not have permission to perform this action
                </td>
            </tr>
            <tr>
                <td class="neuragenix-form-required-text" width="50%">
                <a href="{$baseActionURL}">Click here</a> to go back.
				</td>
            </tr>
 
        </table> 
    </xsl:template>
</xsl:stylesheet>
