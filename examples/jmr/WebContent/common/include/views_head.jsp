<%@ page pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="myFn" uri="http://www.myfun.com/functions" %>

<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
<meta name="Author" content="XM TKB">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="${basePath}common/css/backplate.css" rel="stylesheet" type="text/css" />

<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>JMR</title>
<link href="${basePath}common/css/backplate.css" rel="stylesheet" type="text/css" />
<%@ include file="/common/include/header.jsp" %>
<%@ include file="/common/include/xheditor_util.jsp"%>
<script type="text/javascript" src="${basePath}common/js/datepick_TW/jquery.datepick.min.js"></script>
<script type="text/javascript" src="${basePath}common/js/datepick_TW/jquery.datepick-zh-TW.js"></script>
<link href="${basePath}common/js/datepick_TW/redmond.datepick.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${basePath}common/js/public.js"></script>
<script type="text/javascript" src="${basePath}common/js/jquery/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="${basePath}common/My97DatePicker/WdatePicker.js"></script> 
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

function changeone(id, val, object){
	if(val==''){
		alert("请选择！");
	}else{
		if(val==0){
			toModify(id, object);
		}else if(val==1){
			toDelete(id, object);
		}
	}
}

//转向新增
function toAdd(object){
	var frm = document.forms["baseForm"];
	frm.action="${basePath}" + object + ".do?act=toAdd";
	frm.submit();
	return false;
}

//转向修改
function toModify(id, object){
	var frm = document.forms["baseForm"];
	frm.action="${basePath}" + object + ".do?act=toModify&id="+id;
	frm.submit();
	return false;
}
 
//删除
function toDelete(id, object){	
	if(!confirm("Are you sure you want to delete?")){
		return false;
	}
	var frm = document.forms["baseForm"];
	frm.action="${basePath}" + object + ".do?act=del&id="+id;
	frm.submit();
	return false;
}

//提交验证
function subForm(array) {
	var frm = document.forms["baseForm"];
	for (var i = 0; i < array.length; i++) {
		var object = $("#"+array[i]).val().trim();
		if (object == "") {
			alert(array[i] + "不能为空");
			$("#"+array[i]).focus();
			return false;
		}
	}
	frm.submit();
}

//返回
function toBack(object) {
	var frm = document.forms["baseForm"];
	frm.action = "${basePath}" + object + ".do?act=find&marking=2";
	frm.submit();
	return false;
}

//清除
function toClear(array) {
	for (var i = 0; i < array.length; i++) {
		$("#"+array[i]).val("");
	}
}

function toReset(ids, values) {
	for (var i = 0; i < ids.length; i++) {
		$("#" + ids[i]).val(values[i]);
	}
}


</script>

</head>