package app.j2ee.file;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**  
 * 类说明 ：
 */
public class FileUtil {
	
//	分割符，在 UNIX 系统上，此栏位的值为 '/'；在 Microsoft Windows 系统上，它为 '\\'。
	public static String FILE_SEPARATOR = System.getProperty("file.separator");
	
	/**
	 * 换分割符
	 * @param filePath
	 * @param oldChar
	 * @return
	 */
	public static String replace(String filePath){
		return filePath.replace("/", FILE_SEPARATOR);
	}
	
	/**
	 * 根据需要创建档夹	 * 
	 * @param dirPath  档夹路径
	 * return true-创建成功或已存在 false-创建失败
	*/
	public static boolean mkDir(String dirPath) {
		File dir = new File(dirPath);
		if(!dir.exists()){//不存在
			return dir.mkdirs();
		}
		return true;
	}
	
	/**
	 * 文件写入硬盘
	 * 
	 * @param saveDir
	 *            地址
	 * @param fileName
	 *            文件名
	 * @param data
	 */
	public static void saveFile(String saveDir, String filename, byte[] data)
			throws Exception {
		BufferedOutputStream fout = null;
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		FileOutputStream out = null;
		File f = new File(saveDir);
		if (!f.exists()) {
			if (!f.mkdirs()) {
				throw new Exception("Create File Folder Error...");
			}
		}
		out = new FileOutputStream(new File(f, filename));
		byte[] b = new byte[1024 * 10];
		fout = new BufferedOutputStream(out);
		while (in.read(b) > 0) {
			out.write(b);
		}
		out.flush();
		out.close();
		in.close();
		fout.close();		
	}
	
	
	public static void saveFile(String saveDir, String filename, File file) throws Exception {
		FileOutputStream fos = null;
		FileInputStream fis = null; 
		File f = new File(saveDir);
		if (!f.exists()) {
			if (!f.mkdirs()) {
				throw new Exception("Creat Folder Error");
			}
		}
		// 输出流，从服务器保存到物理地址
		fos = new FileOutputStream(saveDir + filename);
		// 输入流，浏览器上传到服务器
		fis = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = fis.read(buffer)) > 0) {
			fos.write(buffer, 0, len);
		}	
		//关闭数据流
		fos.close();
		fis.close();		
	}
	
	/**
	 * 取得文件对象
	 * @param filePath :文件物理路径：如：d:java\\a.jpg
	 * @return
	 */
	public static File getFile(String filePath) {
		if(filePath != null && !filePath.trim().equals("")){	
			filePath = replace(filePath);
			File file = new File(filePath);
			if (file.isFile()) {
				return file;
			} else {
				return null;
			}
		}else{
			return null;
		}		
	}
	
	public static File getFile(String saveDir, String filename){
		String filePath = saveDir + filename;
		return getFile(filePath);
	}
	
	/**
	 * 取文件夹
	 * @param fileFolder
	 * @param linkId
	 * @return
	 */
	public static File getDir(String saveDir) {
		saveDir = replace(saveDir);
		File file = new File(saveDir);
		if (file.isDirectory()) {
			return file;
		} else {
			return null;
		}
	}
	
	/**
	 * 取得文件夹对象
	 * @param saveDir 目录
	 * @param folder 子文件夹
	 * @return
	 */
	public static File getDir(String saveDir, String folder){
		saveDir = saveDir + folder + FILE_SEPARATOR;
		return getDir(saveDir);
	}
	
	
	/**
	 * 删除文件
	 * 
	 * @param filePath
	 */
	public static void delFile(String filePath) {
		File file = getFile(filePath);
		if (file != null) {
			file.delete();
		}
	}
	
	/**
	 * 删除一个文件
	 * @param saveDir 目录 c:java\\
	 * @param filename  文件名 user
	 */
	public static void delFile(String saveDir, String filename){
		if(filename != null && !filename.trim().equals("")){
			String filePath = saveDir + filename;			
			delFile(filePath);
		}
	}
	
	
	 /**
		 * 删除档夹下所有内容，包括此档夹
		 * 
		 * @param f
		 * @throws IOException
		 */
	public static void delAll(File f) throws IOException {
		if (f == null) {
			// throw new IOException("档不能为空");
			return;
		}
		if (!f.exists()) {// 文件夹不存在不存在
			// throw new IOException("指定目录不存在:"+f.getName());
			return;
		}
		boolean result = true; // 保存中间结果
		if (!(result = f.delete())) {// 先尝试直接删除
		//若文件夹非空。枚举、巡回删除里面內容
			File subs[] = f.listFiles();
			for (int i = 0; i <= subs.length - 1; i++) {
				if (subs[i].isDirectory())
					delAll(subs[i]);// 巡回删除子档夹内容
				result = subs[i].delete();// 删除子档夹本身
			}
			result = f.delete();// 删除此档夹本身
		}
		if (!result)
			throw new IOException("無法刪除:" + f.getName());
	}
	
	/**
	 * 遍历文件，取得文件名
	 * @param filePath
	 * @return
	 */
	public static List<String> ergodicFile(String filePath){
		ArrayList pathList = new ArrayList();
		LinkedList list = new LinkedList(); 
		File dir = new File(filePath);   
		File file[] = dir.listFiles();   
		for (int i = 0; i < file.length; i++) {  
		     if (file[i].isDirectory()){  
		         list.add(file[i]);    
		     }else{  
		    	 pathList.add(file[i].getAbsolutePath());
//		         System.out.println(file[i].getAbsolutePath());
		     }
		} 
		File tmp;     
		while (!list.isEmpty()) {    
		    tmp = (File)list.removeFirst();   
		    if (tmp.isDirectory()) {   
		       file = tmp.listFiles(); 
		       if (file == null) continue;  
		       for (int i = 0; i < file.length; i++) { 
		            if (file[i].isDirectory()) {
		            	list.add(file[i]);  
		            }else{
		            	pathList.add(file[i].getAbsolutePath());
//		            	System.out.println(file[i].getAbsolutePath());
		            } 
		       }
		    }else {   
		         pathList.add(tmp.getAbsolutePath());
//		         System.out.println(tmp.getAbsolutePath());            
		    }        
		} 
		return pathList;
 
	}
	
	
	/**
     * 将文件重命名
     * 
     * @param oldName
     *            源文件名
     * @param newName
     *            新文件名
     */
    public static void reName(String oldName, String newName) {
        File oldF = new File(oldName);
        File newF = new File(newName);
        oldF.renameTo(newF);
    }
    
    /**
     * 拷贝文件
     * 
     * @param oldPath
     *            源文件
     * @param newPath
     *            目标文件
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            File temp = new File(oldPath);
            FileInputStream input = new FileInputStream(temp);
            FileOutputStream output = new FileOutputStream(newPath + "/"
                    + (temp.getName()).toString());
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = input.read(b)) != -1) {
                output.write(b, 0, len);
            }
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
	 * 将数据写入文件
	 * @param str json数据
	 * @param fileName 文件名
	 */
	public static void outputFile(String str,String fileName, String encode){
		try {
			FileOutputStream out=new FileOutputStream(fileName);   
			out.write(str.getBytes(encode));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	



    
	public static void main(String[] args) {
		outputFile("haha中文", "f:\\write.txt", "utf-8");
	}
	
	


}
