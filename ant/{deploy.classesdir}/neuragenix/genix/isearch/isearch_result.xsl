<?xml version="1.0" encoding="utf-8"?>
<!--
    Document   : isearch_result.xml
    Copyright @ 2003 Neuragenix, Inc .  All rights reserved.
    Created on : 01/09/2004
    Author     : Anita Balraj
          
-->
<!-- page for display search result data -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!--xsl:include href="./isearch_menu.xsl"/-->
	<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
	<xsl:param name="formParams">current=isearch_result</xsl:param>
	<xsl:output method="html" indent="no"/>
	<xsl:param name="downloadURL">downloadURL_false</xsl:param>
	<xsl:param name="nodeId">nodeId_false</xsl:param>
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
        
        
	<!-- Get the parameters from the channel class -->
	<xsl:template match="isearch">
		<xsl:param name="intCurrentPage">
			<xsl:value-of select="intCurrentPage"/>
		</xsl:param>
		<xsl:param name="intRecordPerPage">
			<xsl:value-of select="intRecordPerPage"/>
		</xsl:param>
		<xsl:param name="blHasData">
			<xsl:value-of select="blHasData"/>
		</xsl:param>
		<xsl:param name="strFileName">
			<xsl:value-of select="strFileName"/>
		</xsl:param>
		<xsl:param name="strDelimiter">
			<xsl:value-of select="strDelimiter"/>
		</xsl:param>
		<xsl:param name="ExplicitFieldsVal">
			<xsl:value-of select="ExplicitFieldsVal"/>
		</xsl:param>
		<xsl:variable name="pickerMode">
			<xsl:value-of select="pickerMode"/>
		</xsl:variable>
                
                <script language="javascript">

                function mySubmit(suffix){        
                    document.frmISearchResult.action+=suffix;        
                    document.frmISearchResult.submit();       
                }

                </script>
                

		<table cellpadding="0" cellspacing="0" width="100%" height="100%" border="0">
			<tr>
				<td>
					<table width="100%">
						<tr>
							<td class="uportal-channel-subtitle">Search Result</td>
							<td align="right">
								<form action="{$baseActionURL}?uP_root=root&amp;{$formParams}" method="post" enctype="multipart/form-data">
									<!-- IExplorer Hack: since no submit attributes are passed, set "ISEARCH_back" parameter -->
									<input type="hidden" name="ISEARCH_back" value="not_null"/>
									<!-- Back and Next Buttons -->
									<input class="submit_image_button" src="{$buttonImagePath}/previous_enabled.gif" type="image" name="ISEARCH_back" value="&lt; Back" alt="Previous"/>
									<img src="{$buttonImagePath}/next_disabled.gif" border="0" alt="Next"/>
								</form>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<hr/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<form name="frmISearchResult" action="{$baseActionURL}?uP_root=root&amp;{$formParams}" method="post" enctype="multipart/form-data">
				<tr>
					<td>
						<a name="frmSearch_Result_anchor"/> 
						<xsl:if test="blHasData = 'true'">
							<table width="100%">
								<tr>
									<xsl:variable name="intSchemaListCount">
										<xsl:value-of select="intSchemaListCount"/>
									</xsl:variable>
									<!--Display all columns not associated with smartforms in one colour-->
									<xsl:if test="intSchemaListCount != 0">
										<td colspan="{$intSchemaListCount * 2}" class="uportal-channel-table-header"/>
									</xsl:if>
									<!-- Display each of the smartform names associated with the columns in a different colour -->
									<!-- Alternate between two colours to distinguish between adjacent smartforms -->
									<xsl:for-each select="smartformlist">
										<xsl:variable name="intDEcnt">
											<xsl:value-of select="count"/>
										</xsl:variable>
										<xsl:choose>
											<xsl:when test="num mod 2 != 0">
												<td colspan="{$intDEcnt * 2}" class="uportal-channel-table-header" bgcolor="#FFFF99">
													<xsl:value-of select="name"/>
												</td>
											</xsl:when>
											<xsl:otherwise>
												<td colspan="{$intDEcnt * 2}" class="uportal-channel-table-header" bgcolor="#CCFF99">
													<xsl:value-of select="name"/>
												</td>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</tr>
								<tr>
									<xsl:if test="$pickerMode='true'">
										<td/>
									</xsl:if>
									<xsl:for-each select="headers">
										<td class="uportal-channel-table-header">
											<xsl:value-of select="name"/>
										</td>
										<td width="10"/>
									</xsl:for-each>
								</tr>
								<xsl:for-each select="row">
									<xsl:variable name="picked" select="@flagged"/>
									<xsl:variable name="itemID" select="@id"/>
									<tr>
										<xsl:if test="$pickerMode='true'">
											<td>
												<input type="checkbox">
													<xsl:attribute name="name">flags_<xsl:value-of select="$itemID"/></xsl:attribute>
													<xsl:if test="$picked='true'">
														<xsl:attribute name="checked">checked</xsl:attribute>
													</xsl:if>
												</input>
											</td>
										</xsl:if>
										<xsl:choose>
											<xsl:when test="position() mod 2 != 0">
												<xsl:for-each select="record">
													<td class="uportal-input-text">
														<xsl:value-of select="value"/>
													</td>
													<td width="10"/>
												</xsl:for-each>
											</xsl:when>
											<xsl:otherwise>
												<xsl:for-each select="record">
													<td class="uportal-text">
														<xsl:value-of select="value"/>
													</td>
													<td width="10"/>
												</xsl:for-each>
											</xsl:otherwise>
										</xsl:choose>
									</tr>
								</xsl:for-each>
							</table>
							<hr/>
							<table width="500px">
								<tr>
									<td width="40px" class="uportal-label">
								page:
							</td>
									<td width="15px">
										<input type="submit" name="ISEARCH_previous" value="&lt;" tabindex="1" class="uportal-button"/>
									</td>
									<td width="50px">
										<input type="text" name="intCurrentPage" size="4" tabindex="2" value="{$intCurrentPage}" align="right" class="uportal-input-text"/>
									</td>
									<td width="50px" class="uportal-label">
								of <xsl:value-of select="intMaxPage"/>
									</td>
									<td width="15px">
										<input type="submit" name="ISEARCH_next" value="&gt;" tabindex="3" class="uportal-button"/>
									</td>
									<td width="30px">
										<input type="submit" name="ISEARCH_go" value="go" tabindex="4" class="uportal-button"/>
									</td>
									<td width="20px"/>
									<td width="50px" class="uportal-label">
								Display
							</td>
									<td width="50px">
										<input type="text" name="intRecordPerPage" size="4" tabindex="5" value="{$intRecordPerPage}" align="right" class="uportal-input-text"/>
									</td>
									<td width="120px" class="uportal-label">
								records at a time
							</td>
									<td width="40px">
										<input type="submit" name="ISEARCH_set" value="set" tabindex="6" class="uportal-button"/>
									</td>
								</tr>
							</table>
                                                        <table width="100%">
                                                            <tr>
                                                                <td width="100%" class="uportal-label">
                                                                Total Number of Results: <xsl:value-of select="intTotalRecords"/>
                                                                </td>
                                                            </tr>
                                                        </table>
							<hr/>
							<table width="100%">
								<tr>
									<td width="10%">&#160;</td>
									<td width="80%"/>
									<xsl:for-each select="finishButton">
										<xsl:variable name="state" select="."/>
										<xsl:variable name="label" select="@label"/>
										<xsl:if test="$state='true'">
											<td width="10%">
												<input type="submit" name="ISEARCH_finish" value="{$label}" class="uportal-button"/>
											</td>
										</xsl:if>
									</xsl:for-each>
								</tr>
							</table>
							<hr/>
							<table width="100%" border="1">
								<tr>
									<td width="100%" class="uportal-label">
								Export search results to file
							</td>
								</tr>
							</table>
							<table width="100%" border="1">
								<tr>
									<td width="100%" class="uportal-label">
								1. Specify the export delimiter (Enter "tab" for a tab delimiter)&#160;&#160;               
								<input type="text" name="delimiter" size="4" value="{$strDelimiter}" align="right" class="uportal-input-text"/>
								&#160;&#160;&#160;&#160; Explicit Field Names &#160;&#160;
								<input type="checkbox">
											<xsl:attribute name="name">ExplicitFields</xsl:attribute>
											<xsl:if test="$ExplicitFieldsVal = 'true'">
												<xsl:attribute name="checked">true</xsl:attribute>
											</xsl:if>
										</input>
									</td>
								</tr>
								<tr>
									<td width="100%" class="uportal-label">
								2. Click on this button to perform export&#160;&#160;               
							   <input type="button" name="ISEARCH_performExport" value="Perform Export" class="uportal-button" onclick="javascript:mySubmit('&amp;ISEARCH_performExport=true#frmSearch_Export_Anchor')"/>
									</td>
								</tr>
                                                                
								<tr>
									<td width="100%" class="uportal-label">
                                                                        <a name="frmSearch_Export_Anchor"/>
								3. Click on this link to download the export file &#160;&#160;               
								<a href="{$downloadURL}?uP_root={$nodeId}&amp;file_name={$strFileName}&amp;property_name=neuragenix.bio.search.ExportFileLocation&amp;activity_required=do_isearch" target="_blank">Download export file </a>
									</td>
								</tr>
							</table>
						</xsl:if> 
						<xsl:if test="blHasData = 'false'">
							<table width="100%">
								<tr>
									<td class="neuragenix-form-required-text">No matching record found.</td>
								</tr>
							</table>
							<hr/>
							<table width="100%">
								<tr>
									<td width="10%">&#160;</td>
									<td width="90%"/>
								</tr>
							</table>
						</xsl:if>
					</td>
				</tr>
			</form>
			<tr height="100%">
				<td>
					<table width="100%" height="100%" border="0">
						<tr height="100%">
							<td>&#160;</td>
						</tr>
						<tr>
							<td><hr/></td>
						</tr>
						<tr>
							<td align="left">
								<form action="{$baseActionURL}?uP_root=root&amp;{$formParams}#frmSearch_Result_anchor" method="post" enctype="multipart/form-data">
									<!-- IExplorer Hack: since no submit attributes are passed, set "ISEARCH_back" parameter -->
									<input type="hidden" name="ISEARCH_back" value="not_null"/>
									<!-- Back and Next Buttons -->
									<input class="submit_image_button" src="{$buttonImagePath}/previous_enabled.gif" type="image" name="ISEARCH_back" value="&lt; Back" alt="Previous"/>
									<img src="{$buttonImagePath}/next_disabled.gif" border="0" alt="Next"/>
								</form>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
