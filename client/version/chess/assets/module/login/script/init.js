cc.Class({
    extends: cc.Component,

    properties: {
        // foo: {
        //    default: null,      // The default value will be used only when the component attaching
        //                           to a node for the first time
        //    url: cc.Texture2D,  // optional, default is typeof default
        //    serializable: true, // optional, default is true
        //    visible: true,      // optional, default is true
        //    displayName: 'Foo', // optional
        //    readonly: false,    // optional, default is false
        // },
        // ...
        _progress:0.0,
        _splash:null,
        _isLoading:false,
        loaddingPrefab: {
            default: null,
            type: cc.Prefab
        },
        alertPrefab: {
            default: null,
            type: cc.Prefab
        }
    },

    // use this for initialization
    onLoad: function () {
        if(!cc.sys.isNative && cc.sys.isMobile){
            var canvas = this.node.getComponent(cc.Canvas);
            canvas.fitHeight = true;
            canvas.fitWidth = true;
        }
        let win = cc.director.getWinSize() ;
        cc.view.setDesignResolutionSize(win.width, win.height, cc.ResolutionPolicy.EXACT_FIT);
        this.initMgr();

    },
    start:function(){        
        var self = this;
        var SHOW_TIME = 3000;
        var FADE_TIME = 500;
        /***
         * 
         * 控制登录界面或者广告首屏界面显示时间
         * 
         */
    },
    initMgr:function(){
        if(cc.beimi == null){
            /**
             * 增加了游戏全局变量控制，增加了 cc.beimi.gamestatus 参数，可选值：ready|notready|playing
             * @type {{}}
             */
            cc.beimi = {};
            cc.beimi.routes = {} ;
            cc.beimi.http = require("HTTP");
            cc.beimi.seckey = "beimi";
            cc.beimi.gamestatus = "none" ;




            cc.beimi.dialog = null ;   //弹出的提示对话框，  alert

            cc.beimi.openwin = null ;  //弹出的对话窗口，    设置、玩法、战绩等等

            cc.beimi.loadding = new cc.NodePool();
            cc.beimi.loadding.put(cc.instantiate(this.loaddingPrefab)); // 创建节点

            cc.beimi.dialog = new cc.NodePool();
            cc.beimi.dialog.put(cc.instantiate(this.alertPrefab)); // 创建节点

            /**
             * 游客登录，无需弹出注册对话框，先从本地获取是否有过期的对话数据，如果有过期的对话数据，则使用过期的对话数据续期
             * 如果没有对话数据，则重新使用游客注册接口
             */
            // this.loginFormPool = new cc.NodePool();
            // this.loginFormPool.put(cc.instantiate(this.prefab)); // 创建节点
            cc.beimi.game = {
                model : null ,
                playway : null,
                type:function(name){
                    var temp ;
                    if(cc.beimi.games !=null){
                        for(var i=0 ; i<cc.beimi.games.length ; i++){
                            var gamemodel = cc.beimi.games[i] ;
                            for(var inx = 0 ; inx < gamemodel.types.length ; inx++){
                                var  type = gamemodel.types[inx] ;
                                if(type.code == name){
                                    temp = type ;
                                }
                            }
                        }
                    }
                    return temp ;
                }
            };

            var Audio = require("Audio");
            cc.beimi.audio = new Audio();
            cc.beimi.audio.init();

            if(cc.sys.isNative){
                window.io = SocketIO;
            }else{
                window.io = require("socket.io");
            }

            cc.beimi.audio.playBGM("bgMain.mp3");
        }
    }

});
