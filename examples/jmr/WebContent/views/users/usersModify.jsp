<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
  <title>JMR</title>
  <!-- head start -->
	<%@ include file="/common/include/header.jsp" %>
  <!-- head end -->
<script type="text/javascript">
//提交验证
function subForm(){
	var frm = document.forms["baseForm"];
	
	var account = frm.account.value.trim();
	if(account == ""){
		alert("Account cannot be empty!");
		frm.account.focus();
		return false;
	}else{
		if(account.length<6){
      		alert("Account at least length of 6!");
         	frm.account.focus();
            return false;
      	}
	}
	
	var name = frm.name.value.trim();
	if(name == ""){
		alert("Name cannot be empty!");
		frm.name.focus();
		return false;
	}
	
	var password = frm.password.value.trim();
	if(password == ""){
		alert("Password can not be empty!");
		frm.password.focus();
		return false;
	}else{
      	if(password.length<6){
      		alert("Account at least length of 6!");
         	frm.password.focus();
            return false;
      	}else if(!isLetterAndNum(password)){
      	    alert("Passwords should contain both English and Chinese characters!");
         	frm.password.focus();
            return false;
      	}else if(password == account){
      	    alert("The password must not be the same as the account!");
         	frm.password.focus();
            return false;
      	}
	}
	frm.act.value="update";
	frm.submit();
	return false;
}

//验证是否包含英文和数字
function isLetterAndNum(obj){
    var reg=/^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i; 
	var objVal=obj.replace(/[^a-zA-Z0-9]+/g,"");
	return reg.test(objVal);
}

//清除
function toClear(){
	$('#account').val("");
	$('#password').val("");
	$('#name').val("");
}

//返回
function toBack(){
	var frm = document.forms["baseForm"];
	frm.act.value="find";
	frm.submit();
}

</script> 
</head>
<body>
<div class="wrapper">
	<%@ include file="/common/include/menu.jsp" %>
    
    <div class="rightContent">
    <span class="title">Account Management>Update</span><!-- End of title -->
    
    <div class="mainContainer">
    	<div class="function">
		1.In front of a<font color='red'>*</font>labeled as a required field.<br/>
		2.Minimum is 6 characters.<br/>
		3.Not the same password and account.<br/>
		4.Passwords must contain both English characters and numbers.
    	</div><!-- End of function -->
		<form action="${basePath}users.do" method="post" name="baseForm" />
			<input name="pageNo" value="${pageNo}" type="hidden"/>
			<input name="pageSize" value="${pageSize}" type="hidden"/>
			<input name="nameKey" value="${nameKey}" type="hidden"/>
			<input name="accountKey" value="${accountKey}" type="hidden"/>
			<input type="hidden" name="id" value="${us.id}"/>
			<input type="hidden" name="act" value=""/>
			<table width="634" class="tb2_green" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <th width="140"><span>*</span> account：</th>
	            <td width="462"><input type="text" name="account" maxlength="20" value="${us.account}" readonly/></td>
	          </tr>
	          <tr>
	            <th><span>*</span> name：</th>
	            <td bgcolor="#ececec"><input name="name" type="text" maxlength="25" value="${us.name}"/></td>
	          </tr>
			  <tr>
	            <th><span>*</span> password：</th>
	            <td><input name="password" type="password" maxlength="50"/></td>
	          </tr>
	          <tr>
	            <th><span>*</span> state：</th>
	            <td bgcolor="#ececec">
	            	<input type="radio" name="status" value="1"<c:if test="${us.status  == 1}">checked</c:if>>Enable</input>
	            	<input type="radio" name="status" value="0" <c:if test="${us.status == 0}">checked</c:if>>Disable</input>
	            </td>
	          </tr>
	
	        </table>
        </form>
		<div class="btnBlock">
			<input style="width:50px;" name="" value="Save" type="button" onclick='subForm();'/>&nbsp;&nbsp;
			<input style="width:50px;" name="" value="Clear" type="button" onclick='toClear();'/>&nbsp;&nbsp;
			<input style="width:50px;" name="" value="Back" type="button" onclick='toBack();'/></div><!-- End of btnBlock -->
    </div><!-- End of mainContainer -->
    </div><!-- End of rightContent -->
</div><!-- End of wrapper -->
</body>
</html>
