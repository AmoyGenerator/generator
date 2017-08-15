package app.j2ee.util;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import app.other.query.RangeQuery;

/**  
 * 类说明 ：
 */
public class RequestUtil {

	/**
	 * 对前台资料处理，去除前后空格，若为空，回传空字串
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getPara(HttpServletRequest request,
			String name){
		String temp = request.getParameter(name);
		if(temp == null){
			return "";
		}else{
			return temp;
		}
	}

	/**
	 * 对前台资料处理，去除前后空格，若为空，回传null
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getParaTrim(HttpServletRequest request,
			String name){
		String temp = request.getParameter(name);
		if(temp == null){
			return null;
		}else{
			return temp.trim();
		}
	}

	/**
	 * 对前台资料处理，去除前后空格，若为空，回传null
	 * @param request
	 * @param name
	 * @return
	 */
	public static RangeQuery isRangeQuery(HttpServletRequest request,
			String name){
		String temp = request.getParameter(name);
		if(temp == null){
			return null;
		}else{
			try {
				JSONObject jsonobject = JSONObject.fromObject(temp);
				RangeQuery rangeQuery = (RangeQuery) JSONObject.toBean(jsonobject, RangeQuery.class);
				return rangeQuery;
			} catch (Exception e) {

			}
			return null;
		}
	}

