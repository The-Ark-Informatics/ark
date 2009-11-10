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
                  document.updateDetails.nodeID.value = proteinID;
                  if(document.getElementById(proteinID + '_strLOVParentValue'))
                  {
                    document.updateDetails.LOV_strLOVParentValue.value = document.getElementById(proteinID + '_strLOVParentValue').value;
                    }
                  document.updateDetails.submit();
              }
              
              function submitUpdateAntibody(proteinID, name)
              {
                  if(document.getElementById(proteinID + '_SortOrder').value == "")
                  {
                    alert('The sort order cannot be empty');
                  }
                  else
                  {
                      alert ('Remember, you must click Commit Changes for this to be saved to the database!');                  
                      document.updateDetails.name.value = document.getElementById(proteinID + '_Name').value;
                      document.updateDetails.sortOrder.value = document.getElementById(proteinID + '_SortOrder').value;
                      if(document.getElementById(proteinID + '_strLOVParentValue'))
                      {
                            document.updateDetails.LOV_strLOVParentValue.value = document.getElementById(proteinID + '_strLOVParentValue').value;
                        }
                      document.updateDetails.nodeID.value = proteinID;
                      document.updateDetails.submit();
                  }
              }
              
          </Script>
          
          	
		<table>
			<tr>
				<td class="uportal-channel-subtitle">
                                  Manage Lists
                                  <hr/>
				</td>
			</tr>
                        
                        <tr>
                        <td>
                        
                        <br />
                        
                        
                        <Span class="neuragenix-label">
                            <!-- 
                            <A>
                               <xsl:attribute name="href">

                                  <xsl:value-of select="$baseActionURL" />?current=LOVlistManager_addNode&amp;parentNodeID=-1
                               </xsl:attribute>    
                            Add New Protein
                            </A>
                        
                            |
                        
                            -->
                            <A> 
                              <xsl:attribute name="href">
                                    <xsl:value-of select="$baseActionURL" />?current=LOVlistManager_commitChanges
                              </xsl:attribute>    
                              Commit Changes
                            </A>
                             |
                            <A> 
                              <xsl:attribute name="href">
                                    <xsl:value-of select="$baseActionURL" />?current=LOVlistManager_reset
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
List Name 
   

</td>
<td class="uportal-channel-subtitle">
List Description
</td>
<td class="uportal-channel-subtitle">

