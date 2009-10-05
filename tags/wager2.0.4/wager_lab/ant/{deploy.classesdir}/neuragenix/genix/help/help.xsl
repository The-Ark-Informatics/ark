<?xml version="1.0" encoding="utf-8"?>
<!-- first page of import data process -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:param name="formParams">current=help</xsl:param>

  <xsl:output method="html" indent="no" />
  <!-- Get the parameters from the channel class -->
  
<xsl:template match="help">
    
    <form action="{$formParams}" Name="help" method="post" enctype="multipart/form-data">
    <script>
    var screenW = 640, screenH = 480;
    if (parseInt(navigator.appVersion)>3) {
        if (navigator.appName=="Netscape") {
            screenW = screen.width - 36;
        }
        else if (navigator.appName.indexOf("Microsoft")!=-1) {
            screenW = screen.width - 42;
        }
        
        screenH = screen.height;
    }

    document.write("&lt;applet code='media/neuragenix/bio/help/HelpApplet.class' archive='media/jh/jhall.jar' WIDTH='" + screenW + "' HEIGHT='" + screenH + "' &gt;&lt;/applet&gt;" );
    
    </script>
    </form>

</xsl:template>
 
</xsl:stylesheet>
