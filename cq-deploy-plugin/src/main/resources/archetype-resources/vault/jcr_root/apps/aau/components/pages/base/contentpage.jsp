#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%--
  Copyright 2012 - Six Dimensions
  All Rights Reserved.

  Author: Dan Klco

  Is used as base for all "page" components. It basically includes the "head"
  and the "body" scripts.
--%>
<%@page session="false"  contentType="text/html" %><%
%><%@page import="com.day.cq.wcm.api.WCMMode" %><%
%><%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %><%
%><cq:defineObjects/><%
    // read the redirect target from the 'page properties' and perform the
    // redirect if WCM is disabled.
    String location = properties.get("redirectTarget", "");
    if (WCMMode.fromRequest(request) == WCMMode.DISABLED && location.length() > 0) {
        // check for recursion
        if (!location.equals(currentPage.getPath())) {
            response.sendRedirect(request.getContextPath() + location + ".html");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        return;
    }
%>
<!doctype html>
<html>
<cq:include script="head.jsp"/>
<cq:include script="body.jsp"/>
</html>