package com.foodCoverer.session;

import com.foodCoverer.model.Role;
import com.foodCoverer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;


@Component
public class SessionManager {

    public static final String LOGGED_IN_KEY = "LOGGED_IN_USER";

    @Autowired
    private HttpSession httpSession;


    public boolean isLoggedIn() {
        return httpSession.getAttribute(LOGGED_IN_KEY) != null;
    }

    public boolean isLoggedIn(User user) {
        if (isLoggedIn()) {
            String id = ((User) httpSession.getAttribute(LOGGED_IN_KEY)).getId();
            return id.equals(user.getId());

        }
        return false;
    }

    public boolean isAdmin() {
        if (!isLoggedIn()) {
            return false;
        }

        User user = (User) httpSession.getAttribute(LOGGED_IN_KEY);
        return user.getRole().equals(Role.ADMIN);
    }

    public User getLoggedInUser() {
        return (User) httpSession.getAttribute(LOGGED_IN_KEY);
    }

    public void startSession(User user) {
        httpSession.setAttribute(LOGGED_IN_KEY, user);
    }

    public void endSession() {
        httpSession.removeAttribute(LOGGED_IN_KEY);
        httpSession.invalidate();
    }

    public boolean isAuthorized(User resourceCreator) {
        if (!isLoggedIn()) {
            return false;
        }
        if (isAdmin()) {
            return true;
        }
        if (resourceCreator != null && resourceCreator.equals(getLoggedInUser())) {
            return true;
        }
        return false;

    }
}
