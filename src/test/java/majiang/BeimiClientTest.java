package majiang;

import java.net.URISyntaxException;
import java.util.Scanner;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.beimi.core.BMDataContext;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.parser.Packet;

public class BeimiClientTest {
	
	public static void main(String[] args) throws URISyntaxException {
		IO.Options options = new IO.Options();
        options.transports = new String[]{"websocket"};
        options.reconnectionAttempts = 2;
        options.reconnectionDelay = 1000;//失败重连的时间间隔
        options.timeout = 500;//连接超时时间(ms)
 
//        final Socket socket = IO.socket("http://localhost:9081/bm/game?token=123456", options);//错误的token值连接示例
        final Socket socket = IO.socket("http://127.0.0.1:9081" 
        							+ BMDataContext.NameSpaceEnum.GAME.getNamespace(), options);
        
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            	System.out.println("beimi client connect!");
            	
            	JSONObject jsonObject = new JSONObject();
            	jsonObject.put("token", "4187add4ad6a45f2a0276c8ce32e50cf");
            	jsonObject.put("orgi", "beimi");
            	
            	socket.emit("gamestatus", jsonObject.toString());
            }
        });
 
        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("连接关闭");
            }
        });
 
        socket.on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("sessionId:" + socket.id());
                for (Object obj : args) {
                    System.out.println(obj);
                }
                System.out.println("收到服务器应答，将要断开连接...");
                socket.disconnect();
            }
        });
        
        socket.on(BMDataContext.BEIMI_GAMESTATUS_EVENT, new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				if (args.length > 0) {
            		if (args[0] instanceof Packet) {
            			Packet packet = (Packet)args[0];
            			System.out.println("[gamestatus]packet type data="+ packet.data);
					}else if (args[0] instanceof org.json.JSONObject) {
						JSONObject jsonObject = (JSONObject)args[0];
						System.out.println("[gamestatus]json data="+jsonObject.toString());
					}else if(args[0] instanceof java.lang.Object){
						System.out.println("[gamestatus] object="+args[0].getClass().getTypeName());
					}else {
						System.out.println("[callback]other type data,type="+args[0].getClass().getTypeName());
					}
				}
				System.out.println("client receive gamestatus: " + JSON.toJSONString(args));
			}
		});
        
        socket.connect();
        
        Scanner sc = new Scanner(System.in);
        //利用hasNextXXX()判断是否还有下一输入项
        while (sc.hasNext()) {
            //利用nextXXX()方法输出内容
            String str = sc.next();
            System.out.println("scanner send data: "+str+", id: "+socket.id());
            
            JSONObject jsonObject = new JSONObject();
        	jsonObject.put("token", "4187add4ad6a45f2a0276c8ce32e50cf");
        	jsonObject.put("orgi", "beimi");
        	
        	socket.emit("gamestatus", jsonObject.toString());
        }
	}
}