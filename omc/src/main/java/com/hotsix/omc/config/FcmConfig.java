package com.hotsix.omc.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.hotsix.omc.exception.OmcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

import static com.hotsix.omc.exception.ErrorCode.FAILED_FIREBASE_INIT;

@Slf4j
@Configuration
public class FcmConfig {
    @Value("${fcm.key.path}")
    private String fcmKeyPath;

    @Value("${fcm.key.scope}")
    private List<String> fireBaseScope;

    @PostConstruct
    public void init(){
        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(
                            fcmKeyPath).getInputStream()).createScoped(fireBaseScope))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {

                FirebaseApp.initializeApp(options);
            }
        }catch (IOException e){
            log.error(e.getMessage());
            throw new OmcException(FAILED_FIREBASE_INIT);
        }
    }

}
