<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="no" />
  <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
  <xsl:param name="templateUpdateChannelURL">templateUpdateChannelURL_false</xsl:param>
  <xsl:param name="name_prev">world</xsl:param>

  <xsl:template match="/">

    <a name="helloworld" />
    <xsl:apply-templates />
    <form action="{$baseActionURL}#helloworld" method="post">
    <table>
        <tr>
            <td>
              Enter your name:
            </td>
            <td>
                <input type="text" name="name" size="15" class="uportal-input-text" value="{$name_prev}" />
            </td>
        </tr>
        <tr>
            <td>
                Address :
            </td>
            <td>
                <xsl:variable name="address_val"><xsl:value-of select="/helloworld/address"/></xsl:variable>
                <input type="text" name="address" size="15" class="uportal-input-text" value="{$address_val}" />
            </td>
        </tr>
        <tr>
            <td>
                <input type="submit" name="submit" value="submit" class="uportal-button" />
            </td>
            <td>
                <input type="submit" name="clear" value="clear"  class="uportal-button" />
            </td>
        </tr>
        <tr>
            <td>
<!--
@author rennypv
    This is the link to create a document
    the TemplateUpdate requires the functionname(which is the function name of this channel) and the template name which is the name of
    the template in the location specified in the portal.properties file
-->            
            <a href="{$templateUpdateChannelURL}?uP_root=root&amp;functionName=HelloWorld&amp;templateName=testpage.doc">
                                            Create Document
                                        </a>
<!--End of the edit by rennypv-->                                        
            </td>
        </tr>
    </table>
     </form>
             
   </xsl:template>

  <xsl:template match="/helloworld/name">
    <p>Hello <xsl:value-of select="." />!</p>
  </xsl:template>
</xsl:stylesheet>