<%@ page pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${!empty listPage }">
<table width="100%" align="center" cellpadding="0" cellspacing="0"
	style="font-size:12px">
	<tr>
		
		<td align="center">
			<span style="padding-right:5px;">
    	    <a href="javascript:gotoPage(1);" class="blue"><img src="${basePath}common/images/btn_pageFirst.gif" /></a> 

    	<c:choose>
    	<c:when test="${listPage.currentPageNo > 1}">
    	<a href="javascript:gotoPage(${listPage.currentPageNo- 1});" class="blue"><img src="${basePath}common/images/btn_pagePrev.gif" /></a> 
    	</c:when>
    	<c:otherwise>
    	<img src="${basePath}common/images/btn_pagePrev.gif" />
    	</c:otherwise>
    	</c:choose>
    	
    	<c:choose>
    	<c:when test="${listPage.currentPageNo < listPage.totalPageCount }">
    	<a href="javascript:gotoPage(${listPage.currentPageNo + 1});" class="blue"><img src="${basePath}common/images/btn_pageNext.gif" /></a>  
    	</c:when>
    	<c:otherwise>
    	<img src="${basePath}common/images/btn_pageNext.gif" />
    	</c:otherwise>
    	</c:choose>
    
    	<a href="javascript:gotoPage(${listPage.totalPageCount});"  class="blue"><img src="${basePath}common/images/btn_pageFinal.gif" /></a> on
  
    	 <select name="no2" id="no2" onchange="gotoPage(this.value)">
    	 <c:forEach var="x" begin="1" end="${listPage.totalPageCount}">
    	 <option value="${x }" 
    	 <c:if test="${listPage.currentPageNo==x }">
    	 selected
    	 </c:if>
    	 >${x }</option>
    	 </c:forEach>
    	 </select> 
    	page, total ${listPage.totalPageCount } pages
    	</span>
		</td>
	</tr>
</table>
</c:if>
