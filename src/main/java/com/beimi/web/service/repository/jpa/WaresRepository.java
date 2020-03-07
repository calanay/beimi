package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.Wares;

public interface WaresRepository extends JpaRepository<Wares, String>{

  public abstract Wares findByIdAndOrgi(String id, String orgi);

  public abstract Page<Wares> findByOrgi(String orgi, Pageable page);

  public abstract List<Wares> findByOrgi(String orgi);
  
  public abstract List<Wares> findByOrgiAndWarestype(String orgi, String warestype);
  
  
  public abstract Page<Wares> findByOrgiAndWarestype(String orgi, String warestype, Pageable page);
}
