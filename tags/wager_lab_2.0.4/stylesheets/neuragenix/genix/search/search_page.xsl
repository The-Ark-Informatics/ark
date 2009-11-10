<?xml version="1.0" encoding="utf-8"?>
<!-- first page of export data process -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./search_menu.xsl"/>

<xsl:param name="formParams">current=search_page</xsl:param>

  <xsl:output method="html" indent="no" />
  <!-- Get the parameters from the channel class -->
  
<xsl:template match="search">
    <xsl:param name="strCurrentDomainName"><xsl:value-of select="strCurrentDomainName" /></xsl:param>
    <xsl:param name="blHasError"><xsl:value-of select="blHasError" /></xsl:param>
    
    <form action="{$baseActionURL}?{$formParams}" Name="search_page" method="post" enctype="multipart/form-data">

    <table width="100%">
	<tr>
            <td class="uportal-channel-subtitle">
		Advanced Search<br/><hr/>
            </td> 
	</tr> 
    </table>

    <table>
        <tr>
            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
            <td class="uportal-channel-table-header">Step 1: Select domain</td>
            
        </tr>
    </table>

    <table width="100%">
        <tr>
            <td height= "20px"></td>
        </tr>
    </table>

    <table width="100%">
	<tr>
            <td width="25%" class="uportal-label">
		Domain Name:
            </td>

            <td width="35%" class="uportal-label">
                <select Name="strDomainName" class="uportal-input-text" onChange="document.search_page.submit();">
                    <xsl:for-each select="Domain_Names">
                        <xsl:variable name="varDomainName"><xsl:value-of select="Domain_Name" /></xsl:variable>
                        <option>                          
                            <xsl:if test="$strCurrentDomainName = $varDomainName">
				<xsl:attribute name="selected">true</xsl:attribute> 
                            </xsl:if>
                            <xsl:attribute name="value"><xsl:value-of select="Domain_Name" /></xsl:attribute> 
                            <xsl:value-of select="Domain_Name" />
                        </option>
                    </xsl:for-each>
                </select>
            </td>	
            
            <td width="40%">
            </td>
        </tr>
    </table>

    <table width="100%">
        <tr><td><hr /></td></tr>
    </table>

    <table width="100%">
        <tr>
            <td height= "10px"></td>
        </tr>
    </table>

    <table>
        <tr>
            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
            <td class="uportal-channel-table-header">Step 2: Select fields and specify conditions</td>
            
        </tr>
    </table>

    <table width="100%">	 
        <tr>
            <td class="neuragenix-form-required-text" width="85%">
            </td>
            <td class="neuragenix-form-required-text" width="15%" id="neuragenix-required-header">
                Conn = Connector
            </td>
        </tr>

        <tr>
            <td class="neuragenix-form-required-text" width="85%">
            </td>
            <td class="neuragenix-form-required-text" width="15%" id="neuragenix-required-header">
                Ope = Operator
            </td>
        </tr>

        <tr>
            <td class="neuragenix-form-required-text" width="85%">
            </td>
            <td class="neuragenix-form-required-text" width="15%" id="neuragenix-required-header">
                Prio = Priority
            </td>
        </tr>
    </table>

    <table width="100%">
        <tr>
            <td height= "10px"></td>
        </tr>
    </table>

    <xsl:if test="$blHasError = 'true'">
    <table width="100%">
        <xsl:for-each select="Errors">
        <tr>
            <td class="neuragenix-form-required-text" id="neuragenix-required-header">
                <xsl:value-of select="Error" />
            </td>
        </tr>
        </xsl:for-each>
        <tr><td height="10px"></td></tr>
    </table>
    </xsl:if>

    <table width="100%">
        <tr class="uportal-channel-table-header">
            <td width="2%"></td>
            <td width="30%">Field Name</td>
            <td width="8%">Conn</td>
            <td width="8%">Ope</td>
            <td width="12%">Value</td>
            <td width="8%">Conn</td>
            <td width="8%">Ope</td>
            <td width="12%">Value</td>
            <td width="8%">Order</td>
            <td width="4%">Prio</td>
        </tr>
    </table>

    <table width="100%">
        <xsl:for-each select="DOMAIN">
        <tr>
            <td width="2%">
                <xsl:variable name="varValue1"><xsl:value-of select="Value1" /></xsl:variable>
                <input type="checkbox" value="value">
                    <xsl:attribute name="name"><xsl:value-of select="Checkbox" /></xsl:attribute>
                    <xsl:if test="$varValue1 = '1'">
                        <xsl:attribute name="checked">true</xsl:attribute> 
                    </xsl:if>
                </input>
            </td>
            <td width="30%" class="uportal-label">
                <xsl:value-of select="Domain_Field" />
            </td>
            <td width="8%">
                <select class="uportal-input-text">
                    <xsl:variable name="varValue2"><xsl:value-of select="Value2" /></xsl:variable>
                    <xsl:attribute name="name"><xsl:value-of select="Option1" /></xsl:attribute>
                    <option>
                        <xsl:if test="$varValue2 = ''">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                    </option>
                    <option>
                        <xsl:if test="$varValue2 = 'AND'">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                        AND
                    </option>
                    <option>
                        <xsl:if test="$varValue2 = 'OR'">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                        OR
                    </option>
                </select>
            </td>
            <td width="8%">
                <select class="uportal-input-text">
                    <xsl:variable name="varValue3"><xsl:value-of select="Value3" /></xsl:variable>
                    <xsl:attribute name="name"><xsl:value-of select="Option2" /></xsl:attribute>
                    <xsl:for-each select="Operators">
                        <xsl:variable name="varOpt2"><xsl:value-of select="Operator" /></xsl:variable>
                        <option>
                            <xsl:if test="$varValue3 = $varOpt2">
				<xsl:attribute name="selected">true</xsl:attribute> 
                            </xsl:if>
                            <xsl:value-of select="Operator" />
                        </option>
                    </xsl:for-each>    
                </select>
            </td>

            <td width="12%">
                <input type="text" class="uportal-input-text" size="10">
                <xsl:attribute name="name"><xsl:value-of select="Text1" /></xsl:attribute>
                <xsl:attribute name="value"><xsl:value-of select="Value4" /></xsl:attribute>
                </input>
            </td>

            <td width="8%">
                <select class="uportal-input-text">
                    <xsl:variable name="varValue5"><xsl:value-of select="Value5" /></xsl:variable>
                    <xsl:attribute name="name"><xsl:value-of select="Option3" /></xsl:attribute>
                    <option>
                        <xsl:if test="$varValue5 = ''">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                    </option>
                    <option>
                        <xsl:if test="$varValue5 = 'AND'">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                        AND
                    </option>
                    <option>
                        <xsl:if test="$varValue5 = 'OR'">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                        OR
                    </option>
                </select>
            </td>

            <td width="8%">
                <select class="uportal-input-text">
                    <xsl:variable name="varValue6"><xsl:value-of select="Value6" /></xsl:variable>
                    <xsl:attribute name="name"><xsl:value-of select="Option4" /></xsl:attribute>
                    <xsl:for-each select="Operators">
                        <xsl:variable name="varOpt4"><xsl:value-of select="Operator" /></xsl:variable>
                        <option>
                            <xsl:if test="$varValue6 = $varOpt4">
				<xsl:attribute name="selected">true</xsl:attribute> 
                            </xsl:if>
                            <xsl:value-of select="Operator" />
                        </option>
                    </xsl:for-each>    
                </select>
            </td>

            <td width="12%">
                <input type="text" class="uportal-input-text" size="10">
                    <xsl:attribute name="name"><xsl:value-of select="Text2" /></xsl:attribute>
                    <xsl:attribute name="value"><xsl:value-of select="Value7" /></xsl:attribute>
                </input>
            </td>

            <td width="8%">
                <select class="uportal-input-text">
                    <xsl:variable name="varValue8"><xsl:value-of select="Value8" /></xsl:variable>
                    <xsl:attribute name="name"><xsl:value-of select="Order" /></xsl:attribute>
                    <option>
                        <xsl:if test="$varValue8 = ''">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                    </option>
                    <option>
                        <xsl:if test="$varValue8 = 'ASC'">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                        ASC
                    </option>
                    <option>
                        <xsl:if test="$varValue8 = 'DESC'">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                        DESC
                    </option>
                </select>
            </td>

            <td width="4%">
                <select class="uportal-input-text">
                    <xsl:variable name="varValue9"><xsl:value-of select="Value9" /></xsl:variable>
                    <xsl:attribute name="name"><xsl:value-of select="Priority" /></xsl:attribute>
                    <option>
                        <xsl:if test="$varValue9 = '1'">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                        1
                    </option>
                    <option>
                        <xsl:if test="$varValue9 = '2'">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                        2
                    </option>
                    <option>
                        <xsl:if test="$varValue9 = '3'">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                        3
                    </option>
                    <option>
                        <xsl:if test="$varValue9 = '4'">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                        4
                    </option>
                    <option>
                        <xsl:if test="$varValue9 = '5'">
                            <xsl:attribute name="selected">true</xsl:attribute> 
                        </xsl:if>
                        5
                    </option>
                </select>
            </td>
        </tr>
        </xsl:for-each>
    </table>

    <table width="100%">
        <tr><td><hr /></td></tr>
    </table>
    
    <table width="100%">
	<tr>
            <td height= "5px"/>
	</tr>

	<tr>
            <td width="20%">
                <input type="submit" name="check_all" value="check all" class="uportal-button" />
                <input type="submit" name="clear_all" value="clear all" class="uportal-button" />
            </td>
            <td width="30%"></td>
            <td width="50%" class="uportal-label">
		<input type="submit" name="submit_query" value="submit" class="uportal-button" />
            </td>	
	</tr>
    </table>

    </form>

</xsl:template>
 
</xsl:stylesheet>
