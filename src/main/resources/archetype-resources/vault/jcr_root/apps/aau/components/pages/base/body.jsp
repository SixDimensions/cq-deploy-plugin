#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%--
  Copyright 2012 - Six Dimensions
  All Rights Reserved.

  Author: Dan Klco

  Draws the body of the page including the header and footer, but not including the individual page content.
--%>
<%@page session="false"	%>
<%@page import="com.day.cq.wcm.api.WCMMode"%>
<%@include file="/libs/wcm/global.jsp"%>
<% 
final boolean publishMode = WCMMode.DISABLED.equals(WCMMode.fromRequest(request));
%>
<body<% if (publishMode){%> class="contentOnly" <% } %>	>
	<div id="holder">
<% if (!publishMode){%>	
		<div id="header">
			<img src="/etc/designs/${parentArtifactId}/images/header.png" alt="header" border="0" />
		</div>
<% }%>		
		<div id="left_side"></div>
		<cq:include script="content.jsp" />
		<div id="right_side"></div>
<% if (!publishMode){%> 		
		<div id="foot">
			<img src="/etc/designs/${parentArtifactId}/images/footer.png" alt="foot" />
		</div>
<%}%>		
	</div>
</body>