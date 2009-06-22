<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./study_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

  <xsl:output method="html" indent="no" />
  

  <xsl:template match="study">
<xsl:param name="formParams">current=study_download_ethics&amp;intEthicsStudyID=<xsl:value-of select="intEthicsStudyID" />&amp;intStudyID=<xsl:value-of select="intStudyID" />&amp;ETHICS_CONSENT_Timestamp=<xsl:value-of select="ETHICS_CONSENT_Timestamp" /></xsl:param>

<xsl:param name="intStudyID"><xsl:value-of select="intStudyID" /></xsl:param>
  <xsl:param name="intEthicsStudyID"><xsl:value-of select="intEthicsStudyID" /></xsl:param>
   <xsl:param name="ETHICS_CONSENT_Timestamp"><xsl:value-of select="ETHICS_CONSENT_Timestamp"/></xsl:param>
	<form action="{$baseActionURL}?{$formParams}" method="post" enctype="multipart/form-data">
<!--        <input type="hidden" name="ETHICS_CONSENT_Timestamp" value="{$ETHICS_CONSENT_Timestamp}"/>-->
	<table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Add ethics file<br/><hr/>
			</td> 
		</tr> 
	</table>
	
	<table width="100%">
		<tr>
			<td class="neuragenix-form-required-text" width="50%">
				<xsl:value-of select="strErrorFileToLarge" />
			</td>
		</tr>
		<tr>
			<td class="uportal-label">
			Please select the signed ethics file to add
			</td>
		</tr>
		<tr>
			<td class="uportal-label" height="5px">
				
			</td>
		</tr>
		<tr>
			<td>
				<input type="file" name="flEthicsFile" class="uportal-input-text"/>
			</td>
		</tr>
<!--                <tr>
			<td>
				<xsl:value-of select="$ETHICS_CONSENT_Timestamp"/>
			</td>
		</tr>-->
	</table>
	<table width="100%">
		<tr>
			<td width="5%" class="uportal-label">
				<input type="submit" name="saveFile" value="{$saveBtnLabel}" class="uportal-button" />
			
			</td>
			<td width="5%" class="uportal-label">	
				<input type="submit" name="cancel" value="{$cancelBtnLabel}" class="uportal-button" />
			</td>
			<td width="90%"></td>
		</tr>

	</table>

	</form>
  </xsl:template>

      
       
 
</xsl:stylesheet>
