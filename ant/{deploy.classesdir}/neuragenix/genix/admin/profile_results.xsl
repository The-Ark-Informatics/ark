<?xml version="1.0" encoding="utf-8"?>
<!-- admin_password.xsl, Administration channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./admin_menu.xsl"/>
    <xsl:output method="html" indent="no" />

    <xsl:template match="profile">

        <!-- Title of Channel -->
        <table width="100%">
          <tr><td class="uportal-channel-subtitle">Select a  Profile<br/><hr/></td></tr>
		<tr>
			<td>
				<a href="{$baseActionURL}?current=insertprofile">Add New Profile</a><hr/>
			</td>

		</tr>
        </table>
	
		


        <!-- Form Fill -->
        <form action="{$baseActionURL}?current=showprofiles" method="post">
        <!-- Error messages -->
        <table width="100%">
	    <tr><td colspan="2">Search:</td></tr>
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
            <td  class="neuragenix-form-required-text"></td>
            <td id="neuragenix-form-row-label" class="uportal-label">
                <xsl:value-of select="strProfileNameDisplay" />:</td>

            <td class="uportal-label" id="neuragenix-form-row-input">
                <input type="text" name="strProfileName" size="20" class="uportal-input-text" />
            </td>
          </tr>
          <tr>    
            <td  class="neuragenix-form-required-text"></td>
            <td id="neuragenix-form-row-label" class="uportal-label"></td>
            <td class="uportal-label" id="neuragenix-form-row-input">
                <input type="submit" name="search" value="search" class="uportal-button" />
            </td>		
          </tr>
        </table> 
        </form>
	<table width="100%">
	  	<tr><td colspan="2"><hr/></td></tr>
		<tr>
			<td class="uportal-label" id="neuragenix-form-row-input"><xsl:value-of select="strProfileNameDisplay"/></td>
			<td class="uportal-label" id="neuragenix-form-row-input"><xsl:value-of select="strProfileDescDisplay"/></td>
		</tr>
		<xsl:for-each select="searchResult">
		<tr>
			<td id="neuragenix-form-row-input"><a><xsl:attribute name="href"><xsl:value-of select="$baseActionURL"/>?current=viewprofile&amp;intProfileID=<xsl:value-of select="intProfileID"/></xsl:attribute><xsl:value-of select="strProfileName"/></a></td>
			<td id="neuragenix-form-row-input"><a><xsl:attribute name="href"><xsl:value-of select="$baseActionURL"/>?current=viewprofile&amp;intProfileID=<xsl:value-of select="intProfileID"/></xsl:attribute><xsl:value-of select="strProfileDesc"/></a></td>
		</tr>
		</xsl:for-each>
	</table>
    </xsl:template>
</xsl:stylesheet>
