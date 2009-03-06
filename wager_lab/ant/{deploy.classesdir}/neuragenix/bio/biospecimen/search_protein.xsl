<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./biospecimen_menu.xsl"/>


    <xsl:output method="html" indent="no" />
    
    <xsl:template match="proteinMode" />
    
    <xsl:template match="protein">
    
    <xsl:param name="currentField"><xsl:value-of select="currentField" /></xsl:param>
    <xsl:param name="nextField"><xsl:value-of select="nextField"/></xsl:param>
    
    
    
    
    
    <xsl:variable name="intCurrentPage"><xsl:value-of select="currentPage" /></xsl:variable>
    <xsl:variable name="intNoOfPages"><xsl:value-of select="noOfPages" /></xsl:variable>
    <xsl:variable name="intRecordPerPage"><xsl:value-of select="noOfRecords" /></xsl:variable>
    
    <xsl:variable name="PATIENT_intInternalPatientID"><xsl:value-of select="PATIENT_intInternalPatientID"/></xsl:variable>            
    <xsl:variable name="currentField"><xsl:value-of select="currentField"/></xsl:variable>
    <xsl:variable name="nextField"><xsl:value-of select="nextField"/></xsl:variable>
    
    <xsl:variable name="proteinMode"><xsl:value-of select="/body/proteinMode"/> </xsl:variable>
    <xsl:variable name="domain">Bioanalysis</xsl:variable>

    <script language="javascript">
        function getNextField()
        {
           var currentField = '<xsl:value-of select="$currentField" />';
           var nextField = currentField.split('d')[1];
           nextField++;
           
           nextField = 'd' + nextField;

           return nextField;

        }
    </script>
    
    
    <script language="javascript">
    function submitNewPage( offset ){
            document.searchForm.currentPage.value = parseInt( document.searchForm.currentPage.value ) + parseInt(offset);
            document.searchForm.submit();
    }
    
    function reOrder (fieldName)
    {
            document.reOrder.orderBy.value = fieldName;
            document.reOrder.submit();
    }

    function submitBiospecimen( aURL, proteinKey, proteinName, proteinLocalisation, currentField, nextField ){
            document.biospecimen.PROTEIN_intProteinKey.value = proteinKey;
            document.biospecimen.PROTEIN_strName.value = proteinName;
            document.biospecimen.PROTEIN_strLocalisation.value = proteinLocalisation;
            document.biospecimen.currentField.value = currentField;
            document.biospecimen.nextField.value = nextField;
            document.biospecimen.submit();
            
    }
    function submitSmartform( aURL, value1, value2 ){
            var next = getNextField();
    
            if ((aURL == '') || (value1 == '') || (value2 == ''))
            {
               document.smartform.<xsl:value-of select="$currentField" />.value = ' ';
               document.smartform.change_next.value = '';
               document.smartform.change_next.name = next;
               document.smartform.submit();
            }
            else
            {
               document.smartform.action.value += aURL;
               document.smartform.<xsl:value-of select="$currentField" />.value = value1;
               document.smartform.change_next.value = value2;
               document.smartform.change_next.name = next;
               document.smartform.submit();
            }
            
    }
    </script>
    

    
    
    <form name="smartform" action="{$smartformChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformChannelTabOrder}" method="post">
        <xsl:for-each select="parameter">
            <xsl:variable name="name"><xsl:value-of select="name"/></xsl:variable>
            <xsl:variable name="value"><xsl:value-of select="value"/></xsl:variable>
            <input type="hidden" name="{$name}" value="{$value}"/>
        </xsl:for-each>
        <xsl:variable name="current">smartform_result_view</xsl:variable>
        <input type="hidden" name="{$currentField}" value=""/>
        <input type="hidden" name="change_next" value="" />
        <input type="hidden" name="current" value="smartform_result_view" />
        <input type="hidden" name="proteinCurrent" value="proteinPicker" />
        <input type="hidden" name="formLink" value="true"/>
    
    </form>
    
    
    
    <form name="biospecimen" action="{$baseActionURL}?current=search_protein" method="post">
        <xsl:for-each select="parameter">
            <xsl:variable name="name"><xsl:value-of select="name"/></xsl:variable>
            <xsl:variable name="value"><xsl:value-of select="value"/></xsl:variable>
            <input type="hidden" name="{$name}" value="{$value}"/>
        </xsl:for-each>
        
        <input type="hidden" name="formLink" value="true" />
        
        <input type="hidden" name="PROTEIN_intProteinKey" value = "" />
        <input type="hidden" name="PROTEIN_strName" value="" />
        <input type="hidden" name="PROTEIN_strLocalisation" value ="" />
        <input type="hidden" name="currentField" value ="" />
        <input type="hidden" name="nextField" value = "" />
        
    </form>
	<table width="100%">
		<tr>
			
                        <td class="uportal-label" align="right">
                            <input type="button" value="&lt; Back" class="uportal-button" onclick="javascript:submitSmartform('')"/>
                        </td>
		</tr>
                <tr>
			<td class="uportal-channel-subtitle">
			Select Protein<br/><hr/>
			</td>
                        
		</tr>
                
	</table>
        
        <!-- RESULTS -->
        
        <table width="100%">
            <tr>
                <td width="33%" class="uportal-label">
                    <a href="javascript:reOrder('PROTEIN_strName')">
                        <xsl:value-of select="PROTEIN_strNameDisplay"/>
                    </a>
                    
                </td>
                
                <td width="33%" class="uportal-label">
                    <a href="javascript:reOrder('PROTEIN_strGene')">
                        <xsl:value-of select="PROTEIN_strGeneDisplay"/>
                    </a>
                </td>
                
                <td width="33%" class="uportal-label">
                    <a href="javascript:reOrder('PROTEIN_strLocalisation')">
                        <xsl:value-of select="PROTEIN_strLocalisationDisplay"/>
                    </a>
                </td>
                

            </tr>
            <tr>
            <td colspan="3"> <hr/></td>
            </tr>
            <xsl:variable name="lastAction"><xsl:value-of select="lastAction" /></xsl:variable>
            <xsl:variable name="BIOANALYSIS_strAnalysis"><xsl:value-of select="BIOANALYSIS_strAnalysis" /></xsl:variable>

            <xsl:for-each select="protein">
            
            <xsl:variable name="PROTEIN_intProteinKey"><xsl:value-of select="PROTEIN_intProteinKey" /></xsl:variable>
            <xsl:variable name="PROTEIN_strLocalisation"><xsl:value-of select="PROTEIN_strLocalisation" /></xsl:variable>
            <xsl:variable name="PROTEIN_strName"><xsl:value-of select="PROTEIN_strName" /></xsl:variable>
            <xsl:variable name="PROTEIN_strGene"><xsl:value-of select="PROTEIN_strGene" /></xsl:variable>
                <tr>
                    
                    <td width="33%" class="uportal-label">
                        
                     <xsl:choose>
                     <xsl:when test="$proteinMode = 'SEARCH_PROTEIN'">
                            <!-- <xsl:if test="count(/body/biospecimen/patient_details) &gt; 0"> -->
                                
                                <xsl:variable name="PATIENT_intInternalPatientID"><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_intInternalPatientID"/></xsl:variable>
                                
                                <!-- <a href="javascript:submitBiospecimen('&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;PROTEIN_intProteinKey={$PROTEIN_intProteinKey}&amp;PROTEIN_strName={$PROTEIN_strName}&amp;PROTEIN_strGene={$PROTEIN_strGene}&amp;PROTEIN_strLocalisation={$PROTEIN_strLocalisation}&amp;currentField={$currentField}&amp;nextField={$nextField}')">
                                    <xsl:value-of select="PROTEIN_strName"/>
                                </a> -->
                                <a href="javascript:submitSmartform('&amp;formLink=true&amp;PROTEIN_intProteinKey={$PROTEIN_intProteinKey}&amp;{$currentField}={$PROTEIN_strName}&amp;{$nextField}={$PROTEIN_strGene}', '{$PROTEIN_strName}', '{$PROTEIN_strGene}')"> 
                                    <xsl:value-of select="PROTEIN_strName"/>
                                </a>
                                
                                
                            <!-- </xsl:if> -->
                    </xsl:when>
                    <xsl:when test="$proteinMode = 'SEARCH_ANTIBODY'">
                             
                               
                                
                                <a href="javascript:submitBiospecimen('nothing', '{$PROTEIN_intProteinKey}', '{$PROTEIN_strName}', '{$PROTEIN_strGene}', '{$PROTEIN_strLocalisation}', '{$currentField}', '{$nextField}')">
                                     <xsl:value-of select="PROTEIN_strName"/>
                                </a>
                            
                    </xsl:when>        
                     </xsl:choose>
                        
                    </td>
                    
                    
                    <!-- gene name column -->
                    <td width="33%" class="uportal-label">
                        <xsl:if test="string-length( $lastAction ) = 0">
                            <xsl:if test="count(/body/biospecimen/patient_details) &gt; 0">
                                
                                <xsl:variable name="PATIENT_intInternalPatientID"><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_intInternalPatientID"/></xsl:variable>
                                <a href="{$baseActionURL}?current=search_protein&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;PROTEIN_intProteinKey={$PROTEIN_intProteinKey}&amp;PROTEIN_strName={$PROTEIN_strName}&amp;PROTEIN_strGene={$PROTEIN_strGene}&amp;PROTEIN_strLocalisation={$PROTEIN_strLocalisation}">
                                     <xsl:value-of select="PROTEIN_strGene"/>
                                </a>
                            </xsl:if>
                             
                            <xsl:if test="count(/body/biospecimen/patient_details) = 0">
                                
                                <a href="{$baseActionURL}?current=search_protein&amp;PROTEIN_intProteinKey={$PROTEIN_intProteinKey}&amp;PROTEIN_strName={$PROTEIN_strName}&amp;PROTEIN_strGene={$PROTEIN_strGene}&amp;PROTEIN_strLocalisation={$PROTEIN_strLocalisation}">
                                     <xsl:value-of select="PROTEIN_strGene"/>
                                </a>
                            </xsl:if>
                             
                        </xsl:if>
                        <xsl:if test="string-length( $lastAction ) > 0">
                        
                            <xsl:if test="count(/body/biospecimen/patient_details) &gt; 0">
                                
                                <xsl:variable name="PATIENT_intInternalPatientID"><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_intInternalPatientID"/></xsl:variable>
                                <a href="{$baseActionURL}?current=analysis&amp;proteinSelected=true&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;{$lastAction}=true&amp;BIOANALYSIS_strAnalysis={$BIOANALYSIS_strAnalysis}&amp;PROTEIN_intProteinKey={$PROTEIN_intProteinKey}&amp;PROTEIN_strName={$PROTEIN_strName}&amp;PROTEIN_strGene={$PROTEIN_strGene}&amp;PROTEIN_strLocalisation={$PROTEIN_strLocalisation}">
                                     <xsl:value-of select="PROTEIN_strGene"/>
                                </a>
                            </xsl:if>
                            
                            <xsl:if test="count(/body/biospecimen/patient_details) = 0">
                                
                                <a href="{$baseActionURL}?current=analysis&amp;proteinSelected=true&amp;{$lastAction}=true&amp;BIOANALYSIS_strAnalysis={$BIOANALYSIS_strAnalysis}&amp;PROTEIN_intProteinKey={$PROTEIN_intProteinKey}&amp;PROTEIN_strName={$PROTEIN_strName}&amp;PROTEIN_strGene={$PROTEIN_strGene}&amp;PROTEIN_strLocalisation={$PROTEIN_strLocalisation}">
                                     <xsl:value-of select="PROTEIN_strGene"/>
                                </a>
                            </xsl:if>
                        </xsl:if>    
                    </td>
                    
                    <!-- localisation column -->
                    <td width="33%" class="uportal-label">
                        <xsl:if test="string-length( $lastAction ) = 0">
                            <xsl:if test="count(/body/biospecimen/patient_details) &gt; 0">
                                
                                <xsl:variable name="PATIENT_intInternalPatientID"><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_intInternalPatientID"/></xsl:variable>
                                <a href="javascript:submitBiospecimen('&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;PROTEIN_intProteinKey={$PROTEIN_intProteinKey}&amp;PROTEIN_strName={$PROTEIN_strName}&amp;PROTEIN_strGene={$PROTEIN_strGene}&amp;PROTEIN_strLocalisation={$PROTEIN_strLocalisation}&amp;currentField={$currentField}&amp;nextField={$nextField}')">
                                     <xsl:value-of select="PROTEIN_strLocalisation"/>
                                </a>
                            </xsl:if>
                             
                            <xsl:if test="count(/body/biospecimen/patient_details) = 0">
                                
                                <a href="javascript:submitBiospecimen('&amp;current=search_protein&amp;PROTEIN_intProteinKey={$PROTEIN_intProteinKey}&amp;PROTEIN_strName={$PROTEIN_strName}&amp;PROTEIN_strGene={$PROTEIN_strGene}&amp;PROTEIN_strLocalisation={$PROTEIN_strLocalisation}&amp;currentField={$currentField}&amp;nextField={$nextField}')">
                                     <xsl:value-of select="PROTEIN_strLocalisation"/>
                                </a>
                            </xsl:if>
                             
                        </xsl:if>
                        <xsl:if test="string-length( $lastAction ) > 0">
                         
                            <!-- xsl:if test="count(/body/biospecimen/patient_details) &gt; 0">
                                
                                <xsl:variable name="PATIENT_intInternalPatientID"><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_intInternalPatientID"/></xsl:variable>
                                    <!- - <a href="javascript:submitSmartform('&amp;BIOSPECIMEN_intBiospecimenID={$BIOSPECIMEN_intBiospecimenID}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;{$lastAction}=true&amp;BIOANALYSIS_strAnalysis={$BIOANALYSIS_strAnalysis}&amp;BIOSPECIMEN_intBiospecimenID={$BIOSPECIMEN_intBiospecimenID}&amp;PROTEIN_intProteinKey={$PROTEIN_intProteinKey}&amp;PROTEIN_strName={$PROTEIN_strName}&amp;PROTEIN_strGene={$PROTEIN_strGene}&amp;PROTEIN_strLocalisation={$PROTEIN_strLocalisation}')"> - ->
                                    <a href="javascript:submitSmartform('&amp;formLink=true&amp;{$currentField}={$PROTEIN_strName}&amp;{$nextField}={$PROTEIN_strGene}')"> 
                                    <xsl:value-of select="PROTEIN_strLocalisation"/>
                                </a>
                            </xsl:if>
                            
                            <xsl:if test="count(/body/biospecimen/patient_details) = 0">
                                
                                <!- - <a href="javascript:submitSmartform('&amp;formLink=true&amp;{$lastAction}=true&amp;BIOSPECIMEN_intBiospecimenID={$BIOSPECIMEN_intBiospecimenID}&amp;BIOANALYSIS_strAnalysis={$BIOANALYSIS_strAnalysis}&amp;BIOSPECIMEN_intBiospecimenID={$BIOSPECIMEN_intBiospecimenID}&amp;PROTEIN_intProteinKey={$PROTEIN_intProteinKey}&amp;PROTEIN_strGene={$PROTEIN_strGene}&amp;PROTEIN_strName={$PROTEIN_strName}&amp;PROTEIN_strGene={$PROTEIN_strGene}&amp;PROTEIN_strLocalisation={$PROTEIN_strLocalisation}')"> - ->
                                <a href="javascript:submitSmartform('&amp;formLink=true&amp;{$currentField}={$PROTEIN_strName}&amp;{$nextField}={$PROTEIN_strGene}')"> 
                                     <xsl:value-of select="PROTEIN_strLocalisation"/>
                                </a>
                            </xsl:if>
                            -->
                            <a href="javascript:submitSmartform('&amp;formLink=true&amp;{$currentField}={$PROTEIN_strName}&amp;{$nextField}={$PROTEIN_strGene}')"> 
                                     <xsl:value-of select="PROTEIN_strLocalisation"/>
                            </a>
                        </xsl:if>    
                    </td>
                    
                    
                    
                    
                    
                </tr>
            </xsl:for-each>
        
        </table>
        
        
        <form name="reOrder" action="{$baseActionURL}?current=search_protein" method="post">
           <xsl:for-each select="parameter">  
              <xsl:variable name="name"><xsl:value-of select="name"/></xsl:variable>
              <xsl:variable name="value"><xsl:value-of select="value"/></xsl:variable>
              <input type="hidden" name="{$name}" value="{$value}"/>
           </xsl:for-each>
           
           <input type="hidden" name="orderBy" value="" />
           <input type="hidden" name="PATIENT_intInternalPatientID" value="{$PATIENT_intInternalPatientID}" />
           <input type="hidden" name="currentField" value="{$currentField}" />
           <input type="hidden" name="nextField" value="{$nextField}" />
        
        </form>
        
        
        <!-- PAGING FORM -->
        <form name="searchForm" action="{$baseActionURL}?current=search_protein" method="post">
                
           <xsl:for-each select="parameter">
              <xsl:variable name="name"><xsl:value-of select="name"/></xsl:variable>
              <xsl:variable name="value"><xsl:value-of select="value"/></xsl:variable>
              <input type="hidden" name="{$name}" value="{$value}"/>
           </xsl:for-each>
              <xsl:variable name="current">smartform_result_view</xsl:variable>
              <input type="hidden" name="{$currentField}" value=""/>
              <input type="hidden" name="change_next" value="" />
              <input type="hidden" name="current" value="smartform_result_view" />
              <input type="hidden" name="proteinCurrent" value="proteinPicker" />
              <input type="hidden" name="formLink" value="true"/>
        
        
        
        
        
        
        
                    <table width="100%">
                        <tr>
                            <td colspan="14" class="uportal-channel-subtitle">
                                <hr/>
                            </td>
                        </tr>

                        <tr>          
                    
                            <td width="5%" class="uportal-label">
                                page:
                            </td>

                            <td width="3%" class="uportal-label">
                                <input type="submit" name="ptprevious" value="&lt;" tabindex="1" class="uportal-button"  onclick="javascript:submitNewPage('-1')"/>
                            </td>

                            <td width="5%" class="uportal-label">
				<input type="text" name="currentPage" size="4" tabindex="2" value="{$intCurrentPage}" align="right" class="uportal-input-text" />
                            </td>

                            <td width="6%" class="uportal-label">
                                of <xsl:value-of select="noOfPages" />
                            </td>
                            
                            <td width="3%" class="uportal-label">
                                <input type="button" name="ptnext" value="&gt;" tabindex="3" class="uportal-button" onclick="javascript:submitNewPage('1')"/>
                            </td>

                            <td width="4%" class="uportal-label">
                                <input type="submit" name="ptgo" value="Go" tabindex="4" class="uportal-button" />
                            </td>                           

                            <td width="5%"></td>
                            
                            <td width="7%" class="uportal-label">
                                Display
                            </td>

                            <td width="5%" class="uportal-label">
				<input type="text" name="noOfRecords" size="4" tabindex="5" value="{$intRecordPerPage}" align="right" class="uportal-input-text" />
                            </td>

                            <td width="15%" class="uportal-label">
                                records at a time
                            </td>

                            <td width="3%" class="uportal-label">
                                <input type="submit" name="ptset" value="Set" tabindex="6" class="uportal-button" />
                                
                                <input type="hidden" name="PATIENT_intInternalPatientID" value="{$PATIENT_intInternalPatientID}"/>
                            </td>

                            <td width="10%"></td>

                            <td width="10%" class="uportal-label">
                            
                            </td>

                            <td width="13%" ></td>
                        
                        
                        </tr>
                    </table>
                    
        </form>
        
        <table width="100%">
            <tr>
                <td class="uportal-label">
                    <hr/><br/>
                        <input type="button" value="&lt; Back" tabindex="42" class="uportal-button" onclick="javascript:submitSmartform('')"/>
                    
		</td>
            </tr>
        </table>
        
    </xsl:template>

      
       
 
</xsl:stylesheet>
