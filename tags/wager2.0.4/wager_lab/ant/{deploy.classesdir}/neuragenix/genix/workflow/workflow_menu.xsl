<?xml version="1.0" encoding="utf-8"?>

<!-- 
    workflow_menu.xsl, part of the Workflow channel
    Author: aagustian@neuragenix.com
	Date: 14/04/2004
    Neuragenix copyright 2004 
-->


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<!--xsl:output method="html" indent="no"/-->
	<!--xsl:param name="baseActionURL">baseActionURL_false</xsl:param-->

	<xsl:template match="/">
	<script language="javascript" >

	function confirmDelete(aURL) 
	{
		var confirmAnswer = confirm('Are you sure you want to delete this record?');

		if(confirmAnswer == true)
		{
			window.location=aURL + '&amp;delete=true';
		}
	}
	
	function confirmClear(aURL) 
	{
		var confirmAnswer = confirm('Are you sure you want to clear the fields?');

		if(confirmAnswer == true)
		{
			window.location=aURL + '&amp;clear=true';
		}
	}

	</script>
	<xsl:apply-templates />
    </xsl:template>
</xsl:stylesheet>
