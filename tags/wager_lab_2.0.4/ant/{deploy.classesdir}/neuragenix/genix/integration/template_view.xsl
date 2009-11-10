<?xml version="1.0" encoding="utf-8"?>
<!-- first page of import data process -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./integration_menu.xsl"/>

<xsl:param name="formParams">current=template_view</xsl:param>

  <xsl:output method="html" indent="no" />
  <!-- Get the parameters from the channel class -->
  
<xsl:template match="integration">
    <xsl:param name="intCurrentTemplateID"><xsl:value-of select="intCurrentTemplateID" /></xsl:param>
    <xsl:param name="intCurrentQuestionID"><xsl:value-of select="intCurrentQuestionID" /></xsl:param>
    <xsl:param name="strCurrentFieldName"><xsl:value-of select="strCurrentFieldName" /></xsl:param>
    <xsl:param name="strCurrentDomainName"><xsl:value-of select="strCurrentDomainName" /></xsl:param>
    <xsl:param name="blSurveyTemplate"><xsl:value-of select="blSurveyTemplate" /></xsl:param>
    <xsl:param name="blLockError"><xsl:value-of select="blLockError" /></xsl:param>
    <xsl:param name="TEMPLATE_Timestamp"><xsl:value-of select="TEMPLATE_Timestamp" /></xsl:param>

    <table width="100%">
	<tr>
            <td class="uportal-channel-subtitle">
		Use existing template<br/><hr/>
            </td> 
	</tr> 
    </table>

    <form action="{$baseActionURL}?{$formParams}" Name="template_view" method="post">
    <table width="100%">
        <xsl:if test="$blLockError = 'true'">
        <tr>
            <td class="neuragenix-form-required-text" id="neuragenix-required-header">
                This template is being viewed by other users. You cannot update it now. Please try again later.
            </td>
        </tr>
        </xsl:if>
    </table>

    <table width="100%">
	<tr>
            <td width="20%" class="uportal-label">
		Template name:
            </td>

            <td width="50%" class="uportal-label">
                <select name="intTemplateID" tabindex="1" class="uportal-input-text" onChange="document.template_view.submit();">
                    <xsl:for-each select="Template">
                        <xsl:variable name="varTemplateID"><xsl:value-of select="intTemplateID" /></xsl:variable>
                        <option>
                            <xsl:if test="$intCurrentTemplateID = $varTemplateID">
				<xsl:attribute name="selected">true</xsl:attribute> 
                            </xsl:if>
                            <xsl:attribute name="value"><xsl:value-of select="intTemplateID" /></xsl:attribute> 
                            <xsl:value-of select="strTemplateName" />
                        </option>
                    </xsl:for-each>
                </select>
            </td>
            <td width="10%"></td>
            <td width="20%"></td>
	</tr>

        <tr>
            <td width="20%" class="uportal-label">
		Template structure:
            </td>

            <xsl:if test="$blSurveyTemplate = 'false'">
            <td width="50%" class="uportal-label">
                <select Name="strFieldName" size="15" tabindex="2" class="uportal-input-text" onChange="document.template_view.submit();">
                    <xsl:for-each select="Field">
                        <xsl:variable name="varDomainField"><xsl:value-of select="internal_name" /></xsl:variable>
                        <option>
                            <xsl:if test="$strCurrentFieldName = $varDomainField">
				<xsl:attribute name="selected">true</xsl:attribute> 
                            </xsl:if>
                            <xsl:attribute name="value"><xsl:value-of select="internal_name" /></xsl:attribute>
                            <xsl:value-of select="name" />
                        </option>
                    </xsl:for-each>
                </select>
            </td>
            </xsl:if>

            <xsl:if test="$blSurveyTemplate = 'true'">
            <td width="50%" class="uportal-label">
                <select Name="intQuestionID" size="15" tabindex="3" class="uportal-input-text" onChange="document.template_view.submit();">
                    <xsl:for-each select="Question">
                        <xsl:variable name="varQuestionID"><xsl:value-of select="intQuestionID" /></xsl:variable>
                        <option>
                            <xsl:if test="$intCurrentQuestionID = $varQuestionID">
				<xsl:attribute name="selected">true</xsl:attribute> 
                            </xsl:if>
                            <xsl:attribute name="value">
				<xsl:value-of select="intQuestionID" />
                            </xsl:attribute>
                            <xsl:value-of select="strQuestion" />
                        </option>
                    </xsl:for-each>
                </select>
            </td>
            </xsl:if>

            <td width="10%"></td>

            <td width="20%" align="left">
                <table border="0">
                    <tr>
                        <td align="left">
                            <a tabindex='4' href="javascript:window.location='{$baseActionURL}?current=template_view&amp;moveUp=true&amp;intTemplateID={$intCurrentTemplateID}&amp;intQuestionID={$intCurrentQuestionID}&amp;strFieldName={$strCurrentFieldName}&amp;strCurrentDomainName={$strCurrentDomainName}'"  >
                                move up
                            </a>
                        </td>

                        <td align="left"></td>
                    </tr>

                    <tr>
                        <td align="left">
                            <a tabindex='5' href="javascript:confirmDelete('{$baseActionURL}?current=template_view&amp;intTemplateID={$intCurrentTemplateID}&amp;intQuestionID={$intCurrentQuestionID}&amp;strFieldName={$strCurrentFieldName}&amp;strCurrentDomainName={$strCurrentDomainName}')" >
                                    delete
                            </a>
                        </td>

                        <td align="left"></td>
                     </tr>

                     <tr>
                        <td align="left">
                            <a tabindex='6' href="javascript:window.location='{$baseActionURL}?current=template_view&amp;moveDown=true&amp;intTemplateID={$intCurrentTemplateID}&amp;intQuestionID={$intCurrentQuestionID}&amp;strFieldName={$strCurrentFieldName}&amp;strCurrentDomainName={$strCurrentDomainName}'" >
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
            <td class="uportal-label">
		<input type="submit" name="save" value="save" tabindex="7"  class="uportal-button" />
            </td>
            
            <td class="uportal-label">
		<input type="button" name="delete_template" value="delete" tabindex="8" class="uportal-button" onclick="javascript:confirmDelete('{$baseActionURL}?current=template_view&amp;delete_template=true&amp;TEMPLATE_Timestamp={$TEMPLATE_Timestamp}')" />
            </td>	
            
            <td class="uportal-label">
		<input type="submit" name="next" value="---next-->>" tabindex="9"  class="uportal-button" />
            </td>		
            <td width="75%" class="uportal-label"></td>
	</tr>
    </table>

    <input type="hidden" name="TEMPLATE_Timestamp" value="{$TEMPLATE_Timestamp}"/>
    </form>

</xsl:template>
 
</xsl:stylesheet>