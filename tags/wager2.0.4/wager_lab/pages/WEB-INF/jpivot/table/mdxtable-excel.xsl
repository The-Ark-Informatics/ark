<?xml version="1.0"?>

<!-- renders the JPivot Table -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- the id of the table for httpUnit -->
<xsl:param name="renderId"/>
<xsl:param name="context"/>
<xsl:param name="imgpath" select="'jpivot/table'"/>

<xsl:output method="html" indent="yes"/>

<xsl:template match="mdxtable">
  <xsl:if test="@message">
    <div class="table-message"><xsl:value-of select="@message"/></div>
  </xsl:if>
  <table border="1" cellspacing="0" cellpadding="2" id="{$renderId}">
    <xsl:apply-templates select="head"/>
    <xsl:apply-templates select="body"/>
  </table>
</xsl:template>

<xsl:template match="head | body">
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="row">
  <tr>
    <xsl:apply-templates/>
  </tr>
</xsl:template>


<xsl:template match="corner">
  <th nowrap="nowrap" class="corner-heading" colspan="{@colspan}" rowspan="{@rowspan}">
    <xsl:apply-templates/>
    <!-- &#160; == &nbsp; -->
    <xsl:text>&#160;</xsl:text>
  </th>
</xsl:template>


<xsl:template match="column-heading[@indent]">
  <th nowrap="nowrap" class="column-heading-{@style}" colspan="{@colspan}" rowspan="{@rowspan}">
    <div style="margin-top: {@indent}em">
      <xsl:apply-templates/>
    </div>
  </th>
</xsl:template>


<xsl:template match="row-heading[@indent]">
  <th nowrap="nowrap" class="row-heading-{@style}" colspan="{@colspan}" rowspan="{@rowspan}">
    <div style="margin-left: {@indent}em">
      <xsl:apply-templates/>
    </div>
  </th>
</xsl:template>


<xsl:template match="column-heading">
  <th nowrap="nowrap" class="column-heading-{@style}" colspan="{@colspan}" rowspan="{@rowspan}">
    <xsl:apply-templates/>
  </th>
</xsl:template>


<xsl:template match="row-heading">
  <th nowrap="nowrap" class="row-heading-{@style}" colspan="{@colspan}" rowspan="{@rowspan}">
    <xsl:apply-templates/>
  </th>
</xsl:template>


<xsl:template match="heading-heading">
  <th nowrap="nowrap" class="heading-heading" colspan="{@colspan}" rowspan="{@rowspan}">
    <xsl:apply-templates/>
  </th>
</xsl:template>


<!-- caption of a member in row/column heading -->
<xsl:template match="caption[@href]">
  <a href="{@href}">
    <xsl:value-of select="@caption"/>
  </a>
</xsl:template>


<xsl:template match="caption">
  <xsl:call-template name="render-label">
    <xsl:with-param name="label">
      <xsl:value-of select="@caption"/>
    </xsl:with-param>
  </xsl:call-template>
</xsl:template>


<xsl:template match="cell">
  <td nowrap="nowrap" class="cell-{@style}">
    <xsl:apply-templates select="drill-through"/>
    <xsl:call-template name="render-label">
      <xsl:with-param name="label">
            <xsl:value-of select="@value"/>
      </xsl:with-param>
    </xsl:call-template>
  </td>
</xsl:template>


<xsl:template name="render-label">
  <xsl:param name="label"/>
  <xsl:choose>
    <xsl:when test="property[@name='link']">
      <a href="{property[@name='link']/@value}" target="_blank">
        <xsl:value-of select="$label"/>
        <xsl:apply-templates select="property"/>
      </a>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="$label"/>
      <xsl:apply-templates select="property"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="property"/>


<xsl:template match="*|@*|node()">
  <xsl:copy>
    <xsl:apply-templates select="*|@*|node()"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>
