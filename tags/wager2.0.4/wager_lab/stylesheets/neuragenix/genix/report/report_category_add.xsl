<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./report_menu.xsl"/>
    <xsl:include href="./report_list.xsl"/>
    
    <xsl:output method="html" indent="no" />
    
    <xsl:template match="report">
    
    
        <table width="100%">
            <tr>
                <td class="uportal-channel-subtitle">
                    Add report category<br/><hr/>
                </td>
            </tr>
        </table>

        <table width="100%">
            <tr>
                <td class="uportal-label">
                    <a href="{$baseActionURL}?current=report_add">Add new report</a>
                </td>
            </tr>
        </table>
        
        <table width="100%">
            <tr>
                <td class="neuragenix-form-required-text">
                    <xsl:value-of select="strErrorMessage" />
                </td>
            </tr>
        </table>

        <form action="{$baseActionURL}?current=report_category_add" method="post">
        <table width="100%">
            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORTCATEGORY_strReportCategoryNameDisplay" />:
                </td>
                <td width="80%">
                    <input type="text" name="REPORTCATEGORY_strReportCategoryName" size="61" tabindex="1" class="uportal-input-text" />
                </td>
            </tr>

            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORTCATEGORY_strReportCategoryDescDisplay" />:
                </td>
                <td width="80%">
                    <textarea name="REPORTCATEGORY_strReportCategoryDesc" rows="4" cols="60" tabindex="2" class="uportal-input-text" />
                </td>
            </tr>
        </table>
        <hr />
        <table width="100%">
            <tr>	
                <td width="20%">
                    <input type="submit" name="save" tabindex="3" value="Save" class="uportal-button" />
                    <input type="button" name="clear" value="Clear" tabindex="4" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=report_category_add')" />
                </td>
                <td width="80%"></td>
            </tr>
        </table>
        </form>
       
    </xsl:template>

</xsl:stylesheet>