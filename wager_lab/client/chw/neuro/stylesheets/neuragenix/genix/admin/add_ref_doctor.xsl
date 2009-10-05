<?xml version="1.0" encoding="utf-8"?>

<!-- upload_template.xsl To upload templates-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="./admin_menu.xsl"/>
  <xsl:output method="html" indent="no"/>

  <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>


  <xsl:template match="Doctor_details">
     <script language="javascript" >

            function confirmDelete(aURL) {
                var confirmAnswer = confirm('Are you sure you want to delete this record?');

                if(confirmAnswer == true){
                    window.location=aURL;
                }
            }
        </script>
        <xsl:variable name="DOCTOR_strID"><xsl:value-of select="/Doctor_details/Entered/Doctor/ID"/></xsl:variable>
<xsl:variable name="DOCTOR_strTitle"><xsl:value-of select="/Doctor_details/Entered/Doctor/Title"/></xsl:variable>
    <xsl:variable name="DOCTOR_strFirstName"><xsl:value-of select="/Doctor_details/Entered/Doctor/FirstName"/></xsl:variable>
    <xsl:variable name="DOCTOR_strSurname"><xsl:value-of select="/Doctor_details/Entered/Doctor/Surname"/></xsl:variable>
    <xsl:variable name="DOCTOR_strDepartment"><xsl:value-of select="/Doctor_details/Entered/Doctor/Department"/></xsl:variable>
    <xsl:variable name="DOCTOR_strHospital"><xsl:value-of select="/Doctor_details/Entered/Doctor/Hospital"/></xsl:variable>
    <xsl:variable name="DOCTOR_strAddress"><xsl:value-of select="/Doctor_details/Entered/Doctor/Address"/></xsl:variable>
    <xsl:variable name="DOCTOR_strPhone"><xsl:value-of select="/Doctor_details/Entered/Doctor/Phone"/></xsl:variable>
    <xsl:variable name="DOCTOR_strFax"><xsl:value-of select="/Doctor_details/Entered/Doctor/Fax"/></xsl:variable>
    <xsl:variable name="DOCTOR_strEmail"><xsl:value-of select="/Doctor_details/Entered/Doctor/Email"/></xsl:variable>
    <table width='100%'>
        <tr>
                <td colspan='9' class="neuragenix-form-required-text">
                    <xsl:value-of select="strError"/>
                </td>
        </tr>
        <xsl:if test='count(/Doctor_details/Doctors/Doctor) &gt; 0'>
        <tr>
            <td width="10%" class="uportal-channel-table-header">
		<a name="doctors"/>
                Name
            </td>
            <td width="15%" class="uportal-channel-table-header">
                Department
            </td>
            <td width="15%" class="uportal-channel-table-header">
                Hospital
            </td>
            <td width="25%" class="uportal-channel-table-header">
                Address
            </td>
            <td width="5%" class="uportal-channel-table-header">
                Phone
            </td>
            <td width="5%" class="uportal-channel-table-header">
                Fax
            </td>
            <td width="15%" class="uportal-channel-table-header">
                Email
            </td>
            <td width="5%"/>
            <td width="5%"/>
        </tr>
        <xsl:for-each select="/Doctor_details/Doctors/Doctor">
        <xsl:variable name="Doctor_ID"><xsl:value-of select="ID" /></xsl:variable>
        <xsl:if test='not($Doctor_ID = $DOCTOR_strID)'>
            <xsl:choose>
                <xsl:when test="position() mod 2 != 0">
                    <tr class="uportal-background-light">
                        <td width="10%" class="uportal-label">
                            <xsl:value-of select="Title" />&#160;<xsl:value-of select="FirstName" />&#160;<xsl:value-of select="Surname" />
                        </td>
                        <td width="15%" class="uportal-label">
                            <xsl:value-of select="Department" />
                        </td>
                        <td width="15%" class="uportal-label">
                            <xsl:value-of select="Hospital" />
                        </td>
                        <td width="25%" class="uportal-label">
                            <xsl:value-of select="Address" />
                        </td>
                        <td width="5%" class="uportal-label">
                            <xsl:value-of select="Phone" />
                        </td>
                        <td width="5%" class="uportal-label">
                            <xsl:value-of select="Fax" />
                        </td>
                        <td width="15%" class="uportal-label">
                            <xsl:value-of select="Email" />
                        </td>
                        <td width="5%" class="uportal-label">
                           <a href="{$baseActionURL}?current=add_doctor&amp;doctor_ID={$Doctor_ID}&amp;edit=true#addDoctor"> Edit</a>
                        </td>
                        <td width="5%" class="uportal-label">
                            <a href="javascript:confirmDelete('{$baseActionURL}?current=add_doctor&amp;doctor_ID={$Doctor_ID}&amp;delete=true')">Delete</a>
                        </td>
                    </tr>
                </xsl:when>
                <xsl:otherwise>
                    <tr>
                        <td width="10%" class="uportal-label">
                            <xsl:value-of select="Title" />&#160;<xsl:value-of select="FirstName" />&#160;<xsl:value-of select="Surname" />
                        </td>
                        <td width="15%" class="uportal-label">
                            <xsl:value-of select="Department" />
                        </td>
                        <td width="15%" class="uportal-label">
                            <xsl:value-of select="Hospital" />
                        </td>
                        <td width="25%" class="uportal-label">
                            <xsl:value-of select="Address" />
                        </td>
                        <td width="5%" class="uportal-label">
                            <xsl:value-of select="Phone" />
                        </td>
                        <td width="5%" class="uportal-label">
                            <xsl:value-of select="Fax" />
                        </td>
                        <td width="15%" class="uportal-label">
                            <xsl:value-of select="Email" />
                        </td>
                        <td width="5%" class="uportal-label">
                           <a href="{$baseActionURL}?current=add_doctor&amp;doctor_ID={$Doctor_ID}&amp;edit=true#addDoctor"> Edit</a>
                        </td>
                        <td width="5%" class="uportal-label">
                            <a href="javascript:confirmDelete('{$baseActionURL}?current=add_doctor&amp;doctor_ID={$Doctor_ID}&amp;delete=true')">Delete</a>
                        </td>
                    </tr>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
        </xsl:for-each>
        </xsl:if>
    <!--/table>
    <table width="100%" cellpadding="0" cellspacing="0"-->
        <tr>
            <td colspan='9'>
                <hr/>
            </td>
        </tr>
    <!--/table-->
    <form action="{$baseActionURL}?current=add_doctor" method="post" enctype="multipart/form-data">
        <!--table width="100%" cellpadding="0" cellspacing="0"-->
            <tr>
                <td colspan='9' class="uportal-channel-table-header">
                <a name="addDoctor">   		
		 Add New Doctor Details
		</a>
                </td>
            </tr> 
            <tr>
                <td width="10%" class="uportal-channel-table-header">
                Title / First Name / Surname
            </td>
            <td width="15%" class="uportal-channel-table-header">
                Department
            </td>
            <td width="15%" class="uportal-channel-table-header">
                Hospital
            </td>
            <td width="25%" class="uportal-channel-table-header">
                Address
            </td>
            <td width="5%" class="uportal-channel-table-header">
                Phone
            </td>
            <td width="5%" class="uportal-channel-table-header">
                Fax
            </td>
            <td width="15%" class="uportal-channel-table-header">
                Email
            </td>
            <td width="5%"/>
            <td width="5%"/>
            </tr>
            <tr>
                <td width="10%" class="uportal-channel-table-header">
                <input type="text" name="DOCTOR_strTitle" value="{$DOCTOR_strTitle}" size='8'/><input type="text" name="DOCTOR_strFirstName" value="{$DOCTOR_strFirstName}" size='20'/><input type="text" name="DOCTOR_strSurname" size='20' value="{$DOCTOR_strSurname}"/>
            </td>
            <td width="15%" class="uportal-channel-table-header">
                <input type="text" size='20' name="DOCTOR_strDepartment" value="{$DOCTOR_strDepartment}"/>
            </td>
            <td width="15%" class="uportal-channel-table-header">
                <input type="text" size='20' name="DOCTOR_strHospital" value="{$DOCTOR_strHospital}"/>
            </td>
            <td width="25%" class="uportal-channel-table-header">
                <input type="text" size='20' name="DOCTOR_strAddress" value="{$DOCTOR_strAddress}"/>
            </td>
            <td width="5%" class="uportal-channel-table-header">
                <input type="text" size='20' name="DOCTOR_strPhone" value="{$DOCTOR_strPhone}"/>
            </td>
            <td width="5%" class="uportal-channel-table-header">
                <input type="text" size='20' name="DOCTOR_strFax" value="{$DOCTOR_strFax}"/>
            </td>
            <td width="15%" class="uportal-channel-table-header">
                <input type="text" size='20' name="DOCTOR_strEmail" value="{$DOCTOR_strEmail}"/>
            </td>
            <td width="5%"/>
            <td width="5%"/>
        </tr> 
        <tr>
                <td width="10%" class="uportal-label">
                <xsl:variable name="button_name"><xsl:value-of select='/Doctor_details/button_label'/></xsl:variable>
                <input type="hidden" name="DOCTOR_strID" value="{$DOCTOR_strID}" />
                    <input type="submit" name="add" tabindex="24" class="uportal-button" value="{$button_name}"/>
                </td>
                <td colspan = '8'/>
        </tr>
        <!--/table-->
    </form>
    </table>
    </xsl:template>

</xsl:stylesheet> 
