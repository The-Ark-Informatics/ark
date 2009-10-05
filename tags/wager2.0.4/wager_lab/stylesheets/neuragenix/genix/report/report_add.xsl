<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./report_menu.xsl"/>
    <xsl:include href="./report_list.xsl"/>
    
    <xsl:output method="html" indent="no" />
    
    
    
    <xsl:template match="report">
        <xsl:param name="REPORT_strOLAPSchemaFile"><xsl:value-of select="REPORT_strOLAPSchemaFile"/></xsl:param>
        <script language="javascript" >
        // add new report parameters
	function addParameter ()
        {
           // text area
           var paramName = document.forms[0].REPORTPARAMETERS_strName;
           var paramSchemaName = document.forms[0].REPORTPARAMETERS_strSchemaName;
           
           // drop down
           var paramType = document.forms[0].REPORTPARAMETERS_strType;
           
           // drop down
           //var paramConnector = document.forms[0].REPORTPARAMETERS_strOperator;
           
           // operator allowed
           var paramOperatorAllowed;
                      
           if (document.forms[0].REPORTPARAMETERS_strAllowOperator.checked == false)
           {
               paramOperatorAllowed = "N";               
           }
           else
           {
               paramOperatorAllowed = "Y";
           }

           
           // selection list
           var paramList = document.forms[0].REPORT_userParameters;
           
           if (paramName.value == "")
              alert('A name for the parameter has not been entered');
           else
              if (paramType.value == "")
                 alert('A valid type for the parameter has not been selected');
              else
                 if (paramSchemaName.value == "")
                    alert('A valid name for the schema has not been entered');
                 else
                 {
                    var displayOption = paramName.value + ":" + paramSchemaName.value + ":" + paramType.value + ":" + paramOperatorAllowed;
                    paramList.options[paramList.length] = new Option(paramName.value + '(' + paramType.value +')', displayOption);
                 }              
           
        }
        
        // remove report parameters
        function removeParameter ()
        {
           var paramList = document.forms[0].REPORT_userParameters;
           paramList.options[paramList.selectedIndex] = null;
        }
        
        // select all report parameters in the add/edit report form
        function selectAllParameters ()
        {
           var paramList = document.forms[0].REPORT_userParameters;
           var i = 0;
           while(i &lt; paramList.length)
           {
              paramList.options[i].selected = true;
              i++;
           }
           
           return true;
        
        }

        </script>
                
        <table width="100%">
            <tr>
                <td class="uportal-channel-subtitle">
                    Add report<br/><hr/>
                </td>
            </tr>
        </table>

        <table width="100%">
            <tr>
                <td class="uportal-label">
                    <a href="{$baseActionURL}?current=report_category_add">Add new category</a>
                </td>
            </tr>
        </table>
        
        <table width="100%">
            <tr>
                <td class="neuragenix-form-required-text">
                    <xsl:value-of select="strErrorMessage" />
                </td>
            </tr>
        </table>

        <form name="frm_reportadd" action="{$baseActionURL}?current=report_add" method="post" enctype="multipart/form-data">
        <table width="100%">
            <tr>
                <td width="20%" class="uportal-label">
                    Category name:
                </td>
                <td width="80%">
                    <select name="REPORT_intParentKey" tabindex="1" class="uportal-input-text">
                        <xsl:for-each select="search_report_category">

                            <option>
                                <xsl:attribute name="value">
                                    <xsl:value-of select="REPORTCATEGORY_intReportCategoryKey" />
                                </xsl:attribute>

                                <xsl:value-of select="REPORTCATEGORY_strReportCategoryName" />
                            </option>
                        </xsl:for-each>
                    </select>
                </td>
            </tr>
            
           <!-- Browse for an OLAP schema-->
            <tr>                
                <td width="20%" class="uportal-label">
                    OLAP Schema:
                </td>
                <td width="80%">
                <table>
                    <tr >  
                        <td width="20%" class="uportal-label">
                        <xsl:value-of select="REPORT_strOLAPSchemaFile"/>
                        <input type="hidden" name="REPORT_strOLAPSchemaFile" value="{$REPORT_strOLAPSchemaFile}" />
                        </td>
                        <td width="20%">
                        <input type="file" name="REPORTOLAPSCHEMA_strFileName" tabindex="2"  class="uportal-input-text"/>
                        </td>
                        <td width="60%">
                        </td>
                    </tr>    
                 </table>   
                </td>
            </tr>

            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORT_strReportNameDisplay" />:
                </td>
                <td width="80%">
                    <input type="text" name="REPORT_strReportName" size="61" tabindex="3" class="uportal-input-text" />
                </td>
            </tr>
            
            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORT_strReportQueryDisplay" />:
                </td>
                <td width="80%">
                    <textarea name="REPORT_strReportQuery" rows="6" cols="60" tabindex="4" class="uportal-input-text" />
                </td>
            </tr>
           
            
            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORT_strReportDescDisplay" />:
                </td>
                <td width="80%">
                    <textarea name="REPORT_strReportDesc" rows="4" cols="60" tabindex="5" class="uportal-input-text" />
                </td>
            </tr>
            
            
            
            <!-- parameters -->
            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORTPARAMETERS_strNameDisplay" />:
                </td>
                <td width="80%">
                    <input type="text" name="REPORTPARAMETERS_strName" size="61" tabindex="6" class="uportal-input-text" />
                </td>
            
            
            </tr>
            
            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORTPARAMETERS_strSchemaNameDisplay" />:
                </td>
                <td width="80%">
                    <input type="text" name="REPORTPARAMETERS_strSchemaName" size="61" tabindex="7" class="uportal-input-text" />
                </td>
            </tr>
            
            
            
            
            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORTPARAMETERS_strTypeDisplay" />:
                </td>
                <td width="80%">
                    <select name="REPORTPARAMETERS_strType" tabindex="8" class="uportal-input-text">
                        <xsl:for-each select="REPORTPARAMETERS_strType">

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
               <td width="20%" class="uportal-label">
                  
               </td>
               
               <td width="80%">
                  <INPUT TYPE="checkbox" NAME="REPORTPARAMETERS_strAllowOperator" tabindex="9"><font class="uportal-label"><xsl:value-of select="REPORTPARAMETERS_strAllowOperatorDisplay" /></font></INPUT>
                  <BR />
                  <input type="button" name="add" value="Add Parameter" class="uportal-button" tabindex="10"  onclick="javascript:addParameter()" /><BR />
               </td>
            </tr>
            
            
            
            <!--
            
            
               
            <tr>
                 <td width="20%" class="uportal-label">
               
                 
                     <xsl:value-of select="REPORTPARAMETERS_strOperatorDisplay" /> : 
                 </td>
                 <td width="80%">
                    <select name="REPORTPARAMETERS_strOperator" tabindex="1" class="uportal-input-text">
                        <xsl:for-each select="REPORTPARAMETERS_strOperator">

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
            
            
            -->
                
            
        
            <tr>
               <td width="20%" class="uportal-label">
                   Parameters Added :
               </td>
               
               <td width="80%">

                   <Select name="REPORT_userParameters" Size="6" width="120" multiple="multiple" tabindex="11"  class="uportal-input-text" /><BR />
                   <input type="button" name="remove" value="Remove Parameter" tabindex="12" class="uportal-button" onclick="javascript:removeParameter()" />
               </td>
            
            </tr>
            
            
                
        </table>
        
        <hr />
        <table width="100%">
            <tr>	
                <td width="20%">
                    <input type="submit" name="save" tabindex="13" value="Save" onclick="javascript:selectAllParameters()" class="uportal-button" />
                    <input type="button" name="delete" value="Delete" tabindex="14" class="uportal-button" onclick="javascript:confirmDelete('{$baseActionURL}?current=report_add')" />
                    <input type="button" name="clear" value="Clear" tabindex="15" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=report_add')" />
                </td>
                <td width="80%"></td>
            </tr>
        </table>
        
        <input type="hidden" name="REPORTPARAMETERS_intReportkey"/>
        
        </form>
       
    </xsl:template>

</xsl:stylesheet>