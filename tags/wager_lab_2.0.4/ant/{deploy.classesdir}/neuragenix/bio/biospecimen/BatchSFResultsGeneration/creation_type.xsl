<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./header.xsl"/>
    <xsl:include href="./common_javascript.xsl"/>
    
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>   
    <xsl:variable name="backLocation"><xsl:value-of select="backLocation"/></xsl:variable> 
    <xsl:template match="body">         
        <xsl:call-template name="common_javascript"/>           
        <xsl:call-template name="header">
                <xsl:with-param name="activeSubtab">creation</xsl:with-param>
                <xsl:with-param name="previousButtonUrl"><xsl:value-of select="$baseActionURL"/><xsl:value-of select="backLocation"/></xsl:with-param>
                <xsl:with-param name="previousButtonFlag">true</xsl:with-param>
        </xsl:call-template>    
            
        

        <xsl:variable name="rdBatchResultsSelect"><xsl:value-of select="BatchResultSelection"/></xsl:variable>
        
        <table width="100%">
            <tr>
                <td class="uportal-label">
                    1. Please select your Creation Type
                </td>
            </tr>            
        </table>
        <br/>
        
        <form action="{$baseActionURL}?module=BATCH_SFRESULTS_GENERATION&amp;action=generation_mode" method="post">        
            <table width="100%">
                <tr>
                    <td width="5%"></td>
                    <td class="" width="40%">
                        <img src="media/neuragenix/SingleToMultiple.jpg" border="0" width="327" height="175"/>                                                
                    </td>
                    <td width="5%"></td>
                    <td class="" width="40%">
                        <img src="media/neuragenix/MultipleToSingle.jpg" border="0" width="327" height="175"/>                                                
                    </td>

                </tr>
             </table>   
             <table width="100%">
                <tr>    
                    <td width="5%"></td>
                    <td class="" width="2%">
                        <input type="RADIO" name="rdBatchResultsSelect" value="SingleToMultiple">
                             <xsl:if test="$rdBatchResultsSelect = 'SingleToMultiple'">
                                <xsl:attribute name="checked">1</xsl:attribute>                              
                             </xsl:if>
                        </input>
                    </td>
                    <td width="38%" class="uportal-label">
                       Single result to multiple biospecimens
                    </td>

                    <td width="5%"></td>
                    <td class="" width="2%">
                        <input type="RADIO" name="rdBatchResultsSelect" value="MultipleToSingle">
                             <xsl:if test="$rdBatchResultsSelect = 'MultipleToSingle'">
                                <xsl:attribute name="checked">1</xsl:attribute>                              
                             </xsl:if>
                        </input>
                    </td>
                    <td width="38%" class="uportal-label">
                       Multiple results to single biospecimen
                    </td>            
                </tr>
            </table> 

            <br/>
            <hr/>

            <table width="100%">
                <tr>
                    <td align="left">
                            <input type="button" name="Cancel" value="Cancel" class="uportal-button" onclick="javascript:jumpTo('{$baseActionURL}{backLocation}')"/>        
                    </td>
                    <td align="right">
                            <input type="submit" name="Next" value="Next" class="uportal-button"/>        
                    </td>
                </tr>
            </table>
        </form>
               
    </xsl:template>
</xsl:stylesheet>
