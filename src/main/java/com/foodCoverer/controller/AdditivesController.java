package com.foodCoverer.controller;

import com.foodCoverer.model.Additive;
import com.foodCoverer.repository.AdditivesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/additives")
public class AdditivesController {

    @Autowired
    AdditivesRepository additivesRepository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Additive>> getAllAdditives() {

        List<Additive> additives = additivesRepository.findAll();
        Collections.sort(additives, (Additive a1, Additive a2) -> {
            return a1.getSymbol().compareToIgnoreCase(a2.getSymbol());
        });
        return ResponseEntity.ok().body(additives);
    }
}
