<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE nbsp [
<!ENTITY nbsp "&#160;">
]> 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:cms="http://www.ais.columbia.edu/sws/xmlns/cucms#" xmlns:cfs="http://www.ais.columbia.edu/sws/xmlns/cufs#" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:dc="http://purl.org/dc/elements/1.0/">
	<xsl:output method="html" indent="yes" />
	<xsl:param name="baseurl" />
	<xsl:param name="pageTitle" select="false()"/>
	<xsl:variable name="spacer" select="concat($baseurl,'/resources/spacer.gif')" />
	<xsl:variable name="sourceDirectory" select="/cms:wrapper/cms:source/@directory" />
	<xsl:variable name="sourceBaseName" select="/cms:wrapper/cms:source/@basename" />
	
	<xsl:template name="leftlink">
		<xsl:param name="url"/>
		<xsl:param name="include"/>
		<xsl:param name="text" select="false()"/>
		<a href="{$url}">
			<xsl:choose>
				<xsl:when test="/cms:wrapper/cms:source/@path=$include/@path">
					<xsl:attribute name="class">left-nav-active</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class">left-nav-passive</xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="$text">
					<xsl:value-of select="$text"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="includeTitle">
						<xsl:with-param name="include" select="$include"/>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</a>
	</xsl:template>
	
	<xsl:template match="/">
	<html>
		<link type="text/css" rel="stylesheet" href="{concat($baseurl,'/resources/styles.css')}" />
		<head>
			<title>
				<xsl:call-template name="title"/>
			</title>
		</head>
	<body>
		<table border="0" cellpadding="2" cellspacing="2" valign="top" width="100%">
			<tr>
				<td colspan="2" class="header">
					<a href="{concat($baseurl,'/index.html')}" class="header">Abstract University</a>
				</td>
			</tr>	

			<tr>
				<td valign="top">
					<table border="0" cellpadding="2" cellspacing="2">
					<tr><td nowrap="nowrap">
						<xsl:call-template name="leftlink">
							<xsl:with-param name="url" select="concat($baseurl,'/index.html')"/>
							<xsl:with-param name="include" select="/cms:wrapper/child::*[@path='/index.xml']"/>
							<xsl:with-param name="text">Abstract U. Home</xsl:with-param>
						</xsl:call-template>
					</td></tr>
					<tr><td nowrap="nowrap">
						<xsl:call-template name="leftlink">
							<xsl:with-param name="url" select="concat($baseurl,'/faculty-list.html')"/>
							<xsl:with-param name="include" select="/cms:wrapper/child::*[@path='/faculty-list.xml']"/>
							<xsl:with-param name="text">List of All Faculty</xsl:with-param>
						</xsl:call-template>
					</td></tr>
					<xsl:for-each select="/cms:wrapper/cms:include[@basename='department']">
						<tr><td nowrap="nowrap">
						<xsl:call-template name="leftlink">
							<xsl:with-param name="url" select="concat($baseurl,@directory,@basename,'.html')"/>
							<xsl:with-param name="include" select="."/>
						</xsl:call-template>
				
							<xsl:if test="contains(/cms:wrapper/cms:source/@path,@directory)">
								
								<table border="0" cellpadding="0" cellspacing="0">
								<tr><td><img src="" height="1" width="10"/></td>
									<td>
									<xsl:variable name="dir" select="@directory"/>
									<xsl:if test="/cms:wrapper/cms:include[@path=concat($dir,'faculty-list.xml')]">
										<xsl:call-template name="leftlink">
											<xsl:with-param name="url" select="concat($baseurl,@directory,'faculty-list.html')"/>
											<xsl:with-param name="include" select="/cms:wrapper/cms:include[@path=concat($dir,'faculty-list.xml')]"/>
											<xsl:with-param name="text">Faculty List</xsl:with-param>
										</xsl:call-template>
										<br/>
										<xsl:if test="/cms:wrapper/cms:source/faculty/@last-name">
											<table border="0" cellpadding="0" cellspacing="0"><tr>
												<td><img src="" height="1" width="10"/></td>
												<td class="left-nav-active">
													<xsl:value-of select="concat(/cms:wrapper/cms:source/faculty/@last-name,', ',/cms:wrapper/cms:source/faculty/@first-name)"/>
												</td>
											</tr></table>
										</xsl:if>
									</xsl:if>
									<xsl:if test="/cms:wrapper/cms:include[@path=concat($dir,'course-list.xml')]">
										<xsl:call-template name="leftlink">
											<xsl:with-param name="url" select="concat($baseurl,@directory,'course-list.html')"/>
											<xsl:with-param name="include" select="/cms:wrapper/cms:include[@path=concat($dir,'course-list.xml')]"/>
											<xsl:with-param name="text">Course List</xsl:with-param>
										</xsl:call-template>
										<br/>
										<xsl:if test="/cms:wrapper/cms:source/course">
											<table border="0" cellpadding="0" cellspacing="0"><tr>
												<td><img src="" height="1" width="10"/></td>
												<td class="left-nav-active">
													<xsl:call-template name="includeTitle">
														<xsl:with-param name="include" select="/cms:wrapper/cms:source"/>
													</xsl:call-template>
												</td>
											</tr></table>
										</xsl:if>
									</xsl:if>
									<xsl:for-each select="/cms:wrapper/cms:include[contains(@path,concat($dir,'pages/'))]">
										<xsl:call-template name="leftlink">
											<xsl:with-param name="url" select="concat($baseurl,@directory,@basename,'.html')"/>
											<xsl:with-param name="include" select="."/>
										</xsl:call-template>
										<br/>
									</xsl:for-each>
									</td>
								</tr></table>
							</xsl:if>
	
						</td></tr>
					</xsl:for-each>
					</table>
				</td>
				<td valign="top">
					<xsl:apply-templates select="/cms:wrapper/cms:source/content/node()" />
					<xsl:call-template name="main" />
				</td>
			</tr>
			<tr>
				<td colspan="2" class="footer">
					Copyright 2003 Abstract University - All rights reserved
				</td>
			</tr>
		</table>
		</body>
		</html>
	</xsl:template>
	
	<xsl:template name="includeTitle">
		<xsl:param name="include"/>
		<xsl:choose>
			<xsl:when test="$include/rdf:RDF/cfs:File/dc:title">
				<xsl:value-of select="$include/rdf:RDF/cfs:File/dc:title"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$include/@basename"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="title">
		<xsl:choose>
			<xsl:when test="$pageTitle">
				<xsl:value-of select="$pageTitle"/>
			</xsl:when>
			<xsl:when test="/cms:wrapper/cms:source/rdf:RDF/cfs:File/dc:title" >
				<xsl:value-of select="/cms:wrapper/cms:source/rdf:RDF/cfs:File/dc:title"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$sourceBaseName"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- Page Header Template -->
	<xsl:template name="header">
	</xsl:template>
