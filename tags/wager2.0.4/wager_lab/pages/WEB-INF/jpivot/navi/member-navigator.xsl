<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="tree-component">
  <table cellpadding="1" cellspacing="0" border="1" id="{$renderId}">
    <tr>
      <th class="xform-title">

          <table border="0" cellspacing="0" cellpadding="0" width="100%">
            <tr>
              <th align="left" class="xform-title">
                <xsl:value-of select="@title"/>
              </th>
              <td align="right" class="xform-close-button">
                <input type="image" src="{$context}/wcf/form/cancel.png" value="{@cancel-title}" name="{@cancel-id}" width="16" height="16"/>
              </td>
            </tr>
          </table>
      </th>
    </tr>
    <xsl:apply-templates select="tree-node"/>
    <tr>
      <td align="right">
        <div align="right">
          <xsl:if test="@selvisi-id">
            <input type="submit" value="{@selvisi-title}" name="{@selvisi-id}"/><xsl:text> </xsl:text>
            <input type="submit" value="{@selnone-title}" name="{@selnone-id}"/><xsl:text> </xsl:text>
          </xsl:if>
          <input type="submit" value="{@ok-title}" name="{@ok-id}"/><xsl:text> </xsl:text>
          <input type="submit" value="{@cancel-title}" name="{@cancel-id}"/>
        </div>
      </td>
    </tr>
  </table>
</xsl:template>


<xsl:template match="tree-node">
  <tr>
    <td nowrap="nowrap" class="member-node-{@style}">

      <div style="margin-left: {@level}em">
        <!-- checkbox / radiobox is handled by controls.xsl -->
        <xsl:apply-templates select="checkBox|radioButton"/>

        <!-- expand/collapse button -->
        <xsl:choose>
          <xsl:when test="@state='expanded'">
            <input border="0" type="image" name="{@id}.collapse" src="{$context}/wcf/tree/collapse.png" width="9" height="9"/>
          </xsl:when>
          <xsl:when test="@state='collapsed'">
            <input border="0" type="image" name="{@id}.expand" src="{$context}/wcf/tree/expand.png" width="9" height="9"/>
          </xsl:when>
          <xsl:otherwise>
            <img src="{$context}/wcf/tree/leaf.png" width="9" height="9"/>
          </xsl:otherwise>
        </xsl:choose>

        <xsl:apply-templates select="move-button"/>

        <xsl:text> </xsl:text>

        <xsl:value-of select="@label"/>
      </div>
    </td>
  </tr>
  <xsl:apply-templates select="tree-node"/>
</xsl:template>

</xsl:stylesheet>
