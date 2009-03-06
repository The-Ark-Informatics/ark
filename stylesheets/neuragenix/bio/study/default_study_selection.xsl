<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:include href="./study_menu.xsl"/>
	<xsl:include href="../../common/common_btn_name.xsl"/>
	<xsl:output method="html" indent="no"/>
	<xsl:param name="biospecimenChannelURL">biospecimenChannelURL_false</xsl:param>
	<xsl:variable name="buttonImagePath">media/neuragenix/buttons</xsl:variable>
	<xsl:template match="defaultStudy">
	<table cellpadding="0" cellspacing="0" width="100%" height="100%" border="0" valign="top">
				<tr valign="top">
                                
					<td valign="top">
				<table width="100%" valign="top">
							<tr>
								<td class="uportal-channel-subtitle">Study Settings</td>
								<td align="right">
									<!-- IExplorer Hack: since no submit attributes are passed, set "save" parameter -->
                           <!-- <input type="hidden" name="back" value="not_null"/> -->
                           <!-- Back and Next Buttons -->
                           <form name="back_form" method="Post" action="{$baseActionURL}?">
                              
                           </form>   
									<img class="submit_image_button" src="{$buttonImagePath}/previous_enabled.gif" name="back" value="{$backBtnLabel}" alt="Previous"
                              tabindex="40" onblur="javascript:document.study_view.strStudyName.focus()"
                              onclick="javascript:document.back_form.submit();"
                              />
									<img src="{$buttonImagePath}/next_disabled.gif" border="0" alt="Next"/>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<hr/>
								</td>
							</tr>
						</table>		
                                                
                                                <form name="defaultStudySelection" method="post" action="{$baseActionURL}?module=defaultStudySelection&amp;action=updateDefaultStudy">
                                                <table width="100%" valign="top">
  
                                                <tr>
                                                    <td colspan="2" class="uportal-label">
                                                        Current default study: <i><xsl:value-of select="selectedStudy" /></i>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" class="uportal-label">
                                                        <br/>Please select default study:
                                                    </td>
                                                </tr>    
                                               
                                                <xsl:for-each select="study_list">
                                                    <tr>
                                                        <td width="10%" />
                                                        <td>
                                                            <input type="radio" name="defaultStudy" value="{STUDY_strStudyName}">
                                                                <xsl:if test="../selectedStudy=STUDY_strStudyName">
                                                                    <xsl:attribute name="checked">checked</xsl:attribute>
                                                                </xsl:if>
                                                                <xsl:value-of select="STUDY_strStudyName" />
                                                            </input>
                                                            
                                                        </td>
                                                    </tr>
                                                </xsl:for-each>       
                                                
                                                <tr>
                                                   <td colspan="2">
                                                   <br />
                                                        <input type="submit" value="Update Study" />
                                                   </td>
                                                   
                                                </tr>
                                                   
                                                <tr>
                                                   <td colspan="2">
                                                      <br />
                                                      <br />
                                                      <br />
                                                      Please note:  Changing the default study will also effect auto-consenting
                                                   </td>
                                                </tr>
                                                
                                                
                                                
                                                </table>
                                                </form>
                                    </td>
                                </tr>
                            </table>
                         
	
	</xsl:template>
</xsl:stylesheet>
