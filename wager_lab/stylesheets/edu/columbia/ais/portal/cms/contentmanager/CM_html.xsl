<?xml version='1.0'?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:cms="http://www.ais.columbia.edu/sws/xmlns/cucms#" xmlns:cfs="http://www.ais.columbia.edu/sws/xmlns/cufs#" xmlns:dc="http://purl.org/dc/elements/1.0/" version="1.0">
  <xsl:output method="html" indent="yes" />
  <xsl:param name="baseActionURL">default</xsl:param>
  <xsl:param name="baseDownloadURL">default</xsl:param>
  <xsl:param name="feedback" select="false()"/>
  <xsl:param name="screen" select="'browse'"/>
  <xsl:param name="screenParm" select="false()"/>
  <xsl:param name="sortOrder" select="relevance"/>
  <xsl:param name="highlightPath" select="'/'"/>
  <!--<xsl:param name="handleFile" select="false()"/>-->
  <xsl:param name="spacerIMG" select="'media/org/jasig/portal/channels/CUserPreferences/tab-column/transparent.gif'"/>
  <xsl:param name="mediaBase" select="'media/edu/columbia/ais/portal'"/>
  <xsl:param name="userKey"/>
  <xsl:param name="publishWindow" select="'CuCMS_Publishing_Engine_Window'"/>
  <xsl:param name="random">abcdefg</xsl:param>
  <xsl:param name="projectTitle"/>

  <xsl:key name="node" match="node" use="@path"/>
  <xsl:key name="output" match="output" use="@uid"/>
  <xsl:key name="tempFile" match="temp-file" use="concat(@editor,':',parent::file-def/parent::node/@path)"/>
  <xsl:key name="contentType" match="/content-manager/content-types/content-type" use="@type"/>
  <xsl:key name="icon" match="/content-manager/content-types/content-type/@icon" use="parent::content-type/file-extension/@value"/>

  <xsl:variable name="highlightNode" select="key('node',$highlightPath)"/>
  <xsl:variable name="highlightFile" select="$highlightNode/file-def"/>
  <xsl:variable name="currentEdition" select="$highlightFile/edition[string(@index)=string($highlightFile/@currentEdition)]"/>
  <xsl:variable name="highlightResource" select="$currentEdition/rdf:RDF/cfs:File"/>
  
  
  <xsl:variable name="canOutputNode" select="((not($highlightNode/file-def)) or ($highlightNode/file-def/@currentEdition and (not($highlightNode/file-def/@currentEdition='-1')))) and $highlightNode/@hasOutput='true'"/>
  <xsl:variable name="canReadOnly" select="(not(string($highlightNode/@canWrite)='true')) and string($highlightNode/@canRead)='true'"/>
  <xsl:variable name="canBuildNode" select="$canOutputNode and /content-manager/cms-project/build and $highlightNode/@canBuild='true'"/>
  <xsl:variable name="canPublishNode" select="$canOutputNode and /content-manager/cms-project/publish and $highlightNode/@canPublish='true'"/>
  <xsl:variable name="canLabelNode" select="(string($highlightNode/@canWrite)='true' and not($highlightFile))"/>

  <xsl:variable name="hasCreateSub" select="($highlightFile and $highlightNode/parent::node/newFileOptions/newFileOption) or (not($highlightFile) and $highlightNode/newFileOptions/newFileOption)"/>
  <xsl:variable name="hasOutputSub" select="$highlightNode/@hasOutput='true' and ((count($highlightFile/file-output) &gt; 0) or not($highlightFile))"/>
  <xsl:variable name="hasReferencesSub" select="count($highlightNode/references/hit) &gt; 0"/>
  <xsl:variable name="hasCopyMoveSub" select="$highlightNode/moveOptions/moveOption or $highlightNode/copyOptions/copyOption"/>
  <xsl:variable name="hasAdminSub" select="string($highlightNode/@canDelete)='true' or string($highlightNode/@canWrite)='true' or $canLabelNode or $canBuildNode or $canPublishNode"/>
  <xsl:variable name="hasRevisionsSub" select="$highlightFile"/>
 <xsl:variable name="iframename">outputframe</xsl:variable>
  <xsl:variable name="browseSubScreen">
  	<xsl:choose>
  		<!--<xsl:when test="$highlightFile and (not($screenParm) or ($screenParm='create')) and $highlightNode/parent::node/newFileOptions/newFileOption">create</xsl:when>
  		<xsl:when test="(not($screenParm) or ($screenParm='create')) and $highlightNode/newFileOptions/newFileOption">create</xsl:when> -->
        <xsl:when test="(not($screenParm) or ($screenParm='output')) and $hasOutputSub">output</xsl:when>
        <xsl:when test="(not($screenParm) or ($screenParm='create')) and $hasCreateSub">create</xsl:when>
        <xsl:when test="(not($screenParm) or ($screenParm='copy')) and $hasCopyMoveSub">copy</xsl:when>
        <xsl:when test="(not($screenParm) or ($screenParm='admin')) and $hasAdminSub">admin</xsl:when>
		<!--xsl:when test="(not($screenParm) or ($screenParm='output')) and $hasOutputSub">output</xsl:when-->
		<xsl:when test="(not($screenParm) or ($screenParm='references')) and $hasReferencesSub">references</xsl:when>
		<xsl:when test="(not($screenParm) or ($screenParm='revisions')) and $hasRevisionsSub">revisions</xsl:when>
		<xsl:otherwise>false</xsl:otherwise>
	</xsl:choose>
  </xsl:variable>


  <xsl:template match="/">
  	<xsl:if test="$feedback">
      <span class="uportal-channel-warning">
        <xsl:value-of select="$feedback"/>
      </span>
    </xsl:if>
  	<xsl:choose>
  		<!--
		<xsl:when test="$handleFile">
		  <xsl:call-template name="handleFile"/>
		</xsl:when>
		-->
        <xsl:when test="(count(/content-manager/cms-project) = 1) and (count(content-manager/repository-nodes/node)=0)">
  			<xsl:call-template name="handlePrompt"/>
  		</xsl:when>
  		<xsl:when test="count(/content-manager/cms-project) = 1">
  			<xsl:apply-templates select="/content-manager/cms-project"/>
  		</xsl:when>
  		<xsl:when test="count(/content-manager/project-listing) = 0">
  			<span class = "uportal-channel-warning"><xsl:value-of select="$NO_PROJECTS"/></span>
  		</xsl:when>
  		<xsl:otherwise>
  			<xsl:call-template name="selectProject"/>
  		</xsl:otherwise>
  	</xsl:choose>
  </xsl:template>

    <xsl:template name="handlePrompt">
        <form action="{$baseActionURL}">
            <input type="hidden" name="CMCommand" value="setArgs"/>
        <table width="100%" border="0" cellspacing="0" cellpadding="2">
    		<xsl:call-template name="titleRow"/>
    		<tr><td colspan="6" class="uportal-channel-text">
            <xsl:for-each select="/content-manager/cms-project/repository/filesystem-def/argument">
                <xsl:if test="string-length(@prompt) &gt; 0">
                    <xsl:value-of select="@prompt"/>
                    <input class="uportal-input-text" type="{@type}" name="FsArg_{@prompt}" value="{@value}"/>
                    <br/>
                </xsl:if>
            </xsl:for-each>
                <input type="submit"/>
        </td>
        </tr>
        </table>
        </form>
    </xsl:template>

  <xsl:template name="selectProject">
  	<table cellspacing="0" cellpadding="2">
  		<tr><td colspan="2" class="uportal-channel-table-header">
  			<xsl:value-of select="$SELECT_PROJECT"/>
  		</td></tr>
  		<xsl:for-each select="/content-manager/project-listing">
            <xsl:sort select="@title" order="ascending" data-type="text"/>
  			<tr><td colspan="2" class="uportal-channel-text">
  				<a href="{$baseActionURL}?key={@key}&amp;CMCommand=openProject&amp;uP_root=root">
  				<xsl:value-of select="@title"/>
  				</a>
  			</td></tr>
  			<tr><td> <img src="{$spacerIMG}" width="20" height="1"/> </td>
  				<td class="uportal-channel-table-row-even"><xsl:value-of select="@description"/>
  				</td>
  			</tr>
  		</xsl:for-each>
  	</table>
  </xsl:template>

  <xsl:template name="header">
  	    		<table width="100%" border="0" cellspacing="0" cellpadding="2">
    		<xsl:call-template name="titleRow"/>
    		<tr>
			<td width="100%" colspan="6"  class="uportal-background-light" align="center">
				<span class="uportal-channel-table-row-even">
				<xsl:choose>
					<xsl:when test="$screen='search'">
						<span class="uportal-channel-strong">
						<xsl:value-of select="$SEARCH"/>
						</span>
					</xsl:when>
					<xsl:otherwise>
						<a href="{$baseActionURL}?CMScreen=search">
							<xsl:value-of select="$SEARCH"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>

				|

				<xsl:choose>
					<xsl:when test="$screen='browse'">
						<span class="uportal-channel-strong">
						<xsl:value-of select="$BROWSE"/>
						</span>
					</xsl:when>
					<xsl:otherwise>
						<a href="{$baseActionURL}?CMScreen=browse">
							<xsl:value-of select="$BROWSE"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>

				|


				<!--rennypvxsl:if test="/content-manager/cms-project/build">
					<xsl:if test="/content-manager/repository-nodes/node/@canBuild='true'">
					 <a href="javascript:window.open('{$baseDownloadURL}?CMCommand=buildNode&amp;path=/&amp;random={$random}','{$publishWindow}').focus();"><xsl:value-of select="$BUILD_ALL"/></a> |
					</xsl:if>
					<a href="javascript:window.open('{/content-manager/cms-project/build/@base-url}','{$publishWindow}').focus();"><xsl:value-of select="$VIEW_BUILT"/></a> |

				</xsl:if-->

				<!--rennypvxsl:if test="/content-manager/cms-project/publish">
					<xsl:if test="/content-manager/repository-nodes/node/@canPublish='true'">
					 <a href="javascript:window.open('{$baseDownloadURL}?CMCommand=publishNode&amp;path=/&amp;random={$random}','{$publishWindow}').focus();"><xsl:value-of select="$PUBLISH_ALL"/></a> |

					 </xsl:if>
					<a href="javascript:window.open('{/content-manager/cms-project/publish/@base-url}','{$publishWindow}').focus();"><xsl:value-of select="$VIEW_PUBLISHED"/></a> |
				</xsl:if-->

				<xsl:choose>
					<xsl:when test="$screen='project-admin'">
						<span class="uportal-channel-strong">
						<xsl:value-of select="$ADMIN"/>
						</span>
					</xsl:when>
					<xsl:otherwise>
						<a href="{$baseActionURL}?CMScreen=project-admin">
							<xsl:value-of select="$ADMIN"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
                                |
				<!-- rennypv-->

				 <xsl:if test="/content-manager/repository-nodes/node/@canAssign='true'">
				 <a href="{$baseActionURL}?CMCommand=assignPermissions&amp;path=/">Project Permissions</a>
				 </xsl:if>
				 <!--rennypv-->
				 </span>
			</td>
			</tr>
			<tr>
        	<td height="5">
						<img src="{$spacerIMG}" width="2" height="5"/>
				</td>
			</tr>
			</table>
  </xsl:template>

  <xsl:template match="cms-project">


  	<xsl:call-template name="header"/>
    <xsl:choose>
    	<xsl:when test="$screen='search'">
    		<xsl:call-template name="search"/>
    	</xsl:when>
    	<xsl:when test="$screen='project-admin'">
    		<xsl:call-template name="project-admin"/>
    	</xsl:when>
    	<xsl:when test="$screen='browse'">
			<xsl:call-template name="browse"/>
		</xsl:when>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="project-admin">
    <xsl:if test="/content-manager/repository-nodes/node/@canWrite='true'">
        <a href="{$baseActionURL}?CMCommand=optimizeIndex">
            <xsl:value-of select="$OPTIMIZE"/>
        </a>
        <br/>
        <a href="javascript:window.open('{$baseDownloadURL}?CMCommand=rebuildIndex&amp;random={$random}','{$publishWindow}').focus();"><xsl:value-of select="$REBUILD_INDEX"/></a>
        <br/>
    </xsl:if>
	<xsl:if test="/content-manager/repository-nodes/node/@canAssign='true'">
	 <a href="{$baseActionURL}?CMCommand=assignPermissions&amp;path=/"><xsl:value-of select="$PROJECT_PERMISSIONS"/></a>
	 <br/>
	</xsl:if>
    <br/>


  </xsl:template>

  <xsl:template name="browse">
  	<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td rowspan="3" valign="top">
					<xsl:apply-templates select="/content-manager/repository-nodes/node">
							<xsl:with-param name="depth" select="number(0)"/>
					</xsl:apply-templates>
				</td>

			</tr>
			<xsl:if test="$highlightNode">
				<tr>
				<td class="uportal-background-highlight" rowspan="2" width="2"><img src="{$spacerIMG}" width="2" height="2"/></td>
                                        <td valign="top">

						<xsl:call-template name="rightPane"/>

					</td>
                                        <td class="uportal-background-highlight" rowspan="2" width="2"><img src="{$spacerIMG}" width="2" height="2"/></td>
				</tr>
				<tr><td></td></tr>
				<tr><td></td>
					<td colspan="3" height="2">
						<xsl:if test="$highlightNode">
							<xsl:attribute name="class">uportal-background-highlight</xsl:attribute>
							<img src="{$spacerIMG}" width="2" height="2"/>
						</xsl:if>
					</td>
				</tr>
			</xsl:if>
			</table>
  </xsl:template>
  <xsl:template name="rightPane">
    <SCRIPT LANGUAGE='JavaScript1.2' TYPE='text/javascript'>
		function cmsConfirmDeleteFile(url){
			if (window.confirm('<xsl:value-of select="$highlightNode/warnings/delete"/>')){
				this.location.href=url;
			}
		}
		function cmsConfirmRemoveEdition(url){
			if (window.confirm('<xsl:value-of select="$highlightNode/warnings/deleteEdition"/>')){
				this.location.href=url;
			}
		}
   </SCRIPT>

  	<table width="100%" border="0" cellpadding="1" cellspacing="0">
  		<tr>
			<td colspan="2" nowrap="nowrap" align="left" class="uportal-background-highlight">
				<span  class="uportal-channel-strong">
				<xsl:choose>
					<xsl:when test="$highlightResource/dc:title">
						<a title="{$highlightNode/parent::node/@path}" href="{$baseActionURL}?CMCommand=highlightNode&amp;path={$highlightNode/parent::node/@path}">
							<img width="16" align="middle" height="16" src="{$mediaBase}/parentdirectory.gif" border="0" vspace="0" hspace="1"/>
						</a>
						<xsl:value-of select="$highlightResource/dc:title"/>
					</xsl:when>
					<xsl:when test="$highlightPath='/'">
						<xsl:value-of select="$projectTitle"/>: <em><xsl:value-of select="$SITE_ROOT"/></em>
					</xsl:when>
					<xsl:otherwise>
						<a title="{$highlightNode/parent::node/@path}" href="{$baseActionURL}?CMCommand=highlightNode&amp;path={$highlightNode/parent::node/@path}">
							<img  width="16" height="16" src="{$mediaBase}/parentdirectory.gif" border="0" vspace="0" hspace="1"/>
						</a>
						<xsl:value-of select="$highlightNode/@label"/>
					</xsl:otherwise>
				</xsl:choose>
  				<!--<xsl:value-of select="$highlightPath"/>-->
  				</span>
  			</td>
  			<td class="uportal-background-highlight" align="right" nowrap="nowrap">
  				<!-- icons -->
  				<xsl:if test="($highlightNode/@canWrite='true') and ($highlightNode/@hasEditorFactory='true')">
					<a title="{$EDIT}" href="{$baseActionURL}?CMCommand=openFile&amp;path={$highlightPath}&amp;edition={$currentEdition/@index}">
					<img alt="{$EDIT}" width="16" height="16" src="{$mediaBase}/edit.gif" border="0" vspace="0" hspace="1"/>
					</a>
				</xsl:if>
				<xsl:if test="($highlightNode/@hasOutput='true') and (/content-manager/cms-project/preview)">
					<a title="{$PREVIEW}" href="javascript:window.open('{$baseDownloadURL}?CMCommand=previewNode&amp;previewPath={$highlightPath}&amp;edition={$currentEdition/@index}&amp;random={$random}','{$publishWindow}').focus();">
					<img alt="{$PREVIEW}" width="16" height="16" src="{$mediaBase}/eye.gif" border="0" vspace="0" hspace="1"/>
					</a>
				</xsl:if>
				<xsl:if test="(string($highlightNode/@canDelete)='true') and not(string($highlightNode/@isRequired)='true')">
  					<a title="{$DELETE}" href="javascript:cmsConfirmDeleteFile('{$baseActionURL}?CMCommand=deleteFile&amp;path={$highlightPath}');">
  					<img alt="{$DELETE}" width="16" height="16" src="{$mediaBase}/delete.gif" border="0" vspace="0" hspace="1"/>
  					</a>
				</xsl:if>
  			</td>
  		</tr>

  		<tr>
			<td colspan="3" align="center"  class="uportal-background-light">
				<span class="uportal-channel-table-row-even">
                    <!--rennypvxsl:if test="$canBuildNode">
                        <a href="javascript:window.open('{$baseDownloadURL}?CMCommand=buildNode&amp;path={$highlightPath}&amp;random={$random}','{$publishWindow}').focus();"><xsl:value-of select="$BUILD"/></a>
                        <xsl:if test="$canPublishNode or ($highlightNode/@canAssign='true')">
                        |
                        </xsl:if>
                    </xsl:if  rennypv-->
                    <!-- rennypv xsl:if test="$canPublishNode">
                        <a href="javascript:window.open('{$baseDownloadURL}?CMCommand=publishNode&amp;path={$highlightPath}&amp;random={$random}','{$publishWindow}').focus();"><xsl:value-of select="$PUBLISH"/></a>
                        <xsl:if test="($highlightNode/@canAssign='true')">
                        |
                        </xsl:if>
                    </xsl:if  rennypv-->
                    <xsl:if test="($highlightNode/@canAssign='true')">
                        <a href="{$baseActionURL}?CMCommand=assignPermissions&amp;path={$highlightPath}"><xsl:value-of select="$PERMISSIONS"/></a>
                    </xsl:if>
                    <!--This is an example where the path and the project are set to set permissions without having to go to the uPortal permissions interface-->
                    <!--xsl:if test="($highlightNode/@canAssign='true')">
                    |
                    </xsl:if>
                    <xsl:if test="($highlightNode/@canAssign='true')">
                        <a href="{$baseActionURL}?CMCommand=assignPermissions&amp;path={$highlightPath}&amp;project=/projects/BioHelp.xml&amp;owner=user1">PermissionsManager</a>
                    </xsl:if-->
				</span>
			</td>
		</tr>
		<tr>
			<td width="10"><img src="{$spacerIMG}" height="5" width="10"/></td>
			<td width="100%"><img src="{$spacerIMG}" height="5" width="10"/></td>
			<td width="10"><img src="{$spacerIMG}" height="5" width="10"/></td>
		</tr>

		<xsl:if test="$highlightResource/dc:description">

			<tr>
				<td/>
				<td valign="top" colspan="2">
					<!--<span class="uportal-channel-table-row-even"><xsl:text>Description: </xsl:text></span>-->
					<span class="uportal-channel-table-row-odd"><xsl:value-of select="$highlightResource/dc:description"/></span>
				</td>
			</tr>
			<xsl:call-template name="spacerow"/>
		</xsl:if>


  		</table>


		<xsl:if test="$highlightFile">
			<xsl:variable name="currentEdition" select="$highlightFile/edition[string(@index)=string($highlightFile/@currentEdition)]"/>

			<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<input type="hidden" name="path" value="{$highlightPath}"/>

			<xsl:if test="key('tempFile',concat($userKey,':',$highlightPath))">
				<xsl:call-template name="hrow"/>
				<xsl:call-template name="edition">
					<xsl:with-param name="key" select="'temp'"/>
					<xsl:with-param name="path" select="$highlightPath"/>
					<xsl:with-param name="node" select="$highlightNode"/>
					<xsl:with-param name="name" select="concat($DRAFT_FILE,' - ',key('tempFile',concat($userKey,':',$highlightPath))/@date)"/>
					<xsl:with-param name="comment" select="key('tempFile',concat($userKey,':',$highlightPath))/rdf:RDF/cfs:File/cms:comment[string-length((.))>0]"/>
					<xsl:with-param name="editor" select="key('tempFile',concat($userKey,':',$highlightPath))/rdf:RDF/cfs:File/cms:editor"/>
					<xsl:with-param name="showRadio" select="false()"/>
				</xsl:call-template>
				<xsl:call-template name="hrow"/>
			</xsl:if>
			</table>
		</xsl:if>


		<!-- begin inset function box -->
		<xsl:if test="not($browseSubScreen='false')">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<xsl:call-template name="spacerow"/>
				<tr>
					<td rowspan="5" width="10"><img src="{$spacerIMG}" width="10" height="1"/></td>
					<td rowspan="5" width="1" class="uportal-background-dark"><img src="{$spacerIMG}" width="1" height="1"/></td>
					<td class="uportal-background-dark"><img src="{$spacerIMG}" width="1" height="1"/></td>
					<td rowspan="5" width="1" class="uportal-background-dark"><img src="{$spacerIMG}" width="1" height="1"/></td>
					<td rowspan="5" width="10"><img src="{$spacerIMG}" width="10" height="1"/></td>
				</tr>
				<tr>
				<td align="center" class="uportal-background-light">
					<span  class="uportal-channel-table-row-even">
					<!-- <xsl:value-of select="$browseSubScreen"/> -->
					<xsl:if test="$hasCreateSub">
						<xsl:choose>
							<xsl:when test="$browseSubScreen='create'">
								<span class="uportal-channel-strong">
								<xsl:value-of select="$CREATE"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<a href="{$baseActionURL}?CMScreen=browse&amp;CMScreenParm=create">
									<xsl:value-of select="$CREATE"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>

						<!--xsl:if test="$hasCopyMoveSub or $hasAdminSub or $hasOutputSub or $hasReferencesSub or $hasRevisionsSub"-->
                                                <xsl:if test="$hasCopyMoveSub or $hasOutputSub or $hasReferencesSub or $hasRevisionsSub">
							|
						</xsl:if>
					</xsl:if>
                    <xsl:if test="$hasCopyMoveSub">
						<xsl:choose>
							<xsl:when test="$browseSubScreen='copy'">
								<span class="uportal-channel-strong">
								<xsl:value-of select="$COPY_MOVE"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<a href="{$baseActionURL}?CMScreen=browse&amp;CMScreenParm=copy">
									<xsl:value-of select="$COPY_MOVE"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>

						<!--xsl:if test="$hasAdminSub or $hasOutputSub or $hasReferencesSub or $hasRevisionsSub"-->
                                                <xsl:if test="$hasOutputSub or $hasReferencesSub or $hasRevisionsSub">
							|
						</xsl:if>
					</xsl:if>
                                        <!--introducing this bit for teh zip option-->
                    <xsl:if test="$hasAdminSub">
                        <xsl:choose>
							<xsl:when test="$browseSubScreen='admin'">
								<span class="uportal-channel-strong">
								<xsl:value-of select="$ADMIN"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<a href="{$baseActionURL}?CMScreen=browse&amp;CMScreenParm=admin">
									<xsl:value-of select="$ADMIN"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>

						<xsl:if test="$hasOutputSub or $hasReferencesSub or $hasRevisionsSub">
							|
						</xsl:if>
                     </xsl:if>
					<xsl:if test="$hasOutputSub">
						<xsl:choose>
							<xsl:when test="$browseSubScreen='output'">
								<span class="uportal-channel-strong">
								<xsl:value-of select="$OUTPUT"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<!--rennypv-a href="{$baseActionURL}?CMScreen=browse&amp;CMScreenParm=output"-->
                                                                <!--a title="{$PREVIEW}" href="{$baseDownloadURL}?CMCommand=previewNode&amp;previewPath={$highlightPath}&amp;edition={$currentEdition/@index}&amp;random={$random}&amp;CMScreen=browse&amp;CMScreenParm=output"  target="{$iframename}"-->
                                                                <a title="{$PREVIEW}" href="{$baseActionURL}?CMScreen=browse&amp;CMScreenParm=output">
                                                                <!--a title="{$PREVIEW}" href="{$baseActionURL}?CMCommand=previewNode&amp;previewPath={$highlightPath}&amp;edition={$currentEdition/@index}&amp;random={$random}&amp;CMScreen=browse&amp;CMScreenParm=output"-->

									<xsl:value-of select="$OUTPUT"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:if test="$hasReferencesSub or $hasRevisionsSub">
							|
						</xsl:if>
					</xsl:if>
					<xsl:if test="$hasReferencesSub">
						<xsl:choose>
							<xsl:when test="$browseSubScreen='references'">
								<span class="uportal-channel-strong">
								<xsl:value-of select="$REFERENCES"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<a href="{$baseActionURL}?CMScreen=browse&amp;CMScreenParm=references">
									<xsl:value-of select="$REFERENCES"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:if test="$hasRevisionsSub">
							|
						</xsl:if>
					</xsl:if>
					<xsl:if test="$hasRevisionsSub">
						<xsl:choose>
							<xsl:when test="$browseSubScreen='revisions'">
								<span class="uportal-channel-strong">
								<xsl:value-of select="$REVISIONS"/>
								</span>
							</xsl:when>
							<xsl:otherwise>
								<a href="{$baseActionURL}?CMScreen=browse&amp;CMScreenParm=revisions">
									<xsl:value-of select="$REVISIONS"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
					</span>
				</td>
				</tr>
				<tr><td class="uportal-background-dark"><img src="{$spacerIMG}" width="1" height="1"/></td></tr>
				<tr>
				<td width="*">
					<xsl:choose>
						<xsl:when test="$browseSubScreen='create'">
							<xsl:choose>
								<xsl:when test="$highlightFile">
									<xsl:call-template name="create">
										<xsl:with-param name="sourceNode" select="$highlightNode/parent::node"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="create"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
                        <xsl:when test="$browseSubScreen='copy'">
							<xsl:call-template name="copy-move"/>
						</xsl:when>
                        <xsl:when test="$browseSubScreen='admin'">
							<xsl:call-template name="admin"/>
						</xsl:when>
						<xsl:when test="$browseSubScreen='revisions'">
							<xsl:call-template name="revisions"/>
						</xsl:when>
						<xsl:when test="$browseSubScreen='references'">
							<xsl:call-template name="references"/>
						</xsl:when>
						<xsl:when test="$browseSubScreen='output'">
							<xsl:call-template name="pages"/> 
						</xsl:when>
					</xsl:choose>
				</td>
				</tr>
				<tr><td class="uportal-background-dark"><img src="{$spacerIMG}" width="1" height="1"/></td></tr>
				<xsl:call-template name="spacerow"/>
			</table>
		</xsl:if>
		<!-- end inset function box-->

		<table width="100%" border="0" cellpadding="0" cellspacing="0">

  		<tr>
  			<td colspan="4" nowrap="nowrap"  align="center">
  				<span class="uportal-channel-table-row-odd">
				<xsl:value-of select="concat($LAST_MODIFIED,' ',$highlightNode/@lastModified)"/>
				</span>
  			</td>
  		</tr>
  		<xsl:call-template name="spacerow"/>

		<!--<xsl:call-template name="move"/>-->
        <xsl:if test="not($highlightPath='/')">
            <tr><td colspan="4" class="uportal-channel-table-row-even" align="center">
            <xsl:value-of select="$highlightPath"/>
            </td></tr>
        </xsl:if>
	</table>
  </xsl:template>
  <!--
  <xsl:template name="move">
  	<form action="{$baseActionURL}">
			<input type="hidden" name="CMCommand" value="moveNode"/>
			<input type="hidden" name="fromPath" value="{$highlightPath}"/>
				<tr><td colspan="4" class="uportal-channel-table-row-even" align="center">
				<xsl:choose>
					<xsl:when test="($highlightNode/@canWrite='true') and ($highlightNode/@isRequired='false')">
						<input type="submit" value="{$MOVE_TO}" class="uportal-button"/>
                        <input type="hidden" name="fromPath" value="{$highlightPath}"/>
						<br/>
						<input class="uportal-input-text" name="toPath" type="text" size="50" maxlength="400" value="{$highlightPath}"/>
					</xsl:when>
					<xsl:when test="not($highlightPath='/')">
						<xsl:value-of select="$highlightPath"/>
					</xsl:when>
				</xsl:choose>
				</td>
			</tr>

		</form>
  </xsl:template>
  -->
     <xsl:template name="admin">
         <script type="text/javascript"  LANGUAGE='JavaScript1.2'>
            // <![CDATA[
                function compactAlert(url,path){
                    alertMsg=']]><xsl:value-of select="$highlightNode/warnings/compact"/><![CDATA[';
                    if(window.confirm(alertMsg)){
                         window.open(url,']]><xsl:value-of select="$publishWindow"/><![CDATA[').focus();
                    }
                }

            // ]]>
            </script>
         <span class="uportal-channel-text">
            <xsl:if test="$canBuildNode">
                   <a href="javascript:window.open('{$baseDownloadURL}?CMCommand=buildNode&amp;path={$highlightPath}&amp;force=true&amp;random={$random}','{$publishWindow}').focus();">
                       <xsl:value-of select="$FORCE_BUILD"/>
                   </a>
			       <br/>
            </xsl:if>
            <xsl:if test="$canPublishNode">
                   <a href="javascript:window.open('{$baseDownloadURL}?CMCommand=publishNode&amp;path={$highlightPath}&amp;force=true&amp;random={$random}','{$publishWindow}').focus();">
                       <xsl:value-of select="$FORCE_PUBLISH"/>
                   </a>
				   <br/>
            </xsl:if>
            <xsl:if test="string($highlightNode/@canDelete)='true'">
                <a href="javascript:compactAlert('{$baseDownloadURL}?CMCommand=compactNode&amp;path={$highlightPath}&amp;random={$random}','{$highlightPath}');">
                    <xsl:value-of select="$COMPACT"/>
                </a>
                <br/>
            </xsl:if>
             <xsl:if test="string($highlightNode/@canDelete)='true'">
                <a href="{$baseActionURL}?CMCommand=pruneNode&amp;dir={$highlightPath}">
                    <xsl:value-of select="$PRUNE_EMPTY"/>
                </a>
                <br/>
            </xsl:if>
             <a href="javascript:window.open('{$baseDownloadURL}?CMCommand=zipNode&amp;path={$highlightPath}&amp;random={$random}','{$publishWindow}').focus();">
                  <xsl:value-of select="$DOWNLOAD_ZIP"/>
               </a>
               <br/>
             <xsl:if test="string($highlightNode/@canWrite)='true'">
                <form ENCTYPE="multipart/form-data" method="POST" action="{$baseDownloadURL}" target="{$publishWindow}" onSubmit="javascript:window.open('','{$publishWindow}').focus()">
                  <input type="submit" value="{$UPLOAD_ZIP}" class="uportal-button"/>
                  <input type="hidden" name="path" value="{$highlightPath}"/>
                  <input type="hidden" name="CMCommand" value="copyZip"/>
                  <input type="file" name="fUploadZip"/>
                  <xsl:value-of select="$FORCE_COPY"/>
                  <input type="checkbox" name="force" value="true"/>
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="$DELETE_UNKNOWN"/>
                  <input type="checkbox" name="deleteUnknown" value="true"/>
                </form>
                 <br/>
              </xsl:if>
            <xsl:if test="$canLabelNode">
                <form action="{$baseActionURL}">
                    <input type="submit" value="{$CHANGE_DIR_LABEL}" class="uportal-button"/>
                        <input type="hidden" name="CMCommand" value="setLabel"/>
                    <input type="hidden" name="path" value="{$highlightPath}"/>
                    <input type="text" name="label" size="20" maxlength="100" value="{$highlightNode/@label}"/>
                </form>
                 <br/>
             </xsl:if>
        </span>
    </xsl:template>

    <xsl:template name="copy-move">
        <script type="text/javascript"  LANGUAGE='JavaScript1.2'>
            // <![CDATA[

		function processMoveCopy(form,promptStart,curValue,warning){
			if(form.toPath.value.indexOf("*") > 0){
                var promptPath = form.toPath.value.substring(0,form.toPath.value.indexOf("*"));
				var alertString =promptStart+" "+promptPath;
				if(warning.length > 0){
					alertString = alertString + "\n"+warning;
				}
				var replaceString = prompt(alertString,curValue);
				if(replaceString){
					//if (replaceString.indexOf(' ') > 0){
					//	return processMoveCopy(form,promptStart,replaceString,']]><xsl:value-of select="$NO_SPACES"/><![CDATA[');
					//}
					if (replaceString.indexOf('.') > 0){
						return processMoveCopy(form,promptStart,replaceString,']]><xsl:value-of select="$NO_EXTENSION"/><![CDATA[');
					}

					form.newName.value=replaceString;

					return true;
				}
				return false;
			}
			return true;

		}


            // ]]>
            </script>


         <xsl:variable name="moveOptionCount" select="count($highlightNode/moveOptions/moveOption)"/>
        <xsl:if test="$moveOptionCount &gt; 0">
            <xsl:variable name="hasLabel" select="string-length($highlightNode/moveOptions/moveOption/@label) &gt; 0"/>

            <form name="moveForm" id="moveForm" action="{$baseActionURL}" onSubmit="return processMoveCopy(this,'{$highlightNode/moveOptions/@promptString}','{$highlightNode/moveOptions/@currentName}','');">
                <input type="hidden" name="newName" value=""/>
                <input type="hidden" name="fromPath" value="{$highlightPath}"/>
                <input type="hidden" name="CMCommand" value="moveNode"/>
                <!--<a onclick="alert(document.copyForm.copyPath.value)">value check</a>-->
            <table>
            <tr><td class="uportal-channel-table-row-even">
                <input type="submit">
                    <xsl:choose>
                        <xsl:when test="$hasLabel or ($moveOptionCount &gt; 1)">
                            <xsl:attribute name="value">
                                <xsl:value-of select="$MOVE_TO"/>
                            </xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                             <xsl:attribute name="value">
                                <xsl:value-of select="$MOVE"/>
                            </xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>
                </input>
            <xsl:choose>
				<xsl:when test="$moveOptionCount=1">
					<input type="hidden" name="toPath" value="{$highlightNode/moveOptions/moveOption/@path}"/>
					<xsl:if test="$hasLabel">
                        <strong><xsl:value-of select="$highlightNode/moveOptions/moveOption/@label"/></strong>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<!--<xsl:text> to </xsl:text>-->
						<select name="toPath" class="uportal-button">
                            <xsl:for-each select="$highlightNode/moveOptions/moveOption">
								<option value="{@path}">
                                    <xsl:if test="@current='true'">
                                        <xsl:attribute name="selected"><xsl:text>selected</xsl:text></xsl:attribute>
                                    </xsl:if>
									<xsl:choose>
										<xsl:when test="string-length(@label) &gt; 0">
											<xsl:value-of select="@label"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:attribute name="selected">selected</xsl:attribute>
											<xsl:value-of select="$OPEN_MOVE_PROMPT"/>
										</xsl:otherwise>
									</xsl:choose>
								</option>
							</xsl:for-each>
						</select>
				</xsl:otherwise>
			</xsl:choose>
            </td></tr>
            </table>
            </form>
        </xsl:if>

        <xsl:variable name="copyOptionCount" select="count($highlightNode/copyOptions/copyOption)"/>
        <xsl:if test="$copyOptionCount &gt; 0">
            <xsl:variable name="hasLabel" select="string-length($highlightNode/copyOptions/copyOption/@label) &gt; 0"/>
            <form name="copyForm" id="copyForm" action="{$baseActionURL}" onSubmit="return processMoveCopy(this,'{$highlightNode/copyOptions/@promptString}','{$highlightNode/copyOptions/@currentName}','');">
                <input type="hidden" name="newName" value=""/>
                <input type="hidden" name="fromPath" value="{$highlightPath}"/>
                <input type="hidden" name="CMCommand" value="copyNode"/>
                <!--<a onclick="alert(document.copyForm.copyPath.value)">value check</a>-->
            <table>
            <tr><td class="uportal-channel-table-row-even">
                <input type="submit">
                    <xsl:choose>
                        <xsl:when test="$hasLabel or ($copyOptionCount &gt; 1)">
                            <xsl:attribute name="value">
                                <xsl:value-of select="$COPY_TO"/>
                            </xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                             <xsl:attribute name="value">
                                <xsl:value-of select="$COPY"/>
                            </xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>
                </input>
            <xsl:choose>
				<xsl:when test="$copyOptionCount=1">
					<input type="hidden" name="toPath" value="{$highlightNode/copyOptions/copyOption/@path}"/>


					<xsl:if test="$hasLabel">
						<!--<xsl:text> to </xsl:text>    -->
							<strong><xsl:value-of select="$highlightNode/copyOptions/copyOption/@label"/></strong>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<!--<xsl:text> to </xsl:text>  -->
						<select name="toPath" class="uportal-button">
                            <xsl:for-each select="$highlightNode/copyOptions/copyOption">
								<option value="{@path}">
                                    <xsl:if test="@current='true'">
                                        <xsl:attribute name="selected"><xsl:text>selected</xsl:text></xsl:attribute>
                                    </xsl:if>
									<xsl:choose>
										<xsl:when test="string-length(@label) &gt; 0">
											<xsl:value-of select="@label"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:attribute name="selected">selected</xsl:attribute>
											<xsl:value-of select="$OPEN_COPY_PROMPT"/>
										</xsl:otherwise>
									</xsl:choose>
								</option>
							</xsl:for-each>
						</select>
				</xsl:otherwise>
			</xsl:choose>
            </td></tr>
            </table>
            </form>
        </xsl:if>
        <!--
        move
        <xsl:for-each select="$highlightNode/moveOptions/moveOption">
            <xsl:value-of select="concat(@path,' - ',@label)"/>   <br/>
        </xsl:for-each>
        copy
        <xsl:for-each select="$highlightNode/copyOptions/copyOption">
            <xsl:value-of select="concat(@path,' - ',@label)"/>   <br/>
        </xsl:for-each>   -->
    </xsl:template>

  <xsl:template name="create">
  	<xsl:param name="sourceNode" select="$highlightNode"/>
  	<script type="text/javascript"  LANGUAGE='JavaScript1.2'>
	// <![CDATA[
		function replaceWildCard(form,text,label,namedFile,value,warning){
			if(form.CMFilePath.value.indexOf('*') > 0){
				var alertString = text;
				if(warning.length > 0){
					alertString = alertString + "\n"+warning;
				}
				var replaceString = prompt(alertString,value);
				if(replaceString){
					//if (replaceString.indexOf(' ') > 0)
                                        //{
					//	return replaceWildCard(form,text,label,namedFile,replaceString,']]><xsl:value-of select="$NO_SPACES"/><![CDATA[');
					//}
					if (replaceString.indexOf('.') > 0){
						return replaceWildCard(form,text,label,namedFile,replaceString,']]><xsl:value-of select="$NO_EXTENSION"/><![CDATA[');
					}

					if (namedFile=='true'){
						form.CMNewDirectory.value=replaceString;
					}
					else{
						form.CMFileName.value=replaceString;
					}
					return true;
				}
				return false;
			}
			return true;

		}



	// ]]>
	</script>

  	<table border="0" cellpadding="0" cellspacing="1">
  	<tr>
		<td width="10"><img src="{$spacerIMG}" width="10" height="5"/></td>
		<td width="*"><img src="{$spacerIMG}" width="10" height="5"/></td>
		<td width="10"><img src="{$spacerIMG}" width="10" height="5"/></td>
	</tr>
  	<xsl:for-each select="$sourceNode/newFileOptions[newFileOption]">
  		<!-- onsubmit="return replaceWildCard(this,'{@label}','{@namedFile}','','');" -->
  		<form action="{$baseActionURL}" onsubmit="return replaceWildCard(this,'{@prompt}','{@label}','{@namedFile}','','');">
			<input type="hidden" name="CMCommand" value="newFile"/>

			<tr><td class="uportal-channel-strong" colspan="2">
				<xsl:value-of select="@label"/>
			</td>
			<td>
				<input type="submit" value="{$NEW}" class="uportal-button"/>
			</td>
			</tr>
			<!-- determine file extension -->
			<xsl:choose>
				<xsl:when test="(count(content)=1) and (key('contentType',content/@type)/file-extension[@preferred='true'])">
					<input type="hidden" name="CMFileExtension" value="{key('contentType',content/@type)/file-extension[@preferred='true']/@value}"/>
				</xsl:when>
				<xsl:when test="count(content)=1">
					<input type="hidden" name="CMFileExtension" value="{key('contentType',content/@type)/file-extension/@value}"/>
				</xsl:when>
				<xsl:otherwise>
					<tr><td/>
					<td class="uportal-channel-table-row-even">
						<xsl:value-of select="$OF_TYPE"/>
						<select name="CMFileExtension" class="uportal-button">
							<xsl:for-each select="content">
								<option>
									<xsl:choose>
										<xsl:when test="(key('contentType',@type)/file-extension[@preferred='true'])">
											<xsl:attribute name="value">
												<xsl:value-of select="key('contentType',@type)/file-extension[@preferred='true']/@value"/>
											</xsl:attribute>
										</xsl:when>
										<xsl:otherwise>
											<xsl:attribute name="value">
												<xsl:value-of select="key('contentType',@type)/file-extension/@value"/>
											</xsl:attribute>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:value-of select="key('contentType',@type)/@label"/>
								</option>
							</xsl:for-each>
						</select>
					</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
                         
			<xsl:choose>
				<xsl:when test="count(newFileOption)=1">
					<input type="hidden" name="CMFilePath" value="{newFileOption/@path}"/>
					<xsl:if test="string-length(newFileOption/@label) &gt; 0">
						<tr><td/><td class="uportal-channel-table-row-even"><xsl:value-of select="$UNDER"/>
							<xsl:text> </xsl:text>
							<strong><xsl:value-of select="newFileOption/@label"/></strong>
							</td>
						</tr>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<tr><td/>
					<td colspan="2" class="uportal-channel-table-row-even"><xsl:value-of select="$UNDER"/>
						<xsl:text> </xsl:text>
						<select name="CMFilePath" class="uportal-button">
							<xsl:for-each select="newFileOption">
								<option value="{@path}">
									<xsl:choose>
										<xsl:when test="string-length(@label) &gt; 0">
											<xsl:value-of select="@label"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:attribute name="selected">selected</xsl:attribute>
											<xsl:value-of select="$OPEN_CREATE_PROMPT"/>
										</xsl:otherwise>
									</xsl:choose>
								</option>
							</xsl:for-each>
						</select>
					</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
                        <!--tr>
                            <font size='2'><td><a href='{$baseActionURL}?key=/projects/BioHelp.xml&amp;CMCommand=newFile&amp;create=newFolder&amp;CMFilePath=/downloads/*/index.html&amp;CMNewDirectory=test&amp;uP_root=root'>Create Folder</a></td></font>
                        </tr-->
			<xsl:if test="contains(newFileOption/@path,'*')">
					<input type="hidden">
						<xsl:choose>
							<xsl:when test="@namedFile='true'">
								<xsl:attribute name="name">CMNewDirectory</xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="name">CMFileName</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
					</input>
			</xsl:if>
  		</form>
  		<xsl:if test="not(position() = last())">
					<xsl:call-template name="hrow"/>
				</xsl:if>
  	</xsl:for-each>
  	<xsl:call-template name="spacerow"/>
  	</table>
  </xsl:template>

  <xsl:template name="revisions">
  		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<xsl:for-each select="$highlightFile/edition">
				<xsl:sort select="@index" order="descending" data-type="number"/>
				<xsl:call-template name="edition">
					<xsl:with-param name="key" select="@index"/>
					<xsl:with-param name="path" select="$highlightPath"/>
					<xsl:with-param name="node" select="$highlightNode"/>
					<xsl:with-param name="name" select="@date"/>
					<xsl:with-param name="comment" select="normalize-space(rdf:RDF/cfs:File/cms:comment)"/>
					<xsl:with-param name="editor" select="rdf:RDF/cfs:File/cms:editor"/>
					<xsl:with-param name="selected" select="string($highlightFile/@currentEdition)=string(@index)"/>
				</xsl:call-template>
				<xsl:if test="not(position() = last())">
					<xsl:call-template name="hrow"/>
				</xsl:if>
			</xsl:for-each>
		</table>
  </xsl:template>

	<xsl:template name="references">
		<xsl:call-template name="hits">
			<xsl:with-param name="sort">type</xsl:with-param>
			<xsl:with-param name="hits" select="$highlightNode/references/hit"/>
		</xsl:call-template>
	</xsl:template>

  <xsl:template name="pages">
  	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  	  <xsl:choose>
		<xsl:when test="$highlightFile">
			<xsl:if test="count($highlightFile/file-output) &gt; 1">
				<tr>
					<td width="20"><img src="{$spacerIMG}" height="5" width="20"/></td>
					<td width="*"><img src="{$spacerIMG}" height="5" width="10"/></td>
					<td width="*"><img src="{$spacerIMG}" height="5" width="10"/></td>
					<td width="10"><img src="{$spacerIMG}" height="5" width="10"/></td>

				</tr>
				<xsl:for-each select="$highlightFile/file-output">
                                
					<xsl:sort select="@label" order="ascending"/>
					<xsl:variable name="output" select="key('output',@output-id)"/>
					<xsl:variable name="ctype" select="/content-manager/content-types/content-type[(@type=$output/@content-type) or (equivalent/@type=$output/@content-type)]"/>
