<?xml version="1.0" encoding="utf-8"?>
<!-- patient_menu.xsl. Menu used for all patients.-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" indent="no"/>
	<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
	<xsl:param name="patientChannelURL">patientChannelURL_false</xsl:param>
	<xsl:param name="patientChannelTabOrder">patientChannelTabOrder</xsl:param>
	<xsl:param name="smartformAdminChannelURL">smartformAdminChannelURL_false</xsl:param>
	<xsl:param name="smartformAdminChannelTabOrder">smartformAdminChannelTabOrder</xsl:param>
	<xsl:template match="/">
		<script language="javascript">

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

	}
	function cleanURL(aURL) {

	var cleanURL = aURL.replace("'", "/'");
	window.location=cleanURL;

	}


	</script>
		<table width="100%" height="100%" border="0">
			<tr valign="top">
				<td id="neuragenix-border-right" width="15%" class="uportal-channel-subtitle">
			Menu<hr/>
					<br/>
					<a href="{$baseActionURL}?uP_root=root&amp;current=study_search">Search study</a>
					<br/>
					<a href="{$baseActionURL}?uP_root=root&amp;current=study_add">Add study</a>
					<br/>
					<br/>
                                        <br />
					<xsl:variable name="PermissionStudySettings"><xsl:value-of select="//PermissionStudySettings"/></xsl:variable>
                                        <xsl:if test="($PermissionStudySettings = 'true')">
                                            <a href="{$baseActionURL}?uP_root=root&amp;module=defaultStudySelection&amp;action=displayScreen">Study Settings</a>
                                        </xsl:if>
					<!--a href="{$baseActionURL}?uP_root=root&amp;current=survey_search">Search survey</a>
			<br></br>
			<a href="{$baseActionURL}?uP_root=root&amp;current=survey_add">Add survey</a-->
				</td>
				<td width="5%"/>
				<td width="80%">
					<xsl:apply-templates/>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
