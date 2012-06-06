#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%--
  Copyright 2012 - Six Dimensions
  All Rights Reserved.

  Author: Dan Klco

  Draws the HTML head with some default content:
  - initialization of the WCM
  - includes the current design CSS/JS clientlibs
  - sets the HTML title
--%>
<%@page session="false"  %>
<%@include file="/libs/wcm/global.jsp" %>
<head>
	<meta http-equiv="content-type" content="text/html">
	<meta http-equiv="keywords" content="<%= WCMUtils.getKeywords(currentPage) %>">
  	<meta charset="utf-8"> 
	<title><%= currentPage.getTitle() == null ? currentPage.getName() : currentPage.getTitle() %></title>
	<cq:include script="/libs/wcm/core/components/init/init.jsp"/>
	<cq:includeClientLib categories="apps.${parentArtifactId}-main" />
</head>