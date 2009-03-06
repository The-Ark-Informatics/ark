<!-- page for creating a new domain template -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./integration_menu.xsl"/>

<xsl:param name="formParams">current=domain_template_new</xsl:param>

  <xsl:output method="html" indent="no" />
  <!-- Get the parameters from the channel class -->
  
<xsl:template match="integration">
    <xsl:param name="strCurrentTemplateName"><xsl:value-of select="strCurrentTemplateName" /></xsl:param>
    <xsl:param name="strCurrentDomainName"><xsl:value-of select="strCurrentDomainName" /></xsl:param>
    <xsl:param name="strCurrentFieldName"><xsl:value-of select="strCurrentFieldName" /></xsl:param>

    <table width="100%">
	<tr>
            <td class="uportal-channel-subtitle">
		New <xsl:value-of select="strCurrentDomainName" /> template: <xsl:value-of select="strCurrentTemplateName" /><br/><hr/>
            </td> 
	</tr> 
    </table>

    
    <form action="{$baseActionURL}?{$formParams}" Name="domain_template_new" method="post">

    <table width="100%">
        <tr>
            <td width="20%" class="uportal-label">
		Template structure:
            </td>

            <td width="50%" class="uportal-label">
                <select Name="strFieldName" size="15" tabindex="1" class="uportal-input-text" onChange="document.domain_template_new.submit();">
                    <xsl:for-each select="Field">
                        <xsl:variable name="varFieldName"><xsl:value-of select="internal_name" /></xsl:variable>
			<option>
                            <xsl:if test="$strCurrentFieldName = $varFieldName">
				<xsl:attribute name="selected">true</xsl:attribute> 
                            </xsl:if>
                            <xsl:attribute name="value"><xsl:value-of select="internal_name" /></xsl:attribute>
                            <xsl:value-of select="name" />
			</option>
                    </xsl:for-each> 
                </select>
            </td>

            <td width="10%"></td>
                
            <td width="20%"  align="left">
                <table border="0">
                    <tr>
                        <td align="left">
                            <a tabindex='2' href="javascript:window.location='{$baseActionURL}?current=domain_template_new&amp;moveUp=true&amp;strFieldName={$strCurrentFieldName}'" >
                                move up
                            </a>
                        </td>
                    </tr>

                    <tr>
                        <td align="left">
                            <a tabindex='3' href="javascript:confirmDelete('{$baseActionURL}?current=domain_template_new&amp;strFieldName={$strCurrentFieldName}')" >
                                    delete
                            </a>
                        </td>
                     </tr>

                     <tr>
                        <td align="left">
                            <a tabindex='4' href="javascript:window.location='{$baseActionURL}?current=domain_template_new&amp;moveDown=true&amp;strFieldName={$strCurrentFieldName}'" >
                                move down
                            </a>
                        </td>	      
                    </tr>
                </table>
            </td>

	</tr>

    </table>

    <table width="100%">
	<tr><td><hr /></td></tr>
    </table>

    <table width="100%">
	<tr>
            <td width="10%" class="uportal-label">
		<input type="submit" name="back" value="&lt;&lt;--back---" tabindex="5"  class="uportal-button" />
            </td>	
            <td width="10%" class="uportal-label">
		<input type="submit" name="next" value="---next-->>" tabindex="6"  class="uportal-button" />
            </td>		
            <td width="78%" class="uportal-label"></td>
	</tr>
    </table>

    </form>

</xsl:template>
 
</xsl:stylesheet>