<!--
    @author rennypv
    This bit was in to be able to view the page when the output page is clicked
    Uses iframe to display the html
    The xml file as saved by the contentmanager has 3 files to it...a html, a metadata file and the images file...and tha tis the 
    reason why the publish url is checked for the string .htm
-->                                        
                                       <xsl:if test="contains(string(@publish-url),'.htm') or contains(string(@publish-url),'.pdf')">
					<tr>
						<td colspan="4" align="right" nowrap="nowrap">
							<span class="uportal-channel-table-row-even">
                                                           <!--IFRAME src='{@publish-url}' width='100%' height='900' scrolling='auto' name='{$iframename}' id='{$iframename}'-->
                                                           <IFRAME  src='{$baseDownloadURL}?CMCommand=previewNode&amp;previewPath={$highlightPath}&amp;edition={$currentEdition/@index}&amp;random={$random}' width='100%' height='900' scrolling='auto' name='{$iframename}' id='{$iframename}'>
                                                            </IFRAME>
							</span>
						</td>
					</tr>
                                     </xsl:if>
<!--End of edit by rennypv-->                                     
<!--
<xsl:otherwise>
                                            <tr>
    
						<td><img border="0" height="16" width="16" src="{key('icon',substring-after(@output-path,'.'))}" space="0" hspace="0"/></td>
						<td nowrap="nowrap">
							<xsl:if test="@highlighted='true'">
								<xsl:attribute name="class">uportal-background-highlight</xsl:attribute>
							</xsl:if>
							<span class="uportal-channel-table-row-even">

							<xsl:value-of select="@label"/>
							</span>
						</td>

						<td colspan="2" align="right" nowrap="nowrap">
							<span class="uportal-channel-table-row-even">

							<a href="javascript:window.open('{@build-url}','{$publishWindow}').focus();"><xsl:value-of select="$VIEW_BUILT"/></a>
							|
							<a href="javascript:window.open('{@publish-url}','{$publishWindow}').focus();"><xsl:value-of select="$VIEW_PUBLISHED"/></a>
							|
							<a href="{$baseActionURL}?CMCommand=toggleDependencies&amp;path={$highlightPath}&amp;outputPath={@output-path}">
								<xsl:choose>
									<xsl:when test="hit">
										<xsl:value-of select="$HIDE_REQUIRED"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$SHOW_REQUIRED"/>
									</xsl:otherwise>
								</xsl:choose>
								</a>
                                                     

							</span>
						</td>
                                       
					</tr>
                                     </xsl:otherwise>
