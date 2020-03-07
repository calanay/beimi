package com.beimi.web.service.repository.es;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import com.beimi.web.model.ActRecord;

public abstract interface ActRecordESRepository extends ElasticsearchCrudRepository<ActRecord, String>
{
	public abstract ActRecord findByIdAndOrgi(String id ,String orgi);
	
	public abstract List<ActRecord> findByPlayeridAndOrgi(String playerid ,String orgi);
	
	public abstract int countByPlayeridAndOrgiAndDayAndRectype(String playerid ,String orgi , String day , String rectype);
}
