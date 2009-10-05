<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./report_menu.xsl"/>
    <xsl:include href="./report_list.xsl"/>
    
    <xsl:output method="html" indent="no" />
    
    
    
    <xsl:template match="report">
        <xsl:param name="REPORT_intParentKey"><xsl:value-of select="REPORT_intParentKey" /></xsl:param>
        <xsl:param name="REPORT_strReportName"><xsl:value-of select="REPORT_strReportName" /></xsl:param>
        <xsl:param name="REPORT_strOLAPSchemaFile"><xsl:value-of select="REPORT_strOLAPSchemaFile" /></xsl:param>
        <xsl:param name="varSelParamType"><xsl:value-of select="select_param_type"/></xsl:param>                        
        <xsl:param name="varSelParamName"><xsl:value-of select="select_param_name"/></xsl:param>     
        
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
           {
              alert('A name for the parameter has not been entered');
           }   
           else
           {
              if (paramType.value == "")
              {
                 alert('A valid type for the parameter has not been selected');
              }   
              else
              {
                 if (paramSchemaName.value == "")
                 {
                    alert('A valid name for the schema has not been entered');
                 }   
                 else
                 {
                    document.frm_reportedit.strParameterEditing.value = "add";            
                    // Reset the selected parameter after the parameter has been added
                    document.frm_reportedit.REPORT_selectedParameter.value = "";
                    selectAllParameters ();
                    document.frm_reportedit.submit();
                 }
              }                
           }           
        }
        
        
        // remove report parameters
        function removeParameter ()
        {
           var paramList = document.forms[0].REPORT_userParameters;
           paramList.options[paramList.selectedIndex] = null;
           // After removal, clear the parameter fields
           document.forms[0].REPORTPARAMETERS_strName.value = "";
           document.forms[0].REPORTPARAMETERS_strSchemaName.value = "";
           document.forms[0].REPORTPARAMETERS_strAllowOperator.checked = false;
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

        // submit edited parameters
	function submitParameters ()
        {
            document.frm_reportedit.REPORT_selectedParameter.value = document.forms[0].REPORT_userParameters.value;
            selectAllParameters ();
            document.frm_reportedit.submit();
        }
        
        
        // submit on selection of a new OLAP schema
        function changeOLAPSchema()
        {
            selectAllParameters ();
            document.frm_reportedit.submit();
        }
        </script>


   
        <table width="100%">
            <tr>
                <td class="uportal-channel-subtitle">
                    Edit report<br/><hr/>
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

        <form name="frm_reportedit" action="{$baseActionURL}?current=report_edit" method="post" enctype="multipart/form-data">
           <table width="100%">
            <tr>
                <td width="20%" class="uportal-label">
                    Category name:
                </td>
                <td width="80%">
                    <select name="REPORT_intParentKey" tabindex="1" class="uportal-input-text">
                        <xsl:for-each select="search_report_category">
                            <xsl:variable name="varReportCategoryKey"><xsl:value-of select="REPORTCATEGORY_intReportCategoryKey"/></xsl:variable>
                            <option>
                                <xsl:attribute name="value">
                                    <xsl:value-of select="REPORTCATEGORY_intReportCategoryKey" />
                                </xsl:attribute>

                                <xsl:if test="$REPORT_intParentKey=$varReportCategoryKey">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>

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
                        <input type="hidden" name="REPORT_strOLAPSchemaFile" value="{$REPORT_strOLAPSchemaFile}" class="uportal-label"/> 
                        <xsl:value-of select="REPORT_strOLAPSchemaFile" />
                        </td>
                        <td width="20%">
                        <input type="file" name="REPORTOLAPSCHEMA_strFileName" tabindex="2" class="uportal-input-text" onChange="javascript:changeOLAPSchema()" />
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
                    <input type="text" name="REPORT_strReportName" value="{$REPORT_strReportName}" size="61" tabindex="3" class="uportal-input-text" />
                </td>
            </tr>
            
            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORT_strReportQueryDisplay" />:
                </td>
                <td width="80%">
                    <textarea name="REPORT_strReportQuery" rows="6" cols="60" tabindex="4" class="uportal-input-text" >
                        <xsl:value-of select="REPORT_strReportQuery"/>
                    </textarea>    
                </td>
            </tr>
           
            
            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORT_strReportDescDisplay" />:
                </td>
                <td width="80%">
                    <textarea name="REPORT_strReportDesc" rows="4" cols="60" tabindex="5" class="uportal-input-text">
                        <xsl:value-of select="REPORT_strReportDesc"/>
                    </textarea>
                </td>
            </tr>
            
            
            
            <!-- parameters -->
            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORTPARAMETERS_strNameDisplay" />:
                </td>
                <td width="80%">
                    <input type="text" name="REPORTPARAMETERS_strName" size="61" tabindex="6" class="uportal-input-text" >
                        <xsl:attribute name="value">
                                <xsl:value-of select="select_param_name" />
                        </xsl:attribute>
                    </input>
                </td>
            
            
            </tr>
            
            <tr>
                <td width="20%" class="uportal-label">
                    <xsl:value-of select="REPORTPARAMETERS_strSchemaNameDisplay" />:
                </td>
                <td width="80%">
                    <input type="text" name="REPORTPARAMETERS_strSchemaName" size="61" tabindex="7" class="uportal-input-text" >
                        <xsl:attribute name="value">
                                <xsl:value-of select="select_param_schema" />
                        </xsl:attribute>
                    </input> 
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
                                    <xsl:variable name="varParameterTypeValue"><xsl:value-of select="." /></xsl:variable>
                                    
                                    <xsl:attribute name="value">
                                    <xsl:value-of select="." />
                                    </xsl:attribute> 

                                    <xsl:if test="$varParameterTypeValue=$varSelParamType">
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
                  <input TYPE="checkbox" NAME="REPORTPARAMETERS_strAllowOperator" tabindex="9">
                  <xsl:variable name="varAllowOperator"><xsl:value-of select="select_allow_operator"/></xsl:variable>
                  <xsl:if test="$varAllowOperator = 'Y'">
                      <xsl:attribute name="checked">true</xsl:attribute>
                  </xsl:if>
                  </input>
                  <font class="uportal-label">                      
                  <xsl:value-of select="REPORTPARAMETERS_strAllowOperatorDisplay" />
                  </font>
                  
                  <BR />
                  <input type="button" name="add" value="Add/Edit Parameter" tabindex="10" class="uportal-button" onclick="javascript:addParameter()" /><BR />
               </td>
            
            
            
            </tr>
            
            
            <tr>
               <td width="20%" class="uportal-label">
                   Parameters Added :
               </td>
               
               <td width="80%">
                   
                   <Select name="REPORT_userParameters" Size="6" width="120" multiple="multiple" tabindex="11" class="uportal-input-text" onChange="javascript:submitParameters()" >  
                       <!--If busy with an edit display the report parameters for the edited report-->
                       <xsl:for-each select="REPORTPARAMETERS_list">
                           <xsl:variable name="varParameterNameValue"><xsl:value-of select="ViewREPORTPARAMETERS_strName"/></xsl:variable>
                           <xsl:variable name="pValue"><xsl:value-of select="ViewREPORTPARAMETERS_strName"/>:<xsl:value-of select="ViewREPORTPARAMETERS_strSchemaName"/>:<xsl:value-of select="ViewREPORTPARAMETERS_strType"/>:<xsl:value-of select="ViewREPORTPARAMETERS_strAllowOperator"/></xsl:variable>
                               <option value="{$pValue}">
                                   <xsl:if test="$varParameterNameValue=$varSelParamName">
                                       <xsl:attribute name="selected">true</xsl:attribute> 
                                   </xsl:if>
                                   <xsl:value-of select="ViewREPORTPARAMETERS_strName"/>:<xsl:value-of select="ViewREPORTPARAMETERS_strSchemaName"/>:<xsl:value-of select="ViewREPORTPARAMETERS_strType"/>:<xsl:value-of select="ViewREPORTPARAMETERS_strAllowOperator"/>
                               </option>                               
                       </xsl:for-each> 
                   </Select> 
                   <BR />
                   
                   
                   <input type="button" name="remove" value="Remove Parameter" tabindex="12" class="uportal-button" onclick="javascript:removeParameter()" />
                   
                   <!--Hidden parameter to notifiy whether the report parameters are being edited, used in javascript:submitParameters()-->
                   <input type="hidden" name="strParameterEditing" />
                   <input type="hidden" name="REPORT_selectedParameter" />
                   
                                                    
                </td>
               
            
            </tr>
            
            
                
        </table>
        
        <hr />
        <table width="100%">
            <tr>	
                <td width="20%">
                    <input type="submit" name="save" tabindex="13" value="Save" onclick="javascript:selectAllParameters()" class="uportal-button" />
                    <input type="button" name="delete" value="Delete" tabindex="14" class="uportal-button" onclick="javascript:confirmDelete('{$baseActionURL}?current=report_edit')" />
                    <input type="submit" name="cancel" value="Cancel" tabindex="15" class="uportal-button" />
                </td>
                <td width="80%"></td>
            </tr>
        </table>
        
        <input type="hidden" name="REPORTPARAMETERS_intReportkey"/>
        
        </form>
       
    </xsl:template>

</xsl:stylesheet>