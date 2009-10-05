<?xml version="1.0" encoding="utf-8"?>
<!-- 

    Biospecimen Batch Processing - Batch Cloning Module
    Copyright (C) Neuragenix Pty Ltd, 2005
    Author : Daniel Murley
    Email : dmurley@neuragenix.com
    
    Purpose : Provides first stage of data entry for a user to batch create a set of biospecimens
   
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="../biospecimen_menu.xsl"/>
  <xsl:output method="html" indent="no" />
  
  <xsl:template match="QuantityAllocation">
        
	<table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Batch Processing - Confirm Quantity Allocation<br/>
			</td>
                        <td align="right">
                           <form name="OptionSelect" action="{$baseActionURL}?module=BATCH_ALLOCATE&amp;current=bp_allocate&amp;stage=GETMODE" method="POST" class="uportal-text">
                              <input type="submit" name="back" value="&lt; Back" class="uportal-input-text" />
                           </form>
                        </td>
		</tr>
                <tr valign="top">
                    <td colspan="2">
                         <hr/>
                    </td>
                </tr>
	</table>
	<table width="100%">
		 
		<tr>
			<td class="neuragenix-form-required-text" width="50%">
					<xsl:value-of select="strErrorDuplicateKey" /><xsl:value-of select="strErrorRequiredFields" /><xsl:value-of select="strErrorInvalidDataFields" /><xsl:value-of select="strErrorInvalidData" />
			</td>
			<td class="neuragenix-form-required-text" width="50%" id="neuragenix-required-header" align="right">
			* = Required fields
			</td>

		</tr>
	</table>
        
        
        <form name="OptionSelect2" action="{$baseActionURL}?module=BATCH_ALLOCATE&amp;current=bp_allocate&amp;stage=DOALLOCATION" method="POST" class="uportal-text">
	
        <table width="100%">
            <tr>
               <td colspan="2">
                   <span class="uportal-label">
                       <xsl:if test="AllocationType=1">
                          The following Biospecimens will have all of their available quantities allocated : <br />
                       </xsl:if>
                       <xsl:if test="AllocationType=2">
                          The following allocations will be marked as delivered : <br />
                       </xsl:if>
                   </span>
                   <span class="uportal-text">
                       <ul>
                       <xsl:for-each select="AllocationData">
                           <li><xsl:value-of select="BiospecimenID" /></li>
                       </xsl:for-each>
                       </ul>
                   </span>
               </td>
            </tr>
            
            <xsl:if test="AllocationType=1">
                <tr>
                    <td>
                    <span class="uportal-text">Please select the study to allocate to :</span>
                    </td>
                    <td>
                        <select name="STUDY_intStudyID" class="uportal-input-text">
                            <xsl:for-each select="study_list">
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="STUDY_intStudyID" />
                                        </xsl:attribute> 

                                        <xsl:value-of select="STUDY_strStudyName" />		
                                    </option>                           
                            </xsl:for-each>

                        </select>

                 </td>

                </tr>

                <tr>

                   <td class="uportal-text">
                       Treatment : 
                   </td>
                   <td>
                       <!-- <input type="text" name="strTreatment" value="" class="uportal-input-text" /> -->
                       
                       <select name="strTreatment" class="uportal-input-text">
                            <xsl:for-each select="BIOSPECIMEN_TRANSACTIONS_strTreatment">
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="." />
                                        </xsl:attribute> 

                                        <xsl:value-of select="." />		
                                    </option>                           
                            </xsl:for-each>

                        </select>
                       
                   </td>

                </tr>
               <!-- 
                <tr>

                   <td>Collaborator : 
                   </td>
                   <td><input type="text" name="strCollaborator" value="" />
                   </td>

                </tr>            
                -->

                <tr>

                   <td class="uportal-text">
                       Fixation Time : 
                   </td>
                   <td>
                       <input type="text" name="strFixationTime" value="" class="uportal-input-text" />
                   </td>

                </tr>
            </xsl:if>
            
            <xsl:if test="AllocationType=2">
                <tr>

                    <td class="uportal-text">
                        Delivery Date : 
                    </td>
                    <td>
                        <input type="text" name="strDeliveryDate" value="" class="uportal-input-text" />
                    </td>

                </tr>
            
            </xsl:if>
            
            
            <tr>
                <td>
                    
                </td>
                
                <td align="right">
                       <br />
                       <input type="submit" name="finish" value="Finish &gt;" class="uportal-input-text" />             
            
                </td>
            </tr>
        </table>
	
       
       </form>
  </xsl:template>

      
       
 
</xsl:stylesheet>
