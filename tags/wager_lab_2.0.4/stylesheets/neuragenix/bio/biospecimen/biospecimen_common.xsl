<?xml version="1.0" encoding="utf-8"?>

<!-- Biospecimen Stylesheet Commons
     $Id : $
     Copyright (C) Neuragenix Pty Ltd, 2005
     Author : Daniel Murley
     Email : dmurley@neuragenix.com

     This stylesheet contains a set of common functions/templates that are available
     to all the other biospecimen stylesheets

-->


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!-- xsl:output method="html" indent="no"/ -->

  <xsl:template match="/">
      <!-- Global Functions/Variables -->
      <link rel="stylesheet" type="text/css" href="stylesheets/neuragenix/bio/xmlTree.css"/>
  </xsl:template>
      
  

  <xsl:template name="FlaggedRecords">
        <xsl:if test='count(biospecimen/flagged) &gt; 0'>
            Flagged Records :
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
  </xsl:template>
  
  <xsl:template name="Scripting">
      <script language="javascript" >

            function confirmDelete(aURL) {

            var confirmAnswer = confirm('Are you sure you want to delete this record?');

                if(confirmAnswer == true)
                {
                        window.location=aURL + '&amp;delete=true';
                }


            } 

            function jumpTo(aURL)
            {
                window.location=aURL;
            }

            function confirmClear(aURL) 
            {

                var confirmAnswer = confirm('Are you sure you want to clear the fields?');


                if(confirmAnswer == true)
                {
                    window.location=aURL + '&amp;clear=true';
                }
                
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
   </xsl:template>
   
   <xsl:template name="menu">
        Menu<hr></hr>
        <br></br>


        <a href="{$baseActionURL}?uP_root=root&amp;current=biospecimen_search">Search biospecimens</a>
        <br></br>

        <a href="{$baseActionURL}?uP_root=root&amp;current=bp_allocate&amp;stage=BEGIN">Quantity allocation</a>
        <br></br>
        <br />
        
   </xsl:template>
   
   <!-- TODO: Check if field is actually part of a patient details box set of formfields -->   
   <xsl:template name="PatientDetailsBox">
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
                <tr class="uportal-input-text">
                    <td width="30%" >
                            <b><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_strHospitalURDisplay" />: </b>
                    </td>
                    <td width="70%" align="right">
                            <xsl:value-of select="/body/biospecimen/patient_details/PATIENT_strHospitalUR" />
                    </td>
                </tr>

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
   
   </xsl:template>
   
   <xsl:template name="BiospecimenDetailsBox">
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
   </xsl:template>
   
   
</xsl:stylesheet>

