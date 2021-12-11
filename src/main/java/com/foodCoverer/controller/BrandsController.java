package com.foodCoverer.controller;

import com.foodCoverer.model.Brand;
import com.foodCoverer.service.BrandService;
import com.foodCoverer.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@CrossOrigin(origins = {"${cors_origin}"}, allowCredentials = "true")
public class BrandsController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private SessionManager sessionManager;


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Brand>> getAllBrands() {
        return ResponseEntity.ok().body(brandService.getAllBrandsSorted());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteBrand(@RequestBody Brand brand) {


        if (!brandService.isAuthorizedToModify(brand)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!brandService.checkIfExistsByUUID(brand)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        brandService.deleteBrand(brand);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public ResponseEntity<Object> updateBrand(@RequestBody Brand brand) {
        if (!brandService.isAuthorizedToModify(brand)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!brandService.checkIfExistsByUUID(brand)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if (brandService.checkIfExistsByName(brand)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (brandService.saveBrand(brand)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<Object> addBrand(@RequestBody Brand brand) {

        if (!sessionManager.isLoggedIn()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (brandService.checkIfExistsByName(brand)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (brandService.createNewBrand(brand)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/byUser", method = RequestMethod.GET)
    public ResponseEntity<Object> getBrandsByUser() {
        if (!sessionManager.isLoggedIn()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<Brand> brands = brandService.getBrandsByLoggedInUser();
        return ResponseEntity.ok().body(brands);

    }

    @RequestMapping(value = "/verify", method = RequestMethod.PATCH)
    public ResponseEntity<Object> verifyBrand(@RequestBody Brand brand) {

        if (!sessionManager.isAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!brandService.checkIfExistsByUUID(brand)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        brandService.verifyBrand(brand);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
