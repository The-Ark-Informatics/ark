<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:template name="javascript_code">
    <script language="javascript" >
        
        function confirmDelete(aURL) {
        
        var confirmAnswer = confirm('Are you sure you want to delete this record?');
        
        
        if(confirmAnswer == true){
        window.location=aURL + '&amp;delete=true';
        }
        
        
        } 
        
        function jumpTo(aURL)
        {
        window.location=aURL;
        }
        
        function confirmClear(aURL) {
        
        var confirmAnswer = confirm('Are you sure you want to clear the fields?');
        
        
        if(confirmAnswer == true){
        window.location=aURL + '&amp;clear=true';
        }
        //else{
        //		window.location=aURL + '&amp;delete=false';
        //	}
        
        
        }
        
        var openImg = new Image();
        openImg.src = "media/neuragenix/icons/open.gif";
        var closedImg = new Image();
        closedImg.src = "media/neuragenix/icons/closed.gif";
        
        function showBranch(branch){
        var objBranch = document.getElementById(branch).style;
        if(objBranch.display=="block")
        objBranch.display="none";
        else
        objBranch.display="block";
        swapFolder('I' + branch);
        }
        
        function swapFolder(img){
        objImg = document.getElementById(img);
        if(objImg.src.indexOf('closed.gif')>-1)
        objImg.src = openImg.src;
        else
        objImg.src = closedImg.src;
        }

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
