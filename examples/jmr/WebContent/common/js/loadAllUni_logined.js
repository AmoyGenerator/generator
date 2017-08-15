/* load head data footer */
$(function(){
	$(".warHead_uni").load("load/head_uni.jsp?"+(new Date()).getTime());
	$(".warData").load("load/data.jsp?"+(new Date()).getTime());
	$(".warFooter").load("load/footer.jsp?"+(new Date()).getTime());
});