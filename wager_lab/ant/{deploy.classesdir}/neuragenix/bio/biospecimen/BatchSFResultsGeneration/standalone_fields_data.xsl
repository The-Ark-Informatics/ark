<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./header.xsl"/>
    <xsl:include href="./common_javascript.xsl"/>
        
    <xsl:template match="body">        
        <xsl:variable name="backLocation"><xsl:value-of select="backLocation"/></xsl:variable>
        <xsl:call-template name="common_javascript"/>    
        <xsl:call-template name="header">
                <xsl:with-param name="activeSubtab">smartform</xsl:with-param>
                <xsl:with-param name="previousButtonUrl"><xsl:value-of select="$baseActionURL"/>?module=BATCH_SFRESULTS_GENERATION&amp;action=submit_cloneable_fields_data&amp;Back=true</xsl:with-param>
                <xsl:with-param name="previousButtonFlag">true</xsl:with-param>
                
        </xsl:call-template> 
        <xsl:variable name="varSelectedSF">
                    <xsl:value-of select="selectedSmartformID"/>
        </xsl:variable>                    
        <xsl:variable name="varSelectedSFName">
                    <xsl:value-of select="selectedSmartformName"/>
        </xsl:variable>          
        <xsl:variable name="singlePartGeneration">          
            <xsl:value-of select="singlePartGeneration"/>
        </xsl:variable>   
        
        <script language="javascript" >
        function jumpToPickerList(aURL) 
        {
            document.standaloneDataForm.action = aURL;
            document.standaloneDataForm.submit();
        }
        
        function uploadAttachment(aURL) 
        {        
            document.standaloneDataForm.action = aURL;
            document.standaloneDataForm.submit();
        }        

        function deleteAttachment(aURL) 
        {        
            document.standaloneDataForm.action = aURL;
            document.standaloneDataForm.submit();
        }          
        
	function checkboxs() 
	{		
		for (var ax=0; ax &lt; document.forms[0].elements.length; ax++) 
		{
			var elem = document.forms[0].elements[ax];
			if (elem.type == 'checkbox') 
			{
				elem.checked='checked'
		    }
		}
		
	}
	function changeValue(a)
	{
		
		if (a.value == 'yes')
		{
			a.value='no'
		}
		else
		{
			a.value='yes'
		}	
		//window.alert (a.value)
	}        
        </script>             
        
        <table width="100%">       
        <tr>
            <td class="neuragenix-form-required-text">
                <xsl:value-of select="ErrorMsg"/>
            </td>
        </tr>            
        </table>

        <form name="standaloneDataForm" action="{$baseActionURL}?module=BATCH_SFRESULTS_GENERATION&amp;action=submit_standalone_fields_data" method="post" enctype="multipart/form-data" onSubmit="javascript:checkboxs();">
        <table width="100%">
            <tr>
                <td class="uportal-label">
                    Enter the data for each smartform
                </td>
            </tr>            
        </table>
        <br/>
 
        <table width="100%" border="0" cellspacing="0" cellpadding="2">
             <!-- Column Heading Row -->
             <tr>
                <td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
                <td class="stripped_column_heading">
                    Biospecimen ID
                </td>
                
                <xsl:for-each select="SF">
                <xsl:variable name="SFNum"> <xsl:value-of select="SFNum" /></xsl:variable>   
                <xsl:if test="($SFNum = '0')">   
                    <xsl:for-each select="StandaloneField">                 
                        <td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
                        <td class="stripped_column_heading">
                            <xsl:value-of select="Name" />
                        </td>
                   </xsl:for-each>                                         
                </xsl:if>
                </xsl:for-each>     
             </tr>  
             <!-- Fill in Smartform Data -->                 
             <xsl:for-each select="SF">
                <tr>
                    <!-- Determine background colour of each row -->
                    <xsl:choose>
                        <xsl:when test="position() mod 2 != 0">
                                <xsl:attribute name="class">stripped_light</xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                                <xsl:attribute name="class">stripped_dark</xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>                                          
                    
                    <td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
                    <td>
                        <xsl:value-of select="ParticipantID" />
                    </td>
                    <xsl:for-each select="StandaloneField">       
                        <td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
                        <td>
                            <xsl:variable name="varDataElementType"> <xsl:value-of select="Type"/></xsl:variable>   
                            <xsl:choose>                            
                                <!-- TEXT data element -->
                                <xsl:when test="$varDataElementType = 'TEXT' or $varDataElementType = 'REPEATABLE TEXT'">
                                    <textarea  class="uportal-input-text">
                                        <xsl:attribute name="name"><xsl:value-of select="../SFNum"/>_<xsl:value-of select="ID" /></xsl:attribute>
                                        <xsl:attribute name="value"><xsl:value-of select="Result" /></xsl:attribute>
                                        <xsl:value-of select="Result" />
                                    </textarea>
                                    
                                </xsl:when>

                                <!-- NUMERIC data element -->
                                <xsl:when test="$varDataElementType = 'NUMERIC'">
                                    <input type="text" class="uportal-input-text" style="text-align: left" >
                                        <xsl:attribute name="name"><xsl:value-of select="../SFNum"/>_<xsl:value-of select="ID" /></xsl:attribute>
                                        <xsl:attribute name="value"><xsl:value-of select="Result" /></xsl:attribute>                                
                                    </input>
                                </xsl:when>

                                <!-- MONETARY data element -->
                                <xsl:when test="$varDataElementType = 'MONETARY'">
                                    <input type="text" class="uportal-input-text" style="text-align: left">
                                        <xsl:attribute name="name"><xsl:value-of select="../SFNum"/>_<xsl:value-of select="ID" /></xsl:attribute>
                                        <xsl:attribute name="value"><xsl:value-of select="Result" /></xsl:attribute>

                                    </input>

                                </xsl:when>
                                
                                <xsl:when test="$varDataElementType = 'CHECK BOX'">
                                    <xsl:variable name="varName"><xsl:value-of select="../SFNum"/>_<xsl:value-of select="ID" /></xsl:variable>
                                    <xsl:variable name="varValue"><xsl:value-of select="Result" /></xsl:variable>
                                    <input onClick="javascript:changeValue(this);" type="checkbox" name="{$varName}" class="uportal-input-text" style="text-align: left" >
                                        <xsl:choose>
                                        <xsl:when test="$varValue = 'yes'">
                                                <xsl:attribute name="checked" >checked</xsl:attribute>
                                                <xsl:attribute name="value" >yes</xsl:attribute>
                                        </xsl:when>	
                                         <xsl:otherwise>
                                                <xsl:attribute name="value" >no</xsl:attribute>
                                         </xsl:otherwise>
                                        </xsl:choose>
                                    </input>
                                </xsl:when>                                

                                <!-- DROPDOWN data element -->
                                <xsl:when test="$varDataElementType = 'DROPDOWN'">
                                        <xsl:variable name="varName"><xsl:value-of select="../SFNum"/>_<xsl:value-of select="ID" /></xsl:variable>

                                    <select name="{$varName}" class="uportal-input-text">
                                        <option value=""/>                    

                                        <xsl:for-each select="option">                                    
                                            <option>
                                                <xsl:attribute name="value"><xsl:value-of select="optionvalue" /></xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                                </xsl:if>
                                                <xsl:value-of select="optionlabel" />
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </xsl:when>

                                <!-- DATE data element -->
                                <xsl:when test="$varDataElementType = 'DATE'">
                                    <xsl:variable name="varName"><xsl:value-of select="../SFNum"/>_<xsl:value-of select="ID" /></xsl:variable>
                                    <xsl:variable name="varYear"><xsl:value-of select="Year" /></xsl:variable>
                                    <select name="{$varName}_Day" class="uportal-input-text">
                                        <xsl:for-each select="Day">
                                            <option>
                                                <xsl:attribute name="value"><xsl:value-of select="." /></xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                                </xsl:if>
                                                <xsl:value-of select="." />
                                            </option>
                                        </xsl:for-each>
                                    </select>

                                    <select name="{$varName}_Month" class="uportal-input-text">
                                        <xsl:for-each select="Month">
                                            <option>
                                                <xsl:attribute name="value"><xsl:value-of select="." /></xsl:attribute>
                                                <xsl:if test="@selected=1">
                                                    <xsl:attribute name="selected">true</xsl:attribute> 
                                                </xsl:if>
                                                <xsl:value-of select="." />
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                    <input type="text" name="{$varName}_Year" size="5" value="{$varYear}" class="uportal-input-text" />
                                </xsl:when>
                                
                                <!-- Attachment Dataelement Start -->

                                <xsl:when test="$varDataElementType = 'ATTACHMENT'">
                                    <xsl:variable name="varName"><xsl:value-of select="../SFNum" />_<xsl:value-of select="ID" /></xsl:variable>
                                    <xsl:variable name="varValue"><xsl:value-of select="value" /></xsl:variable>
                                    <xsl:variable name="strResultKey"><xsl:value-of select="strResultKey" /></xsl:variable>
                                    <xsl:variable name="varAttachedFileName"><xsl:value-of select="AttachedFileName" /></xsl:variable>
                                    
                                    <input type="file" name="{$varName}" value="{$varValue}" size="20" class="uportal-input-text" style="text-align: left" onChange="javascript:uploadAttachment('{$baseActionURL}?module=BATCH_SFRESULTS_GENERATION&amp;action=submit_standalone_fields_data&amp;UploadAttachment=true&amp;UploadAttachment_{$varName}=true')" />                            
                                    <input type="hidden" name="attachHidden{$varName}" value="{$varValue}" />
                                    
                                    <xsl:if test="string-length($varAttachedFileName) > '0' ">
                                    &#160;&#160; Attached File: &#160; <xsl:value-of select="AttachedFileName" /> 
                                    &#160;&#160;
                                    <a href="javascript:deleteAttachment('{$baseActionURL}?module=BATCH_SFRESULTS_GENERATION&amp;action=submit_standalone_fields_data&amp;DeleteAttachment=true&amp;DeleteAttachment_{$varName}=true')"> 
                                        Del
                                    </a> 
                                    </xsl:if>
                                    
                                </xsl:when>
                                <!-- Attachment End -->     

                                <!-- FORM LINK data element -->
                                <xsl:when test="$varDataElementType = 'FORM LINK'">
                                    <xsl:variable name="varPageName"><xsl:value-of select="DATAELEMENTS_strPageName" /></xsl:variable>
                                    <xsl:variable name="varChannelFunctionName"><xsl:value-of select="DATAELEMENTS_strChannelFunctionalName" /></xsl:variable>
                                    <xsl:variable name="varName"><xsl:value-of select="../SFNum" />_<xsl:value-of select="ID" /></xsl:variable>
                                    <xsl:variable name="varID"><xsl:value-of select="ID" /></xsl:variable>
                                    <xsl:variable name="varSFNum"><xsl:value-of select="../SFNum" /></xsl:variable>
                                    
                                    <textarea name="{$varName}" class="uportal-input-text">
                                        <xsl:value-of select="Result" />
                                    </textarea>

                                    <input type="button" name="refer" value="Lookup" class="uportal-button" onclick="javascript:jumpToPickerList('{$baseActionURL}?module=PICKER&amp;action={$varPageName}&amp;currentField=d{$varID}&amp;standaloneDataFormLink=true&amp;standaloneSFNum={$varSFNum}')" />                                
                                </xsl:when>                               

                            </xsl:choose>
                            
                        </td>
                    </xsl:for-each>                             
                </tr>
            </xsl:for-each>                 
         </table>                                
         
        <br/>
        <br/>
        <hr/>
        
        <table width="100%">
            <tr>
                <td align="left">
                        <input type="button" name="Cancel" value="Cancel" class="uportal-button" onclick="javascript:jumpTo('{$baseActionURL}{backLocation}')"/>        
                </td>                                
                <td align="right">
                        <input type="submit" name="Next" value="Next" class="uportal-button" />        
                </td>
            </tr>
        </table>
     </form>  
               
    </xsl:template>
</xsl:stylesheet>
