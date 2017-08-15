package com.jmr.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;

import util.PageUtil;

import app.j2ee.util.RequestUtil;

import com.jmr.entity.Users;
import com.jmr.service.IUsersService;

/**
 * 登录
 *
 */
public class IndexAction extends AdminVerifyBaseAction{
	
	private IUsersService usersService;
	
	//重写
	public void setServlet(ActionServlet actionServlet){
		super.setServlet(actionServlet);
		usersService = (IUsersService) getCtx().getBean("usersService");//管理员管理
	}

	/**
	 * 跳转首页 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toIndex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("toIndexSuccess");
	}
	
	/**
	 * login out
	 * @return
	 * @throws Exception
	 */
	public ActionForward logOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PageUtil.removeSessionUser(request);
		request.getSession().invalidate();//清除所有session
		request.getRequestDispatcher("/login.do?act=toLogin").forward(request, response);
	    return null;
	}
	
	/**
	 * change password
	 * @return
	 * @throws Exception
	 */
	public ActionForward modifyPassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	    return mapping.findForward("modifyPassword");
	}
	
	/**
	 * 更新密码
	 * @return
	 * @throws Exception
	 */
	public ActionForward modifyPwdUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		  String passWord = RequestUtil.getParaTrim(request, "passWord");
		  String oldPassWord = RequestUtil.getParaTrim(request, "oldPassWord");
		  Users userAccount = PageUtil.getSessionUser(request);
		  
		  if(userAccount.getAccount().equals(passWord)){
			  request.setAttribute("msg", "密码不得与账号相同");
			  return mapping.findForward("modifyPassword");
		  }
		  Users users = usersService.getUsers(userAccount.getAccount());
		  try {
			  String loginPassword =users.getPassword();
			  String encrypted = PageUtil.desDecrypt(loginPassword); //解密
			  if (oldPassWord.equals(encrypted)) {
				  
						String hexedPwd = PageUtil.desEncrypt(passWord); //加密
						
						users.setPassword(hexedPwd);
						userAccount.setPassword(hexedPwd);
						
						usersService.update(users);
						
						request.setAttribute("msg", "修改密码成功");
					
			}else {
				
				request.setAttribute("msg", "旧密码错误,修改密码失败");
				return mapping.findForward("modifyPassword");
			}
			  
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		 

	    return mapping.findForward("modifyPassword");
	}
	
}

