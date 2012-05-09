<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:template match="/">
    <!--    insert into blah()
        values -->
        <xsl:for-each select="/iso_3166_entries/iso_3166_entry">
            insert into country (alpha_2_code, alpha_3_code, numeric_code, name, official_name)
            values('<xsl:value-of select="@alpha_2_code"/>'
            ,'<xsl:value-of select="@alpha_3_code"/>'
            ,'<xsl:value-of select="@numeric_code"/>'
            ,'<xsl:value-of select="@name"/>'
            ,'<xsl:value-of select="@official_name"/>');
        </xsl:for-each>
    </xsl:template>
    
</xsl:stylesheet>
<!--
/iso_3166_entries/iso_3166_entry/@alpha_2_code
/iso_3166_entries/iso_3166_entry/@alpha_3_code
/iso_3166_entries/iso_3166_entry/@numeric_code
/iso_3166_entries/iso_3166_entry/@name
/iso_3166_entries/iso_3166_entry/@official_name
-->