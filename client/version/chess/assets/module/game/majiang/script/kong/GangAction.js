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
        atlas: {
            default: null,
            type: cc.SpriteAtlas
        },
        beimi0: {
            default: null,
            type: cc.SpriteAtlas
        },
        card_one:{
            default: null,
            type: cc.Node
        },
        card_two:{
            default: null,
            type: cc.Node
        },
        card_three:{
            default: null,
            type: cc.Node
        },
        card_four:{
            default: null,
            type: cc.Node
        },
        card_last:{
            default: null,
            type: cc.Node
        },
        target:{
            default: null,
            type: cc.Node
        },
    },

    // use this for initialization
    onLoad: function () {

    },
    init:function(cvalue , gang){
        this.value = cvalue ;

        let cardcolors = parseInt(this.value/4 ) ;
        let cardtype  = parseInt(cardcolors / 9);

        this.mjtype = cardtype ;
        this.mjvalue = parseInt((this.value%36)/4 ) ;

        let deskcard , cardframe ;
        if(cardcolors < 0){
            deskcard = "wind"+(cardcolors + 8) ; //东南西北风 ， 中发白
        }else{
            if(cardtype == 0){ //万
                deskcard = "wan"+ (parseInt((this.value%36)/4)+1) ;
            }else if(cardtype == 1){ //筒
                deskcard = "tong"+ (parseInt((this.value%36)/4)+1) ;
            }else if(cardtype == 2){  //条
                deskcard = "suo"+ (parseInt((this.value%36)/4)+1) ;
            }
        }
        if(deskcard == "suo2"){
            cardframe = this.beimi0.getSpriteFrame('牌面-'+deskcard);
        }else{
            cardframe = this.atlas.getSpriteFrame('牌面-'+deskcard);
        }

        this.card_one.getComponent(cc.Sprite).spriteFrame = cardframe;
        if(this.card_two){
            this.card_two.getComponent(cc.Sprite).spriteFrame = cardframe;
        }
        if(this.card_three){
            this.card_three.getComponent(cc.Sprite).spriteFrame = cardframe;
        }
        if(this.card_four){
            this.card_four.getComponent(cc.Sprite).spriteFrame = cardframe;
        }
        if(this.card_last){
            if(gang == false){
                this.card_last.active = false ;
            }else{
                this.card_last.active = true ;
            }
        }
    }

    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
});
