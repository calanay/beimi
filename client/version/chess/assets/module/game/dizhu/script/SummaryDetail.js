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
                /**
                 * 恢复桌面
                 */
                self.context.restart("begin");
            }
            event.stopPropagation();
        });
        /**
         * SummaryClick发射的事件，方便统一处理 / 明牌开始
         */
        this.workitem.on("opendeal",function(event){
            if(self.context !=null){
                self.context.summarypage.destroy();
                /**
                 * 恢复桌面，然后发送消息给服务端
                 */
                self.context.restart("opendeal");
            }
            event.stopPropagation();
        });
        /**
         * SummaryClick发射的事件，方便统一处理 / 开始
         */
        this.workitem.on("close",function(event){
            if(self.context !=null){
                /**
                 * 显示操作按钮
                 */
                self.context.onCloseClick();
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
            if(player.userid == cc.beimi.user.id){//
                this.process(player , null  , this.myscore, this.myflag) ;
            }else{
                if(index == 0){
                    this.process(player ,this.player_1_name  , this.player_1_score , this.player_1_flag ) ;
                }else if(index == 1){
                    this.process(player ,this.player_2_name  , this.player_2_score , this.player_2_flag ) ;
                }
                index = index + 1 ;
            }
        }
    },
    /**
     * 显示已经处理完毕的结算信息
     * @param player
     * @param username
     * @param score
     * @param flag
     */
    process:function(player , username , score , flag){
        if(username != null){
            username.string = player.username ;
        }
        if(player.win == true){
            score.string = player.score ;
        }else{
            score.string = "-"+player.score ;
        }

        if(player.dizhu == true){
            flag.active = true ;
        }else{
            flag.active = false ;
        }
    }

    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
});
