package com.sur.service;

import java.util.HashSet;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface LogProcess {
	
	public HashSet<String> preprocess(CommonsMultipartFile file);

}
