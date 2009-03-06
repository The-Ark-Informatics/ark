<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./study_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

  <xsl:output method="html" indent="no" />

  <xsl:template match="study">
  <!-- Get the parameters from the channel class -->
  <xsl:param name="strStudyName"><xsl:value-of select="strStudyName" /></xsl:param>
  <xsl:param name="strStudyOwner"><xsl:value-of select="strStudyOwner" /></xsl:param>
  <xsl:param name="strStudyDesc"><xsl:value-of select="strStudyDesc" /></xsl:param>
  <xsl:param name="dtStudyStart"><xsl:value-of select="dtStudyStart" /></xsl:param>
  <xsl:param name="dtStudyEnd"><xsl:value-of select="dtStudyEnd" /></xsl:param>
  <xsl:param name="intTargetPatientNo"><xsl:value-of select="intTargetPatientNo" /></xsl:param>
  <xsl:param name="intActualPatientNo"><xsl:value-of select="intActualPatientNo" /></xsl:param>
  <xsl:param name="strStudyCode"><xsl:value-of select="strStudyCode" /></xsl:param>
  


	<form name="study_add" action="{$baseActionURL}?current=study_add" method="post">
	<table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Add Project<br/><hr/>
			</td>
		</tr>
	</table>
	<table width="100%">
		 
		<tr>
			<td class="neuragenix-form-required-text" width="50%">
					<xsl:value-of select="strErrorDuplicateKey" /><xsl:value-of select="strErrorRequiredFields" /><xsl:value-of select="strErrorInvalidDataFields" /><xsl:value-of select="strErrorInvalidData" /><xsl:value-of select="strError" />
			</td>
			<td class="neuragenix-form-required-text" width="50%" id="neuragenix-required-header" align="right">
			* = Required fields
			</td>

		</tr>
	</table>
	<table width="100%">
		<tr >
			<td width="1%" class="neuragenix-form-required-text">*</td>
			<td width="20%" class="uportal-label">
				<xsl:value-of select="strStudyNameDisplay" />: 
			</td>
			
			<td width="79%" class="uportal-label" id="neuragenix-form-row-input-input">
				<input type="text" name="strStudyName" size="30" value="{$strStudyName}" tabindex="1" class="uportal-input-text" />
			</td>
			

		</tr>
		<tr >
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="20%" class="uportal-label">
				<xsl:value-of select="strStudyCodeDisplay" />: 
			</td>
			
			<td width="79%" class="uportal-label" id="neuragenix-form-row-input-input">
				<input type="text" name="strStudyCode" size="30" value="{$strStudyCode}" tabindex="1" class="uportal-input-text" />
			</td>
			

      </tr>	
         <tr>
			<td width="1%" class="neuragenix-form-required-text">*</td>
			<td width="20%" class="uportal-label">
				<xsl:value-of select="strStudyOwnerDisplay" />: 
			</td>
			
			<td width="79%" class="uportal-label" id="neuragenix-form-row-input-input">
				<input type="text" name="strStudyOwner" size="30" value="{$strStudyOwner}" tabindex="20" class="uportal-input-text" />
			</td>
		</tr>
		
		<tr>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="20%" class="uportal-label">
				<xsl:value-of select="dtStudyStartDisplay" />: 
			</td>
			
			<td class="uportal-label" width="79%">
				<input type="text" name="dtStudyStart" size="12" value="{$dtStudyStart}" tabindex="30" class="uportal-input-text" />
			</td>
		</tr>
		<tr>
			<td width="1%" class="neuragenix-form-required-text">*</td>
			<td width="20%" class="uportal-label">
				<xsl:value-of select="dtStudyEndDisplay" />: 
			</td>
			
			<td class="uportal-label" width="79%">
				<input type="text" name="dtStudyEnd" size="12" value="{$dtStudyEnd}" tabindex="40" class="uportal-input-text" />
			</td>
		</tr>
		<tr>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="20%" class="uportal-label">
				<xsl:value-of select="intTargetPatientNoDisplay" />: 
			</td>
			
			<td class="uportal-label" width="79%">
				<input type="text" name="intTargetPatientNo" size="5" value="{$intTargetPatientNo}" tabindex="50" class="uportal-input-text" />
			</td>
		</tr>
		<tr>
			<td width="1%" class="neuragenix-form-required-text"></td>
			<td width="20%" class="uportal-label">
				<xsl:value-of select="strStudyDescDisplay" />: 
			</td>
			
			<td class="uportal-label" width="79%">
				<textarea name="strStudyDesc" rows="5" cols="28" tabindex="60" class="uportal-input-text"><xsl:value-of select="strStudyDesc" /></textarea>

			</td>
		</tr>
                

                
                
                
                
		
	</table>
	<table width="100%">
		<tr>
			<td width="1%"></td>
			<td width="21%" class="uportal-label">
			</td>
			<td width="5%" class="uportal-label">
				<input type="button" name="clear" value="{$clearBtnLabel}" tabindex="80" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=study_add')" />
			</td>
			<td width="5%" class="uportal-label">

				<input type="submit" name="save" value="{$saveBtnLabel}" tabindex="70" class="uportal-button" onblur="javascript:document.study_add.strStudyName.focus()" />
			</td>
			<td width="68%" class="uportal-label"></td>

		</tr>

	</table>
	</form>
  </xsl:template>

      
       
 
</xsl:stylesheet>
