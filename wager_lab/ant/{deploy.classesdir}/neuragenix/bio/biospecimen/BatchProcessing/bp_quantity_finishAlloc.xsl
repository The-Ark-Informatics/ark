<?xml version="1.0" encoding="utf-8"?>
<!-- 

    Biospecimen Batch Processing - Batch Quantity Allocation Module
    Copyright (C) Neuragenix Pty Ltd, 2005
    Author : Daniel Murley
    Email : dmurley@neuragenix.com
    
    Purpose : Provides a final list of allocated quantities
   
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="../biospecimen_menu.xsl"/>
  <xsl:output method="html" indent="no" />
  
  <xsl:template match="QuantityAllocation">
        
	<table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Batch Processing - Quantity Allocation Complete<br/><hr/>
			</td>
                        <td>
                        </td>
		</tr>
	</table>
	<table width="100%">
		 
		<tr>
			<td class="neuragenix-form-required-text" width="50%">
					<xsl:value-of select="strErrorDuplicateKey" /><xsl:value-of select="strErrorRequiredFields" /><xsl:value-of select="strErrorInvalidDataFields" /><xsl:value-of select="strErrorInvalidData" />
			</td>
		</tr>
	</table>
        
        
        <form name="OptionSelect2" action="{$baseActionURL}?uP_root=root&amp;current=biospecimen_search" method="POST" class="uportal-text">
	
        <table width="100%">
            <tr>
               <td colspan="2">
                   <span class="uportal-label">
                       
                       <xsl:choose>
                           <xsl:when test="AllocationType=1">
                              The following Biospecimens have had their available quantities allocated : <br />
                           </xsl:when>
                           <xsl:when test="AllocationType=2">
                              The following allocations have been marked as delivered : <br />
                           </xsl:when>
                           <xsl:otherwise>
                              A system failure has occured. The following were not able to be allocated : 
                           </xsl:otherwise>
                       </xsl:choose>
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
                 <td colspan="2">
                    <span class="uportal-text">They have been allocated to : <xsl:value-of select="STUDY_strStudyName" /></span>
                    
                 </td>

                </tr>

            </xsl:if>
            
            <xsl:if test="AllocationType=2">
                <tr>

                    <td>
                        Delivery Date : 
                    </td>
                    <td>
                        <xsl:value-of select="strDeliveryDate" />
                    </td>

                </tr>
            
            </xsl:if>
            
            
            <tr>
                <td>
                    
                </td>
                
                <td>

                       <input type="submit" name="finish" value="Return to biospecimen search &gt;" class="uportal-input-text" />             
            
                </td>
            </tr>
        </table>
	
       
       </form>
  </xsl:template>

      
       
 
</xsl:stylesheet>
