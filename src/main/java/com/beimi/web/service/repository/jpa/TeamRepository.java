package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, String>{

  public abstract Team findByIdAndOrgi(String id, String orgi);

  public abstract Page<Team> findByOrgi(String orgi, Pageable paramPageable);

  public abstract List<Team> findByOrgi(String orgi);
  
  public abstract List<Team> findByOrgiAndCode(String orgi, String code);
}
