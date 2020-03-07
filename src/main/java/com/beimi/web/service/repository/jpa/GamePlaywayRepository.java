package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.GamePlayway;

public abstract interface GamePlaywayRepository  extends JpaRepository<GamePlayway, String>{
	
  public abstract GamePlayway findByIdAndOrgi(String id, String orgi);
  
  public abstract List<GamePlayway> findByOrgi(String orgi, Sort sort);
  
  public abstract List<GamePlayway> findByOrgiAndTypeid(String orgi , String typeid , Sort sort);
}
