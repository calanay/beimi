package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository  extends JpaRepository<Project, String>{
	
  public abstract Project findByIdAndOrgi(String id, String orgi);

  public abstract Page<Project> findByOrgi(String orgi , Pageable paramPageable);
  
  public abstract List<Project> findByOrgiAndCode(String orgi, String code);

  public abstract Page<Project> findByOrgiAndTeamid(String orgi ,String teamid, Pageable paramPageable);

  public abstract List<Project> findByOrgiAndTeamid(String orgi, String teamid);
}
