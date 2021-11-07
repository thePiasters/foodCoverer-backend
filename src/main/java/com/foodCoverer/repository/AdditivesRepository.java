package com.foodCoverer.repository;

import com.foodCoverer.model.Additive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdditivesRepository extends JpaRepository<Additive, UUID> {

    @Override
    List<Additive> findAll();
}
