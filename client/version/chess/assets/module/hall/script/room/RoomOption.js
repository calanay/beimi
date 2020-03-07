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
        atlas: {
            default: null,
            type: cc.SpriteAtlas
        },
        memo:{
            default:null ,
            type : cc.Label
        },
        optionsnode:{
            default:null ,
            type : cc.Node
        },
        roomtitle:{
            default:null ,
            type : cc.Node
        },
        optiongroup:{
            default:null ,
            type : cc.Prefab
        },
        optiongroupitem:{
            default:null ,
            type : cc.Prefab
        },
        memonode:{
            default:null ,
            type : cc.Node
        },
        createroom:{
            default:null ,
            type : cc.Node
        },
        freeopt:{
            default:null ,
            type : cc.Node
        }
    },

    // use this for initialization
    onLoad: function () {
        let self = this ;
        this.group = new Array();
        this.node.on('createroom', function (event) {
            /**
             * 把参数 汇总一下， 然后转JSON以后序列化成字符串，发送 创建房间的请求
             */
            var extparams = {} ;
            let values = new Array();
            for(var inx=0 ; inx<self.group.length ; inx++){
                let groupitem = self.group[inx] ;
                let value = "" ;
                for(var j=0 ; j<groupitem.groupoptions.length ; j++){
                    let option = groupitem.groupoptions[j] ;
                    if(option.checked == true){
                        if(value != ""){
                            value = value + "," ;
                        }
                        value = value + option.item.value ;
                    }
                }
                extparams[groupitem.data.code] = value ;
            }
            /**
             * 藏到全局变量里去，进入场景后使用，然后把这个参数置空
             * @type {{}}
             */
            extparams.gametype = self.data.code ;
            extparams.playway = self.data.id;
            extparams.gamemodel = "room" ;
            /**
             * 发送创建房间开始游戏的请求
             */
            event.stopPropagation() ;
            self.preload(extparams , self) ;
        });
    },
    init:function(playway){
        this.data = playway ;
        if(this.memo != null && playway.memo!=null && playway.memo!=""){
            this.memonode.active = true ;
            this.memo.string = playway.memo ;
        }else if(this.memonode!=null){
            this.memonode.active = false ;
        }
        if(playway.free == true){
            this.freeopt.active = true;
            this.createroom.active = false ;
        }else{
            this.freeopt.active = false;
            this.createroom.active = true ;
        }
        if(playway.roomtitle!=null && playway.roomtitle!=""){
            let frame = this.atlas.getSpriteFrame(playway.roomtitle);
            if(frame!=null){
                this.roomtitle.getComponent(cc.Sprite).spriteFrame = frame ;
            }
        }
        if(this.optiongroup!=null && playway.groups!=null){
            for(var inx = 0 ; inx < playway.groups.length ; inx++){
                let group = cc.instantiate(this.optiongroup) ;


                let playWayGroup = group.getComponent("PlaywayGroup") ;
                playWayGroup.init(playway.groups[inx] , this.optiongroupitem , playway.items) ;
                this.group.push(playWayGroup);

                group.parent = this.optionsnode ;
            }
        }
    }
    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
});
