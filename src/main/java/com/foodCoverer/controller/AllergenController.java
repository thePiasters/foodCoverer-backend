package com.foodCoverer.controller;

import com.foodCoverer.model.Allergen;
import com.foodCoverer.repository.AllergenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
@RequestMapping("/allergens")
public class AllergenController {

    @Autowired
    HttpSession httpSession;

    @Autowired
    AllergenRepository allergenRepository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Allergen>> getAllAllergens() {

        return ResponseEntity.ok().body(allergenRepository.findAll());
    }
}
