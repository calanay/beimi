
/**
 * Created by R3_PC_3 on 2016/9/7.
 */
<!-- 换肤 -->
//读取cookie，换肤
var skin = document.getElementById("skin");//拿到link元素
var cookieval = document.cookie;
var skipval = readcookie("skin");
if (skipval) {//如果cookie不存在记录
    skin.href = "/assets/2.0/css/default/R3.css";
} else {
    // skin.href = skipval + ".css";//有记录
    skin.href ="/assets/2.0/css/default/R3.css";
}
;
if (document.all){ 
    window.attachEvent("onload",change1)//对于IE 
}else{ 
    window.addEventListener("load",change1,false);//对于FireFox 
}
function change1(){
    //点击按钮换肤
    var skin1 = document.getElementById("skin1");
    var skin2 = document.getElementById("skin2");
    var skin3 = document.getElementById("skin3");
    var skin4 = document.getElementById("skin4");
    var skin5 = document.getElementById("skin5");
    var skin6 = document.getElementById("skin6");
    var Days = 30;  //设置过期时间，30天以后
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    skin1.onclick = function () {
        skin.href = "/assets/2.0/css/blue01/R3.css";
        document.cookie = "skin=1;expires=" + exp.toUTCString();
    };
    skin2.onclick = function () {
        skin.href = "/assets/2.0/css/blue02/R3.css";
        document.cookie = "skin=2;expires=" + exp.toUTCString();
    };
    skin3.onclick = function () {
        skin.href = "/assets/2.0/css/gray/R3.css";
        document.cookie = "skin=3;expires=" + exp.toUTCString();
    };
    skin4.onclick = function () {
        skin.href = "/assets/2.0/css/default/R3.css";
        document.cookie = "skin=3;expires=" + exp.toUTCString();
    };
    skin5.onclick = function () {
        skin.href = "/assets/2.0/css/purple/R3.css";
        document.cookie = "skin=3;expires=" + exp.toUTCString();
    };
    skin6.onclick = function () {
        skin.href = "/assets/2.0/css/red/R3.css";
        document.cookie = "skin=3;expires=" + exp.toUTCString();
    };
}
//读取cookie指定值
function readcookie(key) {
    var skinval = false;
    var arrkv = cookieval.split(";");
    for (var i = 0; i < arrkv.length; i++) {
        var itemc = arrkv[i].split("=");
        if (itemc[0] == key) {
            skinval = itemc[1];
        }
    }
    ;
    return skinval;
};
if (document.all){ 
    window.attachEvent("onload",change2)//对于IE 
}else{ 
    window.addEventListener("load",change2,false);//对于FireFox 
}
 // 咨询切换
 function change2(){
    var press=document.getElementById('press');
    var start=document.getElementById('start');
    var btn=document.getElementById('btn-change');
    btn.onclick=function(){
        var bar=document.getElementById('bar');
        console.log(press);
        if(bar.style.display=='none'){
            bar.style.display='block';
            press.style.display='none';
        }else{
            return false;           
        }
        bar.onclick=function(){
            start.style.display='block';
            bar.style.display='none';
        }
    } 
}
if(document.all){
    window.attachEvent("onload",change3);
}else{
    window.addEventListener("load",change3,false);
}
function change3(){
    var close=document.getElementById("close-chat");
    var start=document.getElementById('start');
    close.onclick=function(){
        var bar=document.getElementById("bar");
        if(bar.style.display=="none"){
            start.style.display="none";
            bar.style.display="block";
        }else{
            return false;
        }

    }
}
// 工作时间与非工作时间的判断
if(document.all){
    window.attachEvent("onload",change4);
}else{
    window.addEventListener("load",change4,false);
}
function change4(arr){
    var arr=['8:00:00','12:00:00','13:00:00','18:00:00'];
    var date=new Date();
    var hour=date.getHours();
    var minute=date.getMinutes();
    var second=date.getSeconds();
    var current=parseInt(hour*3600)+parseInt(minute*60)+parseInt(second);
    var ar_begin1=arr[0].split(':');
    var ar_begin2=arr[2].split(':');
    var ar_end1=arr[1].split(':');
    var ar_end2=arr[3].split(':');
    var b1=parseInt(ar_begin1[0]*3600)+parseInt(ar_begin1[1]*60)+parseInt(ar_begin1[2]);
    var b2=parseInt(ar_begin2[0]*3600)+parseInt(ar_begin2[1]*60)+parseInt(ar_begin2[2]);
    var e1=parseInt(ar_end1[0]*3600)+parseInt(ar_end1[1]*60)+parseInt(ar_end1[2]);
    var e2=parseInt(ar_end2[0]*3600)+parseInt(ar_end2[1]*60)+parseInt(ar_end2[2]);
    if(current>=b1&&current<=e1||current>=b2&&current<=e2){
        return true;
    }else{
         return true;//window.location.href="leaveMessage.html";   
    }
}
// 根据窗口大小改变盒子高度
window.onresize=function(){
    var above=document.getElementById("above");
    var bot=document.getElementById("bottom");
    var height=document.documentElement.clientHeight-245;
    above.style.height=height+"px";
    if(above.style.height<=50){
        bot.style.top=50+"px";
    }else{
         // bot.style.top=above.style.height;
    }
    console.log(above.style.height);
}
// 接收消息闪动
if(document.all){
    window.attachEvent("onload",remain);
}else{
    window.addEventListener("load",remain,false);
}
function remain(){
    var bar=document.getElementById("bar");
    if(bar.style.display=="block"){
        bar.style.display="none";
    }else{
        bar.style.display="block";
    }
    t=setTimeout("remain()",300);
    bar.onmouseover=function(){
        clearTimeout(t);
    }
}