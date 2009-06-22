<?xml version="1.0" encoding="utf-8"?>

<!-- patient_menu.xsl. Menu used for all patients.-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="html" indent="no"/>

  <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>


  <xsl:template match="/">
  <script language="javascript" >

	function confirmDelete(aURL) {

	var confirmAnswer = confirm('Are you sure you want to delete this record?');


	if(confirmAnswer == true){
		window.location=aURL + '&amp;delete=true';
	}


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

	</script>
	
    <table width="100%">
	<tr valign="top">
		<td id="neuragenix-border-right" width="15%" class="uportal-channel-subtitle">
			Menu<hr></hr>
			<br></br>
			<a href="{$baseActionURL}?current=showlistmanagement_lov">Manage Lists</a>
			<br></br>
                        <!-- <a href="{$baseActionURL}?current=showlistmanagement">Manage Protein and Genes</a> -->
			<br></br>
			<!-- 
                            <a href="{$baseActionURL}?current=showusers">Manage Users</a>
                            <br></br>
                            <a href="{$baseActionURL}?current=showprofiles">Manage Profiles</a>
                            <br></br>
                            <a href="{$baseActionURL}?current=userprofile">Manage Users Profiles</a>
                            <br></br>
                            <a href="{$baseActionURL}?current=changepass">Change Password</a>
                        -->
		</td>
		<td width="5%"></td>
		<td width="80%">
				<xsl:apply-templates/>
		</td>
	</tr>
    </table> 
	   
    

  </xsl:template>

</xsl:stylesheet>

