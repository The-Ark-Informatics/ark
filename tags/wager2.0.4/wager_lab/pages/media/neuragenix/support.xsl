<?xml version='1.0' encoding='utf-8' ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="list">
      <table width="100%" border="0" cellpadding="0" class="uportal-background-content">
        <!--tr align="left" valign="top">
          <td class="uportal-channel-text" nowrap="nowrap">
            <a target="_blank"><xsl:attribute name="href"><xsl:value-of select="support1URL"/></xsl:attribute><xsl:value-of select="support1"/></a>
          </td>
        </tr-->
        <tr></tr>
        
        <tr align="left" valign="top">
          <td class="uportal-channel-text" nowrap="nowrap">
            <xsl:value-of select="title"/>
          </td>
        </tr>
            
        <tr></tr>
        <tr></tr>
        <tr></tr>
        <tr></tr>    
           	
        <tr align="left" valign="top">
          <td class="uportal-channel-text" nowrap="nowrap">
            <xsl:value-of select="email"/>
          </td>
        </tr>
        
        <tr></tr>
        <tr></tr>
        
	<tr align="left" valign="top">
          <td class="uportal-channel-text" nowrap="nowrap">
            <xsl:value-of select="phone"/>
          </td>
        </tr>
    
     <!--
        
	<tr align="left" valign="top">
          <td class="uportal-channel-text" nowrap="nowrap">
            <xsl:value-of select="support5"/>
          </td>
        </tr>
      -->
      
      </table>
  </xsl:template>
</xsl:stylesheet>
