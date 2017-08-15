package app.other.poi.excel;

import java.util.List;

/**
 * 类说明:Excel的模型，模仿Excel的结构
 */
public class ExcelBean {
	
	private String path; //Excel写入的路径名
	private String sheetName; //工作表名称（Excel的左下角）
	private int row; //行数
	private int col; //列数
	private List<String> titleList; //列表头标题
	private int[] width; //每列所占宽度
	private List dataList; //数据集合
	
	public List getDataList() {
		return dataList;
	}
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getTitleList() {
		return titleList;
	}
	public void setTitleList(List<String> titleList) {
		this.titleList = titleList;
	}
	public int[] getWidth() {
		return width;
	}
	public void setWidth(int[] width) {
		this.width = width;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	

}
