package com.beimi.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;

import com.beimi.core.BMDataContext;

public class IPTools {
	private String IP_DATA_PATH = "WEB-INF/data/ip/ip.db";
	private static IPTools iptools = new IPTools();
	private DbSearcher _searcher = null ;
	
	public static IPTools getInstance(){
		return iptools ;
	}

	public IPTools() {
		try {
			File dbFile = new File(BMDataContext.getContext().getEnvironment().getProperty("web.upload-path"), "ipdata/ipdata.db") ;
			if(!dbFile.exists()){
				FileUtils.copyInputStreamToFile(IPTools.class.getClassLoader().getResourceAsStream(IP_DATA_PATH),dbFile);
			}
			_searcher = new DbSearcher(new DbConfig(),dbFile.getAbsolutePath());
		} catch (DbMakerConfigException | IOException e) {
			e.printStackTrace();
		} 
	}
	public IP findGeography(String remote) {
		IP ip = new IP();
		try{
			DataBlock block = _searcher.binarySearch(remote!=null ? remote : "127.0.0.1")  ;
			if(block!=null && block.getRegion() != null){
				String[] region = block.getRegion().split("[\\|]") ;
				if(region.length == 5){
					ip.setCountry(region[0]);
					if(!StringUtils.isBlank(region[1]) && !region[1].equalsIgnoreCase("null")){
						ip.setRegion(region[1]);
					}else{
						ip.setRegion("");
					}
					if(!StringUtils.isBlank(region[2]) && !region[2].equalsIgnoreCase("null")){
						ip.setProvince(region[2]);
					}else{
						ip.setProvince("");
					}
					if(!StringUtils.isBlank(region[3]) && !region[3].equalsIgnoreCase("null")){
						ip.setCity(region[3]);
					}else{
						ip.setCity("");
					}
					if(!StringUtils.isBlank(region[4]) && !region[4].equalsIgnoreCase("null")){
						ip.setIsp(region[4]);
					}else{
						ip.setIsp("");
					}
				}
			}
		}catch(Exception ex){}
		return ip;
	}
}
