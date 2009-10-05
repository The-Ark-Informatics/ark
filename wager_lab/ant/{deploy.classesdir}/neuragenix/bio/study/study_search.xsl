<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./study_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

<xsl:param name="formParams">current=study_search</xsl:param>

  <xsl:output method="html" indent="no" />
  <!-- Get the parameters from the channel class -->
  <xsl:param name="strStudyName"><xsl:value-of select="strStudyName" /></xsl:param>
  <xsl:param name="strStudyOwner"><xsl:value-of select="strStudyOwner" /></xsl:param>
  <xsl:param name="dtStudyStart"><xsl:value-of select="strTitle" /></xsl:param>
  <xsl:param name="dtStudyEnd"><xsl:value-of select="dtStudyEnd" /></xsl:param>
 
  <xsl:template match="study">
	<table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Search study<br/><hr/>
			</td> 
		</tr> 
	</table>
	<form name="study_search" action="{$baseActionURL}?{$formParams}" method="post">
	<table width="100%">
		<tr>
			<td id="neuragenix-form-row-label" class="uportal-label">
				<xsl:value-of select="strStudyNameDisplay" />: 
			</td>
			<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
			<td class="uportal-label" id="neuragenix-form-row-input">
				<input type="text" name="strStudyName" size="20" class="uportal-input-text" tabindex="1" />
			</td>
			<td width="5%"></td>
			<td id="neuragenix-form-row-label" class="uportal-label">
				<xsl:value-of select="strStudyOwnerDisplay" />: 
			</td>
			<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
			<td class="uportal-label" id="neuragenix-form-row-input">
				<input type="text" name="strStudyOwner" size="20" class="uportal-input-text" tabindex="20" />
			</td>
		</tr>
     
		<tr>
		
			
			<td id="neuragenix-form-row-label" class="uportal-label">
				<xsl:value-of select="dtStudyStartDisplay" />: 
			</td>
			<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
			<td id="neuragenix-form-row" class="uportal-label">
				<input type="text" name="dtStudyStart" size="20" class="uportal-input-text" tabindex="30" />
			</td>
			<td width="5%"></td>
			<td id="neuragenix-form-row-label" class="uportal-label">
				<xsl:value-of select="dtStudyEndDisplay" />: 
			</td>
			<td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
			<td id="neuragenix-form-row-input" class="uportal-label">
				<input type="text" name="dtStudyEnd" size="20" class="uportal-input-text" tabindex="40" />
			</td>

		</tr>

	</table>
	<table width="100%">
			<tr>
				<td width="26%"></td>
				<td width="5%" class="uportal-label">
					<input type="submit" name="submit" value="{$searchBtnLabel}" class="uportal-button" tabindex="50" onblur="javascript:document.study_search.strStudyName.focus()" />
				</td>
				<td width="5%" class="uportal-label">
					<input type="button" name="clear" value="{$clearBtnLabel}" class="uportal-button" tabindex="60" onclick="javascript:confirmClear('{$baseActionURL}?{$formParams}')" />
				</td>
				<td width="64%"></td>
			</tr>

	</table>
	</form>
  </xsl:template>

      
       
 
</xsl:stylesheet>