<!-- Footer Template -->
	<xsl:template name="footer">
	</xsl:template>
<!-- Left Navigation -->
	<xsl:template name="left-nav">
	</xsl:template>
<!-- Text Node Template-->
	<xsl:template match="text()">
		<xsl:value-of select="." />
	</xsl:template>
<!-- Content Template -->
	<xsl:template match="content">
		<xsl:apply-templates select="node()" />
	</xsl:template>
<!-- Line Break Template-->
	<xsl:template match="line-break">
		<xsl:if test="@number-of-lines='1'">
			<br />
		</xsl:if>
		<xsl:if test="@number-of-lines='2'">
			<br />
			<br />
		</xsl:if>
		<xsl:if test="@number-of-lines='3'">
			<br />
			<br />
			<br />
		</xsl:if>
		<xsl:if test="@number-of-lines='4'">
			<br />
			<br />
			<br />
			<br />
		</xsl:if>
	</xsl:template>
<!-- Image template -->
	<xsl:template match="image">
		<xsl:choose>
			<xsl:when test="@align = 'center'">
				<div align="{@align}">
					<img border="0"> 
					<xsl:choose>
						<xsl:when test="starts-with(@src,'/')">
							<xsl:attribute name="src">
								<xsl:value-of select="@src" />
							</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="src">
								<xsl:value-of select="concat($baseurl,'/images/',@src)" />
							</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="@alt">
						<xsl:attribute name="alt">
							<xsl:value-of select="@alt" />
						</xsl:attribute>
						<xsl:attribute name="title">
							<xsl:value-of select="@alt" />
						</xsl:attribute>
					</xsl:if>
					</img> 
				</div>
			</xsl:when>
			<xsl:otherwise>
				<img border="0"> 
				<xsl:attribute name="align">
					<xsl:value-of select="@align" />
				</xsl:attribute>
				<xsl:choose>
					<xsl:when test="starts-with(@src,'/')">
						<xsl:attribute name="src">
							<xsl:value-of select="@src" />
						</xsl:attribute>
					</xsl:when>
					<xsl:otherwise>
						<xsl:attribute name="src">
							<xsl:value-of select="concat($baseurl,'/images/',@src)" />
						</xsl:attribute>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="@alt">
					<xsl:attribute name="alt">
						<xsl:value-of select="@alt" />
					</xsl:attribute>
					<xsl:attribute name="title">
						<xsl:value-of select="@alt" />
					</xsl:attribute>
				</xsl:if>
				</img> 
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
<!-- Link Template -->
	<xsl:template match="link">
		<xsl:text>
		</xsl:text>
		<a> 
			<xsl:choose>
				<xsl:when test="starts-with(@url,'http')">
					<xsl:attribute name="href">
						<xsl:value-of select="@url" />
					</xsl:attribute>
					<xsl:if test="@target='new-window'">
						<xsl:attribute name="target">
							<xsl:value-of select="'_blank'" />
						</xsl:attribute>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="href">
						<xsl:value-of select="concat($baseurl,@url)" />
					</xsl:attribute>
					<xsl:if test="@target='new-window'">
						<xsl:attribute name="target">
							<xsl:value-of select="'_blank'" />
						</xsl:attribute>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:attribute name="class">
				<xsl:value-of select="'pagelink'" />
			</xsl:attribute>
