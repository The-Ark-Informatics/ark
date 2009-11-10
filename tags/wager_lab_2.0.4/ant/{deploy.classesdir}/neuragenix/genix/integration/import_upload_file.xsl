<?xml version="1.0" encoding="utf-8"?>
<!-- the page for uploading import file -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./integration_menu.xsl"/>

<xsl:param name="formParams">current=import_upload_file</xsl:param>

  <xsl:output method="html" indent="no" />
  <!-- Get the parameters from the channel class -->
  
<xsl:template match="integration">
    <xsl:param name="blUploadSuccessful"><xsl:value-of select="blUploadSuccessful" /></xsl:param>

    <table width="100%">
	<tr>
            <td class="uportal-channel-subtitle">
		Upload file<br/><hr/>
            </td> 
	</tr> 
    </table>

    <form action="{$baseActionURL}?{$formParams}" method="post" enctype="multipart/form-data">
    <xsl:if test="$blUploadSuccessful = 'true'">
    <table width="100%">
        <tr>
            <td class="uportal-label">
		Your request is being done by a process. To check the process status click on "view process".
            </td>
        </tr>
        <tr>
            <td height= "10px"></td>
        </tr>
    </table>
    </xsl:if>

    <table width="100%">
        <tr>
            <td width="20%" class="uportal-label">
		Select file: 
            </td>
        </tr>

	<tr>
            <td >
                <input type="file" tabindex='1' name="flImportFile" class="uportal-input-text"/>
            </td>	
	</tr>

    </table>

    <table width="100%">
	<tr><td><hr /></td></tr>
    </table>

    <table width="100%">
	<tr>
            <td width="5%" class="uportal-label">
		<input type="submit" name="submit" value="submit" class="uportal-button" tabindex='3'/>
            </td>	
            		
            <td width="90%" class="uportal-label"></td>
	</tr>
    </table>

    </form>

</xsl:template>
 
</xsl:stylesheet>