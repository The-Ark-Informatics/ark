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
      
           // for(i = 0; i &lt; tBox.options.length; i++){
             //   fBox.options[i].selected = true;
          //  }
            //mForm.submit();
        }

        
    </script>


    <table width="100%">
        <tr>
            <td width="20%" valign="top">
                <table width="100%">
                    <tr>
                        <td class="uportal-channel-subtitle">
                            Organization Chart.
                            <hr/>
                        </td>
                    </tr>
                    <tr>
                        <td class="uportal-text-small">
                        
                        <xsl:apply-templates select="orgTree"/>
                        </td>
                    </tr>
                 </table>
           </td>
           
           <td width="80%" valign="top">
                <xsl:apply-templates select="info"/>
           </td>
           
       </tr>
     </table>

  
</xsl:template>




<xsl:template match="orgTree">
	<table>
		<xsl:apply-templates select="treeNode[ORGGROUPTREE_intParentGroupKey='-1']"/>

        </table>	
</xsl:template>

<xsl:template match="treeNode">
	<p>
	    <xsl:variable name="cid"><xsl:value-of select="ORGGROUPTREE_intOrgGroupKey"/></xsl:variable>
	    <xsl:variable name="expanded"><xsl:value-of select="expanded"/></xsl:variable>
	    <xsl:variable name="url_action"><xsl:value-of select="url_action"/></xsl:variable>
	    <xsl:if test="$url_action='group'">






		<xsl:choose>
			<xsl:when test="$expanded='true'">

				<a href="{$baseActionURL}?treeAction=collapse&amp;ORGGROUP_intOrgGroupKey={$cid}">
					<img  src="media/neuragenix/icons/minus.gif" border="0"/>
				</a>&#160;
			</xsl:when>
			<xsl:otherwise>
				<a href="{$baseActionURL}?treeAction=expand&amp;ORGGROUP_intOrgGroupKey={$cid}">
					<img  src="media/neuragenix/icons/plus.gif" border="0"/>
				</a>&#160;
			</xsl:otherwise>
		</xsl:choose>
	  </xsl:if>
             	<xsl:choose>
			<xsl:when test="$url_action='group'">


			    <xsl:variable name="id"><xsl:value-of select="ORGGROUPTREE_intOrgGroupKey"/></xsl:variable>

				<a href="{$baseActionURL}?treeAction=expand&amp;ORGGROUP_intOrgGroupKey={$id}&amp;action={$url_action}">
			    	<xsl:value-of select="ORGGROUPTREE_strOrgGroupName"/>
				</a>	
			</xsl:when>
			<xsl:otherwise>
			
			    <xsl:variable name="id"><xsl:value-of select="ORGUSERTREE_intOrgUserKey"/></xsl:variable>


				<a href="{$baseActionURL}?action={$url_action}&amp;ORGUSER_intOrgUserKey={$id}&amp;action={$url_action}">
                                    <xsl:value-of select="nodeName"/>
			    <xsl:value-of select="ORGUSERTREE_strFirstName"/>&#160; <xsl:value-of select="ORGUSERTREE_strLastName"/></a>
			</xsl:otherwise>
		</xsl:choose>
		


	<xsl:choose>




		<xsl:when test="$expanded='true'">

    		<div style="margin-left: 20px; display: block;"> 
    			<xsl:apply-templates select="../treeNode[ORGGROUPTREE_intParentGroupKey=$cid]"/>

    		</div>
		</xsl:when>
		<xsl:otherwise>
    		<div style="margin-left: 20px; display: none;"> 
    			<xsl:apply-templates select="../treeNode[ORGGROUPTREE_intParentGroupKey=$cid]"/>
    		</div>
		</xsl:otherwise>
		
	</xsl:choose>
	</p>
</xsl:template>

</xsl:stylesheet>
