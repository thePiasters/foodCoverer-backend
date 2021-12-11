package com.foodCoverer.controller;

import com.foodCoverer.model.Tag;
import com.foodCoverer.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = {"${cors_origin}"}, allowCredentials = "true")
@RequestMapping("/tags")
public class TagsController {

    @Autowired
    TagRepository tagRepository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.ok().body(tagRepository.findAll());
    }
}
