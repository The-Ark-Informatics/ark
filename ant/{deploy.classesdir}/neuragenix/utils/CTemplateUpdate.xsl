<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="../common/common_btn_name.xsl"/>
  <xsl:output method="html" indent="no" />
  <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
 <xsl:param name="downloadURL">downloadURL_false</xsl:param>     
 <xsl:param name="nodeId">nodeId_false</xsl:param>
  <xsl:param name="docURL"><xsl:value-of select="template/link"/></xsl:param>
  <xsl:param name="templateURL"><xsl:value-of select="template/template"/></xsl:param>
  <xsl:param name="intDocTemplateID"><xsl:value-of select="template/intDocTemplateID"/></xsl:param>
<xsl:template match="/">  

<!--  
    @author rennypv
    <a name="Template Update" />-->


 <a name="frm1_anchor"/>
 <xsl:apply-templates />
        <xsl:choose>
       <xsl:when test="(template/link = 'No Documents')">
       <table>
            <xsl:if test="template/error">
            <tr>
                <td colspan='2' class="neuragenix-form-required-text">
                    <xsl:value-of select="template/error"/>
                </td>
            </tr>
            <tr>
            <td/>
            </tr>
            </xsl:if>
            <tr>
                <td colspan='2' class="uportal-label">
                    No Documents Loaded
                </td>
            </tr>
        </table>
       </xsl:when>
       <xsl:otherwise>
            <!--table width = "100%"-->
             <xsl:if test='count(template/params) &gt; 0'>
                  <table >  
                    <tr>
                        <td class="uportal-channel-table-header">Add Manual Parameters</td>
                    </tr>
                    <tr>
                        <td class="uportal-text">
                            This section allows you to add new parameters to the document that have not been auto populated
                            (view original template above). For example, if &lt;FREE_TEXT/&gt; appears in the template add the new parameter
                            FREE_TEXT with your comments in the value box and add and update the document. Once completed view the template above.
                        </td>
                    </tr>
                    </table>
            <table border='0' cellpadding='0' cellspacing='0'>            
                    <tr>
                            <td height= "10px"></td>
                    </tr>
<!--
    this bit is to display an error if the same parameter is added twice
-->                    
                    <xsl:if test="starts-with(template/param_exists,'Error')">
                    <tr>
                        <td colspan='2' class="neuragenix-form-required-text">
                            <xsl:value-of select="template/param_exists"/>
                        </td>
                    </tr>
                    </xsl:if>
                    </table>
                    <table width='100%'>
                     <tr>
                        <td width="45%" class="uportal-label">Parameter </td>
                        <!--td width="5%"/-->
                        <td width="55%" class="uportal-label">Value</td>
                        <!--td width="5%"/-->
                         <!--td width="5%"/>
                          <td width="5%"/-->
                     </tr>
<!--
    This bit to to display all the parameter and the values added through this channel
    Also includes buttons to edit and delete the parameters and values
