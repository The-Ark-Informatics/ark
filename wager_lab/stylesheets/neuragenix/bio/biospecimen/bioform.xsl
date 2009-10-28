<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
      xmlns:xs="http://www.w3.org/2001/XMLSchema"
      exclude-result-prefixes="xs"
      version="2.0">
    <xsl:param name="numcolumns" select="2" /> 
    <xsl:output method="html" encoding="UTF-8"/>
    <xsl:template match="bioform">
      


                          <xsl:for-each select="field[((position() - 1) mod $numcolumns) = 0]">
            <tr>
        <td id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text">&#160;</td>
                <xsl:for-each select=". | following-sibling::*[position() &lt; $numcolumns]">
                <xsl:if test="position() mod 2 = 0">
                <td width="5%"/>
                <td id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text" valign="top"/>
                </xsl:if>
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
                        <td id="neuragenix-form-row-input-label" class="uportal-label"><xsl:value-of select="label"/></td>
                    <td class="uportal-label">
            <xsl:choose>
            <xsl:when test="position() mod 2 = 0">
            <xsl:attribute name="width">25%</xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
            <xsl:attribute name="id">neuragenix-form-row-input</xsl:attribute>
            </xsl:otherwise>
            
            </xsl:choose>
                    <xsl:choose>
                            <xsl:when test="type='dropdown'">
                                <select dojoType="{$dojoType}" id = "field_{$field_key}">
                                    <xsl:for-each select="values/value">
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
                            </xsl:choose></td>
                             <xsl:if test="position() mod 2 = 0">
                <td />
                </xsl:if>
                </xsl:for-each>
            </tr>
        </xsl:for-each> 
                  
                  

        
    </xsl:template>

     

</xsl:stylesheet>
