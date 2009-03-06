<?xml version="1.0" encoding="utf-8"?>
<!-- admin_password.xsl, Administration channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./admin_menu.xsl"/>
    <xsl:output method="html" indent="no" />

    <xsl:template match="manage_case">

        <!-- Get variables into the stylesheet -->

        <!-- Form Fill -->
        <form action="{$baseActionURL}?current=search_case" method="post">
        <table width="0%">
            
        </table>
        </form>
    </xsl:template>
</xsl:stylesheet>
