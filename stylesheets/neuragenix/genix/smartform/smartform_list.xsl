<?xml version="1.0" encoding="utf-8"?>

<!-- 
    smartform_list.xsl, part of the Smartform channel
    Author: hhoang@neuragenix.com
    Date: 26/03/2004
    Neuragenix copyright 2004 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="../../common/common_btn_name.xsl"/>
    
    <xsl:param name="formParams">current=smartform_list</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:param name="caseChannelURL">caseChannelURL_false</xsl:param>
    <xsl:param name="caseChannelTabOrder">caseChannelTabOrder</xsl:param>
    <xsl:param name="workspaceChannelURL">workspaceChannelURL_false</xsl:param>
    <xsl:param name="workspaceChannelTabOrder">workspaceChannelTabOrder</xsl:param>
  
    <xsl:template match="smartform">
    
    <xsl:param name="SMARTFORMPARTICIPANTS_strUserNote"><xsl:value-of select="SMARTFORMPARTICIPANTS_strUserNote" /></xsl:param>
    <xsl:param name="domain"><xsl:value-of select="domain" /></xsl:param>
    <xsl:param name="participant"><xsl:value-of select="participant" /></xsl:param>
    <xsl:param name="strTitle"><xsl:value-of select="strTitle" /></xsl:param>
    <xsl:param name="strBackButton"><xsl:value-of select="strBackButton" /></xsl:param>
    <xsl:param name="isFromWorkspace"><xsl:value-of select="isFromWorkspace" /></xsl:param>
    <xsl:param name="strOrigin"><xsl:value-of select="strOrigin" /></xsl:param>
    <xsl:param name="strSource"><xsl:value-of select="strSource" /></xsl:param>
    <xsl:param name="WORKFLOW_TASK_intTaskKey"><xsl:value-of select="WORKFLOW_TASK_intTaskKey" /></xsl:param>
    
    <script language="javascript" >

	function confirmDelete(tfBox, aURL) {
            var confirmAnswer = confirm('Are you sure you want to delete this record?');

            if(confirmAnswer == true){
		getTextValue(tfBox, aURL);
            }
	}
        
        function jumpTo(aURL){
            window.location=aURL;
        }
        
        function getTextValue(tfBox, aURL){
            
            aURL = aURL + '&amp;SMARTFORMPARTICIPANTS_strUserNote=' + tfBox.value;
            jumpTo(aURL);
        }

        
    </script>
    
    <form name="myForm" action="{$baseActionURL}?{$formParams}" method="post">
    
    <table width="100%">
        <tr>
            <td width="10%"></td>
            <td width="2px" bgcolor="black"></td>
            <td width="1%"></td>
            <td width="20%" class="uportal-label">
                Activity Details
            </td>
            <td width="24%" class="neuragenix-form-required-text">
                <xsl:value-of select="strErrorMessage" />
            </td>
            <td width="2px" bgcolor="black"></td>
            <td width="1%"></td>
            <td width="39%" class="uportal-label">
                Ref: <xsl:value-of select="strTitle" /><!--xsl:value-of select="participant" /-->
            </td>
            <td width="5%">
                <xsl:choose>
                <xsl:when test="$domain = 'CASE'">
                    <xsl:choose>
                        <xsl:when test="$isFromWorkspace = 'true'">
                            <input type="button" name="back" tabindex="4" value="{$backBtnLabel}" class="uportal-button" onclick="javascript:jumpTo('{$workspaceChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$workspaceChannelTabOrder}&amp;current={$strOrigin}&amp;source={$strSource}&amp;WORKFLOW_TASK_intTaskKey={$WORKFLOW_TASK_intTaskKey}')" />
                        </xsl:when>
                        <xsl:otherwise>
                            <input type="button" name="back" tabindex="4" value="{$backBtnLabel}" class="uportal-button" onclick="javascript:jumpTo('{$caseChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$caseChannelTabOrder}{$strBackButton}')" />
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                </xsl:choose>
            </td>
        </tr>
        
    </table>
    <hr/>
    
    <table width="100%">
        <tr>    
            <td width="20%" class="uportal-channel-table-header">
                Activity details
            </td>
            <td width="15%" class="uportal-channel-table-header">
                <xsl:value-of select="SMARTFORMPARTICIPANTS_strUserNoteDisplay" />
            </td>
            <td width="10%" class="uportal-channel-table-header">
                <xsl:value-of select="SMARTFORMPARTICIPANTS_strAddedByDisplay" />
            </td>
            <td width="10%" class="uportal-channel-table-header">
                <xsl:value-of select="SMARTFORMPARTICIPANTS_dtDateAddedDisplay" />
            </td>
            <td width="10%" class="uportal-channel-table-header">
                <xsl:value-of select="SMARTFORMPARTICIPANTS_strLastUpdatedByDisplay" />
            </td>
            <td width="10%" class="uportal-channel-table-header">
                <xsl:value-of select="SMARTFORMPARTICIPANTS_dtDateLastUpdatedDisplay" />
            </td>
            <td width="10%" class="uportal-channel-table-header">
                <xsl:value-of select="SMARTFORMPARTICIPANTS_strSmartformStatusDisplay" />
            </td>
            <td width="15%" class="uportal-channel-table-header">
            </td>
        </tr>
        
        <tr>
            <td height="10px"></td>
        </tr>
        
        <xsl:for-each select="search_smartform_participant">
        <xsl:variable name="varSmartformParticipantID"><xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformParticipantID" /></xsl:variable>
        <xsl:variable name="varSmartformID"><xsl:value-of select="SMARTFORMPARTICIPANTS_intSmartformID" /></xsl:variable>
        <xsl:variable name="varCurrentPage"><xsl:value-of select="SMARTFORMPARTICIPANTS_intCurrentPage" /></xsl:variable>
        <xsl:variable name="varUserNote"><xsl:value-of select="SMARTFORMPARTICIPANTS_strUserNote" /></xsl:variable>
        <xsl:variable name="varSmartformStatus"><xsl:value-of select="SMARTFORMPARTICIPANTS_strSmartformStatus" /></xsl:variable>
        
        <tr>    
            <xsl:choose>
            <xsl:when test="position() mod 2 != 0">

        
                <td width="20%" class="uportal-input-text">
                    <xsl:value-of select="SMARTFORM_strSmartformName" />
                </td>
                <td width="15%" class="uportal-input-text">
                    <input type="text" name="SMARTFORMPARTICIPANTS_strUserNote{$varSmartformParticipantID}" size="25" value="{$varUserNote}" class="uportal-input-text" />
                </td>
                <td width="10%" class="uportal-input-text">
                    <xsl:value-of select="SMARTFORMPARTICIPANTS_strAddedBy" />
                </td>
                <td width="10%" class="uportal-input-text">
                    <xsl:value-of select="SMARTFORMPARTICIPANTS_dtDateAdded" />
                </td>
                <td width="10%" class="uportal-input-text">
                    <xsl:value-of select="SMARTFORMPARTICIPANTS_strLastUpdatedBy" />
                </td>
                <td width="10%" class="uportal-input-text">
                    <xsl:value-of select="SMARTFORMPARTICIPANTS_dtDateLastUpdated" />
                </td>
                <td width="10%" class="uportal-input-text">
                    <a href="{$baseActionURL}?current=smartform_result_view&amp;domain={$domain}&amp;participant={$participant}&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}&amp;SMARTFORMPARTICIPANTS_intCurrentPage={$varCurrentPage}&amp;SMARTFORMPARTICIPANTS_strSmartformStatus={$varSmartformStatus}">
                        <xsl:value-of select="SMARTFORMPARTICIPANTS_strSmartformStatus" />
                    </a>
                </td>
                <td width="15%" class="uportal-input-text">
                    <a href="javascript:getTextValue(document.myForm.SMARTFORMPARTICIPANTS_strUserNote{$varSmartformParticipantID}, '{$baseActionURL}?current=smartform_list&amp;domain={$domain}&amp;participant={$participant}&amp;save=true&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}')">
                        Save
                    </a>
                    &#160;
                    <a href="javascript:getTextValue(document.myForm.SMARTFORMPARTICIPANTS_strUserNote{$varSmartformParticipantID}, '{$baseActionURL}?current=smartform_list&amp;domain={$domain}&amp;participant={$participant}&amp;clone=true&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}')">
                        Copy
                    </a>
                    &#160;
                    <a href="javascript:confirmDelete(document.myForm.SMARTFORMPARTICIPANTS_strUserNote{$varSmartformParticipantID}, '{$baseActionURL}?current=smartform_list&amp;domain={$domain}&amp;participant={$participant}&amp;delete=true&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}')">
                        Del
                    </a>
                </td>
            
            </xsl:when>
            <xsl:otherwise>
            
                <td width="20%" class="uportal-text">
                    <xsl:value-of select="SMARTFORM_strSmartformName" />
                </td>
                <td width="15%">
                    <input type="text" name="SMARTFORMPARTICIPANTS_strUserNote{$varSmartformParticipantID}" size="25" value="{$varUserNote}" class="uportal-input-text" />
                </td>
                <td width="10%" class="uportal-text">
                    <xsl:value-of select="SMARTFORMPARTICIPANTS_strAddedBy" />
                </td>
                <td width="10%" class="uportal-text">
                    <xsl:value-of select="SMARTFORMPARTICIPANTS_dtDateAdded" />
                </td>
                <td width="10%" class="uportal-text">
                    <xsl:value-of select="SMARTFORMPARTICIPANTS_strLastUpdatedBy" />
                </td>
                <td width="10%" class="uportal-text">
                    <xsl:value-of select="SMARTFORMPARTICIPANTS_dtDateLastUpdated" />
                </td>
                <td width="10%" class="uportal-text">
                    <a href="{$baseActionURL}?current=smartform_result_view&amp;domain={$domain}&amp;participant={$participant}&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}&amp;SMARTFORMPARTICIPANTS_intCurrentPage={$varCurrentPage}&amp;SMARTFORMPARTICIPANTS_strSmartformStatus={$varSmartformStatus}">
                        <xsl:value-of select="SMARTFORMPARTICIPANTS_strSmartformStatus" />
                    </a>
                </td>
                <td width="15%" class="uportal-text">
                    <a href="javascript:getTextValue(document.myForm.SMARTFORMPARTICIPANTS_strUserNote{$varSmartformParticipantID}, '{$baseActionURL}?current=smartform_list&amp;domain={$domain}&amp;participant={$participant}&amp;save=true&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}')">
                        Save
                    </a>
                    &#160;
                    <a href="javascript:getTextValue(document.myForm.SMARTFORMPARTICIPANTS_strUserNote{$varSmartformParticipantID}, '{$baseActionURL}?current=smartform_list&amp;domain={$domain}&amp;participant={$participant}&amp;clone=true&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}')">
                        Copy
                    </a>
                    &#160;
                    <a href="javascript:confirmDelete(document.myForm.SMARTFORMPARTICIPANTS_strUserNote{$varSmartformParticipantID}, '{$baseActionURL}?current=smartform_list&amp;domain={$domain}&amp;participant={$participant}&amp;delete=true&amp;SMARTFORMPARTICIPANTS_intSmartformParticipantID={$varSmartformParticipantID}&amp;SMARTFORMPARTICIPANTS_intSmartformID={$varSmartformID}')">
                        Del
                    </a>
                </td>
            
            </xsl:otherwise>
            </xsl:choose>
        </tr>
        </xsl:for-each>
    </table>
    <hr/>
    
    <table width="100%">
        <tr>    
            <td colspan="3" class="uportal-channel-table-header">
                Add new smart form
            </td>
        </tr>
        <tr>
            <td width="20%">
                <select name="SMARTFORMPARTICIPANTS_intSmartformID" tabindex="1" class="uportal-input-text">
                    <xsl:for-each select="search_smartform">
                        
                        <option>
                            <xsl:attribute name="value">
                                <xsl:value-of select="SMARTFORM_intSmartformID" />
                            </xsl:attribute> 
                            <xsl:value-of select="SMARTFORM_strSmartformName" />
                        </option>
                        
                    </xsl:for-each>
                </select>
            </td>
            <td width="15%">
                <input type="text" name="SMARTFORMPARTICIPANTS_strUserNote" size="25" tabindex="2" value="{$SMARTFORMPARTICIPANTS_strUserNote}" class="uportal-input-text" />
            </td>
            <td width="65%"></td>
        </tr>
    </table>
    
    <table width="100%">
        <tr>
            <td width="100%">
                <input type="submit" name="add" tabindex="3" value="{$addBtnLabel}" class="uportal-button" />
            </td>
        </tr>
    </table>
    
    <!-- hidden fields to send data to server -->
    <input type="hidden" name="domain" value="{$domain}" />
    <input type="hidden" name="participant" value="{$participant}" />
    <input type="hidden" name="SMARTFORMPARTICIPANTS_strDomain" value="{$domain}" />
    <input type="hidden" name="SMARTFORMPARTICIPANTS_intParticipantID" value="{$participant}" />
    
    </form>
    </xsl:template>

</xsl:stylesheet>
