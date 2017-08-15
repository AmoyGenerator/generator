package app.j2ee.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;



/**
 * 请求参数值若有角括号< >，替换为&lt; &gt;
 */
public class ParameterFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		ServletRequest req = new ParameterHttpServletWrapper((HttpServletRequest) request);
		chain.doFilter(req, response);
	}

	public void destroy() {

	}

	public void init(FilterConfig arg0) throws ServletException {

	}
}

/**
 * 包装http请求，过滤角括号< >
 */
class ParameterHttpServletWrapper extends HttpServletRequestWrapper {
	
	
	public ParameterHttpServletWrapper(HttpServletRequest request) {
		super(request);
	}

	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (value == null) {
			return null;
		}
		return repalceGreaterLowerSymbol(value);
	}

	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values == null) {
			return null;
		}
		for (int i = 0; i < values.length; i++) {
			values[i] = repalceGreaterLowerSymbol(values[i]);
		}
		return values;
	}

	private String repalceGreaterLowerSymbol(String value) {
		if (value == null) {
			return null;
		}
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
/*
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");

		value = value.replaceAll("'", "&#39;");
		
		value = value.replaceAll("\"", "&quot;");

		value = value.replaceAll("eval\\((.*)\\)", "");

		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");

		value = value.replaceAll("script", "");
*/
		return value;
	}
}
