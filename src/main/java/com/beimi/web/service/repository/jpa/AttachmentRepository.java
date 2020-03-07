package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.AttachmentFile;

public abstract interface AttachmentRepository  extends JpaRepository<AttachmentFile, String>{
	
	public abstract AttachmentFile findByIdAndOrgi(String id ,String orgi);
	
	public abstract List<AttachmentFile> findByDataidAndOrgi(String dataid , String orgi);
	
	public abstract List<AttachmentFile> findByModelidAndOrgi(String modelid , String orgi);
	
	public abstract Page<AttachmentFile> findByOrgi(String orgi , Pageable page);
}

