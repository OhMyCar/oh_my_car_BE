package com.hotsix.omc.controller;

import com.hotsix.omc.service.KakaoLoginService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class KakaoLoginController {

	@Autowired
	public KakaoLoginService kakaoLoginService;

	// 1번 카카오톡에 사용자 코드 받기(jsp의 a태그 href에 경로 있음)
	// 2번 받은 code를 iKakaoS.getAccessToken로 보냄 ###access_Token###로 찍어서 잘 나오면은 다음단계진행
	// 3번 받은 access_Token를 iKakaoS.getUserInfo로 보냄 userInfo받아옴, userInfo에 nickname, email정보가 담겨있음
	// redirect URL : http://localhost:8080/login/kakao
	@GetMapping (value = "/login/kakao")
	public ModelAndView kakaoLogin(@RequestParam(value = "code", required = false) String code) throws Throwable {

		// 1번
		System.out.println("####code#######:" + code);

		// 2번
		String access_Token = kakaoLoginService.getAccessToken(code);
		System.out.println("###access_Token#### : " + access_Token);
		// 위의 access_Token 받는 걸 확인한 후에 밑에 진행

		// 3번
		HashMap<String, Object> userInfo = kakaoLoginService.getUserInfo(access_Token);
		System.out.println("###nickname#### : " + userInfo.get("nickname"));
		System.out.println("###email#### : " + userInfo.get("email"));
		// 콘솔 출력용 코드는 나중에 삭제 예정
		return null;
	}
}
