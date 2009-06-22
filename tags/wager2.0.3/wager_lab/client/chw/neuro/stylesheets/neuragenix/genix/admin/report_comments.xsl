<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : single_report_comments.xsl
    Created on : November 8, 2005, 1:43 PM
    Author     : S Parappat
    Description: Manage report comments
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="./admin_menu.xsl"/>
    <xsl:output method="html"/>

    <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>

    <xsl:template match="report_comments">
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
        
        // Add a new comment
        function AddComment(label)
        {            
            var i;
            var length = document.getElementById(label).options.length;
            var exists = false;
            for(i=0; i &lt; length; i++) 
            {
                if(document.getElementById(label).options[i].text == document.getElementById("txtarea_"+ label).value )
                {
                    exists = true;
                }
            }    
        
            // if the value does not already exist in the select box add it
            if (exists == false)
            {    
                var newOption = new Option(document.getElementById("txtarea_"+ label).value, document.getElementById("txtarea_"+ label).value);
                document.getElementById(label).options[document.getElementById(label).options.length] = newOption;
            }
        }

        // Update an existing comment
        function UpdateComment(label)
        {            
            var i;
            var length = document.getElementById(label).options.length;
            var updated = false;
            for(i=0; i &lt; length; i++) 
            {
                // Update the selected item from the select box with the new value
                if(document.getElementById(label).options[i].selected == true )
                {
                    document.getElementById(label).options[i].value = document.getElementById("txtarea_"+ label).value;
                    document.getElementById(label).options[i].text = document.getElementById("txtarea_"+ label).value;
                    updated = true;
                }
            }  
            
            // if it's not an item to update, then just add as a new comment
            if (updated == false)
            {    
                alert ('Please select from the list the item to be updated.');
            }
  
        }
        
        // Remove a comment
        function RemoveComment(label)
        {
            var i;
            var length = document.getElementById(label).options.length;
            for(i=0; i &lt; length; i++) 
            {
                // Remove the selected item in the select box
                if(document.getElementById(label).options[i].selected == true )
                {
                    document.getElementById(label).options[i] = null;
                    break;
                }
            }    
        }
        
        // select all 
        function selectAll (label)
        {
           var list = document.getElementById(label);
           var i = 0;
           while(i &lt; list.length)
           {
              list.options[i].selected = true;
              i++;
           }
           
      
        }            
    </script>
        
        <html>
            <head>

            </head>
            <body>

            <form name="report_comment_form" action="{$baseActionURL}?current=report_comments&amp;action=saveSingleComment" method="POST">                    
                
                <table width="100%">
			<tr>
				<td class="uportal-channel-subtitle">
                                  Manage Report Comments
                                  <hr/>
				</td>
			</tr>
                </table>
                
                <table width="100%">
			<tr>
				<td class="neuragenix-form-required-text">
                                    <xsl:value-of select="error"/>                                  
				</td>
			</tr>
                </table>                
                <table width="100%">
                    <tr>
                        <td width="10%" align="left">
                            Report:
                        </td>

                        <td width="90%" align="left">
                            <xsl:variable name="SelectedReport"><xsl:value-of select="SelectedReport"/></xsl:variable>
                            <select name="Report" tabindex="1" class="uportal-input-text" onchange="document.report_comment_form.submit()" >
                                <xsl:for-each select="report">
                                <xsl:variable name="file_name">
                                    <xsl:value-of select="file_name"/>
                                </xsl:variable>
                                    <option>
                                        <xsl:if test="$SelectedReport = $file_name">
                                          <xsl:attribute name="selected">true</xsl:attribute>
                                        </xsl:if>
                                        <xsl:attribute name="value">
                                          <xsl:value-of select="file_name"/>
                                        </xsl:attribute>
                                        <xsl:value-of select="external_name"/>
                                    </option>
                                </xsl:for-each>
                            </select>
                        </td>
                        
                    </tr>

                </table>
                            
                <table width="100%">
                    <tr>
                        <td width="10%" align="left">
                            Comment Type:
                        </td>

                        <td width="90%" align="left">
                            <select name="CommentType" tabindex="25" onchange="document.report_comment_form.submit()">
                                    <xsl:for-each select="CommentType">
                                            <option>
                                                    <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                                    <xsl:if test="@selected = '1'">
                                                            <xsl:attribute name="selected">true</xsl:attribute>
                                                    </xsl:if>
                                                    <xsl:value-of select="."/>
                                            </option>
                                    </xsl:for-each>
                            </select>                            
                        </td>
                                                
                    </tr>
                    
                    <tr>
                        <td width="10%" align="left">
                            Result Type:
                        </td>

                        <td width="90%" align="left">
                            <select name="ResultType" tabindex="25" onchange="document.report_comment_form.submit()">
                                <xsl:for-each select="ResultType">
                                    <option>
                                        <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                                        <xsl:if test="@selected = '1'">
                                                <xsl:attribute name="selected">true</xsl:attribute>
                                        </xsl:if>
                                        <xsl:value-of select="."/>
                                    </option>
                                </xsl:for-each>
                            </select>                            
                        </td>
                        

                    </tr>                    
                    
                    <tr>
                        <td width="10%" align="left">
                            Comment:
                        </td>

                        <td width="90%" align="left" valign="bottom">
                            <textarea name="Comment" tabindex="4" rows="5" cols="70" class="uportal-input-text" >
                                <xsl:value-of select="Comment"/>
                            </textarea>
                            &#160;
                            <input type="submit" name="UpdateComment" value="Update" tabindex="5" class="uportal-button" />                            
                        </td>
                                                
                    </tr>                    

                </table>
                <br/>
            </form>
        </body>
    </html>
    
    <xsl:variable name="strBiospecID"><xsl:value-of select="Biospecimen/ID"/></xsl:variable>
    <xsl:variable name="intBiospecID"><xsl:value-of select="Biospecimen/IDInternal"/></xsl:variable>
    <xsl:variable name="XMLFile"><xsl:value-of select="document('../../../../../files/auto_doc_comments/comments.xml')"/></xsl:variable>
    
        <html>

            <body>

            <form name="report_form" action="{$baseActionURL}?current=report_comments&amp;action=saveComments" method="POST">                    
                
                            
                <table width="100%">
                        
                        <hr/>
                        <br/>
                        <xsl:for-each select="$XMLFile/AutoDocComments/Comment">
                        <xsl:variable name="label"><xsl:value-of select="./@label"/></xsl:variable>
                        <xsl:variable name="id"><xsl:value-of select="./@ID"/></xsl:variable>
                        <xsl:variable name="lblmod"><xsl:value-of select='translate($id," ","_")'/></xsl:variable>
                        <tr>
                            <td width="20%">
                                <xsl:value-of select="$label"/>:
                            </td>
                            
                            <td width="35%">
                                <select multiple="true" tabindex="1" class="uportal-input-text" size='4' cols='20' onchange="javascript:updateTextArea('{$lblmod}');">
                                    <xsl:attribute name="name"><xsl:value-of select="$lblmod"/></xsl:attribute>
                                    <xsl:attribute name="id"><xsl:value-of select="$lblmod"/></xsl:attribute>
                                    <xsl:for-each select="value">
                                        <option>
                                            <xsl:attribute name="value">
                                              <xsl:value-of select="."/>
                                            </xsl:attribute>
                                            <xsl:value-of select="."/>
                                        </option>
                                    </xsl:for-each>
                                </select>
                            </td>
                            <td width="10%">&gt;</td>
                            <td width="35%">
                            <textarea tabindex="2" cols='40' rows='3'>
                                <xsl:attribute name="id">txtarea_<xsl:value-of select="$lblmod"/></xsl:attribute>
                                <xsl:attribute name="name">txtarea_<xsl:value-of select="$lblmod"/></xsl:attribute>
                            </textarea>
                            &#160;&#160;                             
                            <a href="javascript:AddComment('{$lblmod}');">Add</a>
                            &#160;
                            <a href="javascript:UpdateComment('{$lblmod}');">Update</a>
                            &#160;
                            <a href="javascript:RemoveComment('{$lblmod}');">Remove</a>
                            </td>
                        </tr>
                        </xsl:for-each>
                        
                    </table>
                    <br/>
                    <table width="100%">
                        
                        <script>
                            function SubmitComments ()
                            {
                                <xsl:for-each select="$XMLFile/AutoDocComments/Comment">   
                                    <xsl:variable name="label"><xsl:value-of select="./@label"/></xsl:variable>
                                    <xsl:variable name="id"><xsl:value-of select="./@ID"/></xsl:variable>
                                    <xsl:variable name="lblmod"><xsl:value-of select='translate($id," ","_")'/></xsl:variable>        
                                    selectAll('<xsl:value-of select='$lblmod'/>');
                                </xsl:for-each>
                                document.report_form.submit();
                            }
                        </script>
                        <input type="button" name="SaveComments" value="Save" tabindex="8" class="uportal-button" onClick="javascript:SubmitComments()"/>
                        
                    </table>
            </form>
        </body>
    </html>
    
</xsl:template>

</xsl:stylesheet>
