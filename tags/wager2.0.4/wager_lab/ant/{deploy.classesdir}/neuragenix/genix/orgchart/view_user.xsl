<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./tree_view.xsl"/>
    <xsl:output method="html" indent="no" />
    
<xsl:template match="info">

    <table width="100%">
    <tr>
        <td class="uportal-channel-subtitle">Details<hr/>
        </td>
    </tr>
  
    <tr>
        <td class="neuragenix-form-required-text"><xsl:value-of select="error"/>
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
<xsl:variable name="userKey"><xsl:value-of select="ORGUSER_intOrgUserKey"/></xsl:variable>
<xsl:variable name="userDeleted"><xsl:value-of select="ORGUSER_intDeleted"/></xsl:variable>
<xsl:variable name="userId"><xsl:value-of select="ORGUSER_strOrgUserId"/></xsl:variable>
<xsl:variable name="userTitle"><xsl:value-of select="ORGUSER_strTitle"/></xsl:variable>
<xsl:variable name="userFirstName"><xsl:value-of select="ORGUSER_strFirstName"/></xsl:variable>
<xsl:variable name="userLastName"><xsl:value-of select="ORGUSER_strLastName"/></xsl:variable>
<xsl:variable name="userInitial"><xsl:value-of select="ORGUSER_strInitial"/></xsl:variable>
<xsl:variable name="userEmail"><xsl:value-of select="ORGUSER_strEmail"/></xsl:variable>
<xsl:variable name="userJobTitle"><xsl:value-of select="ORGUSER_strJobTitle"/></xsl:variable>
<xsl:variable name="userEmploymentType"><xsl:value-of select="ORGUSER_strEmploymentType"/></xsl:variable>
<xsl:variable name="userReport"><xsl:value-of select="ORGUSER_intReportTo"/></xsl:variable>
<xsl:variable name="userSystem"><xsl:value-of select="ORGUSER_strSystemUser"/></xsl:variable>
<xsl:variable name="userWorkNo"><xsl:value-of select="ORGUSER_strWorkNo"/></xsl:variable>
<xsl:variable name="userMobileNo"><xsl:value-of select="ORGUSER_strMobileNo"/></xsl:variable>
<xsl:variable name="userHomeNo"><xsl:value-of select="ORGUSER_strHomeNo"/></xsl:variable>
<xsl:variable name="userDeskNo"><xsl:value-of select="ORGUSER_strDeskNo"/></xsl:variable>
<xsl:variable name="userPhoto"><xsl:value-of select="ORGUSER_strPhoto"/></xsl:variable>
<xsl:variable name="userGroup"><xsl:value-of select="ORGUSER_intGroupKey"/></xsl:variable>
<xsl:variable name="pictureLocation"><xsl:value-of select="pictureLocation"/></xsl:variable>


        <table >


        <form name="userDetails" action="{$baseActionURL}?action=user" method="post" enctype="multipart/form-data">
        
        <tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			<input type="hidden" name="ORGUSER_intOrgUserKey" value="{$userKey}"/>
			<input type="hidden" name="ORGUSER_intGroupKey" value="{$userGroup}"/>
			<input type="hidden" name="ORGUSER_intDeleted" value="{$userDeleted}"/>

			</td>
        
        </tr>
        <tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strOrgUserIdDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			<input type="text" name="ORGUSER_strOrgUserId" size="7" value="{$userId}" class="uportal-input-text"/>

			</td>
        </tr>
        
        <tr>
                   	    <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strTitleDisplay"/>: 
                        </td>
						<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			
			     	<select class="uportal-input-text" name="ORGUSER_strTitle">

						<xsl:for-each select="ORGUSER_strTitle">
                            <xsl:variable name="title"><xsl:value-of select="."/></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="@selected='1'">

                                    <option value="{$title}" selected="true"><xsl:value-of select="." /></option>
                                </xsl:when>
                                <xsl:otherwise>

                                    <option value="{$title}"><xsl:value-of select="." /></option>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:for-each>
					</select>

			</td>
        
        </tr>

       <tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strFirstNameDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			<input type="text" name="ORGUSER_strFirstName" size="30" value="{$userFirstName}" class="uportal-input-text"/>

			</td>
        
        </tr>

       <tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strLastNameDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			<input type="text" name="ORGUSER_strLastName" size="30" value="{$userLastName}" class="uportal-input-text"/>

			</td>
        
        </tr>

       <tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strInitialDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			<input type="text" name="ORGUSER_strInitial" size="3" value="{$userInitial}" class="uportal-input-text"/>

			</td>
        
        </tr>
        <tr>
			
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strSystemUserDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
                        
                        <select class="uportal-input-text" name="ORGUSER_strSystemUser">

                            <xsl:for-each select="systemUser">
                                <xsl:variable name="uid"><xsl:value-of select="SYSTEMUSER_strUserName"/></xsl:variable>
                                <xsl:choose>
                                    <xsl:when test="$userSystem=$uid">

                                        <option value="{$uid}" selected="true"><xsl:value-of select="SYSTEMUSER_strUserName" /></option>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <option value="{$uid}"><xsl:value-of select="SYSTEMUSER_strUserName" /></option>

                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:for-each>
                        </select>



			</td>
        
        </tr>
        
         <tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strEmailDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			<input type="text" name="ORGUSER_strEmail" size="30" value="{$userEmail}" class="uportal-input-text"/>

			</td>
        
        </tr>

       <tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strJobTitleDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			<input type="text" name="ORGUSER_strJobTitle" size="20" value="{$userJobTitle}" class="uportal-input-text"/>

			</td>
        
        </tr>

       <tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strEmploymentTypeDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
				 <select class="uportal-input-text" name="ORGUSER_strEmploymentType">

                        <xsl:for-each select="ORGUSER_strEmploymentType">
                            <xsl:variable name="title"><xsl:value-of select="."/></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="@selected='1'">

                                    <option value="{$title}" selected="true"><xsl:value-of select="." /></option>
                                </xsl:when>
                                <xsl:otherwise>

                                    <option value="{$title}"><xsl:value-of select="." /></option>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:for-each>
                    </select>

			</td>
        
        </tr>

       <tr>
			<!-- need to be changed -->
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_intReportToDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			 <select class="uportal-input-text" name="ORGUSER_intReportTo">

                        <xsl:for-each select="reportTo">
                            <xsl:variable name="uid"><xsl:value-of select="ORGUSER_intOrgUserKey"/></xsl:variable>
                            <xsl:choose>
                                <xsl:when test="$userReport=$uid">

                                    <option value="{$uid}" selected="true"><xsl:value-of select="ORGUSER_strFirstName" />&#160;<xsl:value-of select="ORGUSER_strLastName"/></option>
                                </xsl:when>
                                <xsl:otherwise>
                                    <option value="{$uid}"><xsl:value-of select="ORGUSER_strFirstName" />&#160;<xsl:value-of select="ORGUSER_strLastName"/></option>

                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:for-each>
                    </select>



			</td>
        
        </tr>

       <tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strWorkNoDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			<input type="text" name="ORGUSER_strWorkNo" size="12" value="{$userWorkNo}" class="uportal-input-text"/>

			</td>
        
        </tr>

       <tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strHomeNoDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			<input type="text" name="ORGUSER_strHomeNo" size="12" value="{$userHomeNo}" class="uportal-input-text"/>

			</td>
        
        </tr>

       <tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strMobileNoDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			<input type="text" name="ORGUSER_strMobileNo" size="12" value="{$userMobileNo}" class="uportal-input-text"/>

			</td>
        
        </tr>
       <tr>
                        <td id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strDeskNoDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			<input type="text" name="ORGUSER_strDeskNo" size="5" value="{$userDeskNo}" class="uportal-input-text"/>

			</td>
        
        </tr>


       <tr>
                        <td valign="top" id="neuragenix-form-row-label-required" class="uportal-label">
                        <xsl:value-of select="ORGUSER_strPhotoDisplay"/>: 
                        </td>
			<td class="uportal-label" id="neuragenix-form-row-input" colspan="2">
			<img src="./files/picture_files/{$userPhoto}" alt="Image not available"/> <br/><br/>
			<input class="uportal-input-text" type="file" name="ORGUSER_strPhoto" />
			<input type="hidden" name="ORGUSER_strCurPhoto" value="{$userPhoto}" />

			</td>
        
        </tr>

        <tr>
            <td class="uportal-label" colspan="2" align="left">
                <input class="uportal-input-text" type="submit" name="save_button" value="Save" />
                <input class="uportal-input-text" type="submit" name="search_button" value="Search" />
                <input class="uportal-input-text" type="button" name="delete_user" value="Delete" 
			onclick="javascript:confirmDelete('{$baseActionURL}?action=user&amp;ORGUSER_intOrgUserKey={$userKey}&amp;delete_user=true')"/>
                 
                        
                </td>
        </tr>
        </form>
        
        </table>
        
       
</xsl:template>	
</xsl:stylesheet>
