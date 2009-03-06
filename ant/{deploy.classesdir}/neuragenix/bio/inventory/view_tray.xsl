<?xml version="1.0" encoding="utf-8"?>

<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./inventory_menu.xsl"/>
    <xsl:param name="formParams">current=view_tray</xsl:param>
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
        <xsl:param name="TRAY_intBoxID">
            <xsl:value-of select="TRAY_intBoxID"/>
        </xsl:param>
        <xsl:param name="TRAY_strTrayName">
            <xsl:value-of select="TRAY_strTrayName"/>
        </xsl:param>
        <xsl:param name="TRAY_intTrayCapacity">
            <xsl:value-of select="TRAY_intTrayCapacity"/>
        </xsl:param>
        <xsl:param name="TRAY_intTrayAvailable">
            <xsl:value-of select="TRAY_intTrayAvailable"/>
        </xsl:param>
        <xsl:param name="TRAY_intNoOfRow">
            <xsl:value-of select="TRAY_intNoOfRow"/>
        </xsl:param>
        <xsl:param name="TRAY_intNoOfCol">
            <xsl:value-of select="TRAY_intNoOfCol"/>
        </xsl:param>
        <xsl:param name="TRAY_strRowNoType">
            <xsl:value-of select="TRAY_strRowNoType"/>
        </xsl:param>
        <xsl:param name="TRAY_strColNoType">
            <xsl:value-of select="TRAY_strColNoType"/>
        </xsl:param>
        <xsl:param name="strSource">
            <xsl:value-of select="strSource"/>
        </xsl:param>
        <xsl:param name="intCurrentPatientID">
            <xsl:value-of select="intCurrentPatientID"/>
        </xsl:param>
        <xsl:param name="intCurrentBiospecimenID">
            <xsl:value-of select="intCurrentBiospecimenID"/>
        </xsl:param>
        <xsl:param name="intCurrentCellID">
            <xsl:value-of select="intCurrentCellID"/>
        </xsl:param>
        <xsl:param name="blLockError">
            <xsl:value-of select="blLockError"/>
        </xsl:param>
        <xsl:param name="strReportName">
            <xsl:value-of select="strReportName"/>
        </xsl:param>
        <xsl:param name="strInitialBiospecSampleType">
            <xsl:value-of select="strInitialBiospecSampleType"/>
        </xsl:param>
        <xsl:param name="strBackButton">
            <xsl:value-of select="strBackButton"/>
        </xsl:param>
        <xsl:param name="blBackToVialCalc">
            <xsl:value-of select="blBackToVialCalc"/>
        </xsl:param>
        <SCRIPT LANGUAGE="JavaScript" SRC="htmlarea/popupmouseover.js">
