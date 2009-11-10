<?xml version="1.0" encoding="utf-8"?>

<!--
    Document   : build_isearch.xml
    Copyright @ 2004 Neuragenix, Inc .  All rights reserved.
    Created on : 19/08/04   
    Author     : Anita Balraj         
-->

<!-- build_isearch stylesheet of ISearch channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
xmlns:url="http://www.jclark.com/xt/java/java.net.URLEncoder" exclude-result-prefixes="url">
<xsl:include href="./isearch_menu.xsl"/>

<xsl:param name="formParams">current=build_isearch</xsl:param>

  <xsl:output method="html" indent="no" />
  <!-- Get the parameters from the channel class -->
 
<xsl:template match="isearch">
    
    <xsl:param name="domainSel"><xsl:value-of select="domainSel" /></xsl:param>
    <xsl:param name="fieldSel"><xsl:value-of select="fieldSel" /></xsl:param>
    <xsl:param name="SelectedDomainSel"><xsl:value-of select="SelectedDomainSel" /></xsl:param>    
    <xsl:param name="selFieldType"><xsl:value-of select="selFieldType" /></xsl:param>
    <xsl:param name="strErrorMessage"><xsl:value-of select="/root/isearch/strErrorMessage" /></xsl:param>       
    <xsl:param name="target"><xsl:value-of select="target" /></xsl:param>        
    <xsl:param name="number"><xsl:value-of select="number" /></xsl:param>  
    <xsl:param name="criteriasDomain"><xsl:value-of select="criteriasDomain" /></xsl:param>  
    <xsl:param name="expanded"><xsl:value-of select="expanded" /></xsl:param>  
    <xsl:param name="collapsed"><xsl:value-of select="collapsed" /></xsl:param>  
    <xsl:param name="strRadioVal"><xsl:value-of select="strRadioVal" /></xsl:param>
    <xsl:param name="strSaveAs"><xsl:value-of select="strSaveAs" /></xsl:param> 
    <xsl:param name="edit"><xsl:value-of select="edit" /></xsl:param>
    <xsl:param name="labelInColumn"><xsl:value-of select="labelInColumn" /></xsl:param>
    <xsl:param name="SensitivityVal"><xsl:value-of select="SensitivityVal" /></xsl:param>
    <xsl:param name="openBracketSel"><xsl:value-of select="openBracketSel" /></xsl:param>      
    <xsl:param name="closeBracketSel"><xsl:value-of select="closeBracketSel" /></xsl:param>                
    <xsl:param name="participantDomainSel"><xsl:value-of select="participantDomainSel" /></xsl:param>                
    <xsl:param name="showJoin"><xsl:value-of select="showJoin" /></xsl:param>   
    <xsl:param name="MissingForListSel"><xsl:value-of select="MissingForListSel" /></xsl:param>   
    <xsl:param name="MissingWithoutListSel"><xsl:value-of select="MissingWithoutListSel" /></xsl:param>       
    
    <xsl:param name="MissingSFDomainListSel"><xsl:value-of select="MissingSFDomainListSel" /></xsl:param>   
    <xsl:param name="MissingSFListSel"><xsl:value-of select="MissingSFListSel" /></xsl:param>       
    <xsl:param name="MissingSFFieldListSel"><xsl:value-of select="MissingSFFieldListSel" /></xsl:param>   

    <xsl:param name="targetMissing"><xsl:value-of select="targetMissing" /></xsl:param> 
    <xsl:param name="targetMissingSF"><xsl:value-of select="targetMissingSF" /></xsl:param>      
    <xsl:param name="MissingCriteriaNo"><xsl:value-of select="MissingCriteriaNo" /></xsl:param>                 
    <xsl:param name="MissingSFCriteriaNo"><xsl:value-of select="MissingSFCriteriaNo" /></xsl:param>                 
    <xsl:param name="visibility1"><xsl:value-of select="visibility1" /></xsl:param>                 
    <xsl:param name="visibility2"><xsl:value-of select="visibility2" /></xsl:param>                     
    <xsl:param name="visibility3"><xsl:value-of select="visibility3" /></xsl:param>                     
    <xsl:param name="blHasDomain"><xsl:value-of select="blHasDomain" /></xsl:param>
    <xsl:param name="blHasSurveyResults"><xsl:value-of select="blHasSurveyResults" /></xsl:param>
    <xsl:param name="strDisplayOption"><xsl:value-of select="strDisplayOption" /></xsl:param>
    <xsl:param name="strDelimiter"><xsl:value-of select="strDelimiter" /></xsl:param>
    <!-- Start Edit/Delete Query     -->
    <xsl:param name="strQueryID"><xsl:value-of select="strQueryID" /></xsl:param>
    <xsl:param name="strQueryName"><xsl:value-of select="strQueryName" /></xsl:param>
    <xsl:param name="strMode"><xsl:value-of select="strMode" /></xsl:param>    
     <!--End Edit/Delete Query -->
     
    <head>
    <script language="javascript">
    
    function change(form,bool) {
          for (i = 0; i &lt; form.length; i++) {
          
            if( form[i].name != "Sensitivity" ){                           
                if (form[i].type == "checkbox") {
                  form[i].checked = bool;
                }
            }
            
          }
      }
      
         
    function mySubmit(suffix){        
        document.frmBuildISearch.action+=suffix;        
        document.frmBuildISearch.submit();       
    }
    
    
    function confirmDeleteQuery(aURL) {
        var confirmAnswer = confirm('Are you sure you want to delete this query?');

        if(confirmAnswer == true){
            window.location=aURL + '&amp;delete_query=true';
        }
    }
    
    function fieldSelection(){
        
        document.frmBuildISearch.labelInColumn.value = document.frmBuildISearch.fieldSel.value;
        document.frmBuildISearch.submit();     
    }
    
    </script>
    </head>
         
    <form name="frmBuildISearch" action="{$baseActionURL}?uP_root=root&amp;{$formParams}" method="post">
    <a name="frmbuild_isearch_anchor"/>
        
        <table width="100%">
            <tr>
                <td class="uportal-channel-subtitle">
                    Basic Search - Advanced Mode<br/><hr/>                   
                </td> 
            </tr> 
        </table>

        <xsl:if test="$strErrorMessage != ''">
        <table width="100%">	 
            <tr>
                <td class="neuragenix-form-required-text">
                    <xsl:value-of select="/root/isearch/strErrorMessage" /><br/><hr/>
                </td>
            </tr>
        </table>
        </xsl:if> 
        
        <table width="100%">
            <tr>
                <td width="100%">
                    <table width="100%">                        
                        <td width="40%" class="uportal-label">
                            Select the dataset
                        </td>

                        <td width="20%" class="uportal-label">                    
                        </td>

                        <td width="40%" class="uportal-label">                  
                            Click on the dataset to display               
                        </td>
                    </table>
                </td>                
            </tr>
            <tr>
                <td width="100%">
                    <table width="100%">
                    <td width="40%">
                    </td>
                    <td width="20%">
                    </td>
                    <td width="40%" class="uportal-label">
                        a list of fields
                    </td>
                    </table>
                </td>
            </tr>
        </table>
        
        <table width="100%">
            <tr>    
                <td width="100%">
                    <table width="100%">
                    <td width="40%" class="uportal-label">
                        <select name="domains" size="5" tabindex="1" class="uportal-input-text">
                            <xsl:choose>
                                <!--<xsl:when test="string-length($strQueryID) > '0'">-->
                                <xsl:when test="$edit = 'true'">
                                    <xsl:for-each select="edit_domains">
                                    <xsl:variable name="strDomainName"><xsl:value-of select="strDomainName" /></xsl:variable>    
                                        <option value="{$strDomainName}">
                                        <xsl:value-of select="../*[name()=concat($strDomainName,'_DisplayName')]" />
                                        </option>
                                    </xsl:for-each>
                                    <!--
                                     <xsl:for-each select="edit_smartforms">
                                        <option style="color: green; font-weight: bold">
                                           <xsl:value-of select="strDomainName" />
                                        </option>
                                    </xsl:for-each>
                                    <xsl:for-each select="edit_studys">
                                        <option style="color: purple; font-weight: bold">
                                            <xsl:value-of select="strDomainName" />
                                        </option>
                                    </xsl:for-each>-->
                                    
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:for-each select="domain">
                                      <xsl:variable name="strDomainName"><xsl:value-of select="strDomainName" /></xsl:variable> 
                                       <option value="{$strDomainName}">
                                        <xsl:value-of select="../*[name()=concat($strDomainName,'_DisplayName')]" />
                                        </option>
                                    </xsl:for-each>
                                    <!--
                                    <xsl:for-each select="smartform">
                                        <option style="color: green; font-weight: bold">
                                            <xsl:value-of select="strDomainName" />
                                        </option>
                                    </xsl:for-each>
                                     <xsl:for-each select="study">
                                        <option style="color: purple; font-weight: bold">
                                            <xsl:value-of select="strDomainName" />
                                        </option>
                                    </xsl:for-each>-->
                                    
                                </xsl:otherwise>
                            </xsl:choose>
                        </select>
                    </td>

                    <td width="20%" class="uportal-label">
                        <table>
                            <tr>
                                <td>
                                    <input type="submit" name="add" tabindex="2" value="  &gt;  " class="uportal-button" />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="submit" name="remove" tabindex="3" value="  &lt;  " class="uportal-button" />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="submit" name="remove_all" tabindex="4" value=" &lt;&lt; " class="uportal-button" />
                                </td>
                            </tr>
                        </table>
                    </td>                

                    <td width="40%" class="uportal-label">
                        <select name="selected_domains" size="5" tabindex="5" class="uportal-input-text" onClick="document.frmBuildISearch.submit();">
                            <xsl:for-each select="selected_domain">
                            <xsl:variable name="strDomainName"><xsl:value-of select="./strDomainName" /></xsl:variable>
                                <option value="{$strDomainName}">
                                    <xsl:if test="$SelectedDomainSel = $strDomainName">
                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>
                                  <xsl:value-of select="../*[name()=concat($strDomainName,'_DisplayName')]"  /> 
                                </option>
                            </xsl:for-each>
                            
                           
                        </select>
                    </td>
                    </table>
                </td>
            </tr>
        </table>
        
        
    
        <xsl:if test="string-length($SelectedDomainSel) > '0' ">
        
        <table width="100%"> 
            <tr align="right"> 
                <td width="10%">
                    <input type="submit" name="performSearch" value="Perform Search" class="uportal-button" />
                </td>                             
            </tr>
        </table>
        
        
        <table width="100%">
                <tr><td>&#160;&#160;&#160;&#160;</td></tr>                     
        </table>
              
         <table width="100%" border="1">
            <tr>                
                <td width="100%" class="uportal-label">
            
                    <xsl:choose>               
                        <xsl:when test="(string-length($visibility1) > '0' and ($visibility1= 'expanded')) or ($edit = 'true')">   
                           
                           <a href="#" onclick="javascript:mySubmit('&amp;hideVisibility1=true')">
                                <img src="media/neuragenix/icons/open.gif" border="0" /> &#160;&#160;<i>Search Criteria</i>
                           </a>
                        
                          
                         </xsl:when>
                         <xsl:otherwise>
                         
                            <a href="#" onclick="javascript:mySubmit('&amp;showVisibility1=true')">
                                <img src="media/neuragenix/icons/closed.gif" border="0"  />&#160;&#160;Search Criteria
                            </a>
                         
                         </xsl:otherwise>
                    </xsl:choose>
                </td>            
                         
            </tr>
            
         </table>
                        
         <xsl:choose>
         <xsl:when test="(string-length($visibility1) > '0' and ($visibility1= 'expanded')) or ($edit = 'true')">   
                          
      
            <table width="100%">
               
                 <tr class="uportal-label">

                    <td width="10%">Open brackets</td>
                    <td width="10%">Join</td>               
                    <td width="20%">Select the field</td>
                    <td width="10%">Specify the type of matching</td>
                    <td width="20%">Specify the search value</td>
                    <td width="10%">Close brackets </td>
                    <td width="20%">
                        <xsl:if test="count(strParticipantDomainLOV) &gt; 0">    
                            Dataset Area
                        </xsl:if>
                    </td>

                </tr>

                <!--<xsl:for-each select="field"> -->
                <xsl:variable name="joinSel"><xsl:value-of select="joinSel" /></xsl:variable>
                <xsl:variable name="fieldSel"><xsl:value-of select="fieldSel" /></xsl:variable>
                <xsl:variable name="opSel"><xsl:value-of select="opSel" /></xsl:variable>  
                <xsl:variable name="searchValue"><xsl:value-of select="searchValue" /></xsl:variable>  
                <xsl:variable name="openBracketSel"><xsl:value-of select="openBracketSel" /></xsl:variable>  
                <xsl:variable name="closeBracketSel"><xsl:value-of select="closeBracketSel" /></xsl:variable> 
                <xsl:variable name="participantDomainSel"><xsl:value-of select="participantDomainSel" /></xsl:variable>                 


                <tr>

                   <td width="10%">
                         <select class="uportal-input-text" name="openBracket">                  
                            <option>
                                <xsl:if test="$openBracketSel = ''">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                            </option> 
                            <option value="(">
                                <xsl:if test="$openBracketSel = '('">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                (
                            </option>

                            <option value="((">
                                <xsl:if test="$openBracketSel = '(('">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                ((
                            </option>

                            <option value="(((">
                                <xsl:if test="$openBracketSel = '((('">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                (((
                            </option>
                        </select>
                   </td>
                   <td width="10%" class="uportal-label">                    
                         <select class="uportal-input-text" name="join">    
                           <xsl:choose>
                               <xsl:when test="$showJoin = 'false'">   
                                    <option value="">
                                        <xsl:if test="$joinSel = ''">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                    </option> 
                               </xsl:when>
                               <xsl:otherwise>
                                    <option value="">
                                        <xsl:if test="$joinSel = ''">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                    </option> 
                                    <option value="AND">
                                        <xsl:if test="$joinSel = 'AND'">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        And
                                    </option>

                                    <option value="OR">
                                        <xsl:if test="$joinSel = 'OR'">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        Or
                                    </option>

                                    <option value="EXCLUDING">
                                        <xsl:if test="$joinSel = 'EXCLUDING'">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                        Excluding
                                    </option>
                                </xsl:otherwise>
                            </xsl:choose>
                        </select>
                    </td>

                    <td width="20%">                             
                        <select name="field" class="uportal-input-text" onChange="document.frmBuildISearch.submit();">
                            <!--<option value=""/>-->
                            <xsl:for-each select="FieldList">
                            <xsl:variable name="InternalName"><xsl:value-of select="InternalName" /></xsl:variable>
                            <xsl:variable name="Label"><xsl:value-of select="Label" /></xsl:variable> 

                               <!-- <option value="{$Label}">   -->

                                <option value="{$InternalName}"> 
                                    <xsl:choose>
                                    <xsl:when test="$target = 'editCriteria'">

                                        <xsl:if test="$labelInColumn = $Label">
                                            <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                    </xsl:when>
                                    <xsl:otherwise>

                                        <xsl:if test="$fieldSel = $InternalName">                                      
                                             <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>
                                    </xsl:otherwise>
                                    </xsl:choose>

                                    <xsl:value-of select="Label" />                                    
                                </option>
                            </xsl:for-each>

                            <xsl:for-each select="DataelementsList">
                            <xsl:variable name="DATAELEMENTS_intDataElementID"><xsl:value-of select="DATAELEMENTS_intDataElementID" /></xsl:variable>
                            <xsl:variable name="DATAELEMENTS_strDataElementName"><xsl:value-of select="DATAELEMENTS_strDataElementName" /></xsl:variable> 

                                <option value="{$DATAELEMENTS_intDataElementID}">                             
                                   <xsl:choose>
                                       <xsl:when test="$target = 'editCriteria'">
                                           <xsl:if test="$labelInColumn = $DATAELEMENTS_strDataElementName">                                   
                                                 <xsl:attribute name="selected">true</xsl:attribute> 
                                           </xsl:if>
                                       </xsl:when>
                                       <xsl:otherwise>                                            
                                            <xsl:if test="$fieldSel = $DATAELEMENTS_intDataElementID">
                                                <xsl:attribute name="selected">true</xsl:attribute>
                                            </xsl:if>
                                       </xsl:otherwise>
                                   </xsl:choose>

                                    <xsl:value-of select="DATAELEMENTS_strDataElementName" />
                                </option>
                            </xsl:for-each>


                        </select>                 
                    </td>

                    <td width="10%">
                        <select class="uportal-input-text" name="operatorType">                        
                            
                            <option>
                                <xsl:if test="$opSel = 'Contains'">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                Contains
                            </option>   
                            <option>
                                <xsl:if test="$opSel = 'Is equal to'">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                Is equal to
                            </option>
                            <option>
                                <xsl:if test="$opSel = 'Is less than'">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                Is less than
                            </option>
                            <option>
                                <xsl:if test="$opSel = 'Is greater than'">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                Is greater than
                            </option>
                            <option>
                                <xsl:if test="$opSel = 'Greater than equals to'">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                Greater than equals to
                            </option>  
                            <option>
                                <xsl:if test="$opSel = 'Less than equals to'">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                Less than equals to
                            </option>    
                            <option>
                                <xsl:if test="$opSel = 'Is empty'">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                Is empty
                            </option>   
                            <option>
                                <xsl:if test="$opSel = 'Is not empty'">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                Is not empty
                            </option>   


                        </select>
                    </td>


                    <td width="20%">           
                        <xsl:choose>
                        <xsl:when test="$selFieldType = 'LOV'">
                            <select name="searchValue" class="uportal-input-text">
                            <xsl:for-each select="ListOfValues">
                            <xsl:variable name="LOV"><xsl:value-of select="LOV" /></xsl:variable>                        
                                <option value="{$LOV}">                            
                                    <xsl:if test="$LOV = $searchValue">
                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>
                                    <xsl:value-of select="LOV" />
                                </option>
                            </xsl:for-each>
                            </select>    
                        </xsl:when>
                        <xsl:otherwise>                
                            <input type="text" class="uportal-input-text" size="20" name="searchValue" value="{$searchValue}"/>                  
                        </xsl:otherwise>
                        </xsl:choose>

                    </td>       
                    <td width="10%">

                         <select class="uportal-input-text" name="closeBracket">                  
                            <option>
                                <xsl:if test="$closeBracketSel = ''">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                            </option> 
                            <option value=")">
                                <xsl:if test="$closeBracketSel = ')'">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                )
                            </option>

                            <option value="))">
                                <xsl:if test="$closeBracketSel = '))'">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                ))
                            </option>

                            <option value=")))">
                                <xsl:if test="$closeBracketSel = ')))'">
                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>
                                )))
                            </option>
                        </select>

                    </td>

                    <!-- select if smartform domain is PAT/BIO -->         
                    <td width="20%">


                        
                        <xsl:if test="count(strParticipantDomainLOV) &gt; '0'">
                        <select class="uportal-input-text" name="participantDomain">                  
                        <xsl:for-each select="strParticipantDomainLOV/Domain">
                            <xsl:variable name="partDomain">
                                    <xsl:value-of select="."/>
                            </xsl:variable>
                            <option>
                                    <xsl:attribute name="value">
                                            <xsl:value-of select="."/>
                                    </xsl:attribute>
                                    <xsl:if test="$participantDomainSel = $partDomain">
                                            <xsl:attribute name="selected">true</xsl:attribute>
                                    </xsl:if>
                                    <xsl:value-of select="../../*[name()=concat($partDomain,'_DisplayName')]" />                                    
                            </option>
			</xsl:for-each>        
                        </select>
                        </xsl:if>
                        

                    </td>

                </tr>
               <!-- </xsl:for-each> -->
            </table>

            <!-- Add Criteria or string-length($number) > '0' -->
            <table width="100%">
                <tr>
                    <td width="90%">
                    </td>
                    <td width="10%">
                    <xsl:choose>
                        <xsl:when test="$target = 'editCriteria' or $number != 'null'">                        
                            <input type="submit" name="updateCriteria" value="Update" class="uportal-button" /> 

                        </xsl:when>
                        <xsl:otherwise>
                            <input type="submit" name="addCriteria" value="Add Criteria" class="uportal-button" />                     
                        </xsl:otherwise>
                    </xsl:choose>
                    </td>
                </tr>
                <tr></tr>
            </table>
        <!--</xsl:if>-->
        <!-- *** -->
        <!-- Criterias-->
        <table width="100%" border="1">
            <tr>
                <td width="100%" class="uportal-label">
                   Criterias are:
                </td>               
            </tr>
        </table>
        <table width="100%" border="1">

            <!-- Added Criterias -->
            <xsl:for-each select="searchCriteria">
            <xsl:variable name="openBracketSel"><xsl:value-of select="openBracketSel" /></xsl:variable>                
            <xsl:variable name="joinSel"><xsl:value-of select="joinSel" /></xsl:variable>                
            <xsl:variable name="domainVal"><xsl:value-of select="domainVal" /></xsl:variable>
            <xsl:variable name="fieldSel"><xsl:value-of select="fieldSel" /></xsl:variable>
            <xsl:variable name="opSel"><xsl:value-of select="opSel" /></xsl:variable>
            <xsl:variable name="searchValue"><xsl:value-of select="searchValue" /></xsl:variable>
            <xsl:variable name="criteriaNo"><xsl:value-of select="criteriaNo" /></xsl:variable>
            <xsl:variable name="labelInColumn"><xsl:value-of select="labelInColumn" /></xsl:variable>
            <xsl:variable name="closeBracketSel"><xsl:value-of select="closeBracketSel" /></xsl:variable>                
            <xsl:variable name="participantDomainSel"><xsl:value-of select="participantDomainSel" /></xsl:variable>                

            <input type="hidden" name="criteriaNo" value="{$criteriaNo}"/>

            <tr>
                    <xsl:choose>
                    <xsl:when test="position() mod 2 != 0">
                            <td width="10%" class="uportal-input-text">
                                    <xsl:value-of select="openBracketSel" />
                                    <input type="hidden" name="openBracketSel{$criteriaNo}" value="{$openBracketSel}"/>
                            </td>
                            <td width="5%" class="uportal-input-text">
                                    <xsl:value-of select="joinSel" />
                                    <input type="hidden" name="joinSel{$criteriaNo}" value="{$joinSel}"/>
                            </td>

                            <td width="5%" class="uportal-input-text">
                                    <xsl:value-of select="../*[name()=concat($domainVal,'_DisplayName')]"/>
                                    <input type="hidden" name="domainVal{$criteriaNo}" value="{$domainVal}"/>'s
                            </td>

                            <td width="20%" class="uportal-input-text">
                                    <xsl:value-of select="labelInColumn" />
                                    <input type="hidden" name="labelInColumn{$criteriaNo}" value="{$labelInColumn}"/>                                    
                            </td>

                            <td width="20%" class="uportal-input-text">
                                    <xsl:value-of select="opSel" />
                                    <input type="hidden" name="opSel{$criteriaNo}" value="{$opSel}"/>
                            </td>      

                            <td width="10%" class="uportal-input-text">
                                    <xsl:value-of select="searchValue" />
                                     <input type="hidden" name="searchValue{$criteriaNo}" class="uportal-input-text" size="20" value="{$searchValue}" />                  

                            </td> 
                           <td width="10%" class="uportal-input-text">
                                    <xsl:value-of select="closeBracketSel" />
                                    <input type="hidden" name="closeBracketSel{$criteriaNo}" value="{$closeBracketSel}"/>
                            </td>
                            <td width="10%" class="uportal-input-text">
                                    <xsl:if test="$participantDomainSel != 'blank'">
                                        <xsl:value-of select="../*[name()=concat($participantDomainSel,'_DisplayName')]"/>
                                    </xsl:if>
                                    <input type="hidden" name="participantDomainSel{$criteriaNo}" value="{$participantDomainSel}"/>
                            </td>                                              

                            <td width="5%" class="uportal-input-text">
                           
                                <a href="#" onclick="javascript:mySubmit('&amp;target=editCriteria&amp;fieldSel={$fieldSel}&amp;SelectedDomainSel={$domainVal}&amp;selFieldType={$selFieldType}&amp;opSel={$opSel}&amp;searchValue={$searchValue}&amp;joinSel={$joinSel}&amp;number={$criteriaNo}&amp;labelInColumn={$labelInColumn}&amp;openBracketSel={$openBracketSel}&amp;closeBracketSel={$closeBracketSel}&amp;participantDomainSel={$participantDomainSel}')">Edit</a>
                                  
                            </td>                      
                            <td width="5%" class="uportal-input-text">

                                <a href="#" onclick="javascript:mySubmit('&amp;delete=true&amp;fieldSel={$fieldSel}&amp;SelectedDomainSel={$domainVal}&amp;selFieldType={$selFieldType}&amp;opSel={$opSel}&amp;searchValue={$searchValue}&amp;joinSel={$joinSel}&amp;number={$criteriaNo}&amp;labelInColumn={$labelInColumn}&amp;openBracketSel={$openBracketSel}&amp;closeBracketSel={$closeBracketSel}&amp;participantDomainSel={$participantDomainSel}')">Delete</a>
                            
                            <!--/td>
                            <td width="10%"-->                                
                              
                                <input type="hidden" name="fieldSel{$criteriaNo}" value="{$fieldSel}"/>
                            </td>
                    </xsl:when>
                    <xsl:otherwise>

                         <td width="10%" class="uportal-text">
                                    <xsl:value-of select="openBracketSel" />
                                    <input type="hidden" name="openBracketSel{$criteriaNo}" value="{$openBracketSel}"/>
                         </td>

                        <td width="5%" class="uportal-text">
                                <xsl:value-of select="joinSel" />
                                <input type="hidden" name="joinSel{$criteriaNo}" value="{$joinSel}"/>
                        </td>

                        <td width="5%" class="uportal-text">
                                <xsl:value-of select="../*[name()=concat($domainVal,'_DisplayName')]"/>
                                <input type="hidden" name="domainVal{$criteriaNo}" value="{$domainVal}"/>'s
                        </td>

                        <td width="20%" class="uportal-text">
                            <xsl:value-of select="labelInColumn" />
                            <input type="hidden" name="labelInColumn{$criteriaNo}" value="{$labelInColumn}"/> 
                        </td>

                        <td width="20%" class="uportal-text">
                                <xsl:value-of select="opSel" />
                                <input type="hidden" name="opSel{$criteriaNo}" value="{$opSel}"/>
                        </td>      

                        <td width="10%" class="uportal-text">
                                <xsl:value-of select="searchValue" />
                             <input type="hidden"  name="searchValue{$criteriaNo}" class="uportal-input-text" size="20" value="{$searchValue}" />   

                        </td> 
                        <td width="10%" class="uportal-text">
                                <xsl:value-of select="closeBracketSel" />
                                <input type="hidden" name="closeBracketSel{$criteriaNo}" value="{$closeBracketSel}"/>
                        </td>

                        <td width="10%" class="uportal-text">
                                    <xsl:if test="$participantDomainSel != 'blank'">
                                        <xsl:value-of select="../*[name()=concat($participantDomainSel,'_DisplayName')]"/>
                                    </xsl:if>
                                    <input type="hidden" name="participantDomainSel{$criteriaNo}" value="{$participantDomainSel}"/>
                        </td>

                        <td width="5%" class="uportal-text">
                        
                            <a href="#" onclick="javascript:mySubmit('&amp;target=editCriteria&amp;fieldSel={$fieldSel}&amp;SelectedDomainSel={$domainVal}&amp;selFieldType={$selFieldType}&amp;opSel={$opSel}&amp;searchValue={$searchValue}&amp;joinSel={$joinSel}&amp;number={$criteriaNo}&amp;labelInColumn={$labelInColumn}&amp;openBracketSel={$openBracketSel}&amp;closeBracketSel={$closeBracketSel}&amp;participantDomainSel={$participantDomainSel}')">Edit</a>

                            
                        </td>                      
                        <td width="5%" class="uportal-text">
                       
                            <a href="#" onclick="javascript:mySubmit('&amp;delete=true&amp;fieldSel={$fieldSel}&amp;SelectedDomainSel={$domainVal}&amp;selFieldType={$selFieldType}&amp;opSel={$opSel}&amp;searchValue={$searchValue}&amp;joinSel={$joinSel}&amp;number={$criteriaNo}&amp;labelInColumn={$labelInColumn}&amp;openBracketSel={$openBracketSel}&amp;closeBracketSel={$closeBracketSel}&amp;participantDomainSel={$participantDomainSel}')">Delete</a>
                               
                        <!--/td>
                        <td width="10%"-->                                    
                            <input type="hidden" name="fieldSel{$criteriaNo}" value="{$fieldSel}"/>                            
                        </td>
                    </xsl:otherwise>
                    </xsl:choose>
            </tr>
            </xsl:for-each>       
            </table>
            
            <!-- Added Smartform Criterias Start -->
            <table width="100%">
                <tr><td>&#160;&#160;&#160;&#160;</td></tr>                     
            </table>
            
            <table width="100%" border="1">            
             <tr>
                <td width="100%" class="uportal-label">
                   Smartform Criterias are:
                </td>               
            </tr>
            </table>
            <table width="100%" border="1">
            
            <xsl:for-each select="smartformCriteria">
            <xsl:variable name="openBracketSel"><xsl:value-of select="openBracketSel" /></xsl:variable>                
            <xsl:variable name="joinSel"><xsl:value-of select="joinSel" /></xsl:variable>                
            <xsl:variable name="domainVal"><xsl:value-of select="domainVal" /></xsl:variable>
            <xsl:variable name="fieldSel"><xsl:value-of select="fieldSel" /></xsl:variable>
            <xsl:variable name="opSel"><xsl:value-of select="opSel" /></xsl:variable>
            <xsl:variable name="searchValue"><xsl:value-of select="searchValue" /></xsl:variable>
            <xsl:variable name="criteriaNo"><xsl:value-of select="criteriaNo" /></xsl:variable>
            <xsl:variable name="labelInColumn"><xsl:value-of select="labelInColumn" /></xsl:variable>
            <xsl:variable name="closeBracketSel"><xsl:value-of select="closeBracketSel" /></xsl:variable>                
            <xsl:variable name="participantDomainSel"><xsl:value-of select="participantDomainSel" /></xsl:variable>                

            <input type="hidden" name="criteriaNo" value="{$criteriaNo}"/>

            <tr>
                    <xsl:choose>
                    <xsl:when test="position() mod 2 != 0">
                            <td width="10%" class="uportal-input-text">
                                    <xsl:value-of select="openBracketSel" />
                                    <input type="hidden" name="openBracketSel{$criteriaNo}" value="{$openBracketSel}"/>
                            </td>
                            <td width="5%" class="uportal-input-text">
                                    <xsl:value-of select="joinSel" />
                                    <input type="hidden" name="joinSel{$criteriaNo}" value="{$joinSel}"/>
                            </td>

                            <td width="5%" class="uportal-input-text">
                                    <xsl:value-of select="domainVal" />
                                    <input type="hidden" name="domainVal{$criteriaNo}" value="{$domainVal}"/>'s
                            </td>

                            <td width="20%" class="uportal-input-text">
                                    <xsl:value-of select="labelInColumn" />
                                    <input type="hidden" name="labelInColumn{$criteriaNo}" value="{$labelInColumn}"/>                                    
                            </td>

                            <td width="20%" class="uportal-input-text">
                                    <xsl:value-of select="opSel" />
                                    <input type="hidden" name="opSel{$criteriaNo}" value="{$opSel}"/>
                            </td>      

                            <td width="10%" class="uportal-input-text">
                                    <xsl:value-of select="searchValue" />
                                     <input type="hidden" name="searchValue{$criteriaNo}" class="uportal-input-text" size="20" value="{$searchValue}" />                  

                            </td> 
                           <td width="10%" class="uportal-input-text">
                                    <xsl:value-of select="closeBracketSel" />
                                    <input type="hidden" name="closeBracketSel{$criteriaNo}" value="{$closeBracketSel}"/>
                            </td>
                            <td width="10%" class="uportal-input-text">
                                    <xsl:if test="$participantDomainSel != 'blank'">
                                            <xsl:value-of select="../*[name()=concat($participantDomainSel,'_DisplayName')]"/>
                                    </xsl:if>
                                    <input type="hidden" name="participantDomainSel{$criteriaNo}" value="{$participantDomainSel}"/>
                            </td>                                              

                            <td width="5%" class="uportal-input-text">
                           
                                <a href="#" onclick="javascript:mySubmit('&amp;target=editCriteria&amp;fieldSel={$fieldSel}&amp;SelectedDomainSel={$domainVal}&amp;selFieldType={$selFieldType}&amp;opSel={$opSel}&amp;searchValue={$searchValue}&amp;joinSel={$joinSel}&amp;number={$criteriaNo}&amp;labelInColumn={$labelInColumn}&amp;openBracketSel={$openBracketSel}&amp;closeBracketSel={$closeBracketSel}&amp;participantDomainSel={$participantDomainSel}')">Edit</a>
                           
                            </td>                      
                            <td width="5%" class="uportal-input-text">

                                <a href="#" onclick="javascript:mySubmit('&amp;delete=true&amp;fieldSel={$fieldSel}&amp;SelectedDomainSel={$domainVal}&amp;selFieldType={$selFieldType}&amp;opSel={$opSel}&amp;searchValue={$searchValue}&amp;joinSel={$joinSel}&amp;number={$criteriaNo}&amp;labelInColumn={$labelInColumn}&amp;openBracketSel={$openBracketSel}&amp;closeBracketSel={$closeBracketSel}&amp;participantDomainSel={$participantDomainSel}')">Delete</a>
                            
                            <!--/td>
                            <td width="10%"-->                                
                                
                                <input type="hidden" name="fieldSel{$criteriaNo}" value="{$fieldSel}"/>
                            </td>
                    </xsl:when>
                    <xsl:otherwise>

                         <td width="10%" class="uportal-text">
                                    <xsl:value-of select="openBracketSel" />
                                    <input type="hidden" name="openBracketSel{$criteriaNo}" value="{$openBracketSel}"/>
                         </td>

                        <td width="5%" class="uportal-text">
                                <xsl:value-of select="joinSel" />
                                <input type="hidden" name="joinSel{$criteriaNo}" value="{$joinSel}"/>
                        </td>

                        <td width="5%" class="uportal-text">
                                <xsl:value-of select="domainVal" />
                                 <input type="hidden" name="domainVal{$criteriaNo}" value="{$domainVal}"/>'s
                        </td>

                        <td width="20%" class="uportal-text">
                            <xsl:value-of select="labelInColumn" />
                            <input type="hidden" name="labelInColumn{$criteriaNo}" value="{$labelInColumn}"/> 
                        </td>

                        <td width="20%" class="uportal-text">
                                <xsl:value-of select="opSel" />
                                <input type="hidden" name="opSel{$criteriaNo}" value="{$opSel}"/>
                        </td>      

                        <td width="10%" class="uportal-text">
                                <xsl:value-of select="searchValue" />
                             <input type="hidden"  name="searchValue{$criteriaNo}" class="uportal-input-text" size="20" value="{$searchValue}" />   

                        </td> 
                        <td width="10%" class="uportal-text">
                                <xsl:value-of select="closeBracketSel" />
                                <input type="hidden" name="closeBracketSel{$criteriaNo}" value="{$closeBracketSel}"/>
                        </td>

                        <td width="10%" class="uportal-text">
                                    <xsl:if test="$participantDomainSel != 'blank'">
                                        <xsl:value-of select="../*[name()=concat($participantDomainSel,'_DisplayName')]"/>
                                    </xsl:if>
                                    <input type="hidden" name="participantDomainSel{$criteriaNo}" value="{$participantDomainSel}"/>
                        </td>

                        <td width="5%" class="uportal-text">
                        
                            <a href="#" onclick="javascript:mySubmit('&amp;target=editCriteria&amp;fieldSel={$fieldSel}&amp;SelectedDomainSel={$domainVal}&amp;selFieldType={$selFieldType}&amp;opSel={$opSel}&amp;searchValue={$searchValue}&amp;joinSel={$joinSel}&amp;number={$criteriaNo}&amp;labelInColumn={$labelInColumn}&amp;openBracketSel={$openBracketSel}&amp;closeBracketSel={$closeBracketSel}&amp;participantDomainSel={$participantDomainSel}')">Edit</a>

                        </td>                      
                        <td width="5%" class="uportal-text">
                        
                            <a href="#" onclick="javascript:mySubmit('&amp;delete=true&amp;fieldSel={$fieldSel}&amp;SelectedDomainSel={$domainVal}&amp;selFieldType={$selFieldType}&amp;opSel={$opSel}&amp;searchValue={$searchValue}&amp;joinSel={$joinSel}&amp;number={$criteriaNo}&amp;labelInColumn={$labelInColumn}&amp;openBracketSel={$openBracketSel}&amp;closeBracketSel={$closeBracketSel}&amp;participantDomainSel={$participantDomainSel}')">Delete</a>
                        
                        <!--/td>
                        <td width="10%"-->
                           
                            <input type="hidden" name="fieldSel{$criteriaNo}" value="{$fieldSel}"/>                            
                        </td>
                    </xsl:otherwise>
                    </xsl:choose>
            </tr>
            </xsl:for-each>       
            </table>
            
            <!-- Added Smartform Criterias End -->
        <!--/table-->  

        </xsl:when><!-- End of if checking if the visibility1 is expanded-->
        <xsl:otherwise>
            
            <xsl:for-each select="searchCriteria">
            <xsl:variable name="criteriaNo"><xsl:value-of select="criteriaNo" /></xsl:variable>
            <xsl:variable name="openBracketSel"><xsl:value-of select="openBracketSel" /></xsl:variable>                
            <xsl:variable name="joinSel"><xsl:value-of select="joinSel" /></xsl:variable>                
            <xsl:variable name="domainVal"><xsl:value-of select="domainVal" /></xsl:variable>
            <xsl:variable name="fieldSel"><xsl:value-of select="fieldSel" /></xsl:variable>
            <xsl:variable name="opSel"><xsl:value-of select="opSel" /></xsl:variable>
            <xsl:variable name="searchValue"><xsl:value-of select="searchValue" /></xsl:variable>
            <xsl:variable name="labelInColumn"><xsl:value-of select="labelInColumn" /></xsl:variable>
            <xsl:variable name="closeBracketSel"><xsl:value-of select="closeBracketSel" /></xsl:variable>                
            <xsl:variable name="participantDomainSel"><xsl:value-of select="participantDomainSel" /></xsl:variable>                
            

            <input type="hidden" name="openBracketSel{$criteriaNo}" value="{$openBracketSel}"/>
            <input type="hidden" name="joinSel{$criteriaNo}" value="{$joinSel}"/> 
            <input type="hidden" name="domainVal{$criteriaNo}" value="{$domainVal}"/> 
            <input type="hidden" name="fieldSel{$criteriaNo}" value="{$fieldSel}"/>
            <input type="hidden" name="opSel{$criteriaNo}" value="{$opSel}"/> 
            <input type="hidden" name="searchValue{$criteriaNo}" value="{$searchValue}"/>
            <input type="hidden" name="labelInColumn{$criteriaNo}" value="{$labelInColumn}"/> 
            <input type="hidden" name="closeBracketSel{$criteriaNo}" value="{$closeBracketSel}"/> 
            <input type="hidden" name="participantDomainSel{$criteriaNo}" value="{$participantDomainSel}"/> 
            
            <input type="hidden" name="criteriaNo" value="{$criteriaNo}"/>
            </xsl:for-each>
        
            <xsl:for-each select="smartformCriteria">
            <xsl:variable name="criteriaNo"><xsl:value-of select="criteriaNo" /></xsl:variable>
            <xsl:variable name="openBracketSel"><xsl:value-of select="openBracketSel" /></xsl:variable>                
            <xsl:variable name="joinSel"><xsl:value-of select="joinSel" /></xsl:variable>                
            <xsl:variable name="domainVal"><xsl:value-of select="domainVal" /></xsl:variable>
            <xsl:variable name="fieldSel"><xsl:value-of select="fieldSel" /></xsl:variable>
            <xsl:variable name="opSel"><xsl:value-of select="opSel" /></xsl:variable>
            <xsl:variable name="searchValue"><xsl:value-of select="searchValue" /></xsl:variable>
            <xsl:variable name="labelInColumn"><xsl:value-of select="labelInColumn" /></xsl:variable>
            <xsl:variable name="closeBracketSel"><xsl:value-of select="closeBracketSel" /></xsl:variable>                
            <xsl:variable name="participantDomainSel"><xsl:value-of select="participantDomainSel" /></xsl:variable>                
            

            <input type="hidden" name="openBracketSel{$criteriaNo}" value="{$openBracketSel}"/>
            <input type="hidden" name="joinSel{$criteriaNo}" value="{$joinSel}"/> 
            <input type="hidden" name="domainVal{$criteriaNo}" value="{$domainVal}"/> 
            <input type="hidden" name="fieldSel{$criteriaNo}" value="{$fieldSel}"/>
            <input type="hidden" name="opSel{$criteriaNo}" value="{$opSel}"/> 
            <input type="hidden" name="searchValue{$criteriaNo}" value="{$searchValue}"/>
            <input type="hidden" name="labelInColumn{$criteriaNo}" value="{$labelInColumn}"/> 
            <input type="hidden" name="closeBracketSel{$criteriaNo}" value="{$closeBracketSel}"/> 
            <input type="hidden" name="participantDomainSel{$criteriaNo}" value="{$participantDomainSel}"/> 
            
            <input type="hidden" name="criteriaNo" value="{$criteriaNo}"/>
            </xsl:for-each>
        </xsl:otherwise>
        </xsl:choose>

       
        
        <table width="100%">
            <tr><td>&#160;&#160;&#160;&#160;</td></tr>
                
        </table>
        <!-- Search Missing Records Start -->
        <table width="100%" border="1">
            
            <tr>
                <td width="100%" class="uportal-label">
                     <xsl:choose>               
                        <xsl:when test="(string-length($visibility2) > '0' and ($visibility2= 'expanded')) or ($edit = 'true')">   
                           
                           <a href="#" onclick="javascript:mySubmit('&amp;hideVisibility2=true')">
                                <img src="media/neuragenix/icons/open.gif" border="0" /> &#160;&#160;<i>Search Missing Records Criteria</i>
                           </a>                        
                          
                         </xsl:when>
                         <xsl:otherwise>
                         
                            <a href="#" onclick="javascript:mySubmit('&amp;showVisibility2=true')">
                                <img src="media/neuragenix/icons/closed.gif" border="0"  />&#160;&#160;Search Missing Records Criteria
                            </a>
                         
                         </xsl:otherwise>
                    </xsl:choose>
                
                    
                </td>
            </tr>
        </table>
            
        <xsl:choose>
        <xsl:when test="(string-length($visibility2) > '0' and ($visibility2= 'expanded')) or ($edit = 'true')">   
                         
        <table width="100%">
            <tr>
                <td width="100%" class="uportal-label">
                    Search for &#160;&#160;
               </td>
             </tr>
             <tr>
                <td width="100%" class="uportal-label">
                    <select name="MissingForList" class="uportal-input-text" onChange="javascript:mySubmit('&amp;targetMissing={$targetMissing}')">
                            
                            <xsl:for-each select="selected_domain">
                            <xsl:variable name="strDomainName"><xsl:value-of select="./strDomainName" /></xsl:variable>
                            <xsl:variable name="displaystrDomainName"><xsl:value-of select="../*[name()=concat($strDomainName,'_DisplayName')]"  /> </xsl:variable> 
                            
                            <xsl:if test="$strDomainName != 'CONSENT' and ($strDomainName != 'CONSENTSTUDY') and ($strDomainName != 'STUDY')">
                            
                                <option value="{$strDomainName}">
                                    <xsl:if test="$MissingForListSel = $strDomainName">
                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>

                                    <xsl:choose>
                                        <xsl:when test="(string-length($displaystrDomainName)) = '0'">
                                            <xsl:value-of select="strDomainName"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of select="../*[name()=concat($strDomainName,'_DisplayName')]"  /> 
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </option>
                                
                            </xsl:if>
                            </xsl:for-each>
                          
                            
                    </select>                    
                    &#160;&#160;
                
                    without&#160;&#160;
               
                     <select name="MissingWithoutList" class="uportal-input-text">
                            
                            <xsl:for-each select="MissingWithoutList">
                            <xsl:variable name="strDomainName"><xsl:value-of select="./strDomainName" /></xsl:variable>
                            <xsl:variable name="displaystrDomainName"><xsl:value-of select="../*[name()=concat($strDomainName,'_DisplayName')]"  /> </xsl:variable> 
                            <xsl:if test="$strDomainName != 'STUDY'">
                            
                                <option value="{$strDomainName}">
                                    <xsl:if test="$MissingWithoutListSel = $strDomainName">
                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>
                                    
                                    <xsl:choose>
                                        <xsl:when test="(string-length($displaystrDomainName)) = '0'">
                                            <xsl:value-of select="strDomainName"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of select="../*[name()=concat($strDomainName,'_DisplayName')]"  /> 
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </option>
                                
                            </xsl:if>
                            </xsl:for-each>                            
                            
                    </select>   &#160;&#160;
               
                 </td>
            </tr>
            <tr>
                <td width="90%">
                </td>
                <td width="10%">
                
                    <xsl:choose>
                        <xsl:when test="$targetMissing = 'editCriteria'">
                            <input type="submit" name="updateMissingCriteria" value="Update" class="uportal-button" />                            
                        </xsl:when>
                        <xsl:otherwise>
                            <input type="submit" name="addMissingCriteria" value="Add Criteria" class="uportal-button" />                     
                        </xsl:otherwise>
                    </xsl:choose>                    
                    
                </td>
            </tr>
        </table> 
        
      
        <table width="100%" border="1">   
                    
            <tr>
                <td width="100%" class="uportal-label">
                    Search for :
                </td>
            </tr>
        </table>
        <table width="100%" border="1">
                    
            <xsl:for-each select="searchMissingCriterias">
            <xsl:variable name="MissingForSel"><xsl:value-of select="./MissingForSel" /></xsl:variable>
            <xsl:variable name="MissingWithoutSel"><xsl:value-of select="./MissingWithoutSel" /></xsl:variable>                                              
            <xsl:variable name="MissingCriteriaNo"><xsl:value-of select="./MissingCriteriaNo" /></xsl:variable>
            <xsl:variable name="displayMissingForSel"><xsl:value-of select="../*[name()=concat($MissingForSel,'_DisplayName')]"  /> </xsl:variable> 
            <xsl:variable name="displayMissingWithoutSel"><xsl:value-of select="../*[name()=concat($MissingWithoutSel,'_DisplayName')]"  /> </xsl:variable> 
             
                <tr>
                    <xsl:choose>
                    <xsl:when test="position() mod 2 != 0">                    
                       
                         <input type="hidden" name="MissingCriteriaNo{$MissingCriteriaNo}" value="{$MissingCriteriaNo}"/>
                            
                         <td width="23%" class="uportal-input-text">
                            <xsl:choose>
                                <xsl:when test="(string-length($displayMissingForSel)) = '0'">
                                    <xsl:value-of select="MissingForSel"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="../*[name()=concat($MissingForSel,'_DisplayName')]"  /> 
                                </xsl:otherwise>
                            </xsl:choose>
                            <!--xsl:value-of select="./MissingForSel" /--> 
                            <input type="hidden" name="MissingForSel{$MissingCriteriaNo}" value="{$MissingForSel}"/>
                            
                         </td>
                         <td width="10%" class="uportal-input-text">                   
                           &#160; without &#160;
                            
                         </td>                          
                         <td width="22%" class="uportal-input-text">
                            <xsl:choose>
                                <xsl:when test="(string-length($displayMissingWithoutSel)) = '0'">
                                    <xsl:value-of select="MissingWithoutSel"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="../*[name()=concat($MissingWithoutSel,'_DisplayName')]"  /> 
                                </xsl:otherwise>
                            </xsl:choose>
                            <input type="hidden" name="MissingWithoutSel{$MissingCriteriaNo}" value="{$MissingWithoutSel}"/>                            
                            
                         </td>
                         <td width="20%" class="uportal-input-text">
                            <a href="#" onclick="javascript:mySubmit('&amp;targetMissing=editCriteria&amp;selFieldType={$selFieldType}&amp;MissingCriteriaNo={$MissingCriteriaNo}&amp;MissingForListSel={$MissingForSel}&amp;MissingWithoutListSel={$MissingWithoutSel}')">Edit</a>
                            
                         </td>
                         <td width="25%" class="uportal-input-text">
                             <a href="#" onclick="javascript:mySubmit('&amp;deleteMissingCriteria=true&amp;selFieldType={$selFieldType}&amp;MissingCriteriaNo={$MissingCriteriaNo}&amp;MissingForListSel={$MissingForSel}&amp;MissingWithoutListSel={$MissingWithoutSel}')">Delete</a>
                          
                         </td>                

                       
                    </xsl:when>
                    <xsl:otherwise>
                                          
                            <input type="hidden" name="MissingCriteriaNo{$MissingCriteriaNo}" value="{$MissingCriteriaNo}"/>
                            
                        <td width="23%" class="uportal-text">
                            <xsl:choose>
                                <xsl:when test="(string-length($displayMissingForSel)) = '0'">
                                    <xsl:value-of select="MissingForSel"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="../*[name()=concat($MissingForSel,'_DisplayName')]"  /> 
                                </xsl:otherwise>
                            </xsl:choose>
                            <input type="hidden" name="MissingForSel{$MissingCriteriaNo}" value="{$MissingForSel}"/>
                                                       
                         </td>
                         <td width="10%" class="uportal-text">                   
                           &#160; without &#160;
                            
                         </td>                          
                         <td width="22%" class="uportal-text">
                            <xsl:choose>
                                <xsl:when test="(string-length($displayMissingWithoutSel)) = '0'">
                                    <xsl:value-of select="MissingWithoutSel"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="../*[name()=concat($MissingWithoutSel,'_DisplayName')]"  /> 
                                </xsl:otherwise>
                            </xsl:choose>
                            <input type="hidden" name="MissingWithoutSel{$MissingCriteriaNo}" value="{$MissingWithoutSel}"/>                            
                                                
                         </td>
                         <td width="20%" class="uportal-text">
                            <a href="#" onclick="javascript:mySubmit('&amp;targetMissing=editCriteria&amp;selFieldType={$selFieldType}&amp;MissingCriteriaNo={$MissingCriteriaNo}&amp;MissingForListSel={$MissingForSel}&amp;MissingWithoutListSel={$MissingWithoutSel}')">Edit</a>
                            
                         </td>
                         <td width="25%" class="uportal-text">
                             <a href="#" onclick="javascript:mySubmit('&amp;deleteMissingCriteria=true&amp;selFieldType={$selFieldType}&amp;MissingCriteriaNo={$MissingCriteriaNo}&amp;MissingForListSel={$MissingForSel}&amp;MissingWithoutListSel={$MissingWithoutSel}')">Delete</a>
                          
                         </td>                            
                       
                    </xsl:otherwise>
                    </xsl:choose>                    
                    
                </tr>            
            </xsl:for-each>
        </table>
        </xsl:when> <!-- End of Visibility2 if missing records criteria is expanded-->
        <xsl:otherwise>        
            <xsl:for-each select="searchMissingCriterias">
            <xsl:variable name="MissingForSel"><xsl:value-of select="./MissingForSel" /></xsl:variable>
            <xsl:variable name="MissingWithoutSel"><xsl:value-of select="./MissingWithoutSel" /></xsl:variable>                                              
            <xsl:variable name="MissingCriteriaNo"><xsl:value-of select="./MissingCriteriaNo" /></xsl:variable>
            <input type="hidden" name="MissingCriteriaNo{$MissingCriteriaNo}" value="{$MissingCriteriaNo}"/>
            <input type="hidden" name="MissingForSel{$MissingCriteriaNo}" value="{$MissingForSel}"/>
            <input type="hidden" name="MissingWithoutSel{$MissingCriteriaNo}" value="{$MissingWithoutSel}"/>
            </xsl:for-each>            
        </xsl:otherwise>
        </xsl:choose>
        
        
        <table width="100%">
            <tr><td>&#160;&#160;&#160;&#160;</td></tr>             
        </table>
        <!-- Search Missing Records End -->       


        <!-- Search Missing Smartform Records Start -->
        <table width="100%" border="1">
            
            <tr>                
                <td width="100%" class="uportal-label">
                     <xsl:choose>               
                        <xsl:when test="(string-length($visibility3) > '0' and ($visibility3= 'expanded')) or ($edit = 'true')">   
                           
                           <a href="#" onclick="javascript:mySubmit('&amp;hideVisibility3=true')">
                                <img src="media/neuragenix/icons/open.gif" border="0" /> &#160;&#160;<i>Search Missing Smartform Records Criteria</i>
                           </a>                        
                          
                         </xsl:when>
                         <xsl:otherwise>
                         
                            <a href="#" onclick="javascript:mySubmit('&amp;showVisibility3=true')">
                                <img src="media/neuragenix/icons/closed.gif" border="0"  />&#160;&#160;Search Missing Smartform Records Criteria
                            </a>
                         
                         </xsl:otherwise>
                    </xsl:choose>
                
                    
                </td>
            </tr>
        </table>
            
        <xsl:choose>
        <xsl:when test="(string-length($visibility3) > '0' and ($visibility3= 'expanded')) or ($edit = 'true')">   
                         
        <table width="100%">
            <tr>
               <td width="100%" class="uportal-label">
                    Click on a smartform in the dataset in order to select a question  &#160;&#160;
               </td>
             </tr>
             <tr>
                    <td width="100%" class="uportal-label">                    
                    Question &#160;&#160;
                    <select name="MissingSFFieldList" class="uportal-input-text" onChange="javascript:mySubmit('&amp;targetMissingSF={$targetMissingSF}')">

                        <xsl:for-each select="DataelementsList">
                        <xsl:variable name="DATAELEMENTS_intDataElementID"><xsl:value-of select="DATAELEMENTS_intDataElementID" /></xsl:variable>
                        <xsl:variable name="DATAELEMENTS_strDataElementName"><xsl:value-of select="DATAELEMENTS_strDataElementName" /></xsl:variable> 

                            <option value="{$DATAELEMENTS_intDataElementID}">                             
                                <xsl:if test="$MissingSFFieldListSel = $DATAELEMENTS_intDataElementID">
                                    <xsl:attribute name="selected">true</xsl:attribute>
                                </xsl:if>

                                <xsl:value-of select="DATAELEMENTS_strDataElementName" />
                            </option>
                        </xsl:for-each>

                    </select>                     
                    
                    &#160;&#160;                
                    in Domain&#160;&#160;
               
                    <select name="MissingSFDomainList" class="uportal-input-text" onChange="javascript:mySubmit('&amp;targetMissingSF={$targetMissingSF}')">
                            
                            <xsl:for-each select="selected_domain">
                            <xsl:variable name="strDomainName"><xsl:value-of select="./strDomainName" /></xsl:variable>
                            <xsl:if test="$strDomainName != 'CONSENT' and ($strDomainName != 'CONSENTSTUDY') and ($strDomainName != 'STUDY') and ($strDomainName != 'BIOANALYSIS') and ($strDomainName != 'BIOSPECIMEN TRANSACTIONS')">
                            
                                <option value="{$strDomainName}">
                                    <xsl:if test="$MissingSFDomainListSel = $strDomainName">
                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>
                                    
                                    <xsl:value-of select="../*[name()=concat($strDomainName,'_DisplayName')]"  />
                                </option>
                                
                            </xsl:if>
                            </xsl:for-each>
                          
                            
                    </select>                    
                                        
                 </td>
            </tr>
            <tr>
                <td width="90%">
                </td>
                <td width="10%">
                
                    <xsl:choose>
                        <xsl:when test="$targetMissingSF = 'editCriteria'">
                            <input type="submit" name="updateMissingSFCriteria" value="Update" class="uportal-button" />                            
                        </xsl:when>
                        <xsl:otherwise>
                            <input type="submit" name="addMissingSFCriteria" value="Add Criteria" class="uportal-button" />                     
                        </xsl:otherwise>
                    </xsl:choose>                    
                    
                </td>
            </tr>
        </table> 
        
        
      
        <table width="100%" border="1">   
                    
            <tr>
                <td width="100%" class="uportal-label">
                    Search for :
                </td>
            </tr>
        </table>
        <table width="100%" border="1">
                    
            <xsl:for-each select="searchMissingSFCriterias">
            <xsl:variable name="MissingSFDomainSel"><xsl:value-of select="./MissingSFDomainSel" /></xsl:variable>
            <xsl:variable name="MissingSFSel"><xsl:value-of select="./MissingSFSel" /></xsl:variable>                                              
            <xsl:variable name="MissingSFFieldSel"><xsl:value-of select="./MissingSFFieldSel" /></xsl:variable>                                              
            <xsl:variable name="MissingSFCriteriaNo"><xsl:value-of select="./MissingSFCriteriaNo" /></xsl:variable>
            
                <tr>
                    <xsl:choose>
                    <xsl:when test="position() mod 2 != 0">                    
                       
                             <input type="hidden" name="MissingSFCriteriaNo{$MissingSFCriteriaNo}" value="{$MissingSFCriteriaNo}"/>
                            
                         <td width="23%" class="uportal-input-text">
                            <xsl:value-of select="./MissingSFSel" /> 
                            <input type="hidden" name="MissingSFSel{$MissingSFCriteriaNo}" value="{$MissingSFSel}"/>
                            
                         </td>
                         
                         <td width="10%" class="uportal-input-text">                   
                           &#160; question &#160;
                            
                         </td>                          
                         
                         <td width="22%" class="uportal-input-text">
                            <xsl:value-of select="./MissingSFFieldNameSel" />  
                            <input type="hidden" name="MissingSFFieldSel{$MissingSFCriteriaNo}" value="{$MissingSFFieldSel}"/>                            
                            
                         </td>

                         <td width="10%" class="uportal-input-text">                   
                           &#160; in Domain &#160;
                            
                         </td>                          

                         <td width="22%" class="uportal-input-text">
                            <!--xsl:value-of select="./MissingSFDomainSel" /--> 
                            <xsl:value-of select="../*[name()=concat($MissingSFDomainSel,'_DisplayName')]"  /> 
                            <input type="hidden" name="MissingSFDomainSel{$MissingSFCriteriaNo}" value="{$MissingSFDomainSel}"/>                            
                            
                         </td>

                         
                         <td width="20%" class="uportal-input-text">
                            <a href="#" onclick="javascript:mySubmit('&amp;targetMissingSF=editCriteria&amp;selFieldType={$selFieldType}&amp;MissingSFCriteriaNo={$MissingSFCriteriaNo}&amp;MissingSFDomainListSel={$MissingSFDomainSel}&amp;MissingSFFieldListSel={$MissingSFFieldSel}')">Edit</a>
                            
                         </td>
                         <td width="25%" class="uportal-input-text">
                             <a href="#" onclick="javascript:mySubmit('&amp;deleteMissingSFCriteria=true&amp;selFieldType={$selFieldType}&amp;MissingSFCriteriaNo={$MissingSFCriteriaNo}&amp;MissingSFDomainListSel={$MissingSFDomainSel}&amp;MissingSFFieldListSel={$MissingSFFieldSel}')">Delete</a>
                          
                         </td>                

                        
                    </xsl:when>
                    <xsl:otherwise>
                                          
                             <input type="hidden" name="MissingSFCriteriaNo{$MissingSFCriteriaNo}" value="{$MissingSFCriteriaNo}"/>
                            
                        <td width="23%" class="uportal-text">
                            <xsl:value-of select="./MissingSFSel" /> 
                            <input type="hidden" name="MissingSFSel{$MissingSFCriteriaNo}" value="{$MissingSFSel}"/>
                                                       
                         </td>
                         
                         <td width="10%" class="uportal-text">                   
                           &#160; question &#160;
                            
                         </td>                          
                         
                         <td width="22%" class="uportal-text">
                            <xsl:value-of select="./MissingSFFieldSel" />  
                            <input type="hidden" name="MissingSFFieldSel{$MissingSFCriteriaNo}" value="{$MissingSFFieldSel}"/>                            
                                                
                         </td>
                         
                         <td width="10%" class="uportal-text">                   
                           &#160; in Domain &#160;
                            
                         </td>                          
                         <td width="22%" class="uportal-text">
                            <xsl:value-of select="../*[name()=concat($MissingSFDomainSel,'_DisplayName')]"  />
                            <!--xsl:value-of select="./MissingSFDomainSel" /-->  
                            <input type="hidden" name="MissingSFDomainSel{$MissingSFCriteriaNo}" value="{$MissingSFDomainSel}"/>                            
                                                
                         </td>


                         <td width="20%" class="uportal-text">
                            <a href="#" onclick="javascript:mySubmit('&amp;targetMissingSF=editCriteria&amp;selFieldType={$selFieldType}&amp;MissingSFCriteriaNo={$MissingSFCriteriaNo}&amp;MissingSFDomainListSel={$MissingSFDomainSel}&amp;MissingSFFieldListSel={$MissingSFFieldSel}')">Edit</a>
                            
                         </td>
                         <td width="25%" class="uportal-text">
                             <a href="#" onclick="javascript:mySubmit('&amp;deleteMissingSFCriteria=true&amp;selFieldType={$selFieldType}&amp;MissingSFCriteriaNo={$MissingSFCriteriaNo}&amp;MissingSFDomainListSel={$MissingSFDomainSel}&amp;MissingSFFieldListSel={$MissingSFFieldSel}')">Delete</a>
                          
                         </td>                            
                       
                    </xsl:otherwise>
                    </xsl:choose>                    
                    
                </tr>            
            </xsl:for-each>
        </table>
        </xsl:when> <!-- End of Visibility3 if missing records criteria is expanded-->
        <xsl:otherwise>        
            <xsl:for-each select="searchMissingSFCriterias">
            <xsl:variable name="MissingSFDomainSel"><xsl:value-of select="./MissingSFDomainSel" /></xsl:variable>
            <xsl:variable name="MissingSFSel"><xsl:value-of select="./MissingSFSel" /></xsl:variable>                                              
            <xsl:variable name="MissingSFFieldSel"><xsl:value-of select="./MissingSFFieldSel" /></xsl:variable>                                              
            <xsl:variable name="MissingSFCriteriaNo"><xsl:value-of select="./MissingSFCriteriaNo" /></xsl:variable>
            <input type="hidden" name="MissingSFCriteriaNo{$MissingSFCriteriaNo}" value="{$MissingSFCriteriaNo}"/>
            <input type="hidden" name="MissingSFSel{$MissingSFCriteriaNo}" value="{$MissingSFSel}"/>
            <input type="hidden" name="MissingSFFieldSel{$MissingSFCriteriaNo}" value="{$MissingSFFieldSel}"/>
            <input type="hidden" name="MissingSFDomainSel{$MissingSFCriteriaNo}" value="{$MissingSFDomainSel}"/>
            </xsl:for-each>            
        </xsl:otherwise>
        </xsl:choose>        
        
        
        <table width="100%">
            <tr><td>&#160;&#160;&#160;&#160;</td></tr>             
        </table>
        <!-- Search Missing Smartform Records End -->        
                
        <table width="100%" border="1">   
            
            <tr>
                <td class="uportal-label" width="100%">
                    Select fields to be displayed
                </td>
            </tr>
            
            <tr>
                <td width="100%" class="uportal-label">
                    
                    <xsl:variable name="criteriasDomain"><xsl:value-of select="criteriasDomain" /></xsl:variable>                        
                    <!--criteriasDomain : <xsl:value-of select="criteriasDomain" />-->
                     
                       <xsl:for-each select="buildCriteriasDomains">
                       <xsl:variable name="domainName"><xsl:value-of select="domainName" /></xsl:variable> 
                       <xsl:variable name="status"><xsl:value-of select="status" /></xsl:variable> 
                       <table width="100%">
                       <!--domainname: <xsl:value-of select="domainName" />
                       status: <xsl:value-of select="status" />-->
                       <input type="hidden" name="status{$domainName}" value="{$status}"/>
            
                       
                            <tr>
                                <a name="{$domainName}_Anchor"/>
                                <td width="30%" class="uportal-label">                      

                                    <xsl:variable name="displayDomainName"><xsl:value-of select="../*[name()=concat($domainName,'_DisplayName')]"  /> </xsl:variable>                                                                                      
                                    <xsl:choose>
                                       
                                        <xsl:when test="(string-length($status) > '0' and ($status = 'expanded')) or ($edit = 'true')">   
                                       
                                           <a href="#" onclick="javascript:mySubmit('&amp;criteriasDomain={$domainName}&amp;collapsed=true&amp;closed={$domainName}&amp;#{$domainName}_Anchor')">
                                                <img src="media/neuragenix/icons/open.gif" border="0" />&#160;&#160;
                                                <i>
                                                <xsl:choose>
                                                    <xsl:when test="(string-length($displayDomainName)) = '0'">
                                                        <xsl:value-of select="domainName"/>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:value-of select="../*[name()=concat($domainName,'_DisplayName')]"  /> 
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                </i>
                                           </a>
                                         </xsl:when>
                                         <xsl:otherwise>
                                           
                                            <a href="#" onclick="javascript:mySubmit('&amp;criteriasDomain={$domainName}&amp;expanded=true&amp;open={$domainName}&amp;#{$domainName}_Anchor')">
                                                <img src="media/neuragenix/icons/closed.gif" border="0"  />&#160;&#160;
                                                <xsl:choose>
                                                    <xsl:when test="(string-length($displayDomainName)) = '0'">
                                                        <xsl:value-of select="domainName"/>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:value-of select="../*[name()=concat($domainName,'_DisplayName')]"  /> 
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </a>
                                         </xsl:otherwise>
                                     </xsl:choose>
                                </td>
                               
                                <td width="70%"></td>
                                </tr>
                                <tr><td width="30%"></td>
                                <td width="70%">                               
                                    
                                           <xsl:if test="(string-length($status) > '0' and ($status = 'expanded')) or ($edit = 'true')">
                                        
                                            <xsl:for-each select="/root/isearch/selectFields">
                                                <xsl:variable name="InternalName"><xsl:value-of select="./InternalName" /></xsl:variable> 
                                                <!--<xsl:variable name="Label"><xsl:value-of select="/root/isearch/selectFields/Label" /></xsl:variable> -->
                                                <xsl:variable name="fieldForDomain"><xsl:value-of select="./fieldForDomain" /></xsl:variable>
                                                
                                                <xsl:if test="$fieldForDomain = $domainName">
                                                <table width="100%" > 
                                                <tr>
                                                    <td width="5%">
                                                        <xsl:if test="$InternalName != 'nocheckbox'">
                                                        <input type="checkbox"> 
                                                        <xsl:attribute name="name"><xsl:value-of select="./InternalName" /></xsl:attribute>                                                      

                                                            <!-- Show the checkboxes already selected -->
                                                            <xsl:for-each select="/root/isearch/ListOfDomainsAndFields">                  
                                                                <xsl:variable name="NameOfDomain" select="./NameOfDomain"/>
                                                                <xsl:variable name="CheckedFields" select="./CheckedFields"/>
                                                                <xsl:variable name="forcedField" select="./CheckedFields/@forced" />
                                                                                                                                
                                                                <xsl:if test="$NameOfDomain = $domainName">                                                                    
                                                                    <xsl:if test="$InternalName = $CheckedFields">
                                                                        <xsl:attribute name="checked">true</xsl:attribute>     
                                                                        <xsl:if test="$forcedField='true'">
                                                                            <xsl:attribute name="onClick">javascript:this.checked=true;</xsl:attribute>
                                                                        </xsl:if>                                                                   
                                                                    </xsl:if>
                                                                </xsl:if>
                                                           </xsl:for-each>                                                  
                                                        </input>  
                                                        </xsl:if>
                                                    </td>
                                                    <td width="40%" class="uportal-label">
                                                        <xsl:value-of select="./Label" />                                                      
          
                                                    </td>       
                                                    <td width="55%"></td>
                                                </tr>
                                                </table>
                                                </xsl:if>                                      
                                                
                                            </xsl:for-each>
                                            
                                       </xsl:if> 
                                                                        
                                </td><td width="75%"></td>
                            </tr>
                            </table>
                       </xsl:for-each>
                     
                 </td>
            </tr>            
            
            <tr>
                <td width="20%">                    
                    <input type="button" name="display_all" tabindex="5" value="Display All" class="uportal-button" onClick="javascript:change(frmBuildISearch,true)"/>
                    <input type="button" name="clear_all" tabindex="6" value="Clear All" class="uportal-button" onClick="javascript:change(frmBuildISearch,false)"/>                            
                           
                </td>
                <td width="80%">
                </td>                
            </tr>
          </table>

          <table width="100%"> 
            <tr> 
             
            </tr>
            
             <tr>

                <td width="65%">                            
                </td>
                <td width="5%">
                    <input type="checkbox">
                        <xsl:attribute name="name">Sensitivity</xsl:attribute>                   
                        
                        <xsl:if test="$SensitivityVal != 'null'">
                            <xsl:attribute name="checked">true</xsl:attribute>                                                                        
                        </xsl:if>
                    </input>
                </td>
                <td width="15%" class="uportal-label"> 
                    Case Sensitive                                 
                </td>
                <td width="15%" align="right">
                    <input type="submit" name="performSearch" value="Perform Search" class="uportal-button" />
                </td>                
             </tr>
         </table>
             
         <table width="100%" border="1">
            <tr></tr>
           <tr>
             <td width="10%" class="uportal-label">
                Display query as
             </td>
             <td width="90%" class="uportal-label">
               
             </td>
           </tr>
           <tr>
              <td width="100%">
                 <table width="100%">
                 
                 <xsl:variable name="strNewRadioVal"><xsl:value-of select="strNewRadioVal"/></xsl:variable> 
                 
                  <td width="5%">
                    <input type="RADIO" name="rdDisplayAs" value="simplified">    
                         <xsl:choose>
                         <xsl:when test="(string-length($strNewRadioVal) > '0')">
                         <xsl:if test="$strNewRadioVal = 'simplified' or ($strNewRadioVal = '')">
                            <xsl:attribute name="checked">1</xsl:attribute>                              
                         </xsl:if>
                         </xsl:when>
                         <xsl:otherwise>
                         <xsl:if test="$strRadioVal = 'simplified'">
                            <xsl:attribute name="checked">1</xsl:attribute>                              
                         </xsl:if>
                         </xsl:otherwise>
                         </xsl:choose>
                         <xsl:attribute name="onClick">javascript:mySubmit('&amp;changeDisplayMode=true');</xsl:attribute>
                    </input>
                    
                    
                  </td>
                  <td width="10%" class="uportal-label">
                    Simplified
                  </td>
                  
                  <td width="5%">
                    <input type="RADIO" name="rdDisplayAs" value="normal">  
                         <xsl:choose>
                         <xsl:when test="(string-length($strNewRadioVal) > '0')">
                         <xsl:if test="$strNewRadioVal = 'normal'">
                            <xsl:attribute name="checked">1</xsl:attribute>                              
                         </xsl:if>
                         </xsl:when>
                         <xsl:otherwise>
                         <xsl:if test="$strRadioVal = 'normal'">
                            <xsl:attribute name="checked">1</xsl:attribute>
                         </xsl:if>
                         </xsl:otherwise>
                         </xsl:choose>
                         <xsl:attribute name="onClick">javascript:mySubmit('&amp;changeDisplayMode=true');</xsl:attribute>
                    </input>
                    
                  </td>                  
                  <td width="10%" class="uportal-label">
                    Normal
                  </td>
                  
                  <td width="5%">
                  
                    <input type="RADIO" name="rdDisplayAs" value="advanced"> 
                         <xsl:choose>
                         <xsl:when test="(string-length($strNewRadioVal) > '0')">
                         <xsl:if test="$strNewRadioVal = 'advanced'">
                            <xsl:attribute name="checked">1</xsl:attribute>                              
                         </xsl:if>
                         </xsl:when>
                         <xsl:otherwise>
                         <xsl:if test="$strRadioVal = 'advanced' or ($strRadioVal = '')">
                            <xsl:attribute name="checked">1</xsl:attribute> 
                         </xsl:if>
                         </xsl:otherwise>
                         </xsl:choose>
                    </input>
                    
                  </td>
                  <td width="65%" class="uportal-label">
                    Advanced
                  </td>
                 </table>
              </td>
           </tr>
         </table>
         
         <table width="100%" border="1">
            <tr></tr>
             <tr>
                <td width="100%" class="uportal-label">
                    Save or Delete Query
                </td>
             </tr>
             <tr> 
                <td width="100%">
                    <table width="100%">
                        <td width="8%" class="uportal-label">
                            Specify name
                        </td>
                        <td width="10%">
                            <input type="text" name="QueryName" class="uportal-input-text" size="20" value="{$strQueryName}"/>
                        </td> 
                        <td width="25%" class="uportal-label">
                            to save this query as
                        </td>
                        <td width="20%" class="uportal-label">
                            <select name="saveAs" class="uportal-input-text">
                                <option value="my favourite">
                                    <xsl:if test="$strSaveAs = 'my favourite'">
                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>
                                    
                                    my favourite
                                </option>
                                <option value="for all users">
                                    <xsl:if test="$strSaveAs = 'for all users'">
                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                    </xsl:if>
                                    
                                    for all users
                                </option>                    
                            </select>
                        </td>
                        <td width="32%">
                             <input type="submit" name="save_query" value="Save Query" class="uportal-button" />
                        </td>
                    </table>
                </td>
             </tr>
             </table>
             
             <table width="100%" border="1">
             <tr>
                <td width="100%" class="uportal-label">
                    Click on this button to delete this query&#160;&#160;
               
                    <xsl:if test="function-available('url:encode')">
                        <input type="button" name="delete_query" value="Delete" class="uportal-button" onclick="javascript:confirmDeleteQuery('{$baseActionURL}?current=build_isearch&amp;delete_query=true&amp;domainSel={$domainSel}&amp;fieldSel={$fieldSel}&amp;SelectedDomainSel={$SelectedDomainSel}&amp;selFieldType={$selFieldType}&amp;number={$number}&amp;strRadioVal={$strRadioVal}&amp;strQueryID={$strQueryID}&amp;strQueryName={url:encode($strQueryName)}&amp;labelInColumn={$labelInColumn}')" />                                                                                                                                                                                                                                                                                                                                                                                                                                                           
                    </xsl:if>
                </td>
              </tr>     
                     
                     <input type="hidden" name="domainSel" class="uportal-input-text" size="10" value="{$domainSel}"/>
                     <input type="hidden" name="fieldSel" class="uportal-input-text" size="10" value="{$fieldSel}"/>
                     <input type="hidden" name="SelectedDomainSel" class="uportal-input-text" size="10" value="{$SelectedDomainSel}"/>                     
                     <input type="hidden" name="selFieldType" class="uportal-input-text" size="10" value="{$selFieldType}"/>
                     <input type="hidden" name="number" class="uportal-input-text" size="10" value="{$number}"/>
                     <input type="hidden" name="strRadioVal" class="uportal-input-text" size="10" value="{$strRadioVal}"/>
                     <input type="hidden" name="strQueryID" class="uportal-input-text" size="10" value="{$strQueryID}"/>
                     <input type="hidden" name="strQueryName" class="uportal-input-text" size="10" value="{$strQueryName}"/>                     
                     <input type="hidden" name="labelInColumn" class="uportal-input-text" size="10" value="{$labelInColumn}"/>
                     <input type="hidden" name="SensitivityVal" class="uportal-input-text" size="10" value="{$SensitivityVal}"/>                     
                     <input type="hidden" name="openBracketSel" class="uportal-input-text" size="10" value="{$openBracketSel}"/>                     
                     <input type="hidden" name="closeBracketSel" class="uportal-input-text" size="10" value="{$closeBracketSel}"/>                     
                     <input type="hidden" name="participantDomainSel" class="uportal-input-text" size="10" value="{$participantDomainSel}"/>                                          
                     <input type="hidden" name="MissingForListSel" class="uportal-input-text" size="10" value="{$MissingForListSel}"/>                     
                     <input type="hidden" name="MissingWithoutListSel" class="uportal-input-text" size="10" value="{$MissingWithoutListSel}"/>                       
                     <input type="hidden" name="MissingSFListSel" class="uportal-input-text" size="10" value="{$MissingSFListSel}"/>                     
                     <input type="hidden" name="MissingSFDomainListSel" class="uportal-input-text" size="10" value="{$MissingSFDomainListSel}"/>                       
                     <input type="hidden" name="MissingSFFieldListSel" class="uportal-input-text" size="10" value="{$MissingSFFieldListSel}"/>                       
                     <input type="hidden" name="MissingCriteriaNo" class="uportal-input-text" size="10" value="{$MissingCriteriaNo}"/> 
                     <input type="hidden" name="MissingSFCriteriaNo" class="uportal-input-text" size="10" value="{$MissingSFCriteriaNo}"/>                                          
                     <input type="hidden" name="FAVOURITE_QUERY_strQueryName1" class="uportal-input-text" size="10" value="{$strQueryName}"/>
                     <input type="hidden" name="strMode" class="uportal-input-text" size="10" value="{$strMode}"/>
        </table>
      
        </xsl:if> 
    </form>

</xsl:template>
 
</xsl:stylesheet>
