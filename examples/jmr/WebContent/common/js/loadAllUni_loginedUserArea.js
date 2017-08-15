/* load head data footer */

$(function(){
	$(".warHead_uni").load("load/head_uni.jsp?"+(new Date()).getTime());
	$(".left_container").load("load/data_loginedUserArea.jsp?"+(new Date()).getTime());
	$(".warFooter").load("load/footer.jsp?"+(new Date()).getTime());
});