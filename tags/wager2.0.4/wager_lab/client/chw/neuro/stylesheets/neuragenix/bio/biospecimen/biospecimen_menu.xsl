<?xml version="1.0" encoding="utf-8"?>

<!-- patient_menu.xsl. Menu used for all patients.-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!-- xsl:output method="html" indent="no"/ -->

  <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
  <xsl:param name="downloadURL">downloadURL</xsl:param>
   
  <xsl:variable name="downloadNodeId">nodeId</xsl:variable>

  <xsl:template match="/">
  
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

                function move(fbox,tbox) {
                var i;
                for(i=0; i &lt; fbox.options.length; i++) {
                    if(fbox.options[i].selected ){
                        if(fbox.options[i].value != "") {
                            var no = new Option();
                            no.value = fbox.options[i].value;
                            no.text = fbox.options[i].text;
                            tbox.options[tbox.options.length] = no;
                            fbox.options[i].value = "";
                            fbox.options[i].text = "";
                            
                        }
                        fbox.options[i].selected=false;
                    }
                }
                
                BumpUp(fbox);
                
                var temp_opts = new Array();
                var temp = new Object();
                for(var i=0; i &lt; tbox.options.length; i++)  {
                    temp_opts[i] = tbox.options[i];
                }
                for(var x=0; x &lt; temp_opts.length-1; x++)  {
                    for(var y=(x+1); y &lt; temp_opts.length; y++)  {
                        if(temp_opts[x].text.toLowerCase() > temp_opts[y].text.toLowerCase())  {
                            temp = temp_opts[x].text;
                            temp_opts[x].text = temp_opts[y].text;
                            temp_opts[y].text = temp;
                            temp = temp_opts[x].value;
                            temp_opts[x].value = temp_opts[y].value;
                            temp_opts[y].value = temp;
                        }
                    }
                }
                for(var i=0; i &lt; tbox.options.length; i++)  {
                    tbox.options[i].value = temp_opts[i].value;
                    tbox.options[i].text = temp_opts[i].text;
                }
            
        }
        
        function BumpUp(box)  {
            for(var i=0; i &lt; box.options.length; i++) {
                if(box.options[i].value == "")  {
                    for(var j=i; j &lt; box.options.length-1; j++)  {
                        box.options[j].value = box.options[j+1].value;
                        box.options[j].text = box.options[j+1].text;
                    }
                    var ln = i;
                    break;
                }
            }
            if(ln &lt; box.options.length)  {
                box.options.length -= 1;
                BumpUp(box);
            }
        }
        function moveAll(fbox,tbox) {
                var i;
               
                for( i=0; i &lt; fbox.options.length; i++) {
                    
                    var no = new Option();
                    no.value = fbox.options[i].value;
                    no.text = fbox.options[i].text;
                    tbox.options[tbox.options.length] = no;
                    fbox.options[i].value = "";
                    fbox.options[i].text = "";
                }
                
                fbox.options.length =0;
                
                var temp_opts = new Array();
                var temp = new Object();
                for(var i=0; i &lt; tbox.options.length; i++)  {
                    temp_opts[i] = tbox.options[i];
                }
                for(var x=0; x &lt; temp_opts.length-1; x++)  {
                    for(var y=(x+1); y &lt; temp_opts.length; y++)  {
                        if(temp_opts[x].text > temp_opts[y].text)  {
                            temp = temp_opts[x].text;
                            temp_opts[x].text = temp_opts[y].text;
                            temp_opts[y].text = temp;
                            temp = temp_opts[x].value;
                            temp_opts[x].value = temp_opts[y].value;
                            temp_opts[y].value = temp;
                        }
                    }
                }
                for(var i=0; i &lt; box.options.length; i++)  {
                    tbox.options[i].value = temp_opts[i].value;
                    tbox.options[i].text = temp_opts[i].text;
                }
            
        }
        function doSubmit(fBox){
            var i;
            
            for(i = 0; i &lt; fBox.options.length; i++){
                fBox.options[i].selected = true;
            }
      
            //mForm.submit();
        }
	</script>
    <link rel="stylesheet" type="text/css" href="stylesheets/neuragenix/bio/xmlTree.css"/>
    <table width="100%">
	<tr valign="top">
		<td id="neuragenix-border-right" width="15%" class="uportal-channel-subtitle">
			Menu<hr></hr>
			<br></br>
                        
                        
			<a href="{$baseActionURL}?uP_root=root&amp;module=NEW_SEARCH">Search biospecimens</a>
			<br></br>
                        
                        <a href="{$baseActionURL}?uP_root=root&amp;current=bp_allocate&amp;stage=BEGIN&amp;module=BATCH_ALLOCATE">Quantity allocation</a>
			<br></br>
                        <a href="{$baseActionURL}?module=BATCH_SFRESULTS_GENERATION&amp;action=creation_type">Batch Analysis Results</a>
			<br></br>                        
                        <br />
			<!-- <a href="{$baseActionURL}?current=biospecimen_add&amp;intInternalPatientID=101">Add biospecimen</a>  -->
                        <xsl:if test='count(biospecimen/flagged) &gt; 0'>
                        Flagged Records:
                        <table border='0' width='100%'>
                            <xsl:for-each select="/biospecimen/flagged">
                                <tr>
                                    <td class='uportal-input-text'>
                                        <xsl:variable name="strBiospecimenID"><xsl:value-of select="BIOSPECIMEN_strBiospecimenID"/></xsl:variable>                
                                        <xsl:variable name="intFlagID"><xsl:value-of select="FLAG_intID" /></xsl:variable>
                                        <a href="{$baseActionURL}?uP_root=root&amp;module=core&amp;action=view_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={$intFlagID}"><xsl:value-of select="$strBiospecimenID" /></a>
                                    </td>
                                  </tr>
                            </xsl:for-each>
                        </table>
                        </xsl:if>
                        
                        <xsl:if test="string-length( /body/biospecimen/patient_details/PATIENT_strPatientIDDisplay ) &gt; 0">
                            <hr/>
                            <table width="100%" border="0" cellspacing="0" cellpadding="2">
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_strPatientIDDisplay" />:</b> 
                                    </td>
                                    <td width="70%"  align="right">
                                            <xsl:value-of select="/body/biospecimen/patient_details/PATIENT_strPatientID" />
                                    </td>
                                </tr>
