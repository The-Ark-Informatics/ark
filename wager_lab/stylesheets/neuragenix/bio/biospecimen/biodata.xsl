<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
      xmlns:xs="http://www.w3.org/2001/XMLSchema"
      exclude-result-prefixes="xs"
      version="2.0">
    <xsl:param name="numcolumns" select="2" /> 
    <xsl:output method="xml" encoding="UTF-8"/>
    <xsl:template match="group">

   <fieldset>
   <legend><xsl:value-of select="@name"/></legend>
   <table width="100%">
   <tr>
					<td width="50%" align="left"> <!-- First column -->
						<table width="100%" cellspacing="4" cellpadding="0" border="0">
						<xsl:for-each select="field[((position() - 1) mod 2) = 0]">
							<xsl:call-template name="fields"/>
						</xsl:for-each>
    					</table>
    				</td>
    				<td width="5%" />
    				<td width="50%" align="left"> <!-- Second column -->
						<table width="100%" cellspacing="4" cellpadding="0" border="0">
						<xsl:for-each select="field[((position() - 1) mod 2) = 1]">
					<xsl:call-template name="fields"/>
						</xsl:for-each>
    					</table>
    				</td>
    	</tr>
    	</table>
    	</fieldset>
    </xsl:template>
    
    <xsl:template match="bioform">
 <xsl:apply-templates/>
    	
     
    </xsl:template>
    <xsl:template name="fields">
     <tr>
       
                 <xsl:variable name="field_key"><xsl:value-of select="key"/></xsl:variable>
                        <xsl:variable name="dojoType">
                            <xsl:choose>
                                <xsl:when test="type='date'">
                                  <xsl:text>dijit.form.DateTextBox</xsl:text>  
                                </xsl:when>
                                <xsl:when test="type='dropdown'">
                                    <xsl:text>dijit.form.ComboBox</xsl:text>  
                                </xsl:when>
                          
                                <xsl:when test="type='number'">
                                    <xsl:text>dijit.form.NumberTextBox</xsl:text>  
                                </xsl:when>
                                <xsl:when test="type='string'">
                                <xsl:text>dijit.form.TextBox</xsl:text>
                                </xsl:when>
                                <xsl:otherwise></xsl:otherwise>
                            </xsl:choose>
                            
                        </xsl:variable>
                        <td class="form_label" width="40%"><xsl:value-of select="label"/></td><td/>
                        
                    <td class="uportal-label" width="60%">
            
            
                    <xsl:choose>
                            <xsl:when test="type='dropdown'">
                                <select dojoType="{$dojoType}" name = "field_{$field_key}">
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
                                <input dojoType="{$dojoType}" name="field_{$field_key}" value="{$fieldvalue}"/>            
                            </xsl:otherwise>
                            </xsl:choose>
                            
                            </td>
                             
                
            </tr>
    
    </xsl:template>
    
    </xsl:stylesheet>