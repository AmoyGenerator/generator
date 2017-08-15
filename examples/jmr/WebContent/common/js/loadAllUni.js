/* load head data footer */
$(function(){
	$(".warHead_uni").load("/realgood/load/head_uni.jsp?"+(new Date()).getTime());
	$(".warData").load("/realgood/load/data.jsp?"+(new Date()).getTime());
	$(".warFooter").load("/realgood/load/footer.jsp?"+(new Date()).getTime());
});