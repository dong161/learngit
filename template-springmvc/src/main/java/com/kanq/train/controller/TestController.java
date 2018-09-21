package com.kanq.train.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanq.qd.use.entity.ResponseBean;
import com.kanq.train.service.TestService;

@RestController
@RequestMapping(value = "/test")
public class TestController {
	
	// http://localhost:9999/kanq-template-project-springmvc/page/test/test.html
	private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	@Qualifier("testService")
	private TestService testService;

	@RequestMapping(value = "/helloWorld")
	public ResponseBean<String> helloWorld(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final String content = testService.get();
		return ResponseBean.of(content);
	}

}
