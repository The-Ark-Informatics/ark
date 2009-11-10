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
    <xsl:param name="autodocsChannelURL">autodocsChannelURL_false</xsl:param>
    <xsl:template match="AutoDocs">
    <xsl:variable name="infopanelImagePath">media/neuragenix/infopanel</xsl:variable>
    <xsl:variable name="strBiospecID"><xsl:value-of select="Biospecimen/ID"/></xsl:variable>
    <xsl:variable name="intBiospecID"><xsl:value-of select="Biospecimen/IDInternal"/></xsl:variable>
        <html>
            <head>
                <title>Reports</title>
            </head>
            <body>

            <form id="report_form" name="report_form" action="{$baseActionURL}" method="POST">
                    <table width='100%' cellpadding="0" cellspacing="0">
                        <tr>
                            <td class="uportal-channel-subtitle" colspan='4'>BioAnalysis Report</td>
                            <td align="right" width="1%">
                               <table width="100%" cellpadding="0" cellspacing="0">
                                  <tr align="right"><td>
                               <a href="{$baseActionURL}?module=core&amp;action=view_biospecimen&amp;BIOSPECIMEN_intBiospecimenID={$intBiospecID}" method="post">
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
                            <td colspan='3'><hr/></td>
                        </tr>
                    </table>
                    <table width='100%' cellpadding="0" cellspacing="0">
                        <tr>
                            <td class="uportal-label" colspan='2'>
                                <xsl:value-of select="Biospecimen/ID/@label"/> : &#160;&#160;
                            </td>
                            <td colspan='2'>
                                <xsl:value-of select="$strBiospecID"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan='4'></td>
                        </tr>
                        <tr>
                            <td class="uportal-label" colspan='2'> Report : &#160;&#160;&#160;&#160;
                            </td>
                            <td colspan='2'>
                                <select name="report_name" tabindex="1" class="uportal-input-text">
                                    <xsl:for-each select="/AutoDocs/Documents/Document">
                                        <xsl:variable name="externalName"><xsl:value-of select="ExternalName"/></xsl:variable>
                                        <option>
                                            <xsl:attribute name="value">
                                              <xsl:value-of select="$externalName"/>
                                            </xsl:attribute>
                                            <xsl:value-of select="$externalName"/>
                                        </option>
                                    </xsl:for-each>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td colspan='4'></td>
                        </tr>
                        <xsl:if test='count(/AutoDocs/Doctors/Doctor) &gt; 0'>
                        <tr>
                            <td class="uportal-label" valign='center' width="15%"> Referring Doctor : &#160;
                            </td>
                            <td width="20%">
                                <select tabindex="1" name="ref_doctor" class="uportal-input-text" size='4' cols='20' multiple="true">
                                    <xsl:for-each select="/AutoDocs/Doctors/Doctor">
                                    <xsl:variable name="reference">refdoctor_<xsl:value-of select="ID"/></xsl:variable>
                                    <xsl:variable name="doctor_name"><xsl:value-of select="Name"/></xsl:variable>
                                        <option>
                                            <xsl:attribute name="value">
                                              <xsl:value-of select="$reference"/>
                                            </xsl:attribute>
                                            <xsl:value-of select="$doctor_name"/>
                                        </option>
                                    </xsl:for-each>
                                    </select>
                            </td>
                            <td with="5%">
                                            <input type="button" name="add_act_in" value="  &gt;  " 
                                            onclick="javascript:move(document.report_form.ref_doctor,document.report_form.doctor_names)" 
                                            class="uportal-button"/>

                                            <br/>

                                            <input type="button" name="take_act_out" value="  &lt;  " 
                                            onclick="javascript:move(document.report_form.doctor_names,document.report_form.ref_doctor)" 
                                            class="uportal-button"/>

                                            <br/>

                                            <input type="button" name="add_all_act_in" value=" &gt;&gt; " 
                                            onclick="javascript:moveAll(document.report_form.ref_doctor,document.report_form.doctor_names)" 
                                            class="uportal-button"/>

                                            <br/>

                                            <input type="button" name="take_all_act_out" value=" &lt;&lt; " 
                                            onclick="javascript:moveAll(document.report_form.doctor_names,document.report_form.ref_doctor)" 
                                            class="uportal-button"/>
                             </td>
                             <td width="60%">               
                                    <select class="uportal-input-text" multiple="true" size="4" cols='20' name="doctor_names">
                                    </select>
                            </td>
                        </tr>
                        </xsl:if>
                        </table>
                        <table width="100%">
                        <xsl:if test='count(/AutoDocs/Biospecimen/Smartforms) &gt; 0'>
                            <xsl:for-each select="/AutoDocs/Biospecimen/Smartforms">
                                <xsl:variable name="smartformName"><xsl:value-of select="Name"/></xsl:variable>
                                    <xsl:if test='count(Smartform) &gt; 0'>
                                            <tr>
                                                <td class="uportal-channel-subtitle" colspan='7'><xsl:value-of select="$smartformName"/></td>
                                            </tr>
                                            <tr>
                                                <td class="stripped_column_heading"/>                                                                
                                                <td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
                                                <td class="stripped_column_heading">Protein</td>
                                                <td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
                                                <td class="stripped_column_heading">Antibody</td>
                                                <td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
                                                <td class="stripped_column_heading">Result</td>
                                            </tr>
                                            <xsl:for-each select="Smartform">
                                                <xsl:variable name="smartformID"><xsl:value-of select="./@id"/></xsl:variable>
                                                <xsl:variable name="classstyle">
                                                <xsl:choose>
                                                        <xsl:when test="position() mod 2 != 0">
                                                                stripped_light
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                                stripped_dark
                                                        </xsl:otherwise>
                                                </xsl:choose>
                                                </xsl:variable>
                                                <xsl:if test='count(Result)'>
                                                    <tr>
                                                        <td>
                                                            <xsl:attribute name="class"><xsl:value-of select="$classstyle"/></xsl:attribute>
                                                        <input type="checkbox">
                                                            <xsl:attribute name="name"><xsl:value-of select="$smartformID"/></xsl:attribute>
                                                        </input>
                                                        </td>
                                                        <xsl:for-each select="Result">
                                                            <xsl:variable name="DEValue"><xsl:value-of select="."/></xsl:variable>
                                                                <td class="stripped_separator" width="1"><img width="1" height="1" border="0" src="{$infopanelImagePath}/spacer.gif"/></td>
                                                                <td>
                                                                    <xsl:attribute name="class"><xsl:value-of select="$classstyle"/></xsl:attribute>
                                                                    <xsl:value-of select="$DEValue"/>
                                                                </td>
                                                        </xsl:for-each>
                                                    </tr>
                                                </xsl:if>
                                            </xsl:for-each>
                                    <tr>
                                    </tr>
                                    </xsl:if>
                            </xsl:for-each>
                            <br/><br/>
                        </xsl:if>
                        <tr>
                            <td colspan='7' align="right">
                            <input type="hidden" name="module" value="AutoDocs"/>
                            <input type="hidden" name="action" value="next"/>
                            <input type="submit" value="Next" tabindex="4" 
                            onclick="javascript:doSubmit(document.report_form.doctor_names)"
                                        class="uportal-button"/>
                            </td>
                        </tr>
                    </table>
            </form>
        </body>
    </html>
</xsl:template>

</xsl:stylesheet>
