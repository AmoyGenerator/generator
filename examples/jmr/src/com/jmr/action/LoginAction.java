package com.jmr.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;

import ssh.struts1.BaseAction;
import util.ConstantsUtil;
import util.PageUtil;
import app.j2ee.servlet.checkcode.CodeImgControl;
import app.j2ee.util.RequestUtil;

import com.jmr.entity.Users;
import com.jmr.service.IUsersService;

/**
 * 登录
 *
 */
public class LoginAction extends BaseAction{
	
	private IUsersService usersService;
	
	//重写
	public void setServlet(ActionServlet actionServlet){
		super.setServlet(actionServlet);
		usersService = (IUsersService) getCtx().getBean("usersService");//管理员管理
	}

	/**
	 * 跳转登录页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toLogin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("toLoginSuccess");
	}
	
	
	/**
	 * 登录验证
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward login(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//取得数据
		String sessionCode = (String) request.getSession().getAttribute(				
				CodeImgControl.SESSION_VERIFY_CODE); //从session中取得验证码
		String account = RequestUtil.getParaTrim(request, "account");
		String password = RequestUtil.getParaTrim(request, "password");
		String checkCode = RequestUtil.getParaTrim(request, "checkCode");
		
		//业务处理
		if("".equals(checkCode) || sessionCode== null || !sessionCode.equals(checkCode)){
			request.setAttribute("msg", "验证码错误");
			return mapping.findForward("loginFailure");
		}else if("".equals(account) || "".equals(password)){
			request.setAttribute("msg", "用户名或密码不能为空");
			return mapping.findForward("loginFailure");
		}	
		password = PageUtil.desEncrypt(password); //加密
		Users user = usersService.get(account, password);
		if(user != null){
			PageUtil.setSessionUser(request, user); //注入session
			/*开始菜单注入 session */
			Map map =  usersService.getFunctions();
			request.getSession().setAttribute(ConstantsUtil.SESSION_MENU_MAP, usersService.getFunctions());
			/*结束  菜单注入 session*/
			
		}else{
			request.setAttribute("msg", "您所输入的账号或密码错误，请重新输入！");
			return mapping.findForward("loginFailure");
		}	
		
		//注入数据，跳转到页面
		return mapping.findForward("loginSuccess");
	}

	
	
}

