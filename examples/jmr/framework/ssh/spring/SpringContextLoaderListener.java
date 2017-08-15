package ssh.spring;


import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;





/**
 * 类说明:spring监听子类
 */
public class SpringContextLoaderListener extends ContextLoaderListener{

	public void contextInitialized(ServletContextEvent event){
		super.contextInitialized(event);
		SpringContextUtil.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(event.getServletContext()));
	}
}
