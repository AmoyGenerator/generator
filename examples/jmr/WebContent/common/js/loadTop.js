/* load head */
var i =0;
$(function(){
	i++;
	$(".warHead").load("load/head.jsp?"+i);
});