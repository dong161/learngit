package com.kanq.train.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kanq.qd.use.entity.ResponseBean;
import com.kanq.train.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserInfoController {
	
	// http://localhost:9999/kanq-template-project-springmvc/page/test/test.html

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@RequestMapping(value = "/selectbyname",method=RequestMethod.POST)
	public ResponseBean<List<Map<String, Object>>> selectbyname(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final String username = request.getParameter("username");
		final String code = request.getParameter("code");
		List<Map<String, Object>> content = userService.getone(username,code);
		return ResponseBean.of(content);
	}
	
	@RequestMapping(value = "/delbyid",method=RequestMethod.POST)
	public ResponseBean<Integer> delbyid(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final String id = request.getParameter("id");
		int content = userService.delbyid(id);
		return ResponseBean.of(content);
	}
	
	@RequestMapping(value = "/updatebyid",method=RequestMethod.POST)
	public ResponseBean<Integer> updatebyid(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final String id = request.getParameter("id");
		final String field = request.getParameter("field");
		final String value = request.getParameter("value");
		int content = userService.updatebyid(id,field,value);
		return ResponseBean.of(content);
	}
	
	
	@RequestMapping(value = "/add",method=RequestMethod.POST)
	public ResponseBean<Integer> add(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final String id = request.getParameter("id");
		final String name = request.getParameter("name");
		final String code = request.getParameter("code");
		final String password = request.getParameter("password");
		final String altertime = request.getParameter("altertime");
		final String createtime = request.getParameter("createtime");

		int content = userService.add(id,name,code,password,altertime,createtime);
		return ResponseBean.of(content);
	}
	
	@RequestMapping(value = "/getall",method=RequestMethod.POST)
	public ResponseBean<List<Map<String, Object>>> getall(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<Map<String, Object>> content = userService.get();
		return ResponseBean.of(content);
	}

}
