<?xml version="1.0" encoding="utf-8"?>
<!-- normal_explorer.xsl, part of the HelloWorld example channel -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output method="html" indent="no" />
	<xsl:include href="./admin_menu.xsl"/>



	  <xsl:template match="ListManager">
          <Script language="javascript">
          
          
          // Note : Netscape browsers seem to die with the go target code    
          if (navigator.appName.toLowerCase() != 'netscape')
          {
               var goTarget = '<xsl:value-of select="normalize-space(GoTarget)" />'; 
               if (goTarget != '')
               {
                  documentURL = "" + document.location;
                  docSharpIndex = documentURL.indexOf("#");

                  docStrippedURL = documentURL.substring(0, docSharpIndex);
                  document.location.replace(docStrippedURL + '#<xsl:value-of select="GoTarget" />');
              }
          }        
                  
                  
                  
                  
              function submitUpdateProtein(proteinID, name, localisation, gene)
              {
                  alert ('Remember, you must click Commit Changes for this to be saved to the database!');
                  document.updateDetails.name.value = document.getElementById(proteinID + '_Name').value;
                  document.updateDetails.localisation.value = document.getElementById(proteinID + '_Localisation').value;
                  document.updateDetails.gene.value = document.getElementById(proteinID + '_Gene').value;
                  document.updateDetails.disease.value = document.getElementById(proteinID + '_Disease').value;
                  document.updateDetails.reportDisplay.value = document.getElementById(proteinID + '_reportDisplay').value;
                  document.updateDetails.nodeID.value = proteinID;
                  document.updateDetails.submit();
              }
              
              function submitUpdateAntibody(proteinID, name)
              {
                  alert ('Remember, you must click Commit Changes for this to be saved to the database!');
                  document.updateDetails.name.value = document.getElementById(proteinID + '_Name').value;
                  document.updateDetails.nodeID.value = proteinID;
                  document.updateDetails.submit();
              }
              
          </Script>
          
          	
		<table>
			<tr>
				<td class="uportal-channel-subtitle">
                                  Protein List
                                  <hr/>
				</td>
			</tr>
                        
                        <tr>
                        <td>
                        
                        
                        <Span class="neuragenix-label">
                            
                            <A>
                               <xsl:attribute name="href">

                                  <xsl:value-of select="$baseActionURL" />?current=listManager_addNode&amp;parentNodeID=-1
                               </xsl:attribute>    
                            Add New Protein
                            </A>
                        
                            |
                        
                        
                            <A> 
                              <xsl:attribute name="href">
                                    <xsl:value-of select="$baseActionURL" />?current=listManager_commitChanges
                              </xsl:attribute>    
                              Commit Changes
                            </A>
                             |
                            <A> 
                              <xsl:attribute name="href">
                                    <xsl:value-of select="$baseActionURL" />?current=listManager_reset
                              </xsl:attribute>    
                              Reset List
                            </A>
                        </Span>
                        
                        
                        
                        </td>
                        </tr>
                        
                        
                        <tr>
                        <td>
                                              

<table>
<tr>
<td class="uportal-channel-subtitle">
Protein Name 
   

</td>
<td class="uportal-channel-subtitle">
Protein Localisation
</td>
<td class="uportal-channel-subtitle">
Protein Gene
</td>
<td class="uportal-channel-subtitle">
Related Disease
</td>

<td class="uportal-channel-subtitle">
Report Display Name
</td>