</td>
</tr>


     <xsl:for-each select="Categories">

      <xsl:for-each select="Category">
      <tr>
           <xsl:variable name="intID" select="InternalID" />
           <xsl:variable name="refID" select="InternalID" />
           <xsl:variable name="editMode"><xsl:value-of select="EditMode" /></xsl:variable>
           
            <td>
               <xsl:attribute name="class">
                <xsl:if test="$intID mod 2 != 0">
                   uportal-input-text
                </xsl:if>
              </xsl:attribute>
                
            
            
            
             <!--a>
              <xsl:attribute name="value"><xsl:value-of select="normalize-space($intID)" /></xsl:attribute>
             </a-->
            
            
            
              <xsl:choose>
                 <xsl:when test="$editMode='true'">
                    
                    <xsl:variable name="name" select="Value" />
                    <input type="text" class="uportal-input-text">
                       <xsl:attribute name="id"><xsl:value-of select="normalize-space($intID)" />_Name</xsl:attribute>
                       <xsl:attribute name="value"><xsl:value-of select="$name" /></xsl:attribute>
                    </input>
                 </xsl:when>
                 <xsl:otherwise>
                    <xsl:variable name="norm_id"><xsl:value-of select="normalize-space($intID)" /></xsl:variable>
                    <A class="uportal-channel-subtitle" name="{$norm_id}">
                    <!--A class="uportal-channel-subtitle"-->
                       <xsl:attribute name="href">
                          <xsl:value-of select="$baseActionURL" />?current=LOVlistManager_openNode&amp;levelToBuild=<xsl:value-of select="InternalID"/>#<xsl:value-of select="$intID"/>
                       </xsl:attribute>
                       <xsl:value-of select="Value"/>
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
                
               
               <xsl:variable name="Description" select="Description" />
               <xsl:choose>
               <!--   
                  <xsl:when test="$editMode='true'">
                     
                      <input type="text" class="uportal-input-text">
                           <xsl:attribute name="id"><xsl:value-of select="normalize-space($intID)" />_Localisation</xsl:attribute>
                           <xsl:attribute name="value"><xsl:value-of select="$local" /></xsl:attribute>
                      </input>   
                     
                  </xsl:when>
                  -->
                  <xsl:otherwise>
                      <Span class="uportal-text"><xsl:value-of select="Description" /></Span>
                  </xsl:otherwise>
               
               </xsl:choose>
                    
           
           
           
           </td>
           <td>
              
              <xsl:attribute name="class">
                <xsl:if test="$intID mod 2 != 0">
                   uportal-input-text
                </xsl:if>
              </xsl:attribute>
                
           
           
           
           <!-- 
           
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
           -->
           
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
                  <!-- 
                  <A>
                  <xsl:attribute name="href">
                  
                     <xsl:value-of select="$baseActionURL" />?current=LOVlistManager_editNode&amp;nodeID=<xsl:value-of select="InternalID" />
                  </xsl:attribute>    
                    edit
                  </A> -->
                  </xsl:otherwise>
               
               </xsl:choose>
           
           </td>
           
           
           
           
           
           <td>
              <xsl:attribute name="class">
                <xsl:if test="$intID mod 2 != 0">
                   uportal-input-text
                </xsl:if>
              </xsl:attribute>
               <!-- 
               <A>
                  <xsl:attribute name="href">
                  
                     <xsl:value-of select="$baseActionURL" />?current=LOVlistManager_deleteNode&amp;nodeID=<xsl:value-of select="InternalID" />
                  </xsl:attribute>    
               remove list (Remove this)
               </A>
               -->
           </td>
           
           
           <td>
              <xsl:attribute name="class">
                <xsl:if test="$intID mod 2 != 0">
                   uportal-input-text
                </xsl:if>
              </xsl:attribute>
                
           
           <xsl:if test="count(ListValues/Values) > 0">
               <A>
                  <xsl:attribute name="href">
                  
                     <xsl:value-of select="$baseActionURL" />?current=LOVlistManager_addNode&amp;parentNodeID=<xsl:value-of select="InternalID" />#add_<xsl:value-of select="$refID" />
                  </xsl:attribute>    
               add new value
               </A>
                </xsl:if>
           </td>
           
           
           <xsl:for-each select="ListValues">
                          <xsl:for-each select="Values">
                          <xsl:variable name="intID" select="InternalID" />
                          <xsl:variable name="editMode" select="EditMode" />
                          <xsl:variable name="name" select="Value" />
                          <xsl:variable name="sortOrder" select="SortOrder" />
                          <xsl:variable name="ParentCategory" select="ParentCategory" />
                          <xsl:variable name="ParentValue" select="ParentValue" />
                           <tr>
                             <td align="left">
                               <xsl:attribute name="class">
                                  <xsl:if test="$intID mod 2 != 0">
                                    uportal-input-text
                                  </xsl:if>
                                </xsl:attribute>
                                <table width="100%" cellspacing='1' cellpadding='1'>
                                    <tr>
                                        <xsl:choose>
                                        <xsl:when test="$editMode='true'">
                                            <xsl:if test="count(ParentdropdownValues/Value) > 0">
                                            <td align="left" width="30%"><Span class="uportal-label">Parent Value</Span><!--xsl:value-of select="$ParentCategory"/--></td>
                                            <td align="left" width="70%">
                                           
                                            <select name="LOV_strLOVParentValue" class="uportal-input-text">
                                                <xsl:attribute name="id"><xsl:value-of select="normalize-space($intID)" />_strLOVParentValue</xsl:attribute>
                                                <xsl:for-each select="ParentdropdownValues/Value">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="."/>
									</xsl:attribute>
									<xsl:if test="@selected=1">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="."/>
								</option>
							</xsl:for-each>
                                                    </select>
                                                </td>
                                                </xsl:if>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:if test="count(ParentValue) > 0">
                                                <td align="left" width="30%"><Span class="uportal-label">Parent </Span><!--xsl:value-of select="$ParentCategory"/--></td>
                                                <td align="left" width="70%">
                                                <Span class="uportal-text"><xsl:value-of select="normalize-space($ParentValue)"/></Span>                                            
                                                </td>
                                            </xsl:if>
                                        </xsl:otherwise>
                                        </xsl:choose>
                                    </tr>
                                </table> 
                            </td>
                            <td>
                            <xsl:attribute name="class">
                                <xsl:if test="$intID mod 2 != 0">
                                    uportal-input-text
                                </xsl:if>
                            </xsl:attribute>
                                
                            
                            <table width="100%">
                              <tr>
                                <td width="30%" align="left">

                                    <Span class="uportal-label">Value</Span>

                                </td>
                                <td width="70%" align="left">
                                      <xsl:choose>
                                        <xsl:when test="$editMode='true'">
                                        <xsl:variable name="norm_refid">add_<xsl:value-of select="normalize-space($refID)" /></xsl:variable>
                                            <A class="uportal-channel-subtitle" name="{$norm_refid}">
                                            <xsl:if test="$editMode='true'">
                                                  <input type="text" class="uportal-input-text">
                                                     <xsl:attribute name="id"><xsl:value-of select="normalize-space($intID)" />_Name</xsl:attribute>
                                                     <xsl:attribute name="value"><xsl:value-of select="$name" /></xsl:attribute>
                                                  </input>   
                                            </xsl:if>
                                            </A>
                                       </xsl:when>
                                       <xsl:otherwise>
                                           <Span class="uportal-text"><xsl:value-of select="Value"/></Span>       
                                       </xsl:otherwise>
                                   </xsl:choose>
                                </td>
                              </tr>
                            </table>
                                
                                
                            
                            </td>
                            <td>
                            <xsl:attribute name="class">
                              <xsl:if test="$intID mod 2 != 0">
                                uportal-input-text
                              </xsl:if>
                            </xsl:attribute>
                               
                               <xsl:choose>
                                  <xsl:when test="$editMode='true'">
                                  
                                     <table width="100%">
                                        <tr>
                                           <td width="80%" align="left">
                                             
                                              <Span class="uportal-label">Sort Order</Span>                       
                                           </td>
                                           <td width="20%" align="left">
                                                 <xsl:variable name="norm_id"><xsl:value-of select="normalize-space($intID)" /></xsl:variable>
                                                <A class="uportal-channel-subtitle" name="{$norm_id}">
                                              <input type="text" class="uportal-input-text">
                                                 <xsl:attribute name="id"><xsl:value-of select="normalize-space($intID)" />_SortOrder</xsl:attribute>
                                                 <xsl:attribute name="value"><xsl:value-of select="$sortOrder" /></xsl:attribute>
                                              </input>   
                                              </A>
                                           </td>
                                        </tr>
                                     </table>
                                     
                                  </xsl:when>
                                  
                                  <xsl:otherwise>
                                      
                                      
                                      <table width="100%" class="uportal-text">
                                        <tr>
                                           <td width="80%">
                                                <Span class="uportal-label">Sort Order</Span>
                                           </td>
                                           <td width="20%">
                                                <Span class="uportal-text"><xsl:value-of select="SortOrder"/></Span>
                                           </td>
                                        </tr>
                                     </table>
                                      
                                      
                                      
                                      
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
                                      update value
                                   </A>
                                  </xsl:when>               
                            
                                  <xsl:otherwise>
                                      <A >
                                         <xsl:attribute name="href">
                                            <xsl:value-of select="$baseActionURL" />?current=LOVlistManager_editNode&amp;nodeID=<xsl:value-of select="InternalID" />#<xsl:value-of select="$intID" />
                                         </xsl:attribute>    
                                          edit value
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
                                    <xsl:value-of select="$baseActionURL" />?current=LOVlistManager_deleteNode&amp;nodeID=<xsl:value-of select="InternalID" />
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
                <form name="updateDetails" action="{$baseActionURL}?current=LOVlistManager_updateNode">
                <input type="hidden" name="current" value="LOVlistManager_updateNode" />
   <input type="hidden" value="" name="nodeID" />
   <input type="hidden" value="" name="name" />
   <input type="hidden" value="" name="localisation" />
   <input type="hidden" value="" name="gene" />
   <input type="hidden" value="" name="LOV_strLOVParentValue" />
   <input type="hidden" value="" name="sortOrder" />
</form>
                
                
                
	  </xsl:template> 



</xsl:stylesheet>
