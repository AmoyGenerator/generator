<%@ page pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${!empty listPage }">
<table width="100%" align="center" cellpadding="0" cellspacing="0"
	style="font-size:12px">
	<tr>
		 
		<td height="20" align="left" style="padding-left:5px;">
			<!--  
			<a href="javascript:void(0);"
				onClick="selectOrNotAll(baseForm, 'ids', true);" class="blue"><span
				style="cursor:pointer;">全部选择</span>
			</a> |
			<a href="javascript:void(0);"
				onClick="selectOrNotAll(baseForm, 'ids', false);" class="blue"><span
				style="cursor:pointer;">取消选择</span>
			</a> |
			<a href="javascript:void(0);"
				onClick="reverseSelect(baseForm, 'ids');" class="blue"><span
				style="cursor:pointer;">反向选择</span>
			</a>
			
			<input type="button" value="删除" id="delete" onClick="batchDel();" />
			-->
		</td>
		
		<td align="right">
			<span style="padding-right:5px;">
    	     共${listPage.totalPageCount } 頁第 ${ listPage.currentPageNo} 頁 <a href="javascript:gotoPage(1);" class="blue">首頁</a>

    	<c:choose>
    	<c:when test="${listPage.currentPageNo > 1}">
    	<a href="javascript:gotoPage(${listPage.currentPageNo- 1});" class="blue">上一頁</a> 
    	</c:when>
    	<c:otherwise>
    	 上一頁
    	</c:otherwise>
    	</c:choose>
    	
    	<c:choose>
    	<c:when test="${listPage.currentPageNo < listPage.totalPageCount }">
    	<a href="javascript:gotoPage(${listPage.currentPageNo + 1});" class="blue">下一頁</a>  
    	</c:when>
    	<c:otherwise>
    	 下一頁
    	</c:otherwise>
    	</c:choose>
    
    	<a href="javascript:gotoPage(${listPage.totalPageCount});"  class="blue">尾頁</a> 到
  
    	 <select name="no2" id="no2" onchange="gotoPage(this.value)">
    	 <c:forEach var="x" begin="1" end="${listPage.totalPageCount}">
    	 <option value="${x }" 
    	 <c:if test="${listPage.currentPageNo==x }">
    	 selected
    	 </c:if>
    	 >${x }</option>
    	 </c:forEach>
    	 </select> 
    	
    	</span>
		</td>
	</tr>
</table>
</c:if>