<!-- Call template -->
			<xsl:choose>
				<xsl:when test="count(node()) &gt; 0">
					<xsl:apply-templates select="node()" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@url" />
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text>
			</xsl:text>
		</a> 
	</xsl:template>
<!-- Email Link Template -->
	<xsl:template match="email-link">
		<a class="pagelink" href="{concat('mailto:',@email-address)}">
			<xsl:apply-templates select="node()" />
		</a> 
	</xsl:template>
<!-- Text Template -->
	<xsl:template match="page-text">
		<span> 
<!-- Different options for text overall style
			normal,normal,none
			normal,italic,none
			normal,italic,underline
			normal,normal,underline
			
			bold,normal,none
			bold,italic,none
			bold,italic,underline
			bold,normal,underline			
		-->
<!-- bold -->
			<xsl:choose>
				<xsl:when test="starts-with(@font-weight,'bold')">
					<xsl:choose>
						<xsl:when test="starts-with(@font-style,'normal')">
							<xsl:choose>
<!-- Text decoration None -->
								<xsl:when test="starts-with(@text-decoration,'none')">
									<xsl:attribute name="class">
										<xsl:text>
											maintextB
										</xsl:text>
									</xsl:attribute>
								</xsl:when>
<!-- underline -->
								<xsl:otherwise>
									<xsl:attribute name="class">
										<xsl:text>
											maintextBoldUnderline
										</xsl:text>
									</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
<!-- Font Style italic-->
						<xsl:otherwise>
							<xsl:choose>
<!-- Text decoration None -->
								<xsl:when test="starts-with(@text-decoration,'none')">
									<xsl:attribute name="class">
										<xsl:text>maintextBoldItalic</xsl:text>
									</xsl:attribute>
								</xsl:when>
