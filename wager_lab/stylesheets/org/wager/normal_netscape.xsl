<?xml version="1.0" encoding="utf-8"?>
<!-- normal_netscape.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="no" />
  <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
  <xsl:param name="baseWorkerURL"></xsl:param>
  <xsl:param name="name_prev">world</xsl:param>
  <xsl:param name="report_num">0</xsl:param>
  <xsl:param name="error"></xsl:param>
  <xsl:template match="/">
  	<!--<script language="javascript">
  		window.onload = function() {
  		 onUpdate();
  		}
  		
  		function onUpdate() {
 		var selectBox = document.report_submit.SAMPLETYPE;
                        user_input = selectBox.options[selectBox.selectedIndex].value
 	
  		var selectBox2 = document.report_submit.TREATMENT;

                                                        if (user_input == 'Tissue')
                                                                selectBox2.style.visibility='visible';
                                                        else
                                                                selectBox2.style.visibility='hidden';
  		
  		}
  	</script>-->
	<table width="100%">
                                                                                <tr>
                                                                                        <td class="message_text" width="80%">
                                                                                                <xsl:value-of select="$error"/>
                                                                                        </td>
                                                                                </tr>
                                                                        </table>

    <table width="100%">
	<tr>
		<td valign="top" width="30%" class="uportal-channel-subtitle">Report Menu<hr/>
    <xsl:apply-templates select="/REPORTS" />
		</td>
		<td valign="top" width="70%" class="uportal-channel-subtitle">
	<xsl:if test="$report_num">
	Report Parameters<hr/>
    <xsl:apply-templates select="/REPORTS/REPORT[REPORTKEY=$report_num]" >
	<xsl:with-param name="repkey" select="$report_num"/>
	</xsl:apply-templates>
	</xsl:if>
		</td>
	</tr>
	</table>
  </xsl:template>

  <xsl:template match="REPORTS">
	
	<table>
	 	<xsl:for-each select="REPORT">
		<xsl:variable name="repkeyval"><xsl:value-of select="REPORTKEY"/></xsl:variable>
		<tr>
		<td><b><a href="{$baseActionURL}?report={$repkeyval}"><xsl:value-of select="REPORTNAME"/></a> </b></td><td></td>
		</tr>
		</xsl:for-each>		
	</table>
  </xsl:template>
	
  <xsl:template match="REPORT">
	<xsl:param name="repkey"/>
	<xsl:param name="filename"><xsl:value-of select="FILENAME"/></xsl:param>
	<xsl:param name="logofile"><xsl:value-of select="LOGOFILE"/></xsl:param>
	<form action="{$baseActionURL}" method="post" name="report_submit">
	 <input type="hidden" name="report" value="{$report_num}"/>
	 <input type="hidden" name="filename" value="{$filename}"/>
	 <input type="hidden" name="LOGO_FILENAME" value="{$logofile}"/>
	<table>
	<tr><td class="form_label">Report Name</td><td width="5%"/><td><xsl:value-of select="REPORTNAME"/></td></tr>
		<xsl:for-each select="PARAM">
			
		<xsl:variable name="paramLabel"><xsl:value-of select="PARAMLABEL"/></xsl:variable>
	<xsl:variable name="paramKey"><xsl:value-of select="REPORTPARAMKEY"/></xsl:variable>
	<xsl:variable name="paramName"><xsl:value-of select="PARAMNAME"/></xsl:variable>
			<xsl:variable name="paramValue"><xsl:value-of select="VALUE"/></xsl:variable>
		<tr>
			<td><xsl:choose>
				<xsl:when test="PARAMTYPE='HIDDEN'">
					<input type="hidden" name="${paramName}" value="{$paramValue}"></input>
				</xsl:when>
				<xsl:otherwise><b><xsl:value-of select="$paramLabel"/></b></xsl:otherwise></xsl:choose></td><td width="5%"/>
		<td>
		<xsl:choose>
		 <xsl:when test="PARAMTYPE='TEXT'">
		<input type="text" name="{$paramName}"/>
		</xsl:when>
		<xsl:when test="PARAMTYPE='DATE'">
		<xsl:variable name="year" select="VALUE/YEAR"/>
        <select name="{$paramName}_Day">
          <xsl:for-each select="VALUE/DAY">
                <option>
                <xsl:attribute name="value"><xsl:value-of select="." /></xsl:attribute>
                <xsl:if test="@selected=1">
                <xsl:attribute name="selected"/>
                </xsl:if>
                <xsl:value-of select="." />
                </option>
        </xsl:for-each>
                </select>
	<select name="{$paramName}_Month">
          <xsl:for-each select="VALUE/MONTH">
                <option>
                	<xsl:attribute name="value"><xsl:value-of select="." /></xsl:attribute>
                	<xsl:if test="@selected=1">
                		<xsl:attribute name="selected"/>
                	</xsl:if>
                	<xsl:value-of select="." />
                </option>
        </xsl:for-each>
                </select>
 <input type="text" size="4" name="{$paramName}_Year" value="{$year}"/>
		</xsl:when>
		<xsl:when test="PARAMTYPE='DROPDOWN'">
			<select name="{$paramName}" onclick="javascript:onUpdate()">
        <xsl:for-each select="VALUE">
        <option>
        	<xsl:attribute name="value"><xsl:value-of select="." /></xsl:attribute>
        	<xsl:if test="@selected=1">
        		<xsl:attribute name="selected"/>
        	</xsl:if>
        	<xsl:value-of select="." />
        </option>
        </xsl:for-each>
        </select>
		</xsl:when>
			
			<xsl:when test="PARAMTYPE='STUDY'">
				
				<select name="{$paramName}" onclick="javascript:onUpdate()">
					<xsl:for-each select="VALUE">
						<option>
							<xsl:attribute name="value"><xsl:value-of select="." /></xsl:attribute>
							<xsl:if test="@selected=1">
								<xsl:attribute name="selected"/>
							</xsl:if>
							<xsl:value-of select="." />
						</option>
					</xsl:for-each>
				</select>
			</xsl:when>
		</xsl:choose>
		</td>
		</tr>
		</xsl:for-each>
	</table>
	<input type="submit" name="submit" value="Generate Report"
            class="uportal-button"/>
	<xsl:if test="$baseWorkerURL">
	<input type="button" name="download" value="Download Report" onClick="document.location.href='{$baseWorkerURL}'" /></xsl:if>
	</form>
  </xsl:template>




</xsl:stylesheet>
