/*
  Copyright 2012 - Six Dimensions
  All Rights Reserved.

  Author: Tyrone Tse

  Rewrite navigation links to be in the parent window location.
*/

function rewriteLinks(pThis)
{
	var href = pThis.getAttribute("href");
	var currentURL = window.location.href;      
	href=href.substring(2); //remove the ..
	var module=href.split("/")[1];//module part of url
	var versionURL=currentURL.substring(0,currentURL.lastIndexOf(module)-1); // version part of the URL
	$(pThis).click(function(){window.top.location=versionURL+href;return false;}); 
}
//Rewrite Back and Next Links
$("#session_back_and_next a").each(function() { 
	rewriteLinks(this);
}); 

//Rewrite left navigation links
$("#session_navigation li a").each(function() {
	rewriteLinks(this);    
});