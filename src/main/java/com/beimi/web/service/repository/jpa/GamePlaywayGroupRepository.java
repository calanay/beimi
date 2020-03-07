package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.GamePlaywayGroup;

public interface GamePlaywayGroupRepository extends JpaRepository<GamePlaywayGroup, String>{

  public abstract GamePlaywayGroup findByIdAndOrgi(String id, String orgi);

  public abstract int countByNameAndPlaywayidAndOrgi(String name , String playwayid, String orgi);

  public abstract int countByNameAndPlaywayidAndOrgiNotAndId(String name , String playwayid, String orgi , String id);

  public abstract List<GamePlaywayGroup> findByOrgiAndPlaywayid(String orgi, String playwayid , Sort sort);


  public abstract List<GamePlaywayGroup> findByOrgi(String orgi, Sort sort);
}
