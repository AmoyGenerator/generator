<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-bean"%>
<!-- head start -->
<%@ include file="/common/include/header.jsp" %>
<!-- head end -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>系统错误信息－${projectName}</title>
</head>
<body>

<table  border=0 width="100%">
  		<tr>
	  		
	  		<td align='center'>
	  		<h1>系统错误信息</h1>
	  		<div>
				 <font color='red'>${error}</font><br/>
				<%
					String url = request.getHeader("Referer");
					if(url != null){
				%>
					<input type='button' value='返回'  onclick="window.location.href='<%= request.getHeader("Referer") %>'"/>
				<%		
					}
				%>
				<input type='button' value='登录'  onclick="window.location.href='${basePath}login.do?act=login'"/>

	  		</div>
			
			</td>
</tr>
</table>
</body>
</html>
