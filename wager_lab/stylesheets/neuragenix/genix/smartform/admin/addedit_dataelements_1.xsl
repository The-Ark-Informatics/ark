<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : addedit_dataelements.xsl
    Copyright ? 2004 Neuragenix, Inc .  All rights reserved.
    Created on : 4 March 2004    
    Author     : Anita Balraj
    
    Modified on: 
    Author     : 
    Description: 
             
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./smartform_menu.xsl"/>
<xsl:include href="../../../common/common_btn_name.xsl"/>

    <xsl:param name="formParams">current=addedit_dataelements</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
      
    <xsl:template match="smartform">  
    <xsl:param name="strErrorMessage"><xsl:value-of select="strErrorMessage" /></xsl:param>
    <xsl:param name="SMARTFORM_intSmartformID"><xsl:value-of select="SMARTFORM_intSmartformID" /></xsl:param>
    <xsl:param name="SMARTFORM_strSmartformName"><xsl:value-of select="SMARTFORM_strSmartformName" /></xsl:param>        
    <xsl:param name="SMARTFORM_strSmartformDescription"><xsl:value-of select="SMARTFORM_strSmartformDescription" /></xsl:param>        
    <xsl:param name="DATAELEMENTPOOL_intDataElementPoolID"><xsl:value-of select="DATAELEMENTPOOL_intDataElementPoolID" /></xsl:param>    
    <xsl:param name="DATAELEMENTPOOL_strDataElementPoolName"><xsl:value-of select="DATAELEMENTPOOL_strDataElementPoolName" /></xsl:param>
    <xsl:param name="DATAELEMENTS_intDataElementID"><xsl:value-of select="DATAELEMENTS_intDataElementID" /></xsl:param>
    <xsl:param name="DATAELEMENTS_strDataElementName"><xsl:value-of select="DATAELEMENTS_strDataElementName" /></xsl:param>
    <xsl:param name="DATAELEMENTS_intDataElementType"><xsl:value-of select="DATAELEMENTS_intDataElementType" /></xsl:param>    
    <xsl:param name="DEPoolSelected"><xsl:value-of select="DEPoolSelected" /></xsl:param>    
    <xsl:param name="DATAELEMENTS_intDataElementIDselected"><xsl:value-of select="DATAELEMENTS_intDataElementIDselected" /></xsl:param>   
     
    <xsl:param name="DATAELEMENTS_intDataElementOrder"><xsl:value-of select="DATAELEMENTS_intDataElementOrder" /></xsl:param>    
    <xsl:param name="DATAELEMENTS_intDataElementRow"><xsl:value-of select="DATAELEMENTS_intDataElementRow" /></xsl:param>  
    <xsl:param name="DATAELEMENTS_intDataElementColumn"><xsl:value-of select="DATAELEMENTS_intDataElementColumn" /></xsl:param>
    <xsl:param name="DATAELEMENTS_intDataElementIntMin"><xsl:value-of select="DATAELEMENTS_intDataElementIntMin" /></xsl:param>
    <xsl:param name="DATAELEMENTS_intDataElementIntMax"><xsl:value-of select="DATAELEMENTS_intDataElementIntMax" /></xsl:param>
    <xsl:param name="DATAELEMENTS_dtDataElementDateMin"><xsl:value-of select="DATAELEMENTS_dtDataElementDateMin" /></xsl:param>
    <xsl:param name="DATAELEMENTS_dtDataElementDateMax"><xsl:value-of select="DATAELEMENTS_dtDataElementDateMax" /></xsl:param>
    <xsl:param name="DATAELEMENTS_strDataElementScript"><xsl:value-of select="DATAELEMENTS_strDataElementScript" /></xsl:param>
    <xsl:param name="DATAELEMENTS_strDataElementLookupType"><xsl:value-of select="DATAELEMENTS_strDataElementLookupType" /></xsl:param>
    <xsl:param name="DATAELEMENTS_strDataElementHelp"><xsl:value-of select="DATAELEMENTS_strDataElementHelp" /></xsl:param>
    <xsl:param name="DATAELEMENTS_strDataElementDefault"><xsl:value-of select="DATAELEMENTS_strDataElementDefault" /></xsl:param>
    <xsl:param name="DATAELEMENTS_intDataElementTypeSelected"><xsl:value-of select="DATAELEMENTS_intDataElementTypeSelected" /></xsl:param>   
    <xsl:param name="strMode"><xsl:value-of select="strMode" /></xsl:param> 
    <!-- Date -->
    <xsl:param name="DATAELEMENTS_dtDataElementDateMin_Year"><xsl:value-of select="DATAELEMENTS_dtDataElementDateMin_Year" /></xsl:param>   
    <xsl:param name="DATAELEMENTS_dtDataElementDateMax_Year"><xsl:value-of select="DATAELEMENTS_dtDataElementDateMax_Year" /></xsl:param>   
    <xsl:param name="DATAELEMENTS_dtDataElementDefaultDate_Year"><xsl:value-of select="DATAELEMENTS_dtDataElementDefaultDate_Year" /></xsl:param>    
    <xsl:param name="DATAELEMENTS_strDataElementDefaultWorkingDaysOperator"><xsl:value-of select="DATAELEMENTS_strDataElementDefaultWorkingDaysOperator" /></xsl:param>    
    <xsl:param name="DATAELEMENTS_intDataElementDefaultWorkingDaysNumber"><xsl:value-of select="DATAELEMENTS_intDataElementDefaultWorkingDaysNumber" /></xsl:param>    
    <xsl:param name="DATAELEMENTS_strDataElementDefaultWdOrDaysOption"><xsl:value-of select="DATAELEMENTS_strDataElementDefaultWdOrDaysOption" /></xsl:param>    
    <xsl:param name="DATAELEMENTS_strDataElementDefaultTodaysDate"><xsl:value-of select="DATAELEMENTS_strDataElementDefaultTodaysDate" /></xsl:param>
    <xsl:param name="rdDATAELEMENTS_dtDataElementDefaultDate"><xsl:value-of select="rdDATAELEMENTS_dtDataElementDefaultDate" /></xsl:param>  
    <xsl:param name="strRadioVal"><xsl:value-of select="strRadioVal" /></xsl:param>  
   
    
    <!-- System Lookup Type -->
    <xsl:param name="DELookupTypeSelected"><xsl:value-of select="DELookupTypeSelected" /></xsl:param>
    <!-- System Lookup Where -->
    <xsl:param name="SYSTEMLOOKUPWHERE_strConnector"><xsl:value-of select="SYSTEMLOOKUPWHERE_strConnector" /></xsl:param>  
    <xsl:param name="SYSTEMLOOKUPWHERE_strField"><xsl:value-of select="SYSTEMLOOKUPWHERE_strField" /></xsl:param>  
    <xsl:param name="SYSTEMLOOKUPWHERE_strOperator"><xsl:value-of select="SYSTEMLOOKUPWHERE_strOperator" /></xsl:param>  
    <xsl:param name="SYSTEMLOOKUPWHERE_strValue"><xsl:value-of select="SYSTEMLOOKUPWHERE_strValue" /></xsl:param>  
    <xsl:param name="DELookupFieldSelected"><xsl:value-of select="DELookupFieldSelected" /></xsl:param>  
    
    <!-- System Lookup Field -->
    <xsl:param name="SYSTEMLOOKUPFIELD_strInternalName"><xsl:value-of select="SYSTEMLOOKUPFIELD_strInternalName" /></xsl:param>  
    <xsl:param name="SYSTEMLOOKUPFIELD_strFieldOrder"><xsl:value-of select="SYSTEMLOOKUPFIELD_strFieldOrder" /></xsl:param>  
    
    <!-- Lookup Existing Smartform and DataElementID's -->
    <xsl:param name="DATAELEMENTS_intDataElementDefaultSmartformID"><xsl:value-of select="DATAELEMENTS_intDataElementDefaultSmartformID" /></xsl:param>  
    <xsl:param name="DATAELEMENTS_intDataElementDefaultDataElementID"><xsl:value-of select="DATAELEMENTS_intDataElementDefaultDataElementID" /></xsl:param>  
    <xsl:param name="DE_DefaultSmartformIDSelected"><xsl:value-of select="DE_DefaultSmartformIDSelected" /></xsl:param>  
    <xsl:param name="DE_DefaultDataElementIDSelected"><xsl:value-of select="DE_DefaultDataElementIDSelected" /></xsl:param>  
    <xsl:param name="SYSTEMLOOKUPWHERE_intDataElementLookupWhereID"><xsl:value-of select="SYSTEMLOOKUPWHERE_intDataElementLookupWhereID" /></xsl:param>  
    <xsl:param name="DynamicSmartform"><xsl:value-of select="DynamicSmartform" /></xsl:param>
    <!-- Lookup Display Fields 
    <xsl:param name="SYSTEMLOOKUPFIELD_strInternalName"><xsl:value-of select="SYSTEMLOOKUPFIELD_strInternalName" /></xsl:param>  
    <xsl:param name="SYSTEMLOOKUPFIELD_strFieldOrder"><xsl:value-of select="SYSTEMLOOKUPFIELD_strFieldOrder" /></xsl:param>  
    <xsl:param name="SYSTEMLOOKUPWHERE_intDataElementLookupWhereID"><xsl:value-of select="SYSTEMLOOKUPWHERE_intDataElementLookupWhereID" /></xsl:param>  -->
    <xsl:param name="DATAELEMENTS_intMandatory"><xsl:value-of select="DATAELEMENTS_intMandatory" /></xsl:param>
        
    <head>
    <script language="javascript">
    
    function validDecimal(str)
    {       
        if(document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementTypeSelected.value == "MONETARY")
        {
            if (str.indexOf('.') == -1) str += ".";
            var decNum = str.substring(str.indexOf('.')+1, str.length);
            if (decNum.length > 2)//key to change from 2 to 3,45 etc to restrict no of digits aftre decimal
            {
                alert("Please enter only 2 digits after decimal");
                return false;        
            }
            else
            {
              return true;
            }
        }
        else if(document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementTypeSelected.value == "NUMERIC")
        {
           if(!checkNumber(str))
            {
                alert("Please enter valid Data");
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
          return true;
        }        
        
    }
    
    

    function validDecimal()
    {       
        if(document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementIntMin)
        {
            if(document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementTypeSelected.value == "MONETARY")
            {
                var str = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementIntMin.value;
                var str1 = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementIntMax.value;
                if (str.indexOf('.') == -1) str += ".";
                if (str1.indexOf('.') == -1) str1 += ".";
                
                var decNum = str.substring(str.indexOf('.')+1, str.length);
                var decNum1 = str1.substring(str1.indexOf('.')+1, str1.length);
                
                if (decNum.length > 2)//key to change from 2 to 3,45 etc to restrict no of digits aftre decimal
                {
                    alert("Please enter only 2 digits after decimal");
                    return false;        
                }
                 
                else if (decNum1.length > 2)//key to change from 2 to 3,45 etc to restrict no of digits aftre decimal
                {
                    alert("Please enter only 2 digits after decimal");
                    return false;        
                }
                else if(!checkCommas(str))
                {
                    alert("Please enter the valid data in Min value field");
                    return false;        
                }
                else if(!checkCommas(str1))
                {
                    alert("Please enter the valid data in Max value field");
                    return false;
                }
                else
                {
                  return true;
                }            
                
            }
            else if(document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementTypeSelected.value == "NUMERIC")
            {
                
               var str = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementIntMin.value;
               var str1 = document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementIntMax.value;
               if(!checkNumber(str))
                {
                    alert("Please enter valid Data in Min Field");
                    return false;
                }
                else if(!checkNumber(str1))
                {
                    alert("Please enter valid Data in Max Field");
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else
            {
              return true;
            }    
        }
        else
        {
            return true;
        }
     }
   
     function checkCommas(str) 
     {
        var txtNumber = str;
        var searchex = ",";
        var rxSplit = new RegExp('([0-9])([0-9][0-9][0-9][,.])');
        var test = "";
        if(txtNumber.search(searchex) > 0)
        {
                var numsplit = txtNumber.split(searchex);
                var i = 0;
                var length = numsplit.length;
                do
                {
                    test += numsplit[i];
                    i++;
                }while(length > i);
                if(!checkNumber(test))
                {
                    return false;
                }
                var arrNumber = test.split('.');
                arrNumber[0] += '.';
                do 
                {
                    arrNumber[0] = arrNumber[0].replace(rxSplit, '$1,$2');
                } while (rxSplit.test(arrNumber[0]));
                if (arrNumber.length > 1) 
                {

                    test =  arrNumber.join('');
                }
                else 
                {

                    test = arrNumber[0].split('.')[0];
                 }
                  if(txtNumber == test)
                 {
                    return true;
                }
                else
                {
                    return false;
                }
        }
        else if(!checkNumber(txtNumber))
        {
            return false;
        }
        else
        {
            return true;
        }
     }
     
     function checkNumber(str)
    {
        var x=str;
        var anum=/(^\d+$)|(^\d+\.\d+$)/;
        if (anum.test(x))
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    
    </script>
    </head>
   
    <!--form name="anita" method="POST">
        <input type="hidden" name="url" value=""/>
    </form-->
      
    <form name="frmaddedit_dataelements" action="{$baseActionURL}?{$formParams}#frm1_anchor" method="post">
   
    <table width="100%">
        <tr>           
            
            <td width="15%" class="uportal-label">
                <input type="submit" name="addSmartform" tabindex="1" value="{$addBtnLabel}" class="uportal-button" />
                <input type="submit" name="searchSmartform" tabindex="2" value="{$searchBtnLabel}" class="uportal-button" />
            </td>
            
            <td width="80%" class="uportal-label">  
                <span class="uportal-channel-current-subtitle">Add/Edit DataElements</span>
               <!-- <a href="{$baseActionURL}?current=dataelement">DataElement |</a>
                <a href="{$baseActionURL}?current=option"> Option</a>    -->
            </td>
            
            <td width="5" class="uportal-label" align="right">
                <input type="submit" name="backtoBuildSmartform" tabindex= "10" value="{$backBtnLabel}" class="uportal-button" />
            </td>
        </tr>        
    </table>
    <hr/>   
    
    
    <table width="100%">
        <tr valign="top">
            <td width="15%">
                 <!-- First forms Hidden fields -->
                <input type="hidden" name="SMARTFORM_intSmartformID" value="{$SMARTFORM_intSmartformID}" />  <br></br>
                <input type="hidden" name="DEPoolSelected" value="{$DEPoolSelected}" />  <br></br>
                <input type="hidden" name="DATAELEMENTS_intDataElementIDselected" value="{$DATAELEMENTS_intDataElementIDselected}" />    
            </td>
                
            <td width="70%">
                 <a name="frm1_anchor"/>
                <table width="100%">                    
                     <tr>
                        <td colspan="2" class="neuragenix-form-required-text">
                            <xsl:value-of select="strErrorMessage" />
                        </td>
                    </tr>                 
                 </table>
                 
                 <table width="100%">
                    <tr>
                        <td width="30%" class="uportal-label">
                            Select the DataElement Pool:
                        </td>
                        <td width="50%">                        
                             <select name="DATAELEMENTPOOL_intDataElementPoolID" tabindex="1" class="uportal-input-text" onChange="document.frmaddedit_dataelements.submit();">
                             
                              <option value="null">--- Select ---</option> 
                                                                  
				<xsl:for-each select="buildsmartform_DataElementPool">
                                <xsl:variable name="DATAELEMENTPOOL_intDataElementPoolID"><xsl:value-of select="DATAELEMENTPOOL_intDataElementPoolID" /></xsl:variable>
                                <option>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="DATAELEMENTPOOL_intDataElementPoolID" />
                                        </xsl:attribute>                  
                                                    
                                        <xsl:if test="$DATAELEMENTPOOL_intDataElementPoolID=$DEPoolSelected">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                                            
                                        <xsl:value-of select="DATAELEMENTPOOL_strDataElementPoolName" /> 
                                 </option>                    
                                    
				</xsl:for-each>
                            </select>
                        </td>
                        <td width="20%" class="uportal-text">   
                            <a href="{$baseActionURL}?current=view_dataelementpool&amp;DATAELEMENTPOOL_intDataElementPoolID={$DEPoolSelected}&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}" >Edit DataElement Pool</a><br></br>
                            <a href="{$baseActionURL}?current=add_dataelementpool&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}" >Add DataElement Pool</a>
                        </td>
                    </tr> 
                       
                    <tr></tr>
                    
                </table>
                  <hr/>
                 <table width="100%">
		     <tr >			
			<td width="30%" class="uportal-label">
				DataElements:
			</td>
			
			<td width="50%"  align="left" class="uportal-label" id="neuragenix-form-row-input-input">
				<SELECT readonly = "true" Name="DATAELEMENTS_intDataElementID" Size="5" tabindex="2" class="uportal-input-text" onChange="document.frmaddedit_dataelements.submit();">
					<xsl:for-each select="buildsmartform_DataElements">
					<xsl:variable name="DATAELEMENTS_intDataElementID"><xsl:value-of select="DATAELEMENTS_intDataElementID" /></xsl:variable>
					<xsl:variable name="DATAELEMENTS_intDataElementType"><xsl:value-of select="DATAELEMENTS_intDataElementType" /></xsl:variable>
                                        <xsl:variable name="DATAELEMENTS_strDataElementName"><xsl:value-of select="DATAELEMENTS_strDataElementName" /></xsl:variable>

						<option>
							<xsl:if test="$DATAELEMENTS_intDataElementID = $DATAELEMENTS_intDataElementIDselected">
								<xsl:attribute name="selected">true</xsl:attribute> 
							</xsl:if>
							<xsl:attribute name="value"><xsl:value-of select="DATAELEMENTS_intDataElementID" /></xsl:attribute>                                                         
                                                        
							<xsl:choose> 
								<xsl:when test="$DATAELEMENTS_intDataElementType='COMMENT'"> 
									C: <xsl:value-of select="DATAELEMENTS_strDataElementName" />
								</xsl:when>
								<xsl:when test="$DATAELEMENTS_intDataElementType='TITLE'"> 
									T: <xsl:value-of select="DATAELEMENTS_strDataElementName" />
								</xsl:when>
								<xsl:when test="$DATAELEMENTS_intDataElementType='PAGEBREAK'"> 
									---- PAGE BREAK ----
								</xsl:when>
								<xsl:when test="$DATAELEMENTS_intDataElementType='SCRIPT'"> 
									S:  <xsl:value-of select="DATAELEMENTS_strDataElementName" />
								</xsl:when>
								<xsl:otherwise> 
									Q:  <xsl:value-of select="DATAELEMENTS_strDataElementName" />
								</xsl:otherwise> 
							</xsl:choose>
						</option>
                                                
					</xsl:for-each>
				</SELECT>
			</td>
			<td width="20%"  align="left">
                        
                          <xsl:if test="$DATAELEMENTS_intDataElementIDselected != 'null'"> 
				<table border="0">                                
                                    <tr>
                                        <td align="left" class="uportal-text">
                                        
                                            <a href="{$baseActionURL}?current=addedit_dataelements&amp;moveUp=true&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}">     
                                                 move up                                                     
                                           </a> 
                                            
					</td>
                                        <td align="left">                                        
                                        </td>
                                    </tr>                                    
                                    <tr>
                                        <td align="left" class="uportal-text">
                                            <!-- <a href="{$baseActionURL}?current=build_smartform&amp;deleteSFDE=true&amp;SMARTFORMTODATAELEMENTS_DEIdSelected={$SMARTFORMTODATAELEMENTS_DEIdSelected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}"> -->
                                            <a href="#" onclick="javascript:confirmDeleteDE('{$baseActionURL}?current=addedit_dataelements&amp;deleteDE=true&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}')">                                        
                                                    delete
                                            </a> 
                                        </td>
                                        <td align="left">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="left" class="uportal-text">
                                            <a href="{$baseActionURL}?current=addedit_dataelements&amp;moveDown=true&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}">                 
                          
                                                    move down
                                            </a>
                                        </td>
                                    </tr>
				</table>
                          </xsl:if>
			</td>
                    </tr>
                    <!--<tr></tr>
                    <tr></tr>	
                    <tr></tr> -->
                    <tr></tr>	
                </table>
                
                 </td>
                 <td width="15%"/>
                </tr>
                </table>
               
                </form>
                
                <form name="frmaddedit_dataelements1" action="{$baseActionURL}?{$formParams}#frm1_anchor" method="post">
                <xsl:variable name="SMARTFORM_intSmartformID"><xsl:value-of select="SMARTFORM_intSmartformID" /></xsl:variable>
                <xsl:variable name="addDataElement"><xsl:value-of select="addDataElement" /></xsl:variable>
                <xsl:variable name="addTitleComment"><xsl:value-of select="addTitleComment" /></xsl:variable>
                <xsl:variable name="addPageBreak"><xsl:value-of select="addPageBreak" /></xsl:variable>        
             
                              
                <table width="100%">
                <tr>
                <td width="15%"> 
                </td>
                <td width="70%">              
                
                <xsl:if test="$DEPoolSelected != 'null'">
                    <table width="100%">
                        <tr>
                            <td>
                                <input type="submit" name="addDataElement" tabindex="3" value="Add DataElement" class="uportal-button" />
                                <input type="submit" name="addTitleComment" tabindex="4" value="Add Title/Comment" class="uportal-button" />
                                <input type="submit" name="addPageBreak" tabindex="5" value="Add PageBreak" class="uportal-button" />                            
                                <!--<input type="submit" name="manageDataElementPool" value="Manage DataElement Pool" tabindex="8" class="uportal-button" /> -->
                            </td>
                        </tr>
                    </table>
                </xsl:if>
                
               <a name="frm2_anchor"/> 
              <!-- #############################   ADD TITLE/COMMENT ########################################################## -->             
             <xsl:if test="$DATAELEMENTS_intDataElementType != 'PAGEBREAK' or $strMode != 'addPageBreak'"> 
                  <xsl:if test="$strMode = 'newTitleClicked' or $DATAELEMENTS_intDataElementType = 'TITLE' or $DATAELEMENTS_intDataElementType = 'COMMENT'">
                  <hr/>
                  <table width="100%">
                        <tr>
                            <td width="100%" class="uportal-channel-table-header">
                                Add/Edit Titles and Comments
                            </td>
                        </tr>
                        <tr></tr>                    
                  </table> 
                  <table width="100%">
                        <tr>
                            <td width="20%" class="uportal-label">
                                <xsl:value-of select="DATAELEMENTS_intDataElementIDDisplay" />:
                            </td>                  
                            <td width ="80%" class="uportal-text">                        
                                 <!--<xsl:if test="string-length($DATAELEMENTS_intDataElementIDselected) = 0"> -->
                                <xsl:if test="$DATAELEMENTS_intDataElementIDselected = 'null'"> 
                                             System generated    
                                </xsl:if>
                                <xsl:if test="$DATAELEMENTS_intDataElementIDselected != 'null'">
                                            <xsl:value-of select="DATAELEMENTS_intDataElementIDselected" />
                                </xsl:if>                                     
                            </td>                                       
                        </tr>                     
                        <tr>
                            <td width="20%" class="uportal-label">
                                <xsl:value-of select="DATAELEMENTS_intDataElementTypeDisplay" />:
                            </td>
                            <td width="80%">                    
                                <select size="1" name="DATAELEMENTS_intDataElementType" tabindex="6" class="uportal-input-text">
                                            <option value="TITLE">
                                                    <xsl:if test="$DATAELEMENTS_intDataElementType = 'TITLE'">
                                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                                    </xsl:if>
                                                    TITLE
                                            </option>
                                            <option value="COMMENT">
                                                    <xsl:if test="$DATAELEMENTS_intDataElementType = 'COMMENT'">
                                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                                    </xsl:if>
                                                    COMMENT
                                            </option>					
                                    </select>                            
                            </td>
                        </tr>
                        <tr>
                            <td width="20%" class="uportal-label">
                               <xsl:value-of select="DATAELEMENTS_strDataElementNameDisplay" />:
                            </td>
                            <td width="80%">
                                <textarea name="DATAELEMENTS_strDataElementName" rows="2" cols="80" tabindex="7" class="uportal-input-text" >
                                    <xsl:value-of select="DATAELEMENTS_strDataElementName" />
                                </textarea>                            
                            </td>
                        </tr>
                        <tr>
                            <td width="20%" class="uportal-label">
                               <xsl:value-of select="DATAELEMENTS_strDataElementHelpDisplay" />:
                            </td>
                            <td width="80%">
                                <textarea name="DATAELEMENTS_strDataElementHelp" rows="2" cols="80" tabindex="8" class="uportal-input-text">
                                    <xsl:value-of select="DATAELEMENTS_strDataElementHelp" />
                                </textarea>                            
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                            </td>
                            <td width="80%">
                                <input type="submit" name="saveDataElement" tabindex="9" value="{$saveBtnLabel}" class="uportal-button" />
                               <!-- <input type="submit" name="deleteDataElement" tabindex="6" value="{$deleteBtnLabel}" class="uportal-button" /> -->
                                <xsl:if test="$strMode != 'newTitleClicked'">
                                    <input type="submit" name="duplicateDataElement" tabindex="10" value="Duplicate" class="uportal-button" />                   
         
                                </xsl:if>
                            </td>   
                        </tr>
                    </table>
                     <!--<hr/> -->
                  </xsl:if>
              </xsl:if>
              
              
              <xsl:if test="$addPageBreak = 'true'">
              
              </xsl:if>
             <!-- <xsl:if test="$addDataElement = 'true' or ($DATAELEMENTS_intDataElementIDselected != 'null' and ($DATAELEMENTS_intDataElementType != 'TITLE') or ($DATAELEMENTS_intDataElementType != 'COMMENT'))">  -->
             <!-- <xsl:when test="$blAddEditTitleComment = 'true' or $intQuestionType='101' or $intQuestionType='102'"> -->
             <!-- <xsl:if test="$DATAELEMENTS_intDataElementIDselected != 'null'">  -->
             
             
             
              <!-- #############################   ADD/EDIT DATAELEMENTS ########################################################## -->              
             <!--<xsl:if test="$DATAELEMENTS_intDataElementType != 'PAGEBREAK' or $strMode != 'addPageBreak'">-->
                      <xsl:if test="$strMode = 'newDEClicked' or $DATAELEMENTS_intDataElementType != 'TITLE' and $DATAELEMENTS_intDataElementType != 'COMMENT' and $DATAELEMENTS_intDataElementType != 'PAGEBREAK'">
                      <hr/>
                        <table width="100%">
                            <tr>
                                <td width="100%" class="uportal-channel-table-header">
                                    Add/Edit DataElements 
                                </td>
                            </tr>
                            <tr></tr>                    
                        </table>
                        <table width="100%">
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="DATAELEMENTS_intDataElementIDDisplay" />:
                                </td>                  
                                <td width ="80%" class="uportal-text">

                                    <!--<xsl:if test="string-length($DATAELEMENTS_intDataElementIDselected) = 0"> -->
                                   <xsl:if test="$DATAELEMENTS_intDataElementIDselected = 'null'"> 
                                                 System generated    
                                    </xsl:if>
                                    <xsl:if test="$DATAELEMENTS_intDataElementIDselected != 'null'">
                                                <xsl:value-of select="DATAELEMENTS_intDataElementIDselected" />
                                    </xsl:if>


                                </td>                                       
                            </tr>                     
                            <tr>
                                <td width="20%" class="uportal-label">
                                    <xsl:value-of select="DATAELEMENTS_intDataElementTypeDisplay" />:
                                </td>
                                <td width="80%">                    
                                    <select size="1" name="DATAELEMENTS_intDataElementType" tabindex="6" class="uportal-input-text" onChange="document.frmaddedit_dataelements1.submit();">
                                                <option value="TEXT">
                                                        <!-- <xsl:if test="$DATAELEMENTS_intDataElementType = 'TEXT'"> -->
                                                        <xsl:choose>
                                                        <xsl:when test="$DATAELEMENTS_intDataElementTypeSelected != 'null'">
                                                         <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'TEXT'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                         </xsl:if>
                                                        </xsl:when>
                                                        <xsl:when test="$DATAELEMENTS_intDataElementType = 'TEXT'">                                                
                                                                <xsl:attribute name="selected">true</xsl:attribute>                                                
                                                        </xsl:when>   
                                                        </xsl:choose>
                                                        TEXT
                                                </option>
                                                <option value="DROPDOWN">
                                                        <!--<xsl:if test="$DATAELEMENTS_intDataElementType = 'DROPDOWN'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if> -->
                                                        <xsl:choose>
                                                            <xsl:when test="$DATAELEMENTS_intDataElementTypeSelected != 'null'">
                                                                 <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'DROPDOWN'">
                                                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                                                 </xsl:if>
                                                            </xsl:when>
                                                            <xsl:when test="$DATAELEMENTS_intDataElementType = 'DROPDOWN'">                                            
            
                                                                 <xsl:attribute name="selected">true</xsl:attribute>                                                        
                                                            </xsl:when>   
                                                        </xsl:choose>
                                                        DROP DOWN
                                                </option>
                                                <option value="NUMERIC">
                                                        <xsl:choose>
                                                        <xsl:when test="$DATAELEMENTS_intDataElementTypeSelected != 'null'">
                                                         <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'NUMERIC'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                         </xsl:if>
                                                        </xsl:when>

                                                        <xsl:when test="$DATAELEMENTS_intDataElementType = 'NUMERIC'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:when>   
                                                        </xsl:choose>

                                                        <!--<xsl:if test="$DATAELEMENTS_intDataElementType = 'NUMERIC'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if> -->
                                                        NUMERIC
                                                </option>
                                                <option value="DATE">
                                                        <xsl:choose>
                                                        <xsl:when test="$DATAELEMENTS_intDataElementTypeSelected != 'null'">
                                                         <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'DATE'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                         </xsl:if>
                                                        </xsl:when>

                                                        <xsl:when test="$DATAELEMENTS_intDataElementType = 'DATE'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:when>   
                                                        </xsl:choose>
                                                        <!--<xsl:if test="$DATAELEMENTS_intDataElementType = 'DATE'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if> -->
                                                        DATE
                                                </option>
                                                <option value="SCRIPT">
                                                        <xsl:choose>
                                                        <xsl:when test="$DATAELEMENTS_intDataElementTypeSelected != 'null'">
                                                         <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'SCRIPT'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                         </xsl:if>
                                                        </xsl:when>

                                                        <xsl:when test="$DATAELEMENTS_intDataElementType = 'SCRIPT'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:when>   
                                                        </xsl:choose>

                                                        <!--<xsl:if test="$DATAELEMENTS_intDataElementType = 'SCRIPT'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if> -->
                                                        SCRIPT
                                                </option>
                                                <option value="SYSTEM LOOKUP">
                                                        <xsl:choose>
                                                        <xsl:when test="$DATAELEMENTS_intDataElementTypeSelected != 'null'">
                                                         <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'SYSTEM LOOKUP'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                         </xsl:if>
                                                        </xsl:when>

                                                        <xsl:when test="$DATAELEMENTS_intDataElementType = 'SYSTEM LOOKUP'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:when>   
                                                        </xsl:choose>

                                                        <!--<xsl:if test="$DATAELEMENTS_intDataElementType = 'SYSTEM LOOKUP'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if> -->
                                                        SYSTEM LOOKUP
                                                </option>
                                                <option value="MONETARY">
                                                        <xsl:choose>
                                                        <xsl:when test="$DATAELEMENTS_intDataElementTypeSelected != 'null'">
                                                         <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'MONETARY'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                         </xsl:if>
                                                        </xsl:when>

                                                        <xsl:when test="$DATAELEMENTS_intDataElementType = 'MONETARY'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:when>   
                                                        </xsl:choose>

                                                        <!-- <xsl:if test="$DATAELEMENTS_intDataElementType = 'MONETARY'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if> -->
                                                        MONETARY
                                                </option>
                                                
                                                <!-- Attachment Start-->                                                
                                                <option value="ATTACHMENT">
                                                        <xsl:choose>
                                                        <xsl:when test="$DATAELEMENTS_intDataElementTypeSelected != 'null'">
                                                         <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'ATTACHMENT'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                         </xsl:if>
                                                        </xsl:when>

                                                        <xsl:when test="$DATAELEMENTS_intDataElementType = 'ATTACHMENT'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:when>   
                                                        </xsl:choose>

                                                        <!-- <xsl:if test="$DATAELEMENTS_intDataElementType = 'MONETARY'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if> -->
                                                        ATTACHMENT
                                                </option>                                                
                                                <!-- Attachment End -->
                                                
                                        </select>

                                </td>
                            </tr>
                            <!-- Name -->
                            <tr>
                                <td width="20%" class="uportal-label">
                                   <xsl:value-of select="DATAELEMENTS_strDataElementNameDisplay" />:
                                </td>
                                <td width="80%">
                                    <textarea name="DATAELEMENTS_strDataElementName" rows="2" cols="80" tabindex="7" class="uportal-input-text">
                                        <xsl:value-of select="DATAELEMENTS_strDataElementName" />
                                    </textarea>                            
                                </td>
                            </tr>
                            <!-- Help -->
                            <tr>
                                <td width="20%" class="uportal-label">
                                   <xsl:value-of select="DATAELEMENTS_strDataElementHelpDisplay" />:
                                </td>
                                <td width="80%">
                                    <textarea name="DATAELEMENTS_strDataElementHelp" rows="2" cols="80" tabindex="8" class="uportal-input-text">
                                        <xsl:value-of select="DATAELEMENTS_strDataElementHelp" />
                                    </textarea>                            
                                </td>
                            </tr>
                            <!-- ************** if the dataelement type is TEXT ****************************-->
                            <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'TEXT' or $DATAELEMENTS_intDataElementType = 'TEXT' and $DATAELEMENTS_intDataElementTypeSelected = 'null' or $DATAELEMENTS_intDataElementTypeSelected = 'null' and $strMode = 'newDEClicked'">
                                <!-- Rows -->
                                <tr>
                                    <td width="20%" class="uportal-label">
                                       <xsl:value-of select="DATAELEMENTS_intDataElementRowDisplay" />:

                                    </td>
                                    <td width="80%">
                                       <xsl:if test="string-length($DATAELEMENTS_intDataElementRow) != 0">
                                            <input type="text" name="DATAELEMENTS_intDataElementRow" value="{$DATAELEMENTS_intDataElementRow}" size="3" tabindex="9" class="uportal-input-text"/>                          
                                       </xsl:if>
                                       <xsl:if test="string-length($DATAELEMENTS_intDataElementRow) = 0">
                                            <input type="text" name="DATAELEMENTS_intDataElementRow" value="0" size="3" tabindex="9" class="uportal-input-text"/>                          
                                       </xsl:if>
                                    </td>
                                </tr>
                                <!-- Columns -->
                                <tr>
                                    <td width="20%" class="uportal-label">
                                       <xsl:value-of select="DATAELEMENTS_intDataElementColumnDisplay" />:

                                    </td>
                                    <td width="80%">
                                       <xsl:if test="string-length($DATAELEMENTS_intDataElementColumn) != 0"> 
                                            <input type="text" name="DATAELEMENTS_intDataElementColumn" value="{$DATAELEMENTS_intDataElementColumn}" size="3" tabindex="10" class="uportal-input-text"/>                          
                                       </xsl:if>
                                       <xsl:if test="string-length($DATAELEMENTS_intDataElementColumn) = 0">
                                            <input type="text" name="DATAELEMENTS_intDataElementColumn" value="0" size="3" tabindex="10" class="uportal-input-text"/>                          
                                       </xsl:if>
                                    </td>
                                </tr>                    
                            </xsl:if>
                            <!-- ************** if the dataelement type is Dropdown****************************-->
                            <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'DROPDOWN' or $DATAELEMENTS_intDataElementType = 'DROPDOWN' and $DATAELEMENTS_intDataElementTypeSelected = 'null'">
                            <xsl:if test="$DATAELEMENTS_intDataElementIDselected != 'null'">
                            <tr>
                                <td width="20%" class="uportal-label">

                                </td>
                                <td width="80%" class="uportal-text">
                                       <!-- <a href="javascript:window.location='{$baseActionURL}?current=addedit_options&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;strSurveyNameURL={$strSurveyNameURL}&amp;intQuestionID={$intQuestionID}'" > -->
                                       <a href="javascript:window.location='{$baseActionURL}?current=addedit_options&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;displayAddEditOptions=true&amp;DEPoolSelected={$DEPoolSelected}&amp;blFirstTime=true'" > 
                                         Add/Edit Options
                                       </a>                     
                                </td>
                            </tr>
                            </xsl:if>
                            </xsl:if>
                            <!-- ************** if the dataelement type is Numeric****************************-->
                            <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'NUMERIC' or $DATAELEMENTS_intDataElementType = 'NUMERIC' and $DATAELEMENTS_intDataElementTypeSelected = 'null'">

                                <!-- Int Min -->
                                <tr>
                                    <td width="20%" class="uportal-label">
                                       <xsl:value-of select="DATAELEMENTS_intDataElementIntMinDisplay" />:

                                    </td>
                                    <td width="80%">
                                       <xsl:if test="string-length($DATAELEMENTS_intDataElementIntMin) != 0"> 
                                            <input type="text" name="DATAELEMENTS_intDataElementIntMin" value="{$DATAELEMENTS_intDataElementIntMin}" size="10" tabindex="9" class="uportal-input-text"/>                          
                                       </xsl:if>
                                       <xsl:if test="string-length($DATAELEMENTS_intDataElementIntMin) = 0"> 
                                            <input type="text" name="DATAELEMENTS_intDataElementIntMin" value="0" size="10" tabindex="9" class="uportal-input-text"/>                          
                                       </xsl:if>
                                    </td>
                                </tr>  
                                <!-- Int Max -->
                                <tr>
                                    <td width="20%" class="uportal-label">
                                       <xsl:value-of select="DATAELEMENTS_intDataElementIntMaxDisplay" />:

                                    </td>
                                    <td width="80%">
                                       <xsl:if test="string-length($DATAELEMENTS_intDataElementIntMax) != 0">  
                                            <input type="text" name="DATAELEMENTS_intDataElementIntMax" value="{$DATAELEMENTS_intDataElementIntMax}" size="10" tabindex="10" class="uportal-input-text"/>                          
                                       </xsl:if>
                                       <xsl:if test="string-length($DATAELEMENTS_intDataElementIntMax) = 0"> 
                                            <input type="text" name="DATAELEMENTS_intDataElementIntMax" value="0" size="10" tabindex="10" class="uportal-input-text"/>                          
                                       </xsl:if>
                                    </td>
                                </tr>
                                
                            </xsl:if>
                            <!-- ************** if the dataelement type is Date****************************-->
                            <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'DATE' or $DATAELEMENTS_intDataElementType = 'DATE' and $DATAELEMENTS_intDataElementTypeSelected = 'null'">

                                <!-- Dt Min -->
                                <tr>
                                    <td width="20%" class="uportal-label">
                                       <xsl:value-of select="DATAELEMENTS_dtDataElementDateMinDisplay" />:

                                    </td>
                                    <td width="80%">
                                       <!--<input type="text" name="DATAELEMENTS_dtDataElementDateMin" value="{$DATAELEMENTS_dtDataElementDateMin}" size="10" tabindex="6" class="uportal-input-text"/> -->
                                             <select name="DATAELEMENTS_dtDataElementDateMin_Day" tabindex="9" class="uportal-input-text">
                                                <xsl:for-each select="DATAELEMENTS_dtDataElementDateMin_Day">

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

                                            <select name="DATAELEMENTS_dtDataElementDateMin_Month" tabindex="10" class="uportal-input-text">
                                                <xsl:for-each select="DATAELEMENTS_dtDataElementDateMin_Month">

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

                                            <input type="text" name="DATAELEMENTS_dtDataElementDateMin_Year" value="{$DATAELEMENTS_dtDataElementDateMin_Year}" size="5" tabindex="11" class="uportal-input-text" />

                                    </td>
                                </tr>  
                                <!-- Dt Max -->
                                <tr>
                                    <td width="20%" class="uportal-label">
                                       <xsl:value-of select="DATAELEMENTS_dtDataElementDateMaxDisplay" />:

                                    </td>
                                    <td width="80%">
                                       <!--<input type="text" name="DATAELEMENTS_dtDataElementDateMax" value="{$DATAELEMENTS_dtDataElementDateMax}" size="10" tabindex="5" class="uportal-input-text"/> -->
                                       <select name="DATAELEMENTS_dtDataElementDateMax_Day" tabindex="12" class="uportal-input-text">
                                                <xsl:for-each select="DATAELEMENTS_dtDataElementDateMax_Day">

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

                                            <select name="DATAELEMENTS_dtDataElementDateMax_Month" tabindex="13" class="uportal-input-text">
                                                <xsl:for-each select="DATAELEMENTS_dtDataElementDateMax_Month">

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

                                            <input type="text" name="DATAELEMENTS_dtDataElementDateMax_Year" value="{$DATAELEMENTS_dtDataElementDateMax_Year}" size="5" tabindex="14" class="uportal-input-text" />

                                    </td>
                                </tr>
                               <!-- Default fields -->
                               <tr>
                                   <td width="20%" class="uportal-label">
                                         Default Date:
                                   </td>
                                   <td width="80%" class="uportal-text">
                                       <input type="RADIO" name="rdDefaultorExisting" value="DATAELEMENTS_strDataElementDefaultTodaysDate">  
                                                                                                                    
                                           <xsl:if test="$strRadioVal = 'DATAELEMENTS_strDataElementDefaultTodaysDate'">
                                                <xsl:attribute name="checked">1</xsl:attribute>                                           
                                           </xsl:if>
                                                                             
                                        </input> 
                                        
                                        Today's Date
                                    </td>
                               </tr>
                               <!-- Or -->
                               <tr>
                                   <td width="20%">
                                   </td>
                                   <td width="80%" class="uportal-text">
                                        or
                                    </td>
                               </tr>
                                <!-- Default Date first option-->
                                <tr>
                                    <td width="20%" class="uportal-label">
                                      
                                    </td>
                                    
                                    <td width="80%">
                                        
                                      
                                      <input type="RADIO" name="rdDefaultorExisting" value="DATAELEMENTS_dtDataElementDefaultDate">  
                                            <xsl:if test="$strRadioVal = 'DATAELEMENTS_dtDataElementDefaultDate'"> 
                                            <xsl:attribute name="checked">1</xsl:attribute> 
                                           </xsl:if>
                                                                                   
                                        </input> 
                                          
                                      <select name="DATAELEMENTS_dtDataElementDefaultDate_Day" tabindex="15" class="uportal-input-text">
                                                <xsl:for-each select="DATAELEMENTS_dtDataElementDefaultDate_Day">
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

                                            <select name="DATAELEMENTS_dtDataElementDefaultDate_Month" tabindex="16" class="uportal-input-text">
                                                <xsl:for-each select="DATAELEMENTS_dtDataElementDefaultDate_Month">

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

                                            <input type="text" name="DATAELEMENTS_dtDataElementDefaultDate_Year" value="{$DATAELEMENTS_dtDataElementDefaultDate_Year}" size="5" tabindex="17" class="uportal-input-text" />
                                            
                                      </td>
                                   </tr>      
                                   <!-- Or -->
                                   <tr>
                                       <td width="20%">
                                       </td>
                                       <td width="80%" class="uportal-text">
                                            or
                                        </td>
                                   </tr>
                                   
                                   <!-- Today's Date Option -->
                                   <tr>
                                        <td width="20%">
                                        </td>
                                        <!--<td width="80%" class="uportal-text">-->
                                        <td width="20%" class="uportal-text">
                                            <input type="RADIO" name="rdDefaultorExisting" value="DATAELEMENTS_dtDataElementTodaysDatePlusWorkingDays">  
                                                <xsl:if test="$strRadioVal = 'DATAELEMENTS_dtDataElementTodaysDatePlusWorkingDays'"> 
                                                    <xsl:attribute name="checked">1</xsl:attribute> 
                                                </xsl:if>
                                                                                      
                                            </input>                           
                                                                                              
                                            Today's Date 
                                       

                                            <select size="1" name="DATAELEMENTS_strDataElementDefaultWorkingDaysOperator" tabindex="18" class="uportal-input-text">
                                                    <option value="+">
                                                            <xsl:if test="$DATAELEMENTS_strDataElementDefaultWorkingDaysOperator = '+'">
                                                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                                            </xsl:if>
                                                            +
                                                    </option>
                                                    <option value="-">
                                                            <xsl:if test="$DATAELEMENTS_strDataElementDefaultWorkingDaysOperator = '-'">
                                                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                                            </xsl:if>
                                                            -
                                                    </option>					
                                             </select>   
                                        
                                            <xsl:if test="string-length($DATAELEMENTS_intDataElementDefaultWorkingDaysNumber) != 0"> 
                                                <input type="text" name="DATAELEMENTS_intDataElementDefaultWorkingDaysNumber" value="{$DATAELEMENTS_intDataElementDefaultWorkingDaysNumber}" size="5" tabindex="19" class="uportal-input-text" />
                                            </xsl:if>
                                            <xsl:if test="string-length($DATAELEMENTS_intDataElementDefaultWorkingDaysNumber) = 0"> 
                                                <input type="text" name="DATAELEMENTS_intDataElementDefaultWorkingDaysNumber" value="0" size="5" tabindex="19" class="uportal-input-text" />
                                            </xsl:if>

                                            <select size="1" name="DATAELEMENTS_strDataElementDefaultWdOrDaysOption" tabindex="20" class="uportal-input-text">
                                                    <option value="Working Days">
                                                            <xsl:if test="$DATAELEMENTS_strDataElementDefaultWdOrDaysOption = 'Working Days'">
                                                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                                            </xsl:if>
                                                            Working Days
                                                    </option>
                                                    <option value="Days">
                                                            <xsl:if test="$DATAELEMENTS_strDataElementDefaultWdOrDaysOption = 'Days'">
                                                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                                            </xsl:if>
                                                            Days
                                                    </option>					
                                             </select>  
                                        </td>

                                         <!--</td>-->
                                   </tr>
                                   <!-- Or -->
                                   <tr>
                                       <td width="20%">
                                       </td>
                                       <td width="80%" class="uportal-text">
                                            or
                                        </td>
                                   </tr>
                                     
                                  <!--  </td> 
                                </tr>  -->
                            </xsl:if> 
                            <!-- ************** if the dataelement type is Script****************************-->
                            <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'SCRIPT' or $DATAELEMENTS_intDataElementType = 'SCRIPT' and $DATAELEMENTS_intDataElementTypeSelected = 'null'">                        
                                <!-- Script -->
                                <tr>
                                    <td width="20%" >                                    
                                    </td>
                                    <td width="80%" class="uportal-text">
                                        (Hint:DID = DataElement Id Variable)
                                    </td>
                                </tr>
                                <tr>
                                    <td width="20%" class="uportal-label">
                                       <xsl:value-of select="DATAELEMENTS_strDataElementScriptDisplay" />:

                                    </td>
                                    <td width="80%">
                                        <textarea name="DATAELEMENTS_strDataElementScript" rows="8" cols="80" tabindex="9" class="uportal-input-text">
                                            <xsl:value-of select="DATAELEMENTS_strDataElementScript" />
                                        </textarea>   
                                       <!--<input type="text" name="DATAELEMENTS_strDataElementScript" value="{$DATAELEMENTS_strDataElementScript}" size="3" tabindex="6" class="uportal-input-text"/> -->
                                    </td>
                                </tr>  
                            </xsl:if>
                            <!-- ************** if the dataelement type is System Lookup****************************-->
                            <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'SYSTEM LOOKUP' or $DATAELEMENTS_intDataElementType = 'SYSTEM LOOKUP' and $DATAELEMENTS_intDataElementTypeSelected = 'null'">                        
                                <xsl:if test="$DATAELEMENTS_intDataElementIDselected != 'null'">
                                <!-- System Lookup Types -->
                                <tr>
                                    <td width="20%" class="uportal-label">
                                       Lookup Types:

                                    </td>
                                    <td width="80%">
                                        <select name="DATAELEMENTS_strDataElementLookupType" tabindex="9" class="uportal-input-text" onChange="document.frmaddedit_dataelements1.submit();">                                     

                                                <xsl:for-each select="build_LookupTypes">
                                                    <xsl:variable name="DATAELEMENTS_strDataElementLookupType"><xsl:value-of select="DATAELEMENTS_strDataElementLookupType" /></xsl:variable>
                                                    <option>
                                                            <xsl:attribute name="value">
                                                                <xsl:value-of select="DATAELEMENTS_strDataElementLookupType" />
                                                            </xsl:attribute>                  

                                                            <xsl:if test="$DATAELEMENTS_strDataElementLookupType=$DELookupTypeSelected">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                            </xsl:if>

                                                            <xsl:value-of select="DATAELEMENTS_strDataElementLookupType" /> 
                                                     </option>
                                                </xsl:for-each>
                                                
                                        </select>
                                    </td>
                                </tr>
                                
                                
                                <!-- Where Conditions Labels-->
                                <tr>
                                    <td width="20%" class="uportal-label">
                                       Where:
                                    </td>
                                    <td width="80%" class="uportal-label">
                                        
                                   <table width="100%">
                                   <tr>
                                   <td width="10%" class="uportal-channel-table-header"> 
                                        <!--<xsl:value-of select="SYSTEMLOOKUPWHERE_strConnectorDisplay" />-->
                                       Connec
                                    </td>
                                    <td width="25%" class="uportal-channel-table-header">
                                        <!--<xsl:value-of select="SYSTEMLOOKUPWHERE_strFieldDisplay" />-->
                                        Field
                                    </td>
                                    <td width="5%" class="uportal-channel-table-header">                                       
                                        <!--<xsl:value-of select="SYSTEMLOOKUPWHERE_strOperatorDisplay" />-->
                                        Oper
                                    </td>
                                    <td width="10%" class="uportal-channel-table-header">                                       
                                        <!--<xsl:value-of select="SYSTEMLOOKUPWHERE_strValueDisplay" /> -->
                                        Value
                                    </td>
                                    <td width="10%" class="uportal-label">

                                    </td> 
                                    <td width="40%" class="uportal-label">

                                    </td> 
                                </tr>
    <!--- ***********************************************************************************************************88-->
                                <!-- Where Labels -->
                                <xsl:for-each select="buildLookupWhere">
                                <xsl:variable name="SYSTEMLOOKUPWHERE_intDataElementLookupWhereID"><xsl:value-of select="SYSTEMLOOKUPWHERE_intDataElementLookupWhereID" /></xsl:variable>                
                                <xsl:variable name="SYSTEMLOOKUPWHERE_intDataElementID"><xsl:value-of select="SYSTEMLOOKUPWHERE_intDataElementID" /></xsl:variable>
                                <xsl:variable name="SYSTEMLOOKUPWHERE_strConnector"><xsl:value-of select="SYSTEMLOOKUPWHERE_strConnector" /></xsl:variable>
                                <xsl:variable name="SYSTEMLOOKUPWHERE_strField"><xsl:value-of select="SYSTEMLOOKUPWHERE_strField" /></xsl:variable>
                                <xsl:variable name="SYSTEMLOOKUPWHERE_strOperator"><xsl:value-of select="SYSTEMLOOKUPWHERE_strOperator" /></xsl:variable>
                                <xsl:variable name="SYSTEMLOOKUPWHERE_strValue"><xsl:value-of select="SYSTEMLOOKUPWHERE_strValue" /></xsl:variable>


                                <tr>
                                        <xsl:choose>
                                        <xsl:when test="position() mod 2 != 0">


                                                
                                                <td width="10%" class="uportal-input-text">
                                                        <xsl:value-of select="SYSTEMLOOKUPWHERE_strConnector" />
                                                </td>
                                                
                                                <td width="25%" class="uportal-input-text">
                                                        <xsl:value-of select="SYSTEMLOOKUPWHERE_strField" />
                                                </td>
                                                
                                                <td width="5%" class="uportal-input-text">
                                                        <xsl:value-of select="SYSTEMLOOKUPWHERE_strOperator" />
                                                </td>
                                                
                                                <td width="10%" class="uportal-input-text">
                                                        <xsl:value-of select="SYSTEMLOOKUPWHERE_strValue" />
                                                </td>                         
                                                <td width="10%" class="uportal-input-text">
                                                        <xsl:if test="$SYSTEMLOOKUPWHERE_intDataElementLookupWhereID != ''">
                                                                <a href="{$baseActionURL}?uP_root=root&amp;current=addedit_dataelements&amp;target=editWhere&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;DATAELEMENTS_intDataElementTypeSelected={$DATAELEMENTS_intDataElementTypeSelected}&amp;SYSTEMLOOKUPWHERE_intDataElementLookupWhereID={$SYSTEMLOOKUPWHERE_intDataElementLookupWhereID}&amp;DELookupTypeSelected={$DELookupTypeSelected}&amp;DATAELEMENTS_intDataElementDefaultSmartformID={$DE_DefaultSmartformIDSelected}&amp;DATAELEMENTS_intDataElementDefaultDataElementID={$DATAELEMENTS_intDataElementDefaultDataElementID}&amp;DE_DefaultDataElementIDSelected={$DE_DefaultDataElementIDSelected}&amp;strRadioVal={$strRadioVal}">Edit</a>
                                                        </xsl:if>
                                                </td>                      
                                                <td width="40%" class="uportal-input-text">
                                                        <xsl:if test="$SYSTEMLOOKUPWHERE_intDataElementLookupWhereID != ''">
                                                                <!--<a href="javascript:confirmDelete('{$baseActionURL}?uP_root=root&amp;current=addedit_dataelements&amp;target=delete&amp;SYSTEMLOOKUPWHERE_intDataElementLookupWhereID={$SYSTEMLOOKUPWHERE_intDataElementLookupWhereID}&amp;intInternalPatientID={$intInternalPatientID}&amp;CONSENT_STUDY_Timestamp={$CONSENT_STUDY_Timestamp}&amp;strOrigin={$strOrigin}')"> del</a>-->
                                                                <a href="javascript:confirmDeleteWhere('{$baseActionURL}?uP_root=root&amp;current=addedit_dataelements&amp;target=deleteWhere&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;DATAELEMENTS_intDataElementTypeSelected={$DATAELEMENTS_intDataElementTypeSelected}&amp;SYSTEMLOOKUPWHERE_intDataElementLookupWhereID={$SYSTEMLOOKUPWHERE_intDataElementLookupWhereID}&amp;DELookupTypeSelected={$DELookupTypeSelected}&amp;DATAELEMENTS_intDataElementDefaultSmartformID={$DE_DefaultSmartformIDSelected}&amp;DATAELEMENTS_intDataElementDefaultDataElementID={$DATAELEMENTS_intDataElementDefaultDataElementID}&amp;DE_DefaultDataElementIDSelected={$DE_DefaultDataElementIDSelected}')">Del</a>

                                                        </xsl:if>
                                                </td>
                                        </xsl:when>
                                        <xsl:otherwise>

                                                
                                                <td width="10%" class="uportal-text">
                                                        <xsl:value-of select="SYSTEMLOOKUPWHERE_strConnector" />
                                                </td>
                                                
                                                <td width="25%" class="uportal-text">
                                                        <xsl:value-of select="SYSTEMLOOKUPWHERE_strField" />
                                                </td>
                                                
                                                <td width="5%" class="uportal-text">
                                                        <xsl:value-of select="SYSTEMLOOKUPWHERE_strOperator" />
                                                </td>
                                                
                                                <td width="10%" class="uportal-text">
                                                        <xsl:value-of select="SYSTEMLOOKUPWHERE_strValue" />
                                                </td>                         
                                                <td width="10%" class="uportal-text">
                                                        <xsl:if test="$SYSTEMLOOKUPWHERE_intDataElementLookupWhereID != ''">
                                                            <a href="{$baseActionURL}?uP_root=root&amp;current=addedit_dataelements&amp;target=editWhere&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;DATAELEMENTS_intDataElementTypeSelected={$DATAELEMENTS_intDataElementTypeSelected}&amp;SYSTEMLOOKUPWHERE_intDataElementLookupWhereID={$SYSTEMLOOKUPWHERE_intDataElementLookupWhereID}&amp;DELookupTypeSelected={$DELookupTypeSelected}&amp;DATAELEMENTS_intDataElementDefaultSmartformID={$DE_DefaultSmartformIDSelected}&amp;DATAELEMENTS_intDataElementDefaultDataElementID={$DATAELEMENTS_intDataElementDefaultDataElementID}&amp;DE_DefaultDataElementIDSelected={$DE_DefaultDataElementIDSelected}&amp;strRadioVal={$strRadioVal}">Edit</a>
                                                        </xsl:if>
                                                </td>                      
                                                <td width="40%" class="uportal-text">
                                                        <xsl:if test="$SYSTEMLOOKUPWHERE_intDataElementLookupWhereID != ''">
                                                            <!--<a href="{$baseActionURL}?uP_root=root&amp;current=addedit_dataelements&amp;target=deleteWhere&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;SYSTEMLOOKUPWHERE_intDataElementLookupWhereID={$SYSTEMLOOKUPWHERE_intDataElementLookupWhereID}">del </a>-->
                                                            <a href="javascript:confirmDeleteWhere('{$baseActionURL}?uP_root=root&amp;current=addedit_dataelements&amp;target=deleteWhere&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;DATAELEMENTS_intDataElementTypeSelected={$DATAELEMENTS_intDataElementTypeSelected}&amp;SYSTEMLOOKUPWHERE_intDataElementLookupWhereID={$SYSTEMLOOKUPWHERE_intDataElementLookupWhereID}&amp;DELookupTypeSelected={$DELookupTypeSelected}&amp;DATAELEMENTS_intDataElementDefaultSmartformID={$DE_DefaultSmartformIDSelected}&amp;DATAELEMENTS_intDataElementDefaultDataElementID={$DATAELEMENTS_intDataElementDefaultDataElementID}&amp;DE_DefaultDataElementIDSelected={$DE_DefaultDataElementIDSelected}')">Del</a>
                                                        </xsl:if>
                                                </td>
                                        </xsl:otherwise>
                                        </xsl:choose>
                                </tr>

                                </xsl:for-each>       

                                </table>
                               </td>
                               </tr>
    <!-- ************************************************************************************************************-->
                                
                                
                                <!-- Where Conditions Fields -->
                                <tr>
                                    <td width="20%" class="uportal-label">
                                       
                                    </td>
                                    <td width="80%" class="uportal-label">
                                        <select size="1" name="SYSTEMLOOKUPWHERE_strConnector" tabindex="10" class="uportal-input-text">
                                                <option value="AND">
                                                        <xsl:if test="$SYSTEMLOOKUPWHERE_strConnector = 'AND'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if>
                                                        AND
                                                </option>
                                                <option value="OR">
                                                        <xsl:if test="$SYSTEMLOOKUPWHERE_strConnector = 'OR'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if>
                                                        OR
                                                </option>					
                                         </select>  
                                         
                                    <xsl:text>  </xsl:text>                                   
                                        <!--  onChange="document.frmbuild_smartform.submit();" -->
                                         <select name="SYSTEMLOOKUPWHERE_strField" tabindex="11" class="uportal-input-text">                                     

                                                <xsl:for-each select="Lookup_FieldNames">
                                                    <xsl:variable name="SYSTEMLOOKUPWHERE_strField"><xsl:value-of select="SYSTEMLOOKUPWHERE_strField" /></xsl:variable>
                                                    <!--<xsl:variable name="SYSTEMLOOKUPWHERE_strInternalName"><xsl:value-of select="SYSTEMLOOKUPWHERE_strInternalName" /></xsl:variable>-->
                                                    
                                                    <option>
                                                            <xsl:attribute name="value">
                                                                <xsl:value-of select="SYSTEMLOOKUPWHERE_strField" />
                                                            </xsl:attribute>                  

                                                            <xsl:if test="$SYSTEMLOOKUPWHERE_strField=$DELookupFieldSelected">                                           
                
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                            </xsl:if>

                                                            <xsl:value-of select="SYSTEMLOOKUPWHERE_strField" />
                                                            
                                                     </option>
                                                </xsl:for-each>
                                                
                                        </select>
                                    
                                        <xsl:text>  </xsl:text>
                                        
                                        <select size="1" name="SYSTEMLOOKUPWHERE_strOperator" tabindex="12" class="uportal-input-text">
                                                <option value="=">
                                                        <xsl:if test="$SYSTEMLOOKUPWHERE_strOperator = '='">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if>
                                                        =
                                                </option>
                                                <option value="!=">
                                                        <xsl:if test="$SYSTEMLOOKUPWHERE_strOperator = '!='">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if>
                                                        !=
                                                </option>
                                                <option value="&lt;">
                                                        <xsl:if test="$SYSTEMLOOKUPWHERE_strOperator = '&lt;'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if>
                                                        &lt;
                                                </option>
                                                <option value="&lt;=">
                                                        <xsl:if test="$SYSTEMLOOKUPWHERE_strOperator = '&lt;='">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if>
                                                        &lt;=
                                                </option>
                                                <option value="&gt;">
                                                        <xsl:if test="$SYSTEMLOOKUPWHERE_strOperator = '&gt;'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if>
                                                        &gt;
                                                </option>
                                                <option value="&gt;=">
                                                        <xsl:if test="$SYSTEMLOOKUPWHERE_strOperator = '&gt;='">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if>
                                                        &gt;=
                                                </option>
                                                <option value="LIKE">
                                                        <xsl:if test="$SYSTEMLOOKUPWHERE_strOperator = 'LIKE'">
                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                        </xsl:if>
                                                        LIKE
                                                </option>					
                                         </select>  
                                    
                                         <xsl:text>  </xsl:text>
                                                                               
                                        <input type="text" name="SYSTEMLOOKUPWHERE_strValue" value="{$SYSTEMLOOKUPWHERE_strValue}" size="10" tabindex="13" class="uportal-input-text"/>                          
                                   
                                        <xsl:text>  </xsl:text>                                                                             
                                        
                                    </td>
                                </tr>
                                
                                <xsl:if test="string-length($SYSTEMLOOKUPWHERE_intDataElementLookupWhereID) = 0">
                                <!-- Save Where -->
                                <tr>
                                    <td width="20%"> 
                                    </td>   
                                    <td width="80%">
                                        <input type="submit" name="addWhere" tabindex="14" value="{$addBtnLabel}" class="uportal-button" />   
                                        
                                    </td>
                                </tr>
                                </xsl:if>
                                <xsl:if test="$SYSTEMLOOKUPWHERE_intDataElementLookupWhereID != 'null'">
                                <!-- Save Where -->
                                <tr>
                                    <td width="20%"> 
                                    </td>   
                                    <td width="80%">
                                        <input type="submit" name="updateWhere" tabindex="14" value="{$updateBtnLabel}" class="uportal-button" />   
                                        <input type="submit" name="cancelWhere" tabindex="15" value="{$cancelBtnLabel}" class="uportal-button" />   
                                        
                                    </td>
                                </tr>
                                </xsl:if>
                                
                               <!-- Display Fields Labels -->
                                <tr>
                                    <td width="20%" class="uportal-label">
                                       Display Fields:
                                    </td>
                                    <td width="80%">
                                        <table width="100%">
                                            <!-- Checkbox -->                                    
                                            <td width="5%">  
                                                <xsl:text></xsl:text>                                          
                                            </td>
                                            <!-- Field Name -->                                        
                                            <td class="uportal-channel-table-header" width="25%">
                                                FieldName                                            
                                            </td>

                                            <!-- Order -->                                       
                                            <td class="uportal-channel-table-header" width="5%">
                                                Order
                                            </td>    

                                            <!-- Display Fields -->          
                                            <td width="5%" class="uportal-channel-table-header">  
                                                <xsl:text></xsl:text> 
                                                  
                                            </td>

                                            <td class="uportal-channel-table-header" width="60%">
                                                Display Fields 
                                            </td>  
                                        </table>                                                    
                                    </td>                                            
                                </tr>                           

                                 <!-- Display fields -->
                                <xsl:for-each select="Lookup_DisplayField">  

                                <xsl:variable name="SYSTEMLOOKUPFIELD_strInternalName"><xsl:value-of select="SYSTEMLOOKUPFIELD_strInternalName" /></xsl:variable>                                             

                                 <tr>   
                                        <td width="20%" class="uportal-label">
                                            
                                        </td>
                                        <td width="80%">
                                        <table width="100%">                                           
                                                                                 
                                               <!-- Checkbox -->
                                               <td width="5%">  
                                                    <input type="checkbox"> 
                                                        <xsl:attribute name="name"><xsl:value-of select="SYSTEMLOOKUPFIELD_strInternalName" /></xsl:attribute>            

                                                        <xsl:for-each select="/smartform/Lookup_SelectedDisplayField">                  
                                                        <xsl:variable name="SYSTEMLOOKUPFIELD_strInternalNameSelected" select="./SYSTEMLOOKUPFIELD_strInternalNameSelected"/>
                                                        <!--<xsl:variable name="DELookupDisplayOrderSelected"><xsl:value-of select="DELookupDisplayOrderSelected" /></xsl:variable> -->

                                                        <xsl:if test="$SYSTEMLOOKUPFIELD_strInternalName = $SYSTEMLOOKUPFIELD_strInternalNameSelected">
                                                        <xsl:attribute name="checked">true</xsl:attribute>                                                                           

                                                        </xsl:if>

                                                       </xsl:for-each> 

                                                    </input>                                         
                                                </td>

                                                <!-- Field Name -->                                        
                                                <td class="uportal-text" width="25%">                                                       
                                                    <xsl:value-of select="SYSTEMLOOKUPWHERE_strField" />                                                        
                                                </td>

                                                <!-- Order -->                                       
                                                <td class="uportal-text" width="5%">
                                                    <select name="SYSTEMLOOKUPFIELD_strFieldOrder{$SYSTEMLOOKUPFIELD_strInternalName}" tabindex="16" class="uportal-input-text">                                     
                                                        <xsl:for-each select="/smartform/LoadFieldCount">  
                                                         <xsl:variable name="SYSTEMLOOKUPFIELD_strFieldOrder"><xsl:value-of select="SYSTEMLOOKUPFIELD_strFieldOrder" /></xsl:variable>  

                                                            <option>
                                                                    <xsl:attribute name="value">
                                                                        <xsl:value-of select="SYSTEMLOOKUPFIELD_strFieldOrder" />
                                                                    </xsl:attribute>                  

                                                                     <xsl:for-each select="/smartform/Lookup_SelectedDisplayField">                  
                                                                        <xsl:variable name="SYSTEMLOOKUPFIELD_strInternalNameSelected" select="./SYSTEMLOOKUPFIELD_strInternalNameSelected"/>
                                                                        <xsl:variable name="DELookupDisplayOrderSelected"><xsl:value-of select="./DELookupDisplayOrderSelected" /></xsl:variable>                                                          

                                                                        <xsl:if test="$SYSTEMLOOKUPFIELD_strInternalName=$SYSTEMLOOKUPFIELD_strInternalNameSelected">
                                                                            <xsl:if test="$SYSTEMLOOKUPFIELD_strFieldOrder=$DELookupDisplayOrderSelected">           

                                                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                                                            </xsl:if>
                                                                        </xsl:if>
                                                                    </xsl:for-each> 

                                                                    <xsl:value-of select="SYSTEMLOOKUPFIELD_strFieldOrder" />

                                                             </option>
                                                        </xsl:for-each>
                                                    </select>
                                                </td>                                         
                                                    
                                                <td class="uportal-text" width="5%">                                                       
                                                    <!--<xsl:value-of select="SYSTEMLOOKUPWHERE_strField" /> -->
                                                </td>  
                                                
                                                <!-- Checkbox --><!-- Display Fields -->     
                                                <td width="60%">  
                                                    <input type="checkbox"> 
                                                    <xsl:attribute name="name">SYSTEMLOOKUPFIELD_strInDisplayAddnl<xsl:value-of select="SYSTEMLOOKUPFIELD_strInternalName" /></xsl:attribute>            

                                                    <xsl:for-each select="/smartform/Lookup_SelectedDisplayFieldAddnl">                  
                                                    <xsl:variable name="SYSTEMLOOKUPFIELD_strInDisplayAddnlSelected" select="./SYSTEMLOOKUPFIELD_strInDisplayAddnlSelected"/>
                                                    <!--<xsl:variable name="DELookupDisplayOrderSelected"><xsl:value-of select="DELookupDisplayOrderSelected" /></xsl:variable> -->

                                                    <xsl:if test="$SYSTEMLOOKUPFIELD_strInternalName = $SYSTEMLOOKUPFIELD_strInDisplayAddnlSelected">
                                                    <xsl:attribute name="checked">true</xsl:attribute>                                                                           

                                                    </xsl:if>

                                                   </xsl:for-each> 

                                                    </input>                                         
                                                </td>                                                                                                                   
                                                                  
                                           
                                           </table>
                                         </td>
                                       </tr>                                                                                               
                                   </xsl:for-each>                         
                              </xsl:if> <!-- End of not Displaying where in Add mode -->
                                
                                  
                            </xsl:if>

                             <!-- ************** if the dataelement type is Monetary****************************-->
                            <xsl:if test="$DATAELEMENTS_intDataElementTypeSelected = 'MONETARY' or $DATAELEMENTS_intDataElementType = 'MONETARY' and $DATAELEMENTS_intDataElementTypeSelected = 'null'">                        
                                <!-- Int Min -->
                                <tr>
                                    <td width="20%" class="uportal-label">
                                       <xsl:value-of select="DATAELEMENTS_intDataElementIntMinDisplay" />:

                                    </td>
                                    <td width="80%">
                                       <xsl:if test="string-length($DATAELEMENTS_intDataElementIntMin) != 0"> 
                                                <input type="text" name="DATAELEMENTS_intDataElementIntMin" value="{$DATAELEMENTS_intDataElementIntMin}" size="10" tabindex="9" class="uportal-input-text" onblur="javascript:validDecimal(this.value);"/>                          
                                       </xsl:if>
                                       <xsl:if test="string-length($DATAELEMENTS_intDataElementIntMin) = 0">
                                       <!--  onkeypress="return(currencyFormat(document.frmaddedit_dataelements1.DATAELEMENTS_intDataElementIntMin,',','.',event))"-->
                                            <input type="text" name="DATAELEMENTS_intDataElementIntMin" value="0.00" size="10" tabindex="9" class="uportal-input-text" onblur="javascript:validDecimal(this.value);"/>                          
                                       </xsl:if>
                                    </td>
                                </tr>  
                                <!-- Int Max -->
                                <tr>
                                    <td width="20%" class="uportal-label">
                                       <xsl:value-of select="DATAELEMENTS_intDataElementIntMaxDisplay" />:

                                    </td>
                                    <td width="80%">
                                       <xsl:if test="string-length($DATAELEMENTS_intDataElementIntMax) != 0">   
                                            <input type="text" name="DATAELEMENTS_intDataElementIntMax" value="{$DATAELEMENTS_intDataElementIntMax}" size="10" tabindex="10" class="uportal-input-text" onblur="javascript:validDecimal(this.value);"/>                          
                                       </xsl:if>
                                       <xsl:if test="string-length($DATAELEMENTS_intDataElementIntMax) = 0"> 
                                            <input type="text" name="DATAELEMENTS_intDataElementIntMax" value="0.00" size="10" tabindex="10" class="uportal-input-text" onblur="javascript:validDecimal(this.value);"/>                          
                                       </xsl:if>
                                    </td>
                                </tr>                               
                            </xsl:if>                     

                            <!-- ****************************** DEFAULT SMARTFORM ID & DATAELEMENT ID **************** -->
                            <xsl:choose>
                            
                                <xsl:when test="$DATAELEMENTS_intDataElementTypeSelected = 'ATTACHMENT' or $DATAELEMENTS_intDataElementType = 'ATTACHMENT' and $DATAELEMENTS_intDataElementTypeSelected = 'null'">                        
                                <input type="hidden" name="DATAELEMENTS_intMandatory" value="0" />
                                </xsl:when>

                                <xsl:otherwise>
                                    <!-- Default -->
                                    <tr>
                                        <td width="20%" class="uportal-label">
                                        <xsl:choose>
                                        <xsl:when test="$DATAELEMENTS_intDataElementTypeSelected = 'DATE' or $DATAELEMENTS_intDataElementType = 'DATE' and $DATAELEMENTS_intDataElementTypeSelected = 'null'">

                                        </xsl:when>
                                        <xsl:otherwise>
                                            Default Value:
                                        </xsl:otherwise>
                                        </xsl:choose>
                                        </td>
                                        <td width="80%" class="uportal-text">
                                            <input type="RADIO" name="rdDefaultorExisting" value="rdDefault" >                                  
                                                <xsl:if test="$strRadioVal = ''">
                                                    <xsl:attribute name="checked">1</xsl:attribute> 
                                                </xsl:if>                                 

                                                <xsl:if test="($DATAELEMENTS_intDataElementTypeSelected != 'DATE' and $DATAELEMENTS_intDataElementTypeSelected != 'null') ">
                                                <xsl:if test="(($strRadioVal = 'DATAELEMENTS_strDataElementDefaultTodaysDate') or ($strRadioVal = 'DATAELEMENTS_dtDataElementDefaultDate') or ($strRadioVal = 'DATAELEMENTS_dtDataElementTodaysDatePlusWorkingDays'))">
                                                    <xsl:attribute name="checked">1</xsl:attribute> 
                                                </xsl:if>
                                                </xsl:if>


                                                 <xsl:if test="$strRadioVal = 'rdDefault'"> 
                                                    <xsl:attribute name="checked">1</xsl:attribute> 
                                                </xsl:if>
                                            </input>
                                           Default to

                                        <!-- Default textbox -->
                                            <!-- set it to zero if there is no value -->
                                            <xsl:if test="string-length($DATAELEMENTS_strDataElementDefault) = 0">
                                            <input type="text" name="DATAELEMENTS_strDataElementDefault" value="0" size="10" tabindex="21" class="uportal-input-text"/>                          
                                            </xsl:if>
                                            <xsl:if test="string-length($DATAELEMENTS_strDataElementDefault) != 0">
                                           <input type="text" name="DATAELEMENTS_strDataElementDefault" value="{$DATAELEMENTS_strDataElementDefault}" size="10" tabindex="21" class="uportal-input-text"/>                          
                                           </xsl:if>
                                        </td>
                                    </tr>  
                                    <!-- or -->
                                    <tr>
                                        <td width="20%">
                                        </td>
                                        <td width="80%" class="uportal-text">
                                         or
                                        </td>
                                    </tr> 
                                    <!-- Existing -->
                                    <tr>
                                        <td width="20%" class="uportal-label">
                                        </td>

                                        <td width="80%" class="uportal-text">
                                             <!-- Checkbox -->
                                                <table width="100%">  
                                                    <tr>
                                                        <td width="15%">  
                                                            <xsl:text></xsl:text>                                          
                                                        </td>

                                                        <!-- Smartform ID-->                                        
                                                        <td class="uportal-channel-table-header" width="20%">
                                                           Smartform ID                                          
                                                        </td>

                                                        <!-- DataElement ID -->                                       
                                                        <td class="uportal-channel-table-header" width="65%">
                                                            DataElement ID
                                                        </td>                                
                                                    </tr>

                                                    <tr>   

                                                        <td width="15%" class="uportal-text">
                                                            <input type="RADIO" name="rdDefaultorExisting" value="rdExisting" > 

                                                                <xsl:if test="$strRadioVal = 'rdExisting'"> 
                                                                    <xsl:attribute name="checked">1</xsl:attribute> 
                                                                </xsl:if>
                                                            </input>
                                                           Existing
                                                           </td>

                                                        <!-- Smartform Names -->
                                                        <td class="uportal-text" width="20%">
                                                            <!--<select name="DATAELEMENTS_intDataElementDefaultSmartformID" tabindex="4" class="uportal-input-text" onChange="javascript:dynamicSmartform('{$baseActionURL}?current=addedit_dataelements&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}&amp;DATAELEMENTS_intDataElementIDselected={$DATAELEMENTS_intDataElementIDselected}&amp;DATAELEMENTS_intDataElementType={$DATAELEMENTS_intDataElementType}&amp;DATAELEMENTS_intDataElementTypeSelected={$DATAELEMENTS_intDataElementType}&amp;DELookupTypeSelected={$DELookupTypeSelected}&amp;DELookupFieldSelected={$DELookupFieldSelected}&amp;strMode={$strMode}&amp;DE_DefaultSmartformIDSelected={$DATAELEMENTS_intDataElementDefaultSmartformID}&amp;DE_DefaultDataElementIDSelected={$DATAELEMENTS_intDataElementDefaultDataElementID}')"> -->
                                                            <!--<select name="DATAELEMENTS_intDataElementDefaultSmartformID" tabindex="4" class="uportal-input-text" onChange="document.frmaddedit_dataelements1.submit();"> -->
                                                            <!--<select name="DATAELEMENTS_intDataElementDefaultSmartformID" tabindex="4" class="uportal-input-text" onChange="javascript:dynamicSmartform()">-->
                                                            <select name="DATAELEMENTS_intDataElementDefaultSmartformID" tabindex="22" class="uportal-input-text" onChange="javascript:myFunction('{$baseActionURL}?current=addedit_dataelements&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;DEPoolSelected={$DEPoolSelected}&amp;strMode={$strMode}')">


                                                                        <xsl:for-each select="LookupExistingSmartformNames">
                                                                            <xsl:variable name="SMARTFORM_intSmartformID"><xsl:value-of select="SMARTFORM_intSmartformID" /></xsl:variable>
                                                                            <option>
                                                                                    <xsl:attribute name="value">
                                                                                        <xsl:value-of select="SMARTFORM_intSmartformID" />
                                                                                    </xsl:attribute>                  
                                                                                    <!-- or ($SMARTFORM_intSmartformID = $DATAELEMENTS_intDataElementDefaultSmartformID) -->

                                                                                         <xsl:if test="$SMARTFORM_intSmartformID=$DE_DefaultSmartformIDSelected ">
                                                                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                                                                         </xsl:if>


                                                                                     <xsl:value-of select="SMARTFORM_strSmartformName" />
                                                                             </option>
                                                                        </xsl:for-each>

                                                             </select>                                   
                                                            </td>
                                                        <!-- DataElement Id -->
                                                            <td class="uportal-text" width="65%">
                                                            <select name="DATAELEMENTS_intDataElementDefaultDataElementID" tabindex="23" class="uportal-input-text">                                     

                                                                        <xsl:for-each select="LookupExistingDataElementId">
                                                                            <xsl:variable name="DATAELEMENTS_intDataElementID"><xsl:value-of select="DATAELEMENTS_intDataElementID" /></xsl:variable>
                                                                            <option>
                                                                                    <xsl:attribute name="value">
                                                                                        <xsl:value-of select="DATAELEMENTS_intDataElementID" />
                                                                                    </xsl:attribute>                  

                                                                                    <xsl:if test="$DATAELEMENTS_intDataElementID=$DE_DefaultDataElementIDSelected">
                                                                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                                                                    </xsl:if>

                                                                                    <xsl:value-of select="DATAELEMENTS_intDataElementID" />
                                                                             </option>
                                                                        </xsl:for-each>

                                                             </select>                            
                                                            </td>

                                                         </tr> 
                                                    </table>  
                                         </td>
                                    </tr>                             
                                    
                                
                                    <!-- Mandatory Start -->
                                    <tr></tr>
                                    <tr>
                                        <td width="20%" class="uportal-label">
                                            <xsl:value-of select="DATAELEMENTS_intMandatoryDisplay" />:
                                        </td>
                                        <td width="80%" class="uportal-label">
                                            
                                             <select size="1" name="DATAELEMENTS_intMandatory" tabindex="12" class="uportal-input-text">
                                                        <option value="0">
                                                                <xsl:if test="$DATAELEMENTS_intMandatory = '0'">
                                                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                                                </xsl:if>
                                                                No
                                                        </option>
                                                        <option value="-1">
                                                                <xsl:if test="$DATAELEMENTS_intMandatory = '-1'">
                                                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                                                </xsl:if>
                                                                Yes
                                                        </option>

                                              </select>                                           

                                        </td>
                                    </tr> 
                                    <!-- Mandatory End -->
                              </xsl:otherwise>
                            </xsl:choose> 
                                                        
                            <!-- Save & Duplicate DE -->
                            <tr>
                                <td width="20%">
                                </td>
                                <td width="80%">
                                    <!--input type="submit" name="saveDataElement" tabindex="24" value="{$saveBtnLabel}" class="uportal-button" onClick="return validDecimal()"/-->
                                    <input type="submit" name="saveDataElement" tabindex="24" value="{$saveBtnLabel}" class="uportal-button"/>
                                    <!-- <input type="submit" name="deleteDataElement" tabindex="6" value="{$deleteBtnLabel}" class="uportal-button" /> -->
                                    <xsl:if test="$strMode != 'newDEClicked'">
                                        <input type="submit" name="duplicateDataElement" tabindex="25" value="Duplicate" class="uportal-button" />               
             
                                    </xsl:if>
                                </td>   
                            </tr>
                        </table>                                  
                        </xsl:if>
              <!--</xsl:if>-->
                <!--    ************************************List Smartforms *****************************> -->
                 <xsl:if test="$DATAELEMENTS_intDataElementType != 'PAGEBREAK' or $strMode != 'addPageBreak'">
                    <xsl:if test="$DATAELEMENTS_intDataElementIDselected != 'null'"> 
                        <hr/>      
                        <table width="100%">
                            <tr>
                                <td width="100%" class="uportal-channel-table-header">
                                    List of SmartForms
                                </td>
                            </tr>
                        </table>
                        <table width="100%">
                            <tr>                 
                                <td width="20%" class="uportal-channel-table-header">
                                    <xsl:value-of select="SMARTFORM_strSmartformNameDisplay" />
                                </td>                        
                                <td width="80%" class="uportal-channel-table-header">
                                    <xsl:value-of select="SMARTFORM_strSmartformDescriptionDisplay" />
                                </td>                  
                             </tr>                 
                        </table>
                        <hr/>


                        <table width="100%">

                             <xsl:for-each select="smartform_list">             
                             <xsl:variable name="SMARTFORM_intSmartformID"><xsl:value-of select="SMARTFORM_intSmartformID" /></xsl:variable> 

                            <tr>                 
                                <td width="20%" class="uportal-label">                            
                                    <a href="{$baseActionURL}?current=view_smartform&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;displaySmartform=true">
                                        <xsl:value-of select="SMARTFORM_strSmartformName" />
                                    </a>
                                </td>

                                <td width="80%" class="uportal-label">
                                    <a href="{$baseActionURL}?current=view_smartform&amp;SMARTFORM_intSmartformID={$SMARTFORM_intSmartformID}&amp;displaySmartform=true">
                                        <xsl:value-of select="SMARTFORM_strSmartformDescription" />
                                    </a>
                                </td>                  
                            </tr>
                            </xsl:for-each> 
                        </table>     
                    </xsl:if>  
                 </xsl:if>    
                
                
            </td>
            <td width="15%"/>
        </tr>
    </table>
    
    
    <!-- Second forms Hidden Fields -->    
    <input type="hidden" name="strErrorMessage" value="{$strErrorMessage}" />
    <input type="hidden" name="SMARTFORM_intSmartformID" value="{$SMARTFORM_intSmartformID}" />
    <input type="hidden" name="DEPoolSelected" value="{$DEPoolSelected}" />  
    <input type="hidden" name="DATAELEMENTS_intDataElementIDselected" value="{$DATAELEMENTS_intDataElementIDselected}" />    
    <input type="hidden" name="DATAELEMENTS_intDataElementTypeSelected" value="{$DATAELEMENTS_intDataElementTypeSelected}" />    
    <br></br>
    <input type="hidden" name="DELookupTypeSelected" value="{$DELookupTypeSelected}" />   
    <input type="hidden" name="DELookupFieldSelected" value="{$DELookupFieldSelected}" /> 
    <br></br>
    <input type="hidden" name="DE_DefaultSmartformIDSelected" value="{$DE_DefaultSmartformIDSelected}" />      
    <input type="hidden" name="DE_DefaultDataElementIDSelected" value="{$DE_DefaultDataElementIDSelected}" /> 
    
    <br></br>
    <input type="hidden" name="SYSTEMLOOKUPWHERE_intDataElementLookupWhereID" value="{$SYSTEMLOOKUPWHERE_intDataElementLookupWhereID}" />
    <input type="hidden" name="DynamicSmartform" value="{$DynamicSmartform}" />
    
    <br></br>
    <input type="hidden" name="strMode" value="{$strMode}" />
    <input type="hidden" name="strRadioVal" value="{$strRadioVal}" />
    
     
    </form>
    </xsl:template>

</xsl:stylesheet>
