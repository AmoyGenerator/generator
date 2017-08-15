package ssh.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 类说明:spring 应用 单例
 */
public class SpringContextUtil {
	private static ApplicationContext context;
	public static ApplicationContext getApplicationContext() {
		if (context == null) {
			//数组
			/*context = new ClassPathXmlApplicationContext(
					new String[] { "classpath:/config/spring/applicationContext.xml",
							"classpath:/config/spring/serviceContext.xml"							

					});*/
			context = new ClassPathXmlApplicationContext(
			new String[] { "classpath:/config/spring/applicationContext.xml",
					"classpath:/config/spring/mailContext.xml",
					"classpath:/spring/beanContext.xml"							

			});
			/*//通配符
			context = new ClassPathXmlApplicationContext("classpath:/config/spring/*Context.xml");
			*/
		}
		return context;
	}

	public static void setApplicationContext(ApplicationContext outcontext) {
		context = outcontext;
	}

}
