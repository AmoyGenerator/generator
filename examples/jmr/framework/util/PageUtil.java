package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import ssh.spring.DeployInfoUtil;
import ssh.spring.SpringContextUtil;
import app.other.encrypt.DesEncrypt;

//import com.jmr.model.Codedesc;
import com.jmr.entity.Users;


/**
 * 页面工具类
 */
public class PageUtil {
	
	/**
	 * 根据当前时间判断是否下架上架
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static String getPublish(Date startDate, Date endDate) {
		Date now = new Date();
		if (now.after(startDate) && now.before(endDate)) {
			return "上架";
		} else {
			return "下架";
		}
	}

	public static String regularText(java.lang.String content,int count,java.lang.String c){
		StringBuffer cont = new StringBuffer();
		int j=0;
		int i=0;
		if(content.getBytes().length > count){
			for(;i<content.length();i++){
				String simpl = content.substring(i, i+1);
				byte[] byteContent= simpl.getBytes();
				if(byteContent.length==1){
					j++ ;
				}else{
					j=j+2;
				}
				if(j >= count){
					break;
				}
				 
			}
			if(j==count){
				cont.append(content.substring(0, i+1));
				cont.append(c);
			}else{
				cont.append(content.substring(0, i));
				cont.append(c);
			}
		}else{
			cont.append(content);
		}
		return cont.toString();
	}
	/**
	 * 加密
	 * 
	 * @param password
	 * @return
	 */
	public static String desEncrypt(String password) {
		/**
		 * 密码不区分大小写，统一为小写 
		 */
		password = password.toLowerCase();

		String desKey = DeployInfoUtil.getDesKey();
		String desModeAndPadding = DeployInfoUtil.getDesModeAndPadding();
		return DesEncrypt.desEncrypt(password, desKey, desModeAndPadding);
	}

	/**
	 * 解密
	 * 
	 * @param password
	 * @return
	 */
	public static String desDecrypt(String password) {
		String desKey = DeployInfoUtil.getDesKey();
		String desModeAndPadding = DeployInfoUtil.getDesModeAndPadding();
		return DesEncrypt.desDecrypt(password, desKey, desModeAndPadding);
	}
	
	/**
	 */
	public static String getAfter4Bit(int temp,int len){
		String temp1 = String.valueOf(temp);
		StringBuffer sb = new StringBuffer();
		int totalLen = temp1.length();
		int i = len-totalLen;
		for(int j=0;j<i;j++){
			sb.append("0");
		}
		sb.append(temp1);
		return sb.toString();
	}
	
	/**
	 * 根据授权卡号，生成加密密码
	 * @param carId
	 * @return
	 */
	public static String getByCarId(String carId){
		if(!carId.equals("")){
			return desEncrypt(carId);
		}else{
			return "";
		}
	}
	
	/**
	 * 分割字符串
	 * @param tidStr
	 * @return
	 */
	public List getCid(String tidStr){
		String[] tidArr = tidStr.split(",");
		String result ="";
		List whereTid = new ArrayList();
		for (int i = 0; i < tidArr.length; i++) {
			if(!"".endsWith(tidArr[i])){
				whereTid.add(Integer.parseInt(tidArr[i]));
			}
		}
		return whereTid;
	}
	
	
	/**
	 * 生成小时下拉
	 * @return
	 */
	public static List getHourse(){
		String str = "";
		List list = new ArrayList();
		for (int i = 0; i < 24; i++) {
			if(i<10){
				str = "0"+i;
			}else{
				str = String.valueOf(i);
			}
			list.add(str);
		}
		return list;
	}
	
	/**
	 *  生成分钟下拉
	 * @return
	 */
	public static List getMinute(){
		String str = "";
		List list = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if(i<10){
				str = "0"+i;
			}else{
				str = String.valueOf(i);
			}
			list.add(str);
		}
		return list;
	}
	
	
	
	

	// 管理员Session
	public static Users getSessionUser(HttpServletRequest request) {
		return (Users) request.getSession().getAttribute(
				ConstantsUtil.SESSION_USER);
	}

	public static void setSessionUser(HttpServletRequest request, Users user) {
		request.getSession().setAttribute(ConstantsUtil.SESSION_USER, user);
	}

	public static void removeSessionUser(HttpServletRequest request) {
		request.getSession().removeAttribute(ConstantsUtil.SESSION_USER);
	}
	
	/**
	 * 根据code获取notes name
	 * @param code
	 * @return
	 */
	/**
	public static String getNotesName(String code){
		INotesService notesService = (INotesService) SpringContextUtil.getApplicationContext().getBean("notesService");
		Codedesc codeDesc = notesService.getCodedescByCode(code);
		return codeDesc.getName();
	}
	*/
}