	/**
	 * 对前台资料处理，去除前后空格，若为空，回传NULL
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getParaTrimReturnNull(HttpServletRequest request,
			String name){
		String temp = request.getParameter(name);
		if(temp != null && !temp.equals("")){
			return temp.trim();
		}else{
			return null;
		}
	}

	/**
	 * 编码转换
	 * 
	 * @param request
	 * @param name
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getEncodedParameter(HttpServletRequest request,
			String name, String encoding) throws UnsupportedEncodingException {
		// request.setCharacterEncoding(encoding);
		String temp = request.getParameter(name);
		if (temp == null) {
			temp = "";
		} else {
			temp = new String(temp.getBytes("iso-8859-1"), encoding);
		}
		return temp;
	}

	public static String[] getEncodedParameters(HttpServletRequest request,
			String name, String encoding) throws UnsupportedEncodingException {
		request.setCharacterEncoding(encoding);
		String[] temp = request.getParameterValues(name);
		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				temp[i] = new String(temp[i].getBytes("iso-8859-1"), encoding);
			}
		}
		return temp;
	}

	/**
	 * 输入数字
	 * 
	 * @param request
	 * @param name
	 * @param defaultNum
	 * @return
	 */
	public static int getIntParameter(HttpServletRequest request, String name,
			int defaultNum) {
		String temp = request.getParameter(name);
		if (temp != null && !temp.equals("")) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(temp);
			} catch (Exception ex) {
				System.err.println(ex.toString());
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	public static int[] getIntParameters(HttpServletRequest request,
			String name, int defaultNum) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new int[0];
		}
		int[] values = new int[paramValues.length];
		for (int i = 0; i < paramValues.length; i++) {
			try {
				values[i] = Integer.parseInt(paramValues[i]);
			} catch (Exception ex) {
				System.err.println(ex.toString());
				values[i] = defaultNum;
			}
		}
		return values;
	}

	public static Integer[] getIntegerParameterValues(HttpServletRequest request,
			String name, int defaultNum) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new Integer[0];
		}
		Integer[] values = new Integer[paramValues.length];
		for (int i = 0; i < paramValues.length; i++) {
			try {
				values[i] = Integer.parseInt(paramValues[i]);
			} catch (Exception ex) {
				System.err.println(ex.toString());
				values[i] = defaultNum;
			}
		}
		return values;
	}

	public static long getLongParameter(HttpServletRequest request,
			String name, long defaultNum) {
		String temp = request.getParameter(name);
		if (temp != null && !temp.equals("")) {
			long num = defaultNum;
			try {
				num = Long.parseLong(temp);
			} catch (Exception ex) {
				System.err.println(ex.toString());
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	public static long[] getLongParameters(HttpServletRequest request,
			String name, long defaultNum) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new long[0];
		}
		long[] values = new long[paramValues.length];
		for (int i = 0; i < paramValues.length; i++) {
			try {
				values[i] = Long.parseLong(paramValues[i]);
			} catch (Exception ex) {
				System.err.println(ex.toString());
				values[i] = defaultNum;
			}
		}
		return values;
	}

	public static Long[] getLongParameterValues(HttpServletRequest request,
			String name, long defaultNum) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new Long[0];
		}
		Long[] values = new Long[paramValues.length];
		for (int i = 0; i < paramValues.length; i++) {
			try {
				values[i] = Long.valueOf(paramValues[i]);
			} catch (Exception ex) {
				System.err.println(ex.toString());
				values[i] = defaultNum;
			}
		}
		return values;
	}

	public static float getFloatParameter(HttpServletRequest request,
			String name, float defaultNum) {
		String temp = request.getParameter(name);
		if (temp != null && !temp.equals("")) {
			float num = defaultNum;
			try {
				num = Float.parseFloat(temp);
			} catch (Exception ex) {
				System.err.println(ex.toString());
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	public static float[] getFloatParameters(HttpServletRequest request,
			String name, float defaultNum) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new float[0];
		}
		float[] values = new float[paramValues.length];
		for (int i = 0; i < paramValues.length; i++) {
			try {
				values[i] = Float.parseFloat(paramValues[i]);
			} catch (Exception ex) {
				System.err.println(ex.toString());
				values[i] = defaultNum;
			}
		}
		return values;
	}

	public static Float[] getFloatParameterValues(HttpServletRequest request,
			String name, float defaultNum) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new Float[0];
		}
		Float[] values = new Float[paramValues.length];
		for (int i = 0; i < paramValues.length; i++) {
			try {
				values[i] = Float.valueOf(paramValues[i]);
			} catch (Exception ex) {
				System.err.println(ex.toString());
				values[i] = defaultNum;
			}
		}
		return values;
	}

	public static double getDoubleParameter(HttpServletRequest request,
			String name, double defaultNum) {
		String temp = request.getParameter(name);
		if (temp != null && !temp.equals("")) {
			double num = defaultNum;
			try {
				num = Double.parseDouble(temp);
			} catch (Exception ex) {
				System.err.println(ex.toString());
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	public static double[] getDoubleParameters(HttpServletRequest request,
			String name, double defaultNum) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new double[0];
		}
		double[] values = new double[paramValues.length];
		for (int i = 0; i < paramValues.length; i++) {
			try {
				values[i] = Double.parseDouble(paramValues[i]);
			} catch (Exception ex) {
				System.err.println(ex.toString());
				values[i] = defaultNum;
			}
		}
		return values;
	}

	public static Double[] getDoubleParameterValues(HttpServletRequest request,
			String name, double defaultNum) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new Double[0];
		}
		Double[] values = new Double[paramValues.length];
		for (int i = 0; i < paramValues.length; i++) {
			try {
				values[i] = Double.valueOf(paramValues[i]);
			} catch (Exception ex) {
				System.err.println(ex.toString());
				values[i] = defaultNum;
			}
		}
		return values;
	}

	public static boolean getBooleanParameter(HttpServletRequest request,
			String name, boolean defaultFlag) {
		String temp = request.getParameter(name);
		if (temp != null && !temp.equals("")) {
			boolean flag = defaultFlag;
			try {
				flag = Boolean.parseBoolean(temp);
			} catch (Exception ex) {
				System.err.println(ex.toString());
			}
			return flag;
		} else {
			return defaultFlag;
		}
	}

	public static boolean[] getBooleanParameters(HttpServletRequest request,
			String name, boolean defaultFlag) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new boolean[0];
		}
		boolean[] values = new boolean[paramValues.length];
		for (int i = 0; i < paramValues.length; i++) {
			try {
				values[i] = Boolean.parseBoolean(paramValues[i]);
			} catch (Exception ex) {
				System.err.println(ex.toString());
				values[i] = defaultFlag;
			}
		}
		return values;
	}

	public static Boolean[] getBooleanParameterValues(HttpServletRequest request,
			String name, boolean defaultFlag) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new Boolean[0];
		}
		Boolean[] values = new Boolean[paramValues.length];
		for (int i = 0; i < paramValues.length; i++) {
			try {
				values[i] = Boolean.valueOf(paramValues[i]);
			} catch (Exception ex) {
				System.err.println(ex.toString());
				values[i] = defaultFlag;
			}
		}
		return values;
	}

	/**
	 * 格式化日期，返回lang.util.Date 或null
	 * @param request
	 * @param dateTimeStr
	 * @param formatStr yyyy/MM/dd 或yyyy-MM-dd
	 * @return
	 */
	public static Date getDateParameter(HttpServletRequest request,
			String name, String formatStr){
		String dateTimeStr = getParaTrim(request, name);
		try {
			if (dateTimeStr.equals("")) {
				return null;
			}
			DateFormat sdf = new SimpleDateFormat(formatStr);
			Date d = sdf.parse(dateTimeStr);
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 返回根目录
	 * @param request
	 * @return
	 */
	public static String getBasePath(HttpServletRequest request){
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		return basePath;
	}

	/**
	 * 取得项目物理地址
	 * @param request
	 * @return
	 */
	public static String getProRealPath(HttpServletRequest request){
		return request.getRealPath("/");
	}

	/**
	 * 取得ip，有些是代理
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}   

	/**
	 * IP接口
	 * @param request HttpServletRequest
	 * @throws Exception
	 * @return String
	 */
	public static String catchRemoteIP(HttpServletRequest request)
			throws Exception {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null) {
			ip = request.getRemoteAddr();
		} else {
			try {
				ip = ip.split(",")[0];  //取出x-forwarded-for中第一组原始來源IP
				//ip = ip.substring(0, ip.indexOf(",")); //取出x-forwarded-for中第一组原始來源IP
			} catch (Exception e) {
				//             throw new SeatException(SeatException.IP_NULL, e.getMessage());
				e.printStackTrace();
			}
		}
		return ip;
	}

	/**
	 * 判断网段branchNet与使用者来源IP使否相符
	 * @param branchNet String 网段 
	 * @param ip String 要带入判断的IP  
	 * @return boolean
	 * @throws Exception
	 */
	private boolean isBranchIP(String branchNet, String ip)
			throws Exception {
		boolean isMatch = false;
		int[] ipAddr = new int[4];
		StringTokenizer st = new StringTokenizer(ip, ".");
		for (int i = 0; i < 4; i++) {
			ipAddr[i] = (Integer.parseInt(st.nextToken()));
		}

		String netId = branchNet.split("/")[0];
		int maskBits = Integer.parseInt(branchNet.split("/")[1]);

		int[] netMask = new int[4];
		int count = 0;

		StringBuffer temp = new StringBuffer();
		for (int j = 1; j <= 32; j++) {
			if (maskBits >= j) {
				temp.append("1");
			} else {
				temp.append("0");
			}
			if (temp.length() == 8) {
				//System.out.println("netMask[" + count +"] = " + Integer.parseInt(temp.toString(), 2));
				netMask[count++] = Integer.parseInt(temp.toString(), 2);
				temp.setLength(0);
			}
		}
		byte[] net = new byte[4];
		for (int j = 0; j < ipAddr.length; j++) {
			net[j] = (byte) (ipAddr[j] & netMask[j]);
		}
		//System.out.println("InetAddress.getByAddress(net).getHostAddress() = " + InetAddress.getByAddress(net).getHostAddress());
		if (netId.equals(InetAddress.getByAddress(net).getHostAddress())) {
			isMatch = true;
		}
		return isMatch;
	}
	//isBranchIP("192.168.1.0/24","192.168.1.1"); 回传true 表示网段正确
	//isBranchIP("192.168.1.0/24","192.168.2.1"); 回传flase 表示网段不正确


}
