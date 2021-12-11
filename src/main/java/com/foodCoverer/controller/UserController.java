package com.foodCoverer.controller;

import com.foodCoverer.model.Role;
import com.foodCoverer.model.User;
import com.foodCoverer.repository.UserRepository;
import com.foodCoverer.service.UserService;
import com.foodCoverer.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"${cors_origin}"}, allowCredentials = "true")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionManager sessionManager;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<User>> allUsers() {

        if(!sessionManager.isAdmin()){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok().body(userRepository.findAll());
    }

    @RequestMapping(value="/disable", method = RequestMethod.PATCH)
    public ResponseEntity<Object> disableAccount(@RequestBody User user) {

        if(!sessionManager.isAdmin()){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userService.disableAccount(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @RequestMapping(value="/enable", method = RequestMethod.PATCH)
    public ResponseEntity<Object> enableAccount(@RequestBody User user) {

        if(!sessionManager.isAdmin()){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userService.enableAccount(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }


    @RequestMapping(value="/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Object> authenticate(@RequestBody String token) throws GeneralSecurityException, IOException {

        Role role = userService.authenticate(token);
        return ResponseEntity.ok().body(role);
    }

    @RequestMapping(value="/logOut", method = RequestMethod.GET)
    public ResponseEntity<Object> logOut(){
        sessionManager.endSession();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/elevate", method = RequestMethod.PATCH)
    public ResponseEntity<Object> elevate(@RequestBody User user) {

        if(!sessionManager.isAdmin()){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userService.elevate(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
