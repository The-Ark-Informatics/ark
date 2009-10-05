<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./study_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

  <xsl:output method="html" indent="no" />

  <xsl:template match="study">
	<table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Search results<br/><hr/>
			</td>
		</tr>
	</table>
	
	<table width="100%">
		<tr>
			<td class="stripped_column_heading">
				<a href="{$baseActionURL}?current=study_results&amp;orderBy=studyName"  class="stripped_column_heading">
					<xsl:value-of select="strStudyNameDisplay" />
				</a>
			</td>
			<td class="stripped_column_heading">
                                <a href="{$baseActionURL}?current=study_results&amp;orderBy=studyOwner" class="stripped_column_heading">
					<xsl:value-of select="strStudyOwnerDisplay" />
                                </a>
			</td>
			<td class="stripped_column_heading">
                                <a href="{$baseActionURL}?current=study_results&amp;orderBy=studyStart"  class="stripped_column_heading">
					<xsl:value-of select="dtStudyStartDisplay" />
                                </a>
			</td>
			<td class="stripped_column_heading">
                                <a href="{$baseActionURL}?current=study_results&amp;orderBy=studyEnd"  class="stripped_column_heading">
		 		<xsl:value-of select="dtStudyEndDisplay" />
                                </a>
			</td>
		</tr>
		<xsl:for-each select="searchResult">
		<xsl:variable name="intStudyID"><xsl:value-of select="intStudyID" /></xsl:variable>
		
		    <xsl:choose>
                        <xsl:when test="position() mod 2 != 0">
				<tr class="stripped_light">
				<td class="stripped_light" >
					<a href="{$baseActionURL}?current=study_results&amp;intStudyID={$intStudyID}" >
						<xsl:value-of select="strStudyName" />
					</a>
				</td>
				<td class="stripped_light" >
					<a href="{$baseActionURL}?current=study_results&amp;intStudyID={$intStudyID}">
						<xsl:value-of select="strStudyOwner" />
					</a>
				</td>
				<td class="stripped_light" >
					<a href="{$baseActionURL}?current=study_results&amp;intStudyID={$intStudyID}">
						<xsl:value-of select="dtStudyStart" />
					</a>
				</td>
				<td class="stripped_light">
					<a href="{$baseActionURL}?current=study_results&amp;intStudyID={$intStudyID}">
						<xsl:value-of select="dtStudyEnd" />
					</a>
				</td>
				</tr>
                        </xsl:when>
			<xsl:otherwise>
				<tr class="stripped_dark" >
				<td class="stripped_dark" >
					<a href="{$baseActionURL}?current=study_results&amp;intStudyID={$intStudyID}" >
						<xsl:value-of select="strStudyName" />
					</a>
				</td>
				<td class="stripped_dark" >
					<a href="{$baseActionURL}?current=study_results&amp;intStudyID={$intStudyID}">
						<xsl:value-of select="strStudyOwner" />
					</a>
				</td>
				<td class="neuragenix-form-row-label" >
					<a href="{$baseActionURL}?current=study_results&amp;intStudyID={$intStudyID}">
						<xsl:value-of select="dtStudyStart" />
					</a>
				</td>
				<td class="stripped_dark">
					<a href="{$baseActionURL}?current=study_results&amp;intStudyID={$intStudyID}">
						<xsl:value-of select="dtStudyEnd" />
					</a>
				</td>
				</tr>
                        </xsl:otherwise>
                    </xsl:choose>
		
		</xsl:for-each>
	</table>
	<table width="100%">
		<form action="{$baseActionURL}?current=study_results" method="post">
                <tr>
                    <td><br /></td>
                </tr>
		<tr>
                    <td align="left" id="stripped_dark" class="uportal-label">
                            <input type="submit" name="submit" value="{$searchBtnLabel}" class="uportal-button" />
                    </td>
		</tr>
		</form>

			</table>
  </xsl:template>

      
       
 
</xsl:stylesheet>
