package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.beimi.web.model.GamePlaywayGroupItem;

public interface GamePlaywayGroupItemRepository extends JpaRepository<GamePlaywayGroupItem, String>{

  public abstract GamePlaywayGroupItem findByIdAndOrgi(String id, String orgi);

  @Modifying
  @Query("delete from GamePlaywayGroupItem item where playwayid=?1 AND orgi = ?2")
  public void deleteByPlaywayidAndOrgi(String playwayid, String orgi) ;

  public abstract int countByNameAndPlaywayidAndOrgi(String name, String playwayid, String orgi);

  public abstract int countByNameAndPlaywayidAndOrgiNotAndId(String name, String playwayid, String orgi , String id);

  public abstract List<GamePlaywayGroupItem> findByOrgiAndPlaywayid(String orgi, String playwayid , Sort sort);

  public abstract List<GamePlaywayGroupItem> findByOrgi(String orgi, Sort sort);
}
