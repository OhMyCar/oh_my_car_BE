package com.hotsix.omc.controller;

import com.hotsix.omc.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

	private final KakaoLoginService kakaoLoginService;

	@GetMapping("/login/{token}")
	public void login(@PathVariable String token) {
		kakaoLoginService.loginWithAccessToken(token);
	}

	@GetMapping("/user/kakao/oauth") // 카카오 어플리케이션에서 설정해준 redirect url이다. code를 가져온 후 access_token 을 리턴
	public String getCode(@RequestParam String code) {
		return kakaoLoginService.getAccessToken(code);
	}

}
