<?xml version="1.0" encoding="utf-8"?>
<!-- 

    Biospecimen Batch Processing - Batch Cloning Module
    Copyright (C) Neuragenix Pty Ltd, 2005
    Author : Daniel Murley
    Email : dmurley@neuragenix.com
    
    Purpose : Provides data entry for user to  enter details about thhe whatever.
   
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:include href="../biospecimen_menu.xsl"/>
  <xsl:output method="html" indent="yes" />
    <xsl:param name="strErrorMessage">strErrorMessage</xsl:param>
  <xsl:template match="BiospecimenGeneration">
  <!-- Get the parameters from the channel class -->
  <xsl:param name="strBiospecimenID"><xsl:value-of select="strBiospecimenID" /></xsl:param>
  <xsl:param name="strBiospecOtherID"><xsl:value-of select="strBiospecOtherID" /></xsl:param>
  <xsl:param name="intBiospecStudyID"><xsl:value-of select="intBiospecStudyID" /></xsl:param>
  <xsl:param name="intInternalPatientID"><xsl:value-of select="intInternalPatientID" /></xsl:param>
  <xsl:param name="intBiospecParentID"><xsl:value-of select="intBiospecParentID" /></xsl:param>
  <xsl:param name="strBiospecParentID"><xsl:value-of select="strBiospecParentID" /></xsl:param>
  <xsl:variable name="AutoGenerateBiospecimenID" select="AutoGenerateBiospecimenID"></xsl:variable>
  <!-- <xsl:param name="baseActionURL"><xsl:value-of select="baseActionURL" /></xsl:param> -->
  
                        <script>
                        
                          function updateIDPrefix(prefix)
                          {
                             var autoID = <xsl:value-of select="$AutoGenerateBiospecimenID"/>;
                             if (autoID == false)
                             {
                                 for (var i = 1; i &lt;= <xsl:value-of select="number(GenerationDetails/GD_AllocationAmount)" />; i++)
                                 {
                                     var textBox = document.getElementById('id_' + i);
                                     textBox.value = prefix + textBox.value;
                                 }
                             }
                          
                          }
                          
                          function updateBarcodePrefix(prefix)
                          {
                             for (var i = 1; i &lt;= <xsl:value-of select="number(GenerationDetails/GD_AllocationAmount)" />; i++)
                             {
                                 var textBox = document.getElementById('barcode_' + i);
                                 textBox.value = prefix + textBox.value;
                             }
                          
                          }
                          
                          function updateStudy(index)
                          {
                             for (var i = 1; i &lt;= <xsl:value-of select="number(GenerationDetails/GD_AllocationAmount)" />; i++)
                             {
                                 var textBox = document.getElementById('study_' + i);
                                 textBox.selectedIndex = index;
                             }
                          
                          }
                          
                          
                        </script>
  
  
  
  
  
	<table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Batch Processing - Enter Specimen IDs<br/>
                        
			</td>
		    <td align="right">
		        <form name="back_form" action="{$baseActionURL}?module=batch_clone&amp;current=bp_clone&amp;stage=OPTIONSET&amp;mode=VALIDATION" method="post"/>
		        <img border="0" src="media/neuragenix/buttons/previous_enabled.gif"
		            alt="Previous" onclick="javascript:document.back_form.submit();"/>
		        <img border="0" src="media/neuragenix/buttons/next_disabled.gif"
		            alt="Next"/>
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
         <form name="BiospecimenDetails" action="{$baseActionURL}?module=batch_clone&amp;current=bp_clone&amp;stage=ALLOCATOR&amp;mode=VALIDATE&amp;intAllocationID={GenerationDetails/GD_internalID}" method="post"> 
	
        
         <table width="100%">
            
            <xsl:if test="GenerationDetails/GD_AllocationMode=2">
              <tr>
                 <td>
                    <span class="uportal-label">Add new tray</span>
                 </td>
              </tr>
               <tr> 
                    <td class="neuragenix-form-required-text" width="50%">
                        <xsl:value-of select="$strErrorMessage"/>
                    </td>
                 </tr>
              <tr>
                   <td>
                                  <!---  Add tray details -->  
                      
                    <table width="100%">
                        <tr><td width="1%" class="neuragenix-form-required-text">*</td>
                            <td width="19%" class="uportal-label">
                                Box name:
                            </td>
                            <td width="25%">
                                <select name="TRAY_intBoxID" tabindex="1" class="uportal-input-text">
                                    <xsl:for-each select="search_box">

                                        <option>
                                            <xsl:attribute name="value">
                                                <xsl:value-of select="BOX_intBoxID" />
                                            </xsl:attribute>

                                            <xsl:value-of select="BOX_strBoxName" />
                                        </option>
                                    </xsl:for-each>
                                </select>
                            </td>
                            <td width="10%"></td><td width="1%" class="neuragenix-form-required-text">*</td>
                            <td width="19%" class="uportal-label">
                                <xsl:value-of select="TRAY_intNoOfColDisplay" />:
                            </td>
                            <td width="50%">
                                <input type="text" style="text-align: right" name="TRAY_intNoOfCol" size="10" tabindex="3" class="uportal-input-text" />

                                <select name="TRAY_strColNoType" tabindex="4" class="uportal-input-text">
                                    <xsl:for-each select="TRAY_strColNoType">

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

                        <tr><td width="1%" class="neuragenix-form-required-text">*</td>
                            <td width="19%" class="uportal-label">
                                <xsl:value-of select="TRAY_strTrayNameDisplay" />:
                            </td>
                            <td>
                                <input type="text" name="TRAY_strTrayName" size="22" tabindex="2" class="uportal-input-text" />
                            </td>
                            <td width="10%"></td><td width="1%" class="neuragenix-form-required-text">*</td>
                            <td width="19%" class="uportal-label">
                                <xsl:value-of select="TRAY_intNoOfRowDisplay" />:
                            </td>
                            <td width="25%">
                                <input type="text" style="text-align: right" name="TRAY_intNoOfRow" size="10" tabindex="5" class="uportal-input-text" />

                                <select name="TRAY_strRowNoType" tabindex="6" class="uportal-input-text">
                                    <xsl:for-each select="TRAY_strRowNoType">

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
                            <td colspan="7"><hr /></td>
                        </tr>
                  
           
           </table>
           </td>  
           </tr>
         </xsl:if>
             

        <tr>
            <td>
           <table width="100%"> 
            
            
               <tr>
                  
                  <td width="100%">
 
                 <table class="uportal-label">

                     <tr>
                     
                        <xsl:choose>
                            <xsl:when test="$AutoGenerateBiospecimenID='true'">
                                <td colspan="2">
                                    Biospecimen IDs are System Generated
                                </td>
                            </xsl:when>
                            <xsl:otherwise>
                                <td>
                                    ID Prefix :
                                </td>
                                <td>
                                <xsl:variable name="prefixBioID"><xsl:value-of select="/BiospecimenGeneration/prefixBioID" /></xsl:variable>
                                <input type="text" class="uportal-input-text" size="30" id="idMasterPrefix" value="{$prefixBioID}"/>  
                                <input type="button" class="uportal-button" value=" Update " onClick="javascript:updateIDPrefix(document.getElementById('idMasterPrefix').value)" />

                                </td>
                            </xsl:otherwise>
                        </xsl:choose>
                     </tr>
                    <tr>
                        <td>
                            Barcode Prefix :
                        </td>
                        <td>
                             <input type="text" size="30" class="uportal-input-text" id="barcodeMasterPrefix" /> 
                             <input type="button" class="uportal-button" value=" Update " onClick="javascript:updateBarcodePrefix(document.getElementById('barcodeMasterPrefix').value)"/>
                        </td>
                     </tr>
                    <tr>
                        <td>
                            Study :
                        </td>
                        <td>
                            <xsl:call-template name="GetStudyDropdown">
                               <xsl:with-param name="id">master</xsl:with-param>   
                            </xsl:call-template>    
                        
                        <input type="button" class="uportal-button"  value=" Update " onClick="javascript:updateStudy(document.getElementById('study_master').selectedIndex)"/>

                        </td>
                     </tr>

                 </table>    



              </td>
              <td width="50%" />
           </tr>
           </table>
            </td>
        </tr>
             <tr><td><hr /></td></tr>


        <tr>
            <td>
        <table width="100%">
            <tr>
                <td width="1%" class="neuragenix-form-required-text">*</td>
                 <td width="35%" class="uportal-label">Specimen ID </td>
               
                <!--td width="1%" class="neuragenix-form-required-text">*</td-->
                <td width="1%" class="neuragenix-form-required-text">&#160;</td>
               <td class="uportal-label" width="35%">Specimen Barcode
               </td>
                <td width="1%" class="neuragenix-form-required-text">*</td>
               <td width="17%" class="uportal-label">Specimen Study </td>
               <td>
                    <xsl:if test="(count(/BiospecimenGeneration/InvalidBiospecimenID) &gt; 0) or (count(/BiospecimenGeneration/MissingBiospecimenID) &gt; 0)">
                       <span class="uportal-label">Errors</span>
                    </xsl:if>
               </td>
            </tr>
       
           
                <xsl:call-template name="createIDList">
                    <xsl:with-param name="generationsRemaining" select="number(GenerationDetails/GD_AllocationAmount)" />
                    <xsl:with-param name="autogen" select="$AutoGenerateBiospecimenID" />
                </xsl:call-template>
                


            
           <tr>
              <td colspan="4">
                 <br />
              </td>
           </tr>
           <tr>
               <!--
	      <td colspan="2" align="left">
                      <input type="button" class="uportal-input-text" value="&lt; Back" onClick="javascript:jumpTo('{$baseActionURL}?module=batch_clone&amp;current=bp_clone&amp;stage=OPTIONSET&amp;mode=VALIDATION')" />
              </td> -->
              <td colspan="2" align="right">
                   <input type="submit" class="uportal-button" value="Complete This Allocation &gt;" />
              </td>
           </tr>
        </table>
            </td>
            </tr>
        
        </table>
        </form>   
        

        
  </xsl:template>

  <xsl:template name="createIDList">
      <xsl:param name="generationsRemaining" select="generationsRemaining" />
      <xsl:param name="autogen" select="autogen" />
      <xsl:if test="number($generationsRemaining)>0">
      
            <tr>
               <xsl:attribute name="bgcolor">
                    <xsl:for-each select="runtimeData">
                       <xsl:variable name="itemName" select="@name" /> 
                       <xsl:variable name="compName">id_<xsl:value-of select="$generationsRemaining" /></xsl:variable>
                       
                       <xsl:if test="$itemName=$compName">
                       <xsl:variable name="specimenID" select="." />
                       
                       <xsl:choose>
                           <xsl:when test="count(/BiospecimenGeneration/InvalidBiospecimenID) &gt; 0">
                               <xsl:for-each select="/BiospecimenGeneration/InvalidBiospecimenID">
                                   <xsl:variable name="invalidID" select="." /> 
                                   <xsl:if test="$invalidID=$specimenID">FF0000</xsl:if>
                               </xsl:for-each>
                           </xsl:when>
                           <xsl:when test="count(/BiospecimenGeneration/MissingBiospecimenID) &gt; 0">
                               <xsl:for-each select="/BiospecimenGeneration/MissingBiospecimenID">
                                   <xsl:variable name="invalidID" select="." /> 

                                   <xsl:if test="$invalidID=$generationsRemaining">FF0000</xsl:if>
                               </xsl:for-each>

                           </xsl:when>
                       </xsl:choose>
                       
                       
                       </xsl:if>
                    </xsl:for-each>
               
               </xsl:attribute>
               
               
               
               <td width="1%"/>
               <td width="35%"> 
                    <xsl:choose>
                        <xsl:when test="$autogen='true'">
                             System Generated                        
                        </xsl:when>
                        <xsl:otherwise>
                            <input type="text" name="id_{$generationsRemaining}" id="id_{$generationsRemaining}" class="uportal-input-text">
                            <xsl:attribute name="value">

                            <!-- <xsl:value-of select="/BiospecimenGeneration/*[id_$generationsRemaining]" /> -->
                            <!-- <xsl:value-of select="/BiospecimenGeneration/*[name()=concat('id_', $generationsRemaining)]" /> -->

                            <xsl:for-each select="runtimeData">
                               <xsl:variable name="itemName" select="@name" /> 
                               <xsl:variable name="compName">id_<xsl:value-of select="$generationsRemaining" /></xsl:variable>

                               <xsl:if test="$itemName=$compName">
                                  <xsl:value-of select="." />
                               </xsl:if>
                            </xsl:for-each>

                            </xsl:attribute>

                            </input>
                        </xsl:otherwise>
                     </xsl:choose>
               </td>
                <td width="1%"/>     
               <td>
                    <input type="text" name="barcode_{$generationsRemaining}" id="barcode_{$generationsRemaining}" class="uportal-input-text">        
                    <xsl:attribute name="value">
                    
                      <xsl:for-each select="runtimeData">
                       <xsl:variable name="itemName" select="@name" /> 
                       <xsl:variable name="compName">barcode_<xsl:value-of select="$generationsRemaining" /></xsl:variable>
                       
                       <xsl:if test="$itemName=$compName">
                          <xsl:value-of select="." />
                       </xsl:if>
                    </xsl:for-each>
                       
                       </xsl:attribute>
                    
                    </input>
               </td>
                <td width="1%"/>
               <td>
                    <xsl:call-template name="GetStudyDropdown">
                        <xsl:with-param name="id" select="$generationsRemaining" />   
                    </xsl:call-template>
                 
               </td>
               <td>
                    <xsl:for-each select="runtimeData">
                       <xsl:variable name="itemName" select="@name" /> 
                       <xsl:variable name="compName">id_<xsl:value-of select="$generationsRemaining" /></xsl:variable>
                       
                       <xsl:if test="$itemName=$compName">
                       <xsl:variable name="specimenID" select="." />
                       
                       <xsl:choose>
                           <xsl:when test="count(/BiospecimenGeneration/InvalidBiospecimenID) &gt; 0">
                               <xsl:for-each select="/BiospecimenGeneration/InvalidBiospecimenID">
                                   <xsl:variable name="invalidID" select="." /> 

                                   <xsl:if test="$invalidID=$specimenID">
                                      <span class="uportal-label"><xsl:value-of select="@error" /></span>
                                   </xsl:if>
                               </xsl:for-each>
                           </xsl:when>
                           <xsl:when test="count(/BiospecimenGeneration/MissingBiospecimenID) &gt; 0">
                               <xsl:for-each select="/BiospecimenGeneration/MissingBiospecimenID">
                                   <xsl:variable name="invalidID" select="." /> 

                                   <xsl:if test="$invalidID=$generationsRemaining">
                                      <span class="uportal-label"><xsl:value-of select="@error" /></span>
                                   </xsl:if>
                               </xsl:for-each>
                           </xsl:when>
                       </xsl:choose>
                       
                       
                       </xsl:if>
                    </xsl:for-each>
               </td>
            </tr>
            <xsl:call-template name="createIDList">
                <xsl:with-param name="generationsRemaining" select="number($generationsRemaining)-1" />
                <xsl:with-param name="autogen" select="$autogen" />
            </xsl:call-template>
      </xsl:if>
      
  </xsl:template>
 
  <xsl:template name="GetStudyDropdown">
      <xsl:param name="id" select="id" />
         <select id="study_{$id}" name="study_{$id}" class="uportal-input-text">
            <xsl:for-each select="study_list">
               <option name="study_selected_{$id}" value="{STUDY_intStudyID}"> <xsl:value-of select="STUDY_strStudyName" /></option>
               
            </xsl:for-each>
         </select>
  </xsl:template>
             
      
  
  
  
  
  
</xsl:stylesheet>
