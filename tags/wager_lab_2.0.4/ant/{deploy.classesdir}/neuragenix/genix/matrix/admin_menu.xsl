<?xml version="1.0" encoding="utf-8"?>
<!-- admin_menu.xsl. Menu used for all administration.-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="html" indent="no"/>
  <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>

  <xsl:template match="/">
    <table width="100%">
	<tr valign="top">        
	<td id="neuragenix-border-right" width="50%" class="uportal-channel-subtitle">Step 1. Select a Case Type</td><td></td></tr>        
                    <tr class="uportal-label"><td><a href="{$baseActionURL}?current=search_case">Staff defalcation/fraud</a></td><td><img border="0" src="media/neuragenix/cross.gif" alt="Check"/> </td></tr>
                    
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Change management transition</a></td><td><img border="0" src="media/neuragenix/cross.gif" alt="Check"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Inadequate training/knowledge</a></td><td><img border="0" src="media/neuragenix/cross.gif" alt="Check"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Procedures / policy not followed</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Staff / agent error</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Staff / agent negligence</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Inadequate Quality Assurance / control processes</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Lost item </a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Policy/Procedures deficient</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Unable to refute claim</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Within policy/Validing threshold (e.g. verification amount limit)</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Small balance item adjustment</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Chargebacks</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">EFT Disputes - Unclassified</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Fees for photographic evidence</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Systems or Communications down or off-line</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Internal Systems error</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">External Fraudulent activity</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">External Systems / infrastructure failure or off-line </a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">External Theft</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Failure of outsourced activity or provider</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Items lost/damaged  (whilst not in the bank's control)</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Physical security insufficient/defective</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Unable to access building </a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Ex Gratia payments </a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Merchant liquidation (cards)</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td> <a href="{$baseActionURL}?current=search_case">Merchant error (cards) </a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/> </td></tr>
                    <tr class="uportal-label"><td><a href="{$baseActionURL}?current=search_case">Public Liability</a></td><td><img border="0" src="media/neuragenix/tick.gif" alt="Valid"/></td></tr>
                <!-- <td width="0%" valign="top"><xsl:apply-templates/></td> -->
         </table>
  </xsl:template>
</xsl:stylesheet>

