<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./inventory_menu.xsl"/>

    <xsl:param name="formParams">current=add_tray</xsl:param>
    
    <xsl:output method="html" indent="no" />
    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
    <xsl:param name="biospecimenChannelURL">biospecimenChannelURL_false</xsl:param>
    <xsl:param name="biospecimenTabOrder">biospecimenTabOrder</xsl:param>
  
    
    <xsl:template match="inventory">
      
    <xsl:param name="blBackToVialCalc"><xsl:value-of select="blBackToVialCalc" /></xsl:param>
        <link href="htmlarea/dijit/themes/wager/wager.css" rel="stylesheet" type="text/css" />
        <link href="htmlarea/dojo/resources/dojo.css" rel="stylesheet" type="text/css" />
        <script src="htmlarea/dojo/dojo.js" type="text/javascript" djConfig="parseOnLoad: true" />
        <script src="htmlarea/dijit/dijit.js" type="text/javascript" djConfig="parseOnLoad: true" />
        <script type="text/javascript">
            dojo.require("dijit.form.FilteringSelect");
            dojo.require("dijit._Widget");
            dojo.require("dojo.parser");  // scan page for widgets and instantiate them
        </script>
        <script src="htmlarea/add_tray.js" type="text/javascript"/>
    <table width="100%">
        <tr valign="top">
            <td width="30%">
                <table width="250" cellspacing="0" cellpadding="0">
                    <xsl:call-template name="sort_selection"/>   
                </table>


                <table width="250" cellspacing="0" cellpadding="0">
                    <xsl:apply-templates select="site">
                        <xsl:sort select="availbility" data-type="number" order="descending"/>
                    </xsl:apply-templates>
                    <tr class="funcpanel_content">
                        <td colspan="11" class="funcpanel_bottom_border">
                            <img src="media/neuragenix/infopanel/spacer.gif" height="4"
                                width="1"/>
                        </td>
                    </tr>
                </table>
                <br/>
                <br/>
                <br/>
                
                <xsl:variable name="varAdminSection">
                    <xsl:value-of select="intAdminSection"/>
                </xsl:variable>
                <xsl:variable name="accesstoexpandedsite">
                    <xsl:value-of select="site[site_expanded='true']/siteAccess"/>
                </xsl:variable>
                <xsl:if test="$varAdminSection=1 and $accesstoexpandedsite=1">
                    <xsl:call-template name="inventory_admin"/>
                </xsl:if>

                
                
            
                
            </td>
            
            <td width="70%">
                
    <form action="{$baseActionURL}?{$formParams}" method="post">
                <table width="100%">
                    <tr>
                        <td colspan="5" class="uportal-channel-subtitle">
                            Add New <xsl:value-of select="TRAY_strTitleDisplay" /> / Plate
                        </td>
                        <td align='right'>
                        <xsl:if test="$blBackToVialCalc = 'true'">
                            <input type="button" name="back_to_vial_calc" tabindex="10" value="Back to vial calc" class="uportal-button" onclick="javascript:jumpTo('{$biospecimenChannelURL}?uP_root=root&amp;uP_sparam=activeTab&amp;activeTab={$biospecimenTabOrder}&amp;current=create_sub_specimen&amp;back=true')" />
                        </xsl:if>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6">
                            <hr/>
                        </td>
                    </tr>
                </table>
                
                <table width="100%">
                    <tr>
                        <td class="neuragenix-form-required-text">
                            <xsl:value-of select="strErrorMessage" />
                        </td>
                    </tr>
                </table>
                
                <table width="100%">
                    <tr><td width="1%" class="neuragenix-form-required-text"> </td>
                        <td width="19%">
                            <label for="TRAY_intBoxID">Type</label>
                       
                        </td>
                        <td width="25%">
                            <select name="TRAY_intTrayType" 
                                id="typeSelect" tabindex="1" 
                                autoComplete="false" >
                                                                   <option value="0">Box</option>
                                                                    <option value="1">Plate</option>
                            </select>
                        </td>
                        <td width="10%"></td><td width="1%" class="neuragenix-form-required-text"></td>
                        
                    </tr>
                    
                    
                    <tr><td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="19%" class="uportal-label">
                            <label for="TRAY_intBoxID">Location:</label>
                        </td>
                        <td width="25%">
                            <select name="TRAY_intBoxID" id="TRAY_intBoxID"  autoComplete="false"  class="uportal-input-text">
				<xsl:for-each select="search_box">
                                    
                                    <option>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="BOX_intBoxID" />
                                        </xsl:attribute>
                                        
                                        <xsl:value-of select="SITE_strSiteName" /> &gt; <xsl:value-of select="TANK_strTankName" /> &gt;  <xsl:value-of select="BOX_strBoxName" />
                                    </option>
				</xsl:for-each>
                            </select>
                        </td>
                        <td width="10%"></td><td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="TRAY_intNoOfColDisplay" />:
                        </td>
                        <td width="25%">
                            <input type="text" style="text-align: right" name="TRAY_intNoOfCol" size="10" tabindex="3" class="uportal-input-text" />
                            
                            <select name="TRAY_strColNoType" tabindex="4" class="uportal-input-text">
				<xsl:for-each select="TRAY_strColNoType">
		
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="." />
                                        </xsl:attribute> 
                                        <xsl:if test="@selected=1">
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>

                                        <xsl:value-of select="." />		
                                    </option>
				</xsl:for-each>
                            </select>
                        </td>
                    </tr>
                    
                    <tr><td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="TRAY_strTitleDisplay" /> name:
                        </td>
                        <td width="25%">
                            <input type="text" name="TRAY_strTrayName" required="true" id="TRAY_strTrayName"   size="22" tabindex="2" class="uportal-input-text" />
                            
                        </td>
                        <td width="10%"></td><td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="19%" class="uportal-label">
                            <xsl:value-of select="TRAY_intNoOfRowDisplay" />:
                        </td>
                        <td width="25%">
                            <input type="text" style="text-align: right" name="TRAY_intNoOfRow" size="10" tabindex="5" class="uportal-input-text" />
                            
                            <select name="TRAY_strRowNoType" class="uportal-input-text">
                                <xsl:for-each select="TRAY_strRowNoType">
                                    <option>
                                        <xsl:attribute name="value">
                                        <xsl:value-of select="." />
                                        </xsl:attribute> 
                                        <xsl:if test="@selected=1">
                                                <xsl:attribute name="selected">true</xsl:attribute> 
                                        </xsl:if>

                                        <xsl:value-of select="." />		
                                    </option>
				</xsl:for-each>
                            </select>
                        </td>
                        
                    </tr>
                    <tr><td width="1%" class="neuragenix-form-required-text"></td>
                        <td width="19%" class="uportal-label">
                            <label for="studySelect">Study: </label>
                        </td>
                        <td width="25%">
                            <select name="TRAY_intStudyKey" 
                                id="studySelect" 
                              >
                             
                                <xsl:for-each select="study_list">
                                    <option>
                                        <xsl:attribute name="value"><xsl:value-of select="STUDY_intStudyID"/></xsl:attribute>
                                        <xsl:if test="@selected = '1'">
                                            <xsl:attribute name="selected">true</xsl:attribute>
                                        </xsl:if>
                                        <xsl:value-of select="STUDY_strStudyName"/>
                                    </option>
                                </xsl:for-each>
                            </select>
                        </td>
                        <td width="10%"></td>
                        
                    </tr>
                    <tr>
                        <td colspan="7"><hr /></td>
                    </tr>
                </table>
                
                <table width="100%">
                    <tr>	
                        <td width="20%">
                            <input type="submit" name="save" tabindex="7" value="Save" class="uportal-button" />
                            <input type="button" name="clear" value="Clear" tabindex="8" class="uportal-button" onclick="javascript:confirmClear('{$baseActionURL}?current=add_tray')" />
                        </td>
                        <td width="80%"></td>
                    </tr>
                </table>
                
    </form>
            </td>
        </tr>
    </table>
    
    <xsl:if test="$blBackToVialCalc = 'true'">
        <input type="hidden" name="vial_calc" value="true" />
    </xsl:if>
    
    </xsl:template>

</xsl:stylesheet>
