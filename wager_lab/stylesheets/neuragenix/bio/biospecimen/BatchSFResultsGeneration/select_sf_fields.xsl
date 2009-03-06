<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./header.xsl"/>
    <xsl:include href="./common_javascript.xsl"/>
        
    <xsl:template match="body">        
        <xsl:variable name="backLocation"><xsl:value-of select="backLocation"/></xsl:variable>
        <xsl:call-template name="common_javascript"/>    
        <xsl:call-template name="header">
                <xsl:with-param name="activeSubtab">smartform</xsl:with-param>
                <xsl:with-param name="previousButtonUrl"><xsl:value-of select="$baseActionURL"/>?module=BATCH_SFRESULTS_GENERATION&amp;action=specimen_selection</xsl:with-param>
                <xsl:with-param name="previousButtonFlag">true</xsl:with-param>
                
        </xsl:call-template> 
        <xsl:variable name="varSelectedSF">
                    <xsl:value-of select="selectedSmartformID"/>
        </xsl:variable>                    
        <xsl:variable name="varSelectedSFName">
                    <xsl:value-of select="selectedSmartformName"/>
        </xsl:variable>                    
        
        
        <table width="100%">       
        <tr>
            <td class="neuragenix-form-required-text">
                <xsl:value-of select="ErrorMsg"/>
            </td>
        </tr>            
        </table>

        <form action="{$baseActionURL}?module=BATCH_SFRESULTS_GENERATION&amp;action=SelectSFFields" method="post">
            <table width="100%">
                <tr>
                    <td class="uportal-label">
                        Please select the fields you wish to remain the same
                    </td>
                </tr>            
            </table>
            <br/>
         
            <table width="100%" >
                <xsl:for-each select="Field">
                    <xsl:variable name="DEName"><xsl:value-of select="DEName" /></xsl:variable> 
                    <xsl:variable name="DEID"><xsl:value-of select="DEID" /></xsl:variable>
                    <tr>
                        <td width="5%">
                            <input type="checkbox"> 
                            <xsl:attribute name="name"><xsl:value-of select="DEID" /></xsl:attribute>                                                      
                               <!-- Show the checkboxes already selected -->
                               <xsl:for-each select="ListOfCheckedFields">                  
                                    <xsl:variable name="CheckedFields" select="DEID"/>
                                        <xsl:if test="$DEID = $CheckedFields">
                                            <xsl:attribute name="checked">true</xsl:attribute>     
                                        </xsl:if>
                               </xsl:for-each>                                                  
                            </input>  
                        </td>
                        <td width="40%" class="uportal-label">
                            <xsl:value-of select="DEName" />                                                      

                        </td>       
                        <td width="55%"></td>
                    </tr>
                </xsl:for-each>            
            </table>                                
         
            <br/>
            <table width="100%">
                <tr>
                    <td width="10%" class="uportal-label">
                        Enter number of clones:
                    </td>
                    <td width="4%" class="uportal-label">
                        <input type="text" align="left" name="NumberToClone" value="1" class="uportal-input-text"/>
                    </td>
                    <td width="86%">
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
                        <input type="submit" name="Next" value="Next" class="uportal-button"  />        
                </td>
            </tr>
        </table>
     </form>  
               
    </xsl:template>
</xsl:stylesheet>
