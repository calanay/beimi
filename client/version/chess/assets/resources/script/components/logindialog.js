var beiMiCommon = require("BeiMiCommon") ;
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
        // .
    },

    // use this for initialization
    onLoad: function () {
        this.node.on(cc.Node.EventType.TOUCH_START, function(e){
            e.stopPropagation();
        });
    },
    onCloseClick:function(){
        /**
         * *  对象池返回， 释放资源 ，  同时 解除 事件绑定
         *
         * */
        let common = this.getCommon("common");
        if(common!=null){
        	common.loginFormPool.put(common.dialog) ;
        }
    }
});
