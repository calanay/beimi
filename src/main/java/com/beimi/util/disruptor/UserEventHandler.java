package com.beimi.util.disruptor;

import com.beimi.core.BMDataContext;
import com.beimi.util.event.UserDataEvent;
import com.lmax.disruptor.EventHandler;

public class UserEventHandler implements EventHandler<UserDataEvent>{

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(UserDataEvent arg0, long arg1, boolean arg2)
			throws Exception {
		if(BMDataContext.UserDataEventType.SAVE.toString().equals(arg0.getCommand())){
			if(arg0.getDbRes()!=null){
				arg0.getDbRes().save(arg0.getEvent()) ;
			}
			if(arg0.getEsRes()!=null){
				arg0.getEsRes().save(arg0.getEvent()) ;
			}
		}else if(BMDataContext.UserDataEventType.DELETE.toString().equals(arg0.getCommand())){
			if(arg0.getDbRes()!=null){
				arg0.getDbRes().delete(arg0.getEvent()) ;
			}
			if(arg0.getEsRes()!=null){
				arg0.getEsRes().delete(arg0.getEvent()) ;
			}
		}
	}
}
