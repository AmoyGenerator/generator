package com.jmr.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import ssh.struts1.ActionManager;
import app.j2ee.util.RequestUtil;
import com.jmr.entity.Book;
import com.jmr.service.IBookService;

public class BookAction extends AdminVerifyBaseAction{
	private IBookService service;


	public void setServlet(ActionServlet actionServlet){
		super.setServlet(actionServlet);
		service = (IBookService) getCtx().getBean("bookService");
	}

	/**
	 * Query
	 */
	public ActionForward find(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return ActionManager.find(mapping, form, request, response, service, Book.class);
	}


	/**
	 * Jump to new
	 */	
	public ActionForward toAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return ActionManager.toAdd(mapping, form, request, response, Book.class);
	}

	/**
	 * Save
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionManager.save(mapping, form, request, response, basePath, service, Book.class);
		int pageNo = RequestUtil.getIntParameter(request, "pageNo", 1);
		int pageSize = RequestUtil.getIntParameter(request, "pageSize", 10);
		response.sendRedirect(basePath + "/book.do?act=find&marking=1&pageNo=" + pageNo + "&pageSize=" + pageSize);
		return null;
	}

	/**
	 * Jump to update
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toModify(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		return ActionManager.toModify(mapping, form, request, response, service);
	}


	/**
	 * Update
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward update(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
	    ActionManager.update(mapping, form, request, response, basePath, service, Book.class);
		int pageNo = RequestUtil.getIntParameter(request, "pageNo", 1);
		int pageSize = RequestUtil.getIntParameter(request, "pageSize", 10);
		response.sendRedirect(basePath + "/book.do?act=find&marking=1&pageNo=" + pageNo + "&pageSize=" + pageSize);
		return null;
	}


	/**
	 * Delete
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionManager.del(mapping, form, request, response, basePath, service);
		int pageNo = RequestUtil.getIntParameter(request, "pageNo", 1);
		int pageSize = RequestUtil.getIntParameter(request, "pageSize", 10);
		response.sendRedirect(basePath + "/book.do?act=find&marking=1&pageNo=" + pageNo + "&pageSize=" + pageSize);
		return null;
	}

}
 