package com.foodCoverer.service;

import com.foodCoverer.model.Role;
import com.foodCoverer.repository.UserRepository;
import com.foodCoverer.session.SessionManager;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foodCoverer.model.User;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private VerifyService verifyService;


    private static final String PAYLOAD_NAME = "name";
    private static final String PAYLOAD_PICTURE = "picture";

    public UserService() {

    }

    public Role authenticate(String token) throws GeneralSecurityException, IOException {


        GoogleIdToken googleIdToken = verifyService.verifyUserToken(token);

        if (googleIdToken == null) {
            return Role.NONE;
        }

        Payload payload = googleIdToken.getPayload();
        String userId = payload.getSubject();


        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.getRole().equals(Role.INACTIVE)) {
                sessionManager.startSession(user);
            }
            return user.getRole();
        } else {
            return saveNewUserData(payload);
        }
    }


    private Role saveNewUserData(Payload payload) {

        boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
        if (!emailVerified) {
            return Role.NONE;
        }
        String userId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get(PAYLOAD_NAME);
        String picture = (String) payload.get(PAYLOAD_PICTURE);
        User user = new User(userId, name, email, picture);
        userRepository.saveAndFlush(user);
        sessionManager.startSession(user);
        return user.getRole();

    }

    public void disableAccount(User user) {

        user.setRole(Role.INACTIVE);
        userRepository.saveAndFlush(user);
    }

    public void enableAccount(User user) {

        user.setRole(Role.USER);
        userRepository.saveAndFlush(user);
    }

    public void elevate(User user) {

        user.setRole(Role.ADMIN);
        userRepository.saveAndFlush(user);
    }


}
