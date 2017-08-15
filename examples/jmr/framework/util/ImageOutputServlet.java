package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.j2ee.util.RequestUtil;

import ssh.spring.DeployInfoUtil;

/**
 * Servlet implementation class ImageOutputServlet
 * 接口：/imageOutput
 */
public class ImageOutputServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    //http://localhost:8060/huayuschool/imageOutput?fileName=uploadFile/game/pic/19/994063607806/3.jpg
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String fileName = RequestUtil.getParaTrim(request, "fileName");
		fileName = new String(fileName.getBytes("ISO-8859-1"),"gb2312");
		fileName = DeployInfoUtil.getUploadFilePath() + fileName.replace("/", "\\");
		OutputStream output = response.getOutputStream();
		InputStream is = new FileInputStream(new File(fileName));
		BufferedInputStream bis=new BufferedInputStream(is);//输入缓冲流
	    BufferedOutputStream bos=new BufferedOutputStream(output);//输出缓冲流
	    byte data[]=new byte[4096];//缓冲字节数
	    int size=0; 
	    size=bis.read(data);
	    while (size!=-1){
	       bos.write(data,0,size);
	       size=bis.read(data);
	    }
	    bis.close();
	    bos.flush();//清空输出缓冲流
	    bos.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
