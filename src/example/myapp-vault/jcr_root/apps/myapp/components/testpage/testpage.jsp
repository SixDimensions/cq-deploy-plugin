<%--
  Writes 'Hello World' to the page.
  
  Author: Dan Klco 
--%>
<%@include file="/libs/foundation/global.jsp" %>
<%@page import="myapp.HelloWorldSvc" %>
<%
	HelloWorldSvc helloWorldSvc = sling.getService(HelloWorldSvc.class);
%>
<h1><%= helloWorldSvc.sayHello() %></h1>