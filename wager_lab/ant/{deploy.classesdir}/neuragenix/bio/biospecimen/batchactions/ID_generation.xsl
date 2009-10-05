<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:template match="batch_creation">
        <table width="100%">
            <tr>
                <td width="20%"></td>
                <td widht="80%">
                    
                        <table width="100%">
                            <tr>
                                <td class="uportal-channel-subtitle" colspan="6">Batch
                                    Creation Step 3: Generate ID</td>
                                <td align="right" colspan="6">
                                    <form name="back_form" action="{$baseActionURL}" method="POST">
                                        <!-- back to the parent biospecimen -->
                                        
                                        <input type="hidden" name="module"
                                            value="batch_creation"/>
                                        <input type="hidden" name="action"
                                            value="start_inventory_allocation"/>
                                                                            </form>
                                    
                                    <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
                                        alt="Previous" onclick="javascript:document.back_form.submit();"/>
                                    <img border="0" src="media/neuragenix/buttons/next_enabled.gif"
                                        alt="Next" onclick="javascript:document.id_form.submit();"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="12"><hr/></td>
                            </tr>
                            
                            <form name="id_form" action="{$baseActionURL}" method="post">  
                            <!-- this row for the table reader-->
                            <tr>
                                <td width="3%">Order</td>
                                <td width="10%" class="neuragenix-form-row-input-label">
                                    <xsl:value-of select="BIOSPECIMEN_strSampleTypeDisplay"/>
                                </td>
                                <td width="10%" class="neuragenix-form-row-input-label">
                                    <xsl:value-of select="BIOSPECIMEN_strSampleSubTypeDisplay"/>
                                </td>
                                <td width="10%" class="neuragenix-form-row-input-label"> Quantity </td>
                                <td width="10%" class="neuragenix-form-row-input-label"> ID </td>
                                <td width="10%" class="neuragenix-form-row-input-label">Message</td>
                            </tr>
                            <!-- existing generation information -->
                            <xsl:for-each select="generation">
                                <xsl:for-each select="biospecimen">
                                    <tr>
                                        <!--determine the color of the line !-->
                                        <xsl:variable name="classIDNumber">
                                            <xsl:value-of
                                                select="../GenerationDetails/GD_internalID"/>
                                        </xsl:variable>
                                        <xsl:choose>
                                            <xsl:when test="$classIDNumber mod 2 = 0">
                                                <xsl:attribute name="class"
                                                >uportal-input-text</xsl:attribute>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:attribute name="class"
                                                >uportal-text</xsl:attribute>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                        <td class="uportal-input-test">
                                            <!-- only print out when the first index -->
                                            <xsl:if test="internalIndex='0'">
                                                <xsl:value-of select="$classIDNumber+1"/>
                                            </xsl:if>
                                        </td>
                                        <td>
                                            <xsl:value-of
                                                select="../GenerationDetails/BIOSPECIMEN_strSampleType_Selected"
                                            />
                                        </td>
                                        <td>
                                            <xsl:value-of
                                                select="../GenerationDetails/BIOSPECIMEN_strSampleSubType_Selected"
                                            />
                                        </td>
                                        <td>
                                            <xsl:value-of
                                                select="../GenerationDetails/BIOSPECIMEN_TRANSACTIONS_flQuantity"/>
                                            <xsl:value-of
                                                select="../GenerationDetails/BIOSPECIMEN_TRANSACTIONS_strUnit_Selected"
                                            />
                                        </td>
                                        <td>
                                            <xsl:choose>
                                                <xsl:when test="./@autoIDGeneration != 'true'">
                                                  <input>
                                                  <xsl:attribute name="name">
                                                  <xsl:value-of select="id-key"/>
                                              </xsl:attribute>
                                                 <xsl:attribute name="value">
                                                     <xsl:value-of select="id-value"/>
                                              </xsl:attribute>

                                                  </input>
                                                </xsl:when>
                                                
                                                <xsl:otherwise> System Generated </xsl:otherwise>
                                            </xsl:choose>
                                        </td>
                                        <td
                                            class="neuragenix-form-required-text" >
                                            <xsl:value-of select="id-message"/>
                                        </td>
                                    </tr>
                                </xsl:for-each>
                            </xsl:for-each>
                            <tr>
                                <td colspan="12">
                                    <hr/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="12" align="right">
                                    <input type="button" value="back" class="uportal-button" onclick="javascript:document.back_form.submit();"/>
                                    <input type="submit" class="uportal-button" value="Save and Generate Report"/>
                                    <input type="hidden" name="action" value="show_summary"/>
                                    <input type="hidden" name="module" value="Batch_creation"/>
                                </td>
                            </tr>
                            </form>
                        </table>
                    
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
