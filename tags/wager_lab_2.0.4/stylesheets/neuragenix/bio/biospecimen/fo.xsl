<?xml version = '1.0' encoding = 'utf-8'?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xlink="http://www.w3.org/1999/xlink">
    <xsl:variable name="_XDOFOPOS" select="''"/>
    <xsl:variable name="_XDOFOPOS2" select="number(1)"/>
    <xsl:variable name="_XDOFOTOTAL" select="number(1)"/>
    <xsl:variable name="_XDOFOOSTOTAL" select="number(0)"/>
    <xsl:attribute-set name="padding">
        <xsl:attribute name="padding-bottom">0.25pt</xsl:attribute>
        <xsl:attribute name="padding-top">0.25pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="text">
        <xsl:attribute name="text-align">start</xsl:attribute>
        <xsl:attribute name="orphans">2</xsl:attribute>
        <xsl:attribute name="start-indent">0.0pt</xsl:attribute>
        <xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
        <xsl:attribute name="padding-top">0.0pt</xsl:attribute>
        <xsl:attribute name="end-indent">0.0pt</xsl:attribute>
        <xsl:attribute name="padding-bottom">0.0pt</xsl:attribute>
        <xsl:attribute name="height">0.0pt</xsl:attribute>
        <xsl:attribute name="widows">2</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="align-left">
        <xsl:attribute name="text-align">left</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="align-center">
        <xsl:attribute name="text-align">center</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="align-right">
        <xsl:attribute name="text-align">right</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="footer">
        <xsl:attribute name="text-align">right</xsl:attribute>
        <xsl:attribute name="start-indent">5.4pt</xsl:attribute>
        <xsl:attribute name="end-indent">5.4pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="text_2">
        <xsl:attribute name="start-indent">5.4pt</xsl:attribute>
        <xsl:attribute name="end-indent">5.4pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="text_20">
        <xsl:attribute name="height">13.872pt</xsl:attribute>
        <xsl:attribute name="end-indent">5.4pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="text_0">
        <xsl:attribute name="end-indent">5.4pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="page-header">
        <xsl:attribute name="color">#PAGE_HEADER_FONT_COLOR#</xsl:attribute>
        <xsl:attribute name="font-family">#PAGE_HEADER_FONT_FAMILY#</xsl:attribute>
        <xsl:attribute name="white-space-collapse">false</xsl:attribute>
        <xsl:attribute name="font-size">#PAGE_HEADER_FONT_SIZE#pt</xsl:attribute>
        <xsl:attribute name="font-weight">#PAGE_HEADER_FONT_WEIGHT#</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="page-footer">
        <xsl:attribute name="color">#PAGE_FOOTER_FONT_COLOR#</xsl:attribute>
        <xsl:attribute name="font-family">#PAGE_FOOTER_FONT_FAMILY#</xsl:attribute>
        <xsl:attribute name="white-space-collapse">false</xsl:attribute>
        <xsl:attribute name="font-size">#PAGE_FOOTER_FONT_SIZE#pt</xsl:attribute>
        <xsl:attribute name="font-weight">#PAGE_FOOTER_FONT_WEIGHT#</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="body-font">
        <xsl:attribute name="height">12.0pt</xsl:attribute>
        <xsl:attribute name="font-family">#BODY_FONT_FAMILY#</xsl:attribute>
        <xsl:attribute name="white-space-collapse">false</xsl:attribute>
        <xsl:attribute name="font-size">#BODY_FONT_SIZE#pt</xsl:attribute>
        <xsl:attribute name="font-weight">#BODY_FONT_WEIGHT#</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="page-number">
        <xsl:attribute name="height">13.872pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="header-font">
        <xsl:attribute name="height">#HEADER_FONT_SIZE#pt</xsl:attribute>
        <xsl:attribute name="font-family">#HEADER_FONT_FAMILY#</xsl:attribute>
        <xsl:attribute name="white-space-collapse">false</xsl:attribute>
        <xsl:attribute name="font-size">#HEADER_FONT_SIZE#pt</xsl:attribute>
        <xsl:attribute name="font-weight">#HEADER_FONT_WEIGHT#</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="border">
        <xsl:attribute name="border-top">#BORDER_WIDTH#pt solid #BORDER_COLOR#</xsl:attribute>
        <xsl:attribute name="border-bottom">#BORDER_WIDTH#pt solid #BORDER_COLOR#</xsl:attribute>
        <xsl:attribute name="border-start-width">#BORDER_WIDTH#pt</xsl:attribute>
        <xsl:attribute name="border-start-color">#BORDER_COLOR#</xsl:attribute>
        <xsl:attribute name="border-start-style">solid</xsl:attribute>
        <xsl:attribute name="border-end-width">#BORDER_WIDTH#pt</xsl:attribute>
        <xsl:attribute name="border-end-color">#BORDER_COLOR#</xsl:attribute>
        <xsl:attribute name="border-end-style">solid</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="cell">
        <xsl:attribute name="background-color">#BODY_BG_COLOR#</xsl:attribute>
        <xsl:attribute name="color">#BODY_FONT_COLOR#</xsl:attribute>
        <xsl:attribute name="vertical-align">top</xsl:attribute>
        <xsl:attribute name="padding-top">0.0pt</xsl:attribute>
        <xsl:attribute name="number-columns-spanned">1</xsl:attribute>
        <xsl:attribute name="padding-bottom">0.0pt</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="header-color">
        <xsl:attribute name="background-color">#HEADER_BG_COLOR#</xsl:attribute>
        <xsl:attribute name="color">#HEADER_FONT_COLOR#</xsl:attribute>
    </xsl:attribute-set>
    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="master0" margin-left="10mm" margin-right="10mm" page-height="297mm" page-width="210mm" margin-top="36.0pt" margin-bottom="36.0pt">
                    <fo:region-body region-name="region-body" margin-top="54.0pt" margin-bottom="54.0pt"/>
                    <fo:region-before region-name="region-header" extent="54.0pt"/>
                    
                    <fo:region-after region-name="region-footer" extent="54.0pt" display-align="after"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="master0">
                
                <xsl:variable name="_SECTION_NAME" select="string('master0')"/>
                <fo:static-content flow-name="region-header">
                    <fo:block xsl:use-attribute-sets="text text_2 text_0">
                        <fo:inline xsl:use-attribute-sets="page-header">DNA Inventory Report </fo:inline>
                    </fo:block>
                </fo:static-content>
                <fo:static-content flow-name="region-footer">
                    <fo:block xsl:use-attribute-sets="text footer">
                        <fo:inline xsl:use-attribute-sets="body-font page-number">
                            <fo:page-number/>
                        </fo:inline>
                    </fo:block>
                    
                </fo:static-content>
                <fo:flow flow-name="region-body">
                    <fo:block xsl:use-attribute-sets="padding">
                        <fo:table table-layout="fixed" width="100">
                            <xsl:variable name="_XDOFOPOS2" select="number(1)"/>
                            <xsl:variable name="_XDOFOTOTAL" select="number(1)"/>
                            <fo:table-column column-width="proportional-column-width(1)"/>
                            <fo:table-column column-width="proportional-column-width(1)"/>
                            <fo:table-column column-width="proportional-column-width(1)"/>
                            <fo:table-column column-width="proportional-column-width(1)"/>
                            #PRN_TABLE_CELLS# <fo:table-header>
                                <fo:table-row> #PRN_TEMPLATE_HEADER_ROW# </fo:table-row>
                            </fo:table-header>
                            <fo:table-body>
                                <xsl:for-each select=".//ROW">
                                    <fo:table-row> #PRN_TEMPLATE_BODY_ROW# </fo:table-row>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                    <fo:block xsl:use-attribute-sets="text text_2 text_20">
                        <fo:inline id="{concat('page-total-', $_SECTION_NAME, $_XDOFOPOS)}"/>
                        <fo:inline id="{concat('page-total', $_XDOFOPOS)}"/>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>