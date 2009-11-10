<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./report_menu.xsl"/>
    <xsl:include href="./report_list.xsl"/>
    
    <xsl:output method="html" indent="no" />
    
    <xsl:template match="report">
        <xsl:param name="REPORT_strReportName"><xsl:value-of select="REPORT_strReportName" /></xsl:param>
        <xsl:param name="REPORT_intReportKey"><xsl:value-of select="REPORT_intReportKey" /></xsl:param>       
        <xsl:param name="REPORT_strReportDesc"><xsl:value-of select="REPORT_strReportDesc" /></xsl:param>
               
    
        <table width="100%">
            <tr>
                <td class="uportal-channel-subtitle">
                    View report<br/><hr/>
                </td>
            </tr>
        </table>

        <table width="100%">
            <tr>
                <td class="uportal-label">
                    <a href="{$baseActionURL}?uP_root=root&amp;current=report_category_add">Add new category</a>
                    |
                    <a href="{$baseActionURL}?uP_root=root&amp;current=report_add">Add new report</a>
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

        <form action="{$baseActionURL}?uP_root=root&amp;current=report_view" method="post">
        <table width="100%">
            
            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORT_strReportNameDisplay" />:
                </td>
                <td width="80%" class="uportal-label">
                    <xsl:value-of select="REPORT_strReportName"/>                
                </td>
            </tr>
            
            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORT_strReportDescDisplay" />:
                </td>
                <td width="80%" class="uportal-label">
                <xsl:value-of select="REPORT_strReportDesc"/>
                </td>
            </tr>
        </table> 
            

        <table width="100%"> 
        <xsl:if test="string-length(search_report_parameters) > '0'">
            <tr>               
                <td class="uportal-channel-subtitle">
                    Parameters 
                </td> <hr/>
            </tr>
                        
            <xsl:for-each select="search_report_parameters">
                <xsl:variable name="varReportParameterSchemaName"><xsl:value-of select="REPORTPARAMETERS_strSchemaName"/></xsl:variable>
                <tr>
                    <td width="20%" class="uportal-label">
                        <xsl:value-of select="REPORTPARAMETERS_strName" /> :
                    </td>
                    
                    <td width="80%">
                        <xsl:choose>
                            <xsl:when test="REPORTPARAMETERS_strType != 'Date'">
                                <input type="text" name="REPORTPARAMETER_{$varReportParameterSchemaName}" tabindex="1" class="uportal-input-text" />                            
                                    <xsl:if test="REPORTPARAMETERS_strAllowOperator='Y'">
                                        <Font class="uportal-label">
                                           Operator :
                                        </Font>

                                        <select class="uportal-input-text" tabindex="2" name="REPORTPARAMETERS_strOperator">
                                            <option value="&lt;">&lt;</option>
                                            <option value="&lt;=">&lt;=</option>
                                            <option value="&gt;=">&gt;=</option>
                                            <option value="&gt;">&gt;</option>
                                            <option value="=">=</option>
                                        </select>
                                    </xsl:if>
                            </xsl:when>
                            <xsl:otherwise>
                                    <select name="REPORTPARAMETERS_dtDate_Day_{$varReportParameterSchemaName}" tabindex="3" class="uportal-input-text">
                                        <xsl:for-each select="REPORTPARAMETERS_dtDate_Day">
                                            <option>
                                                    <xsl:attribute name="value">
                                                    <xsl:value-of select="." />
                                                    </xsl:attribute> 
                                                    <xsl:if test="@selected=1">
                                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                                    </xsl:if>

                                                    <xsl:value-of select="." />		
                                                </option>
                                        </xsl:for-each>
                                    </select>

                                    
                                    <select name="REPORTPARAMETERS_dtDate_Month_{$varReportParameterSchemaName}" tabindex="4" class="uportal-input-text">
                                        <xsl:for-each select="REPORTPARAMETERS_dtDate_Month">
                                            <option>
                                                    <xsl:attribute name="value">
                                                    <xsl:value-of select="." />
                                                    </xsl:attribute> 
                                                    <xsl:if test="@selected=1">
                                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                                    </xsl:if>

                                                    <xsl:value-of select="." />		
                                                </option>
                                        </xsl:for-each>
                                    </select>

                                    <input type="text" name="REPORTPARAMETERS_dtDate_Year_{$varReportParameterSchemaName}" size="4" tabindex="5" class="uportal-input-text">
                                            <xsl:attribute name="value">
                                                    <xsl:value-of select="REPORTPARAMETERS_dtDate_Year" />
                                            </xsl:attribute>
                                    </input>
                                    
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:for-each>    
        </xsl:if>
                    
        </table>
        <hr />
        <table width="100%">
            <tr>	
                <td width="20%">
                    <input type="submit" name="run" tabindex="6" value="Run" class="uportal-button" />
                    <input type="submit" name="edit" tabindex="7" value="Edit" class="uportal-button" />
                </td>
                <td width="80%"></td>
            </tr>
        </table>
        
        <input type="hidden" name="REPORT_intReportKey" value="{$REPORT_intReportKey}" />
        </form>
       
    </xsl:template>

</xsl:stylesheet>