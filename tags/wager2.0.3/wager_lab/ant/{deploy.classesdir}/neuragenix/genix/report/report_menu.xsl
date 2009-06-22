<?xml version="1.0" encoding="utf-8"?>

<!-- inventory_menu.xsl. Menu used for all inventory.-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="/">
    <script language="javascript" >

	function confirmDelete(aURL) {
            var confirmAnswer = confirm('Are you sure you want to delete this record?');

            if(confirmAnswer == true){
		window.location=aURL + '&amp;delete=true';
            }
	}
        
	function confirmClear(aURL) {
            var confirmAnswer = confirm('Are you sure you want to clear the fields?');

            if(confirmAnswer == true) {
		window.location=aURL + '&amp;clear=true';
            }
        }
	
    </script>
    <xsl:apply-templates />
    </xsl:template>

</xsl:stylesheet>