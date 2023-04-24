package com.hotsix.omc.service;

public interface KakaoLoginService {

	void loginWithAccessToken(String token);

	String getAccessToken(String code);

	void logout(String access_token);

	void unlink(String access_token);
}
