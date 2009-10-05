<?xml version="1.0" encoding="utf-8"?>
<!-- 

    Biospecimen Batch Processing - Batch Cloning Module
    Copyright (C) Neuragenix Pty Ltd, 2005
    Author : Daniel Murley
    Email : dmurley@neuragenix.com
    
    Purpose : Provides first stage of data entry for a user to batch create a set of biospecimens
   
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="../biospecimen_menu.xsl"/>
  <xsl:output method="html" indent="yes" />
  
  <xsl:template match="BATCH_PROCESSING">
  <!-- Get the parameters from the channel class -->
  <xsl:param name="strBiospecimenID"><xsl:value-of select="BIOSPECIMEN_strBiospecimenID" /></xsl:param>
  <xsl:param name="strBiospecOtherID"><xsl:value-of select="strBiospecOtherID" /></xsl:param>
  <xsl:param name="intBiospecStudyID"><xsl:value-of select="intBiospecStudyID" /></xsl:param>
  <xsl:param name="intInternalPatientID"><xsl:value-of select="intInternalPatientID" /></xsl:param>
  <xsl:param name="intBiospecParentID"><xsl:value-of select="BIOSPECIMEN_intBiospecimenID" /></xsl:param>
  <xsl:param name="strBiospecParentID"><xsl:value-of select="strBiospecParentID" /></xsl:param>
  
  <script language="javascript">


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
    
  
  
  
        
  <!-- 
        <input type="hidden" name="intInternalPatientID" value="{$intInternalPatientID}" />
        <input type="hidden" name="intInvCellID" value="-1" />
	<input type="hidden" name="page1completed" value="true" /> -->
        
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td class="uportal-channel-subTitleDisplay">
			Batch Processing - Generate and Allocate sub-specimens
                        </td>
                        <td align="right" width="1%">
                           <table width="100%" cellpadding="0" cellspacing="0">
                              <tr align="right"><td>
                           <a href="{$baseActionURL}?module=core&amp;action=view_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecParentID}" method="post">
                              <img border="0" src="media/neuragenix/buttons/previous_enabled.gif" alt="Previous"/>
                           </a>
                           </td><td>
                           <a href="{$baseActionURL}?module=batch_clone&amp;current=bp_clone&amp;stage=OPTIONSET&amp;mode=VALIDATION" method="POST">
                           <img border="0" src="media/neuragenix/buttons/next_enabled.gif" alt="Next"
                              onclick="javascript:document.next_form.submit();"/>
                              </a>
                                                    
                           </td>
                              </tr>
                           </table>
                           </td>
		</tr>
                <tr>
                    <td colspan="3">
                         <hr/>
                    </td>
                </tr>
                    
	</table>
	<table width="100%">
		 
		<tr>
			<td class="neuragenix-form-required-text" width="50%">
					<xsl:value-of select="strErrorDuplicateKey" /><xsl:value-of select="strErrorRequiredFields" /><xsl:value-of select="strErrorInvalidDataFields" /><xsl:value-of select="strErrorInvalidData" />
			</td>
			<td class="neuragenix-form-required-text" width="50%" id="neuragenix-required-header" align="right">
			* = Required fields
			</td>

		</tr>
	</table>
	<table width="100%">
        
        <tr>
        <td>
        
        <span class="uportal-label">Cloning Biospecimen ID :  </span> <span class="uportal-text"><xsl:value-of select="BIOSPECIMEN_strBiospecimenID" /></span><br />
        
        <table width="100%">
           <xsl:if test="count(GenerationDetails) > 0">
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
                  
                  <td>
                     <span class="uportal-label">Amount</span>
                  </td>
                  <td>
                     <span class="uportal-label">Allocation Mode</span>
                  </td>
                  <td>
                  </td>
              </tr>
           </xsl:if>
        
           <xsl:for-each select="GenerationDetails">
           
           
           
              <tr class="uportal-input-text">
                 
              
                  <td>
                     <span class="uportal-input-text"><xsl:value-of select="SITE_strSiteName" /> </span>
                  </td>
                  
                  <td>
                     <span class="uportal-input-text"><xsl:value-of select="TANK_strTankName" /></span>
                  </td>
                  
                  
                  <td>
                     <span class="uportal-input-text"><xsl:value-of select="BOX_strBoxName" /></span>
                  </td>
                  <td>
                     <span class="uportal-input-text"><xsl:value-of select="TRAY_strTrayName"/></span>
                  </td>
                  
                  <td>
                     <span class="uportal-input-text"><xsl:value-of select="GD_AllocationAmount"/></span>
                  </td>
                  <td>
                     <span class="uportal-input-text">
                        <xsl:choose>
                           <xsl:when test="GD_AllocationMode=0">
                              Continuous
                           </xsl:when>
                           
                           <xsl:when test="GD_AllocationMode=1">
                              Fill Gaps
                           </xsl:when>
                           
                           <xsl:when test="GD_AllocationMode=2">
                              New Tray
                           </xsl:when>
                           
                           <xsl:when test="GD_AllocationMode=3">
                              No Allocation
                           </xsl:when>
                           <xsl:otherwise>
                              Unable to match
                           </xsl:otherwise>
                        </xsl:choose>
                     
                     </span>
                  </td>
                  <td>
                  <!-- if allocation has already occured, do not show this -->
                     <span class="uportal-input-text">
                         <xsl:choose>
                            <xsl:when test="GD_AllocationStatus='1100'">
                               Allocation Completed
                            </xsl:when>
                            <xsl:otherwise>
                               <a href="{$baseActionURL}?current=bp_clone&amp;stage=OPTIONSET&amp;mode=DELETEALLOCATION&amp;intAllocationID={GD_internalID}&amp;module=batch_clone">delete</a>
                            </xsl:otherwise>
                         </xsl:choose>
                     </span>
                  </td>
              </tr>
              </xsl:for-each>
           <!-- </table> -->
              
              
              <tr>
                 <td colspan="7">
                     <br />
                     <hr />
                 </td>
              </tr>
              
        
        
        
        
        <form name="addNewClone" action="{$baseActionURL}?module=BATCH_CLONE&amp;current=bp_clone&amp;stage=OPTIONSET&amp;mode=ADDNEWCLONE" method="POST" class="uportal-label">
           <input type="hidden" name="strInternalBiospecimenID" value="{strBiospecimenID}" />
           <input type="hidden" name="strBiospecimenID" value="{strBiospecimenID}" />
        
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
                  
                  <td>
                     <span class="uportal-label">Amount</span>
                  </td>
                  <td>
                     <span class="uportal-label">Allocation Mode</span>
                  </td>
                  <td>
                  </td>
              </tr>
              
              <tr class="uportal-input-text">
                  <td>
       
                      <!-- site -->
                      <!-- on change get the update from the server -->
                      
                      <select id="SITE" name="SITE_intSiteID" class="uportal-input-text" onChange="updateTank()">
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
                      <select id="TANK" onChange="updateBox()" name="TANK_intTankID" class="uportal-input-text">
                         <option selected="true" value="-1000">*</option>  
                      </select>
                  </td>
                  
                  
                  <td>
                      <!-- box -->
                      <select id="BOX" onChange="updateTray()" name="BOX_intBoxID" class="uportal-input-text">
                         <option value="-1000" selected="true">*</option>
                         </select>
                  </td>
                  <td>
                      <!-- tray -->
                      <select id="TRAY" name="TRAY_intTrayID" class="uportal-input-text">
                        <option value="-1000" selected="true">*</option> 
                      </select>
                  </td>
                  
                  <td>
                      <!-- amount -->
                      <input type="text" size="20" name="intAllocationAmount" class="uportal-input-text"/>
                  </td>
                  <td>
                      <!-- allocation mode -->
                      <select name="intAllocationMode" class="uportal-input-text">
                         <option value="1">Fill Gaps</option>
                         <option value="0">Continuous</option>
                         <option value="2">Create New Tray</option>
                         <option value="3">No Allocation</option>
                      </select>
                      

                  </td>
                  <td align="right">
                     <input type="submit" name="SAVELINE" value="Save Details" class="uportal-button" />
                  </td>
                  <td>
        
        
           </td>
        </tr>
        
        </form>
        
                
	</table>
	
        
        <table>
           <tr>   
              <td align="right">
                 <form name="CANCEL" action="{$baseActionURL}?module=core&amp;action=view_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={BIOSPECIMEN_intBiospecimenID}" method="POST">
                    <input type="submit" name="CANCEL_CLONING" value="Cancel Cloning" class="uportal-button"/> 
                 </form>
              </td>
              <td>
                 
                 <form name="NEXT" action="{$baseActionURL}?module=batch_clone&amp;current=bp_clone&amp;stage=OPTIONSET&amp;mode=VALIDATION" method="POST">
                    <input type="submit"  name="VALIDATE_NEW_ALLOCATIONS" value="Next >" class="uportal-button"/> 
                 </form> 
              </td>
           </tr>
        </table>
        
        </td>
        </tr>
        </table>
        
        
        
  </xsl:template>

      
       
 
</xsl:stylesheet>