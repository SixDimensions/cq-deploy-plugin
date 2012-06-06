#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%--
  Copyright 2012 - Six Dimensions
  All Rights Reserved.

  Author: Dan Klco

  Renders a single session.
--%>
<%@page session="false"	%>
<%@include file="/libs/wcm/global.jsp"%>
<%@page import="com.day.cq.wcm.api.WCMMode"%>
<%
if (WCMMode.fromRequest(request) == WCMMode.EDIT){
    %>
    Please drag Accordian Item(s) to this area
    <%  
}
%>
<c:if test="${symbol_dollar}{not empty properties.title}">
	<h3>${symbol_dollar}{properties.title}</h3>
</c:if>
<c:if test="${symbol_dollar}{not empty properties.description}">
	${symbol_dollar}{properties.description}
</c:if>
<ul id="acc" class="acc">
	<cq:include path="accordian-items" resourceType="foundation/components/parsys" />
</ul>

<script type="text/javascript">
${symbol_dollar}('${symbol_pound}acc h3').click(function(){
	${symbol_dollar}(this).siblings('.acc-section').each(function(){
		if(!${symbol_dollar}(this).is(":visible")){
			${symbol_dollar}('.acc-section:visible').hide('fast');
			${symbol_dollar}(this).toggle('fast');
		}else{
			${symbol_dollar}(this).hide('fast');
		}
	});
});
${symbol_dollar}('${symbol_pound}acc .acc-section').hide();
${symbol_dollar}('${symbol_pound}acc li:first .acc-section').show();
</script>