-->                     
                      <form name="frm1" action="{$baseActionURL}?uP_root=root#frm1_anchor" method="post">
                      <script language="javascript" >
                          function jumpTo(aURL){
                            window.location=aURL;
                            }
                        </script>
                      <xsl:for-each select="/template/params">
                          <xsl:variable name="p_name"><xsl:value-of select="p_name"/></xsl:variable>
                           <xsl:variable name="p_value"><xsl:value-of select="p_value"/></xsl:variable>
                           <input type="hidden" name="paramname" value="{$p_name}"/>
                        <xsl:choose>

                        <xsl:when test="position() mod 2 != 0">                                        

                        <tr class="uportal-background-light" >
                            <td width="45%" class="uportal-label">
                            <input type="hidden" name="param_{$p_name}" value="{$p_name}"/>
                            <!--input type="text" size='40' name="param_{$p_name}"  class="uportal-input-text" readOnly="true" value="{$p_name}" /-->
                            <xsl:value-of select="p_name"/>
                            </td>
                                        <!--td width="5%"/-->
                            <td width="55%"><TEXTAREA name="value_{$p_name}" ROWS='2' COLS='30' class="uportal-input-text"><xsl:value-of select="p_value"/></TEXTAREA></td>
                            <!--td width="5%" class="uportal-label"><input type="submit" name="editparam" value="{$editBtnLabel}" class="uportal-button"/>
                            </td-->
                             <!--td width="5%" class="uportal-label"-->
                             <!--input type="submit" name="deleteparam" value="{$deleteBtnLabel}" class="uportal-button" /-->
                             <!--input type="button" value="{$deleteBtnLabel}" tabindex="42" class="uportal-button" onclick="javascript:jumpTo('{$baseActionURL}?uP_root=root&amp;deleteparam=true&amp;paramname={$p_name}#frm1_anchor')"/-->
                             <!--/td>
                             <td width="5%"/-->
                        </tr>
                    </xsl:when>
                    <xsl:otherwise>
                        <tr>
                            <td width="45%" class="uportal-label">
                            <input type="hidden" name="param_{$p_name}" value="{$p_name}"/>
                            <!--input type="text" size='40' name="param_{$p_name}"  class="uportal-input-text" readOnly="true" value="{$p_name}" /-->
                            <xsl:value-of select="p_name"/>
                            </td>
                                        <!--td width="5%"/-->
                            <td width="55%"><TEXTAREA name="value_{$p_name}" ROWS='2' COLS='30' class="uportal-input-text"><xsl:value-of select="p_value"/></TEXTAREA></td>
                            <!--td width="5%" class="uportal-label"><input type="submit" name="editparam" value="{$editBtnLabel}" class="uportal-button"/>
                            </td-->
                             <!--td width="5%" class="uportal-label"-->
                             <!--input type="submit" name="deleteparam" value="{$deleteBtnLabel}" class="uportal-button" /-->
                             <!--input type="button" value="{$deleteBtnLabel}" tabindex="42" class="uportal-button" onclick="javascript:jumpTo('{$baseActionURL}?uP_root=root&amp;deleteparam=true&amp;paramname={$p_name}#frm1_anchor')"/-->
                             <!--/td>
                             <td width="5%"/-->
                        </tr>
                    </xsl:otherwise>
                    </xsl:choose>
                      </xsl:for-each>
                        <tr>
                            <td  width="45%" height= "10px"></td>
                            <!--td width="5%"/-->
                            <td width="55%"/>
                            <!--td width="5%"/-->
                            <!--td width="5%"/>
                            <td width="5%"/-->
                        </tr>
<!--
    This is the link to update the document with the new parameters set through this channel
