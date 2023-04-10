package com.hotsix.omc.service;

public interface KakaoLoginService {

	void loginWithAccessToken(String token);

	String getAccessToken(String code);
}
