package com.beimi.web.service.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.PlayUserClient;

public abstract interface PlayUserClientRepository  extends JpaRepository<PlayUserClient, String>{
	
  public abstract PlayUserClient findById(String paramString);
  
  public abstract PlayUserClient findByUsername(String username);
  
  public abstract int countByUsername(String username);
  
  public abstract Page<PlayUserClient> findByDatastatus(boolean datastatus , String orgi, Pageable paramPageable);
  
  public abstract Page<PlayUserClient> findByDatastatusAndUsername(boolean datastatus , String orgi ,String username ,Pageable paramPageable);
}
