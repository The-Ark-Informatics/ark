<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./header.xsl"/>
    <xsl:include href="./common_javascript.xsl"/>    
    
    <xsl:template match="body">
        <xsl:variable name="backLocation"><xsl:value-of select="backLocation"/></xsl:variable>
        <xsl:variable name="numSpecimens"><xsl:value-of select="numSpecimens"/></xsl:variable>
        <xsl:call-template name="common_javascript"/>
        <xsl:call-template name="header">
                <xsl:with-param name="activeSubtab">report</xsl:with-param>
                <xsl:with-param name="previousButtonFlag">false</xsl:with-param>
		<xsl:with-param name="previousButtonUrl"></xsl:with-param>
                <xsl:with-param name="nextButtonFlag">true</xsl:with-param>
		<xsl:with-param name="nextButtonUrl"><xsl:value-of select="$baseActionURL"/><xsl:value-of select="backLocation"/></xsl:with-param>
                
        </xsl:call-template>        
        
        <xsl:variable name="error"><xsl:value-of select="error" /></xsl:variable>
         <img src="media/neuragenix/ObservationNotes_Top_Panel.jpg" border="0" />
         <table width="804"  border="0" cellspacing="0" cellpadding="2" background="media/neuragenix/ObservationNotes_Mid_Panel.jpg">    

            <tr height="15">
                <td colspan="3" class="uportal-label" width="1%">
                </td>
                <td   class="uportal-label">
                </td>
            </tr>

            <tr height="15">
                <td class="uportal-label" width="1%">
                </td>
                <td  colspan="2" class="uportal-label">
                    <xsl:value-of select="ReportMessage"/>
                </td>
            </tr>
            
            
         
            <xsl:for-each select="participant">
            <tr height="15">
                <td class="uportal-label" width="1%">
                </td>
                <td class="uportal-label" width="1%">
                    <xsl:choose>
                    <xsl:when test="(string-length( $error ) > 0)">
                        <img src="media/neuragenix/cross.gif" border="0" width="15" height="15"/>                                                
                    </xsl:when>
                    <xsl:otherwise>
                        <img src="media/neuragenix/tick.gif" border="0" width="15" height="15"/>                                           
                    </xsl:otherwise>
                    </xsl:choose>
                </td>
                <td  class="uportal-label" align="left">
                    <xsl:value-of select="."/>
                </td>
            </tr> 
            </xsl:for-each>           

        </table>
        <img src="media/neuragenix/ObservationNotes_Graphic_Panel.jpg" border="0" />
        <br/>
        <hr/>
        
        <table width="100%">
            <tr>
                <td align="left">
                        <input type="button" name="Finish" value="Finish" class="uportal-button" onclick="javascript:jumpTo('{$baseActionURL}{backLocation}')"/>        
                </td>                                
            </tr>
        </table>
               
    </xsl:template>
</xsl:stylesheet>
