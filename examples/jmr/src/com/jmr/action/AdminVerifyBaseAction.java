package com.jmr.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ssh.struts1.BaseAction;
import util.PageUtil;

import com.jmr.entity.Users;






/**
 * 后台权限验证器
 *
 */
public class AdminVerifyBaseAction extends BaseAction{	
	
	
	
//	无需权限的功能列表
	private static List<String> exclusive = new ArrayList<String>();
	
	static {
		exclusive.add("index.do?act=toIndex");
		exclusive.add("index.do?act=logOut");
	}
	
	protected ActionForward dispatchMethod(ActionMapping mapping, ActionForm from, HttpServletRequest request, HttpServletResponse response, String act) throws Exception {
		Users userSession = PageUtil.getSessionUser(request);		
		String queryString = request.getQueryString();
		System.out.println(queryString);
		if(queryString != null && queryString.indexOf("&menu=") !=-1){
			int startIndex = queryString.indexOf("&menu=");//
			int endIndex =  queryString.indexOf("_", startIndex);
			request.getSession().setAttribute("menu", queryString.substring(startIndex+6, endIndex));
		}else if(queryString != null && queryString.indexOf("act=modifyPassword") !=-1){
			request.getSession().setAttribute("menu", "");
			
		}
		
		
		if(userSession == null){
			request.setAttribute("msg", "Administrator Session timeout, please sign in again!");
			request.getSession().invalidate();//清除所有Session
//			response.sendRedirect(basePath + "/login.do?act=toLogin");
			request.getRequestDispatcher("/login.do?act=toLogin").forward(request, response);
		    return null;
		}else{ //管理员			
			
			
			return super.dispatchMethod(mapping, from, request, response, act);			
		}
		
	}





}