</SCRIPT>
        <script language="javascript">
			var myInventory = new Array();
                     // InvContainer OBJECT
			Style=["white","black","#000099","#E8E8FF","","","","","","","","","","",200,"",2,2,10,10,51,1,0,"",""];
        applyCssFilter();

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
           this.getSelected = InvContainer_getSelected;
           this.setSelected = InvContainer_setSelected;
           this.getAvailableSpaces = InvContainer_getAvailableSpaces;
           this.setAvailableSpaces = InvContainer_setAvailableSpaces;

        }

        function InvContainer_addSubContainer(container)
        {
           this.subContainers[this.subContainers.length] = container;
        }

		function htmlLineBreaks(str) 
		{
			var regstr = new String("\n");
		  var regexpstr = new RegExp(regstr, "g");
    		str2 = str.replace(regexpstr, "&lt;BR&gt;");	
			return str2;
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
        function InvContainer_getSelected()
        {
            return this.selected;
        }
        function InvContainer_setSelected(select)
        {
            this.selected = select;
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
   if(element != null)
   {
        resetOptions(element);
    }


      for (var i = 0; i &lt; container.length; i++)
      {
        if(container[i].getSelected() == "true")
        {
            newOpt = new Option(container[i].getName(), container[i].getID());
            element.options[i] = newOpt;
            element.options[i].selected = true;
        }
        else
        {
            var newOpt = new Option(container[i].getName(), container[i].getID());
             element.options[i] = newOpt;
        }
       
        


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
    //alert('updating yo box');
   var siteID = document.getElementById('SITE').value;
   var curr = document.getElementById('TANK').value;
   // alert('going to get site ' + siteID + ' and tank : ' + curr);
   
   var siteContainer = getSite(siteID);
   
   // if (siteContainer == null)
   //   alert ('and its null');
   var tankContainer = siteContainer.getSubContainer(curr);
   //if (tankContainer == null) alert('tank container is null');
   if(tankContainer != null)
   {
       var element =  document.getElementById('BOX');
       var boxContainer = tankContainer.subContainers;

        //if (boxContainer == null)
          //alert ('box container is null');
          
       if(element != null)
       {
            resetOptions(element);
        }


          for (var i = 0; i &lt; boxContainer.length; i++)
          {
             var newOpt = null;
            if(boxContainer[i].getSelected() == "true")
            {
                newOpt = new Option(boxContainer[i].getName(), boxContainer[i].getID());
                element.options[i] = newOpt;
                element.options[i].selected = true;
            }
            else
            {
                var newOpt = new Option(boxContainer[i].getName(), boxContainer[i].getID())
                element.options[i] = newOpt;
            }




          }
      }
   //  buildDropdown (myInventory[0], 'true');
  // updateTray();

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

function setSelected(ID)
{
    for(var i = 0;i &lt; myInventory.length;i++)
    {
        var tanks = myInventory[i].subContainers;
        for(var j=0 ; j &lt; tanks.length ; j++)
        {
            var boxes = tanks[j].subContainers;
            for(var k=0;k &lt; boxes.length; k++)
            {
                if(boxes[k].getID() == ID)
                {
                    myInventory[i].setSelected("true");
                    tanks[j].setSelected("true");
                    boxes[k].setSelected("true");
                }
            }
        }
    }
}

function setSelectedValues(SiteID,TankID,BoxID)
{
    for(var i = 0;i &lt; myInventory.length;i++)
    {
        if(myInventory[i].getID() == SiteID)
        {
            myInventory[i].setSelected("true");
        }
        var tanks = myInventory[i].subContainers;
        for(var j=0 ; j &lt; tanks.length ; j++)
        {
            if(tanks[j].getID() == TankID)
            {
                tanks[j].setSelected("true");            
            }
            var boxes = tanks[j].subContainers;
            for(var k=0;k &lt; boxes.length; k++)
            {
                if(boxes[k].getID() == BoxID)
                {
                    boxes[k].setSelected("true");
                }
            }
        }
    }
}

    function submitform()
    {
        var col = document.getElementById('COLS').value;
        var row = document.getElementById('ROW').value;
        var site = document.getElementById('SITE').value;
        var tank = document.getElementById('TANK').value;
        var box = document.getElementById('BOX').value;
        var tray = document.getElementById('TRAY').value;
        if(col.length == 0 || row.length == 0)
        {
            alert("The No of columns and the No of rows fields need to be filled");
        }
        else if(site == -1000 || tank == -1000 || box == -1000 || tray.length == 0)
        {
            alert("All required fields need to be filled");
        }
        else
        {
            document.form1.submit();
        }
    }   
    
            </script>    
               <DIV id="TipLayer" style="visibility:hidden;position:absolute;z-index:1000;top:-100"></DIV>
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
                    <form name="form1" action="{$baseActionURL}?uP_root=root&amp;{$formParams}" method="post">
                        <table width="100%">
                            <tr>
                                <td colspan="5" class="uportal-channel-subtitle">
                                    <xsl:value-of select="TRAY_strTitleDisplay"/> details<br/>
                                </td>
                                <td align="right">
                                    <xsl:choose>
                                        <xsl:when test="string($strBackButton)">
                                            <input type="button" name="back" tabindex="4"
                                                value="Back" class="uportal-button"
                                                onclick="javascript:jumpTo('{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTabOrder}&amp;{$strBackButton}')"
                                            />
                                        </xsl:when>
                                    </xsl:choose>
                                    <xsl:if test="$blBackToVialCalc = 'true'">
                                        <input type="button" name="back_to_vial_calc" tabindex="5"
                                            value="Back to vial calc" class="uportal-button"
                                            onclick="javascript:jumpTo('{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTabOrder}&amp;current=create_sub_specimen&amp;back=true')"
                                        />
                                    </xsl:if>
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
                            <script>
                        buildInventory(-1000, "*", -1000,"*",-1000,"*", -1000, "*");
                     </script>
                    <xsl:for-each select="InventoryList/SITE">
                        <xsl:variable name="varsiteID"><xsl:value-of select="SITE_intSiteID"/></xsl:variable>
                        <xsl:variable name="varsiteName"><xsl:value-of select="SITE_strSiteName"/></xsl:variable>
                         <script>
                          buildInventory('<xsl:value-of select="$varsiteID" />', '<xsl:value-of select="$varsiteName" />',-1000,"*", -1000,"*", -1000, "*");  
                          </script>
                          <xsl:for-each select="TANK">
                            <xsl:variable name="vartankID"><xsl:value-of select="TANK_intTankID"/></xsl:variable>
                            <xsl:variable name="vartankName"><xsl:value-of select="TANK_strTankName"/></xsl:variable>
                              <script>
                                    buildInventory('<xsl:value-of select="$varsiteID" />', '<xsl:value-of select="$varsiteName" />', '<xsl:value-of select="$vartankID" />', '<xsl:value-of select="$vartankName" />', -1000,"*", -1000, "*");           
                                </script>  
                                <xsl:for-each select="BOX">
                                <xsl:variable name="Box_ID"><xsl:value-of select="BOX_intBoxID"/></xsl:variable>
                                <xsl:variable name="Box_name"><xsl:value-of select="BOX_strBoxName"/></xsl:variable>
                                <script>
                                    buildInventory('<xsl:value-of select="$varsiteID" />', '<xsl:value-of select="$varsiteName" />', '<xsl:value-of select="$vartankID" />', '<xsl:value-of select="$vartankName" />', '<xsl:value-of select="$Box_ID" />', '<xsl:value-of select="$Box_name" />',-1000, "*");            
                                </script>
                                <xsl:if test="count(/inventory/Selected) = 0">
                                    <xsl:if test="$TRAY_intBoxID=$Box_ID">
                                        <script>
                                            setSelected('<xsl:value-of select="$Box_ID"/>');
                                         </script>
                                    </xsl:if>
                                </xsl:if>
                                </xsl:for-each>
                            </xsl:for-each>
                    </xsl:for-each>
                            <xsl:if test="count(Selected) &gt; 0">
                                <script>
                                     setSelectedValues('<xsl:value-of select="Selected/SITE/SITE_intSiteID"/>','<xsl:value-of select="Selected/SITE/TANK/TANK_intTankID"/>','<xsl:value-of select="Selected/SITE/TANK/BOX/BOX_intBoxID"/>');
                                </script>
                            </xsl:if>
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
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TRAY_intTrayCapacityDisplay"/>: </td>
                                <td width="15%" style="text-align: right" class="uportal-input-text">
                                    <xsl:if test="$TRAY_intTrayCapacity &gt; 1">
                                        <xsl:value-of select="TRAY_intTrayCapacity"/> cells </xsl:if>
                                    <xsl:if test="$TRAY_intTrayCapacity &lt; 2">
                                        <xsl:value-of select="TRAY_intTrayCapacity"/> cell </xsl:if>
                                </td>
                                <td width="20%"/>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TRAY_intTrayAvailableDisplay"/>: </td>
                                <td width="15%" style="text-align: right" class="uportal-input-text">
                                    <xsl:if test="$TRAY_intTrayAvailable &gt; 1">
                                        <xsl:value-of select="TRAY_intTrayAvailable"/> cells </xsl:if>
                                    <xsl:if test="$TRAY_intTrayAvailable &lt; 2">
                                        <xsl:value-of select="TRAY_intTrayAvailable"/> cell
                                    </xsl:if>
                                </td>
                                <td width="10%"/>
                            </tr>
                        </table>
                        <table width="100%" align="center">
                            <tr>
                                <td width="20%" class="uportal-label"><xsl:value-of select="SITE_strTitleDisplay" /> name: </td>
                                <td width="25%">
                                <select id="SITE" name="SITE_intSiteID" class="uportal-input-text" onChange="updateTank()">
                             <!--<option value="-1000">*</option>-->
                                  <script> 
                                  <xsl:text>                 
                                        for (var i = 0; i &lt; myInventory.length; i++)
                                          {
                                             document.writeln('&lt;OPTION value=' + myInventory[i].getID());
                                             if(myInventory[i].getSelected() == "true")
                                             {
                                                document.writeln(' selected=true');
                                             }
                                             document.writeln('&gt;');
                                             document.writeln(myInventory[i].getName());
                                             document.writeln('&lt;/OPTION&gt;');   
                                          }
                                          </xsl:text>


                                  </script>
                                  </select>
                                </td>
                                <td width="10%"/>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TANK_strTitleDisplay" /> name:</td>
                                <td width="25%">
                                    <select id="TANK" onChange="updateBox()" name="TANK_intTankID" class="uportal-input-text">
                                        <option selected="true" value="-1000">*</option>  
                                    </select> 
                                </td>
                            </tr>
                            <tr>
                                <td width="20%" class="uportal-label"> <xsl:value-of select="BOX_strTitleDisplay"/> name: </td>
                                <td width="25%">
                                    <select id="BOX" name="TRAY_intBoxID" class="uportal-input-text">
                                            <option value="-1000" selected="true">*</option>
                                     </select>
                                     <script>
                                            updateTank();
                                        </script>
                                </td>
                                <td width="10%"/>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TRAY_intNoOfColDisplay"/>: </td>
                                <td width="25%">
                                    <xsl:choose>
                                    <xsl:when test="count(/inventory/Selected) &gt; 0">
                                        <input id="COLS" type="text" style="text-align: right"
                                        name="TRAY_intNoOfCol" size="10"
                                        tabindex="5" class="uportal-input-text">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="/inventory/Selected/SITE/TANK/BOX/TRAY/TRAY_intNoOfCols"/>
                                        </xsl:attribute>
                                        </input>
                                    </xsl:when>
                                    <xsl:otherwise>
                                    <input id="COLS" type="text" style="text-align: right"
                                        name="TRAY_intNoOfCol" value="{$TRAY_intNoOfCol}" size="10"
                                        tabindex="5" class="uportal-input-text"/>
                                    </xsl:otherwise>
                                    </xsl:choose>
                                    <select name="TRAY_strColNoType" tabindex="6"
                                        class="uportal-input-text">
                                        <xsl:for-each select="TRAY_strColNoType">
                                            <xsl:variable name="TRAY_ColType"><xsl:value-of select="."/></xsl:variable>
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:choose>
                                                <xsl:when test="count(/inventory/Selected) &gt; 0">
                                                <xsl:variable name="TRAY_ColType_Selected"><xsl:value-of select="/inventory/Selected/SITE/TANK/BOX/TRAY/TRAY_strColNoType"/></xsl:variable>
                                                    <xsl:if test="$TRAY_ColType = $TRAY_ColType_Selected">
                                                    <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                </xsl:otherwise>
                                                </xsl:choose>
                                                <xsl:value-of select="$TRAY_ColType"/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TRAY_strTrayNameDisplay"/>: </td>
                                <td width="25%">
                                    <xsl:choose>
                                    <xsl:when test="count(/inventory/Selected) &gt; 0">
                                        <input id="TRAY" type="text" name="TRAY_strTrayName"
                                        size="22" tabindex="2"
                                        class="uportal-input-text">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="/inventory/Selected/SITE/TANK/BOX/TRAY/TRAY_strTrayName"/>
                                        </xsl:attribute>
                                        </input>
                                    </xsl:when>
                                    <xsl:otherwise>
                                    <input id="TRAY" type="text" name="TRAY_strTrayName"
                                        value="{$TRAY_strTrayName}" size="22" tabindex="2"
                                        class="uportal-input-text"/>
                                    </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                                <td width="10%"/>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="TRAY_intNoOfRowDisplay"/>: </td>
                                <td width="25%">
                                <xsl:choose>
                                    <xsl:when test="count(/inventory/Selected) &gt; 0">
                                        <input id="ROW" type="text" style="text-align: right"
                                        name="TRAY_intNoOfRow" size="10"
                                        tabindex="3" class="uportal-input-text">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="/inventory/Selected/SITE/TANK/BOX/TRAY/TRAY_intNoOfRows"/>
                                        </xsl:attribute>
                                        </input>
                                    </xsl:when>
                                    <xsl:otherwise>
                                    <input id="ROW" type="text" style="text-align: right"
                                        name="TRAY_intNoOfRow" value="{$TRAY_intNoOfRow}" size="10"
                                        tabindex="3" class="uportal-input-text"/>
                                    </xsl:otherwise>
                                    </xsl:choose>
                                    <select name="TRAY_strRowNoType" tabindex="4"
                                        class="uportal-input-text">
                                        <xsl:for-each select="TRAY_strRowNoType">
                                        <xsl:variable name="TRAY_RowType"><xsl:value-of select="."/></xsl:variable>
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                 <xsl:choose>
                                                <xsl:when test="count(/inventory/Selected) &gt; 0">
                                                <xsl:variable name="TRAY_RowType_Selected"><xsl:value-of select="/inventory/Selected/SITE/TANK/BOX/TRAY/TRAY_strRowNoType"/></xsl:variable>
                                                    <xsl:if test="$TRAY_RowType = $TRAY_RowType_Selected">
                                                    <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                </xsl:otherwise>
                                                </xsl:choose>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <hr/>
                                </td>
                            </tr>
                        </table>
                        <table width="100%">
                            <tr>
                                <td width="20%">
                                    <input type="button" name="delete" value="Delete" tabindex="8"
                                        class="uportal-button"
                                        onclick="javascript:confirmDelete('{$baseActionURL}?uP_root=root&amp;current=view_tray&amp;TRAY_intTrayID={$TRAY_intTrayID}')"
                                    />
                                </td>
                                <td width="5%" align="right">
                                    <input type="submit" name="save" tabindex="7" value="Save"
                                        class="uportal-button" align="right"/>
                                    <input type="button" name="generateReport"
                                        value="Generate report" tabindex="9" class="uportal-button"
                                        onclick="javascript:window.open('{$downloadURL}?uP_root={$nodeId}&amp;file_name={$strReportName}&amp;property_name=neuragenix.bio.search.ExportFileLocation&amp;activity_required=inventory_view')"
                                        align="right"/>
                                </td>
                            </tr>
                            <tr>
                                <td height="20px"/>
                            </tr>
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
                            <xsl:if test="$strSource = 'inventory'">
                            <tr>
                                <td style="text-align: center" class="uportal-text">

                                    <input type="submit" name="change_cells" value="Change cell locations" tabindex="10"/>
                                </td>
                            </tr>
                            </xsl:if>                            
                            <tr>
                                <td height="10px"/>
                            </tr>
                        </table>
						<div align="center">
                        <table border="1">
                            <xsl:if test="$strSource = 'allocate'">
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
                                                  <!-- get into the cell -->
                                                  <xsl:variable name="CELL_info">
                                                  <xsl:value-of select="CELL_info"/>
                                                  </xsl:variable>
													  <script language="javascript">var cellinfo = "<![CDATA[]]><xsl:value-of select="CELL_info"/><![CDATA[";]]> var celltitle="Biospecimen";</script>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intBiospecimenID = '-1'">
                                                  <!-- unallocated cell -->
													  
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=3&amp;current=biospecimen_view&amp;source=inventory&amp;module=ALLOCATE_CELL&amp;action=allocate&amp;CELL_intCellID={$CELL_intCellID}&amp;PATIENT_intInternalPatientID={$intCurrentPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intCurrentBiospecimenID}&amp;BIOSPECIMEN_strInitialBiospecSampleType={$strInitialBiospecSampleType}" >
                                                  
													  <img
                                                  src="media/neuragenix/icons/unused.gif"
                                                  border="0" onMouseOver="stm([celltitle,htmlLineBreaks(new String('{$CELL_info}'))],Style)" onMouseOut="htm()" />
                                                  </a>
                                                  </xsl:when>
                                                  <!-- cell occupied -->
                                                  <xsl:otherwise>
                                                  
													  <img
                                                  src="media/neuragenix/icons/used.gif"
                                                  border="0" onMouseOver="stm([celltitle,htmlLineBreaks(new String('{$CELL_info}'))],Style)" onMouseOut="htm()"
                                                  />
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
                            <!-- changing the location of the bispecimen -->
                            <xsl:if test="$strSource = 'change'">
                                <xsl:for-each select="Row">
                                    <tr>
                                        <xsl:for-each select="Col">
                                            <xsl:variable name="label">
                                                <xsl:value-of select="label"/>
                                            </xsl:variable>
                                            <td>
                                                <xsl:choose>
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
													  <script language="javascript">var cellinfo = "<![CDATA[]]><xsl:value-of select="CELL_info"/><![CDATA[";]]> var celltitle="Biospecimen";</script>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intBiospecimenID = '-1'">
													  
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=3&amp;current=biospecimen_view&amp;source=inventory&amp;module=ALLOCATE_CELL&amp;CELL_intCellID={$CELL_intCellID}&amp;PATIENT_intInternalPatientID={$intCurrentPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$intCurrentBiospecimenID}&amp;BIOSPECIMEN_strInitialBiospecSampleType={$strInitialBiospecSampleType}&amp;action=relocate">
                                                 
													   <img
                                                  src="media/neuragenix/icons/unused.gif"
                                                  border="0"
                                                  onMouseOver="stm([celltitle,htmlLineBreaks(new String('{$CELL_info}'))],Style)" onMouseOut="htm()"/>
                                                  </a>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intCellID = $intCurrentCellID">
													 
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=3&amp;current=biospecimen_view&amp;source=inventory&amp;CELL_intCellID={$CELL_intCellID}&amp;intInternalPatientID={$CELL_intPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$CELL_intBiospecimenID}&amp;BIOSPECIMEN_strInitialBiospecSampleType={$strInitialBiospecSampleType}">
													  
                                                  <img
                                                  src="media/neuragenix/icons/current.gif"
                                                  border="0"
                                                  onMouseOver="stm([celltitle,htmlLineBreaks(new String('{$CELL_info}'))],Style)" onMouseOut="htm()"
                                                  />
                                                  </a>
                                                  </xsl:when>
                                                  <xsl:otherwise>
													  
                                                  <img
                                                  src="media/neuragenix/icons/used.gif"
                                                  border="0"
                                                  onMouseOver="stm([celltitle,htmlLineBreaks(new String('{$CELL_info}'))],Style)" onMouseOut="htm()"/>
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
                            <!-- view only -->
                            <xsl:if test="$strSource = 'view'">
                                <xsl:for-each select="Row">
                                    <tr>
                                        <xsl:for-each select="Col">
                                            <xsl:variable name="label">
                                                <xsl:value-of select="label"/>
                                            </xsl:variable>
											  
                                            <td>
                                                <xsl:choose>
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
													  <script language="javascript">var cellinfo = "<![CDATA[]]><xsl:value-of select="CELL_info"/><![CDATA[";]]> var celltitle="Biospecimen";</script>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intBiospecimenID = '-1'">
													  
                                                  <img
                                                  src="media/neuragenix/icons/unused.gif"
                                                  border="0" onMouseOver="stm([celltitle,htmlLineBreaks(new String('{$CELL_info}'))],Style)" onMouseOut="htm()"                                                                                                                                                  
                                                  />
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intCellID = $intCurrentCellID">
													  <script language="javascript">var cellinfo = "<![CDATA[]]><xsl:value-of select="CELL_info"/><![CDATA[";]]> var celltitle="Biospecimen";</script>
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=3&amp;current=biospecimen_view&amp;source=inventory&amp;CELL_intCellID={$CELL_intCellID}&amp;Patient_intInternalPatientID={$CELL_intPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$CELL_intBiospecimenID}&amp;strInitialBiospecSampleType={$strInitialBiospecSampleType}&amp;module=core&amp;action=view_biospecimen">
                                                  
													  <img
                                                  src="media/neuragenix/icons/current.gif"
                                                  border="0"
                                                  onMouseOver="stm([celltitle,htmlLineBreaks(new String('{$CELL_info}'))],Style)" onMouseOut="htm()"
                                                  />
                                                  </a>
                                                  </xsl:when>
                                                  <xsl:otherwise>
													
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=3&amp;current=biospecimen_view&amp;source=inventory&amp;CELL_intCellID={$CELL_intCellID}&amp;intInternalPatientID={$CELL_intPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$CELL_intBiospecimenID}&amp;strInitialBiospecSampleType={$strInitialBiospecSampleType}&amp;module=core&amp;action=view_biospecimen">
                                                  
													  <img
                                                  src="media/neuragenix/icons/used.gif"
                                                  border="0"
                                                  onMouseOver="stm([celltitle,htmlLineBreaks(new String('{$CELL_info}'))],Style)" onMouseOut="htm()"
                                                  />
                                                  </a>
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
													  <script language="javascript">var cellinfo = "<![CDATA[]]><xsl:value-of select="CELL_info"/><![CDATA[";]]> var celltitle="Biospecimen";</script>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="$CELL_intBiospecimenID = '-1'">
                                                  <img
                                                  src="media/neuragenix/icons/unused.gif"
                                                  border="0" onMouseOver="stm([celltitle,htmlLineBreaks(new String('{$CELL_info}'))],Style)" onMouseOut="htm()"                                                   
                                                  />
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                  <a
                                                  href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=3&amp;current=biospecimen_view&amp;source=inventory&amp;CELL_intCellID={$CELL_intCellID}&amp;intInternalPatientID={$CELL_intPatientID}&amp;BIOSPECIMEN_intBiospecimenID={$CELL_intBiospecimenID}&amp;strInitialBiospecSampleType={$strInitialBiospecSampleType}&amp;module=core&amp;action=view_biospecimen">
                                                  <img
                                                  src="media/neuragenix/icons/used.gif"
                                                  border="0"
                                                  onMouseOver="stm([celltitle,htmlLineBreaks(new String('{$CELL_info}'))],Style)" onMouseOut="htm()"/>
                                                  </a>
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
                        </table></div>
                        
                        
                        <input type="hidden" name="TRAY_intTrayID" value="{$TRAY_intTrayID}"/>
                        <input type="hidden" name="TRAY_intTrayCapacity"
                            value="{$TRAY_intTrayCapacity}"/>
                        <input type="hidden" name="TRAY_intTrayAvailable"
                            value="{$TRAY_intTrayAvailable}"/>
                        <xsl:if test="$blBackToVialCalc = 'true'">
                            <input type="hidden" name="vial_calc" value="true"/>
                        </xsl:if>
                    </form>

                                        
                </td>
            </tr>
        </table>
        
    </xsl:template>
</xsl:stylesheet>
