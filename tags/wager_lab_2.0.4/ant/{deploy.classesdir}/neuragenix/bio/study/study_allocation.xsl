<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./study_menu.xsl"/>
	<xsl:include href="../../common/common_btn_name.xsl"/>
	<xsl:output method="html" indent="no"/>
	<xsl:param name="biospecimenChannelURL">biospecimenChannelURL_false</xsl:param>
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
	<xsl:template match="study">
		<!-- Get the parameters from the channel class -->
		<!--   <xsl:param name="intStudyID"><xsl:value-of select="intStudyID" /></xsl:param> -->
		<xsl:param name="strStudyName">
			<xsl:value-of select="strStudyName"/>
		</xsl:param>
		<xsl:param name="strStudyOwner">
			<xsl:value-of select="strStudyOwner"/>
		</xsl:param>
		<xsl:param name="strStudyDesc">
			<xsl:value-of select="strStudyDesc"/>
		</xsl:param>
		<xsl:param name="dtStudyStart">
			<xsl:value-of select="dtStudyStart"/>
		</xsl:param>
		<xsl:param name="dtStudyEnd">
			<xsl:value-of select="dtStudyEnd"/>
		</xsl:param>
		<xsl:param name="intTargetPatientNo">
			<xsl:value-of select="intTargetPatientNo"/>
		</xsl:param>
		<xsl:param name="intActualPatientNo">
			<xsl:value-of select="intActualPatientNo"/>
		</xsl:param>
		<xsl:param name="STUDY_Timestamp">
			<xsl:value-of select="STUDY_Timestamp"/>
		</xsl:param>
		<xsl:variable name="intStudyID">
			<xsl:value-of select="intStudyID"/>
		</xsl:variable>
		<table cellpadding="0" cellspacing="0" width="100%" height="100%" border="0">
			<tr>
				<td>
					<table width="100%">
						<form action="{$baseActionURL}?current=study_allocation" method="post">
							<tr>
								<td class="uportal-channel-subtitle">View Biospecimen Allocations for <xsl:value-of select="$strStudyName"/></td>
								<td align="right">
									<input type="hidden" name="intStudyID" value="{$intStudyID}"/>
									<!-- IExplorer Hack: since no submit attributes are passed, set "save" parameter -->
									<input type="hidden" name="back" value="not_null"/>
									<!-- Back and Next Buttons -->
									<input class="submit_image_button" src="{$buttonImagePath}/previous_enabled.gif" type="image" name="back" value="{$backBtnLabel}" alt="Previous"/>
									<img src="{$buttonImagePath}/next_disabled.gif" border="0" alt="Next"/>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<hr/>
								</td>
							</tr>
						</form>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:variable name="locking" select="lockError"/>
					<xsl:if test="$locking='true'">
						<span class="neuragenix-form-required-text">
							<xsl:value-of select="strError"/>
						</span>
					</xsl:if>
                                        
                                        <xsl:variable name="strErrors" select="strError"/>
                                         <xsl:if test="string($strErrors)">
						<span class="neuragenix-form-required-text">
							<xsl:value-of select="strError"/>
						</span>
					</xsl:if>
					<table width="75%" border="0" cellpadding="2" cellspacing="2">
						<tbody>
							<tr>
								<td width="15%" class="uportal-label">Biospecimen Type<br/>
								</td>
								<td width="15%" class="uportal-label">Biospecimen Sub-Type<br/>
								</td>
								<td width="10%" class="uportal-label">Amount Requested<br/>
								</td>
								<td width="1%">
									<br/>
								</td>
								<td width="15%" class="uportal-label">Amount Allocated<br/>
								</td>
								<td width="15%" class="uportal-label">Amount Delivered<br/>
								</td>
								<td width="1%">
									<br/>
								</td>
								<td width="5%">
									<br/>
								</td>
								<td width="5%">
									<br/>
								</td>
							</tr>
							<xsl:for-each select="SavedAllocationSearch">
								<xsl:variable name="AllocationKey" select="STUDY_ALLOCATION_intAllocationKey"/>
								<form name="updateSearch_{$AllocationKey}" action="{$baseActionURL}?current=study_allocation&amp;action=update_allocation_search">
									<input type="hidden" name="current" value="study_allocation"/>
									<input type="hidden" name="action" value="update_allocation_search"/>
									<input type="hidden" name="STUDY_ALLOCATION_intAllocationKey" value="{$AllocationKey}"/>
									<xsl:variable name="editMode" select="editMode"/>
									<tr>
										<xsl:attribute name="class"><xsl:choose><xsl:when test="position() mod 2 != 0">
									 uportal-input-text
								  </xsl:when><xsl:otherwise>
									 uportal-text
								  </xsl:otherwise></xsl:choose></xsl:attribute>
										<td width="15%">
											<xsl:choose>
												<xsl:when test="editMode='true'">
													<script language="javascript">
			
												   function dropDownUpdate_<xsl:value-of select="$AllocationKey"/>()
												   {
													  document.updateSearch_<xsl:value-of select="$AllocationKey"/>.action.value = 'refresh_allocation_search';
													  document.updateSearch_<xsl:value-of select="$AllocationKey"/>.submit();
			
			
												   }
			
			
												</script>
													<select name="STUDY_ALLOCATION_strBiospecimenType" tabindex="14" onChange="javascript:dropDownUpdate_{$AllocationKey}();">
														<xsl:for-each select="STUDY_ALLOCATION_LOVFields/STUDY_ALLOCATION_strBiospecimenType">
															<option>
																<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
																<xsl:if test="@selected=1">
																	<xsl:attribute name="selected">true</xsl:attribute>
																</xsl:if>
																<xsl:value-of select="."/>
															</option>
														</xsl:for-each>
													</select>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="STUDY_ALLOCATION_strBiospecimenType"/>
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td width="15%">
											<xsl:choose>
												<xsl:when test="editMode='true'">
													<xsl:variable name="biospecimenSubType">
														<xsl:value-of select="STUDY_ALLOCATION_strBiospecimenSubType"/>
													</xsl:variable>
													<!-- <input type="text" name="STUDY_ALLOCATION_strBiospecimenSubType" value="{$biospecimenSubType}" class="uportal-input-text" /> -->
													<select name="STUDY_ALLOCATION_strBiospecimenSubType" tabindex="14">
														<xsl:for-each select="STUDY_ALLOCATION_LOVFields/STUDY_ALLOCATION_strBiospecimenSubType">
															<option>
																<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
																<xsl:if test="@selected=1">
																	<xsl:attribute name="selected">true</xsl:attribute>
																</xsl:if>
																<xsl:value-of select="."/>
															</option>
														</xsl:for-each>
													</select>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="STUDY_ALLOCATION_strBiospecimenSubType"/>
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td width="10%">
											<table width="100%" cellpadding="2" cellspacing="2">
												<tbody>
													<tr>
														<td align="right" width="50%">
															<xsl:choose>
																<xsl:when test="editMode='true'">
																	<xsl:variable name="requestedQuantity">
																		<xsl:value-of select="STUDY_ALLOCATION_intRequestedQuantity"/>
																	</xsl:variable>
																	<input type="text" value="{$requestedQuantity}" name="STUDY_ALLOCATION_intRequestedQuantity"/>
																</xsl:when>
																<xsl:otherwise>
																	<span class="uportal-text">
																		<xsl:value-of select="STUDY_ALLOCATION_intRequestedQuantity"/>
																	</span>
																</xsl:otherwise>
															</xsl:choose>
															<br/>
														</td>
														<td align="left" width="50%">
															<xsl:choose>
																<xsl:when test="editMode='true'">
																	<select name="STUDY_ALLOCATION_strRequestedMeasure" tabindex="14">
																		<xsl:for-each select="STUDY_ALLOCATION_LOVFields/STUDY_ALLOCATION_strRequestedMeasure">
																			<option>
																				<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
																				<xsl:if test="@selected=1">
																					<xsl:attribute name="selected">true</xsl:attribute>
																				</xsl:if>
																				<xsl:value-of select="."/>
																			</option>
																		</xsl:for-each>
																	</select>
																</xsl:when>
																<xsl:otherwise>
																	<span class="uportal-text">
																		<xsl:value-of select="STUDY_ALLOCATION_strRequestedMeasure"/>(s)</span>
																</xsl:otherwise>
															</xsl:choose>
															<br/>
														</td>
													</tr>
												</tbody>
											</table>
										</td>
										<td width="1%">
											<br/>
										</td>
										<td width="15%">
											<!-- amount allocated -->
											<xsl:if test="not(editMode='true')">
												<table width="100%" cellpadding="2" cellspacing="2">
													<tbody>
														<tr>
															<td align="right" width="50%">
																<xsl:variable name="biospecimenType" select="STUDY_ALLOCATION_strBiospecimenType"/>
																<xsl:variable name="biospecimenSubType" select="STUDY_ALLOCATION_strBiospecimenSubType"/>
																<xsl:choose>
																
																<xsl:when test="AllocatedQuantity/SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity = 0">																
																	<xsl:value-of select="AllocatedQuantity/SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity"/>
																	
																</xsl:when>
																<xsl:otherwise>
																	<a href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=3&amp;BIOSPECIMEN_intStudyID={$intStudyID}&amp;BIOSPECIMEN_strSampleType={$biospecimenType}&amp;BIOSPECIMEN_strSampleSubType={$biospecimenSubType}&amp;BIOSPECIMEN_TRANSACTIONS_searchType=1&amp;current=biospecimen_search&amp;module=biospecimen_search&amp;action=studyalloc">
																	<xsl:value-of select="AllocatedQuantity/SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity"/>
																	</a>
																</xsl:otherwise>	
																</xsl:choose>
															</td>
															<td align="left" width="50%">
																<xsl:variable name="biospecimenType" select="STUDY_ALLOCATION_strBiospecimenType"/>
																<xsl:variable name="biospecimenSubType" select="STUDY_ALLOCATION_strBiospecimenSubType"/>
																<xsl:choose>
																
																<xsl:when test="AllocatedQuantity/SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity = 0">																
																	<xsl:value-of select="STUDY_ALLOCATION_strRequestedMeasure"/>(s)
																</xsl:when>
																<xsl:otherwise>
																	<a href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=3&amp;BIOSPECIMEN_intStudyID={$intStudyID}&amp;BIOSPECIMEN_strSampleType={$biospecimenType}&amp;BIOSPECIMEN_strSampleSubType={$biospecimenSubType}&amp;BIOSPECIMEN_TRANSACTIONS_searchType=1&amp;current=biospecimen_search&amp;module=biospecimen_search&amp;action=studyalloc">
																	<xsl:value-of select="STUDY_ALLOCATION_strRequestedMeasure"/>(s)
																	</a>
																</xsl:otherwise>	
																</xsl:choose>
																
																
															</td>
														</tr>
													</tbody>
												</table>
											</xsl:if>
										</td>
										<td width="15%">
											<!-- amount delivered -->
											<xsl:if test="not(editMode='true')">
												<table width="100%" cellpadding="2" cellspacing="2">
													<tbody>
														<tr>
															<td align="right" width="50%">
																<xsl:variable name="biospecimenType" select="STUDY_ALLOCATION_strBiospecimenType"/>
																<xsl:variable name="biospecimenSubType" select="STUDY_ALLOCATION_strBiospecimenSubType"/>
																<xsl:choose>
																
																<xsl:when test="DeliveredQuantity/SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity = 0">																
																	<xsl:value-of select="DeliveredQuantity/SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity"/>
																	
																</xsl:when>
																<xsl:otherwise>
																	<a href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=3&amp;BIOSPECIMEN_intStudyID={$intStudyID}&amp;BIOSPECIMEN_strSampleType={$biospecimenType}&amp;BIOSPECIMEN_strSampleSubType={$biospecimenSubType}&amp;BIOSPECIMEN_TRANSACTIONS_searchType=2&amp;current=biospecimen_search&amp;module=biospecimen_search&amp;action=studyalloc">
																	<xsl:value-of select="DeliveredQuantity/SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity"/>
																	</a>
																</xsl:otherwise>	
																</xsl:choose>
																
																
															</td>
															<td align="left" width="50%">
																<xsl:variable name="biospecimenType" select="STUDY_ALLOCATION_strBiospecimenType"/>
																<xsl:variable name="biospecimenSubType" select="STUDY_ALLOCATION_strBiospecimenSubType"/>
																<xsl:choose>
																
																<xsl:when test="DeliveredQuantity/SUM_BIOSPECIMEN_TRANSACTIONS_flQuantity = 0">																
																	<xsl:value-of select="STUDY_ALLOCATION_strRequestedMeasure"/>(s)
																</xsl:when>
																<xsl:otherwise>
																	<a href="{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=3&amp;BIOSPECIMEN_intStudyID={$intStudyID}&amp;BIOSPECIMEN_strSampleType={$biospecimenType}&amp;BIOSPECIMEN_strSampleSubType={$biospecimenSubType}&amp;BIOSPECIMEN_TRANSACTIONS_searchType=2&amp;current=biospecimen_search&amp;module=biospecimen_search&amp;action=studyalloc">
																	<xsl:value-of select="STUDY_ALLOCATION_strRequestedMeasure"/>(s)
																   </a>
																</xsl:otherwise>	
																</xsl:choose>
																
																
															</td>
														</tr>
													</tbody>
												</table>
											</xsl:if>
										</td>
										<td width="1%">
											<br/>
										</td>
										<td width="5%">
											<xsl:choose>
												<xsl:when test="editMode='true'">
													<a href="javascript:document.updateSearch_{$AllocationKey}.submit()">update</a>
													<br/>
												</xsl:when>
												<xsl:otherwise>
													<a href="{$baseActionURL}?current=study_allocation&amp;action=edit_allocation_search&amp;toEdit={$AllocationKey}">edit</a>
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td width="5%">
											<xsl:if test="not(editMode='true')">
												<a href="{$baseActionURL}?current=study_allocation&amp;action=delete_allocation_search&amp;toDelete={$AllocationKey}">delete</a>
												<br/>
											</xsl:if>
										</td>
									</tr>
								</form>
							</xsl:for-each>
						</tbody>
					</table>
					<br/>
					<hr/>
					<br/>
					<xsl:variable name="hideAdd" select="hideAdd"/>
					<xsl:if test="not(hideAdd='true')">
						<span class="uportal-channel-subtitle">Add new search<hr/>
						</span>
						<form name="addNewSearch" action="{$baseActionURL}?current=study_allocation&amp;action=save_allocation_search">
							<script language="javascript">
							   function refreshAddNew()
							   {
								  document.addNewSearch.action.value = "refresh_save_allocation_search";
								  document.addNewSearch.submit();
							   }
							</script>
							<input type="hidden" name="current" value="study_allocation"/>
							<input type="hidden" name="action" value="save_allocation_search"/>
							<table width="75%" border="0" cellpadding="2" cellspacing="2">
								<tbody>
									<tr>
										<td class="uportal-label">Biospecimen Type</td>
										<td class="uportal-label">Biospecimen Sub-Type</td>
										<td class="uportal-label">Amount Requested</td>
										<td/>
										<td/>
									</tr>
									<tr class="uportal-input-text">
										<td>
											<select name="STUDY_ALLOCATION_strBiospecimenType" tabindex="21" class="uportal-input-text" onChange="javascript:refreshAddNew()">
												<xsl:for-each select="STUDY_ALLOCATION_strBiospecimenType">
													<option>
														<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
														<xsl:if test="@selected=1">
															<xsl:attribute name="selected">true</xsl:attribute>
														</xsl:if>
														<xsl:value-of select="."/>
													</option>
												</xsl:for-each>
												<xsl:variable name="firstadd" select="FIRSTADD"/>
												<xsl:if test="$firstadd='true'">
													<option selected="true"/>
												</xsl:if>
											</select>
										</td>
										<td>
											<select name="STUDY_ALLOCATION_strBiospecimenSubType" tabindex="22" class="uportal-input-text">
												<xsl:for-each select="STUDY_ALLOCATION_strBiospecimenSubType">
													<option>
														<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
														<xsl:if test="@selected=1">
															<xsl:attribute name="selected">true</xsl:attribute>
														</xsl:if>
														<xsl:value-of select="."/>
													</option>
												</xsl:for-each>
											</select>
										</td>
										<td>
											<table width="100%" border="0" cellpadding="2" cellspacing="2">
												<tbody>
													<tr>
														<td align="right" width="50%">
															<input type="text" name="STUDY_ALLOCATION_intRequestedQuantity" tabindex="23" value="{STUDY_ALLOCATION_intRequestedQuantity}" size="40" class="uportal-input-text"/>
														</td>
														<td align="left" width="50%">
															<select name="STUDY_ALLOCATION_strRequestedMeasure" tabindex="24" class="uportal-input-text">
																<xsl:for-each select="STUDY_ALLOCATION_strRequestedMeasure">
																	<option>
																		<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
																		<xsl:if test="@selected=1">
																			<xsl:attribute name="selected">true</xsl:attribute>
																		</xsl:if>
																		<xsl:value-of select="."/>
																	</option>
																</xsl:for-each>
															</select>
														</td>
													</tr>
												</tbody>
											</table>
										</td>
										<td>
											<br/>
										</td>
										<td>
											<A href="javascript:document.addNewSearch.submit()" tabindex="25" class="uportal-input-text" onblur="javascript:document.addNewSearch.STUDY_ALLOCATION_strBiospecimenType.focus()">Save</A>
										</td>
									</tr>
								</tbody>
							</table>
						</form>
					</xsl:if>
				</td>
			</tr>
			<tr height="100%">
				<td>
					<table width="100%" height="100%" border="0">
						<form action="{$baseActionURL}?current=study_allocation" method="post">
							<tr height="100%">
								<td>&#160;</td>
							</tr>
							<tr>
								<td><hr/></td>
							</tr>
							<tr>
								<td align="right">
									<input type="hidden" name="intStudyID" value="{$intStudyID}"/>
									<!-- IExplorer Hack: since no submit attributes are passed, set "save" parameter -->
									<input type="hidden" name="back" value="not_null"/>
									<!-- Back and Next Buttons -->
									<input class="submit_image_button" src="{$buttonImagePath}/previous_enabled.gif" type="image" name="back" value="{$backBtnLabel}" alt="Previous"/>
									<img src="{$buttonImagePath}/next_disabled.gif" border="0" alt="Next"/>
								</td>
							</tr>
						</form>
					</table>
				</td>
			</tr>
		</table>
		<script language="JavaScript">
                
			document.addNewSearch.STUDY_ALLOCATION_strBiospecimenType.focus()
		</script>
	</xsl:template>
</xsl:stylesheet>
