package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.GameConfig;

public abstract interface GameConfigRepository extends JpaRepository<GameConfig, String>
{
  public abstract List<GameConfig> findByOrgi(String orgi);
}
