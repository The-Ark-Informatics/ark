<?xml version="1.0" encoding="utf-8"?>



<!-- 

    workflow_search_workflow_instance.xsl, part of the Workflow channel

    Author: aagustian@neuragenix.com

    Date: 15/04/2004

    Neuragenix copyright 2004 

-->





<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:include href="./workflow_menu.xsl"/>

<xsl:include href="../../common/common_btn_name.xsl"/>



	<xsl:output method="html" indent="no" />

	<xsl:param name="formParams">current=workflow_search_workflow_instance</xsl:param>

	<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>



	<xsl:template match="workflow">

	<!-- Get the parameters from the channel class -->

	<xsl:param name="WORKFLOW_INSTANCE_strName"><xsl:value-of select="WORKFLOW_INSTANCE_strName" /></xsl:param>

	<xsl:param name="WORKFLOW_INSTANCE_strStatus"><xsl:value-of select="WORKFLOW_INSTANCE_strStatus" /></xsl:param>

 	<xsl:param name="WORKFLOW_INSTANCE_dtDateCreated_Year"><xsl:value-of select="WORKFLOW_INSTANCE_dtDateCreated_Year" /></xsl:param> 

	<xsl:param name="WORKFLOW_INSTANCE_dtDateCompleted_Year"><xsl:value-of select="WORKFLOW_INSTANCE_dtDateCompleted_Year" /></xsl:param> 



	<form action="{$baseActionURL}?{$formParams}" method="post">

  

	<table width="100%">

        <tr>

            <td width="25%"></td>

            <td width="75%" class="uportal-label">

				<a href="{$baseActionURL}?current=workflow_designer">Create workflow </a>

				<a href="{$baseActionURL}?current=workflow_search_workflow_template">| Search workflow template |</a>

				<span class="uportal-channel-current-subtitle"> Search workflow instance</span>

            </td>

        </tr>

        

    </table>

	<hr/>

	    

    <table width="100%">

        <tr valign="top">

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

                            <xsl:value-of select="WORKFLOW_INSTANCE_intWorkflowInstanceKeyDisplay" />:

                        </td>

                        <td width="40%">

                            <input type="text" name="WORKFLOW_INSTANCE_intWorkflowInstanceKey" size="25" tabindex="1" class="uportal-input-text" />

                        </td>

                    </tr>

					

                    <tr>

                        <td width="40%" class="uportal-label">

                            <xsl:value-of select="WORKFLOW_INSTANCE_strNameDisplay" />:

                        </td>

                        <td width="40%">

                            <input type="text" name="WORKFLOW_INSTANCE_strName" size="25" tabindex="1" class="uportal-input-text" />

                        </td>

                    </tr>

                    

                    <tr>

                        <td width="40%" class="uportal-label">

                            <xsl:value-of select="WORKFLOW_INSTANCE_strStatusDisplay" />:

                        </td>

                        <td width="40%">

                            <select name="WORKFLOW_INSTANCE_strStatus" tabindex="3" class="uportal-input-text">

                                <option value="" />

								<xsl:for-each select="WORKFLOW_INSTANCE_strStatus">

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



					<tr>

                        <td width="40%" class="uportal-label">

                            <xsl:value-of select="WORKFLOW_INSTANCE_dtDateCreatedDisplay" />:

                        </td>

						<td width="60%">



							<select name="WORKFLOW_INSTANCE_strDateCreatedOperator" tabindex="4" class="uportal-input-text">

                                <option></option>

                                <option>=</option>

								<option>&lt;&gt;</option>

								<option>&lt;</option>

								<option>&lt;=</option>

								<option>&gt;</option>

								<option>&gt;=</option>

							</select>



                            <select name="WORKFLOW_INSTANCE_dtDateCreated_Day" tabindex="4" class="uportal-input-text">

                                <option></option>

								<xsl:for-each select="WORKFLOW_INSTANCE_dtDateCreated_Day">

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

                            

                            <select name="WORKFLOW_INSTANCE_dtDateCreated_Month" tabindex="5" class="uportal-input-text">

                                <option></option>

								<xsl:for-each select="WORKFLOW_INSTANCE_dtDateCreated_Month">

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

                            

                            <input type="text" name="WORKFLOW_INSTANCE_dtDateCreated_Year" value="{$WORKFLOW_INSTANCE_dtDateCreated_Year}" size="5" tabindex="6" class="uportal-input-text" />

                        </td>

                    </tr>

                    

                    <tr>

                        <td width="40%" class="uportal-label">

                            <xsl:value-of select="WORKFLOW_INSTANCE_dtDateCompletedDisplay" />:

                        </td>

						<td width="60%">



							<select name="WORKFLOW_INSTANCE_strDateCompletedOperator" tabindex="4" class="uportal-input-text">

                                <option></option>

                                <option>=</option>

								<option>&lt;&gt;</option>

								<option>&lt;</option>

								<option>&lt;=</option>

								<option>&gt;</option>

								<option>&gt;=</option>

							</select>

                            

                            <select name="WORKFLOW_INSTANCE_dtDateCompleted_Day" tabindex="7" class="uportal-input-text">

                                <option></option>

								<xsl:for-each select="WORKFLOW_INSTANCE_dtDateCompleted_Day">

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

                            

                            <select name="WORKFLOW_INSTANCE_dtDateCompleted_Month" tabindex="8" class="uportal-input-text">

                                <option></option>

								<xsl:for-each select="WORKFLOW_INSTANCE_dtDateCompleted_Month">

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

                            

                            <input type="text" name="WORKFLOW_INSTANCE_dtDateCompleted_Year" value="{$WORKFLOW_INSTANCE_dtDateCompleted_Year}" size="5" tabindex="9" class="uportal-input-text" />

                        </td>

                    </tr>

                </table>

                

                <table width="90%">

                    <tr><td><hr /></td></tr>

                </table>

                

                <table width="100%">

                    <tr>

                        <td>

                            <input type="submit" name="search" tabindex="10" value="{$submitBtnLabel}" class="uportal-button" />

							<input type="button" name="clear" value="{$clearBtnLabel}" tabindex="11" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=workflow_search_workflow_instance')" />

                        </td>

                    </tr>

                </table>

            </td>

            

            <td width="75%">

                

                <table width="100%">

					<tr>

						<td width="10%" class="uportal-channel-table-header">

							<xsl:value-of select="WORKFLOW_INSTANCE_intWorkflowInstanceKeyDisplay" />

                        </td>



                        <td width="20%" class="uportal-channel-table-header">

                            <xsl:value-of select="WORKFLOW_INSTANCE_strNameDisplay" />

                        </td>

                        

                        <td width="10%" class="uportal-channel-table-header">

							<xsl:value-of select="WORKFLOW_INSTANCE_strStatusDisplay" />

						</td>



						<td width="10%" class="uportal-channel-table-header">

							<xsl:value-of select="WORKFLOW_INSTANCE_dtDateCreatedDisplay" />

						</td>



						<td width="15%" class="uportal-channel-table-header">

							<xsl:value-of select="WORKFLOW_INSTANCE_dtDateCompletedDisplay" />

						</td>

						

						<td width="10%" class="uportal-channel-table-header">

							Suspend

						</td>



						<td width="10%" class="uportal-channel-table-header">

							Resume

						</td>



						<td width="10%" class="uportal-channel-table-header">

							Delete

						</td>

                    </tr>

                </table>

                <hr/>

                    

                <table width="100%">

                    <xsl:for-each select="workflow_search_workflow_instance_result">

                    <xsl:variable name="WORKFLOW_INSTANCE_intWorkflowInstanceKey"><xsl:value-of select="WORKFLOW_INSTANCE_intWorkflowInstanceKey" /></xsl:variable>

					<xsl:variable name="WORKFLOW_INSTANCE_strName"><xsl:value-of select="WORKFLOW_INSTANCE_strName" /></xsl:variable>

					<tr>



						<td width="10%" class="uportal-label"> 

                            <a href="{$baseActionURL}?current=workflow_view_workflow_instance&amp;WORKFLOW_INSTANCE_intWorkflowInstanceKey={$WORKFLOW_INSTANCE_intWorkflowInstanceKey}">

                                <xsl:value-of select="WORKFLOW_INSTANCE_intWorkflowInstanceKey" />

                            </a>

                        </td>

						

						<td width="20%" class="uportal-label"> 

                            <a href="{$baseActionURL}?current=workflow_view_workflow_instance&amp;WORKFLOW_INSTANCE_intWorkflowInstanceKey={$WORKFLOW_INSTANCE_intWorkflowInstanceKey}">

                                <xsl:value-of select="WORKFLOW_INSTANCE_strName" />

                            </a>

                        </td>

						

                        <td width="10%" class="uportal-label">

                            <a href="{$baseActionURL}?current=workflow_view_workflow_instance&amp;WORKFLOW_INSTANCE_intWorkflowInstanceKey={$WORKFLOW_INSTANCE_intWorkflowInstanceKey}">

                                <xsl:value-of select="WORKFLOW_INSTANCE_strStatus" />

                            </a>

						</td>



						<td width="10%" class="uportal-label">

                            <a href="{$baseActionURL}?current=workflow_view_workflow_instance&amp;WORKFLOW_INSTANCE_intWorkflowInstanceKey={$WORKFLOW_INSTANCE_intWorkflowInstanceKey}">

                                <xsl:value-of select="WORKFLOW_INSTANCE_dtDateCreated" />

                            </a>

						</td>



						<td width="15%" class="uportal-label">

                            <a href="{$baseActionURL}?current=workflow_view_workflow_instance&amp;WORKFLOW_INSTANCE_intWorkflowInstanceKey={$WORKFLOW_INSTANCE_intWorkflowInstanceKey}">

                                <xsl:value-of select="WORKFLOW_INSTANCE_dtDateCompleted" />

                            </a>

						</td>



						<td width="10%" class="uportal-label">

							<a href="{$baseActionURL}?current=workflow_search_workflow_instance&amp;action=suspend&amp;WORKFLOW_INSTANCE_intWorkflowInstanceKey={$WORKFLOW_INSTANCE_intWorkflowInstanceKey}">Suspend</a>

						</td>



						<td width="10%" class="uportal-label">

							<a href="{$baseActionURL}?current=workflow_search_workflow_instance&amp;action=resume&amp;WORKFLOW_INSTANCE_intWorkflowInstanceKey={$WORKFLOW_INSTANCE_intWorkflowInstanceKey}">Resume</a>

						</td>



						<td width="10%" class="uportal-label">

							<a href="javascript:confirmDelete('{$baseActionURL}?current=workflow_search_workflow_instance&amp;action=delete&amp;WORKFLOW_INSTANCE_intWorkflowInstanceKey={$WORKFLOW_INSTANCE_intWorkflowInstanceKey}')">Delete</a>

						</td>



                    </tr>

                    </xsl:for-each>

                </table>

            </td>

        </tr>

    </table>

	</form>

    </xsl:template>



</xsl:stylesheet>	


