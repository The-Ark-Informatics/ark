<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<jp:mondrianQuery id="query01" jdbcDriver="org.postgresql.Driver" jdbcUrl="jdbc:postgresql:mondrianDB?user=postgres&password=postgres" catalogUri="/WEB-INF/queries/FoodMart.xml">
with
member [Measures].[Rendite] as '([Measures].[Store Sales] - [Measures].[Store Cost]) / [Measures].[Store Cost]'
,format_string =
iif(([Measures].[Store Sales] - [Measures].[Store Cost]) / [Measures].[Store Cost] * 100 > Parameter("UpperLimit", NUMERIC, 151, "Obere Grenze"), "|#.00%|arrow='up'",
iif(([Measures].[Store Sales] - [Measures].[Store Cost]) / [Measures].[Store Cost] * 100 < Parameter("LowerLimit", NUMERIC, 150, "Untere Grenze"), "|#.00%|arrow='down'", "|#.00%|arrow='blank'"))

select
  {[Measures].[Rendite], [Measures].[Store Cost], [Measures].[Store Sales]} on columns,
  {[Product].[All Products]} ON rows
from Sales
where ([Time].[1997])
</jp:mondrianQuery>

<c:set var="title01" scope="session">Arrows</c:set>
