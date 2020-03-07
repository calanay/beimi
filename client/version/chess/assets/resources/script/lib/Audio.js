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
        bgVolume:1.0,           // 背景音量

        deskVolume:1.0,         //   房间 房间音量
        
        bgAudioID:-1            //   背景 音乐  id
    },

    // use this for initialization
    init: function () {
        var t = cc.sys.localStorage.getItem("bgVolume");
        if(t != null){
            this.bgVolume = parseFloat(t);    
        }
        
        var t = cc.sys.localStorage.getItem("deskVolume");

        if(t != null){
            this. deskVolume = parseFloat(t);    
        }
        
        cc.game.on(cc.game.EVENT_HIDE, function () {
            cc.audioEngine.pauseAll();
        });
        cc.game.on(cc.game.EVENT_SHOW, function () {
            cc.audioEngine.resumeAll();
        });
    },

    // called every frame, uncomment this function to activate update callback
    // update: function (dt) {

    // },
    
    getUrl:function(url){
        return cc.url.raw("resources/sounds/" + url);
    },
    
    playBGM:function(url){
        var audioUrl = this.getUrl(url);
        if(this.bgAudioID >= 0){
            cc.audioEngine.stop(this.bgAudioID);
        }
        this.bgAudioID = cc.audioEngine.play(audioUrl,true,this.bgVolume);
    },
    
    playSFX:function(url){
        var audioUrl = this.getUrl(url);
        if(this.sfxVolume > 0){
            var audioId = cc.audioEngine.play(audioUrl,false,this.deskVolume);    
        }
    },
    
    setSFXVolume:function(v){
        if(this.sfxVolume != v){
            cc.sys.localStorage.setItem("deskVolume",v);
            this.deskVolume = v;
        }
    },
    getState:function(){
        return cc.audioEngine.getState(this.bgAudioID);
    },
    setBGMVolume:function(v,force){
        if(this.bgAudioID >= 0){
            if(v > 0 && cc.audioEngine.getState(this.bgAudioID) === cc.audioEngine.AudioState.PAUSED){
                cc.audioEngine.resume(this.bgAudioID);
            }else if(v == 0){
                cc.audioEngine.pause(this.bgAudioID);
            }
        }
        if(this.bgVolume != v || force){
            cc.sys.localStorage.setItem("bgVolume",v);
            this.bgmVolume = v;
            cc.audioEngine.setVolume(this.bgAudioID,v);
        }
    },
    
    pauseAll:function(){
        cc.audioEngine.pauseAll();
    },
    
    resumeAll:function(){
        cc.audioEngine.resumeAll();
    }
});
