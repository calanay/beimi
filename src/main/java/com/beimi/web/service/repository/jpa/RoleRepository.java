package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.Role;

public abstract interface RoleRepository
  extends JpaRepository<Role, String>
{
  public abstract Role findByIdAndOrgi(String paramString, String orgi);
  
  public abstract List<Role> findByOrgi(String orgi);
  
  public abstract Role findByNameAndOrgi(String paramString, String orgi);
}

