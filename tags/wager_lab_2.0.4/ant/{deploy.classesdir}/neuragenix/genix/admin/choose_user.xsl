<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : choose_user.xsl
    Created on : June 8, 2004, 4:51 PM
    Author     : renny
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:include href="./tree_view.xsl"/>
    <xsl:output method="html" indent="no"/>

 <xsl:template match="proccess">

    <table width="100%">
    <tr>
        <td class="uportal-channel-subtitle">Choose User<hr/>
        </td>
    </tr>
  
    <tr>
        <td class="uportal-channel-warning"><xsl:value-of select="error"/>
        </td>
    </tr>
    <tr>
           <td>
           <xsl:apply-templates select="userDetails"/>
        </td>
    </tr>
    </table>
</xsl:template>
<xsl:template match="userDetails">
        <xsl:variable name="groupkey"><xsl:value-of select="userGroupKey" /></xsl:variable>
<form action="{$baseActionURL}?action=proccess_group&amp;create_user=true" method="post">
<table>
    <tr>
        <td class="uportal-channel-warning"><xsl:value-of select="error"/>
        </td>
    </tr>    
    <tr>
        <td class="uportal-label">
            <input type="radio" name="newuser" value="newuser"> Add a new User</input><br/>
        </td>
    </tr>
    <tr>
        <td class="uportal-label">
            <input type="radio" name="newuser" value="cloneuser"> Clone this User</input>
        </td>
        <td>
            <select name="user" tabindex="6" class="uportal-input-text">
                        <xsl:for-each select="user">

                            <option>
                                <xsl:attribute name="value">
                                <xsl:value-of select="." />
                                </xsl:attribute> 
                                <xsl:if test="@selected=1">
                                        <xsl:attribute name="selected">true</xsl:attribute> 
                                </xsl:if>

                                <xsl:value-of select="." />		
                            </option>
                        </xsl:for-each>
            </select>
        </td>
    </tr>
    <tr>
        <td>
            <input type="hidden" name="UsergroupKey" value="{$groupkey}"/>
            <input type="submit" name="new_user" value="Create" class="uportal-button" />
        </td>
    </tr>
</table>
</form>
</xsl:template>

</xsl:stylesheet> 
