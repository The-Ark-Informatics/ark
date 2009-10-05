<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : select_data.xsl
    Created on : August 30, 2005, 11:03 AM
    Author     : renny
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="../biospecimen_menu.xsl"/>
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:param name="templateUpdateChannelURL">templateUpdateChannelURL_false</xsl:param>
    <xsl:param name="templateUpdateChannelTabOrder">templateUpdateChannelTabOrder</xsl:param>
    <xsl:template match="AutoDocs">
    <script language="javascript">
        function updateTextArea(label)
        {
            var txtvalue = document.getElementById(label).value;
            var txtarealabel = "txtarea_"+ label;
            document.getElementById("txtarea_"+ label).value = txtvalue;
        }
        function setTextArea(label,value)
        {
            document.getElementById(label).value = value;
        }
    </script>
    <xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>
    <xsl:variable name="strBiospecID"><xsl:value-of select="Biospecimen/ID"/></xsl:variable>
    <xsl:variable name="intBiospecID"><xsl:value-of select="Biospecimen/IDInternal"/></xsl:variable>
    <xsl:variable name="XMLFile"><xsl:value-of select="document('../../../../../../files/auto_doc_comments/comments.xml')"/></xsl:variable>
    <xsl:variable name="intCommentMaxDispChar">
        <xsl:choose>
            <xsl:when test="count(/intCommentMaxDispChar) &gt; 0">
                <xsl:value-of select="intCommentMaxDispChar"/>
            </xsl:when>
            <xsl:otherwise>90</xsl:otherwise>        
        </xsl:choose>
    </xsl:variable>
    <!--xsl:variable name="intCommentMaxDispChar">30</xsl:variable-->
    
        <html>
            <head>
                <title>Reports</title>
            </head>
            <body>

            <form id="report_form" action="{$baseActionURL}?module=AutoDocs&amp;action=save" method="POST">
                    <table width='100%' cellpadding="0" cellspacing="0">
                        <tr>
                            <td class="uportal-channel-subtitle" colspan='3'>Report Comments</td>
                            <td align="right" width="1%">
                               <table width="100%" cellpadding="0" cellspacing="0">
                                  <tr align="right"><td>
                               <a href="{$baseActionURL}?module=AutoDocs&amp;action=prepare&amp;StartOrder=1&amp;EndOrder=3&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecID}&amp;strDomain=Bioanalysis" method="post">
                                  <img border="0" src="media/neuragenix/buttons/previous_enabled.gif" alt="Previous"/>
                               </a>
                               </td><td>
                               <a href="" method="POST">
                               <img border="0" src="media/neuragenix/buttons/next_disabled.gif" alt="Next"/>
                                  </a>

                               </td>
                           </tr>
                           </table>
                           </td>
                        </tr>
                        <tr>
                            <td colspan='4'><hr/></td>
                        </tr>
                    </table>
                    <table width="100%">
                                    
                        
                      <xsl:choose>
                        <!-- Begin of bioanalysis_view_report_comments-->
                        <xsl:when test="count(/AutoDocs/bioanalysis_view_report_comments) &gt; 0">
                        
                        
                        <xsl:for-each select="$XMLFile/AutoDocComments/Comment">
                        <xsl:variable name="label"><xsl:value-of select="./@label"/></xsl:variable>
                        <tr>
                            <td width="20%">
                                <xsl:value-of select="$label"/>:
                            </td>
                            <xsl:variable name="lblmod"><xsl:value-of select='translate($label," ","_")'/></xsl:variable>
                            <td width="35%">
                                <select tabindex="1" class="uportal-input-text" size='4' cols='20' onchange="javascript:updateTextArea('{$lblmod}');">
                                    <xsl:attribute name="name"><xsl:value-of select="$lblmod"/></xsl:attribute>
                                    <xsl:attribute name="id"><xsl:value-of select="$lblmod"/></xsl:attribute>
                                    <xsl:for-each select="value">
                                                                               
                                        <option>
                                            <xsl:attribute name="value">
                                              <xsl:value-of select="."/>
                                            </xsl:attribute>
                                            <!-- Truncate value to intCommentMaxDispChar cahr - add ... if the value was more than intCommentMaxDispChar-->
                                            <xsl:choose>
                                                <xsl:when test="string-length(.) &gt; $intCommentMaxDispChar">                                                    
                                                    <xsl:value-of select='concat(substring(.,0,number($intCommentMaxDispChar)-3), "...")'/>                                                    
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:value-of select="."/>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                                                                    
                                        </option>
                                    </xsl:for-each>
                                </select>
                            </td>
                            <td width="10%">&gt;</td>
                            <td width="35%">
                            <textarea cols='40' rows='3'>
                                <xsl:attribute name="id">txtarea_<xsl:value-of select="$lblmod"/></xsl:attribute>
                                <xsl:attribute name="name">txtarea_<xsl:value-of select="$lblmod"/></xsl:attribute>
                            </textarea>
                            </td>
                        </tr>
                        </xsl:for-each>
                        <xsl:if test="count(/AutoDocs/SavedValues) &gt; 0">
                            <xsl:for-each select="/AutoDocs/SavedValues/value">
                                <xsl:variable name="label"><xsl:value-of select="./@id"/></xsl:variable>
                                <xsl:variable name="value"><xsl:value-of select="."/></xsl:variable>
                                <script>
                                    setTextArea('<xsl:value-of select="$label"/>','<xsl:value-of select="$value"/>');
                                </script>
                            </xsl:for-each>
                        </xsl:if>
                        <!-- end of bioanalysis_view_report_comments-->
                      </xsl:when>
                      <xsl:otherwise>
                        Predefined comments are not enabled. Proceed by clicking the save button.
                        <xsl:for-each select="$XMLFile/AutoDocComments/Comment">
                            <xsl:variable name="label"><xsl:value-of select="./@label"/></xsl:variable>
                            <xsl:variable name="lblmod"><xsl:value-of select='translate($label," ","_")'/></xsl:variable>
                            <input type="hidden" value="">
                                <xsl:attribute name="id">txtarea_<xsl:value-of select="$lblmod"/></xsl:attribute>
                                <xsl:attribute name="name">txtarea_<xsl:value-of select="$lblmod"/></xsl:attribute>
                            </input>
                        </xsl:for-each>
                      </xsl:otherwise>
                      
                      </xsl:choose>
                        
                        
                        
                        <tr>
                            <td colspan='4' align="right">
                            <input type="submit" value="Save" tabindex="4"
                                        class="uportal-button"/>
                                <xsl:if test="count(/AutoDocs/report_selected) &gt; 0">
                                    <xsl:variable name="reportName"><xsl:value-of select="/AutoDocs/report_selected"/></xsl:variable>
                                    <a href="{$templateUpdateChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$templateUpdateChannelTabOrder}&amp;functionName=HelloWorld&amp;templateName={$reportName}">Generate report</a>
                                </xsl:if>
                            </td>
                        </tr>
                    </table>
            </form>
        </body>
    </html>
</xsl:template>

</xsl:stylesheet>
