package com.kanq.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.kanq.qd.use.dao.ICoreDao;
import com.kanq.train.service.TestService;

@Service("testService")
public class TestServiceImpl implements TestService {

	@Autowired
	@Qualifier("coreDao")	
	private ICoreDao coreDao;

	@Override
	public String get() {
		return "hello world! LQ";
	}

}
