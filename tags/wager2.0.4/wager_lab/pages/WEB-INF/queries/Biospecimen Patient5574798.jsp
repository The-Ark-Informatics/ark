<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<jp:mondrianQuery id="query01" jdbcDriver="org.postgresql.Driver" jdbcUrl="jdbc:postgresql:biogenix?user=postgres&password=postgres" catalogUri="/WEB-INF/queries/OLAPSchema.xml">
select
  {[Biospecimen types].[Type].members} on columns,
  {[Patient Biospecimen].[Country].members} ON rows
from Biospecimen 
where [Measures].[Number of bio]</jp:mondrianQuery>
<c:set var="title01" scope="session">
Biospecimen Patient</c:set>