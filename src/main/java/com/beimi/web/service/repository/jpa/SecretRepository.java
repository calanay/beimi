package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.Secret;

public abstract interface SecretRepository  extends JpaRepository<Secret, String>{
	public abstract List<Secret> findByOrgi(String orgi);
}

