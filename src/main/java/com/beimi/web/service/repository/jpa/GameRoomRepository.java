package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.GameRoom;

public abstract interface GameRoomRepository  extends JpaRepository<GameRoom, String>{
	
  public abstract GameRoom findByIdAndOrgi(String id, String orgi);
  
  public abstract Page<GameRoom> findByOrgi(String orgi , Pageable page);
  
  public abstract List<GameRoom> findByRoomidAndOrgi(String roomid, String orgi);
}
