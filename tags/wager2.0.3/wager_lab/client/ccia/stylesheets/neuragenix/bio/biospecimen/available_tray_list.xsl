<?xml version="1.0" encoding="utf-8"?>
<!-- available_tray_list.xsl, part of the Biospecimen channel -->
<!-- author: Huy Hoang -->
<!-- date: 23/07/2004 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <!--xsl:include href="./biospecimen_menu.xsl"/-->
    <!-- <xsl:include href="../../common/common_btn_name.xsl"/> -->
    <xsl:output method="html" indent="yes"/>
    <xsl:param name="inventoryChannelURL">inventoryChannelURL_false</xsl:param>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:param name="inventoryChannelTabOrder">inventoryChannelTabOrder</xsl:param>
    <xsl:param name="strErrorMessage">
        <xsl:value-of select="strErrorMessage"/>
    </xsl:param>
    <xsl:template match="biospecimen">
          <xsl:param name="intInternalPatientID">
            <xsl:value-of select="intInternalPatientID"/>
        </xsl:param>
        <xsl:param name="intBiospecimenID">
            <xsl:value-of select="BIOSPECIMEN_intBiospecimenID"/>
        </xsl:param>
        <xsl:param name="strBiospecimenID">
            <xsl:value-of select="BIOSPECIMEN_strBiospecimenID"/>
        </xsl:param>
        <xsl:param name="strInitialBiospecSampleType">
            <xsl:value-of select="sBIOSPECIMEN_trInitialBiospecSampleType"/>
        </xsl:param>
        <xsl:param name="VIAL_CALCULATION_intVialStored">
            <xsl:value-of select="VIAL_CALCULATION_intVialStored"/>
        </xsl:param>
        <xsl:param name="VIAL_CALCULATION_intLocationNeeded">
            <xsl:value-of select="VIAL_CALCULATION_intLocationNeeded"/>
        </xsl:param>
        <xsl:param name="VIAL_CALCULATION_intPlasma">
            <xsl:value-of select="VIAL_CALCULATION_intPlasma"/>
        </xsl:param>
        <xsl:param name="VIAL_CALCULATION_intGDNA">
            <xsl:value-of select="VIAL_CALCULATION_intGDNA"/>
        </xsl:param>
        
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
        
        
        
        <script language="javascript"> function jumpTo(aURL){ window.location=aURL; } </script>
        <link rel="stylesheet" type="text/css" href="stylesheets/neuragenix/bio/xmlsideTree.css"/>
        <table width="100%">
            <tr valign="top">
                <td id="neuragenix-border-right" width="15%" class="uportal-channel-subtitle"> Menu<hr/>
                    <br/>
                    <a href="{$baseActionURL}?current=biospecimen_search">Search samples</a>
                    <br/>
                    <xsl:if test="count(branch) &gt; 0 or count(leaf) &gt; 0">
                        <xsl:apply-templates/>
                    </xsl:if>
                    <br/>
                    <br/>
                    <xsl:if test="count(/biospecimen/patient_details) &gt; 0">
                        <table width="100%" border="0" cellspacing="0" cellpadding="2">
                            <tr class="uportal-input-text">
                                <td width="30%">
                                    <b>
                                        <xsl:value-of
                                            select="/biospecimen/patient_details/PATIENT_strPatientIDDisplay"
                                        />:</b>
                                </td>
                                <td width="70%" align="right">
                                    <xsl:value-of
                                        select="/biospecimen/patient_details/PATIENT_strPatientID"/>
                                </td>
                            </tr>
                            <tr class="uportal-input-text">
                                <td width="30%">
                                    <b>
                                        <xsl:value-of
                                            select="/biospecimen/patient_details/PATIENT_strHospitalURDisplay"
                                        />: </b>
                                </td>
                                <td width="70%" align="right">
                                    <xsl:value-of
                                        select="/biospecimen/patient_details/PATIENT_strHospitalUR"
                                    />
                                </td>
                            </tr>
                            <tr class="uportal-input-text">
                                <td width="30%">
                                    <b>
                                        <xsl:value-of
                                            select="/biospecimen/patient_details/PATIENT_strSurnameDisplay"
                                        />: </b>
                                </td>
                                <td width="70%" align="right">
                                    <xsl:value-of
                                        select="/biospecimen/patient_details/PATIENT_strSurname"/>
                                </td>
                            </tr>
                            <tr class="uportal-input-text">
                                <td width="30%">
                                    <b>
                                        <xsl:value-of
                                            select="/biospecimen/patient_details/PATIENT_strFirstNameDisplay"
                                        />: </b>
                                </td>
                                <td width="70%" align="right">
                                    <xsl:value-of
                                        select="/biospecimen/patient_details/PATIENT_strFirstName"/>
                                </td>
                            </tr>
                            <tr class="uportal-input-text">
                                <td width="30%">
                                    <b>
                                        <xsl:value-of
                                            select="/biospecimen/patient_details/PATIENT_dtDobDisplay"
                                        />: </b>
                                </td>
                                <td width="70%" align="right">
                                    <xsl:value-of
                                        select="/biospecimen/patient_details/PATIENT_dtDob"/>
                                </td>
                            </tr>
                            <tr class="uportal-input-text">
                                <td width="30%">
                                    <b>First referring doctor:</b>
                                </td>
                                <td width="70%" align="right">
                                    <xsl:value-of
                                        select="/biospecimen/patient_details/ADMISSIONS_strFirstRefDoctor"
                                    />
                                </td>
                            </tr>
                            <tr class="uportal-input-text">
                                <td width="30%">
                                    <b>Last referring doctor:</b>
                                </td>
                                <td width="70%" align="right">
                                    <xsl:value-of
                                        select="/biospecimen/patient_details/ADMISSIONS_strLastRefDoctor"
                                    />
                                </td>
                            </tr>
                        </table>
                    </xsl:if>
                    <br/>
                    <br/>
                    <!-- end of Patient details-->
                    <!-- flagged biospecimen -->
                    <xsl:if test="count(/biospecimen/flagged) &gt; 0"> Flagged Records : <table
                            border="0" width="100%">
                            <xsl:for-each select="/biospecimen/flagged">
                                <tr>
                                    <td class="uportal-input-text">
                                        <xsl:variable name="intBiospecimenID">
                                            <xsl:value-of select="intBiospecimenID"/>
                                        </xsl:variable>
                                        <a
                                            href="{$baseActionURL}?uP_root=root&amp;current=biospecimen_view&amp;intBiospecimenID={$intBiospecimenID}&amp;intInternalPatientID={$intInternalPatientID}">
                                            <xsl:value-of select="strBiospecimenID"/>
                                        </a>
                                    </td>
                                </tr>
                            </xsl:for-each>
                        </table>
                    </xsl:if>
                    <!-- end of flagged biospecimen -->
                </td>
                <td width="5%"/>
                <td width="80%">
                    <table width="100%">
                        <tr>
                            <td class="uportal-channel-subtitle"> Step 2 - Allocate Sub Sample
                                to Inventory<br/>
                              
                            </td>
                            <td align="right">
                                <form name="back_form" action="{$baseActionURL}?uP_root=root" method="POST">
                                    <input type="hidden" name="action" value="back"/>
                                    <input type="hidden" name="module" value="vial_calculation"/>
                                    <input type="hidden" name="BIOSPECIMEN_intBiospecimenID"
                                        value="{$intBiospecimenID}"/>
                                </form>
                                
                                <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
                                    alt="Previous" onclick="javascript:document.back_form.submit();"/>
                                <img border="0" src="media/neuragenix/buttons/next_disabled.gif"
                                    alt="Next"/>
                            </td>
                        </tr>
                        <tr><td colspan="3"> <hr/></td></tr>
                    </table>
                    <table width="100%">
                        <tr>
                            
                            <td class="neuragenix-form-required-text" width="80%">
                                <xsl:value-of select="strErrorDuplicateKey"/>
                                <xsl:value-of select="strErrorRequiredFields"/>
                                <xsl:value-of select="strErrorInvalidDataFields"/>
                                <xsl:value-of select="strErrorInvalidData"/>
                                <xsl:value-of select="strErrorChildStillExist"/>
                                <xsl:value-of select="strErrorBiospecTypeChange"/>
                                <xsl:value-of select="strErrorMessage"/>
                                <xsl:value-of select="strLockError"/>
                            </td>
                            <td class="neuragenix-form-required-text" width="20%"
                                id="neuragenix-required-header" align="right"/>
                        </tr>
                    </table>
                    <form name="allocation_form" action="{$baseActionURL}?current=available_tray_list" method="post">
                        <input type="hidden" name="TRAY_intTrayID" value="0"></input>
                        <table width="100%">
                            <tr>
                                <!--<td width="10%" class="uportal-label"/> -->
                                <td width="20%" class="uportal-channel-table-header">
                                    <xsl:value-of select="SITE_strSiteNameDisplay"/>
                                </td>
                                <td width="20%" class="uportal-channel-table-header">
                                    <xsl:value-of select="TANK_strTankNameDisplay"/>
                                </td>
                                <td width="20%" class="uportal-channel-table-header">
                                    <xsl:value-of select="BOX_strBoxNameDisplay"/>
                                </td>
                                <td width="20%" class="uportal-channel-table-header">
                                    <xsl:value-of select="TRAY_strTrayNameDisplay"/>
                                </td>
                                <td width="10%" class="uportal-label"/>
                            </tr>
                            
                            
                            <!-- drop down stuff -->
                            <xsl:for-each select="search_location">
                                <script>
                                    buildInventory(0,'<xsl:value-of select="SITE_intSiteID" />',
                                    '<xsl:value-of select="SITE_strSiteName" />',
                                    '<xsl:value-of select="TANK_intTankID" />',
                                    '<xsl:value-of select="TANK_strTankName" />',
                                    '<xsl:value-of select="BOX_intBoxID" />',
                                    '<xsl:value-of select="BOX_strBoxName" />',
                                    '<xsl:value-of select="TRAY_intTrayID" />',
                                    '<xsl:value-of select="TRAY_strTrayName" />');
                                    
                                </script>
                                
                                </xsl:for-each>
                            
                            
                            
                            <tr>
                                <td>
                                <!-- drop down goes here with available allocs -->
                                <select name="SITE_0" id="SITE_0" onChange="updateTank(0)" onFocus="updateTank(0)">
                                    <script>    
                                        var currInventory = myInventory[0].getArray();
                                        for (var i = 0; i &lt; currInventory.length; i++)
                                        {
                                        document.writeln('&lt;OPTION value=' + currInventory[i].getID() + '&gt;');
                                        document.writeln(currInventory[i].getName());
                                        document.writeln('&lt;/OPTION&gt;');   
                                        }
                                    </script>
                                    
                                </select> 
                                </td>
                                <td>    
                                <select name="TANK_0" id="TANK_0" onChange="updateBox('0')"/>
                                </td>                
                                <td>
                                    <!-- drop down goes here with available allocs -->
                                    <select name="BOX_0" id="BOX_0" onChange="updateTray('0')"/>
                                    
                                </td>
                                <td>
                                    <select name="TRAY_0" id="TRAY_0" onChange="javascript:document.allocation_form.TRAY_intTrayID.value=this.value;" />
                                </td>
                                </tr>
                               
                            
                            
                            
                            
                            <!--
                            <xsl:for-each select="search_location">
                                <xsl:variable name="TRAY_intTrayID">
                                    <xsl:value-of select="TRAY_intTrayID"/>
                                </xsl:variable>
                                <tr>
                                    <td width="10%" class="uportal-label">
                                        <input type="radio" name="TRAY_intTrayID">
                                            <xsl:attribute name="value">
                                                <xsl:value-of select="TRAY_intTrayID"/>
                                            </xsl:attribute>
                                        </input>
                                    </td>
                                    <xsl:choose>
                                        <xsl:when test="position() mod 2 != 0">
                                            <td width="20%" class="uportal-input-text">
                                                <xsl:value-of select="SITE_strSiteName"/>
                                            </td>
                                            <td width="20%" class="uportal-input-text">
                                                <xsl:value-of select="TANK_strTankName"/>
                                            </td>
                                            <td width="20%" class="uportal-input-text">
                                                <xsl:value-of select="BOX_strBoxName"/>
                                            </td>
                                            <td width="20%" class="uportal-input-text">
                                                <xsl:value-of select="TRAY_strTrayName"/>
                                            </td>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <td width="20%" class="uportal-text">
                                                <xsl:value-of select="SITE_strSiteName"/>
                                            </td>
                                            <td width="20%" class="uportal-text">
                                                <xsl:value-of select="TANK_strTankName"/>
                                            </td>
                                            <td width="20%" class="uportal-text">
                                                <xsl:value-of select="BOX_strBoxName"/>
                                            </td>
                                            <td width="20%" class="uportal-text">
                                                <xsl:value-of select="TRAY_strTrayName"/>
                                            </td>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <td width="10%" class="uportal-label"/>
                                </tr>
                            </xsl:for-each>
                            
                            -->
                            
                            
                            
                        </table>
                        <table width="100%">
                            <tr>
                                <td width="90%"/>
                                <td width="10%">
                                    <input type="button" name="add_tray" value="New tray"
                                        class="uportal-button"
                                        onclick="javascript:jumpTo('{$inventoryChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$inventoryChannelTabOrder}&amp;current=add_tray&amp;source=inventory&amp;vial_calc=true')"
                                    />
                                </td>
                            </tr>
                        </table>
                        <table width="100%">
                            <tr>
                                <td>
                                    <hr/>
                                </td>
                            </tr>
                        </table>
                        <!-- hidden fields to send data to server -->
                        <input type="hidden" name="intInternalPatientID"
                            value="{$intInternalPatientID}"/>
                        <input type="hidden" name="BIOSPECIMEN_intBiospecimenID" value="{$intBiospecimenID}"/>
                        <input type="hidden" name="BIOSPECIMEN_strBiospecimenID" value="{$strBiospecimenID}"/>
                        <input type="hidden" name="BIOSPECIMEN_strInitialBiospecSampleType"
                            value="{$strInitialBiospecSampleType}"/>
                        <input type="hidden" name="VIAL_CALCULATION_intVialStored"
                            value="{$VIAL_CALCULATION_intVialStored}"/>
                        <input type="hidden" name="VIAL_CALCULATION_intLocationNeeded"
                            value="{$VIAL_CALCULATION_intLocationNeeded}"/>
                        <input type="hidden" name="VIAL_CALCULATION_intPlasma"
                            value="{$VIAL_CALCULATION_intPlasma}"/>
                        <input type="hidden" name="VIAL_CALCULATION_intGDNA"
                            value="{$VIAL_CALCULATION_intGDNA}"/>
                        <input type="hidden" name="module" value="VIAL_CALCULATION"/>
                        <input type="hidden" name="action" value="allocate_cells"></input>
                        
                        <table width="100%">
                            <tr>
                                <td>
                                   <!-- <input type="submit" name="back" value="{$backBtnLabel}"
                                        class="uportal-button"/> -->
                                    <input type="submit" name="allocate" value="Allocate"
                                        class="uportal-button"/>
                                     
                                </td>
                            </tr>
                        </table>
                        
                    </form>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
