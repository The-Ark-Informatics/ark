<?xml version="1.0" encoding="utf-8"?>

<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./inventory_menu.xsl"/>
    <xsl:param name="formParams">current=change_tray</xsl:param>
    <xsl:output method="html" indent="no"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:param name="downloadURL">downloadURL_false</xsl:param>
    <xsl:param name="nodeId">nodeId_false</xsl:param>
    <xsl:param name="biospecimenChannelURL">biospecimenChannelURL_false</xsl:param>
    <xsl:param name="biospecimenTabOrder">biospecimenTabOrder</xsl:param>
    
    
    <xsl:template match="inventory">
        <xsl:param name="TRAY_intTrayID">
            <xsl:value-of select="TRAY_intTrayID"/>
        </xsl:param>
        <xsl:param name="strSource">
            <xsl:value-of select="strSource"/>
        </xsl:param>
        <xsl:param name="blLockError">
            <xsl:value-of select="blLockError"/>
        </xsl:param>

        <script language="javascript" >

            function printPage() {
                window.print();
            }
        </script>                
        
        <table width="100%">
            <tr valign="top">
                <td width="30%">
                    <table width="100%">
                        <xsl:call-template name="sort_selection"/>
                    </table>
               
                   
                    <table width="100%">
                        <xsl:apply-templates select="site">
                            <xsl:sort select="availbility" data-type="number" order="descending"/>
                        </xsl:apply-templates>
                    </table>
                    <br/>
                    <br/>
                    <br/>
                    <xsl:variable name="varAdminSection">
                        <xsl:value-of select="intAdminSection"/>
                    </xsl:variable>
                    <xsl:if test="$varAdminSection=1">
                        <xsl:call-template name="inventory_admin"/>
                    </xsl:if>
                </td>
                <td width="70%" style="text-align: center">
                    <form action="{$baseActionURL}?uP_root=root&amp;{$formParams}" method="post">
                        <table width="100%">
                            <tr>
                                <td colspan="5" class="uportal-channel-subtitle">
                                    Change cell locations<br/>
                                </td>
                                <td align="right">
                                    <input  type="submit" name="Back" value="Back" class="uportal-button" />
                                </td>
                            </tr>
                            <tr>
                                <td colspan="6">
                                    <hr/>
                                </td>
                            </tr>
                        </table>
                        <table width="100%">
                            <tr>
                                <td class="neuragenix-form-required-text">
                                    <xsl:value-of select="strErrorMessage"/>
                                </td>
                            </tr>
                        </table>
                        <table width="100%">
                            <xsl:if test="$blLockError = 'true'">
                                <tr>
                                    <td colspan="5" class="neuragenix-form-required-text"> This
                                        record is being viewed by other users. You cannot update it
                                        now. Please try again later. </td>
                                </tr>
                                <tr>
                                    <td height="10px"/>
                                </tr>
                            </xsl:if>
                        </table>
                        
                        
                        <table width="60%">
                            <tr>
                                <td width="15%" class="uportal-channel-table-header">
                                    Original Cell Location
                                </td>
                                <td width="15%" class="uportal-channel-table-header">                                
                                </td>
                                <td width="15%" class="uportal-channel-table-header">
                                </td>
                                <td width="15%" class="uportal-channel-table-header">
                                </td>                                
                            </tr>  
                            
                            <br/>                          
                            <tr>
                                <td width="15%" class="uportal-label">
                                    <xsl:value-of select="SITE_strTitleDisplay" />
                                </td>
                                <td width="15%" class="uportal-label">
                                    <xsl:value-of select="TANK_strTitleDisplay" />
                                </td>
                                <td width="15%" class="uportal-label">
                                    <xsl:value-of select="BOX_strTitleDisplay" />
                                </td>
                                <td width="15%" class="uportal-label">
                                    <xsl:value-of select="TRAY_strTitleDisplay" />
                                </td>                                
                            </tr>
                            
                            
                            <tr>
                                <td width="15%" class="uportal-text">
                                    <xsl:value-of select="strOldSiteName" />
                                </td>
                                <td width="15%" class="uportal-text">
                                    <xsl:value-of select="strOldTankName" />
                                </td>
                                <td width="15%" class="uportal-text">
                                    <xsl:value-of select="strOldBoxName" />
                                </td>
                                <td width="15%" class="uportal-text">
                                    <xsl:value-of select="strOldTrayName" />
                                </td>
                            </tr>
                            
                        </table>    
                        
                        <br/>
                        <table border="1">
                            <xsl:for-each select="fromTray">
                            <xsl:if test="$strSource = 'inventory'">
                                <xsl:for-each select="Row">
                                    <tr>
                                        <xsl:for-each select="Col">
                                            <xsl:variable name="label">
                                                <xsl:value-of select="label"/>
                                            </xsl:variable>
                                            <td>
                                                <xsl:choose>
                                                  <xsl:when test="$label='0'"/>
                                                  <xsl:when test="$label='-1'">
                                                    <xsl:variable name="CELL_intCellID">
                                                        <xsl:value-of select="CELL_intCellID"/>
                                                    </xsl:variable>
                                                    <xsl:variable name="CELL_intBiospecimenID">
                                                        <xsl:value-of select="CELL_intBiospecimenID"/>
                                                    </xsl:variable>
                                                    <xsl:variable name="CELL_intPatientID">
                                                        <xsl:value-of select="CELL_intPatientID"/>
                                                    </xsl:variable>
                                                    <xsl:variable name="CELL_info">
                                                        <xsl:value-of select="CELL_info"/>
                                                    </xsl:variable>
                                                    <xsl:choose>
                                                    <xsl:when test="$CELL_intBiospecimenID = '-1'">
                                                        <img src="media/neuragenix/icons/unused.gif" border="0" title="{$CELL_info}"/>
                                                    </xsl:when>
                                                    <xsl:otherwise>                                                  
                                                        <xsl:choose>
                                                        <xsl:when test="string-length(OldCell) &gt; 0">
                                                        <img src="media/neuragenix/icons/current.gif" border="0" title="{$CELL_info}" /> 
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                        <img src="media/neuragenix/icons/used.gif" border="0" title="{$CELL_info}" />                                                 
                                                        </xsl:otherwise>
                                                        </xsl:choose>
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
                            </xsl:if>
                        </xsl:for-each>
                        </table>
                        
                        <input type="hidden" name="TRAY_intTrayID" value="{$TRAY_intTrayID}"/>
                        <br/>
                        <br/>
                        <br/>
                        
                        <table width="60%">
                            <tr>
                                <td width="15%" class="uportal-channel-table-header">
                                    New Cell Location
                                </td>
                                <td width="15%" class="uportal-channel-table-header">                                
                                </td>
                                <td width="15%" class="uportal-channel-table-header">
                                </td>
                                <td width="15%" class="uportal-channel-table-header">
                                </td>                                
                            </tr>
                            
                            <br/>
                            <tr>
                                <td width="15%" class="uportal-label">
                                    <xsl:value-of select="SITE_strTitleDisplay" />
                                </td>
                                <td width="15%" class="uportal-label">
                                    <xsl:value-of select="TANK_strTitleDisplay" />
                                </td>
                                <td width="15%" class="uportal-label">
                                    <xsl:value-of select="BOX_strTitleDisplay" />
                                </td>
                                <td width="15%" class="uportal-label">
                                    <xsl:value-of select="TRAY_strTitleDisplay" />
                                </td>                                
                            </tr>
                            
                            
                            <tr>
                                <td width="15%" class="uportal-text">
                                    <xsl:value-of select="strNewSiteName" />
                                </td>
                                <td width="15%" class="uportal-text">
                                    <xsl:value-of select="strNewTankName" />
                                </td>
                                <td width="15%" class="uportal-text">
                                    <xsl:value-of select="strNewBoxName" />
                                </td>
                                <td width="15%" class="uportal-text">
                                    <xsl:value-of select="strNewTrayName" />
                                </td>
                            </tr>
                            
                        </table>    

                        <br/>                                                 
                        <table border="1">
                            <xsl:for-each select="toTray">
                            <xsl:if test="$strSource = 'inventory'">
                                <xsl:for-each select="Row">
                                    <tr>
                                        <xsl:for-each select="Col">
                                            <xsl:variable name="label">
                                                <xsl:value-of select="label"/>
                                            </xsl:variable>
                                            <td>
                                                <xsl:choose>
                                                  <xsl:when test="$label='0'"/>
                                                  <xsl:when test="$label='-1'">
                                                    <xsl:variable name="CELL_intCellID">
                                                        <xsl:value-of select="CELL_intCellID"/>
                                                    </xsl:variable>
                                                    <xsl:variable name="CELL_intBiospecimenID">
                                                        <xsl:value-of select="CELL_intBiospecimenID"/>
                                                    </xsl:variable>
                                                    <xsl:variable name="CELL_intPatientID">
                                                        <xsl:value-of select="CELL_intPatientID"/>
                                                    </xsl:variable>
                                                    <xsl:variable name="CELL_info">
                                                        <xsl:value-of select="CELL_info"/>
                                                    </xsl:variable>
                                                    <xsl:choose>
                                                    <xsl:when test="$CELL_intBiospecimenID = '-1'">
                                                        <xsl:choose>
                                                        <xsl:when test="string-length(NewCell) &gt; 0">
                                                        <img src="media/neuragenix/icons/tobeused.gif" border="0" title="{$CELL_info}" /> 
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                        <img src="media/neuragenix/icons/unused.gif" border="0" title="{$CELL_info}" />                                                 
                                                        </xsl:otherwise>
                                                        </xsl:choose>
                                                    </xsl:when>
                                                    <xsl:otherwise>                                                  
                                                        <img src="media/neuragenix/icons/used.gif" border="0" title="{$CELL_info}" />                                                 
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
                            </xsl:if>
                        </xsl:for-each>
                        </table>
                        
                        <table width="100%">
                            
                            <tr>
                                <td height="20px" colspan="2"></td>
                            </tr>
                            
                            <tr>
                                <td width="33%" class="uportal-label">
                                </td>
                                <td width="33%" class="uportal-channel-table-header">
                                    Original Cell Location
                                </td>
                                
                                <td width="33%" class="uportal-channel-table-header">
                                    New Cell Location
                                </td>
                                
                            </tr>
                            
                            
                            <tr>
                                <td width="33%" class="uportal-label">
                                    <xsl:value-of select="SITE_strTitleDisplay" /> name:
                                </td>
                                <td width="33%" class="uportal-text">
                                    <xsl:value-of select="strOldSiteName" />
                                </td>
                                <td width="33%" class="uportal-text">
                                    <xsl:value-of select="strNewSiteName" />
                                </td>
                                
                            </tr>
                            
                            <tr>
                                <td width="33%" class="uportal-label">
                                    <xsl:value-of select="TANK_strTitleDisplay" /> name:
                                </td>
                                <td width="33%" class="uportal-text">
                                    <xsl:value-of select="strOldTankName" />
                                </td>
                                <td width="33%" class="uportal-text">
                                    <xsl:value-of select="strNewTankName" />
                                </td>
                            </tr>
                            
                            <tr>
                                <td width="33%" class="uportal-label">
                                    <xsl:value-of select="BOX_strTitleDisplay" /> name:
                                </td>
                                <td width="33%" class="uportal-text">
                                    <xsl:value-of select="strOldBoxName" />
                                </td>
                                <td width="33%" class="uportal-text">
                                    <xsl:value-of select="strNewBoxName" />
                                </td>
                            </tr>
                            
                            <tr>
                                <td width="33%" class="uportal-label">
                                    <xsl:value-of select="TRAY_strTitleDisplay" /> name:
                                </td>
                                <td width="33%" class="uportal-text">
                                    <xsl:value-of select="strOldTrayName" />
                                </td>
                                <td width="33%" class="uportal-text">
                                    <xsl:value-of select="strNewTrayName" />
                                </td>
                            </tr>
                            
                            
                            <tr>
                                <td height="20px" colspan="2"></td>
                            </tr>
                        </table>
                        
                        <table width="100%">
                            <tr>
                                <td height="20px" colspan="2"></td>
                            </tr>
                            
                            <tr>
                                <td width="20%" class="uportal-channel-table-header">
                                    Sample ID
                                </td>
                                <td width="20%" class="uportal-channel-table-header">
                                    Original Cell ID
                                </td>
                                
                                <td width="20%" class="uportal-channel-table-header">
                                    Original Cell Location
                                </td>
                                
                                <td width="20%" class="uportal-channel-table-header">
                                    New Cell ID
                                </td>
                                
                                <td width="20%" class="uportal-channel-table-header">
                                    New Cell Location
                                </td>                                                                
                            </tr>
                            
                            <xsl:for-each select="cellLocationMapping">
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="strBiospecimenID" />
                                </td>
                                <td width="20%" class="uportal-text">
                                       <xsl:value-of select="strOldID" />
                                </td>
                                
                                <td width="20%" class="uportal-text">
                                       <xsl:value-of select="strOldLocation" />
                                </td>                                
                                
                                <td width="20%" class="uportal-text">
                                       <xsl:value-of select="strNewID" />
                                </td>
                                
                                <td width="20%" class="uportal-text">
                                       <xsl:value-of select="strNewLocation" />
                                </td>
                                
                            </tr>
                            </xsl:for-each>
                        </table>
                                                
                        <table width="100%">
                            <tr><td><hr /></td></tr>
                        </table>
                        
                        
                        <table width="100%">
                            <tr>
                                <td align="left">
                                    <input  type="submit" name="Cancel" value="Cancel" class="uportal-button" />
                                    <input  type="submit" name="confirmMoveCells" value="Confirm" class="uportal-button" />
                                    <input type="button" name="print" value="Print" class="uportal-button" onclick="javascript:printPage()" />
                                </td>
                            </tr>
                        </table>    
                        
                    </form>
                    
                    
                </td>
            </tr>
        </table>

    </xsl:template>
</xsl:stylesheet>
