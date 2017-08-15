package app.other.poi.excel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * 类说明:Cvs工具类
 */
public class CsvUtil {

	private String filename = null;

	private BufferedReader bufferedreader = null;

	private List list = new ArrayList();

	public CsvUtil() {
	}

	public CsvUtil(String filename) throws IOException {
		this.filename = filename;
		bufferedreader = new BufferedReader(new FileReader(filename));
		String stemp;
		//分行读取文件
		while ((stemp = bufferedreader.readLine()) != null) {
			stemp = stemp.replace("\"", ""); //去除引号
			list.add(stemp);
		}
	}

	/**
	 * 取得所有行
	 * @return
	 * @throws IOException
	 */
	public List getList() throws IOException {
		return list;
	}

	/**
	 * 共几行
	 * @return
	 */
	public int getRowNum() {
		return list.size();
	}

	/**
	 * 共几列
	 * @return
	 */
	public int getColNum() {
		if (!list.toString().equals("[]")) {
			if (list.get(0).toString().contains(",")) {
				return list.get(0).toString().split(",").length;
			} else if (list.get(0).toString().trim().length() != 0) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	/**
	 * 取得第几列数据
	 * @param index
	 * @return
	 */
	public String getRow(int index) {
		if (this.list.size() != 0)
			return (String) list.get(index);
		else
			return null;
	}

	/**
	 * 取得第几行数据
	 * @param index
	 * @return
	 */
	public String getCol(int index) {
		if (this.getColNum() == 0) {
			return null;
		}
		StringBuffer scol = new StringBuffer();
		String temp = null;
		int colnum = this.getColNum();
		if (colnum > 1) {
			for (Iterator it = list.iterator(); it.hasNext();) {
				temp = it.next().toString();

				scol = scol.append(temp.split(",")[index] + ",");
			}
		} else {
			for (Iterator it = list.iterator(); it.hasNext();) {
				temp = it.next().toString();
				scol = scol.append(temp + ",");
			}
		}
		String str = new String(scol.toString());
		str = str.substring(0, str.length() - 1);
		return str;
	}

	/**
	 * 取得第(行, 列）数据
	 * @param row
	 * @param col
	 * @return
	 */
	public String getString(int row, int col) {
		String temp = null;
		int colnum = this.getColNum();
		if (colnum > 1) {
			temp = list.get(row).toString().split(",")[col];
		} else if (colnum == 1) {
			temp = list.get(row).toString();
		} else {
			temp = null;
		}
		return temp;
	}

	/**
	 * 关闭数据流
	 * @throws IOException
	 */
	public void CsvClose() throws IOException {
		this.bufferedreader.close();
	}

	/**
	 * 测试
	 * @throws IOException
	 */
	public void test() throws IOException {
		CsvUtil cu = new CsvUtil("c:/test.csv");
		List tt = cu.getList();
		for (Iterator itt = tt.iterator(); itt.hasNext();) {
			System.out.println(itt.next().toString() + "||");
		}
		 System.out.println(cu.getRowNum());
		 System.out.println(cu.getColNum());
		 System.out.println(cu.getRow(0));
		 System.out.println(cu.getCol(0));
		 System.out.println(cu.getString(0, 0));
		cu.CsvClose();

	}

	/**
	 * 测试
	 * @param Response
	 * @throws IOException
	 */
	public void createCsvTest(HttpServletResponse Response) throws IOException {
		CsvUtil cu = new CsvUtil("c:/test.csv");
		List tt = cu.getList();
		String data = "";

		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmm");
		Date today = new Date();
		String dateToday = dataFormat.format(today);
		File file = new File("c:/test.csv");
		if (!file.exists())
			file.createNewFile();
		// else
		// file.delete() ;
		String str[];
		StringBuilder sb = new StringBuilder("");
		BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
		for (Iterator itt = tt.iterator(); itt.hasNext();) {
			String fileStr = itt.next().toString();
			str = fileStr.split(",");
			for (int i = 0; i <= str.length - 1; i++) { // 拆分成阵列 用于插入资料库中
				System.out.print("str[" + i + "]=" + str[i] + " ");
			}
			System.out.println("");
			sb.append(fileStr + "\r\n");
		}
		// System.out.println(sb.toString());
		output.write(sb.toString());
		output.flush();
		output.close();
		cu.CsvClose();
	}

	public static void main(String[] args) throws IOException {
		CsvUtil test = new CsvUtil();
		 test.test();
//		HttpServletResponse response = null;
//		test.createCsvTest(response);
	}
}