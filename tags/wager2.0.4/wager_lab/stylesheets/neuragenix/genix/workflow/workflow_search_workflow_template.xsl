<?xml version="1.0" encoding="utf-8"?>



<!-- 

    workflow_search_workflow_template.xsl, part of the Workflow channel

    Author: aagustian@neuragenix.com

    Date: 14/04/2004

    Neuragenix copyright 2004 

-->





<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:include href="./workflow_menu.xsl"/>

<xsl:include href="../../common/common_btn_name.xsl"/>



	<xsl:output method="html" indent="no" />

	<xsl:param name="formParams">current=workflow_search_workflow_template</xsl:param>

	<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>



	<xsl:template match="workflow">

	<!-- Get the parameters from the channel class -->

	<xsl:param name="WORKFLOW_TEMPLATE_strName"><xsl:value-of select="WORKFLOW_TEMPLATE_strName" /></xsl:param>

	<xsl:param name="WORKFLOW_TEMPLATE_strStatus"><xsl:value-of select="WORKFLOW_TEMPLATE_strStatus" /></xsl:param>

	<xsl:param name="WORKFLOW_TEMPLATE_intWorkflowTemplateKey"><xsl:value-of select="WORKFLOW_TEMPLATE_intWorkflowTemplateKey" /></xsl:param>



	<form action="{$baseActionURL}?" method="post">

  

	<table width="100%">

		<xsl:variable name="WORKFLOW_TEMPLATE_intWorkflowTemplateKey"><xsl:value-of select="WORKFLOW_TEMPLATE_intWorkflowTemplateKey" /></xsl:variable>

		<xsl:variable name="source"><xsl:value-of select="source" /></xsl:variable>

		<xsl:variable name="strPackageID"><xsl:value-of select="strPackageID" /></xsl:variable>
		<xsl:variable name="strWorkflowID"><xsl:value-of select="strWorkflowID" /></xsl:variable>

		
		<tr>

            <td width="25%"></td>

            <td width="65%" class="uportal-label">

				<a href="{$baseActionURL}?current=workflow_designer">Create workflow |</a>

				<span class="uportal-channel-current-subtitle"> Search workflow template </span>

				<a href="{$baseActionURL}?current=workflow_search_workflow_instance">| Search workflow instance</a>

			</td>

			<td width="10%">

				<xsl:if test="prepare_runtimedata = 'true'">

					<xsl:choose>

						<xsl:when test="$source = 'workflow_search_workflow_template'">

							<input type="button" name="cancelInstantiation" value="{$backBtnLabel}" tabindex="1" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?current=workflow_search_workflow_template&amp;cancelInstantiation=true&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}'" />



						</xsl:when>

						<xsl:otherwise>

							<input type="button" name="cancelInstantiation" value="{$backBtnLabel}" tabindex="2" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?current=workflow_view_workflow_template&amp;cancelInstantiation=true&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;strPackageID={$strPackageID}&amp;strWorkflowID={$strWorkflowID}'" />



						</xsl:otherwise>

					</xsl:choose>

				</xsl:if>

			</td>

        </tr>

        

    </table>

	<hr/>

	    

    <table width="100%">

		<tr valign="top">



			<!-- check if this is not instantiation then do this else display the instantiantion page-->

			<xsl:choose>

				<xsl:when test="prepare_runtimedata = 'true'">

					<td width="100%">

						<table width="100%">

							<tr>

								<td class="neuragenix-form-required-text">

									<xsl:value-of select="strErrorMessage" />

									<xsl:value-of select="strInfo" />

								</td>

							</tr>

						</table>



						<table width="100%">

							<tr>

								<td width="20%" class="uportal-channel-table-header">

									Please enter the initialisation data: 

								</td>

							</tr>

						</table>



						<table width="100%">

							<xsl:for-each select="workflow_template_parameter_result">

							<xsl:variable name="WORKFLOW_TEMPLATE_PARAMETER_strType"><xsl:value-of select="WORKFLOW_TEMPLATE_PARAMETER_strType" /></xsl:variable>

							<xsl:variable name="WORKFLOW_TEMPLATE_PARAMETER_strName"><xsl:value-of select="WORKFLOW_TEMPLATE_PARAMETER_strName" /></xsl:variable>

							<xsl:variable name="Field_Year"><xsl:value-of select="Field_Year" /></xsl:variable>

							<tr>

								<td width="20%" class="uportal-label"> 

									<!--input type="hidden" name="current" value="workflow_search_workflow_template" /-->
									<xsl:if test="$WORKFLOW_TEMPLATE_PARAMETER_strName != 'strSystemUserName' and $WORKFLOW_TEMPLATE_PARAMETER_strName != 'strWorkflowInstanceKey'">
										<xsl:value-of select="WORKFLOW_TEMPLATE_PARAMETER_strDescription" />:
									</xsl:if>
								</td>

								

								<td>

									<xsl:choose>

										<xsl:when test="$WORKFLOW_TEMPLATE_PARAMETER_strType = 'Date' ">

										  <xsl:if test="$WORKFLOW_TEMPLATE_PARAMETER_strName != 'strSystemUserName' and $WORKFLOW_TEMPLATE_PARAMETER_strName != 'strWorkflowInstanceKey'">

											<select name="{$WORKFLOW_TEMPLATE_PARAMETER_strName}_Day" tabindex="7" class="uportal-input-text">

												<option></option>

												<xsl:for-each select="Field_Day">

													<option>

														<xsl:attribute name="value">

														<xsl:value-of select="." />

														</xsl:attribute> 

														<xsl:if test="@selected=1">

																<xsl:attribute name="selected">true</xsl:attribute> 

														</xsl:if>



														<xsl:value-of select="." />		

													</option>

												</xsl:for-each>

											</select>

											

											<select name="{$WORKFLOW_TEMPLATE_PARAMETER_strName}_Month" tabindex="8" class="uportal-input-text">

												<option></option>

												<xsl:for-each select="Field_Month">

													<option>

														<xsl:attribute name="value">

														<xsl:value-of select="." />

														</xsl:attribute> 

														<xsl:if test="@selected=1">

																<xsl:attribute name="selected">true</xsl:attribute> 

														</xsl:if>



														<xsl:value-of select="." />		

													</option>

												</xsl:for-each>

											</select>

											

											<input type="text" name="{$WORKFLOW_TEMPLATE_PARAMETER_strName}_Year" value="{$Field_Year}" size="5" tabindex="9" class="uportal-input-text" />
										  </xsl:if>

								

										</xsl:when>

										<xsl:otherwise>
											
											<xsl:if test="$WORKFLOW_TEMPLATE_PARAMETER_strName != 'strSystemUserName' and $WORKFLOW_TEMPLATE_PARAMETER_strName != 'strWorkflowInstanceKey'">
												<input type="text" name="{$WORKFLOW_TEMPLATE_PARAMETER_strName}" size="25" tabindex="1" class="uportal-input-text" />
											</xsl:if>

										</xsl:otherwise>

									</xsl:choose>

								</td>

							</tr>

							</xsl:for-each>

						</table>



						<table width="100%">

							<xsl:variable name="WORKFLOW_TEMPLATE_intWorkflowTemplateKey"><xsl:value-of select="WORKFLOW_TEMPLATE_intWorkflowTemplateKey" /></xsl:variable>
							<xsl:variable name="WORKFLOW_PACKAGE_strWorkflowPackageXPDLId"><xsl:value-of select="WORKFLOW_PACKAGE_strWorkflowPackageXPDLId" /></xsl:variable>
							<xsl:variable name="WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId"><xsl:value-of select="WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId" /></xsl:variable>
						    <xsl:variable name="strPackageID"><xsl:value-of select="strPackageID" /></xsl:variable>
							<xsl:variable name="strWorkflowID"><xsl:value-of select="strWorkflowID" /></xsl:variable>
	

							<xsl:variable name="source"><xsl:value-of select="source" /></xsl:variable>

							<tr>

								<td>

									<xsl:choose>

										<xsl:when test="$source = 'workflow_search_workflow_template'">

											<input type="hidden" name="current" value="workflow_search_workflow_template" />

											<input type="submit" name="instantiate" tabindex="10" value="{$submitBtnLabel}" class="uportal-button" />



											<input type="button" name="clear" value="{$clearBtnLabel}" tabindex="11" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=workflow_search_workflow_template&amp;action=prepare_runtimedata&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}')" />

											

											<input type="button" name="cancelInstantiation" value="{$cancelBtnLabel}" tabindex="12" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?current=workflow_search_workflow_template&amp;cancelInstantiation=true&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}'" />



										</xsl:when>

										<xsl:otherwise>
											

											<!--input type="hidden" name="current" value="workflow_view_workflow_template" />
											<input type="hidden" name="strPackageID" value="{$strPackageID}" />
											<input type="hidden" name="strWorkflowID" value="{$strWorkflowID}" /-->

											<!--input type="submit" name="instantiate" tabindex="10" value="{$submitBtnLabel}" class="uportal-button" /-->
											<input type="button" name="instantiate" value="{$submitBtnLabel}" tabindex="10" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?current=workflow_view_workflow_template&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;strPackageID={$strPackageID}&amp;strWorkflowID={$strWorkflowID}'" />



											<input type="button" name="clear" value="{$clearBtnLabel}" tabindex="11" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=workflow_view_workflow_template&amp;action=prepare_runtimedata&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;strPackageID={$strPackageID}&amp;strWorkflowID={$strWorkflowID}')" />

											

											<input type="button" name="cancelInstantiation" value="{$cancelBtnLabel}" tabindex="12" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?current=workflow_view_workflow_template&amp;cancelInstantiation=true&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;strPackageID={$strPackageID}&amp;strWorkflowID={$strWorkflowID}'" />



										</xsl:otherwise>

									</xsl:choose>



									<input type="hidden" name="WORKFLOW_TEMPLATE_intWorkflowTemplateKey" value="{$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}" />

								</td>

							</tr>

						</table>

					</td>

				</xsl:when>

				<xsl:otherwise>

					<td width="25%">

						

						<table width="100%">

							<tr>

								<td class="neuragenix-form-required-text">

									<xsl:value-of select="strErrorMessage" />

									<xsl:value-of select="strInfo" />

								</td>

							</tr>

						</table>

						

						<table width="100%">

							<tr>

								<td width="40%" class="uportal-label">

									<xsl:value-of select="WORKFLOW_TEMPLATE_strNameDisplay" />:

								</td>

								<td width="40%">

									<input type="text" name="WORKFLOW_TEMPLATE_strName" size="25" tabindex="1" class="uportal-input-text" />

								</td>

							</tr>

							

							<tr>

								<td width="40%" class="uportal-label">

									<xsl:value-of select="WORKFLOW_TEMPLATE_strStatusDisplay" />:

								</td>

								<td width="40%">

									<select name="WORKFLOW_TEMPLATE_strStatus" tabindex="3" class="uportal-input-text">

										<option value="" />

										<xsl:for-each select="WORKFLOW_TEMPLATE_strStatus">

											<option>

												<xsl:attribute name="value">

												<xsl:value-of select="." />

												</xsl:attribute> 

												<xsl:if test="@selected=1">

														<xsl:attribute name="selected">true</xsl:attribute> 

												</xsl:if>



												<xsl:value-of select="." />		

											</option>

										</xsl:for-each>

									</select>

								</td>

							</tr>

							

						</table>

						

						<table width="90%">

							<tr><td><hr /></td></tr>

						</table>

						

						<table width="100%">

							<tr>

								<td>

									<input type="hidden" name="current" value="workflow_search_workflow_template" />

									<input type="submit" name="search" tabindex="10" value="{$submitBtnLabel}" class="uportal-button" />

									<input type="button" name="clear" value="{$clearBtnLabel}" tabindex="11" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=workflow_search_workflow_template')" />

								</td>

							</tr>

						</table>

					</td>



					<td width="75%">

						

						<table width="100%">

							<tr>

								<td width="20%" class="uportal-channel-table-header">

									<xsl:value-of select="WORKFLOW_PACKAGE_strNameDisplay" /> 

								</td>

							

								<td width="20%" class="uportal-channel-table-header">

									<xsl:value-of select="WORKFLOW_TEMPLATE_strNameDisplay" />

								</td>

								

								<td width="10%" class="uportal-channel-table-header">

									<xsl:value-of select="WORKFLOW_TEMPLATE_strStatusDisplay" />

								</td>

								

								<td width="10%" class="uportal-channel-table-header">

									Activate

								</td>



								<td width="10%" class="uportal-channel-table-header">

									Deactivate

								</td>



								<td width="10%" class="uportal-channel-table-header">

									Delete

								</td>



								<td width="10%" class="uportal-channel-table-header">

									Edit

								</td>



								<td width="10%" class="uportal-channel-table-header">

									Instantiate

								</td>

								

							</tr>

						</table>

						<hr/>

							

						<table width="100%">

							<xsl:for-each select="workflow_search_workflow_template_result">

							<xsl:variable name="WORKFLOW_TEMPLATE_intWorkflowTemplateKey"><xsl:value-of select="WORKFLOW_TEMPLATE_intWorkflowTemplateKey" /></xsl:variable>

							<xsl:variable name="WORKFLOW_TEMPLATE_strName"><xsl:value-of select="WORKFLOW_TEMPLATE_strName" /></xsl:variable>

							<xsl:variable name="WORKFLOW_PACKAGE_strName"><xsl:value-of select="WORKFLOW_PACKAGE_strName" /></xsl:variable>
							<xsl:variable name="WORKFLOW_PACKAGE_strWorkflowPackageXPDLId"><xsl:value-of select="WORKFLOW_PACKAGE_strWorkflowPackageXPDLId" /></xsl:variable>
							<xsl:variable name="WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId"><xsl:value-of select="WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId" /></xsl:variable>

							<tr>

								

								<td width="20%" class="uportal-label"> 

									<a href="{$baseActionURL}?current=workflow_view_workflow_template&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;strPackageID={$WORKFLOW_PACKAGE_strWorkflowPackageXPDLId}&amp;strWorkflowID={$WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId}">

										<xsl:value-of select="WORKFLOW_PACKAGE_strName" />

									</a>

								</td>

								

								<td width="20%" class="uportal-label"> 

									<a href="{$baseActionURL}?current=workflow_view_workflow_template&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;strPackageID={$WORKFLOW_PACKAGE_strWorkflowPackageXPDLId}&amp;strWorkflowID={$WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId}">

										<xsl:value-of select="WORKFLOW_TEMPLATE_strName" />

									</a>

								</td>

								

								<td width="10%" class="uportal-label">

									<a href="{$baseActionURL}?current=workflow_view_workflow_template&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;strPackageID={$WORKFLOW_PACKAGE_strWorkflowPackageXPDLId}&amp;strWorkflowID={$WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId}">

										<xsl:value-of select="WORKFLOW_TEMPLATE_strStatus" />

									</a>

								</td>



								<td width="10%" class="uportal-label">

									<a href="{$baseActionURL}?current=workflow_search_workflow_template&amp;action=activate&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}">Activate</a>

								</td>



								<td width="10%" class="uportal-label">

									<a href="{$baseActionURL}?current=workflow_search_workflow_template&amp;action=deactivate&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;WORKFLOW_TEMPLATE_strName={$WORKFLOW_TEMPLATE_strName}&amp;WORKFLOW_PACKAGE_strName={$WORKFLOW_PACKAGE_strName}">Deactivate</a>

								</td>



								<td width="10%" class="uportal-label">

									<a href="javascript:confirmDelete('{$baseActionURL}?current=workflow_search_workflow_template&amp;action=delete&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;WORKFLOW_TEMPLATE_strName={$WORKFLOW_TEMPLATE_strName}&amp;WORKFLOW_PACKAGE_strName={$WORKFLOW_PACKAGE_strName}')">Delete</a>

								</td>



								<td width="10%" class="uportal-label">

									<a href="{$baseActionURL}?current=workflow_designer&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;strPackageID={$WORKFLOW_PACKAGE_strWorkflowPackageXPDLId}&amp;strWorkflowID={$WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId}">Edit</a>

								</td>



								<td width="10%" class="uportal-label">

									<a href="{$baseActionURL}?current=workflow_search_workflow_template&amp;action=prepare_runtimedata&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;WORKFLOW_TEMPLATE_strName={$WORKFLOW_TEMPLATE_strName}&amp;WORKFLOW_PACKAGE_strName={$WORKFLOW_PACKAGE_strName}">Instantiate</a>

								</td>



							</tr>

							</xsl:for-each>

						</table>

					</td>

				</xsl:otherwise>

			</xsl:choose>

        </tr>

    </table>

	</form>

    </xsl:template>



</xsl:stylesheet>	

