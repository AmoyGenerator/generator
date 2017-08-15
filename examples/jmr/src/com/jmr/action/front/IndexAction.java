package com.jmr.action.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;

import ssh.struts1.BaseAction;

/**
 * 首页
 *
 */
public class IndexAction extends BaseAction{
	
	//重写
	public void setServlet(ActionServlet actionServlet){
		super.setServlet(actionServlet);
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
		return mapping.findForward("toIndex");
	}
	
	
}

