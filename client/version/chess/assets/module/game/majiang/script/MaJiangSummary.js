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
        workitem:{
            default: null,
            type: cc.Node
        },
        myscore:{   //底牌
            default: null,
            type: cc.Label
        },
        myflag:{
            default: null,
            type: cc.Node
        },
        player_1:{
            default: null,
            type: cc.Node
        },
        player_1_flag:{
            default: null,
            type: cc.Node
        },
        player_1_name:{
            default: null,
            type: cc.Label
        },
        player_1_score:{
            default: null,
            type: cc.Label
        },
        player_2:{
            default: null,
            type: cc.Node
        },
        player_2_flag:{
            default: null,
            type: cc.Node
        },
        player_2_name:{
            default: null,
            type: cc.Label
        },
        player_2_score:{
            default: null,
            type: cc.Label
        },
    },

    // use this for initialization
    onLoad: function () {
        let self = this ;
        /**
         * SummaryClick发射的事件，方便统一处理 / 开始
         */
        this.workitem.on("begin",function(event){
            if(self.context !=null){
                self.context.summarypage.destroy();
                self.context.restart();
            }
            event.stopPropagation();
        });
        /**
         * SummaryClick发射的事件，方便统一处理 / 开始
         */
        this.workitem.on("close",function(event){
            if(self.context !=null){
                self.context.summarypage.destroy();
            }
            event.stopPropagation();
        });
    },
    create:function(context , data){
        this.context = context ;
        var index = 0 ;
        for(var inx = 0 ; inx < data.players.length ; inx++){
            var player = data.players[inx] ;

        }
    }
});
