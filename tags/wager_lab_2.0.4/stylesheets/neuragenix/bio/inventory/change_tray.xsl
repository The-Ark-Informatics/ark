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
        <xsl:param name="cellList">
            <xsl:value-of select="cellList"/>
        </xsl:param>    
        <xsl:param name="numCellstoMove">
            <xsl:value-of select="numCellstoMove"/>
        </xsl:param>    
        
    <script language="javascript" >
        function updateColor (imgTag, cellID) 
        {

            var numCells = document.getElementById('numCellstoMove').value;
            var str = imgTag.src.toString();
            
            if (str.indexOf("icons/used.gif") != -1 )
            {
                imgTag.src = "media/neuragenix/icons/current.gif";
                numCells ++;
                // Add item to cells to remove list
                document.getElementById('cellList').value = document.getElementById('cellList').value + cellID + ";";
            }    

            if (str.indexOf("icons/current.gif") != -1 )
            {
                imgTag.src = "media/neuragenix/icons/used.gif";
                numCells --;
                // remove item from cells to remove list
                var cellList = document.getElementById('cellList').value.toString();
                document.getElementById('cellList').value = cellList.replace((cellID+";"),"");                
            }    
            
            document.getElementById('numCellstoMove').value = numCells;             
        }
   
        
        function selectCellsToMove ()
        {
            var strCellList = document.getElementById('cellList').value;
            if (strCellList.length &gt; 0)
            {
                document.getElementById('selectCells').value = "Select Cells";
                document.trayInfo.submit();
            }        
        }
        

        function relocateCells ()
        {
            var tray = document.getElementById('TRAY').value;
            
            if (tray != "-1000")
            {
                document.getElementById('moveCells').value = "Move Cells";
                document.changeCellLocation.submit();
            }        
        }
        
        
        function selectAllUsedCells ()
        {
            var numCells = document.getElementById('numCellstoMove').value;
            if (document.images) 
            {
                //Loop for all images
                for (var loopCounter = 0; loopCounter &lt; document.images.length; loopCounter++) 
                {
                    // Get all the cells that are used
                    // and set the image back to current image (selected)
                    // increment the numCells count
                    // add the cell to the cellList
                    if (document.images[loopCounter].src.toString().indexOf("icons/used.gif") != -1 )
                    {
                        document.images[loopCounter].src = "media/neuragenix/icons/current.gif";
                        numCells ++;

                        var strcellInfo = document.images[loopCounter].title.toString();
                        // get the index so that we can get the cell ID as a substring from the cell info
                        // start index = 0 + 9 corresponding the character after "Cell ID: "
                        // end index = index of "Row:" - 2;
                        var index = strcellInfo.indexOf("Row:");                        
                        index = index - 2;
                        var cellID = strcellInfo.substring (9, index);
                        // Add item to cells to remove list
                        document.getElementById('cellList').value = document.getElementById('cellList').value + cellID + ";";
                    }                
                }
                document.getElementById('numCellstoMove').value = numCells; 
            }
        
        }
        
        function clearAllUsedCells ()
        {
            var numCells = document.getElementById('numCellstoMove').value;
            if (document.images) 
            {
                //Loop for all images
                for (var loopCounter = 0; loopCounter &lt; document.images.length; loopCounter++) 
                {
                    // Get all the cells that were selected
                    // and set the image back to the used image
                    // decrement the numCells count
                    // remove the cell from the cellList
                    if (document.images[loopCounter].src.toString().indexOf("icons/current.gif") != -1 )
                    {
                        document.images[loopCounter].src = "media/neuragenix/icons/used.gif";
                        numCells --;
                        
                        var strcellInfo = document.images[loopCounter].title.toString();
                        // get the index so that we can get the cell ID as a substring from the cell info
                        // start index = 0 + 9 corresponding the character after "Cell ID: "
                        // end index = index of "Row:" - 2;
                        var index = strcellInfo.indexOf("Row:");                        
                        index = index - 2;
                        var cellID = strcellInfo.substring (9, index);
                        
                        // remove item from cells to remove list
                        var cellList = document.getElementById('cellList').value.toString();
                        document.getElementById('cellList').value = cellList.replace((cellID +";"),"");                
                    }                
                }
                document.getElementById('numCellstoMove').value = numCells;                 
            }
        
        }

        
                
        // InvContainer OBJECT
        function InvContainer (ID, Name, Type)
        {
           // Properties
           this.ID = ID;
           this.Name = Name;
           this.Type = Type;
           this.subContainers = new Array();

           // Method Declarations

           this.addSubContainer = InvContainer_addSubContainer;
           this.getSubContainer = InvContainer_getSubContainer;
           this.getID = InvContainer_getID;
           this.getName = InvContainer_getName;
           this.getAvailableSpaces = InvContainer_getAvailableSpaces;
           this.setAvailableSpaces = InvContainer_setAvailableSpaces;

        }

        function InvContainer_addSubContainer(container)
        {
           this.subContainers[this.subContainers.length] = container;
        }

        function InvContainer_getSubContainer(container)
        {
           for (var i=0; i &lt; this.subContainers.length; i++)
           {
              if (this.subContainers[i].getID() == container)
                 return this.subContainers[i];
           }
           return null;
        }


        function InvContainer_getID()
        {
           return this.ID;
        }

        function InvContainer_getName()
        {
           return this.Name;
        }

        function InvContainer_setAvailableSpaces(avSpace)
        {
           this.availableSpaces = avSpace;
        }

        function InvContainer_getAvailableSpaces()
        {
           return availableSpaces;
        }


        // ---- end Object
        var myInventory = new Array();

        function buildInventory(siteID, siteName, tankID, tankName, boxID, boxName, trayID, trayName)
        {

           for (var i =0; i &lt; myInventory.length; i++)
           {
    
     
              if (myInventory[i].getID() == siteID)
              {
                 //alert(myInventory[i].getID());
                 var tank = myInventory[i].getSubContainer(tankID);
                 if (tank != null)
                 {
                    var box = tank.getSubContainer(boxID);
                    if (box != null)
                    {
                       var tray = box.getSubContainer(trayID);
                       if (tray == null) // otherwise tray exists -- no need to add
                       {
                          var obj = new InvContainer(trayID, trayName, 'TRAY');
                          box.addSubContainer(obj);
                          return;
                       }
                        return;
                    }
                    else
                    {
                        // add both the box and the tray
                        var objBox = new InvContainer(boxID, boxName, 'BOX');
                        objBox.addSubContainer(new InvContainer(trayID, trayName, 'TRAY'));
                        tank.addSubContainer(objBox);
                        return;
                    }
                 }
                 else
                 {
                    // add the tank, box and tray
                    var objTank = new InvContainer(tankID, tankName, 'TANK');
                    var objBox = new InvContainer(boxID, boxName, 'BOX');

                    objBox.addSubContainer(new InvContainer(trayID, trayName, 'TRAY'));
                    objTank.addSubContainer(objBox);  
                    myInventory[i].addSubContainer(objTank);
                    return;
                 }
              }
          }     
             // Does not exist in set
           
          var site = new InvContainer(siteID, siteName, 'SITE');
          var tank = new InvContainer(tankID, tankName, 'TANK');
          var box = new InvContainer(boxID, boxName, 'BOX');
          var tray = new InvContainer(trayID, trayName, 'TRAY');

          box.addSubContainer(tray);
          tank.addSubContainer(box);
          site.addSubContainer(tank);
          myInventory[myInventory.length] = site;

        }
        
        // this will need to be updated
        function updateTank()
        {

            var curr = document.getElementById('SITE').value;
             var element =  document.getElementById('TANK');



           var container = getSite(curr).subContainers;   

          // populate if user select site as *  


           resetOptions(element);


              for (var i = 0; i &lt; container.length; i++)
              {

                var newOpt = new Option(container[i].getName(), container[i].getID())
                element.options[i] = newOpt;



              }

           //  buildDropdown (myInventory[0], 'true');      
           updateBox();


        }


        function resetOptions (arrayset)
        {
           for (var i = 0; i &lt; arrayset.options.length; i++)
           {
              arrayset.options[i] = null;
           }
           arrayset.options.length = 0;

        }


        function getSite(siteID)
        {

           for (var i = 0; i &lt; myInventory.length; i++)
           {
             if (myInventory[i].getID() == siteID)
             {
                return myInventory[i];
             }
           }

           return null;

        }


        function updateBox()
        {
           // alert('updating yo box');
           var siteID = document.getElementById('SITE').value;
           var curr = document.getElementById('TANK').value;
           // alert('going to get site ' + siteID + ' and tank : ' + curr);

           var siteContainer = getSite(siteID);

           // if (siteContainer == null)
           //   alert ('and its null');

           var tankContainer = siteContainer.getSubContainer(curr);
           if (tankContainer == null) alert('tank container is null');

           var element =  document.getElementById('BOX');
           var boxContainer = tankContainer.subContainers;

           // if (boxContainer == null)
           //   alert ('box container is null');

           resetOptions(element);

              for (var i = 0; i &lt; boxContainer.length; i++)
              {

                var newOpt = new Option(boxContainer[i].getName(), boxContainer[i].getID())
                element.options[i] = newOpt;



              }

           //  buildDropdown (myInventory[0], 'true');
           updateTray();

        }

        function updateTray()
        {
           // alert('updating trays');
           var siteID = document.getElementById('SITE').value;
           var curr = document.getElementById('TANK').value;
           var siteContainer = getSite(siteID);

           // if (siteContainer == null)
           //   alert ('and its null');

           var tankContainer = siteContainer.getSubContainer(curr);
           // if (tankContainer == null) alert('tank container is null');

           var boxID =  document.getElementById('BOX').value;
           var boxContainer = tankContainer.getSubContainer(boxID);

           // if (boxContainer == null) alert ('box container is null');

           var trays = boxContainer.subContainers;

           var element = document.getElementById('TRAY');


           resetOptions(element);


              for (var i = 0; i &lt; trays.length; i++)
              {

                var newOpt = new Option(trays[i].getName(), trays[i].getID())
                element.options[i] = newOpt;



              }
        }
        
    </script>
        
        <!-- load in all the inventory data -->

         <script>
            buildInventory(-1000, "*", -1000,"*",-1000,"*", -1000, "*");
         </script>

        <xsl:for-each select="InventoryLocations">
           <script>
              buildInventory('<xsl:value-of select="SITE_intSiteID" />', '<xsl:value-of select="SITE_strSiteName" />',-1000,"*", -1000,"*", -1000, "*");  
              buildInventory('<xsl:value-of select="SITE_intSiteID" />', '<xsl:value-of select="SITE_strSiteName" />', '<xsl:value-of select="TANK_intTankID" />', '<xsl:value-of select="TANK_strTankName" />', -1000,"*", -1000, "*");           
              buildInventory('<xsl:value-of select="SITE_intSiteID" />', '<xsl:value-of select="SITE_strSiteName" />', '<xsl:value-of select="TANK_intTankID" />', '<xsl:value-of select="TANK_strTankName" />', '<xsl:value-of select="BOX_intBoxID" />', '<xsl:value-of select="BOX_strBoxName" />',-1000, "*");            
              buildInventory('<xsl:value-of select="SITE_intSiteID" />', '<xsl:value-of select="SITE_strSiteName" />', '<xsl:value-of select="TANK_intTankID" />', '<xsl:value-of select="TANK_strTankName" />', '<xsl:value-of select="BOX_intBoxID" />', '<xsl:value-of select="BOX_strBoxName" />', '<xsl:value-of select="TRAY_intTrayID" />', '<xsl:value-of select="TRAY_strTrayName" />');

           </script>        
        </xsl:for-each>
     
        <table width="100%">
            <tr valign="top">
                <td width="30%">
                    <table width="100%" cellspacing="0" cellpadding="0">
                        <xsl:call-template name="sort_selection"/>
                    </table>
               
                   
                    <table width="100%" cellspacing="0" cellpadding="0">
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
                    <form name="trayInfo" action="{$baseActionURL}?uP_root=root&amp;{$formParams}" method="post">
                        <table width="100%">
                            <tr>
                                <td colspan="5" class="uportal-channel-subtitle">
                                    Change cell locations<br/>
                                </td>
                                <td align="right">
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

                        <table width="100%">
                            <tr>
                                <td style="text-align: center" class="uportal-channel-table-header"
                                    >Location grid</td>
                            </tr>
                            <tr>
                                <td height="20px"/>
                            </tr>
                            <tr>
                                <td style="text-align: center" class="uportal-text">To view cell
                                    information, place mouse over it.</td>
                            </tr>
                            <tr>
                                <td style="text-align: center" class="uportal-text">Click
                                    on the cells to be moved and click on "Submit Selected Cells"</td>
                            </tr>
                            <tr>
                                <td height="10px"/>
                            </tr>
                        </table>
                        <table border="1">
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
                                                  <xsl:value-of
                                                  select="CELL_intBiospecimenID"/>
                                                  </xsl:variable>
                                                  <xsl:variable name="CELL_intPatientID">
                                                  <xsl:value-of select="CELL_intPatientID"
                                                  />
                                                  </xsl:variable>
                                                  <xsl:variable name="CELL_info">
                                                  <xsl:value-of select="CELL_info"/>
                                                  </xsl:variable>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intBiospecimenID = '-1'">
                                                  <img
                                                  src="media/neuragenix/icons/unused.gif"
                                                  border="0" title="{$CELL_info}"                                                                                                   
                                                  />
                                                  </xsl:when>
                                                  <xsl:otherwise>                                                  
                                                        <xsl:choose>
                                                        <xsl:when test="string-length(OldCell) &gt; 0">
                                                        <img src="media/neuragenix/icons/current.gif" cellID="{$CELL_intCellID}" border="0" title="{$CELL_info}" 
                                                        onclick="javascript:updateColor(this, {$CELL_intCellID})"/> 
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                        <img src="media/neuragenix/icons/used.gif" border="0" title="{$CELL_info}"
                                                        cellID="{$CELL_intCellID}" onclick="javascript:updateColor(this, {$CELL_intCellID})"  />                                                 
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
                        </table>
                        
                        
                        <table width="100%">
                            <tr>
                                <td align="left">
                                    <input  type="button" value="Select All" class="uportal-button" onclick="javascript:selectAllUsedCells()" />                                    
                                    <input  type="button" value="Clear Selected" class="uportal-button" onclick="javascript:clearAllUsedCells()" />                                    
                                </td>                                
                            </tr>
                        </table>  
                        <br/>
                        <br/>
                        <table width="100%">
                            <tr>
                                <td align="left">
                                <input  type="button" value="Submit Selected Cells" class="uportal-button" onclick="javascript:selectCellsToMove()" />
                                </td>
                            </tr>
                        </table>
                          
                        <input type="hidden" name="TRAY_intTrayID" value="{$TRAY_intTrayID}"/>
                        <input id="numCellstoMove" type="hidden" name="numCellstoMove" value="{$numCellstoMove}"/>
                        <input id="cellList" type="hidden" name="cellList" value="{$cellList}"/>
                        <input id="selectCells" type="hidden" name="selectCells" />
                        
                    </form>
                    <br/>
                    <form name="changeCellLocation" action="{$baseActionURL}?uP_root=root&amp;{$formParams}" method="POST" class="uportal-label">
                       <input type="hidden" name="strInternalBiospecimenID" value="{strBiospecimenID}" />
                       <input type="hidden" name="strBiospecimenID" value="{strBiospecimenID}" />
                          <xsl:if test="string-length(InventoryLocations) &gt; 0">
                          <table width="100%">
                           <tr>
                              <td style="text-align: left" class="uportal-text">Select the new cell location: </td>
                           </tr>
                          
                          </table>
                          <table width="100%">
                       <!-- <table width="100%"> -->
                          <tr>
                              <td>
                                 <span class="uportal-label"><xsl:value-of select="SITE_strTitleDisplay"/></span>
                              </td>

                              <td>
                                 <span class="uportal-label"><xsl:value-of select="TANK_strTitleDisplay"/></span>
                              </td>


                              <td>
                                 <span class="uportal-label"><xsl:value-of select="BOX_strTitleDisplay"/></span>
                              </td>
                              <td>
                                 <span class="uportal-label"><xsl:value-of select="TRAY_strTitleDisplay"/></span>
                              </td>

                          </tr>

                          <tr class="uportal-input-text">
                              <td>

                                  <!-- site -->
                                  <!-- on change get the update from the server -->

                                  <select id="SITE" name="SITE_intSiteIDSelected" class="uportal-input-text" onChange="updateTank()">
                                     <!--<option value="-1000">*</option>-->
                                  <script> 
                                  <xsl:text>                 
                                        for (var i = 0; i &lt; myInventory.length; i++)
                                          {
                                             document.writeln('&lt;OPTION value=' + myInventory[i].getID() + '&gt;');
                                             document.writeln(myInventory[i].getName());
                                             document.writeln('&lt;/OPTION&gt;');   
                                          }
                                          </xsl:text>


                                  </script>
                                  </select>
                              </td>

                              <td>
                                  <!-- tank -->
                                  <!-- on change get the update from the server -->
                                  <select id="TANK" onChange="updateBox()" name="TANK_intTankIDSelected" class="uportal-input-text">
                                        <option selected="true" value="-1000">*</option>  
                                  
                                  </select>
                              </td>


                              <td>
                                  <!-- box -->
                                  <select id="BOX" onChange="updateTray()" name="BOX_intBoxIDSelected" class="uportal-input-text">
                                     <option value="-1000" selected="true">*</option>
                                     </select>
                              </td>
                              <td>
                                  <!-- tray -->
                                  <select id="TRAY" name="TRAY_intTrayIDSelected" class="uportal-input-text">
                                    <option value="-1000" selected="true">*</option> 
                                  </select>
                              </td>
                    </tr>
                    </table>
                    </xsl:if>
                    
                    <table width="100%">
                        <tr><td><hr /></td></tr>
                    </table>
                    
                     <table width="100%">
                        <tr>
                            <td align="left">
                                <input  type="submit" name="Cancel" value="Cancel" class="uportal-button" />
                                 <xsl:if test="string-length(InventoryLocations) &gt; 0">    
                                    <input type="button" value="Move Cells" class="uportal-button" onclick="javascript:relocateCells()" />
                                </xsl:if>
                            </td>
                        </tr>
                    </table>   
                    
                    <input type="hidden" id="moveCells" name="moveCells" />
                    </form>
                    
                </td>
            </tr>
        </table>

    </xsl:template>
</xsl:stylesheet>
