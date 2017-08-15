package app.other.poi.word;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.model.SectionTable;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakClear;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 * @author ShenJunJie
 * @version CreateTime：Oct 8, 2010
 * ClassExplain: word2007
 */
public class WordUtil {

	/**
	 * 将内容写入word文档(限文本内容)
	 * @param str
	 * @param filename
	 * @return
	 */
	public static boolean writeWord(String str,String filename){
	   boolean flag = false;
	   POIFSFileSystem fs = new POIFSFileSystem();
	   try {
		   byte[] b = str.getBytes("utf-8");
		   ByteArrayInputStream in = new ByteArrayInputStream(b);
		    //解决中文乱码问题
			DirectoryEntry directory = fs.getRoot();
			directory.createDocument("WordDocument", in);

			//输出
			mkDir(filename);//先建好上级文件夹
			FileOutputStream out = new FileOutputStream(filename);
		    fs.writeFilesystem(out);
		    in.close();
		    out.flush();
		    out.close();
		    flag = true;
		} catch (IOException e1) {
			e1.printStackTrace();
			flag = false;
		}
	  return flag;
	}
	
	/**
	 *创建word文档
	 */
	public static void createWord(String filename,String content,String[] title,List<Object[]> list){
		XWPFDocument doc = new XWPFDocument();
		
		createPparagraph(doc,content); //段落文本
		if(title!=null){
			createTable(doc,title,list); //表格
		}
		FileOutputStream out;
		try {
			mkDir(filename); //建好文件夹
			out = new FileOutputStream(filename);
			doc.write(out);
			out.flush();
	        out.close();
	        System.out.println("创建word 成功");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("文件不存在");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("创建word 失败");
		}
		
	}


	/**
	 * 创建段落内容
	 * @param doc
	 * @param content
	 */
	public static void createPparagraph(XWPFDocument doc,String content){
		//段落设置
		XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.CENTER); //水平对齐
        p.setVerticalAlignment(TextAlignment.TOP); //垂直对齐
        p.setStyle("");
        //边框设置
        p.setBorderBottom(Borders.DOUBLE);
        p.setBorderTop(Borders.DOUBLE);
        p.setBorderRight(Borders.DOUBLE);
        p.setBorderLeft(Borders.DOUBLE);
        p.setBorderBetween(Borders.SINGLE);
        
        //字体设置
        XWPFRun r = p.createRun();
        r.setBold(true); //加粗
        r.setFontFamily("Courier"); //字体样式
        r.setUnderline(UnderlinePatterns.DOT_DOT_DASH); //下划线
        r.setTextPosition(100); //位置
        r.setText(content); //设置文本
	}
	
	
	/**
	 * 创建word表格(表格样式有限)
	 * @param doc
	 * @param title
	 * @param list
	 */
	public static void createTable(XWPFDocument doc,String[] title,List<Object[]> list){ 
		int col = title.length; //表格列
		//段落样式
		XWPFParagraph p = doc.createParagraph();
		p.setAlignment(ParagraphAlignment.CENTER);
		p.setVerticalAlignment(TextAlignment.BASELINE);
		
		/*******************创建表格 start********************/
        XWPFTable table = doc.createTable(1,col);//创建表格
        //添加表格标题
        XWPFTableRow rowTitle = table.getRow(0);
        for(int i=0;i<title.length;i++){
        	String s = title[i];
        	rowTitle.getCell(i).setParagraph(p); //样式
    		rowTitle.getCell(i).setText(s); //值
    	}
        //添加表格数据
        if(list!=null && list.size()>0){
        	for(int i=0;i<list.size();i++){
        		Object[] obj = list.get(i);
        		XWPFTableRow tr = table.createRow();
        		for(int j=0; j<obj.length;j++){
//        			设置样式
            		tr.getCell(j).setParagraph(p);
//            		设值
            		tr.getCell(j).setText(obj[j].toString());
        		} 	
            }
        }
       /*******************创建表格 end********************/
       
	}
	
	
	/**
	 * 遍历取得word文档中的表格（只能取得表格中内容）
	 * @param file
	 */
	public static void readTable(String filename){   
        try{   
           FileInputStream in = new FileInputStream(filename);//载入文档   
           POIFSFileSystem fs = new POIFSFileSystem(in);      
           HWPFDocument doc = new HWPFDocument(fs);      
           Range range = doc.getRange();//得到文档的读取范围   
           TableIterator it = new TableIterator(range);   
           //迭代文档中的表格   
           while (it.hasNext()) {      
                Table tb = (Table) it.next();      
                //迭代行，默认从0开始   
                for (int i = 0; i < tb.numRows(); i++) {      
                    TableRow tr = tb.getRow(i);      
                    //迭代列，默认从0开始   
                    for (int j = 0; j < tr.numCells(); j++) {      
                        TableCell td = tr.getCell(j);//取得单元格
                        //取得单元格的内容   
                        for(int k=0;k<td.numParagraphs();k++){      
                            Paragraph para =td.getParagraph(k); 
                            String s = para.text();      
                            System.out.println(s);   
                        }       
                    }   
                } 
           } 
        }catch(Exception e){   
            e.printStackTrace();   
        }   
    }
	
	/**
	 * 替换word中的内容
	 * @param filename
	 * @param oldStr
	 * @param newStr
	 */
	public static void replaceWord(String filename,String oldStr,String newStr){
		FileInputStream in;
		try {
			in = new FileInputStream(filename);
			HWPFDocument doc = new HWPFDocument(in);
			Range range = doc.getRange();
			range.replaceText(oldStr, newStr);
			String text = range.text();
			writeWord(text,filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取word文档，并写入另一文档
	 *
	 */
	public static void copyWord(){
		FileInputStream in;
		try {
			in = new FileInputStream("D:\\wordTable.docx");
			POIFSFileSystem fs = new POIFSFileSystem(in);
			HWPFDocument doc = new HWPFDocument(fs);
			Range range = doc.getRange();
			String text = range.text();
			System.out.println("old-----"+text);
			
			FileOutputStream out = new FileOutputStream("D:\\word2.doc");
			fs.writeFilesystem(out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	/**
	 * 建目录
	 * @param filename
	 * @return
	 */
	public static boolean mkDir(String filename){
		int last = filename.lastIndexOf("\\");
		String dirPath = filename.substring(0, last);
		File dir = new File(dirPath);
		if(!dir.exists()){//不存在
			return dir.mkdirs();
		}
		return true;
	}
	
	
	public static void main(String[] args) throws Exception {
		String file1 = "D:\\word.doc";
		String file2 = "D:\\wordTable.docx";
		
		
	
		//模拟数据
		String content = "这是我的导出word文档内容。The quick brown fox";
		String[] title = new String[]{"编号","姓名","年龄"};
		List<Object[]> list = new ArrayList<Object[]>();
		for(int i=0;i<5;i++){
			Object[] obj = new Object[3];
			obj[0] = "A" + i;
			obj[1] = "Zhang" + i;
			obj[2] = "1" + i;
			list.add(obj);
 		}
		
		createWord(file1, content, title, list);
//		copyWord();
//		writeWord(content ,file1);
		
	}

}
