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
    },

    // use this for initialization
    onLoad: function () {

    },
    /**
     * 记录牌的 特殊属性， 是否刚抓起来的牌，已经玩家位置 ， 右、上、左
     * @param spec
     * @param inx
     */
    init:function(spec , inx){
        this.spec = spec ;
        this.inx = inx ;
        if(this.spec == true){
            if(this.inx == 0 || this.inx == 2){
                this.node.height = this.node.height + 50;
            }else{
                this.node.width = this.node.width + 30 ;
            }
        }
    },
    reinit:function(){
        if(this.spec == true){
            if(this.inx == 0 || this.inx == 2){
                this.node.height = this.node.height - 50;
            }else{
                this.node.width = this.node.width - 30 ;
            }
        }
        this.spec = false ;
    }
    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
});
