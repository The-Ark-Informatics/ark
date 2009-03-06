<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./workflow_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

<xsl:param name="formParams">current=workflow_search_workflow_template</xsl:param>

  <xsl:output method="html" indent="no" />
  <!-- Get the parameters from the channel class -->
  <xsl:param name="WORKFLOW_TEMPLATE_strName"><xsl:value-of select="WORKFLOW_TEMPLATE_strName" /></xsl:param>
  <xsl:param name="WORKFLOW_TEMPLATE_strStatus"><xsl:value-of select="WORKFLOW_TEMPLATE_strStatus" /></xsl:param>
  
  <xsl:template match="workflow">
	<table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Workflow template search result<br/><hr/>
			</td> 
		</tr> 
	</table>

	<form action="{$baseActionURL}?{$formParams}" method="post">
		<table width="100%">
			<tr>
				<td width="30%" id="neuragenix-form-row-input-label" class="uportal-label">
                        <xsl:value-of select="WORKFLOW_PACKAGE_strNameDisplay" /> 
                </td>
                <td width="30%" id="neuragenix-form-row-input-label" class="uportal-label">
                        <xsl:value-of select="WORKFLOW_TEMPLATE_strNameDisplay" /> 
                </td>
                <td width="20%" id="neuragenix-form-row-input-label" class="uportal-label">
                        <xsl:value-of select="WORKFLOW_TEMPLATE_strStatusDisplay" />  
                </td>
                <td width="20%" id="neuragenix-end-spacer"></td>
              </tr>
		</table>
		<table width="100%">
			<xsl:for-each select="workflow_search_workflow_template_result">
			  <xsl:choose>
				<xsl:when test="position() mod 2 != 0">
					<tr>
						<td width="30%" class="uportal-background-light">
								<xsl:value-of select="WORKFLOW_PACKAGE_strName" /> 
						</td>
						<td width="30%" class="uportal-background-light">
								<xsl:value-of select="WORKFLOW_TEMPLATE_strName" /> 
						</td>
						<td width="20%" class="uportal-background-light">
								<xsl:value-of select="WORKFLOW_TEMPLATE_strStatus" />
						</td>
						<td width="20%" id="neuragenix-end-spacer"></td>
					</tr>
		  		</xsl:when>
		  	  <xsl:otherwise>
				    <tr>
					    <td width="30%" id="neuragenix-form-row-input">
								<xsl:value-of select="WORKFLOW_PACKAGE_strName" /> 
						</td>
						<td width="30%" id="neuragenix-form-row-input">
								<xsl:value-of select="WORKFLOW_TEMPLATE_strName" /> 
						</td>
						<td width="20%" id="neuragenix-form-row-input">
								<xsl:value-of select="WORKFLOW_TEMPLATE_strStatus" />
						</td>
						<td width="20%" id="neuragenix-end-spacer"></td>
					</tr>
		      </xsl:otherwise>
              </xsl:choose>
			</xsl:for-each>
		</table>
		<table width="100%">
			<tr>	
				<td width="20%">
					<input type="button" name="button" value="{$searchBtnLabel}" tabindex="1" class="uportal-button" onClick="javascript:window.location='{$baseActionURL}?current=workflow_search_workflow_template'"/>
				</td>
									  
				<td width="80%"></td>
			</tr>
        </table>
	</form>
  </xsl:template>

      
       
 
</xsl:stylesheet>
