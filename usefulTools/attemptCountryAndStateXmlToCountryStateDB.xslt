<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:template match="/">
        <xsl:for-each select="/iso_3166_2_entries/iso_3166_country/iso_3166_subset/iso_3166_2_entry">
            insert into country_state (type, code, name, country)
            values('<xsl:value-of select="../@type"/>'
            ,'<xsl:value-of select="@code"/>'
            ,'<xsl:value-of select="@name"/>'
            ,'<xsl:value-of select="../../@code"/>');
        </xsl:for-each>
    </xsl:template>
    
</xsl:stylesheet>