</tr>


     <xsl:for-each select="Proteins">

      <xsl:for-each select="Protein">
      <tr>
           <xsl:variable name="intID" select="InternalID" />
           <xsl:variable name="editMode"><xsl:value-of select="EditMode" /></xsl:variable>
           
            <td>
               <xsl:attribute name="class">
                <xsl:if test="$intID mod 2 != 0">
                   uportal-input-text
                </xsl:if>
              </xsl:attribute>
                
            
            
            
             <a>
              <xsl:attribute name="name"><xsl:value-of select="normalize-space($intID)" /></xsl:attribute>
             </a>
            
            
            
              <xsl:choose>
                 <xsl:when test="$editMode='true'">
                    
                    <xsl:variable name="name" select="Name" />
                    <input type="text" class="uportal-input-text">
                       <xsl:attribute name="id"><xsl:value-of select="normalize-space($intID)" />_Name</xsl:attribute>
                       <xsl:attribute name="value"><xsl:value-of select="$name" /></xsl:attribute>
                    </input>
                 </xsl:when>
                 <xsl:otherwise>
                    <A class="uportal-channel-subtitle">
                       <xsl:attribute name="href">
                          <xsl:value-of select="$baseActionURL" />?current=listManager_openNode&amp;levelToBuild=<xsl:value-of select="InternalID" />
                       </xsl:attribute>
                       <xsl:value-of select="Name"/>
                    </A>
                 </xsl:otherwise>
              </xsl:choose>
              
           </td>
           <td>
              <xsl:attribute name="class">
                <xsl:if test="$intID mod 2 != 0">
                   uportal-input-text
                </xsl:if>
              </xsl:attribute>
                
               
               <xsl:variable name="local" select="Localisation" />
               <xsl:choose>
                  
                  <xsl:when test="$editMode='true'">
                     
                      <input type="text" class="uportal-input-text">
                           <xsl:attribute name="id"><xsl:value-of select="normalize-space($intID)" />_Localisation</xsl:attribute>
                           <xsl:attribute name="value"><xsl:value-of select="$local" /></xsl:attribute>
                      </input>   
                     
                  </xsl:when>
                  
                  <xsl:otherwise>
                      <Span class="uportal-text"><xsl:value-of select="Localisation" /></Span>
                  </xsl:otherwise>
               
               </xsl:choose>
                    
           
           
           
           </td>
           <td>
              
              <xsl:attribute name="class">
                <xsl:if test="$intID mod 2 != 0">
                   uportal-input-text
                </xsl:if>
              </xsl:attribute>
                
           
           
           
           
           
               <xsl:variable name="geneValue" select="Gene" />
               <xsl:choose>
                  <xsl:when test="$editMode='true'">
                     <input type="text" class="uportal-input-text">
                       <xsl:attribute name="id"><xsl:value-of select="normalize-space($intID)" />_Gene</xsl:attribute>
                       <xsl:attribute name="value"><xsl:value-of select="$geneValue" /></xsl:attribute>
                     </input>
                     
                     
                  </xsl:when>
                  
                  
                  
                  
                  
                  <xsl:otherwise>
                      <Span class="uportal-text"><xsl:value-of select="Gene" /></Span>
                  </xsl:otherwise>
               
               </xsl:choose>    
           
           </td>
                      
           <td>
              
              <xsl:attribute name="class">
                <xsl:if test="$intID mod 2 != 0">
                   uportal-input-text
                </xsl:if>
              </xsl:attribute>
                
           
           
           
           
           
               <xsl:variable name="diseaseValue" select="Disease" />
               <xsl:choose>
                  <xsl:when test="$editMode='true'">
                     <input type="text" class="uportal-input-text">
                       <xsl:attribute name="id"><xsl:value-of select="normalize-space($intID)" />_Disease</xsl:attribute>
                       <xsl:attribute name="value"><xsl:value-of select="$diseaseValue" /></xsl:attribute>
                     </input>
                     
                     
                  </xsl:when>
                  
                  
                  
                  
                  
                  <xsl:otherwise>
                      <Span class="uportal-text"><xsl:value-of select="Disease" /></Span>
                  </xsl:otherwise>
               
               </xsl:choose>    
           
           </td>

           <td>
              
              <xsl:attribute name="class">
                <xsl:if test="$intID mod 2 != 0">
                   uportal-input-text
                </xsl:if>
              </xsl:attribute>
                
           
           
           
           
           
               <xsl:variable name="reportDisplay" select="ReportDisplay" />
               <xsl:choose>
                  <xsl:when test="$editMode='true'">
                     <input type="text" class="uportal-input-text">
                       <xsl:attribute name="id"><xsl:value-of select="normalize-space($intID)" />_reportDisplay</xsl:attribute>
                       <xsl:attribute name="value"><xsl:value-of select="$reportDisplay" /></xsl:attribute>
                     </input>
                     
                     
                  </xsl:when>
                  
                  
                  
                  
                  
                  <xsl:otherwise>
                      <Span class="uportal-text"><xsl:value-of select="ReportDisplay" /></Span>
                  </xsl:otherwise>
               
               </xsl:choose>    
           
           </td>

           
           
           
                                 
           <td>
              <xsl:attribute name="class">
                <xsl:if test="$intID mod 2 != 0">
                   uportal-input-text
                </xsl:if>
              </xsl:attribute>
                
              <xsl:choose>
                  <xsl:when test="$editMode='true'">
                   
                  <A>
                      <xsl:attribute name="href">
                         javascript:submitUpdateProtein('<xsl:value-of select="$intID" />')
                      </xsl:attribute>    
                  update
                  </A>  
                  
                  
                  
                  
                  </xsl:when>
                  
                  <xsl:otherwise>
                  <A>
                  <xsl:attribute name="href">
                  
                     <xsl:value-of select="$baseActionURL" />?current=listManager_editNode&amp;nodeID=<xsl:value-of select="InternalID" />
                  </xsl:attribute>    
                   edit protein
                  </A>
                  </xsl:otherwise>
               
               </xsl:choose>
           
           </td>
           
           
           
           
           
           <td>
              <xsl:attribute name="class">
                <xsl:if test="$intID mod 2 != 0">
                   uportal-input-text
                </xsl:if>
              </xsl:attribute>
                
               <A>
                  <xsl:attribute name="href">
                  
                     <xsl:value-of select="$baseActionURL" />?current=listManager_deleteNode&amp;nodeID=<xsl:value-of select="InternalID" />
                  </xsl:attribute>    
               remove protein
               </A>
           </td>
           
           
           <td>
              <xsl:attribute name="class">
                <xsl:if test="$intID mod 2 != 0">
                   uportal-input-text
                </xsl:if>
              </xsl:attribute>
                
           
           
               <A>
                  <xsl:attribute name="href">
                  
                     <xsl:value-of select="$baseActionURL" />?current=listManager_addNode&amp;parentNodeID=<xsl:value-of select="InternalID" />
                  </xsl:attribute>    
               add new antibody
               </A>
                
           </td>
           
           
           <xsl:for-each select="Antibodies">
                          <xsl:for-each select="Antibody">
                          <xsl:variable name="intID" select="InternalID" />
                          <xsl:variable name="editMode" select="EditMode" />
                          <xsl:variable name="name" select="Name" />
                           <tr>
                             <td>
                            
                            </td>
                            <td>
                            <xsl:attribute name="class">
                                <xsl:if test="$intID mod 2 != 0">
                                    uportal-input-text
                                </xsl:if>
                            </xsl:attribute>
                                
                            
                            <Span class="uportal-label">Antibody</Span>
                                  
                            </td>
                            <td>
                            <xsl:attribute name="class">
                              <xsl:if test="$intID mod 2 != 0">
                                uportal-input-text
                              </xsl:if>
                            </xsl:attribute>
                               
                               <xsl:choose>
                                  <xsl:when test="$editMode='true'">
                                  
                                     <input type="text" class="uportal-input-text">
                                        <xsl:attribute name="id"><xsl:value-of select="normalize-space($intID)" />_Name</xsl:attribute>
                                        <xsl:attribute name="value"><xsl:value-of select="$name" /></xsl:attribute>
                                     </input>   
                                  
                                  </xsl:when>
                                  
                                  <xsl:otherwise>
                                      <Span class="uportal-text"><xsl:value-of select="Name"/></Span>
                                  </xsl:otherwise>
                               </xsl:choose>
                            
                            </td>
                            <td>
                            </td>
                            <td>
                               <xsl:attribute name="class">
                                 <xsl:if test="$intID mod 2 != 0">
                                  uportal-input-text
                                </xsl:if>
                               </xsl:attribute>
                               
                               <xsl:choose>
                                  <xsl:when test="$editMode='true'">
                                    <A>
                                      <xsl:attribute name="href">
                                           javascript:submitUpdateAntibody('<xsl:value-of select="$intID" />')
                                      </xsl:attribute>    
                                      update antibody
                                   </A>
                                  </xsl:when>               
                            
                                  <xsl:otherwise>
                                      <A >
                                         <xsl:attribute name="href">
                                            <xsl:value-of select="$baseActionURL" />?current=listManager_editNode&amp;nodeID=<xsl:value-of select="InternalID" />
                                         </xsl:attribute>    
                                          edit antibody
                                      </A>
                                  </xsl:otherwise>
                               </xsl:choose>
                               
                               </td>
                            
                            
                            <td>
                            <xsl:attribute name="class">
                                <xsl:if test="$intID mod 2 != 0">
                                uportal-input-text
                                </xsl:if>
                            </xsl:attribute>
                            
                            
                            <A>
                                <xsl:attribute name="href">
                                    <xsl:value-of select="$baseActionURL" />?current=listManager_deleteNode&amp;nodeID=<xsl:value-of select="InternalID" />
                                </xsl:attribute>    
                                remove
                            </A>
                            
                            </td>
                            </tr> 
                     
                          </xsl:for-each>
                                          
           </xsl:for-each>
</tr>       
       </xsl:for-each>

   </xsl:for-each>
  
  
  
  







</table>

                     
                        </td>
                        </tr>

		</table>
                <form name="updateDetails" action="{$baseActionURL}?current=listManager_updateNode">
                <input type="hidden" name="current" value="listManager_updateNode" />
   <input type="hidden" value="" name="nodeID" />
   <input type="hidden" value="" name="name" />
   <input type="hidden" value="" name="localisation" />
   <input type="hidden" value="" name="gene" />
   <input type="hidden" value="" name="disease" />
   <input type="hidden" value="" name="reportDisplay" />
</form>
                
                
                
	  </xsl:template> 



</xsl:stylesheet>
