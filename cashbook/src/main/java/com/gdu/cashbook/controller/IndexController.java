package com.gdu.cashbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 컨트롤러 에노테이션 서블렛기능
@Controller
public class IndexController {
	// 페이지요청
	@GetMapping(value={"/","/index"})
	public String index() {
		return "index";
	}
}