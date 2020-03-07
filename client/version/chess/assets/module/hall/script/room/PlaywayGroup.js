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
        grouptitle:{
            default:null ,
            type : cc.Label
        },
        groupbox:{
            default:null ,
            type : cc.Node
        },
        groupbox_four:{
            default:null ,
            type : cc.Node
        },
        content:{
            default:null ,
            type : cc.Node
        },
        itemname:{
            default:null ,
            type : cc.Label
        },
        checkbox:{
            default:null ,
            type : cc.Node
        },
        checkboxnode:{
            default:null ,
            type : cc.Node
        }
    },

    // use this for initialization
    onLoad: function () {

        let self = this ;
        this.node.on('checkbox', function (event) {
            if(self.checkbox!=null){
                if(self.checked == false){
                    if(self.data.type == "radio"){
                        for(var inx = 0 ; inx < self.options.length ; inx++){
                            let script = self.options[inx] ;
                            script.doUnChecked() ;
                        }
                    }
                    self.doChecked();
                }else{
                    if(self.data.type == "radio"){
                        for(var inx = 0 ; inx < self.options.length ; inx++){
                            let script = self.options[inx] ;
                            script.doUnChecked() ;
                        }
                        self.doChecked();
                    }else{
                        self.doUnChecked();
                    }
                }
            }
            event.stopPropagation() ;
        });
    },
    init:function(group , itempre , items , parentoptions){
        this.data = group ;
        this.options = parentoptions ;

        this.groupoptions = new Array();
        this.checked = false ;


        this.grouptitle.string = group.name ;
        if(this.groupbox!=null && itempre!=null){
            let itemsnum = 0 ;
            for(var inx=0 ; inx<items.length ; inx++){
                if(items[inx].groupid == group.id){
                    itemsnum = itemsnum + 1;
                    let newitem = cc.instantiate(itempre) ;
                    if(group.style != null && group.style == "three"){
                        newitem.parent = this.groupbox ;
                        this.groupbox_four.active = false ;
                        this.groupbox.active = true ;
                    }else{
                        newitem.parent = this.groupbox_four ;
                        this.groupbox_four.active = true;
                        this.groupbox.active = false;
                    }
                    let script = newitem.getComponent("PlaywayGroup") ;
                    this.groupoptions.push(script);
                    script.inititem(items[inx] , group , this.groupoptions);

                }
            }
            if(group.style != null && group.style == "three") {
                if (itemsnum > 4) {
                    this.content.height = 35 + 50 * (parseInt((itemsnum - 1) / 3) + 1);
                    this.groupbox.height = 50 * (parseInt((itemsnum - 1) / 3) + 1);
                }
            }else{
                if (itemsnum > 4) {
                    this.content.height = 35 + 50 * (parseInt((itemsnum - 1)/ 4) + 1);
                    this.groupbox_four.height = 50 * (parseInt((itemsnum - 1)/ 4) + 1);
                }
            }
        }
    },
    inititem:function(item , group , parentoptions){
        this.data = group ;
        this.item = item ;
        this.options = parentoptions;
        this.itemname.string = item.name ;
        /**
         * 以下代码修正 OPTION超出宽度导致 点击错误的 问题
         */
        if(group.style == "three"){
            this.itemname.node.width = 160 ;
            this.itemname.node.x = 107 ;
        }else{
            this.itemname.node.width = 105 ;
            this.itemname.node.x = 77 ;
        }
        if(item.defaultvalue == true){
            this.doChecked();
        }else{
            this.doUnChecked();
        }
        if(group!=null && group.style!=null && group.style == "three"){
            this.checkboxnode.x = -76 ;
        }
    },
    doChecked:function(){
        this.checked = true ;
        this.checkbox.active = true ;
    },
    doUnChecked:function(){
        this.checked = false ;
        this.checkbox.active = false;
    }

    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
});
