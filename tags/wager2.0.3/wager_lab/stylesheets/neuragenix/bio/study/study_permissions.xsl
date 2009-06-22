<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./study_menu.xsl"/>
<xsl:include href="../../common/common_btn_name.xsl"/>

  <xsl:output method="html" indent="no" />

  <xsl:template match="study">
  <!-- Get the parameters from the channel class -->
  <xsl:param name="strStudyName"><xsl:value-of select="strStudyName" /></xsl:param>
  <xsl:param name="strStudyOwner"><xsl:value-of select="strStudyOwner" /></xsl:param>
  <xsl:param name="strStudyDesc"><xsl:value-of select="strStudyDesc" /></xsl:param>
  <xsl:param name="dtStudyStart"><xsl:value-of select="dtStudyStart" /></xsl:param>
  <xsl:param name="dtStudyEnd"><xsl:value-of select="dtStudyEnd" /></xsl:param>
  <xsl:param name="intTargetPatientNo"><xsl:value-of select="intTargetPatientNo" /></xsl:param>
  <xsl:param name="intActualPatientNo"><xsl:value-of select="intActualPatientNo" /></xsl:param>
  <xsl:variable name="intStudyID"><xsl:value-of select="intStudyID" /></xsl:variable>
        <table width="100%">
		<tr>
			<td class="uportal-channel-subtitle">
			Modify Study Transaction Permissions for <xsl:value-of select="$strStudyName" /><br/>
			</td>
                        <td align="right">
                            <form action="{$baseActionURL}?current=study_allocation" method="post">
                                <input type="hidden" name="intStudyID" value="{$intStudyID}" />
                                <input type="submit" name="back" value="{$backBtnLabel}" class="uportal-button" />
                            </form>
                        </td>
		</tr>
                <tr colspan="2">
                    <hr/>
                </tr>
              
	</table>
	<table width="100%">
		 
		<tr>
			<td class="neuragenix-form-required-text" width="5%">
					<xsl:value-of select="strErrorDuplicateKey" /><xsl:value-of select="strErrorRequiredFields" /><xsl:value-of select="strErrorInvalidDataFields" /><xsl:value-of select="strErrorInvalidData" /><xsl:value-of select="strError" />
			</td>
			<td class="neuragenix-form-required-text" width="95%" id="neuragenix-required-header" align="right">
			* = Required fields (Note : Studies in red will not allow for this study to partake in transactions)
			</td>

		</tr>
	</table>
	
        <form name="matrixData" action="{$baseActionURL}?current=study_permissions&amp;action=update_matrix" method="post">
        <table border="1" cellpadding="2" cellspacing="2">
           <tr>
               <td>
                   <span class="uportal-label">Study Name</span>
               </td>
               
               <td>
                   <span class="uportal-label">Allocate into</span>
               </td>
               <!-- CCV - only allocate into enabled
               <td>
                   <span class="uportal-label">Allocate from</span>
               </td> -->
          </tr>

          <xsl:for-each select="StudyPermissions">
              <xsl:variable name="notfound" select="NotFound" />
              <xsl:variable name="externalStudyKey"><xsl:value-of select="ExternalStudyKey" /></xsl:variable>
              <tr class="uportal-text">
                  
                  <xsl:if test="NotFound=0">
                     <xsl:attribute name="bgcolor">red</xsl:attribute>
                  </xsl:if>
                  <input type="hidden" name="STUDY_PERMISSION_SAVED_{$externalStudyKey}" value="{$notfound}" />
                  <td>
                       <input type="hidden" name="STUDY_PERMISSION_ID_{$externalStudyKey}" value="{$externalStudyKey}" />
                       <xsl:value-of select="ExternalStudyName" />
                  </td>
                       
                  <td>
                       <input type="checkbox" name="STUDY_PERMISSION_IN_{$externalStudyKey}">
                            <xsl:if test="InSelected=0">
                               <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                       </input>
                            
                       <!-- <xsl:value-of select="InSelected" /> -->
                  </td>
                 <!-- 
                  <td>
                      <input type="checkbox" name="STUDY_PERMISSION_OUT_{$externalStudyKey}">
                            <xsl:if test="OutSelected=0">
                               <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                      </input>     
                  
                  
                       <xsl:value-of select="OutSelected" /> 
                  </td>
                  -->
              </tr>
          </xsl:for-each>
</table>
        
        <input type="Submit" class="uportal-input-text" value="Save Changes" />
        <input type="button" class="uportal-input-text" name="reset" value="Reset Permissions" />
        
        </form>
        
        
  </xsl:template>
 
</xsl:stylesheet>
