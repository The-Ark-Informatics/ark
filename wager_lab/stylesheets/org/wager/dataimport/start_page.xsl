<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" indent="yes"/>
  <xsl:variable name="funcpanelImagePath">media/neuragenix/funcpanel</xsl:variable>
  <xsl:variable name="spacerImagePath">media/neuragenix/infopanel/spacer.gif</xsl:variable>
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
 
  <xsl:template name="content">
    
    <form name="aliquot_form" action="{$baseActionURL}?uP_root=root&amp;action=upload" method="post" enctype="multipart/form-data">
      <table>
        <tr>
          <td valign="top">
            <br/>
            Please select the location of a CSV (comma separated) file that contains aliquot information.
            <br/>
            <table> 
              <tr>
                <td class="uportal-label" height="5px">Type:</td>
                <td>
                  <select name="mode"> 
                  <option value="0">Nanodrop</option>
                    <option value="1">DNA Aliquot</option>
                  </select>
                </td>
               
                <td class="uportal-label" height="5px">Separator:</td>
                <td>
                <select name="separator">
                  <option value="comma">Comma</option>
                  <option value="tab">Tab</option>
                </select>
                </td>
              </tr>
              <tr>
              <td class="uportal-label" height="5px">
                File:
              </td>
              <td>
                <input type="file" name="ALIQUOT_strFilename" tabindex="22" class="uportal-input-text"/>
              </td>
              
              
            </tr>
              <tr><td></td><td><input type="submit" value="Submit"/></td></tr></table></td><td><img src="media/neuragenix/aliquot.jpg"/></td></tr>
        
      </table>
    </form>
  </xsl:template>
<xsl:template match="/nanodrop">
  <html>
  <body>
    <br/><br/>
    <table class="funcpanel" cellpadding="0" cellspacing="0" border="0" width="80%">
      <tr valign="bottom">
        <td><img src="{$funcpanelImagePath}/funcpanel_header_active_left.gif"/></td>
        <td class="funcpanel_header_active" align="left" colspan="3" width="100%">DATA IMPORT</td>
        <td><img src="{$funcpanelImagePath}/funcpanel_header_active_right.gif"/></td>
      </tr>
      <tr class="funcpanel_content">
        <td class="funcpanel_content_spacer" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
      </tr>
      <tr class="funcpanel_content">
        <td class="funcpanel_left_border">&#160;</td>
        <td colspan="3">
          <xsl:call-template name="content"/>
        </td>
        <td class="funcpanel_right_border">&#160;</td>
      </tr>
      <tr class="funcpanel_content">
        <td class="funcpanel_bottom_border" colspan="5"><img width="1" height="4" src="{$spacerImagePath}"/></td>
      </tr>
    </table>


       
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>
