<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
        
        <!-- Defined Image File Paths -->
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
        <xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>

        <xsl:template name="header">
		<xsl:param name="activeSubtab"/>
                <xsl:param name="previousButtonFlag">true</xsl:param>
		<xsl:param name="previousButtonUrl"/>
                <xsl:param name="nextButtonFlag"/>
		<xsl:param name="nextButtonUrl"/>                
                
		<table width="100%">
                    <tr>
                                <xsl:choose>
					<xsl:when test="$activeSubtab='creation'">
						<td color="blue" class="uportal-channel-current-subtitle">1. Creation Type</td>
					</xsl:when>
					<xsl:otherwise>
						<td class="uportal-label">
                                                    1. Creation Type
						</td>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$activeSubtab='specimens'">
						<td color="blue" class="uportal-channel-current-subtitle">2. Specimens</td>
					</xsl:when>
					<xsl:otherwise>
						<td class="uportal-label">
                                                    2. Specimens
						</td>
                                        </xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$activeSubtab='smartform'">
						<td color="blue" class="uportal-channel-current-subtitle">3. Smartform</td>
					</xsl:when>
					<xsl:otherwise>
						<td class="uportal-label">
                                                        3. Smartform
						</td>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$activeSubtab='report'">
						<td color="blue" class="uportal-channel-current-subtitle">4. Report</td>
					</xsl:when>
					<xsl:otherwise>
						<td class="uportal-label">
                                                        4. Report
						</td>
					</xsl:otherwise>
				</xsl:choose>
						
				<td align="right">
					<!-- "Previous" Button -->
					<xsl:choose>
						<xsl:when test="$previousButtonFlag='true'">
							<xsl:choose>
								<xsl:when test="$previousButtonUrl=''">
									<a href="{$baseActionURL}?&amp;back=true">
										<img border="0" src="{$buttonImagePath}/previous_enabled.gif" alt="Previous"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="{$previousButtonUrl}">
										<img border="0" src="{$buttonImagePath}/previous_enabled.gif" alt="Previous"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<img border="0" src="{$buttonImagePath}/previous_disabled.gif" alt="Previous"/>
						</xsl:otherwise>
					</xsl:choose>
					<!-- "Next" Button -->
					<xsl:choose>
						<xsl:when test="$nextButtonFlag='true'">
							<xsl:choose>
								<xsl:when test="$nextButtonUrl=''">
									<a href="{$baseActionURL}?&amp;back=true">
										<img border="0" src="{$buttonImagePath}/next_enabled.gif" alt="Next"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="{$nextButtonUrl}">
										<img border="0" src="{$buttonImagePath}/next_enabled.gif" alt="Next"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<img border="0" src="{$buttonImagePath}/next_disabled.gif" alt="Next"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>

		</table>
                
                <hr/>
                <br/>
        
    </xsl:template>
        
</xsl:stylesheet>
