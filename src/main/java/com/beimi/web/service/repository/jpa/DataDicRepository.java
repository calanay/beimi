package com.beimi.web.service.repository.jpa;

import com.beimi.web.model.DataDic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataDicRepository extends JpaRepository<DataDic, String>{

  public abstract DataDic findByIdAndOrgi(String id, String orgi);

  public abstract Page<DataDic> findByOrgi(String orgi, Pageable paramPageable);
  
  public abstract List<DataDic> findByOrgiAndCode(String orgi, String code);

  public List<DataDic> findByOrgiAndProjectid(String orgi , String projectid);
}
