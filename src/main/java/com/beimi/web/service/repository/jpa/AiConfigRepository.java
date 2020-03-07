package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.AiConfig;

public abstract interface AiConfigRepository extends JpaRepository<AiConfig, String>
{
  public abstract List<AiConfig> findByOrgi(String orgi);
}
