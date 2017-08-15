package ssh.hibernate.page;
import java.util.List;
/**
 * 分页
 *
 */
public class ListPage {

    public static final ListPage EMPTY_PAGE = new ListPage(){
        public int getTotalPageCount(){
            return 0 ;
        }
    };

    //一页几条记录
    private int currentPageSize;
    //记录总数
    private int totalSize;
    //第几页
    private int currentPageNo;
    //总页数
    private int totalPageCount;
    //数据列表
    private List dataList;
    
    public ListPage() {
    }    
    
    public int getCurrentPageNo() {
        return currentPageNo;
    }
   
    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }
   
    public int getCurrentPageSize() {
        return currentPageSize;
    }
    
    public void setCurrentPageSize(int currentPageSize) {
        this.currentPageSize = currentPageSize;
    }
    
    public List getDataList() {
        return dataList;
    }
 
    public void setDataList(List dataList) {
        this.dataList = dataList;
    }
    
    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
    
    
    public int getTotalPageCount(){
     	return (getTotalSize() - 1) / getCurrentPageSize() + 1 ;
    }
    
   
    public boolean hasNextPage() {
      return (this.getCurrentPageNo() < this.getTotalPageCount());
    }

   
    public boolean hasPreviousPage() {
      return (this.getCurrentPageNo() > 1);
    }    
    
    public boolean isEmpty(){
        return this == ListPage.EMPTY_PAGE;
    }

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
    
}
