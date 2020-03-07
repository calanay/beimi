package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.Template;

public interface TemplateRepository extends JpaRepository<Template, String> {
	
	public Template findByIdAndOrgi(String id , String orgi);
	public List<Template> findByTemplettypeAndOrgi(String templettype , String orgi);
	public List<Template> findByOrgi(String orgi) ;
}
