package ssh.struts1;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ssh.hibernate.page.ListPage;
import util.BeanUtilsEx;
import app.j2ee.util.RequestUtil;

import com.jmr.service.IBeanService;

public class ActionManager {


	public static ActionForward find(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, IBeanService service, Class<?> clazz) throws Exception {
		Map<String, Object> queryMap = ParamsManager.createParams(clazz, request);
		int pageNo = RequestUtil.getIntParameter(request, "pageNo", 1);
		int pageSize = RequestUtil.getIntParameter(request, "pageSize", 10);
		ListPage listPage = service.find(pageNo, pageSize, queryMap);
		ParamsManager.forwordParams(request, queryMap);
		request.setAttribute("listPage", listPage);
		return mapping.findForward("toFind");
	}

	public static ActionForward toAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, Class<?> clazz) throws Exception {
		int pageNo = RequestUtil.getIntParameter(request, "pageNo", 1);
		int pageSize = RequestUtil.getIntParameter(request, "pageSize", 10);
		request.setAttribute("pageNo", pageNo);
		request.setAttribute("pageSize", pageSize);

		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String name = field.getName();
			Object param = RequestUtil.getParaTrim(request, name);
			request.setAttribute(name, param);	
		}

		return mapping.findForward("toAdd");
	}

	public static void save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, String basePath, IBeanService service, Class<?> clazz) throws Exception {
		Object object = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String name = field.getName();
			Object param = RequestUtil.getParaTrim(request, name);
			if(param != null){
				field.setAccessible(true);
				String firstLetter = name.substring(0, 1).toUpperCase();
				String setMethodName = "set" + firstLetter
						+ name.substring(1);
				Method setMethod = clazz.getMethod(setMethodName,
						new Class[] { field.getType()});
				if(setMethod != null){
					//BeanUtils.setProperty(object, name, param);
					BeanUtilsEx.setProperty(object, name, param);
					//setMethod.invoke(object, new Object[] {field.getType().cast(param)});
				}
			}
		}

		//进行保存
		service.save(object);
	}

	/**
	 * 转向修改
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public static ActionForward toModify(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response, IBeanService service){

		Integer id = RequestUtil.getIntParameter(request, "id", -1);
		int pageNo = RequestUtil.getIntParameter(request, "pageNo", 1);
		int pageSize = RequestUtil.getIntParameter(request, "pageSize", 10);
		//根据id进行查找
		Object object = service.getById(id);
		if(object != null){
			System.err.println("---" + object.getClass().toString().toLowerCase());
			request.setAttribute(object.getClass().getSimpleName().toLowerCase(), object);
			request.setAttribute("id", id);
			request.setAttribute("pageNo", pageNo);
			request.setAttribute("pageSize", pageSize);
		}
		return mapping.findForward("toModify");
	}

	public static void update(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response, String basePath, IBeanService service, Class<?> clazz) throws Exception{
		Integer id = RequestUtil.getIntParameter(request, "id", -1);
		//根据id进行查找
		Object object = service.getById(id);

		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String name = field.getName();
			Object param = RequestUtil.getParaTrim(request, name);
			if(param != null){
				field.setAccessible(true);
				String firstLetter = name.substring(0, 1).toUpperCase();
				String setMethodName = "set" + firstLetter
						+ name.substring(1);
				Method setMethod = clazz.getMethod(setMethodName,
						new Class[] { field.getType() });
				if(setMethod != null){
					BeanUtilsEx.setProperty(object, name, param);
				}
			}
		}
		//进行修改
		service.update(object);
	}

	public static ActionForward del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, String basePath, IBeanService service) throws Exception {
		Integer id = RequestUtil.getIntParameter(request, "id", -1);
		service.delete(id);
		return null;
	}

}
