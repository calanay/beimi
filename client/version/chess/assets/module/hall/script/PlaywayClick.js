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
        // ..
        playway:{
            default: null,
            type: cc.Node
        },
    },

    // use this for initialization
    onLoad: function () {

    },
    onClick:function(){
        let self = this ;

        var selectPlayway = this.getCommon("SelectPlayway");

        let thisplayway = this.playway.getComponent("Playway");

        let extparams = {
            gametype : thisplayway.data.code ,
            playway  : thisplayway.data.id
        } ;
        this.closeOpenWin();
        this.preload(extparams , self);
    },
    createRoom:function(event,data){
        let self = this ;
        this.loadding();
        setTimeout(function(){
            self.scene(data, self) ;
        },200);
    }

    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
});