<!--Seena                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_strHospitalURDisplay" />: </b>
                                    </td>
                                    <td width="70%" align="right">
                                            <xsl:value-of select="/body/biospecimen/patient_details/PATIENT_strHospitalUR" />
                                    </td>
                                </tr>
-->                                
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_strSurnameDisplay" />: </b>
                                    </td>
                                    <td width="70%"  align="right"> 
                                            <xsl:value-of select="/body/biospecimen/patient_details/PATIENT_strSurname" />
                                    </td>
                                </tr>
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_strFirstNameDisplay" />: </b>
                                    </td>
                                    <td width="70%" align="right">
                                            <xsl:value-of select="/body/biospecimen/patient_details/PATIENT_strFirstName" />
                                    </td>
                                </tr>
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_dtDobDisplay" />: </b>
                                    </td>
                                    <td width="70%" align="right">
                                            <xsl:value-of select="/body/biospecimen/patient_details/PATIENT_dtDob" />
                                    </td>
                                </tr>
                            </table>
                         </xsl:if>
                         
                         <xsl:if test="string-length( /body/biospecimen/biospecimen_details/BIOSPECIMEN_strBiospecimenIDDisplay ) &gt; 0">
                            <hr/>
                            <table width="100%" border="0" cellspacing="0" cellpadding="2">
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/body/biospecimen/biospecimen_details/BIOSPECIMEN_strBiospecimenIDDisplay" />:</b> 
                                    </td>
                                    <td width="70%"  align="right">
                                            <xsl:value-of select="/body/biospecimen/biospecimen_details/BIOSPECIMEN_strBiospecimenID" />
                                    </td>
                                </tr>
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/body/biospecimen/biospecimen_details/BIOSPECIMEN_strSampleTypeDisplay" />: </b>
                                    </td>
                                    <td width="70%" align="right">
                                            <xsl:value-of select="/body/biospecimen/biospecimen_details/BIOSPECIMEN_strSampleType" />
                                    </td>
                                </tr>
                                <xsl:if test="string-length(/body/biospecimen/biospecimen_details/BIOSPECIMEN_strGradeDisplay) > 0">
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/body/biospecimen/biospecimen_details/BIOSPECIMEN_strGradeDisplay" />: </b>
                                    </td>
                                    <td width="70%"  align="right"> 
                                            <xsl:value-of select="/body/biospecimen/biospecimen_details/BIOSPECIMEN_strGrade" />
                                    </td>
                                </tr>
                                </xsl:if>
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/body/biospecimen/biospecimen_details/BIOSPECIMEN_strSpeciesDisplay" />: </b>
                                    </td>
                                    <td width="70%" align="right">
                                            <xsl:value-of select="/body/biospecimen/biospecimen_details/BIOSPECIMEN_strSpecies" />
                                    </td>
                                </tr>
                                
                            </table>
                         </xsl:if>
		</td>
		<td width="5%"></td>
		<td width="80%">
				<xsl:apply-templates/>
		</td>
	</tr>
    </table> 
	
    

  </xsl:template>
  <xsl:template match="/body/biospecimen/biospecimen_details"/>
  <xsl:template match="/body/biospecimen/patient_details"/>
</xsl:stylesheet>