<!-- underline -->
								<xsl:otherwise>
									<xsl:attribute name="class">
										<xsl:text>maintextBoldItalicUnderline</xsl:text>
									</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
<!-- Normal -->
				<xsl:otherwise>
					<xsl:choose>
						<xsl:when test="starts-with(@font-style,'normal')">
							<xsl:choose>
<!-- Text decoration None -->
								<xsl:when test="starts-with(@text-decoration,'none')">
									<xsl:attribute name="class">
										<xsl:text>maintext</xsl:text>
									</xsl:attribute>
								</xsl:when>
<!-- underline -->
								<xsl:otherwise>
									<xsl:attribute name="class">
										<xsl:text>
											maintextUnderline
										</xsl:text>
									</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
<!-- Font Style italic-->
						<xsl:otherwise>
							<xsl:choose>
<!-- Text decoration None -->
								<xsl:when test="starts-with(@text-decoration,'none')">
									<xsl:attribute name="class">
										<xsl:text>maintextItalic</xsl:text>
									</xsl:attribute>
								</xsl:when>
<!-- underline -->
								<xsl:otherwise>
									<xsl:attribute name="class">
										<xsl:text>maintextItalicUnderline</xsl:text>
									</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="starts-with(@begin-text-with,'line-break')">
					<br />
				</xsl:when>
				<xsl:when test="starts-with(@begin-text-with,'double-line-break')">
					<br />
					<br />
				</xsl:when>
			</xsl:choose>
			<xsl:text>
			</xsl:text>
			<xsl:apply-templates select="node()" />
			<xsl:text>
			</xsl:text>
			<xsl:choose>
				<xsl:when test="starts-with(@end-text-with,'line-break')">
					<br />
				</xsl:when>
				<xsl:when test="starts-with(@end-text-with,'double-line-break')">
					<br />
					<br />
				</xsl:when>
			</xsl:choose>
		</span> 
	</xsl:template>
<!-- List Template -->
	<xsl:template match="list">
		<span class="maintext">
			<xsl:value-of select="@list-heading" />
		</span> 
		<xsl:choose>
			<xsl:when test="starts-with(@list-type,'bullets')">
				<ul>
					<xsl:apply-templates select="node()" />
				</ul>
			</xsl:when>
			<xsl:when test="starts-with(@list-type,'numbered')">
				<ol>
					<xsl:apply-templates select="node()" />
				</ol>
			</xsl:when>
			<xsl:when test="starts-with(@list-type,alpha-lowercase)">
				<ol type="a">
					<xsl:apply-templates select="node()" />
				</ol>
			</xsl:when>
			<xsl:when test="starts-with(@list-type,alpha-upercase)">
				<ol type="A">
					<xsl:apply-templates select="node()" />
				</ol>
			</xsl:when>
			<xsl:when test="starts-with(@list-type,roman-lowercase)">
				<ol type="i">
					<xsl:apply-templates select="node()" />
				</ol>
			</xsl:when>
			<xsl:when test="starts-with(@list-type,roman-uppercase)">
				<ol type="i">
					<xsl:apply-templates select="node()" />
				</ol>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
<!-- List Item Template -->
	<xsl:template match="list-item">
		<li>
			<xsl:apply-templates select="node()" />
		</li>
	</xsl:template>
<!-- Section Heading Template -->
	<xsl:template match="section-heading">
		<xsl:choose>
			<xsl:when test="starts-with(@begin-text-with,'line-break')">
				<br />
			</xsl:when>
			<xsl:when test="starts-with(@begin-text-with,'double-line-break')">
				<br />
				<br />
			</xsl:when>
		</xsl:choose>
		<a class="sectionhead" name="{generate-id(node())}"> 
			<xsl:apply-templates select="node()" />
		</a> 
		<br />
		<br />
	</xsl:template>
<!-- Back to Top Link -->
	<xsl:template match="back-to-top-link">
		<center>
			<a href="#top" class="pagelink">Back to Top</a> 
		</center>
	</xsl:template>
</xsl:stylesheet>
