package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.WaresType;

public interface WaresTypeRepository  extends JpaRepository<WaresType, String>{
	
  public abstract WaresType findByIdAndOrgi(String id, String orgi);
  
  public abstract List<WaresType> findByOrgi(String orgi, String code);
}
