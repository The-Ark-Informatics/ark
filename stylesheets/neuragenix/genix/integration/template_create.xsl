<?xml version="1.0" encoding="utf-8"?>
<!-- stylesheet for creating template -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./integration_menu.xsl"/>

<xsl:param name="formParams">current=template_create</xsl:param>

  <xsl:output method="html" indent="no" />
  <!-- Get the parameters from the channel class -->
  
<xsl:template match="integration">
    <!-- Get the parameters from the channel class -->
    <xsl:param name="strCurrentTemplateName"><xsl:value-of select="strCurrentTemplateName" /></xsl:param>
    <xsl:param name="strCurrentDomainName"><xsl:value-of select="strCurrentDomainName" /></xsl:param>

    <table width="100%">
	<tr>
            <td class="uportal-channel-subtitle">
		Create new template<br/><hr/>
            </td> 
	</tr> 
    </table>

    <form action="{$baseActionURL}?{$formParams}" method="post">
    <table width="100%">	 
        <tr>
            <td class="neuragenix-form-required-text" width="50%">
                <xsl:value-of select="Error" />
            </td>
            <td class="neuragenix-form-required-text" width="50%" id="neuragenix-required-header" align="right">
                * = Required fields
            </td>
        </tr>
    </table>

    <table width="100%">
	<tr>
            <td id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text">*</td>
            <td width="20%" class="uportal-label">
		Template name:
            </td>

            <td width="50%" class="uportal-label">
                <input type="text" name="strTemplateName" value="{$strCurrentTemplateName}" size="20" tabindex="101" class="uportal-input-text" />
            </td>
            
            <td width="30%"></td>
	</tr>

        <tr>
            <td id="neuragenix-form-row-input-label-required" class="neuragenix-form-required-text"></td>
            <td width="20%" class="uportal-label">
		Domain name:
            </td>                        

            <td width="50%" class="uportal-label">
                <select name="strDomainName" tabindex="102" class="uportal-input-text" width="20">
                    <xsl:for-each select="Domain">
                        <xsl:variable name="varDomainName"><xsl:value-of select="strDomainName" /></xsl:variable>
			<option>
                            <xsl:if test="$strCurrentDomainName = $varDomainName">
					<xsl:attribute name="selected">true</xsl:attribute> 
                            </xsl:if>
                            <xsl:attribute name="value">
				<xsl:value-of select="strDomainName" />
                            </xsl:attribute> 
                            <xsl:value-of select="strDomainName" />
			</option>
                    </xsl:for-each>
                </select>
            </td>	
            <td width="30%"></td>
	</tr>

    </table>

    <table width="100%">
	<tr><td><hr /></td></tr>
    </table>

    <table width="100%">
	<tr>
            <td width="10%" class="uportal-label">
		<input type="submit" name="next" value="---next-->>" tabindex="103"  class="uportal-button" />
            </td>		
            <td width="78%" class="uportal-label"></td>
	</tr>
    </table>

    </form>

</xsl:template>
 
</xsl:stylesheet>