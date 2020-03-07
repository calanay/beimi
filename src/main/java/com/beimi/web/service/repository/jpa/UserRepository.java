package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.User;

public abstract interface UserRepository
  extends JpaRepository<User, String>
{
  public abstract User findByIdAndOrgi(String paramString, String orgi);
  
  public abstract User findByUsernameAndOrgi(String paramString, String orgi);
  
  public abstract User findByEmailAndOrgi(String paramString, String orgi);
  
  public abstract User findByUsernameAndPassword(String paramString1, String password);
  
  public abstract Page<User> findByOrgi(String orgi , Pageable paramPageable);
  
  public abstract Page<User> findByDatastatusAndOrgi(boolean datastatus , String orgi, Pageable paramPageable);
  
  public abstract Page<User> findByDatastatusAndOrgiAndUsernameLike(boolean datastatus , String orgi ,String username ,Pageable paramPageable);
  
  public abstract List<User> findByOrganAndOrgi(String paramString, String orgi);
  
  public abstract List<User> findByOrganAndDatastatusAndOrgi(String paramString , boolean datastatus, String orgi);
  
  public abstract List<User> findByOrgiAndDatastatus(String orgi , boolean datastatus);
  
  public abstract Page<User> findByOrgiAndAgent(String orgi , boolean agent , Pageable paramPageable);
  
  public abstract List<User> findByOrgiAndAgent(String orgi , boolean agent);
  
  public abstract long countByOrgiAndAgent(String orgi , boolean agent) ;
}
