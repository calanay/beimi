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
        text: {
            default: null,
            type: cc.Label
        }
    },

    // use this for initialization
    onLoad: function () {

    },
    init:function(text , time , target){
        let self = this ;
        this.remaining = time ;
        this.text.string = text +"（"+ this.remaining +"）" ;
        this.schedule(function() {
            this.remaining = this.remaining - 1 ;
            if(this.remaining < 0){
                self.unschedule(this);
            }else{
                self.text.string = text +"（"+ this.remaining +"）" ;
            }
        }, 1 , time);
    },
    stop:function(target){
        this.remaining = 0 ;
        target.destroy();
    }

    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
});
