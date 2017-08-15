package util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ssh.spring.DeployInfoUtil;
import app.j2ee.data.RandKeyCreator;
import app.j2ee.file.FileUtil;
import app.other.commons.file.MutiFileUpload;

public class FileTempUtil {
	
	private static final Log log = LogFactory.getLog(FileTempUtil.class);
	
	public static String getParam(String param, Map map){
		String temp = "";
		if(map != null){
			Object o =  map.get(param);
			if (o != null) {
				temp = (String)o;
				try {
					temp = new String(temp.getBytes("iso-8859-1"), "utf8");
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		return temp.trim();
	}
	//接收Integer
	public static Integer getIntParam(String param, Map map,Integer defaultValue){
		int num = defaultValue;
		if(map != null){
			Object o =  map.get(param);
			if (o != null) {
				try {
					o = new String(((String)o).getBytes("iso-8859-1"), "utf8");
					num = Integer.parseInt((String)o);
				} catch (Exception e) {
				}
			}
		}
		return num;
		
	}
	
	/**
	 * 取得普通表单数据
	 * @param request
	 * @return
	 */
	public static Map getPreParameters(MutiFileUpload upload){
		return upload.parameters;
	}
	
	/**
	 * 多文件上传 @author huangyonghua
	 * @param request
	 * @param folder 资源文件 如，game
	 * @param id 表中id
	 * @param files	game下面二級目录如game、pic 若沒有则null
	 * @return
	 * @throws Exception
	 */
	
	public static Map<String,String> upload(MutiFileUpload upload, String folder, String id, String [] files) throws Exception {
		Map<String,String> temp = new HashMap<String,String>();
		
		String rootPath = DeployInfoUtil.getUploadFilePath() + ConstantsUtil.UPLOADFILE_PATH + File.separator +
		folder + File.separator;
		
		String virtualPath = ConstantsUtil.UPLOADFILE_PATH + File.separator +folder + File.separator;
		
		Iterator iterator = upload.files.values().iterator();
		while (iterator.hasNext()) {
			FileItem item = (FileItem) iterator.next();
			if (upload.getFileName(item) == null
					|| upload.getFileName(item).equals(""))
				continue;
			String rankKey = RandKeyCreator.getRandStr(12);
			String fileName = upload.getFileName(item); //原来的档案名			
			String fieldName = item.getFieldName();
			if(files == null){
				FileUtil.saveFile(rootPath + id + File.separator + rankKey,fileName, item.get());
				fileName = virtualPath + id + File.separator + rankKey + File.separator + fileName;
				temp.put(fieldName, fileName.replace("\\", "/"));
			}else{
				for(String file:files){
					if(file.equals(fieldName)){
						FileUtil.saveFile(rootPath + file + File.separator + id + File.separator + rankKey,
								fileName, item.get());
						fileName = virtualPath + file + File.separator + id + File.separator + rankKey + File.separator + fileName;
						temp.put(fieldName, fileName.replace("\\", "/"));
						break;
					}
				}
			}
		}
		
		return temp;
	}
	
	public static void delFile(String fileName) throws Exception{
		String filePath = fileName.substring(0,fileName.lastIndexOf("/"));
		filePath = fileName.substring(0,filePath.lastIndexOf("/"));
		filePath = DeployInfoUtil.getUploadFilePath() + filePath.replace("/", "\\");
		FileUtil.delAll(new File(filePath));
	}
	
	public static void delUpdateFile(String fileName) throws Exception{
		String filePath = fileName.substring(0,fileName.lastIndexOf("/"));
		filePath = DeployInfoUtil.getUploadFilePath() + filePath.replace("/", "\\");
		FileUtil.delAll(new File(filePath));
	}
	

	/**
	 * 文件长度单位处理
	 * @param orilength
	 * @return
	 */
	public static  String getFileSizeHandle(long orilength){
		DecimalFormat df = new DecimalFormat( "0.## "); 
		if(orilength < 1024){
			return orilength+"B";
		}else if(orilength < 1024*1024){
			Float si=orilength/1024f;
			return df.format(si)+"KB";
		}else{
			Float si=orilength/1024f/1024f;
			return df.format(si)+"M";
		}
		
		
	}
}
