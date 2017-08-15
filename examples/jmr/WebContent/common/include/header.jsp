<%@ page pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="myFn" uri="http://www.myfun.com/functions" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);
%>

<c:if test="${empty SESSION_USER}">
    <c:redirect url="${basePath}login.do?act=toLogin"/>
</c:if>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
<meta name="Author" content="XM TKB">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="${basePath}common/css/backplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${basePath}common/js/public.js"></script>
<script type="text/javascript" src="${basePath}common/js/jquery/jquery-1.3.2.min.js"></script>

<script type="text/javascript">

$(document).ready(function(){
	var menu = "${menu}";
	if(menu != ""){
		fmenu1(menu);
	}
});
var s = "1";
function fmenu1(mid){
	if(s != mid){
		var count = $("li[name='" + mid + "']").length;
		var strAll = "ul li[name^='menu']";
		$(strAll).hide();
		for(var a = 1; a <= count; a++){
			var sid = "#"+ mid + a;
			$(sid).attr("style","display:block;");
		}
		s = "1";
	}else{
		var count = $("li[name='" + mid + "']").length;
		var strAll = "ul li[name^='menu']";
		$(strAll).hide();
		for(var a = 1; a <= count; a++){
			var sid = "#"+ mid + a;
			$(sid).attr("style","display:none;");
		}
		s = mid;
	}
	
	
}
</script>