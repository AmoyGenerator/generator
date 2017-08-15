package app.other.commons.file;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 类说明：多文件上传
 * 
 */

public class MutiFileUpload extends HttpServlet {

	private static final long serialVersionUID = 670829239023754119L;

	private long sizeMax = -1; // 上传文件大小（单位：字节byte），为-1时，无限制

	private String encoding = "utf-8";// 字元编码，当读取上传表单的各部分时会用到该encoding
    //缓冲值, 默认是1024*10,即10K
	private int sizeThreshold = DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD; 

	public Map parameters;// 保存表单参数

	public Map files;// 保存上传的档

	public boolean uploadError = false; // 上传是否有错，默认是沒错false

	/**
	 * 解析request
	 * 
	 * @param request
	 */
	public void parse(HttpServletRequest request) {
		parameters = new HashMap();
		files = new HashMap();

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// Set factory constraints
		factory.setSizeThreshold(sizeThreshold);
		// factory.setRepository(repository);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Set overall request size constraint
		upload.setSizeMax(sizeMax);
		upload.setHeaderEncoding(encoding);

		try {
			List items = upload.parseRequest(request);
			Iterator iterator = items.iterator();
			while (iterator.hasNext()) {
				FileItem item = (FileItem) iterator.next();
				if (item.isFormField()) {
					String fieldName = item.getFieldName();
					String value = item.getString();
					parameters.put(fieldName, value);
				} else {
					String fieldName = item.getFieldName();
					files.put(fieldName, item);
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			this.setUploadError(true);// 设为出错
		}

	}
	
	/**
	 * 取得非文件表单的值
	 * @param key
	 * @return
	 */
	public String getParameter(String key) {
		return (parameters == null) ? null : (String) parameters.get(key);
	}

	public String getParameter(String key, String encoding) {
		if (parameters == null)
			return null;
		String value = (String) parameters.get(key);
		try {
			if (value != null) {
				value = new String(value.getBytes("ISO-8859-1"), encoding);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	
	/**
	 * 得到上传档的档案名
	 * 
	 * @param item
	 * 
	 * @return
	 * 
	 */
	public String getFileName(FileItem item) {
		String fileName = item.getName();
		fileName = this.replace(fileName, "\\", "/");
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		return fileName;
	}

	/**
	 * 获得档扩展
	 * 
	 * @param item
	 * @return
	 */

	public String getFileExtension(FileItem item) {
		String fileName = getFileName(item);
		fileName = fileName.substring(fileName.lastIndexOf("."));
		return fileName;
	}
	
	/**
	 * 验证单个的文件是否超过最大值
	 * @param item
	 * @param defalutMaxSize
	 * @return true-正常， false-超过最大值
	 */
	public boolean checkMaxSize(FileItem item, long defalutMaxSize){
		if(defalutMaxSize == -1){
			return true;
		}
		return (item.getSize() <= defalutMaxSize);
	}
	
	/**
	 * 字串替
	 * 
	 * @param source
	 * 
	 * @param oldString
	 * 
	 * @param newString
	 * 
	 * @return
	 * 
	 */
	public static String replace(String source, String oldString, String newString) {
		StringBuffer output = new StringBuffer();
		int lengthOfSource = source.length();
		int lengthOfOld = oldString.length();
		int posStart = 0;
		int pos;
		while ((pos = source.indexOf(oldString, posStart)) >= 0) {
			output.append(source.substring(posStart, pos));
			output.append(newString);
			posStart = pos + lengthOfOld;
		}
		if (posStart < lengthOfSource) {
			output.append(source.substring(posStart));
		}
		return output.toString();
	}

	

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public long getSizeMax() {
		return sizeMax;
	}

	public void setSizeMax(long sizeMax) {
		this.sizeMax = sizeMax;
	}

	public int getSizeThreshold() {
		return sizeThreshold;
	}

	public void setSizeThreshold(int sizeThreshold) {
		this.sizeThreshold = sizeThreshold;
	}

	

	
	

	public boolean getUploadError() {
		return uploadError;
	}

	public void setUploadError(boolean uploadError) {
		this.uploadError = uploadError;
	}

}
