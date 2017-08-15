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
      		alert("Account length of at least 6!");
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
      		alert("Password length of at least 6!");
         	frm.password.focus();
            return false;
      	}else if(!isLetterAndNum(password)){
      	    alert("Passwords must contain both English characters and numbers!");
         	frm.password.focus();
            return false;
      	}else if(password == account){
      	    alert("The password must not be the same as the account!");
         	frm.password.focus();
            return false;
      	}
	}
	
	$.get("${basePath}users.do?act=isExist&account=" + account,function(data){
		if(data == "Y"){
			alert("Account already exists!");
			frm.account.value="";
		}else{
			frm.submit();
		}
	});
	
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
	window.location.href='${basePath}users.do?act=find';
}

</script> 
</head>
<body>
<div class="wrapper">
	<%@ include file="/common/include/menu.jsp" %>
    
    <div class="rightContent">
    <span class="title">Account Management>New</span><!-- End of title -->
    
    <div class="mainContainer">
    	<div class="function">
		1.In front of a<font color='red'>*</font>labeled as a required field.<br/>
		2.Account, password length of at least 6<br/>
		3.The password must not be the same as the account.<br/>
		4.Passwords must contain both English characters and numbers.
    	</div><!-- End of function -->
		<form action="${basePath}users.do?act=save" method="post" name="baseForm" />
		<table width="634" class="tb2_green" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <th width="140"><span>*</span> account：</th>
            <td width="462"><input type="text" name="account" maxlength="20" id="account"/></td>
          </tr>
          <tr>
            <th><span>*</span> name：</th>
            <td bgcolor="#ececec"><input name="name" type="text" maxlength="25" id="name"/></td>
          </tr>
		  <tr>
            <th><span>*</span> password：</th>
            <td><input name="password" type="password" maxlength="50" id="password"/></td>
          </tr>
          <tr>
            <th><span>*</span> state：</th>
            <td bgcolor="#ececec">
            	<input type="radio" name="status" value="1" checked>enable</input>
            	<input type="radio" name="status" value="0" >disable</input>
            </td>
          </tr>
        </table>
      
		<div class="btnBlock">
			<input style="width:50px;" name="" value="Save" type="button" onclick='subForm();'/>&nbsp;&nbsp;
			<input style="width:50px;" name="" value="Clear" type="button" onclick='toClear();'/>&nbsp;&nbsp;
			<input style="width:50px;" name="" value="Back" type="button" onclick='toBack();'/></div><!-- End of btnBlock -->
     </form>
    </div><!-- End of mainContainer -->
    </div><!-- End of rightContent -->
</div><!-- End of wrapper -->
</body>
</html>
