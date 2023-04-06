package com.hotsix.omc.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotsix.omc.repository.CustomerRepository;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class kakaoLoginServiceImpl implements KakaoLoginService {

	@Override
	public String getAccessToken(String authorize_code) throws Throwable {

			String access_Token = "";
			String refresh_Token = "";
			String reqURL = "https://kauth.kakao.com/oauth/token";

			try {
				URL url = new URL(reqURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				// POST 요청을 위해 기본값이 false인 setDoOutput을 true로

				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				// POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송

				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
				StringBuilder sb = new StringBuilder();
				sb.append("grant_type=authorization_code");

				sb.append("&client_id=e6d52f0037bf56ab1fa512a20c248215"); // REST_API키 본인이 발급받은 key 넣어주기
				sb.append("&redirect_uri=http://localhost:8080/login/kakao"); // REDIRECT_URI 본인이 설정한 주소 넣어주기

				sb.append("&code=" + authorize_code);
				bw.write(sb.toString());
				bw.flush();

				// 결과 코드가 200이라면 성공
				int responseCode = conn.getResponseCode();
				System.out.println("responseCode : " + responseCode);

				// 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				String result = "";

				while ((line = br.readLine()) != null) {
					result += line;
				}
				System.out.println("response body : " + result);

				// jackson objectmapper 객체 생성
				ObjectMapper objectMapper = new ObjectMapper();
				// JSON String -> Map
				Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
				});

				access_Token = jsonMap.get("access_token").toString();
				refresh_Token = jsonMap.get("refresh_token").toString();

				System.out.println("access_token : " + access_Token);
				System.out.println("refresh_token : " + refresh_Token);

				br.close();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return access_Token;
		}

	@SuppressWarnings("unchecked")	// unchecked = 어노테이션을 사용해 해당 경고를 억제하되 어노테이션 적용 범위는 최소화 하자. 그리고, 경고 메시지를 억제한 이유를 주석을 통해 알리자.
	@Override
	public HashMap<String, Object> getUserInfo(String access_Token) throws Throwable {
		// 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
		HashMap<String, Object> userInfo = new HashMap<String, Object>();
		String reqURL = "https://kapi.kakao.com/v2/user/me";

		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			// 요청에 필요한 Header에 포함될 내용
			conn.setRequestProperty("Authorization", "Bearer " + access_Token);

			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line = "";
			String result = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("response body : " + result);
			System.out.println("result type" + result.getClass().getName()); // java.lang.String

			try {
				// jackson objectmapper 객체 생성
				ObjectMapper objectMapper = new ObjectMapper();
				// JSON String -> Map
				Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
				});

				System.out.println(jsonMap.get("properties"));

				Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");
				Map<String, Object> kakao_account = (Map<String, Object>) jsonMap.get("kakao_account");

				String nickname = properties.get("nickname").toString();
				String email = kakao_account.get("email").toString();

				userInfo.put("nickname", nickname);
				userInfo.put("email", email);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userInfo;	// 원래 있던 것
	}

//	@Override
//	public KakaoDto getUserInfo(String access_Token) throws Throwable {
//		HashMap<String, Object> userInfo = new HashMap<String, Object>();
//		String reqURL = "https://kapi.kakao.com/v2/user/me";
//		try {
//			URL url = new URL(reqURL);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("GET");
//			conn.setRequestProperty("Authorization", "Bearer " + access_Token);
//			int responseCode = conn.getResponseCode();
//			System.out.println("responseCode : " + responseCode);
//			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String line = "";
//			String result = "";
//			while ((line = br.readLine()) != null) {
//				result += line;
//			}
//			System.out.println("response body : " + result);
//			JsonParser parser = new JsonParser();
//			JsonElement element = parser.parse(result);
//			JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
//			JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
//			String nickname = properties.getAsJsonObject().get("nickname").getAsString();
//			String email = kakao_account.getAsJsonObject().get("email").getAsString();
//			userInfo.put("nickname", nickname);
//			userInfo.put("email", email);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		// catch 아래 코드 추가.
//		KakaoDto result = CustomerRepository.findkakao(userInfo);
//		// 위 코드는 먼저 정보가 저장되있는지 확인하는 코드.
//		System.out.println("S:" + result);
//		if(result==null) {
//			// result가 null이면 정보가 저장이 안되있는거므로 정보를 저장.
//			mr.kakaoinsert(userInfo);
//			// 위 코드가 정보를 저장하기 위해 Repository로 보내는 코드임.
//			return mr.findkakao(userInfo);
//			// 위 코드는 정보 저장 후 컨트롤러에 정보를 보내는 코드임.
//			//  result를 리턴으로 보내면 null이 리턴되므로 위 코드를 사용.
//		} else {
//			return result;
//			// 정보가 이미 있기 때문에 result를 리턴함.
//		}
//	}

}
