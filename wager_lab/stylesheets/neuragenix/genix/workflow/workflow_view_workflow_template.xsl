<?xml version="1.0" encoding="utf-8"?>



<!-- 

    workflow_view_workflow_template.xsl, part of the Workflow channel

    Author: aagustian@neuragenix.com

    Date: 21/04/2004

    Neuragenix copyright 2004 

-->





<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:include href="./workflow_menu.xsl"/>

<xsl:include href="../../common/common_btn_name.xsl"/>



	<xsl:output method="html" indent="no" />

	<xsl:param name="formParams">current=workflow_view_workflow_template</xsl:param>

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

				<span class="uportal-channel-current-subtitle"> View workflow template </span>

				<a href="{$baseActionURL}?current=workflow_search_workflow_instance">| Search workflow instance</a>

			</td>

			<td width="10%">

				<input type="button" name="back" value="{$backBtnLabel}" tabindex="1" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?current=workflow_search_workflow_template&amp;back=true'" />

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

					<xsl:variable name="WORKFLOW_TEMPLATE_intWorkflowTemplateKey"><xsl:value-of select="workflow_view_workflow_template_result/WORKFLOW_TEMPLATE_intWorkflowTemplateKey" /></xsl:variable>

					<xsl:variable name="WORKFLOW_TEMPLATE_strName"><xsl:value-of select="workflow_view_workflow_template_result/WORKFLOW_TEMPLATE_strName" /></xsl:variable>

					<xsl:variable name="WORKFLOW_TEMPLATE_strStatus"><xsl:value-of select="workflow_view_workflow_template_result/WORKFLOW_TEMPLATE_strStatus" /></xsl:variable>

					<xsl:variable name="WORKFLOW_PACKAGE_strName"><xsl:value-of select="workflow_view_workflow_template_result/WORKFLOW_PACKAGE_strName" /></xsl:variable>



					<tr>

						<td width="20%" class="uportal-label"> 

							<xsl:value-of select="WORKFLOW_PACKAGE_strNameDisplay" />:

						</td>

						<td width="20%" class="uportal-label">

							<input type="text" readonly="readonly" name="{$WORKFLOW_PACKAGE_strName}" value="{$WORKFLOW_PACKAGE_strName}" size="25" tabindex="1" class="uportal-input-text" />

						</td>

						<td width="20%" class="uportal-label"/> 

						<td width="20%" class="uportal-label"> 

							<xsl:value-of select="WORKFLOW_TEMPLATE_strNameDisplay" />:

						</td>

						<td width="20%" class="uportal-label">

							<input type="text" readonly="readonly" name="{$WORKFLOW_TEMPLATE_strName}" value="{$WORKFLOW_TEMPLATE_strName}" size="25" tabindex="2" class="uportal-input-text" />

						</td>

					</tr>



					<tr>

						<td width="20%" class="uportal-label"> 

							<xsl:value-of select="WORKFLOW_TEMPLATE_strStatusDisplay" />:

						</td>

						<td width="20%" class="uportal-label">

							<input type="text" readonly="readonly" name="{$WORKFLOW_TEMPLATE_strStatus}" value="{$WORKFLOW_TEMPLATE_strStatus}" size="25" tabindex="3" class="uportal-input-text" />

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
                                                var packageid;
                                                var workflowid;
                                                place = location.href; 
                                                places = place.split("?"); 
                                                if(places[1]){ 
                                                  packageid = places[1].split("strPackageID=")[1]; 
                                                  packageid = packageid.split("&amp;")[0]; 
                                                }

                                                places = place.split("?"); 
                                                if(places[1]){ 
                                                  workflowid = places[1].split("strWorkflowID=")[1]; 
                                                  workflowid = workflowid.split("&amp;")[0]; 
                                                }

                                                document.write("&lt;applet code='media.neuragenix.genix.workflow.WFTemplateViewer.class' archive='media/jgraph/jgraph.jar' WIDTH='" + screenW + "' HEIGHT='" + screenH + "' &gt;&lt;param name='strPackageID' value='" + packageid + "'&gt;" + " &lt;param name='strWorkflowID' value='" + workflowid + "'&gt;" + "&lt;/applet&gt;" );

                                            </script>


                                        </td>
                                        <td width="5%"></td>
                                    </tr>

                                </table>

				<table width="100%">

					<xsl:variable name="WORKFLOW_TEMPLATE_intWorkflowTemplateKey"><xsl:value-of select="workflow_view_workflow_template_result/WORKFLOW_TEMPLATE_intWorkflowTemplateKey" /></xsl:variable>

					<xsl:variable name="WORKFLOW_TEMPLATE_strName"><xsl:value-of select="workflow_view_workflow_template_result/WORKFLOW_TEMPLATE_strName" /></xsl:variable>

					<xsl:variable name="WORKFLOW_PACKAGE_strName"><xsl:value-of select="workflow_view_workflow_template_result/WORKFLOW_PACKAGE_strName" /></xsl:variable>

					<xsl:variable name="WORKFLOW_PACKAGE_strWorkflowPackageXPDLId"><xsl:value-of select="workflow_view_workflow_template_result/WORKFLOW_PACKAGE_strWorkflowPackageXPDLId" /></xsl:variable>
					<xsl:variable name="WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId"><xsl:value-of select="workflow_view_workflow_template_result/WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId" /></xsl:variable>

					<tr>

						<td>

							<input type="button" name="activate" value="{$activateBtnLabel}" tabindex="11" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?current=workflow_view_workflow_template&amp;action=activate&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;strPackageID={$WORKFLOW_PACKAGE_strWorkflowPackageXPDLId}&amp;strWorkflowID={$WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId}'" />



							<input type="button" name="deactivate" value="{$deactivateBtnLabel}" tabindex="12" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?current=workflow_view_workflow_template&amp;action=deactivate&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;strPackageID={$WORKFLOW_PACKAGE_strWorkflowPackageXPDLId}&amp;strWorkflowID={$WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId}'" />



							<input type="button" name="delete" value="{$deleteBtnLabel}" tabindex="13" class="uportal-button" onclick="javascript:confirmDelete('{$baseActionURL}?current=workflow_view_workflow_template&amp;action=delete&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;strPackageID={$WORKFLOW_PACKAGE_strWorkflowPackageXPDLId}&amp;strWorkflowID={$WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId}')" />



							<input type="button" name="edit" value="{$editBtnLabel}" tabindex="14" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?current=workflow_designer&amp;action=edit&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;WORKFLOW_TEMPLATE_strName={$WORKFLOW_TEMPLATE_strName}&amp;WORKFLOW_PACKAGE_strName={$WORKFLOW_PACKAGE_strName}&amp;strPackageID={$WORKFLOW_PACKAGE_strWorkflowPackageXPDLId}&amp;strWorkflowID={$WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId}'" />



							<input type="button" name="prepare_runtimedata" value="{$instantiateBtnLabel}" tabindex="15" class="uportal-button" onclick="javascript:window.location='{$baseActionURL}?current=workflow_view_workflow_template&amp;action=prepare_runtimedata&amp;WORKFLOW_TEMPLATE_intWorkflowTemplateKey={$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}&amp;strPackageID={$WORKFLOW_PACKAGE_strWorkflowPackageXPDLId}&amp;strWorkflowID={$WORKFLOW_TEMPLATE_strWorkflowTemplateXPDLId}'" />



							<!--input type="hidden" name="WORKFLOW_TEMPLATE_intWorkflowTemplateKey" value="{$WORKFLOW_TEMPLATE_intWorkflowTemplateKey}" /-->

						</td>

					</tr>

				</table>

			</td>

        </tr>

    </table>

    
	</form>

    </xsl:template>



</xsl:stylesheet>	


