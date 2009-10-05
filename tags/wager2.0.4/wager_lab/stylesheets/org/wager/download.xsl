<?xml version="1.0" encoding="utf-8"?>
<!-- normal_netscape.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!-- resource URL parameter -->

  	<xsl:param name="resourceURL">default</xsl:param>
	<xsl:template match="/" >
 	<a href="{$resourceURL}?filename=NAME_OF_FILE">
     Filename
	</a>
	</xsl:template>
</xsl:stylesheet>
