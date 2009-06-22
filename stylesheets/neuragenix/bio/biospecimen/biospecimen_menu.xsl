<?xml version="1.0" encoding="utf-8"?>

<!-- patient_menu.xsl. Menu used for all patients.-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!-- xsl:output method="html" indent="no"/ -->

  <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
  <xsl:param name="downloadURL">downloadURL</xsl:param>
     
  <xsl:variable name="downloadNodeId">nodeId</xsl:variable>
        <xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>
        <xsl:variable name="funcpanelImagePath">media/neuragenix/funcpanel</xsl:variable>
        <xsl:variable name="spacerImagePath">media/neuragenix/infopanel/spacer.gif</xsl:variable>
  <xsl:template match="/">
          <xsl:param name="formParams">current=biospecimen_search</xsl:param>
          <xsl:param name="callingDomain"><xsl:value-of select="/biospecimen/callingDomain" /> </xsl:param>
          <xsl:param name="hideSearchBox"><xsl:value-of select="/biospecimen/hideSearchBox" /> </xsl:param>
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


	</script>
    <link rel="stylesheet" type="text/css" href="stylesheets/neuragenix/bio/xmlTree.css"/>
        
                  
                  
                  
        
    <table width="100%">
	<tr valign="top">
		<td id="neuragenix-border-right" width="15%" class="uportal-channel-subtitle">
			<!--Menu<hr></hr>
			<br></br>
                        
                        
			<a href="{$baseActionURL}?uP_root=root&amp;current=biospecimen_search">Search biospecimens</a>
			<br></br>
                        
                        <a href="{$baseActionURL}?uP_root=root&amp;current=bp_allocate&amp;stage=BEGIN&amp;module=BATCH_ALLOCATE">Quantity allocation</a>
			<br></br>-->
                        <br />
			<!-- <a href="{$baseActionURL}?current=biospecimen_add&amp;intInternalPatientID=101">Add biospecimen</a>  -->
                       <!-- <xsl:if test='count(biospecimen/flagged) &gt; 0'>
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
                        </xsl:if>-->
		        <xsl:if test="not($hideSearchBox = 'true')">
		        <form name="biospecimen_search" action="{$baseActionURL}?{$formParams}" method="post">
		        <table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="300">
		                <tr valign="bottom">
		                        <td><img src="{$funcpanelImagePath}/funcpanel_header_active_left.gif"/></td>
		                        <td class="funcpanel_header_active" align="left" colspan="3" width="100%">SEARCH BIOSPECIMEN</td>
		                        <td><img src="{$funcpanelImagePath}/funcpanel_header_active_right.gif"/></td>
		                </tr>
		                <tr class="funcpanel_content">
		                        <td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
		                </tr>
		                <tr class="funcpanel_content">
		                        <td class="funcpanel_left_border">&#160;</td>
		                        <td colspan="3">
		                                <table width="100%">
		                                        <tr>
		                                                <td id="neuragenix-form-row-label" class="uportal-label">
		                                                        <xsl:value-of select="/biospecimen/BIOSPECIMEN_strBiospecimenIDDisplay" />: 
		                                                </td>
		                                                <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                <td class="uportal-label" id="neuragenix-form-row-input">
		                                                        <input type="text" name="BIOSPECIMEN_strBiospecimenID" tabindex="20" size="20" class="uportal-input-text" />
		                                                </td>
		                                                <td width="5%"></td>
		                                                <td id="neuragenix-form-row-label" class="uportal-label">
		                                                </td>
		                                                <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                <td class="uportal-label" id="neuragenix-form-row-input">
		                                                </td>
		                                        </tr>
		                                      
		                                                <tr>
		                                                        <td id="neuragenix-form-row-label" class="uportal-label">
		                                                                <xsl:value-of select="/biospecimen/PATIENT_strPatientIDDisplay" />: 
		                                                        </td>
		                                                        <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                        <td id="neuragenix-form-row" class="uportal-label">
		                                                                <input type="text" name="PATIENT_strPatientID" tabindex="21" size="20" class="uportal-input-text" />
		                                                        </td>
		                                                        <td width="5%"></td>
		                                                        <td id="neuragenix-form-row-label" class="uportal-label">
		                                                        </td>
		                                                        <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                        <td id="neuragenix-form-row-input" class="uportal-label">
		                                                        </td>
		                                                        
		                                                </tr>
		                                      
		                                                <tr>
		                                                        <td id="neuragenix-form-row-label" class="uportal-label">
		                                                                <xsl:value-of select="/biospecimen/BIOSPECIMEN_strOtherIDDisplay" />: 
		                                                        </td>
		                                                        <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                        <td id="neuragenix-form-row" class="uportal-label">
		                                                                <input type="text" name="BIOSPECIMEN_strOtherID" size="20" class="uportal-input-text" />
		                                                        </td>
		                                                        <td width="5%"></td>
		                                                        <td id="neuragenix-form-row-label" class="uportal-label">
		                                                        </td>
		                                                        <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                        <td id="neuragenix-form-row-input" class="uportal-label">
		                                                        </td>
		                                                        
		                                                </tr>
		                                       
		                                                <tr>
		                                                        <td id="neuragenix-form-row-label" class="uportal-label">
		                                                                <xsl:value-of select="/biospecimen/BIOSPECIMEN_dtSampleDateDisplay" />: 
		                                                        </td>
		                                                        <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                        <td id="neuragenix-form-row" class="uportal-label">
		                                                                
		                                                                <xsl:choose>
		                                                                        <xsl:when test="BIOSPECIMEN_dtSampleDate/@display_type='text'">
		                                                                                <input type="text" name="BIOSPECIMEN_dtSampleDate_Day" size="2" tabindex="24" class="uportal-input-text">
		                                                                                        <xsl:attribute name="value"><xsl:value-of select="/biospecimen/BIOSPECIMEN_dtSampleDate_Day"/></xsl:attribute>
		                                                                                </input>
		                                                                                &#160;
		                                                                                <input type="text" name="BIOSPECIMEN_dtSampleDate_Month" size="2" tabindex="25" class="uportal-input-text">
		                                                                                        <xsl:attribute name="value"><xsl:value-of select="/biospecimen/BIOSPECIMEN_dtSampleDate_Month"/></xsl:attribute>
		                                                                                </input>
		                                                                        </xsl:when>
		                                                                        <!-- when it is drop down -->
		                                                                        <xsl:otherwise>
		                                                                                <select name="BIOSPECIMEN_dtSampleDate_Day" tabindex="25" class="uportal-input-text">
		                                                                                        <option value="" selected="true" />
		                                                                                        <xsl:for-each select="/biospecimen/BIOSPECIMEN_dtSampleDate_Day">
		                                                                                                <option>
		                                                                                                        <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
		                                                                                                        
		                                                                                                        <!-- uncheck below for default date of today -->
		                                                                                                        <!-- 
		                                                                                                                <xsl:if test="@selected = '1'">
		                                                                                                                <xsl:attribute name="selected">true</xsl:attribute>
		                                                                                                                </xsl:if>
		                                                                                                        -->
		                                                                                                        <xsl:value-of select="."/>
		                                                                                                </option>
		                                                                                        </xsl:for-each>
		                                                                                </select>
		                                                                                &#160;
		                                                                                <select name="BIOSPECIMEN_dtSampleDate_Month" tabindex="26" class="uportal-input-text">
		                                                                                        <option value="" selected="true" />
		                                                                                        <xsl:for-each select="/biospecimen/BIOSPECIMEN_dtSampleDate_Month">
		                                                                                                <option>
		                                                                                                        <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
		                                                                                                        
		                                                                                                        <!--  uncheck below for default date of today -->
		                                                                                                        <!--
		                                                                                                                <xsl:if test="@selected = '1'">
		                                                                                                                <xsl:attribute name="selected">true</xsl:attribute>
		                                                                                                                </xsl:if>
		                                                                                                        -->
		                                                                                                        <xsl:value-of select="."/>
		                                                                                                </option>
		                                                                                        </xsl:for-each>
		                                                                                </select>
		                                                                        </xsl:otherwise>
		                                                                </xsl:choose>
		                                                                &#160;
		                                                                <input type="text" name="BIOSPECIMEN_dtSampleDate_Year" size="4" tabindex="27" class="uportal-input-text">
		                                                                        <!-- <xsl:attribute name="value"><xsl:value-of select="/biospecimen/BIOSPECIMEN_dtSampleDate_Year"/></xsl:attribute> -->
		                                                                </input>    
		                                                                
		                                                                
		                                                                
		                                                                
		                                                        </td>
		                                                        <td width="5%"></td>
		                                                        <td id="neuragenix-form-row-label" class="uportal-label">
		                                                        </td>
		                                                        <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                        <td id="neuragenix-form-row-input" class="uportal-label">
		                                                        </td>
		                                                </tr>
		                                       
		                                        
		                                        <!-- Extracted Date -->
		                                        
		                                       
		                                                <tr>
		                                                        <td id="neuragenix-form-row-label" class="uportal-label">
		                                                                <xsl:value-of select="/biospecimen/BIOSPECIMEN_dtExtractedDateDisplay" />: 
		                                                        </td>
		                                                        <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                        <td id="neuragenix-form-row" class="uportal-label">
		                                                                <xsl:choose>
		                                                                        <xsl:when test="BIOSPECIMEN_dtExtractedDate/@display_type='text'">
		                                                                                <input type="text" name="BIOSPECIMEN_dtExtractedDate_Day" size="2" tabindex="27" class="uportal-input-text">
		                                                                                        <xsl:attribute name="value"><xsl:value-of select="/biospecimen/BIOSPECIMEN_dtExtractedDate_Day"/></xsl:attribute>
		                                                                                </input>
		                                                                                &#160;
		                                                                                <input type="text" name="BIOSPECIMEN_dtExtractedDate_Month" size="2" tabindex="25" class="uportal-input-text">
		                                                                                        <xsl:attribute name="value"><xsl:value-of select="/biospecimen/BIOSPECIMEN_dtExtractedDate_Month"/></xsl:attribute>
		                                                                                </input>
		                                                                        </xsl:when>
		                                                                        <!-- when it is drop down -->
		                                                                        <xsl:otherwise>
		                                                                                <select name="BIOSPECIMEN_dtExtractedDate_Day" tabindex="27" class="uportal-input-text">
		                                                                                        <option value="" selected="true"/>
		                                                                                        <xsl:for-each select="/biospecimen/BIOSPECIMEN_dtExtractedDate_Day">
		                                                                                                <option>
		                                                                                                        <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
		                                                                                                        <!-- Uncheck this if you want to use todays date as default -->
		                                                                                                        <!-- 
		                                                                                                                <xsl:if test="@selected = '1'">
		                                                                                                                <xsl:attribute name="selected">true</xsl:attribute>
		                                                                                                                </xsl:if>
		                                                                                                        -->
		                                                                                                        <xsl:value-of select="."/>
		                                                                                                </option>
		                                                                                        </xsl:for-each>
		                                                                                </select>
		                                                                                &#160;
		                                                                                <select name="BIOSPECIMEN_dtExtractedDate_Month" tabindex="27" class="uportal-input-text">
		                                                                                        <option value="" selected="true"/>
		                                                                                        <xsl:for-each select="/biospecimen/BIOSPECIMEN_dtExtractedDate_Month">
		                                                                                                <option>
		                                                                                                        <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
		                                                                                                        <!-- uncheck below to use todays date as default -->
		                                                                                                        <!-- 
		                                                                                                                <xsl:if test="@selected = '1'">
		                                                                                                                <xsl:attribute name="selected">true</xsl:attribute>
		                                                                                                                </xsl:if>
		                                                                                                        -->
		                                                                                                        <xsl:value-of select="."/>
		                                                                                                </option>
		                                                                                        </xsl:for-each>
		                                                                                </select>
		                                                                        </xsl:otherwise>
		                                                                </xsl:choose>
		                                                                &#160;
		                                                                <input type="text" name="BIOSPECIMEN_dtExtractedDate_Year" size="4" tabindex="28" class="uportal-input-text">
		                                                                        <!-- <xsl:attribute name="value"><xsl:value-of select="/biospecimen/BIOSPECIMEN_dtExtractedDate_Year"/></xsl:attribute> -->
		                                                                </input>
		                                                                
		                                                        </td>
		                                                        <td width="5%"></td>
		                                                        <td id="neuragenix-form-row-label" class="uportal-label">
		                                                        </td>
		                                                        <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                        <td id="neuragenix-form-row-input" class="uportal-label">
		                                                        </td>
		                                                        
		                                                </tr>
		                                                
		                                        
		                                                <tr>
		                                                        <td id="neuragenix-form-row-label" class="uportal-label">
		                                                                <xsl:value-of select="/biospecimen/BIOSPECIMEN_intStudyKeyDisplay" />
		                                                        </td>
		                                                        <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                        <td id="neuragenix-form-row" class="uportal-label">
		                                                                <select name="BIOSPECIMEN_intStudyKey" tabindex="30" class="uportal-input-text">
		                                                                        <xsl:for-each select="/biospecimen/study_list">
		                                                                                <option>
		                                                                                        <xsl:attribute name="value"><xsl:value-of select="STUDY_intStudyID" /></xsl:attribute>
		                                                                                        <xsl:value-of select="STUDY_strStudyName" />
		                                                                                </option>
		                                                                        </xsl:for-each>
		                                                                        <option value="" selected="true">
		                                                                        </option>
		                                                                        
		                                                                </select>
		                                                        </td>
		                                                        <td width="5%"></td>
		                                                        <td id="neuragenix-form-row-label" class="uportal-label">
		                                                        </td>
		                                                        <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                        <td id="neuragenix-form-row-input" class="uportal-label">
		                                                        </td>
		                                                        
		                                                </tr>
		                                       
		                                        <tr>
		                                                <td id="neuragenix-form-row-label" class="uportal-label">
		                                                        <xsl:value-of select="/biospecimen/BIOSPECIMEN_strSampleTypeDisplay" />
		                                                </td>
		                                                <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                <td id="neuragenix-form-row" class="uportal-label">
		                                                        <select name="BIOSPECIMEN_strSampleType" tabindex="30" class="uportal-input-text">
		                                                                <xsl:for-each select="/biospecimen/BIOSPECIMEN_strSampleType">
		                                                                        <option>
		                                                                                <xsl:attribute name="value"><xsl:value-of select="." /></xsl:attribute>
		                                                                                <xsl:value-of select="." />
		                                                                        </option>
		                                                                </xsl:for-each>
		                                                                <option value="" selected="true">
		                                                                </option>
		                                                                
		                                                        </select>
		                                                </td>
		                                                <td width="5%"></td>
		                                                <td id="neuragenix-form-row-label" class="uportal-label">
		                                                </td>
		                                                <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
		                                                <td id="neuragenix-form-row-input" class="uportal-label">
		                                                </td>
		                                                
		                                        </tr>
		                                </table>
		                                <table width="100%">
		                                        <tr>
		                                        	<td align="right">
		                                                        <input type="hidden" name="module" value="BIOSPECIMEN_SEARCH" />
		                                                	<a class="button" href="#" onclick="this.blur(); document.forms.biospecimen_search.submit();"><span>Submit</span></a>
		                                                        <!--<input type="submit" name="submit" value="Search" class="uportal-button"/>-->
		                                                </td>
		                                                <td align="right">
		                                                	<a class="button" href="#" onclick="this.blur();javascript:confirmClear('{$baseActionURL}?{$formParams}')"><span>Clear</span></a>
		                                                       <!-- <input type="button" name="clear" value="Clear" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?{$formParams}')" />-->
		                                                </td>
		                                                
		                                        </tr>
		                                        
		                                </table>
		                        </td>
		                        <td class="funcpanel_right_border">&#160;</td>
		                </tr>
		                <tr class="funcpanel_content">
		                        <td class="funcpanel_bottom_border" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
		                </tr>
		        </table>
		        </form>
		        </xsl:if>
		        <xsl:if test="$callingDomain='patient'">	
		                <table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="300">
		                        <tr valign="bottom">
		                                <td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_left.gif"/></td>
		                                <td class="funcpanel_header_inactive" align="left" colspan="3" width="100%">PATIENT DETAILS</td>
		                                <td><img src="{$funcpanelImagePath}/funcpanel_header_inactive_right.gif"/></td>
		                        </tr>
		                        <tr class="funcpanel_content">
		                                <td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
		                        </tr>
		                        <tr class="funcpanel_content">
		                                <td class="funcpanel_left_border">&#160;</td>
		                                <td colspan="3">
		                                        <table width="100%">
		                                                <tr>
		                                                       	
		                                                        <td  class="uportal-label"><xsl:value-of select="/biospecimen/PATIENT_strPatientIDDisplay" />: </td>
		                                                        <td>&#160;</td>
		                                                        <td  class="uportal-text"><xsl:value-of select="/biospecimen/PATIENT_strPatientID" /></td>
		                                                       
		                                                </tr>
		                                                <tr>
		                                                        		
		                                                        <td  class="uportal-label"><xsl:value-of select="/biospecimen/PATIENT_dtDobDisplay" />: </td>
		                                                        <td>&#160;</td>
		                                                        <td class="uportal-text" ><xsl:value-of select="/biospecimen/PATIENT_dtDob" /></td>
		                                                       		
		                                                </tr>
		                                                
		                                                <tr> 
		                                                        
		                                                        <td  class="uportal-label"><xsl:value-of select="/biospecimen/PATIENT_strFirstNameDisplay" />: </td>
		                                                        <td>&#160;</td>
		                                                        <td  class="uportal-text"><xsl:value-of select="/biospecimen/PATIENT_strFirstName" /></td>
		                                                        
		                                                </tr>
		                                                <tr>
		                                                      			
		                                                        <td  class="uportal-label"><xsl:value-of select="/biospecimen/PATIENT_strSurnameDisplay" />:</td>
		                                                        <td>&#160;</td>
		                                                        <td  class="uportal-text"><xsl:value-of select="/biospecimen/PATIENT_strSurname" /></td>
		                                                        		
		                                                </tr>
		                                                
		                                               
		                                        </table>
		                                </td>
		                                <td class="funcpanel_right_border">&#160;</td>
		                        </tr>
		                        <tr class="funcpanel_content">
		                                <td class="funcpanel_bottom_border" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
		                        </tr>
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
                                <tr class="uportal-input-text">
                                    <td width="30%" >
                                            <b><xsl:value-of select="/body/biospecimen/biospecimen_details/BIOSPECIMEN_strGradeDisplay" />: </b>
                                    </td>
                                    <td width="70%"  align="right"> 
                                            <xsl:value-of select="/body/biospecimen/biospecimen_details/BIOSPECIMEN_strGrade" />
                                    </td>
                                </tr>
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

