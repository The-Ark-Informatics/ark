<?xml version="1.0" encoding="utf-8"?>
<!-- page for creating a new survey template -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./integration_menu.xsl"/>

<xsl:param name="formParams">current=survey_template_new</xsl:param>

  <xsl:output method="html" indent="no" />
  <!-- Get the parameters from the channel class -->
  
<xsl:template match="integration">
    <xsl:param name="strCurrentTemplateName"><xsl:value-of select="strCurrentTemplateName" /></xsl:param>
    <xsl:param name="strCurrentDomainName"><xsl:value-of select="strCurrentDomainName" /></xsl:param>
    <xsl:param name="intCurrentStudyID"><xsl:value-of select="intCurrentStudyID" /></xsl:param>
    <xsl:param name="intCurrentSurveyID"><xsl:value-of select="intCurrentSurveyID" /></xsl:param>
    <xsl:param name="intCurrentQuestionID"><xsl:value-of select="intCurrentQuestionID" /></xsl:param>
    

    <table width="100%">
	<tr>
            <td class="uportal-channel-subtitle">
		New <xsl:value-of select="strCurrentDomainName" /> template: <xsl:value-of select="strCurrentTemplateName" /><br/><hr/>
            </td> 
	</tr> 
    </table>

    <form action="{$baseActionURL}?{$formParams}" Name="survey_template_new" method="post">
    <table width="100%">
	<tr>
            <td width="20%" class="uportal-label">
		Study name:
            </td>                       

            <td width="25%" class="uportal-label">
                <select Name="intStudyID" tabindex="1" class="uportal-input-text" onChange="document.survey_template_new.submit();">
                    <xsl:for-each select="Study">
                        <xsl:variable name="varStudyID"><xsl:value-of select="intStudyID" /></xsl:variable>
			<option>
                            <xsl:if test="$intCurrentStudyID = $varStudyID">
					<xsl:attribute name="selected">true</xsl:attribute> 
                            </xsl:if>
                            <xsl:attribute name="value">
				<xsl:value-of select="intStudyID" />
                            </xsl:attribute> 
                            <xsl:value-of select="strStudyName" />
			</option>
                    </xsl:for-each>
                </select>
            </td>	
            
            <td width="10%"></td>

            <td width="20%" class="uportal-label">
		Survey name:
            </td>

            <td width="25%" class="uportal-label">
                <select Name="intSurveyID" tabindex="2" class="uportal-input-text" onChange="document.survey_template_new.submit();">
                   <xsl:for-each select="Survey">
                        <xsl:variable name="varSurveyID"><xsl:value-of select="intSurveyID" /></xsl:variable>
			<option>
                            <xsl:if test="$intCurrentSurveyID = $varSurveyID">
					<xsl:attribute name="selected">true</xsl:attribute> 
                            </xsl:if>
                            <xsl:attribute name="value">
				<xsl:value-of select="intSurveyID" />
                            </xsl:attribute> 
                            <xsl:value-of select="strSurveyName" />
			</option>
                    </xsl:for-each> 
                </select>
            </td>
	</tr>
    </table>

    <table width="100%">
        <tr>
            <td width="20%" class="uportal-label">
                Template structure:
            </td>

            <td width="50%" class="uportal-label">
                <select Name="intQuestionID" size="5" tabindex="3" class="uportal-input-text" onChange="document.survey_template_new.submit();">
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

            <td width="10%"></td>

            <td width="20%"  align="left">
                <table border="0">
                    <tr>
                        <td align="left">
                            <a href="javascript:window.location='{$baseActionURL}?current=survey_template_new&amp;moveUp=true&amp;intQuestionID={$intCurrentQuestionID}&amp;intStudyID={$intCurrentStudyID}&amp;intSurveyID={$intCurrentSurveyID}'" >
                                move up
                            </a>
                        </td>
                    </tr>

                    <tr>
                        <td align="left">
                            <a href="javascript:confirmDelete('{$baseActionURL}?current=survey_template_new&amp;intQuestionID={$intCurrentQuestionID}&amp;intStudyID={$intCurrentStudyID}&amp;intSurveyID={$intCurrentSurveyID}')" >
                                    delete
                            </a>
                        </td>
                     </tr>

                     <tr>
                        <td align="left">
                            <a href="javascript:window.location='{$baseActionURL}?current=survey_template_new&amp;moveDown=true&amp;intQuestionID={$intCurrentQuestionID}&amp;intStudyID={$intCurrentStudyID}&amp;intSurveyID={$intCurrentSurveyID}'" >
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
		<input type="submit" name="back" value="&lt;&lt;--back---" tabindex="4"  class="uportal-button" />
            </td>	
            <td width="10%" class="uportal-label">
		<input type="submit" name="next" value="---next-->>" tabindex="5"  class="uportal-button" />
            </td>		
            <td width="78%" class="uportal-label"></td>
	</tr>
    </table>

    </form>

</xsl:template>
 
</xsl:stylesheet>