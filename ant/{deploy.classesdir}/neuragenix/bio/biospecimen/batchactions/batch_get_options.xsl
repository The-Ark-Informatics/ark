<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:template match="batch_creation">
        <!--main table-->
        <table width="100%">
            <tr>
                <td width="20%"></td>
                <td widht="80%">
                    <table width="100%">
                        <!-- this row for the table reader-->
                        <tr>
                            <td class="uportal-channel-subtitle" colspan="6">Batch
                                Creation Step 1: Get Options</td>
                            <td align="right" colspan="6">
                                <form name="back_form" action="{$baseActionURL}" method="POST">
                                    <!-- back to the parent biospecimen -->
                                    
                                            <input type="hidden" name="module"
                                                value="core"/>
                                            <input type="hidden" name="action"
                                                value="view_biospecimen"/>
                                            <input type="hidden"
                                                name="BIOSPECIMEN_intBiospecimenID">
                                                <xsl:attribute name="value">
                                                    <xsl:value-of
                                                        select="parentKey"/>
                                                </xsl:attribute>
                                            </input>
                                            
                                            
                                       
                                </form>
                            
                            <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
                                alt="Previous" onclick="javascript:document.back_form.submit();"/>
                            <img border="0" src="media/neuragenix/buttons/next_enabled.gif"
                                alt="Next" onclick="javascript:document.next_form.submit();" />
                            </td>
                        </tr>
                        <tr>
                            <td colspan="12"><hr/></td>
                        </tr>
                    
                        <tr>
                            <td width="3%"/>
                            <td width="10%" class="neuragenix-form-row-input-label">
                                <xsl:value-of select="BIOSPECIMEN_strSampleTypeDisplay"/>
                            </td>
                            <td width="10%" class="neuragenix-form-row-input-label">
                                <xsl:value-of select="BIOSPECIMEN_strSampleSubTypeDisplay"/>
                            </td>
                            <td width="10%" class="neuragenix-form-row-input-label">
                                <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strTreatmentDisplay"
                                />
                            </td>
                            <td width="10%" class="neuragenix-form-row-input-label"> Number </td>
                            <td width="10%" class="neuragenix-form-row-input-label">
                                <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_flQuantityDisplay"/>
                            </td>
                            <td width="10%" class="neuragenix-form-row-input-label">
                                <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strUnitDisplay"/>
                            </td>
                            <!-- <td width="10%" class="neuragenix-form-row-input-label"> New tray </td> -->
                        </tr>
                        <!-- existing generation information -->
                        <xsl:for-each select="GenerationDetails">
                            <tr>
                                <!--determine the color of the line !-->
                                <xsl:variable name="classIDNumber">
                                    <xsl:value-of select="GD_internalID"/>
                                </xsl:variable>
                                <xsl:choose>
                                    <xsl:when test="$classIDNumber mod 2 = 0">
                                        <xsl:attribute name="class"
                                        >uportal-input-text</xsl:attribute>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:attribute name="class">uportal-text</xsl:attribute>
                                    </xsl:otherwise>
                                </xsl:choose>
                                <td class="uportal-input-test">
                                    <xsl:value-of select="$classIDNumber+1"/>
                                </td>
                                <td>
                                    <xsl:value-of select="BIOSPECIMEN_strSampleType_Selected"/>
                                </td>
                                <td>
                                    <xsl:value-of select="BIOSPECIMEN_strSampleSubType_Selected"/>
                                </td>
                                <td>
                                    <xsl:value-of
                                        select="BIOSPECIMEN_TRANSACTIONS_strTreatment_Selected"/>
                                </td>
                                <td>
                                    <xsl:value-of select="GD_AllocationAmount"/>
                                </td>
                                <td>
                                    <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_flQuantity"/>
                                </td>
                                <td>
                                    <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strUnit_Selected"/>
                                </td>
                                <!--
                                <td>
                                    <xsl:variable name="allocationMode">
                                        <xsl:value-of select="GD_AllocationMode"/>
                                    </xsl:variable>
                                    <xsl:choose>
                                        <xsl:when test="$allocationMode=2"> Yes </xsl:when>
                                        <xsl:otherwise> No </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                                !-->
                                <td>
                                    <!-- only editable if the allocation is not succesful!-->
                                    
                                    <xsl:if test="GD_AllocationStatus != 1100">
                                        <a href="{$baseActionURL}?module=batch_creation&amp;action=edit_full_details&amp;GD_internalKey={$classIDNumber}"
                                        > Edit Details</a>
                                     </xsl:if>
                                </td>
                                <td>
                                    <xsl:if test="GD_AllocationStatus != 1100">
                                    <a
                                        href="{$baseActionURL}?module=batch_creation&amp;action=remove_group&amp;GD_intIDToRemove={$classIDNumber}"
                                        >Remove</a>
                                    </xsl:if>
                                    <xsl:if test="GD_AllocationStatus = 1100">
                                        <font color="green"><b>Allocation Complete</b></font>
                                        
                                    </xsl:if>    
                                </td>
                            </tr>
                        </xsl:for-each>
                        <!-- form to add new generation -->
                        <tr>
                            <td colspan="10">
                                <hr/>
                            </td>
                        </tr>
                        <tr>
                            <form name="generation_information_form" action="{$baseActionURL}"
                                method="POST">
                                <td width="3%"/>
                                <td width="10%" id="neuragenix-form-row-input-input"
                                    class="uportal-label">
                                    <select name="BIOSPECIMEN_strSampleType" tabindex="21"
                                        class="uportal-input-text"
                                        onchange="javascript:dropDownUpdate1();">
                                        <xsl:for-each select="BIOSPECIMEN_strSampleType">
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                    <input type="hidden" name="subaction" value=""/>
                                    
                                    <script language="javascript"> function dropDownUpdate1() {
                                        document.generation_information_form.subaction.value = 'refresh';

                                        document.generation_information_form.submit(); } </script>
                                </td>
                                <td width="10%" id="neuragenix-form-row-input" class="uportal-label"
                                    valign="top">
                                    <!-- select box for subtype -->
                                    <select name="BIOSPECIMEN_strSampleSubType" tabindex="22"
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_strSampleSubType">
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected='1'">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                                <td width="10%" id="neuragenix-form-row-input" class="uportal-label">
                                    <select name="BIOSPECIMEN_TRANSACTIONS_strTreatment"
                                        tabindex="23" class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_TRANSACTIONS_strTreatment">
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                                <td width="10%" id="neuragenix-form-row-input" class="uportal-label">
                                    <input type="text" name="GENERATION_DETAILS_intNumber" size="4"
                                        tabindex="24"
                                        class="uportal-input-text"
                                        align="right" value="1" />
                                </td>
                                <td width="10%" class="uportal-label"
                                    id="neuragenix-form-row-input-input">
                                    <input type="text" name="BIOSPECIMEN_TRANSACTIONS_flQuantity"
                                        size="4" tabindex="24" class="uportal-input-text"
                                        style="text-align: right" value="0"/>
                                </td>
                                <td>
                                    <select name="BIOSPECIMEN_TRANSACTIONS_strUnit" tabindex="25"
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_TRANSACTIONS_strUnit">
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                                <!--
                                <td>
                                    <input type="checkbox" name="GENERATION_DETAILS_blNewTray"
                                        tabindex="26" value="true" class="uportal-text"/>
                                </td> -->
                                <!--spacer-->
                                <td/>
                                <td>
                                    <!-- action for the channel to pick up !-->
                                    <input type="hidden" name="module" value="batch_creation"/>
                                    <input type="hidden" name="action"
                                        value="add_generation_details"/>
                                    <input type="submit" value="Add" tabindex="30"/>
                                </td>
                            </form>
                        </tr>
                        <tr>
                            <!-- take the whole page -->
                            <td colspan="12">
                                <xsl:apply-templates select="details"/>
                            </td>
                        </tr>
                        
                        <tr>
                            <td colspan="12"><hr/></td> 
                        </tr>
                        
                        <tr>
                            <td colspan="12" align="right">
                                <form name="next_form" action="{$baseActionURL}">
                                    
                                    <!-- <input type="button" value="back"/> -->
                                    <input type="submit" value="next" class="uportal-button"></input>
                                    <input type="hidden" name="action" value="start_inventory_allocation"/>
                                    <input type="hidden" name="module" value="Batch_creation"/>
                                    
                                    
                                </form>
                                
                            </td>
                        </tr>    
                    </table>
                </td>
            </tr>
        </table>
    </xsl:template>
    <xsl:template match="details">
        <!-- detail form -->
        <xsl:variable name="GD_internalID">
            <xsl:value-of select="GD_internalID"/>
        </xsl:variable>
        <table width="100%">
            <tr>
                <td colspan="5">
                    <hr/>
                </td>
            </tr>
            <tr>
                <td colspan="5" width="100%" align="left" class="uportal-channel-subtitle"> Edit
                    details for group <xsl:value-of select="GD_internalID +1"/>
                </td>
            </tr>
            <form name="biospecimen_form" action="{$baseActionURL}" method="post">
                <input type="hidden" name="intInvCellID" value="-1"/>
                <input type="hidden" name="module" value="batch_creation"/>
                <input type="hidden" name="action" value="save_details"/>
                <input type="hidden" name="GD_internalKey" value="{$GD_internalID}"/>
                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                                <td class="neuragenix-form-required-text" width="50%"
                                    id="neuragenix-required-header" align="right"> * = Required
                                    fields </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <!-- Order of tabs: left column starts from 21 upwards. The right column starts from 31-->
                        <table width="100%">
                            <!-- Row 1 -->
                            <tr>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text">*</td>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strBiospecimenIDDisplay"/>: </td>
                                <td class="uportal-label" id="neuragenix-form-row-input-input">
                                    <xsl:choose>
                                        <xsl:when test="BiospecimenIDAutoGeneration='true'">
                                            <span class="uportal-label">System Generated</span>
                                            <xsl:variable name="tempValue">
                                                <xsl:value-of
                                                  select="BiospecimenIDAutoGeneration/@tempValue"
                                                />
                                            </xsl:variable>
                                            <input type="hidden" name="BIOSPECIMEN_strBiospecimenID"
                                                value="{$tempValue}"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <!--<input type="text" name="BIOSPECIMEN_strBiospecimenID" size="20" tabindex="21" value="{$strBiospecimenID}" class="uportal-input-text"/>!-->
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </td>
                                <td width="5%"/>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strParentIDDisplay"/>: </td>
                                <td class="uportal-label" id="neuragenix-form-row-input-input">
                                    <xsl:value-of select="BIOSPECIMEN_strParentID"/>
                                    
                                    
                                    <input type="hidden" name="BIOSPECIMEN_strParentID">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="BIOSPECIMEN_strParentID"/>
                                        </xsl:attribute>
                                    </input>
                                    
                                    <input type="hidden" name="BIOSPECIMEN_intParentID">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="BIOSPECIMEN_intParentID"/>
                                        </xsl:attribute>
                                    </input>
                                    
                                    <input type="hidden" name="BIOSPECIMEN_intPatientID">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="BIOSPECIMEN_intPatientID"/>
                                        </xsl:attribute>
                                    </input>    
                                    
                                </td>
                                <td id="neuragenix-end-spacer"/>
                            </tr>
                            <!-- Row 2 -->
                            <tr>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strSampleTypeDisplay"/>: </td>
                                <input name="subaction" value="temp" type="hidden"></input>
                                <script language="javascript"> function dropDownUpdate() {
                                    document.biospecimen_form.action.value = 'edit_full_details';
                                    document.biospecimen_form.subaction.value='refresh';
                                    document.biospecimen_form.submit(); } </script>
                                <td id="neuragenix-form-row-input-input" class="uportal-label">
                                    <select name="BIOSPECIMEN_strSampleType" tabindex="22"
                                        class="uportal-input-text"
                                        onchange="javascript:dropDownUpdate()">
                                        <xsl:for-each select="BIOSPECIMEN_strSampleType">
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                                <!--
                                <td width="5%"/>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strOtherIDDisplay"/>: </td>
                                <td id="neuragenix-form-row-input-input" class="uportal-label">
                                    <input type="text" name="BIOSPECIMEN_strOtherID" size="20"
                                        tabindex="31" class="uportal-input-text"/>
                                </td>
                                <td id="neuragenix-end-spacer"/> -->
                            </tr>
                            <!-- Row 3 -->
                            <tr>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_strSpeciesDisplay"/>: </td>
                                <td id="neuragenix-form-row-input" class="uportal-label">
                                    <select name="BIOSPECIMEN_strSpecies" tabindex="23"
                                        class="uportal-input-text">
                                        <xsl:for-each select="BIOSPECIMEN_strSpecies">
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                </xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                                <td width="5%"/>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_dtSampleDateDisplay"/>
                                </td>
                                <td width="25%" class="uportal-label">
                                        <xsl:choose>
                                        <xsl:when test="BIOSPECIMEN_dtSampleDate/@display_type='dropdown'">
                                            <select name="BIOSPECIMEN_dtSampleDate_Day" tabindex="32" class="uportal-input-text">
                                                    <xsl:for-each select="BIOSPECIMEN_dtSampleDate_Day">
                                                            <option>
                                                                    <xsl:attribute name="value">
                                                                            <xsl:value-of select="."/>
                                                                    </xsl:attribute>
                                                                    <xsl:if test="@selected=1">
                                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                                    </xsl:if>
                                                                    <xsl:value-of select="."/>
                                                            </option>
                                                    </xsl:for-each>
                                            </select>

                                            <select name="BIOSPECIMEN_dtSampleDate_Month" tabindex="33"
                                                    class="uportal-input-text">
                                                    <xsl:for-each select="BIOSPECIMEN_dtSampleDate_Month">
                                                            <option>
                                                                    <xsl:attribute name="value">
                                                                            <xsl:value-of select="."/>
                                                                    </xsl:attribute>
                                                                    <xsl:if test="@selected=1">
                                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                                    </xsl:if>
                                                                    <xsl:value-of select="."/>
                                                            </option>
                                                    </xsl:for-each>
                                            </select>
                                          </xsl:when>
                                          <xsl:otherwise>
                                            <input type="text" name="BIOSPECIMEN_dtSampleDate_Day" size="2" tabindex="32" class="uportal-input-text">
                                                        <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtSampleDate_Day"/></xsl:attribute>
                                            </input>
                                             <input type="text" name="BIOSPECIMEN_dtSampleDate_Month" size="2" tabindex="33" class="uportal-input-text">
                                                        <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtSampleDate_Month"/></xsl:attribute>
                                            </input>

                                          </xsl:otherwise>
                                          </xsl:choose>


                                            <input type="text" name="BIOSPECIMEN_dtSampleDate_Year" size="5" tabindex="34" class="uportal-input-text">
                                                    <xsl:attribute name="value">
                                                            <xsl:value-of select="BIOSPECIMEN_dtSampleDate_Year"/>
                                                    </xsl:attribute>
                                            </input>
                                </td>
                                <td id="neuragenix-end-spacer"/>
                            </tr>
                            <!-- Row 4 -->
                            <tr>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_intStudyKeyDisplay"/>: </td>
                                <td id="neuragenix-form-row-input" class="uportal-label">
                                    <select class="uportal-input-text"
                                        name="BIOSPECIMEN_intStudyKey" tabindex="25">
                                        <xsl:variable name="intBiospecStudyID">
                                            <xsl:value-of select="BIOSPECIMEN_intStudyKey"/>
                                        </xsl:variable>
                                        <xsl:for-each select="study_list">
                                            <xsl:variable name="varIntStudyID">
                                                <xsl:value-of select="STUDY_intStudyID"/>
                                            </xsl:variable>
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="STUDY_intStudyID"/>
                                                </xsl:attribute>
                                                <xsl:if test="$intBiospecStudyID = $varIntStudyID">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="STUDY_strStudyName"/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                                <td width="5%"/>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label">
                                    <xsl:value-of select="BIOSPECIMEN_dtExtractedDateDisplay"/>
                                </td>
                                <td width="25%" class="uportal-label">
                                        <xsl:choose>
                                        <xsl:when test="BIOSPECIMEN_dtExtractedDate/@display_type='dropdown'">
                                            <select name="BIOSPECIMEN_dtExtractedDate_Day" tabindex="35"
                                                    class="uportal-input-text">
                                                    <xsl:for-each select="BIOSPECIMEN_dtExtractedDate_Day">
                                                            <option>
                                                                    <xsl:attribute name="value">
                                                                            <xsl:value-of select="."/>
                                                                    </xsl:attribute>
                                                                    <xsl:if test="@selected=1">
                                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                                    </xsl:if>
                                                                    <xsl:value-of select="."/>
                                                            </option>
                                                    </xsl:for-each>
                                            </select>

                                            <select name="BIOSPECIMEN_dtExtractedDate_Month" tabindex="36"
                                                    class="uportal-input-text">
                                                    <xsl:for-each select="BIOSPECIMEN_dtExtractedDate_Month">
                                                            <option>
                                                                    <xsl:attribute name="value">
                                                                            <xsl:value-of select="."/>
                                                                    </xsl:attribute>
                                                                    <xsl:if test="@selected=1">
                                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                                    </xsl:if>
                                                                    <xsl:value-of select="."/>
                                                            </option>
                                                    </xsl:for-each>
                                            </select>
                                          </xsl:when>
                                          <xsl:otherwise>
                                            <input type="text" name="BIOSPECIMEN_dtExtractedDate_Day" size="2" tabindex="35" class="uportal-input-text">
                                                        <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtExtractedDate_Day"/></xsl:attribute>
                                            </input>
                                             <input type="text" name="BIOSPECIMEN_dtExtractedDate_Month" size="2" tabindex="36" class="uportal-input-text">
                                                        <xsl:attribute name="value"><xsl:value-of select="BIOSPECIMEN_dtExtractedDate_Month"/></xsl:attribute>
                                            </input>

                                          </xsl:otherwise>
                                          </xsl:choose>
                                    
                                            <input type="text" name="BIOSPECIMEN_dtExtractedDate_Year" size="5" tabindex="37" class="uportal-input-text">
                                                    <xsl:attribute name="value">
                                                            <xsl:value-of select="BIOSPECIMEN_dtExtractedDate_Year"/>
                                                    </xsl:attribute>
                                            </input>
                                </td>
                                <td id="neuragenix-end-spacer"/>
                            </tr>
                            <!-- last row-->
                            <xsl:variable name="strEncounterDisplay">
                                <xsl:value-of select="BIOSPECIMEN_strEncounterDisplay"/>
                            </xsl:variable>
                            <xsl:if test="count( BIOSPECIMEN_strEncounterDisplay) &gt; 0">
                            <tr>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text" valign="top">*</td>
                                <td id="neuragenix-form-row-input-label" class="uportal-label"
                                    valign="top">
                                    <xsl:value-of select="BIOSPECIMEN_strEncounterDisplay"/>: </td>
                                <td id="neuragenix-form-row-input" class="uportal-label"
                                    valign="top">
                                    <xsl:variable name="strEncounter">
                                        <xsl:value-of select="BIOSPECIMEN_strEncounter"/>
                                    </xsl:variable>
                                    <select name="BIOSPECIMEN_strEncounter" tabindex="27"
                                        class="uportal-input-text">
                                        <xsl:for-each select="search_encounter_list">
                                            <xsl:variable name="varEncounter">
                                                <xsl:value-of select="ADMISSIONS_strAdmissionID"/>
                                            </xsl:variable>
                                            <option>
                                                <xsl:attribute name="value">
                                                  <xsl:value-of select="ADMISSIONS_strAdmissionID"
                                                  />
                                                </xsl:attribute>
                                                <xsl:if test="$strEncounter=$varEncounter">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="ADMISSIONS_strAdmissionID"/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                                <td width="5%" valign="top"/>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text" valign="top"/>
                                <td id="neuragenix-end-spacer" valign="top"/>
                                <td id="neuragenix-end-spacer" valign="top"/>
                                <td id="neuragenix-end-spacer" valign="top"/>
                            </tr>
                            </xsl:if>
                             <tr>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text" valign="top"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label"
                                    valign="top"/>
                                <td id="neuragenix-form-row-input" class="uportal-label"
                                    valign="top"/>
                                <td width="5%" valign="top"/>
                                <td id="neuragenix-form-row-input-label-required"
                                    class="neuragenix-form-required-text" valign="top"/>
                                <td id="neuragenix-form-row-input-label" class="uportal-label"
                                    valign="top">
                                    <xsl:value-of select="BIOSPECIMEN_strCommentsDisplay"/>: </td>
                                <td width="25%" valign="top">
                                    <textarea name="BIOSPECIMEN_strComments" rows="4" cols="40"
                                        tabindex="43" class="uportal-input-text">
                                        <xsl:value-of select="BIOSPECIMEN_strComments"/>
                                    </textarea>
                                </td>
                                <td id="neuragenix-end-spacer" valign="top"/>
                            </tr>
                            <!-- subtype table go here -->
                            <xsl:if test="count(BIOSPECIMEN_strSampleSubType) &gt; 0">
                                <tr>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text" valign="top"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label" valign="top">Sub type: </td>
                                    <td width="26%" id="neuragenix-form-row-input"
                                        class="uportal-label" valign="top">
                                        <!-- select box for subtype -->
                                        <select name="BIOSPECIMEN_strSampleSubType" tabindex="28"
                                            class="uportal-input-text">
                                            <xsl:for-each select="BIOSPECIMEN_strSampleSubType">
                                                <option>
                                                  <xsl:attribute name="value">
                                                  <xsl:value-of select="."/>
                                                  </xsl:attribute>
                                                  <xsl:if test="@selected='1'">
                                                  <xsl:attribute name="selected"
                                                  >true</xsl:attribute>
                                                  </xsl:if>
                                                  <xsl:value-of select="."/>
                                                </option>
                                            </xsl:for-each>
                                        </select>
                                    </td>
                                    <td width="5%" valign="top"/>
                                    <td width="1%" id="neuragenix-form-row-input-label-required"
                                        class="neuragenix-form-required-text" valign="top"/>
                                    <td width="18%" id="neuragenix-form-row-input-label"
                                        class="uportal-label" valign="top">
                                        <xsl:value-of
                                            select="BIOSPECIMEN_strSubTypeDescriptionDisplay"/>: </td>
                                    <td width="25%" valign="top">
                                        <textarea name="BIOSPECIMEN_strSubTypeDescription" rows="4"
                                            cols="40" tabindex="44" class="uportal-input-text">
                                            <xsl:value-of select="BIOSPECIMEN_strSubTypeDescription"
                                            />
                                        </textarea>
                                    </td>
                                    <td width="5%" id="neuragenix-end-spacer" valign="top"/>
                                </tr>
                            </xsl:if>
                            <!-- subtype table end here -->
                        </table>
                    </td>
                </tr>
                
                <tr>
                    <td colspan="6"><hr/></td>
                </tr>    
                <tr>
                    <td colspan="6" class="uportal-channel-subtitle" align="left">Quantity Details</td>
                </tr>
                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text"/>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                        Available quantity: </td>
                    <td width="26%" id="neuragenix-form-row-input" class="uportal-input-text"
                        style="text-align: right">
                        <xsl:value-of select="BIOSPECIMEN_flNumberCollected"/>
                    </td>
                    <td width="5%"/>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text">
                        <xsl:if
                            test="./BIOSPECIMEN_TRANSACTIONS_strCollaboratorDisplay[@required='true']"
                            > * </xsl:if>
                    </td>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                        Collaborator: </td>
                    <td width="26%" id="neuragenix-form-row-input" class="uportal-label">
                        <select name="BIOSPECIMEN_TRANSACTIONS_strCollaborator" tabindex="61"
                            class="uportal-input-text">
                            <xsl:for-each select="BIOSPECIMEN_TRANSACTIONS_strCollaborator">
                                <option>
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="."/>
                                    </xsl:attribute>
                                    <xsl:if test="@selected=1">
                                        <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="."/>
                                </option>
                            </xsl:for-each>
                        </select>
                    </td>
                    <td width="5%" id="neuragenix-end-spacer"/>
                </tr>
                <tr>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text">
                        <xsl:if
                            test="./BIOSPECIMEN_TRANSACTIONS_dtTransactionDateDisplay[@required='true']"
                            > * </xsl:if>
                    </td>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_dtTransactionDateDisplay"/>: </td>
                    <td width="26%" id="neuragenix-form-row-input" class="uportal-label">
                        <select name="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Day" tabindex="51"
                            class="uportal-input-text">
                            <xsl:for-each select="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Day">
                                <option>
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="."/>
                                    </xsl:attribute>
                                    <xsl:if test="@selected=1">
                                        <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="."/>
                                </option>
                            </xsl:for-each>
                        </select>
                        <select name="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Month"
                            tabindex="52" class="uportal-input-text">
                            <xsl:for-each select="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Month">
                                <option>
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="."/>
                                    </xsl:attribute>
                                    <xsl:if test="@selected=1">
                                        <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="."/>
                                </option>
                            </xsl:for-each>
                        </select>
                        <xsl:variable name="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Year">
                            <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Year"/>
                        </xsl:variable>
                        <input type="text" name="BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Year"
                            value="{$BIOSPECIMEN_TRANSACTIONS_dtTransactionDate_Year}" size="5"
                            tabindex="53" class="uportal-input-text"/>
                    </td>
                    <td width="5%"/>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text">
                        <xsl:if
                            test="BIOSPECIMEN_TRANSACTIONS_dtTransactionDateDisplay[@required='true']"
                            >*</xsl:if>
                    </td>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strTreatmentDisplay"/>: </td>
                    <td width="26%" id="neuragenix-form-row-input" class="uportal-label">
                        <select name="BIOSPECIMEN_TRANSACTIONS_strTreatment" tabindex="62"
                            class="uportal-input-text">
                            <xsl:for-each select="BIOSPECIMEN_TRANSACTIONS_strTreatment">
                                <option>
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="."/>
                                    </xsl:attribute>
                                    <xsl:if test="@selected=1">
                                        <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="."/>
                                </option>
                            </xsl:for-each>
                        </select>
                    </td>
                    <td width="5%" id="neuragenix-end-spacer"/>
                </tr>
                <tr>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text">
                        <xsl:if test="BIOSPECIMEN_TRANSACTIONS_flQuantityDisplay[@required='true']"
                            >*</xsl:if>
                    </td>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_flQuantityDisplay"/>: </td>
                    <td width="26%" class="uportal-label" id="neuragenix-form-row-input-input">
                        <input type="text" name="BIOSPECIMEN_TRANSACTIONS_flQuantity" size="10"
                            tabindex="54" class="uportal-input-text" style="text-align: right">
                            <xsl:attribute name="value">
                                <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_flQuantity"/>
                            </xsl:attribute>
                        </input>    
                        <!-- anton: preselect the quantity unit if there was a quantity added with particular unit-->
                        <xsl:choose>
                            <xsl:when test="count(search_trans/BIOSPECIMEN_TRANSACTIONS_strUnit)
                                &gt; 0"> &#160; <xsl:value-of
                                    select="search_trans/BIOSPECIMEN_TRANSACTIONS_strUnit[1]"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <select name="BIOSPECIMEN_TRANSACTIONS_strUnit" tabindex="55"
                                    class="uportal-input-text">
                                    <xsl:for-each select="BIOSPECIMEN_TRANSACTIONS_strUnit">
                                        <option>
                                            <xsl:attribute name="value">
                                                <xsl:value-of select="."/>
                                            </xsl:attribute>
                                            <xsl:if test="@selected=1">
                                                <xsl:attribute name="selected">true</xsl:attribute>
                                            </xsl:if>
                                            <xsl:value-of select="."/>
                                        </option>
                                    </xsl:for-each>
                                </select>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                    <td width="5%"/>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text">
                        <xsl:if test="BIOSPECIMEN_TRANSACTIONS_strReasonDisplay[@required='true']"
                        >*</xsl:if>
                    </td>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strReasonDisplay"/>: </td>
                    <td width="26%" class="uportal-label" id="neuragenix-form-row-input-input">
                        <textarea name="BIOSPECIMEN_TRANSACTIONS_strReason" rows="4" cols="25"
                            class="uportal-input-text" tabindex="63">
                            <xsl:value-of select="strBiospecDescription"/>
                        </textarea>
                    </td>
                    <td width="5%" id="neuragenix-end-spacer"/>
                </tr>
                <tr>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text">
                        <xsl:if test="BIOSPECIMEN_TRANSACTIONS_strOwnerDisplay[@required='true']"
                        >*</xsl:if>
                    </td>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strOwnerDisplay"/>: </td>
                    <td width="26%" class="uportal-label" id="neuragenix-form-row-input-input">
                        <select name="BIOSPECIMEN_TRANSACTIONS_strOwner" tabindex="56"
                            class="uportal-input-text">
                            <xsl:variable name="strCurrentUser">
                                <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strRecordedBy"/>
                            </xsl:variable>
                            <xsl:for-each select="search_user">
                                <xsl:variable name="strUser">
                                    <xsl:value-of select="USERDETAILS_strUserName"/>
                                </xsl:variable>
                                <option>
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="USERDETAILS_strUserName"/>
                                    </xsl:attribute>
                                    <xsl:if test="$strCurrentUser = $strUser">
                                        <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="USERDETAILS_strUserName"/>
                                </option>
                            </xsl:for-each>
                        </select>
                    </td>
                    <td width="5%"/>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text"/>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strRecordedByDisplay"/>: </td>
                    <td width="26%" class="uportal-text" id="neuragenix-form-row-input-input">
                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strRecordedBy"/>
                        <input type="hidden" name="BIOSPECIMEN_TRANSACTIONS_strRecordedBy">
                            <xsl:attribute name="value">
                                <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strRecordedBy"/>
                            </xsl:attribute>
                        </input>
                    </td>
                    <td width="5%" id="neuragenix-end-spacer"/>
                </tr>
                <!-- study dropdown -->
                <tr>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text">
                        <xsl:if test="BIOSPECIMEN_TRANSACTIONS_intStudyKeyDisplay[@required='true']"
                            >*</xsl:if>
                    </td>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_intStudyKeyDisplay"/>: </td>
                    <td width="26%" class="uportal-label" id="neuragenix-form-row-input-input">
                        <select name="BIOSPECIMEN_TRANSACTIONS_intStudyKey" tabindex="57"
                            class="uportal-input-text">
                            <xsl:variable name="strSelectedBioTxnStudy">
                                <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_intStudyKey"/>
                            </xsl:variable>
                            <xsl:for-each select="study_list">
                                <xsl:variable name="intStudyID">
                                    <xsl:value-of select="./STUDY_intStudyID"/>
                                </xsl:variable>
                                <xsl:variable name="strStudyName">
                                    <xsl:value-of select="./STUDY_strStudyName"/>
                                </xsl:variable>
                                <option>
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="./STUDY_intStudyID"/>
                                    </xsl:attribute>
                                    <xsl:if test="$strSelectedBioTxnStudy = $intStudyID">
                                        <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="./STUDY_strStudyName"/>
                                </option>
                            </xsl:for-each>
                        </select>
                    </td>
                    <td width="5%"/>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text">
                        <xsl:if test="BIOSPECIMEN_TRANSACTIONS_strStatusDisplay[@required='true']"
                        >*</xsl:if>
                    </td>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strStatusDisplay"/>: </td>
                    <td width="26%" class="uportal-text" id="neuragenix-form-row-input-input">
                        <select name="BIOSPECIMEN_TRANSACTIONS_strStatus" tabindex="64"
                            class="uportal-input-text">
                            <xsl:for-each select="BIOSPECIMEN_TRANSACTIONS_strStatus">
                                <option>
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="."/>
                                    </xsl:attribute>
                                    <xsl:if test="@selected=1">
                                        <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="."/>
                                </option>
                            </xsl:for-each>
                        </select>
                    </td>
                    <td width="5%" id="neuragenix-end-spacer"/>
                </tr>
                <!-- Destination Study -->
                <tr>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text">
                        <xsl:if
                            test="BIOSPECIMEN_TRANSACTIONS_strFixationTimeDisplay[@required='true']"
                            >*</xsl:if>
                    </td>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label">
                        <xsl:value-of select="BIOSPECIMEN_TRANSACTIONS_strFixationTimeDisplay"/>: </td>
                    <td width="26%" class="uportal-label" id="neuragenix-form-row-input-input">
                        <input tabindex="58" name="BIOSPECIMEN_TRANSACTIONS_strFixationTime"
                            class="uportal-input-text"/>
                    </td>
                    <td width="5%"/>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text"/>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label"/>
                    <td width="26%" class="uportal-text" id="neuragenix-form-row-input-input"/>
                    <td width="5%" id="neuragenix-end-spacer"/>
                </tr>
                <tr>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text"/>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label"/>
                    <td width="26%" class="uportal-label" id="neuragenix-form-row-input-input"/>
                    <td width="5%"/>
                    <td width="1%" id="neuragenix-form-row-input-label-required"
                        class="neuragenix-form-required-text"/>
                    <td width="18%" id="neuragenix-form-row-input-label" class="uportal-label"/>
                    <td width="26%" class="uportal-text" id="neuragenix-form-row-input-input"/>
                    <td width="5%" id="neuragenix-end-spacer"/>
                </tr>
                </table>
                </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                                <td width="1%"/>
                                <td width="70%" class="uportal-label"/>
                                <td align="right" class="uportal-label">
                                    <input type="submit" name="Update details" 
                                    value="Update details" class="uportal-button" tabindex="46"/>
                                    <input type="reset" name="clear" value="Clear" tabindex="47"
                                        class="uportal-button"/>
                                </td>
                                <td width="2%"/>
                            </tr>
                        </table>
                    </td>
                </tr>
            </form>
            <!-- end of detail form -->
        </table>
    </xsl:template>
</xsl:stylesheet>
