<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="../javascript_code.xsl"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:template match="batch_creation">
        <xsl:call-template name="javascript_code"/>
        <script language="javascript">
            
            
            function updateColor (imgTag, cellID) 
            {
            
            var numToSelect = document.getElementById('numberToSelect').value;
            var numCells = document.getElementById('numCells').value;
            var str = imgTag.src.toString();
            
            if (str.indexOf("icons/unused.gif") != -1 )
            {
            imgTag.src = "media/neuragenix/icons/current.gif";
            numCells ++;
            
            // Add item to cells to remove list
            document.getElementById('cellList').value = document.getElementById('cellList').value + cellID + ";";
            }    
            
            if (str.indexOf("icons/current.gif") != -1 )
            {
            imgTag.src = "media/neuragenix/icons/unused.gif";
            numCells --;
            // remove item from cells to remove list
            var cellList = document.getElementById('cellList').value.toString();
            document.getElementById('cellList').value = cellList.replace((cellID+";"),"");                
            }    
            
            document.getElementById('numCells').value = numCells;
            if (numCells == numToSelect){
                
                
                document.getElementById('submit_cell_form').type = "submit";
                
            }
            else document.getElementById('submit_cell_form').type = "hidden";
            }
        </script>
        
        <!--create drop boxes -->
        <xsl:for-each select="GenerationDetails">
            <xsl:variable name="internalID" select="GD_internalID"/>
            <xsl:for-each select="GD_AvailableLocations">
                <!-- this code needs to also build the remaining drop downs - ie not wait for the click! -->
                <xsl:choose>
                    <xsl:when test="count(AL_Continuous) &gt; 0">
                        <script> buildInventoryContinuous('<xsl:value-of select="$internalID"
                                />','<xsl:value-of select="SITE_intSiteID"/>', '<xsl:value-of
                                select="SITE_strSiteName"/>', '<xsl:value-of select="TANK_intTankID"
                            />', '<xsl:value-of select="TANK_strTankName"/>', '<xsl:value-of
                                select="BOX_intBoxID"/>', '<xsl:value-of select="BOX_strBoxName"/>',
                                '<xsl:value-of select="TRAY_intTrayID"/>', '<xsl:value-of
                                select="TRAY_strTrayName"/>' <xsl:for-each select="AL_Continuous"> ,
                                    '<xsl:value-of select="AL_Continuous_ID"/>', '<xsl:value-of
                                    select="AL_Continuous_UserData"/>' </xsl:for-each>); </script>
                    </xsl:when>
                    <xsl:otherwise>
                        <script> buildInventory('<xsl:value-of select="$internalID"
                                />','<xsl:value-of select="SITE_intSiteID"/>', '<xsl:value-of
                                select="SITE_strSiteName"/>', '<xsl:value-of select="TANK_intTankID"
                            />', '<xsl:value-of select="TANK_strTankName"/>', '<xsl:value-of
                                select="BOX_intBoxID"/>', '<xsl:value-of select="BOX_strBoxName"/>',
                                '<xsl:value-of select="TRAY_intTrayID"/>', '<xsl:value-of
                                select="TRAY_strTrayName"/>' <xsl:for-each select="AL_Continuous"> ,
                                    '<xsl:value-of select="AL_Continuous_ID"/>', '<xsl:value-of
                                    select="AL_Continuous_UserData"/>' </xsl:for-each>); </script>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
        </xsl:for-each>
        <!--main table-->
        <table width="100%">
            <tr>
                <td width="20%">Left Columns</td>
                <td widht="80%">
                    
                    
                    
                    <table width="100%">
                        <!-- this row for the table reader-->
                        <tr>
                            <td class="uportal-channel-subtitle" colspan="6">Batch
                                Creation Step 2: Allocate Inventory</td>
                            <td align="right" colspan="6">
                                <form name="back_form" action="{$baseActionURL}" method="POST">
                                    <!-- back to the parent biospecimen -->
                                    
                                    <input type="hidden" name="module"
                                        value="batch_creation"/>
                                    <input type="hidden" name="action"
                                        value="BATCH_CREATE_START"/>
                                   
                                                          
                                    
                                    
                                    
                                </form>
                                
                                <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
                                    alt="Previous" onclick="javascript:document.back_form.submit();"/>
                                <img border="0" src="media/neuragenix/buttons/next_disabled.gif"
                                    alt="Next"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="12"><hr/></td>
                        </tr>
                        <!-- this row for the table reader-->
                        <tr>
                            <td width="3%"/>
                            <td width="10%" class="neuragenix-form-row-input-label">
                                <xsl:value-of select="BIOSPECIMEN_strSampleTypeDisplay"/>
                            </td>
                            <td width="10%" class="neuragenix-form-row-input-label">
                                <xsl:value-of select="BIOSPECIMEN_strSampleSubTypeDisplay"/>
                            </td>
                            <td width="10%" class="neuragenix-form-row-input-label"> Number </td>
                            <td width="10%" class="neuragenix-form-row-input-label">
                                <xsl:value-of select="SITE_strTitleDisplay"/>
                            </td>
                            <td width="10%" class="neuragenix-form-row-input-label">
                                <xsl:value-of select="TANK_strTitleDisplay"/>
                            </td>
                            <td width="10%" class="neuragenix-form-row-input-label">
                                <xsl:value-of select="BOX_strTitleDisplay"/>
                            </td>
                            <td width="10%" class="neuragenix-form-row-input-label">
                                <xsl:value-of select="TRAY_strTitleDisplay"/>
                            </td>
                            
                            <td class="neuragenix-form-row-input-label">Status</td>
                            <td width="10%" class="neuragenix-form-row-input-label"/>
                        </tr>
                        <!-- existing generation information -->
                        <xsl:for-each select="GenerationDetails">
                            <tr>
                                <!--determine the color of the line !-->
                                <xsl:variable name="classIDNumber">
                                    <xsl:value-of select="GD_internalID"/>
                                </xsl:variable>
                                <xsl:choose>
                                    <xsl:when test="$classIDNumber mod 2 = 0">
                                        <xsl:attribute name="class"
                                        >uportal-input-text</xsl:attribute>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:attribute name="class">uportal-text</xsl:attribute>
                                    </xsl:otherwise>
                                </xsl:choose>
                                <td class="uportal-input-test">
                                    <xsl:value-of select="$classIDNumber+1"/>
                                </td>
                                <td>
                                    <xsl:value-of select="BIOSPECIMEN_strSampleType_Selected"/>
                                </td>
                                <td>
                                    <xsl:value-of select="BIOSPECIMEN_strSampleSubType_Selected"/>
                                </td>
                                <td>
                                    <xsl:value-of select="GD_AllocationAmount"/>
                                </td>
                                <xsl:choose>
                                    <!-- check if the system has decided if there is available spaces or not -->
                                    <xsl:when test="GD_AvailableLocations/GD_NoAvailable='true'">
                                        <!-- display data based on whatever they selected and mark as unavailable -->
                                        <td>
                                            <xsl:value-of select="SITE_strSiteName"/>
                                        </td>
                                        <td>
                                            <xsl:value-of select="TANK_strTankName"/>
                                        </td>
                                        <td>
                                            <xsl:value-of select="BOX_strBoxName"/>
                                        </td>
                                        <td>
                                            <xsl:value-of select="TRAY_strTrayName"/>
                                        </td>
                                        <td/>
                                        <td>
                                            <font color="red">
                                                <b>No available spaces</b>
                                            </font>
                                        </td>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:choose>
                                            <xsl:when test="(GD_AllocationMode=0 or
                                                GD_AllocationMode=1) and GD_AllocationStatus=-1">
                                                <td>
                                                  <!--<xsl:value-of select="SITE_strSiteName" /><br /> -->
                                                  <!-- drop down goes here with available allocs -->
                                                  <select name="SITE_{GD_internalID}"
                                                  id="SITE_{GD_internalID}"
                                                  onChange="updateTank('{GD_internalID}')"
                                                  onFocus="updateTank('{GD_internalID}')">
                                                  <script> var currInventory =
                                                  myInventory[<xsl:value-of
                                                  select="GD_internalID"
                                                  />].getArray(); for (var i = 0; i
                                                  &lt; currInventory.length; i++) {
                                                  document.writeln('&lt;OPTION value='
                                                  + currInventory[i].getID() +
                                                  '&gt;');
                                                  document.writeln(currInventory[i].getName());
                                                  document.writeln('&lt;/OPTION&gt;');
                                                  } </script>
                                                  </select>
                                                </td>
                                                <td>
                                                  <!--<xsl:value-of select="TANK_strTankName" /><br />-->
                                                  <!-- drop down goes here with available allocs -->
                                                  <select name="TANK_{GD_internalID}"
                                                  id="TANK_{GD_internalID}"
                                                  onChange="updateBox('{GD_internalID}')"/>
                                                </td>
                                                <td>
                                                  <!--<xsl:value-of select="BOX_strBoxName" /><br />-->
                                                  <!-- drop down goes here with available allocs -->
                                                  <select name="BOX_{GD_internalID}"
                                                  id="BOX_{GD_internalID}"
                                                  onChange="updateTray('{GD_internalID}')"/>
                                                </td>
                                                <td>
                                                  <!--<xsl:value-of select="TRAY_strTrayName" /><br />-->
                                                  <!-- drop down goes here with available allocs -->
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="count(./GD_AvailableLocations/AL_Continuous)
                                                  &gt; 0">
                                                  <select name="TRAY_{GD_internalID}"
                                                  id="TRAY_{GD_internalID}"
                                                  onChange="updateCells('{GD_internalID}')"
                                                  />
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <!-- fill gaps -->
                                                  <select name="TRAY_{GD_internalID}"
                                                  id="TRAY_{GD_internalID}"
                                                  onChange="document.DOALLOCATION_{GD_internalID}.locationID.value=this.value"
                                                  />
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                </td>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <td>
                                                  <xsl:value-of select="SITE_strSiteName"/>
                                                </td>
                                                <td>
                                                  <xsl:value-of select="TANK_strTankName"/>
                                                </td>
                                                <td>
                                                  <xsl:value-of select="BOX_strBoxName"/>
                                                </td>
                                                <td>
                                                  <xsl:value-of select="TRAY_strTrayName"/>
                                                </td>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                        <td>
                                            <xsl:choose>
                                                <xsl:when test="string(GD_AllocationStatus)='-1'">
                                                  <xsl:choose>
                                                  <xsl:when test="GD_AllocationMode=0">
                                                  <font color="green">
                                                  <b>Ready to allocate</b>
                                                  </font>
                                                  </xsl:when>
                                                  <xsl:when test="GD_AllocationMode=1">
                                                  <font color="green">
                                                  <b>Ready to allocate</b>
                                                  </font>
                                                  </xsl:when>
                                                  <xsl:when test="GD_AllocationMode=2">
                                                  <font color="green">
                                                  <b>Ready to create tray and
                                                  allocate</b>
                                                  </font>
                                                  </xsl:when>
                                                  <xsl:otherwise> No comment defined for this
                                                  mode </xsl:otherwise>
                                                  </xsl:choose>
                                                </xsl:when>
                                                <xsl:when test="GD_AllocationStatus='1100'">
                                                  <font color="green">
                                                  <b>Allocation Complete</b>
                                                  </font>
                                                </xsl:when>
                                            </xsl:choose>
                                        </td>
                                        <td>
                                            <xsl:if test="GD_AllocationStatus='-1'">
                                                <!-- not yet allocated -->
                                                <form name="DOALLOCATION_{GD_internalID}"
                                                  id="DOALLOCATION_{GD_internalID}"
                                                  action="{$baseActionURL}?module=batch_creation&amp;action=allocate_cells&amp;intAllocationID={GD_internalID}"
                                                  method="post">
                                                  <input type="hidden" name="locationID" value=""/>
                                                  <xsl:choose>
                                                  <xsl:when test="GD_AllocationMode=0 or
                                                  GD_AllocationMode=1">
                                                  <input type="button"
                                                  onclick="javascript:submitAllocation({GD_internalID});"
                                                  class="uportal-button"
                                                  value="Allocate"/>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <input type="button"
                                                  onclick="javascript:document.DOALLOCATION_{GD_internalID}.submit()"
                                                  class="uportal-button"
                                                  value="Create Tray"/>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                </form>
                                            </xsl:if>
                                        </td>
                                    </xsl:otherwise>
                                </xsl:choose>
                                
                            </tr>
                        </xsl:for-each>
                        <!-- form to add new generation -->
                        
                        <tr>
                            <!-- take the whole page -->
                            <td colspan="12">
                                <xsl:apply-templates select="details"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="12">
                                <hr/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="12" align="right">
                                <form action="{$baseActionURL}">
                                    <input type="button" value="back"/>
                                    <input type="submit" value="next"/>
                                    <input type="hidden" name="action"
                                        value="generate_id"/>
                                    <input type="hidden" name="module" value="Batch_creation"/>
                                </form>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <script>
        <xsl:for-each select="GenerationDetails">
                   updateTank(<xsl:value-of select="GD_internalID" />);
               </xsl:for-each>
           </script>
    </xsl:template>
    <xsl:template match="details">
        <!-- detail form -->
        <xsl:variable name="GD_internalID">
            <xsl:value-of select="GD_internalID"/>
        </xsl:variable>
        
        <xsl:variable name="numberToSelect">
            <xsl:value-of select="numberOfCellToSelect"/>
        </xsl:variable>
        <div align="center">
       
        <table width="100%">
            <tr>
                <td colspan="6"><hr/></td>
            </tr>    
            <tr>
                <td colspan="5" width="100%" align="left" class="uportal-channel-subtitle"> 
                    Please select <font class="neuragenix-form-required-text"><xsl:value-of select="$numberToSelect"/></font>
                Cells 
                </td>
            </tr>
            <tr>
                <td>
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
                                                  <xsl:when test="$CELL_intBiospecimenID &gt;= 0">
                                                  <img src="media/neuragenix/icons/used.gif"
                                                  border="0" title="{$CELL_info}"/>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <img id="{$row}_{$col}"
                                                  src="media/neuragenix/icons/unused.gif"
                                                      border="0" title="{$CELL_info}" 
                                                      onclick="javascript:updateColor(this, {$CELL_intCellID});"/>
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
                    <form id="cell_form" action="{$baseActionURL}" method="post">
                        <input id="cellList" name="cellList" type="hidden" value=""/>
                        <input name="GD_internalID" type="hidden">
                            <xsl:attribute name="value"><xsl:value-of select="GD_internalID"/></xsl:attribute>
                        </input>
                        
                        <input name="TRAY_intTrayKey" type="hidden">
                            <xsl:attribute name="value"><xsl:value-of select="TRAY_intTrayKey"/></xsl:attribute>
                        </input>
                        
                        <input name="module" type="hidden" value="batch_creation"/>
                        <input name="action" type="hidden" value="save_allocations"></input>
                        <input id="numberToSelect" type="hidden" value="{$numberToSelect}"/>
                        <input id="numCells" type="hidden" value="0"/>
                        <input id="submit_cell_form" type="hidden" value="Update Allocation"></input>
                    </form>    
                </td>
            </tr>

        </table>
        </div>
    </xsl:template>
</xsl:stylesheet>
