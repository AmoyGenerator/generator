package com.jmr.action;

import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import app.j2ee.util.RequestUtil;

import com.jmr.entity.Users;
import com.jmr.service.IUsersService;

import ssh.hibernate.page.ListPage;
import ssh.struts1.BaseAction;
import util.PageUtil;

/**
 * 类说明:管理者账号管理
 */

public class UsersAction extends AdminVerifyBaseAction{
	private IUsersService usersService;
	
	//重写
	public void setServlet(ActionServlet actionServlet){
		super.setServlet(actionServlet);
		usersService = (IUsersService) getCtx().getBean("usersService");//管理员账号管理
	}
	
	
	/**
	 * 首页查询
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward find(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
//1.    读取数据 		
		String nameKey = RequestUtil.getParaTrim(request, "nameKey");
		String accountKey = RequestUtil.getParaTrim(request, "accountKey");
		int pageNo = RequestUtil.getIntParameter(request, "pageNo", 1);
		int pageSize = RequestUtil.getIntParameter(request, "pageSize", 10);

//2.    调用服务		
		ListPage listPage = usersService.find(pageNo, pageSize, accountKey, nameKey);

//3.    转向设值		
		request.setAttribute("nameKey", nameKey);
		request.setAttribute("accountKey", accountKey);
		request.setAttribute("pageNo", pageNo);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("listPage", listPage);
		return mapping.findForward("toFindView");
	}
	
	
	/**
	 * 转向新增
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("toAddView");
	}
	
	
	/**
	 * 转向修改
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
//1.    获取数据		
		int id = RequestUtil.getIntParameter(request, "id", 0);//获取id
		String nameKey = RequestUtil.getParaTrim(request, "nameKey");
		String accountKey = RequestUtil.getParaTrim(request, "accountKey");
		int pageNo = RequestUtil.getIntParameter(request, "pageNo", 1);
		int pageSize = RequestUtil.getIntParameter(request, "pageSize", 10);
		
//2.    调用服务		
		Users us = usersService.get(id);//通過id取得对象
		
//3.    转向设值		
		request.setAttribute("nameKey", nameKey);
		request.setAttribute("accountKey", accountKey);
		request.setAttribute("pageNo", pageNo);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("us", us);//转向设值
		return mapping.findForward("toModifyView");
	}
	
	/**
	 * 删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
//1.    获取数据		
		int id = RequestUtil.getIntParameter(request, "id", 0);//获取id
		String nameKey = RequestUtil.getParaTrim(request, "nameKey");
		String accountKey = RequestUtil.getParaTrim(request, "accountKey");
		int pageNo = RequestUtil.getIntParameter(request, "pageNo", 1);
		int pageSize = RequestUtil.getIntParameter(request, "pageSize", 10);

//2.    调用服务
		if(id>0){
			usersService.delete(id);
		}
		ListPage listPage = usersService.find(pageNo, pageSize, accountKey, nameKey);
		
//3.	转向设值	
		request.setAttribute("nameKey", nameKey);
		request.setAttribute("accountKey", accountKey);
		request.setAttribute("pageNo", pageNo);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("listPage", listPage);
		return mapping.findForward("toFindAction");
	}
	
	/**
	 * 保存
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
//1.    获取数据		
		Users us = getModel(request,0);
		
//2.    调用服务	
		if(us != null){
			usersService.save(us);
		}
		return mapping.findForward("toFindAction");
	}
	
	
	/**
	 * 更新
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
//1.    获取数据	
		int id = RequestUtil.getIntParameter(request, "id", 0);//獲取id
		
//2.    调用服务		
		if(id>0){
			Users us = null;
			us = getModel(request,id);
			usersService.update(us);
		}
		return mapping.findForward("toFindAction");
	}
	
	
	/**
	 * 获取数据
	 * @param request
	 * @param method
	 * @return
	 */
	public Users getModel(HttpServletRequest request,int id){
//1.    获取数据  		
		int status = RequestUtil.getIntParameter(request, "status", 0);//状态  0-停用, 1-启用
		String account = RequestUtil.getParaTrim(request, "account");//管理者账号
		String password = RequestUtil.getParaTrim(request, "password");//密码 DES加密
		String name = RequestUtil.getParaTrim(request, "name"); //姓名
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
//2.    数据封装		
		Users us = null;
		if(id>0){
			us = usersService.get(id);
			us.setModifier(PageUtil.getSessionUser(request).getCreator());
			//us.setModifyDT(now);
		}else{
			us = new Users();
			us.setCreator(PageUtil.getSessionUser(request).getCreator());
//			us.setCreateDT(now);
		}
		us.setAccount(account);
		us.setPassword(PageUtil.desEncrypt(password));
		us.setName(name);
		us.setStatus(status);
		return us;
	}
	
	/**
	 * ajax验证账户唯一性
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward isExist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String account = RequestUtil.getParaTrimReturnNull(request, "account");
		PrintWriter out = response.getWriter();
		if(usersService.isAccountExist(account)){
			out.print("Y");
		}else{
			out.print("N");
		}
		out.close();
		return null;
	}
}
