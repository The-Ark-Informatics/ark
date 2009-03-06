<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:template name="common_javascript">
    <script language="javascript" >
        
            function confirmDelete(aURL) 
            {
                var confirmAnswer = confirm('Are you sure you want to delete this record?');
                if(confirmAnswer == true)
                {
                    window.location=aURL + '&amp;delete=true';
                }
            } 

            function jumpTo(aURL)
            {
                window.location=aURL;
            }
        
    </script>
   </xsl:template>
    
</xsl:stylesheet>
