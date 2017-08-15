package ssh.hibernate.page;

import javax.servlet.http.HttpServletRequest;



/**
 * 类说明
 */
public class PageShow {
	
	/**
     * 分页前台显示代码（文本框）
     * @return
     */
    public static String getListPageStr(ListPage listPage){
    	StringBuffer listPageDisplay = new StringBuffer();
    	listPageDisplay.append("<span style=\" padding-right:5px;\">");
    	listPageDisplay.append(" 共"  +  listPage.getTotalPageCount() + "页 第" +  listPage.getCurrentPageNo() + "页 <a href=\"javascript:gotoPage(1);\" class=\"blue\">首页</a> ");
    	if (listPage.hasPreviousPage()) {
    	listPageDisplay.append(" <a href=\"javascript:gotoPage(" + (listPage.getCurrentPageNo() - 1) + ");\" class=\"blue\">上一页</a> ");
    	} else {
    	listPageDisplay.append(" 上一页 ");
    	} 
    	if (listPage.hasNextPage()) {
    	listPageDisplay.append(" <a href=\"javascript:gotoPage(" + (listPage.getCurrentPageNo() + 1) + ");\" class=\"blue\">下一页</a> ");
    	} else {
    	listPageDisplay.append("  下一页  ");
    	}
    	listPageDisplay.append(" <a href=\"javascript:gotoPage(" + listPage.getTotalPageCount() + ");\"  class=\"blue\">尾页</a> ");
    	listPageDisplay.append(" 到 <input id=\"no2\" name=\"no2\"  type=\"text\" size=\"2\" /> 页<a href=\"javascript:gotoPage(document.baseForm.no2.value);\" class=\"blue\">GO</a>  ");
    	listPageDisplay.append(" </span> ");
    	return listPageDisplay.toString();
    }
    
    /**
     * 分页前台显示代码(下拉框）
     * @return
     */
    public static String getListPageSelect(ListPage listPage){
    	StringBuffer listPageDisplay = new StringBuffer();
    	listPageDisplay.append("<span style=\" padding-right:5px;\">");
    	listPageDisplay.append(" 共"  +  listPage.getTotalPageCount() + "页 第" +  listPage.getCurrentPageNo() + "页 <a href=\"javascript:gotoPage(1);\" class=\"blue\">首页</a> ");
    	if (listPage.hasPreviousPage()) {
    	listPageDisplay.append(" <a href=\"javascript:gotoPage(" + (listPage.getCurrentPageNo() - 1) + ");\" class=\"blue\">上一页</a> ");
    	} else {
    	listPageDisplay.append(" 上一页 ");
    	} 
    	if (listPage.hasNextPage()) {
    	listPageDisplay.append(" <a href=\"javascript:gotoPage(" + (listPage.getCurrentPageNo() + 1) + ");\" class=\"blue\">下一页</a> ");
    	} else {
    	listPageDisplay.append("  下一页  ");
    	}
    	listPageDisplay.append(" <a href=\"javascript:gotoPage(" + listPage.getTotalPageCount() + ");\"  class=\"blue\">尾页</a> ");
    	listPageDisplay.append(" 到<select name=\"no2\" id=\"no2\" onchange=\"gotoPage(this.value)\"> ");
    	for(int i=1; i<=listPage.getTotalPageCount(); i++){
    		listPageDisplay.append(" <option value=\"" + i + "\" ");
    		listPageDisplay.append(i==listPage.getCurrentPageNo()?" selected ":" ");
    		listPageDisplay.append(">");
    		listPageDisplay.append(i);
    		listPageDisplay.append("</option> ");
    	}
    	listPageDisplay.append("</select> ");
    	listPageDisplay.append(" </span> ");
    	return listPageDisplay.toString();
    }
	
	/**
     * 分页前台显示代码(封装用于前台显示)
     * @return
     */
    public static String getListPageFrontStr(ListPage listPage){
    	int currentNo = listPage.getCurrentPageNo(); //当前页
    	StringBuffer listPageDisplay = new StringBuffer();
    	listPageDisplay.append(" <td width=65><span class='text_page02'><a href=\"javascript:gotoPage(1);\">&lt; 第一页 &gt;</a></span></td> ");
    	if (listPage.hasPreviousPage()) {
    		listPageDisplay.append(" <td width=20><span class=text_page02><a href=\"javascript:gotoPage(" + (listPage.getCurrentPageNo() - 1) + ");\"><img src=\"images/page_02.gif\" width=10 height=9 border=0></a></span></td> ");
    	}else{
    		listPageDisplay.append(" <td width=20><span class=text_page02><img src=\"images/page_02.gif\" width=10 height=9 border=0></span></td> ");
    	}
    	
    	if(listPage.getTotalPageCount()>1){
    		for(int i=1;i<=listPage.getTotalPageCount();i++){
    			String style = currentNo==i ?" style=color:gray " : " ";
    			listPageDisplay.append(" <td class=text_page><a href=\"javascript:gotoPage(" + i + ");\"" + style + ">"+i+"</a>&nbsp;</td> ");
    		}
    	}else{
    		listPageDisplay.append(" <td class=text_page><a href=\"javascript:gotoPage(" + listPage.getTotalPageCount() + ");\">"+listPage.getTotalPageCount()+"</a>&nbsp;&nbsp;</td> ");
    	}

    	if (listPage.hasNextPage()) {
    		listPageDisplay.append(" <td width=20 align=right><span class=text_page02><a href=\"javascript:gotoPage(" + (listPage.getCurrentPageNo() + 1) + ");\"><img src=\"images/page_01.gif\" width=10 height=9 border=0></a></span></td> ");
    	} else {
    		listPageDisplay.append(" <td width=20 align=right><span class=text_page02><img src=\"images/page_01.gif\" width=10 height=9 border=0></span></td> ");
    	}
    	listPageDisplay.append(" <td width=65 align=right><span class=text_page02><a href=\"javascript:gotoPage(" + listPage.getTotalPageCount() + ");\">&lt; 最后页 &gt;</a></span></td> ");
    	return listPageDisplay.toString();
    	
    }
    
  

}
