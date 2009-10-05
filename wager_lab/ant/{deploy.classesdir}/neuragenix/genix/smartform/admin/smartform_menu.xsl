<?xml version="1.0" encoding="utf-8"?>

<!-- 
    workspace_menu.xsl, part of the Workspace channel
    Author: hhoang@neuragenix.com
    Date: 01/03/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="/">
    <script language="javascript" >

	function confirmDeleteSmartform(aURL) {
            var confirmAnswer = confirm('Are you sure you want to delete this record?');

            if(confirmAnswer == true){
		window.location=aURL + '&amp;deleteSmartform=true';
            }
	}
        
        function jumpTo(aURL){
            window.location=aURL;
        }
        
        function confirmDeleteDE(aURL) {
            var confirmAnswer = confirm('Are you sure you want to delete this record?');

            if(confirmAnswer == true){
		window.location=aURL + '&amp;deleteDE=true';
            }
	}
        
        function confirmDeleteWhere(aURL) {
            var confirmAnswer = confirm('Are you sure you want to delete this record?');

            if(confirmAnswer == true){
		window.location=aURL + '&amp;target=deleteWhere';
            }
	}  
        
        function confirmDeleteDataElementPool(aURL) {
            var confirmAnswer = confirm('Are you sure you want to delete this record?');

            if(confirmAnswer == true){
		window.location=aURL + '&amp;deleteDataElementPool=true';
            }
	}  
        
        function confirmDeleteOption(aURL) {
            var confirmAnswer = confirm('Are you sure you want to delete this record?');

            if(confirmAnswer == true){
		window.location=aURL + '&amp;deleteOption=true';
            }
	}  
        
        function confirmDeleteOptionPool(aURL) {
            var confirmAnswer = confirm('Are you sure you want to delete this record?');

            if(confirmAnswer == true){
		window.location=aURL + '&amp;deleteOptionPool=true';
            }
	}  
        
	function confirmClear(aURL) {
            var confirmAnswer = confirm('Are you sure you want to clear the fields?');

            if(confirmAnswer == true) {
		window.location=aURL + '&amp;clear=true';
            }
        }
        
        function myFunction(aURL){        
          
          document.frmaddedit_dataelements1.DynamicSmartform.value = true;
          document.frmaddedit_dataelements1.submit();    
        }
        
        function dynamicSmartform(aURL) {            
        
        var strDEId = document.frmaddedit_dataelements.DATAELEMENTS_intDataElementID.value;
        var strDEPoolId = document.frmaddedit_dataelements.DATAELEMENTPOOL_intDataElementPoolID.value;    
        var strName = document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementName.value;
        var strType = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementType.value;
        var addToUrRL = "";  
        
        <!--- To check if it is not blank(When DE from one smartform
        is selected, then DEpool changed to something else, then default sf changed to something, inorder to
        retain runtimeData of the DE selected first, we take it from hidden field since it is blank in this case)-->
        if(strDEId == "")
        {  
            strDEId = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementIDselected.value;
        }
        
         <!-- to check if it is not add new mode -->
        if (document.frmaddedit_dataelements1.strMode.value != 'newDEClicked')
        {
            addToUrRL+= '&amp;DATAELEMENTS_intDataElementID='+strDEId+'&amp;DATAELEMENTS_intDataElementIDselected='+strDEId;
        }
        
        
        if(document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementRow == null)
        {
        }
        else
        {
            var intRow = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementRow.value;
            addToUrRL+= '&amp;DATAELEMENTS_intDataElementRow='+intRow;
        }
        
       
        if(document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementColumn)
        {
            var intCol = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementColumn.value;
            addToUrRL+= '&amp;DATAELEMENTS_intDataElementColumn='+intCol;
        }
        
        
        if(document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementIntMax)
        {
            var intMax = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementIntMax.value;
            addToUrRL+= '&amp;DATAELEMENTS_intDataElementIntMax='+intMax;
        }
        
        
        if(document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementIntMin)
        {
            var intMin = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementIntMin.value;
            addToUrRL+= '&amp;DATAELEMENTS_intDataElementIntMin='+intMin;
        }
        
       
        if(document.frmaddedit_dataelements1.DATAELEMENTS_dtDataElementDateMin_Year)
        {
            var dtMin_Day = document.frmaddedit_dataelements1.DATAELEMENTS_dtDataElementDateMin_Day.value;
            var dtMin_Month = document.frmaddedit_dataelements1.DATAELEMENTS_dtDataElementDateMin_Month.value;
            var dtMin_Year = document.frmaddedit_dataelements1.DATAELEMENTS_dtDataElementDateMin_Year.value;
            var dtMin = dtMin_Day + "/" + dtMin_Month + "/" +dtMin_Year;
            addToUrRL+= '&amp;DATAELEMENTS_dtDataElementDateMin='+dtMin;
        }
        
          
        if(document.frmaddedit_dataelements1.DATAELEMENTS_dtDataElementDateMax_Year)
        {
            var dtMax_Day = document.frmaddedit_dataelements1.DATAELEMENTS_dtDataElementDateMax_Day.value;
            var dtMax_Month = document.frmaddedit_dataelements1.DATAELEMENTS_dtDataElementDateMax_Month.value;
            var dtMax_Year = document.frmaddedit_dataelements1.DATAELEMENTS_dtDataElementDateMax_Year.value;
            var dtMax = dtMax_Day + "/" + dtMax_Month + "/" +dtMax_Year;
            addToUrRL+= '&amp;DATAELEMENTS_dtDataElementDateMax='+dtMax;
        }
        
        
        if(document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementScript)
        {
            var strScript = document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementScript.value;
            addToUrRL+= '&amp;DATAELEMENTS_strDataElementScript='+strScript;
        }
        
        
        if(document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementLookupType)
        {
            var strLookupType = document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementLookupType.value;
            addToUrRL+= '&amp;DATAELEMENTS_strDataElementLookupType='+strLookupType;
        }
        
            
        if(document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementHelp)
        {
            var strHelp = document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementHelp.value;    
            addToUrRL+= '&amp;DATAELEMENTS_strDataElementHelp='+strHelp;
        }
        
        
       
        if(document.frmaddedit_dataelements1.rdDATAELEMENTS_dtDataElementDefaultDate)
        {
           var ffRadioVal = document.frmaddedit_dataelements1.rdDATAELEMENTS_dtDataElementDefaultDate[0].value;
            
                addToUrRL+= '&amp;DATAELEMENTS_strDataElementDefaultTodaysDate=TODAYSDATE';            
            
            
        }
        
       
        
        if(document.frmaddedit_dataelements1.DATAELEMENTS_dtDataElementDefaultDate_Year)
        { 
            var DefaultDtDay = document.frmaddedit_dataelements1.DATAELEMENTS_dtDataElementDefaultDate_Day.value;
            var DefaultDtMonth = document.frmaddedit_dataelements1.DATAELEMENTS_dtDataElementDefaultDate_Month.value;
            var DefaultDtYear = document.frmaddedit_dataelements1.DATAELEMENTS_dtDataElementDefaultDate_Year.value;
            var DefaultDate = DefaultDtDay +"/"+ DefaultDtMonth +"/"+ DefaultDtYear;
            
                                        
               addToUrRL+= '&amp;DATAELEMENTS_dtDataElementDefaultDate='+DefaultDate;
            
        }
        
        
        if(document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementDefaultWorkingDaysOperator)
        {
            var strOper = document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementDefaultWorkingDaysOperator.value;
                      
               addToUrRL+= '&amp;DATAELEMENTS_strDataElementDefaultWorkingDaysOperator='+strOper;
             
        }
        
        
        if(document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementDefaultWorkingDaysNumber)
        {
            var intWDNo = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementDefaultWorkingDaysNumber.value;
              
                addToUrRL+= '&amp;DATAELEMENTS_intDataElementDefaultWorkingDaysNumber='+intWDNo;
             
        }
        
        
        if(document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementDefaultWdOrDaysOption)
        {
            var strWDOption = document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementDefaultWdOrDaysOption.value;
           
                addToUrRL+= '&amp;DATAELEMENTS_strDataElementDefaultWdOrDaysOption='+strWDOption;
            
        }
        
        
        if(document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementDefault)
        {
            var strDefaultVal = document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementDefault.value;
            addToUrRL+= '&amp;DATAELEMENTS_strDataElementDefault='+strDefaultVal;
        }
        
        
        if(document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementDefaultSmartformID)
        {
            var intDefaultSFId = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementDefaultSmartformID.value;
            addToUrRL+= '&amp;DATAELEMENTS_intDataElementDefaultSmartformID='+intDefaultSFId+'&amp;DE_DefaultSmartformIDSelected='+intDefaultSFId;
        }
        
        
        if(document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementDefaultDataElementID)
        {
            var intDefaultDEId = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementDefaultDataElementID.value;
            addToUrRL+= '&amp;DATAELEMENTS_intDataElementDefaultDataElementID='+intDefaultDEId+'&amp;DE_DefaultDataElementIDSelected='+intDefaultDEId;
        }       
        
        if(document.frmaddedit_dataelements1.SYSTEMLOOKUPWHERE_strConnector)
        {
            var strConn = document.frmaddedit_dataelements1.SYSTEMLOOKUPWHERE_strConnector.value;
            addToUrRL+= '&amp;SYSTEMLOOKUPWHERE_strConnector='+strConn;
        } 
        
        if(document.frmaddedit_dataelements1.SYSTEMLOOKUPWHERE_strField)
        {
            var strWhereField = document.frmaddedit_dataelements1.SYSTEMLOOKUPWHERE_strField.value;
            addToUrRL+= '&amp;SYSTEMLOOKUPWHERE_strField='+strWhereField+'&amp;DELookupFieldSelected='+strWhereField;
        }   
        
        if(document.frmaddedit_dataelements1.SYSTEMLOOKUPWHERE_strOperator)
        {
            var strWhereOp = document.frmaddedit_dataelements1.SYSTEMLOOKUPWHERE_strOperator.value;
            addToUrRL+= '&amp;SYSTEMLOOKUPWHERE_strOperator='+strWhereOp;
        } 
        
        if(document.frmaddedit_dataelements1.SYSTEMLOOKUPWHERE_strValue)
        {
            var strWhereVal = document.frmaddedit_dataelements1.SYSTEMLOOKUPWHERE_strValue.value;
            addToUrRL+= '&amp;SYSTEMLOOKUPWHERE_strValue='+strWhereVal;
        } 
        
        
        
        var len = document.frmaddedit_dataelements1.elements.length;
        //alert("len :"+len);
        var j=0;
        //document.write( '&lt;form name="anita" method="POST"&gt;' );
        
        for(var i=0;i &lt; len;i++)
        {
            if(document.frmaddedit_dataelements1.elements[i].type=="checkbox")
            {
                if(document.frmaddedit_dataelements1.elements[i].checked)
                {
                    //alert("element name: "+document.frmaddedit_dataelements1.elements[i].name);                    
                    var name = document.frmaddedit_dataelements1.elements[i].name;
                    alert("name:"+name);                                        
                    
                    var the_char=name.charAt(21);
                    //alert('The 21st I character is '+the_char+'.');
                    
                    
                    j=j+1;
                    //alert("j:"+j);
                    //alert("i:"+i);
                    var intlName = 'SYSTEMLOOKUPFIELD_strInternalName' + j;
                    var fieldOrder = 'SYSTEMLOOKUPFIELD_strFieldOrder' + j;
                    var addnlInfoName = 'SYSTEMLOOKUPFIELD_strInDisplayAddnl' + j;
                    //alert("intlName:"+intlName);
                    //alert("fieldOrder :"+fieldOrder);
                    //alert("addnlInfoName :"+addnlInfoName);
                    var strDisplayName = document.frmaddedit_dataelements1.elements[i].name;

                    addToUrRL+= '&amp;'+intlName+'='+strDisplayName;
                    //document.write( '&lt;input type="hidden" name="' + intlName + '" value="' + strDisplayName + '"/&gt;' );
                    
                    //alert("strDisplayName :"+strDisplayName);

                    //alert("before check");                   

                    var ordName = 'SYSTEMLOOKUPFIELD_strFieldOrder' + strDisplayName;      
                    var ordFieldName ='document.frmaddedit_dataelements1.' + ordName;         
                    //alert("ordFieldName :"+ordFieldName);    
                    //alert("eval :"+eval(ordFieldName));

                   if (the_char != 'I'){      
                        //alert("First column checkbox clicked");
                        //alert("order exist");              
                        var order = eval('document.frmaddedit_dataelements1.' + ordName + '.value');
                        addToUrRL+= '&amp;'+fieldOrder+'='+order;
                        //alert(order);
                        //alert(ordName);  
                        
                        //document.write( '&lt;input type="hidden" name="' + fieldOrder + '" value="' + order + '"/&gt;' );
                    
                        
                    }else{                
                        
                            //alert("Third column checkbox clicked");
                            //alert("no order exist");                        
                            //alert("starts with displayinaddnl"+strDisplayName);
                            addToUrRL+= '&amp;'+addnlInfoName+'='+strDisplayName;         
                            
                            //document.write( '&lt;input type="hidden" name="' + addnlInfoName + '" value="' + strDisplayName + '"/&gt;' );
                    
                    
                    }
                                        
                    
                }
            }
            
           
        }
        //document.write( "&lt;/form&gt;");
        
        if (document.frmaddedit_dataelements1.rdDefaultorExisting)
        {
            if (document.frmaddedit_dataelements1.rdDefaultorExisting[0].value) // if radio btn exist
            { 
              for (var i=0; i &lt; document.frmaddedit_dataelements1.rdDefaultorExisting.length; i++) 
              {
                 if (document.frmaddedit_dataelements1.rdDefaultorExisting[i].checked)
                  {
                    addToUrRL+= '&amp;strRadioVal='+document.frmaddedit_dataelements1.rdDefaultorExisting[i].value;
                  }
              }
            }
        }
           
       
        alert( aURL + '&amp;DATAELEMENTS_intDataElementPoolID='+strDEPoolId+'&amp;DATAELEMENTS_strDataElementName='+strName+'&amp;DATAELEMENTS_intDataElementType='+strType+'&amp;DATAELEMENTS_intDataElementTypeSelected='+strType+'&amp;DynamicSmartform=true'+ addToUrRL );
        //window.location=aURL + '&amp;DATAELEMENTS_intDataElementPoolID='+strDEPoolId+'&amp;DATAELEMENTS_strDataElementName='+strName+'&amp;DATAELEMENTS_intDataElementType='+strType+'&amp;DATAELEMENTS_intDataElementTypeSelected='+strType+'&amp;DynamicSmartform=true'+ addToUrRL;
        //document.anita.url.value = aURL + '&amp;DATAELEMENTS_intDataElementPoolID='+strDEPoolId+'&amp;DATAELEMENTS_strDataElementName='+strName+'&amp;DATAELEMENTS_intDataElementType='+strType+'&amp;DATAELEMENTS_intDataElementTypeSelected='+strType+'&amp;DynamicSmartform=true'+ addToUrRL;
        document.frmaddedit_dataelements1.submit();
        
        }
        
        
        
        
        function buildSmartform(aURL) {  
         var DEId = document.frmbuild_smartform.SMARTFORMTODATAELEMENTS_intDataElementID.value;
         
        window.location=aURL + '&amp;SMARTFORMTODATAELEMENTS_intDataElementID='+ DEId +'&amp;DEClicked=true';
        
        }
        
        function dynamicSmartform1(aURL) {            
        
        var strDEId = document.frmaddedit_dataelements.DATAELEMENTS_intDataElementID.value;
        var strDEPoolId = document.frmaddedit_dataelements.DATAELEMENTPOOL_intDataElementPoolID.value;    
        var strName = document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementName.value;
        var strType = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementType.value;
        
        var intMax = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementIntMax.value;
        var intMin = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementIntMin.value;
        
        
        var strHelp = document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementHelp.value;        
        
        
        var strDefaultVal = document.frmaddedit_dataelements1.DATAELEMENTS_strDataElementDefault.value;
        var intDefaultSFId = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementDefaultSmartformID.value;
        var intDefaultDEId = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementDefaultDataElementID.value;
        
        
        
        window.location=aURL + '&amp;DATAELEMENTS_intDataElementID='+strDEId+'&amp;DATAELEMENTS_intDataElementPoolID='+strDEPoolId+'&amp;DATAELEMENTS_strDataElementName='+strName+'&amp;DATAELEMENTS_intDataElementType='+strType+'&amp;DATAELEMENTS_intDataElementIntMax='+intMax+'&amp;DATAELEMENTS_intDataElementIntMin='+intMin+'&amp;DATAELEMENTS_strDataElementHelp='+strHelp+'&amp;DATAELEMENTS_strDataElementDefault='+strDefaultVal+'&amp;DATAELEMENTS_intDataElementDefaultSmartformID='+intDefaultSFId+'&amp;DATAELEMENTS_intDataElementDefaultDataElementID='+intDefaultDEId+'&amp;DynamicSmartform=true';
        
        }
        
        
        function checkAll(field)
        {
        for (i = 0; i &lt; field.length; i++)
                field[i].checked = true ;
        }

        function uncheckAll(field)
        {
        for (i = 0; i &lt; field.length; i++)
                field[i].checked = false ;
        }
        
        function change(form,bool) {
                      for (i = 0; i &lt; form.length; i++) {
                        if (form[i].type == "checkbox") {
                          form[i].checked = bool;
                        }
                      }
                  } 
       
                  
    	
    </script>
    <xsl:apply-templates />
    </xsl:template>

</xsl:stylesheet>