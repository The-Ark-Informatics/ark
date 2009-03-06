<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : Mantas-4.0-To-Casegenix-1.0.xsl
    Created on : 1 February 2005, 12:39
    Author     : ltran
    Description:
        File to transform Mantas Alert XML 4.0 to Casegenix XML 1.0
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:include href="neuragenix/genix/polling_agent/Mantas-BB-4.0-To-Casegenix-1.0.xsl"/>
    <!--xsl:include href="Mantas-BB-4.0-To-Casegenix-1.0.xsl"/-->
    <xsl:output method="xml"/>
    <xsl:param name="currentTimeMillis"><xsl:value-of select="currentTimeMillis"/></xsl:param>
    <xsl:template match="/">

        <casegenix>
            <!-- HEADE INFORMATION -->
            <data_sequence><xsl:value-of select="/MantasAlertExportData/Header/AlertID"/></data_sequence>
            
            <!-- CASE INFORMATION -->
            <CASE>
                <CASE_intCoreCaseKey><xsl:value-of select="$currentTimeMillis"/></CASE_intCoreCaseKey>
                <CASE_intCaseID>
                    <xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/FocusTypeCode"/><xsl:value-of select="string(' ') "/> <xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/FocalEntityName"/><xsl:value-of select="string(' ') "/> <xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/ScenarioDisplayName"/>                    
                </CASE_intCaseID>
                <CASE_dtCreatedDate type="date" format="EEE MMM dd HH:mm:ss zzz yyyy" formfield="true"><xsl:value-of select="/MantasAlertExportData/Header/ExportDate"/></CASE_dtCreatedDate>
                <CASE_intScore><xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/Score"/></CASE_intScore>
                <CASE_strPriority><xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/Score"/></CASE_strPriority>
                		<strSystemUserName><xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/OwnerUserName"/></strSystemUserName>
                <CASE_strSource>Mantas</CASE_strSource>
                <CASE_strDetection>Automated Detection</CASE_strDetection>
            </CASE>        
            
            <!-- CSAL INFORMATION -->
            
                
	    <CSAL>
		<CSAL_intCoreCaseKey><xsl:value-of select="$currentTimeMillis"/></CSAL_intCoreCaseKey>
		<CSAL_intCaseAlertKey><xsl:value-of select="/MantasAlertExportData/Header/AlertID"/></CSAL_intCaseAlertKey>
		<CSAL_strBusDomain><xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/BusinessDomains"/></CSAL_strBusDomain>
		<CSAL_strScore><xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/Score"/></CSAL_strScore>
		<CSAL_dtCreationDate type="timestamp" formfield="true"><xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/CreationDate"/></CSAL_dtCreationDate>
		<CSAL_strScenario><xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/ScenarioDisplayName"/></CSAL_strScenario>
		<CSAL_strScenClass><xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/SameScenarioClassPriorMatchCount"/></CSAL_strScenClass>                
		<CSAL_strHighlights><xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/ProcessedAlertHighlights"/></CSAL_strHighlights>
		<!-- Multiple-valued attributes -->
		<xsl:for-each select="/MantasAlertExportData/AlertData/AlertContext/Scenarios/Scenario">
			<CSAL_strScenPrior><xsl:value-of select="SameScenarioPriorMatchCount"/></CSAL_strScenPrior>
		</xsl:for-each>
	    </CSAL>
                    
          
	    
	    <!-- CASE DIARY INFORMATION -->
	    <xsl:for-each select="/MantasAlertExportData/AlertData/Actions/Action">
	    	<CDRY>
			<CDRY_intCaseDiaryKey>24882</CDRY_intCaseDiaryKey>
			<CDRY_intCoreCaseKey><xsl:value-of select="$currentTimeMillis"/></CDRY_intCoreCaseKey>
			<CDRY_strDate type="timestamp" formfield="true"><xsl:value-of select="DateActionTaken"/></CDRY_strDate>
			<CDRY_strTime type="time"><xsl:value-of select="DateActionTaken"/></CDRY_strTime>
			<CDRY_strTimeTakenHr>0</CDRY_strTimeTakenHr>
			<CDRY_strTimeTakenMin>0</CDRY_strTimeTakenMin>
			<CDRY_strStatus>Alert</CDRY_strStatus>
			<CDRY_strPriority></CDRY_strPriority>
			<CDRY_strOwner><xsl:value-of select="UserNameTakingAction"/></CDRY_strOwner>
			
			<CDRY_strAction>
			Resulting Owner:<xsl:value-of select="string(' ') "/>
			<xsl:value-of select="OwnerUserName"/><xsl:value-of select="string(' ')"/>
			Resulting Status:<xsl:value-of select="string(' ')"/><xsl:value-of select="AlertStatus"/><xsl:value-of select="string(' ')"/>
			Action:<xsl:value-of select="string(' ')"/><xsl:value-of select="ActionName"/>
			<xsl:value-of select="string(' ')"/>-<xsl:value-of select="string(' ')"/>
			
			<xsl:for-each select="Comments/StandardComments/StandardComment">
				<xsl:if test="string-length( . ) > 0"> 
					<xsl:value-of select="."/>;<xsl:value-of select="string(' ')"/>
				</xsl:if>
			</xsl:for-each>
			<xsl:for-each select="Comments/FreeTextComment[ ModifiedFlag = 'N']">
				<xsl:if test="string-length( . ) > 0">
					<xsl:value-of select="CommentText"/>;<xsl:value-of select="string(' ')"/>
				</xsl:if>			
			</xsl:for-each>
			</CDRY_strAction>
		</CDRY>
	    </xsl:for-each>           
            
            <!-- RELATED ENTITY - RELATIONSHIP: FOCUS -->
            <xsl:variable name="strRPID"><xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/FocalEntityID"/></xsl:variable>
            
            <!-- Go through all the MatchedRecord element -->
            <xsl:for-each select="//MatchedRecord">
                <xsl:variable name="strColName"><xsl:call-template name="get-internal-column"><xsl:with-param name="strType"><xsl:value-of select="TableName"/></xsl:with-param></xsl:call-template></xsl:variable>
                <xsl:variable name="strTableName"><xsl:call-template name="get-table-name"><xsl:with-param name="strType"><xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/FocusTypeName"/></xsl:with-param></xsl:call-template></xsl:variable>
                
                <!-- Check for the new rules update on the 15-Mar-2005 -->
                <xsl:if test="( ./TableName = 'ACCT') or
                                        (../TableName = 'ACCT' and ./TableName = 'ACCT_GRP') or
                                        (../TableName = 'ACCT' and ./TableName = 'CUST') or
                                        (../TableName = 'BACK_OFFICE_TRXN' and ./TableName = 'ACCT') or
                                        (../TableName = 'BACK_OFFICE_TRXN' and ./TableName = 'EMP') or
                                        (../TableName = 'BACK_OFFICE_TRXN' and ./TableName = 'SCRTY') or
                                        (../TableName = 'CASH_TRXN' and ./TableName = 'ACCT') or
                                        (../TableName = 'CASH_TRXN' and ./TableName = 'DERIVED_ADDRESS') or
                                        (../TableName = 'MI_TRXN' and ./TableName = 'DERIVED_ADDRESS') or
                                        (./TableName = 'CLIENT_BANK') or
                                        (../TableName = 'CUST_SMRY_MNTH' and ./TableName = 'CUST') or
                                        (./TableName = 'DERIVED_ADDRESS') or
                                        (./TableName = 'EMP') or
                                        (../TableName = 'EXECUTION' and ./TableName = 'ACCT') or
                                        (../TableName = 'EXECUTION' and ./TableName = 'CUST') or
                                        (../TableName = 'EXECUTION' and ./TableName = 'EMP') or
                                        (./TableName = 'EXTERNAL_ENTITY') or
                                        (../TableName = 'WIRE_TRXN' and ./TableName = 'DERIVED_ADDRESS') or
                                        (../TableName = 'HH_BAL_POSN_SMRY' and ./TableName = 'ACCT_GRP') or
                                        (../TableName = 'INSTRUCTION' and ./TableName = 'ACCT') or
                                        (../TableName = 'INSTRUCTION' and ./TableName = 'EMP') or
                                        (./TableName = 'NVSMT_MGR') or
                                        (../TableName = 'ORDR' and ./TableName = 'ACCT') or
                                        (../TableName = 'ORDR' and ./TableName = 'EMP') or
                                        (../TableName = 'EMP' and ./TableName = 'EMP') or
                                        (./TableName = 'SCRTY') or
                                        (../TableName = 'TRADE' and ./TableName = 'ACCT') or
                                        (../TableName = 'TRADE' and ./TableName = 'EMP') or
                                        (../TableName = 'EMP_ACCT' and ./TableName = 'EMP') or
                                        (../TableName = 'EMP' and ./TableName = 'EMP') 
                                        
                                        or (./MatchedData/*[@NAME=$strColName] = $strRPID and ./TableName = $strTableName)
                                        or ( TableName = 'ORDR' or TableName = 'EXECUTION' )

                                ">

                <!-- Check if MatchRecord has the SAME ID as FOCUS ID, not sure if this necessary , ./MatchedData/*/@NAME = $strColName and-->
                
                    <RPTY>
                    
                        
                        <!-- Setting Relationship here, either 'Focus' if it matches focus information at the header, 
                            'Matched Information' otherwise -->
                        <xsl:choose>
                        <xsl:when test="./MatchedData/*[@NAME=$strColName] = $strRPID and ./TableName = $strTableName">
                            <RPTY_strRelationship>Focus</RPTY_strRelationship>                        
                        </xsl:when>
                        <xsl:when test="TableName = 'ORDR' or TableName = 'EXECUTION'">
                            <RPTY_strRelationship>Focus</RPTY_strRelationship>                        
                        </xsl:when>
                        <xsl:otherwise>
                            <RPTY_strRelationship>Matched Information</RPTY_strRelationship>
                        </xsl:otherwise>
                        </xsl:choose>
                        
                        
			
                        <!-- Creating RELATED ENTITY information here -->
                        <!-- ACCOUNT TYPE -->
                        <xsl:choose>
                        <xsl:when test="TableName = 'ACCT'">
                        
                            <RPTY_strType>Account</RPTY_strType>
                            <RPTY_dtLastUpdated type="timestamp"><xsl:value-of select="./MatchedData/*[@NAME='DATA_DUMP_DT']"/></RPTY_dtLastUpdated>
                            <!--RPTY_dtLastUpdated type="date">new Date()</RPTY_dtLastUpdated-->
                            <RPTY_intPartiesKey>ACCT_<xsl:value-of select="./MatchedData/*[@NAME='ACCT_INTRL_ID']"/></RPTY_intPartiesKey>
                            <RPTY_strAccID><xsl:value-of select="./MatchedData/*[@NAME='ACCT_INTRL_ID']"/></RPTY_strAccID>
                            <RPTY_strName><xsl:value-of select="./MatchedData/*[@NAME='ACCT_DSPLY_NM']"/></RPTY_strName>
                            <RPTY_strAccTaxID><xsl:value-of select="./MatchedData/*[@NAME='ACCT_TAX_ID']"/></RPTY_strAccTaxID>
                            <RPTY_strAccTranType>
                                <xsl:value-of select="./MatchedData/*[@NAME='ACCT_TYPE1_CD']"/>, <xsl:value-of select="./MatchedData/*[@NAME='ACCT_TYPE2_CD']"/>, <xsl:value-of select="./MatchedData/*[@NAME='MANTAS_ACCT_BUS_TYPE_CD']"/>
                            </RPTY_strAccTranType>
                            <RPTY_strAccRegType><xsl:value-of select="./MatchedData/*[@NAME='RGSTN_TYPE_CD']"/></RPTY_strAccRegType>
                            <RPTY_strAccBusUnit><xsl:value-of select="./MatchedData/*[@NAME='ORG_NM']"/></RPTY_strAccBusUnit>
                            <strAccBranchNm><xsl:value-of select="./MatchedData/*[@NAME='DMCLD_BRCH_CD']"/></strAccBranchNm>
                            
                        </xsl:when>
                        <!-- DERIVED ADDRESS -->
                        <xsl:when test="TableName = 'DERIVED_ADDRESS'">
                        
                            <RPTY_strType>Derived Address</RPTY_strType>
                            <RPTY_dtLastUpdated type="timestamp"><xsl:value-of select="./MatchedData/*[@NAME='DATA_DUMP_DT']"/></RPTY_dtLastUpdated>
                            <RPTY_intPartiesKey>DERIVED_ADDRESS_<xsl:value-of select="./MatchedData/*[@NAME='DERVD_ADDR_SEQ_ID']"/></RPTY_intPartiesKey>
                            <RPTY_strDASA1><xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE1_TX']"/></RPTY_strDASA1>
                            <RPTY_strDASA2><xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE2_TX']"/></RPTY_strDASA2>
                            <RPTY_strDASA3><xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE3_TX']"/></RPTY_strDASA3>
                            <RPTY_strDASA4><xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE4_TX']"/></RPTY_strDASA4>
                            <RPTY_strDASA5><xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE5_TX']"/></RPTY_strDASA5>
                            <RPTY_strDASA6><xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE6_TX']"/></RPTY_strDASA6>
                            <RPTY_strDACity><xsl:value-of select="./MatchedData/*[@NAME='ADDR_CITY_NM']"/></RPTY_strDACity>
                            <RPTY_strDAState><xsl:value-of select="./MatchedData/*[@NAME='ADDR_STATE_CD']"/></RPTY_strDAState>
                            <RPTY_strDAPostcode><xsl:value-of select="./MatchedData/*[@NAME='ADDR_POSTL_CD']"/></RPTY_strDAPostcode>
                            <RPTY_strDACountry><xsl:value-of select="./MatchedData/*[@NAME='ADDR_CNTRY_CD']"/></RPTY_strDACountry>
                            
                        </xsl:when>
                        <!-- CORRESPONDENT BANK -->
                        <xsl:when test="TableName = 'CLIENT_BANK'">
                        
                            <RPTY_strType>Correspondent Bank</RPTY_strType>
                            <RPTY_dtLastUpdated type="timestamp"><xsl:value-of select="./MatchedData/*[@NAME='DATA_DUMP_DT']"/></RPTY_dtLastUpdated>
                            <RPTY_intPartiesKey>CLIENT_BANK_<xsl:value-of select="./MatchedData/*[@NAME='INSTN_SEQ_ID']"/></RPTY_intPartiesKey>
                            
                            <RPTY_strID><xsl:value-of select="./MatchedData/*[@NAME='INSTN_ID']"/></RPTY_strID>
                            <RPTY_strName><xsl:value-of select="./MatchedData/*[@NAME='INSTN_NM']"/></RPTY_strName>
                            <RPTY_strBankType><xsl:value-of select="./MatchedData/*[@NAME='INSTN_ID_TYPE_CD']"/></RPTY_strBankType>
                            <RPTY_strBankLocation>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_CITY_NM']"/>, <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STATE_CD']"/>, <xsl:value-of select="./MatchedData/*[@NAME='ADDR_CNTRY_CD']"/>
                            </RPTY_strBankLocation>
                            <RPTY_strBankBusUnit><xsl:value-of select="./MatchedData/*[@NAME='BUS_UNIT_CD']"/></RPTY_strBankBusUnit>
                            
                        </xsl:when>
                        <!-- CUSTOMER -->
                        <xsl:when test="TableName = 'CUST'">
                        
                            <RPTY_strType>Customer</RPTY_strType>
                            <RPTY_dtLastUpdated type="timestamp"><xsl:value-of select="./MatchedData/*[@NAME='DATA_DUMP_DT']"/></RPTY_dtLastUpdated>
                            <RPTY_intPartiesKey>CUST_<xsl:value-of select="./MatchedData/*[@NAME='CUST_INTRL_ID']"/></RPTY_intPartiesKey>
                            
                            <RPTY_strID><xsl:value-of select="./MatchedData/*[@NAME='CUST_INTRL_ID']"/></RPTY_strID>
                            <RPTY_strName><xsl:value-of select="./MatchedData/*[@NAME='FULL_NM']"/></RPTY_strName>
                            <RPTY_strCustType><xsl:value-of select="./MatchedData/*[@NAME='CUST_TYPE_CODE']"/></RPTY_strCustType>
                            <RPTY_strCustOrg><xsl:value-of select="./MatchedData/*[@NAME='ORG_NM']"/></RPTY_strCustOrg>
                            <RPTY_strCustTaxID>
                                <xsl:value-of select="./MatchedData/*[@NAME='TAX_ID_FRMT_CD']"/>, <xsl:value-of select="./MatchedData/*[@NAME='TAX_ID']"/>
                            </RPTY_strCustTaxID>
                            <RPTY_strDOB type="timestamp"><xsl:value-of select="./MatchedData/*[@NAME='BIRTH_DATE']"/></RPTY_strDOB>
                            <RPTY_strCustFirmID><xsl:value-of select="./MatchedData/*[@NAME='HH_CUST_GRP_ID']"/></RPTY_strCustFirmID>
                            
                        </xsl:when>
                        <!-- EMPLOYEE -->
                        <xsl:when test="TableName = 'EMP'">
                        
                            <RPTY_strType>Employee</RPTY_strType>
                            <RPTY_dtLastUpdated type="timestamp"><xsl:value-of select="./MatchedData/*[@NAME='DATA_DUMP_DT']"/></RPTY_dtLastUpdated>
                            <RPTY_intPartiesKey>EMP_<xsl:value-of select="./MatchedData/*[@NAME='EMP_INTRL_ID']"/></RPTY_intPartiesKey>
                            
                            <RPTY_strID><xsl:value-of select="./MatchedData/*[@NAME='EMP_INTRL_ID']"/></RPTY_strID>
                            <RPTY_strName><xsl:value-of select="./MatchedData/*[@NAME='FULL_NM']"/></RPTY_strName>
                            <RPTY_strEmpTOrgName><xsl:value-of select="./MatchedData/*[@NAME='LINE_ORG_NM']"/></RPTY_strEmpTOrgName>
                            <RPTY_strEmpTOrgID><xsl:value-of select="./MatchedData/*[@NAME='LINE_ORG_INTRL_ID']"/></RPTY_strEmpTOrgID>
                            
                        </xsl:when>
                        <!-- HOUSEHOLD -->
                        <xsl:when test="TableName = 'ACCT_GRP'">
                        
                            <RPTY_strType>Household</RPTY_strType>
                            <RPTY_dtLastUpdated type="timestamp"><xsl:value-of select="./MatchedData/*[@NAME='DATA_DUMP_DT']"/></RPTY_dtLastUpdated>
                            <RPTY_intPartiesKey>ACCT_GRP_<xsl:value-of select="./MatchedData/*[@NAME='ACCT_GRP_ID']"/></RPTY_intPartiesKey>
                            <RPTY_strID><xsl:value-of select="./MatchedData/*[@NAME='ACCT_GRP_ID']"/></RPTY_strID>
                            <RPTY_strPrimaryAcc><xsl:value-of select="./MatchedData/*[@NAME='PRMRY_ACCT_INTRL_ID']"/></RPTY_strPrimaryAcc>
                            <RPTY_strName><xsl:value-of select="./MatchedData/*[@NAME='ACCT_GRP_NM']"/></RPTY_strName>
                            
                        </xsl:when>
                        <!-- SECURITY -->
                        <xsl:when test="TableName = 'SCRTY'">
                        
                            <RPTY_strType>Security</RPTY_strType>
                            <RPTY_dtLastUpdated type="timestamp"><xsl:value-of select="./MatchedData/*[@NAME='DATA_DUMP_DT']"/></RPTY_dtLastUpdated>
                            <RPTY_intPartiesKey>SCRTY_<xsl:value-of select="./MatchedData/*[@NAME='SCRTY_INTRL_ID']"/></RPTY_intPartiesKey>
                            
                            <RPTY_strID><xsl:value-of select="./MatchedData/*[@NAME='SCRTY_INTRL_ID']"/></RPTY_strID>
                            <RPTY_strName><xsl:value-of select="./MatchedData/*[@NAME='SCRTY_SHRT_NM']"/></RPTY_strName>
                            <RPTY_strSecDescriptn><xsl:value-of select="./MatchedData/*[@NAME='SCRTY_DESC_TX']"/></RPTY_strSecDescriptn>
                            <RPTY_strSecCategory><xsl:value-of select="./MatchedData/*[@NAME='PROD_CTGRY_CD']"/></RPTY_strSecCategory>
                            <RPTY_strSecISIN><xsl:value-of select="./MatchedData/*[@NAME='SCRTY_ISIN_ID']"/></RPTY_strSecISIN>
                            <RPTY_strSecGroup><xsl:value-of select="./MatchedData/*[@NAME='PROD_TYPE_CD']"/></RPTY_strSecGroup>
                            
                        </xsl:when>
                        <!-- INVESTMENT ADVISOR -->
                        <xsl:when test="TableName = 'NVSMT_MGR'">
                        
                            <RPTY_strType>Investment Advisor</RPTY_strType>
                            <RPTY_dtLastUpdated type="timestamp"><xsl:value-of select="./MatchedData/*[@NAME='DATA_DUMP_DT']"/></RPTY_dtLastUpdated>
                            <RPTY_intPartiesKey>NVSMT_MGR_<xsl:value-of select="./MatchedData/*[@NAME='NVSMT_MGR_INTRL_ID']"/></RPTY_intPartiesKey>
                            
                            <RPTY_strID><xsl:value-of select="./MatchedData/*[@NAME='NVSMT_MGR_INTRL_ID']"/></RPTY_strID>
                            <RPTY_strName><xsl:value-of select="./MatchedData/*[@NAME='NVSMT_MGR_NM']"/></RPTY_strName>
                            <RPTY_strIASubaacts><xsl:value-of select="./MatchedData/*[@NAME='ACTV_SUB_ACCT_CT']"/></RPTY_strIASubaacts>
                            <RPTY_strIAEmpID><xsl:value-of select="./MatchedData/*[@NAME='EMP_INTRL_ID']"/></RPTY_strIAEmpID>
                            <RPTY_strIAEmpName><xsl:value-of select="./MatchedData/*[@NAME='FULL_NM']"/></RPTY_strIAEmpName>
                            <RPTY_strIAAssets><xsl:value-of select="./MatchedData/*[@NAME='MANGD_ASSET_AM']"/></RPTY_strIAAssets>
                            
                        </xsl:when>
                        <!-- EXTERNAL -->
                        <xsl:when test="TableName = 'EXTERNAL_ENTITY'">
                        
                            <RPTY_strType>External</RPTY_strType>
                            <RPTY_dtLastUpdated type="timestamp"><xsl:value-of select="./MatchedData/*[@NAME='DATA_DUMP_DT']"/></RPTY_dtLastUpdated>
                            <RPTY_intPartiesKey>EXTERNAL_ENTITY_<xsl:value-of select="./MatchedData/*[@NAME='EXTRL_NTITY_SEQ_ID']"/></RPTY_intPartiesKey>
                            <RPTY_strID><xsl:value-of select="./MatchedData/*[@NAME='EXTRL_NTITY_SEQ_ID']"/></RPTY_strID>
                            <RPTY_strName><xsl:value-of select="./MatchedData/*[@NAME='EXTRL_NTITY_NM']"/></RPTY_strName>
                            <RPTY_strExtTypeID>
                                <xsl:value-of select="./MatchedData/*[@NAME='EXTRL_NTITY_TYPE_CD']"/>, <xsl:value-of select="./MatchedData/*[@NAME='EXTRL_NTITY_ID']"/>
                            </RPTY_strExtTypeID>
                            <RPTY_strExtType><xsl:value-of select="./MatchedData/*[@NAME='EXTRL_NTITY_TYPE_CD']"/></RPTY_strExtType>
                            <RPTY_dtLastActivity type="timestamp"><xsl:value-of select="./MatchedData/*[@NAME='LAST_ACTVY_DT']"/></RPTY_dtLastActivity>
                            
                        </xsl:when>
                        <!-- ORDER -->
                        <xsl:when test="TableName = 'ORDR'">
                        
                            <RPTY_strType>Order</RPTY_strType>
                            <RPTY_dtLastUpdated type="timestamp"><xsl:value-of select="./MatchedData/*[@NAME='DATA_DUMP_DT']"/></RPTY_dtLastUpdated>
                            <RPTY_intPartiesKey>ORDER_<xsl:value-of select="$strRPID"/></RPTY_intPartiesKey>
                            <RPTY_strID><xsl:value-of select="$strRPID"/></RPTY_strID>
                            <RPTY_strName><xsl:value-of select="$strRPID"/></RPTY_strName>
                            
                        </xsl:when>
                        <xsl:when test="TableName = 'EXECUTION'">
                        
                            <RPTY_strType>Execution</RPTY_strType>
                            <RPTY_dtLastUpdated type="timestamp"><xsl:value-of select="./MatchedData/*[@NAME='DATA_DUMP_DT']"/></RPTY_dtLastUpdated>
                            <RPTY_intPartiesKey>EXECUTION_<xsl:value-of select="$strRPID"/></RPTY_intPartiesKey>
                            <RPTY_strID><xsl:value-of select="$strRPID"/></RPTY_strID>
                            <RPTY_strName><xsl:value-of select="$strRPID"/></RPTY_strName>
                            
                        </xsl:when>
                        <xsl:otherwise>
                            <RPTY_strType>NEW</RPTY_strType>
                            <RPTY_intPartiesKey>NEW</RPTY_intPartiesKey>
                        
                        </xsl:otherwise>
                        </xsl:choose>
                        
                        <!-- Create the link between the CASE and the RELATED ENTITY here -->
                        <RCAS>
                        
                            <xsl:choose>
                            <xsl:when test="TableName = 'ACCT'">
                                <RCAS_intPartiesKey>ACCT_<xsl:value-of select="./MatchedData/*[@NAME='ACCT_INTRL_ID']"/></RCAS_intPartiesKey>
                            </xsl:when>
                            <!-- DERIVED ADDRESS -->
                            <xsl:when test="TableName = 'DERIVED_ADDRESS'">
                                <RCAS_intPartiesKey>DERIVED_ADDRESS_<xsl:value-of select="./MatchedData/*[@NAME='DERVD_ADDR_SEQ_ID']"/></RCAS_intPartiesKey>
                            </xsl:when>
                            <!-- CORRESPONDENT BANK -->
                            <xsl:when test="TableName = 'CLIENT_BANK'">
                                <RCAS_intPartiesKey>CLIENT_BANK_<xsl:value-of select="./MatchedData/*[@NAME='INSTN_SEQ_ID']"/></RCAS_intPartiesKey>
                            </xsl:when>
                            <!-- CUSTOMER -->
                            <xsl:when test="TableName = 'CUST'">
                                <RCAS_intPartiesKey>CUST_<xsl:value-of select="./MatchedData/*[@NAME='CUST_INTRL_ID']"/></RCAS_intPartiesKey>
                            </xsl:when>
                            <!-- EMPLOYEE -->
                            <xsl:when test="TableName = 'EMP'">
                                <RCAS_intPartiesKey>EMP_<xsl:value-of select="./MatchedData/*[@NAME='EMP_INTRL_ID']"/></RCAS_intPartiesKey>
                            </xsl:when>
                            <!-- HOUSEHOLD -->
                            <xsl:when test="TableName = 'ACCT_GRP'">
                                <RCAS_intPartiesKey>ACCT_GRP_<xsl:value-of select="./MatchedData/*[@NAME='ACCT_GRP_ID']"/></RCAS_intPartiesKey>
                            </xsl:when>
                            <!-- SECURITY -->
                            <xsl:when test="TableName = 'SCRTY'">
                                <RCAS_intPartiesKey>SCRTY_<xsl:value-of select="./MatchedData/*[@NAME='SCRTY_INTRL_ID']"/></RCAS_intPartiesKey>
                            </xsl:when>
                            <!-- INVESTMENT ADVISOR -->
                            <xsl:when test="TableName = 'NVSMT_MGR'">
                                <RCAS_intPartiesKey>NVSMT_MGR_<xsl:value-of select="./MatchedData/*[@NAME='NVSMT_MGR_INTRL_ID']"/></RCAS_intPartiesKey>
                            </xsl:when>
                            <!-- EXTERNAL -->
                            <xsl:when test="TableName = 'EXTERNAL_ENTITY'">
                                <RCAS_intPartiesKey>EXTERNAL_ENTITY_<xsl:value-of select="./MatchedData/*[@NAME='EXTRL_NTITY_SEQ_ID']"/></RCAS_intPartiesKey>
                            </xsl:when>
                            <!-- ORDER -->
                            <xsl:when test="TableName = 'ORDR'">
                                <RCAS_intPartiesKey>ORDER_<xsl:value-of select="$strRPID"/></RCAS_intPartiesKey>
                            </xsl:when>
                            <xsl:when test="TableName = 'EXECUTION'">
                                <RCAS_intPartiesKey>EXECUTION_<xsl:value-of select="$strRPID"/></RCAS_intPartiesKey>
                            </xsl:when>
                            <xsl:otherwise>
                                <RCAS_intPartiesKey>NEW</RCAS_intPartiesKey>
                            </xsl:otherwise>
                            </xsl:choose>
                            
                            <RCAS_intCoreCaseKey><xsl:value-of select="$currentTimeMillis"/></RCAS_intCoreCaseKey>
                        
                            <!-- Setting Relationship here, either 'Focus' if it matches focus information at the header, 
                            'Matched Information' otherwise -->
                            <xsl:choose>
                            <xsl:when test="./MatchedData/*[@NAME=$strColName] = $strRPID and ./TableName = $strTableName">
                                <RCAS_strRelationship>Focus</RCAS_strRelationship>                        
                            </xsl:when>
                            <xsl:when test="TableName = 'ORDR' or TableName = 'EXECUTION'">
                                <RCAS_strRelationship>Focus</RCAS_strRelationship>                        
                            </xsl:when>
                            <xsl:otherwise>
                                <RCAS_strRelationship>Matched Information</RCAS_strRelationship>
                            </xsl:otherwise>
                            </xsl:choose>

                        </RCAS>

                    </RPTY>

                    <!-- RELATED ADDRESS IMPORT HERE -->
		<xsl:for-each select="MatchedRecord">

                            <xsl:if test=" (./TableName = 'ACCT_ADDR') or
                                    (./TableName = 'CUST_ADDR') or
                                    (./TableName = 'CUST_EMAIL_ADDR') or
                                    (./TableName = 'CUST_PHON') or
                                    (./TableName = 'EMP_ADDR') or
                                    (./TableName = 'EMP_EMAIL_ADDR') or
                                    (./TableName = 'EMP_PHON') 
                                    ">

                            <RADD>
                                
                                <xsl:choose>
                                    <xsl:when test="../TableName = 'ACCT'">
                                        <RADD_intPartiesKey>ACCT_<xsl:value-of select="./MatchedData/*[@NAME='ACCT_INTRL_ID']"/></RADD_intPartiesKey>
                                    </xsl:when>
                                    <!-- DERIVED ADDRESS -->
                                    <xsl:when test="../TableName = 'DERIVED_ADDRESS'">
                                        <RADD_intPartiesKey>DERIVED_ADDRESS_<xsl:value-of select="./MatchedData/*[@NAME='DERVD_ADDR_SEQ_ID']"/></RADD_intPartiesKey>
                                    </xsl:when>
                                    <!-- CORRESPONDENT BANK -->
                                    <xsl:when test="../TableName = 'CLIENT_BANK'">
                                        <RADD_intPartiesKey>CLIENT_BANK_<xsl:value-of select="./MatchedData/*[@NAME='INSTN_SEQ_ID']"/></RADD_intPartiesKey>
                                    </xsl:when>
                                    <!-- CUSTOMER -->
                                    <xsl:when test="../TableName = 'CUST'">
                                        <RADD_intPartiesKey>CUST_<xsl:value-of select="./MatchedData/*[@NAME='CUST_INTRL_ID']"/></RADD_intPartiesKey>
                                    </xsl:when>
                                    <!-- EMPLOYEE -->
                                    <xsl:when test="../TableName = 'EMP'">
                                        <RADD_intPartiesKey>EMP_<xsl:value-of select="./MatchedData/*[@NAME='EMP_INTRL_ID']"/></RADD_intPartiesKey>
                                    </xsl:when>
                                    <!-- HOUSEHOLD -->
                                    <xsl:when test="../TableName = 'ACCT_GRP'">
                                        <RADD_intPartiesKey>ACCT_GRP_<xsl:value-of select="./MatchedData/*[@NAME='ACCT_GRP_ID']"/></RADD_intPartiesKey>
                                    </xsl:when>
                                    <!-- SECURITY -->
                                    <xsl:when test="../TableName = 'SCRTY'">
                                        <RADD_intPartiesKey>SCRTY_<xsl:value-of select="./MatchedData/*[@NAME='SCRTY_INTRL_ID']"/></RADD_intPartiesKey>
                                    </xsl:when>
                                    <!-- INVESTMENT ADVISOR -->
                                    <xsl:when test="../TableName = 'NVSMT_MGR'">
                                        <RADD_intPartiesKey>NVSMT_MGR_<xsl:value-of select="./MatchedData/*[@NAME='NVSMT_MGR_INTRL_ID']"/></RADD_intPartiesKey>
                                    </xsl:when>
                                    <!-- EXTERNAL -->
                                    <xsl:when test="../TableName = 'EXTERNAL_ENTITY'">
                                        <RADD_intPartiesKey>EXTERNAL_ENTITY_<xsl:value-of select="./MatchedData/*[@NAME='EXTRL_NTITY_SEQ_ID']"/></RADD_intPartiesKey>
                                    </xsl:when>
                                    <!-- ORDER -->
                                    <xsl:when test="../TableName = 'ORDR'">
                                        <RADD_intPartiesKey>ORDER_<xsl:value-of select="$strRPID"/></RADD_intPartiesKey>
                                    </xsl:when>
                                    <xsl:when test="../TableName = 'EXECUTION'">
                                        <RADD_intPartiesKey>EXECUTION_<xsl:value-of select="$strRPID"/></RADD_intPartiesKey>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <RADD_intPartiesKey>NEW</RADD_intPartiesKey>
                                    </xsl:otherwise>
                                </xsl:choose>

							 
                            <xsl:choose>
                            <xsl:when test="TableName = 'ACCT_ADDR'">
                                <RADD_intRAddressKey>ACCT_<xsl:value-of select="./MatchedData/*[@NAME='ACCT_INTRL_ID']"/>-<xsl:value-of select="position()"/></RADD_intRAddressKey>
                                <RADD_strType>Address</RADD_strType>
                                <RADD_strSubType><xsl:call-template name="get-address-type"><xsl:with-param name="strType"><xsl:value-of select="./MatchedData/*[@NAME='ADDR_USAGE_CD']"/></xsl:with-param></xsl:call-template></RADD_strSubType>
                                <RADD_strStreetName><xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE1_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE2_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE3_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE4_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE5_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE6_TX']"/>
                                </RADD_strStreetName>
                                <RADD_strSuburb><xsl:value-of select="./MatchedData/*[@NAME='ADDR_CITY_NM']"/></RADD_strSuburb>
                                <RADD_strState><xsl:value-of select="./MatchedData/*[@NAME='ADDR_STATE_CD']"/></RADD_strState>
                                <RADD_strPostCode><xsl:value-of select="./MatchedData/*[@NAME='ADDR_POSTL_CD']"/></RADD_strPostCode>
                                <RADD_strCountry><xsl:value-of select="./MatchedData/*[@NAME='ADDR_CNTRY_CD']"/></RADD_strCountry>
                                <RADD_strRegion><xsl:value-of select="./MatchedData/*[@NAME='ADDR_RGN_NM']"/></RADD_strRegion>
                            </xsl:when>
                            <!-- CUSTOMER -->
                            <xsl:when test="TableName = 'CUST_ADDR'">
                                <RADD_intRAddressKey>CUST_<xsl:value-of select="./MatchedData/*[@NAME='CUST_INTRL_ID']"/>-<xsl:value-of select="position()"/></RADD_intRAddressKey>
                                <RADD_strType>Address</RADD_strType>
                                <RADD_strSubType><xsl:call-template name="get-address-type"><xsl:with-param name="strType"><xsl:value-of select="./MatchedData/*[@NAME='ADDR_USAGE_CD']"/></xsl:with-param></xsl:call-template></RADD_strSubType>
                                <RADD_strStreetName><xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE1_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE2_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE3_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE4_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE5_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE6_TX']"/>
                                </RADD_strStreetName>
                                <RADD_strSuburb><xsl:value-of select="./MatchedData/*[@NAME='ADDR_CITY_NM']"/></RADD_strSuburb>
                                <RADD_strState><xsl:value-of select="./MatchedData/*[@NAME='ADDR_STATE_CD']"/></RADD_strState>
                                <RADD_strPostCode><xsl:value-of select="./MatchedData/*[@NAME='ADDR_POSTL_CD']"/></RADD_strPostCode>
                                <RADD_strCountry><xsl:value-of select="./MatchedData/*[@NAME='ADDR_CNTRY_CD']"/></RADD_strCountry>
                                <RADD_strRegion><xsl:value-of select="./MatchedData/*[@NAME='ADDR_RGN_NM']"/></RADD_strRegion>

                            </xsl:when>
                            <xsl:when test="TableName = 'CUST_EMAIL_ADDR'">
                                <RADD_intRAddressKey>CUST_<xsl:value-of select="./MatchedData/*[@NAME='CUST_INTRL_ID']"/>-<xsl:value-of select="position()"/></RADD_intRAddressKey>
                                <RADD_strType>Email</RADD_strType>
                                <RADD_strSubType><xsl:value-of select="./MatchedData/*[@NAME='EMAIL_SEQ_NB']"/></RADD_strSubType>
                                <RADD_strStreetName><xsl:value-of select="./MatchedData/*[@NAME='EMAIL_ADDR_TX']"/></RADD_strStreetName>
                            </xsl:when>
                            <xsl:when test="TableName = 'CUST_PHON'">
                                <RADD_intRAddressKey>CUST_<xsl:value-of select="./MatchedData/*[@NAME='CUST_INTRL_ID']"/>-<xsl:value-of select="position()"/></RADD_intRAddressKey>
                                <RADD_strType>Phone</RADD_strType>
                                <RADD_strSubType><xsl:call-template name="get-address-type"><xsl:with-param name="strType"><xsl:value-of select="./MatchedData/*[@NAME='PHON_USAGE_CD']"/></xsl:with-param></xsl:call-template></RADD_strSubType>
                                <RADD_strStreetName><xsl:value-of select="./MatchedData/*[@NAME='PHON_NB']"/> (<xsl:value-of select="./MatchedData/*[@NAME='PHON_EXT_NB']"/>)</RADD_strStreetName>
                            </xsl:when>
                            <!-- EMPLOYEE -->
                            <xsl:when test="TableName = 'EMP_ADDR'">
                                <RADD_intRAddressKey>EMP_<xsl:value-of select="./MatchedData/*[@NAME='EMP_INTRL_ID']"/>-<xsl:value-of select="position()"/></RADD_intRAddressKey>
                                <RADD_strType>Address</RADD_strType>
                                <RADD_strSubType><xsl:call-template name="get-address-type"><xsl:with-param name="strType"><xsl:value-of select="./MatchedData/*[@NAME='ADDR_USAGE_CD']"/></xsl:with-param></xsl:call-template></RADD_strSubType>
                                <RADD_strStreetName><xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE1_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE2_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE3_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE4_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE5_TX']"/>
                                <xsl:value-of select="./MatchedData/*[@NAME='ADDR_STRT_LINE6_TX']"/>
                                </RADD_strStreetName>
                                <RADD_strSuburb><xsl:value-of select="./MatchedData/*[@NAME='ADDR_CITY_NM']"/></RADD_strSuburb>
                                <RADD_strState><xsl:value-of select="./MatchedData/*[@NAME='ADDR_STATE_CD']"/></RADD_strState>
                                <RADD_strPostCode><xsl:value-of select="./MatchedData/*[@NAME='ADDR_POSTL_CD']"/></RADD_strPostCode>
                                <RADD_strCountry><xsl:value-of select="./MatchedData/*[@NAME='ADDR_CNTRY_CD']"/></RADD_strCountry>
                                <RADD_strRegion><xsl:value-of select="./MatchedData/*[@NAME='ADDR_RGN_NM']"/></RADD_strRegion>

                            </xsl:when>
                            <xsl:when test="TableName = 'EMP_EMAIL_ADDR'">
                                <RADD_intRAddressKey>EMP_<xsl:value-of select="./MatchedData/*[@NAME='EMP_INTRL_ID']"/>-<xsl:value-of select="position()"/></RADD_intRAddressKey>
                                <RADD_strType>Email</RADD_strType>
                                <RADD_strSubType><xsl:value-of select="./MatchedData/*[@NAME='EMAIL_SEQ_NB']"/></RADD_strSubType>
                                <RADD_strStreetName><xsl:value-of select="./MatchedData/*[@NAME='EMAIL_ADDR_TX']"/></RADD_strStreetName>
                            </xsl:when>
                            <xsl:when test="TableName = 'EMP_PHON'">
                                <RADD_intRAddressKey>EMP_<xsl:value-of select="./MatchedData/*[@NAME='EMP_INTRL_ID']"/></RADD_intRAddressKey>
                                <RADD_strType>Phone</RADD_strType>
                                <RADD_strSubType><xsl:call-template name="get-address-type"><xsl:with-param name="strType"><xsl:value-of select="./MatchedData/*[@NAME='PHON_USAGE_CD']"/></xsl:with-param></xsl:call-template></RADD_strSubType>
                                <RADD_strStreetName><xsl:value-of select="./MatchedData/*[@NAME='PHON_NB']"/> (<xsl:value-of select="./MatchedData/*[@NAME='PHON_EXT_NB']"/>)</RADD_strStreetName>
                            </xsl:when>
                            </xsl:choose>
                            </RADD> 

                            </xsl:if>
	</xsl:for-each>
                        
			</xsl:if>
                
            </xsl:for-each>

            <!-- BUILDING BLOCK SECTIONS -->
            <xsl:variable name="strClassCode"><xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/ScenarioClassCode"/></xsl:variable>
            <xsl:variable name="strTypeCode"><xsl:value-of select="/MantasAlertExportData/AlertData/AlertContext/FocusTypeCode"/></xsl:variable>

         	<xsl:if test="($strClassCode = 'CST' and $strTypeCode = 'AC')
         		or ($strClassCode = 'MF' and $strTypeCode = 'AC')
         		or ($strClassCode = 'ET' and $strTypeCode = 'EE')
         		or ($strClassCode = 'IA' and $strTypeCode = 'IA')
         		or ($strClassCode = 'MF' and $strTypeCode = 'IA')
         		or ($strClassCode = 'MF' and $strTypeCode = 'RR')
         		or ($strClassCode = 'CST' and $strTypeCode = 'RR')" >   
                        <xsl:call-template name="bb-4-1"/>
                </xsl:if>
                        
                <xsl:if test="($strClassCode = 'CST' and $strTypeCode = 'HH')
         		or ($strClassCode = 'MF' and $strTypeCode = 'HH')" >   
                        <xsl:call-template name="bb-4-2"/>
                </xsl:if>
                
                
         	<xsl:if test="($strClassCode = 'IML' and $strTypeCode = 'AC')
         		or ($strClassCode = 'IML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'EN')" >   
                        <xsl:call-template name="bb-4-3"/>
                </xsl:if>
                
                <xsl:if test="($strClassCode = 'ML' and $strTypeCode = 'AC')
         		or ($strClassCode = 'ML' and $strTypeCode = 'AD')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CB')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'EN')" >   
                        <xsl:call-template name="bb-4-4"/>
                </xsl:if>
                
                <xsl:if test="($strClassCode = 'ML' and $strTypeCode = 'HH')">
                        <xsl:call-template name="bb-4-5"/>
                </xsl:if>
                
                <xsl:if test="($strClassCode = 'BEX' and $strTypeCode = 'OR')
         		or ($strClassCode = 'TC' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'EX')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'OR')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'SC')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'TR')" >   
                        <xsl:call-template name="bb-4-6"/>
                </xsl:if>
                
                <xsl:if test="($strClassCode = 'CST' and $strTypeCode = 'AC')
         		or ($strClassCode = 'CST' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'CST' and $strTypeCode = 'RR')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'RR')    
         		or ($strClassCode = 'ET' and $strTypeCode = 'EE')    
         		or ($strClassCode = 'IA' and $strTypeCode = 'IA')" >   
                        <xsl:call-template name="bb-5-1"/>
                </xsl:if>
                
         	<xsl:if test="($strClassCode = 'ML' and $strTypeCode = 'AC')
         		or ($strClassCode = 'ML' and $strTypeCode = 'AD')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CB')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'HH')" >   
                        <xsl:call-template name="bb-5-2"/>
                </xsl:if>
                
                <!--xsl:if test="($strClassCode = 'CST' and $strTypeCode = 'HH')
         		or ($strClassCode = 'MF' and $strTypeCode = 'HH')" >   
                        <xsl:call-template name="bb-6-1"/>
                </xsl:if-->
                
                <xsl:if test="($strClassCode = 'BEX' and $strTypeCode = 'OR')
         		or ($strClassCode = 'CST' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'CST' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'CST' and $strTypeCode = 'RR')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'ET' and $strTypeCode = 'EE')    
         		or ($strClassCode = 'IA' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CB')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'EX')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'OR')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'SC')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'TR')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'AD')" >   
                        <xsl:call-template name="bb-7-1"/>
                </xsl:if>
                <xsl:if test="($strClassCode = 'CST' and $strTypeCode = 'AC')
         		or ($strClassCode = 'CST' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'CST' and $strTypeCode = 'RR')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'ET' and $strTypeCode = 'EE')    
         		or ($strClassCode = 'IA' and $strTypeCode = 'IA')" >   
						<xsl:call-template name="bb-8-1">
								<xsl:with-param name="intStartPos">1</xsl:with-param>
						</xsl:call-template>
                </xsl:if>
                
                <xsl:if test="($strClassCode = 'IML' and $strTypeCode = 'AC')
         		or ($strClassCode = 'IML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'EN')" >   
						<xsl:call-template name="bb-8-2">
								<xsl:with-param name="intStartPos">1</xsl:with-param>
						</xsl:call-template>
                </xsl:if>
                 
                <xsl:if test="($strClassCode = 'ML' and $strTypeCode = 'AD')
         		or ($strClassCode = 'ML' and $strTypeCode = 'CB')
         		or ($strClassCode = 'ML' and $strTypeCode = 'EN')
         		or ($strClassCode = 'ML' and $strTypeCode = 'HH')" >   
						<xsl:call-template name="bb-8-3">
								<xsl:with-param name="intStartPos">1</xsl:with-param>
						</xsl:call-template>
                        
                </xsl:if>
                <xsl:if test=" ($strClassCode = 'ML' and $strTypeCode = 'AC' )
                        or ($strClassCode = 'ML' and $strTypeCode = 'AD' )
                        or ($strClassCode = 'ML' and $strTypeCode = 'CB' )
                        or ($strClassCode = 'ML' and $strTypeCode = 'CU' )
                        or ($strClassCode = 'ML' and $strTypeCode = 'EN' )
                        or ($strClassCode = 'ML' and $strTypeCode = 'HH' )" >   
						<xsl:call-template name="bb-8-4">
								<xsl:with-param name="intStartPos">1</xsl:with-param>
						</xsl:call-template>

                </xsl:if>
		
                
                <!--xsl:if test="($strClassCode = 'CST' and $strTypeCode = 'AC')
         		or ($strClassCode = 'CST' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'AD')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CB')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'ET' and $strTypeCode = 'EE')    
         		or ($strClassCode = 'IA' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'RR')" >   
                        <xsl:call-template name="bb-9-1"/>
                </xsl:if-->
                
         	<!--xsl:if test="($strClassCode = 'IML' and $strTypeCode = 'AC')
         		or ($strClassCode = 'IML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'EN')" >   
                        <xsl:call-template name="bb-10-1"/>
                </xsl:if-->
                
         	<!--xsl:if test="($strClassCode = 'ML' and $strTypeCode = 'AC')
         		or ($strClassCode = 'ML' and $strTypeCode = 'AD')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CB')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'EN')" >   
                        <xsl:call-template name="bb-10-2"/>
                </xsl:if-->
                
                <!--xsl:if test="($strClassCode = 'ML' and $strTypeCode = 'HH')">
                        <xsl:call-template name="bb-11-1"/>
                </xsl:if-->
                
                <xsl:if test="($strClassCode = 'ML' and $strTypeCode = 'AC')
         		or ($strClassCode = 'ML' and $strTypeCode = 'AD')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CB')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'HH')" >   
                        <xsl:call-template name="bb-12-1"/>
                        <xsl:call-template name="bb-13-1"/>
                        <xsl:call-template name="bb-19-1">
								<xsl:with-param name="intStartPos">1</xsl:with-param>
						</xsl:call-template>
                </xsl:if>
                
         	<xsl:if test="($strClassCode = 'IML' and $strTypeCode = 'AC')
         		or ($strClassCode = 'IML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'EN')" >   
                        <xsl:call-template name="bb-13-2"/>
                </xsl:if>
          
                <xsl:if test="($strClassCode = 'BEX' and $strTypeCode = 'OR')
         		or ($strClassCode = 'CST' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'CST' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'CST' and $strTypeCode = 'RR')    
         		or ($strClassCode = 'ET' and $strTypeCode = 'EE')    
         		or ($strClassCode = 'IA' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'RR')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'AD')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CB')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'EX')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'OR')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'SC')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'TR')" >   
                        <xsl:call-template name="bb-14-1"/>
                        <xsl:call-template name="bb-16-1"/>
                        <xsl:call-template name="bb-17-1"/> 
                        <xsl:call-template name="bb-20-1"/>
                        <xsl:call-template name="bb-21-1">
								<xsl:with-param name="intStartPos">1</xsl:with-param>
						</xsl:call-template>
                        <xsl:call-template name="bb-22-1"/>
                        <xsl:call-template name="bb-23-1"/>
                        <xsl:call-template name="bb-25-1"/>  
                        <xsl:call-template name="bb-26-1"/>
                        <xsl:call-template name="bb-28-1"/>
                        <xsl:call-template name="bb-29-1">
								<xsl:with-param name="intStartPos">1</xsl:with-param>
						</xsl:call-template>
                        <xsl:call-template name="bb-30-1"/>
                        <xsl:call-template name="bb-31-1"/>
                        <xsl:call-template name="bb-32-1"/>
                        <xsl:call-template name="bb-33-1"/>
                        <xsl:call-template name="bb-34-1"/>
                        <xsl:call-template name="bb-35-1"/>
                        <xsl:call-template name="bb-39-1"/>
                        <xsl:call-template name="bb-40-1"/>
                        <!--xsl:call-template name="bb-27-1"/-->
                </xsl:if>
                
                <xsl:if test="($strClassCode = 'CST' and $strTypeCode = 'AC')
         		or ($strClassCode = 'CST' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'CST' and $strTypeCode = 'RR')    
         		or ($strClassCode = 'ET' and $strTypeCode = 'EE')    
         		or ($strClassCode = 'IA' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'RR')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'AD')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CB')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'HH')" >   
                        <xsl:call-template name="bb-15-1"/>
                        <xsl:call-template name="bb-18-1"/>
                        <xsl:call-template name="bb-24-1"/>
                </xsl:if>
                
                <xsl:if test="($strClassCode = 'BEX' and $strTypeCode = 'OR')
         		or ($strClassCode = 'TC' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'EX')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'OR')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'SC')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'TR')" >   
                        <xsl:call-template name="bb-24-2"/>
                </xsl:if>

                <xsl:if test="($strClassCode = 'BEX' and $strTypeCode = 'OR')
         		or ($strClassCode = 'TC' and $strTypeCode = 'EX')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'OR')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'SC')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'TR')
                      or ($strClassCode = 'TC' and $strTypeCode = 'AC')                        
         		or ($strClassCode = 'CST' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'CST' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'CST' and $strTypeCode = 'RR')    
         		or ($strClassCode = 'ET' and $strTypeCode = 'EE')    
         		or ($strClassCode = 'IA' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'RR')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'AD')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CB')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'HH')" >   
                        <xsl:call-template name="bb-36-1"/> 
                        <xsl:call-template name="bb-36-2"/>
                        <xsl:call-template name="bb-37-1"/> 
                        <xsl:call-template name="bb-37-2"/> 
                        <xsl:call-template name="bb-37-3"/> 
                        <xsl:call-template name="bb-37-4"/> 
                </xsl:if>

                <xsl:if test="($strClassCode = 'IML' and $strTypeCode = 'AC')
         		or ($strClassCode = 'IML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'IML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'AD')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CB')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'CU')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'EN')    
         		or ($strClassCode = 'ML' and $strTypeCode = 'HH')" >   
                        <xsl:call-template name="bb-38-1"/> 
                </xsl:if>
 
                <xsl:if test="($strClassCode = 'BEX' and $strTypeCode = 'OR')
         		or ($strClassCode = 'TC' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'EX')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'OR')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'SC')    
         		or ($strClassCode = 'TC' and $strTypeCode = 'TR')    
         		or ($strClassCode = 'CST' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'CST' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'CST' and $strTypeCode = 'RR')    
         		or ($strClassCode = 'ET' and $strTypeCode = 'EE')    
         		or ($strClassCode = 'IA' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'IA')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'AC')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'HH')    
         		or ($strClassCode = 'MF' and $strTypeCode = 'RR')" >   
                        <xsl:call-template name="bb-38-2"/>
                        <xsl:call-template name="bb-38-3"/>
                        <xsl:call-template name="bb-38-4"/>
                        <xsl:call-template name="bb-38-5"/>
                </xsl:if>
                


        </casegenix>
    </xsl:template>

    
    <!-- FUNCTIONS -->
    <!-- Template to return Mantas table name from Mantas FocusTypeName -->
    <xsl:template name="get-table-name">
        <xsl:param name="strType"><xsl:value-of select="strType"/></xsl:param>
        <xsl:choose>
            <xsl:when test="$strType = 'ACCOUNT'">ACCT</xsl:when>
            <xsl:when test="$strType = 'SECURITY'">SCRTY</xsl:when>
            <xsl:when test="$strType = 'CUSTOMER'">CUST</xsl:when>
            <xsl:when test="$strType = 'CLIENT_BANK'">CLIENT_BANK</xsl:when>
            <xsl:when test="$strType = 'ORDER'">ORDR</xsl:when>
            <xsl:when test="$strType = 'EXECUTION'">EXECUTION</xsl:when>
            <xsl:when test="$strType = 'EMPLOYEE'">EMP</xsl:when>
            <xsl:when test="$strType = 'TRADER'">EMP</xsl:when>
            <xsl:when test="$strType = 'REP'">EMP</xsl:when>
            <xsl:when test="$strType = 'EXTERNAL_ENTITY'">EXTERNAL_ENTITY</xsl:when>
            <xsl:when test="$strType = 'ADDRESS'">DERIVED_ADDRESS</xsl:when>
            <xsl:when test="$strType = 'HOUSEHOLD'">ACCT_GRP</xsl:when>
            <xsl:when test="$strType = 'NVSMT_MGR'">NVSMT_MGR</xsl:when>
            <xsl:otherwise>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
 
    <!-- Template to return Address type from usage code -->
    
    <xsl:template name="get-address-type">
        <xsl:param name="strType"><xsl:value-of select="strType"/></xsl:param>
        <xsl:choose>
            <xsl:when test="$strType = 'M'">Mailing</xsl:when>
            <xsl:when test="$strType = 'B'">Business</xsl:when>
            <xsl:when test="$strType = 'L'">Legal</xsl:when>
            <xsl:when test="$strType = 'A'">Alternate</xsl:when>
            <xsl:when test="$strType = 'H'">Home</xsl:when>
            <xsl:when test="$strType = 'C'">Mobile</xsl:when>
            <xsl:when test="$strType = 'P'">Pager</xsl:when>
            <xsl:when test="$strType = 'F'">Fax</xsl:when>
            <xsl:when test="$strType = 'O'">Other</xsl:when>
            <xsl:otherwise><xsl:value-of select="$strType"/></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
   
    <!-- Template to return Mantas table name from Mantas FocusTypeName -->
    
    <xsl:template name="get-internal-column">
        <xsl:param name="strType"><xsl:value-of select="strType"/></xsl:param>
        <xsl:choose>
            <xsl:when test="$strType = 'ACCT'">ACCT_INTRL_ID</xsl:when>
            <xsl:when test="$strType = 'SCRTY'">SCRTY_INTRL_ID</xsl:when>
            <xsl:when test="$strType = 'CUST'">CUST_INTRL_ID</xsl:when>
            <xsl:when test="$strType = 'CLIENT_BANK'">INSTN_SEQ_ID</xsl:when>
            <xsl:when test="$strType = 'ORDR'">ORDR_INTRL_ID</xsl:when>
            <xsl:when test="$strType = 'EXECUTION'">EXCTN_INTRL_ID</xsl:when>
            <xsl:when test="$strType = 'EMP'">EMP_INTRL_ID</xsl:when>
            <xsl:when test="$strType = 'EXTERNAL_ENTITY'">EXTRL_NTITY_SEQ_ID</xsl:when>
            <xsl:when test="$strType = 'DERIVED_ADDRESS'">DERVD_ADDR_SEQ_ID</xsl:when>
            <xsl:when test="$strType = 'ACCT_GRP'">ACCT_GRP_ID</xsl:when>
            <xsl:when test="$strType = 'NVSMT_MGR'">NVSMT_MGR_INTRL_ID</xsl:when>
            <xsl:otherwise>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
