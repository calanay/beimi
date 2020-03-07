package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.Sku;

public interface SkuRepository extends JpaRepository<Sku, String>{

	public abstract Sku findByIdAndOrgi(String id, String orgi);

	public abstract Page<Sku> findByOrgi(String orgi, Pageable page);

	public abstract List<Sku> findByOrgi(String orgi);

	public abstract List<Sku> findByOrgiAndWaresid(String orgi, String waresid);
}
