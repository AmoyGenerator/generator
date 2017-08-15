<%@ page pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${!empty listPage }">
<table width="100%" align="center" cellpadding="0" cellspacing="0"
	style="font-size:12px">
	<tr>
		 
		<td height="20" align="left" style="padding-left:5px;">
			<a href="javascript:void(0);"
				onClick="selectOrNotAll(baseForm, 'ids', true);" class="blue"><span
				style="cursor:pointer;">全部選擇</span>
			</a> |
			<a href="javascript:void(0);"
				onClick="selectOrNotAll(baseForm, 'ids', false);" class="blue"><span
				style="cursor:pointer;">取消選擇</span>
			</a> |
			<a href="javascript:void(0);"
				onClick="reverseSelect(baseForm, 'ids');" class="blue"><span
				style="cursor:pointer;">反向選擇</span>
			</a>
			<input type="button" value="删除" id="delete" onClick="batchDel();" />
		</td>
	</tr>
</table>
</c:if>
