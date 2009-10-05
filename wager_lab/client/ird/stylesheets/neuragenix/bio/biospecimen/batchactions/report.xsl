<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:template match="batch_creation">
        <!--main table-->
        <table width="100%">
            <tr>
                <td width="20%"></td>
                <td widht="80%">
                    
                    
                    <table width="100%">
                        <tr>
                            <td class="uportal-channel-subtitle" colspan="2">Batch
                                Creation Batch - Step 4: Report Generation</td>
                            <td align="right" colspan="4">
                                <form name="back_form" action="{$baseActionURL}" method="POST">
                                    <!-- back to the parent biospecimen -->
                                    
                                    <input type="hidden" name="module"
                                        value="batch_creation"/>
                                    <input type="hidden" name="action"
                                        value="start_inventory_allocation"/>
                                </form>
                                
                                <img border="0" src="media/neuragenix/buttons/previous_disabled.gif"
                                    alt="Previous"/>
                                <img border="0" src="media/neuragenix/buttons/next_enabled.gif"
                                    alt="Next" onclick="javascript:document.back_to_biospecimen.submit();"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="12"><hr/></td>
                        </tr>
                        
                        
                        <!-- existing generation information -->
                        <xsl:for-each select="generation">
                            <tr>
                                <td valign="top" width="30%">
                                    <table width="100%">
                                        <tr class="uportal-input-text" valign="top">
                                            <td width="40%" class="uportal-label">
                                                <xsl:value-of
                                                  select="../BIOSPECIMEN_strSampleTypeDisplay"/>:
                                            </td>
                                            <td>
                                                <xsl:value-of
                                                  select="./GenerationDetails/BIOSPECIMEN_strSampleType_Selected"
                                                />
                                            </td>
                                        </tr>
                                        <tr class="uportal-text">
                                            <td width="40%" class="uportal-label">
                                                <xsl:value-of
                                                  select="../BIOSPECIMEN_strSampleSubTypeDisplay"/>:
                                            </td>
                                            <td>
                                                <xsl:value-of
                                                  select="./GenerationDetails/BIOSPECIMEN_strSampleSubType_Selected"
                                                />
                                            </td>
                                        </tr>
                                        
                                        <tr class="uportal-input-text" valign="top">
                                            <td width="40%" class="uportal-label">
                                                Number:
                                            </td>
                                            <td>
                                                <xsl:value-of
                                                    select="./GenerationDetails/GD_AllocationAmount"
                                                    />
                                            </td>
                                        </tr>
                                        <tr class="uportal-text">
                                            <td width="40%" class="uportal-label">
                                                Quantity
                                            </td>
                                            <td>
                                                <xsl:value-of
                                                    select="./GenerationDetails/BIOSPECIMEN_TRANSACTIONS_flQuantity"
                                                    />
                                                <xsl:value-of
                                                    select="./GenerationDetails/BIOSPECIMEN_TRANSACTIONS_strUnit_Selected"
                                                    />   
                                            </td>
                                        </tr>
                                        
                                        
                                    </table>
                                    <br/>
                                    <table width="100%">
                                        <tr>
                                            <td width="50%" class="uportal-label"> Specimen ID </td>
                                            <td class="uportal-label"> Location </td>
                                        </tr>
                                        <xsl:for-each select="biospecimen">
                                            <tr>
                                                <xsl:variable name="key">
                                                    <xsl:value-of select="./@key"/>
                                                </xsl:variable>
                                                <xsl:choose>
                                                    <xsl:when test="$key mod 2 = 0">
                                                        <xsl:attribute
                                                            name="class" >
                                                            uportal-input-text">
                                                        </xsl:attribute>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute
                                                            name="class">
                                                            uportal-text
                                                        </xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>                                                

                                                
                                                <td width="50%">
                                                  <xsl:value-of select="id"/>
                                                </td>
                                                <td>
                                                  <xsl:value-of select="highlight_location"/>
                                                </td>
                                            </tr>
                                        </xsl:for-each>
                                    </table>
                                </td>
                                <!-- spacer-->
                                <td width="3%"/>
                                <!-- Tray image -->
                                <td width="40%">
                                    <table border="1">
                                        <xsl:for-each select="Row">
                                            <xsl:variable name="row">
                                                <xsl:value-of select="@number"/>
                                            </xsl:variable>
                                            <tr>
                                                <xsl:for-each select="Col">
                                                  <xsl:variable name="col">
                                                  <xsl:value-of select="@number"/>
                                                  </xsl:variable>
                                                  <xsl:variable name="label">
                                                  <xsl:value-of select="label"/>
                                                  </xsl:variable>
                                                  <td>
                                                  <xsl:choose>
                                                  <xsl:when test="$label='0'"/>
                                                  <xsl:when test="$label='-1'">
                                                  <xsl:variable name="CELL_intCellID">
                                                  <xsl:value-of
                                                  select="CELL_intCellID"/>
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_intBiospecimenID">
                                                  <xsl:value-of
                                                  select="CELL_intBiospecimenID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable
                                                  name="CELL_intPatientID">
                                                  <xsl:value-of
                                                  select="CELL_intPatientID"/>
                                                  </xsl:variable>
                                                  <xsl:variable name="CELL_info">
                                                  <xsl:value-of select="CELL_info"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intBiospecimenID
                                                  &gt;= 0">
                                                  <img
                                                  src="media/neuragenix/icons/used.gif"
                                                  border="0"
                                                  title="{$CELL_info}"/>
                                                  </xsl:when>
                                                      <xsl:when test="$CELL_intBiospecimenID =-1">
                                                          <img
                                                              src="media/neuragenix/icons/tobeused.gif"
                                                              border="0"
                                                              title="{$CELL_info}"/>
                                                          
                                                      </xsl:when>
                                                  <xsl:otherwise>
                                                  <img id="{$row}_{$col}"
                                                  src="media/neuragenix/icons/unused.gif"
                                                  border="0"
                                                  title="{$CELL_info}"/>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <xsl:value-of select="label"/>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </td>
                                                </xsl:for-each>
                                            </tr>
                                        </xsl:for-each>
                                    </table>
                                </td>
                                <!-- spacer -->
                                <td width="3%"/>
                                <!-- tray infor -->
                                <td valign="top" width="24%">
                                    <table width="100%">
                                        <tr valign="top" class="uportal-input-text">
                                            <td width="30%" class="uportal-label">
                                                <xsl:value-of select="../SITE_strTitleDisplay"/>
                                            </td>
                                            <td>
                                                <xsl:value-of
                                                  select="./GenerationDetails/SITE_strSiteName"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="30%" class="uportal-label">
                                                <xsl:value-of select="../TANK_strTitleDisplay"/>
                                            </td>
                                            <td>
                                                <xsl:value-of
                                                  select="./GenerationDetails/TANK_strTankName"/>
                                            </td>
                                        </tr>
                                        <tr class="uportal-input-text">
                                            <td width="30%" class="uportal-label">
                                                <xsl:value-of select="../BOX_strTitleDisplay"/>
                                            </td>
                                            <td>
                                                <xsl:value-of
                                                  select="./GenerationDetails/BOX_strBoxName"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="30%" class="uportal-label">
                                                <xsl:value-of select="../TRAY_strTitleDisplay"/>
                                            </td>
                                            <td>
                                                <xsl:value-of
                                                  select="./GenerationDetails/TRAY_strTrayName"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="12">
                                    <hr/>
                                </td>
                            </tr>
                        </xsl:for-each>
                        
                        <tr>
                            <td colspan="12" align="right">
                                <form name="back_to_biospecimen" action="{$baseActionURL}">
                                    <input type="submit" class="uportal-button" value="Back to Parent Biospecimen"/>
                                    <input type="hidden" name="action"
                                        value="view_biospecimen"/>
                                    <input type="hidden" name="module" value="core"/>
                                    <input type="hidden" name="BIOSPECIMEN_intBiospecimenID">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="parentKey"/>
                                        </xsl:attribute>
                                    </input>
                                </form>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
