<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./report_menu.xsl"/>
    <xsl:include href="./report_list.xsl"/>
    
    <xsl:output method="html" indent="no" />
    
    <xsl:template match="report">
        <xsl:param name="REPORTCATEGORY_intReportCategoryKey"><xsl:value-of select="REPORTCATEGORY_intReportCategoryKey" /></xsl:param>
        <xsl:param name="REPORTCATEGORY_strReportCategoryName"><xsl:value-of select="REPORTCATEGORY_strReportCategoryName" /></xsl:param>
        <xsl:param name="REPORTCATEGORY_strReportCategoryDesc"><xsl:value-of select="REPORTCATEGORY_strReportCategoryDesc" /></xsl:param>
    
        <table width="100%">
            <tr>
                <td class="uportal-channel-subtitle">
                    View report category<br/><hr/>
                </td>
            </tr>
        </table>

        <table width="100%">
            <tr>
                <td class="uportal-label">
                    <a href="{$baseActionURL}?current=report_category_add">Add new category</a>
                    |
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

        <form action="{$baseActionURL}?current=report_category_view" method="post">
        <table width="100%">
            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORTCATEGORY_strReportCategoryNameDisplay" />:
                </td>
                <td width="80%">
                    <input type="text" name="REPORTCATEGORY_strReportCategoryName" value="{$REPORTCATEGORY_strReportCategoryName}" size="61" tabindex="1" class="uportal-input-text" />
                </td>
            </tr>

            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORTCATEGORY_strReportCategoryDescDisplay" />:
                </td>
                <td width="80%">
                    <textarea name="REPORTCATEGORY_strReportCategoryDesc" rows="4" cols="60" tabindex="2" class="uportal-input-text">
                        <xsl:value-of select="REPORTCATEGORY_strReportCategoryDesc" />
                    </textarea>
                </td>
            </tr>
        </table>
        <hr />
        <table width="100%">
            <tr>	
                <td width="20%">
                    <input type="submit" name="save" tabindex="3" value="Save" class="uportal-button" />
                    <input type="button" name="delete" value="Delete" tabindex="4" class="uportal-button" onclick="javascript:confirmDelete('{$baseActionURL}?current=report_category_view&amp;REPORTCATEGORY_intReportCategoryKey={$REPORTCATEGORY_intReportCategoryKey}')" />
                </td>
                <td width="80%"></td>
            </tr>
        </table>
        
        <input type="hidden" name="REPORTCATEGORY_intReportCategoryKey" value="{$REPORTCATEGORY_intReportCategoryKey}" />
        </form>
       
    </xsl:template>

</xsl:stylesheet>