var beiMiCommon = require("BeiMiCommon");
cc.Class({
    extends: beiMiCommon,
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
        tag: {
            default: null,
            type: cc.Node
        },
        score: {
            default: null,
            type: cc.Label
        },
        onlineusers: {
            default: null,
            type: cc.Label
        },
        scorelimit: {
            default: null,
            type: cc.Label
        },
        atlas: {
            default: null,
            type: cc.SpriteAtlas
        }
    },

    // use this for initialization
    onLoad: function () {

    },
    init:function(playway){
        /**
         * 需要预先请求 在线人数
         */
        if(playway){
            var frameName  = "初级";
            if(playway.level == '2'){
                frameName = "高级"
            }
            this.data = playway ;

            if(playway.shuffle == false){
                this.tag.active = false ;
            }else{
                this.tag.active = true;
            }

            frameName = frameName + playway.skin ;

            this.getComponent(cc.Sprite).spriteFrame = this.atlas.getSpriteFrame(frameName);

            this.onlineusers.string = playway.onlineusers + " 人 " ;
            var min = parseInt(playway.mincoins/1000)+"千" ;
            if(playway.mincoins >= 10000){
                min = parseInt(playway.mincoins / 10000)+"万" ;
            }
            var max = parseInt(playway.maxcoins/1000)+"千" ;
            if(playway.maxcoins >= 10000){
                max = parseInt(playway.maxcoins / 10000)+"万" ;
            }
            this.scorelimit.string = min + "-" + max ;

            this.score.string = playway.score ;
        }
    }
    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
});
