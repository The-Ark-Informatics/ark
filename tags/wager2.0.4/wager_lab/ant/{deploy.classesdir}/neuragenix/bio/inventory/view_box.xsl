<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./inventory_menu.xsl"/>
    <xsl:param name="formParams">current=view_box&amp;save=save</xsl:param>
    <xsl:output method="html" indent="no"/>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:variable name="SortType"/>
    <xsl:template match="inventory">
        <xsl:param name="BOX_intBoxID">
            <xsl:value-of select="BOX_intBoxID"/>
        </xsl:param>
        <xsl:param name="BOX_intTankID">
            <xsl:value-of select="BOX_intTankID"/>
        </xsl:param>
        <xsl:param name="BOX_intBoxCapacity">
            <xsl:value-of select="BOX_intBoxCapacity"/>
        </xsl:param>
        <xsl:param name="BOX_intBoxAvailable">
            <xsl:value-of select="BOX_intBoxAvailable"/>
        </xsl:param>
        <xsl:param name="BOX_strBoxName">
            <xsl:value-of select="BOX_strBoxName"/>
        </xsl:param>
        <xsl:param name="BOX_strBoxDescription">
            <xsl:value-of select="BOX_strBoxDescription"/>
        </xsl:param>
        <xsl:param name="blLockError">
            <xsl:value-of select="blLockError"/>
        </xsl:param>
               <script language="javascript">        
                     // InvContainer OBJECT
        function InvContainer (ID, Name, Type)
        {
           // Properties
           this.ID = ID;
           this.Name = Name;
           this.Type = Type;
           this.subContainers = new Array();
           this.selected = false;
           // Method Declarations

           this.addSubContainer = InvContainer_addSubContainer;
           this.getSubContainer = InvContainer_getSubContainer;
           this.getID = InvContainer_getID;
           this.getSelected = InvContainer_getSelected;
           this.setSelected = InvContainer_setSelected;
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

        function InvContainer_getSelected()
        {
            return this.selected;
        }
        function InvContainer_setSelected(select)
        {
            this.selected = select;
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
               // populate if user select site as *  
  
               if(element != null)
               {
                   resetOptions(element);
               }
 
            var container = getSite(curr).subContainers;   
   

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
//   updateBox();


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


function setSelected(ID)
{
    for(var i = 0;i &lt; myInventory.length;i++)
    {
        var tanks = myInventory[i].subContainers;
        for(var j=0 ; j &lt; tanks.length ; j++)
        {
            if(tanks[j].getID() == ID)
            {
                myInventory[i].setSelected("true");
                tanks[j].setSelected("true");
            }
        }
    }
}

function setSelectedValues(SiteID,TankID)
{
    for(var i = 0;i &lt; myInventory.length;i++)
    {
        if(myInventory[i].getID() == SiteID)
        {
            myInventory[i].setSelected("true");
            var tanks = myInventory[i].subContainers;
            for(var j=0 ; j &lt; tanks.length ; j++)
            {
                if(tanks[j].getID() == TankID)
                {
                    tanks[j].setSelected("true");            
                }
            }
        }
    }
}
function submitform()
    {
        var site = document.getElementById('SITE').value;
        var tank = document.getElementById('TANK').value;
        var box = document.getElementById('BOX').value;

        if(site == -1000 || tank == -1000 || box.length == 0)
        {
            alert("All required fields need to be filled");
        }
        else
        {
            document.form1.submit();
        }
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
                <td width="70%">
                    <form name="form1" action="{$baseActionURL}?{$formParams}" method="post">
                        <table width="100%">
                            <tr>
                                <td colspan="6" class="uportal-channel-subtitle">
                                    <xsl:value-of select="BOX_strTitleDisplay"/> details<br/>
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
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="BOX_intBoxCapacityDisplay"/>: </td>
                                <td width="15%" style="text-align: right" class="uportal-input-text">
                                    <xsl:if test="$BOX_intBoxCapacity &gt; 1">
                                        <xsl:value-of select="BOX_intBoxCapacity"/> cells </xsl:if>
                                    <xsl:if test="$BOX_intBoxCapacity &lt; 2">
                                        <xsl:value-of select="BOX_intBoxCapacity"/> cell </xsl:if>
                                </td>
                                <td width="20%"/>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="BOX_intBoxAvailableDisplay"/>: </td>
                                <td width="15%" style="text-align: right" class="uportal-input-text">
                                    <xsl:if test="$BOX_intBoxAvailable &gt; 1">
                                        <xsl:value-of select="BOX_intBoxAvailable"/> cells </xsl:if>
                                    <xsl:if test="$BOX_intBoxAvailable &lt; 2">
                                        <xsl:value-of select="BOX_intBoxAvailable"/> cell </xsl:if>
                                </td>
                                <td width="10%"/>
                            </tr>
                        </table>
                        <table width="100%">
                            <!--This is the script to create dynamic drop downs-->
                            <script language="javascript">   
                                buildInventory("-1000", "*", "-1000","*","-1000","*", "-1000", "*",false);
                             </script>
                            <xsl:for-each select="InventoryList/SITE">
                                <xsl:variable name="varsiteID"><xsl:value-of select="SITE_intSiteID"/></xsl:variable>
                                <xsl:variable name="varsiteName"><xsl:value-of select="SITE_strSiteName"/></xsl:variable>
                               <script language="javascript">   
                                    buildInventory('<xsl:value-of select="$varsiteID" />', '<xsl:value-of select="$varsiteName" />',"-1000","*", "-1000","*", "-1000", "*");  
                                </script>
                                  <xsl:for-each select="TANK">
                                  <xsl:variable name="Tank_ID"><xsl:value-of select="TANK_intTankID"/></xsl:variable>
                                <xsl:variable name="Tank_name"><xsl:value-of select="TANK_strTankName"/></xsl:variable>
                                      <script language="javascript">   
                                            buildInventory('<xsl:value-of select="$varsiteID" />', '<xsl:value-of select="$varsiteName" />', '<xsl:value-of select="$Tank_ID" />', '<xsl:value-of select="$Tank_name" />', "-1000","*", "-1000", "*");           
                                        </script> 
                                        <xsl:if test="count(/inventory/Selected) = 0">
                                            <xsl:if test="$BOX_intTankID=$Tank_ID">
                                                <script language="javascript">   
                                                    setSelected('<xsl:value-of select="$Tank_ID"/>');
                                                 </script>
                                            </xsl:if>
                                        </xsl:if>
                                    </xsl:for-each>
                            </xsl:for-each>
                            <xsl:if test="count(Selected) &gt; 0">
                                <script language="javascript">   
                                     setSelectedValues('<xsl:value-of select="Selected/SITE/SITE_intSiteID"/>','<xsl:value-of select="Selected/SITE/TANK/TANK_intTankID"/>');
                                </script>
                            </xsl:if>
                            <tr>
                                <td width="20%" class="uportal-label">
                                        <xsl:value-of select="SITE_strTitleDisplay" /> name:
                                    </td>
                                <td width="25%">
                                    <select id="SITE" name="SITE_intSiteID" class="uportal-input-text" onChange="updateTank()">
                                     <!--<option value="-1000">*</option>-->
                                  <script language="javascript">    
                                  <xsl:text>                 
                                        for (var i = 0; i &lt; myInventory.length; i++)
                                          {
                                             document.write('&lt;OPTION value=' + myInventory[i].getID());
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
                                   <xsl:value-of select="TANK_strTitleDisplay"/> name:  </td>
                                <td width="25%">
                                    <select id="TANK" name="BOX_intTankID" class="uportal-input-text">
                                         <option value="-1000" selected="true">*</option>
                                    </select> 
                                        <script language="javascript">   
                                            updateTank();
                                        </script>
                                    
                                </td>
                            </tr>
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="BOX_strBoxNameDisplay"/>: </td>
                                <td width="25%">
                                    
                                        <input id="BOX" type="text" name="BOX_strBoxName"
                                         size="22" tabindex="2"
                                        class="uportal-input-text">
                                        <xsl:choose>
                                            <xsl:when test="count(/inventory/Selected) &gt; 0">
                                             <xsl:attribute name="value">
                                                <xsl:value-of select="/inventory/Selected/SITE/TANK/BOX/BOX_strBoxName"/>
                                            </xsl:attribute>
                                            </xsl:when>
                                            <xsl:otherwise>
                                            <xsl:attribute name="value">
                                                <xsl:value-of select="$BOX_strBoxName"/>
                                            </xsl:attribute>
                                        </xsl:otherwise>
                                        </xsl:choose>
                                        </input>
                                </td>
                                <td width="10%"/>
                                <td width="20%" class="uportal-label"><xsl:value-of select="BOX_strBoxDescriptionDisplay"/>: </td>
                                <td width="25%"><textarea name="BOX_strBoxDescription" rows="4" cols="20"
                                        tabindex="3" class="uportal-input-text">
                                        <xsl:choose>
                                            <xsl:when test="count(/inventory/Selected) &gt; 0">
                                                <xsl:value-of select="/inventory/Selected/SITE/TANK/BOX/BOX_strBoxDesc"/>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="BOX_strBoxDescription"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </textarea>
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
                                <td width="10%">
                                    <input type="button" name="save" tabindex="4" value="Save"
                                        class="uportal-button" onClick="javascript:submitform();"/>
                                    <input type="button" name="delete" value="Delete" tabindex="5"
                                        class="uportal-button"
                                        onclick="javascript:confirmDelete('{$baseActionURL}?current=view_box&amp;BOX_intBoxID={$BOX_intBoxID}')"
                                    />
                                </td>
                                <td width="80%"/>
                            </tr>
                        </table>
                        <input type="hidden" name="BOX_intBoxID" value="{$BOX_intBoxID}"/>
                    </form>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
