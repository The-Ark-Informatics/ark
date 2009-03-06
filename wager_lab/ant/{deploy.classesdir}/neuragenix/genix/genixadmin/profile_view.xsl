<?xml version="1.0" encoding="utf-8"?>
<!-- admin_password.xsl, Administration channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./admin_menu.xsl"/>
    <xsl:output method="html" indent="no" />

        <!-- Get variables into the stylesheet -->
        <xsl:param name="intProfileID"><xsl:value-of select="profile/intProfileID" /></xsl:param>
        <xsl:param name="strProfileName"><xsl:value-of select="profile/strProfileName" /></xsl:param>
        <xsl:param name="strProfileDesc"><xsl:value-of select="profile/strProfileDesc" /></xsl:param>

    <xsl:template match="profile">

     
        <!-- Title of Channel -->
        <table width="100%">
          <tr><td class="uportal-channel-subtitle">View Profile<br/><hr/></td></tr>
        </table>

        <!-- Form Fill -->
        <form action="{$baseActionURL}?current=viewprofile" method="post">
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
                <input type="hidden" name="intProfileID" value="{$intProfileID}" />
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
	<table width="100%"><tr><td width="100%"><hr/></td></tr></table>
	<xsl:apply-templates select="profileactivities"/>
    </xsl:template>


    <xsl:template match="profileactivities"> 
	<table>
		<tr>
			<td id="neuragenix-form-row-input" class="uportal-channel-subtitle"><xsl:value-of select="strActivityDescDisplay"/></td>
			<td> </td>
			<!-- td id="neuragenix-form-row-label" class="uportal-label"><xsl:value-of select="strActivityDescDisplay"/></td -->
		</tr>
		<xsl:for-each select="searchResult">
		<tr>
			<td id="neuragenix-form-row-input" class="uportal-label"><xsl:value-of select="strActivityDesc"/></td>	
			<td id="neuragenix-form-row-input" class="uportal-label">
				<a onclick="return confirm('Are you sure you want to delete this activity?')">
					<xsl:attribute name="href">
						<xsl:value-of select="$baseActionURL"/>?current=deleteactivity&amp;intProfileID=<xsl:value-of select="$intProfileID"/>&amp;intActivityID=<xsl:value-of select="intActivityID"/>&amp;PROFILEAUTH_Timestamp=<xsl:value-of select="PROFILEAUTH_Timestamp"/>
					</xsl:attribute>(delete)</a>
			</td>
		</tr>
		</xsl:for-each>
		<tr>
			<form  action="{$baseActionURL}?current=insertactivity" method="post">
			<td><select name="intActivityID">
		<xsl:for-each select="LOV_ACTIVITY">
		<option>
			<xsl:attribute name="value">
				 <xsl:value-of select="LOV_intActivityID"/>
			</xsl:attribute>
			<xsl:value-of select="LOV_strActivityDesc"/>
		</option>
		</xsl:for-each>
			</select></td>
			<td>
				<input type="hidden" value="{$intProfileID}" name="intProfileID"/>
				<input type="submit" value="Add" name="addactivity"/>
			</td>	
			</form>
		</tr>
	</table>
    </xsl:template>
</xsl:stylesheet>
