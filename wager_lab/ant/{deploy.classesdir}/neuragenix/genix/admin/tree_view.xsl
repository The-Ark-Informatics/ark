<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" indent="no" />
<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>

<xsl:template match="/">
    <xsl:apply-templates/>
</xsl:template>

<xsl:template match="body">
      <script language="javascript" >

	function confirmDelete(aURL) {
            var confirmAnswer = confirm('Are you sure you want to delete this record?');

            if(confirmAnswer == true){
		window.location=aURL;
            }
	}
        
        function jumpTo(aURL){
            window.location=aURL;
        }
        
        function move(fbox,tbox) {
                var i;
                for(i=0; i &lt; fbox.options.length; i++) {
                    if(fbox.options[i].selected ){
                        if(fbox.options[i].value != "") {
                            var no = new Option();
                            no.value = fbox.options[i].value;
                            no.text = fbox.options[i].text;
                            tbox.options[tbox.options.length] = no;
                            fbox.options[i].value = "";
                            fbox.options[i].text = "";
                            
                        }
                        fbox.options[i].selected=false;
                    }
                }
                
                BumpUp(fbox);
                
                var temp_opts = new Array();
                var temp = new Object();
                for(var i=0; i &lt; tbox.options.length; i++)  {
                    temp_opts[i] = tbox.options[i];
                }
                for(var x=0; x &lt; temp_opts.length-1; x++)  {
                    for(var y=(x+1); y &lt; temp_opts.length; y++)  {
                        if(temp_opts[x].text.toLowerCase() > temp_opts[y].text.toLowerCase())  {
                            temp = temp_opts[x].text;
                            temp_opts[x].text = temp_opts[y].text;
                            temp_opts[y].text = temp;
                            temp = temp_opts[x].value;
                            temp_opts[x].value = temp_opts[y].value;
                            temp_opts[y].value = temp;
                        }
                    }
                }
                for(var i=0; i &lt; tbox.options.length; i++)  {
                    tbox.options[i].value = temp_opts[i].value;
                    tbox.options[i].text = temp_opts[i].text;
                }
            
        }
        
        function BumpUp(box)  {
            for(var i=0; i &lt; box.options.length; i++) {
                if(box.options[i].value == "")  {
                    for(var j=i; j &lt; box.options.length-1; j++)  {
                        box.options[j].value = box.options[j+1].value;
                        box.options[j].text = box.options[j+1].text;
                    }
                    var ln = i;
                    break;
                }
            }
            if(ln &lt; box.options.length)  {
                box.options.length -= 1;
                BumpUp(box);
            }
        }
        function moveAll(fbox,tbox) {
                var i;
               
                for( i=0; i &lt; fbox.options.length; i++) {
                    
                    var no = new Option();
                    no.value = fbox.options[i].value;
                    no.text = fbox.options[i].text;
                    tbox.options[tbox.options.length] = no;
                    fbox.options[i].value = "";
                    fbox.options[i].text = "";
                }
                
                fbox.options.length =0;
                
                var temp_opts = new Array();
                var temp = new Object();
                for(var i=0; i &lt; tbox.options.length; i++)  {
                    temp_opts[i] = tbox.options[i];
                }
                for(var x=0; x &lt; temp_opts.length-1; x++)  {
                    for(var y=(x+1); y &lt; temp_opts.length; y++)  {
                        if(temp_opts[x].text > temp_opts[y].text)  {
                            temp = temp_opts[x].text;
                            temp_opts[x].text = temp_opts[y].text;
                            temp_opts[y].text = temp;
                            temp = temp_opts[x].value;
                            temp_opts[x].value = temp_opts[y].value;
                            temp_opts[y].value = temp;
                        }
                    }
                }
                for(var i=0; i &lt; box.options.length; i++)  {
                    tbox.options[i].value = temp_opts[i].value;
                    tbox.options[i].text = temp_opts[i].text;
                }
            
        }

            
  
  
        function doSubmit(mForm,fBox,tBox){
            var i;
            
            for(i = 0; i &lt; fBox.options.length; i++){
                fBox.options[i].selected = true;
            }
      
            for(i = 0; i &lt; tBox.options.length; i++){
               tBox.options[i].selected = true;
            }
            //mForm.submit();
        }

        
    </script>


    <table width="100%">
        <tr>
            <td width="20%" valign="top">
                <table width="100%">
                    <tr>
                        <td class="uportal-channel-subtitle">
                            User group hierachy
                            <hr/>
                        </td>
                    </tr>
                    <tr>
                        <td class="uportal-text-small">
                        
                        <xsl:apply-templates select="usergroupTree"/>
                        </td>
                    </tr>
                 </table>
           </td>
           
           <td width="80%" valign="top">
                <xsl:apply-templates select="proccess"/>
           </td>
           
       </tr>
     </table>

  
</xsl:template>




<xsl:template match="usergroupTree">
	<table>
		<xsl:apply-templates select="treeNode[parentId='-1']"/>

        </table>	
</xsl:template>

<xsl:template match="treeNode">

<xsl:variable name="treeId"><xsl:value-of select="treeId"/></xsl:variable>
<xsl:variable name="id"><xsl:value-of select="id"/></xsl:variable>
<xsl:variable name="nodeName"><xsl:value-of select="nodeName"/></xsl:variable>
<xsl:variable name="type"><xsl:value-of select="type"/></xsl:variable>
<xsl:variable name="expanded"><xsl:value-of select="expanded"/></xsl:variable>
<xsl:variable name="url"><xsl:value-of select="url"/></xsl:variable>
	<p>
	  <xsl:if test="$type='USERGROUP'">
		<xsl:choose>
			<xsl:when test="$expanded='true'">
				<a href="{$baseActionURL}?treeAction=collapse&amp;id={$id}">
					<img id="I{$treeId}" src="media/neuragenix/icons/minus.gif" border="0"/>
				</a>&#160;
			</xsl:when>
			<xsl:otherwise>
				<a href="{$baseActionURL}?treeAction=expand&amp;id={$id}">
					<img id="I{$treeId}" src="media/neuragenix/icons/plus.gif" border="0"/>
				</a>&#160;
			</xsl:otherwise>
		</xsl:choose>
	  </xsl:if>
             	<xsl:choose>
			<xsl:when test="$type='USERGROUP'">
				<a href="{$baseActionURL}?treeAction=expand&amp;id={$id}&amp;action={$url}&amp;USERDETAILS_strUserName={$nodeName}&amp;USERGROUP_strUsergroupName={$nodeName}&amp;USERGROUP_intUsergroupKey={$id}">
                                    <xsl:value-of select="nodeName"/>
                                </a>
			</xsl:when>
			<xsl:otherwise>
				<a href="{$baseActionURL}?action={$url}&amp;USERDETAILS_strUserName={$nodeName}&amp;USERGROUP_strUsergroupName={$nodeName}">
                                    <xsl:value-of select="nodeName"/>
                                </a>
			</xsl:otherwise>
		</xsl:choose>
		
	
	<xsl:choose>
		<xsl:when test="$expanded='true'">
    		<div id="{$treeId}" style="margin-left: 20px; display: block;"> 
    			<xsl:apply-templates select="../treeNode[parentId=$treeId]"/>

    		</div>
		</xsl:when>
		<xsl:otherwise>
    		<div id="{$treeId}" style="margin-left: 20px; display: none;"> 
    			<xsl:apply-templates select="../treeNode[parentId=$treeId]"/>
    		</div>
		</xsl:otherwise>
	</xsl:choose>
	</p>
</xsl:template>

</xsl:stylesheet>
