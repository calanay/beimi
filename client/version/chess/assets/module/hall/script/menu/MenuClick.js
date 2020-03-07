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
        setting: {
            default: null,
            type: cc.Prefab
        },
        message: {
            default: null,
            type: cc.Prefab
        },
        share: {
            default: null,
            type: cc.Prefab
        },
        playway: {
            default: null,
            type: cc.Prefab
        },
        feedback: {
            default: null,
            type: cc.Prefab
        }
    },


    // use this for initialization
    onLoad: function () {

    },
    onSettingClick:function(){
        cc.beimi.openwin = cc.instantiate(this.setting) ;
        cc.beimi.openwin.parent = this.root();
    },
    onMessageClick:function(){
        cc.beimi.openwin = cc.instantiate(this.message) ;
        cc.beimi.openwin.parent = this.root();
    },
    onShareClick:function(){
        cc.beimi.openwin = cc.instantiate(this.share) ;
        cc.beimi.openwin.parent = this.root();
    },
    onPlaywayClick:function(){
        cc.beimi.openwin = cc.instantiate(this.playway) ;
        cc.beimi.openwin.parent = this.root();
    },
    onRecordClick:function(){
        cc.beimi.openwin = cc.instantiate(this.playway) ;
        cc.beimi.openwin.parent = this.root();
    },
    onFeedBackClick:function(){
        cc.beimi.openwin = cc.instantiate(this.feedback) ;
        cc.beimi.openwin.parent = this.root();
    }

    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
});
