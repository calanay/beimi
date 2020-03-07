var beiMiCommon = require("BeiMiCommon");
cc.Class({
	extends: beiMiCommon,

	properties: {
        qrgraphics:{
            default : null ,
            type : cc.Node
        },
        roomid:{
            default : null ,
            type : cc.Label
        }
	},

	// use this for initialization
	onLoad: function() {

	},
    init:function(data){
	    if(data!=null && data != ""){
	        this.roomid.string = "让好友扫描加入房间，房间号："+data ;
        }
        var qrcode = new QRCode(6, QRErrorCorrectLevel.H);
        qrcode.addData(data);

        qrcode.make();

        let size = this.qrgraphics.width;
        let num = qrcode.getModuleCount();
        var ctx = this.qrgraphics.getComponent(cc.Graphics);
        ctx.clear();
        ctx.fillColor = cc.Color.BLACK;
        // compute tileW/tileH based on node width and height
        var tileW = size / num;
        var tileH = size / num;
        // draw in the Graphics
        for (var row = 0; row < num; row++) {
            for (var col = 0; col < num; col++) {
                if (qrcode.isDark(row, col)) {
                    // cc.log(row, col)
                    // ctx.fillColor = cc.Color.BLACK;
                    var w = (Math.ceil((col + 1) * tileW) - Math.floor(col * tileW));
                    var h = (Math.ceil((row + 1) * tileW) - Math.floor(row * tileW));
                    ctx.rect(Math.round(col * tileW), size - tileH - Math.round(row * tileH), w, h);
                    ctx.fill();
                } else {
                    // ctx.fillColor = cc.Color.WHITE;
                }
                // var w = (Math.ceil((col + 1) * tileW) - Math.floor(col * tileW));
                // var h = (Math.ceil((row + 1) * tileW) - Math.floor(row * tileW));
                // ctx.rect(Math.round(col * tileW), Math.round(row * tileH), w, h);
                // ctx.fill();
            }
        }
    }
});