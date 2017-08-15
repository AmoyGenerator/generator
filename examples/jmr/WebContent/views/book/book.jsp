<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file="/common/include/views_head.jsp"%>
<body>
<div class="wrapper">
	<!-- left menu -->
    <%@ include file="/common/include/menu.jsp" %>
    
    <div class="rightContent">
    <span class="title">Book</span>
    <div class="mainContainer">
    	<form action="${basePath}book.do?act=find" method="post" name="baseForm" />
    	<div class="function">
	    	<div class="btnAddDelete">
	    		<input name="pageNo" value="${param.pageNo}" type="hidden"/>
				<input name="pageSize" value="${param.pageSize}" type="hidden"/>
				<input type="hidden" name="act" value=""/>
	    		
	    	</div><!-- End of btnAddDelete -->
	    	<img src="${basePath}/common/images/icon_quiz.gif" />&nbsp;&nbsp;Query&nbsp;&nbsp;&nbsp;&nbsp;

	  			<input style="width:60px;" value="Search" type="button" onclick="find();"/>&nbsp;&nbsp;
	    		<input style="width:50px;" value="New" type="button" onclick="toAdd('book')"/>&nbsp;&nbsp;
	    		<br/><br/>
	    		name:
				    <input name="name" id="name" 
				    value="${name}" type="text" style="width:90px"/>
	    		type:
				    <select id="type" name="type">
				    <option value="0">novel</option>
				    <option value="1">magazine</option>
                    </select>
                    <script>
                    $("#type").val("${type}");
                    </script>
	    		registTime:
			    <script>
                function setRegistTime(){
                  start = $("#registTime_start").val();
                  end = $("#registTime_end").val();
                  value = "{start:" + "\"" + start + "\"" + "," + "end:" + "\"" + end + "\"" + "}";
                  $("#registTime").val(value);
                }
                </script>
                <input type="hidden" id="registTime" name="registTime"/>
                <input id="registTime_start" name="registTime_start" value="${registTime_start}" 
                class="Wdate" onchange="setRegistTime()" 
                onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" style="width:135px"/>
                to
                <input id="registTime_end" name="registTime_end" value="${registTime_end}" 
                class="Wdate" onchange="setRegistTime()" 
                onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" style="width:135px"/>
        </div><!-- End of function -->
        
        <table width="630" class="tbl_green" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <th width="50">number</th>       
            <th width="50">name</th>
            <th width="50">type</th>
            <th width="50">registTime</th>
            <th width="60">operation</th>
          </tr>
          
          <c:if test="${empty listPage.dataList }">
			    <tr><td colspan="8" align="center">Cannot find data!</td></tr>
		  </c:if>
			            
				<c:if test="${!empty listPage.dataList }">
				  <c:set var="i" value="1" />
					<c:if test="${listPage.currentPageNo>0}">
						<c:set var="i" value="${(listPage.currentPageNo-1)*listPage.currentPageSize +1 }"/>
					</c:if>
				  
				   <c:forEach items="${listPage.dataList}" var="obj" varStatus="vs"> 
				   		 <tr class=${vs.count%2==1?"#ececec":"" }>
	                        <td align="center">&nbsp;${(listPage.currentPageNo-1)*listPage.currentPageSize + vs.count} </td>
							<td align="center">${obj.name}</td>
							<td align="center">
							    <c:if test="${obj.type == 0}">
							      novel
							    </c:if>
							    <c:if test="${obj.type == 1}">
							      magazine
							    </c:if>
						    </td>
							<td align="center">
							<fmt:formatDate value="${obj.registTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</td>
							
							<td align="center">
								<select name="changeOne" id="changeOne" onchange="changeone('${obj.id}',this.value,'book');">
									<option value="">Please select</option>
									<option value="0">update</option>
									<option value="1">delete</option>
								</select>
							</td>
				   		</tr>
				   </c:forEach>
				</c:if>
        </table>
        	<%@ include file="/common/include/views_detail.jsp"%>
	    </form>
    </div><!-- End of mainContainer -->
    </div><!-- End of rightContent -->
</div><!-- End of wrapper -->
</body>
</html>

