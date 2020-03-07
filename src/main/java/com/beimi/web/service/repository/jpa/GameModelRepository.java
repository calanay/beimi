package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.GameModel;

public abstract interface GameModelRepository  extends JpaRepository<GameModel, String>{
	
  public abstract GameModel findByIdAndOrgi(String id, String orgi);
  
  public abstract List<GameModel> findByOrgiAndGame(String orgi , String game);
}
