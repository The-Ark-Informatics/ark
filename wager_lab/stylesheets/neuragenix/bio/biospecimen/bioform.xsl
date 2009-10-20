<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
      xmlns:xs="http://www.w3.org/2001/XMLSchema"
      exclude-result-prefixes="xs"
      version="2.0">
    
    
    <xsl:template match="bioform">
        
        <table width="100%">
            <xsl:for-each select="fieldset">
                <tr>
                    
                    <xsl:for-each select="field">
                        <xsl:variable name="field_key"><xsl:value-of select="key"/></xsl:variable>
                        <xsl:variable name="dojoType">
                            <xsl:choose>
                                <xsl:when test="type='date'">
                                  <xsl:text>dijit.form.DateTextBox</xsl:text>  
                                </xsl:when>
                            </xsl:choose>
                            <xsl:choose>
                                <xsl:when test="type='dropdown'">
                                    <xsl:text>dijit.form.ComboBox</xsl:text>  
                                </xsl:when>
                            </xsl:choose>
                            <xsl:choose>
                                <xsl:when test="type='number'">
                                    <xsl:text>dijit.form.NumberTextBox</xsl:text>  
                                </xsl:when>
                                <xsl:otherwise></xsl:otherwise>
                            </xsl:choose>
                            
                        </xsl:variable>
                        <td>
                        <label><xsl:value-of select="label"/></label>
                        
                            
                        </td>
                        <td>
                            <xsl:choose>
                            <xsl:when test="type='dropdown'">
                                <select dojoType="{$dojoType}" id = "field_{$field_key}">
                                    <xsl:for-each select="value">
                                        <option>
                                            <xsl:if test="@selected">
                                                <xsl:attribute name="selected">1</xsl:attribute>
                                            </xsl:if>
                                            <xsl:value-of select="."/>
                                        
                                        </option>
                                    </xsl:for-each>
                                    
                                </select>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:variable name="fieldvalue"><xsl:value-of select="value"/></xsl:variable>
                                <input dojoType="{$dojoType}" id ="field_{$field_key}" value="{$fieldvalue}"></input>
                   
                            </xsl:otherwise>
                            </xsl:choose>
                        </td>
                        
                        
                    </xsl:for-each>
                    
                    
                    
                    
                </tr>
                
                
                
                
            </xsl:for-each>
      </table>
        
    </xsl:template>

</xsl:stylesheet>
