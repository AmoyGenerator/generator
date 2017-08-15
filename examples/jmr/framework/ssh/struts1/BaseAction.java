package ssh.struts1;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.actions.DispatchAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import util.ConstantsUtil;



/**
 * struts1 分发基类
 *
 */
public class BaseAction extends DispatchAction{
	
	/*
	 * 注：因struts1 和 Servlet 一样，是线程不安全的， 所以最好不要配变量,可以在各个方法內用局部变量
	 */
	private WebApplicationContext ctx;
	protected String basePath; //根目录
	
    //初始化
    private void init(HttpServletRequest request){
    	String path = request.getContextPath();
		basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		request.setAttribute("basePath",basePath);
		request.setAttribute("projectName", ConstantsUtil.PROJECTNAME); //项目名称
	}
	
	
	protected ActionForward dispatchMethod(ActionMapping mapping, ActionForm from, HttpServletRequest request, HttpServletResponse response, String act) throws Exception {
		init(request);
		return super.dispatchMethod(mapping, from, request, response, act);		
	}	

	public WebApplicationContext getCtx() {
		return ctx;
	}
	
	
	//初始化上下文
	public void setServlet(ActionServlet actionServlet) {
		super.setServlet(actionServlet);
		ServletContext servletContext = actionServlet.getServletContext();
		this.ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);
	}
	
	public void setActionMessages(HttpServletRequest request, String errorKey, String errorName) {	
		ActionErrors errors = new ActionErrors();
	    errors.add(errorName, new ActionError(errorKey));
	    this.saveErrors(request, errors);
	}
	
	

}
