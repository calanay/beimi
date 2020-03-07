package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.MJCardsType;

public abstract interface GameMJCardsTypeRepository  extends JpaRepository<MJCardsType, String>{
	
  public abstract MJCardsType findByIdAndOrgi(String id, String orgi);
  
  public abstract List<MJCardsType> findByOrgiAndGame(String orgi , String game);
}
