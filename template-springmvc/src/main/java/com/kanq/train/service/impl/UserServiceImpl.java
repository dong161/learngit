package com.kanq.train.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.kanq.qd.use.dao.ICoreDao;
import com.kanq.train.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	@Qualifier("coreDao")	
	private ICoreDao coreDao;

	@Override
	public List<Map<String, Object>> get() {
		
		return coreDao.selectList("userMapper.userinfo", null);
		
	}
	@Override
	public int delbyid(String id) {
		
		return coreDao.delete("userMapper.delbyid", id);
		
	}
	
	@Override
	public int updatebyid(String id, String field, String value) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id",id);
		map.put("field",field);
		map.put("value", value);
		return coreDao.update("userMapper.updatebyid",map);
		
	}
	
	
	@Override
	public int add(String id, String name, String code,String password,String altertime,String createtime){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id",id);
		map.put("name",name);
		map.put("code", code);
		map.put("password",password);
		map.put("altertime",altertime);
		map.put("createtime",createtime);
		return coreDao.update("userMapper.add",map);
		
	}
	
	@Override
	public List<Map<String, Object>> getone(String username,String code) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("username",username);
		map.put("code", code);
		return coreDao.selectList("userMapper.userinfo", map);
		
	}
}
