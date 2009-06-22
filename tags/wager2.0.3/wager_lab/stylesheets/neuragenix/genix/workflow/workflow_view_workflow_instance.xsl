<?xml version="1.0" encoding="utf-8"?>



<!-- 

    workflow_view_workflow_instance.xsl, part of the Workflow channel

    Author: aagustian@neuragenix.com

    Date: 21/04/2004

    Neuragenix copyright 2004 

-->





<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:include href="./workflow_menu.xsl"/>

<xsl:include href="../../common/common_btn_name.xsl"/>



	<xsl:output method="html" indent="no" />

	<xsl:param name="formParams">current=workflow_view_workflow_instance</xsl:param>

	<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>



	<xsl:template match="workflow">

	<!-- Get the parameters from the channel class -->

	<!--xsl:param name="WORKFLOW_TEMPLATE_strName"><xsl:value-of select="WORKFLOW_TEMPLATE_strName" /></xsl:param>

	<xsl:param name="WORKFLOW_TEMPLATE_strStatus"><xsl:value-of select="WORKFLOW_TEMPLATE_strStatus" /></xsl:param>

	<xsl:param name="WORKFLOW_TEMPLATE_intWorkflowTemplateKey"><xsl:value-of select="WORKFLOW_TEMPLATE_intWorkflowTemplateKey" /></xsl:param-->



	<form action="{$baseActionURL}?{$formParams}" method="post">

  

	<table width="100%">

        <tr>

            <td width="25%"></td>

            <td width="65%" class="uportal-label">

				<a href="{$baseActionURL}?current=workflow_designer">Create workflow |</a>

				<a href="{$baseActionURL}?current=workflow_search_workflow_template"> Search workflow template |</a>

				<a href="{$baseActionURL}?current=workflow_search_workflow_instance"> Search workflow instance |</a>

				<span class="uportal-channel-current-subtitle"> View workflow instance</span>

			</td>

			<td width="10%">

				<input type="button" name="back" value="{$backBtnLabel}" tabindex="1" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?current=workflow_search_workflow_instance&amp;back=true'" />

			</td>

        </tr>

        

    </table>

	<hr/>

	    

    <table width="100%">

		<tr valign="top">

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

					<xsl:variable name="WORKFLOW_INSTANCE_intWorkflowInstanceKey"><xsl:value-of select="workflow_view_workflow_instance_result/WORKFLOW_INSTANCE_intWorkflowInstanceKey" /></xsl:variable>

					<xsl:variable name="WORKFLOW_INSTANCE_strName"><xsl:value-of select="workflow_view_workflow_instance_result/WORKFLOW_INSTANCE_strName" /></xsl:variable>

					<xsl:variable name="WORKFLOW_INSTANCE_strStatus"><xsl:value-of select="workflow_view_workflow_instance_result/WORKFLOW_INSTANCE_strStatus" /></xsl:variable>

					<xsl:variable name="WORKFLOW_INSTANCE_dtDateCreated"><xsl:value-of select="workflow_view_workflow_instance_result/WORKFLOW_INSTANCE_dtDateCreated" /></xsl:variable>

					<xsl:variable name="WORKFLOW_INSTANCE_dtDateCompleted"><xsl:value-of select="workflow_view_workflow_instance_result/WORKFLOW_INSTANCE_dtDateCompleted" /></xsl:variable>



					<tr>

						<td width="20%" class="uportal-label"> 

							<xsl:value-of select="WORKFLOW_INSTANCE_intWorkflowInstanceKeyDisplay" />:

						</td>

						<td width="20%" class="uportal-label">

							<input type="text" readonly="readonly" name="{$WORKFLOW_INSTANCE_intWorkflowInstanceKey}" value="{$WORKFLOW_INSTANCE_intWorkflowInstanceKey}" size="25" tabindex="1" class="uportal-input-text" />

						</td>

						<td width="20%" class="uportal-label"/> 

						<td width="20%" class="uportal-label"> 

							<xsl:value-of select="WORKFLOW_INSTANCE_strNameDisplay" />:

						</td>

						<td width="20%" class="uportal-label">

							<input type="text" readonly="readonly" name="{$WORKFLOW_INSTANCE_strName}" value="{$WORKFLOW_INSTANCE_strName}" size="25" tabindex="2" class="uportal-input-text" />

						</td>

					</tr>



					<tr>

						<td width="20%" class="uportal-label"> 

							<xsl:value-of select="WORKFLOW_INSTANCE_dtDateCreatedDisplay" />:

						</td>

						<td width="20%" class="uportal-label">

							<input type="text" readonly="readonly" name="{$WORKFLOW_INSTANCE_dtDateCreated}" value="{$WORKFLOW_INSTANCE_dtDateCreated}" size="25" tabindex="1" class="uportal-input-text" />

						</td>

						<td width="20%" class="uportal-label"/> 

						<td width="20%" class="uportal-label"> 

							<xsl:value-of select="WORKFLOW_INSTANCE_dtDateCompletedDisplay" />:

						</td>

						<td width="20%" class="uportal-label">

							<input type="text" readonly="readonly" name="{$WORKFLOW_INSTANCE_dtDateCompleted}" value="{$WORKFLOW_INSTANCE_dtDateCompleted}" size="25" tabindex="2" class="uportal-input-text" />

						</td>

					</tr>



					<tr>

						<td width="20%" class="uportal-label"> 

							<xsl:value-of select="WORKFLOW_INSTANCE_strStatusDisplay" />:

						</td>

						<td width="20%" class="uportal-label">

							<input type="text" readonly="readonly" name="{$WORKFLOW_INSTANCE_strStatus}" value="{$WORKFLOW_INSTANCE_strStatus}" size="25" tabindex="3" class="uportal-input-text" />

						</td>

						<td width="20%" class="uportal-label"/> 

						<td width="20%" class="uportal-label"> 

						</td>

						<td width="20%" class="uportal-label">

						</td>

					</tr>

				</table>





				<table width="100%">

					<tr>

						<td width="5%"></td>

						<td width="90%" class="neuragenix-form-required-text">

							<script language="JavaScript">

								var screenW = 640, screenH = 400;

								if (parseInt(navigator.appVersion)>3) {

									if (navigator.appName=="Netscape") {

										screenW = (screen.width - 36) * 0.9;

									}

									else if (navigator.appName.indexOf("Microsoft")!=-1) {

										screenW = (screen.width - 42) * 0.9;

									}

								}



								var place;

								var places;

								var route;

								place = location.href; 

								places = place.split("?"); 

								if(places[1]){ 

								  route = places[1].split("WORKFLOW_INSTANCE_intWorkflowInstanceKey=")[1]; 

								  route = route.split("&amp;")[0]; 

								}

								document.write("&lt;applet code='media.neuragenix.genix.workflow.WFViewer.class' archive='media/jgraph/jgraph.jar' WIDTH='" + screenW + "' HEIGHT='" + screenH + "' &gt;&lt;param name='WorkflowInstanceKey' value='" + route + "'&gt;&lt;/applet&gt;" );

							</script>

							

							

						</td>

						<td width="5%"></td>

					</tr>

				</table>



				<table width="100%">

					<xsl:variable name="WORKFLOW_INSTANCE_intWorkflowInstanceKey"><xsl:value-of select="workflow_view_workflow_instance_result/WORKFLOW_INSTANCE_intWorkflowInstanceKey" /></xsl:variable>

					<xsl:variable name="WORKFLOW_INSTANCE_strName"><xsl:value-of select="workflow_view_workflow_instance_result/WORKFLOW_INSTANCE_strName" /></xsl:variable>

					<xsl:variable name="WORKFLOW_INSTANCE_strStatus"><xsl:value-of select="workflow_view_workflow_instance_result/WORKFLOW_INSTANCE_strStatus" /></xsl:variable>

					<xsl:variable name="WORKFLOW_INSTANCE_dtDateCreated"><xsl:value-of select="workflow_view_workflow_instance_result/WORKFLOW_INSTANCE_dtDateCreated" /></xsl:variable>

					<xsl:variable name="WORKFLOW_INSTANCE_dtDateCompleted"><xsl:value-of select="workflow_view_workflow_instance_result/WORKFLOW_INSTANCE_dtDateCompleted" /></xsl:variable>



					<tr>

						<td>

							<input type="button" name="suspend" value="{$suspendBtnLabel}" tabindex="11" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?current=workflow_view_workflow_instance&amp;action=suspend&amp;WORKFLOW_INSTANCE_intWorkflowInstanceKey={$WORKFLOW_INSTANCE_intWorkflowInstanceKey}'" />

							

							<input type="button" name="resume" value="{$resumeBtnLabel}" tabindex="12" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?current=workflow_view_workflow_instance&amp;action=resume&amp;WORKFLOW_INSTANCE_intWorkflowInstanceKey={$WORKFLOW_INSTANCE_intWorkflowInstanceKey}'" />

							

							<input type="button" name="delete" value="{$deleteBtnLabel}" tabindex="13" class="uportal-button" onclick="javascript:confirmDelete('{$baseActionURL}?current=workflow_view_workflow_instance&amp;action=delete&amp;WORKFLOW_INSTANCE_intWorkflowInstanceKey={$WORKFLOW_INSTANCE_intWorkflowInstanceKey}')" />

						</td>

					</tr>

				</table>

			</td>

        </tr>

    </table>

	</form>

    </xsl:template>



</xsl:stylesheet>	


