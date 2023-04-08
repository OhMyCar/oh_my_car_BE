package com.hotsix.omc.service;

import java.util.HashMap;

public interface KakaoLoginService {

	String getAccessToken(String authorize_code) throws Throwable;

	public HashMap<String, Object> getUserInfo(String access_Token) throws Throwable;

//	public KakaoDto getUserInfo(String access_Token) throws Throwable;
}
