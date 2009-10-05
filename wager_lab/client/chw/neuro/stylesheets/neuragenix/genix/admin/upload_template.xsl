<?xml version="1.0" encoding="utf-8"?>

<!-- upload_template.xsl To upload templates-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./admin_menu.xsl"/>
  <xsl:output method="html" indent="no"/>

  <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>


  <xsl:template match="upload_templates">
     <script language="javascript" >

            function confirmDelete(aURL) {
                var confirmAnswer = confirm('Are you sure you want to delete this record?');

                if(confirmAnswer == true){
                    window.location=aURL;
                }
            }
        </script>
    <xsl:variable name="DOCUMENTTEMPLATE_strExternalName"><xsl:value-of select="DOCUMENTTEMPLATE_strExternalName" /></xsl:variable>
    <xsl:variable name="DOCUMENTTEMPLATE_strTemplateHelp"><xsl:value-of select="DOCUMENTTEMPLATE_strTemplateHelp" /></xsl:variable>
    <xsl:variable name="DOCUMENTTEMPLATE_strHidden"><xsl:value-of select="DOCUMENTTEMPLATE_strHidden" /></xsl:variable>
    <xsl:variable name="DOCUMENTTEMPLATE_intDocTemplateID"><xsl:value-of select="DOCUMENTTEMPLATE_intDocTemplateID" /></xsl:variable>
    <table width='100%'>
        <tr>
                <td colspan='6' class="neuragenix-form-required-text">
                    <xsl:value-of select="strError"/>
                </td>
        </tr>
        <tr>
            <td width="30%" class="uportal-channel-table-header">
                <xsl:value-of select="DOCUMENTTEMPLATE_strExternalNameDisplay" />
            </td>
            <td width="30%" class="uportal-channel-table-header">
                <xsl:value-of select="DOCUMENTTEMPLATE_strTemplateHelpDisplay" />
            </td>
            <td width="10%" class="uportal-channel-table-header">
                <xsl:value-of select="DOCUMENTTEMPLATE_strHiddenDisplay" />
            </td>
            <td width="10%" class="uportal-channel-table-header">
            </td>
            <td width="10%" class="uportal-channel-table-header">
            </td>
            <td width="10%" class="uportal-channel-table-header">
            </td>
        </tr>
        <xsl:for-each select="documents">
        <xsl:variable name="DOCUMENTTEMPLATE_intDocTemplateID"><xsl:value-of select="DOCUMENTTEMPLATE_intDocTemplateID" /></xsl:variable>
            <xsl:choose>
                <xsl:when test="position() mod 2 != 0">
                    <tr class="uportal-background-light">
                        <td width="30%" class="uportal-label">
                            <xsl:value-of select="DOCUMENTTEMPLATE_strExternalName" />
                            
                        </td>
                        <td width="30%" class="uportal-label">
                            <xsl:value-of select="DOCUMENTTEMPLATE_strTemplateHelp" />
                        </td>
                        <td width="10%" class="uportal-label">
                            <xsl:variable name="strHidden"><xsl:value-of select="DOCUMENTTEMPLATE_strHidden" /></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="$strHidden = '0'">
                                    No
                                </xsl:when>
                                <xsl:otherwise>
                                    Yes
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                        <td width="10%" class="uportal-label">
                           <a href="{$baseActionURL}?current=upload_templates&amp;DOCUMENTTEMPLATE_intDocTemplateID={$DOCUMENTTEMPLATE_intDocTemplateID}&amp;edit=true"> Edit</a>
                        </td>
                        <td width="10%" class="uportal-label">
                            <a href="javascript:confirmDelete('{$baseActionURL}?current=upload_templates&amp;DOCUMENTTEMPLATE_intDocTemplateID={$DOCUMENTTEMPLATE_intDocTemplateID}&amp;delete=true')">Delete</a>
                        </td>
                        <td width="10%" class="uportal-label">
                        </td>
                    </tr>
                </xsl:when>
                <xsl:otherwise>
                    <tr>
                        <td width="30%" class="uportal-label">
                            <xsl:value-of select="DOCUMENTTEMPLATE_strExternalName" />
                        </td>
                        <td width="30%" class="uportal-label">
                            <xsl:value-of select="DOCUMENTTEMPLATE_strTemplateHelp" />
                        </td>
                        <td width="10%" class="uportal-label">
                            <xsl:variable name="strHidden"><xsl:value-of select="DOCUMENTTEMPLATE_strHidden" /></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="$strHidden = '0'">
                                    No
                                </xsl:when>
                                <xsl:otherwise>
                                    Yes
                                </xsl:otherwise>
                            </xsl:choose>
                        </td>
                        <td width="10%" class="uportal-label">
                            <a href="{$baseActionURL}?current=upload_templates&amp;DOCUMENTTEMPLATE_intDocTemplateID={$DOCUMENTTEMPLATE_intDocTemplateID}&amp;edit=true"> Edit</a>
                        </td>
                        <td width="10%" class="uportal-label">
                            <a href="javascript:confirmDelete('{$baseActionURL}?current=upload_templates&amp;DOCUMENTTEMPLATE_intDocTemplateID={$DOCUMENTTEMPLATE_intDocTemplateID}&amp;delete=true')">Delete</a>
                        </td>
                        <td width="10%" class="uportal-label">
                        </td>
                    </tr>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </table>
    <table width="100%">
        <tr>
            <td colspan='6'>
                <hr/>
            </td>
        </tr>
    </table>
    <form action="{$baseActionURL}?current=upload_templates" method="post" enctype="multipart/form-data">
        <table width="100%">
            <tr>
                <td colspan='6' class="uportal-channel-table-header">
                    Upload New Templates
                </td>
            </tr> 
            <tr>
                <td width="30%" class="uportal-label">
                    <xsl:value-of select="/upload_templates/DOCUMENTTEMPLATE_strExternalNameDisplay" />
                </td>
                <td width="30%" class="uportal-label">
                    <xsl:value-of select="/upload_templates/DOCUMENTTEMPLATE_strTemplateHelpDisplay" />
                </td>
                <td width="10%" class="uportal-label">
                    <xsl:value-of select="/upload_templates/DOCUMENTTEMPLATE_strHiddenDisplay" />
                </td>
                <td colspan='3' class="uportal-label">
                    File
                </td>
            </tr>
           <tr>
            <td width="30%" class="uportal-label" valign='top'>
                <script language="javascript">
                    function alert_if_not_empty(t) {
                        if (t.value!='') {
                            alert('Changing the name of a saved teplate requires redefining the comments of that teplate');
                            return true;
                        }
                    } 
                </script>
                <input type="text" name="DOCUMENTTEMPLATE_strExternalName" onChange="return alert_if_not_empty(this)" size="30" tabindex="5" value="{$DOCUMENTTEMPLATE_strExternalName}" class="uportal-input-text" />
                <br/>
                Changing the name of a pre existing template will require you to redefine the comments of the report using the link "External Name"
            </td>
            <td width="30%" class="uportal-label" valign='top'>
                <textarea name="DOCUMENTTEMPLATE_strTemplateHelp" rows="5" cols="30" class="uportal-input-text" tabindex="9"><xsl:value-of select="$DOCUMENTTEMPLATE_strTemplateHelp"/></textarea>
            </td>
            <td width="10%" class="uportal-label" valign='top'>
                <select name="DOCUMENTTEMPLATE_strHidden" tabindex="14" class="uportal-input-text">
                    <xsl:choose>
                        <xsl:when test="$DOCUMENTTEMPLATE_strHidden = 0">
                            <option value="Yes">Yes</option>
                            <option value="No" selected="true">No</option>
                        </xsl:when>
                        <xsl:when test="$DOCUMENTTEMPLATE_strHidden = -1">
                            <option value="Yes" selected="true">Yes</option>
                            <option value="No">No</option>
                        </xsl:when>
                        <xsl:otherwise>
                            <option value="Yes">Yes</option>
                            <option value="No" selected="true">No</option>
                        </xsl:otherwise>
                    </xsl:choose>
                </select>
            </td>
            <td colspan='3' class="uportal-label" valign='top'>
                <input type="file" name="DOCUMENTTEMPLATE_strFileName" class="uportal-input-text"/>
            </td>
        </tr> 
        <tr>
                <td width="30%" class="uportal-label">
                <input type="hidden" name="DOCUMENTTEMPLATE_intDocTemplateID" value="{$DOCUMENTTEMPLATE_intDocTemplateID}" />
                    <input type="submit" name="add_button" value="Add" tabindex="24" class="uportal-button" />
                </td>
                <td width="30%" class="uportal-label">
                </td>
                <td width="10%" class="uportal-label">
                </td>
                <td width="10%" class="uportal-label">
                </td>
                <td width="10%" class="uportal-label">
                </td>
                <td width="10%" class="uportal-label">
                </td>                                
        </tr>
        </table>
    </form>
    </xsl:template>

</xsl:stylesheet> 
