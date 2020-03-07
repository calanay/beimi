// 咨询页面
	document.write("<div class=\"service\" id=\"press\">"+
	"<div class=\"icon-box\" style=\"width:100%;height:20px\"></div>"+
	"<div class=\"white-box\">"+"<h1>在线客服"+
	"<span class=\"small\">系统，你所</span>"+"想要"
	+"<span class=\"small\">的：</span>"+"</h1>"
	+"<h2>功能/速度/数据/稳定/服务</h2>"
	+"<h3>应有尽有.......</h3>"
	+"<img src=\"/assets/2.0/img/server.png\">"+"</div>"
	+"<div class=\"service-btn\">"+"<button type=\"button\"class=\"send-btn active special  clearfix\" >"
	+"<a href=\"/conversion.html\">开始咨询</a>"+"</button>"
	+"<button type=\"button\" class=\"send-btn active clearfix\" id=\"btn-change\">"
	+"<a href=\"#\">稍后咨询</a>"+"</button>"
	+"</div>"
	+"</div>");
// 点我咨询条
	document.write("<div id=\"bar\" style=\"display:none\">"+"<div class=\"medium\">"
	+" <a href=\"#\">点我咨询</a>"+"</div>"+"</div>");
//用js生成iframe
if(document.all){
	window.attachEvent("onload",read2);
}else{
	window.addEventListener("load",read2,false);
}
function read2(){
	var iframe=document.createElement("iframe");
	iframe.src="little-buttery.html";
	iframe.width="100%";
	iframe.height="100%";
	iframe.setAttribute("frameborder","0");
	document.body.appendChild(iframe);
}