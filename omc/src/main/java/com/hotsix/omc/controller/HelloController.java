package com.hotsix.omc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

	// 서버 열린거 확인용
	@GetMapping
	public String hello() {
		return "hello omc";
	}
}