-->                                     
					<xsl:if test="$ctype and hit">
						<tr><td/>
							<td colspan="3">
								<xsl:call-template name="hits">
									<xsl:with-param name="hits" select="hit"/>
									<xsl:with-param name="sort">type</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</xsl:if>
					<xsl:if test="not(position() = last())">
						<xsl:call-template name="hrow"/>
					</xsl:if>
				</xsl:for-each>
			</xsl:if>
                        
                        
 <!--
 @author rennypv
 This bit is also required so that if the files that are saved are plain html then it comes to this bit
 -->                       
                        
                        
                        <xsl:if test="count($highlightFile/file-output) &gt; 0 and count($highlightFile/file-output) &lt; 2">
				<tr>
					<td width="20"><img src="{$spacerIMG}" height="5" width="20"/></td>
					<td width="*"><img src="{$spacerIMG}" height="5" width="10"/></td>
					<td width="*"><img src="{$spacerIMG}" height="5" width="10"/></td>
					<td width="10"><img src="{$spacerIMG}" height="5" width="10"/></td>

				</tr>
				<xsl:for-each select="$highlightFile/file-output">
                                
					<xsl:sort select="@label" order="ascending"/>
					<xsl:variable name="output" select="key('output',@output-id)"/>
					<xsl:variable name="ctype" select="/content-manager/content-types/content-type[(@type=$output/@content-type) or (equivalent/@type=$output/@content-type)]"/>
                             <xsl:if test="contains(string(@publish-url),'.htm') or contains(string(@publish-url),'.pdf')">
					<tr>
						<td colspan="4" align="right" nowrap="nowrap">
							<span class="uportal-channel-table-row-even">
                                                           <!--IFRAME src='{@publish-url}' width='100%' height='900' scrolling='auto' name='{$iframename}'-->
                                                           <IFRAME src='{$baseDownloadURL}?CMCommand=previewNode&amp;previewPath={$highlightPath}&amp;edition={$currentEdition/@index}&amp;random={$random}' width='100%' height='900' scrolling='auto' name='{$iframename}' id='{$iframename}'>
                                                            </IFRAME>
							</span>
						</td>
					</tr>
                                     </xsl:if>
