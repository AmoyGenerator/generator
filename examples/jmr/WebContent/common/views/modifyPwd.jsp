<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" " http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
  <title>JMR</title>
  <!-- head start -->
	<%@ include file="/common/include/header.jsp" %>
  <!-- head end -->
  <script type="text/javascript">
	//驗證
	function subForm(){
		var frm = document.forms["baseForm"];
		
		var oldPassWord=frm.oldPassWord.value.trim();
		if(oldPassWord ==""){
			alert("Old password can not be empty!");
			frm.oldPassWord.focus();
			return false;
		}
		var passWord=frm.passWord.value.trim();
		if(passWord==""){
			alert("Password can not be empty!");
			frm.passWord.focus();
			return false;
		}else{
			
			var loginId = $("#loginId").val();
			 if(passWord.length<6){
				alert("Password length of at least 6!");
				frm.passWord.focus();
				return false;
			}else if(!isLetterAndNum(passWord)){
				alert("Passwords must contain both English characters and numbers!");
				frm.passWord.focus();
				return false;
			}else if(passWord == loginId){
				alert("The password must not be the same as the account!");
				frm.passWord.focus();
				return false;
			}
		}

		var resPwd= frm.resPwd.value.trim();
		if(resPwd==""){
			alert("Password can not be empty!");
			frm.resPwd.focus();
			return false;
		}else if(resPwd!=passWord){
				alert("The password must be the same as the confirmation password!");
				return false;
			}
			
		
		frm.submit();
		return false;
	}
	
	
	$(function(){
		showMsg();	
	});
	
	function showMsg(){
		var msg='${msg}';
		if(msg !=''){
		alert(msg);
		}
	}
	
	//清除
	function clearValue(){
		$("#oldPassWord").val("");
		$("#passWord").val("");
		$("#resPwd").val("");
	}
	
	//密碼需包含英數字
	function isLetterAndNum(value){
	    var reg = /^(([A-Za-z]+[0-9]+)|([0-9]+[A-Za-z]+))[A-Za-z0-9]*$/;
	    return reg.test(value);
	}
</script>
</head>
  
  <body>
<div class="wrapper">
	<%@ include file="/common/include/menu.jsp" %>
    
    <div class="rightContent">
    <span class="title">Change password</span><!-- End of title -->
    <form action="${basePath}index.do?act=modifyPwdUpdate" method="post" name="baseForm">
    <div class="mainContainer">
    	<div class="function">
    		<table width='100%'  border=0>
				<tr>
					<td align='left' colspan="2">
			    	1.In front of a<font color='red'>*</font>labeled as a required field.<br/>
			    	2.The password must be the same as the confirmation password.<br/>
			    	3.Not the same password and account.<br/>
			    	4.Minimum is 6 characters.<br/>
			    	5.Passwords must contain both English characters and numbers.<br/>
			    	</td>
				</tr>
			</table>
    	</div><!-- End of function -->
		
		<table width="634" class="tb2_green" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <th width="140"><span>*</span> Old password：</th>
            <td width="462"><input style="width:120px;" name="oldPassWord" type="password" id="oldPassWord"  maxlength="10"/></td>
          </tr>
          <tr>
            <th><span>*</span> New password：</th>
            <td bgcolor="#ececec"><input style="width:120px;" name="passWord" type="password" id="passWord"  maxlength="10"/></td>
          </tr>
		  <tr>
            <th class="thLast"><span>*</span>Confirm new password:</th>
            <td><input style="width:120px;" name="resPwd" type="password" id="resPwd" maxlength="10"/></td>
          </tr>

        </table>
		
		<div class="btnBlock">
			<input style="width:50px;" name="" value="Save" type="button" onclick="subForm();" />&nbsp;&nbsp;
			<input style="width:50px;" name="" value="Clear" type="button" onclick="clearValue()" />&nbsp;&nbsp;
		</div><!-- End of btnBlock -->
    </div><!-- End of mainContainer -->
    </form>
    </div><!-- End of rightContent -->
</div><!-- End of wrapper -->
</body>
</html>

