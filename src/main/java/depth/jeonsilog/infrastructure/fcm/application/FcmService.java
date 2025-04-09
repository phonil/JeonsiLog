package depth.jeonsilog.infrastructure.fcm.application;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import depth.jeonsilog.domain.devide_token.domain.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;

import static depth.jeonsilog.domain.alarm.constant.AlarmConstant.FcmAlarmConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {

    // API_URL은 메세지 전송을 위해 요청하는 주소이다. {프로젝트 ID}넣기
    private static final String API_URL = "https://fcm.googleapis.com/v1/projects/jeonsilog-fd54e/messages:send";
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

    private final DeviceTokenRepository deviceTokenRepository;

    // TODO: AccessToken 발급 받기. -> Header에 포함하여 푸시 알림 요청
    private static String getAccessToken() throws IOException {
        ClassPathResource resource = new ClassPathResource("services-account.json");
        GoogleCredential googleCredential = GoogleCredential
//                .fromStream(new FileInputStream(resource.getFile()))
                .fromStream(resource.getInputStream())
                .createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshToken();
        log.info("액세스 토큰 발급: " + googleCredential.getAccessToken());
        return googleCredential.getAccessToken();
    }

    // TODO: 활동 알림 만들기
    public void makeActiveAlarm(String deviceToken, String title) {
        JSONObject jsonValue = new JSONObject();
        jsonValue.put(TITLE, title);

        JSONObject jsonData = new JSONObject();
        jsonData.put(TOKEN, deviceToken);
        jsonData.put(DATA, jsonValue);

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(MESSAGE, jsonData);

        pushAlarm(jsonMessage);
    }

    // TODO: 전시 알림 만들기
    public void makeExhibitionAlarm(String deviceToken, String title, String body) {
        JSONObject jsonValue = new JSONObject();
        jsonValue.put(TITLE, title);
        jsonValue.put(BODY, body);

        JSONObject jsonData = new JSONObject();
        jsonData.put(TOKEN, deviceToken);
        jsonData.put(DATA, jsonValue);

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(MESSAGE, jsonData);

        pushAlarm(jsonMessage);
    }

    public void sendSilentCheck(String token) {
        JSONObject jsonData = new JSONObject();
        jsonData.put(TYPE, SILENT_CHECK);

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put(TOKEN, token);
        jsonMessage.put(DATA, jsonData);

        JSONObject message = new JSONObject();
        message.put(MESSAGE, jsonMessage);

        pushAlarm(message);
    }

    // TODO: 만들어진 알림을 받아서 푸시한다.
    private void pushAlarm(JSONObject jsonMessage) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + getAccessToken())
                    .addHeader("Content-Type", "application/json; UTF-8")
                    .url(API_URL)
                    .post(RequestBody.create(jsonMessage.toString(), MediaType.parse("application/json")))
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String body = response.body().string();
            if (!response.isSuccessful() && body.contains(SILENT_PUSH_BODY))
                deleteDeviceToken(jsonMessage.getJSONObject(MESSAGE).getString(TOKEN));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteDeviceToken(String token) {
        deviceTokenRepository.deleteByDeviceToken(token);
    }
}
