// JavaScript Document for public

//一些基本的驗證。2008-12-18 

//去除左右空格
String.prototype.trim = function () {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};
//去左空格
String.prototype.ltrim = function () {
	return this.replace(/(^\s*)/g, "");
};  
//去右空格
String.prototype.rtrim = function () {
	return this.replace(/(\s*$)/g, "");
};

//驗證是否手機號碼
String.prototype.isMobile = function () {
	return (/^(?:13\d|15[89])-?\d{5}(\d{3}|\*{3})/.test(this.Trim()));
};


//驗證是否電話號碼
String.prototype.isTel = function () {
	return (/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?/.test(this.Trim()));
};

//驗證是否郵箱
function isEmail(email) {
	var reg = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
	return reg.test(email);
}
//驗證是否郵編
function isZip(zip) {
	var reg = /^\d{6}$/;
	return reg.test(zip);
}
//驗證是否數字
function isNumber(num) {
	return !isNaN(num);
}

//驗證是否是整數
function isInteger(str){
var regu = /^[-]{0,1}[0-9]{1,}$/;
return regu.test(str);
}

//驗證是否是正整數
function isPositiveInteger( str ){
	var regu = /^[0-9]{1,}$/;
	return regu.test(str);
}
function check(name) {
            var pattern=/[\/\\\:\*\?\"\<\>\|]/;
            
            if(name.search(pattern)!=-1) {
                alert("invalidate");
                return false;
            }
            return true;
        }
//驗證名稱是否含有非法字符
function isUnlawful(name){
	var reg = /[\/\\\:\*\?\"\<\>\|]/;
	return reg.test(name);
}

//驗證名稱是否含有中文
function isCN(name){
    var fn = get_fileName(name);
	var reg = /^[A-Za-z0-9]+$/;
	return reg.test(fn);
}

/////////////////////////////////////////////////////////////////////////////////表單驗證
//驗證年齡
function checkAge(age) {
	var age = document.getElementById("age").value.trim();
	if (!isNumber(age)) {
		//alert("請輸入正確的年齡，須為數字");
		return false;
	}
	if (age < 5 || age > 200) {
		//alert("年齡須在（5－200）之間");
		return false;
	}
	return true;
}

//驗證郵箱
function checkEmail(email) {
	var email = document.getElementById("email").value.trim();
	if (!isEmail(email)) {
		//alert("請輸入正確的郵箱格式，如：yangshuo@163.com");
		return false;
	}
	return true;
}

//驗證郵編
function checkZip(zip) {
	var zip = document.getElementById("zip").value.trim();
	if (!isZip(zip)) {
		//alert("請輸入正確的郵編，須為6位數字");
		return false;
	}
	return true;
}

//驗證手機號碼
function checkMobile(mobile) {
	var mobile = document.getElementById("mobile").value.trim();
	if (!mobile.isMobile()) {
		alert("\u8acb\u8f38\u5165\u6b63\u78ba\u7684\u624b\u6a5f\u865f\u78bc\uff0c\u9808\u70ba11\u4f4d\u6578\u5b57\uff01");
		return false;
	}
	return true;
}

//驗證電話號碼
function checkTel(tel) {
	var tel = document.getElementById("tel").value.trim();
	if (!tel.isTel()) {
		alert("\u8acb\u8f38\u5165\u6b63\u78ba\u7684\u96fb\u8a71\u865f\u78bc\u683c\u5f0f\uff0c\u5982\uff1a021-8888888");
		return false;
	}
	return true;
}

//驗證身份證號碼 copy
//20080314 ivor add
function chkIDdata(obj){
  var sid = obj.value.trim();
  //alert("chkdata sid : "+sid);
  var alphabet = new Array(36), little = new Array(36);
            var number = new Array(10);
            alphabet[10] = 'A'; little[10] = 'a';
            alphabet[11] = 'B'; little[11] = 'b';
            alphabet[12] = 'C'; little[12] = 'c';
            alphabet[13] = 'D'; little[13] = 'd';
            alphabet[14] = 'E'; little[14] = 'e';
            alphabet[15] = 'F'; little[15] = 'f';
            alphabet[16] = 'G'; little[16] = 'g';
            alphabet[17] = 'H'; little[17] = 'h';
            alphabet[18] = 'J'; little[18] = 'j';
            alphabet[19] = 'K'; little[19] = 'k';
            alphabet[20] = 'L'; little[20] = 'l';
            alphabet[21] = 'M'; little[21] = 'm';
            alphabet[22] = 'N'; little[22] = 'n';
            alphabet[23] = 'P'; little[23] = 'p';
            alphabet[24] = 'Q'; little[24] = 'q';
            alphabet[25] = 'R'; little[25] = 'r';
            alphabet[26] = 'S'; little[26] = 's';
            alphabet[27] = 'T'; little[27] = 't';
            alphabet[28] = 'U'; little[28] = 'u';
            alphabet[29] = 'V'; little[29] = 'v';
            alphabet[32] = 'W'; little[32] = 'w';
            alphabet[30] = 'X'; little[30] = 'x';
            alphabet[31] = 'Y'; little[31] = 'y';
            alphabet[33] = 'Z'; little[33] = 'z';
            alphabet[34] = 'I'; little[34] = 'i';
            alphabet[35] = 'O'; little[35] = 'o';
            number[0] = '0';    number[1] = '1';
            number[2] = '2';    number[3] = '3';
            number[4] = '4';    number[5] = '5';
            number[6] = '6';    number[7] = '7';
            number[8] = '8';    number[9] = '9';
    //alert("sid.charCodeAt(0) : "+sid.charCodeAt(0));       
   /* */
   
   if(sid.charCodeAt(0) < 65 || sid.charCodeAt(0)> 90)
    {
    alert("您身份證號碼第一位數字輸入的不是大寫英文字")
    return(false)
    }
    

    if(sid.charCodeAt(1)<48 || sid.charCodeAt(1)>50)

    {
    alert("您輸入的第二碼不是1或2")
     return(false)

    }
	for(j=2; j<10; j++)
    if(sid.charCodeAt(j)<48 || sid.charCodeAt(j)>58){
	   alert("你輸入的2~9不是數字")
	     return(false)
	   }
	
	if(sid.charAt(9)=="")     
	 {
	   alert("請輸入完整身份證號碼共10位數字")
	     return(false)
	   }
	 
	 var s=sid.substring(0,1);
     for(i=10; i<=alphabet.length; i++)
       if(s==alphabet[i])       
         s=i         
      c=s.toString()
     
  
   a1=c.substr(0,1)
   a2=c.substr(1,1)

  y=parseInt(a1)+parseInt(a2*9)+parseInt(sid.charAt(1)*8)+parseInt(sid.charAt(2)*7)+parseInt(sid.charAt(3)*6)+parseInt(sid.charAt(4)*5)+parseInt(sid.charAt(5)*4)+parseInt(sid.charAt(6)*3)+parseInt(sid.charAt(7)*2)+parseInt(sid.charAt(8)*1)
     
	      mod=y%10	 
		       
		if((10-mod) == parseInt(sid.charAt(9))){
		      //alert("恭喜您輸入身份證號碼正確") 
		      return true;
		      }		  
		 else if(mod==0 && parseInt(sid.charAt(9))==0){
               //    alert("恭喜您輸入身份證號碼正確")
               return true;
               }
           else{
               alert("您輸入的身分證號碼不正確");
               return false;
           }

}

 
//選擇復選框
function checkSelect(formObj, eleName) {
	var checked = false;
	for (var i = 0; i < formObj.elements.length; i++) {
		if ((formObj.elements[i].name == eleName) && (formObj.elements[i].checked)) {
			checked = true;
			break;
		}
	}
	if (!checked) {
		alert("請選擇要刪除的紀錄!");
	}
	return checked;
}

function checkSelectNoAlert(formObj, eleName) {
	var checked = false;
	for (var i = 0; i < formObj.elements.length; i++) {
		if ((formObj.elements[i].name == eleName) && (formObj.elements[i].checked)) {
			checked = true;
			break;
		}
	}
	return checked; 
}

 //全選或取消選擇復選框
function selectOrNotAll(formObj, eleName, selectOrNotSelect) {
	for (var i = 0; i < formObj.elements.length; i++) {
		if (formObj.elements[i].name == eleName) {
			formObj.elements[i].checked = selectOrNotSelect;
		}
	}
}

//反選復選框
function reverseSelect(formObj, eleName) {
	for (var i = 0; i < formObj.elements.length; i++) {
		if (formObj.elements[i].name == eleName) {
			formObj.elements[i].checked = !formObj.elements[i].checked;
		}
	}
}

//獲取文件副檔名
function get_ext(f_path) {
	var ext = "";
	if (f_path != null && f_path.trim().length > 0) {
		f_path = f_path.toLowerCase().trim();
		ext = f_path.substring(f_path.lastIndexOf(".") + 1, f_path.length);
	}
	return ext;
}

//獲取文件名稱
function get_fileName(f_path){
    var fn = "";
    if(f_path != null && f_path.trim().length > 0){
		//var i = f_path.lastIndexOf("\\"); //返回文件名稱最后出現'\'的索引
	    //var j = f_path.lastIndexOf("."); //返回文件名稱最后出現'.'的索引
	    fn = f_path.substring(f_path.lastIndexOf("\\")+1,f_path.lastIndexOf(".")); //取得文件名稱
    }
    return fn;
}

//獲取文件名稱+副檔名
function get_fileNameAndExt(f_path){
	var name="";
	if(f_path!=null && f_path.trim().length>0){
		var f = f_path.lastIndexOf("/");
		name=f_path.substring(f+1,f_path.length);
	}
	return name;
}

/*
驗證文件副檔名
f_path 文件名 ，  accept_ext 副檔名數組
*/
function chk_ext(f_path, accept_ext){
    var ext = get_ext(f_path);
    //根據需求定制
	//var accept_ext = new Array("png", "jpeg", "jpg", "gif");
	var flag = false;
	if (ext != "") {
		for (var i = 0; i < accept_ext.length; i++) {
			if (ext == accept_ext[i]) {
				flag = true;
			}
		}
	}
	return flag;
}

//驗證圖片文件副檔名
function chk_ext_img(f_path) {
	var ext = get_ext(f_path);
    //根據需求定制
	var accept_ext = new Array("png", "jpeg", "jpg", "gif");
	var flag = false;
	if (ext != "") {
		for (var i = 0; i < accept_ext.length; i++) {
			if (ext == accept_ext[i]) {
				flag = true;
			}
		}
	}
	return flag;
}

//驗證圖片文件副檔名 jpg,gif
function chk_ext_imgJG(f_path) {
	var ext = get_ext(f_path);
    //根據需求定制
	var accept_ext = new Array("jpg", "gif");
	var flag = false;
	if (ext != "") {
		for (var i = 0; i < accept_ext.length; i++) {
			if (ext == accept_ext[i]) {
				flag = true;
			}
		}
	}
	return flag;
}
//驗證zip文件副檔名
function chk_ext_zip(f_path) {
	var ext = get_ext(f_path);
    //根據需求定制
	var accept_ext = new Array("zip");
	var flag = false;
	if (ext != "") {
		for (var i = 0; i < accept_ext.length; i++) {
			if (ext == accept_ext[i]) {
				flag = true;
			}
		}
	}
	return flag;
}

//驗證flv文件副檔名
function chk_ext_flv(f_path) {
	var ext = get_ext(f_path);
    //根據需求定制
	var accept_ext = new Array("flv");
	var flag = false;
	if (ext != "") {
		for (var i = 0; i < accept_ext.length; i++) {
			if (ext == accept_ext[i]) {
				flag = true;
			}
		}
	}
	return flag;
}


//驗證其他文件副檔名
function chk_ext_file(f_path) {
	var ext = get_ext(f_path);
    //根據需求定制
	var accept_ext = new Array("doc", "ppt", "xsl", "rar", "pdf");
	var flag = false;
	if (ext != "") {
		for (var i = 0; i < accept_ext.length; i++) {
			if (ext == accept_ext[i]) {
				flag = true;
			}
		}
	}
	return flag;
}

//XuShaoWei驗證其他文件檔名為CVS格式
function chk_ext_filepath(f_path) {
	var ext = get_ext(f_path);
    //根據需求定制
	var accept_ext = new Array("csv");
	var flag = false;
	if (ext != "") {
		for (var i = 0; i < accept_ext.length; i++) {
			if (ext == accept_ext[i]) {
				flag = true;
			}
		}
	}
	return flag;
}

//驗證瀏覽器
function checkBrowser() {
	if (window.navigator.userAgent.indexOf("MSIE") >= 1) {
		return "IE";
	} else {
		if (window.navigator.userAgent.indexOf("Firefox") >= 1) {
			return "FF";
		} else {
			return "other";
		}
	}
}

//驗證瀏覽器是IE6，還是IE7
function checkIE(){
    var b_v = navigator.appVersion; 
    var IE6 = b_v.search(/MSIE 6/i) != -1; 
    var IE7 = b_v.search(/MSIE 7/i) != -1; 
    if(IE6){
        return "IE6";
    }else if(IE7){
        return "IE7";
    }else{
        return "other";
    }
}


/* 
用途：字元1是否以字串2開始 
輸入：str1：字串；str2：被包含的字串 
返回：如果通過驗證返回true,否則返回false

*/
function isFirstMatch(str1, str2) {
	var index = str1.indexOf(str2);
	if (index == 0) {
		return true;
	}
	return false;
}

//調整圖片寬度
function changeImgWidth(obj, lastWidth){
     if(obj.width>lastWidth) obj.width=lastWidth;
}

//驗證是否由字母或數字組成
function isLetterOrNum(value){
	var reg =/^[A-Za-z0-9]+$/;
	return reg.test(value);
}

//驗證是否由6位字母或數字組成
function idOrPassword(value){
	var reg =/^[A-Za-z0-9]{6,10}$/;
	return reg.test(value);
}
// 驗證表單字段值是否為空
function isFieldEmpty(obj, msg) {
	if (obj.value.trim() == "") {
		alert(msg);
		try {
			obj.focus();
		} catch(e) {}
		return true;
	}
	return false;
}
// 字段是否超長
function isFieldLengthExceed(obj, maxlength, msg) {
	if (obj.value.trim() > maxlength) {
		alert(msg);
		try {
			obj.focus();
		} catch(e) {}
		return true;
	}
	return false;
}
//判斷上傳圖片名是否為字母、數字、-和_
// 參數：physicalPath 文件物理路徑
// 參數：fillIn 是否為填
function checkADAndUnderline(physicalPath,fillIn){
    var pic = physicalPath;
    //表達式
    var reg=/^(\w|-)+$/;
    var allowedSuffix = new Array("jpg","jpeg","gif");
    
    if(fillIn){
    	if(pic == ""){
            alert("必須選擇上傳圖片！");
            return false;
        }
    }
    if(pic != ""){
        var startIndex = pic.lastIndexOf('\\') + 1;
        var endIndex = pic.lastIndexOf(".");
	    var fileName = pic.substring(startIndex, endIndex);
         if(!reg.test(fileName)){
             alert("上傳的圖片名只能包涵字母、數字、-和_！");
             return false;
         }
         var suffix = pic.substring(endIndex+1, pic.length).toLowerCase();
         if((suffix != allowedSuffix[0]) && (suffix != allowedSuffix[1]) && (suffix != allowedSuffix[2])){
            alert("只允許上傳jpg,jpeg,gif后綴的文件！");
            return false;
         }else{
         	return true;
         }
    }else{
    	 return true;
    }
}

//驗證是否由英文、數字、-、_組成
function isLetterNumHyphen(value){
	var reg =/^[A-Za-z0-9-_]+$/;
	return reg.test(value);
}

// 移除內容為空白的子元素
function clearBlank(obj) {
	var nodes = obj.childNodes;
	for( var i = 0;i < nodes.length; i++){
		var node = nodes[i];
		if (node.nodeType == 3) {
			node.parentNode.removeChild(node);
		}
	}
}

function isUrl(url) {
	return url.indexOf("http://") == 0;
}

//窗口最大化
function windowInit(){
   width=screen.availWidth; 
   height=screen.availHeight; 
   window.moveTo(0,0); 
   window.resizeTo(width,height);
} 