<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./biospecimen_menu.xsl"/>


    <xsl:output method="html" indent="no" />
    <xsl:param name="smartformChannelURL">smartformChannelURL</xsl:param>
    <xsl:param name="smartformChannelTabOrder">smartformChannelTabOrder</xsl:param>
    <xsl:template match="proteinMode" />
    <xsl:template match="protein">
    
    
    <xsl:variable name="returnValue"><xsl:value-of select="returnValue" /></xsl:variable>
    <xsl:variable name="returnURL"><xsl:value-of select="returnURL" /></xsl:variable>
    <xsl:variable name="lastAction"><xsl:value-of select="lastAction" /></xsl:variable>
    <xsl:variable name="PROTEIN_strName"><xsl:value-of select="PROTEIN_strName" /></xsl:variable>
    <xsl:variable name="PROTEIN_intProteinKey"><xsl:value-of select="PROTEIN_intProteinKey" /></xsl:variable>
    <xsl:variable name="BIOSPECIMEN_intBiospecimenID"><xsl:value-of select="BIOSPECIMEN_intBiospecimenID" /></xsl:variable>
    <xsl:variable name="BIOANALYSIS_strAnalysis"><xsl:value-of select="BIOANALYSIS_strAnalysis" /></xsl:variable>
    <xsl:variable name="currentField"><xsl:value-of select="currentField"/></xsl:variable>
    <xsl:variable name="nextField"><xsl:value-of select="nextField"/></xsl:variable>
    <xsl:variable name="fromBatchAnalysis"><xsl:value-of select="fromBatchAnalysis"/></xsl:variable>

    
    <script language="javascript">
    
        function getNextField()
        {
           var currentField = '<xsl:value-of select="$currentField" />';
           var nextField = currentField.split('d')[1];
           nextField++;
           
           nextField = 'd' + nextField;
           alert ('nextField');
           return nextField;

        }
    
    
    function submitSmartform( aURL ){
            
    
            document.smartform.submit();
            
    }
    function submitBiospecimen( aURL ){
            document.biospecimen.action.value += aURL;
            document.biospecimen.submit();
            
    }
    
    function submittoBatchResultsGeneration ( value1, value2 ){
            
        document.batchSFGeneration.<xsl:value-of select="$currentField" />.value = value1;
        document.batchSFGeneration.<xsl:value-of select="$nextField" />.value = value2;    
        document.batchSFGeneration.submit();
            
    }    
    
    function submitSmartform( value1, value2 ){
            
            // var next = getNextField();
            
            document.smartform.<xsl:value-of select="$currentField" />.value = value1;
            document.smartform.<xsl:value-of select="$nextField" />.value = value2;
            
            // document.smartform.change_next.value = value2;
            
            // document.smartform.change_next.name = next;
            
            document.smartform.submit();
            
    }
    
    </script>
    <form name="smartform" action="{$smartformChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$smartformChannelTabOrder}" method="post">
        <xsl:for-each select="parameter">
            <xsl:variable name="name"><xsl:value-of select="name"/></xsl:variable>
            <xsl:variable name="value"><xsl:value-of select="value"/></xsl:variable>
            <input type="hidden" name="{$name}" value="{$value}"/>
        </xsl:for-each>
        <input type="hidden" name="{$currentField}" value=""/>
        <input type="hidden" name="{$nextField}" value="" />
        <input type="hidden" name="current" value="smartform_result_view" />
        <!-- <input type="hidden" name="proteinCurrent" value="proteinPicker" /> -->
        <input type="hidden" name="formLink" value="true"/>    
        
    </form>
    
    
    <form name="biospecimen" action="{$baseActionURL}?current=search_protein&amp;module=Picker" method="post">
        <xsl:for-each select="parameter">
            <xsl:variable name="name"><xsl:value-of select="name"/></xsl:variable>
            <xsl:variable name="value"><xsl:value-of select="value"/></xsl:variable>
            <input type="hidden" name="{$name}" value="{$value}"/>
        </xsl:for-each>
    
    </form>
    
    <form name="batchSFGeneration" action="{$baseActionURL}?module=BATCH_SFRESULTS_GENERATION&amp;action=update_form_link_data" method="post">
        <xsl:for-each select="parameter">
        
            <xsl:variable name="name"><xsl:value-of select="name"/></xsl:variable>
            <xsl:variable name="value"><xsl:value-of select="value"/></xsl:variable>
            <input type="hidden" name="{$name}" value="{$value}"/>
        </xsl:for-each>
        <input type="hidden" name="{$currentField}" value=""/>
        <input type="hidden" name="{$nextField}" value="" />
    </form>    
     
        <xsl:if test="(string-length( $fromBatchAnalysis ) = 0)">	
        <table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Select Antibody
			</td>
                        <td class="uportal-label" align="right">
                            <xsl:if test="count(/body/biospecimen/patient_details) &gt; 0">
                                <!--input type="submit" value="View study results" tabindex="42" class="uportal-button" /-->
                                <xsl:variable name="PATIENT_intInternalPatientID"><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_intInternalPatientID"/></xsl:variable>
                                <!--input type="button" value="&lt; Back" tabindex="42" class="uportal-button" onclick="javascript:jumpTo('{$baseActionURL}?uP_root=root&amp;current=search_protein&amp;BIOSPECIMEN_intBiospecimenID={$BIOSPECIMEN_intBiospecimenID}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}')"/-->
                                <input type="button" value="&lt; Back" class="uportal-button" onclick="javascript:submitBiospecimen('&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;currentField={$currentField}&amp;nextField={$nextField}&amp;module=Picker')"/>
                            </xsl:if>
                            <xsl:if test="count(/body/biospecimen/patient_details) = 0">
                                <!--input type="submit" value="View study results" tabindex="42" class="uportal-button" /-->
                                <input type="button" value="&lt; Back" class="uportal-button" onclick="javascript:submitBiospecimen('&amp;currentField={$currentField}&amp;nextField={$nextField}&amp;module=Picker')"/>
                            </xsl:if>  
                        </td>
		</tr>
                <tr>
                <td colspan="2">
                    <hr/>
                </td>
                </tr>
	</table>
        </xsl:if>
        
        <!-- RESULTS -->
        
        <table width="100%">
            <tr>
                <td width="50%" class="uportal-label">
                    
                        <xsl:value-of select="ANTIBODY_strNameDisplay"/>: &#160;
                        <xsl:value-of select="PROTEIN_strName"/>&#160;
                        (<xsl:value-of select="PROTEIN_strLocalisation"/>)
                                        
                </td>
                
            </tr>
            <tr>
                <td><hr/>
                </td>
            </tr>
        
            <xsl:for-each select="antibody">
            <xsl:variable name="ANTIBODY_intProteinKey"><xsl:value-of select="ANTIBODY_intProteinKey" /></xsl:variable>
            <xsl:variable name="ANTIBODY_intAntibodyKey"><xsl:value-of select="ANTIBODY_intAntibodyKey" /></xsl:variable>
            <xsl:variable name="ANTIBODY_strName"><xsl:value-of select="ANTIBODY_strName" /></xsl:variable>
            
                <tr>
                    <td width="50%" class="uportal-label">
                            <!--xsl:if test="count(/body/biospecimen/patient_details) &gt; 0">
                                
                                <xsl:variable name="PATIENT_intInternalPatientID"><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_intInternalPatientID"/></xsl:variable>
                                <a href="javascript:submitSmartform('&amp;formLink=true&amp;{$currentField}={$PROTEIN_strName}&amp;{$nextField}={$PROTEIN_strGene}')"> 
                                    <xsl:value-of select="ANTIBODY_strName"/>
                                </a>
                            </xsl:if>
                             
                            <xsl:if test="count(/body/biospecimen/patient_details) = 0">
                                
                                <a href="javascript:submitSmartform('&amp;formLink=true&amp;{$currentField}={$PROTEIN_strName}&amp;{$nextField}={$PROTEIN_strGene}')"> 
                                    <xsl:value-of select="ANTIBODY_strName"/>
                                </a>
                            </xsl:if-->
                    <xsl:choose>

                    <xsl:when test="(string-length( $fromBatchAnalysis ) > 0)">                        
                    
                        <a href="javascript:submittoBatchResultsGeneration('{$PROTEIN_strName}', '{$ANTIBODY_strName}')"> 
                                <xsl:value-of select="ANTIBODY_strName"/>
                        </a>
                    </xsl:when>
                    <xsl:otherwise>
                        <a href="javascript:submitSmartform('{$PROTEIN_strName}', '{$ANTIBODY_strName}')"> 
                                <xsl:value-of select="ANTIBODY_strName"/>
                        </a>
                    </xsl:otherwise>
                    </xsl:choose>

                    </td>
                    
                </tr> 
            </xsl:for-each>
        
        </table>
        <xsl:if test="(string-length( $fromBatchAnalysis ) = 0)">
        <table width="100%">
            <tr>
                <td class="uportal-label">
                    <hr/><br/>

                    <xsl:if test="count(/body/biospecimen/patient_details) &gt; 0">
                        <!--input type="submit" value="View study results" tabindex="42" class="uportal-button" /-->
                        <xsl:variable name="PATIENT_intInternalPatientID"><xsl:value-of select="/body/biospecimen/patient_details/PATIENT_intInternalPatientID"/></xsl:variable>
                        <!--input type="button" value="&lt; Back" tabindex="42" class="uportal-button" onclick="javascript:jumpTo('{$baseActionURL}?uP_root=root&amp;current=search_protein&amp;BIOSPECIMEN_intBiospecimenID={$BIOSPECIMEN_intBiospecimenID}&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}')"/-->
                        <input type="button" value="&lt; Back" class="uportal-button" onclick="javascript:submitBiospecimen('&amp;PATIENT_intInternalPatientID={$PATIENT_intInternalPatientID}&amp;currentField={$currentField}&amp;nextField={$nextField}&amp;module=Picker')"/>
                    </xsl:if>
                    <xsl:if test="count(/body/biospecimen/patient_details) = 0">
                        <!--input type="submit" value="View study results" tabindex="42" class="uportal-button" /-->
                        <input type="button" value="&lt; Back" class="uportal-button" onclick="javascript:submitBiospecimen('&amp;currentField={$currentField}&amp;nextField={$nextField}&amp;module=Picker')"/>
                    </xsl:if>  
                         
		</td>
            </tr>
        </table>
        </xsl:if>
        
       
        
    </xsl:template>

      
       
 
</xsl:stylesheet>
