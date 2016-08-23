<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:template match="/">
        <xsl:for-each select="/iso_3166_entries/iso_3166_entry">
            update country  
            set alpha_2_code = '<xsl:value-of select="@alpha_2_code"/>',
            set alpha_3_code = '<xsl:value-of select="@alpha_3_code"/>',
            set alpha_2_code = '<xsl:value-of select="@numeric_code"/>',
            set alpha_2_code = '<xsl:value-of select="@name"/>',
            set alpha_2_code = '<xsl:value-of select="@official_name"/>'
            where name =  upper('<xsl:value-of select="@name"/>');            
        </xsl:for-each>
    </xsl:template>
    
</xsl:stylesheet>
