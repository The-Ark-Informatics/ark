<?xml version="1.0" encoding="utf-8"?>
<!-- admin_password.xsl, Administration channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" indent="no" />
	<xsl:param name="baseActionURL"/>

    <xsl:template match="user_password">

        <!-- Get variables into the stylesheet -->
	<xsl:param name="strUsername"><xsl:value-of select="strUsername" /></xsl:param>
        <xsl:param name="strOldPassword"><xsl:value-of select="strOldPassword" /></xsl:param>
        <xsl:param name="strNewPassword1"><xsl:value-of select="strNewPassword1" /></xsl:param>
        <xsl:param name="strNewPassword2"><xsl:value-of select="strNewPassword2" /></xsl:param>
     
        <!-- Title of Channel -->
        <table width="100%">
          <tr><td class="uportal-channel-subtitle">Change Password<br/><hr/></td></tr>
        </table>

        <!-- Form Fill -->
        <form action="{$baseActionURL}?current=changepass" method="post">
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
<!--          <tr>
            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text">*</td>
            <td id="neuragenix-form-row-label" class="uportal-label">
                <xsl:value-of select="strUsernameDisplay" />:</td>
            <td class="uportal-label" id="neuragenix-form-row-input">
                <input type="text" name="strUsername" size="20" value="{$strUsername}" class="uportal-input-text" />
            </td>
          </tr> -->
          <tr>
            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text">*</td>
            <td id="neuragenix-form-row-label" class="uportal-label">
                <xsl:value-of select="strOldPasswordDisplay" />:</td>

            <td class="uportal-label" id="neuragenix-form-row-input">
                <input type="password" name="strOldPassword" size="20" value="{$strOldPassword}" class="uportal-input-text" />
            </td>
          </tr> 
          <tr>
            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text">*</td>
            <td id="neuragenix-form-row-label" class="uportal-label">
                <xsl:value-of select="strNewPassword1Display" />:</td>

            <td class="uportal-label" id="neuragenix-form-row-input">
                <input type="password" name="strNewPassword1" size="20" value="{$strNewPassword1}" class="uportal-input-text" />
            </td>
          </tr>
          <tr>
            <td id="neuragenix-form-row-label-required" class="neuragenix-form-required-text">*</td>
            <td id="neuragenix-form-row-label" class="uportal-label">
                <xsl:value-of select="strNewPassword2Display" />:</td>

            <td class="uportal-label" id="neuragenix-form-row-input">
                <input type="password" name="strNewPassword2" size="20" value="{$strNewPassword2}" class="uportal-input-text" />
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
