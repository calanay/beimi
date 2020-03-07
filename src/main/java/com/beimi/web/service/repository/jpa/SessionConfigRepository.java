package com.beimi.web.service.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.GameConfig;

public abstract interface SessionConfigRepository  extends JpaRepository<GameConfig, String>
{
	public abstract GameConfig findByOrgi(String orgi);
}

