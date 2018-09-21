package com.kanq.train.service;

import java.util.List;
import java.util.Map;

public interface UserService {

	List<Map<String, Object>> get();
	List<Map<String, Object>> getone(String username,String code);
    int delbyid(String id);
	int updatebyid(String id, String field, String value);
	int add(String id, String name, String code, String password, String altertime, String createtime);
}