package app.j2ee.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 类说明 字符过滤器
 * 
 * web.xml:
 * <!-- 过滤器-->
	<filter>
		<filter-name>encodingFilter</filter-name>
		<filter-class>app.j2ee.filter.EncoderFilter</filter-class>
		<init-param>
			<param-name>encode</param-name>
			<param-value>UTF-8</param-value>
		</init-param>
	</filter>



	<filter-mapping>
		<filter-name>encodingFilter</filter-name>
		<servlet-name>action</servlet-name>
	</filter-mapping>
	<filter-mapping>
		<filter-name>encodingFilter</filter-name>
		<url-pattern>*.jsp</url-pattern>
	</filter-mapping>
 */
public class EncoderFilter implements Filter {

	protected String encode;
	
	public static final String ENCODE_UTF8 = "utf-8";

	public void init(FilterConfig filterConfig) throws ServletException {
		encode = filterConfig.getInitParameter("encode");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (encode != null) {
			try {
				request.setCharacterEncoding(encode);
			} catch (java.io.UnsupportedEncodingException e) {
				request.setCharacterEncoding(ENCODE_UTF8);
			}
		} else {
			request.setCharacterEncoding(ENCODE_UTF8);
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}

}
