/* load head footer */
var i =0;
$(function(){
	i=i+1;
	$(".warHead").load("load/head.jsp?"+i);
	$(".warFooter").load("load/footer.jsp?"+i);
});