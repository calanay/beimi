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
        gamebtn: {
            default: null,
            type: cc.Node
        },
        continuegamebtn:{
            default: null,
            type: cc.Node
        },
        poker: {
            default: null,
            type: cc.Node
        },
        lastCardsPanel: {   //底牌
            default: null,
            type: cc.Node
        },
        waitting: {
            default: null,
            type: cc.Prefab
        },
        ratio:{   //底牌
            default: null,
            type: cc.Label
        },
        summary_win:{
            default:null ,
            type : cc.Prefab
        },
        summary:{
            default:null ,
            type : cc.Prefab
        },
        inviteplayer:{
            default:null ,
            type : cc.Prefab
        }
    },

    // use this for initialization
    onLoad: function () {
        /**
         * 适配屏幕尺寸
         */
        this.resize();

        this.player = new Array() ;     //存放玩家数据
        this.pokercards = new Array();
        this.lastcards = new Array();
        this.lastCardsPanel.active = false ;
        this.summarypage = null ;
        this.inited = false ;
        this.lasttip = null ;
        if(cc.beimi!=null){
            if(cc.beimi.gamestatus!=null && cc.beimi.gamestatus == "playing"){
                //恢复数据
                this.recovery() ;
            }else if(cc.beimi.extparams!=null && cc.beimi.extparams.gamemodel == "room"){
                /**
                 * 房卡模式，开始启动游戏，当前玩家进入等待游戏的状态，显示邀请好友游戏，并分配 6位数字的房间号码
                 */
                /**
                 * 处理完毕，清理掉全局变量
                 * @type {null}
                 */
                this.invite = cc.instantiate(this.inviteplayer) ;
            }
            this.initgame();
        }
    },
    begin:function(){
        if(cc.beimi.data!=null && cc.beimi.data.enableai == true){
            this.statictimer("正在匹配玩家" , cc.beimi.data.waittime) ;
        }else{
            this.statictimer("正在匹配玩家，请稍候" , cc.beimi.data.noaiwaitime) ;
        }
        this.startgame("false") ;
    },
    opendeal:function(){
        if(cc.beimi.data!=null && cc.beimi.data.enableai == true){
            this.statictimer("正在匹配玩家" , cc.beimi.data.waittime) ;
        }else{
            this.statictimer("正在匹配玩家，请稍候" , cc.beimi.data.noaiwaitime) ;
        }
        this.startgame("true") ;
    },
    recovery:function(){
        this.statictimer("正在恢复数据，请稍候" , cc.beimi.data.waittime) ;
    },
    initgame:function(){
        let self = this ;
        this.gamebtn.active = true ;
        this.continuegamebtn.active = false ;
        if(this.ready()) {
            let socket = this.socket();



            this.game = this.getCommon("DizhuDataBind");

            this.map("joinroom" , this.joinroom_event) ;          //加入房价
            this.map("players" , this.players_event) ;            //接受玩家列表
            this.map("catch" , this.catch_event) ;                  //叫地主
            this.map("catchresult" , this.catchresult_event) ;      //最终抢到地主的玩家
            this.map("lasthands" , this.lasthands_event) ;            //翻底牌
            this.map("takecards" , this.takecards_event) ;            //出牌信息
            this.map("ratio" , this.ratio_event) ;                      //有炸
            this.map("play" , this.play_event) ;                      //接受玩家列表
            this.map("allcards" , this.allcards_event) ;              //我出的牌
            this.map("cardtips" , this.cardtips_event) ;              //提示
            this.map("roomready" , this.roomready_event) ;              //提示

            this.map("playeready" , this.playeready_event) ;            //玩家点击了开始游戏 ， 即准备就绪

            this.map("cardtips" , this.cardtips_event) ;              //提示

            this.map("recovery" , this.recovery_event) ;              //恢复牌局数据

            socket.on("command" , function(result){
                cc.beimi.gamestatus = "playing" ;
                if(self.inited == true){
                    var data = self.parse(result) ;
                    self.route(data.command)(data , self);
                }
            });
            /**
             * 心跳检查，服务端发起的事件，服务端可以设置 PING的时长和 PING的 TimeOut
             */
            socket.on("ping" , function(){

            });

            /**
             *   此处有个问题：游戏中，如果点击了返回，进入过其他游戏，这个 cc.beimi.extparams.playway
             *   就发生了改变，会导致当前游戏结束后，继续游戏的时候会出现问题
             * @type {{token: (*|null), playway, orgi, extparams: *}}
             */
            var param = {
                token:cc.beimi.authorization,
                playway:cc.beimi.extparams.playway,
                orgi:cc.beimi.user.orgi,
                extparams:cc.beimi.extparams
            } ;
            socket.emit("joinroom" ,JSON.stringify(param)) ;

            this.inited = true ;
        }

    },
    /**
     * 新创建牌局，首个玩家加入，进入等待状态，等待其他玩家加入，服务端会推送 players数据
     * @param data
     * @param context
     */
    joinroom_event:function(data , context){

        if(data.cardroom == true && context.inviteplayer!=null){
            let script = context.invite.getComponent("BeiMiQR")
            script.init(data.roomid);
            context.invite.parent = context.root() ;
        }


        //显示 匹配中，并计时间，超过设定的倒计时时间即AI加入，根据当前的 玩家数量 匹配机器人
        if(data.player.id && data.player.id == cc.beimi.user.id){
            //本人，开始计时
            //console.log("本人") ;
            //self.player[0] = data ;
            context.index = data.index ;   //当前玩家所在位置
        }else{
            //其他玩家加入，初始化
            var inroom = false ;
            for(var i = 0 ; i < context.player.length ; i++){
                var player = context.player[i].getComponent("PlayerRender") ;
                if(player.userid == data.player.id){
                    inroom = true ;
                }
            }
            if(inroom == false){
                context.newplayer(context.player.length , context , data.player , context.index + 1 == data.index) ;
            }
        }
    },
    /**
     * 房卡模式下，邀请的好友人到齐了
     * @param data
     * @param context
     */
    roomready_event:function(data , context){
        if(data.cardroom == true && context.invite!=null){
            context.invite.destroy();
        }
    },
    /**
     *
     * @param data
     * @param context
     */
    playeready_event:function(data , context){
        if(data.userid == cc.beimi.user.id){
            context.gamebtn.active = false ;
        }
    },
    /**
     * 接收到服务端的 推送的 玩家数据，根据玩家数据 恢复牌局 , 玩家座位排列方式 ， 和麻将统一， 都是东南西北方式 排列
     * 0是首位玩家
     * 1是右侧玩家
     * 2是左侧玩家
     * 麻将还有一个对家
     * @param data
     * @param context
     */
    players_event:function(data,context){
        var inx = -1 ;
        for(var i = 0 ; i<data.player.length ; i++){
            if(data.player[i].id == cc.beimi.user.id){
                inx = i ; break ;
            }
        }
        if(data.player.length > 1 && inx >=0){
            var pos = inx+1 ;
            while(true){
                if(pos == data.player.length){pos = 0 ;}
                if(context.playerexist(data.player[pos], context) == false){
                    context.newplayer(context.player.length , context , data.player[pos] , context.player.length == 0 && !(pos == 0 && data.player.length < data.maxplayers) ) ;
                }
                if(pos == inx){break ;}
                pos = pos + 1;
            }
        }
    },
    playerexist:function(player,context){
        var inroom = false ;
        if(player.id == cc.beimi.user.id){
            inroom = true ;
        }else{
            for(var j = 0 ; j < context.player.length ; j++){
                if(context.player[j].id == player.id){
                    inroom = true ; break ;
                }
            }
        }
        return inroom ;
    },
    /**
     * 接收到服务端的 推送的 玩家数据，根据玩家数据 恢复牌局
     * @param data
     * @param context
     */
    catch_event:function(data,context){
        /**
         * 修改倍率
         */
        if(context.ratio){
            context.ratio.string = data.ratio+"倍" ;
        }
        if(data.userid == cc.beimi.user.id){    //该我抢
            context.game.catchtimer(15);
        }else{                              //该别人抢
            for(var inx =0 ; inx<context.player.length ; inx++){
                var render = context.player[inx].getComponent("PlayerRender") ;
                if(render.userid && render.userid == data.userid){
                    render.catchtimer(15);
                    break ;
                }
            }
        }
    },
    /**
     * 接收到服务端的 恢复牌局的数据 恢复牌局
     * @param data
     * @param context
     */
    recovery_event:function(data,context){
        var mycards = context.decode(data.player.cards);
        if(context.waittimer != null){
            let timer = context.waittimer.getComponent("BeiMiTimer");
            if(timer){
                timer.stop(context.waittimer) ;
            }
        }

        /**
         * 清理掉 extparam;
         * @type {boolean}
         */

        context.gamebtn.active = false ;

        if(context.ratio){
            context.ratio.string = data.ratio+"倍" ;
        }

        context.doLastCards(context.game , context , 3 , 0);
        for(var inx =0 ; inx  < mycards.length ; inx++){
            let pokencard = context.playcards(context.game , context, inx * 50-300 , mycards[inx]);
            context.registerProxy(pokencard);
        }
        /**
         * 赋值，解码牌面
         */
        for(var i=0 ; i<context.pokercards.length ; i++){
            var pokencard = context.pokercards[i];
            pokencard.getComponent("BeiMiCard").order();
        }
        context.lastCardsPanel.active = true ;

        if(data.lasthands){
            var lasthands = context.decode(data.lasthands);
            /**
             * 底牌 ， 顶部的 三张底牌显示区域
             */
            for(var i=0 ; i<context.lastcards.length ; i++){
                var last = context.lastcards[i].getComponent("BeiMiCard") ;
                last.setCard(lasthands[i]);
                last.order();
            }
            /**
             * 设置地主标志
             */
            if(data.banker.userid == cc.beimi.user.id){
                context.game.lasthands(context , context.game , data.data ) ;
            }else{
                context.getPlayer(data.banker.userid).setDizhuFlag(data.data);
            }
        }
        /**
         * 恢复最后出的牌
         */
        if(data.last != null){
            let lastcards = context.decode(data.last.cards);        //解析牌型
            if (data.last.userid == cc.beimi.user.id) {
                context.game.lasttakecards(context.game, context, data.last.cardsnum, lastcards , data.last);
            } else {
                context.getPlayer(data.last.userid).lasttakecards(context.game, context, data.last.cardsnum, lastcards , data.last);
            }

            if (data.nextplayer  == cc.beimi.user.id) {
                context.game.playtimer(context.game, 25 , data.automic);
            } else {
                context.getPlayer(data.nextplayer).playtimer(context.game, 25);
            }
        }
        if(data.cardsnum!=null && data.cardsnum.length > 0){
            for(var i =0 ; i<data.cardsnum.length ; i++){
                context.getPlayer(data.cardsnum[i].userid).resetcards(data.cardsnum[i].cardsnum);
            }
        }
    },
    /**
     * 有玩家出炸
     * @param data
     * @param context
     */
    ratio_event:function(data,context){
        /**
         * 修改倍率
         */

        if(data.king == true){
            //王炸，播放音效
        }else if(data.bomb == true){
            //普通炸弹，播放音效
        }
        if(context.ratio){
            context.ratio.string = data.ratio+"倍" ;
        }
    },
    /**
     * 接收到服务端的 推送的 玩家数据，根据玩家数据 恢复牌局
     * @param data
     * @param context
     */
    catchresult_event:function(data,context){
        /**
         * 修改倍率
         */
        if(context.ratio){
            context.ratio.string = data.ratio+"倍" ;
        }
        if(data.userid == cc.beimi.user.id){    //该我抢
            context.game.catchresult(data);
        }else{                              //该别人抢
            setTimeout(function(){
                context.getPlayer(data.userid).catchresult(data);
            },1500) ;
        }
    },
    /**
     * 接收到服务端的 推送的 玩家数据，根据玩家数据 恢复牌局
     * @param data
     * @param context
     */
    lasthands_event:function(data,context){
        var lasthands = context.decode(data.lasthands);
        /**
         * 底牌 ， 顶部的 三张底牌显示区域
         */
        for(var i=0 ; i<context.lastcards.length ; i++){
            var last = context.lastcards[i].getComponent("BeiMiCard") ;
            last.setCard(lasthands[i]);
            last.order();
        }
        /**
         * 当前玩家的 底牌处理
         */
        if(data.userid == cc.beimi.user.id) {
            context.game.lasthands(context , context.game , data ) ;
            /**
             * 隐藏 其他玩家的 抢地主/不抢地主的 提示信息
             */
            for(var inx =0 ; inx<context.player.length ; inx++){
                var render = context.player[inx].getComponent("PlayerRender") ;
                render.hideresult();
            }

            for(var i=0 ; i<lasthands.length ; i++){
                let func = null ;
                if(i == (lasthands.length - 1)){
                    func = cc.callFunc(function (target , data) {
                        if(data.tempcontext){
                            data.tempcontext.layout(data.tempcontext.poker , function(fir , sec){
                                return fir.zIndex - sec.zIndex ;
                            });
                        }
                    }, this , {tempcontext: context});
                }
                let pc = context.current(context.game , context ,2 * 300 + (6 + i) * 50-300, lasthands[i] , func) ;
                var beiMiCard = pc.getComponent("BeiMiCard") ;
                beiMiCard.order();
                context.registerProxy(pc);
            }

            context.game.playtimer(context.game,25 , true);
        }else{
            context.game.hideresult();
            for(var inx =0 ; inx<context.player.length ; inx++){
                var render = context.player[inx].getComponent("PlayerRender") ;
                render.hideresult();
            }
            context.getPlayer(data.userid).lasthands(context,context.game,data);
            context.getPlayer(data.userid).playtimer(context.game , 25);
        }
        for(var inx =0 ; inx<context.pokercards.length ; inx++){
            var pc = context.pokercards[inx] ;
            pc.zIndex = 54 - pc.card ;
        }
    },
    /**
     * 接收到服务端的 推送的 玩家数据，根据玩家数据 恢复牌局
     * @param data
     * @param context
     */
    takecards_event:function(data,context){
        context.lasttip = null ;
        if(data.allow == true) {
            var lastcards ;
            if(data.donot == false){
                lastcards = context.decode(data.cards);        //解析牌型
            }
            if (data.userid == cc.beimi.user.id) {
                context.game.unselected(context , context.game) ;
                context.game.lasttakecards(context.game, context, data.cardsnum, lastcards , data);
            } else {
                context.getPlayer(data.userid).lasttakecards(context.game, context, data.cardsnum, lastcards , data);
            }

            context.game.selectedcards.splice(0 ,context.game.selectedcards.length );//清空
            if(data.over == false){
                if (data.nextplayer == cc.beimi.user.id) {
                    context.game.playtimer(context.game, 25 , data.automic);
                } else {
                    context.getPlayer(data.nextplayer).playtimer(context.game, 25);
                }
            }
        }else{//出牌不符合规则，需要进行提示
            context.game.notallow.active = true ;
            setTimeout(function(){
                context.game.notallow.active = false ;
            } , 2000);
            context.game.unselected(context , context.game);
        }
    },
    /**
     * 接收到服务端的 出牌提示信息
     * @param data
     * @param context
     */
    cardtips_event:function(data,context){
        context.game.unselected(context , context.game) ;
        if(data.allow == true) {
            var tipcards = context.decode(data.cards);        //解析牌型
            context.lasttip = tipcards.join(",") ;
            for(var inx = 0 ; inx < tipcards.length ; inx++){
                context.game.cardtips(context , tipcards[inx] , tipcards) ;
            }
        }else{//出牌不符合规则，需要进行提示
            context.game.cardtipsfornot(context , context.game);
        }
    },
    /**
     * 接收到服务端的 推送的 玩家数据，根据玩家数据 恢复牌局
     * @param data
     * @param context
     */
    play_event:function(data,context){
        /**
         * 增加了全局变量，gamestatus , 用于控制当前玩家退出后恢复数据
         * @type {string}
         */
        cc.beimi.gamestatus = "playing" ;
        var mycards = context.decode(data.player.cards);
        if(context.waittimer){
            let timer = context.waittimer.getComponent("BeiMiTimer");
            if(timer){
                timer.stop(context.waittimer) ;
            }
        }

        let center = context.game.pokerpool.get();
        let left = context.game.pokerpool.get(),right = context.game.pokerpool.get();
        center.parent = context.root() ;
        left.parent = context.root() ;
        right.parent = context.root() ;
        center.setPosition(0,200);
        left.setPosition(0,200);
        right.setPosition(0,200);

        let finished = cc.callFunc(function (target , data) {
            if(data.game){
                data.game.pokerpool.put(data.current) ;
                data.game.pokerpool.put(data.left);
                data.game.pokerpool.put(data.right);

                /**
                 * 赋值，解码牌面
                 */
                for(var i=0 ; i<data.self.pokercards.length ; i++){
                    var pokencard = data.self.pokercards[i];
                    pokencard.getComponent("BeiMiCard").order();
                }

                data.self.lastCardsPanel.active = true ;
            }
        }, this , {game : context.game  , self: context, left :left , right : right , current : center});

        context.doLastCards(context.game , context , 3 , 0);

        /**
         *  发牌动作，每次6张，本人保留总计17张，其他人发完即回收
         */
        setTimeout(function() {
            context.dealing(context.game , 6 , context , 0 , left , right , mycards) ;
            setTimeout(function(){
                context.dealing(context.game , 6 , context , 1, left , right , mycards) ;
                setTimeout(function(){
                    context.dealing(context.game , 5 , context , 2, left , right , mycards , finished) ;
                    context.reordering(context);
                },500) ;
            },500) ;
        }, 0);
    },
    /**
     * 打完牌，进入结算界面，结算界面流程：
     * 1、提示你赢/输了
     * 2、1秒后所有玩家的牌翻出来，显示剩余的牌
     * 3、2秒后显示结算界面
     * 4、玩家点选明牌开始还是继续游戏
     * @param data
     * @param context
     */
    allcards_event:function(data , context){
        /**
         * 全局变量控制，用于恢复数据
         * @type {string}
         */
        cc.beimi.gamestatus = "notready" ;
        //结算界面，
        let player ;
        for(var i=0 ; i<data.players.length ; i++){
            var temp = data.players[i] ;
            if(temp.userid != cc.beimi.user.id){
                var cards = context.decode(temp.cards);        //解析牌型
                var tempscript = context.getPlayer(temp.userid) ;
                for(var inx = 0 ; inx < cards.length ; inx++) {
                    //tempscript.lasttakecards(context.game, context, cards.length, cards, data);
                    /**
                     * 最后牌局结束以后，显示所有玩家的手牌
                     */
                }
            }else{
                player = temp  ;
            }
        }
        if(player!=null) {
            /**
             * 更新个人账号资产信息
             */
            context.pva("gold" , player.balance);
            /**
             * 刷新个人资产显示 , 可以增加相关动画显示，duang的一声，显示的金币变成金光闪闪的，然后再变回去
             */
            context.updatepva() ;
        }
        setTimeout(function(){
            if(player!=null){
                if(player.win == true){
                    context.summarypage = cc.instantiate(context.summary_win) ;
                }else{
                    context.summarypage = cc.instantiate(context.summary) ;
                }
                context.summarypage.parent = context.root() ;
                let temp = context.summarypage.getComponent("SummaryDetail") ;
                temp.create(context , data);
            }
            /**
             * 隐藏顶部的 底牌 显示区域
             * @type {boolean}
             */
            context.lastCardsPanel.active = false ;

            if(data.gameRoomOver == true){//房间解散
                for(var inx = 0 ; inx<context.player.length ; inx++){
                    context.player[inx].destroy();
                }
                context.player.splice(0 , context.player.length) ;//房间解散，释放资源
                context.player = new Array();
                context.clean();
            }
        } , 2000);

    },
    getPlayer:function(userid){
        var tempRender;
        for(var inx =0 ; inx<this.player.length ; inx++){
            var render = this.player[inx].getComponent("PlayerRender") ;
            if(render.userid && render.userid == userid){
                tempRender = render ; break ;
            }
        }
        return tempRender ;
    },
    dealing:function(game , num , self , times , left , right , cards , finished){
        /**
         * 处理当前玩家的 牌， 发牌 ，  17张牌， 分三次动作处理完成
         */
        for(var i=0 ; i<num ; i++){
            var myCards = self.current(game , self ,times * 300 + i * 50-300, cards[times * 6 + i] , finished) ;
            this.registerProxy(myCards);
        }
        self.otherplayer(left  , 0 , num ,game , self) ;
        self.otherplayer(right , 1 , num ,game , self) ;

    },
    otherplayer:function(currpoker , inx, num ,game , self){
        if(inx == 0){
            let seq = cc.sequence(
                cc.spawn(cc.moveTo(0.2, -350, 50) , cc.scaleTo(0.2, 0.3, 0.3)) , cc.moveTo(0 , 0 , 200) , cc.scaleTo(0, 1, 1)
            );
            currpoker.runAction(seq);
        }else{
            let seq = cc.sequence(
                cc.spawn(cc.moveTo(0.2, 350, 50) , cc.scaleTo(0.2, 0.3, 0.3)) , cc.moveTo(0 , 0 , 200) , cc.scaleTo(0, 1, 1)
            );
            currpoker.runAction(seq);
        }
        //currpoker.setScale(1);
        var render = self.player[inx].getComponent("PlayerRender") ;
        for(var i=0 ; i<num ; i++){
            render.countcards(1);
        }
    },
    doLastCards:function(game , self , num , card){//发三张底牌
        for(var i=0 ; i<num ; i++){
            var width = i * 80 - 80;
            let currpoker = game.minpokerpool.get() ;
            currpoker.getComponent("BeiMiCard").setCard(card) ;
            currpoker.card = card ;
            currpoker.parent = this.lastCardsPanel;
            currpoker.setPosition(width , 0);

            self.lastcards[self.lastcards.length] = currpoker ;
        }
    },
    registerProxy:function(myCard){
        if(myCard){
            var beiMiCard = myCard.getComponent("BeiMiCard") ;
            beiMiCard.proxy(this.game);
        }
    },
    playcards:function(game , self , posx , card){
        return self.current(game ,self , posx , card , null);
    },
    current:function(game , self , posx , card , func){
        let currpoker = game.pokerpool.get() ;
        var beiMiCard = currpoker.getComponent("BeiMiCard") ;
        beiMiCard.setCard(card) ;
        currpoker.card = card ;
        currpoker.parent = self.poker ;
        currpoker.setPosition(0,200);

        currpoker.setScale(1,1);
        currpoker.zIndex = 100 - card ;

        self.pokercards.push(currpoker);

        if(func!=null){
            let seq = cc.sequence(cc.moveTo(0.2, posx, -180) , func);
            currpoker.runAction(seq);
        }else{
            let action = cc.moveTo(0.2, posx, -180) ;
            currpoker.runAction(action);
        }
        return currpoker;
    },
    reordering:function(self){
        for(var i=0 ; i<self.pokercards.length ; i++){
            self.pokercards[i].parent = self.poker ;
        }
    },
    newplayer:function(inx , self , data , isRight){
        var pos = cc.v2(520, 100) ;
        if(isRight == false){
            pos = cc.v2(-520,100) ;
        }
        let game = self.getCommon("DizhuDataBind");
        if(game && game.playerspool.size() > 0){
            self.player[inx] = game.playerspool.get() ;
            self.player[inx].parent = self.root() ;
            self.player[inx].setPosition(pos);
            var render = self.player[inx].getComponent("PlayerRender") ;
            render.initplayer(data , isRight);
        }
    },
    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
    /**
     * 不抢/叫地主
     */
    givup:function(){
        if(this.ready()){
            let socket = this.socket();
            socket.emit("giveup","giveup");
        }
    },
    /**
     * 开始游戏
     */
    startgame:function(opendeal){
        if(this.ready()){
            let socket = this.socket();
            socket.emit("start",opendeal);
        }
    },
    /**
     * 不抢/叫地主
     */
    cardtips:function(){
        if(this.ready()){
            let socket = this.socket();

            if(this.lasttip!=null){
                socket.emit("cardtips",this.lasttip);
            }else{
                socket.emit("cardtips","");
            }
            this.lasttip = null ;
        }
    },
    /**
     * 抢/叫地主触发事件
     */
    docatch:function(){
        if(this.ready()){
            let socket = this.socket();
            socket.emit("docatch","docatch");
        }
    },
    /**
     * 出牌
     */
    doPlayCards:function(){
        if(this.ready()){
            let socket = this.socket();
            this.game.selectedcards.splice(0 , this.game.selectedcards.length) ;
            for(var i=0 ; i<this.pokercards.length ; i++){
                var card = this.pokercards[i] ;
                var temp = card.getComponent("BeiMiCard");
                if(temp.selected == true){
                    this.game.selectedcards.push(temp.card) ;
                }
            }
            socket.emit("doplaycards" , this.game.selectedcards.join());
        }
        this.lasttip = null ;
    },
    /**
     * 不出牌
     */
    noCards:function(){
        if(this.ready()){
            let socket = this.socket();
            socket.emit("nocards","nocards");
        }
        this.lasttip = null ;
    },
    clean:function(){
        for(var inx = 0 ; inx<this.pokercards.length ; inx++){
            let pc = this.pokercards[inx] ;
            this.game.pokerpool.put(pc) ;//回收回去
        }
        this.pokercards.splice(0 , this.pokercards.length ) ;
        for(var i=0 ; i<this.lastcards.length ; i++){
            this.game.minpokerpool.put(this.lastcards[i]);
        }

        this.lastcards.splice( 0 , this.lastcards.length) ;

        for(var i = 0 ; i < this.player.length ; i++){
            var player = this.player[i].getComponent("PlayerRender") ;
            player.clean(this.game);
        }
        this.player.splice(0 , this.player.length) ;

        this.game.clean(this);
        this.ratio.string = "15倍" ;
    },
    onCloseClick:function(){
        this.continuegamebtn.active = true ;
    },
    restart:function(command){
        this.game.restart();
        this.statictimer("正在匹配玩家" , 5) ;
        /**
         * 系统资源回收完毕，发送一个 重新开启游戏的 通知
         */
        if(this.ready()){
            let socket = this.socket();
            socket.emit("restart" , command);
        }
    },
    continuegame:function(){
        this.continuegamebtn.active = false ;
        this.restart("begin");
    },
    statictimer:function(message , time){
        this.waittimer = cc.instantiate(this.waitting);
        this.waittimer.parent = this.root();

        let timer = this.waittimer.getComponent("BeiMiTimer");
        if(timer){
            timer.init(message , time , this.waittimer);
        }
    },
    onDestroy:function(){
        this.inited = false ;
        this.cleanmap();
        if(this.ready()){
            let socket = this.socket();
            socket.emit("leave","leave");
        }
    }
});
