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
        username: {
            default: null,
            type: cc.Label
        },
        goldcoins: {
            default: null,
            type: cc.Label
        },
        cards: {
            default: null,
            type: cc.Label
        }
        ,
        girl:{
            default: null,
            type: cc.Node
        }
    },

    // use this for initialization
    onLoad: function () {
        let self = this ;
        if(this.ready()){
            this.username.string = cc.beimi.user.username ;
            this.pva_format(cc.beimi.user.goldcoins , cc.beimi.user.cards , cc.beimi.user.diamonds , self);
            this.pvalistener(self , function (context) {
                context.pva_format(cc.beimi.user.goldcoins , cc.beimi.user.cards , cc.beimi.user.diamonds , context) ;
            });
        }
    },
    pva_format:function(coins, cards , diamonds , object){
        if(coins > 9999){
            var num = coins / 10000  ;
            object.goldcoins.string = num.toFixed(2) + '万';
        }else{
            object.goldcoins.string = coins;
        }
        object.cards.string = cards + "张" ;
    },
    playToLeft:function(){
        this._girlAnimCtrl = this.girl.getComponent(cc.Animation);
        this._girlAnimCtrl.play("girl_to_left");
    },
    playToRight:function(){
        this._girlAnimCtrl = this.girl.getComponent(cc.Animation);
        this._girlAnimCtrl.play("girl_to_right");
    },
    onDestroy:function(){
        this.cleanpvalistener() ;
    }

    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
});
