package app.other.poi.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;




/**
 * 类说明:Excel工具类
 */
public class ExcelUtil{
	
	private static final Log log = LogFactory.getLog(ExcelUtil.class);//日志

	/**
	 * 创建Excel对象
	 * @param excelBean
	 * @return
	 */
	public static HSSFWorkbook creatExcel(ExcelBean excelBean){
		HSSFWorkbook wb = new HSSFWorkbook(); //创建Excel工作薄
		//在Excel工作薄中建一工作表，默认为缺省值
//		HSSFSheet sheet = wb.createSheet();
        HSSFSheet sheet = wb.createSheet(excelBean.getSheetName());
        //设置工作表各列宽度
       // setColumnWidth(sheet, excelBean.getWidth());
        //设置字体，样式
        //HSSFFont font = setFont(wb, "宋体");
        //HSSFCellStyle style = setCellStyle(wb, font);
        //设置第一行
        List<String> titleList = excelBean.getTitleList();
        setRowValue(sheet, titleList, 0); //在索引0的位置创建行（最頂端的行）
        //设置数据行
        List dataList = excelBean.getDataList();
        if(dataList != null){
        	for(int i=0; i<dataList.size(); i++){        		
        		List rowList = (List) dataList.get(i);
        		setRowValue(sheet, rowList, i+1); //在索引1的位置创建行（第2行）        		
        	}
        }
        return wb;
	}
	
	/**
	 * 写入到物理地址中
	 * @param wb
	 * @param path
	 * @return
	 */
	public static boolean writeExcel(HSSFWorkbook wb, String path){
		boolean flag = true;
		try{
            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            fileOut.close();
            log.info("Excel写入到" + path + ", 成功！");
        }catch(Exception e){
        	log.error("Excel写入到" + path + ", 失敗！");
        	flag = false;
            e.printStackTrace();
        }
        return flag;
	}
	
	/**
	 * 下载Excel
	 * @param wb
	 * @param filename MemberList.xls
	 * @param response
	 * @return
	 */
	public static boolean downloadExcel(HSSFWorkbook wb, String filename, HttpServletResponse response){
		boolean flag = true;		
		//设置输入流
		OutputStream output = null;
		try {
			//设置响应类型
			response.setHeader("Content-disposition", "attachment;filename="+new String(filename.getBytes("GBK"),"ISO-8859-1"));
			response.setContentType("application/x-msdownload");
			
			output = response.getOutputStream();			
			wb.write(output);//输出到网页中
			output.flush();
			log.info("下载Excel：" + filename + ", 成功！");
		} catch (Exception e) {
			flag = false;
			log.info("下载Excel：" + filename + ", 失敗！");
			e.printStackTrace();
		} finally{
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;		
	}
	
	/**
	 * 设置工作表各列宽度
	 * @param sheet
	 * @param width
	 */
	public static void setColumnWidth(HSSFSheet sheet, int[] width){
        for(int i=0;i<width.length;i++){
            sheet.setColumnWidth((short)i, (short)(width[i]*256));
        }
    }
	
	/**
	 * 设置字体
	 * @param wb
	 * @param fontName 
	 */
	public static HSSFFont setFont(HSSFWorkbook wb, String fontName){
		HSSFFont font = wb.createFont();
		font.setFontName(fontName); //宋体 
		return font;		
	}
	
	/**
	 * 将字体对象复制给单格样式对象
	 * @param wb
	 * @param font
	 * @return
	 */
	public static HSSFCellStyle setCellStyle(HSSFWorkbook wb, HSSFFont font){
		HSSFCellStyle style = wb.createCellStyle();
        style.setFont(font);
        return style;
	}
	
	/**
	 * 设置行值
	 * @param sheet
	 * @param List
	 * @return
	 */
	public static HSSFSheet setRowValue(HSSFSheet sheet, List<String> list,int rowNo){
		if(list != null){
			//在索引rowNo的位置创建行
			HSSFRow rowTitle = sheet.createRow(rowNo);
			for(int i= 0 ; i<list.size();i++){
				String title = list.get(i);
				//从索引0的位置开始创建单元格（左上端）
				HSSFCell cell = rowTitle.createCell((short)i);
				cell.setCellValue(title); //设置單元格內容
			}
		}
		return sheet;
	}
	
	/**
	 * 设置行值
	 * @param sheet
	 * @param List
	 * @return
	 */
	public static HSSFSheet setRowValue(HSSFSheet sheet, List<String> list,
			int rowNo,  HSSFCellStyle style){
		if(list != null){
			//在索引0的位置创建行（最顶端的行）
			HSSFRow rowTitle = sheet.createRow(rowNo);
			for(int i= 0 ; i<list.size();i++){
				String title = list.get(i);
				//从索引0的位置开始创建单元格（左上端）
				HSSFCell cell = rowTitle.createCell((short)i);
				cell.setCellValue(title); //设置单元格內容
				cell.setCellStyle(style);//单元格样式  
			}
		}
		return sheet;
	}
	
	/**
	 * 下载（从服务器上下载现程的Excel文件）
	 * @param request
	 * @param response
	 * @param path 路径Z:\PowerTest\
	 * @param fileName 文件名 ExampleFile/UserProfileExample.xls
	 * @throws Exception
	 * @return loadFlag ---true 下载成功， false  下载失败
	 */
	public static boolean download(HttpServletRequest request, HttpServletResponse response, String path, String fileName) throws Exception{  
		boolean loadFlag = true; //下载成功标志位
		File file = null;
		InputStream in = null; // 输入流
		OutputStream out = null; // 输出流
		//在下载附件之前设置响应类型和头部文件
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(ExcelUtil.subLastStr(fileName, "/"),"UTF-8"));		
		try{
			file = new File(path + fileName); //取得文件对象
			//从下载附件创建输入流，并向请求获取并写入（客户端或网络）输出流
			in = new BufferedInputStream(new FileInputStream(file));
			out = response.getOutputStream();
			int readSize = 1024 * 10; //读取大小
			int length = 0; //读取剩余大小
			byte[] readByte = new byte[readSize];
			while ((length = in.read(readByte, 0, readSize)) != -1) {
				out.write(readByte, 0, length);
			}
			out.flush();
		}catch(Exception e){
			loadFlag = false;
			e.printStackTrace();
		}finally{
			if(in!=null || out!=null ){
				try {
					in.close();
					out.close();
				} catch (IOException e) {
					loadFlag = false;
					e.printStackTrace();
				}	
			}
		}
		return loadFlag;
	}
	
	/**
	 * 取得分割符最后的字符串
	 * @param path 路径
	 * @param regex '/'
	 * @return
	 */
	public static String subLastStr(String path, String regex){
		String temp = "";
		if(path == null){
			temp = "";
		}else{
			String[] arr = path.split(regex);
			temp = arr[arr.length-1];
		}
		return temp;		
	}


}