<!--
    This bit it to tell it what to do if the file involved is not a html....in which case it instructs you to click on the eye 
    to see the preview
-->                                     
                                     <xsl:if test="not(contains(string(@publish-url),'.htm') or contains(string(@publish-url),'.pdf'))">
                                            <tr>
    
<!--						<td><img border="0" height="16" width="16" src="{key('icon',substring-after(@output-path,'.'))}" space="0" hspace="0"/></td>
						<td nowrap="nowrap">
							<xsl:if test="@highlighted='true'">
								<xsl:attribute name="class">uportal-background-highlight</xsl:attribute>
							</xsl:if>
							<span class="uportal-channel-table-row-even">

							<xsl:value-of select="@label"/>
							</span>
						</td>

						<td colspan="2" align="right" nowrap="nowrap">
							<span class="uportal-channel-table-row-even">

							<a href="javascript:window.open('{@build-url}','{$publishWindow}').focus();"><xsl:value-of select="$VIEW_BUILT"/></a>
							|
							<a href="javascript:window.open('{@publish-url}','{$publishWindow}').focus();"><xsl:value-of select="$VIEW_PUBLISHED"/></a>
							|
							<a href="{$baseActionURL}?CMCommand=toggleDependencies&amp;path={$highlightPath}&amp;outputPath={@output-path}">
								<xsl:choose>
									<xsl:when test="hit">
										<xsl:value-of select="$HIDE_REQUIRED"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$SHOW_REQUIRED"/>
									</xsl:otherwise>
								</xsl:choose>
								</a>
                                                     

							</span>
						</td> -->
                                                <td>
                                                Click on the <img width="16" height="16" src="{$mediaBase}/eye.gif" border="0" vspace="0" hspace="1"/> to view the file
                                                </td>
                                       
					</tr>
                                     </xsl:if>
