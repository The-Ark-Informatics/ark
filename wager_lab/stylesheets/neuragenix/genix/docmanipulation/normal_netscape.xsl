<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="no" />
  <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
  <xsl:param name="templateUpdateChannelURL">templateUpdateChannelURL_false</xsl:param>
  <xsl:param name="ContentManagerChannelURL">ContentManagerChannelURL_false</xsl:param>
  <xsl:param name="name_prev">world</xsl:param>

  <xsl:template match="/">

    <a name="helloworld" />
    <xsl:apply-templates />
    <form action="{$baseActionURL}#helloworld" method="post">
      Enter your name:
      <input type="text" name="name" size="15"
            class="uportal-input-text"
            value="{$name_prev}" />
      <input type="submit" name="submit" value="submit"
            class="uportal-button" />
      <input type="submit" name="clear" value="clear"
            class="uportal-button" />

    <p>This is the <b>netscape</b> stylesheet.</p>
    <table>
    <tr>
    <td>
        Links to the ContentManager Documents
    </td>
    </tr>
    <tr>
    <td>
        <a href="{$ContentManagerChannelURL}?key=/projects/BioHelp.xml&amp;CMCommand=openProject&amp;CMScreen=browse&amp;uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=8&amp;path=/Patients/">
                                            Patients
                                        </a>
    </td>
    </tr>
    <tr>
    <td>
        <a href="{$ContentManagerChannelURL}?key=/projects/BioHelp.xml&amp;CMCommand=openProject&amp;CMScreen=browse&amp;uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=8&amp;path=/Biospecimen/">
                                            Biospecimen
                                        </a>
    </td>
    </tr>
    <tr>
    <td>
        <a href="{$ContentManagerChannelURL}?key=/projects/BioHelp.xml&amp;CMCommand=openProject&amp;CMScreen=browse&amp;uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=8&amp;path=/Study/">
                Study
            </a>
    </td>
    </tr>
    <tr>
    <td>
        <a href="{$ContentManagerChannelURL}?key=/projects/cucms-site.xml&amp;CMCommand=openProject&amp;CMScreen=browse&amp;uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=8&amp;path=/contributors/">
             Contributors in cucms-site
            </a>
    </td>
    </tr>
    </table>
     </form>
     <table>
     <tr>
        <td>
            Creating a new node
        </td>
     </tr>
     <tr>
        <td>
            <a href="{$ContentManagerChannelURL}?key=/projects/cucms-site.xml&amp;CMCommand=newFile&amp;CMFilePath=/downloads/*/index.html&amp;CMNewDirectory=test&amp;uP_root=root&amp;uP_sparam=activeTab&amp;activeTab=8">
             New Node
            </a>
        </td>
     </tr>
     </table>        
   </xsl:template>

  <xsl:template match="name">
    <p>Hello <xsl:value-of select="." />!</p>
  </xsl:template>
</xsl:stylesheet>