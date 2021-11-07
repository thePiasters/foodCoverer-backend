package com.foodCoverer.session;

import com.foodCoverer.model.Role;
import com.foodCoverer.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class SessionManagerTest {

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    SessionManager sessionManager;


    public static final String LOGGED_IN_KEY = "LOGGED_IN_USER";

    @Test
    void testIsAnyUserLoggedIn() {
       when(httpSession.getAttribute(LOGGED_IN_KEY)).thenReturn("Not null test value");
       assertTrue(sessionManager.isLoggedIn());

    }
    @Test
    void testIfGivenUserLoggedIn() {
        String exampleUserId = "457137481953441";
        User mockUser = new User();
        mockUser.setId(exampleUserId);
        when(httpSession.getAttribute(LOGGED_IN_KEY)).thenReturn(mockUser);
        assertTrue(sessionManager.isLoggedIn(mockUser));
    }

    @Test
    void testIfLoggedInUserIsAdmin() {
        String exampleUserId = "457137481953441";
        User mockUser = new User();
        mockUser.setId(exampleUserId);
        mockUser.setRole(Role.ADMIN);

        when(httpSession.getAttribute(LOGGED_IN_KEY)).thenReturn(mockUser);
        assertTrue(sessionManager.isAdmin());
    }

    @Test
    void testGetLoggedInUserIsAdmin() {
        String exampleUserId = "457137481953441";
        User mockUser = new User();
        mockUser.setId(exampleUserId);
        mockUser.setRole(Role.ADMIN);

        when(httpSession.getAttribute(LOGGED_IN_KEY)).thenReturn(mockUser);
        assertEquals(sessionManager.getLoggedInUser(), mockUser);
    }
    @Test
    void testStartSession() {
        User mockUser = new User();
        sessionManager.startSession(mockUser);
        verify(httpSession).setAttribute(LOGGED_IN_KEY, mockUser);

    }
    @Test
    void testEndSession() {
        sessionManager.endSession();
        verify(httpSession).removeAttribute(LOGGED_IN_KEY);
        verify(httpSession).invalidate();
    }
    @Test
    void testIfUserIsAuthorizedShouldREturnTrueForAdmin(){
        String exampleUserId = "457137481953441";
        User mockUser = new User();
        mockUser.setId(exampleUserId);
        mockUser.setRole(Role.ADMIN);

        when(httpSession.getAttribute(LOGGED_IN_KEY)).thenReturn(mockUser);
        assertEquals(sessionManager.isAuthorized(mockUser), true);

    }
    @Test
    void testIfUserIsAuthorizedShouldRetunFalseForNotLoggedInUser(){

        when(httpSession.getAttribute(LOGGED_IN_KEY)).thenReturn(null);
        assertEquals(sessionManager.isAuthorized(new User()), false);

    }
    @Test
    void testIfUserIsAuthorizedShouldRetunTrueForResourceCreator() {
        String exampleUserId = "457137481953441";
        User mockUser = new User();
        mockUser.setId(exampleUserId);
        mockUser.setRole(Role.USER);

        when(httpSession.getAttribute(LOGGED_IN_KEY)).thenReturn(mockUser);
        assertEquals(sessionManager.isAuthorized(mockUser), true);

    }



}