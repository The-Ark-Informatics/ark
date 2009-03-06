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
  
  <xsl:template match="vial_calculation">
  <!-- Get the parameters from the channel class -->
  <xsl:param name="strBiospecimenID"><xsl:value-of select="strBiospecimenID" /></xsl:param>
  <xsl:param name="strBiospecOtherID"><xsl:value-of select="strBiospecOtherID" /></xsl:param>
  <xsl:param name="intBiospecStudyID"><xsl:value-of select="intBiospecStudyID" /></xsl:param>
  <xsl:param name="intInternalPatientID"><xsl:value-of select="intInternalPatientID" /></xsl:param>
  <xsl:param name="intBiospecParentID"><xsl:value-of select="intBiospecParentID" /></xsl:param>
  <xsl:param name="strBiospecParentID"><xsl:value-of select="strBiospecParentID" /></xsl:param>
     <xsl:param name="finishButton"><xsl:value-of select="finishButton" /></xsl:param>
  
        
        <!-- warning :: the following script is pretty complex... 
                        please check with daniel before updating/fixing -->
            
        <Script>

/*
 *  Available Allocations Display Script
 *  Copyright (C) Neuragenix Pty Ltd 2005
 *  Author : Daniel Murley
 *  email : dmurley@neuragenix.com
 *
 *
 *
 *
 */






		// InvContainer OBJECT
		function InvContainer (ID, Name, Type)
		{
		   // Properties
		   this.ID = ID;
		   this.Name = Name;
		   this.Type = Type;
		   this.subContainers = new Array();
	           this.otherID = -1;
                   
		   // Method Declarations
	
		   this.addSubContainer = InvContainer_addSubContainer;
		   this.getSubContainer = InvContainer_getSubContainer;
		   this.getID = InvContainer_getID;
		   this.getName = InvContainer_getName;
		   this.getAvailableSpaces = InvContainer_getAvailableSpaces;
		   this.setAvailableSpaces = InvContainer_setAvailableSpaces;
                   this.getOtherID = InvContainer_getOtherID;
                   this.setOtherID = InvContainer_setOtherID;
	
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
	
	        function InvContainer_setOtherID(otherID)
                {
                   this.otherID = otherID;
                }
                
                function InvContainer_getOtherID()
                {
                   return otherID;
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
	
	
	
	
	
	       // ---- available allocations object
	       
	       
	       function AvailableAllocations (ID, inventoryArray, type)
		{
		   // Properties
		   this.ID = ID;
		   this.inventoryArray = inventoryArray;
		   this.type = type;
		   // Method Declarations
	
		   this.getID = AvailableAllocations_getID;
		   this.getArray = AvailableAllocations_getArray;
	           this.getType = AvailableAllocations_getType;
		}

		function AvailableAllocations_getID()
		{
		   return this.ID;
		}
		
		function AvailableAllocations_getArray()
		{
		   return this.inventoryArray;
		}
		
		function AvailableAllocations_getType()
		{
		   return this.type;
		}
		
		
		
		// --- end available allocations object
	       
	       
	       
	       // ---- available cells object
	       
	       
	       function AvailableCells (trayID, cellAllocID, userData)
		{
		   // Properties
		   this.trayID = trayID;
		   this.cellAllocID = cellAllocID;
		   this.userData = userData;
		   // Method Declarations
	
		   this.getTrayID = AvailableCells_getTrayID;
		   this.getCellAllocID = AvailableCells_getCellAllocID;
	           this.getUserData = AvailableCells_getUserData;
		}

		function AvailableCells_getTrayID()
		{
		   return this.trayID;
		}
		
		function AvailableCells_getCellAllocID()
		{
		   return this.cellAllocID;
		}
		
		function AvailableCells_getUserData()
		{
		   return this.userData;
		}
		
		
		
		// --- end available cells object
	       
	
	
	
	
	
	
		var myInventory = new Array();
	        var cellsAvailable = new Array();
		
		function buildInventoryContinuous(allocID, siteID, siteName, tankID, tankName, boxID, boxName, trayID, trayName, cellAllocID, userData)
		{
		   
		   // call the standard buildInventory to put inventory data in
		   buildInventory(allocID, siteID, siteName, tankID, tankName, boxID, boxName, trayID, trayName);
		   
		   // add the allocation details
		   var myCellAlloc = new AvailableCells(trayID, cellAllocID, userData);
		   
		   cellsAvailable[cellsAvailable.length] = myCellAlloc;
		
		}
		
		
		
		function buildInventory(allocID, siteID, siteName, tankID, tankName, boxID, boxName, trayID, trayName)
		{
	
		  
		   // check the existing inventory object for the values
		   
		   var selectedInventory = null;
		   
		   for (var i=0; i &lt; myInventory.length; i++)
		   {
		      if (myInventory[i].getID() == allocID)
		      {
		         selectedInventory = myInventory[i].getArray();
		      }
		   }
		
		   if (selectedInventory == null) // we didnt find one, so we need to add it
		   {
		      
		      var tempSelectedArray = new Array();
		      var tempAllocObj = new AvailableAllocations(allocID, tempSelectedArray, 'FILL');
		      selectedInventory = tempSelectedArray;
		      myInventory[myInventory.length] = tempAllocObj;
		   }
		
		   for (var i =0; i &lt; selectedInventory.length; i++)
		   {
	
		      if (selectedInventory[i].getID() == siteID)
		      {
			 var tank = selectedInventory[i].getSubContainer(tankID);
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
			       return;  // correctly handle double ups
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
			    selectedInventory[i].addSubContainer(objTank);
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
		  selectedInventory[selectedInventory.length] = site;
	
		}
		
		// this will need to be updated
		
		
		function updateTank(allocID)
		{
		   if (document.getElementById('SITE_' + allocID)== null)
		   {
				return;
		   }		   
		   var curr = document.getElementById('SITE_' + allocID).value;
		   var container = getSite(curr, allocID).subContainers;   
		   var element =  document.getElementById('TANK_' + allocID);
		
		   resetOptions(element);
		
		
		      for (var i = 0; i &lt; container.length; i++)
		      {
		       
			var newOpt = new Option(container[i].getName(), container[i].getID())
			element.options[element.options.length] = newOpt;
			
		
		
		      }
		
		   //  buildDropdown (myInventory[0], true);      
		   updateBox(allocID);
		
		
		}
	
	
		function resetOptions (arrayset)
		{
		   for (var i = 0; i &lt; arrayset.options.length; i++)
		   {
		      //arrayset.options[i] = null;
                      // arrayset.remove(i);
                      
                      //arrayset.options[i].value = "";
                      //arrayset.options[i].text = "";
                      
                      
                      
                    arrayset.options[i]=null;

                      
		   }
                   arrayset.options.length=0;
                   
		   // alert ('array length : ' + arrayset.options.length);
		  
		}
	
	
	function getSite(siteID, allocID)
	{
	
	
	   // get the object we're interested in
	   var selectedInventory = myInventory[allocID].getArray();
	
	   for (var i = 0; i &lt; selectedInventory.length; i++)
	   {
	     if (selectedInventory[i].getID() == siteID)
	     {
		return selectedInventory[i];
	     }
	   }
	
	   return null;
	
	}
	
	
	function updateBox(allocID)
	{
	   // alert(updating yo box);
	   var siteID = document.getElementById('SITE_' + allocID).value;
	   var curr = document.getElementById('TANK_' + allocID).value;
	   // alert(going to get site  + siteID +  and tank :  + curr);
	   
	   var siteContainer = getSite(siteID, allocID);
	   
	   // if (siteContainer == null)
	   //   alert (and its null);
	   
	   var tankContainer = siteContainer.getSubContainer(curr);
	   // if (tankContainer == null) alert('tank container is null');
	   
	   var element =  document.getElementById('BOX_' + allocID);
	   var boxContainer = tankContainer.subContainers;
	   
	   // if (boxContainer == null)
	   //   alert (box container is null);
	   
	   
	   
	   
	   
	
	
	   resetOptions(element);
	
	
	      for (var i = 0; i &lt; boxContainer.length; i++)
	      {
	
		var newOpt = new Option(boxContainer[i].getName(), boxContainer[i].getID())
		element.options[element.options.length] = newOpt;
		
	
	
	      }
	
	   //  buildDropdown (myInventory[0], true);
	   updateTray(allocID);
	
	}
	
	
	function updateCells(allocID)
	{
	   // alert('update cells called');
	   var trayElement = document.getElementById('TRAY_' + allocID);
	
	   var currOptionValue = trayElement.options[trayElement.selectedIndex].value;
	   
	   // alert('curr opt value' + currOptionValue);
	   
	   var element = document.getElementById('CELLS_' + allocID);
	
	   
	   resetOptions(element);
	
	   // alert('cells available - ' + cellsAvailable.length);
	   for (var i = 0; i &lt; cellsAvailable.length; i++)
	   {
		
		var tempAC = cellsAvailable[i];
		
		if (tempAC.getTrayID() == currOptionValue)
	        {
		        var newOpt = new Option(tempAC.getUserData(), tempAC.getCellAllocID())
		//	alert('outing UD - ' + tempAC.getUserData() + 'cell alloc - ' + tempAC.getCellAllocID());
			element.options[element.options.length] = newOpt;
		}
	
	
	   }
	
	
	}
	
	
	
	
	
	function updateTray(allocID)
	{
	   // alert(updating trays);
	   var siteID = document.getElementById('SITE_' + allocID).value;
	   var curr = document.getElementById('TANK_' + allocID).value;
	   var siteContainer = getSite(siteID, allocID);
	   
	   // if (siteContainer == null)
	   //   alert (and its null);
	   
	   var tankContainer = siteContainer.getSubContainer(curr);
	   // if (tankContainer == null) alert(tank container is null);
	   
	   var boxID =  document.getElementById('BOX_' + allocID).value;
	   var boxContainer = tankContainer.getSubContainer(boxID);
	   
	   // if (boxContainer == null) alert (box container is null);
	   
	   var trays = boxContainer.subContainers;
	   
	   var element = document.getElementById('TRAY_' + allocID);
	
	
	   resetOptions(element);
	
	
	      for (var i = 0; i &lt; trays.length; i++)
	      {
		
		var newOpt = new Option(trays[i].getName(), trays[i].getID())
		element.options[element.options.length] = newOpt;
		
	
	
	      }
	
	      if (document.getElementById('CELLS_' + allocID))
	      {
                 // alert('found');
	         updateCells(allocID);
	      }
              
	      
	}
	
	function updateAll(allocID)
        {
           updateTank(allocID);
           updateBox(allocID);
           updateTray(allocID);
           
            if (document.getElementById('CELLS_' + allocID))
	    {
               updateCells(allocID);
	    }
        
        
        }
                     
        
        
        </Script>
        
        <xsl:for-each select="GenerationDetails">
           <xsl:variable name="internalID" select="GD_internalID" />
           
           <xsl:for-each select="GD_AvailableLocations">
              <!-- this code needs to also build the remaining drop downs - ie not wait for the click! -->
              <xsl:choose>
                 <xsl:when test="count(AL_Continuous) &gt; 0">
                     
                     <script>
                          buildInventoryContinuous('<xsl:value-of select="$internalID" />','<xsl:value-of select="SITE_intSiteID" />',
                          '<xsl:value-of select="SITE_strSiteName" />',
                          '<xsl:value-of select="TANK_intTankID" />',
                          '<xsl:value-of select="TANK_strTankName" />',
                          '<xsl:value-of select="BOX_intBoxID" />',
                          '<xsl:value-of select="BOX_strBoxName" />',
                          '<xsl:value-of select="TRAY_intTrayID" />',
                          '<xsl:value-of select="TRAY_strTrayName" />'
                          <xsl:for-each select="AL_Continuous">
                              , '<xsl:value-of select="AL_Continuous_ID" />', '<xsl:value-of select="AL_Continuous_UserData" />'
                          </xsl:for-each>);
                          
                     </script>
                 
                 </xsl:when>
                 <xsl:otherwise>
                      <script>
                          buildInventory('<xsl:value-of select="$internalID" />','<xsl:value-of select="SITE_intSiteID" />',
                          '<xsl:value-of select="SITE_strSiteName" />',
                          '<xsl:value-of select="TANK_intTankID" />',
                          '<xsl:value-of select="TANK_strTankName" />',
                          '<xsl:value-of select="BOX_intBoxID" />',
                          '<xsl:value-of select="BOX_strBoxName" />',
                          '<xsl:value-of select="TRAY_intTrayID" />',
                          '<xsl:value-of select="TRAY_strTrayName" />'
                          <xsl:for-each select="AL_Continuous">
                              , '<xsl:value-of select="AL_Continuous_ID" />', '<xsl:value-of select="AL_Continuous_UserData" />'
                          </xsl:for-each>);
                          
                      </script>
                 </xsl:otherwise>
                 
              </xsl:choose>
                 
                 
              
              
                            
          
         </xsl:for-each>
        </xsl:for-each>
        
        
        
	<table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Vial Calculation - Available Locations<br/>
			</td>
		         
		            <td align="right">
		               <form name="back_form" action="{$baseActionURL}?module=vial_calculation&amp;action=back" method="post">
		                  <!--<input type="submit" value="&lt; Back" class="uportal-input-text"/> -->
		                  <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
		                     alt="Previous" onclick="javascript:document.back_form.submit();"/>
		                  <img border="0" src="media/neuragenix/buttons/next_disabled.gif"
		                     alt="Next"/>
		                </form>
		            </td>
		</tr>
	      <tr>
	         <td colspan="2"><hr/></td>
	      </tr>
	         
	</table>
	<table width="100%">
		 
		<tr>
			<td class="neuragenix-form-required-text" width="100%" id="neuragenix-required-header" align="center">
		        Please check the following is correct before allocation.  After an allocation is finalised, it cannot be reversed.
			</td>

		</tr>
                <tr>
                    <td class="neuragenix-form-required-text">
                       <xsl:value-of select="strMessage" />
                    </td>
                </tr>
	</table>
        
        <!-- display the list of the available spaces -->
        
        
        <table width="100%" class="uportal-text">
            <tr class="uportal-text">
               <td><xsl:value-of select="SITE_strTitleDisplay"/>
               </td>
               <td><xsl:value-of select="TANK_strTitleDisplay"/>
               </td>
               <td><xsl:value-of select="BOX_strTitleDisplay"/>
               </td>
               <td><xsl:value-of select="TRAY_strTitleDisplay"/>
               </td>
	       <td>Amount
               </td>
	       <td>Allocation Mode
               </td>
	       <td>Availability/Status
               </td>
	       <td> <!-- drop down where required -->
               </td>
	       <td> <!-- confirm allocation -->
               </td>
	       <td> <!-- Cancel allocation -->
               </td>
	       
            </tr>
      
	  <xsl:for-each select="GenerationDetails">    
            <xsl:choose>
            <!-- check if the system has decided if there is available spaces or not -->
                <xsl:when test="GD_AvailableLocations/GD_NoAvailable='true'">
                   <!-- display data based on whatever they selected and mark as unavailable -->   

                   <td>
                       <xsl:value-of select="SITE_strSiteName" />
                   </td>
                   <td>
                       <xsl:value-of select="TANK_strTankName" />
                   </td>
                   <td>
                       <xsl:value-of select="BOX_strBoxName" />
                   </td>
                   <td>
                       <xsl:value-of select="TRAY_strTrayName" />
                   </td>
                   <td><xsl:value-of select="GD_AllocationAmount" />
                   </td>
                   <td><xsl:value-of select="GD_AllocationMode" />
                   </td>
                   <td><font color = "red"><b>No available spaces</b></font>
                   </td>
                   <td>
                   </td>
                   <td> <!--  <a>Create Tray</a> -->
                   </td>
                   <td> <!-- <a>Cancel allocation</a> -->
                   </td> 
                   
                </xsl:when>

                <xsl:otherwise>
                    <tr class="uportal-text">
                    
                       <xsl:choose>
                           <xsl:when test="(GD_AllocationMode=0 or GD_AllocationMode=1) and GD_AllocationStatus=-1">
                               <td><!--<xsl:value-of select="SITE_strSiteName" /><br /> -->
                               <!-- drop down goes here with available allocs -->
                                   <select name="SITE_{GD_internalID}" id="SITE_{GD_internalID}" onChange="updateTank('{GD_internalID}')" onFocus="updateTank('{GD_internalID}')">
                                   <script>    
                                      var currInventory = myInventory[<xsl:value-of select="GD_internalID" />].getArray();
                                      for (var i = 0; i &lt; currInventory.length; i++)
                                      {
                                         document.writeln('&lt;OPTION value=' + currInventory[i].getID() + '&gt;');
                                         document.writeln(currInventory[i].getName());
                                         document.writeln('&lt;/OPTION&gt;');   
                                      }
                                   </script>

                                   </select>

                               </td>
                               <td><!--<xsl:value-of select="TANK_strTankName" /><br />-->
                               <!-- drop down goes here with available allocs -->
                                   <select name="TANK_{GD_internalID}" id="TANK_{GD_internalID}" onChange="updateBox('{GD_internalID}')"/>

                               </td>
                               <td><!--<xsl:value-of select="BOX_strBoxName" /><br />-->
                               <!-- drop down goes here with available allocs -->
                                   <select name="BOX_{GD_internalID}" id="BOX_{GD_internalID}" onChange="updateTray('{GD_internalID}')"/>

                               </td>
                               <td><!--<xsl:value-of select="TRAY_strTrayName" /><br />-->
                               <!-- drop down goes here with available allocs -->
                                   <xsl:choose>
                                      <xsl:when test="count(./GD_AvailableLocations/AL_Continuous) &gt; 0">
                                         <select name="TRAY_{GD_internalID}" id="TRAY_{GD_internalID}" onChange="updateCells('{GD_internalID}')" />
                                      </xsl:when>
                                      <xsl:otherwise> <!-- fill gaps -->
                                         <select name="TRAY_{GD_internalID}" id="TRAY_{GD_internalID}" onChange="document.DOALLOCATION_{GD_internalID}.locationID.value=this.value" />
                                      </xsl:otherwise>
                                   </xsl:choose>   
                               </td>
                           </xsl:when>
                          <!-- need to put code here to display chosen values-->
                           <xsl:otherwise>
                              <td>
                                 <xsl:value-of select="SITE_strSiteName" />
                              </td>
                              <td>
                                 <xsl:value-of select="TANK_strTankName" />
                              </td>
                              <td>
                                 <xsl:value-of select="BOX_strBoxName" />
                              </td>
                              <td>
                                 <xsl:value-of select="TRAY_strTrayName" />
                              </td>  
                              </xsl:otherwise>
                           </xsl:choose>
                           
                       <td><xsl:value-of select="GD_AllocationAmount" /><br />
                       
                       
                       
                       </td>
                       <td>
                          <xsl:choose>
                             <xsl:when test="GD_AllocationMode=0">
                                Continuous
                             </xsl:when>
                             <xsl:when test="GD_AllocationMode=1">
                                Fill Gaps
                             </xsl:when>
                             <xsl:when test="GD_AllocationMode=2">
                                Create New Tray
                             </xsl:when>
                             <xsl:when test="GD_AllocationMode=3">
                                No Allocation
                             </xsl:when>
                             
                             <xsl:otherwise>
                                Undefined
                             </xsl:otherwise>
                           </xsl:choose>
                       
                       
                       </td>
                       <td>
                          <xsl:choose>
                              <xsl:when test="string(GD_AllocationStatus)='-1'">
                                   <xsl:choose>
                                      <xsl:when test="GD_AllocationMode=0">
                                          <font color = "green"><b>Ready to allocate</b></font>
                                      </xsl:when>
                                      <xsl:when test="GD_AllocationMode=1">
                                          <font color = "green"><b>Ready to allocate</b></font>
                                      </xsl:when>
                                      <xsl:when test="GD_AllocationMode=2">
                                          <font color = "green"><b>Ready to create tray and allocate</b></font>
                                      </xsl:when>
                                      <xsl:when test="GD_AllocationMode=3">
                                          <font color = "green"><b>Ready to create specimens</b></font>
                                      </xsl:when>
                                      <xsl:otherwise>
                                          No comment defined for this mode 
                                      </xsl:otherwise>
                                   </xsl:choose>
                              </xsl:when>
                              <xsl:when test="GD_AllocationStatus='1100'">
                                  <font color="green"><b>Allocation Complete</b></font>
                              </xsl:when>
                              
                           </xsl:choose>
                                   
                       </td>
                       <td> <!-- OLD TEST CODE - will display all allocations!
                           <xsl:if test="GD_AllocationMode=0">
                               <Select name="allocationSelection_{AL_internalID}">
                                  <xsl:for-each select="GD_AvailableLocations/AL_Continuous">
                                     <option value="{AL_Continuous_ID}" onChange="document.DOALLOCATION_{GD_internalID}.locationID.value={AL_Continuous_ID}"><xsl:value-of select="AL_Continuous_UserData" /></option>
                                  </xsl:for-each>
                               </Select>
                           </xsl:if>
                           -->
                           
                           <xsl:if test="GD_AllocationMode=0">
                              <select name="CELLS_{GD_internalID}" id="CELLS_{GD_internalID}"  onChange="document.DOALLOCATION_{GD_internalID}.locationID.value=this.value"/>
                           </xsl:if>
                               
                       </td>
                       <td>
                          <xsl:if test="GD_AllocationStatus='-1'"> <!-- not yet allocated -->
                              <form name="DOALLOCATION_{GD_internalID}" id="DOALLOCATION_{GD_internalID}" action="{$baseActionURL}?module=vial_calculation&amp;action=preview_tray_selection&amp;intAllocationID={GD_internalID}&amp;subaction=allocate_option" method="post">
                                   
                                  <input type="hidden" name="locationID" value="" />
                                  <xsl:choose>
                                     <xsl:when test="GD_AllocationMode=0 or GD_AllocationMode=1">
                                        
                                        <input type="button" onclick="javascript:submitAllocation({GD_internalID});" class="uportal-button" value="Preview this selection"></input>
                                     </xsl:when>
                                     <xsl:otherwise>
                                         <input type="button" onclick="javascript:document.DOALLOCATION_{GD_internalID}.submit()" class="uportal-button" value="Preview this selection"></input>
                                     </xsl:otherwise>
                                  </xsl:choose>
                                  
                              </form>
                          </xsl:if>
                       </td>
                       <td> 
                       <!-- 
                           <xsl:if test="not(GD_AllocationStatus='1100')">
                              <a>Cancel allocation</a>
                           </xsl:if> -->
                       </td>
                    </tr>
                </xsl:otherwise>
            </xsl:choose>
            
          
	   
	   </xsl:for-each>
	    
        </table>
	<br />
        <br />
        <hr />
        <table width="100%">
            <tr>
               
               <!--
                <td align ="left">
                   <input type="button" value="Cancel" class="uportal-button"/>
                </td> -->
               <!-- only allow proceeding if one of Allocation status is completed -->
               <xsl:if test="contains($finishButton,'true')">
                <td align="left">
                   <form name="displayReport" action="{$baseActionURL}?module=vial_calculation&amp;action=finish_vial_calculation" method="post">
                      <input type="submit" value="Finish and Generate Report &gt;" class="uportal-button"/>
                   </form>
                </td>
                  </xsl:if>
            </tr>
        </table>
        
         <script>
        <xsl:for-each select="GenerationDetails">
        
        	updateTank(<xsl:value-of select="GD_internalID" />);
        
        </xsl:for-each>
        </script>
        
        <script>
        
        function submitAllocation(allocID)
        {
           var form = document.getElementById('DOALLOCATION_' + allocID);
           form.locationID.value = ' ';
           var notSelected = false;
           
           if (document.getElementById('CELLS_' + allocID))
           {
              // we have cells
              var cellElement = document.getElementById('CELLS_' + allocID);
              if (cellElement.selectedIndex == -1)
                 notSelected = true; 
              else
                 form.locationID.value = cellElement.options[cellElement.selectedIndex].value;
           
              
              
           }
           else
           {
              var trayElement = document.getElementById('TRAY_' + allocID);
              
              if (trayElement.selectedIndex == -1)
                 notSelected = true;
              else
                 form.locationID.value = trayElement.options[trayElement.selectedIndex].value;
              
              
           }  
           
           if (notSelected == true)
              alert('You have not yet selected a tray, or cell set to allocate to');
           else
              form.submit();
        }
        
        </script>
  </xsl:template>

      
       
 
</xsl:stylesheet>
