var beiMiCommon = require("BeiMiCommon");

cc.Class({
    extends: beiMiCommon,
    properties: {
        // foo: {
        //     // ATTRIBUTES:
        //     default: null,        // The default value will be used only when the component attaching
        //                           // to a node for the first time
        //     type: cc.SpriteFrame, // optional, default is typeof default
        //     serializable: true,   // optional, default is true
        // },
        // bar: {
        //     get () {
        //         return this._bar;
        //     },
        //     set (value) {
        //         this._bar = value;
        //     }
        // },
        numdata: {
            default:null,
            type:cc.Node


        }
    },

    // LIFE-CYCLE CALLBACKS:

    onLoad : function () {
        this.roomid = new Array() ;
    },
    onClick:function(event,data){
        if(this.roomid.length < 6){
            this.roomid.push(data);
            this.disRoomId();
        }
        if(this.roomid.length == 6){
            this.closeOpenWin();
            /**
             * 查询服务端的房间号码 ， 然后通过房间号码找到对应的房间游戏类型，玩法等信息
             */
            if(this.ready()){
                let socket = this.socket();
                /**
                 * 发送 room请求
                 */
                var param = {
                    token:cc.beimi.authorization,
                    roomid:this.roomid.join(""),
                    orgi:cc.beimi.user.orgi,
                    userid:cc.beimi.user.id
                } ;
                socket.emit("searchroom" , JSON.stringify(param));
                this.registercallback(this.roomCallBack);
            }
            this.loadding();
        }
    },
    roomCallBack:function(result , self){
        var data = self.parse(result) ;
        if(data.result == "ok"){
            var extparams = {
                gametype : data.code ,
                playway :  data.id ,
                gamemodel : "room"
            } ;
            /**
             * 发送创建房间开始游戏的请求
             */
            self.preload(extparams , self) ;
        }else if(data.result == "notexist"){
            self.alert("房间号不存在。");
        }else if(data.result == "full"){
            self.alert("房间已满员。");
        }
    },
    onDeleteClick:function(){
        this.roomid.splice(this.roomid.length-1 , this.roomid.length) ;
        this.disRoomId();
    },
    onCleanClick:function(){
        this.roomid.splice(0 , this.roomid.length) ;
        this.disRoomId();
    },
    disRoomId:function(){
        let children = this.numdata.children ;
        for(var inx = 0 ; inx < 6 ; inx ++){
            if(inx < this.roomid.length){
                children[inx].children[0].getComponent(cc.Label).string = this.roomid[inx] ;
            }else{
                children[inx].children[0].getComponent(cc.Label).string = "" ;
            }
        }
    }
    // update (dt) {},
});