-->                        
                       <tr>
                            <td width="45%" class="neuragenix-form-required-text">
                            <input type="hidden" name="filename" value="{$docURL}"/>
                            <input type="submit" name="updatedoc" value="{$updatedocBtnLabel}" class="uportal-button" />
                            </td>                                                
                            <!--td width="5%"/-->
                            <td width="55%" class="uportal-label">
                            </td>
                            <!--td width="5%"/-->
                            <!--td width="5%"/>
                            <td width="5%"/-->
                       </tr>
                       <tr>
                       <td colspan='2'>
                       <hr/>
                       </td>
                       </tr>
                       </form>                       
                    </table>
                    </xsl:if>                    
                    <!--form name="frm2" action="{$baseActionURL}?uP_root=root#frm1_anchor" method="post">
                    <table >
                    <tr>
                            <td height= "10px" width="10%"></td>
                            <td width="80%"/>
                        <td width="10%"/>
                    
                    </tr>
                    <tr>
                            <td height= "10px"></td>
                    </tr>
                   </table>

                </form-->
            <form name="frm3" action="{$baseActionURL}?uP_root=root#frm1_anchor" method="post">            
           <xsl:variable name="defaultfilename"><xsl:value-of select="template/defaultFileName"/></xsl:variable>
           <xsl:variable name="title"><xsl:value-of select="template/title"/></xsl:variable>
           <xsl:variable name="keywords"><xsl:value-of select="template/keywords"/></xsl:variable>
           <xsl:variable name="description"><xsl:value-of select="template/description"/></xsl:variable>
           
            <table>
            <xsl:if test='template/copied'>
            <tr>
                <td class="neuragenix-form-required-text"><xsl:value-of select='template/copied'/></td>
            </tr>
            </xsl:if>
            <tr>
                <td  class="uportal-channel-table-header"> Add file to Repository</td>
            </tr>
            <tr>
                <td class="uportal-text">
                    Once you have completed updating the document, please attach it to the case by completing the details below
                    and clicking on "Add" button. Once complete, please click the "completed" button
                </td>
            </tr>
            </table>
            <table  width="100%">
            <tr>
                <td class="uportal-label" width="40%">New File Name</td>
                 <td width="5%"/>
                <td width="40%" class="neuragenix-form-required-text">
                            <input type="text" size='32' name="newfilename" align='top' class="uportal-input-text" value="{$defaultfilename}" />
                </td>
                 <td width="5%"/>
                  <td width="5%"/>
                   <td width="5%"/>
            </tr>
            <tr>
                <td class="uportal-label" width="40%">Title</td>
                 <td width="5%"/>
                <td width="40%" class="neuragenix-form-required-text">
                            <input type="text" size='32' name="title" align='top' class="uportal-input-text" value="{$title}" />
                </td>
                 <td width="5%"/>
                  <td width="5%"/>
                   <td width="5%"/>
            </tr>
            <tr>
                <td class="uportal-label" width="40%">Keywords</td> 
                 <td width="5%"/>               
                <td width="40%">
                            <TEXTAREA NAME="keywords" ROWS='2' COLS='30' class="uportal-input-text"><xsl:value-of select="template/keywords"/></TEXTAREA>
                </td>
                 <td width="5%"/>
                  <td width="5%"/>
                   <td width="5%"/>
            </tr>
            <tr>
                <td class="uportal-label" width="40%">Description</td>                
                 <td width="5%"/>
                <td width="40">
                            <TEXTAREA NAME="description" ROWS='2' COLS='30' class="uportal-input-text" > <xsl:value-of select="template/description"/></TEXTAREA>
                </td>
                 <td width="5%"/>
                  <td width="5%"/>
                   <td width="5%"/>
            </tr>
            <tr>
                 <td width="40%"/>
                  <td width="5%"/>
                <td colspan='2' class="uportal-label" width="40%">
                   <input type="hidden" name="filename" value="{$docURL}"/> 
                            <input type="submit" name="addfile" value="{$addBtnLabel}" class="uportal-button" />
                </td>
                 <td width="5%"/>
                  <td width="5%"/>
                   <td width="5%"/>
            </tr>
            </table>
            </form>
            <form name="frm4" action="{$baseActionURL}?uP_root=root#frm1_anchor" method="post">
           
                <table>
                <tr>
                    <td class="uportal-label" width="5%">
                           <input type="submit" name="finished" value="Completed" class="uportal-button" />
                </td>
                </tr>
                </table>
            </form>
        </xsl:otherwise>
    </xsl:choose>
   </xsl:template>
<!--
    This first checks for if any documents are saved and if yes gives an option to download the document
-->   
     <xsl:template name="fileDetails" match="template">
   <xsl:if test="not(link = 'No Documents')">
        <table cellspacing='1' cellpadding='5' width='100%' border='0'>
        <tr>
            <td class="uportal-text">
            This module allows you to auto generate the template you have selected.
            </td>
        </tr>
        <tr>
            
            <!--td class="uportal-channel-table-header">Download the file <a href="./files/store_document/{$docURL}">here</a></td-->
            <td class="uportal-channel-table-header" width='50%' align='left'>
            <ul>
            <li/><a href="{$downloadURL}?uP_root={$nodeId}&amp;file_name={$docURL}&amp;property_name=neuragenix.genix.templates.StoreDocumentLocation&amp;activity_required=auto_template" target="_blank">Click here to download the current document state </a>
           
             <li/><a href="{$downloadURL}?uP_root={$nodeId}&amp;domain=DOCUMENTTEMPLATE&amp;primary_field=DOCUMENTTEMPLATE_intDocTemplateID&amp;primary_value={$intDocTemplateID} &amp;file_name_field=DOCUMENTTEMPLATE_strFileName&amp;property_name=neuragenix.genix.templates.TemplateLocation&amp;activity_required=generate_autodocs" target="_blank">Click here to view the original template </a>
             </ul>
             <hr/>
             </td>
        </tr>
        
        </table>
     </xsl:if>
    </xsl:template>
    

<!--   <xsl:template name="fileDetails" match="template">
   <xsl:if test="not(link = 'No Documents')">
              <table cellspacing='1'>
        <tr>
            <td>File</td><td><xsl:value-of select="link"/></td>
        </tr>
        <tr>
            <td>Author:</td><td><xsl:value-of select="author"/></td>
        </tr>
        </table>
     </xsl:if>   
    </xsl:template>
-->
<!--  <xsl:template match="name">
    <p>Hello <xsl:value-of select="." />!</p>
  </xsl:template> -->
</xsl:stylesheet>