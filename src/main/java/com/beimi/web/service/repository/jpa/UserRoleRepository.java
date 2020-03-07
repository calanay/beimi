package com.beimi.web.service.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.beimi.web.model.Role;
import com.beimi.web.model.User;
import com.beimi.web.model.UserRole;

public abstract interface UserRoleRepository  extends JpaRepository<UserRole, String>
{
	
	public abstract Page<UserRole> findByOrgiAndRole(String orgi ,Role role,Pageable paramPageable);
	
	public abstract List<UserRole> findByOrgiAndRole(String orgi ,Role role);
	
	public abstract List<UserRole> findByOrgiAndUser(String orgi ,User user);
}