<!--End of the edit by rennypv-->                                     
					<xsl:if test="$ctype and hit">
						<tr><td/>
							<td colspan="3">
								<xsl:call-template name="hits">
									<xsl:with-param name="hits" select="hit"/>
									<xsl:with-param name="sort">type</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</xsl:if>
					<xsl:if test="not(position() = last())">
						<xsl:call-template name="hrow"/>
					</xsl:if>
				</xsl:for-each>
			</xsl:if>
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
			</xsl:when>
<!--                        <xsl:otherwise>
				<xsl:call-template name="spacerow"/>
				<tr><td class="uportal-channel-table-row-even" align="center">
				<xsl:if test="/content-manager/cms-project/publish">
					<IFRAME src='{$highlightNode/@publish-url}' width='100%' height='900' scrolling='auto' ></IFRAME>
				</xsl:if>
				</td></tr>
			</xsl:otherwise> -->
			<xsl:otherwise>
				<xsl:call-template name="spacerow"/>
				<tr><td class="uportal-channel-table-row-even" align="center">
				<xsl:if test="/content-manager/cms-project/build">

					<a href="javascript:window.open('{$highlightNode/@build-url}','{$publishWindow}').focus();"><xsl:value-of select="$VIEW_BUILT"/></a> |

				</xsl:if>

				<xsl:if test="/content-manager/cms-project/publish">
					<a href="javascript:window.open('{$highlightNode/@publish-url}','{$publishWindow}').focus();"><xsl:value-of select="$VIEW_PUBLISHED"/></a>
				</xsl:if>
				</td></tr>
			</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="spacerow"/>
		</table>
  </xsl:template>

  <xsl:template name="titleRow">
  	<xsl:param name="subtitle" select="false()"/>
  	<xsl:param name="close" select="true()"/>
  	<tr>
  		<td width="99%" colspan="5" class="uportal-background-dark">
			<span class="uportal-channel-title-reversed">
				<xsl:value-of select="$projectTitle"/>
				<xsl:if test="$subtitle">
				- <xsl:value-of select="$subtitle"/>
				</xsl:if>
			</span>
		</td>
		<xsl:if test="$close">
			<td width="1%" class="uportal-background-dark" align="right">
				<a href="{$baseActionURL}?CMCommand=closeProject&amp;uP_root=root">
				<image border="0" alt="{$CLOSE_PROJECT}" title="{$CLOSE_PROJECT}" src="media/org/jasig/portal/channels/groupsmanager/close.gif" width="16" height="16"/>
				</a>
			</td>
		</xsl:if>

    </tr>

  </xsl:template>

  <xsl:template name="spacerow">
  	<tr><td><img src="$spacerIMG" height="5" width="1"/></td></tr>
  </xsl:template>

  <xsl:template name="hrow">
  	<xsl:param name="width" select="'4'"/>
  	<tr><td><img src="$spacerIMG" height="2" width="1"/></td></tr>
  	<tr>
  		<td colspan="{$width}" align="center" height="1" class="uportal-background-shadow">
  		<img src="$spacerIMG" height="1" width="1"/>
  	</td>
  	</tr>
  	<tr><td><img src="$spacerIMG" height="2" width="1"/></td></tr>
  </xsl:template>

  <xsl:template name="edition">
  	<xsl:param name="key"/>
  	<xsl:param name="name"/>
  	<xsl:param name="path"/>
  	<xsl:param name="node"/>
  	<xsl:param name="selected" select="false()"/>
  	<xsl:param name="comment" select="false()"/>
  	<xsl:param name="editor" select="false()"/>
  	<xsl:param name="showRadio" select="true()"/>
  	<xsl:param name="allowOpen" select="true()"/>

  	<tr>
		  <td>
		  <xsl:if test="$selected">
		  		<xsl:attribute name="class">uportal-background-highlight</xsl:attribute>
		  </xsl:if>
		  <xsl:if test="$showRadio and $node/@canWrite='true'">
			  <input type="radio" name="edition" value="{$key}" onClick="javascript:window.location.href='{$baseActionURL}?CMCommand=setCurrent&amp;path={$path}&amp;edition={$key}';">
				<xsl:if test="$selected">
					<xsl:attribute name="checked"/>
				</xsl:if>
			  </input>
		  </xsl:if>
		  </td>
		  <td nowrap="nowrap">
		  	<xsl:if test="$selected">
		  		<xsl:attribute name="class">uportal-background-highlight</xsl:attribute>
		  	</xsl:if>
		  	<span class="uportal-channel-table-row-even">
			<xsl:value-of select="$name"/>
			</span>
		  </td>
		  <td width="100%">

		  </td>
		  <td nowrap="nowrap" align="right">

		  	<span class="uportal-channel-table-row-even">
		  	<xsl:if test="$node/@canRead='true'">

		  		<xsl:if test="($node/@canWrite='true') and ($node/@hasEditorFactory='true')">
		  			<a title="{$EDIT_REVISION}" href="{$baseActionURL}?CMCommand=openFile&amp;path={$path}&amp;edition={$key}">
					<img width="16" height="16" src="{$mediaBase}/edit.gif" border="0" vspace="0" hspace="1"/>
					</a>
				</xsl:if>
				<xsl:if test="($node/@hasOutput='true') and (/content-manager/cms-project/preview)">
					<a title="{$PREVIEW_REVISION}" href="javascript:window.open('{$baseDownloadURL}?CMCommand=previewNode&amp;previewPath={$path}&amp;edition={$key}&amp;random={$random}','{$publishWindow}').focus();">
					<img width="16" height="16" src="{$mediaBase}/eye.gif" border="0" vspace="0" hspace="1"/>
					</a>
				</xsl:if>
			</xsl:if>
			<xsl:if test="(string($node/@canDelete)='true') and ((not(string($node/@isRequired)='true')) or (count($node/file-def/edition) &gt; 1) or ($key='temp'))">
				<a title="{$DELETE_REVISION}" href="javascript:cmsConfirmRemoveEdition('{$baseActionURL}?CMCommand=removeEdition&amp;path={$path}&amp;edition={$key}');">
  					<img width="16" height="16" src="{$mediaBase}/delete.gif" border="0" vspace="0" hspace="1"/>
  				</a>
  			</xsl:if>
			</span>
		</td>
		<td width="100%">
		</td>
	  </tr>
	<xsl:if test="($comment and (string-length($comment)>0)) or $editor">
	<tr>
		<td></td><td colspan="3" class="uportal-channel-table-row-odd">
		<xsl:if test="$comment and (string-length($comment)>0)">"<xsl:value-of select="$comment"/>"</xsl:if>
		<xsl:if test="$comment and $editor and (string-length($comment)>0)"> - </xsl:if>
		<xsl:if test="$editor"><em><xsl:value-of select="$editor"/></em></xsl:if>
	</td>
	</tr>
	</xsl:if>

  </xsl:template>

  <xsl:template match="node">
  	<xsl:param name="depth" select="number(0)"/>
  	<xsl:variable name="path" select="@path"/>
  	<xsl:variable name="parentPath" select="parent::node/@path"/>
        <a name="{$path}"/>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<!--<xsl:if test="$depth &gt; 0">-->

			<tr>


				<td class="uportal-channel-table-row-even" valign="top">

					<xsl:choose>
						<xsl:when test="(@isFile='false') and ($depth = 0)">
							<a title="{$HIGHLIGHT_SITE_ROOT}" href="{$baseActionURL}?CMCommand=highlightNode&amp;path={@path}&amp;CMScreen=browse">
							<img border="0" height="16" width="16" src="media/edu/columbia/ais/portal/expanded.gif"  align="bottom" vspace="1" hspace="0" alt="{$HIGHLIGHT_SITE_ROOT}" title="{$HIGHLIGHT_SITE_ROOT}"/>
							</a>
						</xsl:when>
						<xsl:when test="(@isFile='false') and (@expanded='true')">
							<a title="{$COLLAPSE_DIRECTORY}" href="{$baseActionURL}?CMCommand=toggleNode&amp;path={@path}&amp;CMScreen={$screen}&amp;CMScreenParm={$screenParm}#{$path}">
							<img border="0" height="16" width="16" src="media/edu/columbia/ais/portal/expanded.gif"  align="bottom" vspace="1" hspace="0" alt="{$COLLAPSE_DIRECTORY}" title="{$COLLAPSE_DIRECTORY}"/>
							</a>
						</xsl:when>
						<xsl:when test="(@isFile='false')">
							<a title="{$EXPAND_DIRECTORY}" href="{$baseActionURL}?CMCommand=toggleNode&amp;path={@path}&amp;CMScreen={$screen}&amp;CMScreenParm={$screenParm}#{$path}">
							<img border="0" height="16" width="16" src="media/edu/columbia/ais/portal/collapsed.gif"  align="bottom" vspace="1" hspace="0" alt="{$EXPAND_DIRECTORY}" title="{$EXPAND_DIRECTORY}"/>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<img border="0" height="16" width="16" src="{key('icon',substring-after($path,'.'))}"  align="bottom" vspace="1" hspace="0"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>

			<td width="100%" nowrap="nowrap">
				<xsl:if test="$highlightPath = @path">
					<xsl:attribute name="class">uportal-background-highlight</xsl:attribute>
				</xsl:if>
				<span class="uportal-channel-table-row-even">
                                <a href="{$baseActionURL}?CMCommand=highlightNode&amp;path={@path}&amp;CMScreen=browse">
                                        <xsl:value-of select="@name"/>
                                </a>
                    </span>
                <span class="uportal-channel-table-row-odd">
                            <xsl:if test="not(starts-with(@name,@label))">
                                <xsl:text> (</xsl:text>
                                <xsl:value-of select="@label"/>
                                <xsl:text>)</xsl:text>
                            </xsl:if>
				    </span>
			</td>
			<td>
				<xsl:if test="$highlightPath = @path">
					<xsl:attribute name="class">uportal-background-highlight</xsl:attribute>
				</xsl:if>
				<img src="{$spacerIMG}" width="10" height="3"/>
			</td>
			</tr>
			<!--</xsl:if>-->
			<xsl:if test="@expanded='true'">
				<tr>
				<td background="media/edu/columbia/ais/portal/dot.gif">

				</td>
				<td colspan="2">
				<xsl:apply-templates select="node[@canRead='true']">
                                  <xsl:sort select="@isFile" order="descending" data-type="text"/>
                                  <xsl:sort select="@name" order="ascending" data-type="text"/>
					<xsl:with-param name="depth" select="number($depth+1)"/>
				</xsl:apply-templates>
				</td>
				</tr>
			</xsl:if>
		</table>


  </xsl:template>

  <xsl:template name="search">


		<table border="0">

			 <tr><td colspan="2">
			 	<span class="uportal-channel-strong">
			 	<xsl:value-of select="$SEARCH_FOR_FILE"/>
			 	</span>
			 	<span class="uportal-channel-text">
			 	<ul>

			 		<li><form action="{$baseActionURL}" method="POST">
			 			 <input type="hidden" name="CMCommand" value="search"/>
			 			<xsl:value-of select="$WHOSE"/>
			 			<xsl:text> </xsl:text>
						<select name="searchField" class="uportal-button">
							<option value="all">
								<xsl:if test="/content-manager/search-results/@field='all'">
									<xsl:attribute name="selected"><xsl:text>selected</xsl:text></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="$ANYTHING"/>
							</option>
							<option value="http://www.ais.columbia.edu/sws/xmlns/cufs#data">
								<xsl:if test="/content-manager/search-results/@field='http://www.ais.columbia.edu/sws/xmlns/cufs#data'">
									<xsl:attribute name="selected"><xsl:text>selected</xsl:text></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="$CONTENTS"/>
							</option>
							<option value="http://purl.org/dc/elements/1.0/keywords">
								<xsl:if test="/content-manager/search-results/@field='http://purl.org/dc/elements/1.0/keywords'">
									<xsl:attribute name="selected"><xsl:text>selected</xsl:text></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="$KEYWORDS"/>
							</option>
							<option value="http://purl.org/dc/elements/1.0/description">
								<xsl:if test="/content-manager/search-results/@field='http://purl.org/dc/elements/1.0/description'">
									<xsl:attribute name="selected"><xsl:text>selected</xsl:text></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="$DESCRIPTION"/>
							</option>
							<option value="http://purl.org/dc/elements/1.0/title">
								<xsl:if test="/content-manager/search-results/@field='http://purl.org/dc/elements/1.0/title'">
									<xsl:attribute name="selected"><xsl:text>selected</xsl:text></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="$TITLE"/>
							</option>
							<option value="http://www.ais.columbia.edu/sws/xmlns/cucms#editor">
								<xsl:if test="/content-manager/search-results/@field='http://www.ais.columbia.edu/sws/xmlns/cucms#editor'">
									<xsl:attribute name="selected"><xsl:text>selected</xsl:text></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="$AUTHOR"/>
							</option>
							<option value="http://www.ais.columbia.edu/sws/xmlns/cufs#name">
								<xsl:if test="/content-manager/search-results/@field='http://www.ais.columbia.edu/sws/xmlns/cufs#name'">
									<xsl:attribute name="selected"><xsl:text>selected</xsl:text></xsl:attribute>
								</xsl:if>
								<xsl:value-of select="$FILE_NAME"/>
							</option>
						</select>
						<xsl:text> </xsl:text>
						<xsl:value-of select="$MATCHES_QUERY"/>
						<xsl:text> </xsl:text>
						<input type="text" name="searchTerm" value="{/content-manager/search-results/@term}" class="uportal-input-text"/>
						<xsl:text> </xsl:text>
						<input class="uportal-button" type="submit" value="{$SEARCH}"/>
						</form>
					</li>
					<li><form action="{$baseActionURL}" method="POST">
						 <input type="hidden" name="CMCommand" value="find"/>
						 <input type="hidden" name="CMScreen" value="browse"/>
						 <xsl:value-of select="$BY_URL"/>
						 <xsl:text> </xsl:text>
						  <input size="50" maxlength="500" type="text" name="url" class="uportal-input-text"/>
						 <xsl:text> </xsl:text>
						 <input class="uportal-button" type="submit" value="{$SEARCH}"/>
						</form>
					</li>


			 	</ul>
			 	</span>
			 </td></tr>

  			<xsl:if test="/content-manager/search-results">
  				<tr><td>
  					<span class="uportal-channel-text">
  					<xsl:value-of select="count(/content-manager/search-results/hit)"/>
  					<xsl:text> </xsl:text>
  					<xsl:value-of select="$HITS_FOR"/>
  					<xsl:text> </xsl:text>
  					</span>
  					<span class="uportal-channel-strong">
  					<xsl:value-of select="/content-manager/search-results/@term"/>
  					</span>
  				</td>
  				</tr>
  				<tr>
				<td colspan="2">
					<xsl:call-template name="hits">
  						<xsl:with-param name="hits" select="/content-manager/search-results/hit"/>
  					</xsl:call-template>
				</td>

				</tr>
  			</xsl:if>

		</table>

  </xsl:template>

  <xsl:template name="hits">
  	<xsl:param name="hits"/>
  	<xsl:param name="sort" select="false()"/>
  	<table border="0" cellpadding="0" cellspacing="0">
  		<tr>
  			<td width="20"><img src="{$spacerIMG}" height="1" width="10"/></td>
			<td width="*"><img src="{$spacerIMG}" height="1" width="10"/></td>
			<td width="52"><img src="{$spacerIMG}" height="1" width="40"/></td>
		</tr>
  		<tr>
  			<xsl:if test="not($sort)">
				<td align="center" colspan="3">
					<xsl:choose>
						<xsl:when test="$sortOrder='type'">
							<span class="uportal-channel-table-row-even">
							<xsl:value-of select="$SORT_BY"/>
							<xsl:text> </xsl:text>
							<a href="{$baseActionURL}?CMSortOrder=relevance"><xsl:value-of select="$RELEVANCE"/></a>
							|
							<strong><xsl:value-of select="$TYPE"/></strong>
							</span>
						</xsl:when>
						<xsl:otherwise>
							<span class="uportal-channel-table-row-even">
							<xsl:value-of select="$SORT_BY"/>
							<xsl:text> </xsl:text>
							<strong><xsl:value-of select="$RELEVANCE"/></strong>
							|
							<a href="{$baseActionURL}?CMSortOrder=type"><xsl:value-of select="$TYPE"/></a>
							</span>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>

			<xsl:choose>
				<xsl:when test="$sort='type' or (not($sort) and $sortOrder='type')">


					<xsl:for-each select="/content-manager/cms-project/child::node()[(name()='xml-doctype' or name()='resource-directory')]">
						<xsl:sort select="@label" order="ascending"/>
						<xsl:variable name="tid" select="@uid"/>
						<xsl:if test="$hits[@typeID=$tid]">
							<tr><td colspan="3" class="uportal-channel-strong"><xsl:value-of select="/content-manager/cms-project/child::node()[@uid=$tid]/@label"/></td></tr>
							<xsl:for-each select="$hits[@typeID=$tid]">
								<xsl:sort select="@score" order="descending" data-type="number"/>
								<xsl:sort select="@path" order="ascending"/>
								<xsl:sort select="@title" order="ascending"/>
								<xsl:call-template name="hrow">
									<xsl:with-param name="width" select="'3'"/>
								</xsl:call-template>

									<xsl:call-template name="hit">
										<xsl:with-param name="elem" select="."/>
									</xsl:call-template>
							</xsl:for-each>
							<xsl:call-template name="spacerow"/>
						</xsl:if>
					</xsl:for-each>


					<xsl:for-each select="/content-manager/content-types/content-type">
						<xsl:variable name="this" select="."/>
						<xsl:if test="$hits[@typeID='']/@extension=file-extension/@value">
							<tr><td colspan="3" class="uportal-channel-strong"><xsl:value-of select="@label"/></td></tr>
							<xsl:for-each select="$hits[@typeID='' and @extension=$this/file-extension/@value]">
								<xsl:sort select="@score" order="descending" data-type="number"/>
								<xsl:sort select="@path" order="ascending"/>
								<xsl:sort select="@title" order="ascending"/>
								<xsl:call-template name="hrow">
									<xsl:with-param name="width" select="'3'"/>
								</xsl:call-template>

									<xsl:call-template name="hit">
										<xsl:with-param name="elem" select="."/>
									</xsl:call-template>
							</xsl:for-each>
							<xsl:call-template name="spacerow"/>
						</xsl:if>
					</xsl:for-each>

				</xsl:when>
				<xsl:otherwise>
					<xsl:for-each select="$hits">
						<xsl:sort select="@score" order="descending" data-type="number"/>
						<xsl:sort select="@path" order="ascending"/>
						<xsl:sort select="@title" order="ascending"/>
						<xsl:call-template name="hrow">
							<xsl:with-param name="width" select="'3'"/>
						</xsl:call-template>

							<xsl:call-template name="hit">
								<xsl:with-param name="elem" select="."/>
							</xsl:call-template>
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
		</tr>
  	</table>
  </xsl:template>

  <xsl:template name="hit">
  	<xsl:param name="elem"/>
		<tr>
			<td>
				<img border="0" height="16" width="16" src="{key('icon',$elem/@extension)}" space="0" hspace="0"/>
			</td>
  			<td class="uportal-channel-text">
  				 <xsl:choose>
  					<xsl:when test="$elem/@current='true'">
  						<xsl:value-of select="$elem/@title"/>
  					</xsl:when>
  					<xsl:otherwise>
  						<strike><xsl:value-of select="$elem/@title"/></strike>
  								<em> <xsl:value-of select="$OLD_REVISION"/> </em>
  					</xsl:otherwise>
  				</xsl:choose>
  				<!--<xsl:value-of select="@typeID"/>-->
  			</td>
  			<td align="right" class="uportal-channel-table-row-even" width="64">
  				<table border="0" cellpadding="0" cellspacing="0" width="64">
  				<tr>
  				<xsl:if test="$elem/@exists='true'">
  					<td>
  						<img width="16" height="16" src="{$spacerIMG}" border="0" vspace="0" hspace="1"/>
  					</td>
					<td>
                        <xsl:choose>
                        <xsl:when test="($elem/@canWrite='true') and ($elem/@current='true')">
                            <a title="{$EDIT}" href="{$baseActionURL}?CMCommand=openFile&amp;path={$elem/@path}">
                                <img width="16" height="16" src="{$mediaBase}/edit.gif" border="0" vspace="0" hspace="1"/>
                            </a>
                        </xsl:when>
                        <xsl:otherwise>
                            <img width="16" height="16" src="{$spacerIMG}" border="0" vspace="0" hspace="1"/>
                        </xsl:otherwise>
                        </xsl:choose>
					</td>
					<td>
                        <xsl:choose>
                        <xsl:when test="($elem/@hasPreview='true') and ($elem/@current='true')">
                            <a title="{$PREVIEW}" href="javascript:window.open('{$baseDownloadURL}?CMCommand=previewNode&amp;previewPath={$elem/@path}&amp;random={$random}','{$publishWindow}').focus();">
                                <img width="16" height="16" src="{$mediaBase}/eye.gif" border="0" vspace="0" hspace="1"/>
                            </a>
                        </xsl:when>
                        <xsl:otherwise>
                            <img width="16" height="16" src="{$spacerIMG}" border="0" vspace="0" hspace="1"/>
                        </xsl:otherwise>
                        </xsl:choose>
					</td>
					<td>
					<a title="{$DETAILS}" href="{$baseActionURL}?CMCommand=highlightNode&amp;path={$elem/@path}&amp;CMScreen=browse">
						<img width="16" height="16" src="{$mediaBase}/magnify.gif" border="0" vspace="0" hspace="1"/>
					</a>
					</td>
  				</xsl:if>
  				<!--
  				<xsl:if test="$elem/@score">
					<td><table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="uportal-background-highlight" colspan="4">
								<img src="{$spacerIMG}" height="1" wdith="1"/>
							</td>
						</tr>
						<tr>
							<td class="uportal-background-highlight">
								<img src="{$spacerIMG}" height="1" wdith="1"/>
							</td>
							<td class="uportal-background-dark">
								<img src="{$spacerIMG}" height="14">
									<xsl:attribute name="width">
										<xsl:value-of select="number($elem/@score)*16"/>
									</xsl:attribute>
								</img>
							</td>
							<td>
								<img src="{$spacerIMG}" height="14">
									<xsl:attribute name="width">
										<xsl:value-of select="(1-number($elem/@score))*16"/>
									</xsl:attribute>
								</img>
							</td>
							<td class="uportal-background-highlight">
								<img src="{$spacerIMG}" height="1" wdith="1"/>
							</td>
						</tr>
						<tr>
							<td class="uportal-background-highlight" colspan="4">
								<img src="{$spacerIMG}" height="1" wdith="1"/>
							</td>
						</tr>
					</table></td>
				</xsl:if>
				-->
				</tr>
				</table>
  			</td>
  		</tr>
  		<xsl:if test="$elem/child::text()">
  			<tr>
  				<td width="20"><img src="{$spacerIMG}" height="1" width="20"/></td>
  				<td colspan="3" class="uportal-channel-table-row-even">
  					<xsl:value-of select="$elem/child::text()"/>
  				</td>
  			</tr>
  		</xsl:if>
  		<tr>
  			<td width="20"><img src="{$spacerIMG}" height="1" width="20"/></td>
  			<td colspan="3" class="uportal-channel-table-row-odd">
				<xsl:value-of select="$elem/@path"/>
			</td>
  		</tr>
  </xsl:template>

</xsl:stylesheet>
