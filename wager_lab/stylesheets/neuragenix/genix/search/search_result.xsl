<?xml version="1.0" encoding="utf-8"?>
<!-- page for downloading export data file -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./search_menu.xsl"/>

<xsl:param name="formParams">current=search_result</xsl:param>

  <xsl:output method="html" indent="no" />
  <!-- Get the parameters from the channel class -->
  
<xsl:template match="search">
    <xsl:param name="blHasExportData"><xsl:value-of select="blHasExportData" /></xsl:param>
    <xsl:param name="blHasError"><xsl:value-of select="blHasError" /></xsl:param>
    <xsl:param name="strError"><xsl:value-of select="strError" /></xsl:param>
    
    <!-- paging -->
    <xsl:param name="intCurrentPage"><xsl:value-of select="intCurrentPage" /></xsl:param>
    <xsl:param name="intRecordPerPage"><xsl:value-of select="intRecordPerPage" /></xsl:param>
    
    <form action="{$baseActionURL}?{$formParams}" Name="search_result" method="post" enctype="multipart/form-data">

    <table width="100%">
	<tr>
            <td class="uportal-channel-subtitle">
		Search Result<br/><hr/>
            </td> 
	</tr> 
    </table>
    
    <xsl:if test="$blHasError = 'true'">
    <table width="100%">
	<tr>
            <td class="neuragenix-form-required-text">
		<xsl:value-of select="strError" />
            </td> 
	</tr> 
    </table>
    </xsl:if>

    <table width="100%">
        <xsl:if test="$blHasExportData = 'true'">
        <tr valign="TOP">
            <xsl:for-each select="Column_Header">
            <td class="uportal-channel-table-header" nowrap='true'><xsl:value-of select="Header" /></td>
            <td width="10"></td>
            </xsl:for-each>
        </tr>

        <xsl:for-each select="Row">
        <tr valign="TOP">
            <xsl:for-each select="Column">
            <td class="uportal-label"  nowrap='true'><xsl:value-of select="Column_Data" /></td>
            <td width="20" nowrap='true'></td>
            </xsl:for-each>
        </tr>
        </xsl:for-each>
        </xsl:if>    
    </table>

    <xsl:if test="$blHasExportData = 'true'">
    <table width="100%">
        
        <tr>
            <td class="uportal-channel-subtitle">
                <hr/>
            </td>
        </tr>
    </table>
    
    <table width="600px">    
        <tr>          

            <td width="40px" class="uportal-label">
                page:
            </td>

            <td width="15px" class="uportal-label">
                <input type="submit" name="previous" value="&lt;" tabindex="1" class="uportal-button" />
            </td>

            <td width="50px" class="uportal-label">
                <input type="text" name="txtCurrentPage" size="4" tabindex="2" value="{$intCurrentPage}" align="right" class="uportal-input-text" />
            </td>

            <td width="50px" class="uportal-label">
                of <xsl:value-of select="intMaxPage" />
            </td>

            <td width="15px" class="uportal-label">
                <input type="submit" name="next" value="&gt;" tabindex="3" class="uportal-button" />
            </td>

            <td width="30px" class="uportal-label">
                <input type="submit" name="go" value="go" tabindex="4" class="uportal-button" />
            </td>                           

            <td width="20px"></td>

            <td width="50px" class="uportal-label">
                Display
            </td>

            <td width="50px" class="uportal-label">
                <input type="text" name="txtRecordPerPage" size="4" tabindex="5" value="{$intRecordPerPage}" align="right" class="uportal-input-text" />
            </td>

            <td width="120px" class="uportal-label">
                records at a time
            </td>

            <td width="40px" class="uportal-label">
                <input type="submit" name="set" value="set" tabindex="6" class="uportal-button" />
            </td>
            
            <td width="20px"></td>
            <td width="80px" class="uportal-label">
                <input type="submit" name="back" value="back to search" tabindex="7" class="uportal-button" />
            </td>
        </tr>                   
    </table>
    </xsl:if>
    
    <table width="100%">
        <xsl:if test="$blHasExportData = 'true'">
        <tr><td><hr/></td></tr>
        <tr>
            <td class="uportal-label">The export file is available. To save the file, right click on the link and select "Save Target As.."</td>
        </tr>
        <tr>
            <td class="uportal-text">
                <a target="_blank" >
                    <xsl:attribute name="href"><xsl:value-of select="fileName" /></xsl:attribute>
                    Download
                </a>
            </td>
        </tr>
        </xsl:if>

        <xsl:if test="$blHasExportData = 'false'">
        <tr>
            <td class="uportal-label">There is no data to export.</td>
        </tr>
        </xsl:if>
    </table>
    
    </form>

</xsl:template> 
 
</xsl:stylesheet>
