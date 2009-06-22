<?xml version="1.0" encoding="utf-8"?>
<!-- first page of import data process -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:param name="formParams">current=workflow_designer</xsl:param>

  <xsl:output method="html" indent="no" />
  <!-- Get the parameters from the channel class -->
  <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
  
<xsl:template match="workflow">
    <!--img src="images/workflow/designer.PNG"  border="0"/-->
    
    
    <table width="100%">
	<tr>

            <td width="25%"></td>

            <td width="65%" class="uportal-label">
                <span class="uportal-channel-current-subtitle">Create workflow </span>
                <a href="{$baseActionURL}?current=workflow_search_workflow_template">| Search workflow template </a>
                <a href="{$baseActionURL}?current=workflow_search_workflow_instance">| Search workflow instance</a>
            </td>

            <td width="10%"></td>
        </tr>
    </table>
    <hr/>

    <script language="JavaScript">
        var screenW = 640, screenH = 600;
        var place;
        var places;
        var packageid;
        var workflowid;
        
        if (parseInt(navigator.appVersion)>3) {
            if (navigator.appName=="Netscape") {
                screenW = screen.width - 36;
            }
            else if (navigator.appName.indexOf("Microsoft")!=-1) {
                screenW = screen.width - 42;
            }
        }

        place = location.href; 
        places = place.split("?"); 
        if(places[1]){ 
          packageid = places[1].split("strPackageID=");
          if (packageid[1]) { 
            packageid = packageid[1].split("&amp;")[0];
          } 
        }

        places = place.split("?"); 
        if(places[1]){ 
          workflowid = places[1].split("strWorkflowID=");
          if (workflowid[1]) {  
            workflowid = workflowid[1].split("&amp;")[0];
          } 
        }
        
        document.write("&lt;applet code='media.neuragenix.genix.workflow.WFDesigner.class' archive='media/jgraph/jgraph.jar' WIDTH='" + screenW + "' HEIGHT='" + screenH + "' &gt;&lt;param name='strPackageID' value='" + packageid + "'&gt;" + " &lt;param name='strWorkflowID' value='" + workflowid + "'&gt;" + "&lt;/applet&gt;" );
        //document.write("&lt;applet code='media.neuragenix.genix.workflow.WFDesigner.class' archive='media/jgraph/jgraph.jar' WIDTH='" + screenW + "' HEIGHT='" + screenH + "' &gt;&lt;/applet&gt;" );
    </script>            

    
    
    
</xsl:template>
 
</xsl:stylesheet>
