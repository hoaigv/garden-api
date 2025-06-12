package com.example.demo.config.security;

import com.example.demo.authentication.controllers.dtos.GoogleUserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GoogleTokenVerifier {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    public GoogleUserInfo verify(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                    .Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idTokenObj = verifier.verify(idToken);

            if (idTokenObj != null) {
                GoogleIdToken.Payload payload = idTokenObj.getPayload();
                return new GoogleUserInfo(
                        payload.getSubject(),
                        (String) payload.get("name"),
                        (String) payload.get("email"),
                        (String) payload.get("picture")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid ID token");
        }
        throw new RuntimeException("ID token verification failed");
    }
}

