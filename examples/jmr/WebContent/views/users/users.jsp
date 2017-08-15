<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
  <title>JMR</title>
  <!-- head start -->
	<%@ include file="/common/include/header.jsp" %>
  <!-- head end -->
<script type="text/javascript">
//查询
function find(){
	var frm = document.forms["baseForm"];
	frm.act.value = "find";
	frm.pageNo.value = 1;
	frm.pageSize.value = 10;
	frm.submit();
	return false;
}

//分页
function  gotoPage(pageNo){
	<c:if test="${!empty listPage}">
	if (pageNo == null || pageNo == "") return;
	if (isNaN(pageNo)) return;
	if (pageNo < 1) pageNo = 1;
	else if (pageNo > ${listPage.totalPageCount}) pageNo = ${listPage.totalPageCount};
	var thisForm = document.forms["baseForm"];	
	thisForm.pageNo.value = pageNo;
	thisForm.act.value = "find";
	thisForm.submit();
	</c:if>
}

//转向新增
function toAdd(){
	window.location.href="${basePath}users.do?act=toAdd";
}

//转向修改
function toModify(id){
	var frm = document.forms["baseForm"];
	frm.id.value=id;
	frm.act.value="toModify";
	frm.submit();
}

//删除
function del(id){
	if(!confirm("Are you sure you want to delete?")){
		return false;
	}
	var frm = document.forms["baseForm"];
	frm.id.value=id;
	frm.act.value="del";
	frm.submit();
}
</script> 
</head>
<body>
<div class="wrapper">
	<%@ include file="/common/include/menu.jsp" %>
    
    <div class="rightContent">
    <span class="title">Account Management</span><!-- End of title -->
    
    <div class="mainContainer">
    
    <form action="${basePath}users.do" method="post" name="baseForm" />
    	<div class="function">
        <img src="${basePath}common/images/icon_quiz.gif" />&nbsp;&nbsp;Query&nbsp;&nbsp;&nbsp;&nbsp;
        	account:<input type="text" name="accountKey" value="${accountKey}" style="width:120px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
        	name:<input type="text" name="nameKey" value="${nameKey}" style="width:120px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
        	<input style="width:60px;" name="" value="Search" type="button" onclick="find()"/>&nbsp;&nbsp;
        	<input style="width:50px;" name="" value="New" type="button" onclick="toAdd()"/>
        	</div><!-- End of function -->
			<input name="pageNo" value="${param.pageNo}" type="hidden"/>
			<input name="pageSize" value="${param.pageSize}" type="hidden"/>
			<input type="hidden" name="act" value=""/>
			<input type="hidden" name="id" value=""/>
	         <table width="634" class="tbl_green" border="0" cellspacing="0" cellpadding="0">
		      <tr>
	            <th>number</th>
	            <th>account</th>
	            <th>name</th>
	            <th>state</th>
	            <th>operation</th>
	          </tr>
	           <c:if test="${empty listPage.dataList }">
			    <tr><td colspan="5" align="center">Cannot find data!</td></tr>
			 	</c:if>
			            
				<c:if test="${!empty listPage.dataList }">
				  <c:set var="i" value="1" />
					<c:if test="${listPage.currentPageNo>0}">
						<c:set var="i" value="${(listPage.currentPageNo-1)*listPage.currentPageSize +1 }"/>
					</c:if>
				  
				   <c:forEach items="${listPage.dataList}" var="us" varStatus="vs"> 
				   		 <tr class=${vs.count%2==1?"odd_row":"even_row" }>
	                        <td align="center">&nbsp;${(listPage.currentPageNo-1)*listPage.currentPageSize + vs.count} </td>
							<td align="center">&nbsp;${us.account}</td>
							<td align="center">&nbsp;${us.name}</td>
							<td align="center">&nbsp;
								<c:choose>
									<c:when test="${us.status == 0}">
										disable
									</c:when>
									<c:otherwise>
										enable
									</c:otherwise>
								</c:choose>
							</td>
							<td align="center">
								<input type="button" value="update" onclick="toModify('${us.id}');"/>
								<input type="button" value="delete" onclick="del('${us.id}');"/>
							</td>
				   		</tr>
				   </c:forEach>
				</c:if>
	        </table>
	        
	        <div><!-- page start -->
	        	<c:if test="${!empty listPage.dataList}" >
					<%@include file="/common/include/listPageJstl_select2.jsp"%>				
				</c:if>	
	        </div><!-- page end -->
	     </form>
    </div><!-- End of mainContainer -->
    </div><!-- End of rightContent -->
</div><!-- End of wrapper -->
</body>
</html>

