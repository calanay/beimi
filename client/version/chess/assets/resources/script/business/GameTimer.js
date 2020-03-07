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
     * @param self              调用的源
     * @param timernode         计时器所在的节点
     * @param atlas             计时器图集
     * @param timer_first       计时器首个计时字母
     * @param timer_sec         计时器第二个数字
     * @param times             计时器执行次数
     */
    runtimer:function(source , timernode  , atlas, timer_first , timer_sec , times){

        let self = this ;
        this.remaining = times ;
        timer_first.string = times ;
        if(timernode){
            timernode.active = true ;
        }

        this.timersrc = function() {
            self.remaining = self.remaining - 1 ;
            if(self.remaining < 0){
                source.unschedule(this);
                timernode.active = false ;
            }else{
                timer_first.string = self.remaining  ;
            }
        } ;
        source.schedule(this.timersrc, 1 , times , 0);

        return this.timersrc ;
    },
    stoptimer:function(source , timernode , timer){
        if(timernode){
            timernode.active = false ;
        }
        let self = this ;
        this.remaining = 0;
        if(timer){
            source.unscheduleAllCallbacks();
        }
    }

    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
});
