package app.j2ee.servlet.checkcode;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类说明:效验码
 * 
 * web.xml:
 *   <servlet>
		<servlet-name>CodeImgControl</servlet-name>
		<servlet-class>
			app.j2ee.servlet.checkcode.CodeImgControl
		</servlet-class>
	</servlet>
	
	<servlet-mapping>
		<servlet-name>CodeImgControl</servlet-name>
		<url-pattern>/code</url-pattern>
	</servlet-mapping>
 */
public class CodeImgControl extends HttpServlet {

	private static final long serialVersionUID = 5557074673200512830L;
	
//	生成的随机码，绑定在Session的变量名
	public static final String SESSION_VERIFY_CODE = "checkCode";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CodeImage.CONTENT_TYPE);

		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		OutputStream out = response.getOutputStream();

		// cImg.setDisturb(false);
		BufferedImage bufImg = CodeImage.getInstance().getImage();
		request.getSession().setAttribute(SESSION_VERIFY_CODE,
				CodeImage.getInstance().getRandValue());
		CodeImage.outPut(out, bufImg);
	}

}
