<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:param name="strErrorMessage">strErrorMessage</xsl:param>
    <xsl:param name="lastViewedID">lastViewedID</xsl:param>
    <xsl:param name="baseActionURL">baseActionURL</xsl:param>
    <xsl:template match="/">
        <table width="100%">
            <tr align="right">

                    <td width="100%" style="text-align: right">
                        <!--back button -->
                        <form name="back_form" action="{$baseActionURL}?uP_root=root" method="POST">
                            <input type="hidden" name="action" value="view_biospecimen"/>
                            <input type="hidden" name="module" value="core"/>
                            <input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
                                value="{$lastViewedID}"/>
                            <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
                                alt="Previous" onclick="javascript:document.back_form.submit();"/>
                            <img border="0" src="media/neuragenix/buttons/next_disabled.gif"
                                alt="Next"/>
                        </form>
                        <!-- end of back button -->
 
                    </td>
                </tr>
            <tr>
                <td class="neuragenix-form-required-text" width="50%">
                    <xsl:value-of select="$strErrorMessage"/>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
