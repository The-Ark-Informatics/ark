<?xml version="1.0" encoding="utf-8"?>
<!-- admin_password.xsl, Administration channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./admin_menu.xsl"/>
    <xsl:output method="html" indent="no" />

    <xsl:template match="profile">

        <!-- Get variables into the stylesheet -->
        <xsl:param name="strProfileName"><xsl:value-of select="strProfileName" /></xsl:param>
        <xsl:param name="strProfileDesc"><xsl:value-of select="strProfileDesc" /></xsl:param>
     
        <!-- Title of Channel -->
        <table width="100%">
          <tr><td class="uportal-channel-subtitle">Add Profile<br/><hr/></td></tr>
        </table>

        <!-- Form Fill -->
        <form action="{$baseActionURL}?current=insertprofile" method="post">
        <!-- Error messages -->
        <table width="100%">
            <tr>
                <td class="neuragenix-form-required-text" width="50%">
                        <xsl:value-of select="strErrorDuplicateKey" />
                        <xsl:value-of select="strErrorRequiredFields" />
                        <xsl:value-of select="strErrorInvalidDataFields" />
                        <br />
                        <xsl:value-of select="strErr" />
                </td>
                <td class="neuragenix-form-required-text" width="50%" id="neuragenix-required-header" align="right">
                * = Required fields
                </td>

            </tr>
        </table>
        <!-- Start of fields -->
        <table width="50%">
          <!-- Data Element -->
          <tr>
            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text">*</td>
            <td id="neuragenix-form-row-label" class="uportal-label">
                <xsl:value-of select="strProfileNameDisplay" />:</td>

            <td class="uportal-label" id="neuragenix-form-row-input">
                <input type="text" name="strProfileName" size="20" value="{$strProfileName}" class="uportal-input-text" />
            </td>
          </tr>
          <tr>
            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text">*</td>
            <td id="neuragenix-form-row-label" class="uportal-label">
                <xsl:value-of select="strProfileDescDisplay" />:</td>

            <td class="uportal-label" id="neuragenix-form-row-input">
                <input type="text" name="strProfileDesc" size="20" value="{$strProfileDesc}" class="uportal-input-text" />
            </td>
          </tr>
          <tr>    
            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text"></td>
            <td id="neuragenix-form-row-label" class="uportal-label"></td>
            <td class="uportal-label" id="neuragenix-form-row-input">
                <input type="submit" name="save" value="save" class="uportal-button" />
            </td>		
          </tr>
        </table> 
        </form>
    </xsl:template>
</xsl:stylesheet>
